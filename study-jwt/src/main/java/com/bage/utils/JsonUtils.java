package com.bage.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON 工具类
 * @author luruihua
 *
 */
public class JsonUtils {
 
	private static Gson gson = new GsonBuilder().create();
	
	/**
	 * 将对象转化成json
	 * @param src
	 * @return
	 */
	public static String toJson(Object src) {
		return gson.toJson(src);
	}
	
	/**
	 * 将json转化成对象
	 * @param src
	 * @return
	 */
	public static <T> T toJson(String json,Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}
	
}
