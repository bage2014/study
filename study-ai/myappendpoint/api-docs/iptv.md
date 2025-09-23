# IPTV Controller API 对接文档

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
[
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
  "电影": [
    {
      "name": "Movie Channel 1",
      "url": "http://example.com/movie1.m3u8",
      "group": "Movies",
      "language": "English",
      "country": "US",
      "logo": "http://example.com/movie1.png",
      "category": "电影"
    }
  ],
  "音乐": [
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
```

---

## 3. 按语言获取频道

### 基本信息
- **接口地址**: `/iptv/language/{language}`
- **请求方式**: `GET`
- **功能描述**: 按指定语言获取频道

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| language | string | 是 | 语言代码（如：chinese, english） |

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/language/chinese"
```

### 响应格式
```json
[
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
```

---

## 4. 搜索频道

### 基本信息
- **接口地址**: `/iptv/search`
- **请求方式**: `GET`
- **功能描述**: 根据关键词搜索频道

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | string | 是 | 搜索关键词 |

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/search?keyword=news"
```

### 响应格式
```json
[
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
```

---

## 5. 获取中文相关频道

### 基本信息
- **接口地址**: `/iptv/chinese`
- **请求方式**: `GET`
- **功能描述**: 获取中文相关的频道（包括中文频道、电影、音乐、新闻、娱乐）

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/chinese"
```

### 响应格式
```json
[
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
```

---

## 6. 获取英文相关频道

### 基本信息
- **接口地址**: `/iptv/english`
- **请求方式**: `GET`
- **功能描述**: 获取英文相关的频道（包括英文频道、电影、音乐、新闻、娱乐）

### 请求示例
```bash
curl -X GET "http://localhost:8080/iptv/english"
```

### 响应格式
```json
[
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
"数据重新加载成功"
```

---

## 数据源

数据来源于: https://github.com/iptv-org/iptv

## 分类说明

| 分类 | 说明 |
|------|------|
| 电影 | 电影相关频道 |
| 音乐 | 音乐相关频道 |
| 体育 | 体育相关频道 |
| 新闻 | 新闻相关频道 |
| 娱乐 | 娱乐相关频道 |
| 教育 | 教育相关频道 |
| 少儿 | 少儿相关频道 |
| 纪录片 | 纪录片频道 |
| 中文频道 | 中文语言频道 |
| 英文频道 | 英文语言频道 |
| 其他 | 其他未分类频道 |

## 使用说明

1. 应用启动时会自动从GitHub加载IPTV数据
2. 支持中英文频道的分类和搜索
3. 数据源为公开的IPTV播放列表
4. 支持按分类、语言、关键词进行筛选

## 错误处理

如果加载数据失败，会返回500错误，建议重试或检查网络连接。
