package com.bage.study.best.practice.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UserTimerMetrics {

    @Autowired
    private MeterRegistry registry;


    public boolean record(long cost, TimeUnit unit) {
        registry.timer("bage_user_request_time", "timer", "bage-time").record(cost, unit);
        return true;
    }

}
