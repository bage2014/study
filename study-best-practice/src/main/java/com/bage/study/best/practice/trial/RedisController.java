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
        String random = basicRedisService.get();
        long end = System.currentTimeMillis();
        log.info("RedisController randomGet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "randomGet", "RedisController");

        return new RestResult(200, random);
    }

    @RequestMapping("/get/key")
    public Object getKey(@RequestParam(value = "key") String key) {
        metricService.increment("getKey", "RedisController");

        long start = System.currentTimeMillis();
        String random = basicRedisService.get(key);
        long end = System.currentTimeMillis();
        log.info("RedisController getKey cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "getKey", "RedisController");

        return new RestResult(200, random);
    }


    @RequestMapping("/big/key/init")
    public Object bigKeyInit(@RequestParam(value = "max", required = false) Integer max,
                             @RequestParam(value = "prefixLoop", required = false) Integer prefixLoop) {
        metricService.increment("bigKeyInit", "RedisController");
        log.info("RedisController bigKeyInit max = {}", max);
        long start = System.currentTimeMillis();
        int random = bigKeyRedisService.initBigKey(max, prefixLoop);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyInit cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyInit", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/key/index/set")
    public Object bigKeyIndexSet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigKeyIndexSet", "RedisController");
        log.info("RedisController bigKeyIndexSet index = {}", index);
        long start = System.currentTimeMillis();
        String random = bigKeyRedisService.setBigKey(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyIndexSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyIndexSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/key/random/set")
    public Object bigKeyRandomSet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigKeyRandomSet", "RedisController");
        log.info("RedisController bigKeyRandomSet key = {}", key);
        long start = System.currentTimeMillis();
        String random = bigKeyRedisService.setBigKeyRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyRandomSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyRandomSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/key/random/get")
    public Object bigKeyRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigKeyRandomGet", "RedisController");
        log.info("RedisController bigKeyRandomGet count2 = {}", index);
        long start = System.currentTimeMillis();
        bigKeyRedisService.getBigKey(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyRandomGet async cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyRandomGet", "RedisController");
        return new RestResult(200, "async");
    }


    @RequestMapping("/big/key/get")
    public Object bigKeyGet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigKeyGet", "RedisController");
        log.info("RedisController bigKeyGet count2 = {}", key);
        long start = System.currentTimeMillis();
        bigKeyRedisService.getBigKey(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyGet async cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigKeyGet", "RedisController");
        return new RestResult(200, "async");
    }

    @RequestMapping("/big/value/init")
    public Object bigValueInit(@RequestParam(value = "max", required = false) Integer max,
                               @RequestParam(value = "prefixLoop", required = false) Integer prefixLoop) {
        metricService.increment("bigValueInit", "RedisController");
        log.info("RedisController bigValueInit max = {}", max);
        long start = System.currentTimeMillis();
        int random = bigValueRedisService.initBigValue(max, prefixLoop);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueInit cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueInit", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/value/index/set")
    public Object bigValueIndexSet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigValueIndexSet", "RedisController");
        log.info("RedisController bigValueIndexSet index = {}", index);
        long start = System.currentTimeMillis();
        String random = bigValueRedisService.setBigValue(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueIndexSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueIndexSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/value/random/set")
    public Object bigValueRandomSet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigValueRandomSet", "RedisController");
        log.info("RedisController bigValueRandomSet key = {}", key);
        long start = System.currentTimeMillis();
        String random = bigValueRedisService.setBigValueRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueRandomSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueRandomSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/value/random/get")
    public Object bigValueRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigValueRandomGet", "RedisController");
        log.info("RedisController bigValueRandomGet count2 = {}", index);
        long start = System.currentTimeMillis();
        bigValueRedisService.getBigValue(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueRandomGet async cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigValueRandomGet", "RedisController");
        return new RestResult(200, "async");
    }

    @RequestMapping("/big/value/get")
    public Object bigValueGet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigValueGet", "RedisController");
        log.info("RedisController bigValueGet key = {}", key);
        long start = System.currentTimeMillis();
        String bigValue = bigValueRedisService.getBigValue(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueGet async cost = {},bigValue = {}", (end - start), bigValue);
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

    @RequestMapping("/big/collection/index/set")
    public Object bigCollectionIndexSet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigCollectionIndexSet", "RedisController");
        log.info("RedisController bigCollectionIndexSet index = {}", index);
        long start = System.currentTimeMillis();
        String random = bigCollectionRedisService.setBigCollection(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionIndexSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionIndexSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/collection/random/set")
    public Object bigCollectionRandomSet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigCollectionRandomSet", "RedisController");
        log.info("RedisController bigCollectionRandomSet key = {}", key);
        long start = System.currentTimeMillis();
        String random = bigCollectionRedisService.setBigCollectionRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionRandomSet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionRandomSet", "RedisController");
        return new RestResult(200, random);
    }

    @RequestMapping("/big/collection/random/get")
    public Object bigCollectionRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        metricService.increment("bigCollectionRandomGet", "RedisController");
        log.info("RedisController bigCollectionRandomGet count2 = {}", index);
        long start = System.currentTimeMillis();
        String bigCollectionRandom = bigCollectionRedisService.getBigCollectionRandom(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionRandomGet async cost = {},bigCollectionRandom = {}", (end - start), bigCollectionRandom);
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionRandomGet", "RedisController");
        return new RestResult(200, "async");
    }

    @RequestMapping("/big/collection/get")
    public Object bigCollectionGet(@RequestParam(value = "key", required = false) String key) {
        metricService.increment("bigCollectionGet", "RedisController");
        log.info("RedisController bigCollectionGet key = {}", key);
        long start = System.currentTimeMillis();
        String bigCollection = bigCollectionRedisService.getBigCollectionRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionGet async cost = {}, bigCollection = {}", (end - start), bigCollection);
        metricService.record((end - start), TimeUnit.MILLISECONDS, "bigCollectionGet", "RedisController");
        return new RestResult(200, "async");
    }

}
