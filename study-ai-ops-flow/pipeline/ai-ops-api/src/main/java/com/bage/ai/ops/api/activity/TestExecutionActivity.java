package com.bage.ai.ops.api.activity;

import com.bage.ai.ops.core.dto.activity.TestExecInput;
import com.bage.ai.ops.core.dto.activity.TestExecResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TestExecutionActivity {

    @ActivityMethod(name = "TestExecution")
    TestExecResult execute(TestExecInput input);
}