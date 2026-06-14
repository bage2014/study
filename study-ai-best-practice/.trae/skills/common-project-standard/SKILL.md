---
name: "common-project-standard"
description: "项目规范技能。提供项目结构、文档管理、代码忽略等方面的标准和最佳实践。"
---

# 项目规范技能

## 功能描述

提供项目结构、文档管理、代码忽略等方面的标准和最佳实践，帮助团队建立统一的项目规范。

## 何时使用

在以下情况调用此技能：
- 创建新项目时
- 维护现有项目时
- 需要制定项目规范时
- 需要培训团队成员时

## 核心功能

- **技能管理**：规范技能的创建和管理流程
- **文档管理**：规范文档的编写和维护标准
- **Git忽略**：规范 .gitignore 文件的配置
- **AI忽略**：规范 AI 相关文件的忽略规则
- **项目结构**：规范项目目录结构

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| projectType | String | 否 | 项目类型（backend/frontend/fullstack） |
| framework | String | 否 | 使用的框架 |

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
    "structure": {...}
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

#### SKILL.md 模板

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

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|

## 输出格式

## 使用流程

## 最佳实践

## 配置要求

## 扩展指南
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

## 6. 代码审查检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 技能结构 | 技能目录和文件是否符合规范 | ✅/❌ |
| 文档完整性 | 是否有必要的文档说明 | ✅/❌ |
| Git忽略 | .gitignore 是否完整 | ✅/❌ |
| AI忽略 | AI 相关文件是否正确忽略 | ✅/❌ |
| 项目结构 | 目录结构是否符合规范 | ✅/❌ |
| 命名规范 | 文件和目录命名是否符合规范 | ✅/❌ |

---

## 7. 配置要求

### 7.1 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| PROJECT_NAME | 项目名称 | - |
| PROJECT_TYPE | 项目类型 | backend |

### 7.2 配置文件

```yaml
project:
  name: xx
  type: backend
  modules:
    - gateway
    - biz
    - infra
```

---

## 8. 扩展指南

### 8.1 添加新技能

1. 在 `.trae/skills/` 目录下创建新目录
2. 创建 `SKILL.md` 文件
3. 在 `AGENTS.md` 中注册技能
4. 在 `SKILLS.md` 中添加技能清单

### 8.2 添加新文档

1. 在 `docs/` 目录下创建文档文件
2. 在 README.md 中添加文档链接
3. 确保文档格式符合规范

### 8.3 更新忽略规则

1. 更新根目录 `.gitignore` 文件
2. 如果需要，添加模块特定的 `.gitignore`
3. 确保 AI 相关文件正确忽略

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