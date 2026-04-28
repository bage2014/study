package com.bage.study.ai.best.practice.exception.analysis.mcp;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BusinessKnowledgeTool implements McpTool {

    private static final String BUSINESS_KNOWLEDGE = "=== Business Domain Knowledge ===\n" +
        "Domain: user-management\n" +
        "Topic: authentication\n" +
        "\nKey Business Rules:\n" +
        "1. User IDs must be non-null and valid\n" +
        "2. Authentication requires valid token\n" +
        "3. User data must be retrieved from database\n" +
        "4. Error handling should include null checks\n" +
        "\nCommon Issues:\n" +
        "- NullPointerException when user ID is null\n" +
        "- Database connection timeouts\n" +
        "- Token validation failures\n" +
        "\nBest Practices:\n" +
        "- Always validate input parameters\n" +
        "- Use defensive programming\n" +
        "- Implement proper error handling\n" +
        "- Log all critical operations\n";

    @Override
    public String getName() {
        return "query_business";
    }

    @Override
    public String getDescription() {
        return "Query business domain knowledge";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
            "domain", "Business domain",
            "topic", "Knowledge topic"
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            return McpToolResult.success(BUSINESS_KNOWLEDGE);
        } catch (Exception e) {
            return McpToolResult.error("Failed to query business knowledge: " + e.getMessage());
        }
    }
}
