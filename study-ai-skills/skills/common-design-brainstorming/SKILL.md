---
name: "common-design-brainstorming"
description: "设计头脑风暴技能，在创意实现前探索意图、路径分类、渐进式设计决策（包装自 obra/superpowers@brainstorming）"
trigger: "做设计决策、技术方案选型、不确定怎么实现某个功能时"
disable-when: "需求和方案已完全明确，只差编码"
category: "common"
tags: ["brainstorming", "design", "exploration", "decision-making"]
requires: ["common-requirement-clarification", "common-spec-driven", "common-solution-research"]
---

# 设计头脑风暴技能

## 功能描述

在创意实现前，通过结构化对话探索意图、分类任务路径、渐进式达成设计共识，避免直接跳到实现阶段。

## 触发条件

- 做设计决策或技术方案选型时
- 不确定怎么实现某个功能时
- 用户要求"头脑风暴"或"想想方案"时

## 何时使用

- 新功能开发前的方案探索
- 不确定用哪种技术/架构时
- 需求较大需要分步设计时
- 想在动手前先理解清楚时

## 核心原则

**创意工作前必须分类路径。** 不知道用哪条路 = 需要用更重的那条。

## 核心功能

- **任务路径分类**：Spike（探索）/ Bounded（小改动）/ Architectural（大架构）
- **共享理解三步法**：发现意图 → 写回理解 → 带入设计
- **HARD-GATE 实施禁令**：没批准不能写代码
- **渐进式设计**：先分类再探索再实施，逐步收敛
- **红线警告**：识别绕过设计的反模式

## 任务分类详解

收到需求后，**先说出分类**，让人类可以覆盖：

### Spike — 可行性探索

适用场景：
- "能不能做到？"
- "有这种技术吗？"
- "先做个快速验证看看"

输出：**答案 + 建议**，不保留代码。

流程：
1. 理解要探索什么（2-3 句话）
2. 人类点头同意
3. 用最小成本调研
4. 报告发现 + 推荐方案
5. 所有代码标记为可丢弃

### Bounded — 已有代码的小改动

适用场景：
- 修改现有 endpoint、加个 flag、修一个文件里的 bug
- **关键：** 要改的流程已经存在于代码库里

流程：
1. 探索项目上下文（文件、文档、最近提交）
2. 问澄清问题（一次一个，问真正重要的）
3. 在**对话中**给出简短设计（几句话）
4. **停下等待人类明确批准**
5. 批准后开始实现

### Architectural — 新架构/大改动

适用场景：
- 新项目
- 新子系统
- 改变组件交互方式
- 多个独立模块

流程：
1. 探索项目上下文
2. 问澄清问题，理解目的/约束/成功标准
3. 提 2-3 个方案 + 优缺点 + 推荐
4. 分节展示设计，每节确认后继续
5. 写设计文档
6. 文档自审（占位符/一致性/范围/歧义）
7. **人类审书面 Spec**
8. 批准后进入实现计划

## 共享理解三步法

1. **发现意图** — 用户到底要什么？给谁用？成功长啥样？
2. **写回你的理解** — 用一句话总结，区分「用户说的」和「你的假设」
3. **把意图带入设计** — 设计决策要对照这个意图检查

## HARD-GATE 实施禁令

**任何实施动作前（写代码、装依赖、建项目），必须完成当前路径的前置条件：**

- Spike → 人类同意探索方案
- Bounded → 人类同意对话中的简短设计
- Architectural → 人类审完书面 Spec + 审完实现计划

**对话中的设计批准 ≠ 尚未存在的工件也被批准。** 别把一个批准当多个用。

## 红线警告 - 回到路径分类

| 念头 | 真相 |
|------|------|
| "这个太简单不用设计" | Bounded 也需要对话里的简短设计 |
| "这个是 Bounded，跳过 Spec" | 用标签逃避工作就是在逃避——升级路径 |
| "设计很明显，我先写代码他们同时看" | Gate 是批准，不是设计长度。要停下来。 |
| "我懂这种应用，所以 Bounded" | Bounded 看仓库，不是看你熟不熟。新项目 = Architectural |
| "Spike 成功了，代码保留" | Spike 输出是答案，不是代码 |
| "任务变大了但快做完了，别重新分类" | 发现隐藏复杂度时升级路径 |

## 反模式检查清单

- [ ] 没有分类就开始实现？→ 分类先
- [ ] 跳过批准阶段？→ 停下等 yes
- [ ] Spike 代码不想丢？→ 当新请求重新分类
- [ ] 范围膨胀了不升级路径？→ 现在升级

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| request | String | 是 | 要探索的需求/想法 |
| suggestedPath | String | 否 | 建议路径：spike/bounded/architectural |

## 输出格式

```json
{
  "path": "bounded",
  "intent": "用户想要...",
  "assumptions": ["假设1", "假设2"],
  "constraints": ["约束1"],
  "design": {
    "approach": "...",
    "files": ["file1", "file2"],
    "testing": "..."
  },
  "status": "awaiting-approval"
}
```

## 与现有 skill 的关系

- **common-requirement-clarification**：侧重需求澄清，本 skill 侧重设计决策和路径分类，两者互补
- **common-spec-driven**：Architectural 路径最终要输出 Spec
- **common-solution-research**：方案调研可集成到 Bounded/Architectural 路径
- **common-test-driven-development**：批准后实现阶段用 TDD

## 参考来源

原始 skill：`obra/superpowers@brainstorming`
来源仓库：https://github.com/obra/superpowers
安装命令：`npx skills add obra/superpowers@brainstorming -y -g`
