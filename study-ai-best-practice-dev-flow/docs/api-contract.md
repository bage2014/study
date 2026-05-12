# API 契约文档

## 1. 概述

本文档定义了 Best Practice Dev Flow 后端服务的 API 接口规范，包括用户管理、项目管理和任务管理模块。

## 2. 基础信息

| 项目 | 值 |
|------|-----|
| API 版本 | 1.0.0 |
| 基础路径 | `/api` |
| 协议 | HTTP/HTTPS |
| 主机地址 | `localhost:8080` |
| 数据格式 | JSON |

## 3. 错误响应格式

```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found"
}
```

## 4. 用户管理 API

### 4.1 注册新用户

| 属性 | 值 |
|------|-----|
| **路径** | `/api/users/register` |
| **方法** | POST |
| **描述** | 创建新用户账户 |

**请求体：**

| 字段名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | string | 是 | 用户名，最大50字符 |
| email | string | 是 | 邮箱地址，最大100字符 |
| password | string | 是 | 密码 |

```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**成功响应 (201 Created)：**

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | number | 用户ID |
| username | string | 用户名 |
| email | string | 邮箱地址 |

```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com"
}
```

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 400 | Username already exists |
| 400 | Email already exists |

---

### 4.2 用户登录

| 属性 | 值 |
|------|-----|
| **路径** | `/api/users/login` |
| **方法** | POST |
| **描述** | 用户登录验证 |

**请求体：**

| 字段名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |

```json
{
  "username": "string",
  "password": "string"
}
```

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com"
}
```

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 404 | Invalid username or password |

---

### 4.3 获取所有用户

| 属性 | 值 |
|------|-----|
| **路径** | `/api/users` |
| **方法** | GET |
| **描述** | 获取用户列表 |

**成功响应 (200 OK)：**

```json
[
  {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com"
  }
]
```

---

### 4.4 获取单个用户

| 属性 | 值 |
|------|-----|
| **路径** | `/api/users/{id}` |
| **方法** | GET |
| **描述** | 根据ID获取用户信息 |

**路径参数：**

| 参数名 | 类型 | 描述 |
|--------|------|------|
| id | number | 用户ID |

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com"
}
```

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 404 | User not found with id: {id} |

---

### 4.5 更新用户信息

| 属性 | 值 |
|------|-----|
| **路径** | `/api/users/{id}` |
| **方法** | PUT |
| **描述** | 更新用户信息 |

**路径参数：**

| 参数名 | 类型 | 描述 |
|--------|------|------|
| id | number | 用户ID |

**请求体：**

| 字段名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | string | 否 | 用户名 |
| email | string | 否 | 邮箱地址 |
| password | string | 否 | 新密码 |

```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "username": "updateduser",
  "email": "updated@example.com"
}
```

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 404 | User not found with id: {id} |
| 400 | Username already exists |
| 400 | Email already exists |

---

### 4.6 删除用户

| 属性 | 值 |
|------|-----|
| **路径** | `/api/users/{id}` |
| **方法** | DELETE |
| **描述** | 删除用户 |

**路径参数：**

| 参数名 | 类型 | 描述 |
|--------|------|------|
| id | number | 用户ID |

**成功响应 (204 No Content)：**

无响应体

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 404 | User not found with id: {id} |

---

## 5. 项目管理 API

### 5.1 获取所有项目

| 属性 | 值 |
|------|-----|
| **路径** | `/api/projects` |
| **方法** | GET |
| **描述** | 获取项目列表 |

**成功响应 (200 OK)：**

```json
[
  {
    "id": 1,
    "name": "项目名称",
    "description": "项目描述",
    "status": "ACTIVE"
  }
]
```

---

### 5.2 获取单个项目

| 属性 | 值 |
|------|-----|
| **路径** | `/api/projects/{id}` |
| **方法** | GET |
| **描述** | 根据ID获取项目信息 |

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "name": "项目名称",
  "description": "项目描述",
  "status": "ACTIVE"
}
```

---

### 5.3 创建项目

| 属性 | 值 |
|------|-----|
| **路径** | `/api/projects` |
| **方法** | POST |
| **描述** | 创建新项目 |

**请求体：**

```json
{
  "name": "string",
  "description": "string",
  "status": "string"
}
```

**成功响应 (201 Created)：**

```json
{
  "id": 1,
  "name": "项目名称",
  "description": "项目描述",
  "status": "ACTIVE"
}
```

---

### 5.4 更新项目

| 属性 | 值 |
|------|-----|
| **路径** | `/api/projects/{id}` |
| **方法** | PUT |
| **描述** | 更新项目信息 |

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "name": "更新后的名称",
  "description": "更新后的描述",
  "status": "INACTIVE"
}
```

---

### 5.5 删除项目

| 属性 | 值 |
|------|-----|
| **路径** | `/api/projects/{id}` |
| **方法** | DELETE |
| **描述** | 删除项目 |

**成功响应 (204 No Content)：**

无响应体

---

### 5.6 按状态查询项目

| 属性 | 值 |
|------|-----|
| **路径** | `/api/projects/status/{status}` |
| **方法** | GET |
| **描述** | 根据状态获取项目列表 |

**成功响应 (200 OK)：**

```json
[
  {
    "id": 1,
    "name": "项目名称",
    "description": "项目描述",
    "status": "ACTIVE"
  }
]
```

---

## 6. 任务管理 API

### 6.1 获取所有任务

| 属性 | 值 |
|------|-----|
| **路径** | `/api/tasks` |
| **方法** | GET |
| **描述** | 获取任务列表 |

**成功响应 (200 OK)：**

```json
[
  {
    "id": 1,
    "name": "任务名称",
    "description": "任务描述",
    "status": "PENDING",
    "priority": "MEDIUM",
    "owner": "张三"
  }
]
```

---

### 6.2 获取单个任务

| 属性 | 值 |
|------|-----|
| **路径** | `/api/tasks/{id}` |
| **方法** | GET |
| **描述** | 根据ID获取任务信息 |

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "name": "任务名称",
  "description": "任务描述",
  "status": "PENDING",
  "priority": "MEDIUM",
  "owner": "张三"
}
```

---

### 6.3 创建任务

| 属性 | 值 |
|------|-----|
| **路径** | `/api/tasks` |
| **方法** | POST |
| **描述** | 创建新任务 |

**请求体：**

```json
{
  "name": "string",
  "description": "string",
  "status": "string",
  "priority": "string",
  "owner": "string"
}
```

**成功响应 (201 Created)：**

```json
{
  "id": 1,
  "name": "任务名称",
  "description": "任务描述",
  "status": "PENDING",
  "priority": "MEDIUM",
  "owner": "张三"
}
```

---

### 6.4 更新任务

| 属性 | 值 |
|------|-----|
| **路径** | `/api/tasks/{id}` |
| **方法** | PUT |
| **描述** | 更新任务信息 |

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "name": "更新后的名称",
  "description": "更新后的描述",
  "status": "COMPLETED",
  "priority": "HIGH",
  "owner": "李四"
}
```

---

### 6.5 删除任务

| 属性 | 值 |
|------|-----|
| **路径** | `/api/tasks/{id}` |
| **方法** | DELETE |
| **描述** | 删除任务 |

**成功响应 (204 No Content)：**

无响应体

---

### 6.6 按状态查询任务

| 属性 | 值 |
|------|-----|
| **路径** | `/api/tasks/status/{status}` |
| **方法** | GET |
| **描述** | 根据状态获取任务列表 |

**成功响应 (200 OK)：**

```json
[
  {
    "id": 1,
    "name": "任务名称",
    "description": "任务描述",
    "status": "PENDING",
    "priority": "MEDIUM",
    "owner": "张三"
  }
]
```

---

### 6.7 按优先级查询任务

| 属性 | 值 |
|------|-----|
| **路径** | `/api/tasks/priority/{priority}` |
| **方法** | GET |
| **描述** | 根据优先级获取任务列表 |

**成功响应 (200 OK)：**

```json
[
  {
    "id": 1,
    "name": "任务名称",
    "description": "任务描述",
    "status": "PENDING",
    "priority": "HIGH",
    "owner": "张三"
  }
]
```

---

### 6.8 按负责人查询任务

| 属性 | 值 |
|------|-----|
| **路径** | `/api/tasks/owner/{owner}` |
| **方法** | GET |
| **描述** | 根据负责人获取任务列表 |

**成功响应 (200 OK)：**

```json
[
  {
    "id": 1,
    "name": "任务名称",
    "description": "任务描述",
    "status": "PENDING",
    "priority": "MEDIUM",
    "owner": "张三"
  }
]
```

---

## 7. 轨迹管理 API

### 7.1 获取轨迹点列表

| 属性 | 值 |
|------|-----|
| **路径** | `/api/trackpoints` |
| **方法** | GET |
| **描述** | 获取用户轨迹点列表 |

**请求参数：**

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | number | 否 | 用户ID，默认1 |

**成功响应 (200 OK)：**

```json
[
  {
    "id": 1,
    "userId": 1,
    "latitude": 39.9042,
    "longitude": 116.4074,
    "name": "天安门",
    "description": "首都北京标志性建筑",
    "createdAt": "2026-05-12T10:00:00",
    "updatedAt": "2026-05-12T10:00:00"
  }
]
```

---

### 7.2 获取单个轨迹点

| 属性 | 值 |
|------|-----|
| **路径** | `/api/trackpoints/{id}` |
| **方法** | GET |
| **描述** | 根据ID获取轨迹点信息 |

**路径参数：**

| 参数名 | 类型 | 描述 |
|--------|------|------|
| id | number | 轨迹点ID |

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "userId": 1,
  "latitude": 39.9042,
  "longitude": 116.4074,
  "name": "天安门",
  "description": "首都北京标志性建筑",
  "createdAt": "2026-05-12T10:00:00",
  "updatedAt": "2026-05-12T10:00:00"
}
```

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 404 | 轨迹点不存在: {id} |

---

### 7.3 创建轨迹点

| 属性 | 值 |
|------|-----|
| **路径** | `/api/trackpoints` |
| **方法** | POST |
| **描述** | 创建新轨迹点 |

**请求体：**

| 字段名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| latitude | number | 是 | 纬度，范围-90~90 |
| longitude | number | 是 | 经度，范围-180~180 |
| name | string | 否 | 位置名称，最大100字符 |
| description | string | 否 | 位置描述，最大500字符 |

```json
{
  "latitude": 39.9042,
  "longitude": 116.4074,
  "name": "天安门",
  "description": "首都北京标志性建筑"
}
```

**成功响应 (201 Created)：**

```json
{
  "id": 1,
  "userId": 1,
  "latitude": 39.9042,
  "longitude": 116.4074,
  "name": "天安门",
  "description": "首都北京标志性建筑",
  "createdAt": "2026-05-12T10:00:00",
  "updatedAt": "2026-05-12T10:00:00"
}
```

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 400 | 纬度和经度不能为空 |
| 400 | 纬度必须在 -90 到 90 之间 |
| 400 | 经度必须在 -180 到 180 之间 |

---

### 7.4 更新轨迹点

| 属性 | 值 |
|------|-----|
| **路径** | `/api/trackpoints/{id}` |
| **方法** | PUT |
| **描述** | 更新轨迹点信息 |

**路径参数：**

| 参数名 | 类型 | 描述 |
|--------|------|------|
| id | number | 轨迹点ID |

**请求体：**

| 字段名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| latitude | number | 否 | 纬度 |
| longitude | number | 否 | 经度 |
| name | string | 否 | 位置名称 |
| description | string | 否 | 位置描述 |

**成功响应 (200 OK)：**

```json
{
  "id": 1,
  "userId": 1,
  "latitude": 39.9042,
  "longitude": 116.4074,
  "name": "更新后的名称",
  "description": "更新后的描述",
  "createdAt": "2026-05-12T10:00:00",
  "updatedAt": "2026-05-12T12:00:00"
}
```

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 404 | 轨迹点不存在: {id} |

---

### 7.5 删除轨迹点

| 属性 | 值 |
|------|-----|
| **路径** | `/api/trackpoints/{id}` |
| **方法** | DELETE |
| **描述** | 删除轨迹点 |

**路径参数：**

| 参数名 | 类型 | 描述 |
|--------|------|------|
| id | number | 轨迹点ID |

**成功响应 (204 No Content)：**

无响应体

**错误响应：**

| HTTP 状态码 | 错误信息 |
|-------------|----------|
| 404 | 轨迹点不存在: {id} |

---

## 8. 数据模型

### 8.1 User (用户)

| 字段名 | 类型 | 约束 | 描述 |
|--------|------|------|------|
| id | number | 主键，自增 | 用户唯一标识 |
| username | string | 非空，唯一，最大50字符 | 用户名 |
| email | string | 非空，唯一，最大100字符 | 邮箱地址 |
| password | string | 非空，最大255字符 | 加密后的密码 |
| createdAt | datetime | 自动生成 | 创建时间 |
| updatedAt | datetime | 自动更新 | 更新时间 |

### 8.2 Project (项目)

| 字段名 | 类型 | 约束 | 描述 |
|--------|------|------|------|
| id | number | 主键，自增 | 项目唯一标识 |
| name | string | 非空 | 项目名称 |
| description | text | 可选 | 项目描述 |
| status | string | 非空，默认"ACTIVE" | 项目状态 |
| createdAt | datetime | 自动生成 | 创建时间 |
| updatedAt | datetime | 自动更新 | 更新时间 |

### 8.3 Task (任务)

| 字段名 | 类型 | 约束 | 描述 |
|--------|------|------|------|
| id | number | 主键，自增 | 任务唯一标识 |
| name | string | 非空 | 任务名称 |
| description | text | 可选 | 任务描述 |
| status | string | 非空，默认"PENDING" | 任务状态 |
| priority | string | 非空，默认"MEDIUM" | 任务优先级 |
| owner | string | 可选 | 负责人 |
| createdAt | datetime | 自动生成 | 创建时间 |
| updatedAt | datetime | 自动更新 | 更新时间 |

### 8.4 TrackPoint (轨迹点)

| 字段名 | 类型 | 约束 | 描述 |
|--------|------|------|------|
| id | number | 主键，自增 | 轨迹点唯一标识 |
| userId | number | 非空，外键 | 用户ID |
| latitude | number | 非空，范围-90~90 | 纬度 |
| longitude | number | 非空，范围-180~180 | 经度 |
| name | string | 可选，最大100字符 | 位置名称 |
| description | string | 可选，最大500字符 | 位置描述 |
| createdAt | datetime | 自动生成 | 创建时间 |
| updatedAt | datetime | 自动更新 | 更新时间 |

---

## 9. 状态码定义

### 用户管理状态码

| 状态码 | 含义 |
|--------|------|
| 200 | 成功（登录、查询、更新） |
| 201 | 创建成功（注册） |
| 204 | 删除成功 |
| 400 | 请求参数错误（用户名/邮箱重复） |
| 404 | 资源不存在（用户不存在） |
| 500 | 服务器内部错误 |

### 项目管理状态码

| 状态码 | 含义 |
|--------|------|
| 200 | 成功（查询、更新） |
| 201 | 创建成功 |
| 204 | 删除成功 |
| 404 | 项目不存在 |
| 500 | 服务器内部错误 |

### 任务管理状态码

| 状态码 | 含义 |
|--------|------|
| 200 | 成功（查询、更新） |
| 201 | 创建成功 |
| 204 | 删除成功 |
| 404 | 任务不存在 |
| 500 | 服务器内部错误 |

### 轨迹管理状态码

| 状态码 | 含义 |
|--------|------|
| 200 | 成功（查询、更新） |
| 201 | 创建成功 |
| 204 | 删除成功 |
| 400 | 请求参数错误（坐标范围错误） |
| 404 | 轨迹点不存在 |
| 500 | 服务器内部错误 |

---

## 10. 版本历史

| 版本 | 日期 | 变更说明 |
|------|------|----------|
| 1.0.0 | 2024-01-01 | 初始版本，包含用户、项目、任务管理API |
| 1.1.0 | 2026-05-12 | 添加轨迹管理API |