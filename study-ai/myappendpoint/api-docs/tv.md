# TV Controller 对接文档

## 1. 获取所有 TV 频道

### 请求信息
- **URL**: `/tv`
- **方法**: `GET`
- **描述**: 获取所有 TV 频道数据，支持分页

### 请求参数
| 参数名 | 类型 | 必需 | 默认值 | 描述 |
|--------|------|------|--------|------|
| page   | int  | 否   | 0      | 页码（从0开始） |
| size   | int  | 否   | 10     | 每页数量 |

### 响应格式
```json
{
  "content": [
    {
      "title": "CCTV 1",
      "logo": "https://i.imgur.com/BHUh7hq.jpg",
      "channelUrls": [
        {
          "title": "信号源 1",
          "url": "https://cctvakhwh5ca-cntv.akamaized.net/clive/cctv1_2/index.m3u8"
        }
      ]
    },
    // 更多频道...
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 45,
  "last": false,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

### 请求示例
```bash
# 获取第一页，每页10条数据
curl -X GET 'http://localhost:8080/tv?page=0&size=10'
```

## 2. 搜索 TV 频道

### 请求信息
- **URL**: `/tv/search`
- **方法**: `GET`
- **描述**: 根据关键词搜索 TV 频道，支持分页

### 请求参数
| 参数名 | 类型 | 必需 | 默认值 | 描述 |
|--------|------|------|--------|------|
| keyword | string | 是   | 无     | 搜索关键词（匹配频道标题） |
| page   | int  | 否   | 0      | 页码（从0开始） |
| size   | int  | 否   | 10     | 每页数量 |

### 响应格式
同「获取所有 TV 频道」的响应格式

### 请求示例
```bash
# 搜索标题中包含"cctv"的频道，获取第一页，每页10条数据
curl -X GET 'http://localhost:8080/tv/search?keyword=cctv&page=0&size=10'
```

## 数据结构说明

### TvChannel 对象
| 字段名 | 类型 | 描述 |
|--------|------|------|
| title  | string | 频道标题 |
| logo   | string | 频道 logo URL |
| channelUrls | array | 频道信号源列表 |

### ChannelUrl 对象
| 字段名 | 类型 | 描述 |
|--------|------|------|
| title  | string | 信号源标题 |
| url    | string | 信号源 URL |