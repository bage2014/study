# Skill Monitor Dashboard 竞品分析与功能规划

> 日期：2026-10-22
> 目标：分析 AI Agent/Skill 监控领域的竞品，识别功能差距，规划后续迭代方向

---

## 1. 市场格局

AI Agent 监控工具分为四大类：

| 类别 | 代表产品 | 特点 |
|------|----------|------|
| LLM 原生可观测平台 | LangSmith, Langfuse, Braintrust, Opik | 专为 AI 设计，支持 trace 树、session replay |
| AI 网关 + 可观测 | Helicone, Portkey, LiteLLM | 代理层监控，成本追踪，预算控制 |
| Agent 专属监控 | AgentOps | Session replay 为核心卖点，"Agent 的 DVR" |
| 传统 APM 扩展 | Datadog, Dynatrace, Elastic, Arize Phoenix | 在现有 APM 基础上增加 LLM/Agent 模块 |

**我们的定位**：自托管、轻量级 Skill 使用追踪器（Spring Boot + H2 + Thymeleaf），最接近 Langfuse/Arize Phoenix 的自托管模式，但范围更聚焦。

---

## 2. 竞品核心功能对比

### 2.1 追踪与可观测性

| 功能 | 竞品 | 我们 | 优先级 |
|------|------|------|--------|
| 完整链路 Trace 树（层级 span） | LangSmith, Langfuse |  扁平事件 | 中 |
| Session Replay（会话回放） | AgentOps（核心卖点） | ❌ | 中 |
| OpenTelemetry 原生支持 | Arize Phoenix, Langfuse | ❌ | 低 |
| 语义 Trace 搜索 | LangSmith（2026.10 新功能） | ❌ | 低 |

**现状**：我们捕获扁平的 skill-usage 事件（tool_load, script_log, validation），包含 skill 名称、触发类型、用户、项目、耗时、状态。基础扎实但缺乏 trace 层级和回放能力。

### 2.2 分析与仪表盘

| 功能 | 竞品 | 我们 | 优先级 |
|------|------|------|--------|
| 时间序列趋势图 | 全部 | ✅ 每日趋势 | - |
| Top-N 排行榜 | Langfuse, Helicone | ✅ Top Skill/Project/User | - |
| 多维筛选（用户/项目/时间） | 全部 | ✅ | - |
| 触发/分布饼图 | 全部 | ✅ trigger-dist, client-dist | - |
| **热力图（时段分布）** | LangSmith, Helicone | ❌ | **高** |
| **Session 级聚合** | AgentOps, LangSmith | ❌ | **高** |
| 对比视图（本周 vs 上周） | Braintrust, LangSmith | ❌ | 中 |
| 导出功能（CSV/JSON） | 部分 |  | 低 |

**现状**：已具备核心分析功能（总览、Top-N、趋势、分布、最近记录）。主要差距在热力图和 Session 聚合。

### 2.3 成本与 Token 分析

| 功能 | 竞品 | 我们 | 优先级 |
|------|------|------|--------|
| 单次调用成本估算 | Helicone, Langfuse, AgentOps | ❌ | 低（不适用） |
| 聚合支出仪表盘 | LiteLLM, Portkey | ❌ | 低 |
| 预算上限与告警 | LiteLLM, Portkey | ❌ | 低 |
| Token 细分（输入/输出） | 全部 | ❌ | 低 |

**相关性**：较低。我们追踪的是 Skill 加载/触发，不是 LLM API 调用。若未来需要追踪 Skill 触发的下游 LLM 成本，可扩展此方向。

### 2.4 调试与错误分析

| 功能 | 竞品 | 我们 | 优先级 |
|------|------|------|--------|
| 错误率追踪（按 Skill/天） | 全部 | ❌ | **高** |
| 错误下钻（查看完整上下文） | LangSmith, AgentOps | ❌ | **高** |
| 状态分布图 | 部分 | ✅ 基础（status 字段） | - |
| **延迟百分位（P50/P95/P99）** | 全部 | ❌ | **高** |
| 时间旅行调试 | AgentOps | ❌ | 低 |
| AI 辅助调试（语义搜索） | LangSmith |  | 低 |

**现状**：有 `status`（ok/error）和 `durationMs` 字段，以及最近记录视图。基础错误追踪存在，但缺乏错误率分析、延迟百分位、错误详情视图。

### 2.5 告警与通知

| 功能 | 竞品 | 我们 | 优先级 |
|------|------|------|--------|
| 阈值告警（错误率/延迟/成本） | Datadog, Langfuse | ❌ | **高** |
| 异常检测 | Datadog, Arize | ❌ | 中 |
| 告警渠道（Slack/邮件/Webhook） | 全部 | ❌ | 中 |
| 按指标分规则 | 部分 | ❌ | 中 |

**现状**：无告警。Dashboard 是被动的——必须主动查看才能发现问题。这是最大的功能差距。

### 2.6 评估与质量

| 功能 | 竞品 | 我们 | 优先级 |
|------|------|------|--------|
| LLM-as-Judge 自动评分 | Braintrust, Langfuse, Opik | ❌ | 低 |
| 人工标注 | 部分 | ❌ | 低 |
| 回归测试 | Braintrust, LangSmith | ❌ | 低 |
| A/B 测试 | Braintrust, LangSmith | ❌ | 低 |

**相关性**：较低。我们追踪 Skill 使用而非 Skill 质量。若想衡量"加载此 Skill 是否导致成功结果"，可添加简单的 outcome/status 字段。

### 2.7 Skill 管理

| 功能 | 竞品 | 我们 | 优先级 |
|------|------|------|--------|
| 版本管理 | Langfuse | ❌ | 中 |
| 部署管理（staging → prod） | LangSmith + LangGraph |  | 低 |
| Skill 配置管理 | 部分 | ❌ | 低 |

**现状**：追踪 Skill 名称和事件，但不追踪 Skill 版本或配置。添加 `skillVersion` 字段可关联 Skill 更新与使用/质量变化。

---

## 3. 功能优先级矩阵

基于**价值/投入比**排序：

| 优先级 | 功能 | 投入 | 价值 | 竞品覆盖 |
|--------|------|------|------|----------|
| **P0** | 错误率分析（按 Skill、按天） | 低 | 高 | 全部 |
| **P0** | 延迟百分位（P50/P95/P99） | 低 | 高 | 全部 |
| **P0** | Session 级聚合（按 sessionId 分组） | 低 | 高 | AgentOps, LangSmith |
| **P1** | 时段热力图（Time-of-day Heatmap） | 中 | 中 | LangSmith, Helicone |
| **P1** | 简单告警（阈值触发） | 中 | 高 | Datadog, Langfuse |
| **P1** | Skill 版本追踪 | 低 | 中 | Langfuse |
| **P2** | 对比视图（本周 vs 上周） | 中 | 中 | Braintrust, LangSmith |
| **P2** | 异常检测 | 中 | 高 | Datadog, Arize |
| **P3** | 导出功能（CSV/JSON） | 低 | 低 | 部分 |
| **P3** | 完整 Trace 瀑布图 | 高 | 中 | LangSmith, Langfuse |
| **P3** | LLM-as-Judge 评估 | 高 | 低 | Braintrust, Opik |

---

## 4. 关键发现

### 4.1 我们已具备的核心能力
- ✅ 总览统计（总调用、唯一 Skill/用户/项目、错误率）
- ✅ Top-N 排行榜（Skill、项目、用户）
- ✅ 每日趋势图（跟随时间选择器）
- ✅ 触发方式分布（自动/手动）
- ✅ 客户端分布
- ✅ 最近记录（平铺 + 按用户分组）
- ✅ 多维筛选（用户、项目、时间范围）
- ✅ 系统状态与运维面板

### 4.2 最大功能差距

1. **错误分析薄弱**：有 status 字段但无错误率趋势图、无错误下钻视图
2. **延迟分析缺失**：有 durationMs 但无 P50/P95/P99 百分位统计
3. **无 Session 聚合**：有 sessionId 字段但未利用，无法查看单次会话的完整行为
4. **无告警机制**：完全被动监控，无法主动发现问题
5. **无热力图**：无法识别使用时段模式（工作时间 vs 非工作时间）

### 4.3 市场趋势

1. **Session Replay 是差异化功能**：AgentOps 的核心卖点，对调试 Agent 行为极有价值
2. **告警是必备能力**：所有严肃工具都有，即使简单的"错误率超阈值"告警也能将 Dashboard 从被动变为主动
3. **自托管 + 轻量级是有效 niche**：Langfuse 和 Arize Phoenix 证明自托管开源工具有强劲需求。我们的优势是简单（单 JAR、H2、无外部依赖）
4. **市场正在融合**：所有工具都在添加评估、追踪、监控。差异化转向 AI 辅助调试（trace 语义搜索）和自动化质量评估

---

## 5. 建议的迭代路线

### Phase 1：基础分析增强（1-2 周）
- [ ] 错误率趋势图（按 Skill、按天）
- [ ] 延迟百分位统计（P50/P95/P99）
- [ ] Session 级聚合视图（按 sessionId 分组显示事件时间线）

### Phase 2：可视化增强（2-3 周）
- [ ] 时段热力图（小时 × 星期）
- [ ] 对比视图（本周 vs 上周、本月 vs 上月）
- [ ] Skill 版本追踪（添加 skillVersion 字段）

### Phase 3：主动监控（3-4 周）
- [ ] 简单阈值告警（错误率 > X%、延迟 > Y ms）
- [ ] 告警渠道集成（邮件/Webhook）
- [ ] 异常检测（基于历史基线）

### Phase 4：高级功能（按需）
- [ ] 导出功能（CSV/JSON）
- [ ] 完整 Trace 瀑布图（需要重构数据模型）
- [ ] LLM-as-Judge 评估（需要集成外部 LLM）

---

## 6. 技术可行性评估

### 6.1 数据模型扩展

当前 `SkillUsage` 实体已包含：
- `ts`（时间戳）
- `skillName`（Skill 名称）
- `eventType`（事件类型）
- `client`（客户端）
- `sessionId`（会话 ID）← **未充分利用**
- `durationMs`（耗时）← **未充分分析**
- `status`（状态）← **未充分分析**
- `triggerType`（触发类型）
- `userId`（用户）
- `projectName`（项目）

**建议新增字段**：
- `skillVersion`（Skill 版本）← Phase 2
- `errorMessage`（错误详情）← Phase 1
- `tokensUsed`（Token 数）← 可选，若需要成本追踪

### 6.2 后端扩展点

1. **错误率分析**：新增 `/api/skill-usage/error-trend` 接口，按天聚合错误数/总数
2. **延迟百分位**：新增 `/api/skill-usage/latency-stats` 接口，计算 P50/P95/P99
3. **Session 聚合**：新增 `/api/skill-usage/session/{sessionId}` 接口，返回会话事件列表
4. **热力图**：新增 `/api/skill-usage/hourly-heatmap` 接口，按小时 × 星期聚合

### 6.3 前端扩展点

1. **错误率图表**：在趋势图下方添加错误率折线图
2. **延迟统计卡片**：在总览卡片中添加 P50/P95/P99 延迟卡片
3. **Session 视图**：新增 Session 详情面板，显示会话事件时间线
4. **热力图组件**：使用 CSS Grid 实现 7×24 热力图

---

## 7. 结论

我们的 Dashboard 已具备**核心分析能力**（Top-N、趋势、分布），在轻量级自托管方案中处于合理水平。主要差距在**错误分析**、**延迟分析**、**Session 聚合**和**告警**四个方面。

**建议优先投入 Phase 1**（错误率、延迟百分位、Session 聚合），这三个功能投入低、价值高，且可充分利用现有数据模型。Phase 2 的热力图和对比视图可进一步提升分析深度。Phase 3 的告警功能是将 Dashboard 从"被动查看"升级为"主动监控"的关键。

**不建议投入**：成本追踪（不适用 Skill 加载场景）、完整 Trace 瀑布图（投入高、收益低）、LLM-as-Judge（超出当前范围）。

---

## 附录：竞品参考链接

- [LangSmith](https://smith.langchain.com/) - LangChain 官方监控平台
- [Langfuse](https://langfuse.com/) - 开源 LLM 可观测平台
- [AgentOps](https://www.agentops.ai/) - Agent Session Replay 先驱
- [Helicone](https://www.helicone.ai/) - 开源 LLM 网关 + 监控
- [Portkey](https://portkey.ai/) - AI 网关 + 可观测
- [Arize Phoenix](https://phoenix.arize.com/) - 开源 LLM 可观测（支持自托管）
- [Braintrust](https://www.braintrust.dev/) - LLM 评估与监控
- [Opik](https://www.comet.com/site/products/opik/) - Comet 的开源 LLM 评估工具
