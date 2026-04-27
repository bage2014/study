package com.study.ai.provider;

import com.study.ai.model.ChatRequest;
import com.study.ai.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ModelProviderManager {

    private final Map<String, ModelProvider> providerMap;

    public ModelProviderManager(List<ModelProvider> providers) {
        this.providerMap = providers.stream()
            .collect(Collectors.toMap(
                p -> p.getProviderName().toLowerCase(),
                Function.identity(),
                (p1, p2) -> p1
            ));
    }

    public ChatResponse chat(ChatRequest request) {
        String modelType = request.modelType() != null ? request.modelType().toLowerCase() : "deepseek";

        ModelProvider provider = providerMap.get(modelType);

        if (provider == null) {
            return ChatResponse.error(modelType, "No provider found for model type: " + modelType);
        }

        if (!provider.isAvailable()) {
            return ChatResponse.error(modelType, "Provider is not available: " + provider.getProviderName());
        }

        try {
            return provider.chat(request);
        } catch (Exception e) {
            return ChatResponse.error(modelType, "Chat failed: " + e.getMessage());
        }
    }

    public ModelProvider getProvider(String modelType) {
        return providerMap.get(modelType.toLowerCase());
    }

    public Map<String, ModelProvider> getAllProviders() {
        return Map.copyOf(providerMap);
    }

    public boolean isProviderAvailable(String modelType) {
        ModelProvider provider = providerMap.get(modelType.toLowerCase());
        return provider != null && provider.isAvailable();
    }
}
