package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class DeploymentRecord {

    private String deploymentId;
    private String appId;
    private String version;
    private String status;
    private LocalDateTime deployTime;
    private String operator;
    private String description;

    public DeploymentRecord() {
    }

    public DeploymentRecord(String deploymentId, String appId, String version, String status, 
                           LocalDateTime deployTime, String operator, String description) {
        this.deploymentId = deploymentId;
        this.appId = appId;
        this.version = version;
        this.status = status;
        this.deployTime = deployTime;
        this.operator = operator;
        this.description = description;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(LocalDateTime deployTime) {
        this.deployTime = deployTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}