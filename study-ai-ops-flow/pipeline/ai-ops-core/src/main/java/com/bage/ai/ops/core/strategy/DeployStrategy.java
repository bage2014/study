package com.bage.ai.ops.core.strategy;

import com.bage.ai.ops.core.dto.activity.DeployInput;
import com.bage.ai.ops.core.dto.activity.DeployResult;

public interface DeployStrategy {

    DeployResult deploy(DeployInput input);

    String getName();
}