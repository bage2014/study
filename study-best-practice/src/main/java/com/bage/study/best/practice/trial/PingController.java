package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.trial.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@RequestMapping("/ping")
@RestController
@Slf4j
public class PingController {

    @Autowired
    private MetricService metricService;

    @RequestMapping("/")
    public Object random() {
        metricService.increment("ping", "PingController");

        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        log.info("PingController ping cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "ping", "PingController");

        return new RestResult(200, "pang");
    }

}
