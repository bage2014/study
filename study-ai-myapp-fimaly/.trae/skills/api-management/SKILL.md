# API 管理规范

## 1. 技能概述

本技能提供了家族树应用的API管理规范，包括API设计规范、API契约定义、最佳实践和错误处理等。通过本规范，开发者可以确保API设计的一致性、可维护性和安全性。

## 2. API设计规范

### 2.1 参数传递规范

**禁止**：将参数直接放在URL路径中

❌ 错误示例：
- `/families/{familyId}/members`
- `/members/{memberId}/relationships`

**允许**：
1. **GET请求**：使用查询参数（query parameters）传递参数
2. **POST/PUT请求**：使用请求体（request body）传递参数
3. **资源ID**：只有资源的唯一标识符（ID）可以放在URL路径中

✅ 正确示例：
- **GET请求**：`/members/family?familyId=1`
- **POST请求**：`/members` 配合请求体 `{ "familyId": 1, "name": "张三" }`
- **资源获取**：`/members/1`（这里的1是成员ID，属于资源标识符）

### 2.2 规范原因

1. **安全性**：敏感参数不应该暴露在URL中
2. **可读性**：URL更清晰，参数语义更明确
3. **一致性**：统一的参数传递方式便于维护
4. **灵活性**：查询参数和请求体可以更好地处理复杂参数

### 2.3 常见场景处理

#### 2.3.1 根据父资源获取子资源列表

❌ 错误：
```
GET /api/families/{familyId}/members
```

✅ 正确：
```
GET /api/members/family?familyId=1
```

#### 2.3.2 创建子资源

❌ 错误：
```
POST /api/families/{familyId}/members
{
  "name": "张三"
}
```

✅ 正确：
```
POST /api/members
{
  "familyId": 1,
  "name": "张三"
}
```

#### 2.3.3 获取单个资源（允许使用ID在URL中）

✅ 允许：
```
GET /api/members/1
PUT /api/members/1
DELETE /api/members/1
```

### 2.4 代码示例

#### 2.4.1 后端Spring Boot示例

##### 2.4.1.1 错误的实现（禁止）

```java
@RestController
@RequestMapping("/api")
public class MemberController {
    
    // ❌ 禁止：familyId在URL路径中
    @GetMapping("/families/{familyId}/members")
    public ApiResponse<List<Member>> getMembers(@PathVariable Long familyId) {
        // ...
    }
    
    // ❌ 禁止：familyId在URL路径中
    @PostMapping("/families/{familyId}/members")
    public ApiResponse<Member> addMember(
            @PathVariable Long familyId, 
            @RequestBody Member member) {
        // ...
    }
}
```

##### 2.4.1.2 正确的实现（推荐）

```java
@RestController
@RequestMapping("/api")
public class MemberController {
    
    // ✅ 正确：使用查询参数
    @GetMapping("/members/family")
    public ApiResponse<List<Member>> getMembers(@RequestParam Long familyId) {
        // ...
    }
    
    // ✅ 正确：使用请求体
    @PostMapping("/members")
    public ApiResponse<Member> addMember(@RequestBody Member member) {
        // member对象中包含familyId字段
        // ...
    }
}
```

#### 2.4.2 前端Vue示例

##### 2.4.2.1 错误的实现（禁止）

```javascript
// ❌ 禁止：familyId在URL路径中
async fetchMembersByFamilyId(familyId) {
  const response = await api.get(`/families/${familyId}/members`)
  // ...
}

// ❌ 禁止：familyId在URL路径中
async createMember(memberData) {
  const { familyId, ...memberDataWithoutFamilyId } = memberData
  const response = await api.post(`/families/${familyId}/members`, memberDataWithoutFamilyId)
  // ...
}
```

##### 2.4.2.2 正确的实现（推荐）

```javascript
// ✅ 正确：使用查询参数
async fetchMembersByFamilyId(familyId) {
  const response = await api.get('/members/family', {
    params: { familyId }
  })
  // ...
}

// ✅ 正确：使用请求体
async createMember(memberData) {
  const response = await api.post('/members', memberData)
  // memberData对象中包含familyId字段
  // ...
}
```

## 3. API契约定义

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
- URL: `/api/members`
- 方法: `POST`
- 请求头: `Authorization: Bearer <token>`
- 内容类型: `application/json`
- 请求体:
  ```json
  {
    "familyId": 1,
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
- URL: `/api/members/family`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`
- 查询参数: `familyId=1`

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
- URL: `/api/members/{id}`
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
- URL: `/api/members/{id}`
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
- URL: `/api/members/{id}`
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
- URL: `/api/relationships/member`
- 方法: `GET`
- 请求头: `Authorization: Bearer <token>`
- 查询参数: `memberId=1`

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
async function addMember(memberData) {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/members', {
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

## 6. API版本管理

### 6.1 版本控制策略
- **URL路径版本**：`/api/v1/resource`
- **头部版本**：`Accept: application/vnd.familytree.v1+json`
- **查询参数版本**：`/api/resource?version=1`

### 6.2 版本迁移策略
- 向后兼容：新版本应兼容旧版本API
- 弃用通知：提前通知API弃用计划
- 版本生命周期：明确每个版本的支持期限

## 7. API安全

### 7.1 认证与授权
- 使用JWT进行身份认证
- 基于角色的访问控制（RBAC）
- 权限验证中间件

### 7.2 安全最佳实践
- HTTPS传输
- 输入验证
- 速率限制
- 防止SQL注入
- 防止XSS攻击
- 防止CSRF攻击

## 8. API监控与分析

### 8.1 监控指标
- 请求响应时间
- 错误率
- 并发请求数
- 资源使用情况

### 8.2 分析工具
- Spring Boot Actuator
- Prometheus + Grafana
- ELK Stack（Elasticsearch, Logstash, Kibana）

## 9. 最佳实践

1. **使用HTTPS**：在生产环境中使用HTTPS协议保护API通信
2. **合理使用缓存**：对于不经常变化的数据，可以使用缓存减少API调用
3. **分页处理**：对于大量数据的API，使用分页参数减少数据传输量
4. **错误处理**：对所有API调用进行错误处理，确保用户体验
5. **参数验证**：在前端对用户输入进行验证，减少无效请求
6. **token管理**：合理管理token的存储和刷新，确保安全性
7. **API文档**：使用Swagger等工具自动生成API文档
8. **版本控制**：合理管理API版本，确保向后兼容性
9. **监控**：实现API监控，及时发现和解决问题
10. **测试**：为API编写单元测试和集成测试

## 10. 检查清单

在开发新的API接口时，请检查以下内容：

- [ ] 所有参数都没有直接放在URL路径中（除了资源ID）
- [ ] GET请求使用查询参数传递参数
- [ ] POST/PUT请求使用请求体传递参数
- [ ] 资源ID（如/members/{id}）可以放在URL路径中
- [ ] API契约文档已经更新
- [ ] 前端和后端实现保持一致
- [ ] API有适当的错误处理
- [ ] API有适当的安全措施
- [ ] API有适当的监控机制
- [ ] API有适当的测试覆盖

## 11. 规范执行

所有新开发的API接口必须严格遵守本规范。对于已有的不符合规范的接口，应在后续的迭代中逐步重构，以保持代码库的一致性。

## 12. 总结

本API管理规范提供了全面的API设计、实现和管理指南，包括：
- API设计规范：统一参数传递方式，提高代码可读性和安全性
- API契约定义：详细的API接口文档，便于前后端开发协作
- 错误处理：统一的错误处理机制，提高用户体验
- 安全措施：全面的API安全最佳实践
- 监控与分析：API性能监控和问题排查
- 最佳实践：行业标准的API开发规范

通过遵循本规范，开发者可以构建高质量、可维护、安全的API服务，为家族树应用提供可靠的后端支持。