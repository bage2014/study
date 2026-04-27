package com.study.ai.service;

import com.study.ai.model.ChatRequest;
import com.study.ai.model.ChatResponse;
import com.study.ai.provider.ModelProviderManager;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ModelProviderManager providerManager;

    public ChatService(ModelProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    public ChatResponse chat(ChatRequest request) {
        return providerManager.chat(request);
    }

    public ChatResponse chat(String message) {
        return chat(ChatRequest.of(message));
    }

    public ChatResponse chat(String modelType, String message) {
        return chat(ChatRequest.of(modelType, message));
    }

    public ModelProviderManager getProviderManager() {
        return providerManager;
    }
}
