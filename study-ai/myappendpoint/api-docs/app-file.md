
# AppFileController API文档

## 概述

AppFileController 提供文件管理相关的 RESTful API，支持文件上传、文件分页查询、文件下载、文件信息查询和文件删除功能。所有文件操作遵循"先操作表，再操作文件"的原则，确保数据一致性。

## 基础路径

所有API的基础路径为：`/file`

## 1. 文件上传

### 1.1 功能描述

上传文件到服务器，先将文件信息写入数据库，再保存文件到磁盘。

### 1.2 API路径

`POST /app/file/upload`

### 1.3 请求参数

| 参数名 | 类型 | 是否必填 | 描述 |
|--------|------|----------|------|
| file | MultipartFile | 是 | 要上传的文件 |

### 1.4 响应示例

```json
{
    "code": 200,
    "message": "文件上传成功",
    "data": {
        "id": 123,
        "fileName": "example.jpg",
        "fileSize": 123456,
        "fileUrl": "/app/file/123",
        "createdAt": "2025-07-25T10:00:00Z"
    }
}
```

### 1.3 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| file | MultipartFile | 是 | 要上传的文件 |

### 1.4 请求样例

```bash
curl -X POST \
  http://localhost:8080/file/upload \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/your/file.txt"
```

### 1.5 响应格式

#### 成功响应

```json
{
  "code": 200,
  "message": "文件上传成功",
  "data": {
    "fileId": 1,
    "fileName": "uuid_filename.txt",
    "originalFileName": "filename.txt",
    "fileSize": 1024,
    "fileType": "text/plain",
    "fileUrl": "/file/download/uuid_filename.txt",
    "createdTime": "2024-01-01T10:00:00"
  }
}
```

#### 错误响应

```json
{
  "code": 400,
  "message": "上传文件不能为空",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "文件上传失败: IO异常详情",
  "data": null
}
```

## 2. 文件分页查询

### 2.1 功能描述

分页查询文件列表，支持关键词搜索。

### 2.2 API路径

### 2.3 请求参数

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- |
| page | Integer | 否 | 0 | 页码，从0开始 |
| size | Integer | 否 | 10 | 每页大小，最大100 |
| keyword | String | 否 | 无 | 搜索关键词，用于匹配文件名或原始文件名 |

### 2.4 请求样例

```bash
# 获取第一页，每页10条记录
curl -X GET \
  "http://localhost:8080/file/list?page=0&size=10"

# 搜索文件名包含"test"的文件
curl -X GET \
  "http://localhost:8080/file/list?keyword=test&page=0&size=10"
```

### 2.5 响应格式

#### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalElements": 25,
    "totalPages": 3,
    "currentPage": 0,
    "pageSize": 10,
    "files": [
      {
        "id": 1,
        "fileName": "uuid_filename1.txt",
        "fileUrl": "/file/download/uuid_filename1.txt",
        "fileSize": 1024,
        "originalFileName": "filename1.txt",
        "fileType": "text/plain",
        "createdTime": "2024-01-01T10:00:00",
        "updatedTime": "2024-01-01T10:00:00"
      },
      // 更多文件对象...
    ]
  }
}
```

#### 错误响应

```json
{
  "code": 500,
  "message": "系统错误: 错误详情",
  "data": null
}
```

## 3. 文件下载

### 3.1 功能描述

根据文件名下载文件。

### 3.2 API路径

### 3.3 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| fileName | String | 是 | 服务器上存储的文件名（包含UUID前缀） |

### 3.4 请求样例

```bash
curl -X GET \
  "http://localhost:8080/file/download/uuid_filename.txt" \
  -o "downloaded_file.txt"
```

### 3.5 响应格式

#### 成功响应

- HTTP状态码: 200
- 响应头:
  - `Content-Type: application/octet-stream`
  - `Content-Disposition: attachment; filename="原始文件名.txt"`
  - `Content-Length: 文件大小`
- 响应体: 文件二进制数据

#### 错误响应

```json
{
  "code": 500,
  "message": "文件下载失败",
  "data": null
}
```

## 4. 文件信息查询

### 4.1 功能描述

根据文件ID查询文件详细信息。

### 4.2 API路径

### 4.3 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| fileId | Long | 是 | 文件ID |

### 4.4 请求样例

```bash
curl -X GET \
  "http://localhost:8080/file/info/123"
```

### 4.5 响应格式

#### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 123,
    "fileName": "uuid_filename.txt",
    "originalFileName": "filename.txt",
    "fileSize": 1024,
    "fileType": "text/plain",
    "fileUrl": "/file/download/uuid_filename.txt",
    "createdTime": "2024-01-01T10:00:00",
    "updatedTime": "2024-01-01T10:00:00"
  }
}
```

#### 错误响应

```json
{
  "code": 404,
  "message": "文件不存在",
  "data": null
}
```

### 4.3 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| fileId | Long | 是 | 文件ID |

### 4.4 请求样例

```bash
curl -X GET \
  "http://localhost:8080/file/info/1"
```

### 4.5 响应格式

#### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "fileName": "uuid_filename.txt",
    "fileUrl": "/file/download/uuid_filename.txt",
    "fileSize": 1024,
    "originalFileName": "filename.txt",
    "fileType": "text/plain",
    "createdTime": "2024-01-01T10:00:00",
    "updatedTime": "2024-01-01T10:00:00"
  }
}
```

#### 错误响应

```json
{
  "code": 500,
  "message": "获取文件信息失败: 文件不存在，ID: 1",
  "data": null
}
```

## 5. 文件删除

### 5.1 功能描述

删除指定ID的文件，同时删除数据库记录和磁盘文件。

### 5.2 API路径

### 5.3 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| fileId | Long | 是 | 文件ID |

### 5.4 请求样例

```bash
curl -X DELETE \
  "http://localhost:8080/file/delete/1"
```

### 5.5 响应格式

#### 成功响应

```json
{
  "code": 200,
  "message": "文件删除成功",
  "data": null
}
```

#### 错误响应

```json
{
  "code": 500,
  "message": "文件删除失败: 文件不存在，ID: 1",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "文件删除失败: IO异常详情",
  "data": null
}
```

## 6. 通用响应格式

所有API返回统一的响应格式：

```json
{
  "code": 响应代码,
  "message": "响应消息",
  "data": 响应数据对象或null
}
```

### 6.1 响应代码说明

| 代码 | 描述 |
| :--- | :--- |
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 500 | 服务器内部错误 |

## 7. 注意事项

1. 文件上传大小限制为100MB
2. 分页查询时，每页最大记录数为100
3. 文件名会自动添加UUID前缀以避免冲突
4. 所有操作均记录详细日志
5. 文件下载时，会自动使用原始文件名作为下载文件名

## 8. 错误处理

所有API都有完善的错误处理机制，对于各类异常情况都会返回相应的错误响应，包含错误代码和错误消息。客户端可以根据错误代码和消息进行相应的处理。
EOL
