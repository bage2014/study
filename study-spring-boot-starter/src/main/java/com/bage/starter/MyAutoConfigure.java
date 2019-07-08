package com.bage.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MyExitsBean.class)
//@ConditionalOnMissingBean
@EnableConfigurationProperties(MyProperties.class)
public class MyAutoConfigure {

    @Autowired
    private MyProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "my.properties", value = "enabled", havingValue = "true")
    MyExitsBean myExitsBean() {
        return new MyExitsBean(properties);
    }

}