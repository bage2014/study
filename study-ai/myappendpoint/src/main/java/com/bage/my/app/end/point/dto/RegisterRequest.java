package com.bage.my.app.end.point.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String captcha;

    private String password;
    // 新增字段
    private String email;
}