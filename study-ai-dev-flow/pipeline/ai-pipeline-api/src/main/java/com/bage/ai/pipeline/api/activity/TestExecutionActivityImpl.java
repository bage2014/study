package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.core.dto.activity.TestExecInput;
import com.bage.ai.pipeline.core.dto.activity.TestExecResult;
import com.bage.ai.pipeline.core.activity.TestExecutionActivity;
import com.bage.ai.pipeline.core.strategy.TestExecutionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestExecutionActivityImpl implements TestExecutionActivity {

    private final TestExecutionStrategy testExecutionStrategy;

    public TestExecutionActivityImpl(TestExecutionStrategy testExecutionStrategy) {
        this.testExecutionStrategy = testExecutionStrategy;
    }

    @Override
    public TestExecResult execute(TestExecInput input) {
        log.info("Starting test execution for pipeline: {}, strategy: {}",
                input.getPipelineId(), testExecutionStrategy.getName());
        TestExecResult result = testExecutionStrategy.execute(input);
        log.info("Test execution completed for pipeline: {}, success: {}", input.getPipelineId(), result.getSuccess());
        return result;
    }
}