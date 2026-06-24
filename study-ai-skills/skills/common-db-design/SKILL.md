---
name: "common-db-design"
description: "数据库设计技能，生成表结构和实体类"
trigger: "需要设计数据库表结构时"
disable-when: "数据库已设计完成或使用NoSQL数据库"
category: "database"
tags: ["database", "design", "sql", "entity"]
---

# 数据库设计技能

## 功能描述

根据业务需求设计数据库表结构，生成 DDL 语句和对应的实体类。

## 何时使用

- 新项目数据库设计
- 新增业务模块表设计
- 数据库表结构文档生成

## 核心功能

- 分析业务需求设计表结构
- 生成 SQL DDL 语句
- 生成 JPA 实体类
- 生成数据库 ER 图描述
- 支持 H2/MySQL/PostgreSQL

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| requirements | String | 是 | 业务需求描述 |
| databaseType | String | 否 | 数据库类型（h2/mysql/postgresql，默认 h2） |
| includeIndexes | Boolean | 否 | 是否包含索引（默认 true） |
| includeComments | Boolean | 否 | 是否包含字段注释（默认 true） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "databaseType": "h2",
  "tables": [
    {
      "name": "users",
      "columns": [
        {"name": "id", "type": "BIGINT", "primaryKey": true, "autoIncrement": true},
        {"name": "username", "type": "VARCHAR(50)", "nullable": false, "unique": true},
        {"name": "email", "type": "VARCHAR(100)", "nullable": false},
        {"name": "created_at", "type": "TIMESTAMP", "default": "CURRENT_TIMESTAMP"}
      ],
      "indexes": [{"name": "idx_users_email", "columns": ["email"]}]
    }
  ],
  "ddl": "CREATE TABLE users (...);",
  "entityClasses": ["User.java"]
}
```

## 使用流程

1. 输入业务需求描述
2. 选择目标数据库类型
3. 生成表结构和实体类

## 最佳实践

- 开发环境使用 H2，便于快速验证
- 设计时考虑索引策略
- 遵循数据库命名规范

## 配置要求

无需额外配置

## 扩展指南

支持自定义字段类型映射和命名规则。
## 触发条件

