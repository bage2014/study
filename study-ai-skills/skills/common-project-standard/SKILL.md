---
name: "common-project-standard"
description: "项目规范技能，提供项目结构、文档管理、代码忽略等方面的标准和最佳实践"
trigger: "需要创建新项目或规范现有项目时"
disable-when: "项目已有完善的规范体系"
category: "common"
tags: ["project", "standard", "best-practices"]
---

# 项目规范技能

## 功能描述

提供项目结构、文档管理、代码忽略等方面的标准和最佳实践，帮助团队建立统一的项目规范。同时提供技能规范验证工具和开源技能包装机制。

## 何时使用

在以下情况调用此技能：
- 创建新项目时
- 维护现有项目时
- 需要制定项目规范时
- 需要验证技能规范性时
- 需要集成开源技能时

## 核心功能

- **技能管理**：规范技能的创建和管理流程
- **文档管理**：规范文档的编写和维护标准
- **Git忽略**：规范 .gitignore 文件的配置
- **AI忽略**：规范 AI 相关文件的忽略规则
- **项目结构**：规范项目目录结构
- **技能安装**：提供技能安装和配置指导
- **规范验证**：验证技能是否符合标准格式
- **开源集成**：支持开源技能的包装和切换

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| projectType | String | 否 | 项目类型（backend/frontend/fullstack） |
| framework | String | 否 | 使用的框架 |
| validateSkills | Boolean | 否 | 是否验证技能规范 |
| openSourceSkills | List | 否 | 要集成的开源技能列表 |

## 输出格式

```json
{
  "status": "SUCCESS",
  "projectType": "backend",
  "framework": "Spring Boot",
  "guidelines": {
    "skill": {...},
    "documentation": {...},
    "gitignore": {...},
    "aiIgnore": {...},
    "structure": {...},
    "installation": {...},
    "validation": {...},
    "openSourceIntegration": {...}
  }
}
```

---

## 1. 技能管理规范

### 1.1 技能目录结构

```
skills/
├── skill-name/           # 技能名称（小写连字符）
│   └── SKILL.md          # 技能定义文件（必填）
```

### 1.2 技能命名规范

| 类型 | 前缀 | 示例 |
|------|------|------|
| 异常分析 | `exception-` | `exception-analysis` |
| AI 聊天 | `spring-ai-` | `spring-ai-chat` |
| 研发流程 | `common-` | `common-coding`, `common-test` |
| 个人技能 | `personal-` | `personal-backend-coding-standard` |
| 项目规范 | `common-project-` | `common-project-standard` |
| 开源包装 | `os-` | `os-ui-library`, `os-chat-model` |

### 1.3 SKILL.md 文档结构

```markdown
---
name: "skill-name"
description: "技能描述，简要说明技能的核心价值和用途"
trigger: "触发条件，简洁描述技能何时被触发"
disable-when: "禁用条件，描述不适合使用此技能的场景"
category: "技能分类，如：backend/frontend/database/devops/ai"
tags: ["标签1", "标签2"]
requires: ["依赖技能1", "依赖技能2"]
---

# 技能名称

## 功能描述
## 触发条件
## 何时使用
## 何时不使用
## 核心功能
## 输入参数
## 输出格式
## 使用流程
## 调用示例
## 失败案例
## 最佳实践
## 配置要求
## 扩展指南
```

### 1.3.0 Frontmatter 元数据规范

Frontmatter 是技能的元数据定义区，用于描述技能的基本信息和属性：

| 字段 | 类型 | 必要性 | 说明 |
|------|------|--------|------|
| **name** | String | 必填 | 技能名称，必须与目录名一致 |
| **description** | String | 必填 | 技能的核心价值和用途描述（1-2句话） |
| **trigger** | String | 必填 | 触发条件，简洁描述技能何时被触发 |
| **disable-when** | String | 必填 | 禁用条件，描述不适合使用此技能的场景 |
| **category** | String | 必填 | 技能分类（backend/frontend/database/devops/ai等） |
| **tags** | Array | 推荐 | 技能标签，用于分类和搜索 |
| **requires** | Array | 可选 | 依赖的其他技能名称 |

**Frontmatter 示例**：
```markdown
---
name: "common-spring-boot-init"
description: "快速创建 Spring Boot 项目结构，支持多种数据库和依赖配置"
trigger: "用户请求创建新的 Spring Boot 项目时"
disable-when: "项目已存在或非 Java 技术栈项目"
category: "backend"
tags: ["spring", "java", "backend", "project-init"]
requires: ["common-db-design"]
---
```

### 1.3.1 文档章节说明

| 章节 | 必要性 | 说明 |
|------|--------|------|
| **功能描述** | 必填 | 技能的核心能力和价值定位 |
| **触发条件** | 必填 | 技能被触发执行的条件和前置要求 |
| **何时使用** | 必填 | 适合使用此技能的场景 |
| **何时不使用** | 推荐 | 不适合使用此技能的场景（禁用样例） |
| **核心功能** | 必填 | 技能提供的具体功能列表 |
| **输入参数** | 必填 | 输入参数定义（表格形式） |
| **输出格式** | 必填 | 输出数据格式（JSON示例） |
| **使用流程** | 推荐 | 使用该技能的步骤说明 |
| **调用示例** | 推荐 | 实际使用的代码或命令示例 |
| **失败案例** | 推荐 | 常见错误场景及解决方案 |
| **最佳实践** | 推荐 | 使用该技能的最佳实践建议 |
| **配置要求** | 可选 | 环境变量、配置文件等要求 |
| **扩展指南** | 可选 | 如何扩展或定制该技能 |

### 1.3.2 触发条件规范

触发条件应明确以下内容：

1. **前置条件**：使用技能前必须满足的条件
2. **环境要求**：所需的软件、依赖、权限等
3. **数据准备**：需要预先准备的数据或配置
4. **触发方式**：技能被调用的方式（API、命令行、事件等）

**示例格式**：
```markdown
## 触发条件

- **前置条件**：项目已初始化，具备基础目录结构
- **环境要求**：Java 21+, Maven 3.8+, Spring Boot 3.2+
- **数据准备**：配置文件 `application.yml` 已存在
- **触发方式**：API 调用 / 命令行执行 / 事件触发
```

### 1.3.3 何时不使用（禁用样例）

明确列出不适合使用此技能的场景：

**示例格式**：
```markdown
## 何时不使用

- 当项目已使用其他 ORM 框架时，不建议使用此技能
- 简单的 CRUD 操作无需使用此技能
- 需要复杂事务管理的场景请使用其他方案
```

### 1.3.4 失败案例规范

记录常见的失败场景及解决方案：

**示例格式**：
```markdown
## 失败案例

| 错误场景 | 错误信息 | 解决方案 |
|----------|----------|----------|
| 数据库连接失败 | `Connection refused` | 检查数据库服务是否启动 |
| 权限不足 | `Permission denied` | 确认用户有足够的文件权限 |
| 依赖缺失 | `ClassNotFoundException` | 检查 pom.xml 依赖配置 |
```

### 1.4 技能注册流程

1. 在 `skills/` 目录下创建技能目录
2. 创建 `SKILL.md` 文件
3. 在 `AGENTS.md` 中添加技能说明
4. 在 `SKILLS.md` 中注册技能

---

## 2. 文档管理规范

### 2.1 文档目录结构

```
docs/
├── api/                  # API 文档
│   └── openapi.yaml
├── architecture/         # 架构文档
│   └── architecture.md
├── guides/               # 使用指南
│   └── getting-started.md
├── README.md             # 项目说明（根目录）
├── AGENTS.md             # 代理与技能架构（根目录）
└── SKILLS.md             # 技能清单（根目录）
```

### 2.2 文档命名规范

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 文档文件 | 小写连字符 | `getting-started.md` |
| 目录名称 | 小写连字符 | `api/`, `guides/` |

### 2.3 文档模板

#### README.md 模板

```markdown
# 项目名称

## 项目简介
## 技术栈
## 快速开始
## 项目结构
## API 接口
## 开发指南
## 部署说明
## 贡献指南
## 许可证
```

#### SKILLS.md 模板

```markdown
# 技能清单

## 个人定制技能

| 技能名称 | 描述 |
|----------|------|
| personal-backend-coding-standard | 后端编码规范技能 |
| personal-frontend-coding-standard | 前端编码规范技能 |
| personal-java-common | Java 通用工具技能 |
| personal-project-runner | 项目启动管理技能 |
| personal-doc-manager | 个人文档管理技能 |
| personal-solution-research | 方案调研技能 |
| personal-brief-style | 简洁风格技能 |

## 通用技能

| 技能名称 | 描述 |
|----------|------|
| common-project-standard | 项目规范技能 |
| common-contract-generation | 契约生成技能 |
| common-frontend-playwright-test | 前端Playwright测试技能 |
| common-backend-unit-test | 后端单元测试技能 |
| common-coding | 编码技能 |
| common-requirement-clarification | 需求澄清技能 |
| common-api-doc | API文档生成技能 |
| common-data-migration | 数据迁移技能 |
| common-db-design | 数据库设计技能 |
| common-dockerize | Docker容器化技能 |
| common-grill-me | Grill Me 深度质询技能 |
| common-spec-driven | Spec驱动规范技能 |
| common-spring-boot-init | Spring Boot项目初始化技能 |
| common-ui-design-spec | UI设计规范技能 |
| common-ui-mockup | UI原型图生成技能 |
| common-vue-init | Vue项目初始化技能 |

## 开源技能包装

| 技能名称 | 描述 | 底层实现 |
|----------|------|----------|
| os-ui-library | UI组件库技能 | 可配置（自研/Component-A/Component-B） |
| os-chat-model | 聊天模型技能 | 可配置（OpenAI/DeepSeek/Claude） |
```

#### AGENTS.md 模板

```markdown
# 代理与技能架构

## 概述

本项目使用 Trae AI 技能系统，通过技能扩展项目能力。

## 技能目录

所有技能位于 `skills/` 目录下。

## 技能分类

### 个人定制技能
- personal-backend-coding-standard - 后端编码规范
- personal-frontend-coding-standard - 前端编码规范
- personal-java-common - Java 通用工具
- personal-project-runner - 项目启动管理
- personal-doc-manager - 个人文档管理
- personal-solution-research - 方案调研
- personal-brief-style - 简洁风格

### 通用技能
- common-project-standard - 项目规范
- common-contract-generation - 契约生成
- common-frontend-playwright-test - 前端测试
- common-backend-unit-test - 后端测试
- common-coding - 编码规范
- common-requirement-clarification - 需求澄清
- common-api-doc - API文档生成
- common-data-migration - 数据迁移
- common-db-design - 数据库设计
- common-dockerize - Docker容器化
- common-grill-me - Grill Me 深度质询
- common-spec-driven - Spec驱动规范
- common-spring-boot-init - Spring Boot初始化
- common-ui-design-spec - UI设计规范
- common-ui-mockup - UI原型图生成
- common-vue-init - Vue项目初始化

## 技能使用方式

### 1. 安装技能

使用项目根目录的 `install-skills.sh` 脚本安装技能：

```bash
# 安装所有技能
./install-skills.sh /path/to/project

# 安装指定技能
./install-skills.sh -s common-coding -s common-project-standard .
```

### 2. 注册技能

在 SKILLS.md 中注册技能，系统会自动加载。

### 3. 使用技能

在需要时调用相应的技能接口。

### 4. 验证技能规范

```bash
# 验证所有技能
./validate-skills.sh

# 验证指定技能
./validate-skills.sh -s common-coding

# 修复技能格式
./validate-skills.sh -f
```
```

### 2.4 文档编写规范

1. **语言**：统一使用中文
2. **格式**：使用 Markdown 格式
3. **标题**：使用 `#` 表示标题层级
4. **代码块**：使用三个反引号 + 语言标识
5. **链接**：使用绝对路径或相对路径

---

## 3. Git 忽略规范

### 3.1 .gitignore 文件位置

- 根目录：`.gitignore` - 全局忽略规则
- 模块目录：`module/.gitignore` - 模块特定规则

### 3.2 通用忽略规则

```gitignore
# 构建产物
target/
build/
dist/
*.class
*.jar
*.war

# IDE 文件
.idea/
.vscode/
.project
.classpath
.settings/

# 日志文件
*.log
logs/

# 配置文件
application-local.yml
application-local.properties
.env

# OS 文件
.DS_Store
Thumbs.db

# Maven
.mvn/
mvnw
mvnw.cmd

# Gradle
.gradle/
gradlew
gradlew.bat
```

### 3.3 后端项目忽略规则

```gitignore
# 数据库文件
*.db
*.sqlite

# JVM 相关
hs_err_pid*
replay_pid*

# Spring Boot
spring-shell.log
```

### 3.4 前端项目忽略规则

```gitignore
# Node.js
node_modules/
package-lock.json
yarn.lock

# 构建工具
.next/
.nuxt/
.svelte-kit/

# TypeScript
*.tsbuildinfo
```

---

## 4. AI 忽略规范

### 4.1 AI 相关文件识别

| 类型 | 描述 | 示例 |
|------|------|------|
| 模型文件 | 训练好的 AI 模型 | `model.pt`, `model.onnx` |
| 数据集 | 训练数据 | `data/`, `dataset/` |
| 缓存文件 | AI 工具生成的缓存 | `.cache/`, `__pycache__/` |
| 向量数据库 | 向量索引文件 | `chroma/`, `pinecone/` |

### 4.2 AI 忽略规则

```gitignore
# AI 模型文件
*.pt
*.pth
*.onnx
*.h5
*.bin
*.ckpt

# 数据集
data/
dataset/
datasets/

# 向量数据库
chroma/
pinecone/
qdrant/
weaviate/

# 缓存目录
.cache/
__pycache__/
.huggingface/
.tensorflow/

# 模型下载目录
models/
downloads/
checkpoints/
```

### 4.3 AI 配置文件管理

```gitignore
# API Key 文件
*.key
*.secret
api-keys.json

# 配置文件
config/secrets.yml
config/secrets.json

# 环境变量
.env.local
.env.production
```

---

## 5. Trae 忽略规范

### 5.1 .traeignore 文件位置

- 根目录：`.traeignore` - 全局 Trae 忽略规则

### 5.2 .traeignore 作用

`.traeignore` 文件用于指定 Trae AI 系统应忽略的文件和目录，这些文件不会被技能系统加载或处理。

### 5.3 .traeignore 标准配置

```gitignore
# 技能缓存目录
.cache/
.skills-cache/

# 技能临时文件
*.tmp
*.temp

# 技能索引文件
skills-index.json
skills-mapping.json

# 日志文件
*.log
logs/

# 测试文件
__tests__/
*.test.js
*.spec.js

# 编辑器文件
.vscode/
.idea/
*.swp
*.swo
*~

# 系统文件
.DS_Store
Thumbs.db

# 技能开发文件
dev/
examples/
samples/

# 技能模板文件
*.template
*.stub

# 技能打包文件
*.zip
*.tar.gz
*.tgz

# 依赖目录
node_modules/
vendor/

# 构建产物
dist/
build/
```

### 5.4 .traeignore 与 .gitignore 的区别

| 文件 | 作用 | 适用范围 |
|------|------|----------|
| `.gitignore` | 版本控制忽略 | Git 仓库 |
| `.traeignore` | 技能系统忽略 | Trae AI 技能加载 |

### 5.5 联合使用建议

```
项目根目录
├── .gitignore          # Git 版本控制忽略
├── .traeignore         # Trae 技能系统忽略
└── .trae/
    ├── skills/         # 技能目录（Git 追踪，Trae 加载）
    └── cache/          # 缓存目录（Git 忽略，Trae 忽略）
```

### 5.6 示例配置

**项目根目录 `.gitignore`**：
```gitignore
# Trae 缓存目录
.trae/cache/
.trae/.cache/

# Trae 临时文件
.trae/*.tmp
```

**项目根目录 `.traeignore`**：
```gitignore
# 忽略技能开发目录
dev/
examples/

# 忽略测试文件
__tests__/
*.test.js

# 忽略编辑器配置
.vscode/
.idea/
```

**技能目录 `.traeignore`**（可选）：
```gitignore
# 技能内部忽略规则
node_modules/
*.log
```

---

## 6. AI 编码标准

### 6.1 AGENTS.md 文档规范

AGENTS.md 是项目级 AI 编码助手的行为规范文档，定义 AI 在本项目中应该遵循的编码规范、禁止操作、上下文规则等。

#### 6.1.1 AGENTS.md 结构

```markdown
# AI 编码助手规范（AGENTS.md）

## 1. 项目上下文

### 1.1 项目概述
{项目名称、技术栈、主要功能}

### 1.2 架构说明
{整体架构、模块划分、关键技术点}

### 1.3 代码风格
{编码规范、命名约定、格式要求}

## 2. 编码规则

### 2.1 必须遵循
- {规则1}
- {规则2}

### 2.2 禁止操作
- {禁止1}
- {禁止2}

### 2.3 最佳实践
- {实践1}
- {实践2}

## 3. 技能使用指引

### 3.1 推荐技能
- {技能名称} - {使用场景}

### 3.2 技能组合
- {场景} → {技能1} → {技能2}

## 4. 工具配置

### 4.1 Linter/Formatter
{ESLint、Prettier、Checkstyle等配置}

### 4.2 测试框架
{测试框架选择、测试覆盖率要求}

## 5. 安全规范

### 5.1 敏感信息
{API Key、密码等敏感信息处理规则}

### 5.2 代码审查
{PR审查要求、安全检查清单}
```

#### 6.1.2 AGENTS.md 示例

```markdown
# AI 编码助手规范（AGENTS.md）

## 1. 项目上下文

### 1.1 项目概述
- 项目名称：xx-service
- 技术栈：Java 21, Spring Boot 3.2, MyBatis Plus
- 主要功能：订单管理、支付处理

### 1.2 架构说明
- 分层架构：Controller → Service → Repository
- 外部调用：统一通过 infra/client 层
- 配置管理：Spring Boot 配置 + 环境变量

### 1.3 代码风格
- 命名：驼峰命名法
- 格式：Google Java Style
- 注释：Javadoc 风格

## 2. 编码规则

### 2.1 必须遵循
- 所有外部调用必须放在 infra/client 层
- 使用 @Metric 注解进行埋点
- 统一使用 ThreadPoolUtils 获取线程池
- 参数校验使用 @Valid + ValidateUtils

### 2.2 禁止操作
- 禁止在 Controller 中写业务逻辑
- 禁止硬编码配置值
- 禁止使用 System.out.println
- 禁止直接操作数据库连接

### 2.3 最佳实践
- 优先使用枚举定义常量
- 使用 DTO 进行数据传输
- 异常统一处理，使用 BusinessException
- 日志使用 LogUtils，分级记录

## 3. 技能使用指引

### 3.1 推荐技能
- common-coding - 编码规范指导
- common-db-design - 数据库设计
- personal-java-common - Java 通用工具

### 3.2 技能组合
- 新建功能 → common-requirement-clarification → common-spec-driven → common-spring-boot-init

## 4. 工具配置

### 4.1 Linter/Formatter
- Checkstyle：google_checks.xml
- Spotless：Google Java Format

### 4.2 测试框架
- JUnit 5 + Mockito
- 测试覆盖率要求：≥80%

## 5. 安全规范

### 5.1 敏感信息
- API Key 存储在环境变量
- 配置文件中使用 ${ENV_VAR} 引用
- 禁止提交 .env 文件

### 5.2 代码审查
- PR 必须通过 CI 检查
- 必须有至少1个 reviewer
- 安全扫描必须通过
```

#### 6.1.3 AGENTS.md 与 .cursorrules 对比

| 文件 | 用途 | 格式 |
|------|------|------|
| `AGENTS.md` | 项目级 AI 规范文档 | Markdown，详细说明 |
| `.cursorrules` | Cursor IDE 规则文件 | 简单规则列表 |
| `.windsurfrules` | WindSurf IDE 规则文件 | YAML 格式 |

### 6.2 多 AI 工具忽略文件规范

为支持多种 AI 编码助手，项目应提供统一的忽略文件体系。

#### 6.2.1 忽略文件矩阵

| 文件 | 适用工具 | 作用 |
|------|----------|------|
| `.gitignore` | Git | 版本控制忽略 |
| `.traeignore` | Trae | Trae 技能系统忽略 |
| `.cursorignore` | Cursor | Cursor AI 忽略 |
| `.codeiumignore` | Codeium | Codeium AI 忽略 |
| `.aiderignore` | Aider | Aider AI 忽略 |
| `.copilotignore` | GitHub Copilot | Copilot AI 忽略 |

#### 6.2.2 .cursorignore 标准配置

```gitignore
# 构建产物
target/
build/
dist/

# IDE 文件
.idea/
.vscode/

# 日志文件
*.log
logs/

# 配置文件
.env
application-local.yml

# 系统文件
.DS_Store
Thumbs.db

# 依赖目录
node_modules/

# AI 模型文件
*.pt
*.pth
*.onnx

# 数据集
data/
dataset/

# 缓存目录
.cache/
__pycache__/
```

#### 6.2.3 .codeiumignore 标准配置

```gitignore
# Codeium 忽略规则
# 大文件（超过 1MB）
*.jar
*.war
*.zip
*.tar.gz

# 二进制文件
*.class
*.dll
*.so

# 日志文件
*.log

# 临时文件
*.tmp
*.temp

# 构建产物
target/
build/
dist/

# 依赖目录
node_modules/

# 缓存目录
.cache/
```

#### 6.2.4 .aiderignore 标准配置

```gitignore
# Aider 忽略规则
# 大型二进制文件
*.jar
*.war
*.exe

# 构建目录
target/
build/
dist/

# 依赖目录
node_modules/

# 日志文件
*.log

# 配置文件
.env
application-local.yml

# IDE 文件
.idea/
.vscode/

# 系统文件
.DS_Store
```

#### 6.2.5 .copilotignore 标准配置

```gitignore
# GitHub Copilot 忽略规则
# 敏感文件
.env
*.key
*.secret

# 构建产物
target/
build/
dist/

# 依赖目录
node_modules/

# 日志文件
*.log

# 测试报告
reports/
coverage/

# 临时文件
*.tmp
*.temp
```

#### 6.2.6 统一忽略规则建议

为简化维护，建议在项目根目录创建统一的忽略规则文件：

```
项目根目录/
├── .gitignore              # Git 版本控制（最全）
├── .traeignore             # Trae 技能系统
├── .cursorignore           # Cursor AI
├── .codeiumignore          # Codeium AI
├── .aiderignore            # Aider AI
└── .copilotignore          # Copilot AI
```

**统一规则策略**：
1. `.gitignore` 作为主忽略文件，包含所有规则
2. 其他 AI 忽略文件引用 `.gitignore` 或仅包含特定规则

### 6.3 AI 辅助开发工作流标准

#### 6.3.1 代码生成流程

```
需求澄清 → 方案设计 → 代码生成 → 规范验证 → 测试编写 → 代码审查
  ↓           ↓           ↓           ↓           ↓           ↓
grill-me   solution-    coding      code-      unit-test  code-review
           research                 review
```

#### 6.3.2 AI 使用规则

| 阶段 | AI 角色 | 使用技能 |
|------|---------|----------|
| 需求分析 | 质询者 | common-grill-me, common-requirement-clarification |
| 方案设计 | 分析师 | personal-solution-research, common-spec-driven |
| 代码实现 | 辅助者 | common-coding, personal-java-common |
| 规范验证 | 检查者 | common-code-review |
| 测试编写 | 测试者 | common-backend-unit-test |
| 文档编写 | 文档者 | personal-doc-manager, common-api-doc |

#### 6.3.3 AI 输出规范

1. **代码质量**：生成的代码必须符合项目编码规范
2. **可维护性**：代码结构清晰，注释完整
3. **安全性**：不包含敏感信息，遵循安全规范
4. **测试覆盖**：关键功能必须有单元测试
5. **文档配套**：代码必须有相应的文档说明

---

## 7. 项目结构规范

### 7.1 后端项目结构

```
xx-parent/                     # Maven 父模块
├── pom.xml                    # 父 POM 配置
├── xx-gateway/               # 网关模块
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/bage/xx/gateway/
│       └── resources/
├── xx-biz/                   # 业务模块
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/bage/xx/biz/
│       └── resources/
├── xx-infra/                 # 基础设施模块
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/bage/xx/infra/
│       └── resources/
└── .trae/                    # 技能目录
    └── skills/
```

### 7.2 模块职责说明

| 模块 | 职责 | 说明 |
|------|------|------|
| `xx-gateway` | 请求路由和认证 | 处理外部请求，进行认证授权 |
| `xx-biz` | 核心业务逻辑 | 实现业务功能，处理业务规则 |
| `xx-infra` | 基础设施支持 | 公共组件、工具类、配置 |

### 7.3 包结构规范

```
com/bage/xx/
├── controller/          # REST API 控制层
├── service/             # 业务逻辑层
│   └── impl/           # 服务实现
├── repository/          # 数据访问层
├── entity/              # 数据库实体
├── dto/                 # 数据传输对象
│   ├── request/        # 请求 DTO
│   └── response/       # 响应 DTO
├── config/              # 配置类
├── exception/           # 自定义异常
└── util/                # 工具类
```

---

## 7. 技能安装与配置

### 7.1 安装方式

#### 使用安装脚本（推荐）

```bash
# 从 skills 仓库安装到目标项目
bash <(curl -s https://raw.githubusercontent.com/your-repo/study-ai-skills/main/install-skills.sh) /path/to/your/project

# 或者克隆仓库后安装
git clone https://github.com/your-repo/study-ai-skills.git
cd study-ai-skills
./install-skills.sh /path/to/your/project
```

#### 手动安装

```bash
# 创建技能目录
mkdir -p /path/to/your/project/.trae/skills

# 复制技能（以 common-project-standard 为例）
cp -r /path/to/study-ai-skills/skills/common-project-standard /path/to/your/project/skills/
```

### 7.2 安装脚本使用说明

```bash
# 查看帮助
./install-skills.sh -h

# 列出所有可用技能
./install-skills.sh -l

# 安装所有技能到当前目录
./install-skills.sh .

# 安装所有技能到指定目录
./install-skills.sh /path/to/project

# 安装指定技能
./install-skills.sh -s common-coding -s personal-backend-coding-standard .

# 强制覆盖已存在的技能
./install-skills.sh -f -s common-project-standard .

# 交互式安装
./install-skills.sh -i

# 指定IDE安装
./install-skills.sh --ide trae .
```

### 7.3 配置文件

创建 `.trae/config.yml` 配置文件：

```yaml
# .trae/config.yml
skills:
  # 是否自动加载所有技能
  auto-load: true
  
  # 技能搜索路径
  paths:
    - .trae/skills
  
  # 默认启用的技能
  enabled:
    - personal-backend-coding-standard
    - personal-frontend-coding-standard
    - personal-java-common
    - personal-project-runner
    - personal-doc-manager
    - personal-solution-research
    - personal-brief-style
    - common-project-standard
    - common-coding
    - common-contract-generation
    - common-backend-unit-test
    - common-frontend-playwright-test
    - common-requirement-clarification
    - common-api-doc
    - common-data-migration
    - common-db-design
    - common-dockerize
    - common-grill-me
    - common-spec-driven
    - common-spring-boot-init
    - common-ui-design-spec
    - common-ui-mockup
    - common-vue-init
```

### 7.4 全局配置（推荐）

在用户主目录创建配置文件，使所有项目默认使用这些技能：

```bash
# 创建全局配置目录
mkdir -p ~/.trae

# 创建全局技能配置
cat > ~/.trae/skills-config.yml << 'EOF'
# 全局技能配置
skills:
  # 技能仓库位置
  repository: ~/bage/github/study/study-ai-skills/.trae/skills
  
  # 默认启用的技能
  default-enabled:
    - personal-backend-coding-standard
    - personal-frontend-coding-standard
    - common-project-standard
    - common-coding
  
  # 自动同步更新
  auto-sync: true
EOF

# 创建全局安装脚本链接
ln -sf ~/bage/github/study/study-ai-skills/install-skills.sh ~/.trae/install-skills.sh

# 添加到 PATH
echo 'export PATH="$HOME/.trae:$PATH"' >> ~/.bashrc
echo 'export PATH="$HOME/.trae:$PATH"' >> ~/.zshrc
```

---

## 8. 技能规范验证

### 8.1 验证脚本使用

```bash
# 验证所有技能
./validate-skills.sh

# 验证指定技能
./validate-skills.sh -s common-coding

# 列出所有技能
./validate-skills.sh -l

# 修复技能格式
./validate-skills.sh -f

# 修复指定技能
./validate-skills.sh -f -s common-coding
```

### 8.2 验证规则

| 检查项 | 说明 | 级别 |
|--------|------|------|
| SKILL.md 存在 | 技能目录必须包含 SKILL.md | 错误 |
| Frontmatter 完整 | 必须包含 name 和 description 字段 | 错误 |
| name 匹配目录名 | name 字段值应与目录名一致 | 警告 |
| 功能描述章节 | 必须包含 ## 功能描述 | 警告 |
| 何时使用章节 | 必须包含 ## 何时使用 | 警告 |
| 核心功能章节 | 必须包含 ## 核心功能 | 警告 |
| 输入参数章节 | 必须包含 ## 输入参数 | 警告 |
| 输出格式章节 | 必须包含 ## 输出格式 | 警告 |
| 输入参数表格 | 输入参数章节应包含表格 | 警告 |
| 输出格式示例 | 输出格式章节应包含示例 | 警告 |

### 8.3 修复功能

验证脚本的 `-f` 参数可以自动修复以下问题：

1. 创建缺失的 SKILL.md 文件
2. 添加缺失的 Frontmatter 字段
3. 添加缺失的章节标题

---

## 9. 开源技能包装机制

### 9.1 设计理念

为了支持多种实现的切换，开源技能采用包装模式：

```
┌─────────────────────────────────────────────────────┐
│                    技能接口层                       │
│              (Skill Interface)                      │
│  ┌─────────────────────────────────────────────┐   │
│  │  - 统一的输入参数                            │   │
│  │  - 统一的输出格式                            │   │
│  │  - 统一的调用方式                            │   │
│  └─────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│                    实现切换层                       │
│              (Implementation Switch)               │
│  ┌─────────────────────────────────────────────┐   │
│  │  - 配置驱动的实现选择                         │   │
│  │  - 运行时动态切换                           │   │
│  │  - 实现隔离与解耦                           │   │
│  └─────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        ▼               ▼               ▼
┌───────────────┐ ┌───────────────┐ ┌───────────────┐
│  自研实现     │ │ 开源实现 A    │ │ 开源实现 B    │
│ (Default)    │ │ (OpenSourceA) │ │ (OpenSourceB) │
└───────────────┘ └───────────────┘ └───────────────┘
```

### 9.2 技能命名约定

| 前缀 | 说明 | 示例 |
|------|------|------|
| `os-` | 开源技能包装 | `os-ui-library`, `os-chat-model` |
| `os-{name}-impl-` | 具体实现 | `os-ui-library-impl-native`, `os-ui-library-impl-componenta` |

### 9.3 配置方式

```yaml
# .trae/config.yml
skills:
  open-source:
    # UI组件库实现选择: native/componenta/componentb
    os-ui-library:
      active: native
      implementations:
        - name: native
          description: 自研轻量级实现
          enabled: true
        - name: componenta
          description: 基于 Component Library A
          enabled: true
          dependencies:
            - "@componenta/core"
        - name: componentb
          description: 基于 Component Library B
          enabled: false
          dependencies:
            - "@componentb/react"
    
    # 聊天模型实现选择: openai/deepseek/claude
    os-chat-model:
      active: deepseek
      implementations:
        - name: openai
          description: OpenAI API
          enabled: true
          env:
            - OPENAI_API_KEY
        - name: deepseek
          description: DeepSeek API
          enabled: true
          env:
            - DEEPSEEK_API_KEY
        - name: claude
          description: Claude API
          enabled: true
          env:
            - CLAUDE_API_KEY
```

### 9.4 开源技能包装示例

#### 技能目录结构

```
skills/
├── os-ui-library/                    # 包装技能（统一接口）
│   └── SKILL.md
├── os-ui-library-impl-native/        # 自研实现
│   └── SKILL.md
├── os-ui-library-impl-componenta/    # 组件库A实现
│   └── SKILL.md
└── os-ui-library-impl-componentb/    # 组件库B实现
    └── SKILL.md
```

#### SKILL.md 包装层示例

```markdown
---
name: "os-ui-library"
description: "UI组件库技能 - 支持多种实现切换"
---

# UI组件库技能

## 功能描述

提供统一的 UI 组件库接口，支持多种实现切换。

## 何时使用

需要使用 UI 组件时，通过配置选择不同实现。

## 核心功能

- 统一的组件接口
- 运行时实现切换
- 多种开源组件库支持

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| componentType | String | 是 | 组件类型 |
| props | Object | 否 | 组件属性 |
| implementation | String | 否 | 指定实现（native/componenta/componentb） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "implementation": "native",
  "component": {...}
}
```

## 实现配置

在 `.trae/config.yml` 中配置：

```yaml
skills:
  open-source:
    os-ui-library:
      active: native
```

## 可用实现

| 实现名称 | 描述 | 状态 |
|----------|------|------|
| native | 自研轻量级实现 | ✅ |
| componenta | 基于 Component Library A | ✅ |
| componentb | 基于 Component Library B | ⚠️ |
```

### 9.5 切换实现流程

```bash
# 查看当前配置
cat .trae/config.yml | grep -A 5 "os-ui-library"

# 切换到 Component Library A
sed -i '' 's/active: native/active: componenta/' .trae/config.yml

# 安装依赖（如果需要）
npm install @componenta/core

# 验证切换
./validate-skills.sh -s os-ui-library
```

---

## 10. 代码审查检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 技能结构 | 技能目录和文件是否符合规范 | ✅/❌ |
| 文档完整性 | 是否有必要的文档说明 | ✅/❌ |
| Git忽略 | .gitignore 是否完整 | ✅/❌ |
| AI忽略 | AI 相关文件是否正确忽略 | ✅/❌ |
| Trae忽略 | .traeignore 是否完整 | ✅/❌ |
| AGENTS.md | 是否存在 AGENTS.md 文档 | ✅/❌ |
| AGENTS内容 | AGENTS.md 是否包含项目上下文和编码规则 | ✅/❌ |
| Cursor忽略 | .cursorignore 是否完整 | ✅/❌ |
| Codeium忽略 | .codeiumignore 是否完整 | ✅/❌ |
| Aider忽略 | .aiderignore 是否完整 | ✅/❌ |
| Copilot忽略 | .copilotignore 是否完整 | ✅/❌ |
| 项目结构 | 目录结构是否符合规范 | ✅/❌ |
| 命名规范 | 文件和目录命名是否符合规范 | ✅/❌ |
| 技能注册 | 是否在 SKILLS.md 中注册 | ✅/❌ |
| 规范验证 | 是否通过技能规范验证 | ✅/❌ |
| 开源包装 | 开源技能是否符合包装规范 | ✅/❌ |

---

## 11. 配置要求

### 11.1 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| PROJECT_NAME | 项目名称 | - |
| PROJECT_TYPE | 项目类型 | backend |
| TRAE_SKILLS_PATH | 技能目录路径 | .trae/skills |

### 11.2 配置文件

```yaml
project:
  name: xx
  type: backend
  modules:
    - gateway
    - biz
    - infra

skills:
  auto-load: true
  paths:
    - .trae/skills
  open-source:
    os-ui-library:
      active: native
    os-chat-model:
      active: deepseek
```

---

## 12. 扩展指南

### 12.1 添加新技能

1. 在 `skills/` 目录下创建新目录
2. 创建 `SKILL.md` 文件
3. 在 `AGENTS.md` 中注册技能
4. 在 `SKILLS.md` 中添加技能清单
5. 使用 `./validate-skills.sh` 验证格式

### 12.2 添加开源技能包装

1. 创建包装层技能（`os-{name}`）
2. 创建各实现技能（`os-{name}-impl-{implementation}`）
3. 在配置文件中定义可用实现
4. 实现统一接口层

### 12.3 切换开源实现

1. 修改 `.trae/config.yml` 中的 active 配置
2. 安装必要的依赖
3. 验证配置正确性

---

## 附录：常用忽略规则汇总

### Java 项目

```gitignore
# Compiled class files
*.class

# Package files
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# IDE
.idea/
*.iws
*.iml
*.ipr
.vscode/
```

### JavaScript/TypeScript 项目

```gitignore
# Dependencies
node_modules/

# Build outputs
dist/
build/
.next/
.nuxt/

# TypeScript
*.tsbuildinfo
*.log

# IDE
.vscode/
.idea/

# Environment
.env
.env.local
.env.*.local
```

### AI/ML 项目

```gitignore
# Model files
*.pt
*.pth
*.onnx
*.h5
*.bin
*.ckpt
*.joblib
*.pickle

# Datasets
data/
dataset/
datasets/

# Vector databases
chroma/
pinecone/
qdrant/

# Cache
.cache/
__pycache__/
.huggingface/
.tensorflow/
```