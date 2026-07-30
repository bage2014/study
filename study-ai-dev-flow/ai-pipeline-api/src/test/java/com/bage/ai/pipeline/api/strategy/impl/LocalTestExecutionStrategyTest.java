package com.bage.ai.pipeline.api.strategy.impl;

import com.bage.ai.pipeline.core.dto.activity.TestExecResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class LocalTestExecutionStrategyTest {

    private static final Pattern TEST_CLASS_LINE_PATTERN = Pattern.compile("\\[INFO\\]\\s*Tests\\s+run:\\s*(\\d+),\\s*Failures:\\s*(\\d+),\\s*Errors:\\s*(\\d+)(?:,\\s*Skipped:\\s*(\\d+))?.*--\\s+in\\s+(\\S+)");
    private static final Pattern TOTAL_RESULT_PATTERN = Pattern.compile("\\[INFO\\]\\s*Tests\\s+run:\\s*(\\d+),\\s*Failures:\\s*(\\d+),\\s*Errors:\\s*(\\d+)(?:,\\s*Skipped:\\s*(\\d+))?");
    private static final Pattern RUNNING_CLASS_PATTERN = Pattern.compile("\\[INFO\\]\\s*Running\\s+(\\S+)");

    @Test
    void testTestClassLinePatternMatchesActualOutput() {
        String actualOutputLine = "[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.493 s -- in com.bage.demo.controller.MessageControllerTest";
        Matcher matcher = TEST_CLASS_LINE_PATTERN.matcher(actualOutputLine);
        
        assertTrue(matcher.find(), "Should match test class line with [INFO] prefix");
        assertEquals("11", matcher.group(1), "Should capture tests run");
        assertEquals("0", matcher.group(2), "Should capture failures");
        assertEquals("0", matcher.group(3), "Should capture errors");
        assertEquals("0", matcher.group(4), "Should capture skipped");
        assertEquals("com.bage.demo.controller.MessageControllerTest", matcher.group(5), "Should capture class name");
    }

    @Test
    void testTotalResultPatternMatchesActualOutput() {
        String actualOutputLine = "[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0";
        Matcher matcher = TOTAL_RESULT_PATTERN.matcher(actualOutputLine);
        
        assertTrue(matcher.find(), "Should match total result line with [INFO] prefix");
        assertEquals("23", matcher.group(1), "Should capture total tests");
        assertEquals("0", matcher.group(2), "Should capture failures");
        assertEquals("0", matcher.group(3), "Should capture errors");
    }

    @Test
    void testRunningClassPatternMatchesActualOutput() {
        String actualOutputLine = "[INFO] Running com.bage.demo.service.MessageServiceTest";
        Matcher matcher = RUNNING_CLASS_PATTERN.matcher(actualOutputLine);
        
        assertTrue(matcher.find(), "Should match running class line with [INFO] prefix");
        assertEquals("com.bage.demo.service.MessageServiceTest", matcher.group(1), "Should capture class name");
    }

    @Test
    void testParseTestResultsWithRealOutput() {
        String realMavenOutput = """
            [INFO] Running com.bage.demo.controller.MessageControllerTest
            [INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.493 s -- in com.bage.demo.controller.MessageControllerTest
            [INFO] Running com.bage.demo.service.MessageServiceTest
            [INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.534 s -- in com.bage.demo.service.MessageServiceTest
            [INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
            [INFO] BUILD SUCCESS
            """;

        List<Map<String, String>> testDetails = new ArrayList<>();
        int totalTests = 0;
        int totalFailures = 0;

        String[] lines = realMavenOutput.split("\n");

        for (String line : lines) {
            Matcher classLineMatcher = TEST_CLASS_LINE_PATTERN.matcher(line);
            if (classLineMatcher.find()) {
                int testsRun = Integer.parseInt(classLineMatcher.group(1));
                int failures = Integer.parseInt(classLineMatcher.group(2));
                int errors = Integer.parseInt(classLineMatcher.group(3));
                String skippedStr = classLineMatcher.group(4);
                int skipped = (skippedStr != null) ? Integer.parseInt(skippedStr) : 0;
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
            }
        }

        assertEquals(23, totalTests, "Total tests should be 23");
        assertEquals(0, totalFailures, "Total failures should be 0");
        assertEquals(2, testDetails.size(), "Should have 2 test class details");
        
        assertEquals("MessageControllerTest", testDetails.get(0).get("name"));
        assertEquals("11", testDetails.get(0).get("total"));
        assertEquals("11", testDetails.get(0).get("passed"));
        
        assertEquals("MessageServiceTest", testDetails.get(1).get("name"));
        assertEquals("12", testDetails.get(1).get("total"));
        assertEquals("12", testDetails.get(1).get("passed"));
    }

    @Test
    void testParseTestResultsWithoutSkippedField() {
        // Test output format without Skipped field
        String mavenOutputWithoutSkipped = """
            [INFO] Tests run: 5, Failures: 0, Errors: 0, Time elapsed: 1.234 s -- in com.example.MyTest
            [INFO] Tests run: 3, Failures: 1, Errors: 0, Time elapsed: 0.567 s -- in com.example.OtherTest
            [INFO] Tests run: 8, Failures: 1, Errors: 0
            [INFO] BUILD SUCCESS
            """;

        List<Map<String, String>> testDetails = new ArrayList<>();
        int totalTests = 0;
        int totalFailures = 0;

        String[] lines = mavenOutputWithoutSkipped.split("\n");

        for (String line : lines) {
            Matcher classLineMatcher = TEST_CLASS_LINE_PATTERN.matcher(line);
            if (classLineMatcher.find()) {
                int testsRun = Integer.parseInt(classLineMatcher.group(1));
                int failures = Integer.parseInt(classLineMatcher.group(2));
                int errors = Integer.parseInt(classLineMatcher.group(3));
                String skippedStr = classLineMatcher.group(4);
                int skipped = (skippedStr != null) ? Integer.parseInt(skippedStr) : 0;
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
            }
        }

        assertEquals(8, totalTests, "Total tests should be 8");
        assertEquals(1, totalFailures, "Total failures should be 1");
        assertEquals(2, testDetails.size(), "Should have 2 test class details");
        
        assertEquals("MyTest", testDetails.get(0).get("name"));
        assertEquals("5", testDetails.get(0).get("total"));
        assertEquals("5", testDetails.get(0).get("passed"));
        
        assertEquals("OtherTest", testDetails.get(1).get("name"));
        assertEquals("3", testDetails.get(1).get("total"));
        assertEquals("2", testDetails.get(1).get("passed"));
        assertEquals("1", testDetails.get(1).get("failed"));
    }

    @Test
    void testFallbackToSummaryPattern() {
        // Output without test class details (no -- in suffix)
        String summaryOnlyOutput = """
            [INFO] Tests run: 15, Failures: 2, Errors: 0, Skipped: 1
            [INFO] BUILD FAILURE
            """;

        int totalTests = 0;
        int totalFailures = 0;

        // First try TEST_CLASS_LINE_PATTERN (should not match because no -- in)
        boolean hasClassDetails = false;
        for (String line : summaryOnlyOutput.split("\n")) {
            if (TEST_CLASS_LINE_PATTERN.matcher(line).find()) {
                hasClassDetails = true;
                break;
            }
        }

        assertFalse(hasClassDetails, "Should not have class details without -- in suffix");

        // Fallback to TOTAL_RESULT_PATTERN
        Matcher totalMatcher = TOTAL_RESULT_PATTERN.matcher(summaryOnlyOutput);
        if (totalMatcher.find()) {
            totalTests = Integer.parseInt(totalMatcher.group(1));
            totalFailures = Integer.parseInt(totalMatcher.group(2)) + Integer.parseInt(totalMatcher.group(3));
        }

        assertEquals(15, totalTests, "Should parse total from summary");
        assertEquals(2, totalFailures, "Should parse failures from summary");
    }
}
