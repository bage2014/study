package com.bage.study.redis;

import redis.clients.jedis.Jedis;

public class Main {

	public static void main(String[] args) {
		
		Jedis jedis = new Jedis("localhost");
		jedis = new Jedis("192.168.19.132");

		jedis.auth("bage"); // 设置密码

		System.out.println(jedis);

		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.out.println(value);
}
