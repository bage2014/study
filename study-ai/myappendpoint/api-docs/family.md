# FamilyController API 对接文档

以下是FamilyController的所有可用接口的详细信息。

## 接口列表

| 接口地址 | 请求方式 | 功能描述 |
|---------|----------|----------|
| `/family/members` | POST | 添加家族成员 |
| `/family/save/mock` | GET/POST | 生成模拟家族成员数据 |
| `/family/members/{id}` | GET | 根据ID获取家族成员信息 |
| `/family/relationships` | POST | 添加家族关系 |
| `/family/tree/{rootId}` | GET | 获取家族树结构 |
| `/family/members/query` | GET | 关键词搜索和分页查询家族成员 |

---

## 1. 添加家族成员

### 基本信息
- **URL**: `/family/members`
- **方法**: `POST`
- **描述**: 添加一个新的家族成员

### 请求参数
| 参数名 | 类型 | 必需 | 默认值 | 描述 |
|--------|------|------|--------|------|
| member | FamilyMember | 是 | 无 | 家族成员对象 |

### FamilyMember 对象结构
```json
{
  "name": "张三",
  "avatar": "https://example.com/avatar.jpg",
  "gender": "MALE",
  "birthDate": "1990-01-01",
  "birthPlace": "北京",
  "occupation": "工程师",
  "education": "本科",
  "contactInfo": "13800138000",
  "deceased": false,
  "deathDate": null,
  "generation": 0
}
```

### 请求示例
```bash
curl -X POST 'http://localhost:8080/family/members' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "张三",
    "avatar": "https://example.com/avatar.jpg",
    "gender": "MALE",
    "birthDate": "1990-01-01",
    "occupation": "工程师",
    "education": "本科",
    "contactInfo": "13800138000",
    "deceased": false,
    "generation": 0
  }'
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "张三",
    "avatar": "https://example.com/avatar.jpg",
    "gender": "MALE",
    "birthDate": "1990-01-01",
    "occupation": "工程师",
    "education": "本科",
    "contactInfo": "13800138000",
    "deceased": false,
    "generation": 0
  }
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "参数错误信息",
  "data": null
}
```

---

## 2. 生成模拟家族成员数据

### 基本信息
- **URL**: `/family/save/mock`
- **方法**: `GET` / `POST`
- **描述**: 生成模拟的家族成员数据（用于测试）

### 请求参数
无

### 请求示例
```bash
curl -X GET 'http://localhost:8080/family/save/mock'
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "张三",
    "avatar": "https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4",
    "gender": "MALE",
    "birthDate": "1990-01-01",
    "occupation": "工程师",
    "education": "本科",
    "contactInfo": "13800138000",
    "deceased": false,
    "generation": 0
  }
}
```

---

## 3. 根据ID获取家族成员信息

### 基本信息
- **URL**: `/family/members/{id}`
- **方法**: `GET`
- **描述**: 根据成员ID获取家族成员详细信息

### 请求参数
| 参数名 | 类型 | 必需 | 默认值 | 描述 |
|--------|------|------|--------|------|
| id | Long | 是 | 无 | 家族成员ID |

### 请求示例
```bash
curl -X GET 'http://localhost:8080/family/members/1'
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "张三",
    "avatar": "https://example.com/avatar.jpg",
    "gender": "MALE",
    "birthDate": "1990-01-01",
    "occupation": "工程师",
    "education": "本科",
    "contactInfo": "13800138000",
    "deceased": false,
    "generation": 0
  }
}
```

### 错误响应
```json
{
  "code": 404,
  "message": "成员不存在",
  "data": null
}
```

---

## 4. 添加家族关系

### 基本信息
- **URL**: `/family/relationships`
- **方法**: `POST`
- **描述**: 添加两个家族成员之间的关系

### 请求参数
| 参数名 | 类型 | 必需 | 默认值 | 描述 |
|--------|------|------|--------|------|
| relationship | FamilyRelationship | 是 | 无 | 家族关系对象 |

### FamilyRelationship 对象结构
```json
{
  "member1": {
    "id": 1
  },
  "member2": {
    "id": 2
  },
  "type": "SPOUSE",
  "verificationStatus": "PENDING",
  "startDate": "2020-01-01",
  "endDate": null
}
```

### 请求示例
```bash
curl -X POST 'http://localhost:8080/family/relationships' \
  -H 'Content-Type: application/json' \
  -d '{
    "member1": {"id": 1},
    "member2": {"id": 2},
    "type": "SPOUSE",
    "verificationStatus": "PENDING",
    "startDate": "2020-01-01"
  }'
```

### 响应格式
```json
{
  "code": 200,
  "message": "success"
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "参数错误信息",
  "data": null
}
```

---

## 5. 获取家族树结构

### 基本信息
- **URL**: `/family/tree/{rootId}`
- **方法**: `GET`
- **描述**: 根据根节点ID获取家族树结构

### 请求参数
| 参数名 | 类型 | 必需 | 默认值 | 描述 |
|--------|------|------|--------|------|
| rootId | Long | 是 | 无 | 根节点成员ID |
| generations | int | 否 | 3 | 要显示的代数 |

### 请求示例
```bash
curl -X GET 'http://localhost:8080/family/tree/1?generations=3'
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "张三",
    "avatar": "https://example.com/avatar.jpg",
    "generation": 0,
    "relationship": "根节点",
    "children": [
      {
        "id": 2,
        "name": "李四",
        "avatar": "https://example.com/avatar2.jpg",
        "generation": 0,
        "relationship": "配偶",
        "children": []
      },
      {
        "id": 3,
        "name": "张小三",
        "avatar": "https://example.com/avatar3.jpg",
        "generation": 1,
        "relationship": "子女",
        "children": []
      }
    ]
  }
}
```

### 错误响应
```json
{
  "code": 404,
  "message": "未找到家族树",
  "data": null
}
```

---

## 6. 关键词搜索和分页查询家族成员

### 基本信息
- **URL**: `/family/members/query`
- **方法**: `GET`
- **描述**: 根据关键词搜索家族成员并支持分页

### 请求参数
| 参数名 | 类型 | 必需 | 默认值 | 描述 |
|--------|------|------|--------|------|
| keyword | String | 否 | 无 | 搜索关键词，可搜索姓名、出生地、职业、教育经历、联系方式等字段 |
| page | int | 否 | 0 | 页码，从0开始 |
| size | int | 否 | 10 | 每页记录数 |
| sort | String | 否 | id,asc | 排序方式，格式为"字段名,asc/desc" |

### 请求示例
```bash
curl -X GET 'http://localhost:8080/family/members/query?keyword=张三&page=0&size=10&sort=id,asc'
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalElements": 50,
    "totalPages": 5,
    "pageNumber": 0,
    "pageSize": 10,
    "members": [
      {
        "id": 1,
        "name": "张三",
        "avatar": "https://example.com/avatar.jpg",
        "gender": "MALE",
        "birthDate": "1990-01-01",
        "birthPlace": "北京",
        "occupation": "工程师",
        "education": "本科",
        "contactInfo": "13800138000",
        "deceased": false,
        "generation": 0
      },
      {
        "id": 2,
        "name": "张三明",
        "avatar": "https://example.com/avatar2.jpg",
        "gender": "MALE",
        "birthDate": "1985-05-10",
        "birthPlace": "上海",
        "occupation": "教师",
        "education": "硕士",
        "contactInfo": "13900139000",
        "deceased": false,
        "generation": 1
      }
      // 更多成员数据
    ]
  }
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "分页参数错误",
  "data": null
}
```

---

## 通用错误响应

所有接口都可能返回以下错误响应：

```json
{
  "code": 500,
  "message": "服务器内部错误",
  "data": null
}
```

## 响应格式说明

所有接口都使用统一的ApiResponse格式：

```json
{
  "code": 200,           // 状态码
  "message": "success",   // 消息
  "data": {}             // 数据内容
}
```

- **200**: 成功
- **400**: 参数错误
- **404**: 资源不存在
- **500**: 服务器内部错误" 