# IPTV API 文档

## 获取所有频道

### 请求
```http
GET /iptv/channels
```

### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "Channel 1",
      "url": "http://example.com/channel1.m3u8",
      "category": "电影",
      "language": "中文",
      "tags": ["热门", "推荐"]
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "获取所有频道失败"
}
```

## 按分类获取频道

### 请求
```http
GET /iptv/categories
```

### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "categories": [
      {
        "type": "电影",
        "desc": "电影",
        "channels": [
          {
            "id": 1,
            "name": "Channel 1"
          }
        ]
      }
    ],
    "totalCategories": 1,
    "totalChannels": 1
  }
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "按分类获取频道失败"
}
```

## 按标签搜索频道

### 请求
```http
GET /iptv/tag/{tag}
```

### 参数
| 参数 | 类型 | 描述 |
|------|------|------|
| tag | string | 要搜索的标签 |

### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "Channel 1",
      "tags": ["热门"]
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "按标签获取频道失败"
}
```

2. 使用以下命令将文档写入iptv.md文件：
```bash
echo '# IPTV API 文档

## 获取所有频道

### 请求
​```http
GET /iptv/channels
```

### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "Channel 1",
      "url": "http://example.com/channel1.m3u8",
      "category": "电影",
      "language": "中文",
      "tags": ["热门", "推荐"]
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "获取所有频道失败"
}
```

## 按分类获取频道

### 请求
```http
GET /iptv/categories
```

### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "channels":{
      "id": 1,
      "name": "Channel 1",
      "url": "http://example.com/channel1.m3u8",
      "category": "电影",
      "language": "中文",
      "tags": ["热门", "推荐"]
    },
    "totalCategories": 1,
    "totalChannels": 1
  }
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "按分类获取频道失败"
}
```

## 按标签搜索频道

### 请求
```http
GET /iptv/tag/{tag}
```

### 参数
| 参数 | 类型 | 描述 |
|------|------|------|
| tag | string | 要搜索的标签 |

### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "Channel 1",
      "tags": ["热门"]
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "按标签获取频道失败"
}

```