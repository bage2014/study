---
name: "plan-executor"
description: "计划执行器。当用户需要自定义分析计划或执行特定分析步骤时使用此技能。"
---

# 计划执行器

此技能提供分析计划的生成和执行能力。

## 何时使用

在以下情况调用此技能：
- 自定义分析计划和步骤
- 执行特定的分析流程
- 调整分析策略

## 核心组件

- **PlanService**：生成和调整分析计划
- **ExecuteService**：执行计划步骤
- **ReactService**：分析执行结果

## 计划生成

```java
// 生成标准分析计划
List<AnalysisStep> plan = planService.generatePlan(request);

// 基于反馈调整计划
List<AnalysisStep> adjustedPlan = planService.adjustPlan(currentSteps, feedback);
```

## 计划执行

```java
// 执行计划
List<AnalysisStep> executedPlan = executeService.executePlan(plan);

// 分析结果
ProblemAnalysisResponse response = reactService.analyzeResults(request, executedPlan);
```

## 分析步骤

标准分析计划包含以下步骤：

1. **INITIAL_ANALYSIS**：分析问题描述和错误信息
2. **LOG_ANALYSIS**：查询相关日志信息
3. **CODE_ANALYSIS**：检查相关代码变更
4. **TRACE_ANALYSIS**：分析错误调用链
5. **BUSINESS_KNOWLEDGE**：应用业务领域知识
6. **ROOT_CAUSE_ANALYSIS**：识别根本原因
7. **RECOMMENDATION**：生成修复建议

## 自定义步骤

在 `PlanService.generatePlan()` 方法中添加自定义步骤：

```java
steps.add(AnalysisStep.start(
    UUID.randomUUID().toString(),
    "CUSTOM_STEP",
    "自定义分析步骤"
));
```

## 执行引擎

执行引擎会根据步骤类型调用相应的 MCP 工具：

- **LOG_ANALYSIS** → `query_logs` 工具
- **CODE_ANALYSIS** → `query_gitlab` 工具
- **TRACE_ANALYSIS** → `query_trace` 工具
- **BUSINESS_KNOWLEDGE** → `query_business` 工具

## 文件位置

核心服务文件：
- `study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exceptionanalysis/service/PlanService.java`
- `study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exceptionanalysis/service/ExecuteService.java`
- `study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exceptionanalysis/service/ReactService.java`
