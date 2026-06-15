---
name: "exception-analysis"
description: "异常分析核心功能。当用户需要分析线上故障、排查错误原因时使用此技能。"
---

# 异常分析技能

## 功能描述

提供完整的异常分析能力，基于 Plan-Execute-React 模式，整合多源数据进行智能根因分析，生成结构化分析报告和修复建议。

## 何时使用

在以下情况调用此技能：
- 分析线上故障和错误
- 排查系统异常原因
- 生成根因分析报告
- 基于用户反馈优化分析结果

## 核心功能

- **问题分析**：输入问题描述和错误信息，生成完整分析报告
- **多轮验证**：基于用户反馈重新分析
- **结构化输出**：包含可能的根本原因和修复建议
- **多源数据收集**：整合发布记录、监控数据、代码信息、拓扑依赖
- **干扰项过滤**：识别并标记与告警无关的干扰证据

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| appId | String | 是 | 应用ID |
| alarmDescription | String | 是 | 告警描述 |
| alarmUrl | String | 否 | 告警链接 |
| alarmTime | DateTime | 否 | 告警时间 |
| scene | String | 否 | 场景参数（default/mq_backlog/high_error/cpu_overload/memory_low） |

## 输出格式

```json
{
  "analysisId": "ANALYSIS-1234567890",
  "appId": "app-001",
  "alarmDescription": "用户登录失败率突然升高",
  "analysisTime": "2024-01-15T10:35:00",
  "rootCause": {
    "type": "高错误率",
    "description": "当前错误率超过阈值",
    "confidence": "85%"
  },
  "evidences": [
    {
      "source": "发布记录",
      "content": "最近发布版本: 1.2.3",
      "relevance": "高"
    }
  ],
  "suggestions": ["检查服务日志", "查看失败请求"]
}
```

## 分析流程

```
输入请求 → 模块识别 → 数据收集(MCP) → 证据整合 → 根因分析 → 生成报告
```

### 详细步骤

1. **模块识别**：从告警描述中提取涉及的业务模块（下单、改单、支付、取消、出票、退票、扣位）
2. **数据收集**：调用各 MCP 服务收集上下文数据
   - 发布记录 MCP
   - 请求量监控 MCP
   - 应用监控 MCP（CPU、内存、GC、线程等）
   - 代码仓库 MCP
   - 拓扑依赖 MCP（MQ、JOB等）
3. **证据整合**：整合所有收集的数据，标记干扰项
4. **根因分析**：基于多源数据判断根因类型
5. **建议生成**：根据根因类型生成修复建议

## 根因分析规则

| 根因类型 | 判断条件 | 置信度 |
|----------|----------|--------|
| MQ积压 | MQ积压且与告警模块相关 | 90% |
| 高错误率 | 错误率 > 10% | 85% |
| CPU过载 | CPU使用率 > 80% | 80% |
| 内存不足 | 内存使用率 > 85% | 75% |
| 模块异常 | 告警描述涉及特定模块 | 75% |
| 发布问题 | 存在最近发布记录 | 70% |
| 认证问题 | 涉及登录/认证/token | 65% |

## API 调用示例

```bash
# 分析问题
curl -X POST http://localhost:8080/api/analysis/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "appId": "app-001",
    "alarmDescription": "下单接口错误率异常升高",
    "alarmUrl": "https://monitor.example.com/alarm/123",
    "alarmTime": "2024-01-15T10:30:00"
  }'

# Mock分析（支持场景参数）
curl "http://localhost:8080/api/analysis/analyze/mock?scene=mq_backlog"

# 健康检查
curl http://localhost:8080/api/analysis/health
```

## 相关文件

| 文件路径 | 描述 |
|----------|------|
| `service/impl/AnalysisServiceImpl.java` | 核心分析逻辑实现 |
| `controller/AnalysisController.java` | REST API 控制层 |
| `mcp/DeploymentMcpService.java` | 发布记录 MCP |
| `mcp/MetricsMcpService.java` | 监控指标 MCP |
| `mcp/CodeMcpService.java` | 代码仓库 MCP |
| `mcp/TopologyMcpService.java` | 拓扑依赖 MCP |
| `context/` | 上下文数据模型 |
| `dto/` | 请求响应 DTO |

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `DEEPSEEK_API_KEY` | DeepSeek API Key | - |
| `OPENAI_API_KEY` | OpenAI API Key | - |
| `SERVER_PORT` | 服务端口 | 8080 |

### 配置文件

```yaml
# application.yml
server:
  port: ${SERVER_PORT:8080}
```

## 扩展指南

### 添加新的根因类型

1. 在 `AnalysisServiceImpl.analyzeRootCause()` 方法中添加新假设判断
2. 在 `generateSuggestions()` 方法中添加对应修复建议
3. 更新本文档中的根因类型表

### 添加新的业务模块

1. 在 `ALL_MODULES` 列表中添加模块名称
2. 在 `generateDisturbanceError()` 方法中添加干扰项错误消息
3. 在 `TopologyMcpService` 中添加模块依赖配置

### 添加新的 MCP 服务

1. 创建新的 MCP 服务类，实现数据提供逻辑
2. 在 `AnalysisServiceImpl` 中注入并调用
3. 更新 `collectEvidences()` 方法收集新证据

## 日志记录

分析过程中记录以下关键日志：

- **分析开始**: 记录输入参数
- **数据收集**: 记录请求量、应用监控、发布记录、代码信息
- **模块识别**: 记录识别到的模块
- **拓扑分析**: 记录应用拓扑结构
- **MQ/JOB指标**: 记录积压和失败情况
- **证据收集**: 记录证据数量和相关性统计
- **根因分析**: 记录各假设验证过程和结果
- **分析完成**: 记录最终结论