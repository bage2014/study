package com.bage.study.best.practice.trial.gc;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 垃圾回收性能指标监控工具
 */
@Slf4j
public class GCMonitor {

    private final List<GarbageCollectorMXBean> gcBeans;
    private final MemoryMXBean memoryBean;
    private final OperatingSystemMXBean osBean;
    
    // GC统计信息
    private final List<GCStat> gcStats;
    
    // 监控开始时间
    private long startTime;
    
    public GCMonitor() {
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
        this.gcStats = new ArrayList<>();
    }
    
    /**
     * 开始监控
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.gcStats.clear();
        
        // 记录初始状态
        logInitialState();
    }
    
    /**
     * 结束监控并返回统计结果
     */
    public GCMonitorResult stop() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // 收集GC统计信息
        collectGCStats();
        
        // 记录结束状态
        logFinalState();
        
        // 计算性能指标
        return calculateMetrics(duration);
    }
    
    /**
     * 记录初始状态
     */
    private void logInitialState() {
        log.info("=== GC Monitor Initial State ===");
        logMemoryUsage("Initial Memory Usage", memoryBean.getHeapMemoryUsage());
        log.info("Available Processors: {}", osBean.getAvailableProcessors());
        log.info("================================");
    }
    
    /**
     * 记录结束状态
     */
    private void logFinalState() {
        log.info("=== GC Monitor Final State ===");
        logMemoryUsage("Final Memory Usage", memoryBean.getHeapMemoryUsage());
        log.info("================================");
    }
    
    /**
     * 记录内存使用情况
     */
    private void logMemoryUsage(String label, MemoryUsage usage) {
        log.info("{}:", label);
        log.info("  Init: {} MB", bytesToMB(usage.getInit()));
        log.info("  Used: {} MB", bytesToMB(usage.getUsed()));
        log.info("  Committed: {} MB", bytesToMB(usage.getCommitted()));
        log.info("  Max: {} MB", bytesToMB(usage.getMax()));
    }
    
    /**
     * 收集GC统计信息
     */
    private void collectGCStats() {
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            GCStat stat = new GCStat();
            stat.setName(gcBean.getName());
            stat.setCollectionCount(gcBean.getCollectionCount());
            stat.setCollectionTime(gcBean.getCollectionTime());
            gcStats.add(stat);
            
            log.info("GC Stat - {}: count={}, time={}ms", 
                    gcBean.getName(), gcBean.getCollectionCount(), gcBean.getCollectionTime());
        }
    }
    
    /**
     * 计算性能指标
     */
    private GCMonitorResult calculateMetrics(long duration) {
        GCMonitorResult result = new GCMonitorResult();
        result.setDuration(duration);
        
        // 计算总GC次数和时间
        long totalGCCount = 0;
        long totalGCTime = 0;
        
        for (GCStat stat : gcStats) {
            totalGCCount += stat.getCollectionCount();
            totalGCTime += stat.getCollectionTime();
        }
        
        result.setTotalGCCount(totalGCCount);
        result.setTotalGCTime(totalGCTime);
        
        // 计算吞吐量（非GC时间占总时间的比例）
        double throughput = ((double) (duration - totalGCTime) / duration) * 100;
        result.setThroughput(throughput);
        
        // 计算平均GC停顿时间
        double avgGCPause = totalGCCount > 0 ? (double) totalGCTime / totalGCCount : 0;
        result.setAvgGCPause(avgGCPause);
        
        // 记录堆内存使用情况
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        result.setHeapUsed(bytesToMB(heapUsage.getUsed()));
        result.setHeapCommitted(bytesToMB(heapUsage.getCommitted()));
        result.setHeapMax(bytesToMB(heapUsage.getMax()));
        
        // 记录非堆内存使用情况
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
        result.setNonHeapUsed(bytesToMB(nonHeapUsage.getUsed()));
        
        // 记录GC统计信息
        result.setGcStats(gcStats);
        
        log.info("=== GC Performance Metrics ===");
        log.info("Total Duration: {}ms", duration);
        log.info("Total GC Count: {}", totalGCCount);
        log.info("Total GC Time: {}ms", totalGCTime);
        log.info("Throughput: {:.2f}%", throughput);
        log.info("Average GC Pause: {:.2f}ms", avgGCPause);
        log.info("Heap Used: {} MB", result.getHeapUsed());
        log.info("Heap Committed: {} MB", result.getHeapCommitted());
        log.info("Heap Max: {} MB", result.getHeapMax());
        log.info("Non-Heap Used: {} MB", result.getNonHeapUsed());
        log.info("===============================");
        
        return result;
    }
    
    /**
     * 字节转换为MB
     */
    private long bytesToMB(long bytes) {
        return bytes / (1024 * 1024);
    }
    
    /**
     * GC统计信息
     */
    public static class GCStat {
        private String name;
        private long collectionCount;
        private long collectionTime;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public long getCollectionCount() {
            return collectionCount;
        }
        
        public void setCollectionCount(long collectionCount) {
            this.collectionCount = collectionCount;
        }
        
        public long getCollectionTime() {
            return collectionTime;
        }
        
        public void setCollectionTime(long collectionTime) {
            this.collectionTime = collectionTime;
        }
        
        @Override
        public String toString() {
            return "GCStat{" +
                    "name='" + name + '\'' +
                    ", collectionCount=" + collectionCount +
                    ", collectionTime=" + collectionTime +
                    "}";
        }
    }
    
    /**
     * GC监控结果
     */
    public static class GCMonitorResult {
        private long duration;
        private long totalGCCount;
        private long totalGCTime;
        private double throughput;
        private double avgGCPause;
        private long heapUsed;
        private long heapCommitted;
        private long heapMax;
        private long nonHeapUsed;
        private List<GCStat> gcStats;
        
        public long getDuration() {
            return duration;
        }
        
        public void setDuration(long duration) {
            this.duration = duration;
        }
        
        public long getTotalGCCount() {
            return totalGCCount;
        }
        
        public void setTotalGCCount(long totalGCCount) {
            this.totalGCCount = totalGCCount;
        }
        
        public long getTotalGCTime() {
            return totalGCTime;
        }
        
        public void setTotalGCTime(long totalGCTime) {
            this.totalGCTime = totalGCTime;
        }
        
        public double getThroughput() {
            return throughput;
        }
        
        public void setThroughput(double throughput) {
            this.throughput = throughput;
        }
        
        public double getAvgGCPause() {
            return avgGCPause;
        }
        
        public void setAvgGCPause(double avgGCPause) {
            this.avgGCPause = avgGCPause;
        }
        
        public long getHeapUsed() {
            return heapUsed;
        }
        
        public void setHeapUsed(long heapUsed) {
            this.heapUsed = heapUsed;
        }
        
        public long getHeapCommitted() {
            return heapCommitted;
        }
        
        public void setHeapCommitted(long heapCommitted) {
            this.heapCommitted = heapCommitted;
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
        
        public List<GCStat> getGcStats() {
            return gcStats;
        }
        
        public void setGcStats(List<GCStat> gcStats) {
            this.gcStats = gcStats;
        }
        
        @Override
        public String toString() {
            return "GCMonitorResult{" +
                    "duration=" + duration +
                    ", totalGCCount=" + totalGCCount +
                    ", totalGCTime=" + totalGCTime +
                    ", throughput=" + String.format("%.2f", throughput) + "%" +
                    ", avgGCPause=" + String.format("%.2f", avgGCPause) + "ms" +
                    ", heapUsed=" + heapUsed + "MB" +
                    ", heapCommitted=" + heapCommitted + "MB" +
                    ", heapMax=" + heapMax + "MB" +
                    ", nonHeapUsed=" + nonHeapUsed + "MB" +
                    ", gcStats=" + gcStats +
                    "}";
        }
    }
}
