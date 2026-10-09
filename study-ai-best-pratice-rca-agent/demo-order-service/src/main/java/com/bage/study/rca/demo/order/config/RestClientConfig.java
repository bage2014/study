package com.bage.study.rca.demo.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * 供应服务 HTTP 客户端配置。
 * 刻意设置较短的 read-timeout（2s）：当供应服务慢响应时，订单服务快速暴露超时，
 * 形成“上游 504 <- 订单服务 read timeout <- 供应服务慢响应”的故障链。
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient supplierRestClient(RestClient.Builder builder,
                                         @Value("${supplier.base-url}") String baseUrl) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(2000);
        return builder
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }
}
