package com.bage.study.ai.best.practice.rca.agent.tool;

/**
 * MCP 风格数据查询工具。工具名是 Playbook/Prompt 引用的唯一事实来源，
 * 注册名与文档/Prompt 必须保持一致。
 */
public interface McpTool {

    String name();

    String description();

    String layer();

    ToolResult execute(ToolQuery query);
}
