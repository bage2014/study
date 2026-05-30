package com.bage.study.ai.best.practice.exception.analysis.mcp;

import com.bage.study.ai.best.practice.exception.analysis.context.AppMetrics;
import com.bage.study.ai.best.practice.exception.analysis.context.RequestMetrics;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MetricsMcpService {

    private final Random random = new Random();

    public List<RequestMetrics> getRequestMetrics(String appId, LocalDateTime startTime, LocalDateTime endTime) {
        List<RequestMetrics> metricsList = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            RequestMetrics metrics = new RequestMetrics();
            metrics.setAppId(appId);
            metrics.setTime(LocalDateTime.now().minusMinutes(i * 10));
            metrics.setTotalRequests((long) (10000 + random.nextInt(5000)));
            metrics.setSuccessRequests((long) (9000 + random.nextInt(4000)));
            metrics.setFailedRequests(metrics.getTotalRequests() - metrics.getSuccessRequests());
            metrics.setAvgResponseTime(50.0 + random.nextDouble() * 100);
            metrics.setMaxResponseTime(200.0 + random.nextDouble() * 300);
            metrics.setMinResponseTime(10.0 + random.nextDouble() * 40);
            metrics.setErrorRate(random.nextInt(15));
            metricsList.add(metrics);
        }
        
        return metricsList;
    }

    public List<AppMetrics> getAppMetrics(String appId, LocalDateTime startTime, LocalDateTime endTime) {
        List<AppMetrics> metricsList = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            AppMetrics metrics = new AppMetrics();
            metrics.setAppId(appId);
            metrics.setTime(LocalDateTime.now().minusMinutes(i * 10));
            metrics.setCpuUsage(30.0 + random.nextDouble() * 60);
            metrics.setMemoryUsage(40.0 + random.nextDouble() * 40);
            metrics.setHeapUsed((long) (512 * 1024 * 1024 + random.nextLong() * 512 * 1024 * 1024));
            metrics.setHeapMax(2048L * 1024 * 1024);
            metrics.setGcCount(random.nextInt(50));
            metrics.setGcTime((long) (100 + random.nextInt(500)));
            metrics.setThreadCount(100 + random.nextInt(200));
            metrics.setActiveThreads(20 + random.nextInt(80));
            metrics.setDaemonThreads(10 + random.nextInt(30));
            metricsList.add(metrics);
        }
        
        return metricsList;
    }
}
