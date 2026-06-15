---
name: "os-ui-library-impl-native"
description: "UI组件库自研实现"
---

# UI组件库自研实现

## 功能描述

轻量级 UI 组件库实现，无外部依赖，适合快速原型和轻量级项目。

## 何时使用

新项目快速开发、轻量级应用、无需复杂组件时使用。

## 核心功能

- Button 按钮组件
- Input 输入框组件
- Card 卡片组件
- Flex 布局组件

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| componentType | String | 是 | 组件类型 |
| props | Object | 否 | 组件属性 |

## 输出格式

```json
{
  "status": "SUCCESS",
  "implementation": "native",
  "component": {...}
}
```

## 使用流程

1. 配置 active 为 native
2. 直接调用，无需安装依赖

## 最佳实践

- 用于快速原型开发
- 轻量级项目首选
- 需要更多组件时切换到其他实现

## 配置要求

无需额外配置，开箱即用。

## 扩展指南

添加新组件时，在此实现中添加即可。