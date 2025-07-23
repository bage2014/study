package com.bage.my.app.end.point.dto;

import lombok.Data;

@Data
public class EmailCaptchaRequest {
    private String email;
    private String captcha;
    private String requestId;
    
}