---
name: "exception-analysis"
description: "异常分析核心功能。当用户需要分析线上故障、排查错误原因时使用此技能。"
---

# 异常分析技能

此技能提供完整的异常分析能力，基于 Plan-Execute-React 模式。

## 何时使用

在以下情况调用此技能：
- 分析线上故障和错误
- 排查系统异常原因
- 生成根因分析报告
- 基于用户反馈优化分析结果

## 核心功能

- **问题分析**：输入问题描述和错误信息，生成完整分析报告
- **多轮验证**：基于用户反馈重新分析
- **AI 驱动**：使用大模型生成根因分析和修复建议
- **结构化输出**：包含可能的根本原因和修复建议

## API 调用

```bash
# 分析问题
curl -X POST http://localhost:8080/api/analysis/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "problemDescription": "用户登录失败",
    "errorMessage": "NullPointerException"
  }'

# 重新分析
curl -X POST http://localhost:8080/api/analysis/reanalyze/{analysisId} \
  -H "Content-Type: application/json" \
  -d '{"feedback": "需要更多日志信息"}'
```

## 分析流程

1. **计划生成**：创建分析步骤计划
2. **执行收集**：调用 MCP 工具收集信息
3. **结果分析**：使用 AI 分析并生成结论
4. **反馈优化**：根据用户反馈重新分析

## 配置

需要配置 DeepSeek API Key：

- 环境变量：`DEEPSEEK_API_KEY=your-key`
- 配置文件：`application.yml` 中的 `spring.ai.openai.api-key`

## 扩展点

- 可添加新的分析步骤
- 可集成真实的 MCP 服务
- 可扩展支持其他 AI 模型
- 可生成项目变更文档

## 项目变更文档

此技能支持生成项目变更文档，记录系统优化和功能变更。

### 文档格式

变更文档命名格式：`{模块名}-更新说明-{日期}.md`

例如：`exception-analysis-更新说明-2026-05-05.md`

### 文档内容

变更文档应包含以下内容：

- **变更概述**：简要描述本次变更的目的和范围
- **详细变更**：具体的代码修改和功能调整
- **影响范围**：变更可能影响的模块和功能
- **部署说明**：部署步骤和注意事项
- **测试建议**：验证变更的测试方法

### 使用方式

1. 执行变更操作
2. 调用此技能生成变更文档
3. 提交变更文档到版本控制系统
4. 在团队内部分享变更内容
