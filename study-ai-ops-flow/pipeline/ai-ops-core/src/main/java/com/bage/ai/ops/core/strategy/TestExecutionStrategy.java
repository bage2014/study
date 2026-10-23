package com.bage.ai.ops.core.strategy;

import com.bage.ai.ops.core.dto.activity.TestExecInput;
import com.bage.ai.ops.core.dto.activity.TestExecResult;

public interface TestExecutionStrategy {

    TestExecResult execute(TestExecInput input);

    String getName();
}