package com.bage.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/counter")
public class CounterController {

    @Autowired
    private MeterRegistry registry;

    private Counter myCounter;
    private Counter counter;

    @PostConstruct
    public void init() {
        myCounter = registry.counter("bage_counter", "counter","bage-count");
        counter = Metrics.counter("bage_counter_2222", "counter", "bage-count");

    }


    @RequestMapping("/incr")
    public String order(@RequestParam(defaultValue = "0") String flag) throws Exception {
        // 统计下单次数
        myCounter.increment();


        counter.increment();
        return "flag: " + flag;
    }

}
