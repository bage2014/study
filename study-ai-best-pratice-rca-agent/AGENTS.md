# AI 编码助手规范（AGENTS.md）

## 1. 项目上下文

### 1.1 项目概述

- **项目名称**：study-ai-best-pratice-rca-agent
- **主要功能**：RCA（根因分析）Agent 多模块工程，含后端分析服务、Web 控制台、两个演练用 demo 服务
- **技术栈**：
  - 后端：Java 17、Spring Boot 3.3.0、Spring AI 1.0.0-M6、Spring Data JPA、H2
  - 前端：Vue 3、Vite 5、Element Plus 2.7、Vue Router 4、axios
  - 测试：JUnit 5（后端）、Playwright（前端 E2E）
  - 构建：Maven 多模块（父 POM 聚合）

### 1.2 模块划分

| 模块 | 端口 | 职责 |
|------|------|------|
| `rca-agent-backend` | 8082 | RCA 分析核心：录制、回放、拓扑、置信度、反事实验证 |
| `rca-agent-web` | 5173（dev） | Web 控制台：5 个页面，Vue 3 SPA |
| `demo-order-service` | 8083 | RCA 演练 demo：订单服务，支持 6 大故障场景注入 |
| `demo-supplier-service` | 8084 | RCA 演练 demo：供应商服务，支持 6 大故障场景注入 |

### 1.3 代码风格

- **后端**：遵循 `personal-backend-coding-standard`，分层 Controller → Service → Repository
- **前端**：遵循 `personal-frontend-coding-standard`，组件化 + Composition API
- **注释**：中文 Javadoc / JSDoc，关键逻辑必须有注释
- **沟通**：简洁直接，禁止废话（遵循 `personal-brief-style`）

## 2. 编码规则

### 2.1 必须遵循（硬约束）

1. **demo 模块命名**：新增演练模块必须以 `demo-` 前缀命名
2. **数据库**：所有模块默认使用 **H2**（开发/演示环境），禁止未经评审切换
3. **持久化**：录制类/业务数据必须落库（JPA），禁止仅用 `ConcurrentHashMap` 等内存方案
4. **文档同步**：任何代码/配置变更，完成后必须在 `docs/changelog/` 新增变更记录
5. **UI 变更**：前端 UI 改动必须同步补充 Playwright 测试用例

### 2.2 禁止操作

- 禁止在 `pom.xml` 中硬编码版本号（统一放 `<properties>`）
- 禁止模块循环依赖
- 禁止在 Controller 中写业务逻辑
- 禁止提交 `application-local.yml`、`.env`、`*.log` 等本地/敏感文件
- 禁止跳过测试直接合并代码
- 禁止创建 `.md` 文档文件，除非用户明确要求

### 2.3 最佳实践

- 新增 demo 模块时同步更新：根 `pom.xml` modules、模块 `application.properties`、`docs/README.md` 模块对照表
- 文档命名统一：`{模块}-{内容描述}-{日期}.md`
- 文档目录分类：`docs/{api,requirement,spec,changelog,report}/`
- 前端 API 调用统一走 `axios` 实例，禁止散落原生 `fetch`
- 后端异常统一 `BusinessException` + `@RestControllerAdvice`

## 3. 技能使用指引

### 3.1 场景 → 技能映射

| 场景 | 推荐技能 |
|------|---------|
| 需求不清晰 | `common-grill-me`、`common-requirement-clarification` |
| 方案设计 / 多方案比对 | `personal-solution-research`、`common-design-brainstorming`、`common-spec-driven` |
| 新建后端模块 | `common-spring-boot-init`、`common-db-design` |
| 新建前端页面 | `common-vue-init`、`common-ui-design-spec`、`common-ui-mockup` |
| 后端编码 | `personal-backend-coding-standard`、`common-coding`、`personal-java-common` |
| 前端编码 | `personal-frontend-coding-standard` |
| 后端单测 | `common-backend-unit-test` |
| 前端 E2E | `common-frontend-playwright-test` |
| API 文档 | `common-api-doc`、`common-contract-generation` |
| 数据迁移 | `common-data-migration` |
| 调试复杂 Bug | `common-systematic-debugging`、`TRAE-debugger` |
| 代码审查 | `common-code-review`、`TRAE-code-review` |
| 安全审查 | `TRAE-security-review` |
| 文档归档 | `personal-doc-manager`、`common-technical-writer` |
| 项目启动/重启 | `personal-project-runner` |
| Docker 化 | `common-dockerize` |

### 3.2 技能组合（典型工作流）

- **新功能**：`common-requirement-clarification` → `personal-solution-research` → `common-spec-driven` → `common-coding` → `common-backend-unit-test` → `personal-doc-manager`
- **UI 新页面**：`common-ui-design-spec` → `common-ui-mockup` → `personal-frontend-coding-standard` → `common-frontend-playwright-test`
- **Bug 排查**：`common-systematic-debugging` → `TRAE-debugger` → `common-code-review`

## 4. 工具配置

### 4.1 构建与运行

```bash
# 全量构建（根目录）
mvn clean install -DskipTests

# 启动后端
cd rca-agent-backend && mvn spring-boot:run

# 启动 demo 服务（演练用）
cd demo-order-service && mvn spring-boot:run
cd demo-supplier-service && mvn spring-boot:run

# 启动前端（开发模式）
cd rca-agent-web && npm run dev

# 前端 E2E 测试
cd rca-agent-web && npm run test:e2e
```

### 4.2 测试要求

- **后端**：JUnit 5 + Spring Boot Test；Service 层覆盖率 ≥ 70%
- **前端**：Playwright；UI 变更必须新增/更新用例
- **冒烟**：demo 服务接口变更后必须跑一遍 6 大故障场景注入

### 4.3 端口约定

| 端口 | 用途 |
|------|------|
| 8082 | rca-agent-backend |
| 8083 | demo-order-service |
| 8084 | demo-supplier-service |
| 5173 | rca-agent-web（dev） |

**禁止**未经评审修改端口。

## 5. 安全规范

### 5.1 敏感信息

- API Key / Token 通过环境变量注入，配置文件用 `${ENV_VAR}` 引用
- 禁止在代码、日志、注释、提交信息中明文写入密钥
- `.env`、`*.key`、`application-local.yml` 必须列入 `.gitignore`

### 5.2 数据库

- H2 Console 仅本机访问，禁止对外暴露
- 生产环境切换 MySQL 需走方案评审（参考 `personal-solution-research`）

### 5.3 代码审查

- PR 必须通过构建 + 单测 + UI 测试
- 至少 1 名 reviewer
- 涉及持久化、外部调用、权限的 PR 必须走 `TRAE-security-review`

## 6. 文档管理约定

- 文档根目录：`docs/`
- 索引入口：[docs/README.md](docs/README.md)
- 新增文档必须同步更新 `docs/README.md` 第 3 节索引
- 命名规范、目录分类、维护约定详见 `docs/README.md` 第 2、5 节

## 7. 常见陷阱（Lessons Learned）

1. **模块重命名**：必须同步更新根 `pom.xml`、模块配置、`application.properties` 三处，否则构建失败
2. **录制数据**：禁止用 `ConcurrentHashMap` 内存存储——重启丢、无容量上限、多实例不共享，必须落 H2
3. **文档位置**：`.trae/specs/` 下的存量文档应迁移至 `docs/spec/`，统一管理
4. **UI 变更**：漏写 Playwright 用例是常见回归源头，一律强制补测
