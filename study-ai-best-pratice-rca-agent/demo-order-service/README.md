# demo-order-service

RCA 演练用**订单服务**（调用方），端口 **8083**。下单前通过 RestClient 远程调用供应服务校验库存，用于演练请求超时级联、OOM、CPU 打满、线程泄漏等典型生产故障的根因分析（RCA）。

> 完整手册（架构、6 大故障场景演练、RCA 排查路径）见 [docs/spec/rca-demo-服务说明与故障注入手册-2026-10-08.md](../docs/spec/rca-demo-服务说明与故障注入手册-2026-10-08.md)

## 技术栈

- Java 17 / Spring Boot 3.3.0（web + actuator + validation）
- 内存存储，零外部中间件依赖

## 快速启动

```bash
# 需先启动供应服务（8084）
mvn -pl demo-supplier-service spring-boot:run
mvn -pl demo-order-service spring-boot:run
```

OOM 演练建议限堆启动：

```bash
java -Xmx128m -XX:+HeapDumpOnOutOfMemoryError -jar target/demo-order-service-0.0.1-SNAPSHOT.jar
```

## 接口速查

### 业务接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/orders` | 下单：`{"skuId":"SKU-1","quantity":2}`，先远程校验库存 |
| GET | `/api/orders` | 订单列表 |
| GET | `/api/orders/{id}` | 订单详情 |

### 故障注入（仅 demo，严禁用于生产）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/fault/timeout?millis=5000` | 调供应服务慢接口制造跨服务超时，~2s 返回 504 |
| GET | `/fault/oom?mb=10` | 一次性泄漏堆内存 |
| POST | `/fault/oom/leak/start?mbPerSecond=5` | 持续泄漏（`/stop`、`/clear` 管理） |
| GET | `/fault/cpu?seconds=10` | CPU 忙等占满一个核 |
| GET | `/fault/threads?count=200` | 线程泄漏（threaddump 见 `fault-leaked-thread-*`） |
| POST | `/fault/reset` | 清理全部注入故障 |

### 观测端点

`/actuator/health`、`/actuator/metrics`、`/actuator/threaddump`、`/actuator/heapdump`、`/actuator/loggers`

## 配置

| 配置 / 环境变量 | 默认值 | 说明 |
|------|------|------|
| `SUPPLIER_BASE_URL` | `http://localhost:8084` | 供应服务基地址 |
| 客户端超时 | connect 1s / read **2s** | 见 `RestClientConfig`，超时级联的关键阈值 |

## 目录结构

```
src/main/java/com/bage/study/rca/demo/order/
├── OrderServiceApplication.java    # 启动类
├── config/RestClientConfig.java    # 下游客户端与超时配置
├── controller/OrderController.java # 业务接口
├── controller/FaultController.java # 故障注入（含跨服务超时）
├── client/SupplierClient.java      # 供应服务客户端
├── service/OrderService.java       # 下单逻辑
├── fault/FaultInjector.java        # 本地故障注入器
└── model/                          # Order / DTO
```
