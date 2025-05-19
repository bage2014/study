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
public class RedisService {
    private static final String prefix = "redis_cache_common_prefix_";
    private static String prefixBigKey = "";
    private CacheService cacheService;

    public RedisService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public int random() {
        String data = UUID.randomUUID().toString();
        String key = "random-key-" + UUID.randomUUID().toString() + "-" + 1;
        log.info("randomKey key = {}", key);
        getService().cache(data, "-random-key-" + data + "-" + 1);
        return 1;
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
        String prefixForBigKey = getBigKeyPrefix();
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
        String key = getBigKeyPrefix() + new Random().nextInt(10000);
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

    public int initBigKey(Integer max) {
        long startTime = System.currentTimeMillis();
        String prefixForBigKey = getBigKeyPrefix();
        for (int i = 0; i < max; i++) {
            getService().cache(prefixForBigKey + i, "-init-" + UUID.randomUUID().toString() + "-");
        }
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return max;
    }
    public String getBigKey(Integer index) {
        String prefixForBigKey = getBigKeyPrefix();
        Object value = getService().get(prefixForBigKey + index);
        return value == null ? null : value.toString();
    }

    private String getBigKeyPrefix() {
        if(prefixBigKey != null && !prefixBigKey.isEmpty()){
            return prefixBigKey;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append(prefix).append("-");
        }
        prefixBigKey = sb.toString();
        return prefixBigKey;
    }


}
