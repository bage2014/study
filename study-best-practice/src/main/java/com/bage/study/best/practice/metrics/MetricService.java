package com.bage.study.best.practice.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MetricService {

    @Autowired
    private MeterRegistry registry;

    public boolean increment(String urlName) {
        // 统计下单次数
        registry.counter("bage_url_count",
                        "counter", "bage-counter",
                        "url", urlName)
                .increment();
        return true;
    }

    public boolean record(long cost, TimeUnit unit, String urlName) {
        registry.timer("bage_url_time",
                        "counter", "bage-counter",
                        "url", urlName)
                .record(cost, unit);
        return true;
    }


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

    public boolean recordTime(Runnable run, String methodName, String className) {
        registry.timer("bage_request_time",
                        "counter", "bage-counter",
                        "class_name", className,
                        "method_name", methodName)
                .record(run);
        return true;
    }

}
