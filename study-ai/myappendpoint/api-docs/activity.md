# ActivityController API 对接文档

## 接口列表

| 接口地址 | 请求方式 | 功能描述 |
|---------|----------|----------|
| `/activities` | GET | 分页获取活动列表 |
| `/activities` | POST | 创建新活动 |
| `/activities/mock` | GET/POST | 生成模拟活动数据 |

---

## 1. 分页获取活动列表

### 基本信息
- **接口地址**: `/activities`
- **请求方式**: `GET`
- **功能描述**: 分页获取活动列表数据，支持自定义页码和每页数量

### 请求参数
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | int | 否 | 0 | 页码（从0开始计数，0表示第一页） |
| size | int | 否 | 5 | 每页显示数量 |

### 请求示例
```bash
# 获取第一页，每页5条数据
curl -X GET http://localhost:8080/activities?page=0&size=5

# 获取第二页，每页10条数据
curl -X GET http://localhost:8080/activities?page=1&size=10

# 获取全部活动（不指定参数）
curl -X GET http://localhost:8080/activities
```

### 响应格式
```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-01-01",
      "description": "新年庆祝活动",
      "creator": "张三"
    },
    {
      "id": 2,
      "time": "2024-01-02",
      "description": "春节联欢晚会",
      "creator": "李四"
    }
  ],
  "totalElements": 25,
  "totalPages": 5,
  "number": 0,
  "size": 5,
  "numberOfElements": 2,
  "first": true,
  "last": false,
  "empty": false
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| content | array | 当前页的活动数据列表 |
| totalElements | number | 总记录数 |
| totalPages | number | 总页数 |
| number | number | 当前页码 |
| size | number | 每页大小 |
| first | boolean | 是否为第一页 |
| last | boolean | 是否为最后一页 |
| empty | boolean | 是否为空列表 |

---

## 2. 创建新活动

### 基本信息
- **接口地址**: `/activities`
- **请求方式**: `POST`
- **功能描述**: 创建新的活动记录，需要传入活动的基本信息

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| time | string | 是 | 活动时间，格式：yyyy-MM-dd |
| description | string | 是 | 活动描述，最大长度建议不超过500字符 |
| creator | string | 是 | 创建人名称 |

### 请求示例
```json
{
  "time": "2024-01-15",
  "description": "新年庆祝活动",
  "creator": "张三"
}
```

### 请求示例（复杂描述）
```json
{
  "time": "2024-02-10",
  "description": "春节联欢晚会，包含歌舞表演、小品相声、魔术杂技等多种节目形式",
  "creator": "行政部-李经理"
}
```

### 响应格式
```json
{
  "id": 26,
  "time": "2024-01-15",
  "description": "新年庆祝活动",
  "creator": "张三"
}
```

### 请求示例（使用curl）
```bash
curl -X POST http://localhost:8080/activities \
  -H "Content-Type: application/json" \
  -d '{
    "time": "2024-01-15",
    "description": "新年庆祝活动",
    "creator": "张三"
  }'
```

---

## 3. 生成模拟活动数据

### 基本信息
- **接口地址**: `/activities/mock`
- **请求方式**: `GET` 或 `POST`
- **功能描述**: 自动生成一条随机的模拟活动数据，用于测试或快速填充数据

### 请求参数
无

### 响应格式
```json
{
  "id": 27,
  "time": "2024-01-08",
  "description": "示例活动1234",
  "creator": "创建人5678"
}
```

### 请求示例
```bash
# 使用GET请求
curl -X GET http://localhost:8080/activities/mock

# 使用POST请求
curl -X POST http://localhost:8080/activities/mock
```

### 模拟数据说明
- **time**: 随机生成当前日期前30天内的日期
- **description**: 格式为"示例活动" + 随机4位数字
- **creator**: 格式为"创建人" + 随机4位数字

---

## 数据模型

### Activity 实体对象
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | long | 活动唯一标识，自动生成 |
| time | string | 活动时间，格式：yyyy-MM-dd |
| description | string | 活动描述 |
| creator | string | 创建人名称 |

### 示例对象
```json
{
  "id": 1,
  "time": "2024-01-15",
  "description": "新年庆祝活动",
  "creator": "张三"
}
```

---

## 错误响应

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request format",
  "path": "/activities"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "服务器内部错误",
  "path": "/activities"
}
```

---

## 使用场景示例

### 场景1：初始化数据
```bash
# 生成多条模拟数据
for i in {1..5}; do
  curl -X POST http://localhost:8080/activities/mock
done
```

### 场景2：分页查询
```bash
# 获取第一页数据
curl -X GET http://localhost:8080/activities?page=0&size=10

# 获取最后一页数据
curl -X GET http://localhost:8080/activities?page=4&size=5
```

### 场景3：创建活动
```bash
# 创建春节活动
curl -X POST http://localhost:8080/activities \
  -H "Content-Type: application/json" \
  -d '{
    "time": "2024-02-10",
    "description": "春节联欢晚会",
    "creator": "活动组织者"
  }'
```

---

## 注意事项
1. 所有日期格式必须为 `yyyy-MM-dd`
2. 分页查询时，page参数从0开始计数
3. 模拟数据接口会立即返回生成的随机数据
4. 系统启动时会自动创建25条模拟活动数据
