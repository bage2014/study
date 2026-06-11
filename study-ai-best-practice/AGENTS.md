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

### 3. 研发流程代理 (Development Process Agent)

#### 职责
- 支持研发流程各阶段的自动化
- 提供规范化的研发实践指导
- 生成相关文档和代码模板

#### 核心能力
- **需求澄清**：帮助明确和细化业务需求
- **代码生成**：生成符合规范的代码
- **测试自动化**：生成单元测试和 UI 测试
- **契约管理**：生成 API 契约文档

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

### 6. common-requirement-clarification 技能

**位置**: `.trae/skills/common-requirement-clarification/SKILL.md`

**功能**: 需求澄清技能，帮助团队明确和细化业务需求

**核心功能**:
- 需求分解：将模糊需求分解为具体、可测试的用户故事
- 疑问收集：识别需求中的模糊点和待确认项
- 验收标准定义：帮助定义清晰的验收标准
- 依赖分析：识别需求之间的依赖关系
- 风险评估：评估需求实现的技术风险和业务风险

---

### 7. common-coding 技能

**位置**: `.trae/skills/common-coding/SKILL.md`

**功能**: 编码技能，提供代码编写指导

**核心功能**:
- 代码生成：根据需求生成符合规范的代码
- 代码审查：检查代码是否符合编码规范
- 代码优化：提供代码优化建议
- 文档生成：生成代码文档和注释
- 设计模式推荐：根据场景推荐合适的设计模式

---

### 8. common-backend-unit-test 技能

**位置**: `.trae/skills/common-backend-unit-test/SKILL.md`

**功能**: 后端单元测试技能，提供后端服务的单元测试指导

**核心功能**:
- 测试生成：根据代码生成单元测试用例
- 测试审查：检查测试代码质量和覆盖率
- Mock生成：生成Mock对象和桩代码
- 测试优化：提供测试优化建议
- 报告生成：生成测试覆盖率报告

---

### 9. common-frontend-playwright-test 技能

**位置**: `.trae/skills/common-frontend-playwright-test/SKILL.md`

**功能**: 前端 Playwright 测试技能，提供 UI 自动化测试能力

**核心功能**:
- 测试生成：根据页面结构生成测试用例
- 定位器生成：生成元素定位器
- 测试审查：检查测试代码质量
- 测试优化：提供测试优化建议
- 报告生成：生成测试报告和截图

---

### 10. common-contract-generation 技能

**位置**: `.trae/skills/common-contract-generation/SKILL.md`

**功能**: 契约生成技能，提供 API 契约文档的生成和管理能力

**核心功能**:
- 契约设计：设计 RESTful API 接口契约
- 文档生成：生成 OpenAPI/Swagger 文档
- 契约验证：验证接口实现与契约的一致性
- SDK生成：生成客户端 SDK 和类型定义
- Mock生成：生成 Mock 数据和服务器

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

###