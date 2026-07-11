package com.bage.ai.pipeline.api.dto.workflow;

import com.bage.ai.pipeline.api.enums.BuildTool;
import com.bage.ai.pipeline.api.enums.ProjectType;
import com.bage.ai.pipeline.api.enums.StageName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipelineStartInput {
    private String runId;
    private Long projectId;
    private String projectLocalPath;
    private String requirementMd;
    private Map<StageName, Boolean> autoApproveMap;
    private ProjectType projectType;
    private BuildTool buildTool;
    private String frontendLocalPath;
    private String prdUrl;
    private boolean sandboxEnabled;
    private boolean autoFixOnTestFail;
    private int maxTestFixRetries;
}