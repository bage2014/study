package com.bage.study.ai.best.practice.rca.agent.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具注册表：Playbook 路由与 LLM 工具目录均以本表为唯一事实来源。
 */
public class ToolRegistry {

    private static final Logger log = LoggerFactory.getLogger(ToolRegistry.class);

    private final Map<String, McpTool> tools = new LinkedHashMap<>();

    public ToolRegistry(List<McpTool> toolList) {
        for (McpTool tool : toolList) {
            McpTool old = tools.put(tool.name(), tool);
            if (old != null) {
                throw new IllegalStateException("Duplicate tool name registered: " + tool.name());
            }
        }
        log.info("ToolRegistry initialized with {} tools: {}", tools.size(), tools.keySet());
    }

    public McpTool get(String name) {
        return tools.get(name);
    }

    public boolean exists(String name) {
        return tools.containsKey(name);
    }

    public Map<String, McpTool> all() {
        return Map.copyOf(tools);
    }
}
