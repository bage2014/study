# 核心数据模型设计

> 创建时间：2026-08-16
> 模块：tech

MES/PLM 核心业务的数据模型设计思路，含主表、关系、关键设计决策。

## 一、模型总览

```
┌──────────┐    ┌──────────┐    ┌──────────┐
│  物料     │◄──►│   BOM    │◄──►│ 工艺路线  │
│  Item    │    │          │    │ Routing  │
└────┬─────┘    └────┬─────┘    └────┬─────┘
     │               │               │
     │               ▼               │
     │          ┌──────────┐         │
     │          │  工单    │◄────────┘
     │          │WorkOrder │
     │          └────┬─────┘
     │               │
     │               ▼
     │          ┌──────────┐
     └─────────►│  报工    │
                │  Report  │
                └────┬─────┘
                     │
                     ▼
                ┌──────────┐
                │ 质量记录  │
                └──────────┘
```

## 二、物料（Item）

### 表设计

```sql
item (物料主表)
├── id              主键
├── code            物料编码（唯一）
├── name            名称
├── spec            规格
├── category_id     分类
├── unit            单位
├── item_type       类型（原料/半成品/成品/包材/虚拟件）
├── status          状态（研发中/试制/量产/失效）
├── is_phantom      是否虚拟件
├── lot_managed     是否批次管理
├── serial_managed  是否序列号管理
├── revision        版本
├── plant_id        工厂
├── create_by/time
└── update_by/time
```

### 设计要点
- 编码唯一，业务键。
- 批次/序列号管理为独立标识，影响库存与追溯。
- 虚拟件标识影响 BOM 展开。

## 三、BOM

### 表设计

```sql
bom (BOM 头)
├── id
├── parent_item_id  父物料
├── bom_type        EBOM/MBOM/SBOM
├── version         版本号
├── status          草稿/审核中/已发布/失效
├── effect_from     生效日期
├── effect_to       失效日期
├── plant_id
└── ...

bom_line (BOM 行)
├── id
├── bom_id          所属 BOM
├── child_item_id   子物料
├── quantity        用量
├── loss_rate       损耗率
├── operation_id    投入工序（MBOM）
├── position_no     位置号
├── is_phantom      子件是否虚拟件
├── substitute_id   替代料
├── effect_from/to
└── ...
```

### 设计要点
- 头行分离，便于版本管理。
- 用量 + 损耗率分离，便于核算。
- 工序关联仅在 MBOM，EBOM 不存。
- 有效性按行控制（细粒度）。

### 版本与有效性
- 每次变更生成新 `bom` 记录（新 version）。
- 同一父物料同一时刻仅一个 `已发布` 版本。
- 工单下达时锁定 `bom_id`（快照）。

## 四、工艺路线（Routing）

```sql
routing (工艺路线头)
├── id
├── item_id         产品
├── version
├── status
└── ...

operation (工序)
├── id
├── routing_id
├── seq             工序序号
├── name            工序名
├── work_center_id  工作中心
├── standard_time   标准工时
├── description
└── ...

work_center (工作中心)
├── id
├── code
├── name
├── capacity        产能
└── ...
```

## 五、工单（WorkOrder）

```sql
work_order (工单)
├── id
├── code            工单号
├── item_id         产品
├── bom_id          锁定的 BOM 版本（快照）
├── routing_id      锁定的工艺版本（快照）
├── quantity        计划数量
├── completed_qty   完工数
├── scrap_qty       报废数
├── status          状态（见状态机）
├── plan_start/end
├── actual_start/end
├── source          来源（ERP同步/手工）
├── plant_id
└── ...
```

### 设计要点
- **版本快照**：`bom_id`/`routing_id` 锁定下达时版本，避免后续变更影响。
- **数量冗余**：完工/报废数冗余存储，便于查询，由报工聚合更新。
- **状态机字段**：status 受状态机守护。

## 六、报工（WorkReport）

```sql
work_report (报工记录)
├── id
├── work_order_id
├── operation_id    工序
├── work_center_id
├── operator_id     操作员
├── good_qty        合格数
├── bad_qty         不合格数
├── bad_code        不良代码
├── lot_no          批次
├── serial_no       序列号
├── report_time
├── consume_records 物料消耗（关联表）
└── ...
```

### 设计要点
- 报工为不可变事件流（append-only），便于追溯。
- 物料消耗单独表，记录批次/序列号。

```sql
work_report_consume (报工物料消耗)
├── id
├── work_report_id
├── item_id
├── lot_no          消耗批次
├── serial_no
├── quantity
└── ...
```

## 七、库存（WIP / 线边）

```sql
stock (库存)
├── id
├── item_id
├── plant_id
├── location_id     库位
├── lot_no
├── serial_no
├── quantity
├── status           可用/冻结/在检
└── ...

stock_transaction (库存事务)
├── id
├── item_id
├── tx_type          入库/出库/转移/冻结
├── quantity         正/负
├── ref_type         关联类型（工单/报工/领料）
├── ref_id
├── lot_no
├── balance_after    事务后余额（流水账）
├── tx_time
└── ...
```

### 设计要点
- **事务流（流水账）**：所有库存变动记事务，余额 = Σ事务。
- **余额冗余**：`stock` 表存当前余额，事务表存历史，定期对账。
- **批次/序列号**：库存维度含批次/序列号，支撑追溯。

## 八、质量记录

```sql
inspection (检验记录)
├── id
├── inspection_type  IQC/IPQC/OQC
├── item_id
├── lot_no / serial_no
├── work_order_id
├── operation_id
├── result           合格/不合格/让步
├── inspector_id
├── inspect_time
└── ...

inspection_item (检验项)
├── id
├── inspection_id
├── check_item_id    检验项定义
├── standard_value   标准值
├── actual_value     实测值
├── result
└── ...
```

## 九、追溯关系模型

追溯的本质是**事务链**：

```
物料批次 ──投入──► 报工 ──产出──► 成品批次/序列号 ──投入──► 下道报工
```

核心字段：
- 报工的 `consume_records` 记录投入批次（反向追溯起点）。
- 报工的 `lot_no/serial_no` 记录产出批次（正向追溯起点）。
- 通过 `work_order_id` + `operation_id` 串联工序链。

详见 [追溯查询实现](./tech-traceability-20260816.md)。

## 十、组织与权限

```sql
org (组织)
├── id
├── parent_id        树形
├── org_type         集团/公司/工厂/部门/班组
└── ...

user
├── id
├── username
├── org_id
└── ...

role
├── id
├── code
├── name
└── data_scope       数据范围（全部/本部门/本部门及下/自定义）

role_permission
├── role_id
├── permission_code

user_role
├── user_id
├── role_id
```

## 十一、通用字段约定

所有业务表统一含：
- `id`：雪花 ID 或自增。
- `create_by` / `create_time`
- `update_by` / `update_time`
- `deleted`：逻辑删除标识。
- `tenant_id`：多租户（如需）。
- `version`：乐观锁（如需）。

## 十二、设计原则总结

1. **版本快照**：工单锁定 BOM/工艺版本，避免变更冲击在制。
2. **事务流**：库存/状态变更走事务记录，可重算可审计。
3. **append-only**：报工、检验、操作日志不可变，支撑追溯。
4. **冗余换性能**：完工数、余额等冗余存储，定时对账。
5. **批次/序列号维度**：贯穿库存、报工、追溯。
6. **软删除 + 审计**：保留历史，可恢复。

## 十三、相关文档

- [框架与分层](./tech-framework-20260816.md)
- [工单状态机](./tech-workorder-state-20260816.md)
- [BOM 展开算法](./tech-bom-explode-20260816.md)
- [追溯查询实现](./tech-traceability-20260816.md)
