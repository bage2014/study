package com.bage.ai.ops.api.activity;

import com.bage.ai.ops.core.dto.activity.UiTestInput;
import com.bage.ai.ops.core.dto.activity.UiTestResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface UiTestActivity {

    @ActivityMethod(name = "UiTest")
    UiTestResult run(UiTestInput input);
}