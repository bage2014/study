package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.core.dto.activity.BuildInput;
import com.bage.ai.pipeline.core.dto.activity.BuildResult;
import com.bage.ai.pipeline.core.activity.BuildActivity;
import com.bage.ai.pipeline.api.strategy.BuildStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BuildActivityImpl implements BuildActivity {

    private final BuildStrategy buildStrategy;

    public BuildActivityImpl(BuildStrategy buildStrategy) {
        this.buildStrategy = buildStrategy;
    }

    @Override
    public BuildResult build(BuildInput input) {
        log.info("Starting build for pipeline: {}, strategy: {}", input.getPipelineId(), buildStrategy.getName());
        BuildResult result = buildStrategy.build(input);
        log.info("Build completed for pipeline: {}, success: {}", input.getPipelineId(), result.getSuccess());
        return result;
    }
}