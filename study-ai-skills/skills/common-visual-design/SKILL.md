---
name: "common-visual-design"
description: "视觉设计哲学技能，生成有辨识度的 UI/UX 设计方案，避免模板化和 AI 默认风格（包装自 anthropics/skills@frontend-design）"
trigger: "需要做 UI 界面、后台 Dashboard、产品落地页、或重新设计现有界面时"
disable-when: "界面风格和组件已完全确定，只需要写代码"
category: "common"
tags: ["visual-design", "ui-design", "design-system", "typography", "color-palette"]
requires: ["common-ui-design-spec", "common-ui-mockup", "common-design-brainstorming"]
---

# 视觉设计哲学技能

## 功能描述

以专业设计工作室的标准输出有辨识度的视觉设计，避免 AI 常见的模板化、默认风格问题。先做设计计划，再写代码。

## 触发条件

- 需要做 UI 界面、后台 Dashboard、产品落地页时
- 用户对现有界面不满意需要重新设计时
- 需要有辨识度的品牌化界面时

## 何时使用

- 新项目的 UI 方向确定
- 现有界面改版/重设计
- 想避免 AI 模板化风格时
- 确定主题但不确定设计方向时

## 核心功能

- **设计 Token 系统**：色彩/字体/布局/原则的紧凑方案
- **反模式检查**：识别 5 大 AI 默认风格并避免
- **两阶段流程**：计划 → 审查 → 构建 → 自审
- **排版/动画/内容规范**：字体、行宽、动画克制、文案风格
- **自审机制**：截图检查可访问性和细节

## 核心原则

**两阶段流程：计划 → 审查 → 构建 → 自审**

## 第一步：建立设计基础

### 锚定主题

如果需求没说清楚产品是什么、给谁用、主要做什么——先自己确定这三点。视觉选择来源于主题的行业、材质、语言。一个给 8-11 岁女孩的玩具 App 和一个金融分析师 Dashboard 的设计应该完全不同。

### 设计 Token 系统

先出紧凑的 token 方案，包含：

```yaml
color:
  base: "#F4F1EA"          # 主背景
  surface: "#FFFFFF"       # 卡片背景
  text: "#1A1A1A"          # 主文字
  text-muted: "#6B7280"    # 次要文字
  accent: "#D97757"        # 主题色（4-6 个命名值）

typography:
  family-display: "xxx"    # 标题字体
  family-body: "xxx"       # 正文字体
  scale: "1.125 modular"  # 行高比例

layout:
  max-width: "1280px"
  grid-gap: "24px"
  radius: "2px"            # 不是一刀切的圆角

principles:
  - "内容驱动布局"
  - "结构即信息"
```

## 第二步：审查计划

在写代码前，检查设计方案是否有以下 AI 默认痕迹：

### 要避免的 5 大 AI 默认风格

| 默认风格 | 特征 | 避免方式 |
|----------|------|----------|
| **暖米色+衬线** | #F4F1EA 背景 + 高对比衬线标题 + 赤陶色 accent (#D97757) | 除非主题真的是这种，否则不用 |
| **近黑背景+酸绿** | #0B0B0B/#111 背景 + 单一高饱和 accent | 避免暗黑模式的默认实现 |
| **报版式** | 发丝线边框、零圆角、密集新闻式列 | 仅在内容是密集阅读物时用 |
| **SaaS 卡片** | 统一圆角卡片、柔和灰色阴影 (rgba(0,0,0,.1))、渐变装饰 | 打破统一圆角，用层级区分 |
| **模板化 Chrome** | ALL-CAPS eyebrow label、`A · B · C` 分隔、`→` 追加在按钮 | 只有内容真需要时才用 |

### 通用反模式

- 标题中只给一个词斜体/加粗/改色（AI 默认手法）
- ALL-CAPS 做标签
- 不必要的装饰性标签在内容上方
- 非真实序列用编号标记 01/02/03（只有真的是流程时才用）
- 每段 fade-and-slide-up 的入场动画
- 每页都加 hover 过渡效果

## 第三步：构建实现

### 排版

- 默认行宽 < 80 字符（衬线可稍长）
- 一个项目用 1-2 个字体，两个就必须明显不同
- 标题字体重量和宽度要刻意选择，不是默认的
- 行高：衬线 > 无衬线

### 动画

- **非触发动画要克制**：一个编排好的入口动画比散乱的效果好
- 对用户操作的响应动画可以有（展开、确认等）
- 尊重 `prefers-reduced-motion`

### CSS 选择器

- 避免 `.section` + `.cta` 这种通用类互相覆盖
- 层级和 padding/margin 要明确
- 类名要具体

### 内容

- 用真实内容，不要用 Lorem ipsum
- 文案从终端用户视角写
- 用户管理「通知」，不是管理「webhook 配置」
- 主动语态："保存更改"，不是"提交"
- 同一操作始终同一名字

## 第四步：自审

- **大胆用在一个地方，其他克制**。一个记忆点，周围安静。
- 响应式到移动端
- 键盘 focus 可见
- 色彩可访问（对比度）
- 代码示例前先截图自审——一图胜千 token

## 设计流程

```
主题分析 → Token 系统 → 检查反模式 → 写代码 → 截图自审 → 交付
```

## 与现有 skill 的关系

- **common-ui-design-spec**：本 skill 是设计哲学层，那个是规范层
- **common-ui-mockup**：原型图可以先用本 skill 出设计方向
- **common-vue-init** / 前端开发 skill：设计确认后再选框架实现

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| subject | String | 是 | 产品/页面主题（"金融分析师 Dashboard"、"游戏官网"等） |
| audience | String | 否 | 目标用户 |
| goal | String | 否 | 页面主要目的 |
| constraints | String | 否 | 约束条件（品牌色、浏览器兼容等） |

## 输出格式

两部分：

**设计计划（YAML）：**
```yaml
color: {...}
typography: {...}
layout: {...}
principles: [...]
avoided-defaults: [...]  # 明确说了避免了哪些 AI 默认风格
```

**设计方向说明：** 简短的设计思路描述 + ASCII 线框。

## 参考来源

原始 skill：`anthropics/skills@frontend-design`
来源仓库：https://github.com/anthropics/skills
安装命令：`npx skills add anthropics/skills@frontend-design -y -g`
