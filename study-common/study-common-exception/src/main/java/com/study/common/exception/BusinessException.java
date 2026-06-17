package com.study.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String code;
    private final String message;
    private final Object data;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public BusinessException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BusinessException(String message) {
        super(message);
        this.code = "ERROR";
        this.message = message;
        this.data = null;
    }

    public static BusinessException of(String code, String message) {
        return new BusinessException(code, message);
    }

    public static BusinessException of(String code, String message, Object data) {
        return new BusinessException(code, message, data);
    }

    public static BusinessException of(String message) {
        return new BusinessException(message);
    }
}