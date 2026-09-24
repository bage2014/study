# RCA Agent 录制与回放 - 需求文档

> 模块：`study-ai-best-pratice-rca-agent`
> 基础路径：`http://<host>:8082/api/rca`
> 版本：v1.0

---

## 1. 背景与目标

RCA Agent 采用确定性管道（Plan → BroadScan → BroadAnalyze → FocusedScan → FocusedAnalyze → GlobalReact）分析告警根因。管道中存在两类**外部交互**：

1. **MCP 工具调用**（`McpTool.execute`）：采集指标、变更事件等数据；
2. **LLM 调用**（`LlmGateway.complete`）：在 Plan 与证据解读节点调用大模型。

为了让 RCA 分析过程**可追溯、可复现、可对比**，本期新增「录制-回放」能力与分析历史管理：

- **录制**：在一次分析期间捕获全部外部交互的入参与出参；
- **回放**：基于历史录制，用录制的出参替代真实外部调用，确定性复现分析结果；
- **历史管理**：持久化分析记录（请求、响应、录制），支持查询、详情、删除。

---

## 2. 功能范围

### 2.1 录制（Record）

- `POST /api/rca/analyze` 请求体新增 `record` 字段（默认 `false`）。
- 当 `record=true` 时，本次分析期间所有 TOOL 与 LLM 交互被录制，随分析结果存入历史。
- 每条交互记录包含：类型、名称、入参 JSON、出参 JSON、时间戳、耗时、是否成功、错误信息。

### 2.2 历史（History）

| 接口 | 说明 |
|------|------|
| `GET /api/rca/history` | 分析历史列表（分页，按时间倒序） |
| `GET /api/rca/history/{id}` | 分析详情（请求 + 响应 + 录制交互明细） |
| `DELETE /api/rca/history/{id}` | 删除分析历史及关联录制 |

### 2.3 回放（Replay）

| 接口 | 说明 |
|------|------|
| `POST /api/rca/replay/{id}` | 基于指定历史的录制进行回放，返回与原始一致的 `RcaResponse` |

回放时，工具调用与 LLM 调用均返回录制的出参，不触发真实外部调用，保证结果确定性。

### 2.4 录制明细（Recordings）

| 接口 | 说明 |
|------|------|
| `GET /api/rca/recordings` | 录制列表 |
| `GET /api/rca/recordings/{id}` | 录制全部交互明细（按时间顺序） |

### 2.5 前端页面

| 页面 | 路由 | 功能 |
|------|------|------|
| RCA 分析 | `/analyze` | 新增「录制交互」开关 |
| 分析历史 | `/history` | 历史列表 + 详情（结论/阶段/假设/证据/录制摘要） |
| 录制明细 | `/recording` | 录制列表 + 交互入参出参查看（支持 TOOL/LLM 过滤） |
| 回放对比 | `/replay` | 选择历史 → 回放 → 原始与回放结果并排对比 + 一致性校验 |
| 服务健康 | `/health` | 后端健康检查 |

---

## 3. 数据模型

### 3.1 InteractionRecord（单条交互录制）

| 字段 | 类型 | 说明 |
|------|------|------|
| type | enum `TOOL` / `LLM` | 交互类型 |
| name | string | 工具名或 `complete`（LLM） |
| inputJson | string | 入参 JSON 字符串 |
| outputJson | string | 出参 JSON 字符串 |
| timestamp | datetime | 交互时间 |
| durationMs | long | 耗时（毫秒） |
| success | boolean | 是否成功 |
| error | string | 错误信息（失败时） |

### 3.2 Recording（一次分析的录制）

| 字段 | 类型 | 说明 |
|------|------|------|
| recordingId | string | 录制唯一 ID（`rec-{analysisId}`） |
| analysisId | string | 关联分析 ID |
| interactions | List\<InteractionRecord\> | 按时间排序的交互列表 |
| createdAt | datetime | 创建时间 |

### 3.3 AnalysisRecord（分析历史记录）

| 字段 | 类型 | 说明 |
|------|------|------|
| analysisId | string | 分析唯一 ID |
| request | RcaRequest | 原始请求 |
| response | RcaResponse | 原始响应 |
| recording | Recording \| null | 录制（未录制时为 null） |
| createdAt | datetime | 创建时间 |
| resultLevel | string | CONFIRMED / SUSPECTED / INCONCLUSIVE |
| recorded | boolean | 是否录制 |

---

## 4. 录制与回放原理

### 4.1 架构

```
RcaOrchestrator.analyze(request)
  ├── 若 record=true：创建 RecordingSession(RECORD)，绑定 RecordingContext(ThreadLocal)
  ├── 若 replayRecording!=null：创建 RecordingSession(REPLAY)
  └── 管道执行：
      ├── McpTool.execute(query)
      │   └── RecordingMcpTool 装饰器：
      │       ├── RECORD：执行真实工具 → 记录 (query, result)
      │       └── REPLAY：按复合键查找录制 → 返回录制 result（不执行真实工具）
      └── LlmGateway.complete(prompt)
          ├── RECORD：执行真实 LLM → 记录 (prompt, response)
          └── REPLAY：按复合键查找录制 → 返回录制 response
```

### 4.2 复合键匹配

回放匹配使用复合键：`{TYPE}:{name}:{SHA-256(inputJson)}`。

- 容忍 BroadScan 并行流导致的交互乱序；
- 同一工具+相同入参的多次调用按顺序消费（`consumed` 下标集合）。

### 4.3 并发安全

- `RecordingContext` 基于 ThreadLocal；
- BroadScan 的 `parallelStream` 会显式将主线程的 `RecordingSession` 传播到 ForkJoinPool worker 线程；
- `RecordingSession` 的 `record` 与 `replay` 方法均为 `synchronized`，保证并发录制与回放消费的线程安全。

### 4.4 零侵入保证

- `record=false`（默认）时，`RecordingContext` 不设置，`RecordingMcpTool` 与 `LlmGateway` 直透传，行为与改造前完全一致；
- 现有全部单元测试保持通过。

---

## 5. 接口详情

### 5.1 POST /api/rca/analyze（扩展）

请求体新增 `record` 字段：

```json
{
  "appId": "order-service",
  "alarmDescription": "MQ 消息堆积，消费者 lag 持续增长",
  "scene": "mq_lag",
  "enableLlm": false,
  "record": true
}
```

### 5.2 GET /api/rca/history

**Query**: `page`（默认 0）、`size`（默认 20）

**响应**:

```json
{
  "items": [
    {
      "analysisId": "de848200d402",
      "appId": "order-service",
      "scene": "mq_lag",
      "resultLevel": "CONFIRMED",
      "recorded": true,
      "recordingId": "rec-de848200d402",
      "createdAt": "2026-09-24T00:21:10"
    }
  ],
  "total": 1,
  "page": 0,
  "size": 20
}
```

### 5.3 GET /api/rca/history/{id}

返回完整 `AnalysisRecord`（含 request、response、recording）。

### 5.4 DELETE /api/rca/history/{id}

- 成功：`204 No Content`
- 不存在：`404 Not Found`

### 5.5 GET /api/rca/recordings

返回 `RecordingSummary` 列表：

```json
[
  {
    "recordingId": "rec-de848200d402",
    "analysisId": "de848200d402",
    "interactionCount": 15,
    "createdAt": "2026-09-24T00:21:10"
  }
]
```

### 5.6 GET /api/rca/recordings/{id}

返回完整 `Recording`（含全部 `InteractionRecord`）。

### 5.7 POST /api/rca/replay/{id}

- 成功：`200` + `RcaResponse`（与原始分析一致）
- 历史不存在：`404`
- 历史未录制：`400`

---

## 6. 使用示例

### 6.1 录制一次分析

```bash
curl -X POST http://localhost:8082/api/rca/analyze \
  -H "Content-Type: application/json" \
  -d '{"appId":"order-service","alarmDescription":"MQ 消息堆积","scene":"mq_lag","record":true}'
```

### 6.2 查看录制交互

```bash
curl http://localhost:8082/api/rca/history/{analysisId} | jq '.recording.interactions[] | {type,name,durationMs,success}'
```

### 6.3 回放

```bash
curl -X POST http://localhost:8082/api/rca/replay/{analysisId}
```

### 6.4 前端操作

1. 进入「RCA 分析」页，开启「录制交互」开关，执行分析；
2. 进入「分析历史」页，点击记录查看详情（含录制摘要）；
3. 进入「录制明细」页，选择录制查看每条工具/LLM 调用的入参出参；
4. 进入「回放对比」页，选择已录制的历史，点击「执行回放」，查看原始与回放结果的一致性对比。

---

## 7. 约束与限制

- **内存存储**：历史与录制存储在内存中，服务重启后丢失；不引入数据库依赖。
- **无 LLM 依赖**：回放不依赖真实 LLM，LLM 出参从录制中取。
- **回放范围**：仅回放工具与 LLM 调用；六阶段业务逻辑（裁决、打分、拓扑、变更感知）仍由确定性程序执行。
- **并发**：录制上下文按请求隔离，支持并发分析。

---

## 8. 验收

- [x] `record=true` 分析后，历史详情中包含非空录制，交互数 > 0
- [x] 回放响应与原始响应的结论数、top1 假设、置信度、证据数完全一致
- [x] 历史列表、详情、删除、录制列表、录制详情接口均返回正确 HTTP 状态码与字段
- [x] `record=false` 时不生成录制，现有全部单元测试通过
- [x] 前端历史、录制、回放页面可正常访问并展示数据
