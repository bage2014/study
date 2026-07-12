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
public class TestGenResult {
    private String pipelineId;
    private String runId;
    private Boolean success;
    private Map<String, String> testFiles;
}