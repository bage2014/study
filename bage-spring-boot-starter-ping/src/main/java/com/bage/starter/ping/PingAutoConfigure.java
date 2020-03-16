package com.bage.starter.ping;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(PingService.class)
public class PingAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(PingService.class)
    @ConditionalOnProperty(prefix = "bage.ping", value = "enabled", havingValue = "true")
    PingService pingService() {
        return new PingService();
    }

}
