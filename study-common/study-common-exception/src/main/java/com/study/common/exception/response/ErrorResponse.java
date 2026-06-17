package com.study.common.exception.response;

import com.study.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;
    private Object data;
    private String timestamp;

    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(DateUtil.formatDateTime(DateUtil.now()))
                .build();
    }

    public static ErrorResponse of(String code, String message, Object data) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .data(data)
                .timestamp(DateUtil.formatDateTime(DateUtil.now()))
                .build();
    }
}