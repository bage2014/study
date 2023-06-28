package com.bage.study.best.practice.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCounterMetrics {

    @Autowired
    private MeterRegistry registry;


    public boolean increment() {
        // 统计下单次数
        registry.counter("bage_user_request_count", "counter", "bage-count","method_name","insert").increment();
        return true;
    }

    public boolean increment2() {
        // 统计下单次数
        registry.counter("bage_user_request_count", "counter", "bage-count","method_name","query").increment();
        return true;
    }

}
