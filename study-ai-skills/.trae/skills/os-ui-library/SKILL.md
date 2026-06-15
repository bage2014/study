---
name: "os-ui-library"
description: "UI组件库技能 - 支持多种实现切换"
---

# UI组件库技能

## 功能描述

提供统一的 UI 组件库接口，支持多种实现切换，包括自研实现和开源组件库。

## 何时使用

需要使用 UI 组件时，通过配置选择不同实现。

## 核心功能

- 统一的组件接口
- 运行时实现切换
- 多种开源组件库支持
- 配置驱动的实现选择

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| componentType | String | 是 | 组件类型（button/input/card等） |
| props | Object | 否 | 组件属性 |
| implementation | String | 否 | 指定实现（native/antd/material） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "implementation": "native",
  "componentType": "button",
  "render": true,
  "props": {...}
}
```

## 实现配置

在 `.trae/config.yml` 中配置：

```yaml
skills:
  open-source:
    os-ui-library:
      active: native
      implementations:
        - name: native
          description: 自研轻量级实现
          enabled: true
        - name: antd
          description: 基于 Ant Design
          enabled: true
          dependencies:
            - "antd"
            - "@ant-design/icons"
        - name: material
          description: 基于 Material UI
          enabled: true
          dependencies:
            - "@mui/material"
            - "@emotion/react"
            - "@emotion/styled"
```

## 可用实现

| 实现名称 | 描述 | 状态 | 依赖 |
|----------|------|------|------|
| native | 自研轻量级实现 | ✅ | 无 |
| antd | 基于 Ant Design | ✅ | antd, @ant-design/icons |
| material | 基于 Material UI | ✅ | @mui/material, @emotion/react |

## 使用流程

1. 在配置文件中选择 active 实现
2. 安装对应依赖（如需要）
3. 调用技能生成组件

## 最佳实践

- 新项目建议使用 native 实现，轻量无依赖
- 需要丰富组件时切换到 antd 或 material
- 保持接口统一，切换实现无需修改业务代码

## 配置要求

### 环境变量

无需特殊环境变量

### 配置文件

```yaml
skills:
  open-source:
    os-ui-library:
      active: native
```

## 扩展指南

### 添加新实现

1. 创建实现技能 `os-ui-library-impl-{name}`
2. 在配置文件中添加实现定义
3. 更新可用实现列表

### 切换实现

```bash
# 切换到 Ant Design
sed -i '' 's/active: native/active: antd/' .trae/config.yml

# 安装依赖
npm install antd @ant-design/icons
```