package com.bage.study.best.practice.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class TimerMetrics {

    @Autowired
    private MeterRegistry registry;

    private Timer myTimer;

    @PostConstruct
    public void init() {
        myTimer = registry.timer("bage_user_request_time", "timer", "bage-time");
    }


    public boolean record(long cost, TimeUnit unit) {
        myTimer.record(cost, unit);
        return true;
    }
//    public String order(long cost, TimeUnit unit) throws Exception {
//        myTimer.record(cost,unit);
//        myTimer.record(()->{
//            try {
//                int timeCost = random.nextInt(1000);
//                Thread.sleep(timeCost);
//                System.out.println("timeCost : " + timeCost);
//            } catch (InterruptedException e) {
//
//            }
//        });
//        return "cost background...";
//    }

}
