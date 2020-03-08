package com.bage.util;

import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class JsonUtils {

    private static Gson gson = null;
    private static JsonParser parser = new JsonParser();

    static {
        gson = new GsonBuilder().addSerializationExclusionStrategy(new MyExclusionStrategy()).create();
    }
    static class MyExclusionStrategy implements ExclusionStrategy {

        public MyExclusionStrategy() {
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            if (f.getDeclaredClass() == Class.class) {
                return true; //过滤掉name字段
            }
            if (f.getDeclaredClass() == Field.class) {
                return true; //过滤掉name字段
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
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
     * @param json
     * @param typeOfT
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}