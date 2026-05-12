# Trae AI 编程全流程自动化范式手册 (v1.1)

## 核心理念

**配置驱动，智能体协作**：通过一个核心配置文件 (AGENTS.md) + 一组斜杠命令，让 AI 自动遵循从需求到运维的全流程范式，实现"一句话驱动开发"。

---

## 1. 项目宪法：核心配置文件 (AGENTS.md)

在项目根目录创建此文件，作为 AI 的"芯片手册"，定义所有流程和规范。

```markdown
# 项目开发全流程宪法

## 核心原则
- **流程优先**：所有任务必须遵循预定义的生命周期顺序。
- **文档即代码**：需求、设计、报告必须生成 Markdown 文件并归档。
- **自动化验证**：代码提交前必须通过单元测试和 UI 自动化测试。
- **安全左移**：严禁生成不安全的代码片段（如 SQL 拼接、硬编码密钥）。
- **模块自治**：每个功能模块独立维护完整的文档体系。

## 全局流程规范
1. **需求阶段**：`/start` -> `docs/requirements/REQ_[模块]_v[版本].md` 和 `docs/features/[功能]/[功能]-需求文档-[日期].md`
2. **设计阶段**：`/design` -> `docs/design/DESIGN_[模块]_v[版本].md`
3. **开发阶段**：SOLO 模式并行开发后端（单元测试覆盖率≥85%）与前端（Playwright 测试）。
4. **验证阶段**：自动执行单元测试和 Playwright 测试，生成 `docs/reports/TEST_[日期].md`。
5. **文档归档**：更新功能模块文档（需求、交互、契约、测试、进度）。
6. **部署阶段**：`/deploy-config` 生成 Dockerfile 和 CI/CD 配置。
7. **报告阶段**：`/report` 更新进度，生成最终报告。

## 命名与路径规范

### 全局文档
| 文档类型 | 路径格式 | 示例 |
|----------|----------|------|
| 需求文档 | `docs/requirements/REQ_[功能]_v[版本].md` | `docs/requirements/REQ_login_v1.0.md` |
| 设计文档 | `docs/design/DESIGN_[功能]_v[版本].md` | `docs/design/DESIGN_login_v1.0.md` |
| 测试报告 | `docs/reports/TEST_[日期].md` | `docs/reports/TEST_2026-05-12.md` |
| 每日进度 | `docs/reports/DAILY_[日期].md` | `docs/reports/DAILY_2026-05-12.md` |
| 故障知识 | `docs/playbooks/[问题类型].md` | `docs/playbooks/database-timeout.md` |

### 功能模块文档（按模块组织）
| 文档类型 | 路径格式 | 必需性 |
|----------|----------|--------|
| 需求文档 | `docs/features/[功能]/[功能]-需求文档-[日期].md` | ✅ |
| 交互文档 | `docs/features/[功能]/[功能]-交互文档-[日期].md` | ✅ |
| 契约文档 | `docs/features/[功能]/[功能]-契约文档-[日期].md` | ✅ |
| 测试文档 | `docs/features/[功能]/[功能]-测试文档-[日期].md` | ✅ |
| 进度文档 | `docs/features/[功能]/[功能]-进度文档-[日期].md` | ✅ |
```

---

## 2. 指令集：核心斜杠命令

将这些命令放入 `.traecli/commands/` 目录，实现一键触发复杂流程。

| 命令 | 功能描述 | 自动执行的任务序列 |
|------|----------|-------------------|
| `/fullcycle` | 全流程总指挥 | `/start` → `/design` → (SOLO 并行开发) → (集成验证) → 文档归档 → `/deploy-config` → `/report` |
| `/start` | 需求澄清与启动 | 头脑风暴 → 生成需求文档 → 拆解子任务 → 更新进度 |
| `/design` | 技术方案与评审 | 生成数据库设计/API定义 → 自动评审（查性能/安全风险） → 输出评审意见 |
| `/impact` | 变更影响分析 | 分析文件引用关系 → 评估影响范围 → 列出需同步修改的测试/文档 |
| `/deploy-config` | 生成部署配置 | 生成 Dockerfile / docker-compose.yml / CI/CD 流水线配置 |
| `/report` | 生成进度报告 | 汇总需求、代码、测试状态 → 更新 DAILY_[日期].md 和各需求文档 |

---

## 3. 约束与扩展：自定义规则及工具

在 `.trae/rules/` 和 MCP 中配置，确保 AI 行为符合预期。

### 3.1 编码与质量规则 (.trae/rules/)

**backend-testing.md**：强制测试先行，单元测试覆盖率不低于 85%。

**security.md**：强制参数化查询、转义输出、禁止硬编码，类似内置 SAST 工具。

**observability.md**：强制添加日志、耗时记录、Prometheus 指标。

### 3.2 前端验证：Playwright MCP

配置 Playwright MCP Server 后，AI 可自动执行打开浏览器、点击、截图、表单填写等 UI 操作。

### 3.3 角色隔离：多智能体协同 (.trae/agents/)

| 角色 | 允许写入路径 | 职责 |
|------|-------------|------|
| backend-dev | `best-practice-dev-flow-backend/src/`、`best-practice-dev-flow-backend/tests/`、`docs/features/{功能}/` | 后端开发、测试编写、文档更新 |
| frontend-dev | `best-practice-dev-flow-frontend/src/`、`best-practice-dev-flow-frontend/tests/`、`docs/features/{功能}/` | 前端开发、Playwright 测试、文档更新 |
| doc-writer | `docs/`、`.traecli/commands/`、`.trae/rules/`、`.trae/agents/`、`.trae/hooks/` | 文档编写、配置更新 |

**作用**：避免不同 AI 角色互相干扰，维持项目结构清晰。

### 3.4 自动化钩子：Git 集成 (.trae/hooks/)

**pre-commit.md**：在提交前自动执行以下检查，不通过则阻止提交：
1. 代码格式化检查
2. Lint 检查
3. 单元测试执行
4. 覆盖率检查
5. 文档完整性检查
6. 安全检查

---

## 4. 故障与知识库

`docs/playbooks/*.md`：存放常见故障（如数据库超时）的症状和自动修复步骤。当 AI 遇到匹配报错时，会自动调用并执行诊断。

---

## 5. 最佳实践工作流

### 5.1 初始化
复制 `AGENTS.md` 和 `.trae/` 配置目录到新项目。

### 5.2 启动
在 Trae 对话框中输入 `/fullcycle "实现[你的需求描述]"`。

### 5.3 见证
观察 AI 作为项目经理，自动完成需求分析、方案设计、并行开发、自动化测试和文档更新。

### 5.4 介入
在 `/design` 阶段确认技术方案，或在 `/impact` 阶段评估风险。

### 5.5 归档
最终 AI 生成完整的发布说明和项目报告。

---

## 6. 质量门槛

### 代码质量
- **后端单元测试覆盖率**：≥ 85%
- **前端 Playwright 测试**：核心流程 100% 覆盖
- **代码规范**：通过 ESLint / Checkstyle 检查
- **安全扫描**：无 SQL 注入、无硬编码密钥

### 文档完整性
- 每个功能模块必须包含完整的文档体系（需求、交互、契约、测试、进度）
- API 契约文档必须与代码同步
- 测试文档必须与测试用例同步

### 安全要求
- 轨迹数据必须验证用户权限
- 敏感信息必须加密传输（HTTPS）
- 日志中严禁记录敏感信息

---

## 7. 版本历史

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0 | 2026-05-12 | 初始化手册 |
| v1.1 | 2026-05-12 | 添加模块自治原则、功能模块文档规范、角色权限更新、质量门槛 |