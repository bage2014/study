# 快速上手（5 分钟版）

> 从零到看到 Dashboard 数据，**5 步搞定**。

---

## 前置条件

| 需要 | 检查命令 |
|------|----------|
| JDK 21+ | `java -version` |
| Maven 3.8+ | `mvn -version` |
| macOS（launchd 是 macOS 原生的） | — |

---

## Step 1：启动 Spring Boot（30s）

```bash
cd monitor/skill-monitor-server
mvn spring-boot:run
```

看到这行就 OK 了：
```
Started SkillMonitorApplication in 6.204 seconds
Tomcat started on port 8090
```

验证：
```bash
curl http://localhost:8090/api/skill-usage/health
# → {"status":"ok","time":...}
```

---

## Step 2：部署脚本（10s）

新开一个 Terminal（Spring Boot 那个别关）：

```bash
# 拷贝脚本到运行时位置
cp monitor/scripts/skill-usage.sh ~/.trae-cn/scripts/
chmod +x ~/.trae-cn/scripts/skill-usage.sh
```

---

## Step 3：安装分钟级 cron（10s）

```bash
~/.trae-cn/scripts/skill-usage.sh start-cron
```

输出类似：
```
cron installed (every 60s)
  plist: ~/Library/LaunchAgents/com.studyskills.skill-monitor.plist
```

验证 cron 在跑：
```bash
launchctl list com.studyskills.skill-monitor
# 有输出就 OK（可能要等几秒）
```

---

## Step 4：造点数据（30s）

```bash
# 手动造几条
~/.trae-cn/scripts/skill-usage.sh log common-test-driven-development --trigger auto
~/.trae-cn/scripts/skill-usage.sh log common-visual-design --trigger manual
~/.trae-cn/scripts/skill-usage.sh log common-systematic-debugging --trigger auto

# 等 cron 自动上传（最多 60s），或者手动触发一次
~/.trae-cn/scripts/skill-usage.sh upload
```

手动 upload 输出：
```
uploading 3 lines (offset=0 -> 3) to http://localhost:8090/api/skill-usage/batch
ok (HTTP 200), offset -> 3
```

---

## Step 5：看 Dashboard（1min）

浏览器打开：**http://localhost:8090/**

应该能看到：
- 总调用次数：3
- 涉及 Skill 数：3
- Top Skill 条形图
- 触发方式分布（auto: 2, manual: 1）
- 最新记录

**同时打开 H2 Console 看原始数据：** http://localhost:8090/h2-console

连接参数：
| 参数 | 值 |
|------|---|
| JDBC URL | `jdbc:h2:file:~/skill-monitor/data` |
| User Name | `sa` |
| Password | （空） |

进去后跑：
```sql
SELECT * FROM SKILL_USAGE ORDER BY TS DESC LIMIT 10;
```

---

## 5 步回顾

```
Step 1  mvn spring-boot:run          # Spring Boot on :8090
Step 2  cp skill-usage.sh ~/...      # 脚本就位
Step 3  start-cron                   # launchd 每分钟上传
Step 4  log + upload                 # 造数据 + 上传
Step 5  浏览器看 Dashboard            # http://localhost:8090/
```

---

## 日常使用 Checklist

| 场景 | 做什么 |
|------|--------|
| 开发完 Spring Boot 后重启 | `mvn spring-boot:run` |
| 忘了 cron 是否在跑 | `launchctl list com.studyskills.skill-monitor` |
| 想重置所有数据 | `rm ~/skill-monitor/data* ~/.trae-cn/skill-usage.jsonl ~/.trae-cn/skill-upload.offset` |
| 想看原始 JSONL | `cat ~/.trae-cn/skill-usage.jsonl \| jq .` |
| 想手动触发上传 | `~/.trae-cn/scripts/skill-usage.sh upload` |
| 想看 cron 错误 | `tail -f ~/.trae-cn/skill-upload.err` |

---

## 常见坑

### "服务不可达"
→ Spring Boot 没启动。回到 Step 1。

### "skip: offset >= total"
→ 没有新数据。先 `skill-usage.sh log xxx --trigger auto` 造一条。

### H2 Console 连不上
→ JDBC URL 要跟 `application.yml` 里的完全一致。默认是 `jdbc:h2:file:~/skill-monitor/data`（注意没有后缀 `.mv.db`）。

### Dashboard 空白
→ 数据还没上传。手动跑一次 `skill-usage.sh upload`。

### 换了 HOME 目录后找不到数据
→ H2 路径是 `${HOME}/skill-monitor/data`，会自动跟随。重启 Spring Boot 就行。

---

## 下一步

看完了？去看完整文档：

- [README.md](./README.md) — 功能清单、REST API、维护命令
- [ARCHITECTURE.md](./ARCHITECTURE.md) — 为什么这样设计、Trae 架构适配分析
