package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class RequestMetrics {

    private String appId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long totalRequests;
    private long successRequests;
    private long failedRequests;
    private double avgResponseTime;
    private double p95ResponseTime;
    private double p99ResponseTime;
    private double errorRate;

    public RequestMetrics() {
    }

    public RequestMetrics(String appId, LocalDateTime startTime, LocalDateTime endTime,
                         long totalRequests, long successRequests, long failedRequests,
                         double avgResponseTime, double p95ResponseTime, double p99ResponseTime,
                         double errorRate) {
        this.appId = appId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalRequests = totalRequests;
        this.successRequests = successRequests;
        this.failedRequests = failedRequests;
        this.avgResponseTime = avgResponseTime;
        this.p95ResponseTime = p95ResponseTime;
        this.p99ResponseTime = p99ResponseTime;
        this.errorRate = errorRate;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public long getSuccessRequests() {
        return successRequests;
    }

    public void setSuccessRequests(long successRequests) {
        this.successRequests = successRequests;
    }

    public long getFailedRequests() {
        return failedRequests;
    }

    public void setFailedRequests(long failedRequests) {
        this.failedRequests = failedRequests;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public double getP95ResponseTime() {
        return p95ResponseTime;
    }

    public void setP95ResponseTime(double p95ResponseTime) {
        this.p95ResponseTime = p95ResponseTime;
    }

    public double getP99ResponseTime() {
        return p99ResponseTime;
    }

    public void setP99ResponseTime(double p99ResponseTime) {
        this.p99ResponseTime = p99ResponseTime;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }
}