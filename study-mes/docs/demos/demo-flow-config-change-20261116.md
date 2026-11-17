# Demo P0-2 ｜ Ⅰ类技术状态更改：轴承换型对在制 19 套飞行件的影响

> 创建时间：2026-11-16
> 模块：demos

贯穿样例见 [demo-case-sada-20261116.md](./demo-case-sada-20261116.md)，本篇编码、SN、LOT、单号、日期、数量取自该文档第二/四/五/九/十章。本篇扩展的 3 个 PO 号、1 个拆换工单号与 2 个事件名在步骤内标注"待回写"。金额口径沿用 [P0-3](./demo-flow-qualification-gate-20261116.md) 步骤 9 的单套 268.50 万元与轴承 3.80 万元/件，主数据未约定单价，本篇推演值在 §5 集中标注。

## 0. 场景背景

2026-08-17，26B 型号初样阶段遗留的 −30℃ 低温试验数据复核出结论：`2JD01000043` 角接触球轴承 71904（接触角 15°、钢球）在 −30℃ 下预紧力漂移 0.18~0.26 kN，导致 SADA 启动力矩超差 12%，超出厂指标。08-20 受理技术状态更改请求 `ECR-26B-20260820-0007`，申请改用 71905（接触角 20°、氮化硅陶瓷球，新编码 `2JD01000047`）。因更改同时改变**配合尺寸与互换性**，按厂内技术状态管理规范判为**Ⅰ类更改，须报用户代表批准**。`CCB-26B-G5-003` 决议通过 → `ECN-26B-20260908-0003` → `ECO-26B-20260909-0012` → 2026-09-10 00:05 生效。

此刻 `WO-26B-20260715-0101` 上有 19 套在制（投料 2026-07-15）：已压装并已通过环试+老炼 3 套、已压装未取得有效试验数据 5 套、未压装 11 套。不做的后果：19 套按 71904 交付，整星在轨首期 −30℃ 启动力矩超差风险由飞行件承担，且出厂基线与实物谱系不一致，FCA/PCA 无法通过、用户代表拒签履历本。

本篇处置结论**三策略并存**，这是它与其他 6 篇最大的差别：

| 策略 | 套数 | 对象 | 技术状态结论 |
|------|------|------|-------------|
| 保留不拆 | 3 | F-001、F-002、F-003 | 试验数据有效性优先，附条件跟踪；`item_rev` RevA 有效性必须保留 |
| 拆换 | 5 | F-004、F-005、F-006、F-007、F-012 | 走拆换工单，旧轴承退库转锁定后退供应商 |
| 顺次切换 | 11 | F-008~F-011、B-001~B-005、Y-001、Y-002 | 未压装，直接按 RevB 发料 |

影响对象总览：`item`/`item_rev` 各 2 行、`bom_line` 2 行、`change_request`/`ccb_decision`/`change_notice`/`change_action` 共 12 行、`change_impact` 22 行、`baseline` 2 行 + `baseline_item` 16 行、`work_order` 2 行、`work_order_operation` 31 行、`work_order_material` 18 行、`sn_master` 19 行、`sn_genealogy` 22 行、`sn_history` 10 行、`sn_doc` 19 行、`inv_lot` 5 行、`inv_txn` 8 行、`po_line` 4 行、`qc_task` 6 行、`approval_record` 7 行、`outbox_msg` 9 行。

## 1. 前置状态快照

### 1.1 单据与技术状态（2026-08-20 10:00）

| 对象 | 业务键 | 关键字段现值 |
|------|--------|-------------|
| 更改请求 | `ECR-26B-20260820-0007` | `status: NEW`、`change_class=null`（待判）、`applicant=机构主任设计师` |
| 生效基线 | `BL-26B-SADA-Z-RevA` | `status=FROZEN`、`effect_from=2026-07-15`、`bom_line_cnt=16`、`op_cnt=11`（P0-3 步骤 3 建） |
| 物料 Rev | `item_rev(2JD01000043, RevA)` | `lifecycle=RELEASED`、`effect_from=2026-07-15`、`change_order_id=null` |
| BOM 行 | `BOM-SADA-26B-RevA` 行 90 | `child_item=2JD01000043`、`qty=2`、`loss_rate=5%`、`pos='上支点/下支点'`、`effective_to=9999-12-31` |
| 工艺路线 | `ROUT-SADA-26B-RevA` | `op50 峰值力 8.0~12.0 kN`、`过盈量 0.030~0.045 mm`、`曲线 200 Hz` |
| 生产工单 | `WO-26B-20260715-0101` | `status=STARTED`、`qty=19`、`bom_id=BOM-SADA-26B-RevA`、`ecn_hold_flag=0` |
| 相关质量问题 | `NCR-26B-20260811-004` | F-012 工序 50 过盈量 0.048 超上限，工序内参数返工，轴承实物未损伤（见 P0-3 步骤 11） |

### 1.2 19 套进度分布（本篇圈定口径，2026-08-20）

| 组 | SN（`sn_master.sn`） | `sn_class` | `sn_history.status` | 工序 50 | 工序 80/90 | 轴承装机 |
|----|--------------------|-----------|--------------------|---------|-----------|---------|
| G1 已过试验 | F-001 / F-002 / F-003 | F | `PASSED` | `COMPLETED` | 均 `COMPLETED` | 6 件 RevA |
| G2 已压装未试 | F-004 / F-006 / F-007 / F-012 | F | `IN_ASSY` | `COMPLETED` | `PENDING` | 8 件 RevA |
| G2′ 试验中失效 | F-005 | F | `IN_ASSY`（08-25 起 `IN_TEST`，08-27 失效，见 P0-4 `ZL-26B-20260827-001`） | `COMPLETED` | `IN_PROGRESS` | 2 件 RevA |
| G3 未压装 | F-008~F-011、B-001~B-005、Y-001~Y-002 | F/B/Y | `IN_ASSY` | `PENDING` | `PENDING` | 0 |
| 合计 | 19 行 | 12F+5B+2Y | 有效节点 19（A1） | 8 套完工 | 3 套完工 | **16 件** |

P0-3 步骤 11 的"工序 50 投产 19 = 18 + 1"是全批到 2026-10-31 的累计报工口径，与本表 08-20 时点口径不矛盾；两口径在 §4 Q2 分别校验。

### 1.3 轴承 `2JD01000043` 数量对齐（本篇起始口径）

主数据 §五 记"库存 38 / 缺口 2"，那是 **2026-07-14 17:00 的批次全量**，不是本篇起始时点的可用量。按 P0-3 步骤 8 的前序流水对齐后：

| LOT | 来源单号 | 收货日 | 数量 | 2026-08-20 状态 |
|-----|---------|-------|------|----------------|
| `LOT-BRG-26B-01` | `PO-26B-0097`（待回写） | 2026-07-10 | 38 | 已由 `MI-20260715-0311` 全部发线边 WH-03；装机 16、线边未耗 22 |
| `LOT-BRG-26B-02` | `PO-26B-0098`（待回写，缺口 2 件补采） | 2026-07-19 | 2 | 在库 WH-01，未发料（PRR 项 9 于当日核销，见 P0-3 步骤 10） |
| 收入合计 | | | **40** | = 装机 16 + 未装机 24，与主数据"需求 40"（19×2×1.05=39.9 取 40）一致 |

本篇要写清的"旧轴承 38 件分配"即 `LOT-BRG-26B-01` 的 38 件：装机 16 + 线边未耗 22，去向见步骤 12 守恒表。在途 PO 行：`po_line where item='2JD01000043' and qty_open > 0` = **0 行**，故影响评估的"在途取消损失"为 0。

## 2. 逐步推演

### 步骤 1 ｜ ECR 受理与更改分类初判

触发：2026-08-20 10:00 机构主任设计师在 PLM"技术状态更改请求"页提交，附低温试验报告 `TEST-26B-LOWT-0105`（扩展单号）与启动力矩超差 12% 的实测曲线。
校验：`change_request` 必填 `change_reason`、`affected_item`、`expect_effect_date`；缺 `test_report` 关联则停 `NEW` 并推待办给试验主任；同一 `affected_item` 已有未闭环 ECR 时拒绝重复受理（唯一键 `item+status<>CLOSED`）。
单据：`ECR-26B-20260820-0007` `status: NEW → ASSESSED`（08-20 10:22 分类初判），`change_class: null → Ⅰ`（待 CCB 确认）。

| 表 | 行 | 变化 |
|----|----|------|
| `change_request` | `ECR-26B-20260820-0007` `[+]` | `title=MGU 主轴轴承 71904→71905`、`affected_item=2JD01000043`、`parent_item=4JD00000013`、`reason_code=低温预紧力漂移`、`change_class=Ⅰ`、`need_user_rep=Y`、`expect_effect_date=2026-09-10` |
| `change_request` | 同上（08-28 评估回填） | `assess_ref: null → IE-26B-20260828-001`、`impact_sn_cnt: 0 → 19`、`impact_lot_cnt: 0 → 2`、`impact_po_cnt=0`、`status: NEW → ASSESSED` |
| `ccb_decision` | `CCB-26B-G5-003` `[+]` | `meeting_date=2026-09-02`、`ecr_cnt=3`、`gate=G5`、`status=SCHEDULED` |

下游：PLM 内部待办推 CCB 秘书与工艺、质量、采购、计划 4 方代表；无跨系统事件（ECR 阶段不进 MES）。
守恒：A7 前置——本步不改任何有效性区间，`item_rev(2JD01000043,RevA).lifecycle` 仍 `RELEASED`，有效 `child_item_rev` 每 `pos` 恰 1 个。

### 步骤 2 ｜ 影响评估取数（`where-used` 反查 + 在制 SN 圈定 + 库存批次 + 在途 PO）

触发：2026-08-21 变更工程师在 PLM"影响评估"工作台点"取数"，四个页签分别调 `bom_line` 反查、MES 在制快照、WMS 批次、ERP 在途。
校验：取数必须带 `as_of` 时点（本篇 `2026-08-20 24:00`），无时点的取数结果不允许写入 `change_impact`（防止把已交付件算进拆换范围，见 §3.1）；`where-used` 递归深度上限 20（A6 同口径）。
单据：影响评估报告 `IE-26B-20260828-001`（PLM 附件，无状态机）。

按 SN 圈定受影响在制件（本篇主线，可执行）：

```sql
SELECT s.sn AS sada_sn, s.sn_class, s.status AS sn_status, s.cur_op,
       op50.status AS op50_status, g.pos AS brg_pos, g.child_lot, g.valid_to
FROM   sn_master s
JOIN   work_order w           ON w.code = 'WO-26B-20260715-0101' AND s.wo_id = w.id
JOIN   work_order_operation op50
       ON op50.wo_id = w.id AND op50.sn = s.sn AND op50.op_seq = 50
LEFT   JOIN sn_genealogy mg   ON mg.parent_sn = s.sn AND mg.child_item = '4JD00000013'
                             AND mg.valid_to IS NULL
LEFT   JOIN sn_genealogy g    ON g.parent_sn = mg.child_sn AND g.child_item = '2JD01000043'
WHERE  s.status <> 'SHIPPED'
ORDER  BY op50_status DESC, s.sn;
-- 期望 19 行：op50=COMPLETED 8 行（g 命中 16 行 pos 级）、PENDING 11 行（g 为空）
```

| 取数口径 | 结果 | 处置输入 |
|---------|------|---------|
| `where-used` 多层反查 | `2JD01000043 → 4JD00000013 → 5JD00000007 → 5BZ00000002 → 6ZH00000001`，4 层 5 个上级件号 | 传导范围含总装，见步骤 13 |
| 在制 SN（上面 SQL） | 19 套 / 38 个 `pos` 位，其中已装机 16 | 步骤 9 逐 SN 登记 |
| 已交付与鉴定件 | `SADA-26B-Z-D-001` 装机 2 件、`status=SHIPPED` 之外仍挂 `RESEARCH`；**不列入本批拆换**，另走"不拆换件继续使用"通道 | A7 前提，步骤 4 |
| 库存批次 | `LOT-BRG-26B-01` 未耗 22、`LOT-BRG-26B-02` 2，合计 24 件未装机 | 步骤 12 分配 |
| 在途 PO | 0 行 | 无取消费用；旧料可全量退供 |

下游：`change_impact` 生成 22 条候选（19 SN + 2 LOT + 1 WO），状态全 `DRAFT`；无外发事件。
守恒：A2 候选口径核算 19 套 × 2 位 = 38 个 `pos` 位 = `bom_line` 行 90 `qty=2 × 19`；A6 递归 CTE 无环、`depth=4 < 20`。

### 步骤 3 ｜ CCB 决议与Ⅰ类更改用户代表批准

触发：2026-09-02 14:00 CCB 第 G5 组会议（设计、工艺、质量、采购、计划、用户代表、试验主任 7 方），入口为 PLM"更改委员会决议"。
校验：Ⅰ类更改判据三条命中即升Ⅰ类——① 改变配合尺寸（71905 外圈与 MGU 轴承座孔配合由 `φ90 H7/js5` 变 `φ90 H7/k5`）② 改变互换性（不可与 RevA 混装）③ 影响接口或安全特性。`need_user_rep=Y` 时，无用户代表签署则 `change_notice.status` 不允许越过 `SIGNED`，ECN 编号可预分配但不能进入 `RELEASING`。
单据：`CCB-26B-G5-003` `status: SCHEDULED → DECIDED`（09-02 17:20）；决议结论"通过，附三条执行要求"。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `ccb_decision` | `CCB-26B-G5-003` | Δ | `decision: null → APPROVED_WITH_CONDITION`、`change_class: Ⅰ候选 → Ⅰ`、`condition_cnt: 0 → 3`、`vote: 7/7` |
| `ccb_decision_item` | `(CCB-26B-G5-003, 2JD01000043→2JD01000047)` | [+] | `switch_strategy=日期切换+按基线切片`、`effective_date=2026-09-10`、`old_part_disposal=RTV` |
| `change_impact` | 3 行（F-001/F-002/F-003） | Δ | `strategy: DRAFT → KEEP_NO_DISASSEMBLE`、`reason=试验数据有效性优先`、`cond_watch=Y` |
| `change_impact` | 5 行（F-004/005/006/007/012） | Δ | `strategy: DRAFT → DISASSEMBLE_REWORK`、`wo_code=WO-26B-20260910-0163` |
| `change_impact` | 11 行（G3） | Δ | `strategy: DRAFT → FOLLOW_ON_SWITCH`、`kit_batch` 拆为 4 套（首批）+ 7 套（二批） |
| `approval_record` | `(ECR-26B-20260820-0007, CCB决议签署)` | [+] | `role=CCB秘书+6方代表`、`result=PASS`、`approved_at=2026-09-02 17:20` |
| `approval_record` | `(ECN-26B-20260908-0003, 用户代表批准)` | [+] | `role=用户代表（卫星总师系统驻厂）`、`result=PASS`、`approved_at=2026-09-04 11:35`、`opinion=不拆换件须附条件跟踪，RevB 件补做低温试验` |

下游：批准结论写入 `change_notice.status: DRAFT → SIGNED → USER_APPROVED`；同事务写 `outbox_msg`（`ECN_SIGNED` 扩展事件，幂等键 `ecnNo:SIGNED`）→ ERP 采购与 QMS 试验策划提前启动，MES 本步不收（生效前不动在制）。
守恒：A1 未动履历节点，19 套有效 `sn_history` 仍 19 行；`change_impact` 22 行覆盖度 = 19 SN + 2 LOT + 1 WO，与步骤 2 取数行数一致，无漏无重。

### 步骤 4 ｜ 新物料建码与旧件生命周期分置（`item` / `item_rev`）

触发：2026-09-05 物料工程师在 PLM 申请 `2JD01000047`（11 位定长：大类 2 + 专业 JD + 小类 010 + 流水 00047），并把 `2JD01000043` 走失效流程。
校验：编码查重命中相似件 `2JD01000043`，按 [item-master](../concepts/item-master-20260906.md) 的规则返回推荐但不阻断（新件为不同技术状态，必须新建）；关重件★属性必须携带 `复验必检`、`需 MTR`、`追溯=LOT`；**失效前校验三条件（无有效 BOM 引用、无在制工单、库存清零）此处不满足**——库存尚有 24 件未装机、3 套装机件永久挂 RevA，故走例外审批：`lifecycle` 与"有效性"分置。
单据：`item_rev` 生命周期变更走 `ECO-26B-20260909-0012` 行 1。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `item` | `2JD01000047` | [+] | `name=角接触球轴承 71905`、`spec=接触角20°/氮化硅陶瓷球/φ90 H7/k5`、`critical_part=Y★`、`lot_managed=Y`、`serial_managed=N`、`purchase_lead_time=90 d`、`inspection_type=复验必检`、`status=RELEASED` |
| `item` | `2JD01000043` | Δ | `status: RELEASED → OBSOLETE`（2026-09-30 退库完成后置，非生效日）、`obsoleted_by=2JD01000047` |
| `item_rev` | `item_rev(2JD01000047, RevA)` | [+] | `lifecycle=RELEASED`、`effect_from=2026-09-10`、`effect_to=9999-12-31`、`change_order_id=ECN-26B-20260908-0003` |
| `item_rev` | `item_rev(2JD01000043, RevA)` | Δ | `lifecycle: RELEASED → CHANGED → OBSOLETE`、**`effect_to` 保持 `9999-12-31`（有效性保留）**、`validity_scope: ALL → EXISTING_ONLY`、`change_order_id=ECN-26B-20260908-0003` |
| `approval_record` | `(item 2JD01000043, OBSOLETE 例外放行)` | [+] | `reason=RevA 供 3 套不拆件与 2 件保障备件继续使用`、`approver=设计+质量+物资 三方`、`result=PASS` |

下游：`ITEM_RELEASED`（幂等键 `2JD01000047:RevA`）→ ERP 建采购信息记录、MES 刷新可引用件号、WMS 建批次属性模板；`status=OBSOLETE` 单独发 `ITEM_CHANGED`，MES 消费时**只置"禁止新发料"，不删主档**。
守恒：A7 前提成立——RevA `item_rev` 有效性未被关闭，3 套不拆件的 `sn_genealogy.child_item_rev='A'` 行仍是合法有效记录；若此处把 `effect_to` 一并置为 2026-09-10，则 A7 对 G1 组必然报 6 个 `pos` 位违规。

### 步骤 5 ｜ ECN 发布与 ECO 执行单下达

触发：2026-09-08 16:00 变更工程师发布 `ECN-26B-20260908-0003`；09-09 09:00 计划、工艺、采购、检验在 ECO 上认领 8 条执行动作。
校验：`RELEASING` 前置断言——`approval_record` 中用户代表记录必须存在且 `result=PASS`；`change_action` 每条必须有 `owner_sys` 与 `due_date`；`change_impact` 无 `DRAFT` 残留；三条都不满足则 ECN 停 `USER_APPROVED`，接口返回 `E_ECN_PRECOND_MISSING`。
单据：`ECN-26B-20260908-0003` `status: USER_APPROVED → RELEASING`（09-08 16:00）；`ECO-26B-20260909-0012` `[+]` `status=OPEN`。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `change_notice` | `ECN-26B-20260908-0003` | [+] | `ecn_type=Ⅰ`、`item_from=2JD01000043`、`item_to=2JD01000047`、`bom_from=BOM-SADA-26B-RevA`、`bom_to=BOM-SADA-26B-RevB`、`switch_type=日期切换+基线切片`、`effective_date=2026-09-10`、`user_rep_approved=Y`、`status: DRAFT → USER_APPROVED → RELEASING` |
| `change_action` | `CA-01` 建码与失效 | [+] | `owner_sys=PLM`、`obj=item 2JD01000047 / 2JD01000043`、`status=DONE`（步骤 4 已完成） |
| `change_action` | `CA-02` BOM 行 90 子件换型 | [+] | `owner_sys=PLM`、`due=2026-09-09`、`status=OPEN` |
| `change_action` | `CA-03` 路线 op50 参数窗口重定 | [+] | `owner_sys=PLM`、`op_scope=40,50,80`、`status=OPEN` |
| `change_action` | `CA-04` 基线 RevA→RevB 重冻结 | [+] | `owner_sys=PLM`、`status=OPEN` |
| `change_action` | `CA-05` 在制 19 套处置与锁定 | [+] | `owner_sys=MES`、`wo=WO-26B-20260715-0101`、`status=OPEN` |
| `change_action` | `CA-06` 旧料退库与退供应商 | [+] | `owner_sys=WMS/ERP`、`qty=30`、`status=OPEN` |
| `change_action` | `CA-07` RevB 补做低温试验 + 拆换件重做环试/老炼 | [+] | `owner_sys=QMS`、`resource=T-02,AR-01`、`status=OPEN` |
| `change_action` | `CA-08` 履历本更改款 + FCA/PCA + 通知供应商与总装 | [+] | `owner_sys=MES/PLM`、`status=OPEN` |

下游：ECO 下达即生成采购需求（`change_action` CA-06/02 联动 ERP）：`po_line(PO-26B-0148)` 34 件分两批 ETA 09-08/09-25。其中首批 18 件为供方现货借调加急（5 d），第二批 16 件为加急新制（22 d），均短于主数据 `purchase_lead_time=90 d`，须在 PO 上标注 `urgent_flag=Y` 与工艺鉴定结论。
守恒：A9 前置——`change_action` 8 行中 `owner_sys` 跨 4 个系统，每行须有落地证据，否则 ECN 不能 `CLOSED`；本步 `qty_open` 采购 34 件 = 净装 32 + 损耗 2（`qty × (1+loss_rate)` = 2×1.05×16=33.6 取 34）。

### 步骤 6 ｜ 新 Rev 生效区间写入 `bom_line` 与基线重冻结

触发：2026-09-09 17:00 PLM 提交 BOM RevB 发布与基线冻结，生效区间由系统写入，不允许人工填日期。
校验：`bom_line` 同一 `(bom_id, line_no)` 的有效性区间不得重叠（A7 的 PLM 侧硬约束）；`item` 已 `OBSOLETE` 的件号不得出现在 RevB 新行；RevB 行数必须 = RevA 行数（16 行），差异只允许在行 90；`baseline_item` 必须逐行引用 `item_rev` 而非 `item`。
单据：`bom(BOM-SADA-26B, RevB)` `status: DRAFT → IN_REVIEW → RELEASED`，`effect_from=2026-09-10`。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `bom_line` | `BOM-SADA-26B-RevA` 行 90 | Δ | `effective_to: 9999-12-31 → 2026-09-10`、`change_order_id: null → ECN-26B-20260908-0003`（区间左闭右开） |
| `bom_line` | `BOM-SADA-26B-RevB` 行 90 | [+] | `parent_item=4JD00000013`、`child_item=2JD01000047`、`child_item_rev=RevA`、`qty=2`、`loss_rate=5%`、`pos='上支点/下支点'`、`routing_op_id=op50`、`effective_from=2026-09-10`、`effective_to=9999-12-31` |
| `bom` | `BOM-SADA-26B-RevA` | Δ | `status: RELEASED → SUPERSEDED`、`superseded_by=BOM-SADA-26B-RevB` |
| `baseline` | `BL-26B-SADA-Z-RevA` | Δ | `status: FROZEN → SUPERSEDED`、`effect_to: null → 2026-09-10`、`keep_for_sn_cnt: 0 → 4`（G1 三套 + 留厂比对件） |
| `baseline` | `BL-26B-SADA-Z-RevB` `[+]`（待回写单号规则 BL-{型号}-{产品}-{阶段}-{Rev}） | [+] | `scope=26B 批产`、`status=FROZEN`、`effect_from=2026-09-10`、`item_cnt=12`、`bom_line_cnt=16`、`op_cnt=11`、`ecn_no=ECN-26B-20260908-0003` |
| `baseline_item` | `(BL-26B-SADA-Z-RevB, 2JD01000043)` | [−] | `valid_to: null → 2026-09-10`，行保留不删 |
| `baseline_item` | `(BL-26B-SADA-Z-RevB, 2JD01000047)` | [+] | `item_rev=(2JD01000047,RevA)`、`qty=2`、`pos=上支点/下支点`、`change_order_id=ECN-26B-20260908-0003` |
| `baseline_item` | `(BL-26B-SADA-Z-RevA, 2JD01000043)` | Δ | `except_flag: 0 → 1`、`except_reason=仅适用 SN 挂 RevA 基线`（3 套不拆件的依据） |

```sql
-- A7 PLM 侧：同一 (bom_id,pos) 有效区间不得重叠
SELECT a.line_no, a.child_item AS rev_old, b.child_item AS rev_new
FROM   bom_line a JOIN bom_line b
       ON b.line_no = a.line_no AND b.bom_id <> a.bom_id
WHERE  a.pos = b.pos
  AND  a.effective_from < b.effective_to AND b.effective_from < a.effective_to;
-- 期望 0 行；行 90 以 2026-09-10 为唯一分界
```

下游：`BOM_EFFECTIVE`（幂等键 `bomId:rev:effectiveFrom` = `BOM-SADA-26B:RevB:2026-09-10`）与 `ECN_EFFECTIVE`（幂等键 `ecnNo`）同事务写 `outbox_msg`；`BOM_EFFECTIVE` 先于 `ECN_EFFECTIVE` 入队（同一分区键 `bizId`，保证 MES 先落 BOM 行再落处置指令）。
守恒：A2 结构侧——RevB 16 行、应装位 38（19×2）与 RevA 完全同构；A7 PLM 侧 0 重叠；`baseline_item` 两版共 24 行（RevA 12 + RevB 12），有效行 12。

### 步骤 7 ｜ `ECN_EFFECTIVE` 跨系统投递与 MES 消费补偿（时序）

触发：2026-09-10 00:05 PLM 定时发布任务把 `change_notice.status` 置 `EFFECTIVE`，业务事务内写 `outbox_msg`，由扫描器投 MQ。
校验：消息与业务同事务（本地消息表模式，见 [tech-mdm-sync](../tech/tech-mdm-sync-20260816.md)）；消费方按 `(event_id, consumer)` 去重；同一 `bizId` 落同一分区保证顺序；payload 带 `version`，旧版本丢弃。

| 事件 | 发布方 | 订阅方 | 幂等键 | 各系统落什么 |
|------|-------|-------|--------|-------------|
| `ECN_EFFECTIVE` | PLM | MES | `ECN-26B-20260908-0003` | `work_order.ecn_hold_flag=1`、31 行 `work_order_operation` 置 `PAUSED`、`change_impact.status=D→EFFECT` |
| `ECN_EFFECTIVE` | PLM | QMS | 同上（各自独立去重行） | 生成 6 行 `qc_task`（IQC 复验、拆换复检、低温补试） |
| `ECN_EFFECTIVE` | PLM | ERP | 同上 | `po_line(PO-26B-0097/0098) mrp_flag=STOP`、备件与 RTV 科目开立 |
| `BOM_EFFECTIVE` | PLM | MES, ERP | `BOM-SADA-26B:RevB:2026-09-10` | MES `bom_line` 镜像 16 行换 RevB；ERP 更新 MRP 展开件号 |
| `ITEM_RELEASED` | PLM | ERP, MES, WMS | `2JD01000047:RevA` | WMS 建 LOT 属性模板（关重件、需 MTR、复验必检） |

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `outbox_msg` | `ECN_EFFECTIVE / ECN-26B-20260908-0003` | [+] | `status: PENDING → SENT`、`created_at=2026-09-10 00:05:12`、`sent_at=00:05:14`、`retry_count=0` |
| `outbox_msg` | `BOM_EFFECTIVE / BOM-SADA-26B:RevB:2026-09-10` | [+] | `status=SENT`、`sent_at=00:05:13`（先入队） |
| `outbox_msg` | `ECN_EFFECTIVE`（MES 首投） | Δ | `status: SENT → FAILED → SENT`、`retry_count: 0 → 1 → 2`、`next_retry_time=02:14 → 02:29 → 02:44`（指数退避 30/60/120 s 的上限档 15 min） |

MES 消费失败怎么补：① 02:14 首次消费在 `work_order_operation` 批量更新时行锁超时回滚，`consumed_event` 未写 → 不产生脏数据；② 02:29、02:44 两次退避重试，02:44 成功写 `consumed_event(event_id='ECN-26B-20260908-0003', consumer='MES')`；③ 第 3 次仍失败则消息进死信 `plm-events.DLT` 并告警集成岗，由人工调 `POST /mes/api/ecn/replay` 重放（重放仍走同一幂等键，不会重复锁定）；④ T+1 d 的 02:00 对账任务把 PLM/MES 的 BOM 行数与 Rev 差异写入 `sync_recon`，即 A9 的抓法。本批 PLM 00:05 生效、MES 02:44 落地，**不一致窗口 2 h 39 min**，期间 MES 仍持 RevA 16 行；02:00 那轮对账记 1 行差异 `OPEN`，03:00 复核转 `RESOLVED`。窗口内的发料拦截靠步骤 10 的生效日校验，不靠事件到达。

下游：MES 消费成功后才允许 `change_impact` 生效；QMS 与 ERP 各自独立幂等，任一失败不影响其余两方。
守恒：A9——`sync_recon` 差异行在 09-10 02:00 为 1（MES `bom_line` 行 90 `child_item` 仍 `2JD01000043`），03:00 起为 0；行数两侧恒等 16 = 16。

### 步骤 8 ｜ MES 锁定在制工单（不做整单 `SUSPENDED`）

触发：2026-09-10 02:44 MES 消费线程执行 `ECN_EFFECTIVE` 处理器，按 `change_impact` 逐 SN 加锁。
校验：工单状态机（[tech-workorder-state](../tech/tech-workorder-state-20260816.md)）里 `STARTED → SUSPENDED` 是整单语义，会把 G1 三套的出厂评审一起挂住，故本篇只在**工序级**加锁 + 工单挂标记，`work_order.status` 保持 `STARTED`；`mandatory_flag=Y` 的关键工序（50/60/100）加锁必须记 `pause_reason`，否则日终审计报 `E_HOLD_WITHOUT_REASON`。
单据：无新单号，`work_order_operation` 31 行状态变更。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `work_order` | `WO-26B-20260715-0101` | Δ | `ecn_hold_flag: 0 → 1`、`hold_ecn_no: null → ECN-26B-20260908-0003`、`hold_scope='F-004~007,F-008~012,B-*,Y-*'`（16 套）、`bom_id: BOM-SADA-26B-RevA → BOM-SADA-26B-RevB`、`routing_id: RevA → RevB`、`status: STARTED → STARTED`（不变） |
| `work_order_operation` | 11 套 × op 50 = 11 行 | Δ | `status: PENDING → PAUSED`、`pause_reason=ECN-26B-20260908-0003`、`paused_at=2026-09-10 02:44` |
| `work_order_operation` | 5 套 × op 60/70/80/90/100 = 20 行 | Δ | `status: PENDING/IN_PROGRESS → PAUSED`、`pause_reason` 同上 |
| `sn_history` | 16 行（G2+G2′+G3） | Δ | `hold_flag: N → Y`、`hold_reason=ECN_HOLD`、`status` 不变（不新增节点，A1 不受扰动） |
| `sn_master` | 16 行 | Δ | `bl_code: BL-26B-SADA-Z-RevA → RevB`；G1 三套 `bl_code` 保持 RevA、`cond_watch_flag: 0 → 1` |

下游：无外发事件（锁定是 MES 内部动作）；APS 收到本地 `WO_OPERATION_HELD` 后重排 op 50，11 套的工序 50 从 09-10 起排到 09-11/09-26 两批。
守恒：A1——19 套有效履历节点仍 19（只改 `hold_flag`，未关闭也未新增节点）；A5——锁定窗口内 op 50/60 报工 0 行，`合格+不合格=投产` 未被破坏。

### 步骤 9 ｜ `change_impact` 逐 SN 登记处置结论

触发：2026-09-10 09:00 责任工艺师与主任设计师在 MES"更改处置登记"页对 19 个 SN 逐条确认，允许在 CCB 结论框架内细化但不允许改策略族。
校验：`strategy` 必须取 `KEEP_NO_DISASSEMBLE / DISASSEMBLE_REWORK / FOLLOW_ON_SWITCH / NONE` 四值之一；`DISASSEMBLE_REWORK` 必须预分配拆换工单号；`KEEP_NO_DISASSEMBLE` 必须挂 `risk_assess_ref` 且 `cond_watch=Y`，否则保存失败；已 `SHIPPED` 的 SN 本表禁止出现 `DISASSEMBLE_REWORK`（§3.1 的守卫）。
单据：`change_impact` 22 行 `status: EFFECT → CONFIRMED`。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `change_impact` | `(ECN-…, SN, SADA-26B-Z-F-007)` | Δ | `strategy=DISASSEMBLE_REWORK`、`mount_state=MOUNTED_UNTESTED`、`disposition_who=装配+设计+质量 三方`、`risk_assess_ref=RA-26B-20260908-002`、`wo_code=WO-26B-20260910-0163`、`status: EFFECT → CONFIRMED` |
| `change_impact` | `(…, SN, SADA-26B-Z-F-001)` | Δ | `strategy=KEEP_NO_DISASSEMBLE`、`mount_state=MOUNTED_TESTED`、`keep_reason=试验数据有效性优先`、`cond_watch=Y`、`watch_items=启动力矩趋势、在轨预紧力反演` |
| `change_impact` | `(…, SN, SADA-26B-Z-Y-001)` | Δ | `strategy=FOLLOW_ON_SWITCH`、`mount_state=NOT_MOUNTED`、`kit_batch=BATCH-2`（等 09-25 二批料）、`switch_op=50` |
| `change_impact` | `(…, LOT, LOT-BRG-26B-01)` | Δ | `strategy=RTV`、`qty=30`（未装机 22 + 拆下 8）、`sup_resp=非供方责任，按厂内协议折价` |
| `change_impact` | `(…, LOT, LOT-BRG-26B-02)` | Δ | `strategy=SPARE_KEEP`、`qty=2`、`spare_purpose=G1 三套与 D-001 后续维修保障` |
| `change_impact` | `(…, WO, WO-26B-20260715-0101)` | Δ | `strategy=PARTIAL_LOCK`、`locked_sn_cnt: 0 → 16`、`kit_recalc=Y` |

下游：`change_impact` 生效触发 MES 内部三张待办（拆换工单、退料单、RevB 发料计划）；无跨系统事件。
守恒：A2——22 行中 SN 类 19 行覆盖 19 套，`mount_state` 分布 = 已装 8 套 / 未装 11 套，与步骤 2 取数一致；`LOT` 类 2 行数量 30 + 2 = 32，加装机保留 6 件 = 38（`LOT-BRG-26B-01` 全量）。

### 步骤 10 ｜ 顺次切换：RevB 到货、复验与发料

触发：2026-09-08 14:00 首批 18 件到库收货；2026-09-11 08:00 库管员按 `MI-20260911-0388` 发料，扫码过账时校验生效日。
校验：① 新件入库即 `lock_flag=ECN_PENDING`——**到货早于生效 2 天，WMS 不许发料**，09-10 00:05 由 `ECN_EFFECTIVE` 的 ERP 分支解锁；② 关重件★复验必检 + MTR 齐套（`mtr_no` 与 `sup_lot` 三元绑定）未通过不得 `OK`；③ 发料时二次校验 `bom_line.effective_from ≤ 发料日`，即使 `ECN_EFFECTIVE` 未消费也要拦住 RevA 口径发料——这是步骤 7 那 2 h 39 min 不一致窗口的兜底；④ 齐套率不足时按 `kit_batch` 分批放行，不允许跨批挪用。
单据：`IT-20260908-21337`（首批复验）、`IT-20260925-21644`（二批复验）、`MI-20260911-0388`。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `po_line` | `PO-26B-0148` 行 1 | [+] | `item=2JD01000047`、`qty=18`、`qty_received=18`、`eta=2026-09-08`、`urgent_flag=Y`、`lot=LOT-BRG-26B-03` |
| `po_line` | `PO-26B-0148` 行 2 | [+] | `qty=16`、`qty_received=16`、`eta=2026-09-25`、`lot=LOT-BRG-26B-04` |
| `inv_lot` | `LOT-BRG-26B-03` | [+] | `item=2JD01000047`、`qty=18`、`alloc_qty: 18 → 0`、`lock_flag: ECN_PENDING → (空)`、`wh=WH-01 → 发线边 WH-03`、`sup_lot`、`mtr_no` 齐 |
| `inv_lot` | `LOT-BRG-26B-04` | [+] | `qty=16`、`lock_flag: ECN_PENDING → (空)`（09-25 生效已过，直接 `OK`） |
| `inv_txn` | `TXN-20260908-103441` | [+] | `txn_type=RECEIPT`、`item=2JD01000047`、`qty=+18`、`balance_after=18`、`ref=PO-26B-0148/1` |
| `inv_txn` | `TXN-20260925-103812` | [+] | `txn_type=RECEIPT`、`qty=+16`、`balance_after=16` |
| `inv_txn` | `TXN-20260911-103472` | [+] | `txn_type=ISSUE`、`qty=−18`、`ref=MI-20260911-0388`、`to_loc=WH-03` |
| `work_order_material` | 11 套 op50 轴承需求行 | Δ | `item_code: 2JD01000043 → 2JD01000047`、`lot: LOT-BRG-26B-01 → 03/04`、`status: ISSUED → PLANNED → ISSUED` |
| `qc_task` | `IT-20260908-21337` | [+] | `plan=QP-BRG-CERAMIC-IQC`、`sample=全检`、`items=MTR核验/游隙/接触角20°抽检/外观`、`result=PASSED`（2026-09-09 10:20） |

首批 18 件分配：拆换 5 套 10 件 + 顺次 4 套 8 件 = 18；余 7 套（B-004、B-005、Y-001、Y-002、F-009、F-010、F-011）等 09-25 二批 16 件（14 装机 + 2 损耗补量）。
下游：`MATERIAL_ISSUED`（幂等键 `txnNo`）→ MES 写 `sn_genealogy` 待发料绑定、ERP 按 SN 归集材料成本（`cost_object.material_cost` +12.00 万元/套）。
守恒：A3 发料 18 = 装机 18 + 线边余 0 + 退料 0 + 报废 0；A4 Σ`inv_lot.qty`(RevB) 34 = `inv_balance` 34；齐套率由 RevA 口径 100% 降至 RevB 口径 18/34 = **52.9%**，09-25 回升 100%。

### 步骤 11 ｜ 拆换工单执行：拆装、力矩复紧、压装曲线重采

触发：2026-09-12 08:00 装配工 OP-1102（`CRIMP-3` 授权至 2027-06-30、力矩作业授权有效）在 MES 扫拆换工单开工，工位 W-JD-01，设备 EQ-330（校准至 2027-01-15）。
校验：① 派工守卫——人员资质、设备校准、工装寿命三查（A10 口径），`EQ-341` 力矩点位数 266 → 336 仍低于 `warn_at=4500`；② 工序不得跳序：拆装 → 清洗涂脂 → 力矩复紧 → 压装曲线重采 → 线束复位与接地复测 → 复检，`reject_next_seq` 强制；③ RevB 参数窗口 `峰值力 9.0~13.0 kN`、`过盈量 0.030~0.045 mm`（不变）、`曲线 200 Hz 强制留存`，超窗直接判不合格；④ 陶瓷球件禁用 RevA 压装模具，模具号不一致即拦。
单据：`WO-26B-20260910-0163`（待回写）`status: CREATED → RELEASED → STARTED`；`ROUT-SADA-26B-RevB-CK1` 拆换返工路线 6 道工序 15 h/套。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `work_order` | `WO-26B-20260910-0163` | [+] | `wo_type=DISASSEMBLE_REWORK`、`item=4JD00000013`、`qty=5`、`sn_scope='F-004,F-005,F-006,F-007,F-012'`、`parent_wo=WO-26B-20260715-0101`、`rout_id=ROUT-SADA-26B-RevB-CK1`、`bom_id=BOM-SADA-26B-RevB`、`source_ecn=ECN-26B-20260908-0003`、`cost_collect=挂源工单对应 SN` |
| `work_order_operation` | `(0163) ck10 拆装 / ck20 清洗涂脂 / ck30 力矩复紧 / ck40 压装曲线重采 / ck50 线束复位与接地复测 / ck60 复检` | [+] 6 行 | `status: PENDING → COMPLETED`；`ck40 op_scope=工序50 ★关键工序`、`params=压入力曲线200Hz`；`ck50` 记录 `接地电阻 ≤2.5 mΩ` 复测 5 组 |
| `work_order_material` | `(0163, 2JD01000043)` | [+] | `req_qty=10`、`issued_qty=0`、`returned_qty: 0 → 10`（拆下退库，见步骤 12） |
| `work_order_material` | `(0163, 2JD01000047)` | [+] | `req_qty=10`、`issued_qty=10`、`lot=LOT-BRG-26B-03` |
| `sn_genealogy` | `(MGU-26B-Z-F-007, pos=上支点, 2JD01000043)` | [−] | `valid_to: null → 2026-09-12 10:35`、`void_reason=ECN-26B-20260908-0003 拆换`、`void_by=OP-1102` |
| `sn_genealogy` | `(MGU-26B-Z-F-007, pos=下支点, 2JD01000043)` | [−] | `valid_to: null → 2026-09-12 10:41` 同上 |
| `sn_genealogy` | `(MGU-26B-Z-F-007, pos=上支点, 2JD01000047)` | [+] | `link_type=SN2LOT`、`child_lot=LOT-BRG-26B-03`、`child_item_rev=RevA`、`qty=1`、`wo_id=0163`、`op_id=ck40`、`valid_to=null` |
| `sn_genealogy` | 5 套 × 2 位 | 共 | [−] 10 行 + [+] 10 行 = 20 行；旧行必须与 `ck10 拆装` 报工时间对齐 |
| `qc_task` | `IT-20260912-21470` | [+] | `sn_scope=5 套`、`plan=QP-SADA-Z-F-02-50`（RevB 方案 10 项，含陶瓷球外观与游隙）、`result=PASSED`（F-006 首压 `峰值力 13.6 kN` 超上限判 `NG`，见下） |

F-006 于 09-13 09:20 首次压装峰值力 13.6 kN 超 RevB 上限 13.0 kN，轴承保持架受载损伤判废 1 件，走 `NCR` → 工序内返压，09-13 16:00 二次压装 11.8 kN 合格；损耗补量 1 件由 `LOT-BRG-26B-03` 出库。另 1 件 RevB 在 09-25 二批开箱复验发现游隙超下限判废（不可退供），故 RevB 报废合计 2 件。
下游：`OP_COMPLETED`（幂等键 `woNo:opSeq:reportNo`）逐工序驱动 ERP 人工归集（拆换 15 h/套挂回 5 个 `cost_object`）；`NCR_RAISED`（`ncrNo`）→ MES 挂返工标记、PLM 记更改执行偏差。
守恒：A5——`ck40` 投产 5 = 合格 4 + 不合格 1，二次报工投产 1 = 合格 1；A1——5 套 SN 在 09-13 的有效履历节点仍各 1（`REFURBISH → IN_ASSY` 顺序关闭）；A10——`ck40/ck30` 操作人 `CRIMP-3`、`TORQUE-2` 授权均在有效期内。

### 步骤 12 ｜ 旧件退库、锁定与退供应商（RTV）

触发：2026-09-11 09:00 线边 22 件整箱退库（`MR-20260911-0076`）；2026-09-12~09-14 拆下 10 件逐件退库；09-14 采购开退供单，`change_action CA-06` 关闭。
校验：① 线边未拆封件按 `LOT-BRG-26B-01` 原批退回 WH-01 并保留 MTR 关联，允许全额退供；② 拆下件过盈压装已卸，逐件判伤：8 件外观与游隙合格可折价退供、2 件拆伤判报废（不可退供，按厂内规范实物破坏后作废）；③ 退库件 `lock_flag` 必须先置 `ECN_HOLD` 再转 `REJECTED`（主数据 §七 批次库存异常分支 `LOCKED → REJECTED(退货)`），跳态直接 `REJECTED` 会被 WMS 状态机拒绝；④ 拆下件不得与原批混箱（`LOT-BRG-26B-05` 单独建批）。
单据：`MR-20260911-0076` 退料单、`OS-RCV` 不适用、退供走 `po_line.rtv_qty` + `TXN`。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `inv_lot` | `LOT-BRG-26B-01` @WH-03 | Δ | `qty: 22 → 0`、`alloc_qty: 22 → 0`（退料回 WH-01） |
| `inv_lot` | `LOT-BRG-26B-01` @WH-01 | Δ | `qty: 0 → 22 → 0`、`lock_flag: (空) → ECN_HOLD → REJECTED` |
| `inv_lot` | `LOT-BRG-26B-05`（拆退件新批） | [+] | `item=2JD01000043`、`qty=10`、`lock_flag: ECN_HOLD → REJECTED`、`split: RTV 8 / SCRAP 2` |
| `inv_lot` | `LOT-BRG-26B-02` | Δ | `lock_flag: (空) → SPARE`、`purpose_note=G1 三套与 D-001 保障备件`、`qty=2` 不变 |
| `inv_txn` | `TXN-20260911-103507` | [+] | `txn_type=RETURN`、`qty=22`、`from=WH-03 to=WH-01`、`ref=MR-20260911-0076` |
| `inv_txn` | `TXN-20260912-103551` | [+] | `txn_type=TRANSFER`、`item=2JD01000043`、`qty=10`、`to=LOT-BRG-26B-05`、`ref=WO-26B-20260910-0163/ck10` |
| `inv_txn` | `TXN-20260914-103618` | [+] | `txn_type=RTV_OUT`、`qty=30`、`ref=PO-26B-0097`（22 件）/ `PO-26B-0097`（8 件） |
| `inv_txn` | `TXN-20260914-103640` | [+] | `txn_type=SCRAP`、`qty=2`、`lot=LOT-BRG-26B-05`、`reason=拆卸损伤，账面原值 7.60 万元` |
| `po_line` | `PO-26B-0097` 行 1 | Δ | `item=2JD01000043`、`qty=38`、`qty_received=38`、`rtv_qty: 0 → 30`、`status: CLOSED → REOPENED → CLOSED` |
| `po_line` | `PO-26B-0098` 行 1 | Δ | `rtv_qty=0`、`spare_qty: 0 → 2`、`note=转保障备件不退供` |

退供折价：未装机 22 件按原价 3.80 万元/件全额退回得 83.60 万元；拆下 8 件按 50% 折价 1.90 万元/件得 15.20 万元；合计回款 98.80 万元，对应原值 114.00 万元，折价损失 15.20 万元。
下游：`RTV_APPROVED`（扩展，幂等键 `poNo:lineNo:rtvQty`）→ ERP 冲减应付与材料成本、SRM 登记质量信用；供应商侧同步收到 `ECN` 换型通知（步骤 13）。
守恒（本篇主写，轴承实物口径）：

| 项 | 件数 | 来源批次 | 等式角色 |
|----|------|---------|---------|
| 装机保留（RevA） | 6 | LOT-BRG-26B-01 | 3 套不拆 × 2 |
| 在库（RevA） | 2 | LOT-BRG-26B-02 | 保障备件 |
| 退供应商（RevA） | 30 | LOT-BRG-26B-01 22 + LOT-BRG-26B-05 8 | 未装机 + 拆下合格件 |
| 报废（RevA） | 2 | LOT-BRG-26B-05 | 拆伤不可退供 |
| **合计** | **40** | | = 期初 38 + 本期收入 2 ✓ |

拆下退库的 10 件是 `TRANSFER` 不是 `RECEIPT`，故"本期收入"仍为 2，不重复计数。RevB 独立闭合：装机 32 + 报废 2 = 收货 34（18 + 16）✓。全批轴承装机总量 6 + 32 = 38 = 19 套 × 2 ✓（A2）。A3 发料 38 + 34 = 装机 16+32 与退料 22 与报废 4 与线边余 0 之间以 `inv_txn` 重算一致；A4 Σ`inv_lot.qty`（RevA）= 2 = 余额 2 ✓。

### 步骤 13 ｜ 补做低温试验、FCA/PCA 与履历本更改款、供应商与总装通知

触发：2026-09-26 10:00 QMS 对 RevB 件补做 −30℃ 低温启动力矩与预紧力漂移试验（厂内规范：Ⅰ类更改影响关重件运动特性须补环境验证）；2026-10-12 技术状态审核组做 FCA（功能配置审核）与 PCA（物理配置审核）并归档履历本更改款；同日 PLM 向供方与 004 星总办发通知。
校验：`qc_task` 试验剖面必须与 GJB 9001C 技术状态管理要求下的验证矩阵逐格对应；PCA 按 SN 抽 `sn_genealogy` 与 `baseline_item` 比对，**G1 三套必须比对 RevA 基线、其余 16 套比对 RevB**（切片依据是 `sn_master.bl_code`，不是当前生效基线，否则 3 套必然报"实物与基线不符"假阳）；FCA 要求 19 套出厂性能复测数据齐全；履历本更改款必须引用 `ecnNo` 且与 `change_impact.strategy` 一一对应。
单据：`IT-20260926-21688`（低温补试 2 台次）、`FAI-W50-26B-002`（RevB 换型首件，AS9102 口径，检查项 34、合格 33、需修 1）、FCA/PCA 审核记录。

| 表 | 行 | 标记 | 字段与取值 |
|----|----|------|-----------|
| `qc_task` | `IT-20260926-21688` | [+] | `sn_scope='SADA-26B-Z-F-004, SADA-26B-Z-F-008'`（拆换与顺次各 1 套）、`profile=−30℃/常温 启动力矩 + 预紧力漂移`、`resource=T-02`、`result=PASSED`（漂移 ≤0.05 kN，启动力矩偏差 3.1%） |
| `equipment` | `T-02` | Δ | `util_hours: 648.0 → 876.0`（+180 h 环试重做 +48 h 低温补试 = 228 h）、`next_avail: 2026-09-18 → 2026-09-29`（被 P0-5 FPGA 替代验证占 09-18~09-21，硬耦合 3） |
| `sn_doc` | 19 行更改款 + F-007 第 12 款样例 | [+] | `doc_type=CHANGE_CLAUSE`、`ecn_no=ECN-26B-20260908-0003`、`signed_by=OP-1102 + QC-2001 + 用户代表`；G1 三套记"经风险评估保留 RevA，附条件跟踪"，G2 五套记拆换与压装曲线号（F-007 `clause_text=主轴轴承 71904→71905，拆换 2 件，LOT-BRG-26B-03`），G3 十一套记 RevB 批次号 |
| `approval_record` | `(FCA/PCA 审核)` × 2 | [+] | `result=PASS`、`approved_at=2026-10-12 17:00`、`exception_cnt=0` |
| `change_notice` | `ECN-26B-20260908-0003` | Δ | `status: EFFECTIVE → CLOSED`（2026-09-30 退库与 RTV 完成后；CA-01~CA-08 全 `DONE`；FCA/PCA 于 10-12 复核通过并回写 `close_review=PASS`） |

下游：`outbox_msg` 追加 `SUPPLIER_CHANGE_NOTICE`（扩展，幂等键 `supCode:ecnNo`）通知轴承供方停产 RevA、切换 RevB 工艺鉴定；`FINAL_ASSY_CONFIG_NOTICE`（扩展）通知 004 星总装 `WING-26B-Z-F-004B / SADA-26B-Z-F-007` 技术状态已转 RevB、随机文件增加更改款；`SN_SHIPPED` 仍按 10-31 全批交付发（幂等键 `sn`）。
守恒：A1 19；A2 38 位；A7 0 违规；A8——`cost_object` 5 套拆换件材料成本由 RevA 7.60 万元/套 移出、RevB 12.75 万元/套 移入，人工 +1.80 万元/套、台费 +14.58 万元/套，与 §5 分摊表逐格相等。

## 3. 异常分支

### 3.1 跳过影响评估直接发布 ECN，已交付鉴定件被误列拆换范围

错误做法：09-08 为赶 09-10 生效窗口，变更工程师不跑步骤 2 的四口径取数，手工把 `change_impact` 置空后发布 ECN；MES 消费 `ECN_EFFECTIVE` 时走兜底逻辑"按 `child_item='2JD01000043'` 全库反查 `valid_to IS NULL` 圈定"，命中 20 个 SN，其中含 2026-06 完工、已交型号试验队的鉴定件 `SADA-26B-Z-D-001`，于是拆换工单 `WO-26B-20260910-0163` 被扩为 6 套。

拦不拦得住：半拦得住。① 实物圈定拦不住——`sn_genealogy` 反查对 MES 而言与在制件无差别；② 拆换动作拦得住一半——`change_impact` 落库守卫要求 `DISASSEMBLE_REWORK` 的 SN 必须 `status NOT IN ('SHIPPED','INSTALLED','ON_ORBIT')`，D-001 的 `status=SHIPPED` 会被强制改写为 `strategy=NONE` 并置 `needs_user_rep_decision=1`，推用户代表待办；③ 但 09-08 到守卫执行之间的 4 h 内，APS 已按 6 套排产、齐套已按 12 件请领，`sn_master` 上 D-001 的 `bl_code` 被刷成 RevB，履历本 `LOG` 已生成更改款草稿——这三处**守卫不覆盖**，仍需人工回滚。

脏数据长这样：

| 表 | 脏行 | 症状 |
|----|------|------|
| `change_impact` | `(ECN-…, SN, SADA-26B-Z-D-001)` | `strategy=DISASSEMBLE_REWORK` 且 `mount_state=MOUNTED_TESTED`，鉴定试验数据被判为待销毁 |
| `work_order` | `WO-26B-20260910-0163` | `qty: 5 → 6`、`sn_scope` 多出 D-001 |
| `work_order_material` | `(0163, 2JD01000047)` | `req_qty: 10 → 12`、已按 12 件请领，多领 2 件 |
| `inv_txn` | `TXN-20260911-103472` | `ISSUE qty=20`，与 RevB 装机 32 冲突，A3 破 |
| `sn_master` | `SADA-26B-Z-D-001` | `bl_code: RevA → RevB`、`dest_code: RESEARCH` 与在制工单绑定矛盾 |
| `sn_history` | `(D-001, REFURBISH)` 新增而 `SHIPPED` 节点未关闭 | **同一 SN 两条 `valid_to IS NULL` → A1 直接崩** |
| `sn_doc` | `LOG-SADA-26B-Z-D-001` | 主数据 §2.0 明确 D 件以鉴定报告代替履历本，此处却建了履历本 |

回滚与红冲：① 拆换工单 6→5，D-001 的 `sn_scope` 行删除并记 `work_order_status_log`；② 多领 2 件 RevB 走 `MR-20260914-0091` 退料，`inv_txn` 追加 `RETURN −2`（流水不可改，[tech-traceability](../tech/tech-traceability-20260816.md) 的 append-only 原则），A3 复位为 发料 18 = 装机 16 + 退料 2；③ `sn_history` 关闭 `REFURBISH` 节点、把 `SHIPPED` 节点 `valid_to` 回置 `null`，该动作属履历改写，必须走 DBA 审批 + `approval_record(obj_type='DATA_FIX')` 留痕，不允许业务界面提交；④ `sn_master.bl_code` 回置 RevA、删 `sn_doc` 草稿行；⑤ 补跑步骤 2 四口径取数，把 22 行 `change_impact` 重新生成为 `CONFIRMED`，并在 `change_action` 追加"影响评估漏做"偏差记录，归口厂内规范的技术状态更改工作检查单。

### 3.2 拆换只插新 `sn_genealogy` 行、未把旧行 `valid_to` 置位

错误做法：09-12 F-004 拆装报工接口超时重试，作业员扫码新轴承 `LOT-BRG-26B-03` 后，`ck40 压装曲线重采` 报工只执行了 `sn_genealogy` 的 INSERT，漏掉同事务里的旧行 UPDATE（拆换服务把两步拆成了两个事务）。

拦不拦得住：拦不住，且是约束写法的坑。① 数量守卫 `Σ qty ≤ bom_line.qty` 按 `child_item` 分组，RevA 1 件 ≤ 2、RevB 1 件 ≤ 2，各自通过；② 装机位置重复性若建成 `UNIQUE(parent_sn, pos, valid_to)`，PostgreSQL 视 NULL 互不相等，两条 `valid_to IS NULL` 反而**都插入成功**；③ 生效区间守卫在 PLM 侧，看不见 MES 谱系。唯一能拦的是部分唯一索引：

```sql
CREATE UNIQUE INDEX ux_gal_valid_pos
  ON sn_genealogy (parent_sn, pos) WHERE valid_to IS NULL;
```

脏数据长这样（F-004 的 MGU，本应 2 个有效位）：

| id | parent_sn | child_item | rev | pos | child_lot | qty | valid_to |
|----|-----------|-----------|-----|-----|-----------|-----|----------|
| 8101 | MGU-26B-Z-F-004 | 2JD01000043 | A | 上支点 | LOT-BRG-26B-01 | 1 | `null` ← 漏置 |
| 8102 | MGU-26B-Z-F-004 | 2JD01000043 | A | 下支点 | LOT-BRG-26B-01 | 1 | `null` |
| 8199 | MGU-26B-Z-F-004 | 2JD01000047 | A | 上支点 | LOT-BRG-26B-03 | 1 | `null` |

后果同时崩两条断言：A2 期望 19 × 2 = 38 个有效位，实查 39，且 `(MGU-26B-Z-F-004, 上支点)` 出现 2 行、外连接双向不为 0；A7 同一 `pos` 上有效 `child_item_rev` = {A, B} 两个。连带 `inv_txn` 已记 2 件 RevA 拆下退库（`LOT-BRG-26B-05` 收到 10 件），而谱系上 RevA 仍挂着 3 件 → 轴承守恒表算成 装机 7 + 退供 29 + 报废 2 + 在库 2 = 40 但装机实际 6，**数量对得上、指向对不上**，这是最隐蔽的一类：日终 A3/A4 全绿，A2/A7 全红。

修复（按事务顺序，不可只补 `valid_to`）：

```sql
BEGIN;
UPDATE sn_genealogy SET valid_to = '2026-09-12 10:35:00',
       void_reason = 'DATA_FIX: DF-26B-20260912-001'
WHERE  id = 8101 AND valid_to IS NULL;         -- 关闭旧行，恢复 pos 唯一
INSERT INTO approval_record(obj_type, obj_id, step, result, opinion)
VALUES ('DATA_FIX','DF-26B-20260912-001','谱系有效性补置','PASS','DBA+质量双签');
COMMIT;
```

补置时间必须取 `ck10 拆装` 的报工时间而非修复时刻，否则 `sn_history` 与谱系时间线倒挂；随后 ① 重跑 A2/A7 与轴承守恒表，期望 38 个有效位、每组 1 个 Rev；② `qc_task IT-20260912-21470` 追加一次装机复核（复检双岗），履历本 `LOG-SADA-26B-Z-F-004` 更改款补记"谱系数据修复单 DF-26B-20260912-001"；③ 把 `ck10` 与 `ck40` 的两个事务合并为同一 `@Transactional`，并把 `ux_gal_valid_pos` 纳入迁移脚本，列为管理归零条款（与 P0-4 的双五条归零同口径）。

### 3.3 生效日前后混发料（PLM/MES 不一致窗口内的第二种漏）

错误做法：09-10 01:00（事件尚未被 MES 消费）库管员对 G3 中 3 套照常按 RevA 口径拣配发料 6 件。拦不拦得住：拦得住——步骤 10 的第 ③ 条发料二次校验读 PLM 只读视图的 `bom_line.effective_from`，不依赖事件到达，直接返回 `E_ITEM_SUPERSEDED_BY_ECN` 并停发；若该视图同步也失败降级为本地缓存，则由日终 A9（Rev 不一致 1 行）+ A3（发料件号与谱系件号不匹配）双抓。脏数据与回滚同 3.2 的退料红冲路径。

## 4. 对账校验点

| # | 断言 | 期望结果 |
|---|------|---------|
| Q1 | A1 单件有效履历节点 | 19 行，每行 `valid_cnt=1`；`bad=0`。G1 三套为 `PASSED`，G2/G2′ 五套 09-13 起为 `IN_TEST`，G3 为 `IN_ASSY` |
| Q2 | A2 装机数与 `pos` 唯一 | 有效位 38 行 = 19 × 2；重复 `pos` 0 行；缺失 `pos` 0 行。分口径：08-20 时点 16 位、10-31 全批 38 位 |
| Q3 | A7 同一 `pos` 仅一个有效 `child_item_rev` | 0 行；`rev` 分布 RevA 6 位 / RevB 32 位 |
| Q4 | A9 PLM 与 MES BOM 行数、Rev 一致 | `sync_recon` `OPEN` 0 行；`bom_line` 两侧各 16 行，行 90 `child_item=2JD01000047` |
| Q5 | 轴承数量守恒（本篇主写） | `mounted_a=6, on_stock=2, rtv=30, scrap=2, receipts=40`，四数之和等于收入 |

```sql
-- Q1：每 SN 恰 1 条有效履历节点
SELECT m.sn, COUNT(*) AS valid_cnt
FROM   sn_history h JOIN sn_master m ON m.sn = h.sn
WHERE  h.valid_to IS NULL AND m.item_code = '5JD00000007'
GROUP  BY m.sn
HAVING COUNT(*) <> 1;
-- 期望 0 行；不加 HAVING 时共 19 行
```

```sql
-- Q2 + Q3：装机位数、pos 重复、同位多 Rev 三合一
SELECT g.parent_sn, g.pos, COUNT(*) AS rows_cnt,
       COUNT(DISTINCT g.child_item_rev) AS rev_cnt
FROM   sn_genealogy g
WHERE  g.child_item IN ('2JD01000043','2JD01000047') AND g.valid_to IS NULL
GROUP  BY g.parent_sn, g.pos
HAVING COUNT(*) > 1 OR COUNT(DISTINCT g.child_item_rev) > 1;
-- 期望 0 行；总组数 38，其中 (child_item=2JD01000043) 组 6 个、(2JD01000047) 组 32 个
```

```sql
-- Q5：轴承实物守恒（RevA 口径，receipts 只计采购收货）
SELECT (SELECT COUNT(*) FROM sn_genealogy WHERE child_item='2JD01000043' AND valid_to IS NULL) AS mounted_a,
       (SELECT COALESCE(SUM(qty),0) FROM inv_lot WHERE item='2JD01000043')                      AS on_stock,
       (SELECT COALESCE(SUM(qty),0) FROM inv_txn WHERE item='2JD01000043' AND txn_type='RTV_OUT') AS rtv,
       (SELECT COALESCE(SUM(qty),0) FROM inv_txn WHERE item='2JD01000043' AND txn_type='SCRAP')   AS scrap,
       (SELECT COALESCE(SUM(qty),0) FROM inv_txn WHERE item='2JD01000043' AND txn_type='RECEIPT') AS receipts;
-- 期望 6 | 2 | 30 | 2 | 40 → 6+2+30+2 = 40 ✓；RevB 另核 32+2 = 34
```

Q4 的取数按 [tech-mdm-sync](../tech/tech-mdm-sync-20260816.md) 的 02:00 对账任务，两侧 `bom_line` 做全外连接比对 `(line_no, child_item, qty, pos)` 四字段；差异非 0 时禁止关闭 ECN——`change_action` 有未关闭行且 `sync_recon` 有 `OPEN` 行时，`change_notice.status: EFFECTIVE → CLOSED` 会被状态守护拒绝。

## 5. 指标与代价

金额单位为万元（推演口径：RevA 3.80 万元/件沿用 P0-3，RevB 6.00 万元/件、综合人工 1,200 元/台时·h、T-02 3,000 元/h、AR-01 1,000 元/h 为本篇假设，待主数据回写）。

| 指标 | 数值 | 口径 |
|------|------|------|
| 更改闭环周期 | 41 天（08-20 受理 → 09-30 `CLOSED`） | ECR→CCB 13 d、CCB→ECN 6 d、ECN→生效 2 d、生效→闭环 20 d |
| 处置分布 | 保留不拆 3 / 拆换 5 / 顺次切换 11 | 19 套全覆盖，`change_impact` 22 行 |
| 拆换工时 | **75 h**（15 h/套 × 5） | 拆装 4 + 清洗涂脂 2 + 力矩复紧 1.5 + 压装曲线重采 6 + 线束复位接地复测 1.5 |
| 其他人工 | 共同 95 h（旧料复检包装退供 30 + 发料切换齐套重排 22 + 履历本/基线/FCA-PCA/CCB-ECN 文件 43） | 三单人工合计 205 h = 24.60 万元 |
| 补做试验台时 | T-02 **228 h**（环试重做 5 套 × 36 = 180 + 低温补试 2 台次 × 24 = 48）；AR-01 **144 h**（5 套老炼 72 h × 2 炉次） | 台费 68.40 + 14.40 = 82.80 万元 |
| 台位冲突代价 | 与 P0-5 争抢 T-02：09-18~09-21 让位 4 台日，低温补试顺延至 09-26（硬耦合 3） | 外协 2 台次可省 6 天、增支 2.40 万元，因节点仍有 75 d 缓冲未采用 |
| 停工损失工时 | **35 h**（7 套涂脂超 48 h 重做工序 40，5 h/套）+ 管理工时已计入共同 95 h | 等待期最长 16 d（Y-001、Y-002 等 09-25 二批料） |
| 旧料处理损失 | **22.80 万元** = 拆伤报废 2 件 7.60 + 退供折价 15.20；另 7.60 万元（2 件）转保障备件非损失 | 退供 30 件回款 98.80 万元 |
| 净代价 | **334.20 万元**（RevB 采购 204.00 + 人工 24.60 + 台费 82.80 + 旧料减值 22.80） | 回款已按折价计入减值，不重复冲减 |
| 齐套率 | RevB 口径 52.9%（18/34，09-11）→ 100%（09-25）；较 RevA 100% 掉 47.1 pt，恢复 15 d | 首批 18 件支撑 5 套拆换 + 4 套顺次 |
| 工序 50 一次交检合格率 | 更改前 8/8 = 100%；更改后 15/16 = 93.8%（F-006 返压 1 次）；本批累计 23/25 = 92.0% | A5 口径，含 1 次返压报工 |
| SN 归集成本涨幅 | 拆换套 **+34.29 万元/套（+12.8%）**；顺次套 +14.25（+5.3%，超期重做涂脂的 7 套 +14.85）；不拆套 +0.60（+0.22%）；全批均 +17.59 万元/套（+6.5%） | 基线单套 268.50 万元（P0-3 步骤 9） |
| 预算冲击 | 全批 5,101.50 + 334.20 = **5,435.70 万元 > 预算 5,380.00 万元，超 55.70 万元**，278.50 万元裕量全部吃掉 | 需向 `WBS-P-26B-SADA-BATCH` 追加 |
| 交付期影响 | **+9 天**：全批交付原计划 2026-10-22 → 实际 2026-10-31（主数据时间线），距 2027-01-14 节点仍余 75 天 | 台位排队 6 d + 拆换执行 3 d |
| 追溯耗时 | 步骤 2 四口径取数首跑 4 min 12 s（19 SN × 38 位）；A2/A7/A5 全批日终校验 38 s；异常分支 3.2 若未拦，人工核对 39 个位需 32 min | 有效性区间索引 + `ux_gal_valid_pos` 是前提 |

一句话代价：**Ⅰ类更改的账不在 BOM 那一行，而在 19 个 SN 上**——`bom_line` 只改了 2 行，但换来 75 h 拆换、228 h 台位、334.20 万元净代价、5 个 SN 成本涨 12.8% 和全批 9 天顺延；而 3 套"不拆"省下的正是 114 h 与 68.40 万元，代价是 `item_rev` RevA 有效性必须保留、PCA 要按 SN 切片比对基线。本篇拆换工单 `ck50 线束复位与接地复测` 的 5 组复测记录，是 [P0-7](./demo-flow-refix-rework-20261116.md) 中 F-007 于 2026-11-05 总装电测异常所发现的线束接地故障的根因取证起点（硬耦合 5），本篇只登记作业与参数，故障树不在本文展开。

## 相关文档

- [贯穿样例主数据：SADA 26B](./demo-case-sada-20261116.md)（装机谱系、BOM 行 90、库存基线 38/2、状态机总表、断言库 A1~A10、全局时间线、五条硬耦合）
- [demos 目录说明与模板](./README.md)（6 节模板与写作约束）
- [Demo P0-3 三件转阶段与工艺定型](./demo-flow-qualification-gate-20261116.md)（RevA 基线冻结、单套 268.50 万元与轴承 3.80 万元/件口径来源、FAI 门控与 `tooling_life`）
- [PLM 业务流程](../plm/plm-workflow-20260816.md)（ECR→ECN 评估要点、"已发布不可改""有效性唯一""源头优先"三规则）
- [BOM 业务流程](../bom/bom-workflow-20260816.md)（四种切换策略选型：本篇为日期切换 + 基线切片）
- [BOM 展开算法实现](../tech/tech-bom-explode-20260816.md)（`where-used` 多层反查，影响评估取数入口）
- [追溯查询实现](../tech/tech-traceability-20260816.md)（谱系边 append-only、纠错走红冲，决定 §3 的回滚写法）
- [工单状态机实现](../tech/tech-workorder-state-20260816.md)（工序级子状态机，本篇不整单 `SUSPENDED` 的依据）
- [主数据事件同步实现](../tech/tech-mdm-sync-20260816.md)（本地消息表、幂等消费、死信与对账，步骤 7 的补偿链）
- [物料主数据管理](../concepts/item-master-20260906.md)（失效前三校验与本篇例外审批的冲突点）
- [返工返修与报废](../concepts/rework-scrap-20260906.md)（拆换工单成本归集到源工单/源 SN）
