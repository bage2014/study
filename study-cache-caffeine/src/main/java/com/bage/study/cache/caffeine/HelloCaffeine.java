package com.bage.study.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * https://juejin.cn/post/7408441793791148084
 */
public class HelloCaffeine {
    public static void main(String[] args) throws InterruptedException {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                //初始数量
                .initialCapacity(5)
                //最大条数
                .maximumSize(10)
                //expireAfterWrite和expireAfterAccess同时存在时，以expireAfterWrite为准
                //最后一次写操作后经过指定时间过期
                .expireAfterWrite(20, TimeUnit.SECONDS)
                //最后一次读或写操作后经过指定时间过期
                .expireAfterAccess(20, TimeUnit.SECONDS)
                //监听缓存被移除
                .removalListener((key, val, removalCause) -> {
                    String res = "removalListener : key = " + key;
                    res += "; val = " + val;
                    res += "; removalCause = " + removalCause;
                    System.out.println(res);
                })
                //记录命中
                .recordStats()
                .build();

        String key = "key_hello";
        cache.put(key,"value_zhangsan");
        //张三
        System.out.println(cache.getIfPresent(key));
        //存储的是默认值
        System.out.println(cache.get("key_hello2",o -> "lisi"));

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {

                cache.put(key + i,"value_zhangsan" + i);
                System.out.println(cache.get(key + i,o -> "none"));

                try {
                    Thread.sleep(1 * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Thread.sleep(30 * 1000L);

    }
}