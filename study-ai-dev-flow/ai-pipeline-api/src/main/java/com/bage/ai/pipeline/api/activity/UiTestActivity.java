package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.api.dto.activity.UiTestInput;
import com.bage.ai.pipeline.api.dto.activity.UiTestResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface UiTestActivity {

    @ActivityMethod(name = "UiTest")
    UiTestResult run(UiTestInput input);
}