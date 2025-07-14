package com.bage.my.app.end.point.dto;

public class EmailCaptchaRequest {
    private String email;
    private String captcha;
    
    // getters and setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCaptcha() {
        return captcha;
    }
    
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}