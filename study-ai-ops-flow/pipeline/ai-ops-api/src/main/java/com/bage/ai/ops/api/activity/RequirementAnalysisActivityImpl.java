package com.bage.ai.ops.api.activity;

import com.bage.ai.ops.agent.service.RequirementAgentService;
import com.bage.ai.ops.core.dto.activity.RequirementAnalysisInput;
import com.bage.ai.ops.core.dto.activity.RequirementAnalysisResult;
import com.bage.ai.ops.core.activity.RequirementAnalysisActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RequirementAnalysisActivityImpl implements RequirementAnalysisActivity {

    private final RequirementAgentService requirementAgentService;

    public RequirementAnalysisActivityImpl(RequirementAgentService requirementAgentService) {
        this.requirementAgentService = requirementAgentService;
    }

    @Override
    public RequirementAnalysisResult analyze(RequirementAnalysisInput input) {
        log.info("Starting requirement analysis for run: {}", input.getRunId());
        String parsedJson = requirementAgentService.analyze(input.getRequirementMd());
        List<String> clarificationQuestions = extractClarificationQuestions(parsedJson);
        log.info("Requirement analysis completed for run: {}, clarification questions: {}",
                input.getRunId(), clarificationQuestions.size());
        return RequirementAnalysisResult.builder()
                .runId(input.getRunId())
                .parsedRequirementJson(parsedJson)
                .clarificationQuestions(clarificationQuestions)
                .message("Requirement analysis completed")
                .build();
    }

    /** 从 LLM 产出的需求 JSON 中提取澄清问题，解析失败返回空列表 */
    private List<String> extractClarificationQuestions(String parsedJson) {
        try {
            String trimmed = parsedJson.trim();
            if (trimmed.startsWith("```")) {
                trimmed = trimmed.replaceAll("(?s)^```[a-z]*\\n?", "").replaceAll("```\\s*$", "").trim();
            }
            int start = trimmed.indexOf('{');
            int end = trimmed.lastIndexOf('}');
            if (start >= 0 && end > start) {
                trimmed = trimmed.substring(start, end + 1);
            }
            com.fasterxml.jackson.databind.JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(trimmed);
            com.fasterxml.jackson.databind.JsonNode questions = root.get("clarification_questions");
            List<String> result = new ArrayList<>();
            if (questions != null && questions.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode q : questions) {
                    if (q.isTextual() && !q.asText().isBlank()) {
                        result.add(q.asText());
                    }
                }
            }
            return result;
        } catch (Exception e) {
            log.warn("Failed to extract clarification questions: {}", e.getMessage());
            return List.of();
        }
    }
}