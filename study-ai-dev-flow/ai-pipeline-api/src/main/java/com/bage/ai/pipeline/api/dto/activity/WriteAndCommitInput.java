package com.bage.ai.pipeline.api.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WriteAndCommitInput {
    private String pipelineId;
    private String runId;
    private String projectLocalPath;
    private Map<String, String> generatedFiles;
    private String commitMessage;
    private String frontendLocalPath;
}