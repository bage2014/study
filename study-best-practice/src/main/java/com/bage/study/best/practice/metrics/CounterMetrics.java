package com.bage.study.best.practice.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Component
public class CounterMetrics {

    @Autowired
    private MeterRegistry registry;
    private Counter myCounter;

    @PostConstruct
    public void init() {
        myCounter = registry.counter("bage_user_request_count", "counter","bage-count");
    }

    public boolean increment() {
        // 统计下单次数
        myCounter.increment();
        return true;
    }

}
