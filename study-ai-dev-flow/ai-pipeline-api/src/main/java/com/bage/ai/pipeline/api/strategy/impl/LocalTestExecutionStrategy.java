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

    private static final Pattern TEST_CLASS_LINE_PATTERN = Pattern.compile("\\[INFO\\]\\s*Tests\\s+run:\\s*(\\d+),\\s*Failures:\\s*(\\d+),\\s*Errors:\\s*(\\d+),\\s*Skipped:\\s*(\\d+).*--\\s+in\\s+(\\S+)");
    private static final Pattern TEST_CLASS_ONLY_PATTERN = Pattern.compile("--\\s+in\\s+(\\S+)");
    private static final Pattern TOTAL_RESULT_PATTERN = Pattern.compile("\\[INFO\\]\\s*Tests\\s+run:\\s*(\\d+),\\s*Failures:\\s*(\\d+),\\s*Errors:\\s*(\\d+)(?:,\\s*Skipped:\\s*(\\d+))?");
    private static final Pattern RUNNING_CLASS_PATTERN = Pattern.compile("\\[INFO\\]\\s*Running\\s+(\\S+)");
    private static final Pattern INDIVIDUAL_TEST_PATTERN = Pattern.compile("\\[INFO\\]\\s*Tests\\s+run:\\s*(\\d+),\\s*Failures:\\s*(\\d+),\\s*Errors:\\s*(\\d+)(?:,\\s*Skipped:\\s*(\\d+))?");
    private static final Pattern SIMPLE_TEST_PATTERN = Pattern.compile("Tests\\s+run:\\s*(\\d+),\\s*Failures:\\s*(\\d+),\\s*Errors:\\s*(\\d+)(?:,\\s*Skipped:\\s*(\\d+))?");

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
            Matcher classLineMatcher = TEST_CLASS_LINE_PATTERN.matcher(line);
            if (classLineMatcher.find()) {
                int testsRun = Integer.parseInt(classLineMatcher.group(1));
                int failures = Integer.parseInt(classLineMatcher.group(2));
                int errors = Integer.parseInt(classLineMatcher.group(3));
                int skipped = Integer.parseInt(classLineMatcher.group(4));
                String className = classLineMatcher.group(5);

                totalTests += testsRun;
                totalFailures += failures + errors;

                String simpleClassName = className.contains(".")
                        ? className.substring(className.lastIndexOf(".") + 1)
                        : className;

                Map<String, String> testDetail = new HashMap<>();
                testDetail.put("name", simpleClassName);
                testDetail.put("status", (failures + errors == 0) ? "PASSED" : "FAILED");
                testDetail.put("total", String.valueOf(testsRun));
                testDetail.put("passed", String.valueOf(testsRun - failures - errors));
                testDetail.put("failed", String.valueOf(failures + errors));
                testDetails.add(testDetail);

                continue;
            }

            Matcher runningMatcher = RUNNING_CLASS_PATTERN.matcher(line);
            if (runningMatcher.find()) {
                currentTestClass = runningMatcher.group(1);
                continue;
            }

            Matcher resultMatcher = INDIVIDUAL_TEST_PATTERN.matcher(line);
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
                log.info("Parsed total tests from summary: total={}, failures={}", totalTests, totalFailures);
            } else {
                Matcher simpleMatcher = SIMPLE_TEST_PATTERN.matcher(output);
                if (simpleMatcher.find()) {
                    totalTests = Integer.parseInt(simpleMatcher.group(1));
                    totalFailures = Integer.parseInt(simpleMatcher.group(2)) + Integer.parseInt(simpleMatcher.group(3));
                    log.info("Parsed total tests from simple pattern: total={}, failures={}", totalTests, totalFailures);
                }
            }
        }

        if (totalTests == 0) {
            log.warn("Failed to parse test results from output. Output snippet: {}", 
                    output.length() > 500 ? output.substring(0, 500) : output);
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
