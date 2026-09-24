package com.bage.study.ai.best.practice.rca.agent.recording;

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
import com.bage.study.ai.best.practice.rca.agent.planner.LlmPlanner;
import com.bage.study.ai.best.practice.rca.agent.planner.RuleBasedPlanner;
import com.bage.study.ai.best.practice.rca.agent.playbook.ScanPlaybook;
import com.bage.study.ai.best.practice.rca.agent.scan.BroadScanService;
import com.bage.study.ai.best.practice.rca.agent.scan.FocusedScanService;
import com.bage.study.ai.best.practice.rca.agent.scan.TimelineService;
import com.bage.study.ai.best.practice.rca.agent.service.RcaOrchestrator;
import com.bage.study.ai.best.practice.rca.agent.tool.MockToolCatalog;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import com.bage.study.ai.best.practice.rca.agent.topology.TopologyService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 录制与回放端到端测试：record=true 录制后回放，断言结果与原始一致。
 */
class RecordReplayTest {

    private RcaOrchestrator buildOrchestrator(RecordingService recordingService) {
        // 使用 RecordingMcpTool 包装，确保录制/回放生效
        List<com.bage.study.ai.best.practice.rca.agent.tool.McpTool> wrapped = MockToolCatalog.allTools().stream()
                .map(RecordingMcpTool::new)
                .map(t -> (com.bage.study.ai.best.practice.rca.agent.tool.McpTool) t)
                .toList();
        ToolRegistry registry = new ToolRegistry(wrapped);
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
                timelineService, globalReact, recordingService);
        ReflectionTestUtils.setField(orchestrator, "focusedTopN", 2);
        return orchestrator;
    }

    @Test
    void recordedAnalysisShouldReplayIdentically() {
        RecordingService service = new RecordingService();
        RcaOrchestrator orchestrator = buildOrchestrator(service);

        RcaRequest request = request(MockToolCatalog.SCENE_MQ, "MQ 消息堆积，lag 增长");
        request.setRecord(true);

        RcaResponse original = orchestrator.analyze(request);

        // 历史中应存在该记录且含录制
        AnalysisRecord record = service.get(original.getAnalysisId());
        assertThat(record).isNotNull();
        assertThat(record.isRecorded()).isTrue();
        assertThat(record.getRecording()).isNotNull();
        assertThat(record.getRecording().getInteractions()).isNotEmpty();
        // 录制中应包含 TOOL 交互
        assertThat(record.getRecording().getInteractions())
                .anyMatch(i -> i.getType() == InteractionType.TOOL);

        // 回放
        RcaRequest replayReq = request(MockToolCatalog.SCENE_MQ, "MQ 消息堆积，lag 增长");
        replayReq.setRecord(false);
        RcaResponse replayed = orchestrator.analyze(replayReq, record.getRecording());

        // 结论与置信度必须完全一致
        assertThat(replayed.getResultLevel()).isEqualTo(original.getResultLevel());
        assertThat(replayed.getConclusions()).hasSameSizeAs(original.getConclusions());
        for (int i = 0; i < original.getConclusions().size(); i++) {
            Conclusion oc = original.getConclusions().get(i);
            Conclusion rc = replayed.getConclusions().get(i);
            assertThat(rc.hypothesisId()).isEqualTo(oc.hypothesisId());
            assertThat(rc.confidence()).isEqualTo(oc.confidence());
            assertThat(rc.targetType()).isEqualTo(oc.targetType());
            assertThat(rc.symptom()).isEqualTo(oc.symptom());
        }

        // 证据数量一致
        assertThat(replayed.getEvidences()).hasSameSizeAs(original.getEvidences());
        // 假设数量与置信度一致
        assertThat(replayed.getHypotheses()).hasSameSizeAs(original.getHypotheses());
        for (int i = 0; i < original.getHypotheses().size(); i++) {
            assertThat(replayed.getHypotheses().get(i).getConfidence())
                    .isEqualTo(original.getHypotheses().get(i).getConfidence());
        }
    }

    @Test
    void nonRecordedAnalysisShouldHaveNoRecording() {
        RecordingService service = new RecordingService();
        RcaOrchestrator orchestrator = buildOrchestrator(service);

        RcaRequest request = request("default", "下单模块失败率略有升高");
        request.setRecord(false);

        RcaResponse response = orchestrator.analyze(request);
        AnalysisRecord record = service.get(response.getAnalysisId());
        assertThat(record).isNotNull();
        assertThat(record.isRecorded()).isFalse();
        assertThat(record.getRecording()).isNull();
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
