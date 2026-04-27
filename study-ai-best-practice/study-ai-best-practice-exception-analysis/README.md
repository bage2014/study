# Exception Analysis Module

Spring Boot AI 异常分析模块 - 基于 Plan-Execute-React 模式的问题排查系统。

## 项目特性

- **Plan-Execute-React 架构**：系统化的问题分析流程
- **多轮验证**：支持基于用户反馈的重新分析
- **Mock MCP 工具**：日志、代码、调用链、业务知识的模拟数据
- **AI 驱动分析**：使用大模型生成根因分析和修复建议
- **RESTful API**：完整的异常分析接口

## 技术栈

- Java 17
- Spring Boot 3.3.0
- Spring AI 1.0.0-M6
- Maven

## 项目结构

```
study-ai-best-practice-exception-analysis/
├── pom.xml
├── README.md
├── .trae/skills/                            # Skill 合集
└── src/main/java/com/bage/study/ai/best/practice/exceptionanalysis/
    ├── ExceptionAnalysisApplication.java    # 应用入口
    ├── config/                              # 配置模块
    ├── model/                               # 数据模型
    │   ├── ProblemAnalysisRequest.java      # 分析请求
    │   ├── ProblemAnalysisResponse.java     # 分析响应
    │   └── AnalysisStep.java                # 分析步骤
    ├── service/                             # 核心服务
    │   ├── PlanService.java                 # 生成分析计划
    │   ├── ExecuteService.java              # 执行计划
    │   ├── ReactService.java                # 分析结果
    │   └── AnalysisService                  # 分析协调器
    ├── controller/                          # REST API
    │   └── AnalysisController.java          # 分析控制器
    └── mcp/                                 # MCP 工具
        ├── McpTool.java                     # 工具接口
        ├── McpToolResult.java               # 工具结果
        ├── McpToolManager.java              # 工具管理器
        ├── LogQueryTool.java                # 日志查询工具
        ├── GitlabQueryTool.java             # 代码查询工具
        ├── TraceQueryTool.java              # 调用链查询工具
        └── BusinessKnowledgeTool.java       # 业务知识工具
```

## 快速开始

### 1. 配置 API Key

设置环境变量：
```bash
export DEEPSEEK_API_KEY=your-api-key
export DEEPSEEK_BASE_URL=https://api.deepseek.com
```

或修改 `src/main/resources/application.yml`：
```yaml
spring:
  ai:
    openai:
      api-key: your-api-key
```

### 2. 编译运行

```bash
# 从父目录运行
mvn spring-boot:run -pl study-ai-best-practice-exception-analysis

# 或进入子模块目录运行
cd study-ai-best-practice-exception-analysis
mvn spring-boot:run
```

### 3. API 调用

**分析问题**：
```bash
curl -X POST http://localhost:8080/api/analysis/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "problemDescription": "用户登录时出现 NullPointerException",
    "errorMessage": "Cannot invoke method on null object",
    "serviceName": "user-service",
    "environment": "production"
  }'
```

**基于反馈重新分析**：
```bash
curl -X POST http://localhost:8080/api/analysis/reanalyze/{analysisId} \
  -H "Content-Type: application/json" \
  -d '{"feedback": "需要查看更多日志信息，特别是数据库连接部分"}'
```

**获取分析结果**：
```bash
curl http://localhost:8080/api/analysis/analysis/{analysisId}
```

**获取最近分析**：
```bash
curl http://localhost:8080/api/analysis/recent
```

**健康检查**：
```bash
curl http://localhost:8080/api/analysis/health
```

## 核心流程

1. **Plan**：生成分析计划，包含多个分析步骤
2. **Execute**：执行计划步骤，调用各种 MCP 工具收集信息
3. **React**：基于收集的信息，使用 AI 分析并生成结论
4. **Feedback**：根据用户反馈，重新分析并优化结论

## MCP 工具

| 工具名称 | 描述 | 输入参数 |
|---------|------|---------|
| `query_logs` | 查询服务日志 | service, startTime, endTime, keywords |
| `query_gitlab` | 检查代码变更 | repository, branch, since, until, filePattern |
| `query_trace` | 分析错误调用链 | traceId, service, operation |
| `query_business` | 应用业务领域知识 | domain, topic |

## 分析步骤

1. **INITIAL_ANALYSIS**：分析问题描述和错误信息
2. **LOG_ANALYSIS**：查询相关日志信息
3. **CODE_ANALYSIS**：检查相关代码变更
4. **TRACE_ANALYSIS**：分析错误调用链
5. **BUSINESS_KNOWLEDGE**：应用业务领域知识
6. **ROOT_CAUSE_ANALYSIS**：识别根本原因
7. **RECOMMENDATION**：生成修复建议

## 示例输出

```json
{
  "analysisId": "uuid",
  "status": "COMPLETED",
  "conclusion": "NullPointerException 是由于 UserService 中缺少对 null 用户 ID 的检查导致",
  "possibleRootCauses": [
    "UserService.getUser 方法未对 null 用户 ID 进行检查",
    "数据库连接可能存在问题",
    "用户认证逻辑存在缺陷"
  ],
  "recommendedActions": [
    "在 UserService.getUser 方法中添加 null 检查",
    "优化数据库连接池配置",
    "增强用户输入验证"
  ],
  "evidence": {
    "aiAnalysis": "...",
    "executedSteps": 7,
    "successfulSteps": 7
  },
  "steps": [
    {
      "id": "step1",
      "type": "INITIAL_ANALYSIS",
      "status": "SUCCESS",
      "result": "分析完成"
    }
  ]
}
```

## 扩展指南

### 添加新的 MCP 工具

实现 `McpTool` 接口：

```java
@Component
public class NewTool implements McpTool {
    @Override
    public String getName() { return "new_tool"; }
    @Override
    public String getDescription() { return "New tool description"; }
    @Override
    public Map<String, String> getInputSchema() { return Map.of(); }
    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        return McpToolResult.success("result");
    }
}
```

### 自定义分析步骤

在 `PlanService.generatePlan()` 方法中添加新步骤：

```java
steps.add(AnalysisStep.start(
    UUID.randomUUID().toString(),
    "CUSTOM_ANALYSIS",
    "自定义分析步骤"
));
```

## License

MIT
