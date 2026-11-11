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

回答一个问题：**要让 AI 完全自动实现「需求分析 → 需求澄清 → 技术评审 → 技术方案 → 编码实现 → 单元测试 → UI 测试」全流程（不设人工门禁），当前体系缺什么、怎么补？**

目标口径由需求方明确为**完全 AI 自动实现**，因此本报告不接受"保留人工审批"作为默认答案；凡出现人工点，都必须给出替代机制或明确其不可去除的理由（见 §12）。

具体拆解为 6 个子目标：

1. 把 7 个阶段逐一映射到「输入 / 技能 / 产物 / 通过判据」，找出无技能覆盖的阶段；
2. 找出"AI 跳过环节"的根因，并给出**不可绕过**（而非"建议遵守"）的门禁机制；
3. 比对 5 种业界主流编排架构，给出适合本团队的推荐组合；
4. **定义自主度分级（L0–L5），确定"完全 AI 自动"落在哪一级**（§11）；
5. **给出"用机器判定替掉人工判定"的具体机制**：确定性预言机、对抗 Agent、自动回滚（§12）；
6. 给出可分阶段落地的路线图与风险清单。

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

> **注 1**：第 3、7 阶段是"看起来有、实际断"的典型。第 7 阶段在本次 User CRUD 验证中因 H2 保留字 `user` 导致 500，若无浏览器实跑，仅跑单测会漏掉该缺陷 —— 证明 UI 阶段不可降级为可选项。
>
> **注 2**：按"完全 AI 自动"目标回看，7 阶段本身**不足以构成闭环**，需在两端各补一段（详见 §12）：
>
> | 补位阶段 | 内容 | 缺失后果 | 所需技能 |
> |----------|------|----------|----------|
> | **阶段 0 需求源** | 需求从哪来、是否值得做 | 流程只能被动接需求，谈不上自主；且"是否值得做"**无法机械化** | 现有技能无覆盖（远期 L5 课题） |
> | **阶段 8 发布与自纠** | 灰度 → 指标判定 → 自动放量/回滚 → 回滚后自动开缺陷需求回到阶段 1 | **闭环断在"测试通过"**，AI 永不接触真实反馈，自动回滚缺位也使人工无法被拿掉 | `common-feature-flag` ✅ 已有；`common-ci-cd` ❌ 缺；`common-release-management` ❌ 缺 |
>
> 也就是说：把 7 个阶段全部做完，仍只是 **L2→L3 的一半**；没有阶段 8，就没有"敢于取消人工审批"的资格。

---

### 3.5 第 0 号实验：本次 User CRUD 全流程回放（一手证据）

上述缺口分析不必假设——**本次会话本身就是一次完整的"当前体系跑全自动研发"实验，且已失败 7 次**。7 次全部由人工发现，**0 次由流程自检发现**。这就是基线。

| # | 实际发生（本会话） | 由谁发现 | 应被哪个 Gate 拦下 | 具体拦截手段（无需模型判断） |
|---|---------------------|----------|-------------------|------------------------------|
| E1 | 初始建项目包名为 `com.bage.studyaidevflow`，未加载任何规范技能 | 人工追问"为什么没按 skill 规范" | G5 | grep：包名末段与项目名连字符拆分不一致即 `exit 2` |
| E2 | 使用 `@RequiredArgsConstructor` 构造器注入 | 人工 | G5 | grep `@RequiredArgsConstructor`（AGENTS.md §2.2） |
| E3 | Controller 出现 `@DeleteMapping`（违反仅 GET/POST） | 人工 | G5 | grep `@(Put|Patch|Delete)Mapping` |
| E4 | 无 DTO 层、无 `GlobalExceptionHandler`、无 `@Slf4j` | 人工 | G4 + G5 | 规则：Controller 返回类型必须为 `*Response`；`@RestControllerAdvice` 类数 ≥1；`@Service` 实现类须含 `@Slf4j` |
| E5 | **交付 User CRUD 时未产出任何测试** | 人工显式追问 | G6 / G7 | **Stop hook**：会话收尾前校验"源码有变更 ⇒ `src/test/**` 或 `e2e/**` 亦有变更"，否则阻断 |
| E6 | H2 保留字 `user` 导致所有接口 HTTP 500 | **只有实跑后端 + 真实调 API 才暴露** | G7 + G8 | 18 个单测（Mockito mock 掉 repository）**结构上不可能测出此缺陷** —— 这是"阶段 7/8 不可降级"的实证 |
| E7 | `/actuator/health` 返回 404（依赖在、端点未暴露） | 我改用 `/api/users` 当就绪探针，**绕过了，无人拦截** | G8 | 健康探针失败即阻断放量/回滚；此例是典型"缺陷被绕过而非被修复"，正是无人化后最危险的模式 |

**回放得出的三条硬结论**：

1. **E1–E5 全部可被 hook 当场挡住**，且拦截手段全是纯文本/AST 规则，不需要模型参与判断，不需要 CI，不需要多 Agent。→ 这印证 §12.4 把 G5 列为"去人工第 1 顺位"不是推测。
2. **E6 证明 mock 型单测对真实缺陷零证明力**。单测把 Repository 整个 mock 掉，**结构上不可能触达 DDL/SQL 层**，因此表名冲突这一致命缺陷只在"真实起服 + 实跑 API"时才暴露（本会话正是在实跑阶段才发现）。→ 直接支持"变异分数 > 覆盖率""阶段 7/8 不可选"两条判断，且说明 **G6 的绿与产品可用之间没有蕴含关系**。
3. **7/7 由人工发现、0/7 由流程发现** —— 若这是"AI 全自动"的现状基线，那么当前实际自主度**低于 L2**，因为 L2 定义中人是逐阶段审批者，而这里人是**事后纠错者**。纠错成本远高于门禁成本：E1–E5 的重构是整包重写，一次 G5 hook 是 1 人天。

> 建议：把这 7 条作为 G5/G6/G7/G8 的**验收用例**固化进仓库（`scripts/tests/gate-replay/`）。任何门禁改动必须先让这 7 例从"人工发现"变成"自动阻断"，才算流程有进步。这是本报告唯一可立即证伪的部分。

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

### 推荐（基线版）：B + D 混合 —— 允许保留人工门禁的前提

> ⚠️ **本节是"允许人工审批"前提下的结论。目标为完全 AI 自动，最终推荐见 §12.6：`B + D + C(限 G3) + 策略引擎(G8)`（94 分）。** 本节保留是因为 B/D 的分工理由不变，仅 C 的地位从"可选"升为"必需"。

| 项目 | 结论 |
|------|------|
| 推荐组合 | **B（Spec 产物状态机）+ D（Hook 即时拦截 + CI 合并门禁）**，A 保留为规范载体层，C 只在 G3/G6 起独立上下文 Agent |
| 推荐理由 | ① 混合评分 91，高于任何单方案（D 单用 88）；② B 补 A 缺的"状态与产物契约"，D 补 A 缺的"拦截与强制力"，二者互补且**都直接消费现有 skill 资产**（规范注入均 ≥ ★★★★）；③ A 的 60 分是"结构正确但无执行力"，外挂两层即可救活，不必推倒重来；④ C 的 78 分被成本拖低（Anthropic 实测多 agent ~15× token），其核心价值——评审与编码**上下文隔离**——可在阶段内部局部实现，用现成子代理即可，不必自建编排框架；⑤ E 规范注入仅 ★★，与"38 个自定义 skill"这一最大资产冲突，且自主度证据（3/20 成功、100/300 合并）不支持作主干 |
| 分层落位 | L0 `AGENTS.md`（建议） → L1 `SKILLS-ROUTER.md`（路由） → L2 `SKILL.md`（执行） → **L3 `PIPELINE.md` + 7 份产物 schema（状态）** → **L4 hooks（动作级即时拦截）+ CI required checks（合并级拦截）** → L5 审计（复用 `monitor/`） |
| 业界对齐做法 | Spec Kit / Kiro / OpenSpec 三家一致把"判定权留人"（checklist 不由 AI 打勾、propose 禁改业务代码、tasks 回指需求编号）。**这与本次目标（完全 AI 自动）冲突**，见 §12：要用「确定性预言机 + 对抗 Agent + 自动回滚」替掉人工判定，而不是简单删掉审批 |
| 备选方案 | C（多 Agent 编排）—— 若 B+D 跑稳后需要阶段间自动回退循环，再演进；成本与 token 放大的账先算清 |
| 不推荐 | E 作主干；A 维持现状（A 单独存在即"当前不可全自动"这一结论的成因） |

### 门禁映射（推荐方案落地后的强制点）

| 阶段 | Gate | 判定方式 | 拦截层 | 可否无人 | 替代人工判定所需的前置条件 |
|------|------|----------|--------|----------|---------------------------|
| 1 需求分析 | G1 | `docs/spec/*-需求分析-*.md` 存在，每条需求项带业务目标溯源，非目标已显式排除 | Hook(Stop) + CI job | ✅ 可无人 | 溯源链写成 schema 必填字段，机判覆盖率 100% |
| 2 需求澄清 | G2 | 待决项无 P0 未决；每条需求有可测验收标准（采用 Kiro 的 EARS 句式） | Hook + CI | ✅ 可无人 | 验收标准必须是**可执行断言**（EARS 天然可转 Playwright/JUnit），不可测即判未澄清 |
| 3 技术评审 | G3 | 评审记录存在且比较 ≥ 3 条替代路径；**由独立上下文 Agent 出具** | 独立 Agent(C) + CI | ⚠️ 有条件 | 需 commit-first 评审 Agent + 人工 golden set 校准 + 分歧仲裁器（见 §12.3）。**最后一个去掉的人工点** |
| 4 技术方案 | G4 | Spec 覆盖全部需求项；任务每项 ≤ 1 接口/1 文件且**回指需求编号** | Hook + CI | ✅ 可无人 | 需求↔任务↔代码↔测试四向可追溯表由脚本生成并校验 |
| 5 编码实现 | G5 | 构建通过 + **规范静态检查 0 违规**（AGENTS.md §5 十条禁止项变脚本） | **Pre/PostToolUse hook（秒级）** + CI | ✅ 可无人 | 十条规则全部机械化；无一条需要模型判断 |
| 6 单元测试 | G6 | 全绿 + 增量行覆盖 ≥ 阈值 + **增量 mutation score ≥ 阈值** + 每 task 关联 ≥1 用例 | CI（JaCoCo + PIT） | ✅ 可无人 | 变异分数取代"人判断测试好不好"；这是无人化的**核心支点** |
| 7 UI 测试 | G7 | E2E 全绿 + 截图归档 + **视觉/结构断言**（可访问性快照比对） | CI | ⚠️ 有条件 | 纯"绿"不足证；需结构断言 + 金丝雀 + 自动回滚兜底 |
| 8 发布验证（新增） | G8 | 灰度指标无异常 → 自动放量；异常 → **自动回滚 + 自动开缺陷需求回到阶段 1** | 部署流水线 | ✅ 可无人 | 无 G8 则闭环断在"测试通过"，不算全自动。依赖 `common-feature-flag` + `common-ci-cd`（当前缺失） |

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
| **P0** | **规范 hook**：把 AGENTS.md §5 十条禁止项写成 Pre/PostToolUse 规则，`exit 2` + stderr 回灌；同步加"源码变更 ⇒ 测试亦变更"的 Stop hook | `hooks/check-conventions.sh` + `hooks/require-tests.sh` | 1 人天 | 当场挡住 §3.5 的 **E1–E5**（含"交付不带测试"） |
| **P0** | 修复悬空依赖：补 `common-ci-cd`、`common-release-management`，`common-solution-research` → `personal-solution-research` | `requires` DAG 可被 `validate-skills.sh` 校验通过 | 0.5 人天 | 前置条件首次可机判 |
| **P1** | 新建 `common-requirement-analysis`（补阶段 1）、`common-tech-review`（补阶段 3） | 2 个 SKILL.md + 路由表注册 | 2 人天 | 7 阶段全覆盖 |
| **P1** | L3 流水线定义：`PIPELINE.md` 阶段状态 + 7 份产物 schema + `tasks→需求编号`回指 | 单一需求可从头追到尾；跨会话可续跑 | 2–3 人天 | 补齐"状态"与"契约" |
| **P1** | 门禁校验脚本 `pipeline-gate.sh {REQ-ID} {G1..G7}` | G1–G5 可机判 | 1 人天 | — |
| **P2** | 首条 CI 流水线：G5–G7 job + branch protection + required status checks | 不合规则无法合并（对齐 OWASP AC.8.3） | 3 人天 | 唯一物理强制层 |
| **P2** | 接 JaCoCo + **PIT 变异测试**；定义**增量 mutation score** 主阈值、增量行覆盖为辅助阈值 | G6 有机械预言机，覆盖率不再可信化 | 2–3 人天 | 对抗"自证测试" |
| **P2** | 解决 E2E 在 CI 内起服：固定端口、健康探针（先修 `/actuator/health` 404）、失败重试 1 次留 trace | G7 可稳定执行 | 2 人天 | — |
| **P3** | 独立技术评审 Agent：不同上下文 + **跨模型 family** + **commit-first**（先独立解题再比对）+ 人工 golden set 校准 | G3 具备对抗性且抗刷分 | 4 人天 | 无人化的最后一块，**最难，放最后** |
| **P3** | 分歧仲裁策略：Proposer/Critic 不一致时走"小爆炸半径 + 强制灰度 + 收紧回滚阈值"，分歧率作指标 | 人从审批者变异常处理者 | 2 人天 | — |
| **P3** | 复用 `monitor/`：把"技能调用序列"升级为"阶段流转审计"（只记技能名/时间/客户端/阶段，禁止代码内容与密钥）；参照 aGiTrack 思路把每轮调用的 prompt/model/token 提交进 git，Phoenix 类做 OTel trace+eval | 跳步率、分歧率、judge 准确率可统计 | 2 人天 | **无人值守后，审计是唯一事后追责手段** |
| **P4** | **补 `common-ci-cd` + `common-release-management` 两个技能**（当前为悬空引用） | 阶段 8 有据可依 | 2 人天 | — |
| **P4** | **阶段 8 发布闭环**：flag 默认关 → 金丝雀 1/5/25/100% → 指标异常自动回滚 → 回滚自动生成缺陷需求回到阶段 1 | 流程真正闭合；**这是取消人工审批的资格证明** | 5 人天 | 依赖 `common-feature-flag` |
| **P4** | 放量决策交给**策略引擎**（非 AI、非人）：`无 P0 告警 ∧ 增量覆盖/变异达标 ∧ 灰度无异常 ∧ 契约测试通过 → 自动放量`；AI 永远只产 draft PR | 同时满足"人不在关键路径"与 OWASP AC.8.3 | 3 人天 | 关键合规设计 |
| **P5** | 自主度里程碑验收：先 L3（人抽检），稳定 3 个月无 P0 逃逸后再评估 L4 | 分级放权，避免一次性去人工 | — | — |
| P6 | 评估自主执行器（方案 E）作旁路，仅接机械型需求；要求 PR 附 proof of work | 试点报告 + merge 率实测 | 待定 | — |
| 🔭 | 阶段 0 需求源自动化（L5） | **本报告不含设计** —— "需求是否值得做"无法机械化 | 待定 | 属另一课题 |

> **顺序理由**：先做 P0 的 hook，因为它是**唯一一天就能上线、且立刻有强制力**的动作；P2 的 CI 是长期地基但起步慢。P3 才谈多 Agent —— 在门禁缺失时先上多 Agent，只会得到"两个会互相客套的 Agent"。**P4 的发布闭环是"能否拿掉人工"的分水岭**：没有自动回滚就没资格取消审批，这条不能往后排到无人化之后再做。

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

> 第 2、3、5、6、8 项在 §12–§14 已给出**建议默认值**，可直接确认而不必展开讨论；第 1、4 项是真正的方向性决策，需你先答复。

1. **目标口径确认**：接受"完全 AI 自动 = L3/L4（AI 自主交付、机器判定、自动回滚、人做抽检与策略）"这一定义吗？若坚持"零人员参与"，那是 L5，缺口在需求源（阶段 0），需另立项。
2. 阈值定档：建议**增量行覆盖 ≥ 70% + 增量 mutation score ≥ 50%**（后者为主门禁）。是否接受把变异分数作为 G6 的**决定性**指标？
3. PIT 变异测试显著拉长构建时间：是否只在 PR 级跑**增量变异**、夜间跑全量？
4. **G3 是否允许最终无人化**：若允许，必须先投入 commit-first + 跨模型 family + golden set 校准（§12.2 四条硬约束）。**不投入则 G3 形同虚设，此时应保留人工而非假装自动** —— 这是本报告最需要一个明确答复的点。
5. **发版权限归属**：是否接受"AI 永远只产 draft PR，放量由策略引擎（非 AI、非人）决定"这一设计？这是同时满足"人不在关键路径"与 OWASP AC.8.3 的唯一已知解。若希望 AI 直接 merge，需明确该安全基线由谁豁免。
6. 无人化推进节奏：按 §12.4 的 6 步顺序逐 Gate 摘除人工，还是先做工具链再一次性摘除？建议前者（可分批验收、风险可控）。
7. 需求分级豁免：S/M 走完整 G1–G8，XS（Bug 修复）走 4 步；豁免由 AI 自动判定并留痕，还是保留一个策略级人工开关？
8. CI 平台：GitHub Actions（当前已有空 `.github/`，成本最低）还是内部平台？
9. hook 层需客户端支持 Pre/PostToolUse 事件；若不支持，退化为 pre-commit + lint，强制力降一档 —— 需确认 Qoder 侧能力。
10. 是否引入机械测试生成（Diffblue 类）作后端覆盖率兜底？代价：断言业务意图弱，长期资产需人工重写。
11. 两个缺失技能（`common-ci-cd`、`common-release-management`）是新建还是包装开源方案？阶段 8 依赖它们。

---

## 11. 自主度分级（对齐"完全 AI 自动"目标）

| 级别 | 名称 | 判定者 | 人的位置 | 状态 |
|------|------|--------|----------|------|
| L0 | 人工研发 | 人 | 关键路径 | — |
| L1 | AI 辅助编码 | 人 | 关键路径 | — |
| L2 | AI 按规范执行，人逐阶段审批 | 人 | 关键路径 | ✅ **我们在此**，且因无拦截层，实际执行率还低于 L2 定义 |
| L3 | AI 全流程自主交付，人抽检 + 处理异常 | **机器** | **旁路** | 🎯 本报告落地目标 |
| L4 | L3 + 自动回滚 + 策略引擎决定放量 | 机器 | 只定策略 | 需 G8 + 三个缺失技能补齐 |
| L5 | 需求源也自动化（AI 从数据/反馈生成需求） | 机器 | 不在环 | 远期，本报告不含设计 |

**必须先纠正一个定义**：「完全 AI 自动」不等于"没有任何人参与"，而是 **"人不在关键路径上"**。L3 之后人的角色从**审批者**变成**异常处理者 + 策略制定者**。若坚持"一个人都不碰"，那要求的是 L5，需要额外解决"需求从哪来"，是另一个课题。

---

## 12. 无人化改造：用什么替掉人工判定

拿掉人工的前提，不是"模型更准"，而是**把人工承担的四件事分别找到替代物**：

| 人工原本做的事 | 替代机制 | 成熟度 |
|----------------|----------|--------|
| 判断"合不合规" | 确定性预言机（hook + 静态规则 + schema） | 高，可立刻做 |
| 判断"测试有没有用" | **变异测试**（机械预言机，与 LLM 无关） | 高，需接 PIT |
| 判断"方案好不好" | 对抗 Agent 三元组 + 分歧仲裁 | **中，最难，最后去人工** |
| 兜住"错了怎么办" | 功能开关 + 金丝雀 + **自动回滚** | 高，是去掉人工的**唯一前置条件** |

### 12.1 确定性预言机（承担全部可机判项）

| 判定项 | 可机判 | 机械手段 |
|--------|--------|----------|
| AGENTS.md §5 十条禁止项 | ✅ | grep / AST 规则，无需模型 |
| 需求溯源完整性 | ✅ | schema 必填字段校验 |
| 验收标准是否可测 | ✅ | EARS 句式 → 自动编译成断言；编译失败即判"未澄清" |
| 需求↔任务↔代码↔测试一致性 | ✅ | 四向追溯表脚本生成 + 校验 |
| 测试是否真的有效 | ✅ | **增量 mutation score（PIT）** |
| 接口契约一致 | ✅ | `common-contract-test` |
| 视觉是否被破坏 | ✅（有限） | 可访问性树/结构快照比对，**不用 Playwright MCP 当门禁** |
| 性能是否回归 | ✅ | `common-load-testing` + 基线比对 |
| 依赖与代码安全 | ✅ | SCA + 静态扫描（CodeQL 类）+ `common-security-audit` |
| 方案取舍的价值判断 | ❌ | → 12.2 |
| 需求是否真实/值得做 | ❌ | → 阶段 0（L5 才自动化） |

**结论：7 个阶段里只有 G3 的"方案好不好"和阶段 0 的"要不要做"无法机械化。** 这是"完全 AI 自动"在工程上可行的根本原因。

### 12.2 对抗 Agent 三元组（承担不可机判项）

无人值守时，`Proposer（编码）→ Critic（评审）→ Arbiter（仲裁）` 必须满足四条硬约束，否则等价于"AI 自己给自己盖章"：

1. **跨模型 family**：Critic 与 Proposer 不得同源模型 —— 同类模型互评会趋同（互相客套放行）。这是无人化的必要条件，不是优化项。
2. **commit-first judging**：Critic 必须先**独立解一遍题**再比对候选。否则会被刷分——审计发现 8 个主流框架的 24 个默认 judge 配置**无一实现 commit-first**，best-of-N 搜索（即使拿不到答案）就让 judge 接受了 90/96、93/96 个"全部通过其可见测试"的错误候选。
3. **rubric 机械化**：判项原子化且不重叠、只判客观事实（RFC 2119 的 MUST）、只判 prompt 里明确要求的项；用人工 golden set 定期校准 judge，judge 准确率跌破阈值则该 Gate 自动降级为"需人工"。
4. **分歧不 escalate 给人，走保守策略**：Proposer 与 Critic 分歧时 → 选爆炸半径更小的方案 + 强制灰度起步比例下调 + 回滚阈值收紧。**分歧率本身作为指标**，连续超阈值才升级人工。这是"人不在关键路径"的具体实现方式。

### 12.3 自动回滚：敢于去掉人工的真正前提

行业把判定权留人，不是因为它比 AI 准，而是因为**人工审批是最便宜的回滚触发器**。拿掉它，必须换成机器可执行的安全网：

- 每次交付默认关在功能开关后（`common-feature-flag` **已存在**）
- 金丝雀 1% → 5% → 25% → 100%，指标异常自动回滚
- 回滚后**自动创建缺陷需求，回到阶段 1** —— 这样流程才闭合成环，否则"全自动"断在测试通过那一步
- 放量决策交给**策略引擎**而非人：`无 P0 告警 ∧ 增量覆盖/变异达标 ∧ 灰度无异常 ∧ 契约测试通过 → 自动放量`

> ⚠️ **一处必须正视的张力**：OWASP AISVS AC.8.3 要求"必须验证自主 agent **无法绕过** branch protection、required reviews、required status checks、签名提交、merge queue"。也就是说安全基线在**主动阻止** AI 自己发版（GitHub 的做法正是把 coding agent 当 outside collaborator：不能 approve/merge、不能直推 main、请求者不能审批自己发起的 PR、工作流还需人点 "Approve and run workflows"）。
> **解法不是让 AI 拿到合并权限**，而是：**AI 永远只产出 draft PR，发版动作由策略引擎（非 AI、非人）持有**。这样既守住 AC.8.3，又实现人不在关键路径。这条是本方案与"完全自主 agent 产品"（方案 E）的根本分歧点。

### 12.4 去掉人工的顺序（按风险从低到高，可分批验收）

| 顺序 | Gate | 去人工条件 | 依据 |
|------|------|-----------|------|
| 1 | G5 | hook 规则上线即生效 | 十条全是语法特征，零判断 |
| 2 | G1 / G2 / G4 | 产物 schema + 可测断言校验就绪 | 纯结构校验 |
| 3 | G6 | PIT 接入、变异阈值定档 | 机械预言机取代"人评测试质量" |
| 4 | G7 | 结构断言 + 金丝雀就位 | 纯 E2E 绿不具备证明力 |
| 5 | G8 | 策略引擎 + 自动回滚就位 | 见 12.3 |
| 6 | **G3（最后）** | 跨 family Critic + commit-first + golden set 校准 | 唯一无机械替代的判定 |

### 12.5 成本与延迟账（有出处，用于设预期）

| 项 | 数据 | 对本方案的影响 |
|----|------|----------------|
| 多 Agent token 放大 | Anthropic 自报 ~**15×** | 每需求至少 3 个上下文（编码/评审/仲裁），成本按此预估，不是"多几次调用" |
| CI 单会话上限 | GitHub Actions 单 agent 会话 **59 分钟** | 大需求必须拆到 G4 的"每项 ≤1 接口/1 文件"粒度，否则跑不完 |
| 并行 guardrail 副作用 | OpenAI：并行护栏可能在触发前**已消耗 token 甚至启动工具** | 需无副作用的前置检查必须改**阻塞式** |
| PIT 构建时长 | — | PR 级只跑**增量变异**，夜间全量 |
| 现实完成率基线 | Devin 早期评测 **3/20 成功**；Linktree **~300 PR 仅 ~100 合并** | 全自动初期必须按"AI 产出需返工"建模，禁止用"AI 直接可合并"做容量假设 |
| 治理成熟度基线 | Deloitte 2026：仅约 **1/5 公司**对自主 agent 有成熟治理，74% 计划部署 | 我们做这件事不晚，但也**没有成熟范本可抄**，必须自带回滚与审计 |

### 12.6 因目标变更而作出的结论修正

原推荐 **B + D（C 仅局部）** 是"允许保留人工点"前提下的最优。目标锁定"完全 AI 自动"后，评估维度需重排：**门禁强制力 20%→30%**，并新增**无人化可行性**（15%）。重算如下：

| 维度 | 权重 | A | B | C | D | E | **B+D+C(G3)+策略引擎** |
|------|------|---|---|---|---|---|---|
| 自动化完整度 | 20% | 3 | 4 | 5 | 4 | 4 | 5 |
| 门禁强制力 | **30%** | 1 | 3 | 4 | 5 | 3 | 5 |
| **无人化可行性（新增）** | **15%** | 1 | 3 | 4 | 5 | 3 | 5 |
| 规范可注入性 | 12% | 5 | 5 | 4 | 4 | 2 | 5 |
| 可观测可追溯 | 10% | 2 | 4 | 4 | 5 | 3 | 5 |
| 落地成本（低=高分） | 8% | 5 | 4 | 2 | 4 | 3 | 2 |
| 生态成熟度 | 5% | 2 | 3 | 3 | 5 | 2 | 4 |
| **综合评分** | 100% | **47** | **72** | **80** | **92** | **61** | **94** |

（分值 1–5，计算式 `Σ(分值 × 权重) / 5`；§6.1 的表是旧权重下的结果，保留作对照）

**目标变更后三处实质变化**：

| 修正项 | 修正前 | 修正后 | 原因 |
|--------|--------|--------|------|
| C（多 Agent）定位 | 可选增强，被成本拖低（旧 78 分） | **必需**，且跃居单方案第二（80 分） | 不可机判项失去人工兜底，只能靠跨 family 对抗 + commit-first |
| 最终推荐 | B + D（91） | **B + D + C(限 G3) + 策略引擎**（94） | 缺一则"全自动"不成立 |
| B 的地位 | 状态与产物载体 | 权重上升：无人判定时**机器读产物是下游唯一输入**，schema 缺失即断链 | — |
| D 的成本分 | 高 | 被 30% 权重压倒性放大 | 无人值守时强制力是唯一防线，基建再贵也要付 |
| E | 不推荐作主干（旧 59） | 仍不推荐（61） | 黑盒导致"无人化条件"根本无法验证，AC.8.3 也无从审计 |

---

## 13. 度量体系与成功定义

无人化之后，**没有仪表盘就等于没有治理**（Deloitte 2026：仅约 1/5 公司对自主 agent 有成熟治理，74% 计划部署）。本报告的落地必须绑定下列指标，否则无法判断"自动化在变好还是在静默腐化"。

### 13.1 指标分层

| 层 | 指标 | 定义 | 目标 | 采集来源 |
|----|------|------|------|----------|
| **北极星** | P0 逃逸缺陷数 / 月 | 到达主干或生产后才被发现的严重缺陷 | 基线 = §3.5 的 7 例 → 目标 **0** | 缺陷回溯标注 |
| 门禁 | **跳步率** | 未产出上游产物就进入下一阶段的比例 | **必须为 0**（非"低"） | hook 阻断日志 + `monitor/` 阶段流转审计 |
| 门禁 | Gate 一次通过率 | 各 Gate 首次执行即通过的比例 | 趋势监控，不设 KPI | CI job 结果 |
| 自主 | **人工介入次数 / 需求** | 人在关键路径上被迫动作的次数 | 逐季下降，L3 目标 ≤1（仅异常处理） | hook 升级事件 + PR 人工评论数 |
| 自主 | AI 产出返工率 | draft PR 被要求重写的比例 | 参照基线 Linktree ~100/300 合并 → 初期预期 **>50% 返工**，须按此建模容量 | PR 状态流转 |
| 质量 | 增量 mutation score | 只对变更代码计算 | **主门禁**，≥50% | PIT |
| 质量 | 增量行覆盖率 | 辅助指标，不作达标依据 | ≥70% | JaCoCo |
| 质量 | E2E Flaky 率 | 同 commit 重跑结果不一致比例 | <2%，超标则该 spec 移出硬门禁 | CI 重试记录 |
| 评审 | judge 分歧率 | Proposer/Critic 不一致比例 | 监控；**连续超阈值 → 该 Gate 自动降级为需人工** | 评审 Agent 日志 |
| 评审 | **judge golden-set 准确率** | 在人工标注集上 judge 的判定正确率 | 跌破阈值即视为 judge 失效（防 best-of-N 刷分） | 月度校准跑批 |
| 效率 | lead time | 需求提出 → 灰度 100% 的历时 | 趋势 | 流水线时间戳 |
| 成本 | 单需求 token 费用 | 含编码/评审/仲裁 3 上下文，参照 ~15× 单 agent | 预算内 | Agents SDK trace / aGiTrack 式提交 |

### 13.2 明确的**反指标**（禁止作为优化目标，会被刷）

| 反指标 | 为什么禁 |
|--------|----------|
| 纯覆盖率 | 实证：过度 mock 让覆盖率高但对真实行为一言不发；部分 LLM 套件变异分数接近 0 |
| AI 生成代码占比 | 只激励多写，与质量无关 |
| PR 数量 / 提交次数 | E6 类缺陷会在"绿了"之后依然存在 |
| judge 通过率 | 24 个默认 judge 无一实现 commit-first，best-of-N 即可刷到 93/96 接受 |

### 13.3 分级放权规则（"去人工"必须可进可退）

- **摘除人工**：某 Gate 连续 ≥20 个需求跳步率 0、且其对应逃逸缺陷为 0 → 该 Gate 转为无人。
- **回退人工**：出现 1 例该 Gate 应拦未拦的 P0，或 judge golden-set 准确率跌破阈值 → **自动降级为需人工**，并在修复前不得再次摘除。
- 该规则本身写入策略引擎，不靠人记。这是"全自动"不与"安全基线"冲突的关键。

---

## 14. 成本总量与分期

### 14.1 人天汇总（对齐 §8 路线图）

| 期 | 内容 | 人天 | 累计 | 达成状态 |
|----|------|------|------|----------|
| M1（约 1 周） | P0 全部（G5 hook + 修悬空依赖） | **1.5** | 1.5 | §3.5 的 E1–E5 转为自动阻断；仍是 L2 但**执行率从随机变确定** |
| M2（约 2 周） | P1（两个新技能 + L3 产物 schema + 门禁脚本） | **5.5** | 7 | G1–G5 可机判，跨会话可续跑 → **L3 前半** |
| M3（约 3 周） | P2（CI 流水线 + JaCoCo/PIT + E2E 起服） | **7.5** | 14.5 | G5–G7 硬门禁 + 变异分数就位 → **L3** |
| M4（约 2 周） | P3（跨 family Critic + 分歧仲裁 + 阶段审计） | **8** | 22.5 | G3 具备对抗性；审计可追责 |
| M5（约 3 周） | P4（补两技能 + 阶段 8 闭环 + 策略引擎） | **10** | 32.5 | G8 自动放量/回滚 → **L4** |

- **总计约 32.5 人天 ≈ 6.5 人周**（单人约 6–7 周）。
- **最小可验证投入 = M1 的 1.5 人天**，即可产生可证伪的效果（把 §3.5 的 7 例做成回归用例跑通）。**建议只先批 M1**，用其结果决定是否继续。
- 运行期成本另计：每需求 ≥3 个上下文（编码/评审/仲裁），参照 Anthropic 自报 ~15× token 放大；PIT 增量变异为 CI 时长主因（建议 PR 级增量、夜间全量）。
- 隐性成本必须入账：初期返工率高（基线参考 ~100/300 PR 合并），**不要用"AI 直接可合并"做容量假设**。

### 14.2 分期原则

先买"强制力"（M1，1.5 人天，收益/成本比最高），再买"状态与契约"（M2），再买"预言机"（M3），**最后才买"智能"**（M4/M5）。顺序颠倒会得到"两个会互相客套的 Agent 在没有门禁的流水线上跑得很快"。

---

## 15. 本报告未覆盖的范围（明确声明）

避免读者误以为下列问题已被回答：

| # | 未覆盖 | 为什么重要 | 建议 |
|---|--------|-----------|------|
| U1 | **存量项目接入与阈值棘轮** | 老项目直接上 70% 覆盖 + 50% 变异会当场全红，团队会关掉门禁（绕过而非修复） | 需专门设计"基线冻结 + 只约束增量 + 阈值逐季抬高"的 ratchet 机制 |
| U2 | **技能体系自身的规模化** | 38 个技能、`SKILLS-ROUTER.md` 已 222 行，L1 索引每次加载都是上下文税；技能继续增加会侵蚀可用窗口 | 需做路由表分片/按需检索，属架构级议题 |
| U3 | **技能与流程的腐化治理** | 谁 review skill 变更？技能间冲突（`personal-*` > `common-*`）如何仲裁？悬空依赖（D1）说明目前无人守 | 需定义 skill 的 CODEOWNERS 与变更门禁 |
| U4 | **提示注入的完整威胁模型** | §9 只给缓解（过滤隐藏字符、outside collaborator 语义、只响应有写权限者），未做系统性建模 | 无人化后攻击面从"人被骗"变成"流水线被骗"，需专项 |
| U5 | **阶段 0 需求源自动化（L5）** | "需求是否值得做"无法机械化；当前只能证明 7 阶段可自动，不能证明需求生成可自动 | 另立项 |
| U6 | **多仓 / monorepo / 非 Java-Vue 栈** | 本报告全部结论锚定在本项目技术栈；G5 十条规则也是这套栈的产物 | 迁移需重估规则可机械率 |
| U7 | **生产数据用于 AI 调试的合规边界** | 全自动需要真实反馈闭环（G8），但 `monitor` 已明令禁止记录代码内容与用户数据 | 需脱敏与指标口径设计 |
| U8 | **组织与角色变化** | 人从审批者变异常处理者与策略制定者，这是职责变更，不只是工具变更 | 需与流程改造同步沟通，否则 M1 之后就会出现"没人对 golden set 负责" |

---

## 16. 一句话结论

**我们的 skill 体系不是失败，而是被放错了层 —— 它是 L2 的执行规范，被当成了 L4 的门禁。**

要走到"完全 AI 自动"（L3/L4），需要且仅需要补四样东西：**产物 schema（机器可读状态）、hook + CI（跳不过）、变异测试（测试是否有效不由 AI 说）、自动回滚 + 策略引擎（把人从关键路径上挪走的唯一许可证）**。

**最短验证路径是 1.5 人天（M1）**：写 G5/G6 规范 hook + 修悬空依赖，然后把 §3.5 的 **7 个真实缺陷**做成回归用例 —— 判据只有一条：让 E1–E7 从"人工事后发现"变成"当场自动阻断"。这同时是"去人工"第 1 顺位，零风险起步，且是唯一可立即证伪本报告的部分。

**现状基线一句话**：本次全流程实验 7 个缺陷，**7 个由人工发现、0 个由流程自检发现**；其中 E6 在 18 个单测全绿的情况下依然存在（单测 mock 掉 Repository，结构上不可能发现建表失败）。当前实际自主度低于 L2 —— 人是事后纠错者，不是审批者。

⚠️ **一条必须承认的边界**：G3「方案价值判断」和阶段 0「需求是否值得做」无法机械化。因此"完全 AI 自动"的现实形态是 **L3/L4：AI 自主交付、机器判定、自动回滚，人做抽检与策略**，而不是"一个人都不碰"。若要求后者，缺口不在工具链，在需求源。

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
