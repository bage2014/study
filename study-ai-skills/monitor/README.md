# Trae Skill 使用监控系统

> 对 Trae IDE 中 Skill 的调用进行采集、存储和可视化。  
> 技术栈：**Spring Boot 3.4 + H2 File DB + Thymeleaf Dashboard + 本地 JSONL + launchd cron（分钟级上传）**

---

## 目录结构

```
monitor/
├── skill-monitor-server/          # Spring Boot 后台服务
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/studyskills/monitor/
│       │   ├── SkillMonitorApplication.java        # 启动类
│       │   ├── entity/SkillUsage.java              # 实体（JPA）
│       │   ├── repository/SkillUsageRepository.java# JPA Repository
│       │   ├── service/SkillUsageService.java      # 业务逻辑 + 聚合查询
│       │   └── controller/
│       │       ├── SkillUsageController.java       # REST API
│       │       └── DashboardController.java        # 页面路由
│       └── resources/
│           ├── application.yml
│           └── templates/dashboard.html            # Thymeleaf Dashboard
│
├── scripts/
│   └── skill-usage.sh             # 客户端埋点 + 上传脚本（已部署到 ~/.trae-cn/scripts/）
│
├── README.md                       # 本文档（使用说明）
├── ARCHITECTURE.md                 # 实现思路 & 设计决策
└── QUICKSTART.md                   # 快速上手 5 分钟版
```

---

## 功能清单

| # | 功能 | 说明 | 入口 |
|---|------|------|------|
| 1 | **JSONL 采集** | Agent 每次 Skill 工具调用 → 追加一行 JSONL | `skill-usage.sh log` |
| 2 | **分钟级上传** | launchd cron 每分钟增量上传到 Spring Boot | `skill-usage.sh start-cron` |
| 3 | **断点续传** | `skill-upload.offset` 记录上次位置，上传失败不丢数据 | 自动 |
| 4 | **去重存储** | H2 唯一索引 `dedupKey` + JPA `existsByDedupKey` | 自动 |
| 5 | **REST API** | batch upload / overview / top / daily-trend / trigger-dist / client-dist / recent | `/api/skill-usage/*` |
| 6 | **Dashboard 页面** | 4 张总览卡片 + Top Skill + 触发方式 + 客户端分布 + 每日趋势 + 最新记录 | `GET /` |
| 7 | **H2 控制台** | 直接 SQL 查询 | `GET /h2-console` |

---

## 快速开始（3 步）

### Step 1：启动 Spring Boot（端口 8090）

```bash
cd monitor/skill-monitor-server
mvn spring-boot:run
# 看到 Started SkillMonitorApplication in X.XXX seconds 即成功
```

### Step 2：拷贝脚本 + 安装 cron

```bash
# 拷贝脚本到 ~/.trae-cn/scripts/
cp monitor/scripts/skill-usage.sh ~/.trae-cn/scripts/
chmod +x ~/.trae-cn/scripts/skill-usage.sh

# 安装 macOS launchd cron（每分钟自动上传）
~/.trae-cn/scripts/skill-usage.sh start-cron
```

### Step 3：使用 Skill，看 Dashboard

- **Dashboard:** http://localhost:8090/
- **H2 Console:** http://localhost:8090/h2-console （JDBC URL: `jdbc:h2:file:~/skill-monitor/data`，user: `sa`，password 空）

---

## 脚本命令参考

> 统一入口：`monitor/scripts/monitor.sh`（包装下方 skill-usage.sh：`start|stop|restart|status|cron-start|cron-stop|deploy|log`），推荐优先使用。仓库根 `scripts/skills.sh` 负责技能安装/更新/注入。

### skill-usage.sh

```bash
# 1. 记录一条（Agent 调用 Skill 时自动执行）
skill-usage.sh log <skill-name> [--trigger auto|manual] [--event tool_load|script_log|validation]
# 示例: skill-usage.sh log common-tdd --trigger auto

# 2. 手动触发上传（一般不需要，cron 会每分钟跑）
skill-usage.sh upload

# 3. 本地分析（不依赖 Spring Boot）
skill-usage.sh report

# 4. 状态总览（服务 + cron + 数据管道）
skill-usage.sh status

# 5. Spring Boot 生命周期管理
skill-usage.sh start-server     # nohup mvn spring-boot:run，等待 health 通过
skill-usage.sh stop-server      # pkill + 等待端口释放
skill-usage.sh restart-server

# 6. 管理 cron
skill-usage.sh start-cron     # 安装 launchd plist（60s 间隔）
skill-usage.sh stop-cron      # 卸载
```

### 环境变量

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `SKILL_MONITOR_URL` | `http://localhost:8090` | Spring Boot 服务地址 |
| `SKILL_MONITOR_DIR` | `~/bage/github/study/study-ai-skills/monitor/skill-monitor-server` | start-server 使用的工程目录 |

### 数据文件（默认位置）

| 文件 | 说明 |
|------|------|
| `~/.trae-cn/skill-usage.jsonl` | 原始 JSONL 记录（追加式） |
| `~/.trae-cn/skill-upload.offset` | 上传断点（已上传到第几行） |
| `~/.trae-cn/skill-upload.log` | launchd cron 输出日志 |
| `~/.trae-cn/skill-upload.err` | launchd cron 错误日志 |
| `~/skill-monitor/data` | H2 File DB（Spring Boot 配置） |

---

## REST API

### POST /api/skill-usage/batch

**批量上传 JSONL（curl 的时候用）**

```bash
curl -X POST http://localhost:8090/api/skill-usage/batch \
  -H "Content-Type: text/plain" \
  --data '{"ts":"2026-10-18T10:00:00Z","skill":"common-tdd","event":"tool_load","client":"trae-macos","session":"no-session","duration_ms":0,"status":"ok","trigger":"auto","user":"bage"}'
```

**返回**

```json
{"status":"ok","received":1,"saved":1,"skipped":0}
```

### GET /api/skill-usage/overview?days=7

```json
{
  "totalCalls": 12,
  "uniqueSkills": 6,
  "errorCount": 1,
  "errorRate": "8.3%",
  "timeRange": {"start": "...", "end": "..."}
}
```

### GET /api/skill-usage/top?days=7&topN=10

```json
[{"skillName": "common-tdd", "count": 5}, ...]
```

### GET /api/skill-usage/daily-trend?days=14

零填充：无数据的日期返回 0，保证趋势图连续。

```json
[{"date": "2026-10-05", "count": 0}, ..., {"date": "2026-10-18", "count": 8}]
```

### GET /api/skill-usage/trigger-dist?days=7

```json
[{"triggerType": "auto", "count": 8}, {"triggerType": "manual", "count": 4}]
```

### GET /api/skill-usage/client-dist?days=7

```json
[{"client": "trae-macos", "count": 12}]
```

### GET /api/skill-usage/top-projects?days=7&topN=8&user=xxx

Top N 项目（次数 + 涉及 Skill 数），可按用户过滤。

```json
[{"projectName": "study-ai-skills", "count": 20, "skills": 5}]
```

### GET /api/skill-usage/top-users?days=7&topN=8&project=xxx

Top N 用户（次数 + 涉及项目数），可按项目过滤。

```json
[{"userId": "bage", "count": 35, "projects": 2}]
```

### GET /api/skill-usage/filters

筛选下拉选项（全部去重用户 + 项目）。

```json
{"users": ["bage"], "projects": ["study-ai-skills", "study-ai-skills-test"]}
```

### 通用过滤参数

`overview / top / daily-trend / trigger-dist / client-dist / recent / recent-by-user` 均支持可选参数 `user`、`project`（可单独或组合使用），用于 Dashboard 的全局筛选联动。

### GET /api/skill-usage/recent?limit=20

返回最新 N 条 `SkillUsage` 实体（JSON，含 `userId` / `projectName` 字段）。

### GET /api/skill-usage/recent-by-user?limit=30

最新记录按用户分组（组间、组内均按时间倒序）。

```json
[{"userId": "bage", "records": [{"skillName": "common-coding", "projectName": "study-ai-skills", ...}]}]
```

### GET /api/skill-usage/health

```json
{"status": "ok", "time": 1792290405413}
```

### 系统运维 API（SystemOpsController）

| 方法 | 端点 | 说明 |
|------|------|------|
| GET | `/api/skill-usage/system/status` | 服务/Cron/脚本/数据管道状态聚合 |
| POST | `/api/skill-usage/system/stop` | 关闭服务（先响应，1-2s 后停止） |
| POST | `/api/skill-usage/system/restart` | 重启服务（detached，约 20-40s 恢复） |
| POST | `/api/skill-usage/system/cron/start` | 安装 launchd Cron |
| POST | `/api/skill-usage/system/cron/stop` | 卸载 Cron |

关闭/重启实际委托给 `~/.trae-cn/scripts/skill-usage.sh`（detached 后台执行，服务可自行关闭/重启）。Dashboard 首页"🩺 系统状态 & 运维"面板可直接查看状态并点击操作。

---

## Dashboard 页面

**URL:** http://localhost:8090/

| 区域 | 内容 |
|------|------|
| 顶部导航 | 时间范围切换（今日 / 7 / 30 / 90 天） |
| Overview 卡片 | 总调用次数 / 涉及 Skill 数 / 错误次数 / 错误率 |
| 系统状态 & 运维 | 服务/Cron/脚本/数据管道状态 + 刷新/重启/关闭服务 + Cron 启停按钮 |
| Top Skill | 横向条形图，Top N |
| 触发方式 & 客户端 | 左右两列：自动 vs 手动 / trae-macos 分布 |
| 每日趋势 | 最近 14 天柱状图 |
| 最新记录 | 时间 / Skill / 事件 / 客户端 / 触发 / 状态 |

---

## H2 File DB 配置

Spring Boot 启动时自动创建。关键配置在 `application.yml`：

```yaml
spring:
  datasource:
    # DB_CLOSE_DELAY=-1 防止连接关闭时丢数据
    url: jdbc:h2:file:${HOME}/skill-monitor/data;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true        # http://localhost:8090/h2-console
  jpa:
    hibernate:
      ddl-auto: update     # 自动建表
```

**H2 Console 连接参数：**

| 参数 | 值 |
|------|---|
| JDBC URL | `jdbc:h2:file:~/skill-monitor/data` |
| User Name | `sa` |
| Password | （空） |

---

## 维护命令

```bash
# 停止 Spring Boot
pkill -f SkillMonitorApplication

# 清空所有数据（⚠️ 危险）
rm ~/skill-monitor/data.mv.db ~/skill-monitor/data.trace.db

# 重置上传 offset（强制全量重新上传）
rm ~/.trae-cn/skill-upload.offset

# 查看 cron 是否在跑
launchctl list com.studyskills.skill-monitor

# 查看 cron 日志
tail -f ~/.trae-cn/skill-upload.log

# 查看 JSONL 原始数据
cat ~/.trae-cn/skill-usage.jsonl | head -20
wc -l ~/.trae-cn/skill-usage.jsonl
```

---

## 常见问题

### Q: Dashboard 500？
确认 Spring Boot 启动成功（`curl http://localhost:8090/api/skill-usage/health` 返回 ok），然后看 `mvn spring-boot:run` 的日志找 ERROR。

### Q: H2 数据重启后丢了？
检查 `application.yml` 的 datasource URL 是不是 `jdbc:h2:file:` 开头（不是 `jdbc:h2:mem:`）。`DB_CLOSE_DELAY=-1` 也要保留。

### Q: 上传报错 "服务不可达"？
先启动 Spring Boot（`mvn spring-boot:run`），再执行 `skill-usage.sh upload`。

### Q: 换了机器 / 换了 HOME 目录？
改 `SKILL_MONITOR_URL` 环境变量即可。H2 数据库路径 `${HOME}/skill-monitor/data` 会自动跟随。

---

## 参考文档

| 文档 | 说明 |
|------|------|
| [ARCHITECTURE.md](./ARCHITECTURE.md) | 实现思路、设计决策、Trae 架构适配分析 |
| [QUICKSTART.md](./QUICKSTART.md) | 5 分钟快速上手 |
| [skill-monitoring-trae-design-2026-10-18.md](../docs/spec/skill-monitoring-trae-design-2026-10-18.md) | Trae 适配方案设计（SPEC） |
| [skill-usage-monitoring-research-2026-10-18.md](../docs/spec/skill-usage-monitoring-research-2026-10-18.md) | 大厂方案 + 国外经典案例调研报告 |
