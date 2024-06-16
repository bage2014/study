package com.bage.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Random;

@RestController
@RequestMapping("/timer")
public class TimerController {

    @Autowired
    private MeterRegistry registry;

    private Timer myTimer;
    private Random random = new Random();

    @PostConstruct
    public void init() {
//        myTimer = registry.timer("bage_timer", "timer","bage-time");

        myTimer = Timer.builder("bage_timer")
                .description("a description of what this timer does") // optional
                .tags("timer", "bage-time") // optional
                .register(registry);

    }


    @RequestMapping("/cost")
    public String order() throws Exception {

        myTimer.record(()->{
            try {
                int timeCost = random.nextInt(1000);
                Thread.sleep(timeCost);
                System.out.println("timeCost : " + timeCost);
            } catch (InterruptedException e) {

            }
        });
        return "cost background";
    }

}
