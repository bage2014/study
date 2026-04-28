package com.bage.study.ai.best.practice.exception.analysis.mcp;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GitlabQueryTool implements McpTool {

    private static final String[] CODE_CHANGES = {
        "commit 123456: Fix NullPointerException in UserService",
        "commit 7890ab: Update getUser method to handle null input",
        "commit cdef12: Add null check for user ID",
        "commit 345678: Refactor user authentication logic",
        "commit 90abc: Update database connection timeout"
    };

    @Override
    public String getName() {
        return "query_gitlab";
    }

    @Override
    public String getDescription() {
        return "Query GitLab for code changes";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
            "repository", "Repository name",
            "branch", "Branch name",
            "since", "Start date",
            "until", "End date",
            "filePattern", "File pattern"
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            StringBuilder changes = new StringBuilder();
            changes.append("=== GitLab Code Changes ===\n");
            changes.append("Repository: " + arguments.getOrDefault("repository", "user-service") + "\n");
            changes.append("Branch: " + arguments.getOrDefault("branch", "main") + "\n");
            changes.append("Date range: " + arguments.getOrDefault("since", "2026-04-26") + " to " + 
                        arguments.getOrDefault("until", "2026-04-27") + "\n");
            changes.append("File pattern: " + arguments.getOrDefault("filePattern", "**/*.java") + "\n\n");

            for (String change : CODE_CHANGES) {
                changes.append(change).append("\n");
            }

            changes.append("\nTotal commits: 5");
            return McpToolResult.success(changes.toString());
        } catch (Exception e) {
            return McpToolResult.error("Failed to query GitLab: " + e.getMessage());
        }
    }
}
