# 变更记录：demo 服务接入 H2 持久化 + Web UI 设计规范与测试

## 基本信息

| 项目 | 内容 |
|------|------|
| 变更编号 | CL-20261013-02 |
| 模块 | demo-order-service / demo-supplier-service / rca-agent-web |
| 创建日期 | 2026-10-13 |
| 状态 | 已完成 |

## 变更原因

1. demo 两个服务的业务数据（订单、库存）为内存存储，重启丢失，且与「全部模块统一依赖 H2」的要求不一致。
2. rca-agent-web 缺少成文的 UI 设计规范与 UI 自动化测试。

## 变更内容

| 变更项 | 变更前 | 变更后 | 影响范围 |
|--------|--------|--------|----------|
| demo-order-service 存储 | `ConcurrentHashMap` 内存订单 | H2（`rca-demo-order`）+ JPA 实体 `Order` + `OrderRepository`，ID 改数据库自增 | 仅存储层，接口行为不变 |
| demo-supplier-service 库存 | `Map.of` 静态内存库存 | H2（`rca-demo-supplier`）+ `Inventory` 实体 + `InventoryRepository` + 启动种子数据（SKU-1/2/3 与原来一致） | 仅存储层，接口行为不变 |
| 两模块依赖 | 无数据库依赖 | `spring-boot-starter-data-jpa` + `com.h2database:h2`(runtime)，`application.yml` 增加数据源与 JPA 配置 | pom / yml |
| UI 设计规范 | 无 | 新增 [rca-agent-web-UI设计规范](../spec/rca-agent-web-UI设计规范-2026-10-13.md)，基于 common-ui-design-spec 基线 | 文档 |
| UI 自动化测试 | 无 | 新增 Playwright 测试（`rca-agent-web/e2e/`，15 条用例，API 打桩不依赖后端），`npm run test:e2e` | rca-agent-web |

## 影响范围

- [x] 数据库变更（demo 两模块新增 H2 内嵌库）
- [x] 前端页面变更（仅新增测试与规范，页面无改动）
- [ ] 接口变更（无，接口行为保持不变）

## 验证结果

| 验证项 | 结果 |
|--------|------|
| `mvn -pl demo-order-service,demo-supplier-service clean test` | BUILD SUCCESS（各 1 个 contextLoads 通过） |
| 冒烟：库存种子 SKU-1=1000/19.99、SKU-2=100/5.50、SKU-3=0/99.00 | ✅ |
| 冒烟：下单 SKU-1×2 → 200，totalAmount=39.98，DB 自增 id=1 | ✅ |
| 冒烟：下单 SKU-3 → 409 库存不足；非法参数 → 400 | ✅ |
| Playwright 15 条 UI 用例 | 见 [UI 测试报告](../report/rca-agent-web-UI测试报告-2026-10-13.md) |

## 风险与注意

- H2 为 `mem` 模式（`DB_CLOSE_DELAY=-1`），进程内持久、重启清空——demo 演练场景下符合预期；如需跨重启保留改 file 模式即可。
- 发现曾运行中的旧进程（改名前 `order-service`/`supplier-service` 旧 jar、9 月旧后端）占用 8082/8083/8084，已全部清理后以新代码重启。

## 相关文档

- [rca-agent-录制数据H2持久化-2026-10-13](rca-agent-录制数据H2持久化-2026-10-13.md)
- [rca-agent-web-UI设计规范-2026-10-13](../spec/rca-agent-web-UI设计规范-2026-10-13.md)
- [rca-agent-web-UI测试报告-2026-10-13](../report/rca-agent-web-UI测试报告-2026-10-13.md)
