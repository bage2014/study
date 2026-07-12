package com.bage.ai.pipeline.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementAnalysisInput {
    private String pipelineId;
    private String runId;
    private String prdMarkdown;
    private String requirementMd;
}