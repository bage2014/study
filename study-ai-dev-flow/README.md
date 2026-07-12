# AI Pipeline Platform

> AI 驱动的自动化软件开发流水线平台

![AI Pipeline Platform](https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20AI%20development%20pipeline%20platform%20dashboard%20with%20workflow%20visualization%20and%20progress%20tracking&image_size=landscape_16_9)

## 🎯 项目简介

AI Pipeline Platform 是一款面向软件开发团队的 AI 驱动自动化开发平台。通过自然语言需求输入，AI 自动完成从需求分析、方案设计、代码实现、测试验证到发布部署的全流程，实现**"一句话需求，一键式交付"**的软件开发体验。

### 核心特性

- **项目中心化**：选择项目，AI 自动识别技术栈和数据库配置
- **完整开发流水线**：需求 → 方案设计 → 编码实现 → 变更预览 → 编译构建 → 测试验证 → 发布部署
- **三层分解聚焦**：需求 → 功能点 → 原子任务，提高代码生成精度
- **测试失败自修复**：自动触发修复循环，减少人工干预
- **企业级可靠性**：基于 Temporal 的持久化工作流，支持断点续跑和人工审批
- **高度可扩展**：策略模式 + 工具自注册，灵活适配不同环境

## 🚀 快速开始

### 环境要求

- Java 21+
- Maven 3.9+
- Node.js 18+
- Docker

### 一键启动

```bash
# 克隆项目
git clone https://github.com/bage/study-ai-dev-flow.git
cd study-ai-dev-flow

# 启动 Temporal 服务
docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0

# 构建后端
mvn clean package -DskipTests

# 启动 API 服务
cd ai-pipeline-api
java -jar target/ai-pipeline-api-1.0.0-SNAPSHOT.jar

# 启动 Gateway 服务（新终端）
cd ai-pipeline-gateway
java -jar target/ai-pipeline-gateway-1.0.0-SNAPSHOT.jar

# 启动前端（新终端）
cd ai-pipeline-ui
npm install
npm run dev
```

### 访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| 前端界面 | http://localhost:8082 | AI Pipeline Platform |
| API 服务 | http://localhost:8080 | 后端核心服务 |
| Gateway | http://localhost:8083 | API 网关 |
| Temporal UI | http://localhost:8233 | 工作流监控 |
| H2 Console | http://localhost:8080/h2-console | 数据库控制台 |

## 📋 使用流程

### 1. 选择项目

进入首页，选择目标项目（如 demo-backend）

### 2. 输入需求

在文本框中输入自然语言需求：

```
添加用户注册功能，支持手机号验证和密码加密
```

### 3. 提交启动

点击"提交需求，启动AI流水线"按钮

### 4. 查看进度

系统自动完成以下流程：

```
需求分析 → 功能点拆分 → 任务拆分 → 代码生成 → 测试生成 → 代码审查 → 测试执行 → 构建 → 部署
```

### 5. 验证结果

部署完成后，访问 http://localhost:8081 验证生成的应用

## 📁 项目结构

```
study-ai-dev-flow/
├── ai-pipeline-core/          # 核心模块（接口、DTO、枚举、Workflow）
│   ├── activity/              # Temporal Activity 接口
│   ├── workflow/              # PipelineWorkflow 接口 + 实现
│   ├── dto/                   # 数据传输对象
│   └── enums/                 # 枚举定义
│
├── ai-pipeline-agent/         # AI Agent 层
│   ├── config/                # 配置类（模型网关、工具注册）
│   ├── service/               # Agent 服务（需求分析、代码生成等）
│   └── tool/                  # 工具类（文件读写、Git、Maven 等）
│
├── ai-pipeline-api/           # API 服务（Spring Boot 主应用）
│   ├── activity/              # Activity 实现
│   ├── config/                # Temporal 配置
│   ├── controller/            # REST 控制器
│   ├── service/               # 业务服务
│   ├── strategy/              # 策略模式实现
│   ├── entity/                # JPA 实体
│   └── repository/            # 数据仓库
│
├── ai-pipeline-gateway/       # API 网关
│   ├── config/                # WebClient 配置
│   ├── controller/            # 网关控制器
│   └── service/               # 网关服务
│
├── ai-pipeline-ui/            # Vue 前端
│   ├── src/api/               # API 封装
│   ├── src/router/            # 路由配置
│   ├── src/views/             # 页面组件
│   └── src/components/        # 公共组件
│
├── demo-backend/              # 演示目标项目（Spring Boot）
├── demo-frontend/             # 演示目标项目（Vue）
├── docs/                      # 项目文档
└── docker-compose.yml         # Docker Compose 配置
```

## 🛠️ 技术栈

### 后端

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.3.4 | Web 框架 |
| Temporal SDK | 1.24.1 | 工作流引擎 |
| LangChain4j | 0.35.0 | AI Agent 框架 |
| JGit | 6.9.0 | Git 操作 |
| JavaParser | - | 代码分析 |
| H2 | - | 内存数据库 |

### 前端

| 组件 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 前端框架 |
| Vite | 5.x | 构建工具 |
| Tailwind CSS | 3.x | 样式框架 |
| Vue Router | 4.x | 路由管理 |

## 📚 文档目录

- [需求文档 (PRD)](docs/PRD.md) - 产品需求文档
- [竞品分析文档](docs/competitive-analysis.md) - 竞品对比分析
- [方案调研文档](docs/solution-research.md) - 技术选型和设计原理
- [架构文档](docs/architecture.md) - 系统架构设计
- [设计文档](docs/design.md) - 详细设计文档
- [操作手册](docs/OPERATION_GUIDE.md) - 系统使用指南
- [变更日志](docs/CHANGELOG.md) - 版本变更记录

## 🔧 API 接口

### 项目管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/project` | 查询所有项目 |
| GET | `/api/project/{id}` | 查询项目详情 |
| POST | `/api/project` | 创建项目 |
| PUT | `/api/project/{id}` | 更新项目 |
| DELETE | `/api/project/{id}` | 删除项目 |
| GET | `/api/project/{id}/requirements` | 查询项目需求 |

### 需求管理

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/requirement` | 创建需求 |
| GET | `/api/requirement/{id}` | 查询需求 |
| PUT | `/api/requirement/{id}` | 更新需求 |
| DELETE | `/api/requirement/{id}` | 删除需求 |

### 流水线管理

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/pipelines/start` | 启动流水线 |
| GET | `/api/pipelines` | 查询所有流水线 |
| GET | `/api/pipelines/{id}` | 查询流水线详情 |
| POST | `/api/pipelines/{id}/cancel` | 取消流水线 |
| GET | `/api/pipelines/{id}/stages` | 查询阶段记录 |

## 🤝 贡献指南

欢迎贡献代码！请遵循以下流程：

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/xxx`)
3. 提交代码 (`git commit -m 'feat: xxx'`)
4. 推送到分支 (`git push origin feature/xxx`)
5. 创建 Pull Request

## 📄 许可证

MIT License

## 📞 联系方式

如有问题或建议，欢迎提交 Issue 或联系开发团队。

---

**项目状态**: 🚀 开发中  
**最后更新**: 2026-07-12