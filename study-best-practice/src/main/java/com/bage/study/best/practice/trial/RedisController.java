package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.trial.redis.BasicRedisService;
import com.bage.study.best.practice.trial.redis.BigCollectionRedisService;
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
 * https://juejin.cn/post/7309482256808509459
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
    @Autowired
    private BigCollectionRedisService bigCollectionRedisService;

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
    public Object randomSet() {
        metricService.increment("randomSet", "RedisController");

        long start = System.currentTimeMillis();
        int random = basicRedisService.random();
        long end = System.currentTimeMillis();
        log.info("RedisController randomSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "randomSet", "RedisController");

        return new RestResult(200, random);
    }


    @RequestMapping("/random/get")
    public Object randomGet() {
        metricService.increment("randomGet", "RedisController");

        long start = System.currentTimeMillis();
        String result = basicRedisService.get();
        long end = System.currentTimeMillis();
        log.info("RedisController randomGet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "randomGet", "RedisController");

        return new RestResult(200, result);
    }

    @RequestMapping("/get/key")
    public Object getKey(@RequestParam(value = "key") String key) {
        metricService.increment("getKey", "RedisController");

        long start = System.currentTimeMillis();
        String result = basicRedisService.get(key);
        long end = System.currentTimeMillis();
        log.info("RedisController getKey cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "getKey", "RedisController");

        return new RestResult(200, result);
    }


    @RequestMapping("/big/key/init")
    public Object bigKeyInit(@RequestParam(value = "max", required = false) Integer max,
                             @RequestParam(value = "prefixLoop", required = false) Integer prefixLoop) {
        metricService.increment("bigKeyInit", "RedisController");
        log.info("RedisController bigKeyInit max = {}", max);
        long start = System.currentTimeMillis();
        int result = bigKeyRedisService.initBigKey(max, prefixLoop);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyInit cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyInit", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/key/random/set")
    public Object bigKeyRandomSet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigKeyRandomSet", "RedisController");
        log.info("RedisController bigKeyRandomSet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigKeyRedisService.setBigKey(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyRandomSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyRandomSet", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/key/random/get")
    public Object bigKeyRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigKeyRandomGet", "RedisController");
        log.info("RedisController bigKeyRandomGet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigKeyRedisService.getBigKey(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyRandomGet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyRandomGet", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/key/set")
    public Object bigKeySet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigKeySet", "RedisController");
        log.info("RedisController bigKeySet key = {}", key);
        long start = System.currentTimeMillis();
        String result = bigKeyRedisService.setBigKeyRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeySet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeySet", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/key/get")
    public Object bigKeyGet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigKeyGet", "RedisController");
        log.info("RedisController bigKeyGet key = {}", key);
        long start = System.currentTimeMillis();
        String result = bigKeyRedisService.getBigKey(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyGet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyGet", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/init")
    public Object bigValueInit(@RequestParam(value = "max", required = false) Integer max,
                               @RequestParam(value = "prefixLoop", required = false) Integer prefixLoop) {
        metricService.increment("bigValueInit", "RedisController");
        log.info("RedisController bigValueInit max = {}", max);
        long start = System.currentTimeMillis();
        int result = bigValueRedisService.initBigValue(max, prefixLoop);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueInit cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueInit", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/random/set")
    public Object bigValueRandomSet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigValueIndexSet", "RedisController");
        log.info("RedisController bigValueIndexSet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigValueRedisService.setBigValue(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueIndexSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueIndexSet", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/set")
    public Object bigValueSet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigValueSet", "RedisController");
        log.info("RedisController bigValueSet key = {}", key);
        long start = System.currentTimeMillis();
        String result = bigValueRedisService.setBigValueRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueSet", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/random/get")
    public Object bigValueRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigValueRandomGet", "RedisController");
        log.info("RedisController bigValueRandomGet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigValueRedisService.getBigValue(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueRandomGet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueRandomGet", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/get")
    public Object bigValueGet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigValueGet", "RedisController");
        log.info("RedisController bigValueGet key = {}", key);
        long start = System.currentTimeMillis();
        String bigValue = bigValueRedisService.getBigValue(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueGet cost = {},bigValue = {}", (end - start), bigValue);
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueGet", "RedisController");
        return new RestResult(200, bigValue);
    }


    @RequestMapping("/big/collection/init")
    public Object bigCollectionInit(@RequestParam(value = "max", required = false) Integer max,
                                    @RequestParam(value = "collectionCount", required = false) Integer collectionCount) {
        metricService.increment("bigCollectionInit", "RedisController");
        log.info("RedisController bigCollectionInit max = {},collectionCount = {}", max, collectionCount);
        long start = System.currentTimeMillis();
        int random = bigCollectionRedisService.initBigCollection(max, collectionCount);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionInit cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionInit", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/collection/random/set")
    public Object bigCollectionRandomSet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigCollectionRandomSet", "RedisController");
        log.info("RedisController bigCollectionRandomSet index = {}", index);
        long start = System.currentTimeMillis();
        String random = bigCollectionRedisService.setBigCollection(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionRandomSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionRandomSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/collection/set")
    public Object bigCollectionSet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigCollectionSet", "RedisController");
        log.info("RedisController bigCollectionSet key = {}", key);
        long start = System.currentTimeMillis();
        String random = bigCollectionRedisService.setBigCollectionRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/collection/random/get")
    public Object bigCollectionRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigCollectionRandomGet", "RedisController");
        log.info("RedisController bigCollectionRandomGet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigCollectionRedisService.getBigCollectionRandom(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionRandomGet cost = {},bigCollectionRandom = {}", (end - start), result);
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionRandomGet", "RedisController");
        return new RestResult(200, result);
    }

    @RequestMapping("/big/collection/get")
    public Object bigCollectionGet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigCollectionGet", "RedisController");
        log.info("RedisController bigCollectionGet key = {}", key);
        long start = System.currentTimeMillis();
        String result = bigCollectionRedisService.getBigCollectionRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionGet  cost = {}, bigCollection = {}", (end - start), result);
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionGet", "RedisController");
        return new RestResult(200, result);
    }

}
