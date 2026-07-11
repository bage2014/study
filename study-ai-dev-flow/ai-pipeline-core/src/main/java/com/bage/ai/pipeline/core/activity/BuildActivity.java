package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.api.dto.activity.BuildInput;
import com.bage.ai.pipeline.api.dto.activity.BuildResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BuildActivity {

    @ActivityMethod(name = "Build")
    BuildResult build(BuildInput input);
}