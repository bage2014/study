---
name: "mcp-tool-creator"
description: "为项目创建 MCP（Model Context Protocol）工具。当用户想要添加新的 MCP 工具或集成外部 MCP 服务时使用此技能。"
---

# MCP 工具创建器

此技能帮助创建 MCP（Model Context Protocol）工具以扩展 AI 能力。

## 何时使用

在以下情况调用此技能：
- 为项目添加新的 MCP 工具
- 集成外部 MCP 服务
- 为 AI 模型上下文创建自定义工具
- 实现基于工具的 AI 交互

## MCP 工具结构

工具必须实现 `McpTool` 接口：

```java
@Component
public class MyTool implements McpTool {

    @Override
    public String getName() {
        return "my_tool";
    }

    @Override
    public String getDescription() {
        return "这个工具的功能描述";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
            "param1", "参数1的描述",
            "param2", "参数2的描述（可选）"
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            // 工具逻辑
            String result = doSomething(arguments);
            return McpToolResult.success(result);
        } catch (Exception e) {
            return McpToolResult.error(e.getMessage());
        }
    }
}
```

## 注册

带有 `@Component` 注解的工具由 `McpToolManager` 自动注册。

## 工具管理器 API

```java
// 执行工具
McpToolResult result = mcpToolManager.executeTool("tool_name", arguments);

// 检查可用性
boolean available = mcpToolManager.isToolAvailable("tool_name");

// 获取所有工具
Map<String, McpTool> tools = mcpToolManager.getAllTools();
```

## 文件位置

MCP 工具放置目录：`src/main/java/com/study/ai/mcp/`
