package com.bage.ai.pipeline.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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
    private Integer totalTests;
    private Integer passedTests;
    private Integer failedTests;
    private List<Map<String, String>> testDetails;
}