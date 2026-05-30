package com.bage.study.ai.best.practice.exception.analysis.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class AnalysisRequest {

    @NotBlank(message = "应用ID不能为空")
    private String appId;

    @NotBlank(message = "告警描述不能为空")
    private String alarmDescription;

    private String alarmUrl;

    private LocalDateTime alarmTime;

    public AnalysisRequest() {
    }

    public AnalysisRequest(String appId, String alarmDescription, String alarmUrl, LocalDateTime alarmTime) {
        this.appId = appId;
        this.alarmDescription = alarmDescription;
        this.alarmUrl = alarmUrl;
        this.alarmTime = alarmTime;
    }

    public String getAppId() {
        return appId