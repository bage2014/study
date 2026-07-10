package com.bage.ai.pipeline.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CodeReviewAgentService {

    private final CodeReviewAiService aiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CodeReviewAgentService(@Qualifier("codeGenModel") ChatLanguageModel model) {
        this.aiService = AiServices.builder(CodeReviewAiService.class)
                .chatLanguageModel(model)
                .build();
    }

    public ReviewResult review(Map<String, String> generatedFiles) {
        StringBuilder codeContent = new StringBuilder();
        for (Map.Entry<String, String> entry : generatedFiles.entrySet()) {
            codeContent.append("=== File: ").append(entry.getKey()).append(" ===\n");
            codeContent.append(entry.getValue()).append("\n\n");
        }

        String result = aiService.review(codeContent.toString());
        return parseReviewResult(result);
    }

    private ReviewResult parseReviewResult(String json) {
        try {
            String trimmed = json.trim();
            if (trimmed.startsWith("```")) {
                trimmed = trimmed.replaceAll("(?s)^```[a-z]*\\n?", "").replaceAll("```\\s*$", "").trim();
            }
            var node = objectMapper.readTree(trimmed);
            boolean hasCritical = node.has("hasCriticalIssues") && node.get("hasCriticalIssues").asBoolean(false);
            List<String> issues = node.has("issues")
                    ? objectMapper.convertValue(node.get("issues"), new TypeReference<List<String>>() {})
                    : Collections.emptyList();
            return new ReviewResult(hasCritical, issues);
        } catch (Exception e) {
            log.error("Failed to parse review result: {}", e.getMessage());
            return new ReviewResult(false, Collections.emptyList());
        }
    }

    public record ReviewResult(boolean hasCriticalIssues, List<String> issues) {}

    interface CodeReviewAiService {

        @SystemMessage("""
                You are a senior software code reviewer.

                Review the generated code and identify issues.

                Output ONLY a valid JSON object with this structure:
                {
                  "hasCriticalIssues": true/false,
                  "issues": [
                    "issue description with severity (CRITICAL/WARNING/INFO)"
                  ]
                }

                Critical issues include:
                - Compilation errors
                - Security vulnerabilities
                - Logic bugs
                - Major performance issues

                Warning issues include:
                - Code style violations
                - Minor best practice violations
                - Potential improvements

                No explanation, no markdown fences.
                """)
        @UserMessage("""
                Generated code to review:

                {{code}}

                Please review and identify issues.
                """)
        String review(@V("code") String code);
    }
}