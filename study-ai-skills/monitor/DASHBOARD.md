# Dashboard 使用说明

> **URL:** http://localhost:8090/  
> **入口:** 浏览器直接打开即可，无需登录  
> **数据源:** Spring Boot + H2 File DB（客户端 launchd 每分钟同步 JSONL）

---

## 页面布局总览

```
┌─────────────────────────────────────────────────────────┐
│  📊 Trae Skill 使用监控                                  │
│  ── 数据源说明                                          │
├─────────────────────────────────────────────────────────┤
│  [今日] [7天] [30天] [90天]   ← 时间范围切换导航        │
├─────────────┬─────────────┬──────────────┬─────────────┤
│ 总调用次数   │ 涉及 Skill   │ 错误次数      │ 错误率       │
│   (大数字)   │   (大数字)   │  (红色数字)   │  (红色%)     │
├─────────────┴─────────────┴──────────────┴─────────────┤
│                                                         │
│  🏆 Top Skill 使用量  │  🎯 触发方式 & 客户端            │
│  (横向条形图表格)     │  (两个小表格)                    │
│                       │                                 │
├───────────────────────┴─────────────────────────────────┤
│                                                         │
│  📈 每日调用趋势（最近 14 天）                              │
│  (竖向柱状图)                                             │
│                                                         │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  🕐 最新记录（20 条）                                      │
│  (表格：时间 / Skill / 事件 / 客户端 / 触发 / 状态)        │
│                                                         │
├─────────────────────────────────────────────────────────┤
│  Trae Skill Monitor · H2 Console · Health API           │
└─────────────────────────────────────────────────────────┘
```

---

## 1. 顶部导航

### 时间范围切换

| 按钮 | URL 参数 | 说明 |
|------|----------|------|
| 今日 | `?days=1` | 只看今天的记录 |
| 7天 | `?days=7` | 默认值 |
| 30天 | `?days=30` | 月度视角 |
| 90天 | `?days=90` | 季度视角 |

点击后页面所有数据（Overview、Top Skill、触发方式、客户端）都会按新时间范围重新聚合。

---

## 2. Overview 卡片（4 张）

| 卡片 | 含义 | 颜色 | 数据源 |
|------|------|------|--------|
| **总调用次数** | 选定时间范围内 Skill 被调用的总条数 | 白色大数字 | `COUNT(*)` WHERE ts BETWEEN |
| **涉及 Skill 数** | 选定范围内至少被调用过一次的不同 Skill 数量 | 🟢 绿色 | `COUNT(DISTINCT skill_name)` |
| **错误次数** | status = error 的记录数 | 🔴 红色 | `SUM(CASE WHEN status='error' THEN 1 END)` |
| **错误率** | 错误次数 / 总调用次数 × 100% | 🔴 红色 | 同上计算 |

### 怎么用

- 每天早上看一眼总调用次数——了解昨天 Skill 的活跃度
- 错误次数 > 0 就点 H2 Console 跑 `SELECT * FROM SKILL_USAGE WHERE STATUS='error'` 看是哪个 Skill 出问题
- 总调用次数少 + 涉及 Skill 数多 → Skill 分散使用，都没重度用
- 总调用次数多 + 涉及 Skill 数少 → 某几个 Skill 高频使用

---

## 3. 🏆 Top Skill 使用量

### 展示内容

| 列 | 说明 |
|----|------|
| Skill | Skill 名称（SKILL.md frontmatter 里的 name） |
| 次数 | 选定范围内的调用总数 |
| 占比 | 横向进度条（长度与次数成正比） |

### 数据来源

```sql
SELECT skill_name, COUNT(*) AS cnt
FROM skill_usage
WHERE ts BETWEEN :start AND :end
GROUP BY skill_name
ORDER BY cnt DESC
LIMIT 10
```

### 怎么用

- **找"明星 Skill"：** 条形最长的那几个是 Trae 里被调用最多的
- **找"僵尸 Skill"：** 如果 90 天视图里某个 Skill 一次都没出现 → 可以考虑删了或合并
- **条形比昨天短了：** 今天这个 Skill 用得少，可能是在忙别的

---

## 4. 🎯 触发方式 & 客户端

### 左半：触发方式

| 触发类型 | 图标 | 含义 |
|----------|------|------|
| manual | 👋 手动 | 用户明确说"用 xxx skill"或"加载 xxx" |
| auto | 🤖 自动 | Agent 根据用户意图自动触发（比如用户说"写测试"→ Agent 调用 TDD skill） |
| validation | — | validate-skills.sh 验证时记录 |
| script | — | 脚本显式 log 时记录 |

### 右半：客户端

| 客户端值 | 含义 |
|----------|------|
| trae-macos | Trae IDE（macOS） |
| script | 我们的 skill-usage.sh |
| 其他 | 可以自定义扩展 |

### 怎么用

- **自动 vs 手动 比例：** 如果 auto 远多于 manual → Skill 的自动触发机制生效得好。如果 manual 多 → 用户自己指定得多
- **客户端分布：** 单机自用会一直是 `trae-macos`。多设备后会出现多个值，能看出哪台机器最活跃

---

## 5. 📈 每日调用趋势

### 展示内容

- **最近 14 天** 的柱状图（跟导航选的 days 无关，固定 14 天）
- 每根柱子高度 = 当天调用次数
- 柱子底部标注日期（MM-DD 格式）

### 数据来源

```sql
SELECT FORMATDATETIME(ts, 'yyyy-MM-dd'), COUNT(*)
FROM skill_usage
WHERE ts BETWEEN :start AND :end
GROUP BY FORMATDATETIME(ts, 'yyyy-MM-dd')
ORDER BY 1 ASC
```

### 怎么用

- **看周内 vs 周末：** 如果周中柱子高、周末低 → 正常工作节奏
- **断崖式下跌：** 某一天柱子突然没了 → 那天 Trae 没开 / 数据没上传
- **持续上升：** 柱子一天比一天高 → Skill 体系在被越来越频繁地用

---

## 6. 🕐 最新记录

### 表格列说明

| 列 | 示例值 | 含义 |
|----|--------|------|
| 时间 | `2026-10-18T08:32:27Z` | ISO 8601 UTC 时间戳 |
| Skill | `common-test-driven-development` | Skill 名称 |
| 事件 | `tool_load` | 事件类型：tool_load / script_log / validation |
| 客户端 | `trae-macos` | 谁触发的 |
| 触发 | `自动` / `手动` | 对应 trigger 字段 |
| 状态 | `OK` (绿) / `ERROR` (红) | 执行状态 |

### 怎么用

- **刚用了 Skill 想确认上报了没：** 刷新页面 → 应该在最上面看到
- **排查某个时间点发生了什么：** 按时间列往下找
- **找 ERROR 记录：** 扫红色标签，或者去 H2 Console 跑 SQL

---

## 7. 页脚链接

| 链接 | URL | 用途 |
|------|-----|------|
| H2 Console | http://localhost:8090/h2-console | SQL 查询原始数据、手动跑报表 |
| Health API | http://localhost:8090/api/skill-usage/health | 检查 Spring Boot 服务健康状态 |

---

## 8. H2 Console（进阶）

**URL:** http://localhost:8090/h2-console

### 连接参数

| 参数 | 值 |
|------|---|
| JDBC URL | `jdbc:h2:file:~/skill-monitor/data`（注意：你的 HOME 目录） |
| User Name | `sa` |
| Password | （空） |

### 常用 SQL

```sql
-- 查所有表
SHOW TABLES;

-- 看原始记录（最新 20 条）
SELECT * FROM SKILL_USAGE ORDER BY TS DESC LIMIT 20;

-- 某个 Skill 的所有调用
SELECT TS, TRIGGER_TYPE, STATUS, DURATION_MS
FROM SKILL_USAGE
WHERE SKILL_NAME = 'common-test-driven-development'
ORDER BY TS DESC;

-- 错误记录
SELECT * FROM SKILL_USAGE WHERE STATUS = 'error';

-- 每天每个 Skill 的调用次数（明细）
SELECT FORMATDATETIME(TS, 'yyyy-MM-dd') AS DAY,
       SKILL_NAME,
       COUNT(*) AS CNT
FROM SKILL_USAGE
GROUP BY FORMATDATETIME(TS, 'yyyy-MM-dd'), SKILL_NAME
ORDER BY DAY DESC, CNT DESC;

-- 去重键冲突？查一下有没有残留
SELECT COUNT(*) AS CNT, DEDUP_KEY
FROM SKILL_USAGE
GROUP BY DEDUP_KEY
HAVING COUNT(*) > 1;
-- 正常应该返回 0 行
```

---

## 9. REST API（用 curl / Postman）

所有 API 都返回 JSON，适合写脚本做自定义报表。

| API | 参数 | 返回 |
|-----|------|------|
| `GET /api/skill-usage/health` | — | `{status: "ok", time: ...}` |
| `GET /api/skill-usage/overview?days=N` | days (默认 7) | totalCalls, uniqueSkills, errorCount, errorRate |
| `GET /api/skill-usage/top?days=N&topN=M` | days + topN (默认 10) | Top N skill 列表 |
| `GET /api/skill-usage/daily-trend?days=N` | days (默认 14) | 日期 → 次数 映射 |
| `GET /api/skill-usage/trigger-dist?days=N` | days (默认 7) | 触发方式分布 |
| `GET /api/skill-usage/client-dist?days=N` | days (默认 7) | 客户端分布 |
| `GET /api/skill-usage/recent?limit=N` | limit (默认 20) | 最新 N 条完整记录 |
| `POST /api/skill-usage/batch` | Body: JSONL 文本 | `{status, received, saved, skipped}` |

### curl 示例

```bash
# 总览（最近 30 天）
curl http://localhost:8090/api/skill-usage/overview?days=30 | jq

# Top 5 Skill
curl "http://localhost:8090/api/skill-usage/top?days=7&topN=5" | jq

# 写个脚本导出日报
curl -s http://localhost:8090/api/skill-usage/overview?days=7 \
  | jq '{totalCalls, uniqueSkills, errorRate}' \
  | mail -s "Skill 周报" your@email.com   # 假设你配置了 mail
```

---

## 10. 常见问题

### Q: 页面空白 / 全是 0？
**可能原因：** 数据没上来。
```bash
# 1. 确认 Spring Boot 在跑
curl http://localhost:8090/api/skill-usage/health

# 2. 确认 JSONL 有数据
wc -l ~/.trae-cn/skill-usage.jsonl

# 3. 手动触发一次上传
~/.trae-cn/scripts/skill-usage.sh upload
```

### Q: 数据延迟多久？
- **Agent 埋点 → JSONL：** 实时（追加文件是毫秒级）
- **JSONL → H2：** 最长 60 秒（cron 间隔）
- **刷新 Dashboard：** 页面静态渲染，刷新即拿到最新数据

### Q: 切换时间范围后数据没更新？
→ 强制刷新浏览器（Cmd+Shift+R）。Thymeleaf 模板有 cache: false，但浏览器可能有缓存。

### Q: Dashboard 和 H2 Console 都打不开？
→ Spring Boot 挂了。
```bash
# 重启
cd monitor/skill-monitor-server
mvn spring-boot:run
```

### Q: 想做个自定义报表（比如"谁最常在深夜用 Skill"）？
→ 用 H2 Console 写 SQL，或者调 REST API 用 Python/R 分析。REST API 是稳定接口，以后升级成前后端分离也不会变。

---

## 11. 数据字段字典

每个 Skill 调用记录包含：

| 字段 | 类型 | 说明 | 示例 |
|------|------|------|------|
| ts | ISO 8601 | 时间戳（UTC） | `2026-10-18T08:32:27Z` |
| skill | string | Skill 名称 | `common-test-driven-development` |
| event | enum | 事件类型 | `tool_load` / `script_log` / `validation` |
| client | string | 客户端 | `trae-macos` / `script` |
| session | string | 会话 ID | `no-session`（暂时未追踪） |
| duration_ms | number | 耗时（毫秒） | `45000`（暂未精确测量，默认 0） |
| status | enum | 执行状态 | `ok` / `error` |
| trigger | enum | 触发方式 | `manual` / `auto` / `validation` |
| user | string | 用户标识 | `bage` |
