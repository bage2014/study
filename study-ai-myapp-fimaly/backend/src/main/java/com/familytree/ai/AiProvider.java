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

    record SimpleAiRequest(String type, Map<String, Object> params) implements AiRequest {
        @Override
        public String getType() { return type; }
        @Override
        public Map<String, Object> getParams() { return params; }
    }

    record SimpleAiStoryRequest(String type, Map<String, Object> params) implements AiStoryRequest {
        @Override
        public String getType() { return type; }
        @Override
        public Map<String, Object> getParams() { return params; }
    }

    class SimpleAiResponse implements AiResponse {
        private final boolean success;
        private final String content;
        private final String errorMessage;
        private final Map<String, Object> metadata;

        public SimpleAiResponse(boolean success, String content, String errorMessage, Map<String, Object> metadata) {
            this.success = success;
            this.content = content;
            this.errorMessage = errorMessage;
            this.metadata = metadata;
        }

        public static AiResponse success(String content) {
            return new SimpleAiResponse(true, content, null, Map.of());
        }

        public static AiResponse error(String errorMessage) {
            return new SimpleAiResponse(false, null, errorMessage, Map.of());
        }

        @Override
        public boolean isSuccess() {
            return success;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public Map<String, Object> getMetadata() {
            return metadata;
        }
    }
}
