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
        myTimer = registry.timer("requests_timer", "timer","bage-time");
    }


    @RequestMapping("/cost")
    public String order() throws Exception {

        myTimer.record(()->{
            try {
                int timeCost = random.nextInt(10000);
                Thread.sleep(timeCost);
                System.out.println("timeCost : " + timeCost);
            } catch (InterruptedException e) {

            }
        });
        return "cost background...";
    }

}
