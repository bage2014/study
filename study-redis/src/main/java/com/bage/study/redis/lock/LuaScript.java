package com.bage.study.redis.lock;

public class LuaScript {

    public static final String LOCK_SCRIPT = "SET {resource_key} {resource_value} NX PX {resource_expire_time}";
    public static final String UNLOCK_SCRIPT = "" +
            "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
            "    return redis.call(\"del\",KEYS[1])\n" +
            "else\n" +
            "    return 0\n" +
            "end" +
            "";

}
