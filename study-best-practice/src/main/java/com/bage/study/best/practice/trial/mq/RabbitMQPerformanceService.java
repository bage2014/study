package com.bage.study.best.practice.trial.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class RabbitMQPerformanceService {
    private static final String PERFORMANCE_QUEUE = "performance.queue";
    private static final String PERFORMANCE_EXCHANGE = "performance.exchange";
    private static final String PERFORMANCE_ROUTING_KEY = "performance.routing.key";
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private final AtomicInteger messageCount = new AtomicInteger(0);
    private final AtomicInteger receivedCount = new AtomicInteger(0);
    private long startTime;

    /**
     * 单线程发送性能测试
     */
    public PerformanceResult singleThreadSend(int count) {
        messageCount.set(0);
        startTime = System.currentTimeMillis();
        int successCount = 0;
        int errorCount = 0;

        for (int i = 0; i < count; i++) {
            try {
                String message = generateMessage(i);
                rabbitTemplate.convertAndSend(PERFORMANCE_EXCHANGE, PERFORMANCE_ROUTING_KEY, message);
                successCount++;
                messageCount.incrementAndGet();
            } catch (Exception e) {
                errorCount++;
                log.error("Single thread send error: {}", e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double qps = (double) successCount / duration * 1000;

        return new PerformanceResult(
                "Single Thread Send",
                count,
                successCount,
                errorCount,
                duration,
                qps
        );
    }

    /**
     * 多线程发送性能测试
     */
    public PerformanceResult multiThreadSend(int count, int threadCount) {
        messageCount.set(0);
        startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        final int perThreadCount = count / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < perThreadCount; j++) {
                        try {
                            String message = generateMessage(threadId * perThreadCount + j);
                            rabbitTemplate.convertAndSend(PERFORMANCE_EXCHANGE, PERFORMANCE_ROUTING_KEY, message);
                            successCount.incrementAndGet();
                            messageCount.incrementAndGet();
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                            log.error("Multi thread send error: {}", e.getMessage());
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double qps = (double) successCount.get() / duration * 1000;

        return new PerformanceResult(
                "Multi Thread Send",
                count,
                successCount.get(),
                errorCount.get(),
                duration,
                qps
        );
    }

    /**
     * 发送并接收性能测试
     */
    public PerformanceResult sendAndReceive(int count) {
        messageCount.set(0);
        receivedCount.set(0);
        startTime = System.currentTimeMillis();
        int successCount = 0;
        int errorCount = 0;

        for (int i = 0; i < count; i++) {
            try {
                String message = generateMessage(i);
                Object response = rabbitTemplate.convertSendAndReceive(PERFORMANCE_EXCHANGE, PERFORMANCE_ROUTING_KEY, message);
                if (response != null) {
                    successCount++;
                    messageCount.incrementAndGet();
                    receivedCount.incrementAndGet();
                }
            } catch (Exception e) {
                errorCount++;
                log.error("Send and receive error: {}", e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double qps = (double) successCount / duration * 1000;

        return new PerformanceResult(
                "Send and Receive",
                count,
                successCount,
                errorCount,
                duration,
                qps
        );
    }

    /**
     * 生成测试消息
     */
    private String generateMessage(int index) {
        return "Performance test message " + index + " - " + UUID.randomUUID().toString();
    }

    /**
     * 性能测试结果
     */
    public static class PerformanceResult {
        private final String testType;
        private final int totalCount;
        private final int successCount;
        private final int errorCount;
        private final long durationMs;
        private final double qps;

        public PerformanceResult(String testType, int totalCount, int successCount, int errorCount, long durationMs, double qps) {
            this.testType = testType;
            this.totalCount = totalCount;
            this.successCount = successCount;
            this.errorCount = errorCount;
            this.durationMs = durationMs;
            this.qps = qps;
        }

        public String getTestType() {
            return testType;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public long getDurationMs() {
            return durationMs;
        }

        public double getQps() {
            return qps;
        }

        @Override
        public String toString() {
            return "PerformanceResult{" +
                    "testType='" + testType + '\'' +
                    ", totalCount=" + totalCount +
                    ", successCount=" + successCount +
                    ", errorCount=" + errorCount +
                    ", durationMs=" + durationMs +
                    ", qps=" + String.format("%.2f", qps) +
                    '}';
        }
    }
}
