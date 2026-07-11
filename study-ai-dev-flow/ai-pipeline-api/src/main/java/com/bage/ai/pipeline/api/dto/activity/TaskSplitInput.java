package com.bage.ai.pipeline.api.dto.activity;

import com.bage.ai.pipeline.api.enums.BuildTool;
import com.bage.ai.pipeline.api.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSplitInput {
    private String pipelineId;
    private String runId;
    private String projectPath;
    private String projectLocalPath;
    private FeaturePoint featurePoint;
    private ProjectType projectType;
    private BuildTool buildTool;
}