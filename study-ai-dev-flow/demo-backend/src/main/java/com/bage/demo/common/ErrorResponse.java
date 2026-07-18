package com.bage.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;

    public static ErrorResponse of(int status, String message, String path) {
        return new ErrorResponse(status, message, LocalDateTime.now(), path);
    }

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now(), null);
    }
}
