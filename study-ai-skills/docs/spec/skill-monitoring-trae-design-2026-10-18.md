# Skill 使用监控方案 — Trae 适配设计

**文档类型:** 方案最终设计（SPEC 驱动）
**创建日期:** 2026-10-18
**版本:** v1.0
**关联文档:** [skill-usage-monitoring-research-2026-10-18.md](file:///Users/bage/bage/github/study/study-ai-skills/docs/spec/skill-usage-monitoring-research-2026-10-18.md)

---

## 一、问题重述（Trae 架构下的 Skill 本质）

在 Trae 中，Skill 不是可执行程序，而是 **LLM prompt 片段**：

| 组件 | 实际角色 | 对监控的影响 |
|------|---------|-------------|
| SKILL.md | Agent 的"操作手册" | 没有 runtime 可以 @observe |
| Skill 工具调用 | Agent 读取 SKILL.md 注入上下文 | 调用发生在 Agent 内部 |
| Skill 执行 | Agent 按手册调用 Shell/Grep/Edit 等 | 执行分散在多个工具调用里 |

**因此大厂方案（Langfuse/@observe、OTel+Prometheus、MCP 埋点）都不直接适用**——它们假设被监控对象有独立 runtime 或 HTTP server。

---

## 二、Trae 适配方案设计

### 2.1 核心洞察

虽然 Skill 没有 runtime，但 **Agent 的 Skill 工具调用动作是可捕获的**——因为：
1. Agent 每次调用 Skill 工具，都是通过 `Skill` 工具函数，这个函数调用 Agent 自己知道
2. Agent 可以被 Skill.md 的内容引导——在 prompt 里加指令让 Agent 先做一件事

### 2.2 三层埋点架构

```
Layer 1: Agent Skill 工具调用层（最精准）
────────────────────────────────────────
Agent 每次调用 Skill 工具时（我们自己就是 Agent）
  → 自动追加一条 JSONL 记录
  → 记录 ts, skill_name, client=trae-macos, duration_ms

Layer 2: SKILL.md 自引导层（捕获自动触发）
────────────────────────────────────────
每个 SKILL.md 开头加：
  <!-- MONITORING HOOK: 执行前先记录 -->
  如果这是一个需要 bash 执行的 skill：
    先运行 ~/.trae-cn/scripts/skill-usage.sh log {skill-name}
    再继续执行 skill 正文的指导

Layer 3: validate-skills.sh 增强层（周期性扫描）
────────────────────────────────────────
每次跑 validate-skills.sh 时
  → 记录每个 skill 的最后验证时间 + 状态到 JSONL
```

### 2.3 数据流

```
Skill 工具调用 → skill-usage.sh log → JSONL 文件 ←── validate-skills.sh
                                              │
                                              ▼
                                   skill-usage.sh report
                                              │
                                              ▼
                                      日报 / 周报 / 月报
```

### 2.4 JSONL 数据格式

```json
{"ts":"2026-10-18T10:00:00+08:00","skill":"common-test-driven-development","event":"tool_load","client":"trae-macos","session":"sess-abc123","duration_ms":150,"status":"ok"}
{"ts":"2026-10-18T10:05:22+08:00","skill":"common-test-driven-development","event":"script_log","client":"trae-macos","session":"sess-abc123","trigger":"auto","user":"user_local"}
{"ts":"2026-10-18T10:12:00+08:00","skill":"common-visual-design","event":"validation","client":"script","duration_ms":80,"status":"ok"}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| ts | ISO 8601 | 时间戳 |
| skill | string | Skill 名称 |
| event | enum | tool_load / script_log / validation |
| client | string | trae-macos / script / trae-* |
| session | string | 会话 ID（可选） |
| duration_ms | number | 耗时（毫秒） |
| status | enum | ok / error |
| trigger | enum | manual / auto |
| user | string | 用户标识 |

---

## 三、实施计划

### Step 1：创建 skill-usage.sh（5 分钟）

**文件位置:** `~/.trae-cn/scripts/skill-usage.sh`

```bash
#!/bin/bash
# Trae Skill 使用监控脚本
# 用法: skill-usage.sh log <skill-name> [--trigger manual|auto]
#       skill-usage.sh report [--days 7]

JSONL_FILE="${HOME}/.trae-cn/skill-usage.jsonl"
LOG_DIR="$(dirname "$JSONL_FILE")"

# 确保目录存在
mkdir -p "$LOG_DIR"

log_usage() {
    local skill_name="$1"
    local trigger="${2:-manual}"
    local ts=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
    local session_id="${3:-no-session}"
    local duration="${4:-0}"
    
    cat >> "$JSONL_FILE" << EOF
{"ts":"$ts","skill":"$skill_name","event":"script_log","client":"trae-macos","session":"$session_id","duration_ms":$duration,"status":"ok","trigger":"$trigger"}
EOF
    echo "✅ 已记录: $skill_name"
}

report_usage() {
    local days="${1:-7}"
    echo "=== Skill 使用日报（最近 $days 天）==="
    echo ""
    
    echo "📊 每日调用次数 Top 10:"
    if [ -f "$JSONL_FILE" ]; then
        jq -s --argjson d "$days" '
            map(select(.ts >= (now - ($d * 86400)) * 1000)) |
            group_by(.skill) |
            map({skill: .[0].skill, count: length}) |
            sort_by(-.count) | .[0:10]
        ' "$JSONL_FILE" 2>/dev/null || grep -o '"skill":"[^"]*"' "$JSONL_FILE" | sort | uniq -c | sort -rn | head -10
    fi
    
    echo ""
    echo "📈 每日趋势:"
    if [ -f "$JSONL_FILE" ]; then
        awk -F'"ts":"|T' '{print $2}' "$JSONL_FILE" | sort | uniq -c | tail -10
    fi
    
    echo ""
    echo "⚡ 使用客户端分布:"
    if [ -f "$JSONL_FILE" ]; then
        grep -o '"client":"[^"]*"' "$JSONL_FILE" | sort | uniq -c | sort -rn
    fi
    
    echo ""
    echo "📁 数据源: $JSONL_FILE"
    echo "📦 记录总数: $(wc -l < "$JSONL_FILE" 2>/dev/null || echo 0)"
}

case "$1" in
    log)
        log_usage "$2" "$3" "$4" "$5"
        ;;
    report)
        report_usage "$2"
        ;;
    *)
        echo "用法:"
        echo "  $0 log <skill-name> [--trigger manual|auto] [session-id] [duration_ms]"
        echo "  $0 report [--days N]"
        exit 1
        ;;
esac
```

**安装:**
```bash
mkdir -p ~/.trae-cn/scripts
# 写入上述脚本
chmod +x ~/.trae-cn/scripts/skill-usage.sh
```

---

### Step 2：Agent 工具调用层自动埋点（已可执行）

**方式:** 每次调用 `Skill` 工具时，同时追加 JSONL 记录。

**实际上，我们在做测试的时候已经在手动记录了——让它变成自动化习惯。**

---

### Step 3：SKILL.md 自引导埋点（可选）

**方式:** 在每个 SKILL.md 的"核心功能"章节开头加一段引导：

```markdown
## 核心功能

> **📊 使用监控（自动执行）：** 在执行下面的任何功能之前，先运行：
> ```bash
> ~/.trae-cn/scripts/skill-usage.sh log common-test-driven-development --trigger auto
> ```

（然后是 skill 的正文内容...）
```

**要不要加？** 可选。加了能捕获 Agent 自动触发 skill 的场景，但会增加 prompt token 消耗。建议：
- **经常被自动触发的 skill**（如 common-coding、personal-brief-style）→ 加
- **手动调用的专项 skill**（如 common-design-brainstorming）→ 不加

---

### Step 4：增强 validate-skills.sh（可选）

**方式:** 在每个 skill 验证成功后追加：

```bash
# 在 validate_skill() 函数成功返回前加：
~/.trae-cn/scripts/skill-usage.sh log "$skill_name" --trigger validation
```

---

## 四、使用方式

### 日常使用

**Agent 自己埋点——不用用户做任何事：**
- 每次 Skill 工具调用 = 自动有一条 JSONL

### 手动记录（如果 Agent 没自动做）

```bash
~/.trae-cn/scripts/skill-usage.sh log common-test-driven-development --trigger manual
```

### 看日报

```bash
~/.trae-cn/scripts/skill-usage.sh report --days 7
```

### 分析原始数据

```bash
# 每天每个 skill 调用次数
cat ~/.trae-cn/skill-usage.jsonl | jq -s 'group_by(.ts[0:10], .skill) | ...'

# 最常被自动触发的 skill
cat ~/.trae-cn/skill-usage.jsonl | grep '"trigger":"auto"' | jq -r '.skill' | sort | uniq -c | sort -rn

# 最常被手动调用的 skill（用户主动说"用 xxx skill"）
cat ~/.trae-cn/skill-usage.jsonl | grep '"trigger":"manual"' | jq -r '.skill' | sort | uniq -c | sort -rn

# 错误记录
cat ~/.trae-cn/skill-usage.jsonl | grep '"status":"error"'
```

---

## 五、与大厂方案对比（在 Trae 场景下）

| 对比项 | GitHub Copilot Metrics | Cursor Analytics | 我们的方案 |
|--------|----------------------|------------------|-----------|
| **是否需要厂商支持** | 是（Enterprise 版） | 是（Enterprise 版） | **否** |
| **能否捕获 Skill 调用** | ✅ 2026/9 刚加 | ✅ 专门端点 | ✅ Agent 自埋点 |
| **能否捕获自动触发** | ✅ IDE 原生 | ✅ IDE 原生 | ⚠️ 需 SKILL.md 引导 |
| **数据存储位置** | 厂商云端 | 厂商云端 | **本地 JSONL** |
| **可定制性** | 厂商固定字段 | 厂商固定字段 | **完全自由** |
| **部署成本** | $$$ Enterprise | $$$ Enterprise | **零** |
| **客户端识别** | IDE 原生 | IDE 原生 | ✅ 我们自己加 |
| **耗时统计** | ✅ | ✅ | ✅ Agent 自记录 |
| **错误统计** | ✅ | ✅ | ✅ |
| **Skill 间调用链** | ❌ | ❌ | ⚠️ 需 session 关联 |
| **升级路径** | 等厂商更新 | 等厂商更新 | **JSONL → Langfuse OTel → Grafana** |

---

## 六、后续升级路径

当本地 JSONL 积累到一定量（比如 > 1000 条），可以考虑升级：

### 升级 1：对接 Langfuse OTel endpoint

```bash
# 在 skill-usage.sh 里加：
curl -X POST https://api.langfuse.com/api/public/otel \
  -H "Authorization: Bearer $LANGFUSE_PUBLIC_KEY" \
  -d "$(cat ~/.trae-cn/skill-usage.jsonl)"
```

Langfuse 免费 tier 1M traces/月够用。

### 升级 2：Prometheus + Grafana

把 JSONL 转成 Prometheus metric：

```prometheus
skill_usage_total{skill="common-tdd",client="trae-macos",trigger="auto"} 125
skill_usage_duration_seconds{skill="common-tdd",quantile="0.95"} 0.34
```

Grafana Dashboard：每天 skill 调用趋势 + Top 10 skill + 错误率面板。

### 升级 3：写一个 Skill 来监控 Skill

创造一个 `common-skill-monitor` Skill：
- 功能：自动读取 JSONL → 生成日报 → 如果某个 skill 一周没被调用就提示
- 触发条件：每天一次 cron 或用户说"看看 skill 用量"

**完全自洽**——用 Skill 监控 Skill。

---

## 七、风险与限制

| 风险 | 影响 | 应对 |
|------|------|------|
| Agent 忘记自埋点 | 数据不完整 | Step 3 在 SKILL.md 里加强制引导 |
| 自动触发的 skill 没被记录 | 低估自动触发率 | SKILL.md 开头加引导 |
| JSONL 无去重 | 同一次调用可能多条 | session_id + 去重逻辑 |
| 本地文件可能被删 | 数据丢失 | 可选：每天 cp 一份到仓库或云上 |
| session_id 不稳定 | 调用链追踪难 | 暂时不追，够用就行 |

---

## 八、验证标准

实施完成后验证：

- [ ] `skill-usage.sh` 能 log 和 report
- [ ] Agent 工具调用层能自动追加 JSONL
- [ ] `skill-usage.jsonl` 有真实数据（≥ 20 条）
- [ ] `report --days 7` 能出有意义的统计
- [ ] jq 查询能正确聚合 skill 调用次数
