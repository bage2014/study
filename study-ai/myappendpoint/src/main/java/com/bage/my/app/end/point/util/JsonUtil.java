package com.bage.my.app.end.point.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bage.my.app.end.point.entity.LocalDateAdapter;
import java.time.LocalDate;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}