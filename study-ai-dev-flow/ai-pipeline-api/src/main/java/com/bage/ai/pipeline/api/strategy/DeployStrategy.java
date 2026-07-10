package com.bage.ai.pipeline.api.strategy;

import com.bage.ai.pipeline.core.dto.activity.DeployInput;
import com.bage.ai.pipeline.core.dto.activity.DeployResult;

public interface DeployStrategy {

    DeployResult deploy(DeployInput input);

    String getName();
}