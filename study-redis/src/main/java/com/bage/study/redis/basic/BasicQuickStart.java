package com.bage.study.redis.basic;

import redis.clients.jedis.Jedis;

public class BasicQuickStart {

	public static void main(String[] args) {

		Jedis jedis = new Jedis("localhost");
		jedis = new Jedis("localhost",6379);
		jedis.auth("bage"); // 设置密码

		System.out.println(jedis);

		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.out.println(value);
		System.out.println(jedis.get("foo"));
		System.out.println(jedis.get("bar"));
	}
}
