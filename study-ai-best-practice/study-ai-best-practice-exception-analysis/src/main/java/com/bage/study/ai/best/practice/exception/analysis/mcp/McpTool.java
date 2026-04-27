package com.bage.study.ai.best.practice.exceptionanalysis.mcp;

import java.util.Map;

public interface McpTool {

    String getName();

    String getDescription();

    Map<String, String> getInputSchema();

    McpToolResult execute(Map<String, Object> arguments);
}
