package com.bage.my.app.end.point.config;

import com.bage.my.app.end.point.util.JsonUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建 GsonHttpMessageConverter
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        // 设置我们自定义的 Gson 实例
        converter.setGson(JsonUtil.getGson());
        // 添加到转换器列表
        converters.add(converter);
    }
}