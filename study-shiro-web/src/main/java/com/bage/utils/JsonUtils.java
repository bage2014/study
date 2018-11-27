package com.bage.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

	private static Gson gson = new GsonBuilder().create();
	
	public static String toJson(Object src) {
		return gson.toJson(src);
	}

	public static <T> T fromJson(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

	public static <T> List<T> fromJsonToList(String json, Type classOfT) {
		return gson.fromJson(json, classOfT);
	}

}
