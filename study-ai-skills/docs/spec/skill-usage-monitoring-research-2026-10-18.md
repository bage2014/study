# Skill 使用监控方案调研报告

**报告编号:** RESEARCH-20261018-001  
**调研主题:** Skill 使用监控（使用量/报错/耗时/客户端）  
**调研范围:** 大厂方案 + 国外经典案例 + 轻量自建方案  
**创建日期:** 2026-10-18  
**版本:** v1.0

---

## 一、调研目标

当前项目有 29 个 Skill（5 个新增包装 + 24 个已有），需要监控：

| 指标 | 说明 |
|------|------|
| **使用量** | 每个 Skill 每天被调用次数、DAU/WAU |
| **报错** | Skill 加载/执行时是否有错误、错误类型 |
| **使用时间** | Skill 从加载到完成的耗时 P50/P95/P99 |
| **使用客户端** | 哪个 IDE/Agent/用户在使用 |
| **Skill 间调用链** | Skill A 是否触发 Skill B（requires 依赖） |

---

## 二、业界标杆方案

### 方案 A：GitHub Copilot Usage Metrics

| 项目 | 内容 |
|------|------|
| **来源** | GitHub Copilot Enterprise |
| **发布日期** | 2025/10 公开预览，2026/9 新增 Skill/MCP/Plugin 指标 |
| **适用场景** | IDE 内置能力的企业级用量监控 |
| **核心机制** | IDE 端 telemetry + 服务端聚合 + API/NDJSON 导出 |

**关键发现（2026/9 新增）：**

| 字段 | 类型 | 说明 |
|------|------|------|
| `totals_by_skill[]` | Array | 每个 skill 的 `interaction_count`（调用次数）+ 名称 |
| `distinct_skill_use_count` | Number | 使用过的不同 skill 数量 |
| `totals_by_custom_agent[]` | Array | 自定义 agent 启动次数 |
| `totals_by_mcp[]` | Array | MCP server 连接次数 |
| `totals_by_plugin[]` | Array | Plugin skill 调用次数 |
| `totals_by_slash_cmd[]` | Array | Slash command 调用次数 |

**指标维度：**

| 类别 | 指标 |
|------|------|
| Adoption | DAU/WAU/总活跃用户 |
| Engagement | 每活跃用户平均 chat 次数、Agent 采用率 |
| Acceptance | 代码建议接受率 |
| Language/Model | 编程语言分布、模型分布 |
| Skill 级别 | 每个 skill 的 invocation_count、distinct 使用数 |

**API 端点：**

```
GET /enterprise/{enterprise}/copilot/usage-metrics/reports
GET /orgs/{org}/copilot/usage-metrics/reports
GET /users/{user}/copilot/usage-metrics/reports
```

**优势：**
- 覆盖 Skill / MCP / Plugin 全链路
- 支持 enterprise / org / user 三级粒度
- NDJSON 原始数据可导出做自定义分析

**不足：**
- 只对 GitHub Copilot Enterprise 用户开放
- 数据有 3 天延迟（预览期）
- 客户自定义 skill 名称会被匿名化（grouped under `other`）

---

### 方案 B：Cursor Analytics API

| 项目 | 内容 |
|------|------|
| **来源** | Cursor Enterprise |
| **适用场景** | AI Coding Agent 的 team 级用量分析 |
| **核心机制** | IDE telemetry → Cursor Cloud → REST API |

**关键端点（直接对应我们的需求）：**

| 端点 | 返回内容 |
|------|----------|
| `GET /analytics/team/skills` | **每天每个 skill 的 adoption count，按 skill name 分组** |
| `GET /analytics/team/dau` | 日活，按 CLI / Cloud Agents / BugBot 分拆 |
| `GET /analytics/team/agent-edits` | Agent 接受的 diff 数量 + 行数 |
| `GET /analytics/team/tabs` | Tab 补全建议的接受率 |
| `GET /analytics/team/models` | 模型使用分布 |
| `GET /analytics/team/error-rate` | **Agent 错误率** |
| `GET /analytics/team/latency` | **响应延迟分布** |

**Skills API 响应示例：**

```json
{
  "data": [
    {
      "event_date": "2025-01-15",
      "skill_name": "test-driven-development",
      "total_invocations": 12,
      "unique_users": 3,
      "avg_duration_ms": 45000,
      "error_count": 1
    }
  ]
}
```

**优势：**
- 专门有 Skill 维度的 endpoint！
- 原生返回 `skill_name`、`invocation_count`、`unique_users`、`avg_duration_ms`
- 支持按用户过滤
- REST API 标准，易于集成

**不足：**
- 只对 Cursor Enterprise 开放
- 数据只在 Cursor Cloud 持久化
- 没有错误详情、调用链追踪

---

### 方案 C：JetBrains AI Activity and Impact

| 项目 | 内容 |
|------|------|
| **来源** | JetBrains IDE Services（Enterprise） |
| **适用场景** | IDE 内置 AI 功能效果评估 |
| **核心机制** | IDE telemetry + License & Activation 模块 → Analytics tab |

**覆盖范围：**

| 类别 | 具体工具 |
|------|----------|
| Chat-based | Junie (Code mode), Claude Agent, AI Assistant Chat |
| In-editor | Code completion, Next edit suggestions |

**核心指标：**

| 指标 | 说明 |
|------|------|
| AI code acceptance rate | AI 生成代码的接受率 |
| AI-modified code | AI 修改的代码量 |
| AI feature activity | 每个 AI 功能的使用频率 |
| 模型分布 | 使用的 AI 模型 |
| 语言分布 | 编程语言 |

**优势：**
- IDE 原生集成，无需额外部署
- 区分 Chat-based vs In-editor 两类 AI 交互
- 有行级别的代码生成统计

**不足：**
- 没有 Skill/Agent 级别的细分
- 只统计 JetBrains 自家 AI 功能
- 数据存在 JetBrains 云端，企业无法自建

---

## 三、国外经典案例

### 案例 1：LangSmith（LangChain）

| 项目 | 内容 |
|------|------|
| **来源** | LangChain |
| **开源/商用** | SaaS + AWS Marketplace + Self-hosted |
| **核心机制** | OpenTelemetry-based tracing |
| **适用场景** | Agent/LLM 应用的全链路 tracing |

**关键特性：**

| 特性 | 说明 |
|------|------|
| **Tracing** | 每个 agent step 的完整 trace（输入→工具调用→输出） |
| **@observe decorator** | 一行代码自动 trace 函数执行、捕获输入输出、耗时、错误 |
| **Dashboards** | 成本、延迟、质量实时面板 + alerts |
| **LLM-as-Judge Evals** | 自动评分 agent 输出质量 |
| **Dataset & Experiments** | 离线评估 + prompt 版本对比 |
| **LangSmith Engine** | 自动检测 recurring issues + 根因分析 |

**@observe 用法（跟我们的 Skill 调用对应）：**

```python
from langsmith import observe

@observe(name="skill-execution")
def execute_skill(skill_name: str, args: dict):
    # 自动捕获 skill_name, args, return, duration, errors
    result = run_skill(skill_name, args)
    return result
```

**Trace 可附加的 metadata（正好是我们要的指标）：**

```python
langfuse.update_current_trace(
    user_id="user_123",
    tags=["production", "skill-test-driven-development"],
    metadata={
        "skill_name": "common-test-driven-development",
        "client": "trae-macos",
        "duration_ms": 45000,
        "status": "success"
    }
)
```

**优势：**
- OpenTelemetry 标准，可对接任何 OTel collector
- 自动 context propagation（调用链追踪）
- 免费 tier 额度充足
- Self-hosted 支持（Docker Compose / Helm）

**不足：**
- 需要在 skill 执行层埋点（我们目前没有独立执行层）
- 对于 IDE 内置 Skill 工具，需要找到 hook 点

---

### 案例 2：Langfuse（开源）

| 项目 | 内容 |
|------|------|
| **来源** | Langfuse GmbH（德国柏林） |
| **开源/商用** | 开源 MIT 协议 + SaaS |
| **核心机制** | OpenTelemetry v4 SDK（2026/3 发布） |
| **GitHub Stars** | ~14k |

**关键特性：**

| 特性 | 说明 |
|------|------|
| **OpenTelemetry 后端** | `POST /api/public/otel` 接收 OTel traces |
| **@observe decorator** | 和 LangSmith 类似，自动 trace |
| **Monitors & Alerts** | 成本/质量/延迟监控 + Slack/webhook 通知 |
| **Scores API v3** | LLM-as-Judge + 自定义评分 |
| **Prompts Hub** | Prompt 版本管理 |
| **LLMS.txt** | AI agent 友好的 API 文档 |
| **MCP Prompt Server** | 通过 MCP 暴露 prompt 给 Claude Desktop/Cursor |

**Python SDK v4 最新用法：**

```python
from langfuse import get_client, observe

langfuse = get_client()  # 自动从环境变量加载 LANGFUSE_PUBLIC_KEY / LANGFUSE_SECRET_KEY

@observe(name="skill-common-test-driven-development")
def handle_skill_call(user_request: str):
    with langfuse.start_as_current_observation(as_type="span", name="red-phase"):
        # 执行 skill 的某个 phase
        pass
    # 整个函数自动被 trace
```

**MCP 层面监控（Coralogix 推荐的标准信号）：**

| 信号 | 类型 | OTel Semantic Convention |
|------|------|--------------------------|
| Tool call 次数 | Counter | `gen_ai.tool.name` |
| Tool call 延迟 | Histogram | `gen_ai.request.model` |
| Error rate | Counter | `error.type` |
| Session 时长 | Histogram | `gen_ai.session.id` |

**优势：**
- 完全开源 MIT，可自托管
- OpenTelemetry 标准，不绑定 LangChain
- SDK v4 是 OTel 原生，不是自建协议
- 免费 tier：1M traces / 月

**不足：**
- 需要自建采集层（OTel collector 或直接 SDK）
- 社区相对 LangSmith 小一些

---

### 案例 3：OpenTelemetry + Prometheus + Grafana（MCP Server 监控）

| 项目 | 内容 |
|------|------|
| **来源** | CNCF 开源（行业标准） |
| **适用场景** | MCP Server / Tool 的 Prometheus 指标暴露 |
| **参考实现** | MCP Server Spot / Elastic APM / Sentry MCP Integration |

**Coralogix 推荐的 MCP/AI Tool 监控信号：**

| 指标名 | 类型 | 标签 | 说明 |
|--------|------|------|------|
| `mcp_tool_calls_total` | Counter | tool_name, status(success/error), client | Tool 调用次数 |
| `mcp_tool_call_duration_seconds` | Histogram | tool_name | Tool 调用延迟 P50/P95/P99 |
| `mcp_errors_total` | Counter | error_type | 协议层错误数 |
| `mcp_active_sessions` | Gauge | client | 当前活跃 session 数 |
| `mcp_session_duration_seconds` | Histogram | client | Session 持续时间 |

**Sentry MCP Integration（一行代码）：**

```javascript
// Node.js
import * as Sentry from "@sentry/node";
Sentry.init({ dsn: "..." });
const server = Sentry.wrapMcpServerWithSentry(new McpServer({ name: "my-mcp" }));
// 自动追踪所有 tool call + input/output + latency + errors

// Python
import sentry_sdk
from sentry_sdk.integrations.mcp import MCPIntegration
sentry_sdk.init(dsn="...", integrations=[MCPIntegration()])
```

**PromQL 查询示例：**

```promql
# 每个 skill 的调用频率
sum(rate(mcp_tool_calls_total{tool_name=~"skill-.*"}[5m])) by (tool_name)

# 最常出错的 skill
sum(rate(mcp_tool_calls_total{status="error"}[5m])) by (tool_name)
/
sum(rate(mcp_tool_calls_total[5m])) by (tool_name)

# Skill 延迟 P99
histogram_quantile(0.99, sum(rate(mcp_tool_call_duration_seconds_bucket[5m])) by (le, tool_name))
```

**优势：**
- 行业标准，CNCF 毕业项目
- Prometheus + Grafana 生态成熟，查询灵活
- OTel semantic convention 统一命名
- 可对接任何后端（Elastic/Datadog/Langfuse/LangSmith）

**不足：**
- 需要自己实现埋点（对于 IDE 内置 Skill 工具）
- 基础设施较重（Prometheus + Grafana + OTel Collector）

---

## 四、针对我们场景的方案分析

### 4.1 我们的独特性

| 维度 | 大厂/经典方案 | 我们的情况 |
|------|--------------|-----------|
| Skill 在哪里 | IDE 内置（Copilot/Cursor/JetBrains）或 Agent 框架内（LangChain） | **SKILL.md 文件**，本地定义，通过 Trae Skill 工具加载 |
| 执行载体 | IDE 进程 / Agent 进程 | **没有独立执行进程**——Skill 是 Agent 对话中的提示指导 |
| 埋点位置 | IDE telemetry SDK / Agent 框架 hook | **未知**——需要找到 Skill 加载的 hook 点 |
| 部署规模 | 企业级，数十/数百开发者 | **单用户/小团队** |
| 核心场景 | 团队 AI 采纳率分析 | **Skill 使用洞察 + 健康检查** |

### 4.2 可行的 5 个方案

#### 方案 1：轻量本地 JSONL + 定时上报（⭐ 推荐入门）

| 项目 | 内容 |
|------|------|
| **思路** | 在 Skill 加载时写一行 JSON 到本地 `~/.trae-cn/skill-usage.jsonl`，每天定时用 curl 上传 |
| **埋点位置** | 在我们的 **Skill 工具**调用处（Agent 主动加载 skill 时）+ **validate-skills.sh 增强** |
| **存储** | 本地 JSONL 文件 + 远程 Langfuse/LangSmith endpoint |
| **部署复杂度** | ⭐ 低（1 个脚本） |
| **数据示例** | `{"ts":"2026-10-18T10:00:00Z","skill":"common-tdd","status":"loaded","duration_ms":150,"client":"trae-macos"}` |

**优点：** 零依赖、零成本、立即可做  
**缺点：** 只能捕获 Agent 主动调用 Skill 工具的场景，自动触发（auto-trigger）的 skill 无法记录

---

#### 方案 2：Langfuse 自托管（⭐ 推荐进阶）

| 项目 | 内容 |
|------|------|
| **思路** | Docker Compose 跑 Langfuse，@observe 装饰器包裹 skill 执行函数 |
| **埋点位置** | 如果能找到 Trae/Solo 的 skill 执行 hook（`tools.Skill` 的内部实现） |
| **存储** | Langfuse PostgreSQL + ClickHouse |
| **部署复杂度** | ⭐⭐ 中（Docker Compose 一键） |
| **可视化** | Langfuse 自带 Dashboard + Metrics API |

**优点：** 开源自托管、OTel 标准、Trace 完整、有 Evals 功能  
**缺点：** 依赖 Trae 内部 hook 点的可访问性

---

#### 方案 3：IDE Telemetry（如果 Trae 有）

| 项目 | 内容 |
|------|------|
| **思路** | 检查 Trae/Solo 是否有类似 Copilot/Cursor 的 telemetry API |
| **可行性** | ❓ 未知——需要搜索 Trae 官方文档 |
| **优势** | 零埋点、IDE 原生数据 |

**建议：** 在深入自建前，先确认 Trae 是否有内置 telemetry

---

#### 方案 4：OpenTelemetry + Prometheus + Grafana

| 项目 | 内容 |
|------|------|
| **思路** | Prometheus HTTP endpoint 暴露 skill 使用指标，Grafana Dashboard 可视化 |
| **部署复杂度** | ⭐⭐⭐ 较高（Prometheus + Grafana + OTel Collector） |
| **指标格式** | 遵循 OTel semantic convention：`gen_ai.tool.name="skill-xxx"` |

**优点：** 行业标准、查询灵活、Grafana 生态成熟  
**缺点：** 对于单用户/小团队来说太重了

---

#### 方案 5：MCP Server 包装（把 Skill 调用转成 MCP tool call）

| 项目 | 内容 |
|------|------|
| **思路** | 写一个 MCP server，把每个 skill 注册成 tool，skill 调用变成 MCP tool call |
| **监控** | MCP 层面用 Sentry / Coralogix / OTel |
| **可行性** | 需要 Trae 支持 MCP 注册自定义 server |

**优点：** 完美适配 MCP 监控生态、Sentry 一行集成  
**缺点：** 架构侵入大、skill 定义方式要变

---

## 五、方案对比矩阵

| 评估维度 | 方案 1<br>本地 JSONL | 方案 2<br>Langfuse 自托管 | 方案 3<br>IDE Telemetry | 方案 4<br>OTel+Prom+Grafana | 方案 5<br>MCP 包装 |
|----------|:---:|:---:|:---:|:---:|:---:|
| **功能匹配度** | ★★★ | ★★★★★ | ★★★★★ | ★★★★ | ★★★★ |
| 覆盖使用量 | ✅ | ✅ | ✅ | ✅ | ✅ |
| 覆盖报错 | ⚠️ 部分 | ✅ | ✅ | ✅ | ✅ |
| 覆盖耗时 | ✅ | ✅ | ✅ | ✅ | ✅ |
| 覆盖客户端 | ⚠️ 固定 | ✅ | ✅ | ⚠️ | ✅ |
| Skill 调用链 | ❌ | ✅ | ⚠️ | ✅ | ✅ |
| 自动触发 skill | ❌ 需 hook | ✅ 需 hook | ✅ IDE 原生 | ✅ 需 hook | ✅ 需改造 |
| **性能** | 无影响 | 低 | IDE 内置 | 低 | 中 |
| **可靠性** | 高（本地文件） | 高 | IDE 原生 | 高 | 中 |
| **成本** | 零 | 零（自托管） | 零 | 零 | 零 |
| **复杂度** | ⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| **生态支持** | 弱 | 强（LangChain/LlamaIndex/OTel） | Trae 生态待查 | CNCF 毕业 | 中（MCP 生态） |
| **扩展性** | 低 | 高 | 取决于 IDE | 极高 | 高 |

---

## 六、推荐方案

### 推荐路径：分阶段实施

#### Phase 1：立即做 — 方案 1（本地 JSONL）

**做什么：**
1. 增强 `validate-skills.sh`，每次验证时记录每个 skill 的最后加载时间 + 状态到 `skill-usage.jsonl`
2. 创建一个轻量 Python 脚本 `skill-monitor.py`：当 Skill 工具被调用时（如果能 hook）或定期扫描，追加 JSONL
3. 每个 JSONL 记录：`{ts, skill_name, status, duration_ms, client, error}`

**产物：** 一个 100 行以内的 Python 脚本 + 本地文件

**验收标准：** 每天能看到一个 JSONL 文件，里面有 skill 使用记录

---

#### Phase 2：进阶 — 方案 2（Langfuse 自托管）

**做什么：**
1. Docker Compose 起 Langfuse（`docker run langfuse/langfuse:latest`）
2. 在 Phase 1 脚本基础上，把 JSONL 同步到 Langfuse OTel endpoint
3. 配置 Langfuse Dashboard：skill usage by day / error rate / latency P95

**产物：** Langfuse 实例 + Dashboard 截图

**验收标准：** 打开 Langfuse 能看到每个 skill 的 trace

---

#### Phase 3：如果 Trae 支持 — 方案 3（IDE Telemetry）

**做什么：** 搜索 Trae 官方文档，看是否有 telemetry API 或配置

**如果有：** 直接用，前两个方案当补充

**如果没有：** 继续 Phase 2

---

## 七、关键决策点

在开始实施前需要确认：

| # | 问题 | 影响 |
|---|------|------|
| 1 | **Trae/Solo 框架是否能 hook 到 Skill 的加载事件？** | 决定能不能捕获自动触发的 skill |
| 2 | **Trae 有没有内置 telemetry？** | 如果有，Phase 1/2 都不需要 |
| 3 | **用户接受自托管 Langfuse 吗？** | 如果接受，Phase 2 直接上 |
| 4 | **监控目标是自用还是团队共享？** | 自用 → Phase 1 就够；团队 → Phase 2 |

---

## 八、参考资料

| # | 来源 | URL |
|---|------|-----|
| 1 | GitHub Copilot Usage Metrics (2026/9 Skill/MCP 更新) | https://github.blog/changelog/2026-09-17-agentic-cli-customizations-now-in-the-usage-metrics-api/ |
| 2 | Cursor Analytics API - Skills Endpoint | https://cursor.com/docs/account/teams/analytics-api |
| 3 | JetBrains AI Activity and Impact | https://www.jetbrains.com/help/ide-services/2025.6/ai-activity-and-impact.html |
| 4 | LangSmith Observability | https://docs.langchain.com/langsmith/observability |
| 5 | Langfuse Python SDK v4 (OpenTelemetry) | https://langfuse.com/docs/observability/sdk/overview |
| 6 | Coralogix MCP Monitoring Guide | https://coralogix.com/guides/mcp-monitoring/ |
| 7 | MCP Server Spot - Prometheus/Grafana | https://www.mcpserverspot.com/learn/building/mcp-monitoring-observability |
| 8 | Sentry MCP Integration | https://sentry.io/cookbook/monitor-mcp-server/ |
| 9 | Elastic APM + OTel + MCP | https://www.elastic.co/observability-labs/blog/mcp-tracing-opentelemetry-elastic-apm |
| 10 | 11 Observability Platforms for AI Coding Assistants | https://www.augmentcode.com/guides/11-observability-platforms-for-ai-coding-assistants |

---

## 九、与 Trae Skill 定义的衔接

当前项目中 Skill 定义的相关文件：

| 文件 | 路径 | 作用 |
|------|------|------|
| validate-skills.sh | 仓库根目录 | 格式验证脚本，可扩展加埋点 |
| SKILLS.md | 仓库根目录 | Skill 列表文档 |
| SKILL.md | 每个 skill 目录 | 定义文件（frontmatter 有 name） |
| ~/.trae-cn/skills/ | 安装目录 | 运行时 skill 位置 |

**最佳埋点位置建议：**
1. `validate-skills.sh` — 每次验证时记录加载时间和状态（Phase 1）
2. 自定义 Skill 加载 hook（如果 Trae 支持）— 在 Skill 工具调用前后埋点（完整方案）
3. Skill frontmatter 可加 `skill-id` 字段（可选，已有 `name`）
