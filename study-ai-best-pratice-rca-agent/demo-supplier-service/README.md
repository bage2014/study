# demo-supplier-service

RCA 演练用**供应服务**（被调方），端口 **8084**。提供内存库存查询、人为慢响应与随机故障接口，作为订单服务的下游，用于构造请求超时、错误传播等 RCA 场景。

> 完整手册（架构、6 大故障场景演练、RCA 排查路径）见 [docs/spec/rca-demo-服务说明与故障注入手册-2026-10-08.md](../docs/spec/rca-demo-服务说明与故障注入手册-2026-10-08.md)

## 技术栈

- Java 17 / Spring Boot 3.3.0（web + actuator）
- 内存库存，零外部中间件依赖

## 快速启动

```bash
mvn -pl demo-supplier-service spring-boot:run
# 或
java -jar target/demo-supplier-service-0.0.1-SNAPSHOT.jar
```

## 接口速查

### 业务接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/supply/check?skuId=&quantity=` | 库存查询，毫秒级返回 |
| GET | `/api/supply/slow?millis=3000` | 人为 sleep（封顶 60s），模拟慢 SQL / 慢下游 |
| GET | `/api/supply/flaky` | 约 50% 概率 500，模拟不稳定节点 |

内置库存：SKU-1（1000 件 / 19.99）、SKU-2（100 件 / 5.50）、SKU-3（0 件 / 99.00）。

### 故障注入（仅 demo，严禁用于生产）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/fault/oom?mb=10` | 一次性泄漏堆内存 |
| POST | `/fault/oom/leak/start?mbPerSecond=5` | 持续泄漏（`/stop`、`/clear` 管理） |
| GET | `/fault/cpu?seconds=10` | CPU 忙等占满一个核 |
| GET | `/fault/threads?count=200` | 线程泄漏（threaddump 见 `fault-leaked-thread-*`） |
| POST | `/fault/reset` | 清理全部注入故障 |

### 观测端点

`/actuator/health`、`/actuator/metrics`、`/actuator/threaddump`、`/actuator/heapdump`、`/actuator/loggers`

## 目录结构

```
src/main/java/com/bage/study/rca/demo/supplier/
├── SupplierServiceApplication.java   # 启动类
├── controller/SupplyController.java  # 库存查询 / slow / flaky
├── controller/FaultController.java   # 故障注入
├── service/SupplyService.java        # 内存库存
├── fault/FaultInjector.java          # 本地故障注入器
└── model/SupplyCheckResult.java
```
