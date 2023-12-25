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
 *  https://juejin.cn/post/7309482256808509459
 *
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

    @RequestMapping("/count/init")
    public Object init(@RequestParam(value = "count", required = false) Integer count) {
        metricService.increment("init", "RedisController");
        log.info("RedisController init count = {}", count);
        count = count == null ? 100000 : count;
        log.info("RedisController init count2 = {}", count);
        long start = System.currentTimeMillis();
        int random = redisService.init(count);
        long end = System.currentTimeMillis();
        log.info("RedisController init cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "init", "RedisController");

        return new RestResult(200, random);
    }

    @RequestMapping("/count/get")
    public Object get(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("get", "RedisController");
        log.info("RedisController get index = {}", index);
        index = index == null ? 1 : index;
        log.info("RedisController get count2 = {}", index);
        long start = System.currentTimeMillis();
        int random = redisService.get(index);
        long end = System.currentTimeMillis();
        log.info("RedisController get cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "get", "RedisController");

        return new RestResult(200, random);
    }

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

    @RequestMapping("/big/key/set")
    public Object bigKey(@RequestParam(value = "count", required = false) Integer count) {
        metricService.increment("bigKey", "RedisController");

        long start = System.currentTimeMillis();
        int random = redisService.bigKey(count);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKey cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKey", "RedisController");

        return new RestResult(200, random);
    }

    @RequestMapping("/big/value/set")
    public Object bigValue(@RequestParam(value = "count", required = false) Integer count) {
        metricService.increment("bigValue", "RedisController");

        long start = System.currentTimeMillis();
        int random = redisService.bigValue(count);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValue cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValue", "RedisController");

        return new RestResult(200, random);
    }

}
