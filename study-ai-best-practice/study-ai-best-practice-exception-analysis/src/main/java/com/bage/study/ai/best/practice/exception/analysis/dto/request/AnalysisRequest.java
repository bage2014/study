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

    private String scene;

    public AnalysisRequest() {
    }

    public AnalysisRequest(String appId, String alarmDescription, String alarmUrl, LocalDateTime alarmTime) {
        this.appId = appId;
        this.alarmDescription = alarmDescription;
        this.alarmUrl = alarmUrl;
        this.alarmTime = alarmTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAlarmDescription() {
        return alarmDescription;
    }

    public void setAlarmDescription(String alarmDescription) {
        this.alarmDescription = alarmDescription;
    }

    public String getAlarmUrl() {
        return alarmUrl;
    }

    public void setAlarmUrl(String alarmUrl) {
        this.alarmUrl = alarmUrl;
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }
}