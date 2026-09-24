package com.bage.study.ai.best.practice.rca.agent.controller;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.dto.RcaResponse;
import com.bage.study.ai.best.practice.rca.agent.recording.AnalysisRecord;
import com.bage.study.ai.best.practice.rca.agent.recording.AnalysisSummary;
import com.bage.study.ai.best.practice.rca.agent.recording.Recording;
import com.bage.study.ai.best.practice.rca.agent.recording.RecordingService;
import com.bage.study.ai.best.practice.rca.agent.recording.RecordingSummary;
import com.bage.study.ai.best.practice.rca.agent.service.RcaOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RCA 分析历史、录制与回放接口。
 */
@RestController
@RequestMapping("/api/rca")
public class RcaHistoryController {

    private final RecordingService recordingService;
    private final RcaOrchestrator orchestrator;

    public RcaHistoryController(RecordingService recordingService, RcaOrchestrator orchestrator) {
        this.recordingService = recordingService;
        this.orchestrator = orchestrator;
    }

    /** 分析历史列表（按时间倒序，分页）。 */
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> history(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        List<AnalysisRecord> all = recordingService.list();
        int total = all.size();
        int from = Math.min(page * size, total);
        int to = Math.min(from + size, total);
        List<AnalysisSummary> items = all.subList(from, to).stream()
                .map(AnalysisSummary::from)
                .toList();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("items", items);
        body.put("total", total);
        body.put("page", page);
        body.put("size", size);
        return ResponseEntity.ok(body);
    }

    /** 分析历史详情（含请求、响应、录制）。 */
    @GetMapping("/history/{id}")
    public ResponseEntity<AnalysisRecord> historyDetail(@PathVariable("id") String id) {
        AnalysisRecord record = recordingService.get(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }

    /** 删除分析历史及其关联录制。 */
    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable("id") String id) {
        if (!recordingService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    /** 录制列表。 */
    @GetMapping("/recordings")
    public ResponseEntity<List<RecordingSummary>> recordings() {
        List<RecordingSummary> items = recordingService.listRecordings().stream()
                .map(RecordingSummary::from)
                .toList();
        return ResponseEntity.ok(items);
    }

    /** 录制详情（全部交互明细）。 */
    @GetMapping("/recordings/{id}")
    public ResponseEntity<Recording> recordingDetail(@PathVariable("id") String id) {
        Recording recording = recordingService.getRecording(id);
        if (recording == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recording);
    }

    /** 基于历史录制回放，返回与原始分析一致的 RcaResponse。 */
    @PostMapping("/replay/{id}")
    public ResponseEntity<RcaResponse> replay(@PathVariable("id") String id) {
        AnalysisRecord record = recordingService.get(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        Recording recording = record.getRecording();
        if (recording == null) {
            return ResponseEntity.badRequest().build();
        }
        // 复制请求，避免修改已存储的历史请求
        RcaRequest request = copyRequest(record.getRequest());
        RcaResponse response = orchestrator.analyze(request, recording);
        return ResponseEntity.ok(response);
    }

    private RcaRequest copyRequest(RcaRequest src) {
        RcaRequest req = new RcaRequest();
        req.setAppId(src.getAppId());
        req.setAlarmDescription(src.getAlarmDescription());
        req.setAlarmTime(src.getAlarmTime());
        req.setScene(src.getScene());
        req.setEnableLlm(src.getEnableLlm());
        req.setRecord(false); // 回放不再录制
        return req;
    }
}
