
# IPTV API 文档

## 概述
IPTV控制器提供IPTV频道的管理功能，包括频道查询、收藏管理、标签过滤等。

## 基础信息
- 基础路径: `/iptv`
- 认证方式: 需要用户登录（部分接口）
- 响应格式: 统一使用 `ApiResponse` 格式

## API接口列表

### 1. 获取频道列表

#### 请求
- **方法**: POST
- **路径**: `/iptv/channels`
- **Content-Type**: `application/json`

#### 请求参数
```json
{
  "page": 0,
  "size": 10,
  "category": "娱乐",
  "keyword": "电影"
}
```

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "电影频道",
      "url": "http://example.com/movie",
      "group": "娱乐",
      "category": "电影",
      "logo": "http://example.com/logo.png"
    }
  ]
}
```

### 2. 重新加载数据

#### 请求
- **方法**: POST
- **路径**: `/iptv/reload`
- **参数**: 无

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": "数据重新加载成功"
}
```

### 3. 按标签查询频道

#### 请求
- **方法**: POST
- **路径**: `/iptv/query/tags`
- **Content-Type**: `application/json`

#### 请求参数
```json
{
  "page": 0,
  "size": 10,
  "tags": ["电影", "娱乐"]
}
```

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "channels": [
      {
        "id": 1,
        "name": "电影频道",
        "url": "http://example.com/movie",
        "group": "娱乐",
        "category": "电影",
        "logo": "http://example.com/logo.png"
      }
    ],
    "totalCategories": 1,
    "totalChannels": 1
  }
}
```

### 4. 按分组查询频道

#### 请求
- **方法**: POST
- **路径**: `/iptv/query/group`
- **Content-Type**: `application/json`

#### 请求参数
```json
{
  "page": 0,
  "size": 10,
  "tags": ["娱乐"]
}
```

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "groupedItems": [
      {
        "groupName": "娱乐",
        "totalChannels": 5
      }
    ]
  }
}
```

### 5. 添加收藏频道

#### 请求
- **方法**: POST
- **路径**: `/iptv/favorite/add/{channelId}`
- **认证**: 需要登录

#### 路径参数
- `channelId`: 频道ID

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": "添加喜欢频道成功"
}
```

### 6. 删除收藏频道

#### 请求
- **方法**: POST
- **路径**: `/iptv/favorite/remove/{channelId}`
- **认证**: 需要登录

#### 路径参数
- `channelId`: 频道ID

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": "删除喜欢频道成功"
}
```

### 7. 获取收藏频道列表（分页）

#### 请求
- **方法**: POST
- **路径**: `/iptv/favorite/list`
- **Content-Type**: `application/json`
- **认证**: 需要登录

#### 请求参数
```json
{
  "page": 0,
  "size": 10,
  "category": "娱乐",
  "keyword": "电影"
}
```

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "电影频道",
        "url": "http://example.com/movie",
        "group": "娱乐",
        "category": "电影",
        "logo": "http://example.com/logo.png"
      }
    ],
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## 错误响应

### 通用错误格式
```json
{
  "code": 错误码,
  "message": "错误描述",
  "data": null
}
```

### 常见错误码
- **400**: 请求参数错误
- **401**: 用户未登录
- **500**: 服务器内部错误

### 错误响应示例
```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "获取频道失败",
  "data": null
}
```

## 数据结构

### ApiResponse
| 字段名 | 类型 | 描述 |
|-------|------|------|
| code | int | 响应码 |
| message | string | 响应消息 |
| data | object | 响应数据 |

### IptvChannel
| 字段名 | 类型 | 描述 |
|-------|------|------|
| id | Long | 频道ID |
| name | string | 频道名称 |
| url | string | 频道URL |
| group | string | 频道分组 |
| category | string | 频道分类 |
| logo | string | 频道Logo |
| language | string | 语言 |
| country | string | 国家 |
| tags | string | 标签列表 |

### SearchRequest
| 字段名 | 类型 | 默认值 | 描述 |
|-------|------|--------|------|
| page | int | 0 | 页码 |
| size | int | 10 | 每页数量 |
| category | string | - | 分类过滤 |
| keyword | string | - | 关键词过滤 |

### TagRequest
| 字段名 | 类型 | 默认值 | 描述 |
|-------|------|--------|------|
| page | int | 0 | 页码 |
| size | int | 10 | 每页数量 |
| tags | List\<string\> | - | 标签列表 |
