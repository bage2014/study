---
name: "common-project-standard"
description: "项目规范技能。提供项目结构、文档管理、代码忽略等方面的标准和最佳实践，以及技能规范验证和开源技能包装机制。"
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
.trae/skills/
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
description: "技能描述"
---

# 技能名称

## 功能描述
## 何时使用
## 核心功能
## 输入参数
## 输出格式
## 使用流程
## 最佳实践
## 配置要求
## 扩展指南
```

### 1.4 技能注册流程

1. 在 `.trae/skills/` 目录下创建技能目录
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

## 通用技能

| 技能名称 | 描述 |
|----------|------|
| common-project-standard | 项目规范技能 |
| common-contract-generation | 契约生成技能 |
| common-frontend-playwright-test | 前端Playwright测试技能 |
| common-backend-unit-test | 后端单元测试技能 |
| common-coding | 编码技能 |
| common-requirement-clarification | 需求澄清技能 |

## 功能技能

| 技能名称 | 描述 |
|----------|------|
| spring-ai-chat | Spring AI 聊天集成 |
| model-provider-creator | 模型提供商创建器 |
| mcp-tool-creator | MCP工具创建器 |
| plan-executor | 计划执行器 |
| exception-analysis | 异常分析技能 |

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

所有技能位于 `.trae/skills/` 目录下。

## 技能分类

### 个人定制技能
- personal-backend-coding-standard - 后端编码规范
- personal-frontend-coding-standard - 前端编码规范

### 通用技能
- common-project-standard - 项目规范
- common-contract-generation - 契约生成
- common-frontend-playwright-test - 前端测试
- common-backend-unit-test - 后端测试
- common-coding - 编码规范
- common-requirement-clarification - 需求澄清

### 功能技能
- spring-ai-chat - AI 聊天功能
- model-provider-creator - 模型提供商
- mcp-tool-creator - MCP工具创建
- plan-executor - 计划执行器
- exception-analysis - 异常分析

### 开源技能包装
- os-ui-library - UI组件库（可切换实现）
- os-chat-model - 聊天模型（可切换实现）

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

## 5. 项目结构规范

### 5.1 后端项目结构

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

### 5.2 模块职责说明

| 模块 | 职责 | 说明 |
|------|------|------|
| `xx-gateway` | 请求路由和认证 | 处理外部请求，进行认证授权 |
| `xx-biz` | 核心业务逻辑 | 实现业务功能，处理业务规则 |
| `xx-infra` | 基础设施支持 | 公共组件、工具类、配置 |

### 5.3 包结构规范

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

## 6. 技能安装与配置

### 6.1 安装方式

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
cp -r /path/to/study-ai-skills/.trae/skills/common-project-standard /path/to/your/project/.trae/skills/
```

### 6.2 安装脚本使用说明

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

### 6.3 配置文件

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
    - common-project-standard
    - common-coding
    - common-contract-generation
    - common-backend-unit-test
    - common-frontend-playwright-test
    - common-requirement-clarification
    - spring-ai-chat
    - model-provider-creator
    - mcp-tool-creator
    - plan-executor
    - exception-analysis
```

### 6.4 全局配置（推荐）

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

## 7. 技能规范验证

### 7.1 验证脚本使用

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

### 7.2 验证规则

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

### 7.3 修复功能

验证脚本的 `-f` 参数可以自动修复以下问题：

1. 创建缺失的 SKILL.md 文件
2. 添加缺失的 Frontmatter 字段
3. 添加缺失的章节标题

---

## 8. 开源技能包装机制

### 8.1 设计理念

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

### 8.2 技能命名约定

| 前缀 | 说明 | 示例 |
|------|------|------|
| `os-` | 开源技能包装 | `os-ui-library`, `os-chat-model` |
| `os-{name}-impl-` | 具体实现 | `os-ui-library-impl-native`, `os-ui-library-impl-componenta` |

### 8.3 配置方式

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

### 8.4 开源技能包装示例

#### 技能目录结构

```
.trae/skills/
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

### 8.5 切换实现流程

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

## 9. 代码审查检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 技能结构 | 技能目录和文件是否符合规范 | ✅/❌ |
| 文档完整性 | 是否有必要的文档说明 | ✅/❌ |
| Git忽略 | .gitignore 是否完整 | ✅/❌ |
| AI忽略 | AI 相关文件是否正确忽略 | ✅/❌ |
| 项目结构 | 目录结构是否符合规范 | ✅/❌ |
| 命名规范 | 文件和目录命名是否符合规范 | ✅/❌ |
| 技能注册 | 是否在 SKILLS.md 中注册 | ✅/❌ |
| 规范验证 | 是否通过技能规范验证 | ✅/❌ |
| 开源包装 | 开源技能是否符合包装规范 | ✅/❌ |

---

## 10. 配置要求

### 10.1 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| PROJECT_NAME | 项目名称 | - |
| PROJECT_TYPE | 项目类型 | backend |
| TRAE_SKILLS_PATH | 技能目录路径 | .trae/skills |

### 10.2 配置文件

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

## 11. 扩展指南

### 11.1 添加新技能

1. 在 `.trae/skills/` 目录下创建新目录
2. 创建 `SKILL.md` 文件
3. 在 `AGENTS.md` 中注册技能
4. 在 `SKILLS.md` 中添加技能清单
5. 使用 `./validate-skills.sh` 验证格式

### 11.2 添加开源技能包装

1. 创建包装层技能（`os-{name}`）
2. 创建各实现技能（`os-{name}-impl-{implementation}`）
3. 在配置文件中定义可用实现
4. 实现统一接口层

### 11.3 切换开源实现

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