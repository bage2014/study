# MapTrajectoryController API 对接文档

## 接口列表

| 接口地址 | 请求方式 | 功能描述 |
|---------|----------|----------|
| `/trajectorys/query` | GET | 按时间区间分页查询轨迹数据 |
| `/trajectorys/save` | POST | 保存轨迹数据 |

---

## 1. 按时间区间分页查询轨迹数据

### 基本信息
- **接口地址**: `/trajectorys/query`
- **请求方式**: `GET`
- **功能描述**: 分页查询轨迹数据，支持按时间区间筛选

### 请求参数
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | int | 否 | 0 | 页码（从0开始计数，0表示第一页） |
| size | int | 否 | 5 | 每页显示数量 |
| startTime | string | 否 | 无 | 开始时间，ISO-8601格式：yyyy-MM-ddTHH:mm:ss |
| endTime | string | 否 | 无 | 结束时间，ISO-8601格式：yyyy-MM-ddTHH:mm:ss |

### 请求示例
```bash
# 获取第一页，每页5条数据
curl -X GET "http://localhost:8080/trajectorys/query?page=0&size=5"

# 获取2024年1月1日到2024年1月31日的数据
curl -X GET "http://localhost:8080/trajectorys/query?startTime=2024-01-01T00:00:00&endTime=2024-01-31T23:59:59"

# 获取2024年1月1日之后的数据，第二页，每页10条
curl -X GET "http://localhost:8080/trajectorys/query?startTime=2024-01-01T00:00:00&page=1&size=10"
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "trajectories": [
      {
        "id": 1,
        "latitude": 31.215,
        "longitude": 121.425,
        "time": "2024-01-15T10:30:00",
        "address": "长宁区仙霞路"
      },
      {
        "id": 2,
        "latitude": 31.218,
        "longitude": 121.432,
        "time": "2024-01-15T11:15:00",
        "address": "长宁区延安西路"
      }
    ],
    "totalElements": 25,
    "totalPages": 5,
    "currentPage": 0,
    "pageSize": 5
  }
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码，200表示成功 |
| message | string | 响应消息 |
| data | object | 响应数据 |
| data.trajectories | array | 轨迹数据列表 |
| data.totalElements | number | 总记录数 |
| data.totalPages | number | 总页数 |
| data.currentPage | number | 当前页码 |
| data.pageSize | number | 每页大小 |

---

## 2. 保存轨迹数据

### 基本信息
- **接口地址**: `/trajectorys/save`
- **请求方式**: `POST`
- **功能描述**: 保存新的轨迹数据记录

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| latitude | double | 是 | 纬度坐标 |
| longitude | double | 是 | 经度坐标 |
| time | string | 否 | 时间，ISO-8601格式：yyyy-MM-ddTHH:mm:ss（不传则使用当前时间） |
| address | string | 否 | 地址描述 |

### 请求体示例
```json
{
  "latitude": 31.215,
  "longitude": 121.425,
  "time": "2024-01-15T10:30:00",
  "address": "长宁区仙霞路"
}
```

### 请求示例（使用curl）
```bash
curl -X POST http://localhost:8080/trajectorys/save \
  -H "Content-Type: application/json" \
  -d '{
    "latitude": 31.215,
    "longitude": 121.425,
    "time": "2024-01-15T10:30:00",
    "address": "长宁区仙霞路"
  }'
```

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 26,
    "latitude": 31.215,
    "longitude": 121.425,
    "time": "2024-01-15T10:30:00",
    "address": "长宁区仙霞路"
  }
}
```

---

## 统一响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "时间格式不正确，请使用 ISO-8601 格式: yyyy-MM-ddTHH:mm:ss",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "保存轨迹失败: 具体错误信息",
  "data": null
}
```

### 常见错误码
| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误（如时间格式不正确） |
| 500 | 服务器内部错误 |

---

## 数据模型

### Trajectory 轨迹实体
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 轨迹唯一标识，自动生成 |
| latitude | double | 纬度坐标 |
| longitude | double | 经度坐标 |
| time | LocalDateTime | 时间，ISO-8601格式 |
| address | string | 地址描述 |

### TrajectoryListResponse 轨迹列表响应
| 字段名 | 类型 | 说明 |
|--------|------|------|
| trajectories | List<Trajectory> | 轨迹数据列表 |
| totalElements | long | 总记录数 |
| totalPages | int | 总页数 |
| currentPage | int | 当前页码 |
| pageSize | int | 每页大小 |

### 示例对象
```json
{
  "id": 1,
  "latitude": 31.215,
  "longitude": 121.425,
  "time": "2024-01-15T10:30:00",
  "address": "长宁区仙霞路"
}
```

---

## 使用说明

1. **时间格式**: 所有时间参数必须使用 ISO-8601 格式：`yyyy-MM-ddTHH:mm:ss`
2. **分页参数**: page从0开始计数，size表示每页记录数
3. **错误处理**: 建议客户端处理400和500错误码，并显示相应的错误信息

## 模拟数据
系统启动时会自动生成10条模拟轨迹数据，可用于测试。