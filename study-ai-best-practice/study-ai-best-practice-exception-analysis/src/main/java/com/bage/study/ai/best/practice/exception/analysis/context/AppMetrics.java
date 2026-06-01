package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class AppMetrics {

    private String appId;
    private LocalDateTime timestamp;
    private double cpuUsage;
    private double memoryUsage;
    private int threadCount;
    private int activeThreads;
    private int daemonThreads;
    private long gcCount;
    private double gcTime;
    private long heapUsed;
    private long heapMax;
    private long nonHeapUsed;
    private int connectionPoolSize;
    private int activeConnections;

    public AppMetrics() {
    }

    public AppMetrics(String appId, LocalDateTime timestamp, double cpuUsage, double memoryUsage,
                     int threadCount, int activeThreads, int daemonThreads,
                     long gcCount, double gcTime, long heapUsed, long heapMax,
                     long nonHeapUsed, int connectionPoolSize, int activeConnections) {
        this.appId = appId;
        this.timestamp = timestamp;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.threadCount = threadCount;
        this.activeThreads = activeThreads;
        this.daemonThreads = daemonThreads;
        this.gcCount = gcCount;
        this.gcTime = gcTime;
        this.heapUsed = heapUsed;
        this.heapMax = heapMax;
        this.nonHeapUsed = nonHeapUsed;
        this.connectionPoolSize = connectionPoolSize;
        this.activeConnections = activeConnections;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(int activeThreads) {
        this.activeThreads = activeThreads;
    }

    public int getDaemonThreads() {
        return daemonThreads;
    }

    public void setDaemonThreads(int daemonThreads) {
        this.daemonThreads = daemonThreads;
    }

    public long getGcCount() {
        return gcCount;
    }

    public void setGcCount(long gcCount) {
        this.gcCount = gcCount;
    }

    public double getGcTime() {
        return gcTime;
    }

    public void setGcTime(double gcTime) {
        this.gcTime = gcTime;
    }

    public long getHeapUsed() {
        return heapUsed;
    }

    public void setHeapUsed(long heapUsed) {
        this.heapUsed = heapUsed;
    }

    public long getHeapMax() {
        return heapMax;
    }

    public void setHeapMax(long heapMax) {
        this.heapMax = heapMax;
    }

    public long getNonHeapUsed() {
        return nonHeapUsed;
    }

    public void setNonHeapUsed(long nonHeapUsed) {
        this.nonHeapUsed = nonHeapUsed;
    }

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public int getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }
}