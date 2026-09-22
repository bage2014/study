# Pilot 平台 Java 生态实现指南

**核心变更**: 将 Next.js/TypeScript AI Agent 协作平台的核心逻辑映射到 Java 生态实现方案  
**变更时间**: 2026-07-15  
**变更人**: 某同学

---

## 一、技术栈选型

### 对标项目

| 项目 | 设计选择 | 背后原因 |
|------|---------|---------|
| **Spring Cloud** | 服务拆分、事件总线（Spring Cloud Bus）、配置中心 | 微服务化，支持多实例横向扩展 |
| **Temporal.io** | 持久化工作流编排，内置重试/回放机制 | 长流程可靠性，Agent任务可中断恢复 |
| **LangChain4j** | Java 原生 LLM 工具集成框架，支持 MCP、工具调用 | 与 Anthropic SDK 对等的 Java 生态实现 |

### 推荐技术栈

```
Spring Boot 3.x (WebFlux + MVC 混合)
  ├── 框架层
  │   ├── Spring WebFlux          → SSE 实时事件流（对标 Next.js SSE）
  │   ├── Spring Data JPA         → ORM（对标 Prisma ORM）
  │   ├── Spring Security         → 认证（对标 src/lib/auth.ts）
  │   ├── Spring Events           → 本地事件总线（对标 ChorusEventBus）
  │   └── Spring Cache (Redis)    → 跨实例事件广播（对标 Redis Pub/Sub）
  │
  ├── 持久化
  │   ├── PostgreSQL 16           → 主数据库（一致）
  │   ├── Flyway                  → 数据库迁移（对标 Prisma Migrations）
  │   └── Redis 7 (Lettuce)       → Pub/Sub + 缓存
  │
  ├── AI/Agent
  │   ├── LangChain4j             → LLM 工具调用、MCP Client、会话管理
  │   ├── Anthropic Java SDK      → 直连 API（非工具场景）
  │   └── MCP Java SDK            → MCP Server/Client 双向实现
  │
  └── 可观测性
      ├── Micrometer + Prometheus → 指标
      ├── OpenTelemetry           → 链路追踪（对标 Langfuse trace）
      └── Logback + ELK           → 日志
```

---

## 二、核心数据模型

对标 `prisma/schema.prisma`，使用 JPA Entity 实现。

### 主要实体关系

```java
// 需求（对标 Requirement）
@Entity
@Table(name = "requirements")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String companyUuid;

    private String projectUuid;
    private String title;
    private String status;          // imported | prd_review | specifying | ...
    private String mode;            // quick | normal
    private String prdDocumentUuid;
    private String elaborationStatus;
    private String elaborationDepth;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}

// 任务（对标 Task）
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String requirementUuid;
    private String title;
    private String status;          // pending | in_progress | to_verify | done | closed | blocked
    private String priority;        // low | medium | high | critical
    private String assigneeType;    // user | agent
    private String assigneeUuid;
    private String repoName;
    private String commitSha;
    private String acceptanceCriteria;

    @OneToMany(mappedBy = "taskUuid", cascade = CascadeType.ALL)
    private List<TaskDependency> dependencies;
}

// 活动流（对标 Activity）
@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String companyUuid;
    private String projectUuid;
    private String targetType;      // requirement | task | ...
    private String targetUuid;
    private String actorType;       // user | agent | system
    private String actorUuid;
    private String action;          // created | prd_imported | ...

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> value;

    private String sessionUuid;
    private String sessionName;

    @CreationTimestamp
    private Instant createdAt;
}

// 工作流运行记录（对标 WorkflowRun）
@Entity
@Table(name = "workflow_runs")
public class WorkflowRun {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String requirementUuid;
    private String companyUuid;
    private String projectUuid;
    private String handlerType;     // pm-agent | dev-agent | l4-pipeline | loop-verifier
    private String promptKey;       // pm-import-prd | dev-execute | ...
    private String status;          // pending | running | completed | failed | aborted
    private int retryCount;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> traceEvents;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> aguiEvents;

    @CreationTimestamp
    private Instant startedAt;
    private Instant completedAt;
}

// Agent SDK 会话（对标 AgentSdkSession）
@Entity
@Table(name = "agent_sdk_sessions")
public class AgentSdkSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String companyUuid;
    private String ownerType;       // requirement | task | dev-workflow
    private String ownerUuid;
    private String agentType;       // pm | dev
    private String agentMode;       // pm-import-prd | dev-execute | ...
    private String sdkSessionId;    // LangChain4j memory ID

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}
```

---

## 三、认证系统

对标 `src/lib/auth.ts`，使用 Spring Security 实现多态认证。

```java
// 认证上下文（对标 AuthContext）
public sealed interface AuthContext permits UserAuthContext, AgentAuthContext {
    String companyUuid();
    String actorUuid();
}

public record UserAuthContext(
    String companyUuid,
    String actorUuid,
    String email,
    String name
) implements AuthContext {}

public record AgentAuthContext(
    String companyUuid,
    String actorUuid,
    String agentName,
    List<AgentRole> roles,
    String ownerUuid           // Agent 所属用户 UUID
) implements AuthContext {
    public boolean hasPmRole()  { return roles.contains(AgentRole.PM_AGENT); }
    public boolean hasDevRole() { return roles.contains(AgentRole.DEVELOPER_AGENT); }
}

// 认证过滤器（对标 getAuthContext）
@Component
public class PilotAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        AuthContext ctx = null;

        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
            String token = authHeader.substring(7).trim();

            if (token.startsWith("ak_")) {
                // 1. API Key 认证 → Agent 上下文
                ctx = apiKeyService.validateKey(token).orElse(null);
            } else {
                // 2. JWT 认证 → User 上下文（本地开发）
                ctx = jwtService.parseToken(token).orElse(null);
            }
        }

        if (ctx == null) {
            // 3. CAS SSO — 从请求头 x-employee-id 获取
            ctx = casService.extractContext(request).orElse(null);
        }

        if (ctx != null) {
            SecurityContextHolder.getContext().setAuthentication(
                new PilotAuthentication(ctx)
            );
        }

        chain.doFilter(request, response);
    }
}

// 权限检查工具
@Component
public class AuthUtils {
    public boolean isAssignee(AuthContext ctx, String assigneeType, String assigneeUuid) {
        return switch (ctx) {
            case UserAuthContext u ->
                "user".equals(assigneeType) && assigneeUuid.equals(u.actorUuid());
            case AgentAuthContext a ->
                ("agent".equals(assigneeType) && assigneeUuid.equals(a.actorUuid())) ||
                ("user".equals(assigneeType) && assigneeUuid.equals(a.ownerUuid()));
        };
    }
}
```

---

## 四、活动流与事件总线

对标 `src/services/activity.service.ts` + `src/lib/event-bus.ts`。

### 活动写入 + 事件发布

```java
// 活动创建参数（对标 ActivityCreateParams）
public record ActivityCreateParams(
    String companyUuid,
    String projectUuid,
    String targetType,
    String targetUuid,
    String actorType,
    String actorUuid,
    String action,
    Map<String, Object> value,
    String sessionUuid,
    String sessionName
) {}

@Service
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisPublisher redisPublisher;

    public Activity createActivity(ActivityCreateParams params) {
        Activity activity = mapper.toEntity(params);
        Activity saved = activityRepo.save(activity);

        // 发布本地 Spring 事件 → 触发 WorkflowOrchestrator
        ActivityCreatedEvent event = new ActivityCreatedEvent(this, saved);
        eventPublisher.publishEvent(event);

        // 发布 Redis Pub/Sub → 跨实例广播 SSE
        redisPublisher.publish("pilot:activity", ActivityMessage.from(saved));

        return saved;
    }
}

// 活动创建事件（Spring Application Event）
public class ActivityCreatedEvent extends ApplicationEvent {
    private final Activity activity;

    public ActivityCreatedEvent(Object source, Activity activity) {
        super(source);
        this.activity = activity;
    }
}
```

### 双层事件总线

```java
// 跨实例 SSE 广播（对标 ChorusEventBus Redis 层）
@Component
public class RedisEventBus {

    private final StringRedisTemplate redisTemplate;
    private final String instanceId = UUID.randomUUID().toString();

    // 发布（发布时附带实例ID，订阅时过滤自己发出的）
    public void publish(String channel, Object data) {
        EventEnvelope envelope = new EventEnvelope(instanceId, channel, data);
        redisTemplate.convertAndSend(channel, JsonUtils.toJson(envelope));
    }

    // 订阅（SSE 场景）
    public Flux<ServerSentEvent<String>> subscribeAgui(String workflowRunUuid) {
        return Flux.create(sink -> {
            MessageListenerAdapter listener = new MessageListenerAdapter((msg, pattern) -> {
                EventEnvelope envelope = JsonUtils.fromJson(msg, EventEnvelope.class);
                if (!instanceId.equals(envelope.origin())) {  // 过滤自己的消息
                    sink.next(ServerSentEvent.builder(envelope.data()).build());
                }
            });
            redisTemplate.getConnectionFactory()
                .getConnection()
                .subscribe(listener, ("agui:" + workflowRunUuid).getBytes());
        });
    }
}
```

---

## 五、Workflow Orchestrator（核心编排层）

对标 `src/services/workflow-orchestrator.ts`，使用 Spring Events + 规则引擎实现。

### 自动化规则定义

```java
public record AutomationRule(
    String action,
    String targetType,
    HandlerType handler,
    String promptKey,
    boolean quickOnly,
    boolean normalOnly
) {
    // 规则表（对标 TypeScript 中的 rules 数组）
    public static final List<AutomationRule> RULES = List.of(
        // PM 阶段 — PRD 拉取
        rule("created",        "requirement", HandlerType.PM_AGENT,    "pm-import-prd"),
        rule("prd_url_set",    "requirement", HandlerType.PM_AGENT,    "pm-import-prd"),
        // 快速模式：直接进入 Dev Plan
        ruleQuick("prd_imported", "requirement", HandlerType.DEV_AGENT, "dev-plan"),
        // 正常模式：PM 澄清
        ruleNormal("prd_imported", "requirement", HandlerType.PM_AGENT, "pm-elaborate"),
        // L4 流水线
        rule("mr_ready",       "requirement", HandlerType.L4_PIPELINE,  null),
        // Loop 验证
        rule("submitted",      "task",        HandlerType.LOOP_VERIFIER, "loop-verifier")
    );
}

// 编排器（监听 ActivityCreatedEvent）
@Service
public class WorkflowOrchestrator {

    private final WorkflowRunService workflowRunService;
    private final PmAgentRunner pmAgentRunner;
    private final DevAgentRunner devAgentRunner;
    private final L4PipelineService l4PipelineService;
    private final ProjectAutomationConfigRepository configRepo;

    @EventListener
    @Async("orchestratorExecutor")   // 异步执行，不阻塞事务
    public void onActivity(ActivityCreatedEvent event) {
        Activity activity = event.getActivity();
        List<AutomationRule> matched = AutomationRule.RULES.stream()
            .filter(r -> r.action().equals(activity.getAction()))
            .filter(r -> r.targetType().equals(activity.getTargetType()))
            .toList();

        for (AutomationRule rule : matched) {
            dispatch(rule, activity);
        }
    }

    private void dispatch(AutomationRule rule, Activity activity) {
        String companyUuid = activity.getCompanyUuid();
        String requirementUuid = activity.getTargetUuid();

        // 1. 检查自动化配置是否启用
        ProjectAutomationConfig config = configRepo
            .findByProjectUuid(activity.getProjectUuid())
            .orElse(null);
        if (!isRuleEnabled(config, rule)) return;

        // 2. 去重：同 promptKey 已有 running 的 WorkflowRun
        if (rule.promptKey() != null &&
            workflowRunService.isRunning(requirementUuid, rule.promptKey())) {
            return;
        }

        // 3. 创建 WorkflowRun
        WorkflowRun run = workflowRunService.createRun(WorkflowRunCreateParams.builder()
            .companyUuid(companyUuid)
            .requirementUuid(requirementUuid)
            .handlerType(rule.handler().name())
            .promptKey(rule.promptKey())
            .build());

        // 4. 派发具体 handler（带重试）
        dispatchWithRetry(run, rule, activity);
    }

    private void dispatchWithRetry(WorkflowRun run, AutomationRule rule, Activity activity) {
        int maxRetries = 3;
        long[] retryDelays = {5_000, 15_000, 45_000};

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                switch (rule.handler()) {
                    case PM_AGENT    -> pmAgentRunner.run(buildPmParams(run, rule, activity));
                    case DEV_AGENT   -> devAgentRunner.run(buildDevParams(run, rule, activity));
                    case L4_PIPELINE -> l4PipelineService.start(run.getRequirementUuid());
                    case LOOP_VERIFIER -> loopVerifier.run(run, activity);
                }
                workflowRunService.complete(run.getUuid());
                return;
            } catch (Exception e) {
                if (isTemporaryError(e) && attempt < maxRetries) {
                    workflowRunService.incrementRetry(run.getUuid());
                    sleepQuietly(retryDelays[attempt]);
                } else {
                    workflowRunService.fail(run.getUuid(), e.getMessage());
                    return;
                }
            }
        }
    }

    private boolean isTemporaryError(Exception e) {
        String msg = e.getMessage();
        return msg != null && (
            msg.contains("429") || msg.contains("504") ||
            msg.contains("timeout") || msg.contains("ECONNRESET") ||
            e instanceof ConnectException
        );
    }
}
```

---

## 六、Agent SDK 适配器

对标 `src/services/agent-sdk-adapter.ts`，使用 LangChain4j 实现。

```java
// 运行参数（对标 RunQueryParams）
public record RunQueryParams(
    String label,
    String systemPrompt,
    String prompt,
    List<String> allowedTools,
    Map<String, McpServerConfig> mcpServers,
    String cwd,
    String resumeSessionId,         // 断点续训
    SessionOwner sessionOwner,
    String workflowRunUuid,
    int maxTurns,
    Duration timeout,
    String langfuseTraceId,
    String codingPlanToken,         // 用户凭证，注入子进程
    Consumer<Map<String, Object>> onAguiEvent
) {}

// 运行结果（对标 RunQueryResult）
public record RunQueryResult(
    String sdkSessionId,
    int totalTokens,
    List<ToolSummaryEntry> toolSummary,
    String finalText
) {}

@Service
public class AgentSdkAdapter {

    private final AgentSdkSessionStore sessionStore;
    private final RedisEventBus eventBus;
    private final LangfuseClient langfuse;

    public RunQueryResult runQuery(RunQueryParams params) {
        // 1. 加载或创建会话记忆（对标 SDK resume）
        ChatMemory memory = params.resumeSessionId() != null
            ? sessionStore.loadMemory(params.resumeSessionId())
            : MessageWindowChatMemory.withMaxMessages(200);

        // 2. 构建 MCP 工具集
        List<ToolSpecification> tools = buildToolSpecs(params.mcpServers(), params.allowedTools());

        // 3. 选择模型（对标 getModelIdForPromptKey）
        String modelId = resolveModelId(params.sessionOwner().agentMode());
        AnthropicChatModel model = AnthropicChatModel.builder()
            .apiKey(resolveApiKey(params.sessionOwner()))
            .baseUrl(resolveBaseUrl(params.sessionOwner()))
            .modelName(modelId)
            .maxTokens(8192)
            .build();

        // 4. 构建 Agent
        AiAgent agent = AiServices.builder(AiAgent.class)
            .chatLanguageModel(model)
            .chatMemory(memory)
            .tools(tools)
            .systemMessageProvider(ctx -> params.systemPrompt())
            .build();

        // 5. 执行（带 AG-UI 事件回调）
        String sessionId = UUID.randomUUID().toString();
        AtomicInteger totalTokens = new AtomicInteger();
        List<ToolSummaryEntry> toolSummary = new CopyOnWriteArrayList<>();

        AgentEventListener listener = buildEventListener(params, sessionId, totalTokens, toolSummary);
        String result = executeWithTimeout(agent, params.prompt(), params.timeout(), listener);

        // 6. 持久化会话
        sessionStore.saveSession(params.sessionOwner(), memory, sessionId);

        return new RunQueryResult(sessionId, totalTokens.get(), toolSummary, result);
    }

    private AgentEventListener buildEventListener(RunQueryParams params,
                                                   String sessionId,
                                                   AtomicInteger tokens,
                                                   List<ToolSummaryEntry> toolSummary) {
        return new AgentEventListener() {
            @Override
            public void onToolCall(ToolCall call) {
                toolSummary.add(new ToolSummaryEntry(call.name(), call.input()));
                // 发布 AG-UI 事件（对标 TOOL_CALL_START）
                if (params.workflowRunUuid() != null) {
                    eventBus.publish("agui:" + params.workflowRunUuid(),
                        Map.of("type", "TOOL_CALL_START", "name", call.name()));
                }
            }

            @Override
            public void onToken(TokenUsage usage) {
                tokens.addAndGet(usage.totalTokenCount());
            }
        };
    }
}

// 会话存储（对标 PrismaSessionStore）
@Service
public class AgentSdkSessionStore {

    private final AgentSdkSessionRepository sessionRepo;

    public ChatMemory loadMemory(String sdkSessionId) {
        AgentSdkSession session = sessionRepo.findBySdkSessionId(sdkSessionId)
            .orElseThrow(() -> new NotFoundException("Session not found: " + sdkSessionId));

        // 从 JSON 反序列化消息历史
        List<ChatMessage> messages = deserializeMessages(session.getMessagesJson());
        return MessageWindowChatMemory.withMaxMessages(200, messages);
    }

    public void saveSession(SessionOwner owner, ChatMemory memory, String newSessionId) {
        sessionRepo.upsertByOwner(owner, newSessionId, serializeMessages(memory.messages()));
    }
}
```

---

## 七、MCP 服务器

对标 `src/mcp/server.ts`，使用 MCP Java SDK 实现。

```java
// MCP Server 工厂（对标 server.ts）
@Component
public class McpServerFactory {

    public McpServer createServer(AuthContext auth) {
        McpServer server = McpServer.builder()
            .serverInfo("pilot", "1.0.0")
            .build();

        // 1. 公共工具
        registerPublicTools(server, auth);

        // 2. 角色特定工具
        if (auth instanceof AgentAuthContext a) {
            if (a.hasPmRole())  registerPmTools(server, auth);
            if (a.hasDevRole()) registerDeveloperTools(server, auth);
        }

        // 3. 外部 MCP 代理（项目配置的外部服务）
        registerExternalMcpTools(server, auth);

        return server;
    }
}

// PM Agent 内置工具（对标 pm-internal-server.ts）
@Component
public class PmInternalMcpServer {

    private final RequirementService requirementService;
    private final ElaborationService elaborationService;
    private final DocumentService documentService;
    private final ActivityService activityService;

    public McpServer createServer(String companyUuid, String requirementUuid) {
        McpServer server = McpServer.builder()
            .serverInfo("pm-internal", "1.0.0")
            .build();

        // get_requirement
        server.tool("get_requirement",
            "读取需求详情（含PRD、状态、关联文档）",
            Map.of("requirementUuid", stringSchema()),
            (params) -> {
                RequirementDetail detail = requirementService
                    .getDetail(companyUuid, (String) params.get("requirementUuid"));
                return TextContent.of(JsonUtils.toJson(detail));
            });

        // import_prd
        server.tool("import_prd",
            "从飞书URL导入PRD文档",
            Map.of("prdUrl", stringSchema(), "format", enumSchema("md", "txt")),
            (params) -> {
                String docContent = feishu2mdService.parseDocUrl((String) params.get("prdUrl"));
                Document doc = documentService.create(DocumentCreateParams.builder()
                    .type("prd")
                    .content(docContent)
                    .requirementUuid(requirementUuid)
                    .build());
                requirementService.updatePrd(companyUuid, requirementUuid, doc.getUuid());
                activityService.createActivity(ActivityCreateParams.builder()
                    .targetType("requirement")
                    .targetUuid(requirementUuid)
                    .action("prd_imported")
                    .build());
                return TextContent.of("PRD已导入: " + doc.getUuid());
            });

        // create_elaboration
        server.tool("create_elaboration",
            "启动需求澄清问卷",
            elaborationInputSchema(),
            (params) -> {
                ElaborationRound round = elaborationService.createRound(
                    companyUuid, requirementUuid, params);
                return TextContent.of("澄清问卷已创建: " + round.getUuid());
            });

        // validate_elaboration
        server.tool("validate_elaboration",
            "验证答案，发现矛盾/歧义",
            Map.of("issues", issuesArraySchema()),
            (params) -> {
                List<ElaborationIssue> issues = parseIssues(params);
                if (issues.isEmpty()) {
                    elaborationService.markValidated(companyUuid, requirementUuid);
                    activityService.createActivity(/* elaboration_resolved */);
                } else {
                    elaborationService.markNeedsFollowup(companyUuid, requirementUuid, issues);
                    activityService.createActivity(/* elaboration_answered */);
                }
                return TextContent.of("验证完成");
            });

        // split_features
        server.tool("split_features",
            "将PRD拆分为需求点（Feature Points）",
            featuresInputSchema(),
            (params) -> {
                List<FeaturePoint> features = featureService.createBatch(
                    companyUuid, requirementUuid, params);
                activityService.createActivity(/* features_split */);
                return TextContent.of("已拆分 " + features.size() + " 个需求点");
            });

        return server;
    }
}
```

---

## 八、Dev Agent 运行器（并发执行）

对标 `src/services/agent-runner-dev.ts`，重点实现拓扑排序 + 并发控制。

### 拓扑排序 + 分层执行

```java
@Service
public class DevAgentRunner {

    private final AgentSdkAdapter agentSdkAdapter;
    private final TaskService taskService;
    private final WorkspaceService workspaceService;
    private final RepoCacheService repoCacheService;
    private final GitLabService gitLabService;
    private final MergeRequestLinkService mrLinkService;

    // 并发参数（对标环境变量）
    @Value("${agent.dev.repo-concurrency:3}")
    private int repoConcurrency;

    @Value("${agent.dev.task-concurrency:5}")
    private int taskConcurrency;

    // dev-execute 主入口
    public DevAgentResult runExecute(DevAgentParams params) {
        List<Task> tasks = taskService.listByRequirement(params.requirementUuid());

        // 1. 拓扑排序 → 分层（同层可并发）
        List<List<Task>> layers = topoLayers(tasks);

        for (List<Task> layer : layers) {
            // 2. 为每个 task 创建独立 worktree
            Map<UUID, Path> taskWorktrees = repoCacheService
                .createParallelWorktrees(params, layer);

            // 3. 信号量控制并发（对标 withSlot）
            Semaphore semaphore = new Semaphore(taskConcurrency);
            List<CompletableFuture<TaskOutcome>> futures = layer.stream()
                .map(task -> CompletableFuture.supplyAsync(() -> {
                    semaphore.acquireUninterruptibly();
                    try {
                        return executeOneTask(task, taskWorktrees.get(task.getUuid()), params);
                    } finally {
                        semaphore.release();
                    }
                }, taskExecutor))
                .toList();

            List<TaskOutcome> outcomes = futures.stream()
                .map(CompletableFuture::join)
                .toList();

            // 4. 成功 task merge 到 feature 分支
            List<Task> successful = outcomes.stream()
                .filter(o -> "success".equals(o.outcome()))
                .map(TaskOutcome::task)
                .toList();

            for (Task t : successful) {
                workspaceService.mergeWorktreeToFeature(
                    params.featureWorktreePath(),
                    taskWorktrees.get(t.getUuid()),
                    params.branchName() + "-" + t.getUuid().toString().substring(0, 8)
                );
            }

            // 5. 层级完成后一次性 commit + push
            if (!successful.isEmpty()) {
                String commitSha = workspaceService.commitAndPushBatch(
                    params.workspace(), params.branchName(), successful,
                    params.authorName(), params.authorEmail()
                );
                taskService.updateCommitSha(successful, commitSha, "to_verify");
            }

            // 6. 创建/更新 MR
            ensureMergeRequest(params, successful);
        }

        // 7. 所有 task terminal → 触发 L4
        checkAndTriggerL4(params.requirementUuid(), params.companyUuid());
        return DevAgentResult.success();
    }

    // 拓扑分层（Kahn算法）
    private List<List<Task>> topoLayers(List<Task> tasks) {
        Map<UUID, Set<UUID>> deps = buildDependencyMap(tasks);
        Map<UUID, Integer> inDegree = tasks.stream()
            .collect(Collectors.toMap(Task::getUuid, t -> deps.getOrDefault(t.getUuid(), Set.of()).size()));

        List<List<Task>> layers = new ArrayList<>();
        Queue<Task> current = tasks.stream()
            .filter(t -> inDegree.get(t.getUuid()) == 0)
            .collect(Collectors.toCollection(LinkedList::new));

        while (!current.isEmpty()) {
            layers.add(new ArrayList<>(current));
            List<Task> next = new ArrayList<>();
            for (Task t : current) {
                // 减少依赖它的 task 的入度
                for (Task candidate : tasks) {
                    if (deps.getOrDefault(candidate.getUuid(), Set.of()).contains(t.getUuid())) {
                        int newDegree = inDegree.merge(candidate.getUuid(), -1, Integer::sum);
                        if (newDegree == 0) next.add(candidate);
                    }
                }
            }
            current = new LinkedList<>(next);
        }
        return layers;
    }

    private void checkAndTriggerL4(String requirementUuid, String companyUuid) {
        List<Task> allTasks = taskService.listByRequirement(requirementUuid);
        Set<String> terminalStatuses = Set.of("to_verify", "done", "closed");
        boolean allTerminal = allTasks.stream()
            .allMatch(t -> terminalStatuses.contains(t.getStatus()));

        if (allTerminal) {
            l4Service.requestL4Start(RequirementL4StartParams.builder()
                .requirementUuid(requirementUuid)
                .companyUuid(companyUuid)
                .build());
        }
    }
}
```

---

## 九、L4 发布流水线

对标 `src/services/l4-pipeline.service.ts`，使用状态机实现。

```java
// L4 阶段状态机（对标 TypeScript 中的 l4Stage 流转）
public enum L4Stage {
    MR_READY_CHECK, CI, CI_SUCCESS, CONFIDENCE_CHECK,
    DEPLOY_FAT, DEPLOY_UAT, DEPLOY_PROD, SUCCESS, DEPLOY_FAILED
}

@Service
public class L4PipelineService {

    @Value("${l4.confidence.threshold:0.85}")
    private double confidenceThreshold;

    public L4StartResult requestL4Start(RequirementL4StartParams params) {
        // 1. 检查 L4 是否启用
        ProjectAutomationConfig config = configRepo.findByProjectUuid(params.projectUuid());
        if (!config.isL4Enabled()) return L4StartResult.failed("l4_disabled");

        // 2. 检查 MR 存在
        List<MergeRequestLink> mrLinks = mrLinkService.listByRequirement(params.requirementUuid());
        if (mrLinks.isEmpty()) return L4StartResult.failed("no_mr_link");

        // 3. 检查是否已有运行中的 L4
        if (workflowRunService.isRunning(params.requirementUuid(), "l4-pipeline")) {
            return L4StartResult.failed("already_running");
        }

        // 4. 触发 Activity → 驱动 Orchestrator
        activityService.createActivity(ActivityCreateParams.builder()
            .targetType("requirement")
            .targetUuid(params.requirementUuid())
            .action("mr_ready")
            .build());

        return L4StartResult.success();
    }

    // GitLab Webhook 回调处理（对标 handlePipelineStatusChange）
    @EventListener
    public void onPipelineStatusChange(GitLabPipelineEvent event) {
        WorkflowRun run = workflowRunService.findByPipelineId(event.getPipelineId());
        if (run == null) return;

        switch (event.getStatus()) {
            case "success" -> advanceToConfidenceCheck(run, event);
            case "failed"  -> handleStageFailure(run, L4Stage.CI, "CI pipeline failed");
            case "running" -> updateL4Stage(run, L4Stage.CI, Map.of("pipelineId", event.getPipelineId()));
        }
    }

    private void advanceToConfidenceCheck(WorkflowRun run, GitLabPipelineEvent event) {
        double score = calculateConfidenceScore(run, event);

        if (score < confidenceThreshold) {
            // 阻断：等待人工审批
            updateL4Stage(run, L4Stage.CONFIDENCE_CHECK, Map.of(
                "confidenceScore", score,
                "confidenceBlockedAt", Instant.now().toString()
            ));
            activityService.createActivity(ActivityCreateParams.builder()
                .action("l4_confidence_blocked")
                .value(Map.of("score", score, "threshold", confidenceThreshold))
                .build());
        } else {
            // 自动推进到 FAT 部署
            deployToFat(run, event.getImageTag());
        }
    }

    private void handleStageFailure(WorkflowRun run, L4Stage stage, String reason) {
        workflowRunService.fail(run.getUuid(), reason);
        activityService.createActivity(ActivityCreateParams.builder()
            .action("l4_pipeline_failed")
            .value(Map.of("stage", stage.name(), "error", reason))
            .build());
    }
}
```

---

## 十、API 层规范

对标 `src/lib/api-response.ts` + `src/lib/api-handler.ts`。

```java
// 统一响应体（对标 ApiSuccessResponse / ApiErrorResponse）
public record ApiResponse<T>(
    boolean success,
    T data,
    ApiError error,
    PageMeta meta
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> paginated(T data, int page, int pageSize, long total) {
        return new ApiResponse<>(true, data, null, new PageMeta(page, pageSize, total));
    }

    public static ApiResponse<?> error(ErrorCode code, String message) {
        return new ApiResponse<>(false, null, new ApiError(code.name(), message), null);
    }
}

// 全局异常处理（对标 withErrorHandler）
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(404)
            .body(ApiResponse.error(ErrorCode.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<?>> handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(403)
            .body(ApiResponse.error(ErrorCode.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidTransition(InvalidStatusTransitionException e) {
        return ResponseEntity.status(422)
            .body(ApiResponse.error(ErrorCode.INVALID_STATUS_TRANSITION,
                "Cannot transition from " + e.getFrom() + " to " + e.getTo()));
    }
}

// Controller 示例（多租户 + 认证）
@RestController
@RequestMapping("/api/requirements")
public class RequirementController {

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<RequirementDto>> create(
        @AuthenticationPrincipal AuthContext auth,
        @RequestBody @Valid CreateRequirementRequest request
    ) {
        RequirementDto dto = requirementService.create(auth.companyUuid(), request);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    // SSE — AG-UI 实时事件流（对标 /api/workflow-runs/[uuid]/agui-stream）
    @GetMapping("/workflow-runs/{runUuid}/events")
    public SseEmitter streamEvents(
        @AuthenticationPrincipal AuthContext auth,
        @PathVariable String runUuid
    ) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 分钟
        eventBus.subscribeAgui(runUuid, event -> {
            try {
                emitter.send(SseEmitter.event().data(JsonUtils.toJson(event)));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
```

---

## 十一、关键架构差异与适配

| TypeScript/Next.js 实现 | Java/Spring 实现 | 关键差异 |
|------------------------|-----------------|---------|
| `src/lib/event-bus.ts` EventEmitter | Spring `ApplicationEventPublisher` + Redis Pub/Sub | Spring 事件默认同步，需加 `@Async` 实现异步 |
| Prisma ORM（relationMode=prisma） | JPA + Hibernate（应用层关联管理） | 需显式处理级联删除，避免 DB 外键 |
| `Promise.all` 并发 | `CompletableFuture.allOf` + `Semaphore` | 需配置独立线程池，防止阻塞 NIO 线程 |
| Next.js API Route | Spring MVC `@RestController` | 相同的 REST 模式，错误处理通过 `@ControllerAdvice` |
| Zod schema 运行时校验 | Bean Validation (`@Valid`) + Jackson 反序列化 | Java 编译期类型安全，减少运行时校验需求 |
| `AbortController` | `CompletableFuture.cancel()` + `Thread.interrupt()` | Java 取消模型更复杂，需设计 interrupt-safe 执行器 |
| AG-UI SSE 事件 | Spring WebFlux `SseEmitter` 或 `Flux<ServerSentEvent>` | WebFlux 更适合高并发 SSE，MVC 的 SseEmitter 也可行 |
| MCP HTTP Streamable Transport | MCP Java SDK（Streamable HTTP） | 官方 Java SDK 覆盖，API 一致 |
| LangChain4j 会话记忆 | `MessageWindowChatMemory` + 自定义 `ChatMemoryStore` | 需实现 Prisma 持久化的 `ChatMemoryStore` 接口 |

---

## 十二、多租户安全要点

所有 Service 层方法必须带 `companyUuid` 作为第一个参数，禁止跨公司查询：

```java
// 正确
public Requirement findByUuid(String companyUuid, String requirementUuid) {
    return requirementRepo.findByUuidAndCompanyUuid(requirementUuid, companyUuid)
        .orElseThrow(() -> new NotFoundException("Requirement not found"));
}

// 错误 — 未限定公司
public Requirement findByUuid(String requirementUuid) {
    return requirementRepo.findById(requirementUuid).orElseThrow(...);
}
```

---

## 附：模块结构建议

```
pilot-java/
├── pilot-api/               # Spring Boot 启动模块（Controller、Filter、Config）
├── pilot-core/              # 核心业务（Service、Domain、Repository）
│   ├── domain/              # JPA Entity
│   ├── service/             # 业务逻辑（对标 src/services/）
│   └── repository/          # Spring Data JPA
├── pilot-agent/             # Agent 运行器（PM、Dev、L4）
│   ├── adapter/             # AgentSdkAdapter（LangChain4j 封装）
│   ├── runner/              # PmAgentRunner、DevAgentRunner
│   └── mcp/                 # MCP Server 工厂 + PM 内置工具
├── pilot-workflow/          # Workflow Orchestrator + 事件总线
│   ├── orchestrator/
│   └── eventbus/
├── pilot-infra/             # 基础设施（Redis、加密、外部 HTTP 客户端）
└── pilot-common/            # 公用类型（AuthContext、ApiResponse、异常）
```

---

## 十三、切换 LLM 提供商 / 去除 CC 依赖

### 13.1 理解当前依赖边界

`@anthropic-ai/claude-agent-sdk` 的 `query()` 内部做了三件事，任何一件都需要替换：

```
Claude Code (CC 二进制) = LLM 推理层 + 工具执行层 + Agent 循环层
                              │               │              │
                    Anthropic API        Read/Write/     ReAct loop
                    (仅支持Claude)       Edit/Bash/      (多轮直到
                                        Glob/Grep        end_turn)
```

**结论**：换 DeepSeek ≠ 只换 API Key。必须同时自实现工具执行层和 Agent 循环层，因为 CC 二进制只能对接 Anthropic API。

---

### 13.2 LLM Provider 抽象层

无论使用哪个 LLM，先定义统一接口，与业务逻辑完全解耦：

```java
// LLM 提供商抽象（对标 agent-sdk-adapter.ts 的 runQuery）
public interface AgentLlmProvider {
    /**
     * 执行一次多轮 Agent 运行，直到 end_turn 或达到 maxTurns。
     * tools 由调用方注入，provider 负责调用并将结果返回给 LLM。
     */
    AgentRunResult run(AgentRunRequest request);
}

public record AgentRunRequest(
    String systemPrompt,
    String userPrompt,
    List<AgentTool> tools,          // 业务层注册的工具（含 MCP 代理）
    String resumeSessionId,         // 断点续训
    int maxTurns,
    Duration timeout,
    String modelHint,               // "fast" | "balanced" | "powerful"
    Consumer<AgentEvent> onEvent    // AG-UI 事件回调
) {}

public record AgentRunResult(
    String sessionId,
    String finalText,
    List<ToolCallRecord> toolCalls,
    TokenUsage tokenUsage
) {}

// 工具定义（统一格式，不绑定具体 LLM）
public record AgentTool(
    String name,
    String description,
    JsonNode inputSchema,                              // JSON Schema
    Function<Map<String, Object>, String> handler     // 返回文本结果
) {}
```

### 13.3 DeepSeek 实现

DeepSeek 提供 OpenAI 兼容 API，使用标准 Chat Completions + Function Calling：

```java
@Component
@ConditionalOnProperty(name = "agent.provider", havingValue = "deepseek")
public class DeepSeekAgentProvider implements AgentLlmProvider {

    // 依赖：openai-java SDK 或 OkHttp 直调，指向 DeepSeek endpoint
    // Maven: com.openai:openai-java:0.x 或 com.squareup.okhttp3:okhttp:4.x
    private final OpenAIClient client;

    public DeepSeekAgentProvider(
        @Value("${agent.deepseek.api-key}") String apiKey,
        @Value("${agent.deepseek.base-url:https://api.deepseek.com/v1}") String baseUrl
    ) {
        this.client = OpenAIClient.builder()
            .apiKey(apiKey)
            .baseUrl(baseUrl)         // DeepSeek OpenAI 兼容端点
            .build();
    }

    @Override
    public AgentRunResult run(AgentRunRequest request) {
        // 1. 工具定义转为 OpenAI Function 格式
        List<ChatCompletionTool> functions = request.tools().stream()
            .map(tool -> ChatCompletionTool.builder()
                .type("function")
                .function(FunctionDefinition.builder()
                    .name(tool.name())
                    .description(tool.description())
                    .parameters(tool.inputSchema())
                    .build())
                .build())
            .toList();

        // 2. 初始化对话历史（支持 resume）
        List<ChatMessage> messages = loadOrInitMessages(request.resumeSessionId());
        messages.add(ChatMessage.system(request.systemPrompt()));
        messages.add(ChatMessage.user(request.userPrompt()));

        String sessionId = UUID.randomUUID().toString();
        List<ToolCallRecord> toolCalls = new ArrayList<>();
        int turn = 0;

        // 3. ReAct 循环（替代 CC 内置的 agent loop）
        while (turn < request.maxTurns()) {
            turn++;

            String modelId = resolveModel(request.modelHint());
            ChatCompletion response = client.chat().completions().create(
                ChatCompletionCreateParams.builder()
                    .model(modelId)
                    .messages(messages)
                    .tools(functions)
                    .toolChoice("auto")
                    .build()
            );

            Choice choice = response.choices().get(0);
            AssistantMessage assistantMsg = choice.message();
            messages.add(assistantMsg);

            // 3a. 无工具调用 → end_turn，循环结束
            if (choice.finishReason() == FinishReason.STOP ||
                assistantMsg.toolCalls() == null ||
                assistantMsg.toolCalls().isEmpty()) {

                request.onEvent().accept(AgentEvent.runFinished(turn));
                return new AgentRunResult(
                    sessionId,
                    assistantMsg.content(),
                    toolCalls,
                    extractUsage(response)
                );
            }

            // 3b. 执行工具调用
            List<ChatMessage> toolResults = new ArrayList<>();
            for (ToolCall call : assistantMsg.toolCalls()) {
                String toolName = call.function().name();
                Map<String, Object> args = parseArgs(call.function().arguments());

                request.onEvent().accept(AgentEvent.toolCallStart(call.id(), toolName, args));

                // 查找并执行工具
                String result = request.tools().stream()
                    .filter(t -> t.name().equals(toolName))
                    .findFirst()
                    .map(t -> safeInvoke(t, args))
                    .orElse("Tool not found: " + toolName);

                toolCalls.add(new ToolCallRecord(toolName, args, result, turn));
                request.onEvent().accept(AgentEvent.toolCallEnd(call.id(), result));

                toolResults.add(ChatMessage.tool(call.id(), result));
            }
            messages.addAll(toolResults);
        }

        // maxTurns 超限
        throw new WorkflowFailureException("Agent exceeded maxTurns=" + request.maxTurns());
    }

    private String resolveModel(String hint) {
        return switch (hint) {
            case "fast"      -> "deepseek-chat";          // DeepSeek-V3（快速、低成本）
            case "powerful"  -> "deepseek-reasoner";      // DeepSeek-R1（带推理链）
            default          -> "deepseek-chat";
        };
    }
}
```

### 13.4 Anthropic 原生实现（保持兼容）

```java
@Component
@ConditionalOnProperty(name = "agent.provider", havingValue = "anthropic", matchIfMissing = true)
public class AnthropicAgentProvider implements AgentLlmProvider {

    // LangChain4j 封装 Anthropic API
    // Maven: dev.langchain4j:langchain4j-anthropic:0.x
    private final AnthropicChatModel model;

    @Override
    public AgentRunResult run(AgentRunRequest request) {
        // LangChain4j AiServices 自带 ReAct loop，直接注册 Java 方法为工具
        // 对于动态工具（MCP 代理），需要用 ToolSpecification + ToolExecutor 动态注册
        // 见 13.5 节
        ...
    }
}
```

### 13.5 文件系统工具实现（替代 CC 内置工具）

CC 二进制内置的 `Read`、`Write`、`Edit`、`Bash`、`Glob`、`Grep` 需要用 Java 自实现，注册为 `AgentTool`：

```java
@Component
public class FileSystemToolkit {

    /**
     * 构建文件系统工具集，cwd 限定沙箱根目录（对标 CC 的 allowedTools + cwd）
     */
    public List<AgentTool> buildTools(Path cwd) {
        return List.of(
            readTool(cwd),
            writeTool(cwd),
            editTool(cwd),
            bashTool(cwd),
            globTool(cwd),
            grepTool(cwd)
        );
    }

    // Read — 读取文件内容（附行号，对标 CC Read 输出格式）
    private AgentTool readTool(Path cwd) {
        return new AgentTool("Read", "Read file contents with line numbers",
            schemaOf("""
                {"type":"object","properties":{"file_path":{"type":"string"},
                "offset":{"type":"integer"},"limit":{"type":"integer"}},
                "required":["file_path"]}
            """),
            args -> {
                Path target = sandboxedPath(cwd, (String) args.get("file_path"));
                int offset = toInt(args.get("offset"), 0);
                int limit  = toInt(args.get("limit"), 2000);
                try {
                    List<String> lines = Files.readAllLines(target);
                    return IntStream.range(offset, Math.min(offset + limit, lines.size()))
                        .mapToObj(i -> (i + 1) + "\t" + lines.get(i))
                        .collect(Collectors.joining("\n"));
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    // Write — 覆盖写文件
    private AgentTool writeTool(Path cwd) {
        return new AgentTool("Write", "Write content to file (creates parent dirs)",
            schemaOf("""
                {"type":"object","properties":{"file_path":{"type":"string"},
                "content":{"type":"string"}},"required":["file_path","content"]}
            """),
            args -> {
                Path target = sandboxedPath(cwd, (String) args.get("file_path"));
                try {
                    Files.createDirectories(target.getParent());
                    Files.writeString(target, (String) args.get("content"));
                    return "Written: " + target;
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    // Edit — 精确字符串替换（对标 CC Edit 工具）
    private AgentTool editTool(Path cwd) {
        return new AgentTool("Edit", "Replace exact string in file",
            schemaOf("""
                {"type":"object","properties":{"file_path":{"type":"string"},
                "old_string":{"type":"string"},"new_string":{"type":"string"},
                "replace_all":{"type":"boolean"}},
                "required":["file_path","old_string","new_string"]}
            """),
            args -> {
                Path target = sandboxedPath(cwd, (String) args.get("file_path"));
                try {
                    String content = Files.readString(target);
                    String oldStr = (String) args.get("old_string");
                    String newStr = (String) args.get("new_string");
                    boolean replaceAll = Boolean.TRUE.equals(args.get("replace_all"));

                    // 唯一性检查（对标 CC Edit 的 unique check）
                    int count = countOccurrences(content, oldStr);
                    if (count == 0) return "Error: old_string not found in file";
                    if (count > 1 && !replaceAll)
                        return "Error: old_string not unique (" + count + " occurrences). Use replace_all=true or provide more context.";

                    String updated = replaceAll
                        ? content.replace(oldStr, newStr)
                        : content.replaceFirst(Pattern.quote(oldStr), Matcher.quoteReplacement(newStr));
                    Files.writeString(target, updated);
                    return "Edited: " + target;
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    // Bash — 沙箱内执行 shell 命令（超时 + 禁止危险命令）
    private AgentTool bashTool(Path cwd) {
        return new AgentTool("Bash", "Execute shell command in workspace",
            schemaOf("""
                {"type":"object","properties":{"command":{"type":"string"},
                "timeout":{"type":"integer","description":"ms, default 30000"}},
                "required":["command"]}
            """),
            args -> {
                String command = (String) args.get("command");
                int timeoutMs = toInt(args.get("timeout"), 30_000);

                // 危险命令拦截（rm -rf / 、fork bomb 等）
                if (isDangerousCommand(command)) {
                    return "Error: command blocked by security policy";
                }

                try {
                    ProcessBuilder pb = new ProcessBuilder("bash", "-c", command)
                        .directory(cwd.toFile())
                        .redirectErrorStream(true);
                    Process proc = pb.start();

                    boolean finished = proc.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
                    if (!finished) {
                        proc.destroyForcibly();
                        return "Error: command timed out after " + timeoutMs + "ms";
                    }

                    String output = new String(proc.getInputStream().readAllBytes());
                    int exitCode = proc.exitValue();
                    return exitCode == 0 ? output : "Exit " + exitCode + ":\n" + output;
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    // Glob — 文件模式匹配
    private AgentTool globTool(Path cwd) {
        return new AgentTool("Glob", "Find files matching glob pattern",
            schemaOf("""
                {"type":"object","properties":{"pattern":{"type":"string"},
                "path":{"type":"string"}},"required":["pattern"]}
            """),
            args -> {
                Path searchRoot = args.get("path") != null
                    ? sandboxedPath(cwd, (String) args.get("path"))
                    : cwd;
                String pattern = (String) args.get("pattern");
                try {
                    PathMatcher matcher = FileSystems.getDefault()
                        .getPathMatcher("glob:" + pattern);
                    return Files.walk(searchRoot)
                        .filter(p -> matcher.matches(searchRoot.relativize(p)))
                        .sorted(Comparator.comparing(p ->
                            p.toFile().lastModified(), Comparator.reverseOrder()))
                        .map(p -> cwd.relativize(p).toString())
                        .collect(Collectors.joining("\n"));
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    // Grep — 内容搜索（对标 ripgrep）
    private AgentTool grepTool(Path cwd) {
        return new AgentTool("Grep", "Search file contents with regex",
            schemaOf("""
                {"type":"object","properties":{"pattern":{"type":"string"},
                "path":{"type":"string"},"glob":{"type":"string"},
                "output_mode":{"type":"string","enum":["content","files_with_matches","count"]}},
                "required":["pattern"]}
            """),
            args -> {
                String regex = (String) args.get("pattern");
                Path searchRoot = args.get("path") != null
                    ? sandboxedPath(cwd, (String) args.get("path")) : cwd;
                String glob = (String) args.getOrDefault("glob", "**/*");
                String mode = (String) args.getOrDefault("output_mode", "content");
                Pattern p = Pattern.compile(regex);
                PathMatcher fileMatcher = FileSystems.getDefault()
                    .getPathMatcher("glob:" + glob);

                try {
                    List<Path> matchedFiles = Files.walk(searchRoot)
                        .filter(Files::isRegularFile)
                        .filter(f -> fileMatcher.matches(searchRoot.relativize(f)))
                        .toList();

                    return switch (mode) {
                        case "files_with_matches" -> matchedFiles.stream()
                            .filter(f -> fileContains(f, p))
                            .map(f -> cwd.relativize(f).toString())
                            .collect(Collectors.joining("\n"));
                        case "count" -> matchedFiles.stream()
                            .filter(f -> fileContains(f, p))
                            .map(f -> cwd.relativize(f) + ": " + countLines(f, p))
                            .collect(Collectors.joining("\n"));
                        default -> matchedFiles.stream()
                            .flatMap(f -> matchingLines(f, p, cwd))
                            .limit(250)
                            .collect(Collectors.joining("\n"));
                    };
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            });
    }

    // 沙箱路径校验（防止路径穿越 ../../../etc/passwd）
    private Path sandboxedPath(Path cwd, String relativePath) {
        Path resolved = cwd.resolve(relativePath).normalize();
        if (!resolved.startsWith(cwd)) {
            throw new SecurityException("Path traversal blocked: " + relativePath);
        }
        return resolved;
    }

    private boolean isDangerousCommand(String cmd) {
        return cmd.matches(".*(rm\\s+-rf\\s+[/~]|:[\\s]*\\(\\)|mkfs|dd.*of=/dev).*");
    }
}
```

### 13.6 MCP 工具代理（替代 CC 内置 MCP 客户端）

CC 内部集成了 MCP 客户端，去掉 CC 后需要用 MCP Java SDK 自行代理：

```java
@Component
public class McpToolProxy {

    private final ExternalMcpClientPool clientPool;

    /**
     * 将外部 MCP Server 的所有工具包装为 AgentTool 列表，注入 Agent 循环。
     * 对标 CC 的 mcpServers 参数 + 自动工具发现。
     */
    public List<AgentTool> buildMcpTools(Map<String, McpServerConfig> servers) {
        return servers.entrySet().stream()
            .flatMap(entry -> {
                String serverName = entry.getKey();
                McpServerConfig config = entry.getValue();
                McpClient client = clientPool.getOrCreate(serverName, config);

                // 列出该 MCP server 的所有工具
                return client.listTools().stream()
                    .map(tool -> new AgentTool(
                        "mcp__" + serverName + "__" + tool.name(),  // 命名格式与 CC 一致
                        tool.description(),
                        tool.inputSchema(),
                        args -> {
                            // 代理调用
                            McpCallResult result = client.callTool(tool.name(), args);
                            return result.content().stream()
                                .filter(c -> "text".equals(c.type()))
                                .map(c -> c.text())
                                .collect(Collectors.joining("\n"));
                        }
                    ));
            })
            .toList();
    }
}
```

### 13.7 完整替换后的 AgentSdkAdapter

```java
@Service
public class AgentSdkAdapter {

    private final AgentLlmProvider llmProvider;   // 注入 DeepSeek 或 Anthropic 实现
    private final FileSystemToolkit fsToolkit;
    private final McpToolProxy mcpProxy;
    private final AgentSdkSessionStore sessionStore;
    private final RedisEventBus eventBus;

    public RunQueryResult runQuery(RunQueryParams params) {
        // 1. 加载历史会话（断点续训）
        String resumeId = sessionStore.findLatest(params.sessionOwner()).orElse(null);

        // 2. 构建工具集 = 文件系统工具 + MCP 代理工具（完全替代 CC 的 allowedTools）
        List<AgentTool> tools = new ArrayList<>();

        // 文件系统工具（仅在有 cwd 时注册）
        if (params.cwd() != null) {
            List<AgentTool> fsTools = fsToolkit.buildTools(Path.of(params.cwd()));
            // 按 allowedTools 过滤（对标 CC 的 allowedTools 参数）
            tools.addAll(fsTools.stream()
                .filter(t -> params.allowedTools().contains(t.name()))
                .toList());
        }

        // MCP 工具代理
        if (params.mcpServers() != null && !params.mcpServers().isEmpty()) {
            tools.addAll(mcpProxy.buildMcpTools(params.mcpServers()));
        }

        // 3. 执行 Agent（对标 query({ prompt, options })）
        AgentRunResult result = llmProvider.run(AgentRunRequest.builder()
            .systemPrompt(params.systemPrompt())
            .userPrompt(params.prompt())
            .tools(tools)
            .resumeSessionId(resumeId)
            .maxTurns(params.maxTurns() != null ? params.maxTurns() : 80)
            .timeout(Duration.ofMillis(params.timeoutMs() != null ? params.timeoutMs() : 600_000))
            .modelHint(resolveModelHint(params.sessionOwner().agentMode()))
            .onEvent(event -> {
                // AG-UI 事件转发
                if (params.workflowRunUuid() != null) {
                    eventBus.publish("agui:" + params.workflowRunUuid(), event.toMap());
                }
            })
            .build());

        // 4. 持久化会话
        sessionStore.save(params.sessionOwner(), result.sessionId());

        return new RunQueryResult(
            result.sessionId(),
            result.tokenUsage().total(),
            result.toolCalls().stream()
                .map(tc -> new ToolSummaryEntry(tc.name(), tc.inputPreview(), tc.turn()))
                .toList(),
            result.finalText()
        );
    }

    private String resolveModelHint(String agentMode) {
        return switch (agentMode) {
            case "dev-execute", "dev-plan" -> "powerful";
            case "pm-import-prd", "pm-elaborate" -> "balanced";
            default -> "fast";
        };
    }
}
```

### 13.8 配置切换

通过 Spring Profile 或 `application.yml` 属性切换，零代码改动：

```yaml
# application.yml — 切换到 DeepSeek
agent:
  provider: deepseek           # anthropic（默认）| deepseek | openai

  deepseek:
    api-key: ${DEEPSEEK_API_KEY}
    base-url: https://api.deepseek.com/v1
    models:
      fast: deepseek-chat       # DeepSeek-V3
      balanced: deepseek-chat
      powerful: deepseek-reasoner  # DeepSeek-R1（带推理链）

  anthropic:
    api-key: ${ANTHROPIC_API_KEY}
    base-url: ${ANTHROPIC_BASE_URL:https://api.anthropic.com}
    models:
      fast: claude-haiku-4-5-20251001
      balanced: claude-sonnet-4-6
      powerful: claude-opus-4-7

  # 文件系统工具沙箱配置
  sandbox:
    bash-timeout-ms: 30000
    blocked-commands: "rm -rf /,:(){ :|:& };:"
```

### 13.9 方案对比

| 维度 | 保留 CC（当前） | 自实现（去掉 CC） |
|------|--------------|----------------|
| **LLM** | 仅 Anthropic / Bedrock | 任意支持 Function Calling 的 LLM |
| **工具执行** | CC 二进制内置 | Java 实现，完全可控 |
| **Agent 循环** | CC 内置 ReAct | 自实现 while 循环，逻辑透明 |
| **会话持久化** | CC SessionStore | 自实现，同样支持 resume |
| **沙箱安全** | CC 内置权限模式 | 需自己实现路径校验、命令过滤 |
| **MCP 支持** | CC 原生 | MCP Java SDK，功能等价 |
| **部署依赖** | 需要 `claude` 二进制 | 纯 JVM，无外部二进制依赖 |
| **成本** | Anthropic 定价 | DeepSeek-V3 约为 Claude 的 1/20 |
| **代码理解质量** | Claude 最强 | DeepSeek-R1 接近，V3 略弱 |

**推荐策略**：
- **生产代码修改任务**（dev-execute）→ 优先 Claude Sonnet，成本可控且质量有保证
- **PRD 导入、需求澄清**（pm-agent）→ DeepSeek-V3 完全胜任，成本降 90%
- **技术方案编写**（dev-write-tech-design）→ DeepSeek-R1（推理链）可作为备选

---

## 十四、开源生态对标分析

> 调研时间：2026-07-15。以下项目均为活跃维护状态（Devika 已 stale，仅列出供参考）。

### 14.1 核心竞品一览

| 项目 | Stars | 技术栈 | PM Agent | Dev Agent | 完整SDLC | Web UI | MR/Git集成 | MCP | Java/JVM |
|------|-------|--------|----------|-----------|---------|--------|-----------|-----|---------|
| **Pilot（本项目）** | — | Next.js/TS | ✓ | ✓ | ✓ | ✓ | ✓ GitLab深度 | ✓ | ✗ |
| **MetaGPT** | 69k | Python | ✓ | ✓ | ✓ | ✗ | ✗ | ✗ | ✗ |
| **OpenHands** | 64k+ | Python | ✗ | ✓ | 部分 | ✓ | 有限 | ✗ | ✗ |
| **AutoDev** | — | Kotlin/JVM | ✗ | ✓ | ✓ | IDE插件 | ✗ | ✗ | **✓** |
| **SWE-agent** | 19.8k | Python | ✗ | ✓ | ✗ | ✗ | ✓ Issues | ✗ | ✗ |
| **CrewAI** | 34k+ | Python | 框架 | 框架 | 框架 | ✗ | ✗ | ✗ | ✗ |
| **Aider** | 41.6k | Python | ✗ | ✓ | ✗ | ✗ | ✓ Git | ✗ | ✗ |

### 14.2 各项目架构要点与参考价值

#### MetaGPT（github.com/geekan/MetaGPT）

**设计理念**："Software Company as Code" — 每个角色是一个 Agent，角色之间通过消息传递协作：

```
需求输入
  └→ ProductManager Agent → PRD 文档
       └→ Architect Agent → 系统设计 + API 规范
            └→ ProjectManager Agent → 任务列表
                 └→ Engineer Agent × N → 代码文件
                      └→ QA Agent → 测试用例
```

**可参考的设计**：
- **Role 基类**：每个 Agent 有 `name`、`goal`、`constraints`、`actions` 四个维度定义，Java 中用接口 + 抽象类实现
- **消息路由**：Role 订阅特定 `MessageType`，类比 Pilot 的 `AutomationRule` 规则匹配
- **Action 抽象**：每个 Action 有 `_aask()`（向 LLM 提问）+ `run()`（执行），职责单一

**Java 参考实现骨架**：
```java
// 对标 MetaGPT 的 Role 基类
public abstract class AgentRole {
    protected final String name;
    protected final String goal;
    protected final List<String> constraints;

    // 订阅的消息类型（对标 MetaGPT 的 _watch）
    protected abstract Set<Class<? extends AgentMessage>> watchedMessages();

    // 收到消息时执行的 Action（对标 MetaGPT 的 _act）
    protected abstract AgentMessage act(AgentMessage input);
}

public class PmAgentRole extends AgentRole {
    @Override
    protected Set<Class<? extends AgentMessage>> watchedMessages() {
        return Set.of(RequirementCreatedMessage.class, PrdUrlSetMessage.class);
    }

    @Override
    protected AgentMessage act(AgentMessage input) {
        // 调用 AgentSdkAdapter.runQuery()
    }
}
```

---

#### OpenHands（github.com/All-Hands-AI/OpenHands）

**设计理念**：Agent 运行在沙箱 Runtime 中，通过 ActionExecutor 执行具体操作：

```
Controller（Agent 循环）
  └→ Agent.step(观察结果) → 输出 Action
       └→ Runtime.execute(Action)
            ├→ CmdRunAction  → Docker 容器内执行 bash
            ├→ FileReadAction → 读文件
            ├→ FileWriteAction → 写文件
            └→ BrowseAction  → Playwright 浏览器
```

**可参考的设计**：
- **Observation/Action 分离**：每一步是"观察 → 决策 → 执行 → 观察"的循环，与 ReAct 等价但类型更清晰
- **Event Stream**：所有事件（用户输入、Agent 动作、执行结果）写入同一个 EventStream，前端订阅展示
- **Docker 沙箱**：代码执行隔离在容器内，Java 可用 `docker-java` 库实现等价方案

**Java 参考实现**：
```java
// 对标 OpenHands 的 Action/Observation 模型
public sealed interface AgentAction permits
    CmdRunAction, FileReadAction, FileWriteAction, AgentFinishAction {}

public record CmdRunAction(String command, boolean background) implements AgentAction {}
public record FileWriteAction(String path, String content) implements AgentAction {}

public sealed interface Observation permits
    CmdOutputObservation, FileReadObservation, ErrorObservation {}

// Agent 循环（对标 OpenHands Controller）
public class AgentController {
    public void run(String task) {
        List<Event> history = new ArrayList<>();
        history.add(new UserMessageEvent(task));

        while (true) {
            AgentAction action = agent.step(history);   // LLM 决策
            if (action instanceof AgentFinishAction) break;

            Observation obs = runtime.execute(action);   // 沙箱执行
            history.add(new ActionEvent(action));
            history.add(new ObservationEvent(obs));
            eventStream.publish(obs);                    // 前端实时展示
        }
    }
}
```

---

#### AutoDev（github.com/unit-mesh/auto-dev）

**设计理念**：JVM 生态中最完整的 SDLC AI 覆盖实现，Kotlin Multiplatform，**直接参考价值最高**。

**可参考的模块**：
- `autodev-core`：语言无关的 Agent 抽象层，定义 `DevTask`、`DevFlow`、`CodeContext`
- `autodev-languages`：TreeSitter 语言解析，提取类/方法/依赖关系（Java 代码理解）
- `DevIns` 脚本语言：Agent 可执行的 DSL，类比 MCP 工具描述

**与 Pilot 的结构对应**：
```
AutoDev DevFlow     ↔  Pilot WorkflowRun
AutoDev DevTask     ↔  Pilot Task
AutoDev CodeContext ↔  Pilot Workspace/RepoCacheService
AutoDev DevIns      ↔  Pilot MCP 工具描述
```

---

#### CrewAI（github.com/crewAIInc/crewAI）

**设计理念**：通用多 Agent 编排框架，两种执行模式：

```
Crew（Hierarchical）  ←→  Pilot PM Agent 管理 Dev Agent 的模式
  └→ Manager Agent 分配任务给 Worker Agent

Flow（Event-driven）  ←→  Pilot WorkflowOrchestrator
  └→ @listen(EventA) → execute task → emit(EventB)
```

**可参考的设计**：`@listen` 装饰器驱动的 Flow 编排，Java 中用 Spring `@EventListener` + `ApplicationEventPublisher` 实现等价语义。

---

### 14.3 Pilot 的差异化护城河

经过横向对比，Pilot 在开源世界中**没有直接竞品**，差距体现在：

1. **PM + Dev 分层 + L4 发布三者集成**：MetaGPT 只到代码生成，OpenHands 只做执行，无一覆盖发布
2. **事件驱动动态编排**（Activity → Rule → WorkflowRun）：MetaGPT 是预定义 SOP，CrewAI 需手动配置 Flow，Pilot 是响应式
3. **GitLab 深度集成**（Webhook → MR 状态 → L4 卡点审批 → 环境部署）：开源项目几乎不涉及
4. **MCP 作为 Agent 能力扩展机制**：可热插拔 KB、Feishu、Portal 等工具，其他项目无此设计

---

## 十五、Java 最优实现方案

> 综合第十三章（去 CC 方案）+ 第十四章（开源生态对标），给出 Java 完整落地的最优组件选型。

### 15.1 组件选型总览

```
┌─────────────────────────────────────────────────────────────┐
│                      Pilot Java 最优技术栈                    │
├─────────────┬──────────────────────┬────────────────────────┤
│    层次      │     推荐组件          │     备选 / 说明         │
├─────────────┼──────────────────────┼────────────────────────┤
│ LLM 抽象层  │ Spring AI            │ LangChain4j（MCP更强）  │
│ Agent 工具  │ LangChain4j          │ 自实现（更轻量）         │
│ MCP 客户端  │ MCP Java SDK（官方）  │ LangChain4j MCP Module │
│ 工作流编排  │ Temporal.io          │ Spring State Machine    │
│ 事件总线    │ Spring Events+Redis  │ Kafka（高吞吐场景）      │
│ Git 操作    │ JGit + GitLab4J      │ 调用 git CLI            │
│ Web/SSE     │ Spring WebFlux       │ Spring MVC SseEmitter   │
│ 持久化      │ Spring Data JPA      │ MyBatis-Plus            │
│ 迁移        │ Flyway               │ Liquibase               │
│ 可观测性    │ OpenTelemetry Java   │ Micrometer + Zipkin     │
└─────────────┴──────────────────────┴────────────────────────┘
```

### 15.2 Maven 依赖清单

```xml
<!-- pom.xml 关键依赖 -->
<properties>
    <spring-boot.version>3.4.0</spring-boot.version>
    <spring-ai.version>1.0.0</spring-ai.version>
    <langchain4j.version>0.36.0</langchain4j.version>
    <temporal.version>1.25.0</temporal.version>
    <mcp-sdk.version>0.9.0</mcp-sdk.version>
</properties>

<dependencies>
    <!-- ===== Spring Boot 基础 ===== -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>  <!-- SSE 流式推送 -->
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- ===== Spring AI — LLM 抽象层 ===== -->
    <!-- 一套 API，配置切换 Claude / OpenAI / DeepSeek / Bedrock / Ollama -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-anthropic-spring-boot-starter</artifactId>
        <version>${spring-ai.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
        <version>${spring-ai.version}</version>
        <!-- DeepSeek 兼容 OpenAI API，同一个 starter -->
    </dependency>

    <!-- ===== LangChain4j — MCP 客户端 + 工具框架 ===== -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-mcp</artifactId>          <!-- MCP HTTP/SSE 客户端 -->
        <version>${langchain4j.version}</version>
    </dependency>
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j</artifactId>              <!-- AiServices + 工具注册 -->
        <version>${langchain4j.version}</version>
    </dependency>
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-anthropic</artifactId>    <!-- Anthropic 适配器 -->
        <version>${langchain4j.version}</version>
    </dependency>
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-open-ai</artifactId>      <!-- OpenAI/DeepSeek 适配器 -->
        <version>${langchain4j.version}</version>
    </dependency>

    <!-- ===== MCP Java SDK（官方） ===== -->
    <dependency>
        <groupId>io.modelcontextprotocol.sdk</groupId>
        <artifactId>mcp</artifactId>
        <version>${mcp-sdk.version}</version>
    </dependency>
    <dependency>
        <groupId>io.modelcontextprotocol.sdk</groupId>
        <artifactId>mcp-spring-webflux</artifactId>       <!-- Spring WebFlux 传输层 -->
        <version>${mcp-sdk.version}</version>
    </dependency>

    <!-- ===== Temporal.io — 持久化工作流编排 ===== -->
    <dependency>
        <groupId>io.temporal</groupId>
        <artifactId>temporal-sdk</artifactId>
        <version>${temporal.version}</version>
    </dependency>
    <dependency>
        <groupId>io.temporal</groupId>
        <artifactId>temporal-spring-boot-starter-alpha</artifactId>
        <version>${temporal.version}</version>
    </dependency>

    <!-- ===== Git 操作 ===== -->
    <dependency>
        <groupId>org.eclipse.jgit</groupId>
        <artifactId>org.eclipse.jgit</artifactId>         <!-- Pure Java Git -->
        <version>7.1.0.202411261347-r</version>
    </dependency>
    <dependency>
        <groupId>org.gitlab4j</groupId>
        <artifactId>gitlab4j-api</artifactId>             <!-- GitLab REST 客户端 -->
        <version>6.0.0</version>
    </dependency>

    <!-- ===== 数据库 ===== -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>              <!-- DB 迁移，对标 Prisma Migrations -->
    </dependency>
    <dependency>
        <groupId>io.hypersistence</groupId>
        <artifactId>hypersistence-utils-hibernate-63</artifactId>
        <version>3.8.3</version>                          <!-- JSONB 类型支持 -->
    </dependency>

    <!-- ===== 可观测性 ===== -->
    <dependency>
        <groupId>io.opentelemetry.instrumentation</groupId>
        <artifactId>opentelemetry-spring-boot-starter</artifactId>
        <version>2.10.0</version>                         <!-- 对标 Langfuse trace -->
    </dependency>
</dependencies>
```

### 15.3 核心决策点详解

#### 决策一：Spring AI vs LangChain4j，二选一还是并用？

**结论：并用，职责不重叠。**

```
Spring AI  → 负责 LLM 模型切换（Provider 抽象）
               ChatClient.builder()
                 .defaultSystem(systemPrompt)
                 .build()
                 .prompt(userPrompt)
                 .call()
                 .content()
               通过 spring.ai.anthropic / spring.ai.openai 配置切换
               零代码改动切换到 DeepSeek：
                 spring.ai.openai.base-url=https://api.deepseek.com/v1
                 spring.ai.openai.api-key=${DEEPSEEK_API_KEY}
                 spring.ai.openai.chat.model=deepseek-chat

LangChain4j → 负责 MCP 客户端 + 动态工具注册 + ChatMemory
               @Tool 注解直接把 Java 方法暴露给 LLM
               McpToolProvider 动态注册 MCP server 工具
               MessageWindowChatMemory 实现会话历史管理
```

**Spring AI 相比 LangChain4j 的优势**：官方 Spring 生态，与 Spring Boot AutoConfiguration 深度集成，模型切换只改配置；不足是 MCP 支持和 Agent Loop 成熟度略低于 LangChain4j。

#### 决策二：为什么选 Temporal.io 而非自实现 Orchestrator？

当前 TypeScript 的 Orchestrator 是自实现的，存在以下问题：

| 问题 | 自实现 Orchestrator | Temporal.io |
|------|-------------------|-------------|
| 服务重启丢失进行中的 WorkflowRun | 需要持久化状态到 DB，自己重新拉起 | 自动从断点恢复，零代码 |
| 重试逻辑 | 手写指数退避循环 | `@RetryOptions` 声明式配置 |
| 超时控制 | setTimeout + AbortController | `@WorkflowMethod(executionTimeout)` |
| 可见性 | 自建 WorkflowRun 表 | Temporal Web UI 内置时间线 |
| 并发 WorkflowRun 去重 | DB 查询 + 乐观锁 | 同 workflowId 的 Workflow 天然幂等 |

**Temporal 实现 WorkflowOrchestrator**：

```java
// 工作流定义（对标 dispatchHandler）
@WorkflowInterface
public interface RequirementWorkflow {
    @WorkflowMethod
    void run(String requirementUuid, String companyUuid, String promptKey);
}

@WorkflowImpl(taskQueues = "pilot-agent")
public class RequirementWorkflowImpl implements RequirementWorkflow {

    // Activity 接口（实际的 Agent 调用）
    private final PmAgentActivities pmAgent = Workflow.newActivityStub(
        PmAgentActivities.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(30))
            .setRetryOptions(RetryOptions.newBuilder()
                .setMaximumAttempts(3)
                .setInitialInterval(Duration.ofSeconds(5))
                .setBackoffCoefficient(3.0)
                .setDoNotRetry(PermanentFailureException.class.getName())
                .build())
            .build()
    );

    @Override
    public void run(String requirementUuid, String companyUuid, String promptKey) {
        switch (promptKey) {
            case "pm-import-prd"  -> pmAgent.importPrd(requirementUuid, companyUuid);
            case "pm-elaborate"   -> pmAgent.elaborate(requirementUuid, companyUuid);
            case "dev-plan"       -> devAgent.plan(requirementUuid, companyUuid);
            case "dev-execute"    -> devAgent.execute(requirementUuid, companyUuid);
            case "l4-pipeline"    -> l4Agent.run(requirementUuid, companyUuid);
        }
    }
}

// 触发工作流（对标 dispatchWithRetry）
@Service
public class WorkflowOrchestrator {

    private final WorkflowClient temporalClient;

    @EventListener
    @Async
    public void onActivity(ActivityCreatedEvent event) {
        AutomationRule rule = matchRule(event.getActivity());
        if (rule == null) return;

        // workflowId = requirementUuid + promptKey，天然去重
        String workflowId = event.getActivity().getTargetUuid() + "/" + rule.promptKey();

        RequirementWorkflow workflow = temporalClient.newWorkflowStub(
            RequirementWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("pilot-agent")
                .setWorkflowId(workflowId)
                .setWorkflowExecutionTimeout(Duration.ofHours(2))
                .setWorkflowIdReusePolicy(
                    WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE_FAILED_ONLY
                )
                .build()
        );

        WorkflowClient.start(workflow::run,
            event.getActivity().getTargetUuid(),
            event.getActivity().getCompanyUuid(),
            rule.promptKey()
        );
    }
}
```

#### 决策三：MCP Server 用官方 SDK 还是 LangChain4j？

**结论：Server 端用官方 MCP Java SDK，Client 端用 LangChain4j McpToolProvider。**

```java
// MCP Server 实现（对标 src/mcp/server.ts）— 用官方 SDK
@Bean
public McpSyncServer mcpServer(PmInternalMcpTools pmTools) {
    return McpServer.sync(new WebFluxSseServerTransportProvider(...))
        .serverInfo("pilot", "1.0.0")
        .tools(
            pmTools.getRequirementTool(),
            pmTools.getImportPrdTool(),
            pmTools.getCreateElaborationTool()
            // ...
        )
        .build();
}

// MCP Client（在 Agent 循环中代理外部工具）— 用 LangChain4j
@Bean
public McpToolProvider externalMcpToolProvider(
    @Value("${mcp.kb.url}") String kbUrl
) {
    McpClient kbClient = new DefaultMcpClient.Builder()
        .transport(new HttpMcpTransport.Builder()
            .sseUrl(kbUrl + "/sse")
            .build())
        .build();

    return McpToolProvider.builder()
        .mcpClients(kbClient)
        .build();
    // LangChain4j 自动发现 KB MCP server 的所有工具，注入 AiServices
}
```

#### 决策四：JGit vs 调用 git CLI

**结论：JGit 做读操作（clone、log、diff），git CLI 做写操作（commit、push、worktree）。**

原因：JGit 的 `git worktree` 和某些 `git push` 高级选项支持不完整；git CLI 更稳定，输出可解析。

```java
@Service
public class WorkspaceService {

    // 用 JGit clone（纯 Java，无外部依赖）
    public Path ensureBareClone(String repoUrl, String token, Path cachePath) {
        if (Files.exists(cachePath)) {
            // fetch 更新
            try (Git git = Git.open(cachePath.toFile())) {
                git.fetch()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("oauth2", token))
                    .call();
            }
        } else {
            Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(cachePath.toFile())
                .setBare(true)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("oauth2", token))
                .call();
        }
        return cachePath;
    }

    // 用 git CLI 创建 worktree（JGit worktree 支持不完整）
    public Path createWorktree(Path barePath, String branch, Path targetPath) {
        runCmd(barePath, "git", "worktree", "add", "-b", branch,
            targetPath.toString(), "origin/" + branch);
        return targetPath;
    }

    // 用 git CLI commit + push（更可靠）
    public String commitAndPush(Path worktreePath, String message, String branch) {
        runCmd(worktreePath, "git", "add", "-A");
        runCmd(worktreePath, "git", "commit", "-m", message);
        runCmd(worktreePath, "git", "push", "origin", branch);
        return getHeadSha(worktreePath);
    }

    private void runCmd(Path cwd, String... cmd) {
        try {
            int exit = new ProcessBuilder(cmd)
                .directory(cwd.toFile())
                .inheritIO()
                .start()
                .waitFor();
            if (exit != 0) throw new GitException("git command failed: " + Arrays.toString(cmd));
        } catch (Exception e) { throw new GitException(e); }
    }
}
```

### 15.4 完整架构图（最优方案）

```
┌─────────────────────── Pilot Java 最优架构 ────────────────────────┐
│                                                                    │
│  Web 层（Spring WebFlux）                                           │
│  ┌──────────┐  ┌──────────────┐  ┌────────────────────────────┐  │
│  │ REST API  │  │ MCP Endpoint │  │  SSE /events/{runUuid}     │  │
│  │ /api/**   │  │ POST /mcp    │  │  ← Redis Pub/Sub 跨实例    │  │
│  └──────────┘  └──────────────┘  └────────────────────────────┘  │
│         │              │                       ↑                  │
│  ─────────────────────────────────────────────────────────────    │
│  编排层                                                            │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │  WorkflowOrchestrator（Spring @EventListener + Temporal）    │  │
│  │  ActivityCreatedEvent → Rule匹配 → Temporal WorkflowClient  │  │
│  └──────────────────────┬──────────────────────────────────────┘  │
│                         │ startWorkflow()                          │
│  ─────────────────────────────────────────────────────────────    │
│  Temporal Worker 层（独立进程，可水平扩展）                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐    │
│  │ PM Workflow  │  │ Dev Workflow  │  │  L4 Workflow         │    │
│  │ Activities:  │  │ Activities:  │  │  Activities:         │    │
│  │  importPrd   │  │  plan        │  │   triggerCI          │    │
│  │  elaborate   │  │  writeTD     │  │   deployFat          │    │
│  │  validate    │  │  execute     │  │   deployProd         │    │
│  └──────┬───────┘  └──────┬───────┘  └──────────────────────┘    │
│         │                  │                                       │
│  ─────────────────────────────────────────────────────────────    │
│  Agent 执行层                                                      │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │  AgentSdkAdapter.runQuery()                                  │  │
│  │  ┌────────────────┐  ┌──────────────────────────────────┐   │  │
│  │  │  Spring AI     │  │  LangChain4j AiServices          │   │  │
│  │  │  ChatClient    │  │  + McpToolProvider                │   │  │
│  │  │  (LLM 抽象)    │  │  + MessageWindowChatMemory        │   │  │
│  │  └────────────────┘  └──────────────────────────────────┘   │  │
│  │                              │ tool calls                    │  │
│  │  ┌───────────────────────────┴────────────────────────────┐  │  │
│  │  │  工具执行层                                             │  │  │
│  │  │  FileSystemToolkit  McpToolProxy    GitLab4J + JGit    │  │  │
│  │  │  (Read/Write/Edit/  (外部 MCP       (worktree/commit/  │  │  │
│  │  │   Bash/Glob/Grep)    服务代理)       push/MR)          │  │  │
│  │  └────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────┘  │
│  ─────────────────────────────────────────────────────────────    │
│  基础设施层                                                         │
│  ┌──────────────┐  ┌──────────┐  ┌──────────┐  ┌─────────────┐   │
│  │ PostgreSQL 16│  │ Redis 7  │  │ Temporal │  │  OTel/      │   │
│  │ + Flyway     │  │ Pub/Sub  │  │ Server   │  │  Jaeger     │   │
│  └──────────────┘  └──────────┘  └──────────┘  └─────────────┘   │
└────────────────────────────────────────────────────────────────────┘
```

### 15.5 最优方案 vs 原始方案对比

| 维度 | TypeScript/Next.js（原） | Java 最优方案 | 改进点 |
|------|------------------------|-------------|--------|
| **LLM 切换** | 环境变量 + 手写路由 | Spring AI AutoConfig | 配置文件一行切换 |
| **工作流持久化** | 自写 WorkflowRun 表 + 重试循环 | Temporal.io | 自动恢复、内置重试、可视化 |
| **MCP Server** | `@modelcontextprotocol/sdk` | 官方 MCP Java SDK | 等价，官方支持 |
| **MCP Client（动态工具）** | CC 内置 | LangChain4j McpToolProvider | 等价，自动发现工具 |
| **Agent 循环** | CC 二进制 | LangChain4j AiServices | 不再依赖 CC 二进制 |
| **Git 操作** | `@gitbeaker/rest` | GitLab4J + JGit + git CLI | JVM 原生，无 Node 依赖 |
| **SSE 流式** | Next.js Route Handler | Spring WebFlux Flux | 更适合高并发 |
| **并发控制** | 手写 Semaphore + Promise | Temporal Worker + Java Semaphore | Temporal 层自动调度 |
| **可观测性** | Langfuse | OpenTelemetry Java | 标准协议，接任何后端 |

### 15.6 快速起步路线图

```
阶段一（2周）：基础框架
  1. Spring Boot 3.x + WebFlux + JPA + Flyway
  2. 认证层（Spring Security，三路：API Key / JWT / SSO）
  3. 活动流 + Spring Events 本地总线 + Redis Pub/Sub

阶段二（2周）：LLM 接入
  4. Spring AI 集成 Claude + DeepSeek（配置切换）
  5. LangChain4j AiServices + @Tool 注册文件系统工具
  6. MCP Java SDK Server + PM 内置 8 个工具

阶段三（3周）：工作流编排
  7. Temporal.io 搭建 + WorkflowOrchestrator 迁移
  8. PM Agent Workflow（importPrd/elaborate/splitFeatures）
  9. Dev Agent Workflow（plan/writeTD/execute 含并发 worktree）

阶段四（2周）：CI/CD 集成
  10. GitLab4J MR 创建 + Webhook 接收
  11. L4 流水线 Workflow（CI → 置信度 → 部署环境）
  12. SSE 前端实时事件流（AG-UI 兼容）

阶段五（1周）：生产就绪
  13. OpenTelemetry 链路追踪
  14. Temporal 可视化接入（Temporal Web UI）
  15. 多实例部署验证（Redis Pub/Sub 跨实例 SSE）
```
