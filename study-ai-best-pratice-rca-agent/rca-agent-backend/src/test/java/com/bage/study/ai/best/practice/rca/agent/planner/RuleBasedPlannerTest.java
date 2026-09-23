package com.bage.study.ai.best.practice.rca.agent.planner;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.SymptomPattern;
import com.bage.study.ai.best.practice.rca.agent.model.TargetType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RuleBasedPlannerTest {

    private final RuleBasedPlanner planner = new RuleBasedPlanner();

    @Test
    void shouldClassifyMqLagAsPrimary() {
        PlanResult result = planner.plan(request("订单状态更新延迟，MQ 消息堆积，消费者 lag 持续增长"));

        Hypothesis primary = result.hypotheses().get(0);
        assertThat(primary.getTargetType()).isEqualTo(TargetType.MQ);
        assertThat(primary.getSymptom()).isEqualTo(SymptomPattern.CONSUMER_LAG);
        assertThat(primary.getTargetName()).isEqualTo("order-service-mq");
    }

    @Test
    void shouldClassifyDbSlowQueryAsPrimary() {
        PlanResult result = planner.plan(request("下单接口大量超时，P99 延迟飙升，疑似慢查询，SQL 执行变慢"));

        Hypothesis primary = result.hypotheses().get(0);
        assertThat(primary.getTargetType()).isEqualTo(TargetType.DB);
        assertThat(primary.getSymptom()).isEqualTo(SymptomPattern.HIGH_LATENCY);
        assertThat(primary.getTargetName()).isEqualTo("order-service-db");
    }

    @Test
    void shouldClassifySupplierFailureAsPrimary() {
        PlanResult result = planner.plan(request("支付环节调用供应商接口大量失败，外部依赖不可用"));

        Hypothesis primary = result.hypotheses().get(0);
        assertThat(primary.getTargetType()).isEqualTo(TargetType.SUPPLIER);
        assertThat(primary.getSymptom()).isEqualTo(SymptomPattern.DEPENDENCY_FAILURE);
        assertThat(primary.getTargetName()).isEqualTo("supplier-api");
    }

    @Test
    void shouldClassifyAppResourceAsPrimary() {
        PlanResult result = planner.plan(request("订单中心 CPU 超过 90%，线程池打满，频繁 Full GC"));

        Hypothesis primary = result.hypotheses().get(0);
        assertThat(primary.getTargetType()).isEqualTo(TargetType.APP);
        assertThat(primary.getSymptom()).isEqualTo(SymptomPattern.RESOURCE_EXHAUSTED);
    }

    @Test
    void shouldProduceStableCompetingHypotheses() {
        PlanResult result = planner.plan(request("MQ 消息堆积"));

        List<Hypothesis> hypotheses = result.hypotheses();
        assertThat(hypotheses).hasSizeGreaterThanOrEqualTo(3);
        assertThat(hypotheses).extracting(Hypothesis::getId)
                .containsExactlyElementsOf(hypotheses.stream().map(h -> "h" + (hypotheses.indexOf(h) + 1)).toList());
        // 主假设先验最高
        assertThat(hypotheses.get(0).getPriorConfidence())
                .isGreaterThan(hypotheses.get(1).getPriorConfidence());
        // 竞争假设与主假设不同 type:symptom
        assertThat(hypotheses).filteredOn(h -> h != hypotheses.get(0))
                .allSatisfy(h -> assertThat(h.playbookKey())
                        .isNotEqualTo(hypotheses.get(0).playbookKey()));
    }

    private RcaRequest request(String desc) {
        RcaRequest request = new RcaRequest();
        request.setAppId("order-service");
        request.setAlarmDescription(desc);
        return request;
    }
}
