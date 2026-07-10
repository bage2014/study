package com.bage.ai.pipeline.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeaturePointSplitResult {
    private String pipelineId;
    private String runId;
    private List<FeaturePoint> featurePoints;
}