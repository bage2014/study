# 需求拆分：本地单机 AI 全自动研发流水线（个人开发者版）

## 基本信息

| 项目 | 内容 |
|------|------|
| Spec 编号 | SPEC-20261113-01 |
| 模块 | ai-dev-flow / pipeline-local |
| 上游依据 | [方案调研报告 RESEARCH-20261110-01](../report/ai-dev-flow-AI全自动研发流程方案调研-2026-11-10.md) |
| 作者 | bage |
| 创建日期 | 2026-11-13 |
| 状态 | 草稿（待评审，未开始实施） |
| 目标形态 | 调研报告的 **L3**（AI 自主交付 + 机器判定 + 自动回滚，人做抽检与策略） |

---

## 1. 目标与非目标

### 目标

在**这一台电脑、一个人**的条件下，跑通 G1–G8 全流程，使 AI 能在无人逐阶段审批的情况下完成「需求 → 澄清 → 评审 → 方案 → 编码 → 单测 → UI 测试 → 发布验证 → 缺陷回流」，并且**跳过环节在物理上不可能**。

### 非目标（明确不做）

| 不做 | 原因 |
|------|------|
| 阶段 0 需求源自动化（L5） | "需求是否值得做"无法机械化，调研报告 §12.1 已论证 |
| 云端 CI / K8s / 多租户 | 单机目标，且会引入密钥出域，违反 AGENTS.md §6 |
| 真实生产流量灰度 | 本地只能**仿真发布机制**；能验证逻辑正确性，不能验证真实故障分布（见 §8 声明） |
| 多栈/多仓泛化 | 全部规则锚定 Java21+SpringBoot3.4+Vue3.5，泛化需重估（调研报告 U6） |
| 自建 Agent 编排框架 | 调研报告方案 C 的"基建最重"对个人开发者更不成立；只用现成子代理 + git worktree |

---

## 2. 本机实测环境（所有约束的事实来源）

| 项 | 实测值 | 对设计的影响 |
|----|--------|--------------|
| CPU / 内存 | Apple M1 8 核 / 16 GB（当前 65% 空闲 ≈ 10 GB） | 并发必须限流；PIT `threads=2`、Playwright `workers=2` |
| 磁盘 | 460 GB，可用 **77 GB** | 可容纳 jar 快照 + 变异报告，但需清理策略 |
| git | **2.33.1** | 支持 `core.hooksPath`、`receive.denyDeletes`、worktree |
| Java / Maven | 21 (2023-09-19 LTS) / 3.8.5 | PIT、JaCoCo 均需验证与 Maven 3.8.5 兼容 |
| Node / npm | v26.5.0 / 11.17.0 | **可用 Node 写零依赖金丝雀代理**（无需装 caddy/nginx） |
| Docker | 27.3.1 已装 | 备用隔离环境；**不进主链路**（16 GB 上常驻代价高） |
| jq / python3 | 1.7.1 / 3.14.6 | schema 校验与脚本判定用 jq，不引入新语言栈 |
| caddy / nginx | **未安装** | 金丝雀路由改用 Node 单文件代理（R-802） |
| git hooks | **`core.hooksPath` 未设置，无任何自定义 hook** | 干净起点，但也意味着当前**零物理强制**（对应调研报告 D2/D8） |
| 后端质量插件 | pom 仅有 `spring-boot-starter-actuator`，**无 JaCoCo、无 PIT** | G6 从零接入（R-601/R-602）；`/actuator/health` 404 的原因即"依赖在但端点未暴露" |
| Qoder 钩子 | 存在原生 `qoder-context-hook`，观测到的事件为 **`user-prompt-submit`** / `qoder-context-prompt`；`~/.qoder-cn/settings.json` 中**无 `hooks` 键**（当前未配置） | ⚠️ **决定性约束，见 §3.1** |
| 技能埋点 | `~/.qoder-cn/skill-usage.json` 已在记录技能名，`monitor/` 服务在 :8090 | 跳步率与阶段审计**已有数据源**，不必新造（R-901） |

---

## 3. 核心设计决策

### 3.1 决策一：物理强制层只能是 git hook，不是 Agent hook

调研报告把 hook 列为"去人工第 1 顺位"。但本机实查发现 Qoder 可观测的钩子事件是 **`user-prompt-submit`** —— 它在用户提交提示词时触发，**只能注入上下文/校验输入，无法阻断一次文件写入**。因此：

| 需求能力 | Agent hook 能否提供 | 本地实际承载层 |
|----------|--------------------|----------------|
| 阻止 AI 跳过阶段 | ❌ | `pre-commit`（本地即时反馈） |
| 阻止不合规代码入库 | ⚠️ 不可靠（`--no-verify` 可绕过 pre-commit） | **本地 bare 仓库的 `pre-receive`** —— 唯一不可绕过的点 |
| 注入阶段状态与规范上下文 | ✅ | `user-prompt-submit` hook（Qoder 支持的那一类，正好用在这） |
| 强制测试与源码同变更 | ❌ | `pre-commit` + `pre-receive` 双卡 |

> **关键洞察（已在本机实测验证，见 §9 V-02）**：把 `origin` 指向本机另一个目录的 bare 仓库，`pre-receive` 钩子就运行在"远端"，`git push --no-verify` **对它无效**。这是个人开发者在没有组织级 CI 的情况下，唯一能拿到的**不可绕过门禁**。整个方案以它为主锁，`pre-commit` 只作快速反馈（省时间，不承担强制责任）。
>
> 实测证据：同一仓库同一 commit，`git push origin main` 与 `git push --no-verify origin main` 都得到 `[remote rejected] main -> main (pre-receive hook declined)`，bare 仓库的 `main` ref 始终未被创建 —— 说明 `--no-verify` 只压制客户端钩子，碰不到接收端。

### 3.2 决策二：组织能力的本地等价物替换表

| 调研报告中的组织能力 | 本地等价物 | 保真度 | 承载需求 |
|----------------------|-----------|--------|----------|
| CI required status checks | bare 仓库 `pre-receive` 跑 G1–G7 校验 | 高（不可绕过） | R-002/R-003 |
| Branch protection（禁直推 main、禁删分支） | `receive.denyCurrentBranch=updateInstead` + `denyDeletes` + `denyNonFastForwards` | 高 | R-004 |
| Required review / CODEOWNERS | 跨 family Critic Agent 产物 + 策略引擎判定（非人） | 中（依赖 R-3xx 质量） | R-301–R-304 |
| 云端流水线 | 本地 `pipeline.sh` + Makefile，阶段化 job，退出码 0/2 | 高 | R-005 |
| Artifact 仓库 + 版本 | `releases/REQ-xxx/{app.jar,symlink current}` | 中 | R-801 |
| 生产流量金丝雀 | Node 加权反代 + 蓝绿双端口 + 本地指标 jsonl | **低（仿真）** | R-802/R-803/R-804 |
| 密钥管理 | 环境变量 + `.env` 不入库（AGENTS.md §6） | 高 | R-010 |
| Agent 审计平台 | 复用 `skill-usage.json` + `monitor:8090` + `.pipeline/state.yaml` | 高 | R-901 |
| 变异测试服务 | PIT Maven 插件，**仅增量作用域**（16GB 硬约束） | 高（但慢，需实测） | R-602 |

### 3.3 决策三：产物即状态，且必须被机器读

个人单机没有会话编排器，跨会话续跑**只能靠文件系统**。因此：

```
.pipeline/
├── REQ-001/
│   ├── state.yaml          # 当前阶段、各 Gate 结果、时间戳、模型标识、产物路径
│   ├── artifacts/          # 各阶段产物的 front-matter（结构化部分）
│   └── audit.log           
docs/
├── spec/REQ-001-{需求分析|澄清|评审|方案}.md     # 人读 + 机读（YAML front-matter）
├── report/REQ-001-{测试报告|发布记录}.md
```

约定：`state.yaml` 由 `pipeline.sh` 独占写入，**AI 只读不写**（调研报告 §12.1"判定权"的本地实现）；产物 markdown 的 YAML front-matter 是 Gate 校验的输入，正文供人抽检。

---

## 4. 本地拓扑

```
                    ┌─ user-prompt-submit hook (Qoder)
                    │   注入: 当前 REQ 阶段 + AGENTS.md 规范摘要
   AI Agent ────────┘
     │ 编辑代码/写产物
     ▼
 工作区 worktree (REQ-xxx)                    ← R-006 隔离并行需求
     │ git commit → pre-commit (快速反馈: G5 规范 + G6/G7 同变更)
     ▼
 本地 origin: ~/dev/pipeline/origin.git  [bare]
     │ pre-receive → pipeline.sh gate G1..G7   ← 不可绕过主锁
     ▼ 通过则更新 refs/heads/main
 releases/REQ-xxx/app.jar  +  symlink current ──┐
                                               │
 [blue] :8081  SpringBoot ──┐                  │  R-801 版本快照
 [green] :8082 SpringBoot ──┤                  │
                            ▼                  ▼
        Node 加权反代 :8000 (canary-proxy.mjs)  ← R-802 权重 1→5→25→100%
                            │
        前端 Vite :5173 ────┤  /api → :8000
                            ▼
        指标 metrics.jsonl → 判定脚本 R-803 → 异常则 weight=0 + 切回 current
                            │
                            └→ 自动创建 REQ-xxx-BUG → 回到阶段 1  (R-804)

 监控: monitor :8090 (localhost only)  ← skill-usage.json 埋点
```

**端口占用约束**（个人机器上最易踩的坑，调研报告 §12.5 已实证过 8080 冲突）：
`8080` 保留给日常开发后端 ｜ `8081/8082` 蓝绿 ｜ `8000` 金丝雀代理 ｜ `5173` 前端 ｜ `8090` monitor ｜ `9527` pipeline runner 状态页（可选）。所有端口由 `personal-project-runner` 统一管理，禁止临时改端口。

---

## 5. 需求分层

优先级：P0=必须先有（无它则整个方案无强制力）；P1=达成 L3 必需；P2=稳定性/成本优化；P3=L4 演进。
工作量单位：人天（个人单机，含调试）。

### E0 平台底座（P0，共 6.5 人天）

| ID | 需求 | 验收标准（EARS） | 依赖 | 人天 | 优先级 |
|----|------|------------------| — |------|--------|
| R-001 | 版本化 git hooks 目录：仓库内 `.githooks/`，`core.hooksPath` 指向它，提供 `make install-hooks` 一次性启用 | WHEN 任何人 clone 并执行 make install-hooks，THE SYSTEM SHALL 使 pre-commit/pre-push 在后续提交中必然执行 | — | 0.5 | P0 |
| R-002 | 本地 bare 仓库作 origin：`~/dev/pipeline/origin.git`，项目 remote 指向它 | IF remote 不是本地 bare，THEN pre-receive SHALL 拒绝推送并 exit 2 | R-001 | 0.5 | P0 |
| R-003 | `pre-receive` 门禁：调用 `pipeline.sh gate G1,G2,G4,G5,G6,G7`，任一非零即拒绝 | WHEN 推送不满足任一 Gate，THE SYSTEM SHALL 拒绝 ref 更新并回显失败 Gate 与原因行号 | R-005 | 1.5 | P0 |
| R-004 | 分支保护仿真：bare 仓库设 `receive.denyDeletes=true`、`receive.denyNonFastForwards=true`；`pre-receive` 对**推入 refs/heads/main 和非 main 分别执行不同强度检查**（main 全量 G1–G7，特性分支只跑 G5），并用 `git push -o pipeline=run` 作为"由流水线发起"的标记 | IF 尝试删除分支或非快进推送 main，THEN 仓库 SHALL 拒绝 | R-002 | 0.5 | P0 |
| R-005 | `pipeline.sh` 阶段执行器：子命令 `gate {G1..G8}`、`status {REQ}`、`advance {REQ}`；退出码 0=通过 / 2=阻断；stdout 机器可读（jq 可解析 JSON） | WHEN 执行 `pipeline.sh gate G5 REQ-001`，THE SYSTEM SHALL 在 3 秒内返回结构化结果且不修改任何文件 | R-007 | 2 | P0 |
| R-006 | 需求级工作隔离：每个 REQ 用 `git worktree` 独立目录，Critic 初始不可读 Proposer 产物（为 commit-first 服务） | WHEN 创建新 REQ，THE SYSTEM SHALL 分配 worktree 与 `.pipeline/REQ-xxx/`，且 Critic worktree 不含 Proposer 未提交改动 | R-001 | 1 | P0 |
| R-007 | 状态载体 `state.yaml` + 产物 front-matter schema（7 份 JSON Schema，jq 校验） | IF 产物缺必填字段或 `generated_at` 早于上游产物，THEN gate SHALL 判失败 | — | 1 | P0 |
| R-008 | 规范上下文注入 hook（Qoder `user-prompt-submit`）：把当前 REQ 阶段 + AGENTS.md 相关条目注入提示 | WHEN 用户提交提示，THE HOOK SHALL 追加当前阶段与适用规则摘要（只读，不阻断） | R-007 | 0.5 | P1 |
| R-010 | 密钥与环境：`.env` 加入 `.gitignore` 与 `.qoderignore`；门禁校验"提交内容不含密钥模式" | IF pre-commit 检到疑似密钥模式，THEN SHALL 拒绝提交 | R-001 | 0.5 | P0 |

### E1 G1 需求分析（P1，1.5 人天）

| ID | 需求 | 验收标准 | 依赖 | 人天 |
|----|------|----------|------|------|
| R-101 | 新建技能 `common-requirement-analysis`（调研报告 D7：整阶段缺失） | 技能含输入/输出格式、检查清单、注册进 SKILLS-ROUTER.md 第二节 | — | 1 |
| R-102 | G1 门禁：需求项均带业务目标溯源、非目标显式排除 | IF 任一需求项 `trace_to` 为空，THEN G1 SHALL 失败 | R-007,R-101 | 0.5 |

### E2 G2 需求澄清（P1，1.5 人天）

| ID | 需求 | 验收标准 | 依赖 | 人天 |
|----|------|----------|------|------|
| R-201 | 澄清产物落盘约定 + P0 待决项规则 | IF 存在 `priority=P0` 且 `status=open` 的待决项，THEN G2 SHALL 失败 | R-007 | 0.5 |
| R-202 | 验收标准采用 EARS 句式，并**可自动编译为断言桩**（每条生成一个 pending JUnit/Playwright 用例） | WHEN 验收标准不可解析为 EARS，THEN G2 SHALL 判"未澄清" | R-201 | 1 |

> R-202 是本地方案里性价比最高的一条：它把"需求写得好不好"变成"能否编译"，彻底机械化，不需要模型判断。

### E3 G3 技术评审（P1→P3，7 人天，最难，最后摘人工）

| ID | 需求 | 验收标准 | 依赖 | 人天 | 优先级 |
|----|------|----------|------|------|--------|
| R-301 | 新建技能 `common-tech-review`（调研报告 D5：`common-code-review` 是事后审查，非事前评审） | 技能要求 ≥3 条替代路径比较，对齐 `personal-solution-research` 硬约束 | — | 1 | P1 |
| R-302 | Critic 以**不同模型 family** 运行，读不到 Proposer 的中间推理 | WHEN 起 Critic，THE SYSTEM SHALL 记录 `model_family`，且与 Proposer 相同则告警并拒绝采信其结论 | R-006 | 1 | P1 |
| R-303 | **commit-first**：Critic 先独立产出评审结论文件，之后才被允许读被评审产物 | IF 产物时间戳早于 Critic 独立结论，THEN G3 SHALL 判无效（防"看完答案再判"） | R-302 | 2 | P1 |
| R-304 | golden set 校准：`evals/golden/*.yaml` + `judge-calibrate.sh`，输出 judge 准确率 | WHEN 准确率 < 阈值，THE SYSTEM SHALL 把 G3 自动降级为"需人工"并在 state.yaml 标记 | R-303 | 2 | P2 |
| R-305 | 分歧仲裁策略：Proposer/Critic 不一致 → 选爆炸半径更小方案 + 灰度起始比例下调 + 回滚阈值收紧；**不 escalate 给人** | IF 分歧率连续超阈值，THEN SHALL 降级为需人工 | R-303,R-304 | 1 | P3 |

> ⚠️ 调研报告 §10 第 4 项的答案在这里落地为一条硬规则：**R-303/R-304 未实施完成前，G3 不得摘除人工。** 否则等于让一个可被 best-of-N 刷到 93/96 接受率的 judge 当唯一防线。

### E4 G4 技术方案与任务拆解（P1，2 人天）

| ID | 需求 | 验收标准 | 依赖 | 人天 |
|----|------|----------|------|------|
| R-401 | Spec 产物 schema：架构/接口/数据模型/任务清单四段必填 | IF 任一需求项未被任务覆盖，THEN G4 SHALL 失败 | R-007 | 0.5 |
| R-402 | 任务粒度约束 + **回指需求编号**（Kiro 做法）；每项 ≤1 接口 / ≤1 文件 | IF 单任务涉及 >1 文件或无 `req_ref`，THEN G4 SHALL 失败 | R-401 | 1 |
| R-403 | 四向追溯表生成器：需求↔任务↔commit↔测试，脚本产出 `trace.json` | WHEN 追溯存在断链，THEN 报告 SHALL 列出断链项且 gate 失败 | R-402 | 0.5 |

> R-402 的粒度约束在本地不是洁癖：调研报告指出 CI 单会话上限 59 分钟，个人机器更慢，任务拆不细就会单次跑不完 G6/G7。

### E5 G5 编码与规范拦截（P0，2 人天，**最先落地**）

| ID | 需求 | 验收标准 | 依赖 | 人天 |
|----|------|----------|------|------|
| R-501 | 十条禁止项机械化规则（grep/AST 混合，无一条需模型判断）：包名连字符拆分、禁 `@RequiredArgsConstructor`、禁 PUT/PATCH/DELETE、Controller 禁业务逻辑、禁返回 Entity、组件内禁直连 axios、必须 DTO、必须异常处理、必须 `@Slf4j`、必须 AGENTS/README | WHEN 违反任一条，`pipeline.sh gate G5` SHALL exit 2 并输出 `文件:行号:规则ID` | R-005 | 1.5 |
| R-502 | **E1–E7 回归用例固化**（调研报告 §3.5 的 7 个真实缺陷） | THE SYSTEM SHALL 在 CI 之外可单跑 `make gate-replay`，7 例全部被自动阻断才算通过 | R-501 | 0.5 |

### E6 G6 单元测试 + 机械预言机（P1，5 人天）

| ID | 需求 | 验收标准 | 依赖 | 人天 |
|----|------|----------|------|------|
| R-601 | 接入 JaCoCo，**只统计增量行**，阈值 ≥70%；禁止通过排除目录达标 | IF 增量行覆盖 <70%，THEN G6 SHALL 失败 | R-007 | 1 |
| R-602 | 接入 PIT，`targetClasses/targetTests` 限定为**变更包**，`mutationThreshold=50`；M1 上 `threads=2`，并实测耗时基线 | WHEN 增量 mutation score <50%，THEN G6 SHALL 失败；WHEN 耗时 >预算，THEN SHALL 支持仅 PR 级增量、夜间全量 | R-601 | 2 |
| R-603 | 空断言 / 纯 mock 断言静态检测（防过度 mock，调研报告实证失败模式） | IF 测试方法无有效断言或仅验证 mock 交互，THEN G6 SHALL 警告→（P2 后）失败 | R-501 | 1 |
| R-604 | 边界用例清单化（防"测 04-00-2025 却不测闰年 2/29"）：日期/空值/边界/权限四类模板 | WHEN 需求含日期/集合运算，THEN 生成的测试 SHALL 覆盖边界模板项 | R-603 | 1 |

### E7 G7 UI / E2E 验证（P1，3.5 人天）

| ID | 需求 | 验收标准 | 依赖 | 人天 |
|----|------|----------|------|------|
| R-701 | 修 `/actuator/health` 404（根因：`spring-boot-starter-actuator` 在 pom 中但端点未暴露），加 `management.endpoints.web.exposure.include=health,info` | WHEN GET :8081/actuator/health，THEN SHALL 返回 200 且 status=UP | — | 0.5 |
| R-702 | 确定性起服编排：`pipeline.sh up` 用健康探针轮询（禁固定 sleep），端口冲突自动清理并记录 | IF 端口被非本流水线进程占用，THEN SHALL 中止并报告占用 PID | R-701,R-005 | 1 |
| R-703 | Playwright 定位约束：**确定性 `.spec.ts` 才是 G7 门禁**；Playwright MCP 只用于探索，产物须回落为 spec | IF 门禁用例来自 MCP 动态探索而无可提交 spec，THEN G7 SHALL 失败 | R-702 | 1 |
| R-704 | E2E 必须断言**接口成功态**（调研报告 E6：页面渲染不依赖接口成功，绿≠可用）+ 截图归档 `docs/report/REQ-xxx/` | IF 列表接口返回 5xx 而用例仍绿，THEN 该用例 SHALL 被判无效 | R-703 | 1 |

### E8 G8 本地发布闭环（P2，7 人天，**L4 的入场券**）

| ID | 需求 | 验收标准 | 依赖 | 人天 |
|----|------|----------|------|------|
| R-801 | 版本快照：`releases/REQ-xxx/app.jar` + `current` 符号链接，保留最近 5 版 | WHEN 门禁全绿后推送 main，THE SYSTEM SHALL 产出新快照并原子切链 | R-003 | 1 |
| R-802 | Node 零依赖加权反代 `canary-proxy.mjs`（本机无 caddy/nginx）：`:8000` → blue(8081)/green(8082)，权重可运行时调整 | WHEN weight=5，THEN SHALL 以约 5% 比例路由至 green 且可观测 | R-801 | 1.5 |
| R-803 | 本地指标采集与判定：错误率/P95 延迟写 `metrics.jsonl`；判定脚本按阈值决定放量或回滚 | IF 窗口内错误率 >1% 或 P95 超基线 1.5×，THEN SHALL 自动 weight=0 并切回 current | R-802 | 2 |
| R-804 | 缺陷回流：回滚后自动创建 `REQ-xxx-BUG` 状态文件并触发阶段 1 | WHEN 发生回滚，THE SYSTEM SHALL 生成新 REQ 且其 `origin_defect` 指向失败发布 | R-803 | 1 |
| R-805 | 策略引擎（**非 AI、非人**）：`无P0告警 ∧ 增量覆盖/变异达标 ∧ E2E全绿 ∧ 契约测试通过 → 自动放量`；AI 不具备绕过门禁的合并权限 | IF 任一条件不满足，THEN SHALL 拒绝放量；WHEN 未通过 `pre-receive` 校验的 push 试图更新 main，THEN 仓库 SHALL 拒绝 | R-803,R-004 | 1.5 |

> R-805 落实调研报告 §12.3 的关键张力：OWASP AC.8.3 要求验证自主 agent 无法绕过 required checks。**本地的合并权威不是"PR 上的人点批准"，而是 `pre-receive` 本身**——它与 AI 无关、与 `--no-verify` 无关、也不是人。这保持了"发版决策不由 AI 持有"的实质，但少了云端 PR 那一层显式审批记录，因此 §8 标注其保真度为"中"。

### E9 审计与度量（P1/P2，3.5 人天）

| ID | 需求 | 验收标准 | 依赖 | 人天 |
|----|------|----------|------|------|
| R-901 | 阶段流转审计：`state.yaml` 变更即追加 `audit.log`；沿用 `monitor/` 约束（只记技能名/时间/客户端/阶段，禁记代码内容·密钥·token·用户数据；仅监听 localhost:8090） | THE SYSTEM SHALL 可回答"REQ-xxx 在 G3 停留多久、judge 用了哪个模型" | R-007 | 1 |
| R-902 | 仪表盘指标：跳步率、Gate 一次通过率、人工介入次数/需求、返工率、增量覆盖与变异分数、E2E Flaky 率、judge 分歧率与 golden 准确率、lead time、单需求 token 费用 | WHEN 跳步率 >0，THE SYSTEM SHALL 红色告警（该指标目标恒为 0，不是"尽量低"） | R-901 | 1.5 |
| R-903 | 反指标防护：覆盖率、AI 代码占比、PR 数、judge 通过率**不得**出现在任何达标口径中 | 代码评审 + 文档声明 | R-902 | 1 |

---

## 6. 依赖关系与关键路径

```
R-001 ─┬─ R-002 ─┬─ R-003 ─── R-801 ─ R-802 ─ R-803 ─┬─ R-804
       │         └─ R-004 ────────────────────────────┘  R-805
       ├─ R-010
       └─ R-006 ── R-302 ─ R-303 ─ R-304 ─ R-305

R-007 ─┬─ R-005 ─┬─ R-501 ─ R-502          ← 最短见效路径
       │         ├─ R-601 ─ R-602/603/604
       │         └─ R-102 ─ R-201 ─ R-202 ─ R-401 ─ R-402 ─ R403
       └─ R-008
R-701 ─ R-702 ─ R-703 ─ R-704
```

**关键路径**：`R-007 → R-005 → R-501 → R-502`（3 人天）—— 走完这一段，调研报告 §3.5 的 7 个真实缺陷就全部变成自动阻断。**这是唯一必须无条件先做的事。**

**最长链**：`R-001→R-002→R-003→R-801→R-802→R-803→R-805`（G8），且它依赖 G5–G7 全绿作为放量前置。

**硬依赖冲突提示**：`R-302` 要求 Critic 跨模型 family，但本机 Qoder 的可用模型由客户端决定。若只有一个 family 可用，R-302 不可满足 → G3 必须保留人工。这是需求里唯一可能被环境卡死的点，需先验证（列入 §9 V-01）。

---

## 7. 分期里程碑

| 里程碑 | 内容（需求 ID） | 人天 | 累计 | 可验证结果 | 自主度 |
|--------|-----------------|------|------|------------|--------|
| **M1 底座+拦截** | R-001–R-007, R-010, R-501, R-502 | **9** | 9 | 7 个历史缺陷全部自动阻断；`--no-verify` 无效 | L2 → 执行率确定 |
| **M2 文档阶段** | R-101, R-102, R-201, R-202, R-401–R-403, R-008 | 6.5 | 15.5 | G1–G4 机判；验收标准可编译为断言桩；四向追溯 | L3 前半 |
| **M3 预言机** | R-601–R-604, R-701–R-704 | 8.5 | 24 | 变异分数成主门禁；E2E 断言接口成功态；起服可复现 | **L3** |
| **M4 评审对抗** | R-301, R-302, R-303, R-901, R-902 | 6 | 30 | G3 可无人（前提 golden set 达标）；审计可答"谁判的" | L3 稳态 |
| **M5 发布闭环** | R-304, R-305, R-801–R-805, R-903 | 11 | 41 | 自动放量/回滚/缺陷回流；AI 无合并权限 | **L4** |

**与调研报告 32.5 人天（32.5 vs 41）的差异说明**：本地版多出的约 8.5 人天来自 ① bare 仓库门禁与分支保护仿真（R-002/R-003/R-004，云端是现成的）② 金丝雀代理与本地指标判定（R-802/R-803，云端有平台）③ 起服编排与端口治理（R-702，云端容器天然隔离）。**这是"单机个人"这个前提付出的代价，不是重复建设。**

**建议批准粒度：只先批 M1（9 人天，其中前 3 天即关键路径）。** M1 完成后可用 `make gate-replay` 客观判定方案是否成立，再决定 M2 之后是否继续。

---

## 8. 本地方案的保真度声明（必须读）

| 能力 | 保真度 | 能验证什么 | 不能验证什么 |
|------|--------|-----------|--------------|
| `pre-receive` 门禁 | **高** | 不可绕过性（`--no-verify` 无效）——与 required check 等价 | 多人并发推送、外部系统触发 |
| 分支保护仿真 | 高 | 禁直推/禁删/禁非快进 | 跨仓 CODEOWNERS 语义 |
| G6 变异分数 | 高 | 测试是否真有判别力 | —— |
| G7 确定性 E2E | 高 | 真实起服 + 真实 API 成功态（正是 E6 缺陷的捕手） | 真实网络延迟分布、并发用户 |
| **G8 金丝雀 + 回滚** | **低（机制仿真）** | 权重路由正确、阈值触发回滚、链回切、缺陷回流闭环 | **真实故障分布、真实流量下的观测噪声** |
| R-302 跨 family | 取决于环境 | 若可用：评审独立性 | 若不可用：G3 无法无人化 |

> 结论：**G1–G7 在本地是可信的，G8 只是机制演练。** 因此本地跑通 ≠ 可以摘除生产发布的人工，只等于"具备摘除的资格证明"。这条边界必须在验收时写进结论，不得因全绿而夸大。

---

## 9. 实施前必须先做的可行性验证（不写代码，先探路）

| ID | 验证项 | 通过判据 | 若不通过的替代方案 |
|----|--------|----------|--------------------|
| V-01 | Qoder 是否支持配置 `hooks`（`settings.json` 当前无 `hooks` 键，仅见原生 `qoder-context-hook` 的 `user-prompt-submit` 事件） | 能配置并实际触发一次自定义脚本 | R-008 降级为"仅 AGENTS.md 注入"；强制力全部压在 git hook（可接受，因为 R-003 才是主锁） |
| ~~V-02~~ **已实测通过** | `pre-receive` 在本地 bare 仓库确实不可被 `--no-verify` 绕过 | ✅ 本机 git 2.33.1 实测：普通 push 与 `git push --no-verify` **均**返回 `[remote rejected] main -> main (pre-receive hook declined)`，bare 仓库 `main` ref 始终未创建 | 无需替代方案。**R-003 的主锁前提成立** |
| V-03 | **PIT 在本机 M1/16G 上对变更包的实测耗时** | 单模块增量变异 ≤5 分钟 | 降级：变异测试改夜间跑；G6 主门禁暂用覆盖率+断言静态检测，并在报告中标注"预言机未完全落地" |
| V-04 | 是否存在 ≥2 个可用模型 family 供 Critic 使用 | 能显式指定不同 family 各跑一次 | G3 保留人工，M4 延后（**不允许**用同 family 假装独立评审） |
| V-05 | Maven 3.8.5 与目标 PIT/JaCoCo 版本兼容性 | 插件可解析且执行成功 | 升级 Maven（注意这属于改动构建基建，需单独确认） |
| V-06 | 77 GB 可用磁盘在保留 jar 快照 + 变异报告 + Playwright 产物下是否够 30 天 | 估算峰值占用 | `releases/` 保留数从 5 降到 3，报告加 7 天清理 |

> V-01、V-03、V-04 是三个**可能推翻方案**的验证点。V-03 不通过会削弱核心卖点（机械预言机）；V-04 不通过会让 G3 无法无人化，"完全自动"退化为 7 个 Gate 里留一个人工点。

---

## 10. 风险（本实施层新增，不重复调研报告 §9）

| 风险 | 等级 | 应对 |
|------|------|------|
| **git hook 成为唯一主锁**，bare 仓库被删/被绕过（换 remote）即全崩 | 高 | ① `origin` 设在 `~/dev/pipeline/` 并纳入 Time Machine；② 门禁脚本随代码版本化（`.githooks/` 入库）；③ `pipeline.sh doctor` 定期自检 remote 指向与 hooksPath |
| 16 GB 上蓝绿双后端 + Playwright + Qoder + PIT 争抢内存 | 高 | 硬约束：`-Xmx512m` ×2、PIT `threads=2`、Playwright `workers=2`、Docker 不进主链路；`pipeline.sh` 串行执行 Gate，禁并行跑 G6 与 G7 |
| 个人机器上 PIT 夜间全量依赖机器不休眠/插电 | 中 | 用 `caffeinate` + launchd，且明确"夜间全量"为可选档，不作为门禁前提 |
| 金丝雀仿真给出虚假信心 | 中 | §8 保真度声明写入每次验收报告首页；G8 通过不等于可摘生产人工 |
| `pre-commit` 拖慢每一次提交导致本人 `--no-verify` 成习惯 | 中 | 分层：`pre-commit` 只跑 <1 秒的静态规则（G5）；重活全部放 `pre-receive`（推送时一次）。**快速反馈和强制力分离**是个人开发者不自欺的关键 |
| 需求产物与代码不同步漂移 | 中 | `R-403` 追溯表在每次 gate 重生成；产物 `generated_at` 早于上游即判失败（R-007） |
| 单人无 review 对手，Critic 与本人共谋（放宽阈值） | 中 | 阈值写入 schema 并由 `pre-receive` 校验，修改阈值本身需一次独立 commit + 在 `audit.log` 留痕 |
| 技能库膨胀使 L1 路由表超上下文预算（调研报告 U2） | 中 | M2 起约束：`SKILLS-ROUTER.md` 行数上限（现 222 行）；超限则按场景分片，改为按需 Read 分片 |

---

## 11. 交付物清单

| 交付物 | 路径 | 关联需求 |
|--------|------|----------|
| 门禁脚本族 | `.githooks/{pre-commit,pre-push}`、`scripts/pipeline.sh` | R-001–R-005 |
| 规范规则集 | `scripts/rules/g5-*.sh` + `rules.json` | R-501 |
| Gate 回放用例 | `scripts/tests/gate-replay/`（E1–E7 七例） | R-502 |
| 产物 schema | `.pipeline/schema/{requirement,clarification,review,spec,tasks,trace}.schema.json` | R-007 |
| 质量插件配置 | 后端 `pom.xml`（JaCoCo + PIT profile） | R-601/R-602 |
| 起服与金丝雀 | `scripts/{canary-proxy.mjs,up.sh,release.sh,rollback.sh}` | R-702/R-801–R-803 |
| 评审对抗 | `evals/{golden/*.yaml,judge-calibrate.sh}` | R-303/R-304 |
| 审计与仪表盘 | 扩展 `study-ai-skills/monitor/` 至阶段维度 | R-901/R-902 |
| 两个新技能 | `study-ai-skills/skills/{common-requirement-analysis,common-tech-review}/` | R-101/R-301 |
| 缺失技能补齐 | `common-ci-cd`、`common-release-management`（当前为悬空引用） | R-003/R-805 |

---

## 12. 验收标准（整体）

1. `make gate-replay` —— 调研报告 §3.5 的 7 个真实缺陷全部被自动阻断，**0 个需要人看**。
2. `git push --no-verify` 尝试推送一个 G5 违规 —— **被 `pre-receive` 拒绝**（证明主锁有效，这是本方案区别于"文档规范"的唯一硬证据）。
3. AI 交付一个新需求（含前后端 + UI），从 G1 到 G7 **全程无人工逐阶段审批**，且 `docs/` 下 7 份产物齐备、追溯表无断链。
4. 人为注入一个后端 5xx —— 金丝雀自动 `weight=0` 并切回上一版本，且自动生成 `REQ-xxx-BUG` 回到阶段 1（G8 闭环成立）。
5. 全程 `audit.log` 可回答：每个 Gate 谁判的、用的什么模型、判定结果、人工介入次数。
6. **G3 若 V-04 不通过，验收口径自动改述为"6/7 Gate 无人化 + G3 保留人工"**，不得宣称全自动。

---

## 13. 参考

- 上游：[RESEARCH-20261110-01 方案调研报告](../report/ai-dev-flow-AI全自动研发流程方案调研-2026-11-10.md)
- 规范：[AGENTS.md](../../AGENTS.md)（§5 十条禁止项 → R-501；§6 安全规范 → R-010）
- 技能路由：`~/bage/github/study/study-ai-skills/SKILLS-ROUTER.md`
- 外部机制依据：GitHub Spec Kit / AWS Kiro 的产物链与 EARS、OpenSpec 的 propose 禁改代码、OWASP AISVS AC.8.3、Meta JiTTesting 的 mutation-guided + LLM-as-Judge 过滤顺序（链接见调研报告 §参考资料）
