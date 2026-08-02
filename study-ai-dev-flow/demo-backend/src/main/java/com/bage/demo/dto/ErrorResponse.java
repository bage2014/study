package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 统一错误响应对象。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * HTTP 状态码。
     */
    private int status;

    /**
     * 错误码（业务错误码）。
     */
    private String code;

    /**
     * 错误消息。
     */
    private String message;

    /**
     * 错误详情（可选）。
     */
    private String details;

    /**
     * 错误发生时间。
     */
    private LocalDateTime timestamp;

    /**
     * 请求路径。
     */
    private String path;
}
