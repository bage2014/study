package com.bage.study.ai.best.practice.hello.model;

public record ChatResponse(
    String modelType,
    String content,
    String modelName,
    Long tokens,
    String error
) {
    public static ChatResponse success(String modelType, String content, String modelName) {
        return new ChatResponse(modelType, content, modelName, null, null);
    }

    public static ChatResponse success(String modelType, String content, String modelName, Long tokens) {
        return new ChatResponse(modelType, content, modelName, tokens, null);
    }

    public static ChatResponse error(String modelType, String error) {
        return new ChatResponse(modelType, null, null, null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }
}
