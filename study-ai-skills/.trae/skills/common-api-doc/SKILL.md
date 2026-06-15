---
name: "common-api-doc"
description: "API文档生成技能 - 基于OpenAPI规范生成文档"
---

# API文档生成技能

## 功能描述

根据代码或需求自动生成 OpenAPI/Swagger 文档，支持交互式文档界面。

## 何时使用

- 后端 API 开发完成后生成文档
- 根据接口需求生成 API 规范
- 维护 API 文档

## 核心功能

- 从代码注释生成 OpenAPI 规范
- 从需求描述生成 API 设计
- 生成 Swagger UI 界面
- 导出多种格式（YAML/JSON/HTML）

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| sourceType | String | 是 | 来源类型（code/requirements） |
| source | String | 是 | 代码路径或需求描述 |
| outputFormat | String | 否 | 输出格式（yaml/json/html，默认 yaml） |
| serverUrl | String | 否 | API 服务地址 |

## 输出格式

```json
{
  "status": "SUCCESS",
  "openapiVersion": "3.0.3",
  "outputFile": "openapi.yaml",
  "endpoints": [
    {"method": "GET", "path": "/api/users", "summary": "获取用户列表"},
    {"method": "POST", "path": "/api/users", "summary": "创建用户"},
    {"method": "GET", "path": "/api/users/{id}", "summary": "获取用户详情"}
  ],
  "swaggerUiUrl": "http://localhost:8080/swagger-ui.html"
}
```

## 使用流程

1. 指定来源类型（代码或需求）
2. 提供代码路径或需求描述
3. 选择输出格式
4. 生成 API 文档

## 最佳实践

- 在 CI/CD 流程中自动更新文档
- 使用 SpringDoc 集成 Swagger UI
- 保持 API 文档与代码同步

## 配置要求

### Spring Boot 集成示例

```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

## 扩展指南

支持自定义模板和文档样式。