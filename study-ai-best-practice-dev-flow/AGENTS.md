# 项目开发全流程宪法

## 核心原则

- **流程优先**：所有任务必须遵循预定义的生命周期顺序。
- **文档即代码**：需求、设计、报告必须生成 Markdown 文件并归档。
- **自动化验证**：代码提交前必须通过单元测试和 UI 自动化测试。
- **安全左移**：严禁生成不安全的代码片段（如 SQL 拼接、硬编码密钥）。
- **模块自治**：每个功能模块独立维护完整的文档体系。

---

## 全局流程规范

1. **需求阶段**：`/start` -> `docs/requirements/REQ_[模块]_v[版本].md` 和 `docs/features/[功能]/[功能]-需求文档-[日期].md`
2. **设计阶段**：`/design` -> `docs/design/DESIGN_[模块]_v[版本].md`
3. **开发阶段**：SOLO 模式并行开发后端（单元测试覆盖率≥85%）与前端（Playwright 测试）。
4. **验证阶段**：自动执行单元测试和 Playwright 测试，生成 `docs/reports/TEST_[日期].md`。
5. **文档归档**：更新功能模块文档（需求、交互、契约、测试、进度）。
6. **部署阶段**：`/deploy-config` 生成 Dockerfile 和 CI/CD 配置。
7. **报告阶段**：`/report` 更新进度，生成最终报告。

---

## 项目结构

```
best-practice-dev-flow/
├── AGENTS.md                   # 项目宪法（核心配置文件）
├── .traeignore                 # Trae AI 忽略配置文件
├── .trae/                      # Trae AI 配置目录
│   ├── agents/                 # 多智能体角色定义
│   │   ├── backend-dev.md
│   │   ├── frontend-dev.md
│   │   └── doc-writer.md
│   ├── hooks/                  # Git 自动化钩子
│   │   └── pre-commit.md
│   └── rules/                  # 编码与质量规则
│       ├── backend-testing.md    # 后端测试规则
│       ├── frontend-testing.md   # 前端测试规则（Playwright）
│       ├── security.md           # 安全规则
│       ├── observability.md      # 可观测性规则
│       ├── prd-spec.md           # PRD 规范文档
│       ├── ux-spec.md            # 交互规范文档
│       ├── contract-spec.md      # 契约生成规范
│       ├── backend-code-spec.md  # 后端编码规范
│       └── frontend-code-spec.md # 前端编码规范
├── .traecli/                   # 斜杠命令目录
│   └── commands/               # 自定义命令
│       ├── fullcycle.md
│       ├── start.md
│       ├── design.md
│       ├── impact.md
│       ├── deploy-config.md
│       └── report.md
├── docs/                       # 文档目录
│   ├── requirements/           # 标准化需求文档
│   ├── design/                 # 设计文档模板
│   ├── reports/                # 测试报告和进度报告
│   ├── playbooks/              # 故障知识库
│   ├── api-contract.md         # API 契约文档
│   ├── project-overview.md     # 项目总览
│   └── features/               # 功能模块文档（按模块组织）
│       ├── login/              # 登录模块
│       ├── register/           # 注册模块
│       ├── user/               # 用户管理模块
│       └── track/              # 轨迹模块
├── best-practice-dev-flow-backend/    # 后端项目
│   └── src/
│       ├── main/
│       └── test/```
└── best-practice-dev-flow-frontend/   # 前端项目
    ├── src/
    ├── tests/                  # Playwright 测试
    └── e2e/
```

---

## 忽略配置：.traeignore

### 功能描述

`.traeignore` 文件定义 Trae AI 在分析和处理项目时应该忽略的文件和目录，类似于 `.gitignore` 的作用。

### 忽略的文件类型

| 类别 | 忽略内容 | 说明 |
|------|----------|------|
| 版本控制 | `.git/`, `.github/` | Git 版本控制目录 |
| 构建输出 | `target/`, `dist/`, `build/` | 编译和打包产物 |
| 依赖目录 | `node_modules/` | npm 依赖 |
| 日志文件 | `*.log`, `logs/` | 运行时日志 |
| 环境配置 | `.env*`, `application-local.*` | 本地环境配置（可能包含敏感信息） |
| 临时文件 | `*.tmp`, `.cache/` | 临时缓存文件 |
| 编辑器配置 | `.vscode/`, `.idea/`, `*.swp` | IDE 配置文件 |
| 系统文件 | `.DS_Store`, `Thumbs.db` | 操作系统生成的文件 |
| 测试输出 | `coverage/`, `playwright-report/` | 测试覆盖率报告和测试结果 |
| 数据库文件 | `*.db`, `*.sqlite` | 本地数据库文件 |

### 配置文件位置

```
项目根目录/.traeignore
```

### 格式说明

- 使用 glob 模式匹配文件和目录
- 以 `#` 开头的行为注释
- 目录以 `/` 结尾
- 支持 `*` 和 `**` 通配符

---

## 命名与路径规范

### 全局文档

| 文档类型 | 路径格式 | 示例 |
|----------|----------|------|
| 需求文档 | `docs/requirements/REQ_[功能]_v[版本].md` | `docs/requirements/REQ_user_v1.0.md` |
| 设计文档 | `docs/design/DESIGN_[功能]_v[版本].md` | `docs/design/DESIGN_user_v1.0.md` |
| 测试报告 | `docs/reports/TEST_[日期].md` | `docs/reports/TEST_2026-05-12.md` |
| 每日进度 | `docs/reports/DAILY_[日期].md` | `docs/reports/DAILY_2026-05-12.md` |
| 故障知识 | `docs/playbooks/[问题类型].md` | `docs/playbooks/database-timeout.md` |
| API 契约 | `docs/api-contract.md` | - |
| 项目总览 | `docs/project-overview.md` | - |

### 功能模块文档（按模块组织）

每个功能模块必须包含以下文档：

| 文档类型 | 路径格式 | 示例 | 必需性 |
|----------|----------|------|--------|
| 需求文档 | `docs/features/[功能]/[功能]-需求文档-[日期].md` | `docs/features/login/登录-需求文档-2026-05-12.md` | ✅ |
| 交互文档 | `docs/features/[功能]/[功能]-交互文档-[日期].md` | `docs/features/login/登录-交互文档-2026-05-12.md` | ✅ |
| 契约文档 | `docs/features/[功能]/[功能]-契约文档-[日期].md` | `docs/features/login/登录-契约文档-2026-05-12.md` | ✅ |
| 测试文档 | `docs/features/[功能]/[功能]-测试文档-[日期].md` | `docs/features/login/登录-测试文档-2026-05-12.md` | ✅ |
| 进度文档 | `docs/features/[功能]/[功能]-进度文档-[日期].md` | `docs/features/login/登录-进度文档-2026-05-12.md` | ✅ |

---

## 指令集：核心斜杠命令

| 命令 | 功能描述 | 自动执行的任务序列 |
|------|----------|-------------------|
| `/fullcycle` | 全流程总指挥 | `/start` -> `/design` -> (SOLO 并行开发) -> (集成验证) -> 文档归档 -> `/deploy-config` -> `/report` |
| `/start` | 需求澄清与启动 | 头脑风暴 -> 生成需求文档 -> 拆解子任务 -> 更新进度 |
| `/design` | 技术方案与评审 | 生成数据库设计/API定义 -> 自动评审（查性能/安全风险） -> 输出评审意见 |
| `/impact` | 变更影响分析 | 分析文件引用关系 -> 评估影响范围 -> 列出需同步修改的测试/文档 |
| `/deploy-config` | 生成部署配置 | 生成 Dockerfile / docker-compose.yml / CI/CD 流水线配置 |
| `/report` | 生成进度报告 | 汇总需求、代码、测试状态 -> 更新 DAILY_[日期].md 和各需求文档 |

---

## 角色隔离：多智能体协同

| 角色 | 允许写入路径 | 职责 |
|------|-------------|------|
| backend-dev | `best-practice-dev-flow-backend/src/`、`best-practice-dev-flow-backend/tests/`、`docs/features/{功能}/` | 后端开发、测试编写、文档更新 |
| frontend-dev | `best-practice-dev-flow-frontend/src/`、`best-practice-dev-flow-frontend/tests/`、`docs/features/{功能}/` | 前端开发、Playwright 测试、文档更新 |
| doc-writer | `docs/`、`.traecli/commands/`、`.trae/rules/`、`.trae/agents/`、`.trae/hooks/` | 文档编写、配置更新 |

---

## 质量门槛

### 代码质量
- **后端单元测试覆盖率**：≥ 85%
- **前端 Playwright 测试**：核心流程 100% 覆盖，所有新增/修改功能必须先编写测试脚本
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

## 前端 Playwright UI 测试规范

### 测试原则

1. **测试先行**：所有前端功能开发必须先编写 Playwright 测试脚本，再实现功能代码
2. **覆盖核心流程**：登录、用户管理、轨迹追踪等核心功能必须 100% 覆盖
3. **自动化验证**：代码提交前必须通过所有 Playwright 测试
4. **UI 模式验证**：支持在 IDE 内置浏览器中进行可视化验证，确保测试结果与实际用户体验一致

### 测试文件结构

```
best-practice-dev-flow-ui/
├── tests/                      # Playwright 测试目录
│   ├── login.spec.js           # 登录功能测试
│   ├── user-management.spec.js # 用户管理功能测试
│   ├── track.spec.js           # 轨迹追踪功能测试
│   └── [功能].spec.js          # 其他功能测试
├── playwright.config.js        # Playwright 配置文件
└── package.json                # 包含测试脚本
```

### 测试脚本命名规范

| 测试类型 | 命名规则 | 示例 |
|----------|----------|------|
| 登录模块 | `login.spec.js` | 测试登录、退出、登录态持久化 |
| 用户管理 | `user-management.spec.js` | 测试用户增删改查 |
| 轨迹模块 | `track.spec.js` | 测试轨迹展示、添加 |

### 测试用例编写规范

每个测试文件应包含以下结构：

```javascript
import { test, expect } from '@playwright/test';

test.describe('功能模块名称', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');
  });

  test('测试场景描述', async ({ page }) => {
    // 步骤1：操作
    // 步骤2：断言验证
    // 步骤3：清理（如需要）
  });
});
```

### 测试执行命令

| 命令 | 描述 |
|------|------|
| `npm test` | 运行所有测试并生成 HTML 报告 |
| `npx playwright test --project=chromium` | 仅运行 Chromium 测试 |
| `npx playwright test tests/user-management.spec.js` | 运行指定测试文件 |
| `npx playwright show-report` | 查看测试报告 |
| `npx playwright test --ui` | 启动 UI 模式进行交互式测试验证 |
| `npx playwright test tests/login.spec.js --ui` | 指定测试文件启动 UI 模式 |

### 测试覆盖要求

| 功能模块 | 必须覆盖的场景 |
|----------|----------------|
| 登录 | 登录成功、登录失败、登录态持久化（刷新页面）、退出登录 |
| 用户管理 | 用户列表展示、添加用户、编辑用户、删除用户 |
| 轨迹追踪 | 轨迹展示、轨迹添加、轨迹删除 |

### 新增需求测试流程

1. **需求分析**：理解需求，确定需要测试的场景
2. **编写测试脚本**：在 `tests/` 目录创建对应测试文件
3. **运行测试**：执行 `npm test` 验证测试脚本
4. **实现功能**：根据测试脚本实现前端功能
5. **验证通过**：确保所有测试通过后提交代码

---

---

## 技术栈

### 后端
- Spring Boot 3.1.10
- Java 21
- H2 / MySQL
- JUnit 5
- Mockito
- JaCoCo（代码覆盖率）

### 前端
- Vue 3.4+
- Ant Design Vue 4.x
- Vite 5.x
- Playwright（UI 自动化测试）
- 百度地图 JavaScript API 3.0

---

## 版本历史

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0 | 2026-05-12 | 初始化项目宪法 |
| v1.1 | 2026-05-12 | 添加功能模块文档规范、更新角色权限、增加质量门槛 |
| v1.2 | 2026-05-12 | 添加前端 Playwright UI 测试规范，强制测试先行原则 |
| v1.3 | 2026-05-12 | 完善规则文档体系：PRD规范、交互规范、契约规范、前后端编码规范、前端测试规则 |
| v1.4 | 2026-05-12 | 支持 Playwright UI 模式测试，支持 IDE 内置浏览器可视化验证 |