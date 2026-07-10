package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.core.dto.activity.DeployInput;
import com.bage.ai.pipeline.core.dto.activity.DeployResult;
import com.bage.ai.pipeline.core.activity.DeployActivity;
import com.bage.ai.pipeline.api.strategy.DeployStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeployActivityImpl implements DeployActivity {

    private final DeployStrategy deployStrategy;

    public DeployActivityImpl(DeployStrategy deployStrategy) {
        this.deployStrategy = deployStrategy;
    }

    @Override
    public DeployResult deploy(DeployInput input) {
        log.info("Starting deployment for pipeline: {}, strategy: {}", input.getPipelineId(), deployStrategy.getName());
        DeployResult result = deployStrategy.deploy(input);
        log.info("Deployment completed for pipeline: {}, success: {}", input.getPipelineId(), result.getSuccess());
        return result;
    }
}