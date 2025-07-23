package com.bage.my.app.end.point.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

    private String requestId;
    private String captcha;
}