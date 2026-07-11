package com.bage.ai.pipeline.api.dto.activity;

import com.bage.ai.pipeline.api.enums.ProjectType;
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
public class TestGenInput {
    private String pipelineId;
    private String runId;
    private String projectPath;
    private String projectLocalPath;
    private ProjectType projectType;
    private List<String> generatedFilePaths;
    private Map<String, String> generatedFiles;
    private String frontendLocalPath;
}