# 技能清单

## 概述

本项目包含多个技能模块，用于支持 AI 驱动的异常分析、聊天功能和研发流程自动化。所有技能统一存放在根模块的 `.trae/skills/` 目录下进行集中管理。

---

## 技能目录

| 技能名称 | 所属模块 | 功能描述 | 文件路径 |
|----------|----------|----------|----------|
| `exception-analysis` | exception-analysis | 异常分析核心功能 | `.trae/skills/exception-analysis/SKILL.md` |
| `mcp-tool-creator` | exception-analysis | MCP工具创建与管理 | `.trae/skills/mcp-tool-creator/SKILL.md` |
| `plan-executor` | exception-analysis | 分析计划执行器 | `.trae/skills/plan-executor/SKILL.md` |
| `spring-ai-chat` | hello | AI聊天能力 | `.trae/skills/spring-ai-chat/SKILL.md` |
| `model-provider-creator` | hello | AI模型提供者管理 | `.trae/skills/model-provider-creator/SKILL.md` |
| `common-requirement-clarification` | common | 需求澄清能力 | `.trae/skills/common-requirement-clarification/SKILL.md` |
| `common-coding` | common | 编码指导能力 | `.trae/skills/common-coding/SKILL.md` |
| `common-backend-unit-test` | common | 后端单元测试能力 | `.trae/skills/common-backend-unit-test/SKILL.md` |
| `common-frontend-playwright-test` | common | 前端Playwright测试能力 | `.trae/skills/common-frontend-playwright-test/SKILL.md` |
| `common-contract-generation` | common | API契约生成能力 | `.trae/skills/common-contract-generation/SKILL.md` |
| `personal-backend-coding-standard` | personal | 后端编码规范指导 | `.trae/skills/personal-backend-coding-standard/SKILL.md` |
| `personal-frontend-coding-standard` | personal | 前端编码规范指导 | `.trae/skills/personal-frontend-coding-standard/SKILL.md` |

---

## 技能分类

### 异常分析类

| 技能名称 | 功能描述 |
|----------|----------|
| `exception-analysis` | 提供完整的异常分析能力，基于多源数据进行根因分析 |
| `mcp-tool-creator` | 创建和管理 MCP 工具，扩展数据收集能力 |
| `plan-executor` | 执行分析计划，协调多步骤有序执行 |

### AI 聊天类

| 技能名称 | 功能描述 |
|----------|----------|
| `spring-ai-chat` | 提供 AI 聊天交互能力，支持多种大语言模型 |
| `model-provider-creator` | 管理和配置 AI 模型提供者 |

### 研发流程类 (common-*)

| 技能名称 | 功能描述 |
|----------|----------|
| `common-requirement-clarification` | 需求澄清技能，帮助团队明确和细化业务需求 |
| `common-coding` | 编码技能，提供代码编写指导和最佳实践 |
| `common-backend-unit-test` | 后端单元测试技能，提供测试指导和生成能力 |
| `common-frontend-playwright-test` | 前端 Playwright 测试技能，提供 UI 自动化测试能力 |
| `common-contract-generation` | 契约生成技能，提供 API 契约文档的生成和管理能力 |

### 个人技能类 (personal-*)

| 技能名称 | 功能描述 |
|----------|----------|
| `personal-backend-coding-standard` | 后端编码规范技能，提供后端开发的编码标准和最佳实践指导 |
| `personal-frontend-coding-standard` | 前端编码规范技能，提供前端开发的编码标准和最佳实践指导 |

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

### 6. common-requirement-clarification

**文件**: `.trae/skills/common-requirement-clarification/SKILL.md`

**功能**: 需求澄清技能

**核心功能**:
- 需求分解：将模糊需求分解为具体、可测试的用户故事
- 疑问收集：识别需求中的模糊点和待确认项
- 验收标准定义：帮助定义清晰的验收标准
- 依赖分析：识别需求之间的依赖关系
- 风险评估：评估需求实现的技术风险和业务风险

**使用场景**:
- 需求文档模糊或不完整时
- 需要确认需求的业务背景和目标
- 需要细化需求的验收标准

---

### 7. common-coding

**文件**: `.trae/skills/common-coding/SKILL.md`

**功能**: 编码技能

**核心功能**:
- 代码生成：根据需求生成符合规范的代码
- 代码审查：检查代码是否符合编码规范
- 代码优化：提供代码优化建议
- 文档生成：生成代码文档和注释
- 设计模式推荐：根据场景推荐合适的设计模式

**使用场景**:
- 需要编写新代码时
- 需要审查代码质量时
- 需要优化现有代码时

---

### 8. common-backend-unit-test

**文件**: `.trae/skills/common-backend-unit-test/SKILL.md`

**功能**: 后端单元测试技能

**核心功能**:
- 测试生成：根据代码生成单元测试用例
- 测试审查：检查测试代码质量和覆盖率
- Mock生成：生成Mock对象和桩代码
- 测试优化：提供测试优化建议
- 报告生成：生成测试覆盖率报告

**使用场景**:
- 需要为后端代码编写单元测试时
- 需要评估测试覆盖率时
- 需要优化现有测试时

---

### 9. common-frontend-playwright-test

**文件**: `.trae/skills/common-frontend-playwright-test/SKILL.md`

**功能**: 前端 Playwright 测试技能

**核心功能**:
- 测试生成：根据页面结构生成测试用例
- 定位器生成：生成元素定位器
- 测试审查：检查测试代码质量
- 测试优化：提供测试优化建议
- 报告生成：生成测试报告和截图

**使用场景**:
- 需要编写前端 UI 自动化测试时
- 需要测试用户交互流程时
- 需要验证页面功能正确性时

---

### 10. common-contract-generation

**文件**: `.trae/skills/common-contract-generation/SKILL.md`

**功能**: 契约生成技能

**核心功能**:
- 契约设计：设计 RESTful API 接口契约
- 文档生成：生成 OpenAPI/Swagger 文档
- 契约验证：验证接口实现与契约的一致性
- SDK生成：生成客户端 SDK 和类型定义
- Mock生成：生成 Mock 数据和服务器

**使用场景**:
- 需要设计新的 API 接口时
- 需要生成 OpenAPI/Swagger 文档时
- 需要验证契约一致性时

---

## 技能协作关系

```
用户请求
    │
    ├── 异常分析请求
    │       │
    │       ▼
    │   ┌──────────────────────────────┐
    │   │     exception-analysis       │
    │   │    (异常分析主技能)          │
    │   └───────────┬──────────────────┘
    │               │
    │       ┌──────┼──────┐
    │       ▼      ▼      ▼
    │   ┌───────┐ ┌─────┐ ┌──────────┐
    │   │plan-  │ │mcp- │ │spring-   │
    │   │executor│ │tool-│ │ai-chat   │
    │   │       │ │creator││          │
    │   └───────┘ └─────┘ └──────────┘
    │
    └── 研发流程请求
            │
            ├─→ common-requirement-clarification
            ├─→ common-coding
            ├─→ common-backend-unit-test
            ├─→ common-frontend-playwright-test
            └─→ common-contract-generation
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
| 6 | common-* | 研发流程相关请求 |

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
├── spring-ai-chat/
│   └── SKILL.md
├── common-requirement-clarification/
│   └── SKILL.md
├── common-coding/
│   └── SKILL.md
├── common-backend-unit-test/
│   └── SKILL.md
├── common-frontend-playwright-test/
│   └── SKILL.md
├── common-contract-generation/
│   └── SKILL.md
├── personal-backend-coding-standard/
│   └── SKILL.md
└── personal-frontend-coding-standard/
    └── SKILL.md
```

---

## 扩展技能指南

### 创建新技能步骤

1. **创建技能目录**: 在 `.trae/skills/` 下创建新目录
2. **编写 SKILL.md**: 定义技能名称、描述、触发条件、输入输出
3. **实现技能逻辑**: 创建对应的服务类和Controller（如需要）
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