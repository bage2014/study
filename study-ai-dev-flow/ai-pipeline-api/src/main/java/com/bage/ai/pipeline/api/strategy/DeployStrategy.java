package com.bage.ai.pipeline.api.strategy;

import com.bage.ai.pipeline.api.dto.activity.DeployInput;
import com.bage.ai.pipeline.api.dto.activity.DeployResult;

public interface DeployStrategy {

    DeployResult deploy(DeployInput input);

    String getName();
}