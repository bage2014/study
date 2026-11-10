# 方案调研报告：完全 AI 驱动的研发流程

## 调研信息

| 项目 | 内容 |
|------|------|
| 报告编号 | RESEARCH-20261110-01 |
| 调研主题 | 全流程 AI 自主完成「需求分析 → 需求澄清 → 技术评审 → 技术方案 → 编码实现 → 单元测试 → UI 测试」 |
| 调研范围 | 编排架构、阶段门禁、产物契约、验证闭环、可观测性 |
| 作者 | bage |
| 创建日期 | 2026-11-10 |
| 版本 | v1.0 |
| 状态 | 草稿（待评审） |

---

## 1. 调研目标

回答一个问题：**当前「Skill 路由表 + AGENTS.md」的体系，能否支撑"无人介入"的端到端研发？如果不能，缺什么？**

具体拆解为 4 个子目标：

1. 把 7 个阶段逐一映射到「输入 / 技能 / 产物 / 通过判据」，找出无技能覆盖的阶段；
2. 找出"AI 跳过环节"的根因，并给出**不可绕过**（而非"建议遵守"）的门禁机制；
3. 比对 5 种业界主流编排架构，给出适合本团队的推荐组合；
4. 给出可分阶段落地的路线图与风险清单。

**不在范围内**：模型选型、Prompt 工程技巧、IDE 厂商对比。

---

## 2. 现状盘点（基于实际代码库，非推测）

### 2.1 已有资产

| 资产 | 位置 | 作用 |
|------|------|------|
| L0 项目规范 | `AGENTS.md` | 打开项目自动加载，硬约束清单 |
| L1 技能路由 | `study-ai-skills/SKILLS-ROUTER.md` | 场景 → 技能索引 + 7/8/4 步强制流程 |
| L2 技能正文 | `study-ai-skills/skills/*/SKILL.md` | 38 个技能，含流程与输出格式 |
| 项目初始化模板 | `study-ai-skills/templates/project-init-template.md` | 新空项目接入 |
| 技能安装/校验 | `study-ai-skills/scripts/{install-skills.sh,validate-skills.sh,skills.sh}` | 双平台同步 |
| 技能调用埋点 | `study-ai-skills/monitor/`（Spring Boot :8090 + JSONL + Dashboard） | 已可观测"哪些技能被调用过" |

### 2.2 关键发现：依赖关系已存在，但未被用于执行

24 个技能的 frontmatter 声明了 `requires`，事实上构成一张 DAG。例如：

```
common-spec-driven        → requires [common-requirement-clarification]
common-grill-me           → requires [common-requirement-clarification, common-spec-driven]
common-design-brainstorming → requires [common-requirement-clarification, common-spec-driven, common-solution-research]
personal-solution-research → requires [common-spec-driven, personal-doc-manager]
common-product-verification → requires [common-requirement-clarification, common-frontend-playwright-test]
```

**这意味着"阶段前置条件"已经有机器可读的表达，只是没有任何执行器读取它。** 路由表里的"强制流程"是给 AI 看的散文，AI 可以选择忽略。

### 2.3 已确认的缺陷

| # | 缺陷 | 证据 | 影响 |
|---|------|------|------|
| D1 | 悬空依赖 | `requires` 指向不存在的 `common-ci-cd`、`common-release-management`、`common-solution-research`（实际名为 `personal-solution-research`） | DAG 校验必然失败；无法作为执行依据 |
| D2 | 无 CI | `study-ai-dev-flow/.github/` 为空目录，无任何 workflow | 阶段门禁无处强制，全靠 AI 自觉 |
| D3 | 无流程状态 | 不存在任何"当前需求处于哪个阶段"的持久化状态 | 跨会话中断即丢失；无法判定"能不能进下一阶段" |
| D4 | 产物无契约 | 各 SKILL.md 有输出示例，但无 schema、无落盘路径约定、无校验 | 下游阶段无法可靠读取上游产物 |
| D5 | 无评审门禁 | `common-code-review` 是**事后代码审查**，不是**事前技术评审** | "技术评审"阶段实际无技能覆盖 |
| D6 | 测试靠提醒 | 本会话中实现 User CRUD 时未自动产出测试，需人工追问 | 已实证：散文约束不成立 |
| D7 | 无需求分析阶段 | 只有"澄清"，没有"从业务目标推导需求项 + 优先级" | 流程第一步缺位 |
| D8 | **无拦截层** | 全仓库无 hook、无 CI、无 required check；门禁只有 `AGENTS.md` 散文 | 业界一致把这一层归为"建议"而非"拦截"，跳过无成本 |
| D9 | **无独立预言机** | 同一会话写代码、写测试、跑测试、下结论；无变异测试、无覆盖率工具（后端未接 JaCoCo） | 测试全绿不具备证明力（见 §7 机械预言机） |
| D10 | 无阶段状态载体 | `docs/` 目录本次才首次创建（此前不存在），历史交付无阶段产物 | 无法判定"能否进下一阶段" |

---

## 3. 七阶段映射与缺口

| 阶段 | 现状技能 | 输入 | 应有产物 | 通过判据（Gate） | 缺口 |
|------|----------|------|----------|------------------|------|
| 1 需求分析 | ❌ 无 | 业务目标/用户诉求 | `docs/spec/{req}-需求分析-{date}.md`（需求项清单 + 优先级 + 非目标） | 需求项可追溯到业务目标；非目标已显式排除 | **D7：整阶段缺失** |
| 2 需求澄清 | `common-requirement-clarification` + `common-grill-me` | 需求分析产物 | 澄清记录（问答对 + 已确认假设 + 待决项） | 无 P0 级待决项；验收标准可测 | 产物未落盘约定 |
| 3 技术评审 | ⚠️ 错位（`common-code-review` 属事后） | 澄清记录 | 技术评审意见（风险/替代路径/成本） | 至少 N 条替代路径被比较；风险有缓解措施 | **D5：需新建 `common-tech-review`** |
| 4 技术方案 | `common-design-brainstorming` → `personal-solution-research` → `common-spec-driven` | 评审意见 | `docs/spec/`：架构图 + 接口 + 数据模型 + 任务拆解 | Spec 覆盖全部需求项；任务粒度 ≤ 1 文件/1 接口 | 任务拆解无机器可读格式 |
| 5 编码实现 | `personal-backend-coding-standard` / `personal-frontend-coding-standard` + `common-db-design` / `common-api-doc` | Spec + tasks | 代码 + 契约 | 编译通过；规范静态检查 0 违规；每个 task 有 commit 关联 | 无静态检查器（AGENTS.md 的禁止清单不可执行） |
| 6 单元测试 | `common-backend-unit-test` / `common-test-driven-development` | 代码 | `src/test/**` + 覆盖率报告 | 全部通过；增量行覆盖 ≥ 阈值；失败用例可追溯到需求项 | 覆盖率阈值未定义；JaCoCo 未接入 |
| 7 UI 测试 | `common-frontend-playwright-test` (+ `common-visual-regression-test`) | 前端 + 后端起服 | `e2e/*.spec.ts` + 截图 + HTML 报告 | 关键路径全绿；截图归档；报告落 `docs/report/` | 需真实起服（本会话已验证端口冲突/500 等故障会直接卡死） |

> 注：第 3、7 阶段是"看起来有、实际断"的典型。第 7 阶段在本次 User CRUD 验证中因 H2 保留字 `user` 导致 500，若无浏览器实跑，仅跑单测会漏掉该缺陷 —— 证明 UI 阶段不可降级为可选项。

---

## 4. 核心结论（先行）

**纯 Prompt 驱动无法实现"全自动"。** 原因不是模型能力不足，而是五件事缺失：

1. **状态** —— 没有"进行到哪一步"的持久化记录，AI 每次从零推断；
2. **门禁** —— 前置条件写在散文里，没有可执行校验，跳过无成本；
3. **产物契约** —— 上游输出无 schema，下游无法确定性消费，只能靠 AI 重新解读；
4. **拦截层** —— 缺少在动作发生时点返回非零状态码、把拒绝理由回灌给 Agent 的确定性钩子。业界共识是：`AGENTS.md`/`CLAUDE.md` 属**建议**（模型可跳过），hooks/required checks/approval policy 属**拦截**（模型跳不过）。我们的体系目前只有前者。
5. **独立预言机** —— 让同一个模型写代码、写测试、再评审自己的测试，是"自己给自己打分"。实证结论：**mutation score 比 coverage 诚实**；LLM-as-judge 可被反向刷分。

因此候选方案的差异，本质是**把这五件事放在哪一层**：放在 Prompt 里（方案 A）、放在文档产物里（方案 B）、放在 Agent 编排里（方案 C）、放在 CI/hook 里（方案 D）、放在外部托管产品里（方案 E）。

---

## 5. 候选方案

| 方案 | 名称 | 来源类型 | 门禁落在哪一层 |
|------|------|----------|----------------|
| A | Skill 路由 + 强制流程清单（**现状**） | 团队自研 / Anthropic Agent Skills 生态 | Prompt 散文 |
| B | Spec 产物驱动状态机（Spec-as-Source-of-Truth） | 业界标杆：GitHub Spec Kit、AWS Kiro、OpenSpec | 文档产物 + CLI 阶段命令 |
| C | 多 Agent 角色编排（Planner/Coder/Reviewer/QA + HITL） | 开源：LangGraph、Microsoft AutoGen、CrewAI、Claude 子代理 | 编排图 + interrupt |
| D | CI/CD 原生 AI 门禁（阶段 = job，required check） | 业界标杆：GitHub Copilot coding agent + Actions、GitLab Duo、Atlassian Rovo Dev | 流水线 + 分支保护 |
| E | 端到端自主编码 Agent 产品托管 | 商业/开源：Devin、OpenHands、Factory.ai、Cursor 后台代理 | 产品黑盒 |

---

## 6. 方案比对分析

### 6.1 维度评分（★ = 1，满分 5）

| 评估维度 | 权重 | A 现状 | B Spec产物 | C 多Agent | D CI+Hook | E 自主产品 | **B+D 推荐** |
|----------|------|--------|-----------|-----------|-----------|-----------|-------------|
| 自动化完整度（7 阶段能否无人介入跑通） | 25% | ★★★ | ★★★★ | ★★★★★ | ★★★★ | ★★★★ | ★★★★★ |
| 门禁强制力（能否**物理阻止**跳过） | 20% | ★ | ★★★ | ★★★★ | ★★★★★ | ★★★ | ★★★★★ |
| 团队规范可注入性（38 个 skill 能否复用） | 15% | ★★★★★ | ★★★★★ | ★★★★ | ★★★★ | ★★ | ★★★★★ |
| 落地成本（越低越高分） | 15% | ★★★★★ | ★★★★ | ★★ | ★★★★ | ★★★ | ★★★ |
| 可观测与可追溯 | 10% | ★★ | ★★★★ | ★★★★ | ★★★★★ | ★★★ | ★★★★★ |
| 生态成熟度 | 10% | ★★ | ★★★ | ★★★ | ★★★★★ | ★★ | ★★★★ |
| 扩展性 | 5% | ★★★ | ★★★★ | ★★★★★ | ★★★★ | ★★ | ★★★★ |
| **综合评分** | 100% | **60** | **77** | **78** | **88** | **59** | **91** |

计算式：`Σ(分值 × 权重) / 5`。

**评分依据要点（有出处的调整）**：

- B 的门禁由 ★★★★★ 下调为 ★★★：OpenSpec 自述短板即"**无强制质量门禁，步骤可被跳过**"，产物驱动本身不含拦截能力。
- E 的完整度由 ★★★★★ 下调为 ★★★★、总分跌至垫底：Answer.AI 20 任务 **3 成功 / 3 挂起 / 14 失败**；Linktree 月度 **~300 PR 仅 ~100 合并**。自主度≠完成率。
- D 由 84 上调为 88、成本分由 ★★★ 升到 ★★★★：hook 层提供**秒级本地拦截**，比"必须等 CI"便宜得多，`exit 2 + stderr 回灌`是现成机制。
- E 的规范注入仍是最短板（★★），因为它的 7 阶段边界不由我们定义。

### 6.2 逐项详析

#### 方案 A：Skill 路由 + 强制流程清单（现状）

| 项目 | 内容 |
|------|------|
| 机制 | `AGENTS.md`(L0) → `SKILLS-ROUTER.md`(L1) → `SKILL.md`(L2)，AI 识别场景后自行加载并遵循"7/8/4 步强制流程" |
| 对应业界物 | Anthropic Agent Skills / AGENTS.md 规范层。**注意：业界把这一层归为"上下文/建议"，不作为门禁** |
| 核心优势 | ① 规范零改写即可跨项目复用；② 新空项目一次 `init` 接入；③ 无外部依赖、无基础设施成本；④ 38 个技能覆盖 5/7 阶段；⑤ `requires` DAG 已是机器可读的前置条件（§2.2） |
| 主要不足 | ① **无强制力**：跳过环节零成本（本会话实证：CRUD 交付未带测试，需人工追问）；② 无状态，跨会话/中断即丢进度；③ 上下文窗口有限，长流程后段会遗忘前段决策；④ 悬空依赖无法校验；⑤ 无独立预言机，写码/写测/自评同源 |
| 受益 | 维护成本几乎为 0，适合作为"规范载体层"继续存在 |
| 成本 | 已投入，边际成本 0；但要达到"全自动"需外挂拦截层 |
| 复杂度 | 低 |
| 风险 | 流程执行率随机，模型状态波动时静默跳步，无人察觉 |

#### 方案 B：Spec 产物驱动状态机

| 项目 | 内容 |
|------|------|
| 来源 | GitHub Spec Kit、AWS Kiro、OpenSpec（Fission-AI） |
| 实际机制（非宣传） | **Spec Kit**：命令链 `constitution → specify → clarify → plan → checklist → tasks → analyze → implement → converge`，短路径 5 步、长路径中间插 3 个质量门；`tasks.md` 按 phase 分组并标注可并行组。**Kiro**：`.kiro/specs/<name>/` 下 `requirements.md`（用户故事 + EARS 句式验收标准 + 术语表）、`design.md`、`tasks.md`（每项复选框**回指需求编号**）；缺陷走独立 `bugfix.md` 三段式并必须写"未变行为"。**OpenSpec**：`explore`（零写入）→ `propose` → `apply` → `archive`，DAG 是 proposal→{specs,design}→tasks 的**分叉而非链** |
| 关键克制（三家一致） | 把"判定权"留人：Spec Kit 的 `implement` 读 checklist 勾选状态**当门，但只读、不替 AI 打勾**，且 `converge` 只追加、绝不改删代码；OpenSpec 的 `propose` 命令内写死**只产规划文件、禁止编辑项目代码**，即使用户说"顺便修一下"也必须停下 |
| 核心优势 | ① 与现有 `personal-doc-manager` / `common-spec-driven` 天然衔接；② 门禁可判定：进入下一阶段前脚本校验上游产物字段；③ 决策留痕，人工可审；④ `tasks → requirement ID` 回指即是我们缺的可追溯链；⑤ 产物即状态，跨会话可续跑 |
| 主要不足 | ① OpenSpec 明确短板：**HOW 层弱、无强制质量门禁，步骤可被跳过** —— 印证"B 单独不够，必须配 D"；② 文档膨胀，小改动也走全流程；③ 产物质量依赖模型，垃圾进垃圾出 |
| 受益 | 建立需求↔任务↔代码↔测试追溯链；参考 AWS AI-DLC 披露的案例数据：LG 电子 MS 事业部效率翻倍、CJ Olive Young 3 天完成 5 个 MVP、某 B2B 项目 2 周→1 周。同时其前提判断对我们极重要：**编码只占研发流程约 20%，94% 企业引入 AI 未达预期** —— 说明只在编码段加 AI 收益有限，必须全流程 |
| 成本 | 新增流程目录约定 + 校验脚本 + 产物 schema，约 3–4 人天 |
| 复杂度 | 低–中 |
| 风险 | 只做产物不做拦截 → 退化为"更多没人读的 markdown"（OpenSpec 已自证此风险） |

#### 方案 C：多 Agent 角色编排

| 项目 | 内容 |
|------|------|
| 来源 | LangGraph（StateGraph + 类型化 state + Checkpointer）、Microsoft Agent Framework（AutoGen + Semantic Kernel 合并，2026-04-03 GA，5 种编排 pattern：Sequential/Concurrent/Handoff/Group Chat/Magentic 规划型 supervisor）、CrewAI（Process=sequential/hierarchical + Flows 事件驱动）、OpenAI Agents SDK（`handoffs` 移交控制权 vs `agents-as-tools` 子 agent 独立上下文；guardrails 分 agent 级与工具级，tripwire 触发即中止）、Claude 子代理（**独立上下文窗口，但不等于仓库隔离**，需显式 worktree） |
| HITL 真实机制 | LangGraph 用 `interrupt(payload)` 在节点内暂停，外部以 `Command(resume=v)` 恢复，**不重跑已完成节点**；前提是编译时配 checkpointer 且恢复用同 `thread_id`；支持 state 时间旅行回放。LangChain `HumanInTheLoopMiddleware` **只能在 tool_calls 执行前拦截**，内置 approve/edit/reject/respond |
| 核心优势 | ① 真正解决"技术评审无技能覆盖"：评审者是**不同上下文**的 Agent，天然缓解自证；② 每阶段上下文独立，绕开窗口限制；③ 支持自动回退循环与断点恢复 |
| 主要不足 | ① 基建最重，需自研编排层；② Agent 间信息传递有损，评审 Agent 易因缺实现细节误判；③ **成本有实测依据：Anthropic 自报多 agent 研究系统耗 ~15× token**；④ OpenAI guardrails 存在副作用陷阱：**并行护栏可能在触发前已消耗 token 甚至启动工具**，要无副作用前置检查必须改阻塞式；⑤ 38 个 skill 需转译为 agent 定义 |
| 受益 | 阶段间形成对抗性检查，质量上限最高 |
| 成本 | 高，10+ 人天起步，长期维护编排代码 + token 费用放大 |
| 复杂度 | 高 |
| 风险 | 同类模型互评趋同（相互"客套放行"）；调试链路复杂；上下文隔离 ≠ 权限隔离，误以为子代理看不到仓库改动 |

#### 方案 D：CI/CD 原生 AI 门禁 + Hook 拦截

| 项目 | 内容 |
|------|------|
| 来源 | GitHub Copilot coding agent、GitLab Duo Agent Platform、Atlassian Rovo Dev、Jenkins/GitHub Actions 内嵌 `claude -p` / `codex exec` 无头模式 |
| 实际机制 | **Copilot coding agent**：Actions 临时隔离环境里 research→plan→implement→自动跑 tests/linters→在 `copilot/*` 新分支开 **draft PR**；自动挂 CodeQL、secret scanning、dependency review、组织级防火墙 allowlist。人工门：它被视为 outside collaborator，工作流需有写权限者点 "Approve and run workflows"；它**不能**标 ready-for-review、不能 approve/merge、不能直推 main；且**"请求者不能审批自己让 Copilot 开的 PR"** —— 专门用来保住 required-approval 不被自产自销。**GitLab Duo**：tool-level approval policies 在**执行时点**卡人审 + AI audit event report 供合规。**Rovo Dev**：官方明确不直接 merge，PR 回链 Jira 验收标准 |
| **Hook 层（本方案真正的强制点）** | `AGENTS.md` 是建议，hook 是确定性脚本：在 PreToolUse/PostToolUse/Stop 等事件返回状态码，**exit 2 直接阻断动作并把 stderr 回灌给 Agent**。业界称 `AGENTS.md + hooks + skills + tests` 为 "harness engineering"（外层缰绳）。典型 `production-gate.sh`：命令含 deploy+production 时校验 `$RELEASE_APPROVAL`；pre-commit 强制把 diff 交评审，发现 CRITICAL 即阻断提交 |
| 标准依据 | OWASP AISVS 控制项 **AC.8.3**：必须验证自主 agent **无法绕过** branch protection、required reviews、required status checks、签名提交、merge queue |
| 核心优势 | ① **唯一具备物理强制力的方案**，与 AI 是否自觉无关；② hook 提供秒级本地拦截、CI 提供合并级拦截，两层互补且都比多 Agent 便宜；③ 门禁/产物/审计天然统一，复用平台原生能力；④ 生态最成熟 |
| 主要不足 | ① CI 反馈以分钟计，交互性差；② CI 内跑 E2E 需容器起服 + 浏览器（本会话已暴露端口占用/500 类环境故障，需先解决）；③ 只守"提交点"，对需求澄清的**对话过程**无约束力；④ LLM 进 CI 带来密钥与成本管理（AGENTS.md §6 已要求环境变量注入） |
| 受益 | 把"必须做"从道德约束变成物理约束。参考 Atlassian 自述数据：改造前 PR 中位 >3 天、等首条评审评论 18 小时，使用后 **PR 周期缩短 45%** |
| 成本 | 中。hook 层 1–2 人天，首条流水线 3–5 人天；需先补 `common-ci-cd` 技能 |
| 复杂度 | 中（hook 低、CI 中、E2E 环境高） |
| 风险 | 门禁过死诱发绕过（关检查 / `--no-verify`）；Flaky E2E 侵蚀信任；hook 拖慢每次工具调用 |

#### 方案 E：端到端自主编码 Agent 产品

| 项目 | 内容 |
|------|------|
| 来源 | Devin（Cognition）、OpenHands（MIT，~9.1 万 star，CodeAct：让 agent 写可执行 Python 而非 JSON function call；Sonnet 4.5 下 SWE-bench Verified ~72%）、SWE-agent（贡献 ACI：为 LLM 重造 shell/编辑器/检索）、Cursor（Plan Mode → 云 VM 并行多方案 → PR）、Codex 云 agent、Google Jules/Antigravity（同形：plan→diff→verify→PR） |
| 实际流程形态 | Devin：启动后数秒扫库出**初始计划**等人确认（或默认只等 30 秒输入），第二个 agent **Devin Review** 在人看之前先评审 PR，官方建议对 main 开分支保护防绕过；团队工作流形态上 Cursor 要求 agent **在 PR 贴截图/视频作为 proof of work** |
| 核心优势 | ① 端到端自主度最高；② 自带沙箱、终端、浏览器；③ 独立评审 agent 已是产品内建（Devin Review） |
| 主要不足 | ① **黑盒**，7 阶段边界与产物不由我们定义，"技术评审"这类定制环节无法插入；② 规范注入只能靠仓库 `AGENTS.md`，控制力弱；③ 代码与上下文出域，与安全规范冲突（`monitor` 已约束"禁止记录代码内容/密钥"）；④ 与现有 38 个 skill 无法直接复用；⑤ **独立评测数据不乐观：Answer.AI 20 任务 —— 3 成功 / 3 挂起 / 14 失败**，暴露"兔子洞循环"；Linktree 月复盘 **~300 PR 仅 ~100 合并** |
| 受益 | 适合做"批量机械任务"的旁路执行器；成本已大幅下降（Devin 2.0：$500→$20/月，ACU $2.25） |
| 成本 | 采购 + 试点，流程需重构适配 |
| 复杂度 | 接入低、治理高 |
| 风险 | 供应商锁定；审计缺失导致质量无法追责；merge 率低意味着人工兜底成本转移而非消失 |

---

## 7. 方案推荐

### 推荐：B + D 混合（B 定状态与产物，D 定拦截与门禁），C 作为阶段内局部增强

| 项目 | 结论 |
|------|------|
| 推荐组合 | **B（Spec 产物状态机）+ D（Hook 即时拦截 + CI 合并门禁）**，A 保留为规范载体层，C 只在 G3/G6 起独立上下文 Agent |
| 推荐理由 | ① 混合评分 91，高于任何单方案（D 单用 88）；② B 补 A 缺的"状态与产物契约"，D 补 A 缺的"拦截与强制力"，二者互补且**都直接消费现有 skill 资产**（规范注入均 ≥ ★★★★）；③ A 的 60 分是"结构正确但无执行力"，外挂两层即可救活，不必推倒重来；④ C 的 78 分被成本拖低（Anthropic 实测多 agent ~15× token），其核心价值——评审与编码**上下文隔离**——可在阶段内部局部实现，用现成子代理即可，不必自建编排框架；⑤ E 规范注入仅 ★★，与"38 个自定义 skill"这一最大资产冲突，且自主度证据（3/20 成功、100/300 合并）不支持作主干 |
| 分层落位 | L0 `AGENTS.md`（建议） → L1 `SKILLS-ROUTER.md`（路由） → L2 `SKILL.md`（执行） → **L3 `PIPELINE.md` + 7 份产物 schema（状态）** → **L4 hooks（动作级即时拦截）+ CI required checks（合并级拦截）** → L5 审计（复用 `monitor/`） |
| 业界对齐做法 | Spec Kit / Kiro / OpenSpec 三家一致地把"判定权留人"（checklist 不由 AI 打勾、propose 禁改业务代码、tasks 回指需求编号）—— 我们的 G3、G7 应保留人工批准点，其余可全自动 |
| 备选方案 | C（多 Agent 编排）—— 若 B+D 跑稳后需要阶段间自动回退循环，再演进；成本与 token 放大的账先算清 |
| 不推荐 | E 作主干；A 维持现状（A 单独存在即"当前不可全自动"这一结论的成因） |

### 门禁映射（推荐方案落地后的强制点）

| 阶段 | Gate | 判定方式 | 拦截层 |
|------|------|----------|--------|
| 1 需求分析 | G1 | `docs/spec/*-需求分析-*.md` 存在，每条需求项带业务目标溯源，非目标已显式排除 | Hook(Stop) + CI job |
| 2 需求澄清 | G2 | 待决项无 P0 未决；每条需求有可测验收标准（建议采用 Kiro 的 EARS 句式） | Hook + CI |
| 3 技术评审 | G3 | 评审记录存在且比较 ≥ 3 条替代路径（对齐 `personal-solution-research` 硬要求）；**由独立上下文 Agent 出具，不得由编码同一会话自评** | 独立 Agent(C) + CI；**保留人工批准** |
| 4 技术方案 | G4 | Spec 覆盖全部需求项；任务每项 ≤ 1 接口/1 文件且**回指需求编号** | Hook + CI |
| 5 编码实现 | G5 | 构建通过 + **规范静态检查 0 违规**（把 AGENTS.md §5 十条禁止项变成脚本规则） | **PreToolUse/PostToolUse hook（秒级）** + CI |
| 6 单元测试 | G6 | 全绿 + 增量行覆盖 ≥ 阈值 + **增量 mutation score ≥ 阈值** + 每个 task 关联 ≥1 用例 | CI（需先接 JaCoCo + PIT） |
| 7 UI 测试 | G7 | E2E 全绿 + 截图归档 `docs/report/`（proof of work）；流水线内可复现起服 | CI；**保留人工抽审** |

> **G5 是最便宜也最关键的一格**。AGENTS.md §5 的 10 条禁止项（包名拼接、`@RequiredArgsConstructor`、PUT/PATCH/DELETE、Controller 写业务、返回 Entity、组件内直连 axios、缺 DTO、缺异常处理、缺 `@Slf4j`、缺 AGENTS/README）目前**全部不可执行**。它们是纯语法特征，一条 grep/AST 规则即可拦截，无需模型判断——这正是 hook 该干的活。本次会话的 `com.bage.studyaidevflow` 与 `@RequiredArgsConstructor` 两次违规，一个 PostToolUse hook 就能当场挡住。

### 新增必要环节：机械预言机（G6 的实质升级）

调研中最硬的负面证据是 **AI 自写自测自评不可信**，必须引入与 LLM 无关的判定源：

| 证据 | 数据 | 对策 |
|------|------|------|
| 生成测试失败主因是**错误断言**（oracle 问题） | 综述称 **>85%** | G6 要求测试先于实现落盘（TDD 红→绿留痕），先有失败断言再有代码 |
| 模型有**确认偏误**：验证既有错误行为而非暴露它 | 定性 | 单测不能只由编码 Agent 自己写；G3/G6 引入独立上下文 |
| LLM 生成测试的变异分数显著低于人类 | **0.546 vs 人类 0.690**；部分大规模实验接近 0 | **把 mutation score 设为 G6 主门禁，coverage 降为辅助指标** |
| Defects4J 上最佳 LLM 检出率 | 163 个 bug 只检出 **8** 个 | 覆盖率绿不等于无缺陷；缺陷类断言需真实预言机 |
| 过度 mock + 边界盲区 | 测 `04-00-2025` 这类明显非法值，却不写闰年 2/29；原码与变异码同时失败 | 静态规则禁止无断言/纯 mock 断言的测试；边界用例清单化 |
| **LLM-as-judge 可被反向刷分** | 审计 8 个框架 24 个默认 judge，**无一实现 commit-first judging**；best-of-N 搜索（拿不到答案）即让 judge 接受 **90/96、93/96** 候选 | 若 G3 用 LLM 评审，必须 commit-first（评审者先独立解题再比对）+ 人工 golden set 校准 |
| Diffblue Cover 路线 | **不用 LLM**，RL 在 JVM 字节码上搜索，测试数学确定、保证可编译可通过；基准：同时间 Apache Tika 行覆盖 **54% vs Claude Code 17%** | 后端 JUnit 覆盖率补齐可用机械生成兜底；代价是断言业务意图弱，需人工重写 |
| 覆盖率反馈闭环 | Qodo cover-agent：指定 `lines_to_cover` → 生成 → **实跑校验**再迭代 | 可作为 G6 的自动补测回路 |
| Meta JiTTesting | 把每个可疑 diff **当突变体**做 mutation-guided 测试生成，再用 LLM-as-Judge 集成过滤假阳性，人只审高风险命中；全量规模捕获 **74 个 SEV 缺陷，真阳性率 ~60%** | 这是"机械预言机 + LLM 过滤 + 人兜底"的正确分层顺序，直接照抄 |

> **UI 测试的例外结论**：Playwright MCP 用可访问性快照驱动、探索后再生成 spec，多篇实战一致结论是**不宜作为硬性发布门禁**（执行带概率性、跨模型版本表现不稳）。正确定位是**探索式测试助手**，发现问题后回落到确定性 Playwright spec 再进 CI 当 G7。这条直接约束我们 `common-frontend-playwright-test` 的用法。

---

## 8. 实施路线图

| 优先级 | 事项 | 产出 | 工作量 | 收益 |
|--------|------|------|--------|------|
| **P0** | **规范 hook**：把 AGENTS.md §5 十条禁止项写成 Pre/PostToolUse 规则，`exit 2` + stderr 回灌 | `hooks/check-conventions.sh`（本地秒级拦截） | 1 人天 | 直接消灭本会话两类违规 |
| **P0** | 修复悬空依赖：补 `common-ci-cd`、`common-release-management`，`common-solution-research` → `personal-solution-research` | `requires` DAG 可被 `validate-skills.sh` 校验通过 | 0.5 人天 | 前置条件首次可机判 |
| **P1** | 新建 `common-requirement-analysis`（补阶段 1）、`common-tech-review`（补阶段 3） | 2 个 SKILL.md + 路由表注册 | 2 人天 | 7 阶段全覆盖 |
| **P1** | L3 流水线定义：`PIPELINE.md` 阶段状态 + 7 份产物 schema + `tasks→需求编号`回指 | 单一需求可从头追到尾；跨会话可续跑 | 2–3 人天 | 补齐"状态"与"契约" |
| **P1** | 门禁校验脚本 `pipeline-gate.sh {REQ-ID} {G1..G7}` | G1–G5 可机判 | 1 人天 | — |
| **P2** | 首条 CI 流水线：G5–G7 job + branch protection + required status checks | 不合规则无法合并（对齐 OWASP AC.8.3） | 3 人天 | 唯一物理强制层 |
| **P2** | 接 JaCoCo + **PIT 变异测试**；定义**增量 mutation score** 主阈值、增量行覆盖为辅助阈值 | G6 有机械预言机，覆盖率不再可信化 | 2–3 人天 | 对抗"自证测试" |
| **P2** | 解决 E2E 在 CI 内起服：固定端口、健康探针（先修 `/actuator/health` 404）、失败重试 1 次留 trace | G7 可稳定执行 | 2 人天 | — |
| **P3** | 独立技术评审 Agent：不同上下文 + **commit-first**（先独立解题再比对）+ 人工 golden set 校准 | G3 具备对抗性且抗刷分 | 3 人天 | — |
| **P3** | 复用 `monitor/`：把"技能调用序列"升级为"阶段流转审计"（只记技能名/时间/客户端/阶段，禁止代码内容与密钥） | 跳步率可统计 | 2 人天 | — |
| **P4** | 评估自主执行器（方案 E）作旁路，仅接机械型需求；要求 PR 附 proof of work | 试点报告 + merge 率实测 | 待定 | — |

> **顺序理由**：先做 P0 的 hook，因为它是**唯一一天就能上线、且立刻有强制力**的动作；P2 的 CI 是长期地基但起步慢。P3 才谈多 Agent —— 在门禁缺失时先上多 Agent，只会得到"两个会互相客套的 Agent"。

---

## 9. 风险与应对

| 风险 | 等级 | 证据 | 应对 |
|------|------|------|------|
| **AI 自证测试**：写的单测只验证自己错误的实现，全绿但错 | 高 | 模型有确认偏误（**验证既有错误行为而非暴露它**）；>85% 生成测试失败源于错误断言；Defects4J 最佳 LLM 仅检出 163 个 bug 中的 8 个 | ① G6 主门禁改**增量 mutation score**，coverage 降为辅助；② 要求测试先于实现落盘（TDD 红→绿留痕）；③ G3/G6 引入独立上下文；④ 后端覆盖率补齐可用机械生成（Diffblue 路线）兜底 |
| **LLM 评审被反向刷分** | 高 | 审计 8 个框架 24 个默认 judge，**无一实现 commit-first judging**；best-of-N 即让 judge 接受 90/96、93/96 全部通过其可见测试的候选 | G3 若用 LLM 评审：强制 commit-first（先独立解题再比对）+ rubric 原子化不重叠 + 只判客观事实（RFC 2119 MUST）+ 只判 prompt 明确要求项 + 人工 golden set 校准 |
| 覆盖率造假（刷空断言、排除目录、过度 mock） | 高 | 实证失败模式：**过度 mock** 让测试跑得快、覆盖高、对真实行为一言不发；**边界盲区**（测明显非法值却不写闰年 2/29） | 静态规则禁止无断言/纯 mock 断言测试；边界用例清单化；只看增量覆盖，禁止改排除配置 |
| **门禁被绕过** | 高 | 成熟度基线：Deloitte 2026 称仅约 **1/5 公司**对自主 agent 有成熟治理，而 74% 计划部署 | 照 OWASP AC.8.3 逐项验证 agent **无法绕过** branch protection / required reviews / required status checks / 签名提交 / merge queue；参照 Copilot 做法加"请求者不能审批自己发起的 PR" |
| 提示注入（Issue/评论里藏指令诱导 agent） | 中 | GitHub 对 Copilot agent 的处理：**过滤评论/Issue 中的隐藏字符**、只响应有写权限者、agent 视为 outside collaborator、工作流需人点 "Approve and run workflows" | 采用同一套：AI job 权限降为 outside collaborator 语义；输入过滤隐藏字符；不接受来自 Issue 文本的提权指令 |
| Playwright MCP 当硬门禁导致 Flaky 红海 | 中 | 多篇实战一致：**不宜作为发布门禁**，执行带概率性、跨模型版本不稳 | MCP 只作探索式助手；产出回落为确定性 `.spec.ts` 才进 CI 当 G7 |
| 多 Agent token 成本失控 | 中 | Anthropic 自报多 agent 研究系统 ~**15× token**；OpenAI 并行 guardrail 可能在触发前已消耗 token 甚至启动工具 | C 仅在 G3/G6 局部用；guardrail 改阻塞式；按阶段设配额 |
| 门禁僵化，改一行也走 7 阶段 | 中 | — | 需求分级：S/M 走完整 G1–G7，XS（Bug 修复）走现有 4 步；豁免需显式声明并留痕 |
| 文档产物膨胀、无人读 | 中 | OpenSpec 自述短板：HOW 层弱、步骤可跳过 | 产物只服务门禁校验，禁止重复写"给人看的长文"；`common-technical-writer` 仅对外文档时启用 |
| 跨会话状态漂移（AI 读旧产物当现状） | 中 | — | 产物带版本 + `generated_at`；每次阶段推进前重读，禁止依赖记忆 |
| CI 内 E2E 环境故障（本会话已实证） | 中 | 端口 8080 占用、H2 保留字 `user` 导致 500、`/actuator/health` 404 | 先修健康探针；固定端口；`reuseExistingServer` 仅本地；失败自动重试 1 次并保留 trace |
| LLM 密钥/用量进 CI 与 §6 安全规范冲突 | 中 | — | 仅环境变量注入；作业级配额；日志不落密钥；`.env` 禁止提交 |
| 埋点越界记录代码内容 | 中 | `monitor` 现有约束 | 沿用：只记技能名/时间/客户端/阶段，禁止代码内容、密钥、token、用户数据；服务仅监听 localhost:8090 |
| E 供应商锁定 / 质量无法追责 | 中 | Devin 早期评测 3/20 成功；Linktree ~100/300 PR 合并 | 只作旁路执行器；要求 PR 附 proof of work（截图/视频）；merge 率作为考核指标 |

---

## 10. 待确认事项

1. 覆盖率与变异阈值：建议**增量行覆盖 ≥ 70% + 增量 mutation score ≥ 50%**（后者为主），是否接受？
2. PIT 变异测试进 CI 会显著拉长构建时间，是否只在 PR 级跑、主干全量夜间跑？
3. 需求分级豁免由谁裁定 —— AI 自动分级，还是保留一个人工批准点？（业界三家一致把判定权留人）
4. G3「技术评审」是否允许完全取消人工？当前结论：**可取消但保留 30 天抽检**；若接受 LLM 评审，是否投入 commit-first + golden set 校准（这是防刷分的前提，不做则 G3 形同虚设）？
5. CI 平台：GitHub Actions（当前已有空 `.github/`，成本最低）还是内部平台？
6. hook 层需要 Qoder 侧支持 Pre/PostToolUse 事件；若客户端不支持，退化方案是 pre-commit + 编辑器侧 lint，强制力下降一档 —— 需确认可行性。
7. 是否引入机械测试生成（Diffblue 类）作后端覆盖率兜底？代价：断言业务意图弱，长期资产需人工重写。

---

## 11. 一句话结论

**我们的 skill 体系不是失败，而是被放错了层 —— 它是 L2 的执行规范，被当成了 L4 的门禁。**
把"必须做"从 `AGENTS.md` 的散文迁到 hook 的 `exit 2` 和 CI 的 required check，把"测试是否有效"从模型自评迁到 mutation score，全自动流程才成立。
最短验证路径：**先花一天写 G5 规范 hook**，用本次 User CRUD 的两类违规当回归用例。

---

## 参考资料

### 规格驱动
- [GitHub Spec Kit](https://github.com/github/spec-kit) · [官方文档](https://github.github.com/spec-kit/)
- [Spec Kit 机制解析（DeepWiki）](https://deepwiki.com/github/spec-kit) · [Microsoft Learn：Spec-Driven Development](https://learn.microsoft.com/en-us/training/modules/spec-driven-development-github-spec-kit-greenfield-intro/)
- [AWS Kiro](https://kiro.dev) · [AWS 中文博客：基于 Kiro 构建企业级 Agentic AI](https://aws.amazon.com/cn/blogs/china/building-enterprise-agentic-ai-with-kiro-on-aws/) · [AI-DLC 案例数据转载](https://www.120btc.com/coin/37194.html)
- [OpenSpec (Fission-AI)](https://github.com/Fission-AI/OpenSpec)

### 多 Agent 编排
- [LangGraph 文档](https://langchain-ai.github.io/langgraph/)
- [Microsoft Agent Framework](https://github.com/microsoft/agent-framework)
- [CrewAI 文档](https://docs.crewai.com)
- [OpenAI Agents SDK](https://openai.github.io/openai-agents-python/)
- [Anthropic：多 agent 研究系统](https://www.anthropic.com/engineering/built-multi-agent-research-system) · [长时运行 harness](https://www.anthropic.com/engineering/effective-harnesses-for-long-running-agents)

### 自主编码 Agent
- [Devin 2025 Release Notes](https://docs.devin.ai/release-notes/2025)
- [OpenHands](https://github.com/All-Hands-AI/OpenHands) · [SWE-agent](https://github.com/SWE-agent/SWE-agent)
- [Cursor 文档](https://cursor.com/docs) · [Google Codelab：Antigravity agentic UI 自动化](https://codelabs.developers.google.cn/agentic-ui-automation-with-antigravity)

### CI/CD 与治理
- [GitHub Copilot coding agent 概念文档](https://docs.github.com/en/copilot/concepts/agents/coding-agent/about-coding-agent)
- [GitLab Duo Agent Platform](https://docs.gitlab.com/user/duo_agent_platform/)
- [Atlassian：AI 代码评审将 PR 周期缩短 45%](https://www.atlassiancn.com/post/atlassian-%E5%A6%82%E4%BD%95%E9%80%9A%E8%BF%87-ai-%E4%BB%A3%E7%A0%81%E8%AF%84%E5%AE%A1%E5%B0%86-pr-%E5%91%A8%E6%9C%9F%E7%BC%A9%E7%9F%AD-45)
- [OWASP AISVS 附录 C：AI 安全编码（控制项 AC.8.3）](https://github.com/OWASP/AISVS/blob/main/1.0/research/appendices/Appendix-C-AI-Secure-Coding.md)
- [Claude Code Hooks](https://docs.claude.com/en/docs/claude-code/hooks) · [AI Coding Agent Hooks 解释](https://www.mindstudio.ai/blog/ai-coding-agent-hooks-explained) · [Harness Engineering](https://heym.run/blog/harness-engineering)
- [awesome-ai-agent-governance](https://github.com/systempromptio/awesome-ai-agent-governance) · [UIPath：agent 安全与治理](https://www.uipath.com/platform/agentic-automation/foundation/security-governance)

### 测试验证与预言机
- [Diffblue Cover](https://diffblue.com/cover) · [Qodo cover-agent](https://github.com/QodoAI/cover-agent)
- [arXiv 2511.21382：LLM 测试生成综述（oracle 问题、确认偏误、变异分数 0.546 vs 0.690）](https://arxiv.org/abs/2511.21382)
- [arXiv 2508.00408：Defects4J 基准研究](https://arxiv.org/abs/2508.00408) · [arXiv 2602.00409：coding agent 测试过度 mock 与边界盲区](https://arxiv.org/abs/2602.00409)
- [Meta Engineering：JiTTesting（2026-10-29）](https://engineering.fb.com/2026-10-29/security/catching-bugs-before-they-land-jittestings-human-on-the-loop-approach-at-full-scale)
- [LLM-as-judge 可被刷分的审计（2026-09-03 日报）](https://aihot.news/daily/2026-09-03)
- [microsoft/playwright-mcp](https://github.com/microsoft/playwright-mcp) · [Playwright MCP 不宜作硬门禁的实战评述](https://blog.csdn.net/weixin_28829325/article/details/167503922)

> 注：外部数据均为二手来源（官方文档、厂商自述、arXiv 预印本、第三方评测），厂商自述类（AWS AI-DLC、Atlassian 45%、Devin 效率 +83%）存在利益相关，仅作参考不作承诺依据。arXiv 编号为检索所得，落地前建议复核原文。
