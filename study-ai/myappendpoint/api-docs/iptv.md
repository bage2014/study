# IPTV Controller API 文档

## 接口列表

| 接口地址 | 请求方式 | 功能描述 |
|---------|----------|----------|
| `/iptv/channels` | GET | 获取所有IPTV频道 |
| `/iptv/categories` | GET | 按分类获取频道 |
| `/iptv/language/{language}` | GET | 按语言获取频道 |
| `/iptv/search` | GET | 搜索频道 |
| `/iptv/chinese` | GET | 获取中文相关频道 |
| `/iptv/english` | GET | 获取英文相关频道 |
| `/iptv/reload` | POST | 重新加载IPTV数据 |

---

## 统一响应格式

所有接口都返回统一的ApiResponse格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {响应数据},
  "total": null,
  "page": null,
  "size": null
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "错误描述",
  "data": null
}
```

---

## 1. 获取所有IPTV频道

### 基本信息
- **接口地址**: `/iptv/channels`
- **请求方式**: `GET`
- **功能描述**: 获取所有IPTV频道列表

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/channels"
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "name": "CCTV-1",
      "url": "http://example.com/cctv1.m3u8",
      "group": "China",
      "language": "Chinese",
      "country": "CN",
      "logo": "http://example.com/cctv1.png",
      "category": "新闻"
    },
    {
      "name": "BBC News",
      "url": "http://example.com/bbc.m3u8",
      "group": "UK",
      "language": "English",
      "country": "UK",
      "logo": "http://example.com/bbc.png",
      "category": "新闻"
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "获取所有频道失败",
  "data": null
}
```

---

## 2. 按分类获取频道

### 基本信息
- **接口地址**: `/iptv/categories`
- **请求方式**: `GET`
- **功能描述**: 按分类分组获取频道

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/categories"
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "categories": [
      {
        "type": "电影",
        "desc": "电影频道分类",
        "channels": [
          {
            "name": "Movie Channel 1",
            "url": "http://example.com/movie1.m3u8",
            "group": "Movies",
            "language": "English",
            "country": "US",
            "logo": "http://example.com/movie1.png",
            "category": "电影"
          }
        ]
      },
      {
        "type": "音乐",
        "desc": "音乐频道分类",
        "channels": [
          {
            "name": "Music Channel",
            "url": "http://example.com/music.m3u8",
            "group": "Music",
            "language": "English",
            "country": "US",
            "logo": "http://example.com/music.png",
            "category": "音乐"
          }
        ]
      }
    ],
    "totalCategories": 2,
    "totalChannels": 2
  }
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "按分类获取频道失败",
  "data": null
}
```

---

## 3. 按语言获取频道

### 基本信息
- **接口地址**: `/iptv/language/{language}`
- **请求方式**: `GET`
- **功能描述**: 按指定语言获取频道

### 请求参数
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| language | string | 是 | 语言代码 | chinese, english |

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/language/chinese"
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "name": "CCTV-1",
      "url": "http://example.com/cctv1.m3u8",
      "group": "China",
      "language": "Chinese",
      "country": "CN",
      "logo": "http://example.com/cctv1.png",
      "category": "新闻"
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "按语言获取频道失败",
  "data": null
}
```

---

## 4. 搜索频道

### 基本信息
- **接口地址**: `/iptv/search`
- **请求方式**: `GET`
- **功能描述**: 根据关键词搜索频道

### 请求参数
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| keyword | string | 是 | 搜索关键词 | news |

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/search?keyword=news"
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "name": "BBC News",
      "url": "http://example.com/bbc.m3u8",
      "group": "UK",
      "language": "English",
      "country": "UK",
      "logo": "http://example.com/bbc.png",
      "category": "新闻"
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "搜索频道失败",
  "data": null
}
```

---

## 5. 获取中文相关频道

### 基本信息
- **接口地址**: `/iptv/chinese`
- **请求方式**: `GET`
- **功能描述**: 获取中文相关的频道

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/chinese"
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "name": "CCTV-1",
      "url": "http://example.com/cctv1.m3u8",
      "group": "China",
      "language": "Chinese",
      "country": "CN",
      "logo": "http://example.com/cctv1.png",
      "category": "新闻"
    },
    {
      "name": "湖南卫视",
      "url": "http://example.com/hunan.m3u8",
      "group": "China",
      "language": "Chinese",
      "country": "CN",
      "logo": "http://example.com/hunan.png",
      "category": "娱乐"
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "获取中文频道失败",
  "data": null
}
```

---

## 6. 获取英文相关频道

### 基本信息
- **接口地址**: `/iptv/english`
- **请求方式**: `GET`
- **功能描述**: 获取英文相关的频道

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/english"
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "name": "BBC News",
      "url": "http://example.com/bbc.m3u8",
      "group": "UK",
      "language": "English",
      "country": "UK",
      "logo": "http://example.com/bbc.png",
      "category": "新闻"
    },
    {
      "name": "HBO",
      "url": "http://example.com/hbo.m3u8",
      "group": "US",
      "language": "English",
      "country": "US",
      "logo": "http://example.com/hbo.png",
      "category": "电影"
    }
  ]
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "获取英文频道失败",
  "data": null
}
```

---

## 7. 重新加载IPTV数据

### 基本信息
- **接口地址**: `/iptv/reload`
- **请求方式**: `POST`
- **功能描述**: 重新从GitHub加载IPTV数据

### 请求示例
```bash
curl -X POST "http://localhost:8080/iptv/reload"
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": "数据重新加载成功"
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "重新加载数据失败",
  "data": null
}
```

---

## 数据模型

### IptvChannel 实体
```json
{
  "name": "频道名称",
  "url": "播放地址",
  "group": "分组",
  "language": "语言",
  "country": "国家",
  "logo": "图标地址",
  "category": "分类"
}
```

### CategoryChannelsResponse
```json
{
  "categories": [
    {
      "type": "分类类型",
      "desc": "分类描述",
      "channels": [IptvChannel数组]
    }
  ],
  "totalCategories": "总分类数",
  "totalChannels": "总频道数"
}
```

---

## 数据源
数据来源于: https://github.com/iptv-org/iptv

## 使用说明
1. 应用启动时会自动从GitHub加载IPTV数据
2. 支持中英文频道的分类和搜索
3. 数据源为公开的IPTV播放列表
4. 支持按分类、语言、关键词进行筛选
