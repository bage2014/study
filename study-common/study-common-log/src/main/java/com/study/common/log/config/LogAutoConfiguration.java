package com.study.common.log.config;

import com.study.common.log.aspect.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class LogAutoConfiguration {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}