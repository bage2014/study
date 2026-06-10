---
name: "plan-executor"
description: "计划执行器。当用户需要自定义分析计划或执行特定分析步骤时使用此技能。"
---

# 计划执行器

## 功能描述

提供分析计划的生成和执行能力，支持自定义分析流程，协调多步骤有序执行。

## 何时使用

在以下情况调用此技能：
- 自定义分析计划和步骤
- 执行特定的分析流程
- 调整分析策略
- 扩展分析步骤

## 核心功能

- **计划生成**：根据输入请求生成标准分析计划
- **计划执行**：按顺序执行计划中的每个步骤
- **结果汇总**：收集执行结果并生成分析报告
- **计划调整**：基于反馈动态调整分析计划

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| request | AnalysisRequest | 是 | 分析请求对象 |
| steps | List\<AnalysisStep\> | 否 | 自定义步骤列表（可选） |

## 输出格式

```json
{
  "analysisId": "ANALYSIS-1234567890",
  "status": "SUCCESS",
  "steps": [
    {
      "stepId": "step-001",
      "name": "INITIAL_ANALYSIS",
      "description": "分析问题描述",
      "status": "COMPLETED",
      "result": "分析完成"
    }
  ],
  "result": "分析报告内容"
}
```

## 执行流程

```
输入请求 → 生成计划 → 执行步骤 → 收集结果 → 生成报告
```

### 标准分析步骤

| 步骤名称 | 描述 | 关联MCP工具 |
|----------|------|-------------|
| INITIAL_ANALYSIS | 分析问题描述和错误信息 | - |
| REQUEST_METRICS | 查询请求量监控数据 | MetricsMcpService |
| APP_METRICS | 查询应用监控数据（CPU、内存、GC、线程） | MetricsMcpService |
| DEPLOYMENT_RECORDS | 查询发布记录 | DeploymentMcpService |
| CODE_ANALYSIS | 检查代码仓库信息 | CodeMcpService |
| TOPOLOGY_ANALYSIS | 分析应用拓扑和依赖 | TopologyMcpService |
| EVIDENCE_COLLECTION | 整合所有证据 | - |
| ROOT_CAUSE_ANALYSIS | 识别根本原因 | - |
| RECOMMENDATION | 生成修复建议 | - |

## 核心组件

| 组件 | 职责 | 文件路径 |
|------|------|----------|
| AnalysisServiceImpl | 核心分析服务，整合所有步骤 | `service/impl/AnalysisServiceImpl.java` |
| DeploymentMcpService | 提供发布记录数据 | `mcp/DeploymentMcpService.java` |
| MetricsMcpService | 提供监控指标数据 | `mcp/MetricsMcpService.java` |
| CodeMcpService | 提供代码仓库信息 | `mcp/CodeMcpService.java` |
| TopologyMcpService | 提供应用拓扑数据 | `mcp/TopologyMcpService.java` |

## 使用示例

```java
// 生成分析请求
AnalysisRequest request = new AnalysisRequest();
request.setAppId("app-001");
request.setAlarmDescription("下单接口错误率升高");

// 执行分析
AnalysisResponse response = analysisService.analyze(request);

// 获取结果
RootCause rootCause = response.getRootCause();
List<Evidence> evidences = response.getEvidences();
List<String> suggestions = response.getSuggestions();
```

## 扩展指南

### 添加自定义步骤

1. 在分析流程中添加新的步骤调用
2. 创建对应的 MCP 服务（如果需要）
3. 在证据收集方法中集成新数据
4. 在根因分析方法中添加新的判断逻辑

### 调整执行顺序

修改 `AnalysisServiceImpl.analyze()` 方法中的步骤调用顺序。

## 配置要求

无需额外配置，依赖于异常分析模块的基础配置。

## 日志记录

执行过程中记录以下关键日志：
- **计划开始**: 记录分析ID和输入参数
- **步骤执行**: 记录每个步骤的开始和完成
- **结果汇总**: 记录分析结果和根因判定