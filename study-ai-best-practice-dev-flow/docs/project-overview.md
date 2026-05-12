# Best Practice Dev Flow - 项目总览

## 1. 项目概述

### 1.1 项目简介

Best Practice Dev Flow 是一套AI辅助开发的最佳实践流程范式，包含前端Vue项目和后端Spring Boot项目模板，实现标准化的开发、测试和验证流程。

**核心理念**：配置驱动，智能体协作。通过一个核心配置文件 (AGENTS.md) + 一组斜杠命令，让 AI 自动遵循从需求到运维的全流程范式。

### 1.2 技术栈

| 分类 | 技术 | 版本 |
|------|------|------|
| 前端框架 | Vue | 3.4+ |
| UI组件 | Ant Design Vue | 4.x |
| 构建工具 | Vite | 5.x |
| UI测试 | Playwright | 1.40+ |
| 后端框架 | Spring Boot | 3.1.10 |
| 数据库 | H2 / MySQL | - |
| 单元测试 | JUnit 5 | 5.9+ |
| 代码覆盖率 | JaCoCo | - |
| 地图服务 | 百度地图 API | 3.0 |

### 1.3 功能模块总览

| 功能模块 | 状态 | 描述 |
|----------|------|------|
| 用户登录 | ✅ 已实现 | 用户身份验证功能 |
| 用户注册 | ✅ 已实现 | 用户账户创建功能 |
| 用户管理 | ✅ 已实现 | 用户CRUD管理功能 |
| 用户轨迹 | ✅ 已实现 | 基于百度地图的轨迹记录和展示 |
| 任务管理 | ✅ 已实现 | 任务CRUD管理功能 |
| 项目管理 | ✅ 已实现 | 项目CRUD管理功能 |

---

## 2. 项目宪法

项目根目录的 [AGENTS.md](/AGENTS.md) 是项目的"宪法"文件，定义了：
- 核心原则（流程优先、文档即代码、自动化验证、安全左移、模块自治）
- 全局流程规范（需求→设计→开发→验证→文档归档→部署→报告）
- 命名与路径规范
- 指令集（斜杠命令）
- 角色隔离规则
- 质量门槛

---

## 3. 研发流程规范

### 3.1 流程总览

```
需求分析 → 需求文档 → 交互设计 → 契约文档 → 后端实现 → 后端测试 → 前端实现 → 前端测试 → 文档归档 → 验证交付
```

### 3.2 流程阶段说明

| 阶段 | 名称 | 产出物 | 责任人 |
|------|------|--------|--------|
| 1 | 需求分析 | 需求细化 | 产品/开发 |
| 2 | 需求文档 | `docs/features/[功能]/[功能]-需求文档-[日期].md` | 开发 |
| 3 | 交互设计 | `docs/features/[功能]/[功能]-交互文档-[日期].md` | 开发/设计 |
| 4 | 契约文档 | `docs/features/[功能]/[功能]-契约文档-[日期].md` | 后端开发 |
| 5 | 后端实现 | 后端代码 | 后端开发 |
| 6 | 后端测试 | 单元测试（覆盖率≥85%） | 后端开发 |
| 7 | 前端实现 | 前端代码 | 前端开发 |
| 8 | 前端测试 | Playwright 测试 | 前端开发 |
| 9 | 文档归档 | 更新所有模块文档 | 开发 |
| 10 | 验证交付 | 测试报告、进度报告 | 测试/开发 |

---

## 4. 目录结构

### 4.1 项目根目录结构

```
best-practice-dev-flow/
├── AGENTS.md                    # 项目宪法（核心配置文件）
├── .trae/                       # Trae AI 配置目录
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
│   ├── project-overview.md     # 项目总览
│   ├── init.md                 # 初始化手册
│   ├── api-contract.md         # API契约汇总
│   ├── requirements/           # 标准化需求文档
│   ├── design/                 # 设计文档模板
│   ├── reports/                # 测试报告和进度报告
│   ├── playbooks/              # 故障知识库
│   └── features/               # 功能模块文档（按模块组织）
├── best-practice-dev-flow-backend/    # 后端项目
└── best-practice-dev-flow-frontend/   # 前端项目
```

### 4.2 功能模块文档结构

每个功能模块必须包含完整的文档体系：

```
docs/features/[功能]/
├── [功能]-需求文档-[日期].md    # ✅ 必需
├── [功能]-交互文档-[日期].md    # ✅ 必需
├── [功能]-契约文档-[日期].md    # ✅ 必需
├── [功能]-测试文档-[日期].md    # ✅ 必需
└── [功能]-进度文档-[日期].md    # ✅ 必需
```

### 4.3 文档目录完整结构

```
docs/
├── project-overview.md          # 项目总览文档
├── init.md                      # 初始化手册
├── api-contract.md              # API契约汇总
├── requirements/                # 标准化需求文档（全局）
│   ├── REQ_login_v1.0.md
│   ├── REQ_register_v1.0.md
│   ├── REQ_user_v1.0.md
│   └── REQ_track_v1.0.md
├── design/                      # 设计文档
│   ├── DESIGN_template_v1.0.md
│   ├── DESIGN_login_v1.0.md
│   └── DESIGN_track_v1.0.md
├── reports/                     # 测试报告和进度报告
│   ├── TEST_template_2026-05-12.md
│   └── DAILY_template_2026-05-12.md
├── playbooks/                   # 故障知识库
│   ├── database-connection-timeout.md
│   ├── api-404-not-found.md
│   └── frontend-page-load-failed.md
└── features/                    # 功能模块文档（按模块组织）
    ├── login/                   # 登录模块
    │   ├── 登录-需求文档-2026-05-12.md
    │   ├── 登录-交互文档-2026-05-12.md
    │   ├── 登录-契约文档-2026-05-12.md
    │   ├── 登录-测试文档-2026-05-12.md
    │   └── 登录-进度文档-2026-05-12.md
    ├── register/                # 注册模块
    ├── user/                    # 用户管理模块
    └── track/                   # 轨迹模块
```

---

## 5. 文档命名规范

### 5.1 功能模块文档命名

```
{功能模块名}-{文档类型}-{日期}.md
```

### 5.2 文档类型说明

| 文档类型 | 说明 | 示例 |
|----------|------|------|
| 需求文档 | 功能需求描述 | 登录-需求文档-2026-05-12.md |
| 交互文档 | 交互流程和UI设计 | 登录-交互文档-2026-05-12.md |
| 契约文档 | API接口规范 | 登录-契约文档-2026-05-12.md |
| 测试文档 | 测试用例清单 | 登录-测试文档-2026-05-12.md |
| 进度文档 | 开发进度跟踪 | 登录-进度文档-2026-05-12.md |

### 5.3 全局文档命名规范

| 类型 | 路径格式 | 示例 |
|------|----------|------|
| 需求文档 | `docs/requirements/REQ_[功能]_v[版本].md` | `docs/requirements/REQ_login_v1.0.md` |
| 设计文档 | `docs/design/DESIGN_[功能]_v[版本].md` | `docs/design/DESIGN_login_v1.0.md` |
| 测试报告 | `docs/reports/TEST_[日期].md` | `docs/reports/TEST_2026-05-12.md` |
| 每日进度 | `docs/reports/DAILY_[日期].md` | `docs/reports/DAILY_2026-05-12.md` |
| 故障知识 | `docs/playbooks/[问题类型].md` | `docs/playbooks/database-timeout.md` |

---

## 6. 斜杠命令

### 6.1 命令列表

| 命令 | 功能描述 | 自动执行的任务序列 |
|------|----------|-------------------|
| `/fullcycle` | 全流程总指挥 | `/start` → `/design` → 并行开发 → 集成验证 → 文档归档 → `/deploy-config` → `/report` |
| `/start` | 需求澄清与启动 | 头脑风暴 → 生成需求文档 → 拆解子任务 → 更新进度 |
| `/design` | 技术方案与评审 | 生成数据库设计/API定义 → 自动评审 → 输出评审意见 |
| `/impact` | 变更影响分析 | 分析文件引用关系 → 评估影响范围 → 列出需同步修改的测试/文档 |
| `/deploy-config` | 生成部署配置 | 生成 Dockerfile / docker-compose.yml / CI/CD 配置 |
| `/report` | 生成进度报告 | 汇总需求、代码、测试状态 → 更新 DAILY 和各需求文档 |

### 6.2 使用方式

```bash
/fullcycle [需求描述]   # 启动完整开发流程
/start [需求描述]       # 开始需求分析
/design [功能模块]      # 生成技术方案
/impact [变更描述]      # 分析变更影响
/deploy-config [环境]   # 生成部署配置
/report                # 生成进度报告
```

---

## 7. 角色隔离

### 7.1 角色定义

| 角色 | 职责 | 允许写入路径 |
|------|------|--------------|
| backend-dev | 后端开发 | `best-practice-dev-flow-backend/src/`、`best-practice-dev-flow-backend/tests/`、`docs/features/{功能}/` |
| frontend-dev | 前端开发 | `best-practice-dev-flow-frontend/src/`、`best-practice-dev-flow-frontend/tests/`、`docs/features/{功能}/` |
| doc-writer | 文档编写 | `docs/`、`.traecli/commands/`、`.trae/rules/`、`.trae/agents/`、`.trae/hooks/` |

### 7.2 质量门槛

#### 代码质量
- **后端单元测试覆盖率**：≥ 85%
- **前端 Playwright 测试**：核心流程 100% 覆盖
- **代码规范**：通过 ESLint / Checkstyle 检查
- **安全扫描**：无 SQL 注入、无硬编码密钥

#### 文档完整性
- 每个功能模块必须包含完整的文档体系（需求、交互、契约、测试、进度）
- API 契约文档必须与代码同步
- 测试文档必须与测试用例同步

#### 安全要求
- 轨迹数据必须验证用户权限
- 敏感信息必须加密传输（HTTPS）
- 日志中严禁记录敏感信息

---

## 8. 编码规范

### 8.1 后端编码规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | PascalCase | UserController |
| 方法名 | camelCase | getUserById |
| 变量名 | camelCase | userName |
| 常量名 | UPPER_SNAKE_CASE | MAX_SIZE |
| 包名 | lowercase | com.bage.study.ai |

### 8.2 前端编码规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 组件名 | PascalCase | LoginModal.vue |
| 方法名 | camelCase | handleLogin |
| 变量名 | camelCase | userInfo |
| CSS类名 | kebab-case | .login-form |

---

## 9. 测试规范

### 9.1 测试用例命名规范

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 单元测试 | UT-{模块}-{编号} | UT-LOGIN-001 |
| 集成测试 | IT-{模块}-{编号} | IT-USER-001 |
| UI 测试 | UI-{模块}-{编号} | UI-TRACK-001 |

### 9.2 测试执行命令

```bash
# 后端单元测试
cd best-practice-dev-flow-backend && mvn test

# 生成覆盖率报告
mvn test jacoco:report

# 前端 Playwright 测试
cd best-practice-dev-flow-frontend && npm run test:e2e
```

---

## 10. 安全规范

### 10.1 禁止行为
- SQL 拼接（必须使用参数化查询或 ORM）
- 硬编码密钥（必须使用环境变量）
- 直接输出未转义的用户输入（防止 XSS）
- 日志中记录敏感信息

### 10.2 强制要求
- 密码必须使用 BCrypt 加密
- 所有 API 调用必须使用 HTTPS
- 用户轨迹数据必须验证权限
- 敏感字段必须过滤（不返回密码等信息）

---

## 11. 版本历史

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0 | 2026-05-12 | 初始化项目总览 |
| v1.1 | 2026-05-12 | 更新功能模块状态、完善文档结构、添加角色权限、补充安全规范 |
| v1.2 | 2026-05-12 | 同步更新规则文档体系：添加 PRD规范、交互规范、契约规范、前后端编码规范、前端测试规则 |