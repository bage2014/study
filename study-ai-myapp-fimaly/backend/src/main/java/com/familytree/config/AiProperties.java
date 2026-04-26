package com.familytree.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiProperties {

    private boolean enabled = true;
    private Map<String, ProviderConfig> providers = new HashMap<>();
    private int timeoutMs = 30000;
    private int maxRetries = 3;
    private RetryConfig retry = new RetryConfig();
    private CircuitBreakerConfig circuitBreaker = new CircuitBreakerConfig();

    @Data
    public static class ProviderConfig {
        private String type;
        private String apiKey;
        private String endpoint;
        private String model;
        private int timeoutMs = 30000;
        private Map<String, String> parameters = new HashMap<>();
    }

    @Data
    public static class RetryConfig {
        private int maxAttempts = 3;
        private long delayMs = 1000;
        private double multiplier = 2.0;
        private boolean enabled = true;
    }

    @Data
    public static class CircuitBreakerConfig {
        private boolean enabled = true;
        private int failureThreshold = 5;
        private long recoveryTimeoutMs = 60000;
        private int halfOpenMaxCalls = 3;
    }
}