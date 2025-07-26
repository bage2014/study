package com.bage.my.app.end.point.config;

import com.bage.my.app.end.point.util.JsonUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import org.springframework.context.annotation.Bean;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 添加到转换器列表
        converters.clear();
        converters.add(gsonHttpMessageConverter());
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(JsonUtil.getGson());
        return converter;
    }

    // @Bean
    // public FilterRegistrationBean<AuthFilter> authFilter(AuthFilter authFilter) {
    //     FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
    //     registrationBean.setFilter(authFilter);
    //     registrationBean.addUrlPatterns("/*");
    //     // 设置不拦截的请求
    //     registrationBean.addInitParameter("excludedPaths", "/login,/captcha");
    //     registrationBean.setOrder(1);
    //     return registrationBean;
    // }

}