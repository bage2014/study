package com.study.ai.mcp;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class CurrentTimeTool implements McpTool {

    @Override
    public String getName() {
        return "current_time";
    }

    @Override
    public String getDescription() {
        return "Get current date and time";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
            "format", "optional, date format pattern (default: yyyy-MM-dd HH:mm:ss)"
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            String format = (String) arguments.getOrDefault("format", "yyyy-MM-dd HH:mm:ss");
            String result = LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
            return McpToolResult.success(result);
        } catch (Exception e) {
            return McpToolResult.error("Failed to get current time: " + e.getMessage());
        }
    }
}
