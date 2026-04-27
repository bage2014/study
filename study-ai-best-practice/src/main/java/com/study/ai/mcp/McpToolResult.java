package com.study.ai.mcp;

public record McpToolResult(
    boolean success,
    String content,
    String error
) {
    public static McpToolResult success(String content) {
        return new McpToolResult(true, content, null);
    }

    public static McpToolResult error(String error) {
        return new McpToolResult(false, null, error);
    }
}
