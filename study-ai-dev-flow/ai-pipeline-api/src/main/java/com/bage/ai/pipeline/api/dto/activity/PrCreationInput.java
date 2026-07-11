package com.bage.ai.pipeline.api.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrCreationInput {

    private String pipelineId;
    private String projectPath;
    private String branchName;
    private String title;
    private String description;
}