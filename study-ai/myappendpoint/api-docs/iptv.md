# IPTV 控制器 API 文档

## 1. 获取所有频道

### 接口描述
获取系统中所有的 IPTV 频道列表。

### 请求信息
- **请求方法**: GET
- **请求路径**: `/iptv/channels`
- **请求参数**: 无

### 请求示例
```bash
curl -X GET http://localhost:8080/iptv/channels
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
      "tags": ["标签1", "标签2"]
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
  "message": "获取所有频道失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
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
curl -X POST http://localhost:8080/iptv/reload
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

## 3. 按标签获取频道（分页）

### 接口描述
根据指定的标签获取 IPTV 频道列表，并支持分页。

### 请求信息
- **请求方法**: POST
- **请求路径**: `/iptv/query/tags`
- **请求参数**: 
  - 路径参数: 无
  - 查询参数: 
    - `page` (可选，默认: 0): 页码
    - `size` (可选，默认: 10): 每页数量
  - 请求体: 
    ```json
    {
      "tags": ["标签1", "标签2"]
      "page":0,
      "size",10
    }
    ```

### 请求示例
```bash
curl -X POST -H "Content-Type: application/json" -d '{"tags": ["CCTV"],"page": 0,"size": 10}' http://localhost:8080/iptv/query/tags
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
        "tags": ["标签1", "标签2"]
      }
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

## 4. 获取分组频道

### 接口描述
获取所有 IPTV 频道的分组信息，包括每个分组的名称和频道数量。

### 请求信息
- **请求方法**: GET
- **请求路径**: `/iptv/query/group`
- **请求参数**: 无

### 请求示例
```bash
curl -X GET http://localhost:8080/iptv/query/group
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
        "groupName": "央视",
        "totalChannels": 15
      },
      {
        "groupName": "卫视",
        "totalChannels": 30
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

## 5. 添加喜欢的频道

### 接口描述
将指定的 IPTV 频道添加到当前用户的喜欢列表中。

### 请求信息
- **请求方法**: POST
- **请求路径**: `/iptv/favorite/add/{channelId}`
- **请求参数**: 
  - 路径参数: 
    - `channelId`: 频道 ID
  - 请求体: 无

### 请求示例
```bash
curl -X POST http://localhost:8080/iptv/favorite/add/1
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
- **状态码**: 401
  - **描述**: 用户未登录
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

- **状态码**: 500
  - **描述**: 服务器内部错误
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

## 6. 获取用户喜欢的频道

### 接口描述
获取当前用户喜欢的所有 IPTV 频道列表。

### 请求信息
- **请求方法**: GET
- **请求路径**: `/iptv/favorite/list`
- **请求参数**: 无

### 请求示例
```bash
curl -X GET http://localhost:8080/iptv/favorite/list
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
      "tags": ["标签1", "标签2"]
    }
    // 更多喜欢的频道...
  ],
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
- **状态码**: 401
  - **描述**: 用户未登录
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

- **状态码**: 500
  - **描述**: 服务器内部错误
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
