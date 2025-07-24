package com.bage.my.app.end.point.entity;

import java.time.LocalDateTime;

// 内部类用于存储验证码信息
public class CaptchaInfo {
    private String captcha;
    private LocalDateTime expireTime;

    public CaptchaInfo(String captcha, LocalDateTime expireTime) {
        this.captcha = captcha;
        this.expireTime = expireTime;
    }

    public String getCaptcha() {
        return captcha;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
