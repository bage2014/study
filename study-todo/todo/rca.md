# 技能清单





这是 BroadScan 设计的核心问题，分两块回答：拉什么数据 和 怎么知道拉哪些。

---
一、拉什么数据（数据分类）

SRE 场景下的数据源分 6 类：

┌─────────────────────────────────────────────────────┐
│              故障信号数据分类                         │
├──────────────┬──────────────────────────────────────┤
│ 应用层        │ 错误率、P99延迟、QPS、线程池、GC日志   │
│ 数据库层      │ 慢查询、连接数、锁等待、主从延迟        │
│ 缓存层        │ 命中率、驱逐数、连接数、内存使用        │
│ 消息队列层    │ 消费堆积(lag)、生产/消费速率、死信队列  │
│ 基础设施层    │ CPU/内存/磁盘IO、容器重启、Pod状态      │
│ 外部依赖层    │ 下游成功率、响应时间、熔断状态          │
└──────────────┴──────────────────────────────────────┘

还有一类非指标数据——变更事件：
发布记录、配置变更、DB Schema 变更、限流阈值调整
（往往是根因的直接触发器，时序上在故障前几分钟）

---
二、怎么知道拉哪些（映射机制）

三种方案，复杂度递增：

---
方案 A：规则映射（target.type → 工具集）

最简单，最确定性，适合已知目标类型

// 假设结构
record Hypothesis(String id, String desc, TargetType targetType, String targetName) {}

enum TargetType { APP, DB, REDIS, MQ, SUPPLIER }

// 规则映射表：target类型 → 要拉的工具列表
Map<TargetType, List<String>> SCAN_PLAYBOOK = Map.of(
    APP,      List.of("app_error_rate", "app_p99_latency", "app_gc_log", "app_thread_pool"),
    DB,       List.of("db_slow_query", "db_connections", "db_lock_waits", "db_cpu"),
    REDIS,    List.of("redis_hit_rate", "redis_eviction", "redis_memory", "redis_latency"),
    MQ,       List.of("mq_consumer_lag", "mq_produce_rate", "mq_consume_rate", "mq_dead_letter"),
    SUPPLIER  List.of("http_success_rate", "http_p99_latency", "circuit_breaker_status")
);

// BroadScan：按假设类型并发拉数据
Map<String, Object> broadScanNode(List<Hypothesis> hypotheses) {
    return hypotheses.parallelStream().collect(toMap(
        h -> h.id(),
        h -> {
            List<String> tools = SCAN_PLAYBOOK.get(h.targetType());
            // 并发调用该假设对应的所有工具
            return tools.parallelStream().collect(toMap(
                tool -> tool,
                tool -> mcpClient.call(tool, Map.of("appId", h.targetName(), "timeRange", "30m"))
            ));
        }
    ));
}

优点：快、稳、无幻觉
缺点：target.type 得在 Plan 阶段就分类准确

---
方案 B：LLM 动态选工具（Hypothesis → Tool Selection）

适合工具多、假设描述复杂的场景

// 给 LLM 提供工具描述列表，让它选
List<String> selectToolsForHypothesis(Hypothesis h, List<ToolMeta> availableTools) {

    String toolCatalog = availableTools.stream()
        .map(t -> "- %s: %s".formatted(t.name(), t.description()))
        .collect(joining("\n"));
    
    String prompt = """
        假设：%s（目标：%s）
    
        可用工具列表：
        %s
    
        请从上述工具中选择3-5个最相关的，用于验证这个假设。
        只返回工具名列表，格式：["tool_a", "tool_b"]
        """.formatted(h.desc(), h.targetName(), toolCatalog);
    
    return llm.invoke(prompt); // → parse → List<String>
}

// BroadScan
Map<String, Object> broadScanNode(List<Hypothesis> hypotheses, List<ToolMeta> tools) {
    return hypotheses.parallelStream().collect(toMap(
        h -> h.id(),
        h -> {
            List<String> selectedTools = selectToolsForHypothesis(h, tools);
            return selectedTools.parallelStream().collect(toMap(
                tool -> tool,
                tool -> mcpClient.call(tool, extractArgs(h, tool))
            ));
        }
    ));
}

优点：灵活，工具集变化无需改代码
缺点：多一次 LLM 调用，有幻觉选错工具的风险

---
方案 C：症状驱动 Playbook（推荐，两者结合）

核心思路：Plan 阶段 LLM 同时输出 targetType + symptomPattern，由症状模式决定数据采集策略

// Plan 节点输出更丰富的假设
record Hypothesis(
    String id,
    String desc,
    TargetType targetType,
    String targetName,
    SymptomPattern symptom   // ← 新增：症状模式
) {}

enum SymptomPattern {
    HIGH_LATENCY,       // 高延迟
    HIGH_ERROR_RATE,    // 高错误率
    RESOURCE_EXHAUSTED, // 资源耗尽（连接池/线程池/内存）
    CONSUMER_LAG,       // 消费堆积
    DEPENDENCY_FAILURE  // 依赖故障
}

// 二维映射：target类型 + 症状 → 更精准的工具集
Map<String, List<String>> SYMPTOM_PLAYBOOK = Map.of(
    "APP:HIGH_LATENCY",        List.of("app_p99_latency", "db_slow_query", "redis_latency", "trace_p99"),
    "APP:HIGH_ERROR_RATE",     List.of("app_error_rate", "app_error_log", "downstream_success_rate"),
    "APP:RESOURCE_EXHAUSTED",  List.of("app_thread_pool", "app_gc_log", "app_heap_usage", "db_connections"),
    "DB:HIGH_LATENCY",         List.of("db_slow_query", "db_lock_waits", "db_cpu", "db_connections"),
    "MQ:CONSUMER_LAG",         List.of("mq_consumer_lag", "mq_produce_rate", "mq_consumer_status", "app_gc_log"),
    "SUPPLIER:DEPENDENCY_FAILURE", List.of("http_success_rate", "http_p99_latency", "circuit_breaker_status", "dns_resolve")
);

List<String> selectTools(Hypothesis h) {
    String key = h.targetType() + ":" + h.symptom();
    // 优先精确匹配，fallback 到 target 类型通用集
    return SYMPTOM_PLAYBOOK.getOrDefault(key,
           SCAN_PLAYBOOK.get(h.targetType()));
}

---
三、Plan 节点如何输出 targetType + symptom

关键在于 Plan 的 Prompt 要求 LLM 结构化输出：

String planPrompt = """
    告警：%s，时间：%s

    请生成3-5个根因假设，每个假设必须包含：
    - id: 唯一标识
    - desc: 假设描述
    - targetType: 枚举值之一 [APP, DB, REDIS, MQ, SUPPLIER]
    - targetName: 具体服务/实例名（如 order-service, order-db）
    - symptom: 枚举值之一 [HIGH_LATENCY, HIGH_ERROR_RATE, RESOURCE_EXHAUSTED, CONSUMER_LAG, DEPENDENCY_FAILURE]
    - confidence: 初始置信度 0-1
    
    JSON 数组格式输出。
    """.formatted(alertSubject, alertTime);

LLM 输出示例：
[
  {
    "id": "h1",
    "desc": "订单DB慢查询导致API超时",
    "targetType": "DB",
    "targetName": "order-db",
    "symptom": "HIGH_LATENCY",
    "confidence": 0.6
  },
  {
    "id": "h2",
    "desc": "MQ消费堆积导致履约延迟",
    "targetType": "MQ",
    "targetName": "order-fulfill-topic",
    "symptom": "CONSUMER_LAG",
    "confidence": 0.3
  }
]

然后 BroadScan 直接按 DB:HIGH_LATENCY → 拉 [db_slow_query, db_lock_waits, db_cpu, db_connections]，完全确定性，无需再问 LLM。

---
总结

Plan（LLM）         → 输出 targetType + symptom
                              ↓
Playbook（规则）    → targetType:symptom → 工具列表
                              ↓
BroadScan（并发）   → 按工具列表确定性采集数据
                              ↓
BroadAnalyze（LLM） → 分析采集结果，更新置信度

LLM 负责理解语义（分类），规则负责确定工具（路由），两者各司其职。



















------------------

SRE 根因分析 AI 准确率提升方向总结

---
方向一：信息裁剪（Context Pruning）

核心思路：减少噪声，提高信噪比，让 LLM 只看"有价值的信号"

┌──────────────┬──────────────────────────────────────────────┐
│     手段     │                     做法                     │
├──────────────┼──────────────────────────────────────────────┤
│ 异常前置过滤 │ 原始指标先过异常检测，只保留偏离基线的数据点 │
├──────────────┼──────────────────────────────────────────────┤
│ 基线对比编码 │ 喂给 LLM 的是"偏差量"（40x），而不是绝对值   │
├──────────────┼──────────────────────────────────────────────┤
│ 时间线重建   │ 把所有事件按时序排列，裁掉正常时段的冗余数据 │
├──────────────┼──────────────────────────────────────────────┤
│ 拓扑感知裁剪 │ 只保留故障节点 ±2 跳内的依赖数据             │
└──────────────┴──────────────────────────────────────────────┘

▎ 本质：LLM 不是搜索引擎，喂少而精的数据 > 喂全量数据

---
方向二：推理/分析框架（Reasoning Framework）

核心思路：给 LLM 装上"脚手架"，约束推理路径，防止漂移

┌───────────────────────┬──────────────┬───────────────────────────────────┐
│         框架          │   适合场景   │               特点                │
├───────────────────────┼──────────────┼───────────────────────────────────┤
│ 假设-验证（当前架构） │ 通用         │ Plan先验 → Scan采集 → Analyze收敛 │
├───────────────────────┼──────────────┼───────────────────────────────────┤
│ 5-Why 因果链          │ 单链条故障   │ 强制逐层追因，每层必须有证据      │
├───────────────────────┼──────────────┼───────────────────────────────────┤
│ Fault Tree（FTA）     │ 已知故障模式 │ 自顶向下，AND/OR 逻辑门分解       │
├───────────────────────┼──────────────┼───────────────────────────────────┤
│ Tree of Thoughts      │ 多根因并发   │ 并行维护多假设树，评分剪枝        │
├───────────────────────┼──────────────┼───────────────────────────────────┤
│ 反事实验证            │ 置信度校准   │ "若根因是X，还应看到Y"——实际查Y   │
└───────────────────────┴──────────────┴───────────────────────────────────┘

▎ 本质：结构化推理 > 自由生成，每一步约束输入输出格式

---
方向三：知识增强（Knowledge Augmentation）

核心思路：用外部知识弥补 LLM 对系统的"无知"

┌──────────────────────┬────────────────────────────────────────────────┐
│         手段         │                      做法                      │
├──────────────────────┼────────────────────────────────────────────────┤
│ 历史 RCA 检索（RAG） │ 相似故障 embedding 召回 → 注入历史根因作为先验 │
├──────────────────────┼────────────────────────────────────────────────┤
│ 服务拓扑图注入       │ 提前告知上下游依赖，避免盲目假设传播方向       │
├──────────────────────┼────────────────────────────────────────────────┤
│ 变更感知             │ 故障前 N 分钟内的发布/配置变更优先注入         │
├──────────────────────┼────────────────────────────────────────────────┤
│ Runbook 集成         │ 已知故障类型直接匹配 SOP，绕过推理             │
└──────────────────────┴────────────────────────────────────────────────┘

▎ 本质：先验知识越强，LLM 需要"瞎猜"的空间越小

---
方向四：多 Agent 模式（Multi-Agent）

核心思路：单 Agent 认知有限，分工或对抗可以弥补盲区

┌───────────────┬───────────────────────────────────────────────────┬────────────────────────────┐
│     模式      │                       做法                        │            优势            │
├───────────────┼───────────────────────────────────────────────────┼────────────────────────────┤
│ 专家分工      │ DB/网络/应用/基础设施各一个 Agent 并发分析        │ 并发快，领域深             │
├───────────────┼───────────────────────────────────────────────────┼────────────────────────────┤
│ 辩论对抗      │ 正方提根因 → 反方攻击漏洞 → Judge 裁定            │ 暴露薄弱假设，防止过度自信 │
├───────────────┼───────────────────────────────────────────────────┼────────────────────────────┤
│ 批评-生成     │ Generator 出报告 → Critic 找矛盾 → Generator 修正 │ 自我质疑闭环               │
├───────────────┼───────────────────────────────────────────────────┼────────────────────────────┤
│ Ensemble 投票 │ 多个独立 Agent 各自得出结论，投票/加权合并        │ 降低单点偏差               │
└───────────────┴───────────────────────────────────────────────────┴────────────────────────────┘

▎ 本质：LLM 的锚定偏差靠"自己否定自己"或"他人否定"来纠偏

---
方向五：数据结构化预处理（Pre-Structuring）

核心思路：让 LLM 分析结构，而不是分析原始文本

┌───────────────────┬──────────────────────────────────────────────────────────┐
│       手段        │                           做法                           │
├───────────────────┼──────────────────────────────────────────────────────────┤
│ 因果图（DAG）构建 │ 先用规则/轻量模型把事件构建成有向图，LLM 在图上推理      │
├───────────────────┼──────────────────────────────────────────────────────────┤
│ 时序事件序列化    │ 所有信号统一时间戳排列，暴露"变更→故障"的时序因果        │
├───────────────────┼──────────────────────────────────────────────────────────┤
│ 异常聚类          │ 相似报错先聚类归并，LLM 看类别代表，而不是几千条原始日志 │
└───────────────────┴──────────────────────────────────────────────────────────┘

▎ 本质：LLM 理解结构化输入的准确率 >> 理解非结构化原始数据

---
一张图总览

         输入质量                    推理质量                   输出质量
┌─────────────────────┐   ┌────────────────────────┐   ┌──────────────────┐
│   信息裁剪           │   │   分析框架              │   │  验证与校准       │
│  - 异常过滤          │──►│  - 假设-验证（分阶段）   │──►│  - 反事实验证     │
│  - 基线对比          │   │  - 5-Why 因果链         │   │  - 证据链强制     │
│  - 时间线重建         │   │  - FTA 故障树           │   │  - 置信度校准     │
│  - 拓扑裁剪          │   │  - Tree of Thoughts     │   └──────────────────┘
└─────────────────────┘   └────────────────────────┘
         ▲                           ▲
┌─────────────────────┐   ┌────────────────────────┐
│   知识增强           │   │   多 Agent 模式         │
│  - 历史RCA RAG      │   │  - 专家分工             │
│  - 拓扑图注入        │   │  - 辩论对抗             │
│  - 变更感知          │   │  - 批评-生成            │
│  - Runbook 集成     │   │  - Ensemble 投票        │
└─────────────────────┘   └────────────────────────┘

---
投入产出比速查

┌───────────────────────┬──────────┬────────────┬────────┐
│         方向          │ 实现成本 │ 准确率提升 │ 优先级 │
├───────────────────────┼──────────┼────────────┼────────┤
│ 时间线重建 + 变更感知 │ 低       │ +++        │ P0     │
├───────────────────────┼──────────┼────────────┼────────┤
│ 历史 RCA RAG          │ 中       │ +++        │ P0     │
├───────────────────────┼──────────┼────────────┼────────┤
│ 反事实验证            │ 低       │ ++         │ P1     │
├───────────────────────┼──────────┼────────────┼────────┤
│ 专家分工 Agent        │ 高       │ ++         │ P1     │
├───────────────────────┼──────────┼────────────┼────────┤
│ 因果图预构建          │ 中       │ +++        │ P1     │
├───────────────────────┼──────────┼────────────┼────────┤
│ 辩论对抗 Agent        │ 高       │ +++        │ P2     │
└───────────────────────┴──────────┴────────────┴────────┘

● Ran 1 stop hook
  ⎿  Stop hook error: Failed with non-blocking status code: Error: Bun not found. Please install Bun: https://bun.sh
  After installation, restart your terminal.







---------

这是个很值得深挖的问题，我把业界主流方案按思路维度分层整理。

---
维度一：推理框架（Reasoning Framework）

1. ReAct（你们已有）

Thought → Action → Observation → Thought → ...
优点：灵活自适应；缺点：无约束时容易"reasoning drift"，越想越偏。

2. Tree of Thoughts（ToT）

           根因假设A ──► 证据支持？→ 分支A1、A2
          /
告警 ────
          \
           根因假设B ──► 证据支持？→ 分支B1、B2
核心思路：并行维护多条假设树，用评分函数剪枝，保留高置信路径。

适合场景：根因不唯一、多条因果链并存的复杂故障。

3. 5-Why 结构化钻取

为什么用户报错？→ API 超时
为什么 API 超时？→ DB 查询慢
为什么 DB 查询慢？→ 锁等待
为什么锁等待？→ 大事务未提交
为什么大事务未提交？→ 发布变更引入
让 LLM 强制执行因果链，而不是跳跃性给结论，每一层必须有证据锚定。

4. Fault Tree Analysis（FTA）—— 自顶向下

[顶层故障：订单失败]
    AND/OR
[支付超时] [库存扣减失败]
    |               |
[DB慢查询] [Redis宕机]
把故障建模为逻辑树（AND/OR门），LLM 负责填充叶节点的证据。

---
维度二：多智能体架构（Multi-Agent Patterns）

5. 辩论模式（Debate / Adversarial）

Agent-A（正方）：根因是 DB
Agent-B（反方）：不对，是 MQ 堆积
Judge Agent：综合双方证据，裁定
原理：LLM 单独推理容易锚定，多个 Agent 相互挑战可以暴露薄弱假设。Google DeepMind 研究证明辩论能提升 20-30% 准确率。

6. 专家分工模式（Specialist Agents）

Coordinator
├── DB-Expert Agent    → 分析慢查询、锁、连接池
├── Network-Expert     → 分析延迟、丢包、DNS
├── App-Expert Agent   → 分析 GC、线程池、OOM
└── Infra-Expert       → 分析 CPU、磁盘、容器重启
各 Agent 独立得出局部结论，Coordinator 汇总。适合大型分布式系统，并发快。

7. 批评-生成模式（Critic-Generator）

Generator：生成根因报告
Critic：找漏洞（"你说是 DB，但 DB 监控正常，怎么解释？"）
Generator：修正报告
强制自我质疑，避免 LLM 过于自信。

---
维度三：信息架构（Context Engineering）

8. 因果图构建优先（Causal Graph First）

原始数据 → 构建事件因果图（DAG）→ 再交给 LLM 分析
关键：LLM 分析图结构比分析原始文本准确得多，因为图已经编码了时序和依赖关系。

实现：先用规则/轻量模型构建事件 DAG，LLM 只需在图上做推理。

9. 异常前置过滤（Anomaly-First）

原始指标（1000条）→ 异常检测（Z-Score/STL）→ 仅保留 20条异常 → 喂给LLM
降低信噪比，让 LLM 只看"出格"的数据点，而不是全量时序。

10. 时间线重建（Timeline Reconstruction）

[10:00:01] 告警触发
[10:00:03] DB 连接数上升
[10:00:05] API P99 飙升
[10:00:07] 用户错误率 > 1%
[09:58:00] 发布变更（发现：变更在故障前 2 分钟！）
把所有事件强制排成时间线，LLM 更容易发现"变更 → 故障"的因果方向。

---
维度四：知识增强（Knowledge Augmentation）

11. 历史 RCA 向量检索（RAG）

当前故障 embedding → 相似历史事故 Top-K → 注入 Prompt
"3 个月前类似告警，根因是 XX"——历史案例是最强的先验。

12. 服务拓扑图注入

[用户服务] → [订单服务] → [库存服务] → [DB]
                        ↘ [MQ] → [履约服务]
告警发生在哪个节点，上下游依赖是什么，提前告知 LLM，避免盲目假设。

13. 基线对比（Baseline Encoding）

当前：DB查询 P99 = 2000ms
基线：DB查询 P99 = 50ms（最近7天均值）
偏差：40x → 明显异常
让 LLM 看到的是偏差量而不是绝对值，降低判断门槛。

---
维度五：验证与校准（Verification）

14. 反事实验证（Counterfactual Check）

假设根因是 DB 慢查询，那么：
✓ 应该看到 DB CPU 上升 → 检查：实际 DB CPU 正常 → 假设被否定
强制 LLM 推导"如果这是根因，还应该看到什么"，然后实际验证，形成闭环。

15. 证据链强制要求（Evidence Anchoring）

每个结论必须附：
  - 支持证据（指标/日志/事件）
  - 置信度（0-1）
  - 反对证据（有没有矛盾数据）
LLM 不允许给"裸结论"，必须给证据链，质量可审计。

---
整体框架对比

┌───────────────┬──────────────┬────────┬────────────┐
│   框架/方案   │   适合场景   │ 复杂度 │ 准确率提升 │
├───────────────┼──────────────┼────────┼────────────┤
│ ToT（假设树） │ 多根因并发   │ 高     │ +++        │
├───────────────┼──────────────┼────────┼────────────┤
│ 5-Why 钻取    │ 单链条故障   │ 低     │ ++         │
├───────────────┼──────────────┼────────┼────────────┤
│ FTA 故障树    │ 已知故障类型 │ 中     │ ++         │
├───────────────┼──────────────┼────────┼────────────┤
│ 辩论多 Agent  │ 高置信要求   │ 高     │ +++        │
├───────────────┼──────────────┼────────┼────────────┤
│ 专家分工      │ 大规模分布式 │ 高     │ ++         │
├───────────────┼──────────────┼────────┼────────────┤
│ 因果图构建    │ 数据密集     │ 中     │ +++        │
├───────────────┼──────────────┼────────┼────────────┤
│ 时间线重建    │ 变更引发故障 │ 低     │ +++        │
├───────────────┼──────────────┼────────┼────────────┤
│ 历史 RAG      │ 重复性故障   │ 中     │ +++        │
├───────────────┼──────────────┼────────┼────────────┤
│ 反事实验证    │ 置信度校准   │ 中     │ ++         │
└───────────────┴──────────────┴────────┴────────────┘

---
对你们项目的建议优先级

你们已有的架构（Plan→BroadScan→BroadAnalyze→FocusedScan→FocusedAnalyze→GlobalReact）已经覆盖了维度一/三，下一步性价比最高的是：

1. 时间线重建（低成本，对变更引发故障效果极好）
2. 历史 RCA RAG（已规划 Hive 集成，补上 embedding 检索）
3. 反事实验证（在 GlobalReact 阶段加一轮自我否定）
4. 服务拓扑注入（Plan 阶段，替代现在 fallback 的通用假设）



