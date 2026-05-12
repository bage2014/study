# DESIGN_login_v1.0 - 用户登录设计文档

## 1. 设计概述

### 1.1 功能描述

用户登录功能设计，实现用户身份验证。

### 1.2 文档信息

| 字段 | 值 |
|------|------|
| 文档版本 | v1.0 |
| 创建日期 | 2026-05-12 |
| 状态 | 已评审 |

---

## 2. 技术方案

### 2.1 技术栈

| 组件 | 技术选型 |
|------|----------|
| 后端框架 | Spring Boot 3.1.10 |
| 数据库 | H2 / MySQL |
| 密码加密 | BCrypt |

### 2.2 架构设计

```
前端请求 → UserController → UserService → UserRepository → 数据库
```

---

## 3. 数据库设计

### 3.1 用户表 (users)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(20) | NOT NULL, UNIQUE | 用户名 |
| email | VARCHAR(100) | NOT NULL, UNIQUE | 邮箱 |
| password | VARCHAR(255) | NOT NULL | BCrypt加密密码 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

---

## 4. API 设计

### 4.1 登录接口

| 属性 | 值 |
|------|------|
| 路径 | POST /api/users/login |
| 方法 | POST |
| 描述 | 用户登录验证 |

#### 请求体

```json
{
  "username": "string",
  "password": "string"
}
```

#### 响应

| 状态码 | 响应体 |
|--------|--------|
| 200 | `{"id": 1, "username": "admin", "email": "admin@test.com", ...}` |
| 401 | `{"status": 401, "error": "Unauthorized", "message": "用户名或密码错误"}` |

---

## 5. 安全评审

### 5.1 安全检查

| 检查项 | 状态 | 说明 |
|--------|------|------|
| SQL注入防护 | ✅ | 使用JPA参数化查询 |
| 密码加密 | ✅ | BCrypt加密存储 |
| 敏感信息保护 | ✅ | 不返回密码 |

---

## 6. 性能评审

### 6.1 性能指标

| 指标 | 目标值 |
|------|--------|
| 响应时间 | ≤ 500ms |
| 并发处理 | ≥ 100 QPS |

---

## 7. 项目结构

```
backend/src/main/java/.../
├── controller/
│   └── UserController.java
├── service/
│   ├── UserService.java
│   └── impl/
│       └── UserServiceImpl.java
├── repository/
│   └── UserRepository.java
└── dto/
    ├── LoginRequest.java
    └── UserDTO.java
```

---

## 8. 评审意见

| 评审项 | 结果 | 备注 |
|--------|------|------|
| 架构合理性 | 通过 | - |
| API设计 | 通过 | RESTful风格 |
| 安全性 | 通过 | - |

---

## 9. 变更历史

| 版本 | 日期 | 修改内容 | 作者 |
|------|------|----------|------|
| v1.0 | 2026-05-12 | 初始版本 | - |