# 库存事务与账本

> 创建时间：2026-09-06
> 模块：concepts

库存管理的概念、事务模型、账本（Ledger）实现方案。区别于 WMS 实物管理，本文聚焦库存账务。

## 一、核心概念

### 1. 库存维度

```
库存 = 物料 + 批次 + 序列号 + 库位 + 状态 + 库主
```

| 维度 | 说明 |
|------|------|
| 物料 | 唯一编码 |
| 批次 | 批次号（lot） |
| 序列号 | 单件SN |
| 库位 | 仓库-库区-库位 |
| 状态 | 可用/冻结/在检/预留/报废 |
| 库主 | 多租户/委外 |

### 2. 库存账本（Ledger）

Append-only 的事务日志，记录每次库存变动。当前库存 = 期初 + 事务汇总。

```
事务类型：
  INBOUND（入库）   +
  OUTBOUND（出库）  -
  TRANSFER（移库）  源-目的+
  ADJUST（调整）    ±
  RESERVE（预留）   -可用 +预留
  RELEASE（释放）   +可用 -预留
```

### 3. 账实分离

| 层 | 系统 | 职责 |
|----|------|------|
| 账 | ERP/MES | 库存数量、价值 |
| 实 | WMS | 实物库位、批次 |

账实定期对账，保持一致。

## 二、数据模型

```sql
stock_ledger (库存账本 - append only)
├── id
├── txn_no              事务号
├── txn_type            INBOUND/OUTBOUND/TRANSFER/ADJUST
├── item_id
├── lot_no
├── serial_no
├── from_location_id    源库位（出库/移库）
├── to_location_id      目的库位（入库/移库）
├── from_status         源状态
├── to_status           目的状态
├── quantity             数量（正数）
├── direction           IN/OUT
├── biz_ref_type        工单/采购/销售
├── biz_ref_id
├── txn_time
├── create_by/time
└── ...

stock_balance (库存余额 - 当前快照)
├── id
├── item_id
├── lot_no
├── location_id
├── status
├── quantity             当前数量
├── version              乐观锁版本
└── update_time
```

设计要点：
- `stock_ledger` 只追加，不改不删，完整审计链。
- `stock_balance` 是汇总缓存，可由 ledger 重算。

## 三、业务流程

### 1. 入库流程

```
完工入库/采购入库 → 生成入库事务 → ledger+balance 更新
```

### 2. 出库流程

```
销售/领料出库 → 校验可用 → 生成出库事务 → ledger+balance 更新
```

### 3. 移库流程

```
源库位 → 生成移库事务 → 源- 目的+ → 双账本更新
```

### 4. 预留与释放

```
工单配料 → 预留（可用→预留） → 实际发料（预留→出库）
工单取消 → 释放（预留→可用）
```

### 5. 盘点流程

```
1. 冻结库位（暂停出入库）
2. 实盘录入
3. 账实对比
4. 差异处理 → 调整事务
5. 解冻
```

## 四、Java 实现

### 库存事务服务

```java
@Service
public class StockLedgerService {

    @Autowired private StockLedgerRepository ledgerRepo;
    @Autowired private StockBalanceRepository balanceRepo;
    @Autowired private SequenceService seqService;

    // 记录事务 + 更新余额（同事务）
    @Transactional
    public void record(StockTxnCmd cmd) {
        // 1. 追加账本
        StockLedger ledger = StockLedger.of(cmd, seqService.next("STK"));
        ledgerRepo.save(ledger);

        // 2. 更新余额
        applyToBalance(ledger);
    }

    private void applyToBalance(StockLedger txn) {
        switch (txn.getTxnType()) {
            case INBOUND -> increase(txn.getToLocationId(), txn.getToStatus(),
                txn.getItemId(), txn.getLotNo(), txn.getQuantity());
            case OUTBOUND -> decrease(txn.getFromLocationId(), txn.getFromStatus(),
                txn.getItemId(), txn.getLotNo(), txn.getQuantity());
            case TRANSFER -> {
                decrease(txn.getFromLocationId(), txn.getFromStatus(),
                    txn.getItemId(), txn.getLotNo(), txn.getQuantity());
                increase(txn.getToLocationId(), txn.getToStatus(),
                    txn.getItemId(), txn.getLotNo(), txn.getQuantity());
            }
            case ADJUST -> adjust(txn);
        }
    }

    // 扣减（乐观锁防超扣）
    private void decrease(Long locId, StockStatus status, Long itemId, String lot, BigDecimal qty) {
        StockBalance bal = balanceRepo.findOrThrow(itemId, lot, locId, status);
        BigDecimal after = bal.getQuantity().subtract(qty);
        Assert.isTrue(after.compareTo(BigDecimal.ZERO) >= 0,
            "库存不足: 当前=" + bal.getQuantity() + " 扣减=" + qty);
        bal.setQuantity(after);
        bal.incVersion();  // 乐观锁
        balanceRepo.save(bal);
    }

    private void increase(Long locId, StockStatus status, Long itemId, String lot, BigDecimal qty) {
        StockBalance bal = balanceRepo.findOrCreate(itemId, lot, locId, status);
        bal.setQuantity(bal.getQuantity().add(qty));
        balanceRepo.save(bal);
    }
}
```

### 可用库存查询

```java
public class StockQueryService {

    // 可用库存 = 可用状态余额 - 已预留
    public BigDecimal getAvailable(Long itemId, Long plantId) {
        BigDecimal onHand = balanceRepo.sumByItemStatus(itemId, plantId, AVAILABLE);
        BigDecimal reserved = balanceRepo.sumByItemStatus(itemId, plantId, RESERVED);
        return onHand.subtract(reserved);
    }

    // 按批次明细
    public List<StockLot> findLots(Long itemId, Long plantId) {
        return balanceRepo.findLotsByItem(itemId, plantId, AVAILABLE).stream()
            .filter(b -> b.getQuantity().compareTo(ZERO) > 0)
            .collect(toList());
    }
}
```

### 预留服务

```java
@Service
public class ReservationService {

    // 预留：可用 → 预留
    @Transactional
    public void reserve(Long itemId, String lotNo, Long locId, BigDecimal qty, Long workOrderId) {
        // 检查可用
        BigDecimal available = stockQuery.getAvailable(itemId, lotNo, locId);
        Assert.isTrue(available.compareTo(qty) >= 0, "可用库存不足");

        // 记账：可用减、预留加
        ledgerService.record(StockTxnCmd.reserve(itemId, lotNo, locId, qty, workOrderId));
    }

    // 释放：预留 → 可用
    @Transactional
    public void release(Long reservationId) {
        Reservation r = reservationRepo.findOrThrow(reservationId);
        ledgerService.record(StockTxnCmd.release(r));
    }

    // 消耗：预留 → 实际出库
    @Transactional
    public void consume(Long reservationId, BigDecimal actualQty) {
        Reservation r = reservationRepo.findOrThrow(reservationId);
        ledgerService.record(StockTxnCmd.consume(r, actualQty));
    }
}
```

### 盘点服务

```java
@Service
public class StockCountService {

    @Transactional
    public void submitCount(Long taskId, List<CountLine> lines) {
        StockCountTask task = taskRepo.findOrThrow(taskId);
        Assert.isTrue(task.isFrozen(), "库位未冻结");

        for (CountLine line : lines) {
            StockBalance bal = balanceRepo.findOrThrow(line.getBalanceId());
            BigDecimal diff = line.getActualQty().subtract(bal.getQuantity());
            if (diff.compareTo(ZERO) != 0) {
                // 生成调整事务
                ledgerService.record(StockTxnCmd.adjust(bal, diff, task));
            }
        }
        task.complete();
    }
}
```

### 账本重算（容灾）

```java
// 从 ledger 重建 balance（数据修复）
public void rebuildBalance(Long itemId) {
    // 清空该物料 balance
    balanceRepo.deleteByItem(itemId);
    // 汇总 ledger
    List<StockLedger> txns = ledgerRepo.findByItem(itemId);
    Map<BalanceKey, BigDecimal> map = new HashMap<>();
    for (StockLedger t : txns) {
        applyToMap(map, t);  // 同 applyToBalance 逻辑
    }
    // 批量写回
    map.forEach((k, v) -> balanceRepo.save(StockBalance.of(k, v)));
}
```

## 五、性能与一致性

| 优化 | 说明 |
|------|------|
| Append-only ledger | 高并发写入友好 |
| 余额缓存 + 乐观锁 | 避免全表扫 |
| 批次/序列号索引 | 查询高效 |
| 对账兜底 | 定期 balance vs ledger 汇总校验 |
| 重算能力 | ledger 为准，可重建 balance |

## 六、关键设计要点

| 要点 | 说明 |
|------|------|
| 账本 append-only | 不可改不可删，完整审计 |
| 余额是快照 | 可由 ledger 重算 |
| 乐观锁防超扣 | version 字段 |
| 预留机制 | 配料预占，防止他人占用 |
| 账实分离 | ERP 账 + WMS 实，定期对账 |
| 事务驱动 | 每次变动都有事务记录 |

## 七、相关文档

- [WMS 仓储管理](../systems/wms-overview-20260906.md)
- [物料齐套与配料](../tech/tech-material-kitting-20260816.md)
- [追溯查询](../tech/tech-traceability-20260816.md)
- [核心数据模型](../tech/tech-data-model-20260816.md)
