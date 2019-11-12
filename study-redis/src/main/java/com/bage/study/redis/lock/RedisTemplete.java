package com.bage.study.redis.lock;

import java.util.List;

public class RedisTemplete {

    public boolean exvel(String script, List<String> keys, List<String> args){
        return false;
    }

    public boolean setIfNotExist(String key, String value,int expiredTime){
        return false;
    }

}
