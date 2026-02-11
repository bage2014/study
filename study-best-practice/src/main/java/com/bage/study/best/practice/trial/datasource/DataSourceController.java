package com.bage.study.best.practice.trial.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接池监控控制器
 */
@RequestMapping("/data/source")
@RestController
@Slf4j
public class DataSourceController {

    @Autowired
    private DataSource dataSource;

    /**
     * 获取连接池基本信息
     */
    @RequestMapping("/info")
    public Object getDataSourceInfo() {
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            Map<String, Object> info = new HashMap<>();
            
            // 基本配置信息
            info.put("url", druidDataSource.getUrl());
            info.put("username", druidDataSource.getUsername());
            info.put("driverClassName", druidDataSource.getDriverClassName());
            
            // 连接池配置
            info.put("initialSize", druidDataSource.getInitialSize());
            info.put("maxActive", druidDataSource.getMaxActive());
            info.put("minIdle", druidDataSource.getMinIdle());
            info.put("maxWait", druidDataSource.getMaxWait());
            info.put("timeBetweenEvictionRunsMillis", druidDataSource.getTimeBetweenEvictionRunsMillis());
            info.put("minEvictableIdleTimeMillis", druidDataSource.getMinEvictableIdleTimeMillis());
            info.put("maxEvictableIdleTimeMillis", druidDataSource.getMaxEvictableIdleTimeMillis());
            
            // 连接池状态
            info.put("activeCount", druidDataSource.getActiveCount());
            info.put("poolingCount", druidDataSource.getPoolingCount());
            info.put("createCount", druidDataSource.getCreateCount());
            info.put("destroyCount", druidDataSource.getDestroyCount());
            info.put("closeCount", druidDataSource.getCloseCount());
            info.put("connectCount", druidDataSource.getConnectCount());
            info.put("waitThreadCount", druidDataSource.getWaitThreadCount());
            
            // 错误统计
            info.put("errorCount", druidDataSource.getErrorCount());
            info.put("commitCount", druidDataSource.getCommitCount());
            info.put("rollbackCount", druidDataSource.getRollbackCount());
            
            // 性能统计
//            info.put("queryTimeoutCount", druidDataSource.getQueryTimeoutCount());
//            info.put("transactionQueryTimeoutCount", druidDataSource.getTransactionQueryTimeoutCount());
            info.put("removeAbandonedCount", druidDataSource.getRemoveAbandonedCount());
            
            log.info("获取连接池信息: {}", info);
            return info;
        } else {
            return "当前数据源不是DruidDataSource";
        }
    }

    /**
     * 获取连接池状态指标
     */
    @RequestMapping("/status")
    public Object getDataSourceStatus() {
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            Map<String, Object> status = new HashMap<>();
            
            // 关键状态指标
            status.put("activeCount", druidDataSource.getActiveCount());
            status.put("poolingCount", druidDataSource.getPoolingCount());
            status.put("maxActive", druidDataSource.getMaxActive());
            status.put("minIdle", druidDataSource.getMinIdle());
            status.put("waitThreadCount", druidDataSource.getWaitThreadCount());
            status.put("errorCount", druidDataSource.getErrorCount());
            
            // 计算使用率
            int maxActive = druidDataSource.getMaxActive();
            int activeCount = druidDataSource.getActiveCount();
            double usageRate = maxActive > 0 ? (double) activeCount / maxActive * 100 : 0;
            status.put("usageRate", String.format("%.2f%%", usageRate));
            
            // 连接池健康状态
            boolean isHealthy = druidDataSource.isEnable() && 
                               druidDataSource.getActiveCount() < druidDataSource.getMaxActive() && 
                               druidDataSource.getErrorCount() == 0;
            status.put("isHealthy", isHealthy);
            
            log.info("获取连接池状态: {}", status);
            return status;
        } else {
            return "当前数据源不是DruidDataSource";
        }
    }

    /**
     * 测试连接获取
     */
    @RequestMapping("/get/connection")
    public Object getConnection() {
        long start = System.currentTimeMillis();
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            long end = System.currentTimeMillis();
            String result = "获取连接成功，耗时: " + (end - start) + "ms, 连接: " + conn;
            log.info(result);
            return result;
        } catch (SQLException e) {
            log.error("获取连接失败: {}", e.getMessage());
            return "获取连接失败: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("关闭连接失败: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 并发测试连接获取
     */
    @RequestMapping("/test/concurrency")
    public Object testConcurrency(@RequestParam("threadCount") int threadCount, 
                                 @RequestParam("iterations") int iterations) {
        log.info("开始并发测试，线程数: {}, 每个线程迭代次数: {}", threadCount, iterations);
        
        long startTime = System.currentTimeMillis();
        final int[] successCount = {0};
        final int[] failureCount = {0};
        final long[] totalTime = {0};
        
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterations; j++) {
                    long start = System.currentTimeMillis();
                    Connection conn = null;
                    try {
                        conn = dataSource.getConnection();
                        successCount[0]++;
                        totalTime[0] += (System.currentTimeMillis() - start);
                    } catch (SQLException e) {
                        failureCount[0]++;
                        log.error("并发测试获取连接失败: {}", e.getMessage());
                    } finally {
                        if (conn != null) {
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                log.error("关闭连接失败: {}", e.getMessage());
                            }
                        }
                    }
                }
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error("线程等待中断: {}", e.getMessage());
            }
        }
        
        long endTime = System.currentTimeMillis();
        double avgTime = successCount[0] > 0 ? (double) totalTime[0] / successCount[0] : 0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("threadCount", threadCount);
        result.put("iterations", iterations);
        result.put("totalRequests", threadCount * iterations);
        result.put("successCount", successCount[0]);
        result.put("failureCount", failureCount[0]);
        result.put("totalTime", endTime - startTime);
        result.put("averageTimePerRequest", String.format("%.2fms", avgTime));
        result.put("successRate", String.format("%.2f%%", (double) successCount[0] / (threadCount * iterations) * 100));
        
        log.info("并发测试完成: {}", result);
        return result;
    }

    /**
     * 测试连接池极限
     */
    @RequestMapping("/test/limit")
    public Object testLimit(@RequestParam("connectionCount") int connectionCount) {
        log.info("开始测试连接池极限，尝试获取连接数: {}", connectionCount);
        
        long startTime = System.currentTimeMillis();
        Connection[] connections = new Connection[connectionCount];
        int successCount = 0;
        int failureCount = 0;
        
        for (int i = 0; i < connectionCount; i++) {
            try {
                connections[i] = dataSource.getConnection();
                successCount++;
                log.info("获取第{}个连接成功", i + 1);
            } catch (SQLException e) {
                failureCount++;
                log.error("获取第{}个连接失败: {}", i + 1, e.getMessage());
                break;
            }
        }
        
        // 关闭所有获取的连接
        for (int i = 0; i < successCount; i++) {
            if (connections[i] != null) {
                try {
                    connections[i].close();
                } catch (SQLException e) {
                    log.error("关闭连接失败: {}", e.getMessage());
                }
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        Map<String, Object> result = new HashMap<>();
        result.put("requestedConnections", connectionCount);
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("totalTime", endTime - startTime);
        result.put("status", failureCount > 0 ? "达到连接池极限" : "未达到连接池极限");
        
        log.info("连接池极限测试完成: {}", result);
        return result;
    }

    /**
     * 重置连接池状态
     */
    @RequestMapping("/reset")
    public Object resetDataSource() {
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            try {
                druidDataSource.resetStat();
                log.info("重置连接池状态成功");
                return "重置连接池状态成功";
            } catch (Exception e) {
                log.error("重置连接池状态失败: {}", e.getMessage());
                return "重置连接池状态失败: " + e.getMessage();
            }
        } else {
            return "当前数据源不是DruidDataSource";
        }
    }

    /**
     * 使用DataSourceMonitor监控连接池性能
     */
    @RequestMapping("/monitor")
    public Object monitorDataSource(@RequestParam("testCount") int testCount) {
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            DataSourceMonitor monitor = new DataSourceMonitor(druidDataSource);
            
            monitor.start();
            
            // 执行连接获取测试
            for (int i = 0; i < testCount; i++) {
                monitor.testConnectionAcquisition();
                
                // 短暂休眠，模拟实际使用场景
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // 停止监控并获取结果
            DataSourceMonitor.DataSourceMonitorResult result = monitor.stop();
            
            log.info("连接池监控结果: {}", result);
            return result;
        } else {
            return "当前数据源不是DruidDataSource";
        }
    }

    /**
     * 批量测试连接池性能
     */
    @RequestMapping("/batch/test")
    public Object batchTestDataSource(@RequestParam("batchSize") int batchSize) {
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            DataSourceMonitor monitor = new DataSourceMonitor(druidDataSource);
            
            monitor.start();
            
            // 执行批量测试
            monitor.batchTest(batchSize);
            
            // 停止监控并获取结果
            DataSourceMonitor.DataSourceMonitorResult result = monitor.stop();
            
            log.info("连接池批量测试结果: {}", result);
            return result;
        } else {
            return "当前数据源不是DruidDataSource";
        }
    }
} 
