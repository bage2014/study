package com.bage.study.ai.best.practice.rca.agent.recording;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.dto.RcaResponse;

import java.time.LocalDateTime;

/**
 * RCA 分析历史记录：保存一次分析的请求、响应与（可选）录制。
 */
public class AnalysisRecord {

    private String analysisId;
    private RcaRequest request;
    private RcaResponse response;
    private Recording recording;
    private LocalDateTime createdAt;
    private String resultLevel;
    private boolean recorded;

    public AnalysisRecord() {
    }

    public AnalysisRecord(String analysisId, RcaRequest request, RcaResponse response, Recording recording) {
        this.analysisId = analysisId;
        this.request = request;
        this.response = response;
        this.recording = recording;
        this.createdAt = LocalDateTime.now();
        this.resultLevel = response != null ? response.getResultLevel() : null;
        this.recorded = recording != null;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public RcaRequest getRequest() {
        return request;
    }

    public void setRequest(RcaRequest request) {
        this.request = request;
    }

    public RcaResponse getResponse() {
        return response;
    }

    public void setResponse(RcaResponse response) {
        this.response = response;
    }

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
        this.recorded = recording != null;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
}
