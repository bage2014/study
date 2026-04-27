package com.bage.study.ai.best.practice.hello.model;

import java.util.Map;

public record ChatRequest(
    String modelType,
    String message,
    Double temperature,
    Integer maxTokens,
    Map<String, String> metadata
) {
    public ChatRequest {
        if (modelType == null || modelType.isBlank()) {
            modelType = ModelType.DEEPSEEK.getCode();
        }
        if (temperature == null) {
            temperature = 0.7;
        }
        if (maxTokens == null) {
            maxTokens = 2048;
        }
        if (metadata == null) {
            metadata = Map.of();
        }
    }

    public static ChatRequest of(String message) {
        return new ChatRequest(null, message, null, null, null);
    }

    public static ChatRequest of(String modelType, String message) {
        return new ChatRequest(modelType, message, null, null, null);
    }
}
