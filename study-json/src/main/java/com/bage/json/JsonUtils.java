package com.bage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class JsonUtils {

    private static Gson gson = new GsonBuilder()
            .setExclusionStrategies(new MyExclusionStrategy(String.class))
            .serializeNulls()
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json,typeOfT);
    }

    public static JsonObject parse(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }
}
