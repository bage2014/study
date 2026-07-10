package com.bage.ai.pipeline.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UiTestInput {
    private String pipelineId;
    private String runId;
    private String frontendLocalPath;
    private String projectLocalPath;
    private String frontendUrl;
    private String backendUrl;
}