package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.trial.redis.BasicRedisService;
import com.bage.study.best.practice.trial.redis.BigKeyRedisService;
import com.bage.study.best.practice.trial.redis.BigValueRedisService;
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
    private BasicRedisService basicRedisService;
    @Autowired
    private BigKeyRedisService bigKeyRedisService;
    @Autowired
    private BigValueRedisService bigValueRedisService;

    @RequestMapping("/random/init")
    public Object init(@RequestParam(value = "max", required = false) Integer max) {
        metricService.increment("init", "RedisController");
        log.info("RedisController init max = {}", max);
        long start = System.currentTimeMillis();
        int random = basicRedisService.init(max);
        long end = System.currentTimeMillis();
        log.info("RedisController init cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "init", "RedisController");

        return new RestResult(200, random);
    }


    @RequestMapping("/random/set")
    public Object setRandom() {
        metricService.increment("setRandom", "RedisController");

        long start = System.currentTimeMillis();
        int random = basicRedisService.random();
        long end = System.currentTimeMillis();
        log.info("RedisController random cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "setRandom", "RedisController");

        return new RestResult(200, random);
    }


    @RequestMapping("/random/get")
    public Object getRandom() {
        metricService.increment("getRandom", "RedisController");

        long start = System.currentTimeMillis();
        String random = basicRedisService.get();
        long end = System.currentTimeMillis();
        log.info("RedisController random cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "getRandom", "RedisController");

        return new RestResult(200, random);
    }

    @RequestMapping("/get/key")
    public Object getKey(@RequestParam(value = "key") String key) {
        metricService.increment("getRandom", "RedisController");

        long start = System.currentTimeMillis();
        String random = basicRedisService.get(key);
        long end = System.currentTimeMillis();
        log.info("RedisController random cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "getRandom", "RedisController");

        return new RestResult(200, random);
    }


    @RequestMapping("/big/key/init")
    public Object initBigKey(@RequestParam(value = "max", required = false) Integer max,
                             @RequestParam(value = "prefixLoop", required = false) Integer prefixLoop) {
        metricService.increment("initBigKey", "RedisController");
        log.info("RedisController initBigKey max = {}", max);
        long start = System.currentTimeMillis();
        int random = bigKeyRedisService.initBigKey(max,prefixLoop);
        long end = System.currentTimeMillis();
        log.info("RedisController initBigKey cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "initBigKey", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/key/index/set")
    public Object indexSet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("indexSet", "RedisController");
        log.info("RedisController indexSet index = {}", index);
        long start = System.currentTimeMillis();
        String random = bigKeyRedisService.setBigKey(index);
        long end = System.currentTimeMillis();
        log.info("RedisController indexSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "indexSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/key/random/set")
    public Object randomSet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("randomSet", "RedisController");
        log.info("RedisController randomSet key = {}", key);
        long start = System.currentTimeMillis();
        String random = bigKeyRedisService.setBigKeyRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController randomSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "randomSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/key/random/get")
    public Object getBigKeyRandom(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("getBigKeyRandom", "RedisController");
        log.info("RedisController getBigKeyRandom count2 = {}", index);
        final Integer indexx = index;
        metricService.recordTime(() -> {
            long start = System.currentTimeMillis();
            bigKeyRedisService.getBigKey(indexx);
            long end = System.currentTimeMillis();
            log.info("RedisController getBigKeyRandom async cost = {}", (end - start));

        },"getBigKeyRandom", "RedisController");
        return new RestResult(200, "async");
    }


    @RequestMapping("/big/key/get")
    public Object getBigKey(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("getBigKey", "RedisController");
        log.info("RedisController getBigKey count2 = {}", key);
        metricService.recordTime(() -> {
            long start = System.currentTimeMillis();
            bigKeyRedisService.getBigKey(key);
            long end = System.currentTimeMillis();
            log.info("RedisController getBigKey async cost = {}", (end - start));

        },"getBigKey", "RedisController");
        return new RestResult(200, "async");
    }

    @RequestMapping("/big/value/init")
    public Object initBigValue(@RequestParam(value = "max", required = false) Integer max,
                             @RequestParam(value = "prefixLoop", required = false) Integer prefixLoop) {
        metricService.increment("initBigValue", "RedisController");
        log.info("RedisController initBigValue max = {}", max);
        long start = System.currentTimeMillis();
        int random = bigValueRedisService.initBigValue(max,prefixLoop);
        long end = System.currentTimeMillis();
        log.info("RedisController initBigValue cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "initBigValue", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/value/index/set")
    public Object indexSetValue(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("indexSetValue", "RedisController");
        log.info("RedisController indexSetValue index = {}", index);
        long start = System.currentTimeMillis();
        String random = bigValueRedisService.setBigValue(index);
        long end = System.currentTimeMillis();
        log.info("RedisController indexSetValue cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "indexSetValue", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/value/random/set")
    public Object randomSetValue(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("randomSetValue", "RedisController");
        log.info("RedisController randomSetValue key = {}", key);
        long start = System.currentTimeMillis();
        String random = bigValueRedisService.setBigValueRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController randomSetValue cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "randomSetValue", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/value/random/get")
    public Object getBigValueRandom(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("getBigValueRandom", "RedisController");
        log.info("RedisController getBigValueRandom count2 = {}", index);
        final Integer indexx = index;
        metricService.recordTime(() -> {
            long start = System.currentTimeMillis();
            bigValueRedisService.getBigValue(indexx);
            long end = System.currentTimeMillis();
            log.info("RedisController getBigValueRandom async cost = {}", (end - start));

        },"getBigValueRandom", "RedisController");
        return new RestResult(200, "async");
    }
    @RequestMapping("/big/value/get")
    public Object getBigValue(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("getBigValue", "RedisController");
        log.info("RedisController getBigValue key = {}", key);
        metricService.recordTime(() -> {
            long start = System.currentTimeMillis();
            bigValueRedisService.getBigValue(key);
            long end = System.currentTimeMillis();
            log.info("RedisController getBigValue async cost = {}", (end - start));

        },"getBigValue", "RedisController");
        return new RestResult(200, "async");
    }

//    @RequestMapping("/big/number/set")
//    public Object bigNumber(@RequestParam(value = "count", required = false) Integer count) {
//        metricService.increment("bigNumber", "RedisController");
//
//        long start = System.currentTimeMillis();
//        int random = redisService.bigNumber(count);
//        long end = System.currentTimeMillis();
//        log.info("RedisController bigNumber cost = {}", (end - start));
//        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigNumber", "RedisController");
//
//        return new RestResult(200, random);
//    }
//
//    @RequestMapping("/big/key/set")
//    public Object bigKey(@RequestParam(value = "count", required = false) Integer count) {
//        metricService.increment("bigKey", "RedisController");
//
//        long start = System.currentTimeMillis();
//        int random = redisService.bigKey(count);
//        long end = System.currentTimeMillis();
//        log.info("RedisController bigKey cost = {}", (end - start));
//        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKey", "RedisController");
//
//        return new RestResult(200, random);
//    }
//
//
//    @RequestMapping("/big/key/get")
//    public Object getBigKey(@RequestParam(value = "key", required = false) String key) {
//        metricService.increment("getBigKey", "RedisController");
//
//        long start = System.currentTimeMillis();
//        String cache = redisService.get(key);
//        long end = System.currentTimeMillis();
//        log.info("RedisController getBigKey cost = {}", (end - start));
//        metricService.record((end - start), TimeUnit.MILLISECONDS, "getBigKey", "RedisController");
//
//        return new RestResult(200, cache);
//    }
//
//    @RequestMapping("/big/value/set")
//    public Object bigValue(@RequestParam(value = "count", required = false) Integer count) {
//        metricService.increment("bigValue", "RedisController");
//
//        long start = System.currentTimeMillis();
//        int random = redisService.bigValue(count);
//        long end = System.currentTimeMillis();
//        log.info("RedisController bigValue cost = {}", (end - start));
//        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValue", "RedisController");
//
//        return new RestResult(200, random);
//    }
//
//    @RequestMapping("/big/value/get")
//    public Object getBigValue(@RequestParam(value = "key", required = false) String key) {
//        metricService.increment("getBigValue", "RedisController");
//
//        long start = System.currentTimeMillis();
//        String cache = redisService.get(key);
//        long end = System.currentTimeMillis();
//        log.info("RedisController getBigKey cost = {}", (end - start));
//        metricService.record((end - start), TimeUnit.MILLISECONDS, "getBigValue", "RedisController");
//
//        return new RestResult(200, cache);
//    }


}
