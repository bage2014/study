# 主数据事件同步实现

> 创建时间：2026-08-16
> 模块：tech

跨系统主数据同步的工程实现思路：事件驱动、本地消息表、幂等消费、最终一致。

## 一、同步场景

### 主数据流向

```
PLM (源头) ──► ERP ──► MES
   │           │        │
   ├─ 物料     ├─ 工单   ├─ 报工(反向)
   ├─ BOM      ├─ 采购
   └─ 工艺     └─ 库存
```

### 同步特点
- 主数据变更频率低，但可靠性要求高。
- 跨系统，不能强一致。
- 允许秒级延迟，但必须最终一致。

## 二、同步模式对比

| 模式 | 优点 | 缺点 | 适用 |
|------|------|------|------|
| 定时全量同步 | 简单 | 延迟大、压力集中 | 数据量小 |
| 定时增量同步 | 压力小 | 依赖时间戳、易漏 | 中等 |
| 实时接口调用 | 实时 | 耦合、失败难处理 | 不推荐 |
| **事件驱动（MQ）** | 解耦、可靠、实时 | 复杂 | **推荐** |
| CDC（订阅 binlog） | 无侵入 | 顺序/格式难控 | 补充 |

**推荐**：事件驱动为主，CDC/定时对账为辅。

## 三、事件驱动架构

```
┌────────┐  发事件   ┌─────────┐  消费   ┌────────┐
│  PLM   │ ────────► │   MQ    │ ──────► │  ERP   │
└────────┘           └─────────┘         └────────┘
                          │ 消费
                          ▼
                     ┌────────┐
                     │  MES   │
                     └────────┘
```

### 事件结构

```json
{
  "eventId": "uuid",
  "eventType": "ITEM_RELEASED",
  "source": "PLM",
  "bizId": "100001",
  "bizType": "ITEM",
  "version": 1,
  "timestamp": "2026-08-16T10:00:00",
  "traceId": "...",
  "payload": {
    "itemCode": "M001",
    "itemName": "零件A",
    "status": "RELEASED",
    ...
  }
}
```

### 事件类型

| 事件 | 触发 | 消费方 |
|------|------|--------|
| ITEM_CREATED | 物料创建 | ERP/MES |
| ITEM_RELEASED | 物料发布 | ERP/MES |
| ITEM_CHANGED | 物料变更 | ERP/MES |
| BOM_RELEASED | BOM 发布 | ERP/MES |
| BOM_CHANGED | BOM 变更 | ERP/MES |
| ECN_EFFECTIVE | 变更生效 | ERP/MES |
| WORKORDER_RELEASED | 工单下达（ERP→MES） | MES |
| WORKREPORT_COMPLETED | 报工完成（MES→ERP） | ERP |

## 四、可靠投递：本地消息表

### 问题
直接发 MQ：业务事务提交了但 MQ 发送失败 → 数据不一致。

### 方案：本地消息表（事务消息）

```
业务表更新 + 消息表插入 ──同一事务──► 提交
                              │
                              ▼
                   定时扫描消息表，发送 MQ
                              │
                              ▼
                   发送成功 → 标记已发送
```

### 表设计

```sql
outbox_message (本地消息表)
├── id
├── event_id           事件唯一 ID（幂等键）
├── event_type
├── source
├── biz_id
├── biz_type
├── payload (json)
├── status             PENDING / SENT / FAILED
├── retry_count
├── next_retry_time
├── create_time
└── sent_time
```

### 发送流程

```java
@Service
public class ItemApplicationService {

    @Autowired private ItemRepository itemRepo;
    @Autowired private OutboxRepository outboxRepo;

    @Transactional
    public void release(Long itemId) {
        Item item = itemRepo.findOrThrow(itemId);
        item.release();
        itemRepo.save(item);

        // 同事务写消息表
        OutboxMessage msg = OutboxMessage.of(
            "ITEM_RELEASED", "PLM", itemId, "ITEM", item);
        outboxRepo.save(msg);
    }
}
```

### 定时扫描发送

```java
@Scheduled(fixedDelay = 5000)
public void publishPending() {
    List<OutboxMessage> pending = outboxRepo.findPending(100);
    for (OutboxMessage msg : pending) {
        try {
            kafkaTemplate.send("plm-events", msg.getEventId(), msg.toJson());
            outboxRepo.markSent(msg.getId());
        } catch (Exception e) {
            outboxRepo.incrementRetry(msg.getId(), nextRetryTime());
            if (msg.getRetryCount() >= MAX_RETRY) {
                log.error("消息发送失败超限: {}", msg.getEventId());
                alertService.notify("消息发送失败", msg);
            }
        }
    }
}
```

### 优势
- 业务与消息同事务，强一致。
- MQ 宕机不影响业务。
- 重启可恢复。

## 五、幂等消费

### 问题
MQ 可能重复投递（at-least-once），消费方必须幂等。

### 方案：事件 ID 去重

```sql
consumed_event (已消费事件表)
├── event_id     唯一
├── consumer     消费者标识
├── consume_time
└── PRIMARY KEY (event_id, consumer)
```

```java
@Service
public class ItemEventConsumer {

    @KafkaListener(topics = "plm-events")
    @Transactional
    public void onMessage(String message) {
        OutboxEvent event = parse(message);

        // 幂等检查
        if (consumedRepo.exists(event.getEventId(), "MES")) {
            return;  // 已处理
        }

        // 业务处理
        handleEvent(event);

        // 标记已消费
        consumedRepo.save(new ConsumedEvent(event.getEventId(), "MES"));
    }

    private void handleEvent(OutboxEvent event) {
        switch (event.getEventType()) {
            case "ITEM_RELEASED" -> itemSyncService.upsert(event.getPayload());
            case "BOM_RELEASED" -> bomSyncService.upsert(event.getPayload());
            case "ECN_EFFECTIVE" -> ecnSyncService.apply(event.getPayload());
        }
    }
}
```

### 乱序处理

同一物料多次变更可能乱序到达：
- 用 `version` 字段，仅接受更高版本。
- 或用 `timestamp`，旧事件丢弃。

```java
public void upsert(ItemPayload payload) {
    Item existing = itemRepo.findByCode(payload.getCode());
    if (existing != null && payload.getVersion() <= existing.getVersion()) {
        return;  // 旧版本，忽略
    }
    itemRepo.upsert(payload);
}
```

## 六、CDC 补充方案

订阅数据库 binlog，无侵入同步：

```
PLM DB ──binlog──► Debezium ──► Kafka ──► 消费方
```

### 适用
- 老旧系统无事件能力。
- 全量 + 增量初始化。
- 对账。

### 局限
- 顺序性难保证（多表）。
- 字段映射需适配层。
- 不适合复杂业务事件（如"已发布"是状态变更，binlog 能感知但需解读）。

## 七、对账机制

### 定时对账

```java
@Scheduled(cron = "0 0 2 * * ?")  // 每天凌晨
public void reconcile() {
    // 1. PLM 查最近变更的物料
    List<Item> changed = plmItemRepo.findChangedSince(lastReconcileTime);
    // 2. ERP / MES 比对
    for (Item item : changed) {
        if (!erpItemRepo.exists(item.getCode())) {
            // 缺失，补发事件
            outboxRepo.save(OutboxMessage.reconcile(item));
        }
    }
}
```

### 校验
- 主键数量对比。
- 关键字段抽样对比。
- 版本号对比。

## 八、失败与重试

### 重试策略

```java
// 指数退避
LocalDateTime nextRetry(int retryCount) {
    long delaySeconds = (long) Math.pow(2, retryCount) * 30;  // 30s, 60s, 120s, ...
    return LocalDateTime.now().plusSeconds(delaySeconds);
}
```

### 死信队列
- 超过最大重试次数 → 进死信队列。
- 人工介入处理。

### 告警
- 消息堆积超阈值告警。
- 死信队列新增告警。

## 九、顺序性

### 问题
同一物料的多条事件需按顺序处理（创建 → 发布 → 变更）。

### 方案

**1. 分区保证顺序**

按 `bizId` 分区，同物料事件落同一分区，单消费者按序消费：

```java
kafkaTemplate.send("plm-events", bizId, event);  // key=bizId
```

**2. 版本号兜底**

即使乱序，版本号保证只接受更新版本。

## 十、监控

| 指标 | 说明 |
|------|------|
| 消息表堆积数 | outbox PENDING 数量 |
| 发送延迟 | 创建时间 → 发送时间 |
| 消费延迟 | 发送时间 → 消费时间 |
| 死信数量 | 失败超限消息 |
| 消费失败率 | 消费异常占比 |

## 十一、设计要点

| 要点 | 说明 |
|------|------|
| 源头唯一 | 每类主数据只有一个权威源 |
| 事件不可变 | 已发送事件不修改 |
| 幂等消费 | 事件 ID 去重 |
| 版本号 | 防乱序、防旧覆盖新 |
| 本地消息表 | 业务与消息同事务 |
| 重试 + 死信 | 失败可恢复 |
| 对账兜底 | 最终一致保障 |
| 监控告警 | 异常可发现 |

## 十二、相关文档

- [框架与分层](./tech-framework-20260816.md)
- [集成架构](../integration/integration-architecture-20260816.md)
- [工单状态机](./tech-workorder-state-20260816.md)
