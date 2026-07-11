package com.bage.ai.pipeline.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${pipeline.api-url:http://localhost:8080/api/pipeline}")
    private String pipelineApiUrl;

    @Bean
    public WebClient pipelineWebClient() {
        return WebClient.builder()
                .baseUrl(pipelineApiUrl)
                .build();
    }
}
