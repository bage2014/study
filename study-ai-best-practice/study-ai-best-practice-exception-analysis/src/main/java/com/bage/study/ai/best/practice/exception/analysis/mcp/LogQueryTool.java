package com.bage.study.ai.best.practice.exceptionanalysis.mcp;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LogQueryTool implements McpTool {

    private static final String[] ERROR_LOGS = {
        "2026-04-27 10:15:32 ERROR UserService.getUser - NullPointerException: Cannot invoke method on null object",
        "2026-04-27 10:15:33 ERROR UserController.getUser - Failed to get user: java.lang.NullPointerException",
        "2026-04-27 10:15:34 WARN  UserService.getUser - User ID null detected",
        "2026-04-27 10:15:35 ERROR DatabaseService.query - Connection timeout",
        "2026-04-27 10:15:36 INFO  UserService.getUser - User not found: null"
    };

    @Override
    public String getName() {
        return "query_logs";
    }

    @Override
    public String getDescription() {
        return "Query service logs for errors and warnings";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
            "service", "Service name",
            "startTime", "Start time (ISO format)",
            "endTime", "End time (ISO format)",
            "keywords", "Search keywords"
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            StringBuilder logs = new StringBuilder();
            logs.append("=== Log Query Results ===\n");
            logs.append("Service: " + arguments.getOrDefault("service", "user-service") + "\n");
            logs.append("Time range: " + arguments.getOrDefault("startTime", "2026-04-27T00:00:00") + " to " + 
                        arguments.getOrDefault("endTime", "2026-04-27T23:59:59") + "\n");
            logs.append("Keywords: " + arguments.getOrDefault("keywords", "NullPointerException") + "\n\n");

            for (String log : ERROR_LOGS) {
                if (log.contains(arguments.getOrDefault("keywords", "").toString())) {
                    logs.append(log).append("\n");
                }
            }

            logs.append("\nTotal log entries: 5");
            return McpToolResult.success(logs.toString());
        } catch (Exception e) {
            return McpToolResult.error("Failed to query logs: " + e.getMessage());
        }
    }
}
