# RCA Agent 录制与回放 - Implementation Plan

## Task 1: 后端录制数据模型与存储服务
- **Status**: `pending`
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 新建 `recording` 包，定义 `InteractionRecord`（type/name/inputJson/outputJson/timestamp/durationMs/success/error）、`Recording`（recordingId/analysisId/interactions）、`AnalysisRecord`（analysisId/request/response/recording/createdAt/resultLevel）。
  - 实现 `RecordingService`：基于 `ConcurrentHashMap` 的内存存储，提供 save/get/list/delete 方法；分析记录与录制关联。
- **Acceptance Criteria Addressed**: AC-1, AC-3, AC-4, AC-6
- **Test Requirements**:
  - `rule` TR-1.1: `RecordingService.save` 后可通过 `get` 取回，`list` 包含该记录；`delete` 后 `get` 返回 null。证据：单元测试通过。
- **Notes**: type 枚举为 TOOL / LLM。

## Task 2: 后端录制上下文与工具/LLM 装饰器
- **Status**: `pending`
- **Priority**: high
- **Depends On**: Task 1
- **Description**:
  - 实现 `RecordingContext`（ThreadLocal）：保存当前分析的 `Recording` 构建器与模式（LIVE/RECORD/REPLAY）。
  - 实现 `RecordingMcpTool` 装饰器：包装 `McpTool`，RECORD 模式下记录 execute 的入参/出参/耗时，REPLAY 模式下按复合键（type+name+inputHash）返回录制出参，LIVE 模式直透传。
  - 实现 `RecordingLlmGateway` 或改造 `LlmGateway`：同样支持 RECORD/REPLAY。
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-9
- **Test Requirements**:
  - `rule` TR-2.1: RECORD 模式下调用工具后，RecordingContext 中存在对应 InteractionRecord，inputJson/outputJson 非空。证据：单元测试。
  - `rule` TR-2.2: REPLAY 模式下调用工具返回录制出参，不触发真实 execute。证据：单元测试 mock 验证 execute 未被调用。
  - `rule` TR-2.3: LIVE 模式下行为与原 McpTool 完全一致。证据：单元测试。
- **Notes**: 复合键使用 `TOOL:{name}:{inputHash}` 与 `LLM:complete:{inputHash}`，inputHash 用 Jackson 序列化后 SHA-256。

## Task 3: 后端工具注册与 LLM 网关接入录制装饰
- **Status**: `pending`
- **Priority**: high
- **Depends On**: Task 2
- **Description**:
  - 修改 `ToolConfig.toolRegistry()`：将所有 `McpTool` 用 `RecordingMcpTool` 包装后注册。
  - 修改 `LlmGateway`：在 `complete` 方法中接入 `RecordingContext` 的录制/回放逻辑。
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-9
- **Test Requirements**:
  - `rule` TR-3.1: ToolRegistry 中的工具均为 RecordingMcpTool 实例。证据：启动后断言。
  - `rule` TR-3.2: LlmGateway.complete 在 RECORD 模式产生录制，REPLAY 模式返回录制值。证据：单元测试。
- **Notes**: 保持 `LlmGateway` 原有软降级逻辑不变。

## Task 4: 后端编排器接入录制开关
- **Status**: `pending`
- **Priority**: high
- **Depends On**: Task 3
- **Description**:
  - `RcaRequest` 新增 `record` 字段（默认 false）。
  - 修改 `RcaOrchestrator.analyze`：当 `request.isRecord()` 为 true 时，启动 `RecordingContext`（RECORD 模式），分析结束后将录制交由 `RecordingService` 持久化并关联到分析记录；无论是否录制，每次分析都生成一条 `AnalysisRecord` 存入历史。
- **Acceptance Criteria Addressed**: AC-1, AC-9
- **Test Requirements**:
  - `rule` TR-4.1: `record=true` 时分析后 RecordingService 中存在含录制的 AnalysisRecord。证据：集成测试。
  - `rule` TR-4.2: `record=false` 时不产生录制，AnalysisRecord.recording 为 null。证据：集成测试。
- **Notes**: 录制上下文必须在 finally 中清理，避免 ThreadLocal 泄漏。

## Task 5: 后端历史/录制/回放 HTTP 接口
- **Status**: `pending`
- **Priority**: high
- **Depends On**: Task 4
- **Description**:
  - 新增 `RcaHistoryController`（或扩展 `RcaController`）：
    - `GET /api/rca/history` —— 历史列表（分页 page/size）
    - `GET /api/rca/history/{id}` —— 历史详情（含录制）
    - `DELETE /api/rca/history/{id}` —— 删除
    - `GET /api/rca/recordings` —— 录制列表
    - `GET /api/rca/recordings/{id}` —— 录制详情
    - `POST /api/rca/replay/{id}` —— 基于历史录制回放，返回 RcaResponse
  - 统一返回结构，404 处理不存在的记录。
- **Acceptance Criteria Addressed**: AC-3, AC-4, AC-5, AC-2
- **Test Requirements**:
  - `rule` TR-5.1: 各接口 HTTP 状态码与字段符合契约。证据：MockMvc 测试。
  - `rule` TR-5.2: `POST /api/rca/replay/{id}` 返回与原始记录一致的结论与置信度。证据：MockMvc 测试断言。
  - `rule` TR-5.3: 删除后查询返回 404。证据：MockMvc 测试。
- **Notes**: 回放时从历史记录取出 request 与 recording，设置 REPLAY 上下文后调用 orchestrator.analyze。

## Task 6: 后端录制与回放集成测试
- **Status**: `pending`
- **Priority**: high
- **Depends On**: Task 5
- **Description**:
  - 编写端到端测试：record=true 分析 → 取回录制 → 回放 → 断言回放结果与原始结果关键字段一致。
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `rule` TR-6.1: 回放响应的 conclusions.hypothesisId/confidence 与原始完全一致。证据：测试断言通过。
- **Notes**: 覆盖 default 与 mq_lag 两个场景。

## Task 7: 前端 API 服务层扩展
- **Status**: `pending`
- **Priority**: medium
- **Depends On**: Task 5
- **Description**:
  - 在 `src/api/rca.js` 新增：`getHistory(page,size)`、`getHistoryDetail(id)`、`deleteHistory(id)`、`getRecordings()`、`getRecordingDetail(id)`、`replay(id)`。
- **Acceptance Criteria Addressed**: AC-6, AC-7, AC-8
- **Test Requirements**:
  - `rule` TR-7.1: 每个 API 函数封装正确的 HTTP 方法与路径。证据：代码审查 + 构建通过。
- **Notes**: 沿用现有 axios 实例与 baseURL `/api/rca`。

## Task 8: 前端历史管理页
- **Status**: `pending`
- **Priority**: medium
- **Depends On**: Task 7
- **Description**:
  - 新建 `src/views/History.vue`：历史列表表格（analysisId/appId/scene/resultLevel/录制标记/时间），支持分页；点击行展开或弹窗查看详情（结论、阶段轨迹、假设、证据、录制摘要）。
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `rubric` TR-8.1: 历史页功能完整度；scale 1-5；anchors 1/3/5；threshold >= 4；evidence 页面运行截图。
- **Notes**: 复用 Analyze.vue 中的结果展示组件逻辑。

## Task 9: 前端录制明细页
- **Status**: `pending`
- **Priority**: medium
- **Depends On**: Task 7
- **Description**:
  - 新建 `src/views/Recording.vue`：录制列表，点击查看交互明细（按时间排序的表格/时间线，展示 type/name/耗时/成功，展开查看 inputJson/outputJson）；支持按 TOOL/LLM 过滤。
- **Acceptance Criteria Addressed**: AC-8
- **Test Requirements**:
  - `rubric` TR-9.1: 录制明细展示完整度；scale 1-5；anchors 1/3/5；threshold >= 4；evidence 页面运行截图。
- **Notes**: JSON 用 `<pre>` 格式化展示。

## Task 10: 前端回放页
- **Status**: `pending`
- **Priority**: medium
- **Depends On**: Task 7
- **Description**:
  - 新建 `src/views/Replay.vue`：选择历史记录 → 触发回放 → 并排展示原始结果与回放结果，标注是否一致；提供关键指标（结论数、top1 置信度）对比。
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `rubric` TR-10.1: 回放功能完整度；scale 1-5；anchors 1/3/5；threshold >= 4；evidence 页面运行截图。
- **Notes**: 回放结果结构与 RcaResponse 一致，可复用 Analyze 的展示片段。

## Task 11: 前端导航与路由接入
- **Status**: `pending`
- **Priority**: medium
- **Depends On**: Task 8, Task 9, Task 10
- **Description**:
  - 在 `router/index.js` 注册 /history、/recording、/replay 路由；在 `App.vue` 顶部菜单增加对应入口。
- **Acceptance Criteria Addressed**: AC-6, AC-7, AC-8
- **Test Requirements**:
  - `rule` TR-11.1: 三个新页面路由可正常访问，菜单可跳转。证据：构建通过 + 浏览器访问。
- **Notes**: 无。

## Task 12: 需求文档与接口文档更新
- **Status**: `pending`
- **Priority**: medium
- **Depends On**: Task 5
- **Description**:
  - 新建 `docs/requirement/rca-record-replay-需求文档.md`：覆盖功能范围、数据模型、接口清单、录制/回放原理、使用示例。
  - 更新 `docs/api/rca-agent-接口文档.md`：补充新增接口。
- **Acceptance Criteria Addressed**: AC-10
- **Test Requirements**:
  - `rule` TR-12.1: `docs/requirement/rca-record-replay-需求文档.md` 存在且包含数据模型、接口、使用示例章节。证据：文件内容检查。
- **Notes**: 文档语言中文，与现有文档风格一致。
