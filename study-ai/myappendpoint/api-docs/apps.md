# AppUpdateController API对接文档
以下是按照user.md格式生成的AppUpdateController API对接文档，包含所有可用接口的详细信息。

## 1. 上传文件

### 请求信息
- **URL**: `/app/upload`
- **方法**: `POST`
- **描述**: 上传应用更新文件

### 请求参数
| 参数名 | 类型          | 必需 | 默认值 | 描述         |
| ------ | ------------- | ---- | ------ | ------------ |
| file   | MultipartFile | 是   | 无     | 要上传的文件 |

### 响应格式
```json
{
  "code": 200,
  "message": "文件上传成功",
  "data": {
    "fileId": "uuid-123456",
    "originalFileName": "app-update.apk",
    "fileName": "uuid-123456_app-update.apk"
  }
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
# 上传文件
curl -X POST 'http://localhost:8080/app/upload' -F 'file=@/path/to/your/file.apk'
```

## 2. 下载文件

### 请求信息
- **URL**: `/app/download/{fileId}`
- **方法**: `GET`
- **描述**: 根据文件ID下载文件

### 请求参数
| 参数名 | 类型   | 必需 | 默认值 | 描述           |
| ------ | ------ | ---- | ------ | -------------- |
| fileId | string | 是   | 无     | 文件唯一标识符 |

### 响应格式
文件下载（二进制文件）

### 错误响应

|      |      |      |
|--------|------|------|
|      |      |      |
|      |      |      |

## 3. 获取版本列表

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
      "downloadUrl": "",
      "forceUpdate": false
    },
    {
      "id": 4,
      "version": "2.0.0",
      "releaseDate": "2023-12-03",
      "releaseNotes": "全新UI设计",
      "downloadUrl": "",
      "forceUpdate": true
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10
	}
}

```

### 请求示例
```bash
# 获取所有版本（第一页，每页10条）
curl -X GET 'http://localhost:8080/app/versions'

# 分页获取版本列表（第1页，每页5条）
curl -X GET 'http://localhost:8080/app/versions?page=1&size=5&keyword=哈哈哈'

# 搜索包含特定关键词的版本
curl -X GET 'http://localhost:8080/app/versions?keyword=UI'
```
