package com.bage.ai.pipeline.api.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeReviewResult {
    private String pipelineId;
    private String runId;
    private boolean hasCriticalIssues;
    private List<String> issues;
}