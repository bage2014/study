package com.study.common.exception;

public enum ErrorCode {

    SUCCESS("0000", "success"),
    SYSTEM_ERROR("9999", "system error"),
    PARAM_ERROR("0001", "parameter error"),
    NOT_FOUND("0002", "resource not found"),
    UNAUTHORIZED("0003", "unauthorized"),
    FORBIDDEN("0004", "forbidden"),
    TIMEOUT("0005", "timeout"),
    VALIDATION_ERROR("0006", "validation error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public BusinessException exception() {
        return BusinessException.of(this.code, this.message);
    }

    public BusinessException exception(Object data) {
        return BusinessException.of(this.code, this.message, data);
    }
}