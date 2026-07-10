package com.bage.ai.pipeline.api.strategy.impl;

import com.bage.ai.pipeline.agent.tool.MavenTool;
import com.bage.ai.pipeline.agent.tool.NpmTool;
import com.bage.ai.pipeline.core.dto.activity.TestExecInput;
import com.bage.ai.pipeline.core.dto.activity.TestExecResult;
import com.bage.ai.pipeline.core.enums.BuildTool;
import com.bage.ai.pipeline.api.strategy.TestExecutionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalTestExecutionStrategy implements TestExecutionStrategy {

    private final MavenTool mavenTool;
    private final NpmTool npmTool;

    public LocalTestExecutionStrategy(MavenTool mavenTool, NpmTool npmTool) {
        this.mavenTool = mavenTool;
        this.npmTool = npmTool;
    }

    @Override
    public TestExecResult execute(TestExecInput input) {
        try {
            String projectPath = input.getProjectLocalPath();
            BuildTool buildTool = input.getBuildTool();

            String testOutput;
            if (buildTool == null || buildTool == BuildTool.MAVEN) {
                testOutput = mavenTool.run(projectPath, "test");
            } else if (buildTool == BuildTool.GRADLE) {
                testOutput = mavenTool.executeCommand(projectPath, "./gradlew test");
            } else {
                testOutput = npmTool.run(projectPath, "test");
            }

            boolean success = !testOutput.contains("BUILD FAILURE") &&
                    !testOutput.contains("FAILED") &&
                    !testOutput.contains("TestFailed");

            log.info("Local test execution completed for project: {}, success: {}", projectPath, success);
            return TestExecResult.builder()
                    .runId(input.getRunId())
                    .success(success)
                    .testOutput(testOutput)
                    .build();
        } catch (Exception e) {
            log.error("Local test execution failed: {}", e.getMessage());
            return TestExecResult.builder()
                    .runId(input.getRunId())
                    .success(false)
                    .testOutput("Test execution failed: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public String getName() {
        return "local";
    }
}