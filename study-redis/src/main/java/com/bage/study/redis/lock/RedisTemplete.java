package com.bage.study.redis.lock;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Objects;

public class RedisTemplete {

    private String host = "localhost";
    private int post = 8379;

    private Jedis jedis = null;

    private Jedis getJedis(){
        if(Objects.isNull(jedis)){
            Jedis jedis = new Jedis(host,post);
            jedis.auth("redis"); // 设置密码
            this.jedis = jedis;
        }
        return jedis;
    }

    public String get(String key){
        Jedis jedis = getJedis();
        return jedis.get(key);
    }

    public boolean set(String key, String value){
        Jedis jedis = getJedis();
        Object eval = jedis.set(key, value);
        return isSuccess(eval);
    }

    public boolean exvel(String script, List<String> keys, List<String> args){
        Jedis jedis = getJedis();
        Object eval = jedis.eval(script, keys, args);
        return isSuccess(eval);
    }

    /**
     * @param key
     * @param value
     * @param expiredSeconds
     * @return
     */
    public boolean setIfNotExist(String key, String value,long expiredSeconds){
        Jedis jedis = getJedis();
        Object eval = jedis.set(key, value,"NX","EX", expiredSeconds);
        return isSuccess(eval);
    }

    private boolean isSuccess(String eval) {
        return Objects.equals(eval,"ok") || Objects.equals(eval,"OK");
    }
    private boolean isSuccess(Object eval) {
        return isSuccess(String.valueOf(eval));
    }

}
