package com.bage.study.ai.best.practice.rca.agent.recording;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 录制记录 JPA 实体：recordingId 为主键，interactions 序列化为 JSON 字符串存储。
 */
@Entity
@Table(name = "rca_recording")
public class RecordingEntity {

    @Id
    private String recordingId;

    private String analysisId;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String interactionsJson;

    private LocalDateTime createdAt;

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

    public String getInteractionsJson() {
        return interactionsJson;
    }

    public void setInteractionsJson(String interactionsJson) {
        this.interactionsJson = interactionsJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
