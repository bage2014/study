package com.bage.ai.ops.core.dto.activity;

import com.bage.ai.ops.core.enums.BuildTool;
import com.bage.ai.ops.core.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeaturePointSplitInput {
    private String pipelineId;
    private String runId;
    private String projectPath;
    private String parsedRequirementJson;
    private ProjectType projectType;
    private BuildTool buildTool;
    private String projectLocalPath;
}