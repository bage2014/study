package com.bage.ai.pipeline.agent.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.DisabledChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ModelGatewayConfig {

    @Value("${ai.deepseek.api-key:}")
    private String deepseekApiKey;

    @Value("${ai.deepseek.base-url:https://api.deepseek.com/v1}")
    private String deepseekBaseUrl;

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
        log.info("Building requirement model with provider: {}, model: {}", requirementProvider, requirementModelName);
        ChatLanguageModel model = buildModel(requirementProvider, requirementModelName, 4096);
        log.info("Requirement model built successfully: {}", model.getClass().getSimpleName());
        return model;
    }

    @Bean("codeGenModel")
    public ChatLanguageModel codeGenModel() {
        log.info("Building codegen model with provider: {}, model: {}", codeGenProvider, codeGenModelName);
        ChatLanguageModel model = buildModel(codeGenProvider, codeGenModelName, 8192);
        log.info("Codegen model built successfully: {}", model.getClass().getSimpleName());
        return model;
    }

    private ChatLanguageModel buildModel(String provider, String modelName, int maxTokens) {
        return switch (provider.toLowerCase()) {
            case "openai", "deepseek" -> buildDeepSeekModel(modelName, maxTokens);
            case "none" -> {
                log.warn("AI provider is set to 'none', using DisabledChatLanguageModel");
                yield new DisabledChatLanguageModel();
            }
            default -> throw new IllegalStateException("Unknown AI provider: " + provider + ". Supported providers: openai, deepseek, none");
        };
    }

    private ChatLanguageModel buildDeepSeekModel(String modelName, int maxTokens) {
        if (deepseekApiKey == null || deepseekApiKey.isEmpty()) {
            throw new IllegalStateException("DeepSeek API key is not configured. " +
                    "Please set AI_DEEPSEEK_API_KEY in .env file or environment variable.");
        }
        log.info("Creating DeepSeek model: {} with baseUrl: {}, maxTokens: {}", 
                modelName, deepseekBaseUrl, maxTokens);
        OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
                .apiKey(deepseekApiKey)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .baseUrl(deepseekBaseUrl);
        return builder.build();
    }
}