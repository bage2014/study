package com.bage.study.ai.best.practice.exception.analysis.mcp;

import com.bage.study.ai.best.practice.exception.analysis.context.AppMetrics;
import com.bage.study.ai.best.practice.exception.analysis.context.RequestMetrics;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class MetricsMcpService {

    private final Random random = new Random();

    public RequestMetrics getRequestMetrics(String appId, LocalDateTime startTime, LocalDateTime endTime) {
        long totalRequests = random.nextLong(10000) + 1000;
        double errorRate = random.nextDouble() * 0.15;
        long failedRequests = (long) (totalRequests * errorRate);
        long successRequests = totalRequests - failedRequests;

        return new RequestMetrics(
            appId,
            startTime != null ? startTime : LocalDateTime.now().minusHours(1),
            endTime != null ? endTime : LocalDateTime.now(),
            totalRequests,
            successRequests,
            failedRequests,
            random.nextDouble() * 500 + 50,
            random.nextDouble() * 1000 + 200,
            random.nextDouble() * 2000 + 500,
            errorRate
        );
    }

    public AppMetrics getAppMetrics(String appId, LocalDateTime timestamp) {
        double cpuUsage = random.nextDouble() * 100;
        double memoryUsage = random.nextDouble() * 100;

        return new AppMetrics(
            appId,
            timestamp != null ? timestamp : LocalDateTime.now(),
            cpuUsage,
            memoryUsage,
            random.nextInt(500) + 50,
            random.nextInt(200) + 10,
            random.nextInt(100) + 5,
            random.nextLong(100) + 10,
            random.nextDouble() * 60 + 5,
            random.nextLong(1024 * 1024 * 1024L) + 512 * 1024 * 1024L,
            2 * 1024 * 1024 * 1024L,
            random.nextLong(256 * 1024 * 1024L) + 64 * 1024 * 1024L,
            random.nextInt(100) + 10,
            random.nextInt(50) + 5
        );
    }
}