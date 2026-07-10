package com.bage.ai.pipeline.api.strategy;

import com.bage.ai.pipeline.core.dto.activity.TestExecInput;
import com.bage.ai.pipeline.core.dto.activity.TestExecResult;

public interface TestExecutionStrategy {

    TestExecResult execute(TestExecInput input);

    String getName();
}