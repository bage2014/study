package com.bage.ai.pipeline.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bage.ai.pipeline.agent.tool.FileReadTool;
import com.bage.ai.pipeline.core.dto.activity.AtomicTask;
import com.bage.ai.pipeline.core.dto.activity.FeaturePoint;
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
import java.util.List;

@Slf4j
@Service
public class TaskSplitAgentService {

    private final TaskSplitAiService aiService;
    private final ObjectMapper objectMapper;

    public TaskSplitAgentService(
            @Qualifier("codeGenModel") ChatLanguageModel model,
            ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.aiService = AiServices.builder(TaskSplitAiService.class)
                .chatLanguageModel(model)
                .build();
    }

    public List<AtomicTask> split(String projectLocalPath, FeaturePoint featurePoint,
                                   ProjectType projectType, BuildTool buildTool) {
        String techStack = describeTechStack(projectType, buildTool);
        String result = aiService.split(
                featurePoint.getId(),
                featurePoint.getTitle(),
                featurePoint.getDescription(),
                String.join("\n- ", featurePoint.getAcceptanceCriteria()),
                featurePoint.getScope(),
                techStack);
        return parseTasks(result, featurePoint.getId());
    }

    private List<AtomicTask> parseTasks(String json, String featurePointId) {
        try {
            String trimmed = json.trim();
            if (trimmed.startsWith("```")) {
                trimmed = trimmed.replaceAll("(?s)^```[a-z]*\\n?", "").replaceAll("```\\s*$", "").trim();
            }
            int start = trimmed.indexOf('[');
            int end = trimmed.lastIndexOf(']');
            if (start >= 0 && end > start) {
                trimmed = trimmed.substring(start, end + 1);
            }
            List<AtomicTask> tasks = objectMapper.readValue(trimmed, new TypeReference<>() {});
            tasks.forEach(t -> { if (t.getFeaturePointId() == null) t.setFeaturePointId(featurePointId); });
            return tasks;
        } catch (Exception e) {
            log.error("Failed to parse atomic tasks JSON: {}", e.getMessage());
            return Collections.emptyList();
        }
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

    interface TaskSplitAiService {

        @SystemMessage("""
                You are a senior {{techStack}} engineer.

                Your task: split a feature point into 1-3 atomic implementation tasks.

                Rules for each atomic task:
                - Modifies 1-3 files maximum
                - type must be exactly one of: BACKEND, FRONTEND, DATABASE, CONFIG
                - targetFiles must be real relative paths (use listDirectory tool to verify project structure)
                - id format: "{{featureId}}-task-N" (e.g. "fp-1-task-1")
                - title must be ≤ 60 chars, action-oriented

                IMPORTANT: Use the listDirectory tool first to inspect the project structure and determine accurate target file paths.

                Output ONLY a valid JSON array — no markdown fences, no explanation:
                [
                  {
                    "id": "{{featureId}}-task-1",
                    "featurePointId": "{{featureId}}",
                    "title": "...",
                    "description": "exactly what code change to make",
                    "targetFiles": ["src/main/java/com/example/service/UserService.java"],
                    "type": "BACKEND"
                  }
                ]
                """)
        @UserMessage("""
                Tech stack: {{techStack}}
                Feature Point: {{title}} (scope: {{scope}})
                Description: {{description}}
                Acceptance criteria:
                - {{criteria}}

                Split into 1-3 atomic tasks with exact file paths.
                """)
        String split(
                @V("featureId") String featureId,
                @V("title") String title,
                @V("description") String description,
                @V("criteria") String criteria,
                @V("scope") String scope,
                @V("techStack") String techStack);
    }
}