cat > /Users/bage/bage/github/study/study-ai/myappendpoint/api-docs/user.md << 'EOF'
# UserController API对接文档
以下是UserController的完整API对接文档，包含所有可用接口的详细信息。

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
    "avatarUrl": "https://example.com/avatar.jpg",
    "loginAttempts": 0,
    "lockTime": null,
    "gender": "MALE",
    "birthDate": "1990-01-01"
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

## 4. 发送邮箱验证码

### 请求信息
- **URL**: `/sendEmailCaptcha`
- **方法**: `POST`
- **描述**: 发送邮箱验证码用于注册或重置密码

### 请求参数
| 参数名    | 类型   | 必需 | 默认值 | 描述       |
| --------- | ------ | ---- | ------ | ---------- |
| email     | string | 是   | 无     | 邮箱地址   |
| captcha   | string | 是   | 无     | 图形验证码 |
| requestId | string | 是   | 无     | 请求ID     |

### 请求体示例
```json
{
  "email": "test@example.com",
  "captcha": "ABCD",
  "requestId": "uuid-123456"
}
```

### 响应格式
```json
{
  "code": 200,
  "message": "验证码已发送",
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
# 发送邮箱验证码
curl -X POST 'http://localhost:8080/sendEmailCaptcha' -H 'Content-Type: application/json' -d '{
  "email": "test@example.com",
  "captcha": "ABCD",
  "requestId": "uuid-123456"
}'
```

## 5. 校验token是否有效

### 请求信息
- **URL**: `/checkToken`
- **方法**: `POST`
- **描述**: 校验用户的访问令牌是否有效

### 请求参数
| 参数名 | 类型   | 必需 | 默认值 | 描述   |
| ------ | ------ | ---- | ------ | ------ |
| token  | string | 是   | 无     | 访问令牌 |

### 请求体示例
```json
{
  "token": "1-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

### 响应格式
```json
{
  "code": 200,
  "message": "校验成功",
  "data": true
}
```

### 错误响应
```json
{
  "code": 401,
  "message": "token已过期",
  "data": false
}
```

### 请求示例
```bash
# 校验token是否有效
curl -X POST 'http://localhost:8080/checkToken' -H 'Content-Type: application/json' -d '{
  "token": "1-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}'
```

## 6. 刷新token有效期

### 请求信息
- **URL**: `/refreshToken`
- **方法**: `POST`
- **描述**: 使用刷新令牌获取新的访问令牌

### 请求参数
| 参数名      | 类型   | 必需 | 默认值 | 描述   |
| ----------- | ------ | ---- | ------ | ------ |
| refreshToken | string | 是   | 无     | 刷新令牌 |

### 请求体示例
```json
{
  "refreshToken": "1-refresh-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

### 响应格式
```json
{
  "code": 200,
  "message": "token刷新成功",
  "data": {
    "token": "1-newtoken-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "refreshToken": "1-newrefresh-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "tokenExpireTime": "2023-12-31T23:59:59",
    "refreshTokenExpireTime": "2024-01-14T23:59:59",
    "userId": 1
  }
}
```

### 错误响应
```json
{
  "code": 401,
  "message": "刷新token已过期",
  "data": null
}
```

### 请求示例
```bash
# 刷新token有效期
curl -X POST 'http://localhost:8080/refreshToken' -H 'Content-Type: application/json' -d '{
  "refreshToken": "1-refresh-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}'
```

## 7. 完善用户信息

### 请求信息
- **URL**: `/updateUserInfo`
- **方法**: `POST`
- **描述**: 更新当前登录用户的基本信息

### 请求参数
| 参数名   | 类型   | 必需 | 默认值 | 描述   |
| -------- | ------ | ---- | ------ | ------ |
| username | string | 否   | 无     | 用户名 |
| gender   | string | 否   | 无     | 性别   |
| birthDate | string | 否   | 无     | 出生日期 |

### 请求体示例
```json
{
  "username": "newusername",
  "gender": "MALE",
  "birthDate": "1990-01-01"
}
```

### 响应格式
```json
{
  "code": 200,
  "message": "用户信息更新成功",
  "data": {
    "id": 1,
    "username": "newusername",
    "email": "test@example.com",
    "gender": "MALE",
    "birthDate": "1990-01-01",
    "loginAttempts": 0,
    "lockTime": null
  }
}
```

### 错误响应
```json
{
  "code": 404,
  "message": "用户不存在",
  "data": null
}
```

### 请求示例
```bash
# 完善用户信息
curl -X POST 'http://localhost:8080/updateUserInfo' -H 'Authorization: Bearer your_token_here' -H 'Content-Type: application/json' -d '{
  "username": "newusername",
  "gender": "MALE",
  "birthDate": "1990-01-01"
}'
```

## 8. 重置密码

### 请求信息
- **URL**: `/resetPassword`
- **方法**: `POST`
- **描述**: 通过邮箱验证码重置用户密码

### 请求参数
| 参数名    | 类型   | 必需 | 默认值 | 描述       |
| --------- | ------ | ---- | ------ | ---------- |
| email     | string | 是   | 无     | 邮箱地址   |
| captcha   | string | 是   | 无     | 邮箱验证码 |
| newPassword | string | 是   | 无     | 新密码     |

### 请求体示例
```json
{
  "email": "test@example.com",
  "captcha": "ABCD",
  "newPassword": "newpassword123"
}
```

### 响应格式
```json
{
  "code": 200,
  "message": "密码重置成功",
  "data": null
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "验证码已过期，请重新获取",
  "data": null
}
```

### 请求示例
```bash
# 重置密码
curl -X POST 'http://localhost:8080/resetPassword' -H 'Content-Type: application/json' -d '{
  "email": "test@example.com",
  "captcha": "ABCD",
  "newPassword": "newpassword123"
}'
```

## 9. 查询用户列表

### 请求信息
- **URL**: `/queryUsers`
- **方法**: `GET`
- **描述**: 查询用户列表（支持分页和关键词搜索）

### 请求参数
| 参数名  | 类型   | 必需 | 默认值 | 描述                 |
| ------- | ------ | ---- | ------ | -------------------- |
| keyword | string | 否   | 无     | 搜索关键词（用户名或邮箱） |
| page    | int    | 否   | 0      | 页码（从0开始）      |
| size    | int    | 否   | 10     | 每页条数             |

### 响应格式
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "users": [
        {
          "id": 1,
          "username": "zhangsan",
          "email": "zhangsan@qq.com",
          "avatarUrl": "https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4",
          "loginAttempts": 0,
          "lockTime": null
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "last": true,
      "size": 10,
      "number": 0,
      "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
      },
      "first": true,
      "numberOfElements": 1,
      "empty": false
    },
    "totalElements": 1,
    "totalPages": 1,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "查询用户列表失败",
  "data": null
}
```

### 请求示例
```bash
# 查询用户列表（第1页，每页10条）
curl -X GET 'http://localhost:8080/queryUsers?page=0&size=10'

# 根据关键词搜索用户
curl -X GET 'http://localhost:8080/queryUsers?keyword=zhangsan&page=0&size=10'
```

## 数据结构说明

### User 对象
| 字段名        | 类型          | 描述                           |
| ------------- | ------------- | ------------------------------ |
| id            | Long          | 用户ID                         |
| username      | string        | 用户名                         |
| email         | string        | 邮箱                           |
| avatarUrl     | string        | 头像URL                        |
| gender        | string        | 性别                           |
| birthDate     | string        | 出生日期                       |
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

### QueryUserResponse 对象
| 字段名        | 类型      | 描述         |
| ------------- | --------- | ------------ |
| users         | Page<User>| 用户列表     |
| totalElements | long      | 总记录数     |
| totalPages    | int       | 总页数       |
| currentPage   | int       | 当前页码     |
| pageSize      | int       | 每页记录数   |