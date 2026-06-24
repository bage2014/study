# Gray Flow 设计文档

## 一、架构设计

### 1.1 架构风格

采用经典的三层架构模式：

- **Controller 层**：处理 HTTP 请求，参数校验，调用 Service
- **Service 层**：业务逻辑处理，事务管理
- **Repository 层**：数据访问，基于 Spring Data JPA

### 1.2 模块划分

```
┌─────────────────────────────────────────────────────────────┐
│                      Controller 层                          │
│  LogController        ReplayController                      │
│       │                     │                              │
└───────┼─────────────────────┼──────────────────────────────┘
        ▼                     ▼
┌─────────────────────────────────────────────────────────────┐
│                      Service 层                            │
│  LogService           ReplayService                        │
│       │                     │                              │
└───────┼─────────────────────┼──────────────────────────────┘
        ▼                     ▼
┌─────────────────────────────────────────────────────────────┐
│                    Repository 层                           │
│  LogRecordRepository  ReplaySessionRepository              │
│       │                     │                              │
└───────┼─────────────────────┼──────────────────────────────┘
        ▼                     ▼
┌─────────────────────────────────────────────────────────────┐
│                        数据库 (H2)                         │
│  log_record      replay_session      replay_record         │
└─────────────────────────────────────────────────────────────┘
```

### 1.3 核心流程图

#### 日志记录流程

```
请求进入 → @GrayLog 切面 → 记录方法调用 → 存储到数据库
                              │
                              ▼
                      生成 TraceId
                              │
                              ▼
                      传递给下游服务
```

#### 请求回放流程

```
创建回放会话 → 查询最近 N 条日志 → 逐条转发请求 → 对比响应差异 → 存储结果
                                                      │
                                                      ▼
                                               生成 Diff 报告
```

## 二、关键设计

### 2.1 调用链追踪设计

**设计目标**：实现全链路追踪，支持通过 TraceId 查询完整调用链

**实现方案**：

1. **TraceId 生成**：使用 UUID 生成唯一追踪 ID
2. **ThreadLocal 存储**：通过 ThreadLocal 在同一线程内传递 TraceId
3. **AOP 切面**：通过 `@GrayLog` 注解自动记录日志
4. **日志串联**：所有日志记录同一 TraceId，支持链式查询

### 2.2 请求回放设计

**设计目标**：支持将历史请求重新发送到灰度服务进行验证

**实现方案**：

1. **请求快照**：记录请求的完整信息（方法、路径、参数、Body）
2. **异步转发**：使用 HttpClient 异步转发请求到灰度目标
3. **响应对比**：对比原始响应和灰度响应的差异
4. **Diff 算法**：使用 JSON Diff 算法生成结构化差异报告

### 2.3 数据模型设计

#### log_record 表

| 字段名 | 类型 | 约束 | 说明 |
|-------|------|-----|------|
| id | BIGINT | PRIMARY KEY | 主键 |
| log_type | VARCHAR(20) | NOT NULL | 日志类型 |
| trace_id | VARCHAR(64) | NOT NULL | 追踪 ID |
| class_name | VARCHAR(255) | NOT NULL | 类名 |
| method_name | VARCHAR(128) | NOT NULL | 方法名 |
| duration_ms | BIGINT | - | 耗时 |
| call_index | INT | - | 调用顺序 |
| args | TEXT | - | 参数 JSON |
| result_summary | TEXT | - | 结果摘要 |
| error_msg | TEXT | - | 错误信息 |
| created_at | TIMESTAMP | NOT NULL | 创建时间 |

#### replay_session 表

| 字段名 | 类型 | 约束 | 说明 |
|-------|------|-----|------|
| id | BIGINT | PRIMARY KEY | 主键 |
| session_id | VARCHAR(64) | NOT NULL UNIQUE | 会话 ID |
| status | VARCHAR(20) | NOT NULL | 状态 |
| total_count | INT | NOT NULL | 总请求数 |
| completed_count | INT | NOT NULL | 已完成数 |
| created_at | TIMESTAMP | NOT NULL | 创建时间 |

#### replay_record 表

| 字段名 | 类型 | 约束 | 说明 |
|-------|------|-----|------|
| id | BIGINT | PRIMARY KEY | 主键 |
| session_id | VARCHAR(64) | NOT NULL | 会话 ID |
| http_method | VARCHAR(10) | NOT NULL | HTTP 方法 |
| request_uri | VARCHAR(512) | NOT NULL | 请求路径 |
| match_result | VARCHAR(20) | - | 匹配结果 |
| chain_match_result | VARCHAR(20) | - | 链路匹配结果 |
| diff_patch | TEXT | - | 差异 JSON |
| error_msg | TEXT | - | 错误信息 |

## 三、方案比对

### 3.1 数据库选择

| 方案 | 优点 | 缺点 | 选择 |
|-----|------|------|-----|
| H2（内存） | 启动快，无需安装 | 数据不持久化 | 开发/测试 |
| MySQL | 稳定可靠，支持复杂查询 | 需要独立部署 | 生产环境 |
| SQLite | 轻量级，文件存储 | 并发性能有限 | 嵌入式场景 |

**当前选择**：H2 内存数据库，便于开发测试

### 3.2 日志存储方案

| 方案 | 优点 | 缺点 | 选择 |
|-----|------|------|-----|
| 数据库存储 | 支持复杂查询，事务性好 | 写入性能受限 | 当前方案 |
| ELK 栈 | 搜索能力强，支持大数据量 | 部署复杂 | 大规模场景 |
| ClickHouse | 写入性能极高 | 查询灵活性有限 | 海量日志场景 |

**当前选择**：数据库存储，适合中小规模日志量

### 3.3 请求转发方案

| 方案 | 优点 | 缺点 | 选择 |
|-----|------|------|-----|
| Java HttpClient | 内置支持，无需额外依赖 | 功能相对简单 | 当前方案 |
| OkHttp | 性能优秀，功能丰富 | 需要额外依赖 | 高并发场景 |
| RestTemplate | Spring 集成度高 | 已标记为 deprecated | 不推荐 |

**当前选择**：Java 11+ HttpClient，轻量级且功能足够

## 四、安全性设计

### 4.1 输入校验

- 使用 `@Valid` 注解进行参数校验
- 限制请求大小，防止 DoS 攻击
- 过滤敏感信息，避免日志泄露

### 4.2 资源保护

- H2 控制台仅在开发环境启用
- API 接口应添加认证授权（可扩展）
- 限制并发回放数量，防止资源耗尽

### 4.3 异常处理

- 全局异常处理器统一处理异常
- 详细日志记录便于问题排查
- 友好的错误信息返回

## 五、性能考虑

### 5.1 批量操作

- 批量插入日志记录，减少数据库交互
- 异步处理回放任务，避免阻塞主线程

### 5.2 索引优化

- `trace_id` 字段建立索引，加速调用链查询
- `created_at` 字段建立索引，加速时间范围查询

### 5.3 缓存策略

- 近期日志可缓存在内存中
- 频繁查询的会话信息可缓存

## 六、扩展性设计

### 6.1 模块化设计

- 核心逻辑独立为 Service 层
- Repository 层可替换不同数据源
- 配置类集中管理，便于修改

### 6.2 接口抽象

- 定义 `LogService`、`ReplayService` 接口
- 实现类可灵活替换
- 支持多种实现策略

### 6.3 配置化

- 关键参数通过配置文件管理
- 支持运行时动态调整
- 配置项有合理默认值