# AI 研发流水线平台 — 架构文档

- **版本**：v2.0
- **变更时间**：2026-07-11

---

## 1. 系统全景

```
┌─────────────────────────────────────────────────────────────────┐
│                   ai-pipeline-ui  (Vue 3 + Vite)                │
│  端口: 8082                                                     │
│  页面: 需求录入 | 流水线列表 | 流水线详情 | 阶段进度 | 生成代码   │
└───────────────────────┬─────────────────────────────────────────┘
                        │ /api/* 代理
┌───────────────────────▼─────────────────────────────────────────┐
│                ai-pipeline-gateway  (Spring Boot)                │
│  端口: 8083                                                     │
│  功能: CORS | 请求转发 | API 聚合                                │
│  WebClient → http://localhost:8080/api/pipeline                 │
└───────────────────────┬─────────────────────────────────────────┘
                        │ REST API + SSE
┌───────────────────────▼─────────────────────────────────────────┐
│                   ai-pipeline-api  (Spring Boot 3.3)            │
│  端口: 8080                                                     │
│  PipelineController | PipelineService                           │
│                                                                  │
│  ┌─ strategy/ ────────────────────────────────────────────────┐ │
│  │  deploy:      LocalDocker                                   │ │
│  │  build:       LocalBuild                                    │ │
│  │  testExec:    Local                                         │ │
│  │  scm:         None                                          │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
│  ┌─ activity/ (Temporal Activity 实现) ───────────────────────┐ │
│  │  RequirementAnalysis → FeaturePointSplit → TaskSplit        │ │
│  │  → CodeGeneration → TestGen → CodeReview                    │ │
│  │  → WriteAndCommit → TestExecution → Build → Deploy          │ │
│  └────────────────────────────────────────────────────────────┘ │
└───────────────────────┬─────────────────────────────────────────┘
                        │  Temporal Java SDK
┌───────────────────────▼─────────────────────────────────────────┐
│             ai-pipeline-core  (接口 + DTO + Workflow 编排)       │
│                                                                  │
│  PipelineWorkflow (Interface + Impl)                             │
│  ├── RequirementAnalysisActivity                                 │
│  ├── FeaturePointSplitActivity  ← 需求 → 3~8 功能点              │
│  ├── TaskSplitActivity          ← 功能点 → 1~3 原子任务           │
│  ├── [Signal: approve/reject/cancel]  ── 人工门禁 & 取消         │
│  ├── CodeGenerationActivity    ← 原子任务聚焦代码生成              │
│  ├── TestGenActivity                                             │
│  ├── CodeReviewActivity        ← CRITICAL 问题触发人工审批        │
│  ├── WriteAndCommitActivity    ← JGit + SCM PR 创建              │
│  ├── TestExecutionActivity     ← Local | DockerSandbox           │
│  ├── BuildActivity                                               │
│  └── DeployActivity                                              │
│                                                                  │
│  enums: ProjectType | BuildTool | PipelineStatus | StageName    │
│  dto:   PipelineStartInput | CodeGenInput | FeaturePoint ...     │
└───────────────────────┬─────────────────────────────────────────┘
                        │  Spring DI
┌───────────────────────▼─────────────────────────────────────────┐
│         ai-pipeline-agent  (LangChain4j Agent + Tool 层)         │
│                                                                  │
│  Agent Services:                                                 │
│  RequirementAgentService      → 需求分析                          │
│  FeaturePointSplitAgentService → 功能点拆分                       │
│  TaskSplitAgentService        → 任务拆分                          │
│  CodeGenAgentService          → 代码生成（三模式分发）             │
│  TestGenAgentService          → 测试生成                          │
│  CodeReviewAgentService       → 代码审查                          │
│                                                                  │
│  AgentToolRegistry: @Component 自动扫描所有 AgentTool Bean        │
│                                                                  │
│  Tools (实现 AgentTool 接口，自动注册到 Registry):               │
│    file-read    → FileReadTool                                   │
│    file-write   → FileWriteTool                                  │
│    git          → GitTool                                        │
│    maven        → MavenTool                                      │
│    npm          → NpmTool                                        │
│    docker       → DockerTool                                     │
│    ast-analysis → AstAnalysisTool (JavaParser)                   │
│                                                                  │
│  ModelGatewayConfig:                                             │
│  chatLanguageModel ─→ provider: claude | openai | ollama          │
└───────────────────────┬─────────────────────────────────────────┘
                        │
          ┌─────────────┼──────────────┐
          ▼             ▼              ▼
   Anthropic Claude  OpenAI GPT    Ollama (本地)
```

---

## 2. 多模块结构

```
study-ai-dev-flow/                ← 父 POM（BOM 统一版本管理）
├── ai-pipeline-core/             ← 无 Spring 依赖的纯 Java 模块
│   ├── activity/                 ← Temporal Activity 接口（10 个）
│   ├── workflow/                 ← PipelineWorkflow 接口 + Impl
│   ├── dto/activity/             ← 各阶段 Input/Result（Lombok @Data）
│   ├── dto/workflow/             ← PipelineStartInput / Signal DTO
│   └── enums/                    ← ProjectType | BuildTool | TaskType | StageName
│
├── ai-pipeline-agent/            ← LangChain4j Agent 层
│   ├── config/
│   │   ├── ModelGatewayConfig    ← 多 Provider 工厂
│   │   └── AgentToolRegistry     ← 自动收集所有 AgentTool Bean
│   ├── service/
│   │   ├── RequirementAgentService
│   │   ├── FeaturePointSplitAgentService
│   │   ├── TaskSplitAgentService
│   │   ├── CodeGenAgentService
│   │   ├── TestGenAgentService
│   │   └── CodeReviewAgentService
│   └── tool/
│       ├── AgentTool             ← 标记接口，toolName() 方法
│       ├── FileReadTool
│       ├── FileWriteTool
│       ├── GitTool
│       ├── MavenTool
│       ├── NpmTool
│       ├── DockerTool
│       └── AstAnalysisTool
│
├── ai-pipeline-api/              ← Spring Boot 主应用（端口 8080）
│   ├── activity/*ActivityImpl    ← 10 个 Temporal Activity 实现
│   ├── config/TemporalConfig     ← Worker 注册所有 Activity
│   ├── strategy/                 ← 4 个可插拔策略维度
│   ├── service/PipelineService   ← 流水线入口
│   ├── controller/               ← REST 控制器
│   ├── entity/                   ← JPA 实体
│   └── repository/               ← Spring Data Repository
│
├── ai-pipeline-gateway/          ← Spring Boot 网关服务（端口 8083）
│   ├── config/WebClientConfig    ← WebClient 配置
│   ├── controller/PipelineGatewayController ← REST API（CORS）
│   └── service/PipelineGatewayService ← 请求转发
│
├── ai-pipeline-ui/               ← Vue 3 + Vite 前端（端口 8082）
│   ├── src/api/                  ← API 封装
│   ├── src/router/               ← 路由配置
│   ├── src/views/                ← 页面组件
│   └── src/App.vue               ← 根组件
│
└── demo-backend/                 ← 演示目标项目（H2 内存 CRUD）
```

---

## 3. 三层分解核心数据流

```
用户提交需求
    │
    ▼
PipelineService.startPipeline()
    │
    ├── 持久化 PipelineRunEntity
    └── Temporal WorkflowClient.start(PipelineWorkflow)
                │
                ▼
        PipelineWorkflowImpl（运行于 Temporal Worker 线程）
                │
         Stage 1: RequirementAnalysisActivity
                │  → agentService.analyze(prd) → parsedRequirementJson
                │  [Signal 门禁？]
                │
         Stage 2: FeaturePointSplitActivity
                │  → agentService.split(parsedJson) → List<FeaturePoint>(3~8个)
                │
         Stage 3+4 （循环）：for each FeaturePoint：
                │
                ├── TaskSplitActivity
                │     → agentService.split(fp) → List<AtomicTask>(1~3个)
                │
                └── for each AtomicTask：
                      CodeGenerationActivity      ← 原子任务聚焦模式
                            → codeGenInput.currentTask = task
                            → generateTaskCode(taskTitle, taskDesc, targetFiles)
                            → 仅修改 task.targetFiles 指定的文件
                │
                │  （合并所有 AtomicTask 生成的文件）
                │  [Signal 门禁？]
                │
         Stage 5: TestGenActivity
         Stage 6: CodeReviewActivity（CRITICAL 问题触发门禁）
         Stage 7: WriteAndCommitActivity
                │    ├── JGit: branch + add + commit
                │    └── PrCreationStrategy:
                │           └── None: 仅本地提交
                │    [Signal 门禁？]
                │
         Stage 8: TestExecutionActivity
                │    └── LocalTestExecutionStrategy: 宿主机 mvn test
                │
                │  测试失败自修复循环（最多 maxTestFixRetries 次）：
                │    ← CodeGenerationActivity（fixCode 模式）
                │    ← WriteAndCommitActivity
                │
         Stage 9: UiTestActivity（仅全栈项目）
         Stage 10: BuildActivity → BuildStrategy.execute()
         Stage 11: DeployActivity → DeployStrategy.execute()
```

---

## 4. 三层分解对比

| 维度 | 一次性 CodeGen | 三层分解 |
|------|--------------|---------|
| LLM 上下文 | 全量需求 + 整个项目结构 | 每个 AtomicTask 只处理 1~3 个目标文件 |
| 指令聚焦度 | 低（AI 需自行拆解+分配） | 高（任务标题+描述+targetFiles 精确指向）|
| 失败恢复粒度 | 全部重生成 | 仅失败的 AtomicTask 重执行 |
| 代码冲突概率 | 高（多模块同时修改） | 低（每任务独立 diff）|
| 复杂需求成功率 | 低（上下文超长易截断） | 高（短上下文精确执行）|

---

## 5. AgentToolRegistry 设计

```
AgentTool（标记接口）
    └── toolName(): String

@Component FileReadTool   → implements AgentTool, toolName="file-read"
@Component FileWriteTool  → implements AgentTool, toolName="file-write"
@Component GitTool        → implements AgentTool, toolName="git"
@Component MavenTool      → implements AgentTool, toolName="maven"
@Component NpmTool        → implements AgentTool, toolName="npm"
@Component DockerTool     → implements AgentTool, toolName="docker"
@Component AstAnalysisTool → implements AgentTool, toolName="ast-analysis"

AgentToolRegistry（Spring @Component）
    构造时：List<AgentTool> agentTools → Map<toolName, tool>
    getTools(String... names) → Object[]（按名称提取 LangChain4j Tool 数组）

Service 使用示例：
    AiServices.builder(CodeGenAiService.class)
        .tools(toolRegistry.getTools("file-read", "file-write", "ast-analysis"))
        .build();

新增工具：只需 @Component + implements AgentTool + toolName()，无需修改任何 Service
```

---

## 6. 策略矩阵

| 维度 | 当前默认策略 | 可切换策略 | 激活方式 |
|------|------------|----------|---------|
| LLM Provider | claude | openai / ollama | `ai.model-provider` |
| 部署方式 | local-docker | - | 代码扩展 |
| 构建方式 | local | - | 代码扩展 |
| 测试执行 | local | - | 代码扩展 |
| SCM PR 创建 | none | - | 代码扩展 |

---

## 7. 部署拓扑（开发环境）

```
本机
├── Docker Compose
│   ├── temporal-server      :7233
│   └── postgresql           :5432
│
├── ai-pipeline-api          :8080  （内嵌 H2，无需独立 DB）
├── ai-pipeline-gateway      :8083  （API 网关，CORS 处理）
├── ai-pipeline-ui           :8082  （Vue 前端）
└── demo-backend (部署目标)   :8081
```

### 端口汇总

| 服务 | 端口 | 说明 |
|------|------|------|
| ai-pipeline-ui | 8082 | Vue 前端界面 |
| ai-pipeline-gateway | 8083 | API 网关 |
| ai-pipeline-api | 8080 | 后端核心服务 |
| temporal-server | 7233 | Temporal 工作流引擎 |
| postgresql | 5432 | Temporal 数据库 |
| demo-backend (部署目标) | 8081 | AI 生成的应用 |

---

## 8. API 接口列表

### Gateway API（前端访问入口，端口 8083）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/pipelines` | 查询所有流水线 |
| GET | `/api/pipelines/{pipelineId}` | 查询流水线详情 |
| POST | `/api/pipelines/start` | 启动流水线 |
| GET | `/api/pipelines/{pipelineId}/stages` | 查询阶段记录 |
| GET | `/api/pipelines/{pipelineId}/files` | 查询生成文件列表 |
| GET | `/api/pipelines/{pipelineId}/files/{fileName}` | 查询文件内容 |
| POST | `/api/pipelines/{pipelineId}/cancel` | 取消流水线 |

### Core API（后端内部，端口 8080）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/pipeline/start` | 启动流水线 |
| GET | `/api/pipeline/{pipelineId}` | 查询流水线状态 |
| GET | `/api/pipeline` | 查询所有流水线 |
| GET | `/api/pipeline/status/{status}` | 按状态查询 |
| POST | `/api/pipeline/{pipelineId}/approval/{stage}` | 审批操作 |
| GET | `/api/pipeline/{pipelineId}/approval/{stage}` | 查询审批状态 |
| POST | `/api/pipeline/{pipelineId}/cancel` | 取消流水线 |
| GET | `/api/pipeline/{pipelineId}/stages` | 查询阶段记录 |
| GET | `/api/pipeline/{pipelineId}/files` | 查询生成文件列表 |
| GET | `/api/pipeline/{pipelineId}/files/{fileName}` | 查询文件内容 |
| GET | `/api/pipeline/{pipelineId}/stream` | SSE 实时状态推送 |

---

## 9. 安全边界

| 方面 | 现状 | 风险等级 |
|------|------|---------|
| 文件读写路径 | FileReadTool/FileWriteTool 有路径穿越检测 | ✅ 已处理 |
| AI 生成代码执行 | Local 模式执行 | ⚠️ 开发阶段可接受 |
| WebSocket 认证 | 无鉴权 | ⚠️ 开发阶段可接受 |