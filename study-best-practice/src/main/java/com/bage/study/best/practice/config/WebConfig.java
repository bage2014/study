package com.bage.study.best.practice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MetricsHandlerInterceptor metricsHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加埋点拦截器，应用到所有API请求路径
        registry.addInterceptor(metricsHandlerInterceptor)
                .addPathPatterns("/**");
    }


}
