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
public class BigCollectionRedisService {
    private final String prefix = "redis_cache_big_key_prefix_";
    private String prefixBigValue = "";
    private Integer maxCount = 100;
    private CacheService cacheService;

    public BigCollectionRedisService(CacheService cacheService) {
        this.cacheService = cacheService;
    }


    /**
     * redis 的 value值很大
     *
     * @param number
     * @return
     */
    public int bigValve(int number) {
        String data = "";
        for (int j = 0; j < number; j++) {
            data += UUID.randomUUID().toString();
        }
        String prefixForBigKey = getBigValuePrefix();
        String key = "big-key-" + UUID.randomUUID().toString() + "-" + number;
        log.info("bigValve key = {}", key);
        long startTime = System.currentTimeMillis();
        getService().cache(key, "-big-value-" + data + "-" + number);
        log.info("time cost： " + (System.currentTimeMillis() - startTime));
        return 1;
    }

    /**
     * 集合很大
     *
     * @param length
     * @return
     */
    public int bigCollection(int length) {
        List<Object> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(UUID.randomUUID().toString() + "-" + i);
        }
        String key = "big-collection-" + UUID.randomUUID().toString() + "-" + length;
        log.info("bigCollection key = {}", key);
        long startTime = System.currentTimeMillis();
        getService().cache(key, list);
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return length;
    }

    /**
     * 很多的普通的key + value
     *
     * @param number
     * @return
     */
    public int bigNumber(int number) {
        for (int i = 0; i < number; i++) {
            String data = UUID.randomUUID().toString();
            String key = "big-number-" + UUID.randomUUID().toString() + "-" + number;
            if (i == 0) {
                log.info("bigNumber key = {}", key);
            }
            getService().cache(key, "-big-number-" + data + "-" + i);
        }
        return number;
    }
    public int bigValue(int length) {
        String data = "";
        for (int i = 0; i < length; i++) {
            data += UUID.randomUUID().toString();
        }
        String key = getBigValuePrefix() + new Random().nextInt(10000);
        log.info("bigValue key = {}", key);
        long startTime = System.currentTimeMillis();
        getService().cache(key, "-big-value-" + data + "-");
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return 1;
    }


    public int bigKey(int length) {
        String data = "";
        for (int i = 0; i < length; i++) {
            data += UUID.randomUUID().toString();
        }
        String key = "big-key-" + data;
        log.info("bigKey key = {}", key);
        long startTime = System.currentTimeMillis();
        getService().cache(key, "-big-key-" + UUID.randomUUID().toString() + "-");
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return 1;
    }

    private CacheService getService() {
        return cacheService;
    }

    public int initBigKey(Integer max) {
        if (max == null){
            max = 100;
        }
        maxCount = max;
        long startTime = System.currentTimeMillis();
        String prefixForBigKey = getBigValuePrefix();
        for (int i = 0; i < max; i++) {
            getService().cache(prefixForBigKey + i, "-init-" + UUID.randomUUID().toString() + "-");
        }
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return max;
    }
    public int init(Integer count) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            getService().cache(prefix + i, "-init-" + UUID.randomUUID().toString() + "-");
        }
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return 1;
    }

    public String get(Integer index) {
        Object value = getService().get(prefix + index);
        return value == null ? null : value.toString();
    }

    public String get(String key) {
        Object value = getService().get(key);
        return value == null ? null : value.toString();
    }

    public String getBigKey(Integer index) {
        String prefixForBigKey = getBigValuePrefix();
        Object value = getService().get(prefixForBigKey + index);
        return value == null ? null : value.toString();
    }

    private String getBigValuePrefix() {
        if(prefixBigValue != null && !prefixBigValue.isEmpty()){
            return prefixBigValue;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append(prefix).append("-");
        }
        prefixBigValue = sb.toString();
        return prefixBigValue;
    }


}
