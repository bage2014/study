# 工单管理与派工

> 创建时间：2026-09-06
> 模块：concepts

工单（Work Order）管理的概念、派工流程、Java 实现方案。状态机见 [tech-workorder-state](../tech/tech-workorder-state-20260816.md)，本文聚焦管理服务与派工。

## 一、核心概念

### 1. 工单类型

| 类型 | 说明 | 触发 |
|------|------|------|
| 正常生产工单 | 量产订单 | MRP/APS |
| 试制工单 | NPI 试制 | 项目 |
| 返工工单 | 不良返工 | 不合格评审 |
| 返修工单 | 降级修复 | 不合格评审 |
| 报废工单 | 报废处理 | 不合格评审 |
| 外协工单 | 委外加工 | 产能不足 |

### 2. 工单结构

```
生产订单(ERP)
    └─ 工单(MES)
        ├─ 工单行（产品+数量）
        ├─ 工序计划（Routing 展开）
        ├─ 用料计划（BOM 展开）
        └─ 资源分配（设备/人员/工装）
```

### 3. 派工 Dispatching

将工序任务分配到具体**设备/人员/工位**的过程。

| 派工方式 | 说明 |
|---------|------|
| 自动派工 | 按规则自动分配（设备组→具体设备） |
| 手动派工 | 调度员人工指派 |
| 派工池 | 任务入池，工人抢单/领单 |

## 二、数据模型

```sql
work_order (工单头)
├── id
├── work_order_no       工单号
├── source_order_id     来源生产订单
├── item_id             产品
├── bom_id              锁定 BOM
├── routing_id          锁定工艺
├── quantity             计划数量
├── completed_qty        完工数
├── scrap_qty            报废数
├── status               CREATED/RELEASED/STARTED/COMPLETED/CLOSED
├── priority             优先级
├── plan_start/end       计划开完工
├── actual_start/end     实际开完工
└── ...

work_order_operation (工单工序)
├── id
├── work_order_id
├── operation_id         工艺工序
├── seq                  工序序号
├── work_center_id       工作中心
├── device_id            派工设备（派工后填）
├── planned_qty          计划数
├── completed_qty        完工数
├── status               PENDING/DISPATCHED/STARTED/DONE
├── plan_start/end
└── ...

dispatch_task (派工任务)
├── id
├── work_order_operation_id
├── device_id             派工到设备
├── operator_id           派工到人员
├── status                ASSIGNED/ACCEPTED/STARTED/COMPLETED
├── assigned_by/time
├── accepted_time
└── ...
```

## 三、业务流程

### 1. 工单创建与下达

```
1. 接收生产订单（ERP/APS）
2. 创建工单，锁定 BOM + Routing 版本
3. 展开工序计划（routing → work_order_operation）
4. 展开用料计划（bom → material_requirement）
5. 齐套校验（见 tech-material-kitting）
6. 下达（CREATED → RELEASED）
7. 通知车间
```

### 2. 派工流程

```
工单下达 → 工序待派工
    ├─ 自动派工（按规则）
    └─ 手动派工（调度员）
        ├─ 选设备/人员
        ├─ 生成派工任务
        ├─ 推送工位终端
        └─ 工人接单 → 开工
```

### 3. 工序流转

```
工序1 完成 → 工序2 待开工 → ... → 末工序完成 → 工单完工
```

可选：并行工序、跳序、返工回退。

### 4. 工单完工与结算

```
末工序完成 → 数量校验 → 工单完工 → 入库 → 工单关闭 → 归档
```

## 四、Java 实现

### 领域模型

```java
public class WorkOrder extends BaseAggregate {
    private Long id;
    private String workOrderNo;
    private Long itemId;
    private Long bomId;
    private Long routingId;
    private BigDecimal quantity;
    private BigDecimal completedQty;
    private BigDecimal scrapQty;
    private WorkOrderStatus status;
    private List<WorkOrderOperation> operations;

    // 下达
    public void release() {
        Assert.isTrue(status == CREATED, "仅创建状态可下达");
        Assert.notEmpty(operations, "工序不能为空");
        this.status = RELEASED;
        operations.forEach(op -> op.status(PENDING));
    }

    // 开工
    public void start() {
        Assert.isTrue(status == RELEASED, "工单未下达");
        this.status = STARTED;
        this.actualStart = LocalDateTime.now();
    }

    // 累加完工（来自报工）
    public void addCompleted(BigDecimal qty) {
        BigDecimal newTotal = completedQty.add(qty);
        Assert.isTrue(newTotal.compareTo(quantity) <= 0, "完工数超计划");
        this.completedQty = newTotal;
        if (newTotal.compareTo(quantity) == 0) {
            this.status = COMPLETED;
            this.actualEnd = LocalDateTime.now();
        }
    }
}

public class WorkOrderOperation extends BaseEntity {
    private Long id;
    private Long workOrderId;
    private int seq;
    private Long workCenterId;
    private Long deviceId;          // null=未派工
    private BigDecimal plannedQty;
    private BigDecimal completedQty;
    private OperationStatus status;

    public void dispatchTo(Long deviceId) {
        Assert.isTrue(status == PENDING, "仅待派工可派工");
        this.deviceId = deviceId;
        this.status = DISPATCHED;
    }

    public void start() {
        Assert.isTrue(status == DISPATCHED, "工序未派工");
        this.status = STARTED;
    }

    public void complete() {
        Assert.isTrue(status == STARTED, "工序未开工");
        this.status = DONE;
        // 触发下一工序可开工
    }
}
```

### 工单创建服务

```java
@Service
public class WorkOrderService {

    @Autowired private WorkOrderRepository woRepo;
    @Autowired private BomRepository bomRepo;
    @Autowired private RoutingRepository routingRepo;
    @Autowired private WorkOrderOperationRepository opRepo;
    @Autowired private MaterialRequirementService materialReqService;
    @Autowired private KittingCheckService kittingCheck;
    @Autowired private SequenceService seqService;

    @Transactional
    public Long create(WorkOrderCreateCmd cmd) {
        // 1. 查有效 BOM + Routing
        Bom bom = bomRepo.findEffective(cmd.getItemId(), cmd.getPlanStart().toLocalDate());
        Routing routing = routingRepo.findEffective(cmd.getItemId(), cmd.getPlanStart().toLocalDate());
        Assert.notNull(bom, "无有效 BOM");
        Assert.notNull(routing, "无有效工艺路线");

        // 2. 创建工单
        WorkOrder wo = WorkOrder.create(cmd, bom, routing, seqService.next("WO"));
        woRepo.save(wo);

        // 3. 展开工序
        List<WorkOrderOperation> ops = routing.getOperations().stream()
            .map(op -> WorkOrderOperation.of(wo, op))
            .collect(toList());
        opRepo.saveAll(ops);
        wo.setOperations(ops);

        // 4. 展开用料
        materialReqService.calculate(wo.getId());
        return wo.getId();
    }

    @Transactional
    public void release(Long woId) {
        WorkOrder wo = woRepo.findOrThrow(woId);
        // 齐套校验
        KittingResult kitting = kittingCheck.check(woId);
        Assert.isTrue(kitting.isReady(), "物料不齐套：" + kitting.getShortages());
        wo.release();
        woRepo.save(wo);
        eventPublisher.publish(new WorkOrderReleasedEvent(wo));
    }
}
```

### 派工服务

```java
@Service
public class DispatchService {

    @Autowired private WorkOrderOperationRepository opRepo;
    @Autowired private DeviceRepository deviceRepo;
    @Autowired private DispatchRuleEngine ruleEngine;

    // 自动派工：按规则分配设备
    @Scheduled(cron = "0 */5 * * * ?")  // 每5分钟
    public void autoDispatch() {
        List<WorkOrderOperation> pending = opRepo.findByStatus(PENDING);
        for (WorkOrderOperation op : pending) {
            // 前置工序必须完成
            if (!isPrevDone(op)) continue;
            // 规则匹配设备
            Long deviceId = ruleEngine.matchDevice(op);
            if (deviceId != null) {
                dispatch(op, deviceId, null);
            }
        }
    }

    // 手动派工
    @Transactional
    public void dispatch(Long opId, Long deviceId, Long operatorId) {
        WorkOrderOperation op = opRepo.findOrThrow(opId);
        // 校验设备可用、未停机
        Device device = deviceRepo.findOrThrow(deviceId);
        Assert.isTrue(device.isAvailable(), "设备不可用");
        op.dispatchTo(deviceId);
        opRepo.save(op);

        // 生成派工任务，推送到工位终端
        DispatchTask task = DispatchTask.of(op, deviceId, operatorId);
        taskRepo.save(task);
        terminalService.pushTask(task);
    }
}
```

### 派工规则引擎

```java
@Component
public class DispatchRuleEngine {

    public Long matchDevice(WorkOrderOperation op) {
        // 1. 候选设备：工作中心下属可用设备
        List<Device> candidates = deviceRepo
            .findByWorkCenter(op.getWorkCenterId()).stream()
            .filter(Device::isAvailable)
            .collect(toList());
        if (candidates.isEmpty()) return null;

        // 2. 按规则排序：负载最低优先
        candidates.sort(Comparator.comparing(this::currentLoad));
        return candidates.get(0).getId();
    }

    private int currentLoad(Device d) {
        return opRepo.countByDeviceAndStatus(d.getId(), STARTED);
    }
}
```

### 报工回写工序

```java
@EventListener
public void onWorkReported(WorkReportedEvent event) {
    WorkOrderOperation op = opRepo.findOrThrow(event.operationId());
    op.addCompleted(event.goodQty(), event.badQty());
    opRepo.save(op);

    if (op.isLastOperation()) {
        // 末工序完成 → 工单完工
        WorkOrder wo = woRepo.findOrThrow(op.getWorkOrderId());
        wo.addCompleted(event.goodQty());
        woRepo.save(wo);
    } else {
        // 触发下一工序可开工
        WorkOrderOperation next = opRepo.findNext(op.getWorkOrderId(), op.getSeq());
        next.enable();
        opRepo.save(next);
    }
}
```

## 五、派工池模式（抢单）

适合多技能工人、柔性产线：

```java
// 任务入池
public void pushToPool(WorkOrderOperation op) {
    DispatchPoolTask task = DispatchPoolTask.of(op);
    poolRepo.save(task);
}

// 工人领单
@Transactional
public void claim(Long taskId, Long operatorId) {
    DispatchPoolTask task = poolRepo.findOrThrow(taskId);
    task.claim(operatorId);  // 乐观锁防并发抢单
    poolRepo.save(task);
}
```

## 六、关键设计要点

| 要点 | 说明 |
|------|------|
| BOM/Routing 版本锁定 | 下达时快照，变更不影响在制 |
| 工序前置关系 | 严格顺序，前工序完成才可派工 |
| 派工灵活 | 自动+手动+抢单多模式 |
| 并发抢单 | 乐观锁防护 |
| 报工回写 | 报工驱动工序/工单状态 |
| 工单关闭归档 | 关闭后不可修改，历史可查 |

## 七、相关文档

- [工单状态机](../tech/tech-workorder-state-20260816.md)
- [报工高并发](../tech/tech-concurrency-20260816.md)
- [物料齐套与配料](../tech/tech-material-kitting-20260816.md)
- [工艺路线管理](./routing-management-20260906.md)
- [MES 业务流程](../mes/mes-workflow-20260816.md)
