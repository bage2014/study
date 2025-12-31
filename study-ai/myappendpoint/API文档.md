# 项目API文档

## 1. 项目介绍

本项目是一个包含用户管理、文件管理、图片管理、验证码、消息管理和家族成员管理等功能的RESTful API服务。

## 2. 基础URL

所有API请求的基础URL为：`http://localhost:8080`

## 3. 认证方式

- 使用JWT Token进行认证
- Token需放在请求头的Authorization字段中
- 格式：`Bearer {token}`

## 4. API分类

### 4.1 用户管理

#### 4.1.1 登录
- **请求URL**：`/login`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | username | String | 是 | 用户名/邮箱 |
  | password | String | 是 | 密码 |
  | captcha | String | 否 | 验证码（登录失败1次后需要） |
  | requestId | String | 否 | 请求ID（用于验证码验证） |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "user": {
        "id": 1,
        "username": "zhangsan",
        "email": "zhangsan@qq.com",
        "avatarUrl": "https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4"
      },
      "userToken": {
        "token": "1-xxx",
        "refreshToken": "1-refresh-xxx",
        "tokenExpireTime": "2023-12-31T12:00:00",
        "refreshTokenExpireTime": "2024-01-14T12:00:00"
      }
    }
  }
  ```

#### 4.1.2 注册
- **请求URL**：`/register`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | email | String | 是 | 邮箱 |
  | password | String | 是 | 密码 |
  | captcha | String | 是 | 邮箱验证码 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": null
  }
  ```

#### 4.1.3 获取用户信息
- **请求URL**：`/profile`
- **请求方法**：GET
- **请求参数**：无
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "username": "zhangsan",
      "email": "zhangsan@qq.com",
      "avatarUrl": "https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4"
    }
  }
  ```

#### 4.1.4 发送邮箱验证码
- **请求URL**：`/sendEmailCaptcha`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | email | String | 是 | 邮箱 |
  | captcha | String | 是 | 图片验证码 |
  | requestId | String | 是 | 请求ID |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "验证码已发送",
    "data": null
  }
  ```

#### 4.1.5 校验Token
- **请求URL**：`/checkToken`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | token | String | 是 | Token值 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "校验成功",
    "data": true
  }
  ```

#### 4.1.6 刷新Token
- **请求URL**：`/refreshToken`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | refreshToken | String | 是 | 刷新Token |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "token刷新成功",
    "data": {
      "token": "1-xxx",
      "refreshToken": "1-refresh-xxx",
      "tokenExpireTime": "2023-12-31T12:00:00"
    }
  }
  ```

#### 4.1.7 更新用户信息
- **请求URL**：`/updateUserInfo`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | username | String | 否 | 用户名 |
  | gender | String | 否 | 性别 |
  | birthDate | String | 否 | 出生日期 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "用户信息更新成功",
    "data": {
      "id": 1,
      "username": "张三",
      "gender": "MALE",
      "birthDate": "1990-01-01"
    }
  }
  ```

#### 4.1.8 重置密码
- **请求URL**：`/resetPassword`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | email | String | 是 | 邮箱 |
  | captcha | String | 是 | 验证码 |
  | newPassword | String | 是 | 新密码 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "密码重置成功",
    "data": null
  }
  ```

#### 4.1.9 查询用户列表
- **请求URL**：`/queryUsers`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | keyword | String | 否 | 搜索关键词 |
  | page | int | 否 | 页码（默认0） |
  | size | int | 否 | 每页大小（默认10） |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "users": [
        {
          "id": 1,
          "username": "zhangsan"
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "currentPage": 0,
      "pageSize": 10
    }
  }
  ```

### 4.2 文件管理

#### 4.2.1 上传文件
- **请求URL**：`/app/file/upload`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | file | MultipartFile | 是 | 要上传的文件 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "文件上传成功",
    "data": {
      "fileId": 1,
      "fileName": "xxx.jpg",
      "originalFileName": "test.jpg",
      "fileSize": 1024,
      "fileType": "image/jpeg",
      "fileUrl": "/app/file/download/xxx.jpg",
      "createdTime": "2023-12-01T12:00:00"
    }
  }
  ```

#### 4.2.2 查询文件列表
- **请求URL**：`/app/file/list`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | page | int | 否 | 页码（默认1） |
  | size | int | 否 | 每页大小（默认10） |
  | keyword | String | 否 | 搜索关键词 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "files": [
        {
          "id": 1,
          "fileName": "xxx.jpg",
          "originalFileName": "test.jpg"
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "currentPage": 1,
      "pageSize": 10
    }
  }
  ```

#### 4.2.3 下载文件
- **请求URL**：`/app/file/download/{fileName}`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | fileName | String | 是 | 文件名 |
- **响应**：文件流

#### 4.2.4 获取文件信息
- **请求URL**：`/app/file/info/{fileId}`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | fileId | Long | 是 | 文件ID |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "fileName": "xxx.jpg",
      "originalFileName": "test.jpg",
      "fileSize": 1024,
      "fileType": "image/jpeg"
    }
  }
  ```

#### 4.2.5 删除文件
- **请求URL**：`/app/file/delete/{fileId}`
- **请求方法**：DELETE
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | fileId | Long | 是 | 文件ID |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "文件删除成功",
    "data": null
  }
  ```

### 4.3 图片管理

#### 4.3.1 上传图片
- **请求URL**：`/images/upload`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | file | MultipartFile | 是 | 要上传的图片 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "文件上传成功",
    "data": {
      "fileId": "1",
      "originalFileName": "test.jpg",
      "fileName": "xxx.jpg"
    }
  }
  ```

#### 4.3.2 访问图片
- **请求URL**：`/images/item/{fileName}`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | fileName | String | 是 | 图片文件名 |
- **响应**：图片流

### 4.4 验证码

#### 4.4.1 获取验证码
- **请求URL**：`/captcha`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | requestId | String | 是 | 请求ID |
- **响应**：验证码图片流

### 4.5 消息管理

#### 4.5.1 发送消息
- **请求URL**：`/message/send`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | receiverId | Long | 否 | 接收者ID |
  | content | String | 是 | 消息内容 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "senderId": 1,
      "receiverId": 2,
      "content": "你好",
      "createTime": "2023-12-01T12:00:00"
    }
  }
  ```

#### 4.5.2 发送邮件消息
- **请求URL**：`/message/sendEmail`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | senderId | Long | 是 | 发送者ID |
  | email | String | 是 | 接收者邮箱 |
  | content | String | 是 | 消息内容 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "senderId": 1,
      "email": "test@example.com",
      "content": "你好",
      "isEmail": true,
      "createTime": "2023-12-01T12:00:00"
    }
  }
  ```

#### 4.5.3 标记消息为已读
- **请求URL**：`/message/{id}/read`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | id | Long | 是 | 消息ID |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "isRead": true,
      "readTime": "2023-12-01T12:01:00"
    }
  }
  ```

#### 4.5.4 删除消息
- **请求URL**：`/message/{id}`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | id | Long | 是 | 消息ID |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": null
  }
  ```

#### 4.5.5 查询消息列表
- **请求URL**：`/message/query`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | receiverId | Long | 否 | 接收者ID |
  | isRead | Boolean | 否 | 是否已读 |
  | startTime | String | 否 | 开始时间（格式：yyyy-MM-dd'T'HH:mm:ss） |
  | endTime | String | 否 | 结束时间（格式：yyyy-MM-dd'T'HH:mm:ss） |
  | page | int | 否 | 页码（默认1） |
  | size | int | 否 | 每页大小（默认10） |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "messages": [
        {
          "id": 1,
          "senderId": 1,
          "receiverId": 2,
          "senderName": "zhangsan",
          "senderAvatar": "https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4",
          "content": "你好",
          "isRead": false,
          "createTime": "2023-12-01T12:00:00"
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "currentPage": 1,
      "pageSize": 10
    }
  }
  ```

### 4.6 家族成员管理

#### 4.6.1 查询成员列表
- **请求URL**：`/family/members/query`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | keyword | String | 否 | 搜索关键词 |
  | page | int | 否 | 页码（默认0） |
  | size | int | 否 | 每页大小（默认10） |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "members": [
        {
          "id": 1,
          "name": "张三",
          "avatar": "https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4",
          "gender": "MALE",
          "birthDate": "1990-01-01",
          "occupation": "工程师"
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "currentPage": 0,
      "pageSize": 10
    }
  }
  ```

#### 4.6.2 添加成员
- **请求URL**：`/family/members/add`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | name | String | 是 | 姓名 |
  | avatar | String | 否 | 头像URL |
  | gender | String | 否 | 性别（MALE/FEMALE） |
  | birthDate | String | 否 | 出生日期 |
  | occupation | String | 否 | 职业 |
  | education | String | 否 | 学历 |
  | contactInfo | String | 否 | 联系方式 |
  | deceased | Boolean | 否 | 是否去世 |
  | deathDate | String | 否 | 去世日期 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "name": "张三"
    }
  }
  ```

#### 4.6.3 更新成员
- **请求URL**：`/family/members/update`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | id | Long | 是 | 成员ID |
  | name | String | 否 | 姓名 |
  | avatar | String | 否 | 头像URL |
  | gender | String | 否 | 性别 |
  | birthDate | String | 否 | 出生日期 |
  | occupation | String | 否 | 职业 |
  | education | String | 否 | 学历 |
  | contactInfo | String | 否 | 联系方式 |
  | deceased | Boolean | 否 | 是否去世 |
  | deathDate | String | 否 | 去世日期 |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "name": "张三",
      "occupation": "高级工程师"
    }
  }
  ```

#### 4.6.4 删除成员
- **请求URL**：`/family/members/delete`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | id | Long | 是 | 成员ID |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": "ok"
  }
  ```

#### 4.6.5 获取成员详情
- **请求URL**：`/family/members/{id}`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | id | Long | 是 | 成员ID |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "name": "张三",
      "avatar": "https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4",
      "gender": "MALE",
      "birthDate": "1990-01-01",
      "occupation": "工程师"
    }
  }
  ```

#### 4.6.6 添加关系
- **请求URL**：`/family/relationships`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | member1.id | Long | 否 | 成员1 ID |
  | member2.id | Long | 是 | 成员2 ID |
  | type | String | 是 | 关系类型（SPOUSE/PARENT_CHILD） |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "member1": {
        "id": 1,
        "name": "张三"
      },
      "member2": {
        "id": 2,
        "name": "李四"
      },
      "type": "SPOUSE"
    }
  }
  ```

#### 4.6.7 删除关系
- **请求URL**：`/family/relationships/delete`
- **请求方法**：POST
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | id | Long | 是 | 关系ID |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": "ok"
  }
  ```

#### 4.6.8 获取家族树
- **请求URL**：`/family/tree/{rootId}`
- **请求方法**：GET
- **请求参数**：
  | 参数名 | 类型 | 是否必填 | 描述 |
  | ------ | ---- | -------- | ---- |
  | rootId | Long | 是 | 根节点成员ID |
  | generations | int | 否 | 要获取的代数（默认3） |
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "member": {
        "id": 1,
        "name": "张三"
      },
      "children": [
        {
          "member": {
            "id": 3,
            "name": "张小三"
          },
          "children": []
        }
      ],
      "spouse": {
        "member": {
          "id": 2,
          "name": "李四"
        }
      }
    }
  }
  ```

#### 4.6.9 生成模拟数据
- **请求URL**：`/family/save/mock`
- **请求方法**：GET
- **请求参数**：无
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "name": "张三"
    }
  }
  ```

## 5. 错误码说明

| 错误码 | 描述 |
| ------ | ---- |
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权/登录失败 |
| 403 | 拒绝访问（账号锁定） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 6. 响应格式说明

所有API响应都遵循以下格式：

```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

- `code`：响应状态码
- `message`：响应消息
- `data`：响应数据（根据接口不同而变化）

## 7. 注意事项

1. 所有时间格式均为ISO 8601格式：`yyyy-MM-dd'T'HH:mm:ss`
2. 文件上传最大限制为100MB
3. 图片上传最大限制为10MB
4. 验证码有效期为5分钟
5. Token有效期为7天，刷新Token有效期为14天
6. 登录失败5次后账号将被锁定1天

## 8. 版本信息

- 版本号：1.0.0
- 更新时间：2025-12-31
- 更新内容：初始版本