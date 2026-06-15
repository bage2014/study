---
name: "common-data-migration"
description: "数据迁移技能 - 支持Flyway和Liquibase"
---

# 数据迁移技能

## 功能描述

管理数据库版本迁移，支持 Flyway 和 Liquibase 两种迁移工具。

## 何时使用

- 数据库结构变更时
- 多环境数据库同步
- 数据库版本管理

## 核心功能

- 生成迁移脚本模板
- 执行数据库迁移
- 回滚迁移
- 查看迁移状态
- 支持 Flyway 和 Liquibase

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tool | String | 是 | 迁移工具（flyway/liquibase） |
| action | String | 是 | 操作（create/migrate/rollback/status） |
| description | String | 否 | 迁移描述（创建时使用） |
| version | String | 否 | 目标版本（回滚时使用） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "tool": "flyway",
  "action": "migrate",
  "migrations": [
    {"version": "1", "description": "create_users_table", "type": "SQL", "status": "SUCCESS"},
    {"version": "2", "description": "add_email_column", "type": "SQL", "status": "SUCCESS"}
  ],
  "message": "Successfully applied 2 migrations"
}
```

## 使用流程

1. 选择迁移工具
2. 执行对应操作（创建/迁移/回滚/状态）
3. 查看执行结果

## 最佳实践

- 使用版本控制管理迁移脚本
- 开发环境使用 H2 测试迁移
- 生产环境迁移前备份数据

## 配置要求

### Flyway 配置示例

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
```

## 扩展指南

支持自定义迁移脚本模板。