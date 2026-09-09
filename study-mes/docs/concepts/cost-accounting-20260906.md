# 生产成本核算

> 创建时间：2026-09-06
> 模块：concepts

生产成本归集、分配、核算的概念、流程、Java 实现。MES 侧重成本归集，ERP 侧重财务核算。

## 一、核心概念

### 1. 成本构成

```
生产成本 = 直接材料 + 直接人工 + 制造费用
                │         │         │
                └─ BOM用料   └─ 工时   └─ 设备+辅料+折旧+...
```

### 2. 成本方法

| 方法 | 说明 | 适用 |
|------|------|------|
| 分批法（订单法） | 按工单归集 | 离散制造（按单生产） |
| 分步法 | 按工序/车间归集 | 流程制造（连续生产） |
| 作业成本法 ABC | 按作业动因分配 | 间接费用大 |
| 标准成本法 | 标准价±差异 | 通用 |
| 实际成本法 | 实际发生额 | 精细核算 |

### 3. 标准成本 vs 实际成本

| 项 | 标准 | 实际 |
|----|------|------|
| 价 | BOM 用量 × 标准单价 | 实际采购价 |
| 量 | 标准 BOM 用量 | 实际消耗量 |
| 工时 | 标准工时 | 实际工时 |
| 差异 | 量差 + 价差 | - |

### 4. 在制品 WIP

```
期初 WIP + 本期投入 - 本期完工 = 期末 WIP
```

## 二、数据模型

```sql
cost_standard (标准成本)
├── id
├── item_id
├── plant_id
├── material_cost       标准材料成本
├── labor_cost          标准人工成本
├── overhead_cost       标准制造费用
├── total_cost          标准总成本
├── effect_from/to
└── ...

cost_actual (实际成本归集 - 按工单)
├── id
├── work_order_id
├── item_id
├── quantity            完工数量
├── material_cost       实际材料
├── labor_cost          实际人工
├── overhead_cost       实际制造费用
├── scrap_cost          报废成本
├── rework_cost         返工成本
├── total_cost
├── period              核算期间
└── ...

cost_element (成本要素明细)
├── id
├── cost_actual_id
├── element_type        MATERIAL/LABOR/OVERHEAD
├── item_id             材料/工序/费用项
├── quantity
├── unit_price
├── amount
├── txn_time
└── ...

wip_account (在制品账)
├── work_order_id
├── period
├── beginning_wip       期初
├── input_cost           本期投入
├── complete_cost        本期完工转出
├── ending_wip           期末
└── ...
```

## 三、业务流程

### 1. 标准成本制定

```
1. 物料标准价（采购价/历史价）
2. BOM 展开 → 标准材料成本
3. 工艺路线 → 标准工时 → 标准人工成本
4. 制造费用分摊率 → 标准制造费用
5. 审批发布
```

### 2. 实际成本归集

```
工单执行过程实时归集：
  ├─ 领料 → 材料成本
  ├─ 报工 → 人工成本（工时 × 工资率）
  ├─ 设备运行 → 设备成本（工时 × 设备费率）
  ├─ 辅料消耗 → 制造费用
  ├─ 返工 → 返工成本
  └─ 报废 → 报废成本
```

### 3. 工单完工结案

```
1. 归集所有成本要素
2. 计算总成本
3. 完工数量分摊
4. 差异分析（实际 vs 标准）
5. 入库 → 库存价值
6. 期末 WIP 调整
```

### 4. 期末结账

```
1. 关闭期间
2. WIP 重新计算
3. 差异分摊到库存/销售
4. 生成成本报表
```

## 四、Java 实现

### 标准成本管理

```java
@Service
public class StandardCostService {

    @Autowired private StandardCostRepository costRepo;
    @Autowired private BomExploder bomExploder;
    @Autowired private RoutingRepository routingRepo;

    // 计算产品标准成本（BOM+工艺展开累加）
    public StandardCost calculate(Long itemId, LocalDate date) {
        // 1. 材料成本：BOM 展开 × 子件标准价
        List<FlatBomEntry> flat = bomExploder.explode(itemId, BigDecimal.ONE, date);
        BigDecimal material = flat.stream()
            .map(e -> e.getTotalQuantity().multiply(getStdPrice(e.getItemId())))
            .reduce(ZERO, BigDecimal::add);

        // 2. 人工成本：工序工时 × 工资率
        Routing routing = routingRepo.findEffective(itemId, date);
        BigDecimal labor = routing.getOperations().stream()
            .map(op -> calcLaborCost(op))
            .reduce(ZERO, BigDecimal::add);

        // 3. 制造费用：工时 × 分摊率
        BigDecimal overhead = routing.getOperations().stream()
            .map(op -> calcOverheadCost(op))
            .reduce(ZERO, BigDecimal::add);

        return StandardCost.of(material, labor, overhead);
    }

    private BigDecimal calcLaborCost(Operation op) {
        BigDecimal hours = BigDecimal.valueOf(op.getStandardTime().toSeconds())
            .divide(BigDecimal.valueOf(3600), 4, HALF_UP);
        return hours.multiply(workCenterRate.getLaborRate(op.getWorkCenterId()));
    }
}
```

### 实际成本归集

```java
@Service
public class CostCollectionService {

    @Autowired private CostActualRepository costRepo;
    @Autowired private CostElementRepository elementRepo;

    // 领料 → 归集材料成本
    @EventListener
    public void onMaterialIssued(MaterialIssuedEvent event) {
        if (event.getWorkOrderId() == null) return;
        addElement(event.getWorkOrderId(), MATERIAL, event.getItemId(),
            event.getQuantity(), event.getUnitPrice());
    }

    // 报工 → 归集人工+设备成本
    @EventListener
    public void onWorkReported(WorkReportedEvent event) {
        // 人工成本 = 实际工时 × 工资率
        BigDecimal labor = event.getActualHours().multiply(event.getLaborRate());
        addElement(event.getWorkOrderId(), LABOR, null, event.getActualHours(), labor);

        // 设备成本 = 设备工时 × 设备费率
        BigDecimal machine = event.getMachineHours().multiply(event.getMachineRate());
        addElement(event.getWorkOrderId(), OVERHEAD, null, event.getMachineHours(), machine);
    }

    @Transactional
    private void addElement(Long woId, CostElementType type, Long itemId,
                            BigDecimal qty, BigDecimal amount) {
        CostActual cost = costRepo.findOrCreate(woId);
        cost.addElement(type, amount);
        costRepo.save(cost);

        CostElement element = CostElement.of(cost.getId(), type, itemId, qty, amount);
        elementRepo.save(element);
    }
}
```

### 工单结案核算

```java
@Service
public class CostSettlementService {

    @Autowired private CostActualRepository costRepo;
    @Autowired private StandardCostService stdCostService;
    @Autowired private InventoryValueService inventoryValueService;

    // 工单完工结案
    @Transactional
    public void settleWorkOrder(Long workOrderId) {
        WorkOrder wo = woRepo.findOrThrow(workOrderId);
        CostActual actual = costRepo.findOrThrow(workOrderId);

        // 1. 完工数量分摊成本
        BigDecimal completedQty = wo.getCompletedQty();
        BigDecimal unitCost = actual.getTotalCost().divide(completedQty, 4, HALF_UP);

        // 2. 入库 → 库存价值
        inventoryValueService.recordInbound(
            wo.getItemId(), completedQty, unitCost);

        // 3. 差异分析（实际 vs 标准）
        StandardCost std = stdCostService.find(wo.getItemId());
        CostVariance variance = CostVariance.of(actual, std, completedQty);
        varianceRepo.save(variance);

        actual.setStatus(SETTLED);
        costRepo.save(actual);
    }

    // 差异构成
    // 价差 = (实际单价 - 标准单价) × 实际量
    // 量差 = (实际量 - 标准量) × 标准单价
}
```

### WIP 账务

```java
@Service
public class WipAccountService {

    @Transactional
    public void recordInput(Long woId, BigDecimal cost, String period) {
        WipAccount wip = wipRepo.findOrCreate(woId, period);
        wip.addInput(cost);
        wipRepo.save(wip);
    }

    @Transactional
    public void recordComplete(Long woId, BigDecimal cost, String period) {
        WipAccount wip = wipRepo.findOrCreate(woId, period);
        wip.addComplete(cost);
        wipRepo.save(wip);
    }

    // 期末结账：算期末 WIP
    public WipResult settlePeriod(String period) {
        List<WipAccount> accounts = wipRepo.findByPeriod(period);
        // 期初 + 投入 - 完工 = 期末
        accounts.forEach(a -> {
            a.setEndingWip(a.getBeginningWip().add(a.getInputCost()).subtract(a.getCompleteCost()));
            wipRepo.save(a);
        });
    }
}
```

### 成本查询

```java
// 工单成本明细
public WorkOrderCostDetail getDetail(Long woId) {
    CostActual actual = costRepo.findOrThrow(woId);
    List<CostElement> elements = elementRepo.findByCost(actual.getId());
    StandardCost std = stdCostService.find(actual.getItemId());

    return WorkOrderCostDetail.builder()
        .actual(actual)
        .elements(elements)
        .standard(std)
        .variance(CostVariance.of(actual, std, actual.getQuantity()))
        .build();
}
```

## 五、差异分析

| 差异 | 公式 | 责任 |
|------|------|------|
| 材料价差 | (实际价-标准价)×实际量 | 采购 |
| 材料量差 | (实际量-标准量)×标准价 | 生产 |
| 人工价差 | (实际工资率-标准)×实际工时 | HR |
| 人工效率差 | (实际工时-标准工时)×标准工资率 | 生产 |
| 制造费用差异 | 实际-标准 | 车间管理 |

## 六、关键设计要点

| 要点 | 说明 |
|------|------|
| 事件驱动归集 | 领料/报工/报废事件实时归集 |
| 标准先行 | 标准成本制定是基础 |
| 差异可追溯 | 量差价差分解到要素 |
| WIP 期末结账 | 期初+投入-完工=期末 |
| 与库存联动 | 完工入库带价值 |
| 与返工报废联动 | 异常成本归集到源工单 |

## 七、相关文档

- [BOM 展开算法](../tech/tech-bom-explode-20260816.md)
- [工单管理与派工](./work-order-impl-20260906.md)
- [库存事务与账本](./inventory-ledger-20260906.md)
- [返工返修与报废](./rework-scrap-20260906.md)
- [设备 OEE 计算](../tech/tech-oee-20260816.md)
