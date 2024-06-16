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

import java.util.Random;
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
    public Object init(@RequestParam(value = "max", required = false) Integer max) {
        metricService.increment("init", "RedisController");
        log.info("RedisController init max = {}", max);
        max = max == null ? 100000 : max;
        log.info("RedisController init max2 = {}", max);
        long start = System.currentTimeMillis();
        int random = redisService.init(max);
        long end = System.currentTimeMillis();
        log.info("RedisController init cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "init", "RedisController");

        return new RestResult(200, random);
    }
    @RequestMapping("/count/big/key/init")
    public Object initBigKey(@RequestParam(value = "max", required = false) Integer max) {
        metricService.increment("initBigKey", "RedisController");
        log.info("RedisController initBigKey max = {}", max);
        max = max == null ? 1000 : max;
        log.info("RedisController initBigKey max2 = {}", max);
        long start = System.currentTimeMillis();
        int random = redisService.initBigKey(max);
        long end = System.currentTimeMillis();
        log.info("RedisController initBigKey cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "initBigKey", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/count/big/key/random")
    public Object getBigKey(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("getBigKey", "RedisController");
        log.info("RedisController getBigKey index = {}", index);
        index = index == null ? new Random().nextInt(1000) : index;
        log.info("RedisController getBigKey count2 = {}", index);
        long start = System.currentTimeMillis();
        String random = redisService.getBigKey(index);
        long end = System.currentTimeMillis();
        log.info("RedisController getBigKey cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "getBigKey", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/count/big/key/random2")
    public Object getBigKey2(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("getBigKey2", "RedisController");
        log.info("RedisController getBigKey2 index = {}", index);
        index = index == null ? new Random().nextInt(1000) : index;
        log.info("RedisController getBigKey2 count2 = {}", index);
        final Integer indexx = index;
        metricService.recordTime(() -> {
            long start = System.currentTimeMillis();
            redisService.getBigKey(indexx);
            long end = System.currentTimeMillis();
            log.info("RedisController getBigKey2 async cost = {}", (end - start));

        },"getBigKey2", "RedisController");
        return new RestResult(200, "async");
    }

    @RequestMapping("/count/get")
    public Object get(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("get", "RedisController");
        log.info("RedisController get index = {}", index);
        index = index == null ? 1 : index;
        log.info("RedisController get count2 = {}", index);
        long start = System.currentTimeMillis();
        String random = redisService.get(index);
        long end = System.currentTimeMillis();
        log.info("RedisController get cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "get", "RedisController");

        return new RestResult(200, random);
    }

    @RequestMapping("/count/random")
    public Object getRandom(@RequestParam(value = "max", required = false) Integer max) {
        metricService.increment("getRandom", "RedisController");
        log.info("RedisController getRandom max = {}", max);
        max = max == null ? 10000 : max;
        log.info("RedisController getRandom max2 = {}", max);
        long start = System.currentTimeMillis();
        int random = new Random().nextInt(max);
        log.info("RedisController getRandom random = {}", random);
        String value = redisService.get(random);
        long end = System.currentTimeMillis();
        log.info("RedisController getRandom cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "getRandom", "RedisController");

        return new RestResult(200, value);
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


    @RequestMapping("/big/key/get")
    public Object getBigKey(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("getBigKey", "RedisController");

        long start = System.currentTimeMillis();
        String cache = redisService.get(key);
        long end = System.currentTimeMillis();
        log.info("RedisController getBigKey cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "getBigKey", "RedisController");

        return new RestResult(200, cache);
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

    @RequestMapping("/big/value/get")
    public Object getBigValue(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("getBigValue", "RedisController");

        long start = System.currentTimeMillis();
        String cache = redisService.get(key);
        long end = System.currentTimeMillis();
        log.info("RedisController getBigKey cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "getBigValue", "RedisController");

        return new RestResult(200, cache);
    }


}
