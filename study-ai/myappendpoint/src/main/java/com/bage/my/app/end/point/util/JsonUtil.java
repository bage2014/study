package com.bage.my.app.end.point.util;

import com.bage.my.app.end.point.config.LocalDateAdapter;
import com.bage.my.app.end.point.config.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    // 添加这个方法以便外部获取 Gson 实例
    public static Gson getGson() {
        return gson;
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}