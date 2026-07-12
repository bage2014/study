package com.bage.ai.pipeline.core.dto.activity;

import com.bage.ai.pipeline.core.enums.BuildTool;
import com.bage.ai.pipeline.core.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeGenInput {
    private String pipelineId;
    private String runId;
    private String projectPath;
    private String projectLocalPath;
    private String parsedRequirementJson;
    private ProjectType projectType;
    private BuildTool buildTool;
    private String frontendLocalPath;
    private AtomicTask currentTask;
    private String testFailureContext;
}