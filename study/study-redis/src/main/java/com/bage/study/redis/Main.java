package com.bage.study.redis;

import redis.clients.jedis.Jedis;

public class Main {

	public static void main(String[] args) {
		
		Jedis jedis = new Jedis("192.168.19.130");
		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.out.println(value);
		System.out.println(jedis);
	}
	
}
