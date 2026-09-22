package com.bage.study.ai.best.practice.rca.agent.service;

import com.bage.study.ai.best.practice.rca.agent.analyze.BroadAnalyzeService;
import com.bage.study.ai.best.practice.rca.agent.analyze.EvidenceEvaluator;
import com.bage.study.ai.best.practice.rca.agent.analyze.FocusedAnalyzeService;
import com.bage.study.ai.best.practice.rca.agent.analyze.GlobalReactService;
import com.bage.study.ai.best.practice.rca.agent.analyze.HypothesisScorer;
import com.bage.study.ai.best.practice.rca.agent.analyze.SuggestionService;
import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.dto.RcaResponse;
import com.bage.study.ai.best.practice.rca.agent.llm.LlmGateway;
import com.bage.study.ai.best.practice.rca.agent.model.Conclusion;
import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.SymptomPattern;
import com.bage.study.ai.best.practice.rca.agent.model.TargetType;
import com.bage.study.ai.best.practice.rca.agent.model.TimelineEvent;
import com.bage.study.ai.best.practice.rca.agent.model.Verdict;
import com.bage.study.ai.best.practice.rca.agent.planner.LlmPlanner;
import com.bage.study.ai.best.practice.rca.agent.planner.RuleBasedPlanner;
import com.bage.study.ai.best.practice.rca.agent.playbook.ScanPlaybook;
import com.bage.study.ai.best.practice.rca.agent.scan.BroadScanService;
import com.bage.study.ai.best.practice.rca.agent.scan.FocusedScanService;
import com.bage.study.ai.best.practice.rca.agent.scan.TimelineService;
import com.bage.study.ai.best.practice.rca.agent.tool.MockToolCatalog;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import com.bage.study.ai.best.practice.rca.agent.topology.TopologyService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 确定性管道端到端测试：手工装配全部组件，不启动 Spring、不调用 LLM。
 */
class RcaOrchestratorTest {

    private RcaOrchestrator buildOrchestrator() {
        ToolRegistry registry = new ToolRegistry(MockToolCatalog.allTools());
        ScanPlaybook playbook = new ScanPlaybook(registry);
        playbook.validate();

        RuleBasedPlanner rulePlanner = new RuleBasedPlanner();
        LlmGateway gateway = new LlmGateway(null, false);
        LlmPlanner planner = new LlmPlanner(gateway, rulePlanner);

        EvidenceEvaluator evaluator = new EvidenceEvaluator(playbook);
        HypothesisScorer scorer = new HypothesisScorer();
        BroadScanService broadScan = new BroadScanService(registry, playbook);
        BroadAnalyzeService broadAnalyze = new BroadAnalyzeService(evaluator, scorer, gateway);
        FocusedScanService focusedScan = new FocusedScanService(registry, playbook);
        FocusedAnalyzeService focusedAnalyze = new FocusedAnalyzeService(evaluator, scorer);
        TimelineService timelineService = new TimelineService();
        TopologyService topologyService = new TopologyService();
        SuggestionService suggestionService = new SuggestionService();

        GlobalReactService globalReact = new GlobalReactService(
                playbook, timelineService, topologyService, suggestionService);
        ReflectionTestUtils.setField(globalReact, "changeWindowMinutes", 30);
        ReflectionTestUtils.setField(globalReact, "topologyHops", 2);

        RcaOrchestrator orchestrator = new RcaOrchestrator(
                planner, broadScan, broadAnalyze, focusedScan, focusedAnalyze,
                timelineService, globalReact);
        ReflectionTestUtils.setField(orchestrator, "focusedTopN", 2);
        return orchestrator;
    }

    @Test
    void mqLagSceneShouldPinMqAsTop1Confirmed() {
        RcaResponse response = buildOrchestrator().analyze(
                request(MockToolCatalog.SCENE_MQ, "订单状态更新延迟，MQ 消息堆积，消费者 lag 持续增长"));

        Conclusion top1 = response.getConclusions().get(0);
        assertThat(top1.targetType()).isEqualTo(TargetType.MQ);
        assertThat(top1.symptom()).isEqualTo(SymptomPattern.CONSUMER_LAG);
        assertThat(top1.confidence()).isGreaterThanOrEqualTo(0.6);
        assertThat(response.getResultLevel()).isEqualTo("CONFIRMED");

        // MQ 假设置信度必须显著高于其它竞争假设
        assertThat(top1.confidence())
                .isGreaterThan(response.getConclusions().get(1).confidence());

        // 时间线必须包含告警前的变更事件
        assertThat(response.getTimeline())
                .anyMatch(e -> e.kind() == TimelineEvent.Kind.CHANGE
                        && "order-service-mq".equals(e.relatedTarget()));
    }

    @Test
    void dbSlowQuerySceneShouldPinDbAsTop1Confirmed() {
        RcaResponse response = buildOrchestrator().analyze(
                request(MockToolCatalog.SCENE_DB, "下单接口大量超时，P99 延迟飙升，疑似慢查询"));

        Conclusion top1 = response.getConclusions().get(0);
        assertThat(top1.targetType()).isEqualTo(TargetType.DB);
        assertThat(top1.symptom()).isEqualTo(SymptomPattern.HIGH_LATENCY);
        assertThat(top1.confidence()).isGreaterThanOrEqualTo(0.6);
        assertThat(response.getResultLevel()).isEqualTo("CONFIRMED");

        // top1 因果链应串起 变更 → 信号 → 告警
        assertThat(String.join("\n", top1.causalChain()))
                .contains("[变更]", "[信号]", "[告警]");
    }

    @Test
    void defaultSceneShouldBeInconclusiveWithoutAnomaly() {
        RcaResponse response = buildOrchestrator().analyze(request("default", "下单模块失败率略有升高"));

        assertThat(response.getResultLevel()).isEqualTo("INCONCLUSIVE");
        assertThat(response.getSuggestions().mitigation()).isNotEmpty();
    }

    @Test
    void allStagesAndEvidenceShouldBeTraceable() {
        RcaResponse response = buildOrchestrator().analyze(
                request(MockToolCatalog.SCENE_MQ, "MQ 消息堆积，lag 增长"));

        assertThat(response.getStages()).hasSize(6);
        assertThat(response.getStages())
                .extracting("stage")
                .containsExactly("Plan", "BroadScan", "BroadAnalyze",
                        "FocusedScan", "FocusedAnalyze", "GlobalReact");
        assertThat(response.isLlmUsed()).isFalse();

        // 每条证据都有 id 且裁决合法
        Set<String> evidenceIds = response.getEvidences().stream()
                .peek(e -> assertThat(e.getId()).isNotBlank())
                .map(Evidence::getId)
                .collect(Collectors.toSet());

        // 结论锚定的 evidenceId 必须真实存在，禁止裸结论
        for (Conclusion c : response.getConclusions()) {
            assertThat(evidenceIds).containsAll(c.supportEvidenceIds());
            assertThat(evidenceIds).containsAll(c.refuteEvidenceIds());
        }
        // 至少存在一条 SUPPORT 证据
        assertThat(response.getEvidences())
                .anyMatch(e -> e.getVerdict() == Verdict.SUPPORT);
    }

    private RcaRequest request(String scene, String desc) {
        RcaRequest request = new RcaRequest();
        request.setAppId("order-service");
        request.setScene(scene);
        request.setAlarmDescription(desc);
        request.setAlarmTime(LocalDateTime.now());
        request.setEnableLlm(false);
        return request;
    }
}
