package com.study.common.metrics.service.impl;

import com.study.common.metrics.service.MetricService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class MetricServiceImpl implements MetricService {

    private static class MetricData {
        AtomicLong count = new AtomicLong(0);
        AtomicLong errorCount = new AtomicLong(0);
        AtomicLong totalDuration = new AtomicLong(0);
        AtomicLong maxDuration = new AtomicLong(0);
        AtomicLong minDuration = new AtomicLong(Long.MAX_VALUE);
    }

    private final Map<String, MetricData> metrics = new ConcurrentHashMap<>();

    @Override
    public void incrementCount(String metricName) {
        metrics.computeIfAbsent(metricName, k -> new MetricData()).count.incrementAndGet();
    }

    @Override
    public void incrementErrorCount(String metricName) {
        metrics.computeIfAbsent(metricName, k -> new MetricData()).errorCount.incrementAndGet();
    }

    @Override
    public void recordDuration(String metricName, long duration, boolean success) {
        MetricData data = metrics.computeIfAbsent(metricName, k -> new MetricData());
        data.totalDuration.addAndGet(duration);
        data.maxDuration.updateAndGet(current -> Math.max(current, duration));
        data.minDuration.updateAndGet(current -> Math.min(current, duration));
        log.debug("Metric [{}] duration: {}ms, success: {}", metricName, duration, success);
    }

    @Override
    public long getCount(String metricName) {
        MetricData data = metrics.get(metricName);
        return data != null ? data.count.get() : 0;
    }

    @Override
    public long getErrorCount(String metricName) {
        MetricData data = metrics.get(metricName);
        return data != null ? data.errorCount.get() : 0;
    }

    @Override
    public double getAverageDuration(String metricName) {
        MetricData data = metrics.get(metricName);
        if (data == null || data.count.get() == 0) {
            return 0;
        }
        return (double) data.totalDuration.get() / data.count.get();
    }

    @Override
    public long getMaxDuration(String metricName) {
        MetricData data = metrics.get(metricName);
        return data != null ? data.maxDuration.get() : 0;
    }

    @Override
    public long getMinDuration(String metricName) {
        MetricData data = metrics.get(metricName);
        if (data == null) {
            return 0;
        }
        long min = data.minDuration.get();
        return min == Long.MAX_VALUE ? 0 : min;
    }

    @Override
    public void reset(String metricName) {
        metrics.remove(metricName);
    }
}