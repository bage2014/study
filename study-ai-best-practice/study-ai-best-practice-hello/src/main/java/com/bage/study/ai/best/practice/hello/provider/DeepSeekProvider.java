package com.bage.study.ai.best.practice.hello.provider;

import com.bage.study.ai.best.practice.hello.model.ChatRequest;
import com.bage.study.ai.best.practice.hello.model.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeepSeekProvider implements ModelProvider {

    private final ChatModel chatModel;
    private final String modelName;

    public DeepSeekProvider(
            ChatModel chatModel,
            @Value("${spring.ai.openai.chat.options.model:deepseek-chat}") String modelName) {
        this.chatModel = chatModel;
        this.modelName = modelName;
    }

    @Override
    public String getProviderName() {
        return "deepseek";
    }

    @Override
    public boolean supports(String modelType) {
        return "deepseek".equalsIgnoreCase(modelType);
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        try {
            ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

            String content = chatClient.prompt()
                .user(request.message())
                .call()
                .content();

            return ChatResponse.success(getProviderName(), content, modelName);
        } catch (Exception e) {
            return ChatResponse.error(getProviderName(), e.getMessage());
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
