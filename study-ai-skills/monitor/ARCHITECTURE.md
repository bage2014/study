# 实现思路 & 设计决策

> 本文档讲清楚 **为什么** 这个监控系统是这样设计的——不是"怎么做"（看 README），而是"为什么这么做"。

---

## 一、核心问题：Trae Skill 的本质

在 Trae IDE 里，Skill **不是可执行程序**，而是 **Agent 对话时注入的 prompt 片段**：

```
用户说："帮我写测试"
  → Agent 调用 Skill 工具（读取 SKILL.md）
  → Agent 把 SKILL.md 的内容注入上下文
  → Agent 按 SKILL.md 里的指导去调 Shell / Grep / Edit / WebFetch
  → 结束。没有任何 Python/JS runtime 执行过。
```

这个本质决定了：

| 大厂方案 | 为什么在 Trae 下不行 |
|----------|---------------------|
| LangSmith / Langfuse @observe | Skill 没有 Python runtime 可以包装饰器 |
| OTel + Prometheus + Grafana | 没有 HTTP server 可以暴露 metrics |
| MCP Server 包装 Skill | 架构侵入大，跟 Trae Skill 机制冲突 |
| IDE 内置 telemetry | 搜不到 Trae 有这个能力 |

所以我们的方案必须 **绕过 Skill 本身没有 runtime 这个事实**——在 **Skill 调用发生的那个瞬间** 由外部记录。

---

## 二、三层埋点架构

```
┌───────────────────────────────────────────────────────────────────┐
│                    Trae Skill 监控：三层埋点                         │
├───────────────────────────────────────────────────────────────────┤
│                                                                   │
│ Layer 1: Agent 自己（最精准）                                       │
│ ────────────────────────────                                       │
│ Agent 每次调用 Skill 工具时                                          │
│   → 调用 Skill 工具前，自己追加一条 JSONL                            │
│   → 我们自己就是 Agent，所以能做到                                   │
│                                                                   │
│ Layer 2: SKILL.md 自引导（捕获自动触发）                              │
│ ──────────────────────────────────────                              │
│ 每个 SKILL.md 开头可以加：                                           │
│   "执行前先跑 ~/.trae-cn/scripts/skill-usage.sh log {skill-name}"  │
│   （可选，当前项目没加——保持 SKILL.md 纯粹）                          │
│                                                                   │
│ Layer 3: validate-skills.sh 增强（周期性扫描）                       │
│ ──────────────────────────────────────                              │
│ 每次跑 validate-skills.sh 时                                        │
│   → 顺便记录每个 skill 的最后验证时间                                 │
│   （可选，当前没加）                                                 │
│                                                                   │
│ ⚠️ 核心洞察：Layer 1 已经够用，Layer 2/3 是增强。                   │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘
```

---

## 三、数据流设计

### 3.1 端到端

```
Agent 调用 Skill
    │
    ▼
skill-usage.sh log (追加 JSONL)
    │
    ▼
~/.trae-cn/skill-usage.jsonl  ← 纯追加，不修改已写入行
    │
    ▼
launchd cron (每 60s)
    │
    ▼
skill-usage.sh upload (读 offset → 增量 curl POST)
    │
    ▼
Spring Boot REST API (/api/skill-usage/batch)
    │
    ▼
H2 File DB (dedupKey 唯一索引防重复)
    │
    ▼
Thymeleaf Dashboard (GET /)
```

### 3.2 为什么用 JSONL（不是 SQLite / 直接写 H2）

| 方案 | 我们选的？ | 原因 |
|------|:---------:|------|
| JSONL 文件 | ✅ | ① Agent 埋点只需一行 `cat >> file`，零依赖 ② 人类可读 ③ 可 `grep` 过滤 ④ 后续可以解析导入任何后端 |
| 直接写 H2（JDBC） | ❌ | Agent 不能调 JDBC；需要 Java runtime，跟 Skill prompt-first 架构不兼容 |
| SQLite | ❌ | 需要 sqlite3 CLI，不是所有机器都有 |
| 直接调 Spring Boot API | ❌ | Agent 没有 curl 权限；Spring Boot 还没启动就写不了 |

**JSONL 是最佳中间格式**——任何语言都能读，任何后端都能 ingest。

### 3.3 为什么用 launchd（不是 crontab）

| 方案 | 我们选的？ | 原因 |
|------|:---------:|------|
| macOS launchd | ✅ | macOS 原生，开机自动，RunAtLoad，不用装 cron |
| crontab | ❌ | macOS 上 cron 不稳定，系统更新后可能失效 |
| Node/Java 定时任务 | ❌ | 为了跑一个定时上传还要起一个常驻进程，重了 |

### 3.4 为什么用 H2 File DB（不是 MySQL/Postgres）

| 方案 | 我们选的？ | 原因 |
|------|:---------:|------|
| H2 File 模式 | ✅ | ① 单文件，`jdbc:h2:file:~/skill-monitor/data` ② 零配置，Spring Boot 自动创建 ③ 重启不丢数据（关键：加 `DB_CLOSE_DELAY=-1`） ④ 自带 Web Console |
| MySQL | ❌ | 要装 MySQL Server、要配用户、要持久化到磁盘，对单机自用太重 |
| PostgreSQL | ❌ | 同 MySQL |
| SQLite + Java 驱动 | ⚠️ | 也可以，但 H2 跟 Spring Boot/JPA 集成更好 |

---

## 四、去重策略

同一个 JSONL 可能被上传多次（cron 重试、手动 upload 等），但 H2 里应该只有一条。

**去重键：** `dedupKey = ts + "|" + skillName + "|" + client + "|" + eventType + "|" + sessionId`

**两层保护：**

| 层 | 位置 | 机制 |
|----|------|------|
| 1 | SkillUsage 实体 | `@Column(unique = true, name = "dedupKey")` + `@PrePersist` 自动生成 |
| 2 | SkillUsageService | 保存前 `repo.existsByDedupKey(key)` 判断，跳过重复 |

这样 JSONL 可以自由重放（比如 `rm skill-upload.offset` 强制全量重传），数据库不会有重复。

---

## 五、断点续传

**问题：** Spring Boot 没启动时，cron 上传失败了。启动后要能补传。

**方案：**

```
~/.trae-cn/skill-usage.jsonl       # 100 行
~/.trae-cn/skill-upload.offset    # 值为 80（上次成功上传到第 80 行）

cron 每次跑 upload:
  1. 读 offset = 80
  2. total = wc -l(jsonl) = 100
  3. 增量 = tail -n +81 jsonl → 20 行
  4. curl POST 20 行
  5. 成功 → 写 offset = 100
  6. 失败 → offset 还是 80，下次重试这 20 行
```

**为什么这么设计：**

- Agent 可以放心追加 JSONL，不用考虑上传状态——追加是单向的、幂等的
- upload 可以随时手动跑，也可以让 cron 自动跑——两者互不干扰
- Spring Boot 短暂不可用不丢数据，所有数据先堆在 JSONL 里

---

## 六、为什么选 Spring Boot + Thymeleaf（不是前后端分离）

| 对比项 | Spring Boot + Thymeleaf | Spring Boot + Vue/React |
|--------|:----------------------:|:----------------------:|
| 启动方式 | `mvn spring-boot:run`（一个进程） | Spring Boot 后端 + Node dev server（两个） |
| 部署 | 一个 jar 包 | jar + 前端打包产物 |
| 复杂度 | 低 | 中 |
| 适合场景 | ✅ 单机自用、内部监控 | 多用户、复杂交互 |
| 样式自由度 | 够用（CSS 内嵌在模板里） | 高 |
| 数据接口 | 页面直接渲染，也有 REST API | REST API only |

**结论：** 单机自用场景，Thymeleaf 最省事。如果后续要加复杂图表（ECharts）或多用户，可以升级成前后端分离——REST API 已经准备好了。

---

## 七、技术选型总结

| 组件 | 选型 | 一句话理由 |
|------|------|-----------|
| 客户端采集 | JSONL + bash | Agent 能调 bash，不能调 JDBC |
| 定时上传 | macOS launchd | 原生、开机自启、RunAtLoad |
| 后端框架 | Spring Boot 3.4 | 团队主力技术栈、有现成 skill |
| 数据库 | H2 File 模式 | 单文件、零配置、自带 Console |
| ORM | Spring Data JPA | 自动生成 CRUD、@Query 写聚合方便 |
| 页面模板 | Thymeleaf | 一个进程搞定，适合内部工具 |
| 图表 | CSS bar + 纯 div | 不用 ECharts，够直观 |
| 去重 | dedupKey 唯一索引 | 防止重放 JSONL 时数据重复 |
| 断点续传 | offset 文件 | 失败不丢，幂等重试 |

---

## 八、大厂方案对比（我们为什么选这条）

| 对比项 | GitHub Copilot Metrics | Cursor Analytics | **我们的方案** |
|--------|----------------------|------------------|--------------|
| 采集方式 | IDE telemetry SDK | IDE telemetry SDK | Agent 自埋点 + bash |
| 存储 | 厂商云端 | 厂商云端 | 本地 H2 File DB |
| 能否自建 | ❌ | ❌ | ✅ |
| 成本 | $$$ Enterprise | $$$ Enterprise | **零** |
| 分钟级上传 | ❓ | ❓ | ✅ launchd 60s |
| 重启不丢数据 | ✅ | ✅ | ✅ H2 File |
| 可定制字段 | ❌ 厂商固定 | ❌ 厂商固定 | ✅ 想加什么加什么 |
| 依赖 Skill runtime | ❌ | ❌ | ❌ **不需要** |

---

## 九、扩展路径

当前设计够用，但未来可以：

| 阶段 | 做什么 | 怎么改 |
|------|--------|--------|
| v1.1 | 对接 Langfuse | 在 `batchSave` 里加一个 HTTP 调用把 trace 推到 Langfuse OTel endpoint |
| v1.2 | 加 ECharts 图表 | Thymeleaf 里加 ECharts CDN，REST API 返回 JSON，前端渲染 |
| v2.0 | 前后端分离 | 保留 Spring Boot REST API，新建 Vue 项目调 API |
| v2.1 | 换成 MySQL | 改 `application.yml` 的 datasource URL 就行，JPA Repository 不用动 |
| v2.2 | Docker 化 | 写 Dockerfile + docker-compose.yml（Spring Boot + 可选 MySQL） |
| v3.0 | 多用户 | 加 `user_id` 过滤 + 登录鉴权（Spring Security） |

---

## 十、关键约束

| # | 约束 | 影响 |
|---|------|------|
| 1 | Trae Skill 没有 runtime | 决定了只能用 bash 脚本 + JSONL，不能用 LangSmith/Langfuse @observe |
| 2 | Agent 不能调 JDBC | 必须有一个中间格式（JSONL），再由另一个进程 ingest |
| 3 | macOS 环境 | launchd 比 crontab 更稳定 |
| 4 | 单机自用 | 不需要 MySQL/Postgres/Hazelcast/Kafka 这些重家伙 |
| 5 | Skill 使用量不会爆炸 | H2 单文件足够，不需要分片/归档 |
| 6 | 团队主力是 Java | Spring Boot 比 Go/Python 后端更顺 |
