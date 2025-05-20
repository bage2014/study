package com.bage.study.best.practice.trial.redis;

import com.bage.study.best.practice.cache.CacheService;
import com.bage.study.best.practice.cache.RedisCacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 主要包括：
 * <p>
 * 1、value值很长很大
 * 2、集合存储过多的元素
 * 3、key上亿
 * <p>
 * https://juejin.cn/post/7298989375370166298
 * <p>
 * https://juejin.cn/post/7303719808880590886
 */
@Slf4j
@Component
public class BigCollectionRedisService {
    private final String prefix = "redis_cache_big_collection_prefix_";
    private Integer collectionCount = 100;
    private Integer maxCount = 100;
    @Autowired
    private RedisCacheServiceImpl cacheService;


    private RedisCacheServiceImpl getService() {
        return cacheService;
    }

    public int initBigCollection(Integer max, Integer collectionCount) {
        if (max == null) {
            max = 100;
        }
        if (collectionCount == null) {
            collectionCount = 100;
        }
        this.collectionCount = collectionCount;
        this.maxCount = max;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            initBigCollectionItem(prefix + i, UUID.randomUUID().toString());
        }
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return max;
    }

    public int initBigCollectionItem(String key,String valuePrefix) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < collectionCount; i++) {
            getService().cacheSet(key, valuePrefix + "-init-" + i + "-");
        }
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return 1;
    }


    public String getBigCollectionRandom(String key) {
        Object value = getService().randomGet(key);
        return value == null ? null : value.toString();
    }

    public String getBigCollectionRandom(Integer index) {
        if(index == null){
            index = new Random().nextInt(maxCount);
        }
        Object value = getService().randomGet(prefix + index);
        return value == null ? null : value.toString();
    }

    public String setBigCollection(Integer index) {
        if(index == null){
            index = new Random().nextInt(maxCount);
        }
        String value = UUID.randomUUID().toString();
        getService().cacheSet(prefix + index, UUID.randomUUID().toString());
        return value;
    }

    public String setBigCollectionRandom(String key) {
        String value = UUID.randomUUID().toString();
        getService().cacheSet(key, value);
        return value;
    }


    private String getBigCollectionPrefix() {
        return prefix;
    }

}
