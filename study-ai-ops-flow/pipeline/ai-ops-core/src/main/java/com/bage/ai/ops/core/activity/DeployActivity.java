package com.bage.ai.ops.core.activity;

import com.bage.ai.ops.core.dto.activity.DeployInput;
import com.bage.ai.ops.core.dto.activity.DeployResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DeployActivity {

    @ActivityMethod(name = "Deploy")
    DeployResult deploy(DeployInput input);
}