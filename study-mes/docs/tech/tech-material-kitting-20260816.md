# 物料齐套与配料

> 创建时间：2026-08-16
> 模块：tech

工单下达前的齐套校验、配料、领料、配送的工程实现思路。

## 一、业务流程

```
工单下达
   │
   ▼
齐套校验 ──不齐套──► 缺料预警（暂停下达）
   │ 齐套
   ▼
生成配料单
   │
   ▼
WMS 配料 ──► 配送（AGV/人工） ──► 工位扫码接收
   │
   ▼
线边库存更新，工单可开工
```

## 二、数据模型

```sql
material_kit (配料单)
├── id
├── kit_no
├── work_order_id
├── plant_id
├── status            PENDING / PICKING / DELIVERED / RECEIVED / CANCELED
├── create_time
└── ...

material_kit_line (配料行)
├── id
├── kit_id
├── item_id
├── bom_line_id       来源 BOM 行
├── required_qty      需求量
├── picked_qty        已拣料量
├── lot_no            分配批次
├── substitute        是否替代料
└── status            PENDING / PICKED / DELIVERED
```

## 三、BOM 用料展开

工单用料 = MBOM 展开（仅当前工单产品）：

```java
@Service
public class MaterialRequirementService {

    @Autowired private BomExploder exploder;
    @Autowired private BomRepository bomRepo;

    public List<MaterialRequirement> calculate(Long workOrderId) {
        WorkOrder wo = workOrderRepo.findOrThrow(workOrderId);
        // 用工单锁定的 BOM 版本
        Bom bom = bomRepo.findOrThrow(wo.getBomId());
        List<FlatBomEntry> flat = exploder.explode(wo.getBomId(), wo.getQuantity(), wo.getPlanStart().toLocalDate());

        // 按工序分组（MBOM 行含 operation_id）
        return flat.stream()
            .map(e -> new MaterialRequirement(e.getItemId(), e.getTotalQuantity(), wo.getQuantity()))
            .toList();
    }
}
```

## 四、齐套校验

校验需求 vs 可用库存（线边 + 在途 - 已分配）：

```java
@Service
public class KittingCheckService {

    public KittingResult check(Long workOrderId) {
        List<MaterialRequirement> reqs = requirementService.calculate(workOrderId);
        List<KittingItem> items = new ArrayList<>();
        boolean allReady = true;

        for (MaterialRequirement req : reqs) {
            BigDecimal available = stockService.getAvailable(req.getItemId(), plantId);
            BigDecimal allocated = allocationService.getAllocated(req.getItemId(), plantId);
            BigDecimal netAvailable = available.subtract(allocated);
            boolean ready = netAvailable.compareTo(req.getQuantity()) >= 0;

            if (!ready) allReady = false;
            items.add(new KittingItem(req, netAvailable, ready, req.getQuantity().subtract(netAvailable)));
        }

        return new KittingResult(allReady, items);
    }
}
```

### 可用库存定义
```
可用库存 = 现有量（status=AVAILABLE）
        - 已分配（其他工单配料单）
        - 安全库存
        + 在途（采购在途，可选）
```

## 五、批次分配策略

齐套通过后，为每行分配具体批次：

### FIFO（先进先出）
```java
public List<LotAllocation> allocateFifo(Long itemId, BigDecimal qty, Long plantId) {
    List<StockLot> lots = stockRepo.findByItemOrderByReceiptDateAsc(itemId, plantId);  // 入库日期升序
    List<LotAllocation> result = new ArrayList<>();
    BigDecimal remaining = qty;
    for (StockLot lot : lots) {
        if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
        BigDecimal use = lot.getAvailable().min(remaining);
        result.add(new LotAllocation(lot.getLotNo(), use));
        remaining = remaining.subtract(use);
    }
    if (remaining.compareTo(BigDecimal.ZERO) > 0) {
        throw new BusinessException("库存不足");
    }
    return result;
}
```

### 其他策略
| 策略 | 说明 |
|------|------|
| FIFO | 先进先出（默认，保质期场景） |
| FEFO | 先到期先出（食品/医药） |
| LIFO | 后进先出（极少用） |
| 批次指定 | 客户指定批次（追溯要求） |

## 六、替代料切换

主料不足时按替代料规则切换：

```sql
item_substitute (替代料关系)
├── id
├── primary_item_id     主料
├── substitute_item_id  替代料
├── priority            优先级
├── ratio               用量比（如 1:1.2）
└── effective           生效范围
```

```java
public Allocation allocateWithSubstitute(MaterialRequirement req, Long plantId) {
    // 先尝试主料
    try {
        return allocateFifo(req.getItemId(), req.getQuantity(), plantId);
    } catch (InsufficientException e) {
        // 主料不足，查替代料
        List<ItemSubstitute> subs = substituteRepo.findByPrimaryOrderByPriority(req.getItemId());
        for (ItemSubstitute sub : subs) {
            try {
                BigDecimal subQty = req.getQuantity().multiply(sub.getRatio());
                return allocateFifo(sub.getSubstituteItemId(), subQty, plantId).markSubstitute(sub);
            } catch (InsufficientException ignored) {}
        }
        throw new BusinessException("主料及替代料均不足");
    }
}
```

## 七、配料单生成与执行

```java
@Service
public class MaterialKitService {

    @Transactional
    public Long createKit(Long workOrderId) {
        KittingResult check = kittingCheckService.check(workOrderId);
        Assert.isTrue(check.isReady(), "物料不齐套");

        MaterialKit kit = MaterialKit.of(workOrderId);
        kitRepo.save(kit);

        List<MaterialRequirement> reqs = requirementService.calculate(workOrderId);
        for (MaterialRequirement req : reqs) {
            Allocation alloc = allocateWithSubstitute(req, kit.getPlantId());
            for (LotAssignment a : alloc.getLots()) {
                MaterialKitLine line = new MaterialKitLine(kit, req, a);
                kitLineRepo.save(line);
                // 预占库存
                allocationService.allocate(req.getItemId(), a.getLotNo(), a.getQty(), workOrderId);
            }
        }

        // 通知 WMS 配料
        wmsClient.sendPickList(kit);
        eventPublisher.publish(new KitCreatedEvent(kit));
        return kit.getId();
    }
}
```

## 八、与 WMS 集成

### 接口
| 接口 | 方向 | 数据 |
|------|------|------|
| 发送配料单 | MES → WMS | 配料行 |
| 配料完成 | WMS → MES | 实际拣料批次/数量 |
| 配送完成 | WMS → MES | 工位送达 |
| 工位接收 | MES → WMS | 确认接收 |
| 退料 | MES → WMS | 工单剩余退回 |

### 接收确认
```java
public void confirmReceived(Long kitId, List<LineReceive> lines) {
    MaterialKit kit = kitRepo.findOrThrow(kitId);
    for (LineReceive l : lines) {
        MaterialKitLine line = kitLineRepo.findOrThrow(l.getLineId());
        line.receive(l.getActualQty(), l.getLotNo());
        kitLineRepo.save(line);
        // 库存事务：从仓到线边
        stockService.transfer(line.getItemId(), l.getLotNo(), WAREHOUSE, LINE_SIDE, l.getActualQty());
    }
    if (kit.allReceived()) {
        kit.status(RECEIVED);
        eventPublisher.publish(new KitReceivedEvent(kit));  // 触发工单可开工
    }
}
```

## 九、缺料预警

不齐套时生成缺料清单并预警：

```java
public void onNotReady(KittingResult result, Long workOrderId) {
    List<ShortageItem> shortages = result.getItems().stream()
        .filter(i -> !i.isReady())
        .map(i -> new ShortageItem(i.getItemId(), i.getShortageQty()))
        .toList();
    shortageRepo.save(new Shortage(workOrderId, shortages));
    alertService.notify("工单 " + workOrderId + " 缺料", shortages);
}
```

## 十、设计要点

| 要点 | 说明 |
|------|------|
| BOM 版本锁定 | 工单用锁定版本，避免变更冲击 |
| 可用库存计算 | 含已分配扣减 |
| 批次分配可追溯 | 记录每行批次 |
| 替代料闭环 | 切换需记录，追溯可查 |
| 预占机制 | 配料即预占，防他人占用 |
| 与 WMS 解耦 | 走接口/事件，不强耦合 |

## 十一、相关文档

- [BOM 展开算法](./tech-bom-explode-20260816.md)
- [工单状态机](./tech-workorder-state-20260816.md)
- [追溯查询](./tech-traceability-20260816.md)
- [BOM 业务流程](../bom/bom-workflow-20260816.md)
