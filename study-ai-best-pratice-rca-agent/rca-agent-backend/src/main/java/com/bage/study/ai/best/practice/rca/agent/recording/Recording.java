package com.bage.study.ai.best.practice.rca.agent.recording;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 一次 RCA 分析的全部外部交互录制。交互按采集时间排序存储。
 */
public class Recording {

    private String recordingId;
    private String analysisId;
    private LocalDateTime createdAt;
    private List<InteractionRecord> interactions = new ArrayList<>();

    public Recording() {
    }

    public Recording(String recordingId, String analysisId) {
        this.recordingId = recordingId;
        this.analysisId = analysisId;
        this.createdAt = LocalDateTime.now();
    }

    public void add(InteractionRecord record) {
        interactions.add(record);
    }

    public String getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(String recordingId) {
        this.recordingId = recordingId;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<InteractionRecord> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<InteractionRecord> interactions) {
        this.interactions = interactions;
    }
}
