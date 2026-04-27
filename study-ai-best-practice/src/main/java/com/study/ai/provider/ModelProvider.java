package com.study.ai.provider;

import com.study.ai.model.ChatRequest;
import com.study.ai.model.ChatResponse;

public interface ModelProvider {

    String getProviderName();

    boolean supports(String modelType);

    ChatResponse chat(ChatRequest request);

    default boolean isAvailable() {
        return true;
    }
}
