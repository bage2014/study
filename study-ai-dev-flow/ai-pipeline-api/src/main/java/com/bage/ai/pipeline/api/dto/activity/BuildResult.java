package com.bage.ai.pipeline.api.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildResult {
    private String pipelineId;
    private String runId;
    private Boolean success;
    private String imageName;
    private String imageTag;
    private String artifactPath;
    private String buildOutput;
    private String output;
}