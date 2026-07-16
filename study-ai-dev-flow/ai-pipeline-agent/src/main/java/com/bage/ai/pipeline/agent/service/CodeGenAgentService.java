package com.bage.ai.pipeline.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bage.ai.pipeline.core.dto.activity.AtomicTask;
import com.bage.ai.pipeline.core.enums.BuildTool;
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
import java.util.Map;

@Slf4j
@Service
public class CodeGenAgentService {

    private final CodeGenAiService aiService;
    private final ObjectMapper objectMapper;

    public CodeGenAgentService(@Qualifier("codeGenModel") ChatLanguageModel model,
                               ObjectMapper objectMapper) {
        this.aiService = AiServices.builder(CodeGenAiService.class)
                .chatLanguageModel(model)
                .build();
        this.objectMapper = objectMapper;
    }

    public Map<String, String> generateCode(String projectPath, String parsedRequirementJson,
                                             ProjectType projectType, BuildTool buildTool) {
        return generateCode(projectPath, parsedRequirementJson, projectType, buildTool, null, null);
    }

    public Map<String, String> generateCode(String projectPath, String parsedRequirementJson,
                                             ProjectType projectType, BuildTool buildTool,
                                             String testFailureContext) {
        return generateCode(projectPath, parsedRequirementJson, projectType, buildTool,
                testFailureContext, null);
    }

    public Map<String, String> generateCode(String projectPath, String parsedRequirementJson,
                                             ProjectType projectType, BuildTool buildTool,
                                             String testFailureContext, AtomicTask currentTask) {
        String techStack = describeTechStack(projectType, buildTool);
        String taskInfo = currentTask != null ? 
                "Task: " + currentTask.getTitle() + "\nTarget files: " + String.join(", ", currentTask.getTargetFiles()) : "Full implementation";
        
        String result = aiService.generateCode(
                techStack,
                parsedRequirementJson,
                taskInfo,
                testFailureContext != null ? testFailureContext : "No test failures"
        );
        
        return parseGeneratedFiles(result);
    }

    private Map<String, String> parseGeneratedFiles(String json) {
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
            log.error("Failed to parse generated files JSON: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    private String describeTechStack(ProjectType projectType, BuildTool buildTool) {
        String typeLabel = switch (projectType != null ? projectType : ProjectType.SPRING_BOOT) {
            case SPRING_BOOT -> "Java 21 + Spring Boot 3.3";
            case PYTHON_FASTAPI -> "Python 3.11 + FastAPI";
            case NODE_EXPRESS -> "Node.js + Express";
            case VUE -> "Vue 3 + Vite";
            case REACT -> "React 18 + Vite";
            case FULLSTACK -> "Java Spring Boot backend + Vue 3 frontend";
            default -> "Java Spring Boot";
        };
        if (buildTool == null) return typeLabel;
        return typeLabel + " + " + switch (buildTool) {
            case MAVEN -> "Maven";
            case GRADLE -> "Gradle";
            case NPM -> "npm";
            case PNPM -> "pnpm";
            case PIP -> "pip";
            case POETRY -> "Poetry";
        };
    }

    interface CodeGenAiService {

        @SystemMessage("""
                You are a senior {{techStack}} developer.

                Your task: generate complete, production-ready code based on the requirements.

                Rules:
                - Generate ONLY valid Java/Spring Boot code
                - Follow standard project structure (src/main/java, src/main/resources)
                - Include proper imports, error handling, and documentation
                - Make sure the code compiles and runs
                - Follow Spring Boot best practices
                - IMPORTANT: Detect and use the existing package structure from the project. Look for existing Java files to determine the base package name.

                Output ONLY a valid JSON object with file paths as keys and code content as values:
                {
                  "src/main/java/com/example/controller/ProductController.java": "package com.example.controller; ...",
                  "src/main/java/com/example/service/ProductService.java": "package com.example.service; ..."
                }

                No explanation, no markdown fences.
                """)
        @UserMessage("""
                Tech stack: {{techStack}}
                Requirements: {{requirements}}
                Current task: {{taskInfo}}
                Test failure context: {{testFailureContext}}

                Generate complete code for the given requirements.
                """)
        String generateCode(
                @V("techStack") String techStack,
                @V("requirements") String requirements,
                @V("taskInfo") String taskInfo,
                @V("testFailureContext") String testFailureContext);
    }
}