# RCA Agent 录制与回放 - Product Requirements Document

## Overview
- **Summary**: 为 RCA 后端服务增加「录制-回放」能力与 RCA 分析历史管理，并由 web 模块提供可视化页面。录制模式下捕获 RCA 管道中全部外部交互（MCP 工具调用、LLM 调用）的入参与出参；回放模式下用录制的出参替代真实外部调用，确定性复现任意一次分析结果。
- **Purpose**: 让 RCA 分析过程可追溯、可复现、可对比，支撑故障复盘、回归验证、LLM 行为审计与问题定位。
- **Target Users**: SRE、研发工程师、RCA 系统维护者。

## Goals
- 完整记录一次 RCA 分析期间所有外部交互的输入与输出。
- 支持基于历史录制进行确定性回放，复现与原始分析一致的结论。
- 提供 RCA 分析历史、录制明细、回放结果的 HTTP API 与 web 可视化页面。
- 录制/回放机制对现有 RCA 主链路零侵入（默认关闭，不影响现有行为）。

## Non-Goals
- 不引入持久化数据库（本期使用内存存储，重启丢失）。
- 不实现跨节点分布式录制协调。
- 不修改 RCA 六阶段的业务逻辑与打分规则。
- 不对接真实 MCP Server 或真实 LLM（保持现有 Mock 与可插拔架构）。

## Background & Context
- 当前 RCA 管道（Plan→BroadScan→BroadAnalyze→FocusedScan→FocusedAnalyze→GlobalReact）存在两类外部交互：
  1. `McpTool.execute(ToolQuery)` —— 数据采集工具（指标、变更事件），由 `BroadScanService`、`FocusedScanService`、`ChangeEventTool` 调用。
  2. `LlmGateway.complete(String prompt)` —— LLM 调用，由 `LlmPlanner`、`BroadAnalyzeService` 调用。
- 现有工具为确定性 Mock，LLM 为可插拔软降级；但缺少对外部交互的统一记录与回放能力。
- 项目已拆分为 `rca-agent-backend`（Spring Boot）与 `rca-agent-web`（Vue3+Element Plus）两个子模块。

## Functional Requirements

- **FR-1（录制触发）**: `POST /api/rca/analyze` 请求体新增 `record` 布尔字段（默认 false）。当 `record=true` 时，本次分析期间所有外部交互被录制并随分析结果持久化。
- **FR-2（录制内容）**: 每条录制记录包含：交互类型（TOOL/LLM）、名称、入参 JSON、出参 JSON、时间戳、耗时、是否成功、错误信息。
- **FR-3（历史列表）**: `GET /api/rca/history` 返回分析历史摘要列表（analysisId、appId、scene、resultLevel、是否录制、创建时间），支持分页。
- **FR-4（历史详情）**: `GET /api/rca/history/{id}` 返回某次分析的完整请求、响应及（若有）录制交互明细。
- **FR-5（回放）**: `POST /api/rca/replay/{id}` 基于指定历史记录的录制进行回放：工具调用与 LLM 调用均返回录制的出参，返回与原始分析一致的 `RcaResponse`。
- **FR-6（录制列表）**: `GET /api/rca/recordings` 返回所有录制的摘要列表。
- **FR-7（录制详情）**: `GET /api/rca/recordings/{id}` 返回指定录制的全部交互明细（按时间顺序）。
- **FR-8（删除历史）**: `DELETE /api/rca/history/{id}` 删除指定历史记录及其关联录制。
- **FR-9（前端历史页）**: web 模块提供 RCA 分析历史列表页，支持查看详情（含阶段轨迹、结论、证据）。
- **FR-10（前端回放页）**: web 模块提供回放功能，可选择历史记录触发回放并展示回放结果与原始结果的对比。
- **FR-11（前端录制页）**: web 模块提供录制列表与交互明细查看页，展示每次工具/LLM 调用的入参与出参。

## Non-Functional Requirements
- **NFR-1（确定性）**: 同一份录制在相同请求下回放，产出的 `RcaResponse`（结论、置信度、证据）必须与原始分析完全一致。
- **NFR-2（零侵入）**: 不录制时（`record=false`），RCA 主链路行为、性能与改造前完全一致。
- **NFR-3（并发安全）**: 录制上下文基于请求隔离（ThreadLocal），并发分析互不干扰。
- **NFR-4（API 契约）**: 所有新增接口遵循 RESTful 风格，返回统一结构，错误使用 HTTP 状态码 + message。

## Constraints
- **Technical**: Java 17、Spring Boot 3.3、Spring AI 1.0.0-M6；前端 Vue 3 + Vite + Element Plus。
- **Business**: 本期仅内存存储，不引入 DB 依赖；保持现有 Mock 工具与 LLM 软降级架构。
- **Dependencies**: 无新增外部依赖（复用 Jackson、Spring Web）。

## Assumptions
- RCA 管道的工具调用序列在相同请求+录制出参下是确定性的（LLM 出参也被录制，故回放不依赖真实 LLM）。
- 录制与回放匹配策略采用「交互类型+名称+入参哈希」复合键，容忍 BroadScan 的并发乱序。
- 内存存储容量足够支撑学习/演示场景的历史记录量。

## Acceptance Criteria

### AC-1: 录制触发与捕获
- **Type**: `rule`
- **Given**: 调用 `POST /api/rca/analyze` 且 `record=true`
- **When**: 分析完成
- **Then**: 生成一条历史记录，其录制中包含本次分析全部 TOOL 与 LLM 交互，每条含入参出参 JSON
- **Pass Condition**: 录制的交互数 > 0，且每条交互的 inputJson/outputJson 非空
- **Evidence**: 后端单测 + `GET /api/rca/history/{id}` 接口返回

### AC-2: 回放确定性
- **Type**: `rule`
- **Given**: 存在一条录制过的历史记录 R（含完整录制）
- **When**: 调用 `POST /api/rca/replay/{R.id}`
- **Then**: 返回的 `RcaResponse` 与 R 原始响应的 conclusions、hypotheses.confidence、evidences 完全一致
- **Pass Condition**: 回放响应与原始响应关键字段逐字段相等
- **Evidence**: 后端单测断言回放结果 equals 原始结果

### AC-3: 历史列表与详情 API
- **Type**: `rule`
- **Given**: 系统中存在 N 条历史记录
- **When**: 调用 `GET /api/rca/history` 与 `GET /api/rca/history/{id}`
- **Then**: 列表返回 N 条摘要；详情返回完整 request+response+recording
- **Pass Condition**: HTTP 200，字段齐全
- **Evidence**: 接口调用日志

### AC-4: 录制列表与详情 API
- **Type**: `rule`
- **Given**: 存在录制记录
- **When**: 调用 `GET /api/rca/recordings` 与 `GET /api/rca/recordings/{id}`
- **Then**: 返回录制摘要与按时间排序的交互明细
- **Pass Condition**: HTTP 200，交互按时间正序
- **Evidence**: 接口调用日志

### AC-5: 删除历史
- **Type**: `rule`
- **Given**: 存在历史记录 R
- **When**: 调用 `DELETE /api/rca/history/{R.id}`
- **Then**: R 从历史列表与录制列表中均消失
- **Pass Condition**: 后续 `GET /api/rca/history/{R.id}` 返回 404
- **Evidence**: 接口调用日志

### AC-6: 前端历史页可视化
- **Type**: `rubric`
- **Dimension**: 历史页功能完整度与可用性
- **Scale**: 1-5
- **Anchors**: 1 = 仅有静态表格无数据；3 = 列表+详情弹窗，字段基本展示；5 = 列表分页+搜索+详情完整展示结论/阶段/证据/录制
- **Pass Threshold**: >= 4
- **Evidence**: 页面实际运行截图与交互

### AC-7: 前端回放页可视化
- **Type**: `rubric`
- **Dimension**: 回放功能完整度与结果对比清晰度
- **Scale**: 1-5
- **Anchors**: 1 = 仅有按钮无结果；3 = 可触发回放并展示结果；5 = 可选择历史、触发回放、并排对比原始与回放结果、展示差异
- **Pass Threshold**: >= 4
- **Evidence**: 页面实际运行截图与交互

### AC-8: 前端录制页可视化
- **Type**: `rubric`
- **Dimension**: 录制明细展示完整度
- **Scale**: 1-5
- **Anchors**: 1 = 仅列表无明细；3 = 列表+展开查看单条交互入参出参；5 = 列表+时间线/表格展示全部交互、支持按类型过滤、JSON 格式化展示
- **Pass Threshold**: >= 4
- **Evidence**: 页面实际运行截图与交互

### AC-9: 不录制时零侵入
- **Type**: `rule`
- **Given**: `record=false`（默认）
- **When**: 执行 RCA 分析
- **Then**: 行为与改造前完全一致，不生成录制记录，性能无显著下降
- **Pass Condition**: 现有全部单元测试通过，响应结构不变
- **Evidence**: `mvn test` 全部通过

### AC-10: 需求文档
- **Type**: `rule`
- **Given**: 功能开发完成
- **When**: 查看 `docs/` 目录
- **Then**: 存在录制回放功能的需求文档，覆盖功能范围、接口、数据模型、使用方式
- **Pass Condition**: `docs/requirement/rca-record-replay-需求文档.md` 存在且内容完整
- **Evidence**: 文件存在性检查

## Open Questions
- [ ] 历史记录是否需要限制最大条数（如 1000 条）以避免内存无限增长？→ 本期默认不限制，由调用方管理。
