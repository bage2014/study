package com.bage.study.ai.best.practice.rca.agent.service;

import com.bage.study.ai.best.practice.rca.agent.analyze.BroadAnalyzeService;
import com.bage.study.ai.best.practice.rca.agent.analyze.FocusedAnalyzeService;
import com.bage.study.ai.best.practice.rca.agent.analyze.GlobalReactService;
import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.dto.RcaResponse;
import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.StageTrace;
import com.bage.study.ai.best.practice.rca.agent.planner.LlmPlanner;
import com.bage.study.ai.best.practice.rca.agent.planner.PlanResult;
import com.bage.study.ai.best.practice.rca.agent.recording.AnalysisRecord;
import com.bage.study.ai.best.practice.rca.agent.recording.Recording;
import com.bage.study.ai.best.practice.rca.agent.recording.RecordingContext;
import com.bage.study.ai.best.practice.rca.agent.recording.RecordingService;
import com.bage.study.ai.best.practice.rca.agent.recording.RecordingSession;
import com.bage.study.ai.best.practice.rca.agent.scan.BroadScanService;
import com.bage.study.ai.best.practice.rca.agent.scan.FocusedScanService;
import com.bage.study.ai.best.practice.rca.agent.scan.ScanBundle;
import com.bage.study.ai.best.practice.rca.agent.scan.TimelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * RCA 主编排：Plan → BroadScan → BroadAnalyze → FocusedScan → FocusedAnalyze → GlobalReact。
 * 裁决全部由确定性程序完成，LLM 仅可出现在 Plan 与证据解读两处，失败软降级。
 */
@Service
public class RcaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(RcaOrchestrator.class);

    private final LlmPlanner planner;
    private final BroadScanService broadScanService;
    private final BroadAnalyzeService broadAnalyzeService;
    private final FocusedScanService focusedScanService;
    private final FocusedAnalyzeService focusedAnalyzeService;
    private final TimelineService timelineService;
    private final GlobalReactService globalReactService;
    private final RecordingService recordingService;

    @Value("${rca.scan.focused-top-n:2}")
    private int focusedTopN;

    public RcaOrchestrator(LlmPlanner planner,
                           BroadScanService broadScanService,
                           BroadAnalyzeService broadAnalyzeService,
                           FocusedScanService focusedScanService,
                           FocusedAnalyzeService focusedAnalyzeService,
                           TimelineService timelineService,
                           GlobalReactService globalReactService,
                           RecordingService recordingService) {
        this.planner = planner;
        this.broadScanService = broadScanService;
        this.broadAnalyzeService = broadAnalyzeService;
        this.focusedScanService = focusedScanService;
        this.focusedAnalyzeService = focusedAnalyzeService;
        this.timelineService = timelineService;
        this.globalReactService = globalReactService;
        this.recordingService = recordingService;
    }

    public RcaResponse analyze(RcaRequest request) {
        return analyze(request, null);
    }

    /**
     * 执行 RCA 分析。
     * @param replayRecording 非 null 时进入 REPLAY 模式，工具/LLM 调用返回录制出参。
     */
    public RcaResponse analyze(RcaRequest request, Recording replayRecording) {
        if (request.getAlarmTime() == null) {
            request.setAlarmTime(LocalDateTime.now());
        }
        boolean llmAllowed = Boolean.TRUE.equals(request.getEnableLlm());
        String analysisId = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        List<StageTrace> stages = new ArrayList<>();

        boolean record = Boolean.TRUE.equals(request.getRecord());
        Recording recording = null;
        RecordingSession session = null;
        if (replayRecording != null) {
            session = new RecordingSession(RecordingSession.Mode.REPLAY, replayRecording);
        } else if (record) {
            recording = new Recording("rec-" + analysisId, analysisId);
            session = new RecordingSession(RecordingSession.Mode.RECORD, recording);
        }
        if (session != null) {
            RecordingContext.set(session);
        }

        try {
            // 1. Plan：生成竞争假设
            PlanResult plan = planner.plan(request);
            List<Hypothesis> hypotheses = plan.hypotheses();
            boolean llmUsed = plan.llmUsed();
            stages.add(StageTrace.of("Plan", "SUCCESS", plan.llmUsed(),
                    plan.note() + "；假设数=" + hypotheses.size()));
            log.info("[RCA-{}] Plan done, hypotheses={}", analysisId, hypotheses.size());

            // 2. BroadScan：按 Playbook 并发采集
            ScanBundle bundle = broadScanService.scan(request, hypotheses);
            List<Evidence> evidences = new ArrayList<>(bundle.evidences());
            stages.add(StageTrace.of("BroadScan", "SUCCESS", false,
                    String.format("采集证据 %d 条，变更事件 %d 条", evidences.size(), bundle.changeEvents().size())));

            // 3. BroadAnalyze：裁决 + 首轮置信度
            boolean narrated = broadAnalyzeService.analyze(hypotheses, evidences, llmAllowed);
            llmUsed = llmUsed || narrated;
            stages.add(StageTrace.of("BroadAnalyze", "SUCCESS", narrated,
                    "决定性信号裁决完成，已刷新首轮置信度"));

            // 4. FocusedScan：topN 假设递归下钻
            List<Evidence> focusedEvidences = focusedScanService.scan(
                    request, hypotheses, bundle, focusedTopN, evidences.size() + 1);
            evidences.addAll(focusedEvidences);
            stages.add(StageTrace.of("FocusedScan", "SUCCESS", false,
                    "top" + focusedTopN + " 假设下钻，新增证据 " + focusedEvidences.size() + " 条"));

            // 5. FocusedAnalyze：全量证据复核
            focusedAnalyzeService.analyze(hypotheses, evidences);
            stages.add(StageTrace.of("FocusedAnalyze", "SUCCESS", false,
                    "下钻假设用 BROAD+FOCUSED 全量证据复核置信度"));

            // 时间线重建（供 GlobalReact 变更感知与最终报告展示）
            var timeline = timelineService.build(request, evidences, bundle.changeEvents());

            // 6. GlobalReact：拓扑/变更/反事实校准 + 排序分级 + 建议
            GlobalReactService.ReactResult react = globalReactService.react(
                    request, hypotheses, evidences, bundle.changeEvents());
            stages.add(react.trace());

            RcaResponse response = new RcaResponse();
            response.setAnalysisId(analysisId);
            response.setAppId(request.getAppId());
            response.setScene(request.getScene());
            response.setAlarmDescription(request.getAlarmDescription());
            response.setAlarmTime(request.getAlarmTime());
            response.setAnalysisTime(LocalDateTime.now());
            response.setLlmUsed(llmUsed);
            response.setResultLevel(react.resultLevel());
            response.setStages(stages);
            response.setHypotheses(hypotheses);
            response.setTimeline(timeline);
            response.setEvidences(evidences);
            response.setConclusions(react.conclusions());
            response.setSuggestions(react.suggestions());

            // 回放模式不写入历史，避免污染
            if (replayRecording == null) {
                recordingService.save(new AnalysisRecord(analysisId, request, response, recording));
            }

            log.info("[RCA-{}] done, resultLevel={}", analysisId, react.resultLevel());
            return response;
        } finally {
            RecordingContext.clear();
        }
    }
}
