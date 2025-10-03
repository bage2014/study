# IPTV 控制器 API 文档

## 1. 获取所有频道（支持搜索和分页）

### 接口描述
获取系统中所有的 IPTV 频道列表，支持关键词搜索和分页查询。

### 请求信息
- **请求方法**: GET/POST
- **请求路径**: `/iptv/channels`
- **请求参数**: 
  - keyword: 搜索关键词（可选）
  - page: 页码，默认为0
  - size: 每页数量，默认为10

### 请求体示例
```json
{
  "keyword": "体育",
  "page": 0,
  "size": 10
}
```

### 响应格式
- **状态码**: 200
- **响应数据**: 
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "频道名称",
      "url": "频道播放地址",
      "group": "频道分组",
      "category": "频道分类",
      "language": "语言",
      "country": "国家",
      "logo": "Logo URL",
      "tags": "标签1,标签2"
    }
    // 更多频道...
  ],
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- **状态码**: 500
- **响应数据**: 
```json
{
  "code": 500,
  "message": "获取频道失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

### 请求示例
```bash
# 不带搜索条件的请求
curl -X GET 'http://localhost:8080/iptv/channels' \
  -H 'Content-Type: application/json'

# 带搜索条件的请求
curl -X POST 'http://localhost:8080/iptv/channels' \
  -H 'Content-Type: application/json' \
  -d '{"keyword": "体育", "page": 0, "size": 10}'
```

## 2. 重新加载数据

### 接口描述
重新从数据源加载 IPTV 频道数据。

### 请求信息
- **请求方法**: POST
- **请求路径**: `/iptv/reload`
- **请求参数**: 无

### 请求示例
```bash
curl -X POST 'http://localhost:8080/iptv/reload' \
  -H 'Content-Type: application/json'
```

### 响应格式
- **状态码**: 200
- **响应数据**: 
```json
{
  "code": 200,
  "message": "success",
  "data": "数据重新加载成功",
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- **状态码**: 500
- **响应数据**: 
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

## 3. 按标签获取频道

### 接口描述
根据指定的标签获取频道列表，支持分页查询。

### 请求信息
- **请求方法**: POST
- **请求路径**: `/iptv/query/tags`
- **请求参数**: 
  - tags: 标签列表
  - page: 页码，默认为0
  - size: 每页数量，默认为10

### 请求体示例
```json
{
  "tags": ["体育", "新闻"],
  "page": 0,
  "size": 10
}
```

### 响应格式
- **状态码**: 200
- **响应数据**: 
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "channels": [
      {
        "id": 1,
        "name": "频道名称",
        "url": "频道播放地址",
        "group": "频道分组",
        "category": "频道分类",
        "language": "语言",
        "country": "国家",
        "logo": "Logo URL",
        "tags": "标签1,标签2"
      }
      // 更多频道...
    ],
    "totalCategories": 10,
    "totalChannels": 20
  },
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- **状态码**: 500
- **响应数据**: 
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

### 请求示例
```bash
curl -X POST 'http://localhost:8080/iptv/query/tags' \
  -H 'Content-Type: application/json' \
  -d '{"tags": ["体育"], "page": 0, "size": 10}'
```

## 4. 获取分组频道

### 接口描述
获取按分组统计的频道信息。

### 请求信息
- **请求方法**: POST
- **请求路径**: `/iptv/query/group`
- **请求参数**: 
  - tags: 标签列表（当前版本只使用第一个标签作为关键词过滤）

### 请求体示例
```json
{
  "tags": ["体育"]
}
```

### 响应格式
- **状态码**: 200
- **响应数据**: 
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "groups": [
      {
        "groupName": "体育",
        "totalChannels": 15
      },
      {
        "groupName": "足球",
        "totalChannels": 8
      }
      // 更多分组...
    ]
  },
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- **状态码**: 500
- **响应数据**: 
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

### 请求示例
```bash
curl -X POST 'http://localhost:8080/iptv/query/group' \
  -H 'Content-Type: application/json' \
  -d '{"tags": ["体育"]}'
```

## 5. 添加喜欢的频道

### 接口描述
将指定的频道添加到当前用户的喜欢列表中。

### 请求信息
- **请求方法**: POST
- **请求路径**: `/iptv/favorite/add/{channelId}`
- **请求参数**: 
  - channelId: 频道ID（路径参数）

### 注意事项
- 需要用户登录（通过token验证）

### 请求示例
```bash
curl -X POST 'http://localhost:8080/iptv/favorite/add/1' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer {token}'
```

### 响应格式
- **状态码**: 200
- **响应数据**: 
```json
{
  "code": 200,
  "message": "success",
  "data": "添加喜欢频道成功",
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- **状态码**: 401（用户未登录）
- **响应数据**: 
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

- **状态码**: 500（添加失败）
- **响应数据**: 
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

## 6. 获取当前用户喜欢的所有频道

### 接口描述
获取当前登录用户添加到喜欢列表的所有频道。

### 请求信息
- **请求方法**: GET
- **请求路径**: `/iptv/favorite/list`

### 注意事项
- 需要用户登录（通过token验证）

### 请求示例
```bash
curl -X GET 'http://localhost:8080/iptv/favorite/list' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer {token}'
```

### 响应格式
- **状态码**: 200
- **响应数据**: 
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "频道名称",
      "url": "频道播放地址",
      "group": "频道分组",
      "category": "频道分类",
      "language": "语言",
      "country": "国家",
      "logo": "Logo URL",
      "tags": "标签1,标签2"
    }
    // 更多喜欢的频道...
  ],
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- **状态码**: 401（用户未登录）
- **响应数据**: 
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

- **状态码**: 500（获取失败）
- **响应数据**: 
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

## 通用响应说明

所有接口的响应格式均遵循 ApiResponse 结构：

```json
{
  "code": 状态码, // 200 表示成功，其他值表示失败
  "message": 消息, // 操作结果描述
  "data": 数据, // 具体返回的数据内容
  "total": 总数, // 可选，分页查询时的总记录数
  "page": 页码, // 可选，当前页码
  "size": 每页数量 // 可选，每页记录数
}
```

## 请求对象说明

### SearchRequest
```json
{
  "keyword": "搜索关键词",
  "page": 页码,
  "size": 每页数量
}
```

### TagRequest
```json
{
  "tags": ["标签1", "标签2"],
  "page": 页码,
  "size": 每页数量
}
```

## 响应对象说明

### IptvChannel
```json
{
  "id": 1,
  "name": "频道名称",
  "url": "频道播放地址",
  "group": "频道分组",
  "category": "频道分类",
  "language": "语言",
  "country": "国家",
  "logo": "Logo URL",
  "tags": "标签1,标签2"
}
```

### CategoryChannelsResponse
```json
{
  "channels": ["IptvChannel对象列表"],
  "totalCategories": 总分类数,
  "totalChannels": 总频道数
}
```

### GroupedChannelsResponse
```json
{
  "groups": [
    {
      "groupName": "分组名称",
      "totalChannels": 该分组下的频道数量
    }
  ]
}
```
