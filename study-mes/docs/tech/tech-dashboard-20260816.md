# 实时看板与数据聚合

> 创建时间：2026-08-16
> 模块：tech

车间实时看板、数据聚合、推送的工程实现思路。

## 一、看板类型

| 类型 | 场景 | 数据来源 |
|------|------|---------|
| 生产进度看板 | 工单进度、完工数 | 报工数据 |
| 设备状态看板 | 设备运行/停机 | 采集数据 |
| 质量看板 | 不良率、Top 不良 | 检验数据 |
| 异常看板 | 报警、待处理 | 多源 |
| 大屏 | 综合指标 | 聚合数据 |

## 二、整体架构

```
数据源（报工/采集/检验）
        │
        ▼
   事件总线 (Kafka)
        │
   ┌────┴────┐
   ▼         ▼
实时聚合    定时聚合
(内存)      (预计算表)
   │         │
   └────┬────┘
        ▼
   推送服务 (WebSocket / SSE)
        │
        ▼
     前端看板
```

## 三、数据聚合策略

### 1. 实时聚合（推）

数据变更即更新内存指标，推送给前端。适合秒级实时。

```java
@Service
public class RealtimeDashboardService {

    private final Map<Long, WorkOrderMetric> cache = new ConcurrentHashMap<>();

    @EventListener
    public void onWorkReported(WorkReportedEvent event) {
        WorkOrderMetric metric = cache.computeIfAbsent(event.workOrderId(), WorkOrderMetric::new);
        metric.addCompleted(event.goodQty());
        metric.addScrap(event.badQty());

        // 推送
        messagingTemplate.convertAndSend("/topic/workorder/" + event.workOrderId(), metric);
        messagingTemplate.convertAndSend("/topic/dashboard/production", summary());
    }
}
```

### 2. 定时聚合（拉/推）

定时查询预计算表，推或供前端轮询。适合分钟级。

```sql
dashboard_production_hourly (按小时预聚合)
├── plant_id
├── work_order_id
├── hour_bucket        小时桶（2026-08-16 10:00）
├── planned_qty
├── completed_qty
├── scrap_qty
└── ...
```

```java
@Scheduled(cron = "0 * * * * ?")  // 每分钟
public void aggregateAndPush() {
    List<DashboardData> data = dashboardMapper.aggregateCurrentHour();
    messagingTemplate.convertAndSend("/topic/dashboard/hourly", data);
}
```

### 3. 按需查询（拉）

前端主动查询，适合低频看板。走只读库/缓存。

## 四、实时推送技术

| 技术 | 特点 | 适用 |
|------|------|------|
| WebSocket | 全双工、持久连接 | 主推方案 |
| SSE | 单向、简单 | 服务端推送单向数据 |
| 轮询 | 简单、兼容好 | 低实时性 |
| 长轮询 | 折中 | 兼容性要求高 |

### WebSocket 实现（STOMP）

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/dashboard").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
```

```java
@Controller
public class DashboardController {

    @MessageMapping("/dashboard/subscribe")
    public void subscribe(SubscribeRequest req, SimpMessageHeaderAccessor accessor) {
        String user = accessor.getUser().getName();
        // 按工厂/工位订阅
        messagingTemplate.convertAndSendToUser(user, "/queue/dashboard", snapshot(req.getPlantId()));
    }
}
```

### 前端订阅

```ts
const client = new Client({
  brokerURL: 'ws://host/ws/dashboard',
  onConnect: () => {
    client.subscribe('/topic/dashboard/production', (msg) => {
      updateChart(JSON.parse(msg.body))
    })
  }
})
client.activate()
```

## 五、权限与数据范围

看板数据按工厂/工位隔离：

```java
@SubscribeMapping("/dashboard/plant/{plantId}")
public DashboardData plantDashboard(@DestinationVariable Long plantId, Principal principal) {
    UserContext user = SecurityUtils.get(principal);
    Assert.isTrue(user.getPlantIds().contains(plantId), "无该工厂数据权限");
    return dashboardService.snapshot(plantId);
}
```

## 六、预聚合表设计

```sql
dashboard_workorder_progress (工单进度)
├── work_order_id
├── plant_id
├── item_id
├── planned_qty
├── completed_qty
├── scrap_qty
├── status
├── update_time
└── ...

dashboard_equipment_status (设备状态汇总)
├── plant_id
├── total_count
├── running_count
├── downtime_count
├── alarm_count
└── ...

dashboard_quality (质量汇总)
├── plant_id
├── hour_bucket
├── inspected_qty
├── defect_qty
├── defect_rate
└── ...
```

### 刷新策略
- 报工/事件触发增量更新。
- 定时全量重算兜底（防漂移）。

## 七、大屏指标计算

```java
public class DashboardCalculator {

    public ProductionSummary summary(Long plantId) {
        return ProductionSummary.builder()
            .plannedQty(totalPlanned(plantId))
            .completedQty(totalCompleted(plantId))
            .completionRate(rate(completed, planned))
            .defectRate(rate(defect, inspected))
            .oeeAvg(avgOee(plantId))
            .build();
    }
}
```

## 八、性能优化

| 优化 | 说明 |
|------|------|
| 内存缓存 | 指标常驻内存，避免每次查 DB |
| 预聚合表 | 避免实时全表扫 |
| 只读库 | 查询走从库 |
| 限流 | 高频订阅限流 |
| 连接管理 | WebSocket 连接数监控、超时关闭 |
| 降采样 | 大屏按分钟聚合，非实时逐条 |

## 九、异常告警看板

异常事件实时推送 + 待处理列表：

```java
@EventListener
public void onAlarm(AlarmEvent event) {
    alertService.create(event);
    messagingTemplate.convertAndSend("/topic/dashboard/alert", event);
}
```

## 十、相关文档

- [设备数据采集](./tech-iot-collection-20260816.md)
- [设备 OEE 计算](./tech-oee-20260816.md)
- [报工高并发](./tech-concurrency-20260816.md)
- [MES 核心模块](../mes/mes-modules-20260816.md)
