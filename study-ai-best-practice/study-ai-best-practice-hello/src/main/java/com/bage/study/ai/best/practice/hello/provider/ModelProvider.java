package com.bage.study.ai.best.practice.hello.provider;

import com.bage.study.ai.best.practice.hello.model.ChatRequest;
import com.bage.study.ai.best.practice.hello.model.ChatResponse;

public interface ModelProvider {

    String getProviderName();

    boolean supports(String modelType);

    ChatResponse chat(ChatRequest request);

    default boolean isAvailable() {
        return true;
    }
}
