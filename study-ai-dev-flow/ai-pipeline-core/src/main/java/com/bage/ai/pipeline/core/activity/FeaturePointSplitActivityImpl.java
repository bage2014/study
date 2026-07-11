package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.agent.service.FeaturePointSplitAgentService;
import com.bage.ai.pipeline.api.dto.activity.FeaturePoint;
import com.bage.ai.pipeline.api.dto.activity.FeaturePointSplitInput;
import com.bage.ai.pipeline.api.dto.activity.FeaturePointSplitResult;
import com.bage.ai.pipeline.core.activity.FeaturePointSplitActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FeaturePointSplitActivityImpl implements FeaturePointSplitActivity {

    private final FeaturePointSplitAgentService featurePointSplitAgentService;

    public FeaturePointSplitActivityImpl(FeaturePointSplitAgentService featurePointSplitAgentService) {
        this.featurePointSplitAgentService = featurePointSplitAgentService;
    }

    @Override
    public FeaturePointSplitResult split(FeaturePointSplitInput input) {
        log.info("Starting feature point split for run: {}", input.getRunId());
        List<FeaturePoint> featurePoints = featurePointSplitAgentService.split(
                input.getProjectLocalPath(),
                input.getParsedRequirementJson(),
                input.getProjectType(),
                input.getBuildTool()
        );
        log.info("Feature point split completed: {} feature points generated", featurePoints.size());
        return FeaturePointSplitResult.builder()
                .runId(input.getRunId())
                .featurePoints(featurePoints)
                .build();
    }
}