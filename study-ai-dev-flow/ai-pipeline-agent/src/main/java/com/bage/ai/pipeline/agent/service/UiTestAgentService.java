package com.bage.ai.pipeline.agent.service;

import com.bage.ai.pipeline.agent.tool.FileWriteTool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UiTestAgentService {

    private final FileWriteTool fileWriteTool;
    private final UiTestAiService aiService;

    public UiTestAgentService(
            @Qualifier("codeGenModel") ChatLanguageModel model,
            FileWriteTool fileWriteTool) {
        this.fileWriteTool = fileWriteTool;
        this.aiService = AiServices.builder(UiTestAiService.class)
                .chatLanguageModel(model)
                .tools(fileWriteTool)
                .build();
    }

    public String generateAndRunTests(String frontendPath, String frontendUrl, String backendUrl) {
        fileWriteTool.setActivePath(frontendPath);
        return aiService.generateAndRunTests(frontendPath, frontendUrl, backendUrl);
    }

    interface UiTestAiService {

        @SystemMessage("""
                You are a senior frontend QA engineer specializing in Playwright testing.

                Your task: generate and run Playwright UI tests for the given application.

                Steps:
                1. Inspect the frontend project structure.
                2. Generate Playwright test files.
                3. Output a test report summary.

                Output ONLY the test report.
                """)
        @UserMessage("""
                Frontend path: {{frontendPath}}
                Frontend URL: {{frontendUrl}}
                Backend URL: {{backendUrl}}

                Generate and run UI tests.
                """)
        String generateAndRunTests(
                @V("frontendPath") String frontendPath,
                @V("frontendUrl") String frontendUrl,
                @V("backendUrl") String backendUrl);
    }
}