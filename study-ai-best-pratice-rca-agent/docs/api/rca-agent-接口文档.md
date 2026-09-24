# RCA Agent 接口文档

> 模块：`study-ai-best-pratice-rca-agent`
> 基础路径：`http://<host>:8082/api/rca`
> 架构：确定性管道（Plan → BroadScan → BroadAnalyze → FocusedScan → FocusedAnalyze → GlobalReact），LLM 仅在受约束节点可插拔使用，失败软降级。

---

## 1. 核心端点

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/rca/analyze` | 执行完整 RCA 分析（传入告警描述与场景，支持 `record` 录制） |
| GET  | `/api/rca/analyze/mock` | Mock 场景便捷接口（按 scene 驱动模拟数据） |
| GET  | `/api/rca/health` | 服务健康检查 |
| GET  | `/api/rca/history` | 分析历史列表（分页） |
| GET  | `/api/rca/history/{id}` | 分析历史详情（含请求、响应、录制） |
| DELETE | `/api/rca/history/{id}` | 删除分析历史及关联录制 |
| GET  | `/api/rca/recordings` | 录制列表 |
| GET  | `/api/rca/recordings/{id}` | 录制详情（全部交互明细） |
| POST | `/api/rca/replay/{id}` | 基于历史录制回放，返回确定性一致结果 |

---

## 2. POST /api/rca/analyze

执行一次完整的六阶段根因分析。

### 请求

`Content-Type: application/json`

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| appId | string | 是 | `order-service` | 告警应用标识，用于拓扑中心与 targetName 归一 |
| alarmDescription | string | 是 | — | 告警描述文本，Plan 阶段据此做关键字分类 |
| alarmTime | datetime | 否 | 当前时间 | 告警触发时间，作为时间线锚点 |
| scene | string | 否 | `default` | Mock 场景名，决定各工具返回正常/异常值（见第 5 节） |
| enableLlm | boolean | 否 | `false` | 是否允许在 Plan/BroadAnalyze 调用 LLM；关闭时全链路确定性 |
| record | boolean | 否 | `false` | 是否录制本次分析的全部工具与 LLM 交互；为 `true` 时结果将保存到历史并可回放 |

### 请求示例

```json
{
  "appId": "order-service",
  "alarmDescription": "订单状态更新延迟，MQ 消息堆积，消费者 lag 持续增长",
  "alarmTime": "2026-09-22T22:28:17",
  "scene": "mq_lag",
  "enableLlm": false,
  "record": true
}
```

### 响应

HTTP 200，返回 `RcaResponse`。

| 字段 | 类型 | 说明 |
|------|------|------|
| analysisId | string | 本次分析唯一 ID |
| appId / scene / alarmDescription / alarmTime | — | 回显请求入参 |
| analysisTime | datetime | 分析执行时间 |
| llmUsed | boolean | 本次分析是否实际调用了 LLM |
| resultLevel | string | `CONFIRMED` / `SUSPECTED` / `INCONCLUSIVE` |
| stages | List\<StageTrace\> | 六阶段执行轨迹（见第 3 节） |
| hypotheses | List\<Hypothesis\> | 所有竞争假设及其最终置信度 |
| timeline | List\<TimelineEvent\> | 统一时间线：变更 → 信号 → 告警 |
| evidences | List\<Evidence\> | 全量证据（每条带 evidenceId，结论必须锚定这些 ID） |
| conclusions | List\<Conclusion\> | 按置信度排序的结论（含因果链、正反证据） |
| suggestions | Suggestions | 三层建议：止血 / 根治 / 预防 |

### 响应示例（mq_lag 场景，节选）

```json
{
  "analysisId": "24316d48554f",
  "resultLevel": "CONFIRMED",
  "llmUsed": false,
  "conclusions": [
    {
      "hypothesisId": "h1",
      "rank": 1,
      "title": "MQ 消费堆积导致消息处理延迟，业务履约滞后",
      "targetType": "MQ",
      "targetName": "order-service-mq",
      "symptom": "CONSUMER_LAG",
      "confidence": 0.92,
      "confirmed": true,
      "causalChain": [
        "[变更] MQ 消费者发布 v2.3.1：消费逻辑新增 DB 同步写，消费吞吐下降",
        "[信号] mq_consumer_lag 在 order-service-mq 出现异常（偏离基线 1550.0x）",
        "[传导] order-service-mq（MQ）异常沿依赖传导，最终表现为：...",
        "[告警] 告警触发"
      ],
      "supportEvidenceIds": ["e1", "e2", "e5"],
      "refuteEvidenceIds": []
    }
  ],
  "suggestions": {
    "mitigation": ["临时扩容消费组实例/分区，加快追平 lag", "死信消息先旁路隔离"],
    "remediation": ["修复消费者慢逻辑，同步写 DB 异步化"],
    "prevention": ["lag 增长率与死信量告警"]
  }
}
```

---

## 3. 六阶段输入输出

管道由 [RcaOrchestrator](../../src/main/java/com/bage/study/ai/best/practice/rca/agent/service/RcaOrchestrator.java) 串行编排，每个阶段产出 `StageTrace` 记入响应的 `stages` 字段。

### 阶段 1 — Plan（假设生成）

- **输入**：`RcaRequest`
- **输出**：`PlanResult{ hypotheses, llmUsed, note }`
- **行为**：
  1. 优先 LLM 结构化输出 `targetType + symptom`（受约束 Prompt，枚举合法、3–5 条）；
  2. 非法或未启用 → 走 `RuleBasedPlanner`：按告警文本关键字（堆积/redis/供应商/慢查询/线程池/错误率…）分类主假设，再补齐 3 个竞争假设；
  3. `targetName` 强制按 targetType 归一（APP→appId，DB→appId-db，MQ→appId-mq，REDIS→appId-redis，SUPPLIER→supplier-api，INFRA→appId-host），不采信 LLM 自由文本；
  4. 先验置信度裁剪到 `[0.1, 0.9]`。
- **StageTrace**：`Plan / SUCCESS / llmUsed`

### 阶段 2 — BroadScan（并发采集）

- **输入**：`RcaRequest`, `List<Hypothesis>`
- **输出**：`ScanBundle{ evidences, changeEvents, collectedToolsByHypothesis }`
- **行为**：
  1. 对每个假设按 `ScanPlaybook.broadTools(h)` 路由工具：精确命中 `targetType:symptom` 走方案 C，未命中回退 targetType 通用集（方案 A）；
  2. 假设间 `parallelStream` 并发、同一假设的多个工具并发执行；
  3. 工具返回 `MetricSnapshot` 转为 `Evidence`（Phase=BROAD），分配确定性 evidenceId；
  4. 额外调用 `query_change_events` 拉取告警前变更事件，供时间线与变更感知使用。
- **StageTrace**：`BroadScan / SUCCESS / false`

### 阶段 3 — BroadAnalyze（裁决 + 首轮置信度）

- **输入**：`List<Hypothesis>`, `List<Evidence>`, `llmAllowed`
- **输出**：`boolean`（是否实际调用 LLM 生成叙述）
- **行为**：
  1. 对每个假设的 BROAD 证据，按 `ScanPlaybook.decisiveTools(symptom)` 裁决：
     - 决定性信号异常 → SUPPORT，权重随偏离倍数递增（最高 1.0）；
     - 决定性信号正常 → REFUTE（弱权重，反事实含义"应看到却没看到"）；
     - 非决定性信号异常 → SUPPORT（旁证，权重 0.4）；其余正常 → NEUTRAL；
  2. 用贝叶斯式公式更新置信度：`confidence = 0.35 * prior + 0.65 * belief`，`belief = support / (support + 1.2 * refute + 0.5)`；
  3. 可选 LLM 解读：仅改写 `rationale` 叙述，不参与打分；失败回退模板化中文叙述。
- **StageTrace**：`BroadAnalyze / SUCCESS / narrated`

### 阶段 4 — FocusedScan（topN 下钻）

- **输入**：`RcaRequest`, `List<Hypothesis>`, `ScanBundle`, `topN`, `idStartSeq`
- **输出**：`List<Evidence>`（Phase=FOCUSED）
- **行为**：
  1. 按置信度降序取 topN（默认 2），标记 `focused=true`；
  2. 调 `ScanPlaybook.focusedTools(h, alreadyCollected)` 取递归下钻工具集（trace_span_detail / db_top_sql / consumer_group_detail / http_error_breakdown 等），自动去重已采集工具；
  3. 新增证据并入全量证据，evidenceId 接续 BroadScan 编号。
- **StageTrace**：`FocusedScan / SUCCESS / false`

### 阶段 5 — FocusedAnalyze（全量复核）

- **输入**：`List<Hypothesis>`, `List<Evidence>`（BROAD + FOCUSED）
- **输出**：无（原地更新 focused 假设的置信度）
- **行为**：对每个 focused 假设，用 BROAD+FOCUSED 全量证据重新裁决（FOCUSED 阶段证据权重 ×1.5）并刷新置信度。
- **StageTrace**：`FocusedAnalyze / SUCCESS / false`

### 阶段 6 — GlobalReact（全局校准 + 分级）

- **输入**：`RcaRequest`, `List<Hypothesis>`, `List<Evidence>`, `List<TimelineEvent>`
- **输出**：`ReactResult{ conclusions, suggestions, resultLevel, trace }`
- **行为**：
  1. **拓扑裁剪**：目标不在告警应用 ±2 跳范围内 → 置信度 ×0.7；
  2. **变更感知**：故障前 30 分钟内存在同 target 变更 → 置信度 +0.12；
  3. **反事实验证**：若根因成立，决定性信号应异常；已采集决定性信号中支持的比例 <50% → 置信度 -0.15，并写入 `counterfactual` 字段；
  4. 置信度裁剪到 `[0.01, 0.99]`，按降序排序并赋值 rank；
  5. 构建 `causalChain`：变更事件 → 异常信号 → 传导 → 告警；
  6. **分级**：top1 ≥0.60 → CONFIRMED；≥0.35 → SUSPECTED；否则 INCONCLUSIVE；
  7. 按 top1 的 targetType/symptom 生成三层建议；INCONCLUSIVE 时给兜底止血清单。
- **StageTrace**：`GlobalReact / SUCCESS / false`

---

## 4. 数据模型速查

### Hypothesis

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 稳定编号 `h1..hN` |
| desc | string | 因果机制描述 |
| targetType | enum | APP / DB / REDIS / MQ / SUPPLIER / INFRA |
| targetName | string | 归一化实例名 |
| symptom | enum | HIGH_LATENCY / HIGH_ERROR_RATE / RESOURCE_EXHAUSTED / CONSUMER_LAG / DEPENDENCY_FAILURE |
| priorConfidence | double | Plan 阶段先验 |
| confidence | double | 经各阶段更新后的最终置信度 |
| focused | boolean | 是否进入 FocusedScan |
| rationale | string | 证据解读（模板或 LLM 生成） |
| counterfactual | string | 反事实验证结论 |
| supportEvidenceIds / refuteEvidenceIds | List\<string\> | 锚定到具体 evidenceId |

### Evidence

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | `e1..eN` |
| hypothesisId | string | 关联假设 |
| phase | enum | BROAD / FOCUSED |
| toolName / layer / targetName | string | 来源工具与目标 |
| value / baseline / deviationRatio | double | 当前值 / 基线 / 偏离倍数 |
| anomaly | boolean | 是否异常 |
| verdict | enum | SUPPORT / REFUTE / NEUTRAL |
| weight | double | 裁决权重 |
| observedAt | datetime | 信号观测时间 |

### TimelineEvent

| 字段 | 类型 | 说明 |
|------|------|------|
| time | datetime | 事件时间 |
| kind | enum | ALERT / CHANGE / SIGNAL |
| source | string | 来源（工具名或 alarm） |
| relatedTarget | string | 关联目标 |
| description | string | 事件描述 |

### Conclusion

| 字段 | 类型 | 说明 |
|------|------|------|
| hypothesisId / rank | string / int | 对应假设与排名 |
| title / targetType / targetName / symptom | — | 假设关键信息 |
| confidence / confirmed | double / boolean | 最终置信度与是否确认 |
| rationale / counterfactual | string | 解读与反事实 |
| causalChain | List\<string\> | 时序因果链 |
| supportEvidenceIds / refuteEvidenceIds | List\<string\> | 锚定证据 ID |

---

## 5. Mock 场景用法

Mock 数据由 [MockToolCatalog](../../src/main/java/com/bage/study/ai/best/practice/rca/agent/tool/MockToolCatalog.java) 与 [ChangeEventTool](../../src/main/java/com/bage/study/ai/best/practice/rca/agent/tool/ChangeEventTool.java) 驱动：每个工具声明 `normal` / `threshold` / `abnormal` 三档值，当 `scene` 命中该工具的 `abnormalScenes` 集合时返回异常值并计算 `deviationRatio`。

| scene | 注入异常域 | 关键异常指标 | 故障前变更事件 |
|-------|-----------|--------------|---------------|
| `default` | 无 | 全量正常 | 无 |
| `db_slow_query` | DB + APP 延迟 | db_slow_query、db_lock_waits、db_connections、db_cpu、db_top_sql，trace_p99/trace_span_detail | DB Schema 变更：新增索引失败，大事务持锁 8 分钟 |
| `mq_lag` | MQ | mq_consumer_lag、mq_consume_rate(降)、mq_dead_letter、mq_consume_status、consumer_group_detail | MQ 消费者发布 v2.3.1：消费逻辑新增 DB 同步写 |
| `app_resource` | APP + 日志聚类 | app_gc_log、app_thread_pool、app_heap_usage、log_error_pattern | 应用发布 v3.1.0：本地缓存未设上限，堆内存持续上涨 |
| `supplier_failure` | SUPPLIER + APP 延迟 | http_success_rate(降)、http_p99_latency、circuit_breaker_status、downstream_success_rate(降)、http_error_breakdown | 无 |
| `high_error` | APP 错误 | app_error_rate、app_error_log、log_error_pattern | 应用发布 v3.1.2：下单参数校验重构引入 NPE |
| `redis` | REDIS + APP 延迟 | redis_hit_rate(降)、redis_eviction、redis_memory、redis_latency，trace_p99/trace_span_detail | Redis 配置变更：maxmemory-policy 改为 noeviction |

### 通过 GET 便捷接口调用

```bash
# 默认随机场景
curl "http://localhost:8082/api/rca/analyze/mock?scene=mq_lag"

# 开启 LLM（需先设置 DEEPSEEK_API_KEY 环境变量）
curl "http://localhost:8082/api/rca/analyze/mock?scene=db_slow_query&enableLlm=true"
```

### 通过 POST 自定义告警描述

```bash
curl -X POST http://localhost:8082/api/rca/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "appId": "order-service",
    "alarmDescription": "下单接口大量超时，P99 延迟飙升，疑似慢查询",
    "scene": "db_slow_query"
  }'
```

### 典型预期结果

| scene | 预期 top1 targetType:symptom | resultLevel |
|-------|------------------------------|-------------|
| `mq_lag` | MQ:CONSUMER_LAG | CONFIRMED |
| `db_slow_query` | DB:HIGH_LATENCY | CONFIRMED |
| `app_resource` | APP:RESOURCE_EXHAUSTED | CONFIRMED |
| `supplier_failure` | SUPPLIER:DEPENDENCY_FAILURE | CONFIRMED |
| `high_error` | APP:HIGH_ERROR_RATE | CONFIRMED |
| `redis` | REDIS:HIGH_LATENCY/RESOURCE_EXHAUSTED | CONFIRMED |
| `default` | 竞争假设均无决定性支持 | INCONCLUSIVE |

---

## 6. 健康检查

```bash
curl http://localhost:8082/api/rca/health
```

响应：

```json
{"service":"rca-agent","status":"UP"}
```

---

## 7. 分析历史与录制回放

本节接口提供 RCA 分析历史管理、录制明细查询与确定性回放能力。数据存储于内存，服务重启后清空。

### 7.1 GET /api/rca/history

分析历史列表，按创建时间倒序分页。

**Query**

| 参数 | 类型 | 默认 | 说明 |
|------|------|------|------|
| page | int | 0 | 页码（从 0 开始） |
| size | int | 20 | 每页条数 |

**响应 200**

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

### 7.2 GET /api/rca/history/{id}

返回完整 `AnalysisRecord`，包含原始请求、响应及录制交互明细。

| 状态 | 说明 |
|------|------|
| 200 | 成功，返回 AnalysisRecord |
| 404 | 历史不存在 |

### 7.3 DELETE /api/rca/history/{id}

删除分析历史及其关联录制。

| 状态 | 说明 |
|------|------|
| 204 | 删除成功 |
| 404 | 历史不存在 |

### 7.4 GET /api/rca/recordings

录制列表。

**响应 200**

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

### 7.5 GET /api/rca/recordings/{id}

返回完整 `Recording`，含全部 `InteractionRecord`（按时间顺序）。

`InteractionRecord` 字段：

| 字段 | 类型 | 说明 |
|------|------|------|
| type | string | `TOOL` 或 `LLM` |
| name | string | 工具名或 `complete`（LLM） |
| inputJson | string | 入参 JSON |
| outputJson | string | 出参 JSON |
| timestamp | datetime | 交互时间 |
| durationMs | long | 耗时（毫秒） |
| success | boolean | 是否成功 |
| error | string | 错误信息（失败时） |

| 状态 | 说明 |
|------|------|
| 200 | 成功，返回 Recording |
| 404 | 录制不存在 |

### 7.6 POST /api/rca/replay/{id}

基于指定历史的录制进行回放。回放时工具与 LLM 调用均返回录制的出参，不触发真实外部调用，保证结果确定性。

| 状态 | 说明 |
|------|------|
| 200 | 成功，返回与原始分析一致的 RcaResponse |
| 400 | 历史未录制（无法回放） |
| 404 | 历史不存在 |

**示例**

```bash
curl -X POST http://localhost:8082/api/rca/replay/de848200d402
```

---

## 8. 配置项

| 配置 | 环境变量 | 默认 | 说明 |
|------|----------|------|------|
| `server.port` | — | 8082 | 服务端口 |
| `spring.ai.openai.base-url` | `DEEPSEEK_BASE_URL` | https://api.deepseek.com | LLM 接入点 |
| `spring.ai.openai.api-key` | `DEEPSEEK_API_KEY` | `sk-dummy-local`（占位） | LLM 密钥；占位仅用于通过 Spring AI 启动校验 |
| `rca.llm.enabled` | `RCA_LLM_ENABLED` | false | 全局 LLM 开关；false 时 ChatClient 不构建 |
| `rca.scan.focused-top-n` | — | 2 | FocusedScan 下钻假设数 |
| `rca.scan.change-window-minutes` | — | 30 | 变更感知时间窗（分钟） |
| `rca.scan.topology-hops` | — | 2 | 拓扑裁剪跳数 |

> LLM 仅在 `rca.llm.enabled=true` **且** 请求体 `enableLlm=true` 时才会在 Plan / BroadAnalyze 节点调用；任何调用失败均软降级到确定性路径，不影响主链路。

---

## 9. 启动

```bash
mvn -pl study-ai-best-pratice-rca-agent spring-boot:run
```

默认 `rca.llm.enabled=false`，无需真实 API Key 即可运行全流程。启用 LLM：

```bash
export DEEPSEEK_API_KEY=sk-xxxx
export RCA_LLM_ENABLED=true
mvn -pl study-ai-best-pratice-rca-agent spring-boot:run
```
