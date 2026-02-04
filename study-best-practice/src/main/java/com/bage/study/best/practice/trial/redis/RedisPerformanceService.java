package com.bage.study.best.practice.trial.redis;

import com.bage.study.best.practice.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisPerformanceService {
    private static final String PREFIX = "redis_perf_";
    private static final int DEFAULT_BATCH_SIZE = 1000;
    private static final int DEFAULT_THREAD_COUNT = 10;
    
    private final CacheService cacheService;
    private final Random random = new Random();

    public RedisPerformanceService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 单线程写入性能测试
     */
    public PerformanceResult singleThreadWrite(int count) {
        long startTime = System.currentTimeMillis();
        int successCount = 0;
        int errorCount = 0;

        for (int i = 0; i < count; i++) {
            try {
                String key = PREFIX + "single_write_" + i;
                String value = generateRandomValue(100);
                cacheService.cache(key, value);
                successCount++;
            } catch (Exception e) {
                errorCount++;
                log.error("Single thread write error: {}", e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double qps = (double) successCount / duration * 1000;

        return new PerformanceResult(
                "Single Thread Write",
                count,
                successCount,
                errorCount,
                duration,
                qps
        );
    }

    /**
     * 多线程写入性能测试
     */
    public PerformanceResult multiThreadWrite(int count, int threadCount) {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        final int[] successCount = {0};
        final int[] errorCount = {0};
        final int perThreadCount = count / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < perThreadCount; j++) {
                        try {
                            String key = PREFIX + "multi_write_" + threadId + "_" + j;
                            String value = generateRandomValue(100);
                            cacheService.cache(key, value);
                            synchronized (successCount) {
                                successCount[0]++;
                            }
                        } catch (Exception e) {
                            synchronized (errorCount) {
                                errorCount[0]++;
                            }
                            log.error("Multi thread write error: {}", e.getMessage());
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
        double qps = (double) successCount[0] / duration * 1000;

        return new PerformanceResult(
                "Multi Thread Write",
                count,
                successCount[0],
                errorCount[0],
                duration,
                qps
        );
    }

    /**
     * 单线程读取性能测试
     */
    public PerformanceResult singleThreadRead(int count) {
        long startTime = System.currentTimeMillis();
        int successCount = 0;
        int errorCount = 0;

        for (int i = 0; i < count; i++) {
            try {
                String key = PREFIX + "single_write_" + (i % count);
                Object value = cacheService.get(key);
                if (value != null) {
                    successCount++;
                }
            } catch (Exception e) {
                errorCount++;
                log.error("Single thread read error: {}", e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double qps = (double) successCount / duration * 1000;

        return new PerformanceResult(
                "Single Thread Read",
                count,
                successCount,
                errorCount,
                duration,
                qps
        );
    }

    /**
     * 多线程读取性能测试
     */
    public PerformanceResult multiThreadRead(int count, int threadCount) {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        final int[] successCount = {0};
        final int[] errorCount = {0};
        final int perThreadCount = count / threadCount;

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < perThreadCount; j++) {
                        try {
                            String key = PREFIX + "single_write_" + (random.nextInt(count));
                            Object value = cacheService.get(key);
                            if (value != null) {
                                synchronized (successCount) {
                                    successCount[0]++;
                                }
                            }
                        } catch (Exception e) {
                            synchronized (errorCount) {
                                errorCount[0]++;
                            }
                            log.error("Multi thread read error: {}", e.getMessage());
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
        double qps = (double) successCount[0] / duration * 1000;

        return new PerformanceResult(
                "Multi Thread Read",
                count,
                successCount[0],
                errorCount[0],
                duration,
                qps
        );
    }

    /**
     * 混合读写性能测试
     */
    public PerformanceResult mixedReadWrite(int count, int threadCount, double readRatio) {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        final int[] successCount = {0};
        final int[] errorCount = {0};
        final int perThreadCount = count / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < perThreadCount; j++) {
                        try {
                            if (random.nextDouble() < readRatio) {
                                // 读操作
                                String key = PREFIX + "single_write_" + (random.nextInt(count));
                                Object value = cacheService.get(key);
                                if (value != null) {
                                    synchronized (successCount) {
                                        successCount[0]++;
                                    }
                                }
                            } else {
                                // 写操作
                                String key = PREFIX + "mixed_write_" + threadId + "_" + j;
                                String value = generateRandomValue(100);
                                cacheService.cache(key, value);
                                synchronized (successCount) {
                                    successCount[0]++;
                                }
                            }
                        } catch (Exception e) {
                            synchronized (errorCount) {
                                errorCount[0]++;
                            }
                            log.error("Mixed read/write error: {}", e.getMessage());
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
        double qps = (double) successCount[0] / duration * 1000;

        return new PerformanceResult(
                "Mixed Read/Write",
                count,
                successCount[0],
                errorCount[0],
                duration,
                qps
        );
    }

    /**
     * 清理测试数据
     */
    public void cleanup() {
        log.info("Cleaning up Redis performance test data");
        // 注意：实际生产环境中不要使用keys命令，这里为了测试方便
        // 生产环境应该使用scan命令
    }

    /**
     * 生成大key - 大字符串
     */
    public String generateBigKey(int sizeInKB) {
        String key = PREFIX + "big_key_" + sizeInKB + "kb";
        StringBuilder sb = new StringBuilder();
        // 生成指定大小的字符串
        for (int i = 0; i < sizeInKB * 1024; i++) {
            sb.append('a');
        }
        String value = sb.toString();
        cacheService.cache(key, value);
        log.info("Generated big key: {} with size: {}KB", key, sizeInKB);
        return key;
    }

    /**
     * 生成大集合key - 大List
     */
    public String generateBigListKey(int size) {
        String key = PREFIX + "big_list_" + size;
        // 生成大List
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add("item_" + i);
        }
        cacheService.cache(key, list);
        log.info("Generated big list key: {} with size: {}", key, size);
        return key;
    }

    /**
     * 模拟热点key访问
     */
    public PerformanceResult simulateHotKey(int count, int threadCount) {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        final int[] successCount = {0};
        final int[] errorCount = {0};
        final int perThreadCount = count / threadCount;
        
        // 先创建一个热点key
        String hotKey = PREFIX + "hot_key";
        String value = generateRandomValue(100);
        cacheService.cache(hotKey, value);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < perThreadCount; j++) {
                        try {
                            // 大部分请求访问热点key
                            if (random.nextDouble() < 0.8) {
                                Object result = cacheService.get(hotKey);
                                if (result != null) {
                                    synchronized (successCount) {
                                        successCount[0]++;
                                    }
                                }
                            } else {
                                // 小部分请求访问其他key
                                String randomKey = PREFIX + "random_" + random.nextInt(1000);
                                Object result = cacheService.get(randomKey);
                                if (result != null) {
                                    synchronized (successCount) {
                                        successCount[0]++;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            synchronized (errorCount) {
                                errorCount[0]++;
                            }
                            log.error("Hot key simulation error: {}", e.getMessage());
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
        double qps = (double) successCount[0] / duration * 1000;

        return new PerformanceResult(
                "Hot Key Simulation",
                count,
                successCount[0],
                errorCount[0],
                duration,
                qps
        );
    }

    /**
     * 测试大key读写性能
     */
    public PerformanceResult bigKeyPerformance(int keySizeInKB) {
        long startTime = System.currentTimeMillis();
        int successCount = 0;
        int errorCount = 0;

        // 生成大key
        String bigKey = generateBigKey(keySizeInKB);

        // 读取大key
        try {
            Object value = cacheService.get(bigKey);
            if (value != null) {
                successCount++;
            }
        } catch (Exception e) {
            errorCount++;
            log.error("Big key read error: {}", e.getMessage());
        }

        // 更新大key
        try {
            String newValue = generateRandomValue(keySizeInKB * 1024);
            cacheService.cache(bigKey, newValue);
            successCount++;
        } catch (Exception e) {
            errorCount++;
            log.error("Big key update error: {}", e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double qps = (double) successCount / duration * 1000;

        return new PerformanceResult(
                "Big Key Performance " + keySizeInKB + "KB",
                2, // 2个操作：读和写
                successCount,
                errorCount,
                duration,
                qps
        );
    }

    /**
     * 生成随机值
     */
    private String generateRandomValue(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) (random.nextInt(26) + 'a'));
        }
        return sb.toString();
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
