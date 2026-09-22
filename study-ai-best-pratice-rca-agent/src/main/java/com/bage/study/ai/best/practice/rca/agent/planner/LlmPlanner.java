package com.bage.study.ai.best.practice.rca.agent.planner;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.llm.LlmGateway;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.SymptomPattern;
import com.bage.study.ai.best.practice.rca.agent.model.TargetType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Plan 阶段 LLM 实现：按 rca.md 要求让 LLM 结构化输出
 * targetType + symptom，Playbook 据此做确定性工具路由。
 *
 * 安全约束：
 * 1. 枚举/数量/JSON 任一不合法 → 整体回退规则 Plan；
 * 2. targetName 不采信 LLM 自由文本，强制按 targetType 归一到拓扑命名；
 * 3. 先验置信度裁剪到 [0.1, 0.9]。
 */
@Component
public class LlmPlanner {

    private static final Logger log = LoggerFactory.getLogger(LlmPlanner.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LlmGateway gateway;
    private final RuleBasedPlanner fallback;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LlmPlanner(LlmGateway gateway, RuleBasedPlanner fallback) {
        this.gateway = gateway;
        this.fallback = fallback;
    }

    public PlanResult plan(RcaRequest request) {
        boolean wantLlm = Boolean.TRUE.equals(request.getEnableLlm());
        if (!wantLlm || !gateway.isEnabled()) {
            return fallback.plan(request);
        }
        try {
            String prompt = buildPrompt(request);
            String raw = gateway.complete(prompt);
            if (raw == null || raw.isBlank()) {
                return fallback.plan(request);
            }
            List<Hypothesis> hypotheses = parse(raw, request.getAppId());
            if (hypotheses.isEmpty()) {
                log.warn("LLM Plan produced no valid hypothesis, fallback to rule planner");
                return fallback.plan(request);
            }
            return new PlanResult(hypotheses, true, "LLM Plan：结构化输出 targetType+symptom，已通过枚举校验");
        } catch (Exception e) {
            log.warn("LLM Plan failed, fallback to rule planner: {}", e.getMessage());
            return fallback.plan(request);
        }
    }

    private String buildPrompt(RcaRequest request) {
        String time = request.getAlarmTime() != null ? request.getAlarmTime().format(FMT) : "当前时间";
        return """
                你是 SRE 根因分析专家。请根据告警生成 3-5 个相互竞争的根因假设。

                告警应用：%s
                告警描述：%s
                告警时间：%s

                每个假设必须包含以下字段：
                - id: 唯一标识，如 h1
                - desc: 假设描述（一句话说清因果机制）
                - targetType: 枚举值之一 [APP, DB, REDIS, MQ, SUPPLIER, INFRA]
                - targetName: 具体实例名（APP 用应用名，DB 用 应用名-db，REDIS 用 应用名-redis，MQ 用 应用名-mq，外部依赖用 supplier-api）
                - symptom: 枚举值之一 [HIGH_LATENCY, HIGH_ERROR_RATE, RESOURCE_EXHAUSTED, CONSUMER_LAG, DEPENDENCY_FAILURE]
                - confidence: 初始置信度 0-1

                只输出 JSON 数组，不要输出任何解释文字或 Markdown 代码块。
                """.formatted(request.getAppId(), request.getAlarmDescription(), time);
    }

    private List<Hypothesis> parse(String raw, String appId) throws Exception {
        String json = stripCodeFence(raw);
        JsonNode array = objectMapper.readTree(json);
        if (!array.isArray()) {
            throw new IllegalArgumentException("LLM Plan output is not a JSON array");
        }
        List<Hypothesis> result = new ArrayList<>();
        for (int i = 0; i < array.size() && result.size() < 5; i++) {
            JsonNode node = array.get(i);
            TargetType type = TargetType.valueOf(node.path("targetType").asText().trim().toUpperCase());
            SymptomPattern symptom = SymptomPattern.valueOf(node.path("symptom").asText().trim().toUpperCase());
            String desc = node.path("desc").asText("").trim();
            if (desc.isEmpty()) {
                continue;
            }
            double confidence = clamp(node.path("confidence").asDouble(0.3), 0.1, 0.9);
            String target = RuleBasedPlanner.targetName(type, appId);
            result.add(new Hypothesis("h" + (result.size() + 1), desc, type, target, symptom, confidence));
        }
        return result;
    }

    private static String stripCodeFence(String raw) {
        String s = raw.trim();
        if (s.startsWith("```")) {
            int firstNewline = s.indexOf('\n');
            if (firstNewline > 0) {
                s = s.substring(firstNewline + 1);
            }
            if (s.endsWith("```")) {
                s = s.substring(0, s.length() - 3);
            }
        }
        return s.trim();
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
