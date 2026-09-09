# 返工返修与报废

> 创建时间：2026-09-06
> 模块：concepts

不合格品处理（返工/返修/降级/报废/让步）的概念、流程、Java 实现。质量检验见 [tech-quality-spc](../tech/tech-quality-spc-20260816.md)，本文聚焦不合格处置闭环。

## 一、核心概念

### 1. 处置决策类型

| 类型 | 说明 | 库存影响 | 质量 |
|------|------|---------|------|
| 返工 Rework | 重新加工达合格 | 不出库 | 不算不良（最终合格） |
| 返修 Repair | 修复缺陷，可能降级 | 不出库 | 仍计不良 |
| 降级 Downgrade | 转低规格产品 | 转物料编码 | 计不良 |
| 让步 Concession | 有条件接收 | 出库 | 计不良、需审批 |
| 报废 Scrap | 直接报废 | 出库扣减 | 计不良 |
| 退货 Return | 退供应商 | 退库 | 计供应商不良 |

### 2. 返工 vs 返修

| 区别 | 返工 | 返修 |
|------|------|------|
| 目标 | 达到原质量标准 | 可使用但非原标准 |
| 工艺 | 按原工艺重做 | 局部修补 |
| 不良统计 | 最终合格不算不良 | 永久计不良 |
| 成本 | 高（重做） | 较低 |

## 二、数据模型

```sql
nc_record (不合格品记录)
├── id
├── nc_no
├── source                IQC/IPQC/OQC/FQC
├── item_id
├── lot_no / serial_no
├── work_order_id         关联工单
├── operation_id          关联工序
├── defect_code           不良代码
├── defect_qty
├── disposition           REWORK/REPAIR/DOWNGRADE/CONCESSION/SCRAP/RETURN
├── status                PENDING/IN_REVIEW/DISPOSED/CLOSED
├── reviewer_id
├── review_time
└── ...

rework_order (返工/返修工单)
├── id
├── rework_no
├── source_nc_id          来源不合格单
├── source_work_order_id  源工单
├── item_id
├── quantity
├── rework_type           REWORK/REPAIR
├── routing_id            返工工艺
├── status                同生产工单
├── cost
└── ...

scrap_order (报废单)
├── id
├── scrap_no
├── source_nc_id
├── item_id
├── lot_no / serial_no
├── quantity
├── scrap_reason
├── scrap_value           残值
├── status                PENDING/APPROVED/DONE
└── ...

downgrade_order (降级单)
├── id
├── source_nc_id
├── from_item_id          原物料
├── to_item_id            降级后物料
├── lot_no
├── quantity
└── ...
```

## 三、业务流程

### 1. 不合格评审流程

```
检验不合格 → 创建 NC 记录 → 隔离库存
   → 评审（工程师）
      ├─ 返工 → 返工工单
      ├─ 返修 → 返修工单
      ├─ 降级 → 降级单
      ├─ 让步 → 让步审批
      ├─ 报废 → 报废审批
      └─ 退货 → 退货单
   → 执行 → 库存状态变更 → NC 关闭
```

### 2. 返工流程

```
1. 评审决策返工
2. 创建返工工单（关联源工单、不良品）
3. 制定返工工艺（路由到问题工序）
4. 不良品从隔离库发料
5. 执行返工报工
6. 返工后检验
   ├─ 合格 → 转可用
   └─ 仍不合格 → 二次评审
7. 关闭 NC + 返工工单
```

### 3. 报废流程

```
1. 评审决策报废
2. 创建报废单
3. 审批（按金额分级）
4. 库存扣减（出库到报废库）
5. 残值入账
6. 关闭 NC
```

### 4. 让步接收

```
1. 评审建议让步
2. 让步审批（质量+客户）
3. 通过 → 库存可用，标记让步
4. 拒绝 → 走其他处置
```

## 四、Java 实现

### NC 领域

```java
public class NcRecord extends BaseAggregate {
    private Long id;
    private String ncNo;
    private Long itemId;
    private BigDecimal defectQty;
    private Disposition disposition;     // 待评审时为 null
    private NcStatus status;

    public void review(Disposition decision, Long reviewerId) {
        Assert.isTrue(status == PENDING || status == IN_REVIEW, "NC 不可评审");
        this.disposition = decision;
        this.reviewerId = reviewerId;
        this.reviewTime = LocalDateTime.now();
        this.status = DISPOSED;
    }

    public void close() {
        Assert.isTrue(status == DISPOSED, "处置未完成不可关闭");
        this.status = CLOSED;
    }
}
```

### 评审与处置服务

```java
@Service
public class DispositionService {

    @Autowired private NcRecordRepository ncRepo;
    @Autowired private ReworkOrderService reworkService;
    @Autowired private ScrapOrderService scrapService;
    @Autowired private DowngradeService downgradeService;
    @Autowired private StockLedgerService stockLedger;
    @Autowired private WorkflowService workflowService;

    // 评审决策
    @Transactional
    public void dispose(Long ncId, DispositionDecision decision) {
        NcRecord nc = ncRepo.findOrThrow(ncId);
        nc.review(decision.getType(), SecurityUtils.currentUserId());
        ncRepo.save(nc);

        // 按处置类型分流
        switch (decision.getType()) {
            case REWORK, REPAIR -> reworkService.createFromNc(nc, decision);
            case SCRAP -> scrapService.createFromNc(nc, decision);
            case DOWNGRADE -> downgradeService.createFromNc(nc, decision);
            case CONCESSION -> workflowService.start("CONCESSION_APPROVAL",
                new FlowBizRef("NC", ncId));
            case RETURN -> createReturn(nc, decision);
        }
    }
}
```

### 返工工单服务

```java
@Service
public class ReworkOrderService extends WorkOrderService {

    @Transactional
    public Long createFromNc(NcRecord nc, DispositionDecision decision) {
        // 创建返工工单
        ReworkOrder ro = ReworkOrder.of(nc, decision);
        reworkRepo.save(ro);

        // 锁定返工工艺（指定问题工序的子集）
        Routing reworkRouting = routingRepo.findOrThrow(decision.getRoutingId());
        ro.lockRouting(reworkRouting);

        // 隔离库 → 返工发料
        stockLedger.record(StockTxnCmd.outbound(
            nc.getItemId(), nc.getLotNo(), nc.getDefectQty(),
            nc.getWorkOrderId(), "REWORK_ISSUE"));

        return ro.getId();
    }

    // 返工完工 → 重新检验
    @EventListener
    public void onReworkCompleted(ReworkCompletedEvent event) {
        // 触发重新检验
        inspectionService.createForRework(event.getReworkOrderId());
    }
}
```

### 报废服务

```java
@Service
public class ScrapOrderService {

    @Transactional
    public Long createFromNc(NcRecord nc, DispositionDecision decision) {
        ScrapOrder order = ScrapOrder.of(nc, decision);
        scrapRepo.save(order);
        // 启动报废审批（按金额）
        workflowService.start("SCRAP_APPROVAL", new FlowBizRef("SCRAP", order.getId()));
        return order.getId();
    }

    // 审批通过 → 执行报废
    @EventListener
    public void onScrapApproved(ApprovedEvent event) {
        if (!"SCRAP".equals(event.getBizType())) return;
        ScrapOrder order = scrapRepo.findOrThrow(event.getBizId());
        execute(order);
    }

    @Transactional
    public void execute(ScrapOrder order) {
        // 库存扣减到报废库
        stockLedger.record(StockTxnCmd.outbound(
            order.getItemId(), order.getLotNo(), order.getQuantity(),
            null, "SCRAP"));
        stockLedger.record(StockTxnCmd.inbound(
            order.getItemId(), order.getLotNo(), order.getQuantity(),
            SCRAP_LOCATION, "SCRAP"));
        // 残值入账
        costService.recordScrap(order.getItemId(), order.getQuantity(), order.getScrapValue());
        order.status(DONE);
        scrapRepo.save(order);
        // 关联 NC 关闭
        ncRepo.findOrThrow(order.getSourceNcId()).close();
    }
}
```

### 降级服务

```java
@Service
public class DowngradeService {

    @Transactional
    public void createFromNc(NcRecord nc, DispositionDecision decision) {
        DowngradeOrder order = DowngradeOrder.of(nc, decision);
        downgradeRepo.save(order);
        // 库存转物料编码：原物料出库、降级物料入库
        stockLedger.record(StockTxnCmd.outbound(
            nc.getItemId(), nc.getLotNo(), nc.getDefectQty(), null, "DOWNGRADE"));
        stockLedger.record(StockTxnCmd.inbound(
            decision.getToItemId(), nc.getLotNo(), nc.getDefectQty(), null, "DOWNGRADE"));
    }
}
```

### 让步审批回调

```java
@EventListener
public void onConcessionApproved(ApprovedEvent event) {
    if (!"CONCESSION_APPROVAL".equals(event.getBizType())) return;
    NcRecord nc = ncRepo.findOrThrow(event.getBizId());
    // 库存从隔离 → 可用（带让步标记）
    stockLedger.record(StockTxnCmd.transfer(
        nc.getItemId(), nc.getLotNo(), QUARANTINE, AVAILABLE, "CONCESSION"));
    nc.close();
    ncRepo.save(nc);
}
```

## 五、成本与统计

### 返工成本归集

```java
// 返工工单报工时归集成本（人工+设备+物料）
@EventListener
public void onReworkReported(WorkReportedEvent event) {
    if (!event.isRework()) return;
    ReworkOrder ro = reworkRepo.findByOperation(event.getOperationId());
    costService.addReworkCost(ro.getSourceWorkOrderId(),
        event.getLaborCost(), event.getMachineCost(), event.getMaterialCost());
}
```

### 不良统计

```java
// 不良率 = 不良数 / 检验数
// 永久计不良：返修/降级/让步/报废
// 临时不良（最终合格）：返工
```

## 六、关键设计要点

| 要点 | 说明 |
|------|------|
| NC 全流程闭环 | 评审→处置→执行→关闭 |
| 处置分流 | 不同决策走不同流程 |
| 库存状态联动 | 隔离→处置后变更 |
| 返工需重检 | 返工后必须重新检验 |
| 报废审批分级 | 按金额控制审批层级 |
| 成本归集到源工单 | 返工成本算入原工单 |

## 七、相关文档

- [质量管理与 SPC](../tech/tech-quality-spc-20260816.md)
- [QMS 质量管理体系](../systems/qms-overview-20260906.md)
- [工单管理与派工](./work-order-impl-20260906.md)
- [库存事务与账本](./inventory-ledger-20260906.md)
- [生产成本核算](./cost-accounting-20260906.md)
