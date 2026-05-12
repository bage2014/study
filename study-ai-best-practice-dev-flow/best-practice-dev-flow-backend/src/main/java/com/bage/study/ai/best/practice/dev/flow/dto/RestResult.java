package com.bage.study.ai.best.practice.dev.flow.dto;

import lombok.Data;

@Data
public class RestResult<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;

    public RestResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> RestResult<T> success(T data) {
        RestResult<T> result = new RestResult<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> RestResult<T> success(String message, T data) {
        RestResult<T> result = new RestResult<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> RestResult<T> error(int code, String message) {
        RestResult<T> result = new RestResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> RestResult<T> error(String message) {
        return error(500, message);
    }
}