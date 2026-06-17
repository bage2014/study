package com.study.common.metrics.service;

public interface MetricService {

    void incrementCount(String metricName);

    void incrementErrorCount(String metricName);

    void recordDuration(String metricName, long duration, boolean success);

    long getCount(String metricName);

    long getErrorCount(String metricName);

    double getAverageDuration(String metricName);

    long getMaxDuration(String metricName);

    long getMinDuration(String metricName);

    void reset(String metricName);
}