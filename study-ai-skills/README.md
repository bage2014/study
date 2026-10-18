# study-ai-skills

AI Skill 研究与运维仓库：一套可跨 IDE 部署的 AI 技能库 + 技能使用监控系统。

## 目录结构

```
study-ai-skills/
├── skills/                  # 技能源码（personal-* 个人定制 / common-* 通用包装）
├── scripts/                 # 技能管理脚本
│   ├── skills.sh            #   技能管理：install / update / validate / list / inject
│   ├── install-skills.sh    #   底层安装脚本
│   ├── install-global.sh    #   全局命令安装
│   ├── install-skills.ps1   #   Windows 安装脚本
│   └── validate-skills.sh   #   技能规范校验
├── monitor/                 # 技能使用监控系统（监控相关全部在此目录）
│   ├── skill-monitor-server/  # Spring Boot 3 服务（8090：Dashboard + REST API + H2）
│   └── scripts/               # monitor.sh（服务启停/状态入口）、skill-usage.sh、inject-monitor-hook.sh
└── docs/                    # 变更记录 / Spec / 报告
```

## 快速开始

```bash
# 技能管理
./scripts/skills.sh install          # 安装全部技能到 ~/.trae-cn/skills/
./scripts/skills.sh update           # 全量更新（覆盖 + 重注入监控埋点）
./scripts/skills.sh validate         # 校验技能规范

# 监控系统
./monitor/scripts/monitor.sh start   # 启动监控服务（localhost:8090）
./monitor/scripts/monitor.sh status  # 服务/Cron/数据管道状态
./monitor/scripts/monitor.sh restart # 重启
open http://localhost:8090/          # Dashboard
```

## 文档索引

| 文档 | 内容 |
|------|------|
| [AGENTS.md](AGENTS.md) | AI 编码助手协作规范（必读） |
| [SKILLS.md](SKILLS.md) | 技能清单与安装说明 |
| [monitor/README.md](monitor/README.md) | 监控系统架构、API、脚本命令 |
| [monitor/DASHBOARD.md](monitor/DASHBOARD.md) | Dashboard 使用说明 |
| [monitor/QUICKSTART.md](monitor/QUICKSTART.md) | 监控系统快速上手 |
| [monitor/ARCHITECTURE.md](monitor/ARCHITECTURE.md) | 监控系统架构设计 |
| docs/ | 变更记录 / Spec / 报告（`{module}-{content}-{date}.md`） |

## 监控链路

```
SKILL.md 内嵌埋点指令 → Agent 执行 skill-usage.sh log（写 JSONL）
  → launchd Cron 每 60s 增量上传 → Spring Boot 入库（H2）
  → Dashboard 展示（调用量 / Top Skill / 趋势 / 系统运维）
```
