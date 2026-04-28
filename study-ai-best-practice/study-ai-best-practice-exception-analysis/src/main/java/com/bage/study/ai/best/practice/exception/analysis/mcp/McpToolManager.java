package com.bage.study.ai.best.practice.exception.analysis.mcp;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class McpToolManager {

    private final Map<String, McpTool> tools = new ConcurrentHashMap<>();

    public McpToolManager(List<McpTool> mcpTools) {
        for (McpTool tool : mcpTools) {
            registerTool(tool);
        }
    }

    public void registerTool(McpTool tool) {
        tools.put(tool.getName(), tool);
    }

    public void unregisterTool(String toolName) {
        tools.remove(toolName);
    }

    public McpTool getTool(String toolName) {
        return tools.get(toolName);
    }

    public Map<String, McpTool> getAllTools() {
        return Map.copyOf(tools);
    }

    public McpToolResult executeTool(String toolName, Map<String, Object> arguments) {
        McpTool tool = tools.get(toolName);
        if (tool == null) {
            return McpToolResult.error("Tool not found: " + toolName);
        }
        try {
            return tool.execute(arguments);
        } catch (Exception e) {
            return McpToolResult.error("Tool execution failed: " + e.getMessage());
        }
    }

    public boolean isToolAvailable(String toolName) {
        return tools.containsKey(toolName);
    }

    public int getToolCount() {
        return tools.size();
    }
}
