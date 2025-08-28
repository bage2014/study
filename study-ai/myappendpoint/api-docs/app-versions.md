# API对接文档

## 1. 上传文件并保存版本信息

### 请求信息
- **URL**: `/app/upload`
- **方法**: `POST`
- **描述**: 上传应用更新文件并保存对应的版本信息

### 请求参数
| 参数名       | 类型          | 必需 | 默认值 | 描述                     |
| ------------ | ------------- | ---- | ------ | ------------------------ |
| file         | MultipartFile | 是   | 无     | 要上传的应用更新文件     |
| version      | String        | 是   | 无     | 应用版本号               |
| forceUpdate  | boolean       | 否   | false  | 是否强制更新             |
| releaseNotes | String        | 否   | 空字符串 | 更新说明                 |

### 响应格式
```json
{
  "code": 200,
  "message": "文件上传和版本保存成功",
  "data": {
    "fileId": "uuid-123456",
    "originalFileName": "app-update.apk",
    "fileName": "uuid-123456_app-update.apk",
    "versionId": 1,
    "version": "2.0.0",
    "downloadUrl": "/app/files/uuid-123456_app-update.apk"
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
  "message": "文件上传失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

### 请求示例
```bash
# 上传文件并保存版本信息
curl -X POST 'http://localhost:8080/app/upload' \
  -F 'file=@/path/to/your/app-update.apk' \
  -F 'version=2.0.0' \
  -F 'forceUpdate=true' \
  -F 'releaseNotes=全新UI设计，性能优化'


# 上传文件并保存版本信息
curl -X POST 'http://localhost:8080/app/upload' \
  -F 'file=@/Users/bage/bage/github/study/study-ai/myappflutter/build/app/outputs/flutter-apk/app-release.apk' \
  -F 'version=2.0.0' \
  -F 'forceUpdate=true' \
  -F 'releaseNotes=全新UI设计，性能优化'  
```

## 2. 下载文件

### 请求信息
- **URL**: `/app/download/{fileId}`
- **方法**: `GET`
- **描述**: 根据文件ID下载应用更新文件

### 请求参数
| 参数名 | 类型   | 必需 | 默认值 | 描述           |
| ------ | ------ | ---- | ------ | -------------- |
| fileId | string | 是   | 无     | 文件唯一标识符 |

### 响应格式
文件下载（二进制文件）

### 错误响应
```json
{
  "code": 500,
  "message": "文件下载失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

### 请求示例
```bash
# 下载文件
curl -X GET 'http://localhost:8080/app/download/uuid-123456' --output downloaded-app.apk
```

## 3. 检查版本更新

### 请求信息
- **URL**: `/app/check`
- **方法**: `GET`
- **描述**: 检查应用是否需要更新

### 请求参数
| 参数名        | 类型   | 必需 | 默认值 | 描述         |
| ------------- | ------ | ---- | ------ | ------------ |
| currentVersion | string | 是   | 无     | 当前应用版本 |

### 响应格式（已是最新版本）
```json
{
  "needUpdate": false,
  "message": "当前已是最新版本",
  "version": null
}
```

### 响应格式（有新版本可用）
```json
{
  "needUpdate": true,
  "message": "new version available",
  "version": {
    "id": 5,
    "version": "2.1.0",
    "releaseDate": "2023-12-05",
    "releaseNotes": "性能优化",
    "downloadUrl": "/app/files/uuid-123456_app-update.apk",
    "forceUpdate": false
  }
}
```

### 请求示例
```bash
# 检查版本更新
curl -X GET 'http://localhost:8080/app/check?currentVersion=1.0.0'
```

## 4. 访问文件

### 请求信息
- **URL**: `/app/files/{fileName}`
- **方法**: `GET`
- **描述**: 通过文件名直接访问文件

### 请求参数
| 参数名  | 类型   | 必需 | 默认值 | 描述   |
| ------- | ------ | ---- | ------ | ------ |
| fileName | string | 是   | 无     | 文件名 |

### 响应格式
文件访问（二进制文件）

### 错误响应
```json
{
  "code": 500,
  "message": "文件访问失败",
  "data": null,
  "total": null,
  "page": null,
  "size": null
}
```

### 请求示例
```bash
# 访问文件
curl -X GET 'http://localhost:8080/app/files/uuid-123456_app-update.apk'
```

## 5. 获取版本列表

### 请求信息
- **URL**: `/app/versions`
- **方法**: `GET`
- **描述**: 获取应用版本列表，支持分页和关键词搜索

### 请求参数
| 参数名  | 类型   | 必需 | 默认值 | 描述                               |
| ------- | ------ | ---- | ------ | ---------------------------------- |
| page    | int    | 否   | 0      | 页码（从0开始）                    |
| size    | int    | 否   | 10     | 每页数量                           |
| keyword | string | 否   | 无     | 搜索关键词（匹配版本号或更新说明） |

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "versions": [
      {
        "id": 5,
        "version": "2.1.0",
        "releaseDate": "2023-12-05",
        "releaseNotes": "性能优化",
        "downloadUrl": "/app/files/uuid-123456_app-update.apk",
        "forceUpdate": false
      },
      {
        "id": 4,
        "version": "2.0.0",
        "releaseDate": "2023-12-03",
        "releaseNotes": "全新UI设计",
        "downloadUrl": "/app/files/uuid-654321_app-update.apk",
        "forceUpdate": true
      }
    ],
    "totalElements": 5,
    "totalPages": 1,
    "currentPage": 0,
    "pageSize": 10
  },
  "total": null,
  "page": null,
  "size": null
}
```

### 请求示例
```bash
# 获取所有版本（第一页，每页10条）
curl -X GET 'http://localhost:8080/app/versions'

# 分页获取版本列表（第1页，每页5条）
curl -X GET 'http://localhost:8080/app/versions?page=1&size=5'

# 搜索包含特定关键词的版本
curl -X GET 'http://localhost:8080/app/versions?keyword=UI'
```

## 数据结构说明

### ApiResponse
```json
{
  "code": 200,              // 响应状态码
  "message": "success",     // 响应消息
  "data": object,           // 响应数据
  "total": null,            // 总数（用于分页）
  "page": null,             // 当前页码（用于分页）
  "size": null              // 每页数量（用于分页）
}
```

### AppVersion
```json
{
  "id": 1,                  // 版本ID
  "version": "2.0.0",      // 版本号
  "releaseDate": "2023-12-03", // 发布日期
  "releaseNotes": "全新UI设计", // 更新说明
  "downloadUrl": "/app/files/uuid-123456_app-update.apk", // 下载链接
  "forceUpdate": true       // 是否强制更新
}
```

### AppVersionResponse
```json
{
  "needUpdate": true,       // 是否需要更新
  "message": "new version available", // 消息
  "version": {}             // AppVersion对象（有更新时返回）
}
```