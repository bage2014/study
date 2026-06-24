---
name: "common-contract-generation"
description: "契约生成技能，提供API契约文档的生成和管理能力"
trigger: "需要定义API接口契约时"
disable-when: "项目已有完善的API契约或使用其他契约工具"
category: "backend"
tags: ["api", "contract", "swagger", "openapi"]
---

# 契约生成技能

## 功能描述

提供 API 契约文档的生成和管理能力，帮助团队定义和维护服务间的接口契约，确保前后端协作的一致性。

## 何时使用

在以下情况调用此技能：
- 需要设计新的 API 接口时
- 需要生成 OpenAPI/Swagger 文档时
- 需要验证契约一致性时
- 需要生成客户端 SDK 时

## 核心功能

- **契约设计**：设计 RESTful API 接口契约
- **文档生成**：生成 OpenAPI/Swagger 文档
- **契约验证**：验证接口实现与契约的一致性
- **SDK生成**：生成客户端 SDK 和类型定义
- **Mock生成**：生成 Mock 数据和服务器

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| apiName | String | 是 | API 名称 |
| endpoints | List\<Endpoint\> | 是 | 接口端点列表 |
| format | String | 否 | 输出格式（openapi3/swagger2） |
| version | String | 否 | API 版本 |

## 输出格式

```json
{
  "status": "SUCCESS",
  "apiName": "用户服务 API",
  "version": "1.0.0",
  "openapiSpec": "OpenAPI YAML/JSON 规范",
  "endpoints": [
    {
      "path": "/api/users",
      "method": "GET",
      "summary": "获取用户列表",
      "request": {},
      "response": {}
    }
  ],
  "validationResult": {
    "valid": true,
    "errors": []
  },
  "generatedFiles": ["openapi.yaml", "client-sdk.ts"]
}
```

## 契约设计流程

```
需求分析 → 接口设计 → 契约编写 → 契约验证 → 文档生成 → SDK生成 → Mock生成
```

### 详细步骤

1. **需求分析**：理解业务需求，确定接口功能
2. **接口设计**：设计 API 端点、参数和响应结构
3. **契约编写**：编写 OpenAPI 规范文档
4. **契约验证**：验证契约的语法和语义正确性
5. **文档生成**：生成可视化的 API 文档
6. **SDK生成**：生成客户端 SDK 和类型定义
7. **Mock生成**：生成 Mock 数据和服务器

## 契约质量检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 路径规范 | API 路径是否符合 REST 规范 | ✅/❌ |
| HTTP方法 | 是否使用正确的 HTTP 方法 | ✅/❌ |
| 参数完整 | 请求参数是否完整定义 | ✅/❌ |
| 响应定义 | 响应结构是否完整定义 | ✅/❌ |
| 错误处理 | 是否定义错误响应 | ✅/❌ |
| 状态码 | HTTP 状态码是否正确使用 | ✅/❌ |
| 版本管理 | 是否有版本控制策略 | ✅/❌ |
| 文档完整 | 是否有必要的描述和示例 | ✅/❌ |

## 最佳实践指南

### OpenAPI 规范示例

```yaml
openapi: 3.0.3
info:
  title: 用户服务 API
  description: 用户管理相关接口
  version: 1.0.0
servers:
  - url: https://api.example.com/v1
paths:
  /users:
    get:
      summary: 获取用户列表
      description: 获取所有用户的列表
      parameters:
        - name: page
          in: query
          description: 页码
          required: false
          schema:
            type: integer
            default: 1
      responses:
        '200':
          description: 成功获取用户列表
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: array
                    items:
                      $ref: '#/components/schemas/User'
                  pagination:
                    $ref: '#/components/schemas/Pagination'
components:
  schemas:
    User:
      type: object
      properties:
        id:
          type: string
          format: uuid
        email:
          type: string
          format: email
        name:
          type: string
      required:
        - id
        - email
```

### REST API 设计规范

1. **路径命名**：使用小写字母和连字符，如 `/api/user-profiles`
2. **HTTP 方法**：
   - GET：获取资源
   - POST：创建资源
   - PUT：更新完整资源
   - PATCH：更新部分资源
   - DELETE：删除资源
3. **状态码**：
   - 200：成功获取/更新
   - 201：成功创建
   - 204：成功删除（无内容）
   - 400：请求参数错误
   - 401：未认证
   - 403：无权限
   - 404：资源不存在
   - 500：服务器错误

### 错误响应格式

```yaml
components:
  schemas:
    ErrorResponse:
      type: object
      properties:
        code:
          type: string
          description: 错误代码
        message:
          type: string
          description: 错误消息
        details:
          type: array
          items:
            type: string
          description: 详细错误信息
      required:
        - code
        - message
```

## 契约管理策略

### 版本控制

| 策略 | 说明 | 适用场景 |
|------|------|----------|
| URL 版本 | `/v1/users` | 简单直接，便于调试 |
| Header 版本 | `Accept-Version: 1.0` | 保持 URL 简洁 |
| Media Type 版本 | `Accept: application/vnd.example.v1+json` | 符合 REST 最佳实践 |

### 契约演进

1. **向后兼容变更**：添加新字段、新端点
2. **非向后兼容变更**：修改现有字段、删除字段、删除端点
3. **版本升级策略**：使用新版本号，保持旧版本可用

## 核心组件

| 组件 | 职责 | 描述 |
|------|------|------|
| ContractDesigner | 契约设计器 | 设计 API 接口契约 |
| SpecGenerator | 规范生成器 | 生成 OpenAPI 规范 |
| ContractValidator | 契约验证器 | 验证契约一致性 |
| SdkGenerator | SDK 生成器 | 生成客户端 SDK |
| MockGenerator | Mock 生成器 | 生成 Mock 数据 |

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| CONTRACT_FORMAT | 默认输出格式 | openapi3 |
| API_VERSION | 默认 API 版本 | 1.0.0 |

### 配置文件

```yaml
contract:
  format: openapi3
  version: 1.0.0
  output:
    spec: openapi.yaml
    docs: docs/api
    sdk:
      - language: typescript
        output: src/client
      - language: java
        output: client/java
```

## 扩展指南

### 添加新语言 SDK 支持

1. 创建对应语言的 SDK 生成器
2. 定义代码模板和映射规则
3. 添加 SDK 生成配置

### 添加自定义验证规则

1. 在验证器中添加新规则
2. 定义规则的检查逻辑和错误消息
3. 更新契约质量检查清单
## 触发条件

