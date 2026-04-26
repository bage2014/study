package com.familytree.ai;

import java.util.Map;

public interface AiProvider {

    String getProviderName();

    boolean isAvailable();

    AiResponse predict(AiRequest request);

    AiResponse generateStory(AiStoryRequest request);

    interface AiRequest {
        String getType();
        Map<String, Object> getParams();
    }

    interface AiResponse {
        boolean isSuccess();
        String getContent();
        String getErrorMessage();
        Map<String, Object> getMetadata();
    }

    interface AiStoryRequest {
        String getType();
        Map<String, Object> getParams();
    }
}