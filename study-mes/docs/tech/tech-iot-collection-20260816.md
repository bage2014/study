# 设备数据采集（OPC-UA / MQTT）

> 创建时间：2026-08-16
> 模块：tech

MES 与设备层集成、实时数据采集的工程实现思路。

## 一、采集需求

| 场景 | 数据 | 频率 |
|------|------|------|
| 设备状态 | 运行/停机/故障 | 秒级 |
| 计数 | 产量/不良数 | 事件触发 |
| 工艺参数 | 温度/压力/转速 | 秒~毫秒 |
| 报警 | 故障代码 | 事件触发 |
| 能耗 | 电/水/气 | 分钟级 |

## 二、协议选型

| 协议 | 场景 | 特点 |
|------|------|------|
| OPC-UA | PLC/CNC/通用设备 | 工业标准、可订阅、安全 |
| MQTT | IoT/网关汇聚 | 轻量、发布订阅、广域网 |
| Modbus TCP | 老旧 PLC | 简单、轮询 |
| S7 | 西门子 PLC | 专有协议 |
| SECS/GEM | 半导体 | 行业标准 |

**推荐**：设备层 OPC-UA / Modbus → 边缘网关汇聚 → MQTT → MES。

## 三、整体架构

```
PLC/设备 ──OPC-UA/Modbus──► 边缘网关 ──MQTT──► EMQX/Kafka ──► MES 采集服务
                                                            │
                                                            ▼
                                                    时序库 + 事件处理
```

### 分层职责

| 层 | 职责 |
|----|------|
| 设备层 | 产生数据 |
| 边缘网关 | 协议转换、本地缓存、断点续传 |
| 消息层 | MQTT broker / Kafka，解耦缓冲 |
| 采集服务 | 消费消息、解析、落库、触发事件 |
| 存储层 | 时序库（参数）+ 关系库（事件） |
| 业务层 | 状态更新、报警、追溯 |

## 四、数据模型

### 点位定义（Tag）

```sql
device_tag (采集点位)
├── id
├── tag_code          唯一编码
├── device_id         设备
├── tag_name          名称
├── data_type         INT/FLOAT/BOOL/STRING
├── address           PLC 地址（如 DB1.DBX0.0）
├── sampling_rate     采样频率 ms
├── tag_type          STATUS/COUNTER/PARAMETER/ALARM
└── unit              单位
```

### 时序数据

```
device_metric (时序库)
├── timestamp
├── tag_code
├── value (float)
└── quality           GOOD/BAD
```

存时序库（InfluxDB / TDengine / IoTDB），不要存 MySQL。

### 事件数据

```sql
device_event (设备事件)
├── id
├── device_id
├── event_type       STATUS_CHANGE / COUNT / ALARM
├── tag_code
├── value
├── occur_time
├── work_order_id    关联工单（若有）
└── ...
```

## 五、采集服务实现

### MQTT 消费

```java
@Service
public class MqttCollector {

    @Autowired private MetricWriter metricWriter;
    @Autowired private DeviceEventService eventService;

    @KafkaListener(topics = "device-data")
    public void onMessage(DeviceData data) {
        // 1. 时序数据落时序库
        metricWriter.write(data.getTagCode(), data.getValue(), data.getTimestamp());

        // 2. 关键事件触发业务
        switch (data.getTagType()) {
            case STATUS_CHANGE -> eventService.onStatusChange(data);
            case COUNT -> eventService.onCount(data);
            case ALARM -> eventService.onAlarm(data);
        }
    }
}
```

### 状态变更处理

```java
@Service
public class DeviceEventService {

    public void onStatusChange(DeviceData data) {
        DeviceStatus newStatus = parseStatus(data.getValue());
        Device device = deviceRepo.findOrThrow(data.getDeviceId());

        if (device.getStatus() != newStatus) {
            // 记录事件
            DeviceEvent event = DeviceEvent.of(device, STATUS_CHANGE, newStatus);
            eventRepo.save(event);

            // 更新状态
            device.setStatus(newStatus);
            deviceRepo.save(device);

            // 触发 OEE 计算、工单联动
            eventPublisher.publish(new DeviceStatusChangedEvent(device.getId(), newStatus));
        }
    }

    public void onCount(DeviceData data) {
        // 计数器累加，关联工单
        Long workOrderId = bindingService.findCurrentWorkOrder(data.getDeviceId());
        countService.increment(data.getDeviceId(), data.getTagCode(), data.getValue(), workOrderId);
    }
}
```

## 六、设备-工单绑定

采集的计数需关联到正在生产的工单：

```java
@Service
public class DeviceWorkOrderBinding {

    // 设备当前绑定的工单
    public Long findCurrentWorkOrder(Long deviceId) {
        return bindingRepo.findActiveByDevice(deviceId)
            .map(DeviceBinding::getWorkOrderId)
            .orElse(null);
    }

    // 工单开工时绑定
    @EventListener
    public void onWorkOrderStart(WorkOrderStartedEvent event) {
        WorkOrder wo = workOrderRepo.findOrThrow(event.workOrderId());
        bindingRepo.save(new DeviceBinding(wo.getDeviceId(), wo.getId()));
    }

    // 工单完工时解绑
    @EventListener
    public void onWorkOrderComplete(WorkOrderCompletedEvent event) {
        bindingRepo.unbind(event.workOrderId());
    }
}
```

## 七、断点续传

边缘网关网络断开时本地缓存，恢复后补传：

```
1. 网关本地 SQLite/文件缓存
2. 网络恢复后按序补传
3. MES 按时间戳去重（幂等）
4. 时序库支持乱序写入
```

## 八、高频写入优化

| 优化 | 说明 |
|------|------|
| 批量写时序库 | 攒一批写入，非逐条 |
| 时序库选型 | InfluxDB/TDengine，别用 MySQL |
| 冷热分离 | 热数据 7 天，冷数据归档 |
| 降采样 | 历史数据按分钟聚合存储 |
| 丢弃低质量 | quality=BAD 的可不入库 |

## 九、设备指令下发

不仅采集，还需反向控制（参数下发、启停）：

```java
@Service
public class DeviceCommandService {

    @Autowired private MqttGateway mqtt;

    public void sendParameter(Long deviceId, String tagCode, Object value) {
        DeviceCommand cmd = DeviceCommand.of(deviceId, tagCode, value);
        // 发到设备主题
        mqtt.send("device/" + deviceId + "/cmd", cmd.toJson());
        // 记录指令日志
        commandLogRepo.save(cmd);
    }
}
```

## 十、设计要点

| 要点 | 说明 |
|------|------|
| 协议网关解耦 | 设备协议与业务解耦 |
| 时序与事件分离 | 参数走时序库，事件走关系库 |
| 幂等消费 | 重复消息安全 |
| 断点续传 | 网络抖动不丢数据 |
| 设备-工单绑定 | 计数关联工单 |
| 指令可追溯 | 下发指令记录日志 |

## 十一、相关文档

- [实时看板与数据聚合](./tech-dashboard-20260816.md)
- [设备 OEE 计算](./tech-oee-20260816.md)
- [报工高并发](./tech-concurrency-20260816.md)
- [MES 核心模块](../mes/mes-modules-20260816.md)
