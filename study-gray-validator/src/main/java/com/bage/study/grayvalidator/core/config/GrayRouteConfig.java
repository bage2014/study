package com.bage.study.grayvalidator.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GrayRouteConfig {

    private static final String[] GRAY_UID_WHITELIST = {"10001", "10002", "88888", "99001"};

    @Bean
    public List<GrayRule> grayRules() {
        return List.of(
            GrayRule.byQuery("uid")
                    .matchValues(GRAY_UID_WHITELIST),

            GrayRule.byQuery("orderId")
                    .matchPrefix("202406"),

            GrayRule.byQuery("orderId")
                    .matchSuffix("0000"),

            GrayRule.byQuery("orderId")
                    .matchValues(GRAY_UID_WHITELIST),

            GrayRule.byHeader("X-Gray-User")
                    .matchValues("88888")
        );
    }
}