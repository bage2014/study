# 设备管理与 TPM

> 创建时间：2026-09-06
> 模块：concepts

设备资产全生命周期管理、TPM（全员生产维护）、点检/保养/维修的概念、流程、Java 实现。

## 一、核心概念

### 1. 设备管理范畴

| 范畴 | 说明 |
|------|------|
| 资产管理 | 设备档案、位置、价值 |
| 运行监控 | 状态、参数、OEE（见 tech-oee） |
| 点检/保养 | 定期巡检、预防性维护 |
| 维修管理 | 故障报修、工单、停机记录 |
| 备件管理 | 备件库存、更换记录 |
| TPM | 全员生产维护体系 |

### 2. TPM 八大支柱

| 支柱 | 说明 |
|------|------|
| 自主保全 | 操作工自主清洁/点检 |
| 专业保全 | 计划保养/维修 |
| 个别改善 | 针对瓶颈改善 |
| 质量保全 | 质量问题根因设备改善 |
| 初期管理 | 新设备规划/导入 |
| 事务改善 | 间接部门效率化 |
| 环境安全 | 5S/安全/环境 |
| 人才育成 | 技能培训/多能工 |

### 3. 维护策略

| 策略 | 说明 | 触发 |
|------|------|------|
| 事后维修 BM | 坏了再修 | 故障 |
| 预防维护 PM | 定期保养 | 时间/次数 |
| 预测维护 PdM | 状态监测维护 | 振动/温度阈值 |
| 改善维护 CM | 持续改进 | 数据分析 |

## 二、数据模型

```sql
equipment (设备档案)
├── id
├── code                设备编码
├── name
├── equipment_type      类型
├── model               型号
├── serial_no           出厂序列号
├── plant_id/workshop_id
├── work_center_id      关联工作中心
├── install_date        投用日期
├── status              运行/停机/维修/报废
├── supplier_id
├── purchase_date
├── purchase_value      原值
└── ...

maintenance_plan (保养计划)
├── id
├── equipment_id
├── plan_type           DAILY/WEEKLY/MONTHLY/QUARTERLY
├── cycle_interval      周期（天/次）
├── trigger_type        TIME/USAGE/CONDITION
├── items               保养项（JSON）
├── next_date           下次保养日期
├── enabled
└── ...

maintenance_order (保养/维修工单)
├── id
├── order_no
├── type                INSPECTION/PM/REPAIR
├── equipment_id
├── status              PLANNED/ASSIGNED/IN_PROGRESS/DONE/CANCELED
├── priority
├── plan_date
├── actual_start/end
├── downtime_minutes   停机时长
├── cost
└── ...

maintenance_item (保养项执行记录)
├── id
├── order_id
├── item_name
├── result              OK/NG
├── actual_value
├── remark
└── ...

spare_part (备件)
├── id
├── part_no
├── name
├── specification
├── equipment_type_id    适用设备类型
├── stock_qty            库存
├── safety_stock         安全库存
└── ...

spare_part_usage (备件消耗记录)
├── id
├── maintenance_order_id
├── spare_part_id
├── quantity
└── ...
```

## 三、业务流程

### 1. 点检流程

```
1. 系统生成点检任务（按计划）
2. 推送工位终端
3. 操作工执行点检，录入结果
4. 异常 → 触发维修工单
5. 正常 → 关闭任务
```

### 2. 保养流程（PM）

```
1. 计划触发（时间/次数）
2. 生成保养工单
3. 派工给维修人员
4. 执行保养项
5. 记录结果/消耗备件
6. 完成并更新下次保养日期
```

### 3. 维修流程

```
1. 故障报修（自动/人工）
2. 生成维修工单
3. 设备停机记录
4. 维修人员派工
5. 故障诊断/处理
6. 更换备件
7. 试机验证
8. 恢复运行
9. 根因分析/8D
```

### 4. 备件管理

```
备件库存 < 安全库存 → 触发采购
更换备件 → 扣减库存 + 记录到设备履历
```

## 四、Java 实现

### 领域模型

```java
public class Equipment extends BaseAggregate {
    private Long id;
    private String code;
    private EquipmentStatus status;
    private List<MaintenancePlan> plans;

    public void reportFault(FaultReport report) {
        this.status = EquipmentStatus.DOWN;
        // 生成维修工单
    }

    public void recover() {
        this.status = EquipmentStatus.RUNNING;
    }
}

public class MaintenanceOrder extends BaseAggregate {
    private Long id;
    private String orderNo;
    private MaintenanceType type;
    private Long equipmentId;
    private MaintenanceStatus status;
    private List<MaintenanceItem> items;
    private Integer downtimeMinutes;

    public void assign(Long maintainerId) {
        Assert.isTrue(status == PLANNED, "仅计划状态可派工");
        this.status = ASSIGNED;
    }

    public void start() {
        Assert.isTrue(status == ASSIGNED, "工单未派工");
        this.status = IN_PROGRESS;
        this.actualStart = LocalDateTime.now();
    }

    public void complete(List<MaintenanceItemResult> results) {
        Assert.isTrue(status == IN_PROGRESS, "工单未开始");
        // 校验所有保养项已完成
        applyResults(results);
        this.status = DONE;
        this.actualEnd = LocalDateTime.now();
    }
}
```

### 保养计划调度

```java
@Service
public class MaintenancePlanScheduler {

    @Autowired private MaintenancePlanRepository planRepo;
    @Autowired private MaintenanceOrderService orderService;

    // 每日生成保养/点检工单
    @Scheduled(cron = "0 0 6 * * ?")
    public void generateOrders() {
        LocalDate today = LocalDate.now();
        List<MaintenancePlan> due = planRepo.findDue(today);
        for (MaintenancePlan plan : due) {
            orderService.create(plan);
            // 更新下次保养日期
            plan.updateNextDate();
            planRepo.save(plan);
        }
    }
}
```

### 维修工单服务

```java
@Service
public class MaintenanceOrderService {

    @Autowired private MaintenanceOrderRepository orderRepo;
    @Autowired private EquipmentRepository equipmentRepo;
    @Autowired private SparePartService sparePartService;
    @Autowired private EventPublisher eventPublisher;

    @Transactional
    public Long createFromPlan(MaintenancePlan plan) {
        MaintenanceOrder order = MaintenanceOrder.fromPlan(plan);
        orderRepo.save(order);
        return order.getId();
    }

    // 故障报修
    @Transactional
    public Long createFromFault(FaultReport report) {
        Equipment eq = equipmentRepo.findOrThrow(report.getEquipmentId());
        eq.reportFault(report);  // 设备置停机
        equipmentRepo.save(eq);

        MaintenanceOrder order = MaintenanceOrder.fromFault(report);
        order.setPriority(HIGH);
        orderRepo.save(order);

        // 通知维修人员
        eventPublisher.publish(new FaultReportedEvent(order));
        return order.getId();
    }

    // 完成 + 消耗备件
    @Transactional
    public void complete(Long orderId, CompleteCmd cmd) {
        MaintenanceOrder order = orderRepo.findOrThrow(orderId);
        order.complete(cmd.getResults());
        // 消耗备件
        for (SparePartUsage usage : cmd.getParts()) {
            sparePartService.consume(usage);
        }
        orderRepo.save(order);

        // 设备恢复
        Equipment eq = equipmentRepo.findOrThrow(order.getEquipmentId());
        eq.recover();
        equipmentRepo.save(eq);

        eventPublisher.publish(new MaintenanceCompletedEvent(order));
    }
}
```

### 备件管理

```java
@Service
public class SparePartService {

    @Autowired private SparePartRepository partRepo;
    @Autowired private StockLedgerService stockLedger;

    // 消耗备件
    @Transactional
    public void consume(SparePartUsage usage) {
        SparePart part = partRepo.findOrThrow(usage.getSparePartId());
        // 扣库存（走库存账本）
        stockLedger.record(StockTxnCmd.outbound(
            part.getItemId(), usage.getQuantity(), SPARE_PART));
        part.decStock(usage.getQuantity());
        partRepo.save(part);
        // 低于安全库存 → 触发采购申请
        if (part.needReplenish()) {
            purchaseService.createRequisition(part);
        }
    }
}
```

### 设备履历

```java
// 设备完整履历：安装/点检/保养/维修/备件更换
public EquipmentHistory getHistory(Long equipmentId, LocalDate from, LocalDate to) {
    return EquipmentHistory.builder()
        .equipment(equipmentRepo.findOrThrow(equipmentId))
        .maintenanceOrders(orderRepo.findByEquipment(equipmentId, from, to))
        .spareParts(sparePartUsageRepo.findByEquipment(equipmentId, from, to))
        .oeeRecords(oeeRepo.findByEquipment(equipmentId, from, to))
        .build();
}
```

## 五、关键设计要点

| 要点 | 说明 |
|------|------|
| 计划驱动保养 | 定时生成，避免遗漏 |
| 故障闭环 | 报修→维修→验证→根因 |
| 备件库存联动 | 消耗自动扣库存、低于安全线触发采购 |
| 设备履历完整 | 全生命周期可追溯 |
| 与 OEE 集成 | 停机记录驱动 OEE 计算 |
| 与工单联动 | 设备故障触发工单异常处理 |

## 六、相关文档

- [设备 OEE 计算](../tech/tech-oee-20260816.md)
- [设备数据采集](../tech/tech-iot-collection-20260816.md)
- [库存事务与账本](./inventory-ledger-20260906.md)
- [实时看板](../tech/tech-dashboard-20260816.md)
