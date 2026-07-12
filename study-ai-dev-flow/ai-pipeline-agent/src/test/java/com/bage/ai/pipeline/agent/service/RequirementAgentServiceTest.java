package com.bage.ai.pipeline.agent.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class RequirementAgentServiceTest {

    private RequirementAgentService requirementAgentService;

    @BeforeEach
    void setUp() {
        String deepseekApiKey = System.getenv("AI_DEEPSEEK_API_KEY");
        String deepseekBaseUrl = System.getenv("AI_DEEPSEEK_BASE_URL");
        
        ChatLanguageModel model;
        if (deepseekApiKey != null && !deepseekApiKey.isEmpty()) {
            model = OpenAiChatModel.builder()
                    .apiKey(deepseekApiKey)
                    .baseUrl(deepseekBaseUrl != null ? deepseekBaseUrl : "https://api.deepseek.com/v1")
                    .modelName("deepseek-chat")
                    .maxTokens(4096)
                    .build();
        } else {
            fail("AI_DEEPSEEK_API_KEY environment variable is not set");
            return;
        }
        
        requirementAgentService = new RequirementAgentService(model);
    }

    @Test
    void testAnalyzeWithDeepseek() {
        String prd = "Generate API documentation for a user management system with CRUD operations.";
        
        String result = requirementAgentService.analyze(prd);
        
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should not be empty");
        
        System.out.println("LLM Response:");
        System.out.println(result);
        
        assertTrue(result.contains("summary") || result.contains("features"), 
                "Result should contain structured JSON with summary or features");
    }
}
