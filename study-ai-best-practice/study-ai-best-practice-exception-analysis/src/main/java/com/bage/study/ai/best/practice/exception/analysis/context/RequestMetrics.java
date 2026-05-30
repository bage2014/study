package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class RequestMetrics {

    private String appId;
    private LocalDateTime time;
    private Long totalRequests;
    private Long successRequests;
    private Long failedRequests;
    private Double avgResponseTime;
    private Double maxResponseTime;
    private Double minResponseTime;
    private Integer errorRate;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public Long getSuccessRequests() {
        return successRequests;
    }

    public void setSuccessRequests(Long successRequests) {
        this.successRequests = successRequests;
    }

    public Long getFailedRequests() {
        return failedRequests;
    }

    public void setFailedRequests(Long failedRequests) {
        this.failedRequests = failedRequests;
    }

    public Double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(Double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public Double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Double maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Double getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(Double minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public Integer getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(Integer errorRate) {
        this.errorRate = errorRate;
    }
}
