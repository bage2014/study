package com.bage.ai.pipeline.api.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestExecResult {
    private String pipelineId;
    private String runId;
    private Boolean success;
    private String output;
    private String testOutput;
}