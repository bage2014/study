package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class JobMetrics {

    private String jobName;
    private String jobGroup;
    private String triggerType;
    private String status;
    private LocalDateTime lastRunTime;
    private LocalDateTime nextRunTime;
    private long duration;
    private long executionCount;
    private long failureCount;
    private double successRate;
    private String lastErrorMessage;

    public JobMetrics() {
    }

    public JobMetrics(String jobName, String jobGroup, String triggerType, String status) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.triggerType = triggerType;
        this.status = status;
        this.lastRunTime = LocalDateTime.now().minusMinutes(30);
        this.nextRunTime = LocalDateTime.now().plusMinutes(30);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(LocalDateTime lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public LocalDateTime getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(LocalDateTime nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(long executionCount) {
        this.executionCount = executionCount;
    }

    public long getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(long failureCount) {
        this.failureCount = failureCount;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }
}