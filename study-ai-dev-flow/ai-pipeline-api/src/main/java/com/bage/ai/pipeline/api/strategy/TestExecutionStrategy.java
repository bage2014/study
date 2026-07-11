package com.bage.ai.pipeline.api.strategy;

import com.bage.ai.pipeline.api.dto.activity.TestExecInput;
import com.bage.ai.pipeline.api.dto.activity.TestExecResult;

public interface TestExecutionStrategy {

    TestExecResult execute(TestExecInput input);

    String getName();
}