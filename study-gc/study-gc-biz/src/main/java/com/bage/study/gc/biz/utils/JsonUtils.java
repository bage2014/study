package com.bage.study.gc.biz.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonUtils {

    private static Gson gson;
    private static JsonParser parser;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, TypeAdapter.localDateTimeJsonSerializer)
                .registerTypeAdapter(LocalDateTime.class, TypeAdapter.localDateTimeJsonDeserializer)
                .registerTypeAdapter(LocalDate.class, TypeAdapter.localDateJsonSerializer)
                .registerTypeAdapter(LocalDate.class, TypeAdapter.localDateJsonDeserializer)
                .serializeNulls().create();
        parser = new JsonParser();
    }

    /**
     * 将一个对象转换成JSON字符串返回
     *
     * @param obj
     * @return jsonString
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 解析一个 key
     *
     * @param json
     * @param key
     * @return
     */
    public static String getAsString(String json, String key) {
        JsonObject parse = parser.parse(json).getAsJsonObject();
        return parse.get(key).getAsString();
    }

    /**
     * 将JSON字符串转换成一个对象返回
     *
     * @param json
     * @param classOfT
     * @return javaBean
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * 解析泛型 <br/>
     * <code>
     * List&lt;User&gt; result = JsonUtils.fromJson(json, new TypeToken&lt;List&lt;User&gt;&gt;() {}.getType()); <br/>
     * Map&lt;String,User&gt; result = JsonUtils.fromJson(json, new TypeToken&lt;HashMap&lt;String,User&gt;&gt;() {}.getType()); <br/>
     * List&lt;RestResponse&gt; result = JsonUtils.fromJson(json, new TypeToken&lt;RestResponse&lt;User&gt;&gt;() {}.getType()); <br/>
     * </code>
     *
     * @param json
     * @param typeOfT
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}

