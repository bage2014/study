package com.bage.study.ai.best.practice.rca.agent.playbook;

import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.SymptomPattern;
import com.bage.study.ai.best.practice.rca.agent.model.TargetType;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据采集 Playbook（rca.md 方案 C：症状驱动，推荐方案）。
 *
 * 方案 A：targetType → 通用工具集（SCAN_PLAYBOOK，兜底）
 * 方案 C：targetType:symptom → 精准工具集（SYMPTOM_PLAYBOOK，优先精确匹配）
 *
 * LLM 负责语义分类（targetType + symptom），规则负责确定性工具路由，两者各司其职。
 */
@Component
public class ScanPlaybook {

    private static final Logger log = LoggerFactory.getLogger(ScanPlaybook.class);

    /** 方案 A：target 类型 → 通用扫描工具集 */
    private static final Map<TargetType, List<String>> SCAN_PLAYBOOK = Map.of(
            TargetType.APP, List.of("app_error_rate", "app_p99_latency", "app_gc_log", "app_thread_pool"),
            TargetType.DB, List.of("db_slow_query", "db_connections", "db_lock_waits", "db_cpu"),
            TargetType.REDIS, List.of("redis_hit_rate", "redis_eviction", "redis_memory", "redis_latency"),
            TargetType.MQ, List.of("mq_consumer_lag", "mq_produce_rate", "mq_consume_rate", "mq_dead_letter"),
            TargetType.SUPPLIER, List.of("http_success_rate", "http_p99_latency", "circuit_breaker_status"),
            TargetType.INFRA, List.of("infra_cpu", "infra_memory", "infra_disk_io", "container_restart")
    );

    /** 方案 C：targetType:SymptomPattern → 精准工具集（优先精确匹配） */
    private static final Map<String, List<String>> SYMPTOM_PLAYBOOK = Map.ofEntries(
            Map.entry("APP:HIGH_LATENCY",
                    List.of("app_p99_latency", "db_slow_query", "redis_latency", "trace_p99")),
            Map.entry("APP:HIGH_ERROR_RATE",
                    List.of("app_error_rate", "app_error_log", "downstream_success_rate")),
            Map.entry("APP:RESOURCE_EXHAUSTED",
                    List.of("app_thread_pool", "app_gc_log", "app_heap_usage", "db_connections")),
            Map.entry("DB:HIGH_LATENCY",
                    List.of("db_slow_query", "db_lock_waits", "db_cpu", "db_connections")),
            Map.entry("MQ:CONSUMER_LAG",
                    List.of("mq_consumer_lag", "mq_produce_rate", "mq_consume_status", "app_gc_log")),
            Map.entry("SUPPLIER:DEPENDENCY_FAILURE",
                    List.of("http_success_rate", "http_p99_latency", "circuit_breaker_status", "dns_resolve"))
    );

    /** FocusedScan：topN 高置信假设的递归下钻工具集 */
    private static final Map<String, List<String>> FOCUSED_PLAYBOOK = Map.ofEntries(
            Map.entry("APP:HIGH_LATENCY",
                    List.of("trace_span_detail", "log_error_pattern", "db_top_sql")),
            Map.entry("APP:HIGH_ERROR_RATE",
                    List.of("log_error_pattern", "trace_span_detail", "http_error_breakdown")),
            Map.entry("APP:RESOURCE_EXHAUSTED",
                    List.of("log_error_pattern", "trace_span_detail")),
            Map.entry("DB:HIGH_LATENCY",
                    List.of("db_top_sql", "trace_span_detail", "log_error_pattern")),
            Map.entry("MQ:CONSUMER_LAG",
                    List.of("consumer_group_detail", "mq_dead_letter", "log_error_pattern")),
            Map.entry("SUPPLIER:DEPENDENCY_FAILURE",
                    List.of("http_error_breakdown", "trace_span_detail", "dns_resolve"))
    );

    /** 按 targetType 的下钻兜底 */
    private static final Map<TargetType, List<String>> FOCUSED_FALLBACK = Map.of(
            TargetType.APP, List.of("log_error_pattern", "trace_span_detail"),
            TargetType.DB, List.of("db_top_sql", "trace_span_detail"),
            TargetType.REDIS, List.of("trace_span_detail", "log_error_pattern"),
            TargetType.MQ, List.of("consumer_group_detail", "mq_dead_letter"),
            TargetType.SUPPLIER, List.of("http_error_breakdown", "trace_span_detail"),
            TargetType.INFRA, List.of("log_error_pattern")
    );

    /**
     * 决定性信号集：某症状下"一锤定音"的工具。
     * 异常 → SUPPORT；正常 → REFUTE（弱权重，相当于反事实验证"应看到却没看到"）。
     */
    private static final Map<SymptomPattern, Set<String>> DECISIVE_TOOLS = Map.of(
            SymptomPattern.HIGH_LATENCY,
            Set.of("app_p99_latency", "db_slow_query", "db_lock_waits", "db_top_sql",
                    "redis_latency", "http_p99_latency", "trace_p99", "trace_span_detail"),
            SymptomPattern.HIGH_ERROR_RATE,
            Set.of("app_error_rate", "app_error_log", "log_error_pattern",
                    "http_success_rate", "downstream_success_rate", "http_error_breakdown"),
            SymptomPattern.RESOURCE_EXHAUSTED,
            Set.of("app_thread_pool", "app_gc_log", "app_heap_usage",
                    "db_connections", "redis_memory", "infra_cpu", "infra_memory",
                    "infra_disk_io", "container_restart"),
            SymptomPattern.CONSUMER_LAG,
            Set.of("mq_consumer_lag", "mq_consume_status", "mq_consume_rate",
                    "mq_dead_letter", "consumer_group_detail"),
            SymptomPattern.DEPENDENCY_FAILURE,
            Set.of("http_success_rate", "circuit_breaker_status", "dns_resolve",
                    "downstream_success_rate", "http_error_breakdown")
    );

    private final ToolRegistry registry;

    public ScanPlaybook(ToolRegistry registry) {
        this.registry = registry;
    }

    /** BroadScan 工具路由：精确匹配 targetType:symptom，未命中回退 targetType 通用集 */
    public List<String> broadTools(Hypothesis hypothesis) {
        List<String> exact = SYMPTOM_PLAYBOOK.get(hypothesis.playbookKey());
        return exact != null ? exact : SCAN_PLAYBOOK.get(hypothesis.getTargetType());
    }

    /** FocusedScan 工具路由，排除 BroadScan 已采集的工具避免重复取数 */
    public List<String> focusedTools(Hypothesis hypothesis, Set<String> alreadyCollected) {
        List<String> candidates = FOCUSED_PLAYBOOK.getOrDefault(
                hypothesis.playbookKey(), FOCUSED_FALLBACK.get(hypothesis.getTargetType()));
        return candidates.stream()
                .filter(t -> !alreadyCollected.contains(t))
                .toList();
    }

    public Set<String> decisiveTools(SymptomPattern symptom) {
        return DECISIVE_TOOLS.getOrDefault(symptom, Set.of());
    }

    public Map<String, List<String>> symptomPlaybookView() {
        return new LinkedHashMap<>(SYMPTOM_PLAYBOOK);
    }

    public Map<TargetType, List<String>> scanPlaybookView() {
        return new LinkedHashMap<>(SCAN_PLAYBOOK);
    }

    /**
     * 启动时一致性校验：Playbook 引用的每个工具必须存在于注册表，
     * 防止"Prompt/规则里写了工具名，运行时 tool-not-found"。
     */
    @PostConstruct
    public void validate() {
        List<Map<String, List<String>>> tables = List.of(
                new LinkedHashMap<>(SYMPTOM_PLAYBOOK),
                new LinkedHashMap<>(FOCUSED_PLAYBOOK)
        );
        for (List<String> tools : SCAN_PLAYBOOK.values()) {
            check(tools);
        }
        for (Map<String, List<String>> table : tables) {
            for (List<String> tools : table.values()) {
                check(tools);
            }
        }
        for (List<String> tools : FOCUSED_FALLBACK.values()) {
            check(tools);
        }
        log.info("ScanPlaybook validated: all referenced tools exist in ToolRegistry");
    }

    private void check(List<String> toolNames) {
        for (String name : toolNames) {
            if (!registry.exists(name)) {
                throw new IllegalStateException(
                        "Playbook references tool not registered: " + name);
            }
        }
    }
}
