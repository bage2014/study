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
 * <p>
 * https://juejin.cn/post/7298989375370166298
 * <p>
 * https://juejin.cn/post/7303719808880590886
 */
@Slf4j
@Component
public class BigValueRedisService {
    private final String prefix = "redis_cache_big_value_prefix_";
    private String prefixBigValue = "";
    private Integer maxCount = 100;
    private Integer forCount = 100;
    private CacheService cacheService;

    public BigValueRedisService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    private CacheService getService() {
        return cacheService;
    }

    public int initBigValue(Integer max, Integer forCount) {
        if (max == null) {
            max = 100;
        }
        if (forCount == null) {
            forCount = 100;
        }
        this.forCount = forCount;
        this.maxCount = max;
        long startTime = System.currentTimeMillis();
        String valuePrefix = getBigValuePrefix();
        for (int i = 0; i < max; i++) {
            getService().cache(prefix + i, valuePrefix + "-init-" + UUID.randomUUID().toString() + "-");
        }
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return max;
    }


    public String getBigValue(String key) {
        Object value = getService().get(key);
        return value == null ? null : value.toString();
    }

    public String getBigValue(Integer index) {
        if(index == null){
            index = new Random().nextInt(maxCount);
        }
        Object value = getService().get(prefix + index);
        return value == null ? null : value.toString();
    }

    public String setBigValue(Integer index) {
        if(index == null){
            index = new Random().nextInt(maxCount);
        }
        String valuePrefix = getBigValuePrefix();
        String value = UUID.randomUUID().toString();
        getService().cache(prefix + index, valuePrefix + UUID.randomUUID().toString());
        return value;
    }

    public String setBigValueRandom(String key) {
        String valuePrefix = getBigValuePrefix();
        String value = UUID.randomUUID().toString();
        getService().cache(key, valuePrefix + UUID.randomUUID().toString());
        return value;
    }


    private String getBigValuePrefix() {
        if (prefixBigValue != null && !prefixBigValue.isEmpty()) {
            return prefixBigValue;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < forCount; i++) {
            sb.append(prefix).append("-");
        }
        prefixBigValue = sb.toString();
        return prefixBigValue;
    }


}
