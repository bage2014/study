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

    @Value("${ai.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ai.requirement-model-provider:claude}")
    private String requirementProvider;

    @Value("${ai.codegen-model-provider:claude}")
    private String codeGenProvider;

    @Value("${ai.requirement-model:claude-sonnet-4-6}")
    private String requirementModelName;

    @Value("${ai.codegen-model:claude-sonnet-4-6}")
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
            case "openai" -> OpenAiChatModel.builder()
                    .apiKey(openAiApiKey)
                    .modelName(modelName)
                    .build();
            case "ollama" -> OllamaChatModel.builder()
                    .baseUrl(ollamaBaseUrl)
                    .modelName(modelName)
                    .build();
            default -> AnthropicChatModel.builder()
                    .apiKey(anthropicApiKey)
                    .modelName(modelName)
                    .maxTokens(maxTokens)
                    .build();
        };
    }
}