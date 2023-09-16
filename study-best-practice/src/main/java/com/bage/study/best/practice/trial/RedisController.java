package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.model.User;
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
@RequestMapping("/redis")
@RestController
@Slf4j
public class RedisController {

    @Autowired
    private MetricService metricService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/random/set")
    public Object random() {
        metricService.increment("random", "RedisController");

        long start = System.currentTimeMillis();
        int random = redisService.random();
        long end = System.currentTimeMillis();
        log.info("RedisController random cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "random", "RedisController");

        return new RestResult(200, random);
    }

    @RequestMapping("/big/number/set")
    public Object bigNumber(@RequestParam(value = "count", required = false) Integer count) {
        metricService.increment("bigNumber", "RedisController");

        long start = System.currentTimeMillis();
        int random = redisService.bigNumber(count);
        long end = System.currentTimeMillis();
        log.info("RedisController bigNumber cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigNumber", "RedisController");

        return new RestResult(200, random);
    }

}
