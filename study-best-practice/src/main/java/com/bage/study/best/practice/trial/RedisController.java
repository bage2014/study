package com.bage.study.best.practice.trial;

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

/**
 * https://juejin.cn/post/7309482256808509459
 */
@RequestMapping("/redis")
@RestController
@Slf4j
public class RedisController {

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
        log.info("RedisController init max = {}", max);
        long start = System.currentTimeMillis();
        int random = basicRedisService.init(max);
        long end = System.currentTimeMillis();
        log.info("RedisController init cost = {}", (end - start));
        return new RestResult(200, random);
    }


    @RequestMapping("/random/set")
    public Object randomSet() {
        long start = System.currentTimeMillis();
        int random = basicRedisService.random();
        long end = System.currentTimeMillis();
        log.info("RedisController randomSet cost = {}", (end - start));
        return new RestResult(200, random);
    }


    @RequestMapping("/random/get")
    public Object randomGet() {
        long start = System.currentTimeMillis();
        String result = basicRedisService.get();
        long end = System.currentTimeMillis();
        log.info("RedisController randomGet cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/get/key")
    public Object getKey(@RequestParam(value = "key") String key) {
        long start = System.currentTimeMillis();
        String result = basicRedisService.get(key);
        long end = System.currentTimeMillis();
        log.info("RedisController getKey cost = {}", (end - start));
        return new RestResult(200, result);
    }


    @RequestMapping("/big/key/init")
    public Object bigKeyInit(@RequestParam(value = "max", required = false) Integer max,
                             @RequestParam(value = "prefixLoop", required = false) Integer prefixLoop) {
        log.info("RedisController bigKeyInit max = {}", max);
        long start = System.currentTimeMillis();
        int result = bigKeyRedisService.initBigKey(max, prefixLoop);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyInit cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/key/random/set")
    public Object bigKeyRandomSet(@RequestParam(value = "index", required = false) Integer index) {
        log.info("RedisController bigKeyRandomSet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigKeyRedisService.setBigKey(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyRandomSet cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/key/random/get")
    public Object bigKeyRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        log.info("RedisController bigKeyRandomGet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigKeyRedisService.getBigKey(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyRandomGet cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/key/set")
    public Object bigKeySet(@RequestParam(value = "key", required = false) String key) {
        log.info("RedisController bigKeySet key = {}", key);
        long start = System.currentTimeMillis();
        String result = bigKeyRedisService.setBigKeyRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeySet cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/key/get")
    public Object bigKeyGet(@RequestParam(value = "key", required = false) String key) {
        log.info("RedisController bigKeyGet key = {}", key);
        long start = System.currentTimeMillis();
        String result = bigKeyRedisService.getBigKey(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigKeyGet cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/init")
    public Object bigValueInit(@RequestParam(value = "max", required = false) Integer max,
                               @RequestParam(value = "prefixLoop", required = false) Integer prefixLoop) {
        log.info("RedisController bigValueInit max = {}", max);
        long start = System.currentTimeMillis();
        int result = bigValueRedisService.initBigValue(max, prefixLoop);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueInit cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/random/set")
    public Object bigValueRandomSet(@RequestParam(value = "index", required = false) Integer index) {
        log.info("RedisController bigValueIndexSet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigValueRedisService.setBigValue(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueIndexSet cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/set")
    public Object bigValueSet(@RequestParam(value = "key", required = false) String key) {
        log.info("RedisController bigValueSet key = {}", key);
        long start = System.currentTimeMillis();
        String result = bigValueRedisService.setBigValueRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueSet cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/random/get")
    public Object bigValueRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        log.info("RedisController bigValueRandomGet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigValueRedisService.getBigValue(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueRandomGet cost = {}", (end - start));
        return new RestResult(200, result);
    }

    @RequestMapping("/big/value/get")
    public Object bigValueGet(@RequestParam(value = "key", required = false) String key) {
        log.info("RedisController bigValueGet key = {}", key);
        long start = System.currentTimeMillis();
        String bigValue = bigValueRedisService.getBigValue(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigValueGet cost = {},bigValue = {}", (end - start), bigValue);
        return new RestResult(200, bigValue);
    }


    @RequestMapping("/big/collection/init")
    public Object bigCollectionInit(@RequestParam(value = "max", required = false) Integer max,
                                    @RequestParam(value = "collectionCount", required = false) Integer collectionCount) {
        log.info("RedisController bigCollectionInit max = {},collectionCount = {}", max, collectionCount);
        long start = System.currentTimeMillis();
        int random = bigCollectionRedisService.initBigCollection(max, collectionCount);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionInit cost = {}", (end - start));
        return new RestResult(200, random);
    }

    @RequestMapping("/big/collection/random/set")
    public Object bigCollectionRandomSet(@RequestParam(value = "index", required = false) Integer index) {
        log.info("RedisController bigCollectionRandomSet index = {}", index);
        long start = System.currentTimeMillis();
        String random = bigCollectionRedisService.setBigCollection(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionRandomSet cost = {}", (end - start));
        return new RestResult(200, random);
    }

    @RequestMapping("/big/collection/set")
    public Object bigCollectionSet(@RequestParam(value = "key", required = false) String key) {
        log.info("RedisController bigCollectionSet key = {}", key);
        long start = System.currentTimeMillis();
        String random = bigCollectionRedisService.setBigCollectionRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionSet cost = {}", (end - start));
        return new RestResult(200, random);
    }

    @RequestMapping("/big/collection/random/get")
    public Object bigCollectionRandomGet(@RequestParam(value = "index", required = false) Integer index) {
        log.info("RedisController bigCollectionRandomGet index = {}", index);
        long start = System.currentTimeMillis();
        String result = bigCollectionRedisService.getBigCollectionRandom(index);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionRandomGet cost = {},bigCollectionRandom = {}", (end - start), result);
        return new RestResult(200, result);
    }

    @RequestMapping("/big/collection/get")
    public Object bigCollectionGet(@RequestParam(value = "key", required = false) String key) {
        log.info("RedisController bigCollectionGet key = {}", key);
        long start = System.currentTimeMillis();
        String result = bigCollectionRedisService.getBigCollectionRandom(key);
        long end = System.currentTimeMillis();
        log.info("RedisController bigCollectionGet  cost = {}, bigCollection = {}", (end - start), result);
        return new RestResult(200, result);
    }

}
