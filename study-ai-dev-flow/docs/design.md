# AI 研发流水线平台 — 设计文档

- **版本**：v2.0
- **变更时间**：2026-07-11

---

## 1. 核心设计原则

| 原则 | 体现 |
|------|------|
| **可插拔策略** | Build、Deploy、TestExecution、PrCreation 通过 Strategy 接口实现，改配置不改代码 |
| **薄 Activity 层** | Activity 只做状态管理和事件推送，核心逻辑下沉到 Strategy 或 AgentService |
| **工具自注册** | `AgentTool` 标记接口 + `AgentToolRegistry` 自动收集，新增工具无需修改 Service |
| **三层分解聚焦** | 需求 → 功能点 → 原子任务，每次 CodeGen 只处理 1~3 个文件，精确执行 |
| **测试失败自修复** | TEST_EXEC 失败后自动触发 CodeGeneration 修复循环 |

---

## 2. 流水线阶段设计

### 2.1 阶段列表

| 阶段 | StageName | Activity 实现 | 人工门禁 | 序号 |
|------|-----------|-------------|---------|------|
| 需求解析 | REQUIREMENT_ANALYSIS | RequirementAnalysisActivityImpl | 可配 | 1 |
| 功能点拆解 | FEATURE_POINT_SPLIT | FeaturePointSplitActivityImpl | 无门禁 | 2 |
| 原子任务拆解 | TASK_SPLIT | TaskSplitActivityImpl | 无门禁（循环内执行）| 3 |
| 代码生成 | CODE_GEN | CodeGenerationActivityImpl | 可配 | 4 |
| 测试生成 | TEST_GEN | TestGenActivityImpl | 可配 | 5 |
| 代码审查 | CODE_REVIEW | CodeReviewActivityImpl | CRITICAL 问题自动触发 | 6 |
| 文件写入 & Git 提交 | PR_CREATION | WriteAndCommitActivityImpl | 可配 | 7 |
| 测试执行 | TEST_EXEC | TestExecutionActivityImpl | 可配，失败后自修复循环 | 8 |
| 构建 | BUILD | BuildActivityImpl | 可配 | 9 |
| 部署 | DEPLOY | DeployActivityImpl | 无门禁（终态）| 10 |

> **说明**：UI_TEST、CODE_SEARCH、CI_TRIGGER 阶段暂未启用，保留在枚举中以备扩展。

### 2.2 三层分解工作流设计

```java
var fpResult = featurePointSplitActivity.split(fpInput);

Map<String, String> allGeneratedFiles = new LinkedHashMap<>();
for (FeaturePoint fp : fpResult.getFeaturePoints()) {
    var taskResult = taskSplitActivity.split(taskInput(fp));

    for (AtomicTask task : taskResult.getTasks()) {
        CodeGenInput input = CodeGenInput.builder()
                .currentTask(task)
                .frontendLocalPath(isFrontendTask(task) ? frontendPath : null)
                .build();
        var result = codeGenActivity.generate(input);
        allGeneratedFiles.putAll(result.getGeneratedFiles());
    }
}
```

### 2.3 DTO 结构

**FeaturePoint**（功能点）
```json
{
  "id": "fp-001",
  "title": "用户管理模块",
  "description": "实现用户 CRUD 接口",
  "acceptanceCriteria": ["GET /users 返回列表", "POST /users 创建用户"],
  "scope": "backend"
}
```

**AtomicTask**（原子任务）
```json
{
  "id": "task-001",
  "featurePointId": "fp-001",
  "title": "创建 UserController",
  "description": "实现 /users 的 GET/POST 端点",
  "targetFiles": ["src/main/java/com/bage/demo/controller/UserController.java"],
  "type": "BACKEND"
}
```

### 2.4 人工审批机制

- 基于 **Temporal Signal**，等待期间 Worker 线程释放，0 资源消耗
- 每个阶段独立配置 `autoApprove: true/false`
- `CODE_REVIEW` 阶段：`reviewResult.isHasCriticalIssues()` 为 true 时才触发门禁

### 2.5 测试失败自修复循环

```
TestExecutionActivity
    ├── 成功 → 继续
    └── 失败 → testFailureContext = 错误信息
                  │
                  ├── 已达 maxTestFixRetries？→ 等待人工审批或抛出
                  └── 否 → CodeGenerationActivity（fixCode 模式）
                            → WriteAndCommitActivity（fix commit）
                            → 重试 TestExecutionActivity
```

---

## 3. CodeGenAgentService 三模式分发

| 模式 | 触发条件 | 精度 |
|------|---------|------|
| 原子任务聚焦 | currentTask != null | 最高，只处理指定文件 |
| 测试失败修复 | testFailureContext != null | 高，最小化变更 |
| 全量需求生成 | 其他情况 | 兜底模式 |

---

## 4. AgentToolRegistry + AgentTool 设计

```java
public interface AgentTool {
    String toolName();
}

@Component
public class AgentToolRegistry {
    private final Map<String, AgentTool> tools;

    public AgentToolRegistry(List<AgentTool> agentTools) {
        this.tools = agentTools.stream().collect(
            Collectors.toMap(AgentTool::toolName, t -> t, (a,b)->b, LinkedHashMap::new));
    }

    public Object[] getTools(String... names) {
        return Arrays.stream(names).map(tools::get).filter(Objects::nonNull).toArray();
    }
}
```

**新增 Tool 成本**：只需 `@Component + implements AgentTool + toolName()`

---

## 5. 可插拔策略设计

### 5.1 策略接口设计模式

```
接口（定义契约）
   └── 默认实现（@ConditionalOnMissingBean）
   └── 可选实现（@ConditionalOnProperty）
       │
       注入到 ActivityImpl（构造器注入，不感知具体实现）
```

### 5.2 四个策略维度

**部署策略 `DeployStrategy`**

| 实现类 | 行为 |
|-------|------|
| `LocalDockerDeployStrategy` | docker stop/rm → docker run |

**构建策略 `BuildStrategy`**

| 实现类 | 行为 |
|-------|------|
| `LocalBuildStrategy` | mvn package / npm run build |

**测试执行策略 `TestExecutionStrategy`**

| 实现类 | 行为 |
|-------|------|
| `LocalTestExecutionStrategy` | 宿主机直接执行 mvn test / npm test |

**PR 创建策略 `PrCreationStrategy`**

| 实现类 | 行为 |
|-------|------|
| `NonePrCreationStrategy` | 仅本地 commit，不推送不创建 PR |

---

## 6. LLM 模型网关设计

| 配置键 | 默认值 | 可选值 |
|-------|-------|-------|
| `ai.model-provider` | `claude` | `openai` / `ollama` |
| `ai.model-name` | `claude-sonnet-4-6` | 任意合法模型名 |

---

## 7. 数据模型

### PipelineRunEntity

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| pipelineId | String | 流水线 ID（UUID）|
| status | Enum | PENDING/RUNNING/WAITING_APPROVAL/COMPLETED/FAILED/CANCELLED |
| currentStage | String | 当前阶段 |
| inputJson | String | 输入参数 JSON |
| resultJson | String | 执行结果 JSON |

### ApprovalEntity

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| pipelineId | String | 流水线 ID |
| stage | String | 阶段名称 |
| approved | Boolean | 是否通过 |
| reviewer | String | 审核人 |
| comment | String | 审核意见 |

### PipelineStageEntity

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| pipelineId | String | 流水线 ID |
| stageName | String | 阶段名称（StageName 枚举值）|
| status | String | 阶段状态（RUNNING/COMPLETED/FAILED）|
| startTime | LocalDateTime | 开始时间 |
| endTime | LocalDateTime | 结束时间 |
| durationMs | Long | 耗时（毫秒）|
| resultJson | String | 结果 JSON |
| errorMessage | String | 错误信息 |
| orderNum | Integer | 阶段序号 |
| createdAt | LocalDateTime | 创建时间 |

---

## 8. 关键技术选型

| 组件 | 选型 | 版本 | 作用 |
|------|------|------|------|
| 主框架 | Spring Boot | 3.3.4 | Web / JPA |
| AI 框架 | LangChain4j | 0.35.0 | Agent / Tool Calling |
| Workflow 引擎 | Temporal SDK | 1.24.1 | 持久化工作流 / Signal 审批 |
| 数据库 | H2 | - | 开发期零配置运行 |
| Git 操作 | JGit | 6.9.0 | 纯 Java，跨平台 |
| Java AST | JavaParser | - | AstAnalysisTool |
| 容器化 | Docker | - | Build + Deploy |