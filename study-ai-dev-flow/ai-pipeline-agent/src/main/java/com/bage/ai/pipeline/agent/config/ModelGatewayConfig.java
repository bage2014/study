package com.bage.ai.pipeline.agent.config;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelGatewayConfig {

    @Value("${ai.anthropic.api-key:}")
    private String anthropicApiKey;

    @Value("${ai.openai.api-key:}")
    private String openAiApiKey;

    @Value("${ai.deepseek.api-key:}")
    private String deepseekApiKey;

    @Value("${ai.deepseek.base-url:https://api.deepseek.com/v1}")
    private String deepseekBaseUrl;

    @Value("${ai.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ai.requirement-model-provider:deepseek}")
    private String requirementProvider;

    @Value("${ai.codegen-model-provider:deepseek}")
    private String codeGenProvider;

    @Value("${ai.requirement-model:deepseek-chat}")
    private String requirementModelName;

    @Value("${ai.codegen-model:deepseek-chat}")
    private String codeGenModelName;

    @Bean("requirementModel")
    public ChatLanguageModel requirementModel() {
        return buildModel(requirementProvider, requirementModelName, 4096);
    }

    @Bean("codeGenModel")
    public ChatLanguageModel codeGenModel() {
        return buildModel(codeGenProvider, codeGenModelName, 8192);
    }

    private ChatLanguageModel buildModel(String provider, String modelName, int maxTokens) {
        return switch (provider.toLowerCase()) {
            case "mock" -> new MockChatLanguageModel();
            case "openai" -> buildOpenAiModel(openAiApiKey, null, modelName, maxTokens);
            case "deepseek" -> buildOpenAiModel(deepseekApiKey, deepseekBaseUrl, modelName, maxTokens);
            case "ollama" -> OllamaChatModel.builder()
                    .baseUrl(ollamaBaseUrl)
                    .modelName(modelName)
                    .build();
            default -> buildAnthropicModel(anthropicApiKey, modelName, maxTokens);
        };
    }

    private ChatLanguageModel buildOpenAiModel(String apiKey, String baseUrl, String modelName, int maxTokens) {
        if (apiKey == null || apiKey.isEmpty()) {
            return new MockChatLanguageModel();
        }
        OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .maxTokens(maxTokens);
        if (baseUrl != null && !baseUrl.isEmpty()) {
            builder.baseUrl(baseUrl);
        }
        return builder.build();
    }

    private ChatLanguageModel buildAnthropicModel(String apiKey, String modelName, int maxTokens) {
        if (apiKey == null || apiKey.isEmpty()) {
            return new MockChatLanguageModel();
        }
        return AnthropicChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .build();
    }
}