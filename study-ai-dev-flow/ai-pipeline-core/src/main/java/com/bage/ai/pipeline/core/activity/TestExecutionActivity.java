package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.api.dto.activity.TestExecInput;
import com.bage.ai.pipeline.api.dto.activity.TestExecResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TestExecutionActivity {

    @ActivityMethod(name = "TestExecution")
    TestExecResult execute(TestExecInput input);
}