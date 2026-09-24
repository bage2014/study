package com.bage.study.ai.best.practice.rca.agent.recording;

import java.time.LocalDateTime;

/**
 * 录制摘要，用于录制列表接口。
 */
public record RecordingSummary(
        String recordingId,
        String analysisId,
        int interactionCount,
        LocalDateTime createdAt
) {
    public static RecordingSummary from(Recording recording) {
        return new RecordingSummary(
                recording.getRecordingId(),
                recording.getAnalysisId(),
                recording.getInteractions() != null ? recording.getInteractions().size() : 0,
                recording.getCreatedAt()
        );
    }
}
