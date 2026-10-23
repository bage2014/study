package com.bage.ai.ops.core.activity;

import com.bage.ai.ops.core.dto.activity.BuildInput;
import com.bage.ai.ops.core.dto.activity.BuildResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BuildActivity {

    @ActivityMethod(name = "Build")
    BuildResult build(BuildInput input);
}