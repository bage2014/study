package com.bage.study.best.practice.trial.redis;

import com.bage.study.best.practice.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
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
 *
 * https://juejin.cn/post/7298989375370166298
 *
 * https://juejin.cn/post/7303719808880590886
 *
 *
 */
@Slf4j
@Component
public class BigKeyRedisService {
    private final String prefixItem = "redis_cache_big_key_prefix_";
    private String prefixBigKey = "";
    private Integer forCount = 100;
    private Integer maxCount = 100;
    private CacheService cacheService;

    public BigKeyRedisService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    private CacheService getService() {
        return cacheService;
    }

    public int initBigKey(Integer max,Integer forCount) {
        if (max == null){
            max = 100;
        }
        if (forCount == null){
            forCount = 100;
        }
        this.forCount = forCount;
        this.maxCount = max;
        long startTime = System.currentTimeMillis();
        String prefixForBigKey = getBigKeyPrefix();
        for (int i = 0; i < max; i++) {
            getService().cache(prefixForBigKey + "_"+ i, "-init-" + UUID.randomUUID().toString() + "-");
        }
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return max;
    }


    public String getBigKey(String key) {
        Object value = getService().get(key);
        return value == null ? null : value.toString();
    }

    public String getBigKey(Integer index) {
        if(index == null){
            index = new Random().nextInt(maxCount);
        }
        String prefixForBigKey = getBigKeyPrefix();
        Object value = getService().get(prefixForBigKey + index);
        return value == null ? null : value.toString();
    }

    public String setBigKey(Integer index) {
        if(index == null){
            index = new Random().nextInt(maxCount);
        }
        String prefixForBigKey = getBigKeyPrefix();
        String key = prefixForBigKey + index;
        String value = UUID.randomUUID().toString();
        getService().cache(key, value);
        return value;
    }

    public String setBigKeyRandom(String key) {
        if(key == null){
            key = UUID.randomUUID().toString();
        }
        String prefixForBigKey = getBigKeyPrefix();
        key = prefixForBigKey + key;
        String value = UUID.randomUUID().toString();
        getService().cache(key, value);
        return value;
    }

    private String getBigKeyPrefix() {
        if(prefixBigKey != null && !prefixBigKey.isEmpty()){
            return prefixBigKey;
        }
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < forCount; j++) {
            sb.append(prefixItem).append("_");
        }
        prefixBigKey = sb.toString();
        return prefixBigKey;
    }


}
