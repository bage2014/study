package com.bage.ai.pipeline.core.dto.activity;

import com.bage.ai.pipeline.core.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployInput {
    private String pipelineId;
    private String runId;
    private String projectPath;
    private ProjectType projectType;
    private String imageName;
    private String imageTag;
    private String containerName;
    private int hostPort;
    private int containerPort;
    private String frontendLocalPath;
    private int frontendHostPort;
}