package com.bage.study.best.practice.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SimpleMetricService {

    private MeterRegistry registry = new SimpleMeterRegistry();

    public boolean increment(String methodName, String className) {
        // 统计下单次数
        registry.counter("bage_request_count",
                        "counter", "bage-counter",
                        "class_name", className,
                        "method_name", methodName)
                .increment();
        return true;
    }

    public boolean record(long cost, TimeUnit unit, String methodName, String className) {
        registry.timer("bage_request_time",
                        "counter", "bage-counter",
                        "class_name", className,
                        "method_name", methodName)
                .record(cost, unit);
        return true;
    }

}
