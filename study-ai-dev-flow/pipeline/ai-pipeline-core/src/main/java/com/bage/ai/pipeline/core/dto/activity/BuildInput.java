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
public class BuildInput {
    private String pipelineId;
    private String runId;
    private String projectPath;
    private String projectLocalPath;
    private ProjectType projectType;
    private BuildTool buildTool;
    private String imageName;
    private String frontendLocalPath;
}