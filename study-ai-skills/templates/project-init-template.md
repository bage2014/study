# 项目初始化模板

本目录包含新项目初始化的标准模板和脚本。

## 快速开始

### 1. 创建后端项目

```bash
# 使用技能自动创建（推荐）
# 在 Qoder/Trae 中调用 common-spring-boot-init 技能，提供以下参数：
# - projectName: 你的项目名（如 user-management）
# - groupId: 留空（自动生成 com.bage.xxx）
# - artifactId: 项目 artifactId
```

### 2. 创建前端项目

```bash
# 使用技能自动创建（推荐）
# 在 Qoder/Trae 中调用 common-vue-init 技能
```

## 包命名规范（必须遵守）

### Java 后端

**格式**: `com.bage.<项目名按连字符拆分>`

**示例**:
| 项目名 | 正确包名 | 错误包名 |
|--------|----------|----------|
| `user-backend` | `com.bage.study.userbackend` | `com.study.userbackend` ❌ |
| `study-ai-dev-flow` | `com.bage.study.ai.dev.flow` | `com.bage.studyaidevflow` ❌ |
| `order-service` | `com.bage.study.order.service` | `com.study.order` ❌ |

**规则**:
1. 前缀必须是 `com.bage.`
2. 项目名按 `-` 拆分，每段作为一级包名
3. 禁止拼接（连字符必须拆分）

### Vue 前端

- 组件名：PascalCase（如 `UserManagement.vue`）
- API 文件：camelCase（如 `user.js`）
- 目录名：kebab-case（如 `user-management/`）

## 项目结构模板

### 后端标准结构

```
<artifactId>/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/bage/study/<module>/
    │   │   ├── controller/
    │   │   ├── service/
    │   │   ├── repository/
    │   │   ├── entity/
    │   │   └── <Module>Application.java
    │   └── resources/
    │       ├── application.yml
    │       └── schema.sql (可选)
    └── test/
        └── java/com/bage/study/<module>/
```

### 前端标准结构

```
<project-name>/
├── package.json
├── vite.config.js
├── index.html
└── src/
    ├── main.js
    ├── App.vue
    ├── api/
    ├── components/
    ├── views/
    └── assets/
```

## AGENTS.md 模板

新项目必须包含 AGENTS.md，内容如下：

```markdown
# AGENTS.md — AI 编码助手规范

## 项目概述

- 项目名称：<project-name>
- 技术栈：Spring Boot 3 + Vue 3

## 包命名规范（必须遵守）

**Java 后端包名规则：**
- 根包名格式：`com.bage.<项目名按连字符拆分>`
- 示例：`user-backend` → `com.bage.study.userbackend`
- **禁止**：`com.study.xxx` 或 `com.bage.studyaixxx`

## 目录结构

```
<project-name>/
├── backend/
│   └── src/main/java/com/bage/study/
└── frontend/
```

## 编码规范

- 后端：遵循 `personal-backend-coding-standard`
- 前端：遵循 `personal-frontend-coding-standard`
- 测试：遵循 `common-backend-unit-test`
```

## 检查清单

创建新项目后，必须验证：

- [ ] 后端包名是否为 `com.bage.study.xxx` 格式
- [ ] 目录结构是否为 `com/bage/study/xxx`
- [ ] 所有 Java 文件的 package 声明是否正确
- [ ] AGENTS.md 是否包含包命名规范
- [ ] 是否包含单元测试目录
