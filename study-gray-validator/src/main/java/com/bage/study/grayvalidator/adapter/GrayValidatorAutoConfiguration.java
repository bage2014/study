package com.bage.study.grayvalidator.adapter;

import com.bage.study.grayvalidator.adapter.aop.GrayRouteAspect;
import com.bage.study.grayvalidator.adapter.embedded.GrayEmbeddedFilter;
import com.bage.study.grayvalidator.adapter.gateway.GrayProxyFilter;
import com.bage.study.grayvalidator.core.FieldResolver;
import com.bage.study.grayvalidator.core.ForwardingEngine;
import com.bage.study.grayvalidator.core.GrayFieldExtractor;
import com.bage.study.grayvalidator.core.WhitelistMatcher;
import com.bage.study.grayvalidator.core.config.GrayMode;
import com.bage.study.grayvalidator.core.config.GrayRule;
import com.bage.study.grayvalidator.core.config.GrayTargetConfig;
import com.bage.study.grayvalidator.core.config.OrderUserConfig;
import com.bage.study.grayvalidator.core.resolver.OrderToUserFieldResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

@Configuration
public class GrayValidatorAutoConfiguration {

    @Bean
    public HttpClient grayHttpClient(GrayTargetConfig config) {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(config.getForwardTimeoutMs()))
                .build();
    }

    @Bean
    public GrayFieldExtractor grayFieldExtractor(ObjectMapper objectMapper) {
        return new GrayFieldExtractor(objectMapper);
    }

    @Bean
    public WhitelistMatcher whitelistMatcher(List<GrayRule> grayRules,
                                             GrayFieldExtractor extractor,
                                             List<FieldResolver> resolvers,
                                             GrayTargetConfig config) {
        return new WhitelistMatcher(grayRules, extractor, resolvers, config.getGrayTargetUrl());
    }

    @Bean
    public ForwardingEngine forwardingEngine(HttpClient grayHttpClient) {
        return new ForwardingEngine(grayHttpClient);
    }

    @Bean
    public FilterRegistrationBean<GrayProxyFilter> grayProxyFilter(
            WhitelistMatcher matcher, ForwardingEngine engine, GrayTargetConfig config) {
        FilterRegistrationBean<GrayProxyFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new GrayProxyFilter(matcher, engine, config));
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
        reg.setEnabled(config.getMode() == GrayMode.GATEWAY);
        return reg;
    }

    @Bean
    public FilterRegistrationBean<GrayEmbeddedFilter> grayEmbeddedFilter(
            WhitelistMatcher matcher, ForwardingEngine engine, GrayTargetConfig config) {
        FilterRegistrationBean<GrayEmbeddedFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new GrayEmbeddedFilter(matcher, engine));
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
        reg.setEnabled(config.getMode() == GrayMode.EMBEDDED);
        return reg;
    }

    @Bean
    public OrderToUserFieldResolver orderToUserFieldResolver(OrderUserConfig orderUserConfig) {
        return new OrderToUserFieldResolver(orderUserConfig);
    }

    @Bean
    public GrayRouteAspect grayRouteAspect(WhitelistMatcher matcher, ForwardingEngine engine) {
        return new GrayRouteAspect(matcher, engine);
    }
}