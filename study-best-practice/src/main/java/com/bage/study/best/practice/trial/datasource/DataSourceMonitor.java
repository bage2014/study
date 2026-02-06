package com.bage.study.best.practice.trial.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 数据库连接池监控服务
 */
@Slf4j
public class DataSourceMonitor {

    private final DruidDataSource dataSource;
    private final List<ConnectionStats> connectionStats;
    private long startTime;

    public DataSourceMonitor(DruidDataSource dataSource) {
        this.dataSource = dataSource;
        this.connectionStats = new ArrayList<>();
    }

    /**
     * 开始监控
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.connectionStats.clear();
        log.info("开始监控连接池，数据源: {}", dataSource.getUrl());
    }

    /**
     * 结束监控并返回统计结果
     */
    public DataSourceMonitorResult stop() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 收集连接池统计信息
        DataSourceMonitorResult result = collectStats(duration);

        log.info("连接池监控结束，结果: {}", result);
        return result;
    }

    /**
     * 测试连接获取性能
     */
    public ConnectionStats testConnectionAcquisition() {
        long start = System.currentTimeMillis();
        Connection conn = null;
        ConnectionStats stats = new ConnectionStats();

        try {
            conn = dataSource.getConnection();
            stats.setSuccess(true);
            stats.setAcquisitionTime(System.currentTimeMillis() - start);
            stats.setConnectionId(conn.hashCode());

            // 测试连接有效性
            long validateStart = System.currentTimeMillis();
            boolean isValid = conn.isValid(1);
            stats.setValidationTime(System.currentTimeMillis() - validateStart);
            stats.setValid(isValid);

            // 记录连接信息
            stats.setPoolingCount(dataSource.getPoolingCount());
            stats.setActiveCount(dataSource.getActiveCount());
            stats.setWaitThreadCount(dataSource.getWaitThreadCount());

        } catch (SQLException e) {
            stats.setSuccess(false);
            stats.setAcquisitionTime(System.currentTimeMillis() - start);
            stats.setErrorMessage(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    long closeStart = System.currentTimeMillis();
                    conn.close();
                    stats.setCloseTime(System.currentTimeMillis() - closeStart);
                } catch (SQLException e) {
                    log.error("关闭连接失败: {}", e.getMessage());
                }
            }
        }

        connectionStats.add(stats);
        return stats;
    }

    /**
     * 批量测试连接获取性能
     */
    public List<ConnectionStats> batchTest(int count) {
        List<ConnectionStats> results = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ConnectionStats stats = testConnectionAcquisition();
            results.add(stats);

            // 短暂休眠，模拟实际使用场景
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return results;
    }

    /**
     * 收集统计信息
     */
    private DataSourceMonitorResult collectStats(long duration) {
        DataSourceMonitorResult result = new DataSourceMonitorResult();
        result.setDuration(duration);

        // 基本信息
        result.setUrl(dataSource.getUrl());
        result.setUsername(dataSource.getUsername());
        result.setDriverClassName(dataSource.getDriverClassName());

        // 配置信息
        result.setInitialSize(dataSource.getInitialSize());
        result.setMaxActive(dataSource.getMaxActive());
        result.setMinIdle(dataSource.getMinIdle());
        result.setMaxWait(dataSource.getMaxWait());

        // 状态信息
        result.setActiveCount(dataSource.getActiveCount());
        result.setPoolingCount(dataSource.getPoolingCount());
        result.setCreateCount(dataSource.getCreateCount());
        result.setDestroyCount(dataSource.getDestroyCount());
        result.setCloseCount(dataSource.getCloseCount());
        result.setErrorCount(dataSource.getErrorCount());
        result.setWaitThreadCount(dataSource.getWaitThreadCount());

        // 连接获取统计
        if (!connectionStats.isEmpty()) {
            int successCount = 0;
            long totalAcquisitionTime = 0;
            long totalValidationTime = 0;
            long totalCloseTime = 0;

            for (ConnectionStats stats : connectionStats) {
                if (stats.isSuccess()) {
                    successCount++;
                    totalAcquisitionTime += stats.getAcquisitionTime();
                    totalValidationTime += stats.getValidationTime();
                    totalCloseTime += stats.getCloseTime();
                }
            }

            result.setConnectionTestCount(connectionStats.size());
            result.setConnectionSuccessCount(successCount);
            result.setConnectionFailureCount(connectionStats.size() - successCount);

            if (successCount > 0) {
                result.setAvgAcquisitionTime((double) totalAcquisitionTime / successCount);
                result.setAvgValidationTime((double) totalValidationTime / successCount);
                result.setAvgCloseTime((double) totalCloseTime / successCount);
            }

            // 计算成功率
            result.setConnectionSuccessRate((double) successCount / connectionStats.size() * 100);
        }

        // 计算连接池使用率
        double usageRate = dataSource.getMaxActive() > 0 ? 
                (double) dataSource.getActiveCount() / dataSource.getMaxActive() * 100 : 0;
        result.setUsageRate(usageRate);

        // 评估连接池状态
        evaluatePoolStatus(result);

        return result;
    }

    /**
     * 评估连接池状态
     */
    private void evaluatePoolStatus(DataSourceMonitorResult result) {
        StringBuilder statusBuilder = new StringBuilder();
        List<String> warnings = new ArrayList<>();

        // 检查连接池使用率
        if (result.getUsageRate() > 80) {
            warnings.add("连接池使用率过高 (>80%)，可能需要增加maxActive");
        } else if (result.getUsageRate() < 10) {
            warnings.add("连接池使用率过低 (<10%)，可能存在资源浪费");
        }

        // 检查错误率
        if (result.getConnectionFailureCount() > 0) {
            double errorRate = (double) result.getConnectionFailureCount() / result.getConnectionTestCount() * 100;
            if (errorRate > 5) {
                warnings.add("连接获取错误率过高 (>5%)，可能存在连接池配置问题");
            }
        }

        // 检查连接获取时间
        if (result.getAvgAcquisitionTime() > 100) {
            warnings.add("平均连接获取时间过长 (>100ms)，可能存在连接池瓶颈");
        }

        // 检查等待线程数
        if (result.getWaitThreadCount() > 5) {
            warnings.add("等待线程数过多 (>5)，可能存在连接池争用");
        }

        // 检查错误计数
        if (result.getErrorCount() > 0) {
            warnings.add("连接池存在错误记录，可能需要检查数据库连接");
        }

        // 生成状态评估
        if (warnings.isEmpty()) {
            statusBuilder.append("连接池状态良好");
        } else {
            statusBuilder.append("连接池存在以下问题:");
            for (String warning : warnings) {
                statusBuilder.append("\n- ").append(warning);
            }
        }

        result.setStatusEvaluation(statusBuilder.toString());
        result.setWarnings(warnings);
    }

    /**
     * 连接统计信息
     */
    public static class ConnectionStats {
        private boolean success;
        private long acquisitionTime;
        private long validationTime;
        private long closeTime;
        private int connectionId;
        private boolean valid;
        private int poolingCount;
        private int activeCount;
        private int waitThreadCount;
        private String errorMessage;

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public long getAcquisitionTime() { return acquisitionTime; }
        public void setAcquisitionTime(long acquisitionTime) { this.acquisitionTime = acquisitionTime; }
        public long getValidationTime() { return validationTime; }
        public void setValidationTime(long validationTime) { this.validationTime = validationTime; }
        public long getCloseTime() { return closeTime; }
        public void setCloseTime(long closeTime) { this.closeTime = closeTime; }
        public int getConnectionId() { return connectionId; }
        public void setConnectionId(int connectionId) { this.connectionId = connectionId; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public int getPoolingCount() { return poolingCount; }
        public void setPoolingCount(int poolingCount) { this.poolingCount = poolingCount; }
        public int getActiveCount() { return activeCount; }
        public void setActiveCount(int activeCount) { this.activeCount = activeCount; }
        public int getWaitThreadCount() { return waitThreadCount; }
        public void setWaitThreadCount(int waitThreadCount) { this.waitThreadCount = waitThreadCount; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

        @Override
        public String toString() {
            return "ConnectionStats{" +
                    "success=" + success +
                    ", acquisitionTime=" + acquisitionTime + "ms" +
                    ", validationTime=" + validationTime + "ms" +
                    ", closeTime=" + closeTime + "ms" +
                    ", connectionId=" + connectionId +
                    ", valid=" + valid +
                    ", poolingCount=" + poolingCount +
                    ", activeCount=" + activeCount +
                    ", waitThreadCount=" + waitThreadCount +
                    ", errorMessage='" + errorMessage + '\'' +
                    "}";
        }
    }

    /**
     * 连接池监控结果
     */
    public static class DataSourceMonitorResult {
        private String url;
        private String username;
        private String driverClassName;
        private int initialSize;
        private int maxActive;
        private int minIdle;
        private long maxWait;
        private int activeCount;
        private int poolingCount;
        private long createCount;
        private long destroyCount;
        private long closeCount;
        private long errorCount;
        private int waitThreadCount;
        private long duration;
        private int connectionTestCount;
        private int connectionSuccessCount;
        private int connectionFailureCount;
        private double connectionSuccessRate;
        private double avgAcquisitionTime;
        private double avgValidationTime;
        private double avgCloseTime;
        private double usageRate;
        private String statusEvaluation;
        private List<String> warnings;

        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getDriverClassName() { return driverClassName; }
        public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }
        public int getInitialSize() { return initialSize; }
        public void setInitialSize(int initialSize) { this.initialSize = initialSize; }
        public int getMaxActive() { return maxActive; }
        public void setMaxActive(int maxActive) { this.maxActive = maxActive; }
        public int getMinIdle() { return minIdle; }
        public void setMinIdle(int minIdle) { this.minIdle = minIdle; }
        public long getMaxWait() { return maxWait; }
        public void setMaxWait(long maxWait) { this.maxWait = maxWait; }
        public int getActiveCount() { return activeCount; }
        public void setActiveCount(int activeCount) { this.activeCount = activeCount; }
        public int getPoolingCount() { return poolingCount; }
        public void setPoolingCount(int poolingCount) { this.poolingCount = poolingCount; }
        public long getCreateCount() { return createCount; }
        public void setCreateCount(long createCount) { this.createCount = createCount; }
        public long getDestroyCount() { return destroyCount; }
        public void setDestroyCount(long destroyCount) { this.destroyCount = destroyCount; }
        public long getCloseCount() { return closeCount; }
        public void setCloseCount(long closeCount) { this.closeCount = closeCount; }
        public long getErrorCount() { return errorCount; }
        public void setErrorCount(long errorCount) { this.errorCount = errorCount; }
        public int getWaitThreadCount() { return waitThreadCount; }
        public void setWaitThreadCount(int waitThreadCount) { this.waitThreadCount = waitThreadCount; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public int getConnectionTestCount() { return connectionTestCount; }
        public void setConnectionTestCount(int connectionTestCount) { this.connectionTestCount = connectionTestCount; }
        public int getConnectionSuccessCount() { return connectionSuccessCount; }
        public void setConnectionSuccessCount(int connectionSuccessCount) { this.connectionSuccessCount = connectionSuccessCount; }
        public int getConnectionFailureCount() { return connectionFailureCount; }
        public void setConnectionFailureCount(int connectionFailureCount) { this.connectionFailureCount = connectionFailureCount; }
        public double getConnectionSuccessRate() { return connectionSuccessRate; }
        public void setConnectionSuccessRate(double connectionSuccessRate) { this.connectionSuccessRate = connectionSuccessRate; }
        public double getAvgAcquisitionTime() { return avgAcquisitionTime; }
        public void setAvgAcquisitionTime(double avgAcquisitionTime) { this.avgAcquisitionTime = avgAcquisitionTime; }
        public double getAvgValidationTime() { return avgValidationTime; }
        public void setAvgValidationTime(double avgValidationTime) { this.avgValidationTime = avgValidationTime; }
        public double getAvgCloseTime() { return avgCloseTime; }
        public void setAvgCloseTime(double avgCloseTime) { this.avgCloseTime = avgCloseTime; }
        public double getUsageRate() { return usageRate; }
        public void setUsageRate(double usageRate) { this.usageRate = usageRate; }
        public String getStatusEvaluation() { return statusEvaluation; }
        public void setStatusEvaluation(String statusEvaluation) { this.statusEvaluation = statusEvaluation; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }

        @Override
        public String toString() {
            return "DataSourceMonitorResult{" +
                    "url='" + url + '\'' +
                    ", username='" + username + '\'' +
                    ", driverClassName='" + driverClassName + '\'' +
                    ", initialSize=" + initialSize +
                    ", maxActive=" + maxActive +
                    ", minIdle=" + minIdle +
                    ", maxWait=" + maxWait +
                    ", activeCount=" + activeCount +
                    ", poolingCount=" + poolingCount +
                    ", errorCount=" + errorCount +
                    ", waitThreadCount=" + waitThreadCount +
                    ", duration=" + duration + "ms" +
                    ", connectionTestCount=" + connectionTestCount +
                    ", connectionSuccessCount=" + connectionSuccessCount +
                    ", connectionFailureCount=" + connectionFailureCount +
                    ", connectionSuccessRate=" + String.format("%.2f%%", connectionSuccessRate) +
                    ", avgAcquisitionTime=" + String.format("%.2fms", avgAcquisitionTime) +
                    ", usageRate=" + String.format("%.2f%%", usageRate) +
                    ", statusEvaluation='" + statusEvaluation + '\'' +
                    "}";
        }
    }
}
