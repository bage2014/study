package com.bage.ai.pipeline.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementAnalysisResult {
    private String pipelineId;
    private String runId;
    private String parsedRequirementJson;
    private String message;
}