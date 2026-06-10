# 项目代理与技能架构

## 项目概述

本项目是一个基于 Spring Boot 的 AI 最佳实践学习项目，包含两个核心模块：

| 模块 | 描述 | 技术栈 |
|------|------|--------|
| `study-ai-best-practice-hello` | AI 聊天服务模块 | Spring AI、OpenAI/DeepSeek |
| `study-ai-best-practice-exception-analysis` | 异常分析服务模块 | Spring Boot、MCP 架构 |

---

## 代理架构

### 1. 异常分析代理 (Exception Analysis Agent)

#### 职责
- 接收异常分析请求
- 执行多步骤分析流程
- 整合多源数据进行根因分析
- 生成结构化分析报告和修复建议

#### 核心能力
- **计划生成**：基于告警信息制定分析计划
- **数据收集**：调用各 MCP 服务收集上下文数据
- **根因分析**：基于收集的数据进行智能分析
- **反馈优化**：支持用户反馈后的重新分析

#### 分析流程
```
输入请求 → 计划生成 → 数据收集(MCP) → 根因分析 → 生成报告 → 反馈优化
```

### 2. AI 聊天代理 (AI Chat Agent)

#### 职责
- 处理用户聊天请求
- 集成多种 AI 模型
- 提供流式和非流式响应
- 管理对话状态

#### 核心能力
- **多模型支持**：支持 OpenAI、DeepSeek 等模型
- **参数配置**：支持温度、token 数量等参数
- **流式响应**：支持实时输出模式

---

## 技能列表

### 1. exception-analysis 技能

**位置**: `.trae/skills/exception-analysis/SKILL.md`

**功能**: 提供完整的异常分析能力

**调用场景**:
- 分析线上故障和错误
- 排查系统异常原因
- 生成根因分析报告
- 基于用户反馈优化分析结果

**核心步骤**:
1. 收集请求量监控数据
2. 收集应用监控数据（CPU、内存、GC、线程）
3. 收集发布记录
4. 收集代码仓库信息
5. 模块识别与拓扑分析
6. 收集 MQ 和 JOB 指标
7. 证据整合与干扰项过滤
8. 根因分析与建议生成

---

### 2. mcp-tool-creator 技能

**位置**: `.trae/skills/mcp-tool-creator/SKILL.md`

**功能**: 创建和管理 MCP（Model Context Provider）工具

**支持的 MCP 服务**:
| MCP 服务 | 职责 |
|----------|------|
| `DeploymentMcpService` | 模拟发布记录数据 |
| `MetricsMcpService` | 模拟监控指标数据 |
| `CodeMcpService` | 模拟代码仓库信息 |
| `TopologyMcpService` | 模拟应用拓扑和依赖关系 |

---

### 3. plan-executor 技能

**位置**: `.trae/skills/plan-executor/SKILL.md`

**功能**: 执行分析计划，协调各步骤执行

**执行流程**:
1. 解析分析请求
2. 生成执行计划
3. 按顺序执行各步骤
4. 收集执行结果
5. 汇总并返回分析报告

---

### 4. spring-ai-chat 技能

**位置**: `.trae/skills/spring-ai-chat/SKILL.md`

**功能**: 提供 AI 聊天能力，支持与大模型交互

**支持模型**:
- OpenAI GPT
- DeepSeek
- 其他兼容 Spring AI 的模型

---

### 5. model-provider-creator 技能

**位置**: `.trae/skills/model-provider-creator/SKILL.md`

**功能**: 管理和配置 AI 模型提供者

**配置方式**:
- 环境变量配置
- 配置文件配置
- 动态配置管理

---

## MCP 服务架构

### MCP 服务列表

| 服务类 | 文件路径 | 职责 |
|--------|----------|------|
| `DeploymentMcpService` | `study-ai-best-practice-exception-analysis/mcp/DeploymentMcpService.java` | 提供发布记录数据 |
| `MetricsMcpService` | `study-ai-best-practice-exception-analysis/mcp/MetricsMcpService.java` | 提供监控指标数据 |
| `CodeMcpService` | `study-ai-best-practice-exception-analysis/mcp/CodeMcpService.java` | 提供代码仓库信息 |
| `TopologyMcpService` | `study-ai-best-practice-exception-analysis/mcp/TopologyMcpService.java` | 提供应用拓扑数据 |

### 数据模型

#### 上下文数据模型
- `RequestMetrics`: 请求量监控数据
- `AppMetrics`: 应用监控数据
- `DeploymentRecord`: 发布记录
- `CodeInfo`: 代码仓库信息
- `TopologyInfo`: 应用拓扑信息
- `MqMetrics`: MQ 监控指标
- `JobMetrics`: JOB 监控指标

#### 请求响应模型
- `AnalysisRequest`: 分析请求
- `AnalysisResponse`: 分析响应
- `RootCause`: 根因分析结果
- `Evidence`: 证据信息
- `ChatRequest`: 聊天请求
- `ChatResponse`: 聊天响应

---

## 核心业务逻辑

### 根因分析规则

| 根因类型 | 判断条件 | 置信度 |
|----------|----------|--------|
| MQ积压 | MQ积压且与告警模块相关 | 90% |
| 高错误率 | 错误率 > 10% | 85% |
| CPU过载 | CPU使用率 > 80% | 80% |
| 内存不足 | 内存使用率 > 85% | 75% |
| 模块异常 | 告警描述涉及特定模块 | 75% |
| 发布问题 | 存在最近发布记录 | 70% |
| 认证问题 | 涉及登录/认证/token | 65% |

### 干扰项过滤逻辑

1. **模块识别**: 从告警描述中提取涉及的模块（下单、改单、支付、取消、出票、退票、扣位）
2. **干扰项判定**: 其他未涉及模块的错误标记为"干扰项-不相关"
3. **证据关联**: 根据拓扑依赖关系判断 MQ/JOB 指标是否与告警模块相关

---

## API 接口

### 异常分析模块接口

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 分析接口 | POST | `/api/analysis/analyze` | 执行异常分析 |
| Mock分析 | GET | `/api/analysis/analyze/mock` | 使用模拟数据分析 |
| 健康检查 | GET | `/api/analysis/health` | 服务健康检查 |

### AI 聊天模块接口

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 聊天接口 | POST | `/api/chat` | 发送聊天消息 |
| 流式聊天 | POST | `/api/chat/stream` | 流式响应聊天 |

### Mock 场景参数

| 参数值 | 场景描述 |
|--------|----------|
| `default` | 默认场景，随机数据 |
| `mq_backlog` | MQ积压场景 |
| `high_error` | 高错误率场景 |
| `cpu_overload` | CPU过载场景 |
| `memory_low` | 内存不足场景 |

---

## 日志记录

### 核心步骤日志

分析过程中记录以下关键日志：

1. **分析开始**: 记录输入参数
2. **数据收集**: 记录请求量、应用监控、发布记录、代码信息
3. **模块识别**: 记录识别到的模块
4. **拓扑分析**: 记录应用拓扑结构
5. **MQ/JOB指标**: 记录积压和失败情况
6. **证据收集**: 记录证据数量和相关性统计
7. **根因分析**: 记录各假设验证过程和结果
8. **分析完成**: 记录最终结论

### 日志格式

```
[ANALYSIS-xxx] 【步骤1/6】开始收集上下文数据
[ANALYSIS-xxx]  - 请求量监控数据: 总请求数=xxx, 错误率=xx.xx%, 平均响应时间=xx.xxms
[ANALYSIS-xxx] 假设1: MQ积压 - hasMqBacklog=true, isAlarmModuleMqRelated=true
[ANALYSIS-xxx] 假设1成立: MQ(xxx)积压与告警模块相关
```

---

## 配置说明

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `DEEPSEEK_API_KEY` | DeepSeek API Key | - |
| `OPENAI_API_KEY` | OpenAI API Key | - |
| `SERVER_PORT` | 服务端口 | 8080 |

### 配置文件

- 主配置: `application.yml`
- 本地配置: `application-local.yml`（需自行创建）

---

## 扩展指南

### 添加新的根因类型

1. 在 `AnalysisServiceImpl.analyzeRootCause()` 方法中添加新假设
2. 在 `generateSuggestions()` 方法中添加对应建议
3. 更新 `SKILL.md` 文档中的根因类型表

### 添加新的 MCP 服务

1. 创建新的 MCP 服务类，实现数据提供逻辑
2. 在 `AnalysisServiceImpl` 中注入并调用
3. 更新 `collectEvidences()` 方法收集新证据

### 添加新的业务模块

1. 在 `ALL_MODULES` 列表中添加模块名称
2. 在 `generateDisturbanceError()` 方法中添加干扰项错误消息
3. 在 `TopologyMcpService` 中添加模块依赖配置

### 添加新的技能

1. 在 `.trae/skills/` 目录下创建新目录
2. 创建 `SKILL.md` 文件
3. 在 `AGENTS.md` 和 `SKILLS.md` 中注册技能

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