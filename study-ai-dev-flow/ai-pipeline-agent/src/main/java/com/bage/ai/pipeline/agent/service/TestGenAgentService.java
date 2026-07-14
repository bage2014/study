package com.bage.ai.pipeline.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bage.ai.pipeline.core.enums.ProjectType;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TestGenAgentService {

    private final TestGenAiService aiService;
    private final ObjectMapper objectMapper;

    public TestGenAgentService(@Qualifier("codeGenModel") ChatLanguageModel model,
                               ObjectMapper objectMapper) {
        this.aiService = AiServices.builder(TestGenAiService.class)
                .chatLanguageModel(model)
                .build();
        this.objectMapper = objectMapper;
    }

    public Map<String, String> generateTests(String projectPath, ProjectType projectType,
                                              List<String> generatedFilePaths) {
        String techStack = describeTechStack(projectType);
        String filesToTest = String.join("\n", generatedFilePaths);
        
        String result = aiService.generateTests(techStack, filesToTest);
        return parseGeneratedTests(result);
    }

    private Map<String, String> parseGeneratedTests(String json) {
        try {
            String trimmed = json.trim();
            if (trimmed.startsWith("```")) {
                trimmed = trimmed.replaceAll("(?s)^```[a-z]*\\n?", "").replaceAll("```\\s*$", "").trim();
            }
            int start = trimmed.indexOf('{');
            int end = trimmed.lastIndexOf('}');
            if (start >= 0 && end > start) {
                trimmed = trimmed.substring(start, end + 1);
            }
            return objectMapper.readValue(trimmed, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            log.error("Failed to parse generated tests JSON: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    private String describeTechStack(ProjectType projectType) {
        return switch (projectType != null ? projectType : ProjectType.SPRING_BOOT) {
            case SPRING_BOOT -> "Java 21 + Spring Boot 3.3 + JUnit 5 + MockMvc";
            case PYTHON_FASTAPI -> "Python 3.11 + FastAPI + pytest";
            case NODE_EXPRESS -> "Node.js + Express + Jest";
            case VUE -> "Vue 3 + Vite + Vitest";
            case REACT -> "React 18 + Vite + Jest";
            case FULLSTACK -> "Java Spring Boot backend + Vue 3 frontend";
            default -> "Java Spring Boot + JUnit 5";
        };
    }

    interface TestGenAiService {

        @SystemMessage("""
                You are a senior {{techStack}} QA engineer.

                Your task: generate comprehensive unit tests for the given Java source files.

                Rules:
                - Generate ONLY valid JUnit 5 tests
                - Include test cases for normal and edge cases
                - Use Spring Boot test annotations (WebMvcTest, MockBean, etc.)
                - Follow testing best practices
                - Make sure tests are runnable

                Output ONLY a valid JSON object with test file paths as keys and test code as values:
                {
                  "src/test/java/com/example/controller/ProductControllerTest.java": "package com.example.controller; ...",
                  "src/test/java/com/example/service/ProductServiceTest.java": "package com.example.service; ..."
                }

                No explanation, no markdown fences.
                """)
        @UserMessage("""
                Tech stack: {{techStack}}
                Source files to test:
                {{files}}

                Generate comprehensive unit tests for the above source files.
                """)
        String generateTests(
                @V("techStack") String techStack,
                @V("files") String files);
    }
}