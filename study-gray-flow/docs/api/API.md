# Gray Flow API 文档

## 一、基础信息

- **服务端口**: 8083
- **API 前缀**: `/api`
- **内容类型**: `application/json`

## 二、日志管理接口

### 2.1 查询日志列表

**接口**: `GET /api/logs`

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| `logType` | String | 否 | 日志类型：CONTROLLER/PROXY |
| `page` | Integer | 否 | 页码，默认 0 |
| `size` | Integer | 否 | 每页大小，默认 20 |

**请求示例**:
```bash
curl "http://localhost:8083/api/logs?logType=CONTROLLER&page=0&size=10"
```

**响应示例**:
```json
{
  "content": [
    {
      "id": 1,
      "logType": "CONTROLLER",
      "traceId": "abc12345-dead-beef-0000-000000000001",
      "className": "UserController",
      "methodName": "list",
      "durationMs": 12,
      "createdAt": "2026-06-23T10:00:00Z"
    }
  ],
  "totalElements": 42,
  "totalPages": 3,
  "number": 0,
  "size": 10
}
```

### 2.2 查询调用链

**接口**: `GET /api/logs/trace/{traceId}`

**路径参数**:

| 参数 | 类型 | 说明 |
|-----|------|------|
| `traceId` | String | 调用链追踪 ID |

**请求示例**:
```bash
curl "http://localhost:8083/api/logs/trace/abc12345-dead-beef-0000-000000000001"
```

**响应示例**:
```json
[
  {
    "id": 1,
    "logType": "CONTROLLER",
    "traceId": "abc12345-dead-beef-0000-000000000001",
    "className": "UserController",
    "methodName": "list",
    "durationMs": 12,
    "callIndex": 0,
    "args": "[]",
    "resultSummary": "[{\"id\":1}]",
    "errorMsg": null,
    "createdAt": "2026-06-23T10:00:00Z"
  },
  {
    "id": 2,
    "logType": "PROXY",
    "traceId": "abc12345-dead-beef-0000-000000000001",
    "className": "UserServiceImpl",
    "methodName": "findAll",
    "durationMs": 8,
    "callIndex": 1,
    "args": "[]",
    "resultSummary": "[{\"id\":1}]",
    "errorMsg": null,
    "createdAt": "2026-06-23T10:00:01Z"
  }
]
```

## 三、回放管理接口

### 3.1 查询回放会话列表

**接口**: `GET /api/replay/sessions`

**请求示例**:
```bash
curl "http://localhost:8083/api/replay/sessions"
```

**响应示例**:
```json
[
  {
    "id": 1,
    "sessionId": "sess-uuid-0000-0001",
    "status": "COMPLETED",
    "totalCount": 2,
    "completedCount": 2,
    "createdAt": "2026-06-23T10:00:00Z"
  }
]
```

### 3.2 创建回放会话

**接口**: `POST /api/replay/sessions`

**请求体**:
```json
{
  "count": 10
}
```

| 字段 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| `count` | Integer | 是 | 回放最近 N 条请求（1-100） |

**请求示例**:
```bash
curl -X POST "http://localhost:8083/api/replay/sessions" \
  -H "Content-Type: application/json" \
  -d '{"count": 10}'
```

**响应示例**:
```json
{
  "sessionId": "sess-uuid-0000-0002",
  "status": "RUNNING",
  "totalCount": 10,
  "completedCount": 0
}
```

### 3.3 查询回放详情

**接口**: `GET /api/replay/sessions/{sessionId}`

**路径参数**:

| 参数 | 类型 | 说明 |
|-----|------|------|
| `sessionId` | String | 会话 ID |

**请求示例**:
```bash
curl "http://localhost:8083/api/replay/sessions/sess-uuid-0000-0001"
```

**响应示例**:
```json
{
  "id": 1,
  "sessionId": "sess-uuid-0000-0001",
  "status": "COMPLETED",
  "totalCount": 2,
  "completedCount": 2,
  "createdAt": "2026-06-23T10:00:00Z",
  "records": [
    {
      "id": 1,
      "httpMethod": "GET",
      "requestUri": "/api/users",
      "matchResult": "MATCH",
      "chainMatchResult": "CHAIN_MATCH",
      "diffPatch": null,
      "errorMsg": null
    },
    {
      "id": 2,
      "httpMethod": "POST",
      "requestUri": "/api/users",
      "matchResult": "MISMATCH",
      "chainMatchResult": "CHAIN_MISMATCH",
      "diffPatch": "[{\"path\":\"/name\",\"expected\":\"Alice\",\"actual\":\"Bob\"}]",
      "errorMsg": null
    }
  ]
}
```

## 四、数据模型

### 4.1 LogRecord（日志记录）

| 字段 | 类型 | 说明 |
|-----|------|------|
| `id` | Long | 主键 ID |
| `logType` | String | 日志类型：CONTROLLER/PROXY |
| `traceId` | String | 调用链追踪 ID |
| `className` | String | 类名 |
| `methodName` | String | 方法名 |
| `durationMs` | Long | 执行耗时（毫秒） |
| `callIndex` | Integer | 调用顺序索引 |
| `args` | String | 参数 JSON |
| `resultSummary` | String | 结果摘要 |
| `errorMsg` | String | 错误信息 |
| `createdAt` | LocalDateTime | 创建时间 |

### 4.2 ReplaySession（回放会话）

| 字段 | 类型 | 说明 |
|-----|------|------|
| `id` | Long | 主键 ID |
| `sessionId` | String | 会话唯一标识 |
| `status` | String | 状态：RUNNING/COMPLETED/FAILED |
| `totalCount` | Integer | 总请求数 |
| `completedCount` | Integer | 已完成数 |
| `createdAt` | LocalDateTime | 创建时间 |

### 4.3 ReplayRecord（回放记录）

| 字段 | 类型 | 说明 |
|-----|------|------|
| `id` | Long | 主键 ID |
| `sessionId` | String | 所属会话 ID |
| `httpMethod` | String | HTTP 方法 |
| `requestUri` | String | 请求路径 |
| `matchResult` | String | 匹配结果：MATCH/MISMATCH |
| `chainMatchResult` | String | 链路匹配结果 |
| `diffPatch` | String | 差异 JSON |
| `errorMsg` | String | 错误信息 |

## 五、状态码说明

| 状态码 | 说明 |
|-----|------|
| 200 | 成功 |
| 201 | 创建成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |