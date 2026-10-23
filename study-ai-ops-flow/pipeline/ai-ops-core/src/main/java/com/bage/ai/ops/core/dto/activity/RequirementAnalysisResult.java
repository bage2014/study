package com.bage.ai.ops.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementAnalysisResult {
    private String pipelineId;
    private String runId;
    private String parsedRequirementJson;
    private String message;
    /** 需求澄清问题（有歧义时产出，供人工确认回环） */
    private List<String> clarificationQuestions;
}