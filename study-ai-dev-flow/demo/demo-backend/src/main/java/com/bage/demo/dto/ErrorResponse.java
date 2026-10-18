package com.bage.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 统一错误响应 DTO。
 * <p>
 * 用于所有 REST 接口在发生错误时返回统一结构的 JSON 响应体。
 * 例如：
 * <pre>
 * {
 *   "timestamp": "2024-01-01T12:00:00",
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "title 不能为空",
 *   "path": "/api/books",
 *   "details": { "title": "must not be blank" }
 * }
 * </pre>
 *
 * @author bage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * 错误发生时间。
     */
    private LocalDateTime timestamp;

    /**
     * HTTP 状态码。
     */
    private int status;

    /**
     * HTTP 状态原因短语，例如 "Bad Request"、"Not Found"。
     */
    private String error;

    /**
     * 人类可读的错误信息。
     */
    private String message;

    /**
     * 发生错误的请求路径。
     */
    private String path;

    /**
     * 可选的详细错误信息，例如字段校验错误。
     */
    private Map<String, String> details;
}
