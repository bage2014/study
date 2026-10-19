# AGENTS.md — study-ai-skills AI 协作规范

> 本文件面向 AI 编码助手（Qoder/Trae/Claude/Cursor 等），规范在本仓库工作时的行为约定。

## 1. 项目概述

本仓库是 **AI Skill 研究与运维仓库**，包含两大部分：

```
study-ai-skills/
├── skills/                  # Skill 源码目录（跨 IDE 兼容，部署到 ~/.trae-cn/skills/ 和 ~/.qoder-cn/skills/）
│   ├── personal-*           # 个人技能（基于 study-common 等内部代码库）
│   └── common-*             # 通用技能（多为外部技能的本地包装）
├── scripts/                 # 技能管理脚本（监控相关全部在 monitor/ 下）
│   ├── skills.sh              # 技能安装/更新/校验/注入统一入口（同时支持 Trae 和 Qoder）
│   ├── install-skills.sh      # 底层安装脚本（支持 -s 指定、-f 强制、--ide qoder/trae）
│   ├── install-global.sh      # 全局命令安装（skills-install 等）
│   ├── install-skills.ps1     # Windows 安装脚本
│   └── validate-skills.sh     # 技能规范校验
├── monitor/                 # Skill 使用监控系统（本仓库唯一 Java 工程，监控相关全在此目录）
│   ├── skill-monitor-server/  # Spring Boot 3 服务（端口 8090，Thymeleaf Dashboard + H2）
│   └── scripts/               # monitor.sh（服务启停/状态入口）、skill-usage.sh（埋点/上传/生命周期）、inject-monitor-hook.sh
└── docs/                    # 变更记录 / Spec / 报告（changelog、spec、report 子目录）
```

- **Skill 是 Markdown 文档，不是可执行插件**：Agent 读取 SKILL.md 后按其中流程执行。
- 监控链路：SKILL.md 内嵌埋点指令 → Agent 调 `skill-usage.sh log` 写 JSONL（含 ts/skill/user/**project** 等字段，project 自动从项目标记探测，支持 monorepo）→ launchd cron 每分钟增量上传 → Spring Boot 入库 → Dashboard（Vue 3 + API 驱动）展示。

## 2. 编码规则

### Skill 文件
- 每个 Skill 目录必须含 `SKILL.md`，Frontmatter 必须包含：`name`、`description`、`trigger`、`disable-when`、`category`。
- 命名前缀：个人技能 `personal-`，通用技能 `common-`。
- 新技能优先做成**本地包装技能**（引用外部源技能），并在 description 末尾注明外部来源；`requires` 字段引用已有内部技能。
- 修改 Skill 后需同步更新 `~/.trae-cn/skills/` 和 `~/.qoder-cn/skills/` 下的部署副本（或重新执行注入脚本）。

### Shell 脚本（monitor/scripts）
- `bash -n` 语法检查通过后再部署；`set -eo pipefail`。
- `pkill -f` 匹配自身命令时使用 `[i]` 括号技巧避免误杀自己。
- 长生命周期进程用 `nohup ... &` + health 探活循环，超时必须报错退出。
- 输出面向终端用户：每步打印一行状态，不输出调试噪音。

### Java（skill-monitor-server）
- Spring Boot 3 + Lombok `@RequiredArgsConstructor` 构造注入，不用字段注入。
- 遵守 `personal-backend-coding-standard` 技能；外部调用/IO 集中在 service 层。
- 不为单次操作造抽象；不过度设计（无多余配置项、兼容层）。
- 修改后必须重新编译部署验证（见第 4 节命令）。

### 文档
- 变更/Spec/报告存放在 `docs/{changelog|spec|report}/`，文件名 `{module}-{content}-{date}.md`。
- 禁止主动创建 README/文档，除非用户明确要求。

## 3. 技能使用指南

| 场景 | 使用技能 |
|------|----------|
| 写/改 Java 代码 | `personal-backend-coding-standard`、`personal-java-common` |
| 需求澄清 / 方案设计 | `common-requirement-clarification`、`common-design-brainstorming`、`personal-solution-research`（3-5 方案比对） |
| 调试 | `common-systematic-debugging`（根因优先，禁止盲试） |
| 写测试 | `common-test-driven-development` |
| 文档管理 | `personal-doc-manager`（docs 目录规范） |
| 服务启停 | `personal-project-runner`（保持原端口） |

- **调用任何 Skill 时必须执行埋点**：`~/.trae-cn/scripts/skill-usage.sh log <skill-name> --trigger auto`（SKILL.md 顶部埋点块为第 1 步指令）。若报 `Permission denied`/沙箱拦截，必须以非沙箱方式重试，禁止静默跳过。
- 技能冲突时优先 `personal-*`（项目强约束）。

## 4. 工具配置

### 常用命令

```bash
# ── 监控服务（统一入口 monitor/scripts/monitor.sh，端口 8090）──
./monitor/scripts/monitor.sh status          # 状态总览
./monitor/scripts/monitor.sh start|stop|restart
./monitor/scripts/monitor.sh cron-start      # launchd 60s 上传
./monitor/scripts/monitor.sh deploy          # 部署最新 skill-usage.sh 到 ~/.trae-cn/scripts/

# ── 技能管理（统一入口 scripts/skills.sh，同时安装到 Trae 和 Qoder）──
./scripts/skills.sh install          # 安装到 ~/.trae-cn/skills/ 和 ~/.qoder-cn/skills/
./scripts/skills.sh update           # 全量更新（-f 覆盖 + 重注入埋点，双平台）
./scripts/skills.sh validate         # 规范校验

# ── 底层 ──
monitor/scripts/skill-usage.sh       # 埋点/上传/生命周期原生命令
./scripts/validate-skills.sh         # 技能格式校验

# 服务端手工启动（需非沙箱环境，H2 文件 DB 受沙箱限制）
cd monitor/skill-monitor-server && mvn spring-boot:run

# Dashboard / API
open http://localhost:8090/
curl http://localhost:8090/api/skill-usage/system/status
```

### 环境与路径
- 服务地址：`SKILL_MONITOR_URL`（默认 `http://localhost:8090`）
- 工程目录：`SKILL_MONITOR_DIR`（默认本仓库 `monitor/skill-monitor-server`）
- 数据文件：`~/.trae-cn/skill-usage.jsonl`（埋点）、`~/.trae-cn/skill-upload.offset`（上传游标）
- 技能部署目录：`~/.trae-cn/skills/`（Trae）、`~/.qoder-cn/skills/`（Qoder）
- launchd：`~/Library/LaunchAgents/com.studyskills.skill-monitor.plist`（label `com.studyskills.skill-monitor`）

### AI 工具忽略文件
`.cursorignore`、`.codeiumignore`、`.aiderignore`、`.copilotignore` 属于项目标准的一部分，新增顶层大目录时需同步维护。

## 5. 安全规范

- 监控服务**只监听 localhost:8090**，禁止绑定 0.0.0.0 或暴露公网；运维 API（stop/restart/cron）不鉴权，因此必须保持本机限定。
- 埋点 JSONL 只记录 skill 名称/时间/客户端，**禁止写入代码内容、密钥、token 或用户数据**。
- 脚本只操作 `$HOME` 下白名单路径（`~/.trae-cn/`、`~/.qoder-cn/`、`~/Library/LaunchAgents/`）和本仓库目录，禁止递归扫描全盘。
- 外部调用（HTTP 上传）仅指向 `SKILL_MONITOR_URL`，不得引入其他网络出口。
- 提交前检查不引入 `.env`、credentials 等敏感文件。
