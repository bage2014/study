package com.bage.study.redis.basic;

import redis.clients.jedis.Jedis;

public class DataStructTest {

    public static void main(String[] args) {

        Jedis jedis = new Jedis("localhost");
        jedis = new Jedis("localhost", 6379);
        jedis.auth("bage"); // 设置密码

        string(jedis);
        hash(jedis);
    }

    private static void hash(Jedis jedis) {
        jedis.geohash("foo", "bar");
        String value = jedis.get("foo");
        System.out.println("value:" + value);
    }

    private static void string(Jedis jedis) {
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        System.out.println("value:" + value);
    }
}
