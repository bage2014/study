# IptvController API文档
以下是IptvController的完整API文档，包含所有可用接口的详细信息。

## 1. 获取所有频道

### 基本信息
- **接口地址**: `/iptv/channels`
- **请求方式**: `GET`
- **功能描述**: 获取所有IPTV频道列表

### 请求参数
无

### 请求示例
```bash
curl -X GET 'http://localhost:8080/iptv/channels'
```

### 响应格式
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "name": "频道名称",
      "url": "http://example.com/stream.m3u8",
      "group": "频道分组",
      "category": "频道分类",
      "language": "语言",
      "country": "国家",
      "logo": "http://example.com/logo.png",
      "tags": ["标签1", "标签2"]
    },
    // 更多频道...
  ],
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "获取所有频道失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

## 2. 重新加载数据

### 基本信息
- **接口地址**: `/iptv/reload`
- **请求方式**: `POST`
- **功能描述**: 重新从远程URL加载IPTV数据

### 请求参数
无

### 请求示例
```bash
curl -X POST 'http://localhost:8080/iptv/reload'
```

### 响应格式
```json
{
  "code": 200,
  "message": "数据重新加载成功",
  "data": "数据重新加载成功",
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "重新加载数据失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

## 3. 按标签获取频道（分页）

### 基本信息
- **接口地址**: `/iptv/query/tags`
- **请求方式**: `POST`
- **功能描述**: 根据标签查询频道，支持分页

### 请求参数
#### 路径参数
无

#### 查询参数
| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| page   | int  | 否   | 0      | 页码（从0开始） |
| size   | int  | 否   | 10     | 每页条数 |

#### 请求体
```json
{
  "tags": ["标签1", "标签2"]
}
```

### 请求示例
```bash
curl -X POST 'http://localhost:8080/iptv/query/tags?page=0&size=10' \
  -H 'Content-Type: application/json' \
  -d '{"tags": ["电影", "中文"]}'
```

### 响应格式
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "channels": [
      {
        "id": 1,
        "name": "频道名称",
        "url": "http://example.com/stream.m3u8",
        "group": "频道分组",
        "category": "频道分类",
        "language": "语言",
        "country": "国家",
        "logo": "http://example.com/logo.png",
        "tags": ["标签1", "标签2"]
      },
      // 更多频道...
    ],
    "totalCategories": 10,
    "totalChannels": 10
  },
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "按标签获取频道失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

## 4. 获取分组频道

### 基本信息
- **接口地址**: `/iptv/query/group`
- **请求方式**: `GET`
- **功能描述**: 获取所有分组的频道列表

### 请求参数
无

### 请求示例
```bash
curl -X GET 'http://localhost:8080/iptv/query/group'
```

### 响应格式
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "groupedChannels": {
      "分组名称1": [
        {
          "id": 1,
          "name": "频道名称1",
          "url": "http://example.com/stream1.m3u8",
          "group": "分组名称1",
          "category": "频道分类",
          "language": "语言",
          "country": "国家",
          "logo": "http://example.com/logo1.png",
          "tags": ["标签1", "标签2"]
        },
        // 更多频道...
      ],
      "分组名称2": [
        // 该分组下的频道...
      ]
    },
    "totalGroups": 5
  },
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "获取分组频道失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

## 5. 添加喜欢的频道

### 基本信息
- **接口地址**: `/iptv/favorite/add/{channelId}`
- **请求方式**: `POST`
- **功能描述**: 将指定频道添加到当前用户的喜欢列表
- **权限要求**: 用户必须登录

### 请求参数
#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| channelId | int | 是 | 频道ID |

#### 查询参数
无

### 请求示例
```bash
curl -X POST 'http://localhost:8080/iptv/favorite/add/1' \
  -H 'Authorization: Bearer your_token_here'
```

### 响应格式
```json
{
  "code": 200,
  "message": "成功",
  "data": "添加喜欢频道成功",
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- 用户未登录
```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

- 服务器错误
```json
{
  "code": 500,
  "message": "添加喜欢频道失败: [具体错误信息]",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

## 6. 获取用户喜欢的频道

### 基本信息
- **接口地址**: `/iptv/favorite/list`
- **请求方式**: `GET`
- **功能描述**: 获取当前登录用户喜欢的所有频道
- **权限要求**: 用户必须登录

### 请求参数
无

### 请求示例
```bash
curl -X GET 'http://localhost:8080/iptv/favorite/list' \
  -H 'Authorization: Bearer your_token_here'
```

### 响应格式
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "name": "频道名称",
      "url": "http://example.com/stream.m3u8",
      "group": "频道分组",
      "category": "频道分类",
      "language": "语言",
      "country": "国家",
      "logo": "http://example.com/logo.png",
      "tags": ["标签1", "标签2"]
    },
    // 更多喜欢的频道...
  ],
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- 用户未登录
```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

- 服务器错误
```json
{
  "code": 500,
  "message": "获取喜欢频道失败: [具体错误信息]",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

## 数据结构说明

### IptvChannel 对象
| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | int | 频道唯一标识 |
| name | string | 频道名称 |
| url | string | 频道流地址 |
| group | string | 频道分组 |
| category | string | 频道分类 |
| language | string | 频道语言 |
| country | string | 频道所属国家 |
| logo | string | 频道Logo URL |
| tags | List\<string\> | 频道标签列表 |

### CategoryChannelsResponse 对象
| 字段名 | 类型 | 描述 |
|--------|------|------|
| channels | List\<IptvChannel\> | 频道列表 |
| totalCategories | int | 分类总数 |
| totalChannels | int | 频道总数 |

### GroupedChannelsResponse 对象
| 字段名 | 类型 | 描述 |
|--------|------|------|
| groupedChannels | Map\<String, List\<IptvChannel\>\> | 按分组名称组织的频道列表 |
| totalGroups | int | 分组总数 |

### ApiResponse 对象
| 字段名 | 类型 | 描述 |
|--------|------|------|
| code | int | 响应状态码（200表示成功） |
| message | string | 响应消息 |
| data | object | 响应数据（根据接口不同而不同） |
| total | long | 总记录数（分页接口使用） |
| page | int | 当前页码（分页接口使用） |
| size | int | 每页记录数（分页接口使用） |

## 通用错误码说明
| 错误码 | 描述 |
|--------|------|
| 200 | 请求成功 |
| 401 | 用户未登录或登录失效 |
| 500 | 服务器内部错误 |
