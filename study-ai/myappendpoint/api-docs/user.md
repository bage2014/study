# UserController API对接文档
以下是按照tv.md格式生成的UserController API对接文档，包含所有可用接口的详细信息。

## 1. 获取用户信息

### 请求信息
- **URL**: `/profile`
- **方法**: `GET`
- **描述**: 获取当前登录用户的基本信息

### 请求参数
无（需在请求头中携带有效token）

### 响应格式
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "username": "test@example.com",
    "email": "test@example.com",
    "loginAttempts": 0,
    "lockTime": null
  }
}
```

### 错误响应
```json
{
  "code": 401,
  "message": "未登录",
  "data": null
}
```

### 请求示例
```bash
# 获取当前登录用户信息
curl -X GET 'http://localhost:8080/profile' -H 'Authorization: Bearer your_token_here'
```

## 2. 用户登录

### 请求信息
- **URL**: `/login`
- **方法**: `POST`
- **描述**: 用户登录认证

### 请求参数
| 参数名    | 类型   | 必需 | 默认值 | 描述                        |
| --------- | ------ | ---- | ------ | --------------------------- |
| username  | string | 是   | 无     | 用户名/邮箱                 |
| password  | string | 是   | 无     | 密码                        |
| captcha   | string | 否   | 无     | 验证码（登录失败1次后需要） |
| requestId | string | 否   | 无     | 请求ID（验证码相关）        |

### 请求体示例
```json
{
  "username": "test@example.com",
  "password": "123456",
  "captcha": "ABCD",
  "requestId": "uuid-123456"
}
```

### 响应格式
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "user": {
      "id": 1,
      "username": "test@example.com",
      "email": "test@example.com",
      "loginAttempts": 0,
      "lockTime": null
    },
    "userToken": {
      "token": "1-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
      "refreshToken": "1-refresh-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
      "tokenExpireTime": "2023-12-31T23:59:59",
      "refreshTokenExpireTime": "2024-01-14T23:59:59",
      "userId": 1
    }
  }
}
```

### 错误响应
```json
{
  "code": 401,
  "message": "用户名或密码错误，还有4次机会",
  "data": {
    "user": {
      "loginAttempts": 1
    },
    "userToken": null
  }
}
```

### 请求示例
```bash
# 用户登录
curl -X POST 'http://localhost:8080/login' -H 'Content-Type: application/json' -d '{
  "username": "test@example.com",
  "password": "123456"
}'
```

## 3. 用户注册

### 请求信息
- **URL**: `/register`
- **方法**: `POST`
- **描述**: 注册新用户

### 请求参数
| 参数名   | 类型   | 必需 | 默认值 | 描述       |
| -------- | ------ | ---- | ------ | ---------- |
| email    | string | 是   | 无     | 邮箱地址   |
| password | string | 是   | 无     | 密码       |
| captcha  | string | 是   | 无     | 邮箱验证码 |

### 请求体示例
```json
{
  "email": "test@example.com",
  "password": "123456",
  "captcha": "ABCD"
}
```

### 响应格式
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "验证码错误",
  "data": null
}
```

### 请求示例
```bash
# 用户注册
curl -X POST 'http://localhost:8080/register' -H 'Content-Type: application/json' -d '{
  "email": "test@example.com",
  "password": "123456",
  "captcha": "ABCD"
}'
```

## 数据结构说明

### User 对象
| 字段名        | 类型          | 描述                           |
| ------------- | ------------- | ------------------------------ |
| id            | Long          | 用户ID                         |
| username      | string        | 用户名                         |
| email         | string        | 邮箱                           |
| loginAttempts | Integer       | 登录尝试次数                   |
| lockTime      | LocalDateTime | 账号锁定时间（null表示未锁定） |
| password      | string        | 密码（响应中会置为null）       |

### UserToken 对象
| 字段名                 | 类型          | 描述             |
| ---------------------- | ------------- | ---------------- |
| token                  | string        | 访问令牌         |
| refreshToken           | string        | 刷新令牌         |
| tokenExpireTime        | LocalDateTime | 令牌过期时间     |
| refreshTokenExpireTime | LocalDateTime | 刷新令牌过期时间 |
| userId                 | Long          | 用户ID           |

### LoginResponse 对象
| 字段名    | 类型      | 描述     |
| --------- | --------- | -------- |
| user      | User      | 用户信息 |
| userToken | UserToken | 令牌信息 |



## 4. 用户查询

### 请求信息

- **URL**: `/query
- **方法**: `POST`
- **描述**: 注册新用户

```
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "users": {
      "content": [
        {
          "id": 1,
          "username": "zhangsan",
          "email": "zhangsan@qq.com",
          "avatarUrl": "https://...",
          "loginAttempts": 0,
          "lockTime": null
        }
      ],
      "pageable": { /* 分页信息 */ },
      "totalElements": 1,
      "totalPages": 1,
      "last": true,
      "size": 10,
      "number": 0
    },
    "totalElements": 1,
    "totalPages": 1,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

