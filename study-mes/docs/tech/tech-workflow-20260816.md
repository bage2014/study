# 审批工作流实现

> 创建时间：2026-08-16
> 模块：tech

审批工作流的工程实现思路：流程建模、引擎选型、流转控制、与业务集成。

## 一、工作流需求

### 典型审批场景
- BOM 发布审核
- 工程变更（ECN）
- 工单下达审批
- 质量异常处理
- 请假/报销

### 通用要素

| 要素 | 说明 |
|------|------|
| 流程定义 | 节点 + 连线 + 条件 |
| 节点类型 | 任务节点、网关、开始/结束 |
| 处理人 | 用户/角色/部门/上级/发起人/动态规则 |
| 动作 | 同意/驳回/转办/加签/会签/撤回 |
| 表单 | 业务数据展示与录入 |
| 超时 | 提醒、自动升级、自动通过 |
| 历史 | 审批轨迹 |

## 二、方案选型

| 方案 | 优点 | 缺点 | 适用 |
|------|------|------|------|
| Flowable / Camunda | 功能全、BPMN 标准、可视化 | 重、学习成本 | 复杂流程 |
| 自研轻量工作流 | 可控、简单 | 功能有限 | 流程简单、规则明确 |
| 状态机 + 审批表 | 极简 | 无流程编排 | 极简审批 |

**推荐**：
- 流程多变、需可视化建模 → Flowable。
- 流程固定、规则明确 → 自研轻量工作流。

## 三、自研轻量工作流设计

### 1. 流程定义（配置化）

```sql
flow_definition (流程定义)
├── id
├── code              流程编码（如 BOM_RELEASE）
├── name
├── biz_type          业务类型
├── version
└── status

flow_node (节点)
├── id
├── definition_id
├── node_code
├── name
├── node_type         START / TASK / GATEWAY / END
├── assignee_type     USER / ROLE / DEPT / LEADER / INITIATOR / EXPRESSION
├── assignee_expr     表达式（如 role:ENGINEERING_MANAGER）
├── multi_instance    NONE / PARALLEL(会签) / SEQUENTIAL(或签)
├── skip_condition    跳过条件（SpEL）
└── seq

flow_transition (连线)
├── id
├── definition_id
├── from_node
├── to_node
├── action           AGREE / REJECT / ...
└── condition        SpEL 表达式
```

### 2. 流程实例

```sql
flow_instance (流程实例)
├── id
├── definition_id
├── biz_type
├── biz_id            业务单据 ID
├── title
├── initiator_id
├── status            RUNNING / COMPLETED / TERMINATED / WITHDRAWN
├── current_node
├── start_time
└── end_time

flow_task (待办任务)
├── id
├── instance_id
├── node_id
├── assignee_id      当前处理人
├── status           PENDING / COMPLETED / DELEGATED
├── create_time
├── due_time         超时时间
└── ...

flow_history (审批历史)
├── id
├── instance_id
├── node_id
├── assignee_id
├── action           AGREE / REJECT / DELEGATE
├── comment
├── operator_id
├── operate_time
└── ...
```

### 3. 引擎核心

```java
@Service
public class FlowEngine {

    @Transactional
    public Long start(String flowCode, FlowBizRef bizRef, Long initiatorId) {
        FlowDefinition def = defRepository.findLatest(flowCode);
        FlowNode startNode = def.findStartNode();

        FlowInstance instance = new FlowInstance(def, bizRef, initiatorId);
        instanceRepository.save(instance);

        // 推进到第一个任务节点
        advanceToNext(instance, startNode);
        return instance.getId();
    }

    @Transactional
    public void approve(Long taskId, String action, String comment) {
        FlowTask task = taskRepository.findOrThrow(taskId);
        Assert.isTrue(task.isPending(), "任务已处理");
        Long operatorId = SecurityUtils.currentUserId();
        Assert.isTrue(task.canHandle(operatorId), "非任务处理人");

        // 记录历史
        historyRepository.save(task.toHistory(action, comment, operatorId));
        task.complete();
        taskRepository.save(task);

        // 会签场景：等所有人处理完
        FlowInstance instance = task.getInstance();
        if (instance.currentNode().isMultiInstance()
            && !allTasksCompleted(instance)) {
            return;
        }

        // 根据动作 + 条件找下一节点
        FlowNode next = findNextNode(instance, action);
        if (next.isEnd()) {
            completeInstance(instance);
            publishBizEvent(instance, FlowEventType.COMPLETED);
        } else {
            advanceToNext(instance, next);
        }
    }

    private FlowNode findNextNode(FlowInstance instance, String action) {
        List<FlowTransition> transitions = defRepository
            .findTransitions(instance.getDefinitionId(), instance.getCurrentNode(), action);
        for (FlowTransition t : transitions) {
            if (evaluateCondition(t.getCondition(), instance)) {
                return t.getToNode();
            }
        }
        throw new FlowException("找不到下一节点");
    }

    private boolean evaluateCondition(String expr, FlowInstance instance) {
        if (StringUtils.isBlank(expr)) return true;
        // SpEL 求值，上下文含业务变量
        return spelEvaluator.eval(expr, instance.getVariables());
    }
}
```

### 4. 处理人解析

```java
public class AssigneeResolver {
    public List<Long> resolve(FlowNode node, FlowInstance instance) {
        return switch (node.getAssigneeType()) {
            case USER -> List.of(Long.valueOf(node.getAssigneeExpr()));
            case ROLE -> userRepository.findByRoleCode(node.getAssigneeExpr());
            case DEPT -> userRepository.findByDeptId(instance.getInitiatorDept());
            case LEADER -> List.of(orgService.findLeader(instance.getInitiatorId()));
            case INITIATOR -> List.of(instance.getInitiatorId());
            case EXPRESSION -> spelResolver.resolveUsers(node.getAssigneeExpr(), instance);
        };
    }
}
```

### 5. 会签 / 或签

```java
// 会签：当前节点为所有人各生成一个任务，全部 AGREE 才推进
public void createMultiTasks(FlowNode node, FlowInstance instance) {
    List<Long> assignees = assigneeResolver.resolve(node, instance);
    for (Long uid : assignees) {
        taskRepository.save(new FlowTask(instance, node, uid));
    }
}

// 或签：任一人处理即推进（其余任务自动取消）
public void approveOrSign(FlowTask task) {
    task.complete();
    List<FlowTask> others = taskRepository.findPendingByNode(task.getInstanceId(), task.getNodeId());
    others.forEach(t -> { t.cancel(); taskRepository.save(t); });
    // 推进下一节点
}
```

## 四、与业务集成

### 启动流程

```java
@Service
public class BomApplicationService {

    @Autowired private FlowEngine flowEngine;

    @Transactional
    public void submitForApproval(Long bomId) {
        Bom bom = bomRepository.findOrThrow(bomId);
        bom.submit();  // 状态：草稿 → 审核中
        bomRepository.save(bom);

        flowEngine.start("BOM_RELEASE",
            new FlowBizRef("BOM", bomId), SecurityUtils.currentUserId());
    }
}
```

### 监听流程完成

```java
@Component
public class BomFlowHandler {

    @EventListener
    public void onFlowCompleted(FlowCompletedEvent event) {
        if (!event.getBizType().equals("BOM")) return;
        Bom bom = bomRepository.findOrThrow(event.getBizId());
        bom.approve();  // 审核中 → 已发布
        bomRepository.save(bom);
        // 同步 ERP / MES
        eventPublisher.publish(new BomReleasedEvent(bom));
    }

    @EventListener
    public void onFlowRejected(FlowRejectedEvent event) {
        if (!event.getBizType().equals("BOM")) return;
        Bom bom = bomRepository.findOrThrow(event.getBizId());
        bom.reject();  // 审核中 → 草稿
        bomRepository.save(bom);
    }
}
```

## 五、驳回策略

| 策略 | 说明 |
|------|------|
| 驳回上一节点 | 回到上一个任务节点 |
| 驳回发起人 | 回到开始节点 |
| 驳回指定节点 | 用户选择回到哪个节点 |
| 驳回重提 | 回到发起人，修改后重新走全流程 |

实现：在 `findNextNode` 时根据 action=REJECT 找到目标节点（连线配置）。

## 六、超时处理

```java
@Scheduled(cron = "0 */10 * * * ?")
public void checkTimeout() {
    List<FlowTask> timeoutTasks = taskRepository.findTimeout(LocalDateTime.now());
    for (FlowTask task : timeoutTasks) {
        // 策略1：提醒
        notifyService.notify(task.getAssigneeId(), "任务超时");
        // 策略2：自动升级给上级
        Long leader = orgService.findLeader(task.getAssigneeId());
        task.delegateTo(leader);
        taskRepository.save(task);
        // 策略3：自动通过（慎用）
    }
}
```

## 七、撤回

发起人在下一节点处理前可撤回：

```java
public void withdraw(Long instanceId, Long initiatorId) {
    FlowInstance instance = instanceRepository.findOrThrow(instanceId);
    Assert.isTrue(instance.getInitiatorId().equals(initiatorId), "非发起人");
    Assert.isTrue(instance.isFirstTaskPending(), "已处理无法撤回");
    instance.terminate(WITHDRAWN);
    // 业务回退
    publishBizEvent(instance, FlowEventType.WITHDRAWN);
}
```

## 八、Flowable 集成要点

若用 Flowable：

### 流程定义
- 用 Flowable Modeler 建模，导出 BPMN XML。
- 部署到引擎。

### 与业务关联
```java
// 启动流程，传业务变量
ProcessInstance pi = runtimeService.startProcessInstanceByKey(
    "BOM_RELEASE", Map.of("bomId", bomId, "initiator", userId));

// 完成任务
taskService.complete(taskId, Map.of("action", "agree"));
```

### 监听节点事件
```java
@Component
public class BomFlowListener implements TaskListener {
    @Override
    public void notify(DelegateTask task) {
        if ("end".equals(task.getEventName())) {
            // 业务处理
        }
    }
}
```

## 九、设计要点

| 要点 | 说明 |
|------|------|
| 业务与流程解耦 | 流程引擎只管流转，业务状态由事件驱动 |
| 流程可配置 | 节点/连线配置化，不改代码 |
| 历史完整 | 每步动作记录，可审计 |
| 处理人动态 | 表达式支持，避免硬编码 |
| 幂等 | 任务重复提交安全 |
| 异常补偿 | 流程异常时业务可回退 |

## 十、常见陷阱

| 陷阱 | 对策 |
|------|------|
| 流程与业务状态不同步 | 事件驱动业务状态变更 |
| 会签漏人 | 创建任务后校验数量 |
| 驳回逻辑混乱 | 连线明确配置驳回目标 |
| 超时任务堆积 | 定时清理 + 升级 |
| 历史表膨胀 | 定期归档 |

## 十一、相关文档

- [框架与分层](./tech-framework-20260816.md)
- [权限实现](./tech-permission-20260816.md)
- [BOM 业务流程](../bom/bom-workflow-20260816.md)
- [PLM 业务流程](../plm/plm-workflow-20260816.md)
