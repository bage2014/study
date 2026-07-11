# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added

- **ai-pipeline-gateway** 模块：新增 API 网关服务，提供统一前端访问入口和 CORS 支持
- **ai-pipeline-ui** 模块：新增 Vue 3 + Vite 前端界面
- **阶段记录功能**：PipelineStageEntity 实体和阶段记录 API，支持查看流水线执行进度
- **Gateway API 文档**：ai-pipeline-gateway/README.md
- **UI 文档**：ai-pipeline-ui/README.md
- **根目录 .gitignore**：统一忽略配置
- **启动脚本升级**：start.sh 支持一键启动所有服务（API、Gateway、UI）

### Changed

- **架构升级**：从单模块 API 升级为三层架构（UI → Gateway → API）
- **StageName 枚举整理**：重新排序枚举值，未启用阶段移至末尾
- **ActivityImpl 命名统一**：`TestGenerationActivityImpl` → `TestGenActivityImpl`
- **架构文档更新**：v2.0 添加 Gateway 和 UI 模块说明、端口汇总、Gateway API 接口列表
- **设计文档更新**：v2.0 补充阶段序号、PipelineStageEntity 数据模型

### Fixed

- **编译器参数问题**：在 ai-pipeline-api 和 ai-pipeline-gateway 的 pom.xml 中添加 `-parameters` 参数
- **CORS 问题**：Gateway 层添加 `@CrossOrigin` 注解

### Removed

- 无

## [v1.0] - 2026-07-10

### Added

- **ai-pipeline-core** 模块：Temporal Activity 接口、DTO、枚举、Workflow 编排
- **ai-pipeline-agent** 模块：LangChain4j Agent 层、工具注册机制
- **ai-pipeline-api** 模块：Spring Boot 主应用、Activity 实现、策略模式
- **demo-backend** 模块：演示目标项目
- **架构文档**：docs/architecture.md
- **设计文档**：docs/design.md
- **操作手册**：docs/OPERATION_GUIDE.md
- **Docker Compose**：Temporal 服务器和 PostgreSQL 配置
- **启动脚本**：start.sh

### Changed

- 无

### Fixed

- 无

### Removed

- 无
