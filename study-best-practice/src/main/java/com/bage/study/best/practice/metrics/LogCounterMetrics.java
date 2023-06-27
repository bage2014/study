package com.bage.study.best.practice.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LogCounterMetrics {

    @Autowired
    private MeterRegistry registry;

    public boolean increment(String methodName) {
        // 统计下单次数
        registry.counter("bage_log_request_count", "counter", "bage-count", "method_name", methodName).increment();
        return true;
    }
}
