# API 契约文档

## 1. 统一返回格式

所有API响应都将采用以下统一格式：

```json
{
  "code": 123,
  "data": xxx,
  "message": "xx"
}
```

- **code**: 状态码，200表示成功，400表示请求错误，500表示服务器内部错误
- **data**: 响应数据，根据不同API返回不同结构
- **message**: 响应消息，成功时为"Success"，错误时为具体错误信息

## 2. 认证相关 API

### 2.1 登录

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
    "code": 200,
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Invalid email or password"
  }
  ```

### 2.2 注册

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
    "code": 200,
    "data": {
      "id": 1,
      "email": "user@example.com",
      "nickname": "用户昵称",
      "avatar": null,
      "phone": null,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Email already exists"
  }
  ```

### 2.3 获取当前用户信息

**请求**
- URL: `/api/auth/me`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": {
      "id": 1,
      "email": "user@example.com",
      "nickname": "用户昵称",
      "avatar": null,
      "phone": null,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "User not found"
  }
  ```

## 3. 家族管理 API

### 3.1 创建家族

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
    "code": 200,
    "data": {
      "id": 1,
      "name": "我的家族",
      "description": "这是我的家族",
      "avatar": "https://example.com/avatar.jpg",
      "creatorId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to create family"
  }
  ```

### 3.2 获取家族列表

**请求**
- URL: `/api/families`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": [
      {
        "id": 1,
        "name": "我的家族",
        "description": "这是我的家族",
        "avatar": "https://example.com/avatar.jpg",
        "creatorId": 1,
        "createdAt": "2024-01-01T00:00:00"
      }
    ],
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to get families"
  }
  ```

### 3.3 获取家族详情

**请求**
- URL: `/api/families/{id}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": {
      "id": 1,
      "name": "我的家族",
      "description": "这是我的家族",
      "avatar": "https://example.com/avatar.jpg",
      "creatorId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Family not found"
  }
  ```

### 3.4 更新家族

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
    "code": 200,
    "data": {
      "id": 1,
      "name": "更新后的家族名称",
      "description": "更新后的家族描述",
      "avatar": "https://example.com/new-avatar.jpg",
      "creatorId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to update family"
  }
  ```

### 3.5 删除家族

**请求**
- URL: `/api/families/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to delete family"
  }
  ```

## 4. 成员管理 API

### 4.1 添加成员

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
    "code": 200,
    "data": {
      "id": 1,
      "name": "张三",
      "gender": "MALE",
      "birthDate": "1980-01-01",
      "deathDate": null,
      "photo": "https://example.com/photo.jpg",
      "details": "这是成员详情",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to add member"
  }
  ```

### 4.2 获取成员列表

**请求**
- URL: `/api/families/{familyId}/members`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": [
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
    ],
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to get members"
  }
  ```

### 4.3 获取成员详情

**请求**
- URL: `/api/families/{familyId}/members/{id}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": {
      "id": 1,
      "name": "张三",
      "gender": "MALE",
      "birthDate": "1980-01-01",
      "deathDate": null,
      "photo": "https://example.com/photo.jpg",
      "details": "这是成员详情",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Member not found"
  }
  ```

### 4.4 更新成员

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
    "code": 200,
    "data": {
      "id": 1,
      "name": "张三(更新)",
      "gender": "MALE",
      "birthDate": "1980-01-01",
      "deathDate": null,
      "photo": "https://example.com/new-photo.jpg",
      "details": "更新后的成员详情",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to update member"
  }
  ```

### 4.5 删除成员

**请求**
- URL: `/api/families/{familyId}/members/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to delete member"
  }
  ```

## 5. 关系管理 API

### 5.1 创建关系

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
    "code": 200,
    "data": {
      "id": 1,
      "memberId1": 1,
      "memberId2": 2,
      "relationshipType": "FATHER",
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to create relationship"
  }
  ```

### 5.2 获取关系列表

**请求**
- URL: `/api/relationships`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": [
      {
        "id": 1,
        "memberId1": 1,
        "memberId2": 2,
        "relationshipType": "FATHER",
        "createdAt": "2024-01-01T00:00:00"
      }
    ],
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to get relationships"
  }
  ```

### 5.3 获取成员的关系

**请求**
- URL: `/api/relationships/member/{memberId}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": [
      {
        "id": 1,
        "memberId1": 1,
        "memberId2": 2,
        "relationshipType": "FATHER",
        "createdAt": "2024-01-01T00:00:00"
      }
    ],
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to get relationships"
  }
  ```

### 5.4 删除关系

**请求**
- URL: `/api/relationships/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to delete relationship"
  }
  ```

## 6. 事件管理 API

### 6.1 创建事件

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
    "code": 200,
    "data": {
      "id": 1,
      "title": "生日派对",
      "description": "张三的生日派对",
      "date": "2024-01-01",
      "location": "家中",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to create event"
  }
  ```

### 6.2 获取事件列表

**请求**
- URL: `/api/events`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": [
      {
        "id": 1,
        "title": "生日派对",
        "description": "张三的生日派对",
        "date": "2024-01-01",
        "location": "家中",
        "familyId": 1,
        "createdAt": "2024-01-01T00:00:00"
      }
    ],
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to get events"
  }
  ```

### 6.3 获取事件详情

**请求**
- URL: `/api/events/{id}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": {
      "id": 1,
      "title": "生日派对",
      "description": "张三的生日派对",
      "date": "2024-01-01",
      "location": "家中",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Event not found"
  }
  ```

### 6.4 更新事件

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
    "code": 200,
    "data": {
      "id": 1,
      "title": "生日派对(更新)",
      "description": "张三的生日派对(更新)",
      "date": "2024-01-02",
      "location": "餐厅",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to update event"
  }
  ```

### 6.5 删除事件

**请求**
- URL: `/api/events/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to delete event"
  }
  ```

## 7. 媒体管理 API

### 7.1 上传媒体

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
    "code": 200,
    "data": {
      "id": 1,
      "fileName": "photo.jpg",
      "filePath": "/uploads/photo.jpg",
      "description": "家族照片",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to upload media"
  }
  ```

### 7.2 获取媒体列表

**请求**
- URL: `/api/media`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": [
      {
        "id": 1,
        "fileName": "photo.jpg",
        "filePath": "/uploads/photo.jpg",
        "description": "家族照片",
        "familyId": 1,
        "createdAt": "2024-01-01T00:00:00"
      }
    ],
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to get media"
  }
  ```

### 7.3 获取媒体详情

**请求**
- URL: `/api/media/{id}`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": {
      "id": 1,
      "fileName": "photo.jpg",
      "filePath": "/uploads/photo.jpg",
      "description": "家族照片",
      "familyId": 1,
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Media not found"
  }
  ```

### 7.4 删除媒体

**请求**
- URL: `/api/media/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to delete media"
  }
  ```

## 8. 权限管理 API

### 8.1 分配权限

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
    "code": 200,
    "data": {
      "id": 1,
      "userId": 2,
      "familyId": 1,
      "role": "MEMBER",
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to assign permission"
  }
  ```

### 8.2 获取权限列表

**请求**
- URL: `/api/permissions`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `200 OK`
- 响应体:
  ```json
  {
    "code": 200,
    "data": [
      {
        "id": 1,
        "userId": 2,
        "familyId": 1,
        "role": "MEMBER",
        "createdAt": "2024-01-01T00:00:00"
      }
    ],
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to get permissions"
  }
  ```

### 8.3 更新权限

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
    "code": 200,
    "data": {
      "id": 1,
      "userId": 2,
      "familyId": 1,
      "role": "ADMIN",
      "createdAt": "2024-01-01T00:00:00"
    },
    "message": "Success"
  }
  ```

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to update permission"
  }
  ```

### 8.4 删除权限

**请求**
- URL: `/api/permissions/{id}`
- 方法: `DELETE`
- 请求头: `Authorization: Bearer <token>`

**响应**
- 状态码: `204 No Content`

**错误响应**
- 状态码: `400 Bad Request`
- 响应体:
  ```json
  {
    "code": 400,
    "data": null,
    "message": "Failed to delete permission"
  }
  ```
