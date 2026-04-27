package com.bage.study.ai.best.practice.exceptionanalysis.mcp;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TraceQueryTool implements McpTool {

    private static final String TRACE_DATA = "=== Trace Analysis ===\n" +
        "Trace ID: 1234567890\n" +
        "Service: user-service\n" +
        "Operation: getUser\n" +
        "Duration: 150ms\n" +
        "Status: ERROR\n" +
        "\nCall Chain:\n" +
        "1. UserController.getUser() [20ms]\n" +
        "2. UserService.getUser() [100ms]\n" +
        "3. DatabaseService.query() [30ms]\n" +
        "\nError Details:\n" +
        "- Service: UserService\n" +
        "- Method: getUser\n" +
        "- Error: java.lang.NullPointerException\n" +
        "- Message: Cannot invoke method on null object\n" +
        "- Stack trace:\n" +
        "  at com.example.UserService.getUser(UserService.java:42)\n" +
        "  at com.example.UserController.getUser(UserController.java:28)\n" +
        "  at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n";

    @Override
    public String getName() {
        return "query_trace";
    }

    @Override
    public String getDescription() {
        return "Query distributed trace data for error analysis";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
            "traceId", "Trace ID",
            "service", "Service name",
            "operation", "Operation name"
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            return McpToolResult.success(TRACE_DATA);
        } catch (Exception e) {
            return McpToolResult.error("Failed to query trace: " + e.getMessage());
        }
    }
}
