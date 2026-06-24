---
name: "common-vue-init"
description: "Vue项目初始化技能，快速创建Vue前端项目"
trigger: "用户请求创建新的Vue项目时"
disable-when: "项目已存在或非前端项目"
category: "frontend"
tags: ["vue", "frontend", "javascript", "project-init"]
---

# Vue 项目初始化技能

## 功能描述

快速创建 Vue 项目结构，支持 Vue 3 + Vite + TypeScript 配置。

## 何时使用

- 创建新的 Vue 前端项目
- 快速搭建前端项目骨架
- 标准化前端项目结构

## 核心功能

- 生成 Vue 3 + Vite 项目结构
- 配置 TypeScript
- 集成 TailwindCSS
- 创建组件模板
- 配置 ESLint 和 Prettier

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| projectName | String | 是 | 项目名称 |
| packageManager | String | 否 | 包管理器（npm/yarn/pnpm，默认 npm） |
| withTypeScript | Boolean | 否 | 是否使用 TypeScript（默认 true） |
| withTailwind | Boolean | 否 | 是否集成 TailwindCSS（默认 true） |
| features | Array | 否 | 额外功能（router/pinia/axios等） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "projectName": "my-vue-app",
  "packageManager": "npm",
  "withTypeScript": true,
  "withTailwind": true,
  "filesCreated": [
    "package.json",
    "vite.config.ts",
    "tsconfig.json",
    "src/App.vue",
    "src/main.ts",
    "src/components/"
  ],
  "nextSteps": [
    "cd my-vue-app",
    "npm install",
    "npm run dev"
  ]
}
```

## 使用流程

1. 指定项目名称
2. 选择包管理器和语言
3. 添加所需功能
4. 生成项目结构

## 最佳实践

- 使用 pnpm 作为包管理器（更快、更节省空间）
- 集成 Pinia 进行状态管理
- 使用 Axios 进行 HTTP 请求

## 配置要求

### vite.config.ts 示例

```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  }
})
```

## 扩展指南

支持添加自定义模板和插件。
## 触发条件

