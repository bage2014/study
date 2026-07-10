package com.bage.ai.pipeline.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bage.ai.pipeline.agent.tool.FileReadTool;
import com.bage.ai.pipeline.agent.tool.FileWriteTool;
import com.bage.ai.pipeline.core.enums.ProjectType;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class TestGenAgentService {

    private final FileWriteTool fileWriteTool;
    private final FileReadTool fileReadTool;
    private final TestGenAiService aiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TestGenAgentService(
            @Qualifier("codeGenModel") ChatLanguageModel model,
            FileReadTool fileReadTool,
            FileWriteTool fileWriteTool) {
        this.fileReadTool = fileReadTool;
        this.fileWriteTool = fileWriteTool;
        this.aiService = AiServices.builder(TestGenAiService.class)
                .chatLanguageModel(model)
                .tools(fileReadTool, fileWriteTool)
                .build();
    }

    public Map<String, String> generateTests(String projectPath, ProjectType projectType,
                                              List<String> generatedFilePaths) {
        fileReadTool.setActivePath(projectPath);
        fileWriteTool.setActivePath(projectPath);

        String techStack = describeTechStack(projectType);
        String filesList = String.join("\n- ", generatedFilePaths);
        String result = aiService.generateTests(projectPath, techStack, filesList);

        Map<String, String> written = fileWriteTool.getWrittenFiles();
        if (written.isEmpty()) {
            written = parseGeneratedFilesJson(result);
        }
        return written;
    }

    private String describeTechStack(ProjectType projectType) {
        return switch (projectType != null ? projectType : ProjectType.JAVA_SPRING) {
            case JAVA_SPRING -> "Java Spring Boot with JUnit 5";
            case PYTHON_FASTAPI -> "Python FastAPI with pytest";
            case NODE_EXPRESS -> "Node.js Express with Jest";
            case VUE -> "Vue 3 with Vitest";
            case REACT -> "React with Jest";
            case FULLSTACK -> "Java Spring Boot (JUnit 5) + Vue 3 (Vitest)";
        };
    }

    private Map<String, String> parseGeneratedFilesJson(String json) {
        try {
            String trimmed = json.trim();
            if (trimmed.startsWith("```")) {
                trimmed = trimmed.replaceAll("^```[a-z]*\\n?", "").replaceAll("```$", "").trim();
            }
            return objectMapper.readValue(trimmed, new TypeReference<>() {});
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    interface TestGenAiService {

        @SystemMessage("""
                You are a senior software engineer specializing in {{techStack}} testing.

                Your task: generate comprehensive unit tests for the given files.

                Rules:
                1. Read the source files first using readFile tool.
                2. Generate test files using writeFile tool.
                3. Follow the existing test conventions in the project.
                4. Cover all major methods and edge cases.
                5. Output a JSON summary when done: {"test/file/path.java": "test description"}

                Output ONLY the JSON summary.
                """)
        @UserMessage("""
                Project path: {{projectPath}}
                Tech stack: {{techStack}}

                Files to test:
                {{files}}

                Generate comprehensive unit tests for these files.
                """)
        String generateTests(
                @V("projectPath") String projectPath,
                @V("techStack") String techStack,
                @V("files") String files);
    }
}