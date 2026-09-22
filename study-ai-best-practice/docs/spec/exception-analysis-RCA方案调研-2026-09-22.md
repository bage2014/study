# 方案调研报告：RCA 根因分析逻辑优化

## 调研信息

| 项目 | 内容 |
|------|------|
| 报告编号 | RESEARCH-20260922-01 |
| 调研主题 | RCA（Root Cause Analysis）根因分析逻辑方案调研与当前项目优化 |
| 调研范围 | study-ai-best-practice-exception-analysis 模块的 RCA 分析内核 |
| 创建日期 | 2026-09-22 |
| 版本 | v1.0 |

## 一、调研目标

1. 盘点当前项目 RCA 实现的真实链路与设计缺陷（基于代码事实，不凭印象）。
2. 对标至少 3 家互联网大厂（实际覆盖 5 家：微软、阿里、字节、腾讯、Google）的 RCA 工程范式。
3. 建立统一评估维度打分对比，给出推荐架构。
4. 输出可分阶段落地的当前项目优化思路，精确到代码落点。

---

## 二、当前项目实现现状（代码事实）

核心入口：[AnalysisServiceImpl.java](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/service/impl/AnalysisServiceImpl.java)

当前是**硬编码线性 6 步管道 + 8 假设 if-else 首中即返回**：

```
采集4类上下文 → 模块名匹配 → 拓扑查询 → MQ/JOB指标 → 证据罗列 → 固定顺序假设匹配 → 静态建议模板
```

### 2.1 关键缺陷清单

| # | 缺陷 | 代码位置 | 后果 |
|---|------|----------|------|
| D1 | **单链首中即裁决**：8 个假设按固定顺序 if-else，多因素并存（MQ积压+CPU高+刚发布）永远只报第一个 | [analyzeRootCause L388-493](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/service/impl/AnalysisServiceImpl.java#L388-L493) | 无多假设并行评估、无候选排序，优先级即结论 |
| D2 | **置信度为写死常量字符串**（"90%"/"85%"/"80%"…） | L403-492 | 不随证据数量/强度/时效变化，跨假设不可比 |
| D3 | **证据"采集即罗列"，无证据→假设裁决**：Evidence 只有 source/content/relevance 字符串 | [AnalysisResponse.java L114-151](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/dto/response/AnalysisResponse.java#L114-L151) | 无 SUPPORT/REFUTE 方向，无证据聚合，relevance 靠阈值拍标签 |
| D4 | **无因果传播链**：TopologyInfo 已有 modules + MQ/JOB/DB/外部依赖 + 上下游，仅用于 MQ 名称精确匹配 | [isMqRelatedToAlarmModule L506-528](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/service/impl/AnalysisServiceImpl.java#L506-L528) | 只报现象（"MQ积压"），不回答"谁导致积压、如何传播到告警模块、谁是源头谁是受害方" |
| D5 | **无时间相关性计算**：DeploymentRecord 有 deployTime 字段，但发布假设只判断 list 非空 | L444-452；[DeploymentRecord.java](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/context/DeploymentRecord.java) | 一周前的发布也会被判为根因；指标用绝对阈值（错误率>10%），无动态基线 |
| D6 | **两套体系割裂**：AnalysisServiceImpl（规则链）与 PlanService/ExecuteService（7 步静态计划 + MCP 工具）互不调用 | [PlanService.java](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/service/PlanService.java)、[ExecuteService.java L50-106](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/service/ExecuteService.java#L50-L106) | Log/Trace/GitLab/业务知识 4 个 MCP 工具完全没进 RCA 主链路；工具参数硬编码（user-service、固定日期、traceId=1234567890） |
| D7 | **干扰项用 Random 伪造** | [selectDisturbanceModules L344-348](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/service/impl/AnalysisServiceImpl.java#L344-L348) | 不可复现、不可测试；真实噪声过滤能力不存在 |
| D8 | **建议静态模板**，与证据无结构化绑定 | [generateSuggestions L530-605](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/src/main/java/com/bage/study/ai/best/practice/exception/analysis/service/impl/AnalysisServiceImpl.java#L530-L605) | 无"止血/根治/预防"分层，无法追溯建议依据 |
| D9 | **无反馈学习闭环**：adjustPlan 仅关键字 contains 匹配 | PlanService L62-82 | 历史案例无沉淀、无检索 |
| D10 | **RCA 核心链路零 LLM**：本项目定位"AI 最佳实践"，hello 模块已有 Spring AI 能力 | — | 分析内核与项目定位不符 |

**一句话定性**：当前实现是一个"教学演示级的专家规则链"——确定性、可解释是优点，但裁决模型（首中即返回）、置信模型（常量）、证据模型（无方向）、因果模型（无传播）、时间模型（无窗口）五个核心子模型全部缺失。

---

## 三、互联网大厂实现方案调研（5 家 + 1 个学术范式）

### 方案 A：微软 RCACopilot —— 预案（Handler/TSG）驱动 + 历史案例 RAG + LLM 分类解释

**来源**：Microsoft，EuroSys 2024 论文《Automatic Root Cause Analysis via Large Language Models for Cloud Incidents》，基于微软一整年真实生产事件评估；诊断信息采集组件已在微软生产运行 4 年以上。

**架构链路**：

```
告警进入 → 按 alert type 匹配 Incident Handler（人工沉淀的 TSG 排障手册，含预定义动作集）
        → Handler 自动聚合多源运行时诊断信息（logs/metrics/deployment/服务配置）
        → Embedding 近邻检索历史相似事件
        → LLM 预测根因类别 + 生成解释性叙述
```

**关键事实**：
- RCA 准确率最高 0.766；后续 GPT-4 In-Context Learning 在 10 万生产事件上比微调 GPT-3 平均提升 24.8%，比 zero-shot 提升 49.7%，人工评估正确性 +43.5%。
- 产品化形态（Azure Copilot Troubleshooting Agent）固化为五阶段：**Trigger → Scope → Diagnose → Resolve → Escalate**；找不到确定根因时降级为自助内容，并携带完整调查上下文升级人工。
- 论文明确洞察：**单一数据源不足以定根因，必须多源交叉印证**。

| 项目 | 评价 |
|------|------|
| 核心优势 | 确定性采集 + LLM 仅做分类/叙述，幻觉可控；RAG 复用历史案例；工程验证最久 |
| 主要不足 | Handler 需按告警类型人工维护，未覆盖类型效果差；根因输出是"类别"而非因果链；单轮总结式，无自由探索 |
| 复杂度 | 中（Handler 体系建设是主要成本） |
| 适用场景 | 告警类型收敛、排障手册成熟的团队 |

参考：<https://arxiv.org/html/2305.15778v4>、<https://www.microsoft.com/en-us/research/publication/automated-root-causing-of-cloud-incidents-using-in-context-learning-with-gpt-4/>

---

### 方案 B：阿里 RCAgent —— 工具增强自主 Agent（ReAct 双智能体）+ 轨迹自洽投票

**来源**：阿里巴巴（清华/南大合作），论文《RCAgent: Cloud Root Cause Analysis by Autonomous Agents with Tool-Augmented LLMs》；已落地阿里云 Apache Flink 实时计算平台诊断流程，本地部署模型（非 GPT）运行，隐私友好。

**架构链路**：

```
Controller Agent（ReAct: Think-Act-Observe 自由决策调什么工具）
   ├── 循环规则模块 + 任务指令模块 + 工具文档模块
   ├── Expert Agents（LLM 化的领域专用工具，补领域知识）
   ├── OBSK 观察快照键（长观察压缩成键，按需回溯，省 token）
   ├── 稳定性策略（动作失败重试/纠错）
   ├── TSC 轨迹自洽聚合（多条采样轨迹投票，再 LLM 汇总）
   └── finalize 工具（模型自主决定何时收口输出）
```

**关键事实**：
- 根因/解决方案/证据/责任归属四项任务全面优于原生 ReAct（根因 METEOR +8.71、方案 +6.52），规则已覆盖与未覆盖场景均优。
- 2025 云监控 2.0 演进：**UModel 统一拓扑** + SLS 统一可观测数据底座（logs/metrics/traces/events/profiles）解决数据孤岛，再叠加 Agentic 能力——先治数据，再治智能。

| 项目 | 评价 |
|------|------|
| 核心优势 | 自由数据收集，能处理规则未覆盖的新颖故障；轨迹投票显著降随机性；本地模型可跑 |
| 主要不足 | token 成本与延迟高；无 SOP 约束时 ReAct 易跑偏（字节实测原生 ReAct 仅 35.50% 准确率）；工程约束多 |
| 复杂度 | 高 |
| 适用场景 | 故障模式开放、工具生态丰富、有模型推理资源的团队 |

参考：<https://arxiv.org/pdf/2310.16340v1>、<https://www.alibabacloud.com/blog/apsara-conference-insights-rebuild-observability---craft-a-large-model-driven-cloud-monitor-2-0-and-a-new-aiops-paradigm_602694>

---

### 方案 C：字节 Flow-of-Action —— SOP 约束的多 Agent（确定性骨架 + LLM 节点执行）

**来源**：ByteDance（与中科院/清华合作），WWW 2025 论文《Flow-of-Action: SOP Enhanced LLM-Based Multi-Agent System for Root Cause Analysis》；生产侧为字节 SRE Agent 观测平台（2025 aCon 公开架构）。

**架构链路**：

```
SOP Flow（核心）：
  事件 → 检索匹配 SOP（无则自动生成 SOP）→ SOP 转代码 → SOP 在关键节点约束 LLM 执行
辅助 Agent：去噪 Agent（剔除无关线索）/ 搜索空间收敛 Agent / 停止判定 Agent
产品分层（生产）：
  Tools（low/mid/high level tool service，APM 能力即 MCP Tools）
  → Workflow（DAG / CodeAct 流程编排）
  → Agent Studio（Plan Agent、反思 Agent、指标/trace/LogID 专家 Agent、Agent-as-Tool、多层级 Supervisor）
  + 短-中-长记忆 + 知识管理 + AgentPattern 学习
```

**关键事实**：
- **准确率 64.01%，原生 ReAct 仅 35.50%**——接近翻倍，核心增益来自 SOP 在关键 juncture 对 LM 的约束。
- 思想本质：高频故障的诊断路径固化为代码（确定性、可测试、快、省 token），LLM 只在节点做判断；新颖故障才让 LLM 动态生成 SOP。

| 项目 | 评价 |
|------|------|
| 核心优势 | 确定性与灵活性平衡最好；SOP 可测试可复现；准确率提升有公开数据支撑；分层产品架构清晰 |
| 主要不足 | SOP 库建设与维护成本；跨业务 SOP 泛化；停止判定仍需调优 |
| 复杂度 | 中（可从少量高频 SOP 起步） |
| 适用场景 | 有稳定高频故障模式、追求准确率与成本平衡的团队（最贴合本项目） |

参考：<https://ar5iv.labs.arxiv.org/html/2502.08224>

---

### 方案 D：腾讯 TCOP SRE Agent —— 因果拓扑两阶段下钻 + Multi-Agent 分工 + MCP 生态

**来源**：腾讯云可观测平台 TCOP，2025 年发布（官方称国内首个 SRE Agent），200+ 云产品 MCP 标准化集成。

**架构链路**：

```
三类 Agent 分工：
  数据智能体（指标/链路/用户画像取数）
  专家智能体（问答/预测/诊断/FinOps）
  流程智能体（报告/配置执行/告警协同）
RCA 两阶段：
  ① 初因判断：基于因果关系拓扑定位异常起点（5 分钟内）
  ② 深度下钻：从起点继续钻到具体 SQL / 日志等深度根因
双模驱动：探索模式（AI 自主）+ 规划模式（人机协同）
知识双飞轮：腾讯专家知识库 + 企业私有知识库，边用边沉淀
自治分级：L1 推理 → L2 反思 → L3 规划 → L4 自主触发（Background Agent）
```

| 项目 | 评价 |
|------|------|
| 核心优势 | 因果图区分"异常源头 vs 级联受害方"；两阶段平衡速度与深度；MCP 工具生态标准化；知识库飞轮 |
| 主要不足 | 强依赖高质量统一拓扑与数据标准；两阶段切换门槛需调优 |
| 复杂度 | 中高 |
| 适用场景 | 拓扑数据完备、追求"分钟级下钻到代码/SQL"的平台型团队 |

参考：<https://developer.cloud.tencent.com.cn/article/2655070>

---

### 方案 E：Google SRE + Gemini Cloud Assist —— 阶段分离（止血优先）+ 并行假设检验 + 复盘沉淀

**来源**：Google Cloud 官方博客《How Google SREs Use Gemini CLI to Solve Real-World Outages》（2026-01）+ Gemini Cloud Assist 产品文档（Gemini 3 多智能体）。

**架构链路**：

```
SRE 四阶段（方法论核心）：
  Paging → Mitigation（MTTM 平均缓解时间：原因未知也要先止血——切流/回滚/重启/扩容）
        → Root Cause（止血后才深查）→ Postmortem（无指责复盘 + action items 防复发）
Gemini 实战：症状分类 → 匹配缓解手册 → 自动拉配置变更与生产日志交叉引用 → 2 分钟定位配置 push 逻辑错误
Cloud Assist：多智能体 + 迭代工具调用 + 并行假设测试（parallel hypothesis testing）
            + 跨轮长期记忆 + 仅在显式授权后行动 + Investigation 持久化（可保存/分享/附带上下文升级支持）
```

| 项目 | 评价 |
|------|------|
| 核心优势 | MTTM 导向使业务影响最小化；并行多假设避免单链误判；复盘 action items 知识回流；授权/审计安全模型清晰 |
| 主要不足 | 依赖成熟的"通用缓解手段封闭集"与高度自动化平台；并行假设 token 成本高 |
| 复杂度 | 高（平台依赖型） |
| 适用场景 | 变更/回滚/切流自动化成熟、把"止血速度"置于"根因完美"之前的团队 |

参考：<https://cloud.google.com/blog/topics/developers-practitioners/how-google-sres-use-gemini-cli-to-solve-real-world-outages>、<https://cloud.google.com/products/gemini/cloud-assist>

---

### 参考范式 F：因果图定位（MicroRCA 系）与"确定性派生 + LLM 解释"（Derive-then-Explain）

**来源**：学术界 MicroRCA（服务调用图 + 异常属性 → 因果图 → personalized PageRank/随机游走定位根因节点）；软件学报 AmazeMap 多层次影响图；2026 年 OpsCortex 明确提出 **"Root cause is computed, not guessed"**——在确认的调用边上 BFS，用阈值穿越时间先后 `root = argmin(t_amber)` 区分源头与下游受害者，LLM 只负责 explain / confirm / recommend。

**与本项目的相关性**：该范式与生产级混合架构经验一致——**确定性程序化管道（图 enrich→validate→prune→证据裁决→finalize）负责"算"，LLM 在受约束节点负责"想和说"**，并要求 LLM 失败时软降级回纯确定性结果。本方案 F 作为推荐架构的"内核"使用。

---

## 四、方案对比评估

### 4.1 能力对照

| 能力点 | A 微软 | B 阿里 | C 字节 | D 腾讯 | E Google | F 混合范式 |
|--------|:------:|:------:|:------:|:------:|:--------:|:----------:|
| 多假设并行评估（非首中即返回） | ❌ 单轮分类 | 部分（轨迹投票） | ✅ SOP 多路径 | ✅ 图上多候选 | ✅ 并行假设 | ✅ |
| 证据-假设裁决（支持/反驳） | 隐式（LLM） | 隐式（LLM） | ✅ SOP 判据 | ✅ 下钻印证 | ✅ 交叉引用 | ✅ 显式结构化 |
| 因果传播链/源头定位 | ❌ 类别输出 | ❌ | 部分 | ✅ 因果拓扑两阶段 | 部分 | ✅ 图+时间序 |
| 时间窗口相关性 | 部分 | ✅ 自由取数 | ✅ | ✅ | ✅ | ✅ |
| 确定性/可复现/可测试 | 高 | 低 | 高 | 中 | 中 | 最高 |
| 新颖故障覆盖（泛化） | 低（依赖手册） | 高 | 中高（自动生成SOP） | 中 | 高 | 中（LLM兜底） |
| LLM 幻觉控制 | 强（只分类） | 中（多重工程约束） | 强（SOP约束） | 中 | 中（授权护栏） | 最强（LLM不裁决） |
| 运行成本/token | 低 | 高 | 中低 | 中 | 高 | 低 |
| 历史案例/反馈学习 | ✅ RAG | 部分 | ✅ 知识沉淀 | ✅ 双飞轮 | ✅ 复盘回流 | 需自建 |
| 止血与根治分离 | ❌ | 部分 | ❌ | ❌ | ✅ MTTM | 可吸收 |

### 4.2 加权评分（针对"学习项目、可演进到生产、Java/Spring AI 技术栈"定位）

| 维度（权重） | A 微软 | B 阿里 | C 字节 | D 腾讯 | E Google | F 混合 |
|-------------|:------:|:------:|:------:|:------:|:--------:|:------:|
| 功能匹配度（25%） | 20 | 21 | 23 | 21 | 20 | 22 |
| 性能/成本（20%） | 17 | 12 | 16 | 15 | 12 | 18 |
| 可靠性（15%） | 13 | 11 | 13 | 12 | 12 | 14 |
| 落地成本（15%） | 12 | 8 | 12 | 10 | 9 | 13 |
| 实现复杂度友好（10%） | 8 | 5 | 8 | 6 | 5 | 7 |
| 生态/资料（10%） | 8 | 9 | 8 | 7 | 9 | 7 |
| 扩展性（5%） | 3 | 5 | 4 | 4 | 5 | 4 |
| **综合评分** | **81** | **71** | **84** | **75** | **72** | **85** |

### 4.3 推荐结论

**不照搬任何一家，采用"F 为核、C 为骨、A/E 为翼、B 兜底"的混合架构**：

> **确定性 SOP/规则管道负责可枚举故障的证据裁决与因果图定位（F+C）；LLM 只在三个受约束节点工作——假设生成、证据解读、报告叙述；RAG 历史案例与复盘反馈形成知识闭环（A+E）；规则/SOP 均未命中时才降级到自由 ReAct Agent（B）。**

理由：
1. 当前项目是学习项目，纯自主 Agent（B）成本高、不可测、效果无保证（35.5% 的公开数据），且会丢掉现有规则链的确定性优点。
2. 字节 64.01% vs 35.50% 的对照实验证明：**对 LLM 加确定性约束是当前 ROI 最高的改进方向**。
3. 现有代码资产（TopologyInfo 依赖模型、McpToolManager 注册表、4 个 MCP 工具、8 个假设规则）可平滑迁移为 SOP + 证据裁决器，无需推倒重来。
4. "LLM 不做最终裁决、失败软降级回规则结果"是经生产验证的安全模式。

---

## 五、当前项目优化思路（分阶段，精确到代码落点）

### 目标架构

```
告警
 → 假设生成（规则枚举全集 + LLM 补充候选，受约束映射到 SOP 注册表）
 → 候选假设集（并行，借鉴 Google）
 → 各假设 SOP 确定性执行（MCP 工具：metrics/deploy/code/log/trace/topology）
 → 证据收集（强制带时间窗 + 拓扑归属，删除 Random 造数）
 → EvidenceVerdict 统一裁决（SUPPORT / REFUTE / UNRELATED + 权重）
 → AttributionGraph 构建（复用 TopologyInfo）→ 异常裁决传导 → 裁剪 → 时间序定位源头
 → 假设打分排序（证据加权，置信度由公式计算）→ 充分性闸门（证据不足则回到补充调查）
 → 结论渲染（事实模板 + LLM 仅润色叙述，强制引用 evidenceId 防幻觉）
 → 建议三层：Mitigation 止血 / Remediation 根治 / Prevention 预防
 → Investigation 持久化（H2）→ 人工反馈 → 案例库 RAG + 新 SOP 沉淀
```

### P0：规则内核升级（不引入 LLM，先补五个缺失子模型）

| 编号 | 改造项 | 具体做法 | 代码落点 |
|------|--------|----------|----------|
| P0-1 | **多假设并行评分替代首中即返回**（修 D1/D2） | 每个假设独立产出 `HypothesisResult{type, score, evidences}`；分数由证据加权公式计算（如 `score = Σsupport权重 / (Σsupport+Σrefute) × 时效系数 × 拓扑系数`）；输出 topN 候选而非单一 RootCause；confidence 由 `String` 改为 `double` | 重写 analyzeRootCause L388-493；新增 `Hypothesis`、`HypothesisEvaluator` 抽象，8 个 if 分支改为 8 个 Evaluator Bean（策略模式，注册表管理） |
| P0-2 | **证据裁决模型**（修 D3） | 新增 `EvidenceVerdict{evidenceId, hypothesisType, verdict(SUPPORT/REFUTE/UNRELATED), weight, reason}`；Evidence 增加 direction/weight 字段；阈值命中不再直接拍"高/中/低"，而是产出指向具体假设的裁决 | 重构 collectEvidences L218-342；RootCause 携带支撑证据 ID 列表 |
| P0-3 | **时间窗口相关性**（修 D5） | 发布假设得分 = 时间窗衰减函数：`|alarmTime - deployTime| ≤ 30min` 满分，30min~2h 线性衰减，超窗 UNRELATED。指标阈值改为"相对基线偏离"（MCP mock 增返回 baseline 字段，保持 Mock 架构不变） | L444-452 发布分支；MetricsMcpService 数据模型加 baseline |
| P0-4 | **因果传播图**（修 D4） | 以告警模块为起点，基于 TopologyInfo 的 mq/job/db/externalServiceDependencies + 上下游构建 AttributionGraph（节点=模块/MQ/JOB/DB，边=依赖方向+异常标记）；异常沿边传导并裁剪；源头判定用异常出现时间序 `argmin`（范式 F）；结论输出"源头节点 → 传播路径 → 受影响模块"三段式 | 新增 `AttributionGraph`、`GraphBuilder`、`GraphPruner`；替换 isMqRelatedToAlarmModule 的名称匹配 |
| P0-5 | **干扰项确定性化**（修 D7） | 删除 Random：非告警子图上的异常节点统一裁决为 UNRELATED 并附拓扑理由（"该 MQ 属于 X 模块，与告警模块无依赖路径"），保证同输入同输出、可单测 | 删 selectDisturbanceModules L344-348，改由图裁剪产生 |
| P0-6 | **建议三层拆分**（修 D8） | suggestions 改为 `{mitigation:[], remediation:[], prevention:[]}`：止血项对应 Google 通用缓解（回滚/扩容/重启/切流），每条建议绑定依据 evidenceId/hypothesisType | 重构 generateSuggestions L530-605 |

**P0 验收标准（代码级）**：`mq_backlog + 刚发布 + CPU 85%` 同输入时，响应包含 ≥3 个带不同分数的候选假设；MQ 候选的证据链含传播路径；发布候选分数随 deployTime 与 alarmTime 间隔变化；全流程无 Random 调用。

### P1：打通两套体系 + 受约束 LLM 接入

| 编号 | 改造项 | 具体做法 | 对标 |
|------|--------|----------|------|
| P1-1 | **PlanService 静态计划 → SOP 注册表** | 每种根因类型一个 `DiagnosisSop`：声明所需工具、调用顺序、判据（什么算 SUPPORT）、出口条件；ExecuteService 参数从 AnalysisRequest 动态构造（删除 user-service/固定日期/traceId 硬编码）；SOP 产物即 EvidenceVerdict 列表 | C 字节 |
| P1-2 | **LLM 三受约束接入点**（复用 hello 模块 Spring AI） | ① 假设生成：LLM 读告警描述提候选 → 只能映射到 SOP 注册表内类型，非法类型拒绝；② 证据解读：确定性裁决之后，LLM 只解释 top1 假设为何成立；③ 报告叙述：模板填事实、LLM 润色，**强制每个结论句引用 evidenceId**，无引用句不得输出 | C+F |
| P1-3 | **LLM 失败软降级** | LLM 调用异常/输出不守契约时，整体降级为 P0 纯规则结果；降级后重新校验结论充分性，避免"LLM 半截话"污染响应 | F |
| P1-4 | **充分性闸门 + 补充调查循环** | 若 top1 分数低于阈值或关键证据缺失，按 informationNeeds 触发增量工具调用（如补查日志/Trace），设最大轮次防死循环；现有 LogQueryTool/TraceQueryTool/GitlabQueryTool/BusinessKnowledgeTool 由此正式进入主链路（修 D6/D10） | A+C |
| P1-5 | **Investigation 持久化** | H2 建表存 analysisId 全量调查记录：请求、执行计划、每步工具调用入参/出参、证据、裁决、因果图 JSON、结论；提供查询 API，可复查可分享（也为 P2 RAG 备料） | E Google |

### P2：智能化闭环（远期）

| 编号 | 改造项 | 对标 |
|------|--------|------|
| P2-1 | 历史案例 RAG：结案案例入案例库，新告警 embedding 检索 topK 相似案例作为假设先验 | A 微软 |
| P2-2 | 反馈 → SOP 沉淀：用户判定"分析错误/补充根因"回流；高频新模式经人工确认后转成新 Java SOP（SOP 自动生成草案） | C 字节 |
| P2-3 | LLM 假设生成多轨迹自洽投票（TSC），仅在低置信场景触发以控成本 | B 阿里 |
| P2-4 | McpToolManager 对齐 MCP 标准描述（name/description/inputSchema），支持 LLM 自动选工具；工具分 low/mid/high 三层 | C/D |
| P2-5 | 高频、高置信根因类型对接通用缓解动作（回滚/扩容建议一键生成操作单），引入"显式授权才执行"护栏 | E Google |

### 与现有 optimization-plan.md 的关系

仓库已有 [optimization-plan.md](file:///Users/bage/bage/github/study/study-ai-best-practice/study-ai-best-practice-exception-analysis/optimization-plan.md)，其方向（真实数据源、多模型、知识图谱、持续学习）不错但偏"堆基础设施"，缺少对**裁决/置信/因果/时间四个子模型缺失**的诊断，也无大厂范式对照与分阶段代码落点。本报告 P0/P1 是其"短期/中期"项的可执行细化，建议以本报告为准推进。

---

## 六、风险提示

| 风险 | 应对 |
|------|------|
| 一次铺开 P0+P1 导致范围失控 | 严格按 P0 六项先闭环（纯 Java、可单测、零外部依赖），P1 另行立项 |
| Mock 数据下因果图/时间窗改造"看起来真、实际假" | P0 全部基于 MCP 接口层改造，Mock 实现同步补 baseline/时间戳字段，保持可替换为真实数据源 |
| LLM 幻觉污染结论 | LLM 不参与裁决与打分；叙述层强制 evidenceId 引用 + 软降级 |
| SOP 注册表膨胀难维护 | 只把 P0 的 8 个 Evaluator 迁移为首批 SOP；新增 SOP 必须有反馈案例支撑（P2-2） |

## 七、参考资料

1. Microsoft, RCACopilot: Automatic Root Cause Analysis via Large Language Models for Cloud Incidents (EuroSys 2024) — <https://arxiv.org/html/2305.15778v4>
2. Microsoft, Automated Root Causing of Cloud Incidents using In-Context Learning with GPT-4 (FSE 2024) — <https://arxiv.org/pdf/2401.13810.pdf>
3. Alibaba, RCAgent: Cloud Root Cause Analysis by Autonomous Agents with Tool-Augmented LLMs — <https://arxiv.org/pdf/2310.16340v1>
4. 阿里云：大模型驱动的云监控 2.0 与 AIOps 新范式（2025）— <https://www.alibabacloud.com/blog/apsara-conference-insights-rebuild-observability---craft-a-large-model-driven-cloud-monitor-2-0-and-a-new-aiops-paradigm_602694>
5. ByteDance et al., Flow-of-Action: SOP Enhanced LLM-Based Multi-Agent System for RCA (WWW 2025) — <https://ar5iv.labs.arxiv.org/html/2502.08224>
6. 字节 SRE Agent：从 0 到 1 的降噪与排障实战（aCon 2025）— <https://wesee.club/usr/uploads/2025/11/3635334579.pdf>
7. 腾讯云 TCOP：AI 驱动可观测平台与 SRE Agent（2026）— <https://developer.cloud.tencent.com.cn/article/2655070>
8. Google, How Google SREs Use Gemini CLI to Solve Real-World Outages（2026）— <https://cloud.google.com/blog/topics/developers-practitioners/how-google-sres-use-gemini-cli-to-solve-real-world-outages>
9. Google Cloud, Gemini Cloud Assist（并行假设测试、Investigation 持久化）— <https://cloud.google.com/products/gemini/cloud-assist>
10. AmazeMap：基于多层次影响图的微服务故障定位方法，软件学报 2024 — <https://www.jos.org.cn/html/2024/7/7104.htm>
