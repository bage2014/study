package com.bage.study.ai.best.practice.rca.agent.recording;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 分析历史记录 JPA 实体：analysisId 为主键，request/response 序列化为 JSON 字符串存储。
 */
@Entity
@Table(name = "rca_analysis_record")
public class AnalysisRecordEntity {

    @Id
    private String analysisId;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String requestJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String responseJson;

    private String resultLevel;

    private boolean recorded;

    private LocalDateTime createdAt;

    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public String getResultLevel() {
        return resultLevel;
    }

    public void setResultLevel(String resultLevel) {
        this.resultLevel = resultLevel;
    }

    public boolean isRecorded() {
        return recorded;
    }

    public void setRecorded(boolean recorded) {
        this.recorded = recorded;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
