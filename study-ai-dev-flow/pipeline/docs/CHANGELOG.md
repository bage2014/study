# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Changed

- **项目结构调整**：根 POM 迁移至仓库根目录 `study-ai-dev-flow/pom.xml`，聚合 `pipeline/`（pipeline-parent）与 `demo/`（demo-parent）两个模块组；`docs/`、`docker-compose.yml`、`start.sh`、`.env` 归入 `pipeline/` 目录
- **文档适配新结构**：README、architecture、OPERATION_GUIDE 同步更新模块结构树、启动命令（`pipeline/` 目录构建、API jar 名 `*-exec.jar`）、访问端口
- **端口修正**：ai-pipeline-ui 前端开发服务器端口 8082 → 8081（Vite 配置）；补充部署目标应用端口说明
- **API 路径修正**：文档中 `/api/pipelines/*` 统一修正为实际控制器路径 `/api/pipeline/*`，补充 approval / files / stream 等接口
- **Temporal SDK 版本对齐**：文档版本号 1.24.1 修正为根 POM 实际版本 1.30.0

### Fixed

- **任务拆分解析重试**：DeepSeek 输出对话文本导致任务 JSON 解析失败时，`TaskSplitAgentService` 以硬约束提示词（只输出 JSON 数组、禁用工具）自动重试一次
- **空产物校验**：流水线 CODE_GEN 后若 0 个文件生成则标记 FAILED 并终止，杜绝"假成功"
- **部署失败校验**：DEPLOY 失败时流水线标记 FAILED（原实现会带 `Access: null` 仍报 COMPLETED）
- **测试自修复触发条件**：`PipelineWorkflowImpl` 自修复循环原仅在 Activity 抛异常时触发；现 `TestExecResult.success=false`（如测试编译失败）同样进入修复循环（受 `maxTestFixRetries` 约束）
- **部署日志**：`LocalDockerDeployStrategy` 子进程输出重定向至 `target/deploy.log`，不再丢失
- **Gateway 补齐**：新增 `/api/pipeline/status/{status}` 转发；`/start` 透传 `autoApproveMap`、`projectType`、`buildTool`、`frontendLocalPath`、`autoFixOnTestFail`、`maxTestFixRetries`、`sandboxEnabled`
- **部署端口冲突**：部署目标应用硬编码端口 8081 → 8082（常量 `DEPLOY_HOST_PORT`），避免与前端 UI（8081）冲突，并与 demo-backend 默认配置保持一致
- **start.sh 端口打印**：修正 Gateway 地址打印（8082 → 8083）及验证命令路径（`/api/pipelines` → `/api/pipeline`）

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
