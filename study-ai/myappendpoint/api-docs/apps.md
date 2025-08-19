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