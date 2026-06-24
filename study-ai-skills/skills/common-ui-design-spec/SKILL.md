---
name: "common-ui-design-spec"
description: "UI设计规范技能，提供设计系统、配色方案、排版规则、无障碍标准等专业UI设计指导"
trigger: "需要创建或遵循UI设计规范时"
disable-when: "已有成熟的设计系统且无需变更时"
category: "common"
tags: ["ui", "design", "spec", "system", "accessibility", "wcag"]
---

# common-ui-design-spec

## 功能描述

UI设计规范技能提供专业的设计系统指导，包括配色方案、排版规则、间距系统、组件规范、无障碍标准等。基于业界最佳实践，帮助团队建立统一的视觉语言和交互规范。

## 触发条件

- 新项目需要建立设计系统时
- 需要为产品定义配色方案时
- 需要确保无障碍设计合规时
- 需要统一组件样式和交互规范时

## 何时使用

- 项目初始化阶段需要定义设计规范时
- 开发过程中需要参考设计规范时
- 代码审查时需要检查设计合规性时
- 需要生成设计文档时

## 何时不使用

- 已有完善的设计系统且无需调整时
- 仅需要简单的页面修改时
- 用户明确不需要设计规范时

## 核心功能

### 1. 设计系统定义

- 颜色系统（主色、辅色、中性色、语义色）
- 排版系统（字体、字号、行高、字重）
- 间距系统（统一的间距令牌）
- 阴影系统（层级和深度）
- 圆角系统（统一的圆角大小）

### 2. 配色方案

- 行业适配的配色方案推荐
- 对比度检查（WCAG 2.1 AA/AAA）
- 配色方案生成器
- 深色模式支持

### 3. 排版规则

- 字体选择和组合
- 字号层级系统
- 行高和字间距
- 可读性最佳实践

### 4. 组件规范

- 按钮、输入框、卡片等组件规范
- 状态设计（正常、悬停、禁用、错误）
- 交互模式和动画效果
- 响应式设计规则

### 5. 无障碍标准

- WCAG 2.2 合规检查
- 键盘导航支持
- 屏幕阅读器兼容性
- 颜色对比度要求

## 设计系统结构

### 颜色系统

```
颜色分类：
├── Primary（主色）- 品牌识别色
├── Secondary（辅色）- 辅助功能色
├── Neutral（中性色）- 文本和背景
├── Semantic（语义色）- 成功/警告/错误/信息
└── Accent（强调色）- 点缀和突出
```

**颜色规范示例**：
| 颜色 | 用途 | 十六进制 |
|------|------|----------|
| Primary | 品牌主色 | #0052CC |
| Primary Light | 主色浅版 | #E5F0FF |
| Secondary | 辅色 | #F2F4F7 |
| Success | 成功状态 | #36B37E |
| Warning | 警告状态 | #FFAB00 |
| Error | 错误状态 | #E53935 |
| Info | 信息提示 | #177FFF |

### 排版系统

```
字号层级：
├── Display 1 - 超大标题（48px）
├── Display 2 - 大标题（36px）
├── Heading 1 - 一级标题（24px）
├── Heading 2 - 二级标题（20px）
├── Heading 3 - 三级标题（18px）
├── Body Large - 大正文（16px）
├── Body - 正文（14px）
├── Body Small - 小正文（12px）
└── Caption - 说明文字（11px）
```

**排版规范**：
| 元素 | 字号 | 行高 | 字重 |
|------|------|------|------|
| 标题 H1 | 24px | 1.2 | 600 |
| 标题 H2 | 20px | 1.3 | 600 |
| 正文 | 14px | 1.5 | 400 |
| 小文字 | 12px | 1.4 | 400 |

### 间距系统

```
间距令牌（基于4px基准）：
├── 0 - 0px
├── xs - 4px
├── sm - 8px
├── md - 16px
├── lg - 24px
├── xl - 32px
├── 2xl - 48px
└── 3xl - 64px
```

### 阴影系统

| 层级 | 用途 | CSS值 |
|------|------|-------|
| Shadow 0 | 无阴影 | none |
| Shadow 1 | 卡片悬浮 | 0 2px 4px rgba(0,0,0,0.06) |
| Shadow 2 | 卡片默认 | 0 4px 12px rgba(0,0,0,0.08) |
| Shadow 3 | 弹窗 | 0 8px 24px rgba(0,0,0,0.12) |
| Shadow 4 | 模态框 | 0 16px 48px rgba(0,0,0,0.16) |

### 圆角系统

| 大小 | 用途 | CSS值 |
|------|------|-------|
| none | 直角 | 0 |
| sm | 小圆角 | 4px |
| md | 默认圆角 | 8px |
| lg | 大圆角 | 12px |
| xl | 超大圆角 | 16px |
| full | 圆形 | 9999px |

## 无障碍标准（WCAG 2.2）

### 四大原则（POUR）

| 原则 | 描述 | 关键要求 |
|------|------|----------|
| **Perceivable** | 可感知 | 对比度4.5:1、替代文本、字幕 |
| **Operable** | 可操作 | 键盘导航、44px触摸目标、跳过链接 |
| **Understandable** | 可理解 | 清晰标签、错误提示、一致导航 |
| **Robust** | 健壮性 | 语义HTML、ARIA、屏幕阅读器兼容 |

### 对比度要求

| 文本类型 | AA标准 | AAA标准 |
|----------|--------|---------|
| 正常文本（<18pt） | 4.5:1 | 7:1 |
| 大文本（≥18pt） | 3:1 | 4.5:1 |
| UI组件/图形 | 3:1 | 4.5:1 |

### 交互要求

- 触摸目标最小 44×44px
- 焦点指示器可见（3:1对比度）
- 所有交互元素支持键盘操作
- 时间限制内容可暂停/延长

## 输入参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| projectType | String | 是 | 项目类型（saas/ecommerce/enterprise/consumer等） |
| brandColor | String | 否 | 品牌主色，十六进制 |
| colorScheme | String | 否 | 配色方案（light/dark/system） |
| fontSizeBase | Number | 否 | 基础字号，默认14 |
| accessibilityLevel | String | 否 | 无障碍级别（aa/aaa），默认aa |

## 输出格式

```json
{
  "designSystem": {
    "colors": {
      "primary": "#0052CC",
      "secondary": "#F2F4F7",
      "success": "#36B37E"
    },
    "typography": {
      "fontFamily": "Inter, -apple-system, sans-serif",
      "baseSize": 14,
      "lineHeight": 1.5
    },
    "spacing": {
      "xs": 4,
      "sm": 8,
      "md": 16
    },
    "accessibility": {
      "contrastRatio": "4.5:1",
      "touchTarget": "44px"
    }
  }
}
```

## 使用流程

1. 确定项目类型和品牌色
2. 生成设计系统配置
3. 应用到组件和页面
4. 验证无障碍合规性
5. 输出设计规范文档

## 最佳实践

1. **系统优先**：使用设计令牌而非硬编码值
2. **一致性**：保持颜色、间距、字体的统一
3. **无障碍优先**：设计阶段考虑WCAG合规
4. **响应式设计**：适配多种屏幕尺寸
5. **状态完整**：设计所有状态（正常/悬停/禁用/错误）

参考来源：
- https://mohablog.com/python-ai-ui-ux-skill-development/
- https://skillmd.ai/how-to-build/uiux-design-expert/
- https://github.com/gabeosx/agent-skills/blob/main/skills/ux-designer/SKILL.md
