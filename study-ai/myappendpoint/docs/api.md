# 创建docs/api.md文件并写入API设计文档内容
cat > /Users/bage/bage/github/study/study-ai/myappendpoint/docs/api.md << 'EOF'
# 家庭管理应用 API 设计文档

## 1. 概述

本文档定义了家庭管理应用的RESTful API接口规范，包括用户认证、家庭管理、事务管理、日程管理和财务管理等核心功能模块。

### 1.1 基础信息
- **API版本**: v1.0
- **基础路径**: `/api/v1`
- **认证方式**: JWT Bearer Token
- **数据格式**: JSON
- **字符编码**: UTF-8

### 1.2 通用响应格式

#### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2024-01-01T10:00:00Z"
}
```

#### 错误响应
```json
{
  "code": 400,
  "message": "请求参数错误",
  "data": null,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### 1.3 状态码说明
| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 2. 认证授权接口

### 2.1 用户注册
- **URL**: `POST /api/v1/auth/register`
- **描述**: 新用户注册
- **请求头**: `Content-Type: application/json`

**请求参数**:
```json
{
  "username": "string, 必填, 用户名",
  "email": "string, 必填, 邮箱",
  "password": "string, 必填, 密码",
  "realName": "string, 选填, 真实姓名",
  "phone": "string, 选填, 手机号"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 1,
    "username": "testuser",
    "email": "test@example.com"
  }
}
```

### 2.2 用户登录
- **URL**: `POST /api/v1/auth/login`
- **描述**: 用户登录

**请求参数**:
```json
{
  "username": "string, 必填, 用户名或邮箱",
  "password": "string, 必填, 密码"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 3600,
    "userInfo": {
      "id": 1,
      "username": "testuser",
      "email": "test@example.com",
      "realName": "张三"
    }
  }
}
```

### 2.3 刷新Token
- **URL**: `POST /api/v1/auth/refresh`
- **描述**: 刷新访问令牌
- **认证**: 需要Refresh Token

**请求参数**:
```json
{
  "refreshToken": "string, 必填, 刷新令牌"
}
```

## 3. 家庭管理接口

### 3.1 获取家庭列表
- **URL**: `GET /api/v1/families`
- **描述**: 获取用户所属的家庭列表
- **认证**: 需要Access Token

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "families": [
      {
        "id": 1,
        "familyName": "张家",
        "familyCode": "ZHANG001",
        "description": "这是一个温馨的家庭",
        "memberCount": 4,
        "ownerId": 1,
        "createdTime": "2024-01-01T10:00:00Z"
      }
    ],
    "total": 1
  }
}
```

### 3.2 创建家庭
- **URL**: `POST /api/v1/families`
- **描述**: 创建新的家庭

**请求参数**:
```json
{
  "familyName": "string, 必填, 家庭名称",
  "description": "string, 选填, 家庭描述"
}
```

### 3.3 获取家庭详情
- **URL**: `GET /api/v1/families/{familyId}`
- **描述**: 获取指定家庭的详细信息

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "familyName": "张家",
    "familyCode": "ZHANG001",
    "description": "这是一个温馨的家庭",
    "owner": {
      "id": 1,
      "username": "zhangsan",
      "realName": "张三"
    },
    "members": [
      {
        "id": 1,
        "username": "zhangsan",
        "realName": "张三",
        "role": "FAMILY_OWNER",
        "joinTime": "2024-01-01T10:00:00Z"
      },
      {
        "id": 2,
        "username": "lisi",
        "realName": "李四",
        "role": "FAMILY_MEMBER",
        "joinTime": "2024-01-02T10:00:00Z"
      }
    ],
    "createdTime": "2024-01-01T10:00:00Z"
  }
}
```

### 3.4 添加家庭成员
- **URL**: `POST /api/v1/families/{familyId}/members`
- **描述**: 邀请用户加入家庭

**请求参数**:
```json
{
  "usernameOrEmail": "string, 必填, 用户名或邮箱",
  "role": "string, 选填, 角色（FAMILY_MEMBER/GUEST）"
}
```

## 4. 事务管理接口

### 4.1 获取事务列表
- **URL**: `GET /api/v1/families/{familyId}/tasks`
- **描述**: 获取家庭事务列表，支持分页和筛选

**查询参数**:
- `page`: 页码，默认0
- `size`: 每页大小，默认10
- `status`: 事务状态（PENDING/IN_PROGRESS/COMPLETED/CANCELLED）
- `priority`: 优先级（LOW/MEDIUM/HIGH/URGENT）
- `assignedTo`: 分配给的用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "tasks": [
      {
        "id": 1,
        "title": "购买 groceries",
        "description": "购买本周的食品和生活用品",
        "taskType": "WEEKLY",
        "priority": "MEDIUM",
        "status": "PENDING",
        "assignedTo": {
          "id": 2,
          "realName": "李四"
        },
        "createdBy": {
          "id": 1,
          "realName": "张三"
        },
        "dueDate": "2024-01-07",
        "createdTime": "2024-01-01T10:00:00Z"
      }
    ],
    "totalElements": 15,
    "totalPages": 2,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

### 4.2 创建事务
- **URL**: `POST /api/v1/families/{familyId}/tasks`
- **描述**: 创建新的事务

**请求参数**:
```json
{
  "title": "string, 必填, 事务标题",
  "description": "string, 选填, 事务描述",
  "taskType": "string, 必填, 事务类型",
  "priority": "string, 选填, 优先级",
  "assignedTo": "number, 选填, 分配给的用户ID",
  "dueDate": "string, 选填, 截止日期"
}
```

### 4.3 更新事务状态
- **URL**: `PUT /api/v1/families/{familyId}/tasks/{taskId}/status`
- **描述**: 更新事务状态

**请求参数**:
```json
{
  "status": "string, 必填, 新状态"
}
```

## 5. 日程管理接口

### 5.1 获取日程列表
- **URL**: `GET /api/v1/schedules`
- **描述**: 获取个人和家庭共享的日程

**查询参数**:
- `startDate`: 开始日期
- `endDate`: 结束日期
- `scheduleType`: 日程类型（PERSONAL/FAMILY）

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "schedules": [
      {
        "id": 1,
        "title": "家庭聚餐",
        "description": "周末家庭聚餐",
        "scheduleType": "FAMILY",
        "startTime": "2024-01-06T18:00:00Z",
        "endTime": "2024-01-06T20:00:00Z",
        "location": "家里",
        "createdBy": {
          "id": 1,
          "realName": "张三"
        },
        "familyId": 1,
        "reminderMinutes": 60
      }
    ]
  }
}
```

### 5.2 创建日程
- **URL**: `POST /api/v1/schedules`
- **描述**: 创建新的日程

**请求参数**:
```json
{
  "title": "string, 必填, 日程标题",
  "description": "string, 选填, 日程描述",
  "scheduleType": "string, 必填, 日程类型",
  "startTime": "string, 必填, 开始时间",
  "endTime": "string, 必填, 结束时间",
  "location": "string, 选填, 地点",
  "familyId": "number, 选填, 家庭ID（家庭日程需要）",
  "reminderMinutes": "number, 选填, 提醒时间（分钟）"
}
```

## 6. 财务管理接口

### 6.1 获取财务记录
- **URL**: `GET /api/v1/families/{familyId}/financial-records`
- **描述**: 获取家庭财务记录，支持分页和筛选

**查询参数**:
- `page`: 页码，默认0
- `size`: 每页大小，默认10
- `recordType`: 记录类型（INCOME/EXPENSE）
- `startDate`: 开始日期
- `endDate`: 结束日期
- `category`: 分类

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "recordType": "EXPENSE",
        "amount": 150.50,
        "category": "食品",
        "description": "超市购物",
        "recordDate": "2024-01-05",
        "createdBy": {
          "id": 1,
          "realName": "张三"
        }
      }
    ],
    "totalElements": 25,
    "totalPages": 3,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

### 6.2 添加财务记录
- **URL**: `POST /api/v1/families/{familyId}/financial-records`
- **描述**: 添加新的财务记录

**请求参数**:
```json
{
  "recordType": "string, 必填, 记录类型",
  "amount": "number, 必填, 金额",
  "category": "string, 必填, 分类",
  "description": "string, 选填, 描述",
  "recordDate": "string, 必填, 记录日期"
}
```

### 6.3 获取财务统计
- **URL**: `GET /api/v1/families/{familyId}/financial-summary`
- **描述**: 获取财务统计信息

**查询参数**:
- `startDate`: 开始日期
- `endDate`: 结束日期

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalIncome": 5000.00,
    "totalExpense": 3500.00,
    "balance": 1500.00,
    "expenseByCategory": [
      {
        "category": "食品",
        "amount": 1200.00,
        "percentage": 34.29
      },
      {
        "category": "住房",
        "amount": 1500.00,
        "percentage": 42.86
      }
    ],
    "monthlyTrends": [
      {
        "month": "2024-01",
        "income": 5000.00,
        "expense": 3500.00
      }
    ]
  }
}
```

## 7. 消息通知接口

### 7.1 获取消息列表
- **URL**: `GET /api/v1/notifications`
- **描述**: 获取用户的消息通知

**查询参数**:
- `page`: 页码，默认0
- `size`: 每页大小，默认10
- `read`: 是否已读（true/false）

### 7.2 标记消息已读
- **URL**: `PUT /api/v1/notifications/{notificationId}/read`
- **描述**: 标记消息为已读

## 8. 文件上传接口

### 8.1 上传家庭头像
- **URL**: `POST /api/v1/families/{familyId}/avatar`
- **描述**: 上传家庭头像
- **Content-Type**: `multipart/form-data`

**请求参数**:
- `file`: 图片文件（支持JPG、PNG格式，最大5MB）

## 9. 错误码说明

| 错误码 | 说明 | 解决方案 |
|--------|------|----------|
| 1001 | 用户名已存在 | 使用其他用户名 |
| 1002 | 邮箱已注册 | 使用其他邮箱或找回密码 |
| 1003 | 密码强度不足 | 密码需包含字母、数字和特殊字符 |
| 2001 | 家庭不存在 | 检查家庭ID是否正确 |
| 2002 | 无权限操作家庭 | 需要家庭管理员权限 |
| 2003 | 家庭成员已达上限 | 升级家庭套餐或移除部分成员 |
| 3001 | 事务不存在 | 检查事务ID是否正确 |
| 4001 | 日程时间冲突 | 调整日程时间 |

## 10. 接口测试

### 10.1 测试环境
- **基础URL**: `http://localhost:8080`
- **测试账号**: testuser / password123

### 10.2 测试工具
- Postman
- Swagger UI (如果启用)
- curl命令

### 10.3 示例请求
```bash
# 用户登录
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# 获取家庭列表
curl -X GET http://localhost:8080/api/v1/families \
  -H "Authorization: Bearer {access_token}"
```

---

**文档版本**: v1.0  
**最后更新**: 2024年  
**维护人员**: API开发团队
EOF

# 确认文件创建成功
ls -la /Users/bage/bage/github/study/study-ai/myappendpoint/docs/api.md