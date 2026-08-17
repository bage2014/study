# 工单状态机实现

> 创建时间：2026-08-16
> 模块：tech

工单状态机的工程实现思路：状态定义、流转规则、守护机制、扩展点。

## 一、状态定义

```
[CREATED] ──下达──► [RELEASED] ──派工──► [DISPATCHED] ──开工──► [IN_PROGRESS]
                      │                       │                      │
                      │                       │                      │ 完工
                      ▼                       ▼                      ▼
                  [CANCELLED]             [PAUSED] ──恢复──► [IN_PROGRESS] ──► [COMPLETED] ──► [CLOSED]
                                              │
                                              └──终止──► [TERMINATED]
```

| 状态 | 含义 | 可执行动作 |
|------|------|-----------|
| CREATED | 已创建未下达 | 下达、删除 |
| RELEASED | 已下达 | 派工、暂停、取消 |
| DISPATCHED | 已派工 | 开工、暂停、取消 |
| IN_PROGRESS | 执行中 | 报工、暂停、终止 |
| PAUSED | 暂停 | 恢复、终止 |
| COMPLETED | 已完工 | 关闭 |
| CLOSED | 已关闭 | （终态） |
| CANCELLED | 已取消 | （终态） |
| TERMINATED | 已终止 | （终态） |

## 二、状态机实现方案对比

| 方案 | 优点 | 缺点 | 适用 |
|------|------|------|------|
| 枚举 + if/else | 简单 | 难维护、易出错 | 极简场景 |
| 状态转移表（配置） | 清晰、可配置 | 需自研框架 | 中等复杂 |
| Spring StateMachine | 标准、功能全 | 重、学习成本 | 复杂状态机 |
| Stateless4j | 轻量、 fluent | Java 库 | 推荐 |
| 自研轻量状态机 | 可控、可扩展 | 需投入 | 长期项目 |

**推荐**：自研轻量状态机（配置驱动 + 守护注解），可控且足够灵活。

## 三、自研轻量状态机设计

### 1. 状态转移表（配置）

```java
public enum WorkOrderStatus {
    CREATED, RELEASED, DISPATCHED, IN_PROGRESS, PAUSED,
    COMPLETED, CLOSED, CANCELLED, TERMINATED
}

public enum WorkOrderAction {
    RELEASE, DISPATCH, START, REPORT, PAUSE, RESUME,
    COMPLETE, CLOSE, CANCEL, TERMINATE
}
```

```java
@Configuration
public class WorkOrderStateMachine {

    private static final Map<WorkOrderStatus, Map<WorkOrderAction, WorkOrderStatus>> TRANSITIONS = Map.of(
        CREATED,   Map.of(RELEASE, RELEASED, CANCEL, CANCELLED),
        RELEASED,  Map.of(DISPATCH, DISPATCHED, PAUSE, PAUSED, CANCEL, CANCELLED),
        DISPATCHED,Map.of(START, IN_PROGRESS, PAUSE, PAUSED, CANCEL, CANCELLED),
        IN_PROGRESS,Map.of(PAUSE, PAUSED, COMPLETE, COMPLETED, TERMINATE, TERMINATED),
        PAUSED,    Map.of(RESUME, IN_PROGRESS, TERMINATE, TERMINATED),
        COMPLETED, Map.of(CLOSE, CLOSED)
    );

    public static WorkOrderStatus transit(WorkOrderStatus current, WorkOrderAction action) {
        WorkOrderStatus next = TRANSITIONS.getOrDefault(current, Map.of()).get(action);
        if (next == null) {
            throw new IllegalStateTransitionException(
                "非法状态流转: " + current + " + " + action);
        }
        return next;
    }
}
```

### 2. 动作处理器（Action Handler）

每个动作一个 Handler，含：前置校验、状态流转、副作用、事件发布。

```java
public interface WorkOrderActionHandler {
    WorkOrderAction action();
    void handle(WorkOrderContext ctx);
}

@Component
public class StartHandler implements WorkOrderActionHandler {

    @Override public WorkOrderAction action() { return START; }

    @Transactional
    @Override public void handle(WorkOrderContext ctx) {
        WorkOrder wo = ctx.getWorkOrder();

        // 1. 前置校验
        Assert.isTrue(wo.getStatus() == DISPATCHED, "工单未派工，无法开工");
        Assert.isTrue(wo.getWorkCenter().isAvailable(), "工作中心不可用");
        checkMaterialReady(wo);  // 齐套校验

        // 2. 状态流转（乐观锁）
        WorkOrderStatus next = WorkOrderStateMachine.transit(wo.getStatus(), START);
        int rows = workOrderMapper.updateStatus(wo.getId(), wo.getStatus(), next, wo.getVersion());
        Assert.isTrue(rows == 1, "工单状态已变更，请刷新");

        // 3. 副作用
        wo.setActualStart(LocalDateTime.now());
        wo.setStatus(next);
        workCenterService.occupy(wo.getWorkCenterId(), wo.getId());

        // 4. 发布领域事件
        eventPublisher.publish(new WorkOrderStartedEvent(wo.getId()));
    }
}
```

### 3. 调度入口

```java
@Service
public class WorkOrderApplicationService {

    private final Map<WorkOrderAction, WorkOrderActionHandler> handlers;

    public WorkOrderApplicationService(List<WorkOrderActionHandler> list) {
        handlers = list.stream().collect(toMap(WorkOrderActionHandler::action, h -> h));
    }

    @Transactional
    public void execute(Long workOrderId, WorkOrderAction action, WorkOrderCommand cmd) {
        WorkOrder wo = workOrderRepository.findOrThrow(workOrderId);
        WorkOrderContext ctx = new WorkOrderContext(wo, cmd);
        handlers.get(action).handle(ctx);
    }
}
```

## 四、并发与一致性

### 问题
- 多人同时操作同一工单 → 状态错乱。
- 报工并发 → 完工数超额。

### 方案

**1. 乐观锁（推荐）**

```sql
UPDATE work_order
SET status = ?, version = version + 1, update_by = ?
WHERE id = ? AND status = ? AND version = ?
```

影响行数 = 0 即状态已变，抛异常让前端刷新重试。

**2. 状态前置校验**

SQL 中带 `AND status = ?`，DB 层兜底防并发。

**3. 报工数量防超**

```sql
UPDATE work_order
SET completed_qty = completed_qty + #{delta}
WHERE id = #{id}
  AND completed_qty + #{delta} <= quantity + #{tolerance}
```

DB 层校验，避免应用层竞态。

**4. 分布式锁（慎用）**

仅用于跨资源强一致场景（如设备占用），常规状态流转用乐观锁即可。

## 五、状态机守护

### 注解守护（防绕过）

```java
@Target(METHOD) @Retention(RUNTIME)
public @interface RequireStatus {
    WorkOrderStatus[] value();
    WorkOrderAction action();
}

@RequireStatus(value = {DISPATCHED}, action = START)
public void start(Long id) { ... }
```

AOP 拦截：进入方法前校验状态，避免业务代码直接改 status 字段。

### 禁止直接 set status
- 领域模型 `WorkOrder` 不暴露 `setStatus`。
- 状态变更只能通过 `ActionHandler` 走状态机。
- DB 更新只能通过专用 SQL（带状态前置条件）。

## 六、状态变更历史

```sql
work_order_status_log
├── id
├── work_order_id
├── from_status
├── to_status
├── action
├── operator_id
├── remark
├── create_time
```

每次流转记一条，便于审计与排查"工单怎么变成这个状态"。

## 七、子状态机：工序任务

工单下有多个工序任务，每个工序任务也有自己的状态机：

```
[待开始] → [进行中] → [已完工]
              │
              └─[已暂停]
```

- 工单状态与工序任务状态联动。
- 所有工序完工 → 触发工单 COMPLETED。
- 工单 PAUSED → 所有进行中工序 PAUSED。

## 八、扩展点

| 扩展点 | 用途 |
|--------|------|
| `ActionHandler` | 新增动作（如返工、合并） |
| 状态扩展 | 增加 PENDING_REVIEW 等中间态 |
| 事件订阅 | 状态变更触发下游（MQ 通知 ERP） |
| 校验规则 | 注入自定义前置校验 |

## 九、事件发布

```java
public sealed interface WorkOrderEvent permits
    WorkOrderReleasedEvent, WorkOrderStartedEvent, WorkOrderCompletedEvent, ... {
    Long workOrderId();
}
```

- 同步事件：同事务，更新 WIP 等。
- 异步事件：MQ，通知 ERP/WMS。

## 十、常见陷阱

| 陷阱 | 对策 |
|------|------|
| 直接 set status 绕过状态机 | 领域模型不暴露 setter |
| 并发更新覆盖 | 乐观锁 + SQL 前置条件 |
| 终态被复活 | 终态不在转移表中，天然拒绝 |
| 事件同事务失败回滚 | 同步事件同事务；跨系统走 MQ |
| 子状态与父状态不一致 | 子状态变更联动父状态，事务包裹 |

## 十一、相关文档

- [框架与分层](./tech-framework-20260816.md)
- [核心数据模型](./tech-data-model-20260816.md)
- [报工高并发](./tech-concurrency-20260816.md)
- [MES 业务流程](../mes/mes-workflow-20260816.md)
