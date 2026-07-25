package com.bage.demo.exception;

import lombok.Getter;

/**
 * 自定义业务异常类
 * 用于在业务逻辑处理中抛出可识别的业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 构造业务异常
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造业务异常
     *
     * @param code    错误码
     * @param message 错误消息
     * @param cause   原始异常
     */
    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造业务异常（默认错误码为 BIZ_ERROR）
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = "BIZ_ERROR";
        this.message = message;
    }

    /**
     * 构造业务异常（默认错误码为 BIZ_ERROR）
     *
     * @param message 错误消息
     * @param cause   原始异常
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = "BIZ_ERROR";
        this.message = message;
    }
}