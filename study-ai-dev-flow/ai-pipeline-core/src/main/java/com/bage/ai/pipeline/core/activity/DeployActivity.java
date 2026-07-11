package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.api.dto.activity.DeployInput;
import com.bage.ai.pipeline.api.dto.activity.DeployResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DeployActivity {

    @ActivityMethod(name = "Deploy")
    DeployResult deploy(DeployInput input);
}