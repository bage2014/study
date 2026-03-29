# API 契约 SKILL

## 1. 技能概述

本技能提供了家族树应用的完整API契约文档，详细描述了后端API的请求和响应格式，以及前端与后端的交互方式。通过本技能，开发者可以了解如何与后端API进行交互，实现前端功能。

## 2. 目录结构

```
docs/api-contract/
└── api-contract.md  # API契约文档
```

## 3. API 契约详情

### 3.1 认证相关 API

#### 3.1.1 登录

**请求**
- URL: `/api/auth/login`
- 方法: `POST`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

#### 3.1.2 注册

**请求**
- URL: `/api/auth/register`
- 方法: `POST`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "nickname": "用户昵称"
  }
  ```

**响应**
- 状态码: `201 Created`
- 响应体:
  ```json
  {
    "id": 1,
    "email": "user@example.com",
    "nickname": "用户昵称",
    "avatar": null,
    "phone": null,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.1.3 获取当前用户信息

**请求**
- URL: `/api/auth/me`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "email": "user@example.com",
    "nickname": "用户昵称",
    "avatar": null,
    "phone": null,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

### 3.2 家族管理 API

#### 3.2.1 创建家族

**请求**
- URL: `/api/families`
- 方法: `POST`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "name": "我的家族",
    "description": "这是我的家族",
    "avatar": "https://example.com/avatar.jpg"
  }
  ```

**响应**
- 状态码: `201 Created`
- 响应体:
  ```json
  {
    "id": 1,
    "name": "我的家族",
    "description": "这是我的家族",
    "avatar": "https://example.com/avatar.jpg",
    "creatorId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.2.2 获取家族列表

**请求**
- URL: `/api/families`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  [
    {
      "id": 1,
      "name": "我的家族",
      "description": "这是我的家族",
      "avatar": "https://example.com/avatar.jpg",
      "creatorId": 1,
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
  ```

#### 3.2.3 获取家族详情

**请求**
- URL: `/api/families/{id}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "name": "我的家族",
    "description": "这是我的家族",
    "avatar": "https://example.com/avatar.jpg",
    "creatorId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.2.4 更新家族

**请求**
- URL: `/api/families/{id}`
- 方法: `PUT`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "name": "更新后的家族名称",
    "description": "更新后的家族描述",
    "avatar": "https://example.com/new-avatar.jpg"
  }
  ```

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "name": "更新后的家族名称",
    "description": "更新后的家族描述",
    "avatar": "https://example.com/new-avatar.jpg",
    "creatorId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.2.5 删除家族

**请求**
- URL: `/api/families/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

### 3.3 成员管理 API

#### 3.3.1 添加成员

**请求**
- URL: `/api/families/{familyId}/members`
- 方法: `POST`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "name": "张三",
    "gender": "MALE",
    "birthDate": "1980-01-01",
    "deathDate": null,
    "photo": "https://example.com/photo.jpg",
    "details": "这是成员详情"
  }
  ```

**响应**
- 状态码: `201 Created`
- 响应体:
  ```json
  {
    "id": 1,
    "name": "张三",
    "gender": "MALE",
    "birthDate": "1980-01-01",
    "deathDate": null,
    "photo": "https://example.com/photo.jpg",
    "details": "这是成员详情",
    "familyId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.3.2 获取成员列表

**请求**
- URL: `/api/families/{familyId}/members`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  [
    {
      "id": 1,
      "name": "张三",
      "gender": "MALE",
      "birthDate": "1980-01-01",
      "deathDate": null,
      "photo": "https://example.com/photo.jpg",
      "details": "这是成员详情",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
  ```

#### 3.3.3 获取成员详情

**请求**
- URL: `/api/families/{familyId}/members/{id}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "name": "张三",
    "gender": "MALE",
    "birthDate": "1980-01-01",
    "deathDate": null,
    "photo": "https://example.com/photo.jpg",
    "details": "这是成员详情",
    "familyId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.3.4 更新成员

**请求**
- URL: `/api/families/{familyId}/members/{id}`
- 方法: `PUT`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "name": "张三(更新)",
    "gender": "MALE",
    "birthDate": "1980-01-01",
    "deathDate": null,
    "photo": "https://example.com/new-photo.jpg",
    "details": "更新后的成员详情"
  }
  ```

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "name": "张三(更新)",
    "gender": "MALE",
    "birthDate": "1980-01-01",
    "deathDate": null,
    "photo": "https://example.com/new-photo.jpg",
    "details": "更新后的成员详情",
    "familyId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.3.5 删除成员

**请求**
- URL: `/api/families/{familyId}/members/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

### 3.4 关系管理 API

#### 3.4.1 创建关系

**请求**
- URL: `/api/relationships`
- 方法: `POST`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "memberId1": 1,
    "memberId2": 2,
    "relationshipType": "FATHER"
  }
  ```

**响应**
- 状态码: `201 Created`
- 响应体:
  ```json
  {
    "id": 1,
    "memberId1": 1,
    "memberId2": 2,
    "relationshipType": "FATHER",
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.4.2 获取关系列表

**请求**
- URL: `/api/relationships`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  [
    {
      "id": 1,
      "memberId1": 1,
      "memberId2": 2,
      "relationshipType": "FATHER",
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
  ```

#### 3.4.3 获取成员的关系

**请求**
- URL: `/api/members/{memberId}/relationships`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  [
    {
      "id": 1,
      "memberId1": 1,
      "memberId2": 2,
      "relationshipType": "FATHER",
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
  ```

#### 3.4.4 删除关系

**请求**
- URL: `/api/relationships/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

### 3.5 事件管理 API

#### 3.5.1 创建事件

**请求**
- URL: `/api/events`
- 方法: `POST`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "title": "生日派对",
    "description": "张三的生日派对",
    "date": "2024-01-01",
    "location": "家中",
    "familyId": 1
  }
  ```

**响应**
- 状态码: `201 Created`
- 响应体:
  ```json
  {
    "id": 1,
    "title": "生日派对",
    "description": "张三的生日派对",
    "date": "2024-01-01",
    "location": "家中",
    "familyId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.5.2 获取事件列表

**请求**
- URL: `/api/events`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  [
    {
      "id": 1,
      "title": "生日派对",
      "description": "张三的生日派对",
      "date": "2024-01-01",
      "location": "家中",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
  ```

#### 3.5.3 获取事件详情

**请求**
- URL: `/api/events/{id}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "title": "生日派对",
    "description": "张三的生日派对",
    "date": "2024-01-01",
    "location": "家中",
    "familyId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.5.4 更新事件

**请求**
- URL: `/api/events/{id}`
- 方法: `PUT`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "title": "生日派对(更新)",
    "description": "张三的生日派对(更新)",
    "date": "2024-01-02",
    "location": "餐厅",
    "familyId": 1
  }
  ```

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "title": "生日派对(更新)",
    "description": "张三的生日派对(更新)",
    "date": "2024-01-02",
    "location": "餐厅",
    "familyId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.5.5 删除事件

**请求**
- URL: `/api/events/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

### 3.6 媒体管理 API

#### 3.6.1 上传媒体

**请求**
- URL: `/api/media`
- 方法: `POST`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `multipart/form-data`
- 请求体:
  - `file`: 文件
  - `familyId`: 家族ID
  - `description`: 描述

**响应**
- 状态码: `201 Created`
- 响应体:
  ```json
  {
    "id": 1,
    "fileName": "photo.jpg",
    "filePath": "/uploads/photo.jpg",
    "description": "家族照片",
    "familyId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.6.2 获取媒体列表

**请求**
- URL: `/api/media`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  [
    {
      "id": 1,
      "fileName": "photo.jpg",
      "filePath": "/uploads/photo.jpg",
      "description": "家族照片",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
  ```

#### 3.6.3 获取媒体详情

**请求**
- URL: `/api/media/{id}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "fileName": "photo.jpg",
    "filePath": "/uploads/photo.jpg",
    "description": "家族照片",
    "familyId": 1,
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.6.4 删除媒体

**请求**
- URL: `/api/media/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

### 3.7 权限管理 API

#### 3.7.1 分配权限

**请求**
- URL: `/api/permissions`
- 方法: `POST`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "userId": 2,
    "familyId": 1,
    "role": "MEMBER"
  }
  ```

**响应**
- 状态码: `201 Created`
- 响应体:
  ```json
  {
    "id": 1,
    "userId": 2,
    "familyId": 1,
    "role": "MEMBER",
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.7.2 获取权限列表

**请求**
- URL: `/api/permissions`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  [
    {
      "id": 1,
      "userId": 2,
      "familyId": 1,
      "role": "MEMBER",
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
  ```

#### 3.7.3 更新权限

**请求**
- URL: `/api/permissions/{id}`
- 方法: `PUT`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "role": "ADMIN"
  }
  ```

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "id": 1,
    "userId": 2,
    "familyId": 1,
    "role": "ADMIN",
    "createdAt": "2024-01-01T00:00:00"
  }
  ```

#### 3.7.4 删除权限

**请求**
- URL: `/api/permissions/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

## 4. 前端与后端交互示例

### 4.1 登录流程

```javascript
// 前端登录请求
async function login(email, password) {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ email, password })
  });
  
  if (!response.ok) {
    throw new Error('登录失败');
  }
  
  const data = await response.json();
  localStorage.setItem('token', data.token);
  return data;
}
```

### 4.2 获取家族列表

```javascript
// 前端获取家族列表
async function getFamilies() {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/families', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  if (!response.ok) {
    throw new Error('获取家族列表失败');
  }
  
  return await response.json();
}
```

### 4.3 添加成员

```javascript
// 前端添加成员
async function addMember(familyId, memberData) {
  const token = localStorage.getItem('token');
  const response = await fetch(`http://localhost:8080/api/families/${familyId}/members`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(memberData)
  });
  
  if (!response.ok) {
    throw new Error('添加成员失败');
  }
  
  return await response.json();
}
```

## 5. 错误处理

### 5.1 常见错误状态码

| 状态码 | 描述 | 处理方式 |
|--------|------|----------|
| 400 | 请求参数错误 | 检查请求参数格式是否正确 |
| 401 | 未授权 | 重新登录获取新的token |
| 403 | 禁止访问 | 检查用户是否有相应权限 |
| 404 | 资源不存在 | 检查请求的资源ID是否正确 |
| 500 | 服务器内部错误 | 联系后端开发人员 |

### 5.2 错误处理示例

```javascript
// 前端错误处理示例
try {
  const data = await login(email, password);
  // 处理成功逻辑
} catch (error) {
  console.error('登录失败:', error.message);
  // 显示错误提示
}
```

## 6. 最佳实践

1. **使用HTTPS**：在生产环境中使用HTTPS协议保护API通信
2. **合理使用缓存**：对于不经常变化的数据，可以使用缓存减少API调用
3. **分页处理**：对于大量数据的API，使用分页参数减少数据传输量
4. **错误处理**：对所有API调用进行错误处理，确保用户体验
5. **参数验证**：在前端对用户输入进行验证，减少无效请求
6. **token管理**：合理管理token的存储和刷新，确保安全性

## 7. 总结

本API契约文档详细描述了家族树应用的后端API接口，包括认证、家族管理、成员管理、关系管理、事件管理、媒体管理和权限管理等功能。前端开发者可以根据本文档实现与后端的交互，构建完整的家族树应用。
