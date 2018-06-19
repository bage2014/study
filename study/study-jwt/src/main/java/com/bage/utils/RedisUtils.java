package com.bage.utils;

import java.util.HashMap;
import java.util.Map;

public class RedisUtils {

	private static Map<String,Object> map = new HashMap<String,Object>();
	
	public static Object put(String key,Object value) {
		return map.put(key, value);
	}

	public static void clear() {
		map.clear();
	}

	public static Object clear(String key) {
		return map.remove(key);
	}
	
	public static Object replace(String key,Object value) {
		return map.replace(key, value);
	}

	public static Object get(String key) {
		return map.get(key);
	}
	

	public static String getString(String key) {
		return String.valueOf(map.get(key));
	}
	
	public static Object containsKey(String key) {
		return map.containsKey(key);
	}
	
	
}
