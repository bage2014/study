package com.bage.ai.pipeline.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bage.ai.pipeline.agent.config.AgentToolRegistry;
import com.bage.ai.pipeline.agent.tool.FileReadTool;
import com.bage.ai.pipeline.agent.tool.FileWriteTool;
import com.bage.ai.pipeline.core.dto.activity.AtomicTask;
import com.bage.ai.pipeline.core.enums.BuildTool;
import com.bage.ai.pipeline.core.enums.ProjectType;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CodeGenAgentService {

    private final FileWriteTool fileWriteTool;
    private final FileReadTool fileReadTool;
    private final CodeGenAiService aiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CodeGenAgentService(
            @Qualifier("codeGenModel") ChatLanguageModel model,
            AgentToolRegistry toolRegistry,
            FileReadTool fileReadTool,
            FileWriteTool fileWriteTool) {
        this.fileReadTool = fileReadTool;
        this.fileWriteTool = fileWriteTool;
        this.aiService = AiServices.builder(CodeGenAiService.class)
                .chatLanguageModel(model)
                .tools(toolRegistry.getTools("file-read", "file-write", "ast-analysis"))
                .build();
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
        fileReadTool.setActivePath(projectPath);
        fileWriteTool.setActivePath(projectPath);

        String techStack = describeTechStack(projectType, buildTool);
        String result;

        if (currentTask != null) {
            String targetFilesHint = currentTask.getTargetFiles() != null
                    ? String.join(", ", currentTask.getTargetFiles()) : "（未指定）";
            result = aiService.generateTaskCode(projectPath, parsedRequirementJson, techStack,
                    currentTask.getTitle(), currentTask.getDescription(),
                    targetFilesHint, currentTask.getType() != null ? currentTask.getType().name() : "BACKEND");
        } else if (testFailureContext != null && !testFailureContext.isBlank()) {
            result = aiService.fixCode(projectPath, parsedRequirementJson, techStack, testFailureContext);
        } else {
            result = aiService.generateCode(projectPath, parsedRequirementJson, techStack);
        }

        Map<String, String> written = fileWriteTool.getWrittenFiles();
        if (written.isEmpty()) {
            written = parseGeneratedFilesJson(result);
        }
        return written;
    }

    private String describeTechStack(ProjectType projectType, BuildTool buildTool) {
        String typeLabel = switch (projectType != null ? projectType : ProjectType.JAVA_SPRING) {
            case JAVA_SPRING -> "Java Spring Boot";
            case PYTHON_FASTAPI -> "Python FastAPI";
            case NODE_EXPRESS -> "Node.js Express";
            case VUE -> "Vue 3";
            case REACT -> "React";
            case FULLSTACK -> "Java Spring Boot backend + Vue 3 frontend";
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

    interface CodeGenAiService {

        @SystemMessage("""
                You are a senior software engineer specializing in {{techStack}}.

                You have access to tools to read and write files in the target project.
                Use analyzeJavaFile and listAllClasses tools to understand the project structure
                before generating code — this gives you precise class and method information.

                Your task:
                1. Use listDirectory, readFile, analyzeJavaFile, listAllClasses tools to understand the project.
                2. Based on the structured requirements, implement the necessary changes.
                3. Use writeFile tool to write each modified or new file.
                4. Write complete, compilable code — no TODOs, no stubs.
                5. Follow the existing code style, package conventions, and tech stack ({{techStack}}).

                After writing all files, output a JSON summary of what was changed:
                {"relative/path/File.ext": "brief description of change"}
                """)
        @UserMessage("""
                Project path: {{projectPath}}
                Tech stack: {{techStack}}

                Structured requirements:
                {{requirements}}

                Please analyze the project structure and implement the required changes.
                """)
        String generateCode(
                @V("projectPath") String projectPath,
                @V("requirements") String requirements,
                @V("techStack") String techStack);

        @SystemMessage("""
                You are a senior software engineer specializing in {{techStack}}.

                You are implementing ONE specific atomic task. Focus ONLY on this task — do not modify other files.

                Steps:
                1. Use listDirectory and readFile to read the target files and understand the context.
                2. Implement EXACTLY what the task description says — minimal change, no scope creep.
                3. Use writeFile to write the modified file(s).
                4. Output a JSON summary: {"relative/path/File.ext": "what was changed"}
                """)
        @UserMessage("""
                Project path: {{projectPath}}
                Tech stack: {{techStack}}
                Task type: {{taskType}}

                Task: {{taskTitle}}
                Description: {{taskDescription}}
                Target files (hint): {{targetFiles}}

                Context (full requirement):
                {{requirements}}

                Implement ONLY this atomic task.
                """)
        String generateTaskCode(
                @V("projectPath") String projectPath,
                @V("requirements") String requirements,
                @V("techStack") String techStack,
                @V("taskTitle") String taskTitle,
                @V("taskDescription") String taskDescription,
                @V("targetFiles") String targetFiles,
                @V("taskType") String taskType);

        @SystemMessage("""
                You are a senior software engineer specializing in {{techStack}}.

                The previous code generation passed review but FAILED automated tests.
                Your task is to FIX the code to make the tests pass.

                You have access to tools to read and write files.
                Use analyzeJavaFile and listAllClasses tools for precise code navigation.

                Steps:
                1. Read the test failure output carefully.
                2. Use readFile and analyzeJavaFile to locate the problematic code.
                3. Fix ONLY what is needed to make tests pass — minimal change, no unrelated refactoring.
                4. Write the fixed files using writeFile.
                5. Output a JSON summary: {"relative/path/File.ext": "what was fixed"}
                """)
        @UserMessage("""
                Project path: {{projectPath}}
                Tech stack: {{techStack}}

                Original requirements:
                {{requirements}}

                Test failure output:
                {{testFailureContext}}

                Please fix the code to make the tests pass.
                """)
        String fixCode(
                @V("projectPath") String projectPath,
                @V("requirements") String requirements,
                @V("techStack") String techStack,
                @V("testFailureContext") String testFailureContext);
    }
}