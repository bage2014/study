# 变更记录：新增两个 RCA 演练服务并统一 demo- 命名

## 基本信息

| 项目 | 内容 |
|------|------|
| 变更编号 | CL-20261008-01 |
| 模块 | demo-order-service / demo-supplier-service |
| 创建日期 | 2026-10-08 |
| 状态 | 已完成 |

## 变更原因

RCA Agent 需要可复现、可观测的真实故障案例（OOM、请求超时等）来验证根因分析链路。原工程只有 agent 自身的 backend / web，缺少可注入故障的靶子服务。

## 变更内容

| 变更项 | 变更前 | 变更后 | 影响范围 |
|--------|--------|--------|----------|
| 新增订单服务 | 无 | `demo-order-service`（端口 8083） | 根 POM modules |
| 新增供应服务 | 无 | `demo-supplier-service`（端口 8084） | 根 POM modules |
| 统一命名 | 初版目录为 `order-service` / `supplier-service` | 重命名为 `demo-order-service` / `demo-supplier-service`（artifactId、name、spring.application.name 同步） | 目录名、模块 POM、application.yml、根 POM |
| 文档 | 无 | 新增服务说明与故障注入手册（docs/spec）、本变更记录（docs/changelog） | docs |

## 服务说明

- **demo-order-service（8083）**：下单前通过 RestClient 调供应服务校验库存；客户端 connect 1s / read 2s；提供 `/fault/timeout` 跨服务超时及 OOM / CPU / 线程故障注入。
- **demo-supplier-service（8084）**：内存库存（SKU-1/2/3）；提供 `/api/supply/slow`、`/flaky` 慢响应与随机故障接口及同样的本地故障注入。
- 两个服务均内置 Actuator：health、metrics、threaddump、heapdump、loggers。
- 零外部中间件依赖，clone 即可启动。

## 影响范围

- [x] 配置变更（根聚合 POM 新增两个 module）
- [ ] 接口变更（不涉及 rca-agent-backend 现有接口）
- [ ] 数据库变更（无数据库，内存存储）
- [ ] 前端页面变更（不涉及）

对 `rca-agent-backend`、`rca-agent-web` 的代码与现有接口零影响；仅在聚合工程中新增两个独立可执行模块。

## 风险评估

| 风险项 | 风险等级 | 缓解措施 |
|--------|----------|----------|
| `/fault/**` 故障注入接口被误带到生产 | 中 | 接口仅在独立 demo 模块；包名含 demo；手册已加醒目警告 |
| 端口 8083/8084 与本机其他服务冲突 | 低 | 启动前确认端口占用；可按需调整 application.yml |

## 验证结果

- `mvn -pl demo-order-service,demo-supplier-service clean test`：**BUILD SUCCESS**，两模块 contextLoads 均通过。
- 冒烟验证：正常下单 200（totalAmount=39.98）、库存不足 409、参数校验 400、`/fault/timeout` 精确 2.05s 返回 504、OOM/CPU/线程注入与 `/fault/reset` 全部正常。

## 相关文档

- [RCA Demo 服务说明与故障注入手册](../spec/rca-demo-服务说明与故障注入手册-2026-10-08.md)
