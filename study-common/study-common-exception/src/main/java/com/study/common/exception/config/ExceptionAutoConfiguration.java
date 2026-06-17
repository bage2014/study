package com.study.common.exception.config;

import com.study.common.exception.aspect.ExceptionAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class ExceptionAutoConfiguration {

    @Bean
    public ExceptionAspect exceptionAspect() {
        return new ExceptionAspect();
    }
}