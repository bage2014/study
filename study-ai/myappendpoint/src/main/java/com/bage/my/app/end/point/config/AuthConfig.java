package com.bage.my.app.end.point.config;

import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;

@Configuration
public class AuthConfig {
    // 不拦截的URL模式列表
    private final List<String> excludedUrlPatterns = Arrays.asList(
            "/login",
            "/captcha",
            "/register",
            "/sendEmailCaptcha",
            "/resetPassword",

            "/app/download/**",


            "/app/version/all",

            "/checkToken",
            "/refreshToken",

            "/h2-console/**",
            "/actuator/**"
    );

    public List<String> getExcludedUrlPatterns() {
        return excludedUrlPatterns;
    }
}