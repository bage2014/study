package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.api.dto.activity.TestGenInput;
import com.bage.ai.pipeline.api.dto.activity.TestGenResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TestGenActivity {

    @ActivityMethod(name = "TestGeneration")
    TestGenResult generate(TestGenInput input);
}