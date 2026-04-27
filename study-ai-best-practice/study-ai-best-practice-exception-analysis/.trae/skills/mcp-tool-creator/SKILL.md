---
name: "mcp-tool-creator"
description: "MCP 工具创建器。当用户需要为异常分析系统添加新的 MCP 工具时使用此技能。"
---

# MCP 工具创建器

此技能帮助为异常分析系统创建新的 MCP（Model Context Protocol）工具。

## 何时使用

在以下情况调用此技能：
- 为异常分析系统添加新的 MCP 工具
- 集成外部服务到分析流程
- 扩展系统的信息收集能力

## 工具结构

MCP 工具需要实现 `McpTool` 接口：

```java
@Component
public class MyTool implements McpTool {

    @Override
    public String getName() {
        return "tool_name";
    }

    @Override
    public String getDescription() {
        return "工具描述";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
            "param1", "参数1描述",
            "param2", "参数2描述"
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

## 工具注册

使用 `@Component` 注解的工具会被 `McpToolManager` 自动注册：

```java
@Autowired
private McpToolManager mcpToolManager;

// 执行工具
McpToolResult result = mcpToolManager.executeTool("tool_name", arguments);
```

## 内置工具

系统已内置以下 MCP 工具：

| 工具名称 | 描述 |
|---------|------|
| `query_logs` | 查询服务日志 |
| `query_gitlab` | 检查代码变更 |
| `query_trace` | 分析错误调用链 |
| `query_business` | 应用业务领域知识 |

## 文件位置

MCP 工具放置目录：
`study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exceptionanalysis/mcp/`
