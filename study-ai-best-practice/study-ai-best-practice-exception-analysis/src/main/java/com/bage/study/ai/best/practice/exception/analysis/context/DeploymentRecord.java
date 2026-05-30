package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class DeploymentRecord {

    private String deploymentId;
    private String appId;
    private String version;
    private String status;
    private LocalDateTime deployTime;
    private String changes;
    private String operator;

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

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
