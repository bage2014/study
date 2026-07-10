package com.bage.ai.pipeline.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeReviewInput {
    private String pipelineId;
    private String runId;
    private String projectLocalPath;
    private Map<String, String> generatedFiles;
}