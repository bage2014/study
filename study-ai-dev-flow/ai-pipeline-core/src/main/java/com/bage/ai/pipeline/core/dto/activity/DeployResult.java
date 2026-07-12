package com.bage.ai.pipeline.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployResult {
    private String pipelineId;
    private String runId;
    private Boolean success;
    private String containerId;
    private String accessUrl;
    private String deployedUrl;
    private String deployInfo;
    private String output;
}