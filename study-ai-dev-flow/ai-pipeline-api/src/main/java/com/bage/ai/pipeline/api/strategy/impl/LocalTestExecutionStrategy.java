package com.bage.ai.pipeline.api.strategy.impl;

import com.bage.ai.pipeline.agent.tool.MavenTool;
import com.bage.ai.pipeline.agent.tool.NpmTool;
import com.bage.ai.pipeline.core.dto.activity.TestExecInput;
import com.bage.ai.pipeline.core.dto.activity.TestExecResult;
import com.bage.ai.pipeline.core.enums.BuildTool;
import com.bage.ai.pipeline.core.strategy.TestExecutionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class LocalTestExecutionStrategy implements TestExecutionStrategy {

    private final MavenTool mavenTool;
    private final NpmTool npmTool;

    private static final Pattern TEST_CLASS_PATTERN = Pattern.compile("Running\\s+(\\S+)");
    private static final Pattern TEST_RESULT_PATTERN = Pattern.compile("Tests\\s+run:\\s*(\\d+),\\s*Failures:\\s*(\\d+),\\s*Errors:\\s*(\\d+),\\s*Skipped:\\s*(\\d+)");
    private static final Pattern TOTAL_RESULT_PATTERN = Pattern.compile("Tests\\s+run:\\s*(\\d+),\\s*Failures:\\s*(\\d+),\\s*Errors:\\s*(\\d+)");

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

            TestExecResult.TestExecResultBuilder builder = TestExecResult.builder()
                    .runId(input.getRunId())
                    .success(success)
                    .testOutput(testOutput);

            parseTestResults(testOutput, builder);

            TestExecResult result = builder.build();
            log.info("Local test execution completed for project: {}, success: {}, total: {}, passed: {}, failed: {}",
                    projectPath, success, result.getTotalTests(), result.getPassedTests(), result.getFailedTests());
            return result;
        } catch (Exception e) {
            log.error("Local test execution failed: {}", e.getMessage());
            return TestExecResult.builder()
                    .runId(input.getRunId())
                    .success(false)
                    .testOutput("Test execution failed: " + e.getMessage())
                    .totalTests(0)
                    .passedTests(0)
                    .failedTests(0)
                    .testDetails(new ArrayList<>())
                    .build();
        }
    }

    private void parseTestResults(String output, TestExecResult.TestExecResultBuilder builder) {
        List<Map<String, String>> testDetails = new ArrayList<>();
        int totalTests = 0;
        int totalFailures = 0;

        String[] lines = output.split("\n");
        String currentTestClass = null;

        for (String line : lines) {
            Matcher classMatcher = TEST_CLASS_PATTERN.matcher(line);
            if (classMatcher.find()) {
                currentTestClass = classMatcher.group(1);
                continue;
            }

            Matcher resultMatcher = TEST_RESULT_PATTERN.matcher(line);
            if (resultMatcher.find() && currentTestClass != null) {
                int testsRun = Integer.parseInt(resultMatcher.group(1));
                int failures = Integer.parseInt(resultMatcher.group(2));
                int errors = Integer.parseInt(resultMatcher.group(3));

                totalTests += testsRun;
                totalFailures += failures + errors;

                String simpleClassName = currentTestClass.contains(".")
                        ? currentTestClass.substring(currentTestClass.lastIndexOf(".") + 1)
                        : currentTestClass;

                Map<String, String> testDetail = new HashMap<>();
                testDetail.put("name", simpleClassName);
                testDetail.put("status", (failures + errors == 0) ? "PASSED" : "FAILED");
                testDetail.put("total", String.valueOf(testsRun));
                testDetail.put("passed", String.valueOf(testsRun - failures - errors));
                testDetail.put("failed", String.valueOf(failures + errors));
                testDetails.add(testDetail);

                currentTestClass = null;
            }
        }

        if (testDetails.isEmpty()) {
            Matcher totalMatcher = TOTAL_RESULT_PATTERN.matcher(output);
            if (totalMatcher.find()) {
                totalTests = Integer.parseInt(totalMatcher.group(1));
                totalFailures = Integer.parseInt(totalMatcher.group(2)) + Integer.parseInt(totalMatcher.group(3));
            }
        }

        builder.totalTests(totalTests)
                .passedTests(totalTests - totalFailures)
                .failedTests(totalFailures)
                .testDetails(testDetails);
    }

    @Override
    public String getName() {
        return "local";
    }
}
