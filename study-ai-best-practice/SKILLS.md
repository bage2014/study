# 技能清单

## 概述

本项目包含多个技能模块，用于支持 AI 驱动的异常分析和聊天功能。所有技能统一存放在根模块的 `.trae/skills/` 目录下进行集中管理。

---

## 技能目录

| 技能名称 | 所属模块 | 功能描述 | 文件路径 |
|----------|----------|----------|----------|
| `exception-analysis` | exception-analysis | 异常分析核心功能 | `.trae/skills/exception-analysis/SKILL.md` |
| `mcp-tool-creator` | exception-analysis | MCP工具创建与管理 | `.trae/skills/mcp-tool-creator/SKILL.md` |
| `plan-executor` | exception-analysis | 分析计划执行器 | `.trae/skills/plan-executor/SKILL.md` |
| `spring-ai-chat` | hello | AI聊天能力 | `.trae/skills/spring-ai-chat/SKILL.md` |
| `model-provider-creator` | hello | AI模型提供者管理 | `.trae/skills/model-provider-creator/SKILL.md` |

---

## 技能详细说明

### 1. exception-analysis

**文件**: `.trae/skills/exception-analysis/SKILL.md`

**功能**: 提供完整的异常分析能力

**触发条件**:
- 用户询问故障分析相关问题
- 需要排查系统异常原因
- 请求生成根因分析报告

**输入**:
```json
{
  "appId": "应用ID",
  "alarmDescription": "告警描述",
  "alarmUrl": "告警链接(可选)",
  "alarmTime": "告警时间(可选)",
  "scene": "场景参数(可选)"
}
```

**输出**: 结构化分析报告（根因、证据、建议）

---

### 2. mcp-tool-creator

**文件**: `.trae/skills/mcp-tool-creator/SKILL.md`

**功能**: 创建和管理 MCP（Model Context Provider）工具

**支持的 MCP 服务**:
- **DeploymentMcpService**: 发布记录数据
- **MetricsMcpService**: 监控指标数据
- **CodeMcpService**: 代码仓库信息
- **TopologyMcpService**: 应用拓扑数据

**使用场景**:
- 需要模拟上下文数据时
- 需要扩展新的数据源时

---

### 3. plan-executor

**文件**: `.trae/skills/plan-executor/SKILL.md`

**功能**: 执行分析计划

**执行流程**:
1. 解析分析请求
2. 生成执行计划（步骤列表）
3. 按顺序执行各步骤
4. 收集执行结果
5. 汇总生成报告

**核心价值**: 协调多步骤分析流程，确保有序执行

---

### 4. spring-ai-chat

**文件**: `.trae/skills/spring-ai-chat/SKILL.md`

**功能**: AI聊天交互能力

**支持模型**:
- OpenAI GPT
- DeepSeek
- 其他 Spring AI 兼容模型

**使用场景**:
- 需要与大模型交互时
- 需要生成自然语言回复时
- 需要 AI 辅助分析时

---

### 5. model-provider-creator

**文件**: `.trae/skills/model-provider-creator/SKILL.md`

**功能**: AI模型提供者配置管理

**配置方式**:
- 环境变量配置
- YAML配置文件
- 动态配置更新

**核心价值**: 统一管理多个AI模型，支持灵活切换

---

## 技能协作关系

```
用户请求
    │
    ▼
┌──────────────────────────────────┐
│      exception-analysis          │
│  (异常分析主技能)                 │
└───────────────┬──────────────────┘
                │
        ┌──────┼──────┐
        ▼      ▼      ▼
┌─────────┐ ┌───────┐ ┌──────────┐
│ plan-   │ │  mcp- │ │ spring-  │
│ executor│ │ tool- │ │ ai-chat  │
│         │ │creator│ │          │
└─────────┘ └───────┘ └──────────┘
```

---

## 技能调用优先级

| 优先级 | 技能 | 触发场景 |
|--------|------|----------|
| 1 | exception-analysis | 故障分析请求 |
| 2 | plan-executor | 需要执行多步骤计划 |
| 3 | mcp-tool-creator | 需要获取上下文数据 |
| 4 | spring-ai-chat | 需要AI生成回复 |
| 5 | model-provider-creator | 需要配置模型 |

---

## 技能目录结构

```
.trae/skills/
├── exception-analysis/
│   └── SKILL.md
├── mcp-tool-creator/
│   └── SKILL.md
├── model-provider-creator/
│   └── SKILL.md
├── plan-executor/
│   └── SKILL.md
└── spring-ai-chat/
    └── SKILL.md
```

---

## 扩展技能指南

### 创建新技能步骤

1. **创建技能目录**: 在 `.trae/skills/` 下创建新目录
2. **编写 SKILL.md**: 定义技能名称、描述、触发条件、输入输出
3. **实现技能逻辑**: 创建对应的服务类和Controller
4. **注册技能**: 在 `AGENTS.md` 和 `SKILLS.md` 中添加技能说明

### SKILL.md 模板

```markdown
---
name: "skill-name"
description: "技能描述"
---

# 技能名称

## 功能描述

## 何时使用

## 核心功能

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| param1 | String | 是 | 说明 |

## 输出格式

## API调用示例（或使用模式）

## 核心组件

## 配置要求

## 扩展指南
```

---

## 技能管理

### 添加新技能

1. 创建目录: `.trae/skills/new-skill/`
2. 创建文件: `.trae/skills/new-skill/SKILL.md`
3. 更新: `AGENTS.md` - 添加技能说明
4. 更新: `SKILLS.md` - 添加技能到目录

### 删除技能

1. 删除目录: `.trae/skills/skill-name/`
2. 更新: `AGENTS.md` - 移除技能说明
3. 更新: `SKILLS.md` - 移除技能从目录

### 更新技能

1. 编辑: `.trae/skills/skill-name/SKILL.md`
2. 更新相关文档（如需要）