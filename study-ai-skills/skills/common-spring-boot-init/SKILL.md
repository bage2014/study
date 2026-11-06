---
name: "common-spring-boot-init"
description: "快速创建 Spring Boot 项目结构，支持多种数据库和依赖配置"
trigger: "用户请求创建新的 Spring Boot 项目时"
disable-when: "项目已存在或非 Java 技术栈项目"
category: "backend"
tags: ["spring", "java", "backend", "project-init"]
requires: ["common-db-design"]
---

# Spring Boot 项目初始化技能

## 功能描述

快速创建 Spring Boot 项目结构，支持多种数据库和依赖配置。

## 何时使用

- 创建新的 Spring Boot 项目
- 快速搭建项目骨架
- 标准化项目结构

## 核心功能

- 生成标准项目结构
- 配置 pom.xml 依赖
- 创建启动类
- 配置 application.yml
- 支持多种数据库（H2/MySQL/PostgreSQL）
- 集成 Spring AI

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| projectName | String | 是 | 项目名称 |
| groupId | String | 否 | Maven Group ID（如未提供，自动根据项目名生成：`com.bage.<项目名按连字符拆分>`） |
| artifactId | String | 是 | Maven Artifact ID |
| javaVersion | String | 否 | Java 版本（默认 21） |
| database | String | 否 | 数据库类型（h2/mysql/postgresql，默认 h2） |
| dependencies | Array | 否 | 额外依赖（spring-web, spring-data-jpa, spring-ai等） |

## 包命名规范（强制）

**必须遵守**：Java 包名必须将项目名按连字符拆分，前缀 `com.bage.`

**示例**：
- `user-backend` → `com.bage.study.userbackend`（本项目简化）或 `com.bage.user.backend`（完整）
- `study-ai-dev-flow` → `com.bage.study.ai.dev.flow`
- **禁止**：`com.study.xxx` 或 `com.bage.studyaixxx`（连字符必须拆分）

**自动生成规则**：
如果用户未提供 `groupId`，技能应自动根据以下规则生成：
1. 前缀：`com.bage.`
2. 项目名按 `-` 拆分，每段作为一级包名
3. 示例：`my-project` → `com.bage.my.project`

## 输出格式

```json
{
  "status": "SUCCESS",
  "projectName": "my-spring-app",
  "groupId": "com.example",
  "artifactId": "my-spring-app",
  "javaVersion": "21",
  "database": "h2",
  "filesCreated": [
    "pom.xml",
    "src/main/java/com/example/MySpringAppApplication.java",
    "src/main/resources/application.yml",
    "src/main/resources/schema.sql",
    "src/test/java/com/example/MySpringAppApplicationTests.java"
  ],
  "nextSteps": [
    "cd my-spring-app",
    "mvn spring-boot:run"
  ]
}
```

## 使用流程

1. 指定项目基本信息
2. 选择数据库类型（开发环境默认 H2）
3. 添加所需依赖
4. 生成项目结构

## 最佳实践

- 开发环境使用 H2 内存数据库
- 生产环境切换为 MySQL/PostgreSQL
- 按需添加依赖，避免冗余

## 配置要求

### application.yml 示例

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:example_db
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

## 扩展指南

支持添加自定义依赖模板和项目模板。
## 触发条件

