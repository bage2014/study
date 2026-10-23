package com.bage.ai.ops.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bage.ai.ops.agent.tool.FileReadTool;
import com.bage.ai.ops.core.dto.activity.FeaturePoint;
import com.bage.ai.ops.core.enums.BuildTool;
import com.bage.ai.ops.core.enums.ProjectType;
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
public class FeaturePointSplitAgentService {

    private final FeaturePointSplitAiService aiService;
    private final ObjectMapper objectMapper;

    public FeaturePointSplitAgentService(
            @Qualifier("requirementModel") ChatLanguageModel model,
            ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.aiService = AiServices.builder(FeaturePointSplitAiService.class)
                .chatLanguageModel(model)
                .build();
    }

    public List<FeaturePoint> split(String projectLocalPath, String parsedRequirementJson,
                                    ProjectType projectType, BuildTool buildTool) {
        String techStack = describeTechStack(projectType, buildTool);
        String result = aiService.split(parsedRequirementJson, techStack);
        List<FeaturePoint> fps = parseFeaturePoints(result);
        if (fps.isEmpty()) {
            log.warn("Feature point split parse failed, retrying with strict JSON-only prompt");
            String retryResult = aiService.splitStrict(parsedRequirementJson, techStack);
            fps = parseFeaturePoints(retryResult);
        }
        return fps;
    }

    private List<FeaturePoint> parseFeaturePoints(String json) {
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
            List<FeaturePoint> fps = objectMapper.readValue(trimmed, new TypeReference<>() {});
            int index = 1;
            for (FeaturePoint fp : fps) {
                if (fp.getId() == null || fp.getId().isBlank()) {
                    fp.setId("fp-" + index++);
                }
            }
            return fps;
        } catch (Exception e) {
            log.error("Failed to parse feature points JSON: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String describeTechStack(ProjectType projectType, BuildTool buildTool) {
        String typeLabel = switch (projectType != null ? projectType : ProjectType.JAVA_SPRING) {
            case JAVA_SPRING, SPRING_BOOT -> "Java Spring Boot";
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

    interface FeaturePointSplitAiService {

        @SystemMessage("""
                You are a senior {{techStack}} engineer and product architect.

                Your task: decompose the parsed requirements into 3-8 feature points.

                Rules for each feature point:
                - Independent deliverable unit
                - scope must be one of: backend, frontend, fullstack
                - id format: fp-N (e.g. fp-1, fp-2)
                - title must be ≤ 50 chars, action-oriented
                - Use the listDirectory tool to understand the project structure first

                Output ONLY a valid JSON array — no markdown fences, no explanation:
                [
                  {
                    "id": "fp-1",
                    "title": "...",
                    "description": "detailed description",
                    "acceptanceCriteria": ["criterion 1", "criterion 2"],
                    "scope": "backend"
                  }
                ]
                """)
        @UserMessage("""
                Tech stack: {{techStack}}

                Parsed requirements:
                {{requirements}}

                Decompose into 3-8 feature points.
                """)
        String split(
                @V("requirements") String requirements,
                @V("techStack") String techStack);

        @SystemMessage("""
                You are a senior {{techStack}} engineer and product architect.

                CRITICAL: Output ONLY a raw JSON array. No explanation, no markdown fences, no thinking out loud.
                The first character of your response MUST be '[' and the last character MUST be ']'.

                Decompose the parsed requirements into 3-8 feature points. Each feature point object:
                {
                  "id": "fp-1",
                  "title": "≤ 50 chars, action-oriented",
                  "description": "detailed description",
                  "acceptanceCriteria": ["criterion 1", "criterion 2"],
                  "scope": "backend"
                }
                scope must be exactly one of: backend, frontend, fullstack.
                """)
        @UserMessage("""
                Tech stack: {{techStack}}

                Parsed requirements:
                {{requirements}}

                Respond with the JSON array only.
                """)
        String splitStrict(
                @V("requirements") String requirements,
                @V("techStack") String techStack);
    }
}