package com.bage.study.ai.best.practice.rca.agent.playbook;

import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.SymptomPattern;
import com.bage.study.ai.best.practice.rca.agent.model.TargetType;
import com.bage.study.ai.best.practice.rca.agent.tool.MockToolCatalog;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;

class ScanPlaybookTest {

    private final ToolRegistry registry = new ToolRegistry(MockToolCatalog.allTools());
    private final ScanPlaybook playbook = new ScanPlaybook(registry);

    @Test
    void validateShouldPassWhenAllReferencedToolsRegistered() {
        assertThatCode(playbook::validate).doesNotThrowAnyException();
    }

    @Test
    void broadToolsShouldPreferExactSymptomMatch() {
        Hypothesis appLatency = new Hypothesis("h1", "app latency",
                TargetType.APP, "order-service", SymptomPattern.HIGH_LATENCY, 0.6);

        // 精确命中 APP:HIGH_LATENCY，包含精准工具，而非 APP 通用集
        assertThat(playbook.broadTools(appLatency))
                .contains("app_p99_latency", "db_slow_query", "redis_latency", "trace_p99");
    }

    @Test
    void broadToolsShouldFallbackToTargetTypeGenericSet() {
        Hypothesis redisLag = new Hypothesis("h1", "redis weird",
                TargetType.REDIS, "order-service-redis", SymptomPattern.CONSUMER_LAG, 0.3);

        assertThat(playbook.broadTools(redisLag))
                .containsExactly("redis_hit_rate", "redis_eviction", "redis_memory", "redis_latency");
    }

    @Test
    void focusedToolsShouldExcludeAlreadyCollected() {
        Hypothesis dbLatency = new Hypothesis("h1", "db slow",
                TargetType.DB, "order-service-db", SymptomPattern.HIGH_LATENCY, 0.6);

        Set<String> already = Set.of("db_top_sql");
        assertThat(playbook.focusedTools(dbLatency, already))
                .doesNotContain("db_top_sql")
                .contains("trace_span_detail", "log_error_pattern");
    }

    @Test
    void decisiveToolsShouldCoverEachSymptom() {
        for (SymptomPattern symptom : SymptomPattern.values()) {
            assertThat(playbook.decisiveTools(symptom))
                    .as("decisive tools for %s", symptom)
                    .isNotEmpty();
        }
        assertThat(playbook.decisiveTools(SymptomPattern.CONSUMER_LAG))
                .contains("mq_consumer_lag");
    }
}
