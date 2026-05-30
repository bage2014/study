package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class AppMetrics {

    private String appId;
    private LocalDateTime time;
    private Double cpuUsage;
    private Double memoryUsage;
    private Long heapUsed;
    private Long heapMax;
    private Integer gcCount;
    private Long gcTime;
    private Integer threadCount;
    private Integer activeThreads;
    private Integer daemonThreads;

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

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Long getHeapUsed() {
        return heapUsed;
    }

    public void setHeapUsed(Long heapUsed) {
        this.heapUsed = heapUsed;
    }

    public Long getHeapMax() {
        return heapMax;
    }

    public void setHeapMax(Long heapMax) {
        this.heapMax = heapMax;
    }

    public Integer getGcCount() {
        return gcCount;
    }

    public void setGcCount(Integer gcCount) {
        this.gcCount = gcCount;
    }

    public Long getGcTime() {
        return gcTime;
    }

    public void setGcTime(Long gcTime) {
        this.gcTime = gcTime;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public Integer getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(Integer activeThreads) {
        this.activeThreads = activeThreads;
    }

    public Integer getDaemonThreads() {
        return daemonThreads;
    }

    public void setDaemonThreads(Integer daemonThreads) {
        this.daemonThreads = daemonThreads;
    }
}
