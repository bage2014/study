# 框架选型与分层架构

> 创建时间：2026-08-16
> 模块：tech

MES/PLM 类工业系统的技术框架选型与分层架构思路。面向中小规模、可演进的实现。

## 一、技术栈选型

| 层 | 选型 | 理由 |
|----|------|------|
| 前端 | Vue 3 + TypeScript + Element Plus | 生态成熟、表单/表格场景丰富 |
| 构建 | Vite | 快速构建 |
| 状态 | Pinia | 轻量、TS 友好 |
| 后端 | Java 17 + Spring Boot 3 | 工业系统主流、稳定 |
| ORM | MyBatis-Plus | 灵活 SQL、动态查询 |
| 数据库 | MySQL 8（生产）/ H2（开发） | 主流关系型 |
| 缓存 | Redis | 字典、会话、分布式锁 |
| 消息 | Kafka / RocketMQ | 事件驱动、跨系统同步 |
| 工作流 | Flowable / Camunda | BPMN、可视化 |
| 文件 | MinIO | 自建对象存储 |
| 网关 | Spring Cloud Gateway | 统一入口、鉴权 |
| 注册中心 | Nacos | 配置 + 注册 |
| 鉴权 | Sa-Token / Spring Security | 简单可控 |
| 监控 | Prometheus + Grafana | 指标 |
| 链路 | SkyWalking | 跨服务追踪 |
| 日志 | ELK / Loki | 集中检索 |

## 二、后端分层架构

经典 DDD 轻量分层，避免过度设计。

```
┌─────────────────────────────────────────────┐
│              Interface 接口层                │
│   Controller / DTO / Validator / 异常处理    │
├─────────────────────────────────────────────┤
│            Application 应用层                │
│   ApplicationService / 事务边界 / 编排       │
│   事件发布 / 权限校验                         │
├─────────────────────────────────────────────┤
│              Domain 领域层                   │
│   Entity / ValueObject / DomainService      │
│   Repository 接口 / 领域事件                 │
├─────────────────────────────────────────────┤
│          Infrastructure 基础设施层           │
│   RepositoryImpl / Mapper / Cache / MQ      │
│   第三方集成 / 工具                          │
└─────────────────────────────────────────────┘
```

### 各层职责

| 层 | 职责 | 禁止 |
|----|------|------|
| Interface | 协议转换、参数校验、统一响应 | 写业务逻辑 |
| Application | 用例编排、事务、事件发布 | 直接操作 DB |
| Domain | 业务规则、状态机、领域模型 | 依赖框架 |
| Infrastructure | 技术实现、持久化、外部对接 | 含业务规则 |

## 三、包结构示例

```
com.example.mes
├── interfaces        # 接口层
│   ├── rest          # Controller
│   ├── dto           # 入参/出参
│   └── advice        # 全局异常
├── application       # 应用层
│   ├── workorder     # 工单应用服务
│   ├── bom           # BOM 应用服务
│   └── event         # 事件处理
├── domain            # 领域层
│   ├── workorder     # 工单聚合
│   │   ├── model     # 实体/值对象
│   │   ├── service   # 领域服务
│   │   ├── event     # 领域事件
│   │   └── repository # 仓储接口
│   └── shared        # 共享内核
└── infrastructure    # 基础设施层
    ├── persistence   # Mapper/RepositoryImpl
    ├── mq            # 消息
    ├── cache         # Redis
    └── integration   # 外部系统集成
```

## 四、关键架构决策

### 1. 单体 vs 微服务

| 规模 | 推荐 |
|------|------|
| 单工厂、<30 万行 | 单体模块化（按业务包划分） |
| 多工厂、跨地域 | 微服务（按子系统拆分：MES/PLM/WMS） |
| 起步阶段 | 先单体，预留拆分边界（按领域包+事件） |

**建议**：起步单体模块化，按领域包强约束边界，未来按领域拆服务。

### 2. 领域事件驱动

- 领域内：同步发布，事务内处理。
- 跨领域/跨系统：异步发布（MQ），最终一致。

```
工单完工（领域事件）
   ├─ 同步：更新 WIP（同事务）
   └─ 异步：通知 ERP（MQ）
            通知 WMS 入库（MQ）
```

### 3. 事务边界

- 应用服务为事务边界（`@Transactional`）。
- 跨聚合修改通过领域事件解耦。
- 跨系统通过 MQ + 本地消息表保证最终一致。

### 4. 读写分离（CQRS 轻量版）

- 写：聚合根 + 仓储，强一致。
- 读：复杂查询直接 Mapper + 视图/DTO，绕过聚合。
- 报表/看板：独立查询服务，可走只读库。

### 5. 幂等设计

- 接口层：业务键 + 幂等表。
- MQ 消费：消息 ID 去重。
- 关键操作：状态机校验（同状态重复操作天然幂等）。

## 五、前端架构

```
src/
├── api/              # 接口封装
├── views/            # 页面（按业务模块）
├── components/       # 公共组件
├── composables/      # 组合式函数
├── store/            # Pinia 状态
├── router/           # 路由（动态菜单）
├── utils/            # 工具
└── types/            # TS 类型
```

### 通用前端能力
- 动态菜单 + 路由（按权限加载）
- 通用 CRUD 表格组件（列表/查询/分页/导出）
- 表单引擎（基于 schema 配置生成）
- 字典缓存
- 全局异常/loading 拦截

## 六、通用后端能力

| 能力 | 实现 |
|------|------|
| 统一响应 | `Result<T>` + 全局异常处理 |
| 参数校验 | JSR-303 + 自定义注解 |
| 审计字段 | MyBatis 拦截器自动填充 createBy/updateBy/createTime |
| 逻辑删除 | `@TableLogic` |
| 多租户 | 字段隔离 + 拦截器 |
| 数据权限 | SQL 拦截器注入条件（见 tech-permission） |
| 操作日志 | AOP + 注解 |
| 限流 | Sentinel / Bucket4j |
| 缓存 | `@Cacheable` + Redis |
| 国际化 | MessageSource |

## 七、部署架构

```
Nginx (静态 + 反向代理)
   ├── Spring Boot 应用（多实例）
   ├── MySQL（主从）
   ├── Redis（哨兵/集群）
   ├── Kafka（集群）
   └── MinIO
```

- 应用水平扩展，无状态。
- 会话/缓存走 Redis。
- 静态资源 CDN/Nginx。

## 八、演进路径

1. **阶段一**：单体模块化，覆盖核心 MES（工单/报工/质量/追溯）。
2. **阶段二**：引入 MQ，对接 PLM/ERP。
3. **阶段三**：按领域拆服务，引入网关/注册中心。
4. **阶段四**：数据中台、报表平台、AI 质检。

## 九、相关文档

- [核心数据模型](./tech-data-model-20260816.md)
- [工单状态机](./tech-workorder-state-20260816.md)
- [集成架构](../integration/integration-architecture-20260816.md)
- [管理系统通用能力](../management/management-overview-20260816.md)
