# 报工高并发与编码生成

> 创建时间：2026-08-16
> 模块：tech

车间报工高并发场景的处理，以及业务单号编码生成的工程实现。

## 一、报工高并发场景

### 特点
- 产线密集报工：班次结束、工序完工集中提交。
- 多工位同时报工同一工单。
- 高峰 QPS 可达数百到上千。
- 数据一致性要求高（完工数不可超额）。

### 核心挑战

| 挑战 | 说明 |
|------|------|
| 完工数超额 | 并发报工导致 `completed_qty > quantity` |
| 库存超扣 | 并发扣料导致负库存 |
| 状态错乱 | 并发触发工单状态变更 |
| 重复报工 | 网络重试导致重复提交 |
| 性能瓶颈 | 锁竞争、DB 写入压力 |

## 二、防超额：DB 层乐观锁

### 原理
更新时带条件，DB 层保证不超：

```sql
UPDATE work_order
SET completed_qty = completed_qty + #{delta},
    update_time = NOW()
WHERE id = #{id}
  AND completed_qty + #{delta} <= quantity + #{tolerance}
```

影响行数 = 0 → 超额，抛异常。

### Java 实现

```java
@Service
public class WorkReportService {

    @Transactional
    public void report(WorkReportCommand cmd) {
        // 1. 保存报工记录（append-only）
        WorkReport report = WorkReport.of(cmd);
        reportRepository.save(report);

        // 2. 累加完工数（DB 层防超额）
        int rows = workOrderMapper.addCompletedQty(
            cmd.getWorkOrderId(),
            cmd.getGoodQty(),
            cmd.getTolerance());
        if (rows == 0) {
            throw new BusinessException("完工数超额，请检查工单剩余数量");
        }

        // 3. 扣减库存（同样 DB 层防超扣）
        for (MaterialConsume c : cmd.getConsumes()) {
            int r = stockMapper.deduct(c.getItemId(), c.getLotNo(),
                                       c.getQuantity(), c.getPlantId());
            if (r == 0) {
                throw new BusinessException("物料库存不足: " + c.getItemCode());
            }
        }

        // 4. 发布报工事件（异步）
        eventPublisher.publish(new WorkReportedEvent(report));
    }
}
```

### 库存扣减 SQL

```sql
UPDATE stock
SET quantity = quantity - #{qty},
    update_time = NOW()
WHERE item_id = #{itemId}
  AND lot_no = #{lotNo}
  AND plant_id = #{plantId}
  AND quantity >= #{qty}
```

`quantity >= #{qty}` 保证不超扣。

## 三、幂等：防重复报工

### 问题
网络超时 → 客户端重试 → 重复报工 → 完工数翻倍。

### 方案：客户端幂等键

```java
@PostMapping("/report")
public Result<Long> report(@RequestHeader("X-Idempotent-Key") String key,
                           @RequestBody WorkReportCommand cmd) {
    return reportService.reportIdempotent(key, cmd);
}
```

```sql
idempotent_record
├── id
├── idempotent_key     客户端生成 UUID
├── biz_type
├── result             处理结果（缓存响应）
├── create_time
└── UNIQUE KEY (idempotent_key, biz_type)
```

```java
@Transactional
public Result<Long> reportIdempotent(String key, WorkReportCommand cmd) {
    // 1. 尝试插入幂等记录（唯一约束）
    try {
        idempotentRepo.save(new IdempotentRecord(key, "WORK_REPORT"));
    } catch (DuplicateKeyException e) {
        // 已处理，返回上次结果
        return Result.of(idempotentRepo.find(key).getResult());
    }

    // 2. 执行业务
    Long reportId = doReport(cmd);

    // 3. 更新幂等记录结果
    idempotentRepo.updateResult(key, reportId.toString());
    return Result.of(reportId);
}
```

## 四、热点工单：分片与排队

### 问题
单工单高并发报工 → 行锁竞争 → 性能下降。

### 方案 1：内存预聚合

报工先入内存累加，定时批量落库：

```java
@Service
public class ReportAggregator {

    private final ConcurrentHashMap<Long, AtomicLong> completedMap = new ConcurrentHashMap<>();

    public void addCompleted(Long workOrderId, long delta) {
        completedMap.computeIfAbsent(workOrderId, k -> new AtomicLong())
                    .addAndGet(delta);
    }

    @Scheduled(fixedDelay = 1000)
    public void flush() {
        // 批量更新 DB，带防超额校验
        completedMap.forEach((woId, delta) -> {
            long val = delta.getAndSet(0);
            if (val > 0) {
                workOrderMapper.addCompletedQty(woId, val, tolerance);
            }
        });
    }
}
```

**风险**：内存数据宕机丢失。需配合报工记录表（已落库）+ 启动时重算。

### 方案 2：分桶排队

按工单 ID 取模分到不同队列，单队列串行处理：

```java
int bucket = Math.abs(workOrderId.hashCode()) % BUCKET_COUNT;
executor.submit(bucket, () -> processReport(cmd));
```

降低单工单锁竞争。

### 方案 3：分布式锁（慎用）

仅在跨资源强一致场景使用，常规报工不推荐（性能差）。

## 五、报工事件异步化

报工的副作用（库存、追溯、通知）异步处理：

```
报工请求
  ├─ 同步：保存报工记录 + 累加完工数 + 扣料
  └─ 异步（MQ）：
        ├─ 更新 WIP
        ├─ 写追溯边
        ├─ 通知 ERP
        └─ 触发质检任务
```

主链路只保留核心一致操作，非核心异步化。

## 六、写性能优化

| 优化 | 说明 |
|------|------|
| 批量插入 | 报工记录、消耗明细批量 insert |
| 异步落库 | 追溯边、操作日志异步 |
| 分表 | 报工记录按月分表 |
| 连接池 | HikariCP 调优 |
| 写分离 | 报工写主库，查询走从库 |

## 七、业务编码生成

### 常见编码规则

| 业务 | 规则示例 |
|------|---------|
| 工单号 | WO + yyyyMMdd + 4位流水（WO20260816-0001） |
| 物料编码 | 类别 + 流水（RM-000001） |
| 报工单号 | RP + yyyyMMddHHmmss + 3位流水 |
| 采购单号 | PO + yyyy + 5位流水 |

### 需求
- 全局唯一。
- 连续（业务可读性）。
- 高并发下不重复、不阻塞。
- 跨实例安全（分布式）。

## 八、编码生成方案对比

| 方案 | 优点 | 缺点 | 适用 |
|------|------|------|------|
| DB 自增 | 简单 | 单点、不连续（回滚） | 单体小规模 |
| DB 取号（独占行） | 连续、可控 | 锁竞争 | 推荐 |
| Redis INCR | 高性能 | 宕机丢号、不连续 | 高并发 |
| 雪花算法 | 分布式、无中心 | 不连续、长 | 分布式 |
| 号段模式 | 高性能、连续 | 实现稍复杂 | 推荐高并发 |

## 九、DB 取号方案（推荐中小规模）

### 表设计

```sql
sequence_generator
├── id
├── biz_type          业务类型（WORK_ORDER / ITEM / ...）
├── prefix            前缀（WO）
├── date_part         日期部分（20260816）
├── current_seq       当前流水号
├── step              步长（1）
└── UNIQUE KEY (biz_type, prefix, date_part)
```

### 取号逻辑（行锁）

```java
@Service
public class SequenceService {

    @Transactional
    public String next(String bizType, String prefix) {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        // 行锁获取或创建
        Sequence seq = sequenceMapper.selectForUpdate(bizType, prefix, date);
        if (seq == null) {
            seq = new Sequence(bizType, prefix, date, 0);
            try {
                sequenceMapper.insert(seq);
            } catch (DuplicateKeyException e) {
                seq = sequenceMapper.selectForUpdate(bizType, prefix, date);
            }
        }

        seq.increment();
        sequenceMapper.update(seq);

        return prefix + date + "-" + String.format("%04d", seq.getCurrentSeq());
    }
}
```

```sql
-- selectForUpdate
SELECT * FROM sequence_generator
WHERE biz_type = ? AND prefix = ? AND date_part = ?
FOR UPDATE;
```

### 局限
- 行锁串行，高并发瓶颈。
- 适合 QPS < 500 的场景。

## 十、号段模式（推荐高并发）

### 原理
每次从 DB 取一段号（如 1000 个），内存分发，用完再取。

```
DB: current = 1000  →  取号段 [1001, 2000]
内存: 1001, 1002, ..., 2000 分发
DB: current = 2000  →  取号段 [2001, 3000]
```

### 表设计

```sql
segment_allocator
├── id
├── biz_type
├── prefix
├── date_part
├── max_seq           已分配的最大值
├── step              号段长度（如 1000）
└── UNIQUE KEY (biz_type, prefix, date_part)
```

### 实现

```java
@Service
public class SegmentSequenceService {

    private final ConcurrentHashMap<String, Segment> cache = new ConcurrentHashMap<>();

    public String next(String bizType, String prefix) {
        String key = bizType + ":" + prefix + ":" + today();
        Segment seg = cache.compute(key, (k, v) -> {
            if (v == null || !v.hasNext()) {
                v = loadSegment(bizType, prefix);
            }
            return v;
        });
        long seq = seg.next();
        return prefix + today() + "-" + String.format("%04d", seq);
    }

    @Transactional
    protected Segment loadSegment(String bizType, String prefix) {
        SegmentAllocator alloc = allocatorMapper.selectForUpdate(bizType, prefix, today());
        if (alloc == null) {
            alloc = new SegmentAllocator(bizType, prefix, today(), 0, 1000);
            allocatorMapper.insert(alloc);
        }
        long start = alloc.getMaxSeq() + 1;
        long end = start + alloc.getStep() - 1;
        allocatorMapper.updateMaxSeq(alloc.getId(), end);
        return new Segment(start, end);
    }
}
```

### 优势
- DB 压力降低 1000 倍（每 1000 个号一次 DB）。
- 内存分发高性能。
- 号段连续。

### 风险
- 宕机丢失未用号段（号段不连续，业务可接受）。
- 跨日期切换需重新加载（按 date_part 隔离）。

## 十一、雪花算法（分布式）

```java
// 64 位：1 符号 + 41 时间戳 + 10 机器ID + 12 序列
long id = ((timestamp - epoch) << 22)
        | (machineId << 12)
        | sequence;
```

### 适用
- 不需要业务可读编码的场景（如内部 ID）。
- 分布式多实例。

### 局限
- 编码长（19 位）。
- 依赖时钟，时钟回拨需处理。

## 十二、工单号生成示例

```java
@Component
public class WorkOrderCodeGenerator {

    @Autowired private SegmentSequenceService seqService;

    public String generate() {
        return seqService.next("WORK_ORDER", "WO");
        // 输出: WO20260816-0001
    }
}
```

## 十三、设计要点

| 要点 | 说明 |
|------|------|
| 防超额用 DB 条件 | `WHERE completed + delta <= max` |
| 幂等用业务键 | 客户端 UUID + 唯一约束 |
| 热点用号段/预聚合 | 降低 DB 锁竞争 |
| 异步化副作用 | 主链路只留核心一致操作 |
| 编码用号段模式 | 高性能 + 连续 |
| 监控堆积 | 报工延迟、队列长度告警 |

## 十四、相关文档

- [框架与分层](./tech-framework-20260816.md)
- [工单状态机](./tech-workorder-state-20260816.md)
- [核心数据模型](./tech-data-model-20260816.md)
- [追溯查询实现](./tech-traceability-20260816.md)
- [MES 业务流程](../mes/mes-workflow-20260816.md)
