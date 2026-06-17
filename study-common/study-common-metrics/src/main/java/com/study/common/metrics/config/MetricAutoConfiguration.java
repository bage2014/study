package com.study.common.metrics.config;

import com.study.common.metrics.aspect.MetricAspect;
import com.study.common.metrics.service.MetricService;
import com.study.common.metrics.service.impl.MetricServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class MetricAutoConfiguration {

    @Bean
    public MetricService metricService() {
        return new MetricServiceImpl();
    }

    @Bean
    public MetricAspect metricAspect(MetricService metricService) {
        return new MetricAspect(metricService);
    }
}