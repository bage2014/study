package com.bage.study.ai.best.practice.rca.agent.recording;

import java.time.LocalDateTime;

/**
 * 分析历史列表摘要，避免在列表接口返回完整响应体。
 */
public record AnalysisSummary(
        String analysisId,
        String appId,
        String scene,
        String resultLevel,
        boolean recorded,
        String recordingId,
        LocalDateTime createdAt
) {
    public static AnalysisSummary from(AnalysisRecord record) {
        return new AnalysisSummary(
                record.getAnalysisId(),
                record.getRequest() != null ? record.getRequest().getAppId() : null,
                record.getRequest() != null ? record.getRequest().getScene() : null,
                record.getResultLevel(),
                record.isRecorded(),
                record.getRecording() != null ? record.getRecording().getRecordingId() : null,
                record.getCreatedAt()
        );
    }
}
