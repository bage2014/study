# 工艺路线与 BOP 管理

> 创建时间：2026-09-06
> 模块：concepts

工艺路线（Routing）与工艺清单（BOP, Bill of Process）的概念、业务流程、Java 实现方案。

## 一、核心概念

### 1. 工艺路线（Routing）

定义产品生产的**工序序列**，包含每道工序的：工作中心、标准工时、工艺参数、检验要求。

```
产品 A
├── 工序 10：下料      工作中心 WC-CUT     标准工时 2min
├── 工序 20：加工      工作中心 WC-MACH    标准工时 5min
├── 工序 30：装配      工作中心 WC-ASM     标准工时 3min
└── 工序 40：包装      工作中心 WC-PACK    标准工时 1min
```

### 2. BOP（Bill of Process）

工艺清单，BOM + 工艺的关联。描述每道工序**投入哪些物料**。

```
工序 20（加工）
├── 投入：原料 X（2）
├── 投入：原料 Y（3）
└── 产出：半成品 B（1）
```

### 3. 相关概念

| 术语 | 说明 |
|------|------|
| 工序 Operation | 最小加工步骤 |
| 工作中心 Work Center | 设备/产线/人员组 |
| 标准工时 | 单件加工理论时间 |
| 工艺参数 | 温度、压力、转速等 |
| 工装 | 模具、夹具、刀具 |
| 工艺版本 | 工艺变更产生新版本 |
| EBOM→MBOM 转化 | 设计 BOM 到制造 BOM（含工序） |

## 二、数据模型

```sql
routing (工艺路线头)
├── id
├── item_id             产品
├── version             版本号
├── status              草稿/审核中/已发布/失效
├── plant_id
├── effect_from/to      有效性
└── ...

operation (工序)
├── id
├── routing_id
├── seq                 工序序号
├── name
├── work_center_id      工作中心
├── standard_time       标准工时（秒）
├── setup_time          换型时间
├── description
└── ...

operation_param (工艺参数)
├── id
├── operation_id
├── param_code
├── param_name
├── target_value        标准值
├── upper_limit         上限
├── lower_limit         下限
├── unit
└── ...

operation_material (工序投料 - BOP)
├── id
├── operation_id
├── item_id             投入物料
├── quantity            用量
├── loss_rate           损耗率
└── ...

work_center (工作中心)
├── id
├── code
├── name
├── wc_type             设备/产线/人工
├── capacity            产能（件/小时）
└── ...
```

## 三、业务流程

### 1. 工艺路线创建流程

```
1. 工艺工程师创建路由草稿
2. 维护工序序列、工作中心、工时
3. 维护每工序工艺参数
4. 维护每工序投料（BOP）
5. 提交审核
6. 审核通过 → 发布版本
7. 同步至 ERP/MES
```

### 2. 工艺变更流程

```
1. 变更申请（工艺优化/设备变更/参数调整）
2. 影响评估：
   ├─ 在制工单（已开工的不受影响）
   ├─ 未开工工单（是否切换）
   └─ 成本影响
3. 审批
4. 生成新版本，旧版本失效
5. 通知 MES/ERP
6. 未开工工单按新版本，已开工按旧版本
```

### 3. 工艺下发执行

```
工单下达 → 锁定 routing 版本 → MES 加载工艺 → 工位展示 SOP/参数
```

## 四、Java 实现

### 领域模型

```java
public class Routing extends BaseAggregate {
    private Long id;
    private Long itemId;
    private String version;
    private RoutingStatus status;
    private List<Operation> operations;
    private LocalDate effectFrom;
    private LocalDate effectTo;

    // 发布：校验完整性 → 状态流转
    public void publish() {
        Assert.notEmpty(operations, "工艺路线至少包含一道工序");
        operations.forEach(Operation::validate);
        this.status = RoutingStatus.RELEASED;
    }

    // 新增版本：基于当前版本复制
    public Routing newVersion() {
        Routing copy = this.copy();
        copy.version = nextVersion(this.version);
        copy.status = RoutingStatus.DRAFT;
        return copy;
    }
}

public class Operation extends BaseEntity {
    private Long id;
    private int seq;
    private String name;
    private Long workCenterId;
    private Duration standardTime;
    private Duration setupTime;
    private List<OperationParam> params;
    private List<OperationMaterial> materials;  // BOP

    public void validate() {
        Assert.notNull(workCenterId, "工序必须指定工作中心");
        Assert.isTrue(standardTime != null && standardTime.getSeconds() > 0,
            "标准工时必须大于 0");
    }
}
```

### 应用服务

```java
@Service
public class RoutingApplicationService {

    @Autowired private RoutingRepository routingRepo;
    @Autowired private WorkCenterRepository wcRepo;
    @Autowired private EventPublisher eventPublisher;

    // 创建草稿
    @Transactional
    public Long create(RoutingCreateCmd cmd) {
        // 校验产品存在
        itemService.checkExists(cmd.getItemId());
        Routing routing = Routing.create(cmd.getItemId());
        routingRepo.save(routing);
        return routing.getId();
    }

    // 添加工序
    @Transactional
    public void addOperation(Long routingId, OperationAddCmd cmd) {
        Routing routing = routingRepo.findOrThrow(routingId);
        Assert.isTrue(routing.isDraft(), "仅草稿状态可编辑");

        WorkCenter wc = wcRepo.findOrThrow(cmd.getWorkCenterId());
        Operation op = Operation.create(cmd, wc);
        routing.addOperation(op);
        routingRepo.save(routing);
    }

    // 维护工序投料（BOP）
    @Transactional
    public void addOperationMaterial(Long operationId, MaterialAddCmd cmd) {
        Operation op = operationRepo.findOrThrow(operationId);
        // 校验物料存在且非虚拟件
        itemService.checkUsable(cmd.getItemId());
        op.addMaterial(OperationMaterial.of(cmd));
        operationRepo.save(op);
    }

    // 发布
    @Transactional
    public void publish(Long routingId) {
        Routing routing = routingRepo.findOrThrow(routingId);
        routing.publish();
        routingRepo.save(routing);
        // 旧版本失效
        routingRepo.expirePrevious(routing.getItemId(), routing.getVersion());
        // 通知下游
        eventPublisher.publish(new RoutingReleasedEvent(routing));
    }
}
```

### 工艺版本锁定（工单下达时）

```java
// 工单下达时锁定工艺版本
public WorkOrder release(WorkOrder wo) {
    Routing routing = routingRepo.findEffective(wo.getItemId(), wo.getPlanStart());
    wo.lockRouting(routing.getId(), routing.getVersion());  // 快照
    return wo;
}
```

### BOM 展开时关联工序

```java
// MBOM 展开时按工序分组
public List<OperationMaterial> explodeByOperation(Long bomId, Long routingId, BigDecimal qty) {
    // 展开 BOM → 每个子件关联到对应工序
    // 见 tech-bom-explode，这里增加 operation_id 维度
}
```

## 五、关键设计要点

| 要点 | 说明 |
|------|------|
| 版本快照 | 工单锁定 routing 版本，变更不影响在制 |
| BOP 关联 | MBOM 行带 operation_id，用料明确到工序 |
| 工艺参数标准 | 上下限，用于 MES 防错与 SPC |
| 工作中心产能 | APS 排产依据 |
| 与 BOM 协同 | EBOM→MBOM 转化时同步生成工艺 |

## 六、相关文档

- [BOM 概述](../bom/bom-overview-20260816.md)
- [BOM 业务流程](../bom/bom-workflow-20260816.md)
- [PLM 业务流程](../plm/plm-workflow-20260816.md)
- [MES 业务流程](../mes/mes-workflow-20260816.md)
- [核心数据模型](../tech/tech-data-model-20260816.md)
