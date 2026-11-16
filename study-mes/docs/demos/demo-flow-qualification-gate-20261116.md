# Demo P0-3 ｜ 三件转阶段：鉴定件通过后工艺定型、PRR 与批量放行

> 创建时间：2026-11-16
> 模块：demos

贯穿样例见 [demo-case-sada-20261116.md](./demo-case-sada-20261116.md)，本篇所有编码、SN、单号、日期、数量取自该文档第二/四/五/十章，未新造数值；本篇用到的 5 个新单号前缀（`TEST-`/`TR-`/`PRR-`/`SKG-`/`COC-`）与 `ROUTING_RELEASED`、`SPQ_APPROVED`、`PRR_APPROVED`、`FAI_PASSED` 等事件超出主数据第四/八章现有清单，步骤内均标注"待回写"。

## 0. 场景背景

2026-07-08，鉴定件 `SADA-26B-Z-D-001`（2026-06-28 完工）跑完振动、冲击、热真空、10 年等效加速寿命四项鉴定试验并全部合格入档，电源与机构车间召开转阶段评审：把试制阶段"能做出来"的工艺，冻结成批产阶段"19 套都做同一个东西"的工艺定型结论，再过 PRR 生产准备状态检查，批准 2026-07-15 对 19 套（飞行件 12 + 备份件 5 + 验收件 2）批量投料 `WO-26B-20260715-0101`。不做的后果：19 套按草稿路线各干各的，关键工序参数窗口由操作人自行掌握，关重件★轴承压装的过盈量无法保证一致；成本继续挂在研制 WBS 上，5,101.50 万元生产成本无法归集；出厂时飞行件缺少可追溯的定型依据，用户代表拒绝在履历本上签字。件状态（`sn_class`）是本篇主控维度：同一个 `5JD00000007`、同一条 `ROUT-SADA-26B`，D/Y/F/B 绑不同检验方案、不同试验项目集合、不同放行签字人、不同成本科目、不同随机文件必备项，差异必须落成表里的行。

| 影响对象 | 范围 |
|----------|------|
| `sn_master` 行 | 20 行 = D-001 1 行 + 26B 批 19 行（12 F + 5 B + 2 Y，与主数据"19 套"一致） |
| 工艺与成本对象 | `ROUT-SADA-26B` RevA（11 道工序）、`BOM-SADA-26B-RevA`（16 行）；成本对象 20 个（1 研制 + 19 生产）、WBS 2 条 |
| 特殊过程与资源 | 工序 40 MGU 清洗涂脂、工序 60 力矩紧固；OP-1102 / OP-1140 / QC-2001，EQ-330、EQ-341、T-02、AR-01 |

## 1. 前置状态快照

### 1.1 单据与基线（2026-07-08 09:00）

| 对象 | 业务键 | 关键字段现值 |
|------|--------|-------------|
| 型号任务书 | `MT-26B-2026-0044` | `deliver_date=2027-02-28`、`sada_qty=19`（2026-06-20 下达，P0-1 未发生） |
| 技术状态基线 | `BL-26B-SADA-Z-D` | `status=FROZEN`、`scope=D 鉴定件`、`item_cnt=12`；批产基线 `BL-26B-SADA-Z-RevA` 不存在（步骤 3 新建） |
| 物料 Rev / 工艺路线 | `item_rev(5JD00000007, RevA)`、`ROUT-SADA-26B RevA` | `lifecycle=WORKING`、`effect_from=null`；路线 `status=DRAFT`、11 道工序 |
| 工单 / 检验方案 | — | 无 `WO-26B-20260715-0101`（07-15 创建）；`qc_plan` 仅 1 套试制方案 `QP-SADA-PROTOTYPE`，无 `sn_class` 维度 |
| 特殊过程鉴定 | `special_process_qual` | 仅 `SPQ-PLATING-2026-003`（滑环镀覆委外，`valid_to=2027-05-31`）；清洗涂脂、力矩未鉴定 |
| 鉴定件 SN | `SADA-26B-Z-D-001` | `sn_class=D`、`sn_history.status=IN_TEST`、`wo_no=WO-26B-20260601-0098`（已 `CLOSED`） |
| 批 19 套 SN | `SADA-26B-Z-{F001..F012,B001..B005,Y001..Y002}` | 不存在（主数据"2026-07-15 投料"时点建账） |

### 1.2 数量与资源起点（引用主数据第五章，数值不得改）

| 项 | 现值 | 本篇用法 |
|----|------|---------|
| 轴承 `2JD01000043` | 需求 40 / 可用 38 / 在途 0 / 缺口 **2** | PRR 项 9 挂起条件；首批发料 18 套耗 38 件（含 2 件损耗） |
| FPGA `2DZ10000034` | 需求 21 / 可用 12 / 在途 9（ETA 2026-09-20） | 工序 30 才耗用，不阻塞 07-15 投料 |
| 步进电机 / 灌封胶 | 电机 19 需 / 11 可用 / 在途 8；胶 3.20 需 / 2.80 可用 | 同为 PRR 项 9 挂起条件；胶缺口 0.40 kg |
| 润滑脂 LP-400 | 0.24 kg 需 / 1.20 kg 可用 / 效期 2027-03-31 | 工序 40 特殊过程在效期校验 |
| OP-1102 | 机构装配 3 级、力矩作业授权、证书 2027-06-30 | 压装 `CRIMP-3` 未授权，步骤 6 新增 |
| EQ-330 / EQ-341 / T-02 | 压装机校准至 2027-01-15、曲线 200 Hz；扳手 30 d、寿命 5000；环试台一次独占 36 h | 曲线项定型冻结、扳手寿命步骤 6 建账；T-02 交付提前后成瓶颈 |

## 2. 逐步推演

每步固定 6 段：触发 / 校验 / 单据 / 数据变化（下表：`[+]` 新增行、`[−]` 作废行、`字段: 旧值 → 新值`）/ 下游 / 守恒。

### 2.0 件状态差异对照表（本篇核心，逐行对应 `qc_plan` / `cost_object` / `sn_doc` 数据）

| 维度 | D 鉴定件（1） | Y 验收件（2） | F 飞行件（12） | B 备份件（5） |
|------|--------------|--------------|---------------|--------------|
| `qc_plan` 族 | `QP-SADA-Z-D-01` | `QP-SADA-Z-Y-01` | `QP-SADA-Z-F-01` | `QP-SADA-Z-B-01` |
| 检验项合计 | **46** | **38** | **41** | **44** |
| 工序检验项 40/50/60/100/出厂 | 6/12/8/5/15 | 4/9/6/4/15 | 4/9/6/4/18 | 4/9/6/4/21 |
| 试验项目数 | 7（鉴定振动/冲击/热真空/10 年等效寿命/热循环/老炼/出厂） | 4（验收振动/热循环/老炼/出厂） | 4（同 Y） | 4 + 封存检查 1 |
| 关键工序放行签字 | 责任工艺师 + 主管设计师 | 检验主管 QC-2001 | 检验主管 + 责任工艺师 | 检验主管 |
| 出厂放行签字 | 转阶段评审委员会 | 检验部长 + 用户代表 | 检验部长 + 设计代表 | 检验部长 + 储备管理员 |
| 成本归集 / 完工去向 | WBS-R 研制费，不结转库存 / `dest_code=RESEARCH` | WBS-P，完工留厂 / `REMAIN` | WBS-P，交付出库 / `DELIVER` | WBS-P，转储备存货 / `STOCK_RESERVED` |
| 履历本 LOG | 不需要（以鉴定报告代替） | 简版 | 必备 | 必备 |
| 质量证明文件 CofC | 不签发 | 验收合格证 | 必备（含 4 项试验矩阵） | 必备 |
| 材料证明书 MTR | 12 个主线件全项 + 复验报告 | 金属/关重件 4 件号（轴承、谐波、钛紧、电机） | 同 Y | 同 Y + 效期件（脂、胶）双份 |

方案族码存于 `routing_op`，`qc_plan_id` 在运行时按 `(op_seq, sn_class)` 解析：

```sql
SELECT p.qc_plan_code, p.sn_class, p.op_seq, COUNT(pi.item_id) AS item_cnt
FROM   qc_plan p JOIN qc_plan_item pi ON pi.plan_id = p.plan_id
WHERE  p.item_code = '5JD00000007' AND p.status = 'RELEASED'
GROUP  BY 1,2,3,4 ORDER BY p.op_seq, p.sn_class;
-- 期望 20 行（5 方案码 × 4 件状态），检验项合计 46+38+41+44 = 169
```

### 步骤 1 ｜ 鉴定试验数据入档

触发：试验站 T-02 与老化房 AR-01 完成 4 项鉴定试验（2026-06-28 ~ 2026-06-30），试验主任在 QMS"试验报告归档"页逐份上传并电子签署。
校验：报告必备 5 字段（试验大纲号、剖面、传感器编号与校准有效期、实测曲线、结论）缺 1 项即停 `DRAFT`；`sn_master.status` 必须是 `IN_TEST`；同 SN 同类型报告已存在则拒绝（唯一键 `sn+test_type+rev`）。
单据：`TEST-26B-VIB-0101`、`TEST-26B-SHK-0102`、`TEST-26B-TVC-0103`、`TEST-26B-LIFE-0104`（前缀 `TEST-26B-{试验码}-{流水}` 为主数据第四章扩展，待回写），`status: DRAFT → ARCHIVED`。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `test_report` | `TEST-26B-VIB-0101` `[+]` | `sn=SADA-26B-Z-D-001`、`test_type=VIB`、`profile=鉴定量级`、`result=PASS`、`archived_at=2026-07-02 10:12` |
| `test_report` | `TEST-26B-SHK-0102` `[+]` | `test_type=SHK`、`result=PASS`、`archived_at=2026-07-02 10:15` |
| `test_report` | `TEST-26B-TVC-0103` `[+]` | `test_type=TVAC`、`cycles=12`、`result=PASS`、`archived_at=2026-07-02 10:20` |
| `test_report` | `TEST-26B-LIFE-0104` `[+]` | `test_type=LIFE`、`equiv_years=10`、`result=PASS`、`archived_at=2026-07-02 10:26` |
| `sn_history` | `SADA-26B-Z-D-001` | `status: IN_TEST → PASSED`、`valid_to: null → 2026-07-08 15:00`（由步骤 2 关闭） |

下游：`TEST_REPORT_ARCHIVED`（扩展事件，幂等键 `sn:test_type:rev`）→ MES 更新 SN 履历、PLM 关联到 `BL-26B-SADA-Z-D`；4 份齐备触发转阶段评审待办推送给型号总师。
守恒：A1 此步只关闭旧履历节点、不改件状态，`count(sn_history where sn='SADA-26B-Z-D-001' and valid_to is null)=1`；D 件试验项目集合 7 项，已入档 4 项鉴定 + 热循环/老炼/出厂 3 项在 2026-06 完成，7/7 齐。

### 步骤 2 ｜ 转阶段评审与件状态记录

触发：2026-07-08 09:00 转阶段评审会（对应 [process-npi](../processes/process-npi-20260906.md) G6/G7 门径在本厂合并执行），主持人从 MES"转阶段评审"入口发起，勾入 4 份 `test_report` 与试制问题清单。
校验：`test_report` 中 D 件 7 项必须全 `PASS`；`baseline_item(BL-26B-SADA-Z-D)` 12 行必须全 `RELEASED`；评审五要素（设计、工艺、检验、供应、质量）签字缺一不可，缺项时评审单停 `IN_REVIEW` 并推待办给缺签人。
单据：`TR-26B-20260708-001`（前缀 `TR` 为扩展，`TR-26B-{日期}-{流水}`），`status: DRAFT → IN_REVIEW → APPROVED`（2026-07-08 15:00）。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `sn_class_history` | `(SADA-26B-Z-D-001, valid_from=2026-06-28)` `[−]` | `valid_to: null → 2026-07-08 15:00`，`class D` 记录作废、行不删 |
| `sn_class_history` | `(SADA-26B-Z-D-001, 2026-07-08 15:00)` `[+]` | `class_from=D`、`class_to=Y`、`change_type=STAGE_PASS`、`gate_id=TR-26B-20260708-001`、`approver=型号总师`、`ship_flag=N` |
| `sn_master` | `SADA-26B-Z-D-001` | `sn_class: D → Y`、`dest_code: (空) → RESEARCH`、`usage_note=留厂作批产比对基线，不占本批 19 套` |
| `item_rev` | `item_rev(5JD00000007, RevA)` | `lifecycle: WORKING → REVIEW`、`change_order_id=TR-26B-20260708-001` |
| `prr_checklist` | `PRR-26B-20260708-001` `[+]` | `stage_gate=TR PASS`、`status=OPEN`、`item_total=12`、`plan_close_date=2026-07-14` |

下游：同事务写 `outbox_message`（`ROUTING_RELEASED` 的前置通知，幂等键 `5JD00000007:RevA`）；MES 把 D 件锁定为"比对件"，禁止挂生产工单；ERP 收到评审结论后将 `WBS-R-26B-SADA-IDENT` 置 `FROZEN_PENDING`。
守恒：A1 任一 SN 同一时刻仅 1 条有效件状态：D-001 的 `sn_class_history` 由 2 行、`valid_to is null` 恰 1 行；件状态分布变化 `D 1→0`、`Y 0→1`（留厂 D-001）；07-15 批投料后 `Y` 再 +2 达 3（本批 2 + 留厂 1），本批交付口径始终为 12 F + 5 B + 2 Y = 19，与主数据第一章节一致。

### 步骤 3 ｜ 工艺定型：参数窗口固化与路线发布

触发：2026-07-09 责任工艺师按评审结论把试制参数收敛为批产窗口，2026-07-11 16:00 在 PLM 提交发布（评审流 设计→校对→审核→会签→批准，见 [plm-workflow](../plm/plm-workflow-20260816.md)）。
校验：`publish()` 前置断言——11 道工序 `work_center`、`std_hours` 非空；3 道关键工序（50/60/100）`mandatory_flag` 必须为 `Y` 且 `reject_next_seq` 不为空；`qc_plan_code` 必须能在 QMS 解析到 4 个件状态方案；参数上下限缺失的工序不允许 `RELEASED`。
单据：`routing(ROUT-SADA-26B, RevA)` `status: DRAFT → REVIEW → RELEASED`，`effect_from=2026-07-15`。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `routing` | `ROUT-SADA-26B-RevA` | `status: DRAFT → RELEASED`、`effect_from: null → 2026-07-15`、`std_hours_sum: null → 194 h`（11 道工序合计 4+8+6+5+6+3+12+36+72+4+8=194） |
| `routing_op` | `op_seq=50 轴承压装 ★` | `qc_plan_code: (空) → OP50-CRIMP`、`fai_gate_flag: null → Y`、`mandatory_skill_code: (空) → CRIMP-3` |
| `routing_op` | `op50 参数 过盈量` | `窗口: 试制记录 0.018~0.062 → 定型 0.030~0.045 mm`、`cpk_target: null → 1.33` |
| `routing_op` | `op50 参数 压装速度/峰值力` | `压装速度: (未冻结) → 2.0±0.4 mm/s`、`峰值力: (未冻结) → 8.0~12.0 kN`、`曲线采样: 200 Hz 强制留存` |
| `routing_op` | `op_seq=40 / 60 / 100` | `op40: op_type: 装配 → SPECIAL`、`spq_code: (空) → CLEAN-LP400`；`op60: spq_code → TORQUE-TI`、`记录点数 14`；`op100: dual_sign_flag: N → Y` |
| `item_rev` | `item_rev(5JD00000007, RevA)` | `lifecycle: REVIEW → RELEASED`、`effect_from: null → 2026-07-15` |
| `baseline` | `BL-26B-SADA-Z-RevA` `[+]` ／ `BL-26B-SADA-Z-D` | 新行 `scope=26B 批产`、`item_cnt=12`、`bom_line_cnt=16`、`op_cnt=11`、`kc_cnt=23`、`status=FROZEN`；旧行 `status: FROZEN → ARCHIVED`（`[−]` 作废但保留，供 D 件履历引用） |

下游：`ROUTING_RELEASED`（扩展事件，幂等键 `routId:version:effectFrom` = `ROUT-SADA-26B:RevA:2026-07-15`）→ MES 缓存工艺快照、QMS 按 20 个 `qc_plan` 生成待启用方案、ERP 更新物料 `routing_version`。
守恒：A9 PLM↔MES 工艺对账差异行 0（11 道工序、23 项 KC、16 行 BOM 三处计数一致）；基线唯一有效——`select count(*) from baseline where item_code='5JD00000007' and status='FROZEN'` = 1。

### 步骤 4 ｜ 按件状态绑定检验方案

触发：2026-07-11 17:00 检验主管 QC-2001 与责任工艺师在 QMS"检验方案"页按 2.0 矩阵建 20 条方案（5 方案码 × 4 件状态），引用已冻结的 `qc_item` 库。
校验：每个方案码必须覆盖 4 个件状态，缺 1 个则该工序派工即报 `E_QC_PLAN_NOT_RESOLVED`；`sn_class` 不在枚举 `D/Y/F/B` 内直接拒绝保存；关键工序方案的 `sample_ratio` 强制 `1.00`。
单据：`QP-SADA-Z-{D,Y,F,B}-01`，`status: DRAFT → RELEASED`。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `qc_plan` | `(OP50-CRIMP, sn_class=D)` `[+]` | `plan_id=QP-SADA-Z-D-01-50`、`item_cnt=12`、`sample_ratio=1.00`、`version=RevA` |
| `qc_plan` | `OP50-CRIMP` 另 3 行 `[+]`、`(OP40,OP60,OP100,OUT) × 4 类` 16 行 `[+]` | `item_cnt: D 12 / Y 9 / F 9 / B 9`；`qc_plan` 累计 20 行 |
| `qc_plan_item` | `(QP-SADA-Z-D-01-50, 冲击后尺寸复测)` `[+]` | 仅 D 有；`qc_plan_item` 累计 169 行（46+38+41+44） |
| `qc_plan_item` | `(QP-SADA-Z-B-01-OUT, 封存状态检查)` `[+]` | 仅 B 有，`op_seq=OUT`、`photo_required=Y` |
| `qc_plan` | `QP-SADA-PROTOTYPE` `[−]` | `status: RELEASED → OBSOLETE`、`obsolete_reason=被件状态方案族取代`、`effect_to=2026-07-14` |

下游：`QC_PLAN_RELEASED` 由 QMS 内部订阅，MES 只读缓存（职责边界：方案判定归 QMS）；`routing_op.qc_plan_code` 与方案族码做全外连接校验。
守恒：A2 口径下的"应检数=实配数"——4 个件状态各自 `Σ qc_plan_item.item_cnt` = 46 / 38 / 41 / 44，与 2.0 表逐格相等；`qc_plan` 行数 20、作废 1。

### 步骤 5 ｜ 特殊过程鉴定 SPQ

触发：2026-07-10 工艺与计量对工序 40（清洗涂脂）、工序 60（力矩紧固）做特殊过程确认试验，07-13 提交 QMS 批准；滑环镀覆沿用已有效的 `SPQ-PLATING-2026-003`。
校验：确认批次连续 3 批、每批 3 件，脂层厚度与力矩偏差的 `Cpk ≥ 1.33` 方可判有效；`method_doc_rev` 必须是已发布版本；有效期不超过工艺文件复审周期（12 个月）；未鉴定通过则 `routing_op(op40).op_type=SPECIAL` 的工单无法派工。
单据：`SPQ-CLEAN-2026-004`、`SPQ-TORQUE-2026-005`（沿用主数据 `SPQ-{过程码}-{年}-{流水3}`），`status: RUNNING → APPROVED`。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `special_process_qual` | `SPQ-CLEAN-2026-004` `[+]` | `op_seq=40`、`capability_range: (空) → ≤3.0 套/月`、`valid_from=2026-07-13`、`valid_to=2027-07-12`、`cpk=1.41` |
| `special_process_qual` | `SPQ-TORQUE-2026-005` `[+]` | `op_seq=60`、`tool_id=EQ-341`、`cpk=1.52`、`requal_trigger: 换脂批号/超月产/校准失效` |
| `special_process_qual` | `SPQ-PLATING-2026-003` | `bound_item: (空) → 4DL00000014`、`valid_to: 2027-05-31`（不变，覆盖本批） |
| `routing_op` | `op_seq=40` / `op_seq=60` | `spq_id: (空) → SPQ-CLEAN-2026-004` / `→ SPQ-TORQUE-2026-005` |

下游：`SPQ_APPROVED`（扩展，幂等键 `spqId:opSeq`）→ MES 建立工序特殊过程守卫、QMS 挂 12 个月再确认提醒（2027-07-12）；`capability_range` 同步给 APS 作节拍上限。
守恒：`special_process_qual` 有效行 3（40/60/委外镀覆），与路线中标记为 `SPECIAL` 的工序数一致；D 件试制阶段的 2 次过程参数漂移记录保留为 `result=PRE_QUAL` 历史行，不计入有效行。

### 步骤 6 ｜ 人员资质授权与设备/工装基线

触发：2026-07-13 车间主任提交授权申请，计量与工装工程师同步完成 EQ-330 期间核查、EQ-341 新扳手寿命建账。
校验：见本节 SQL（A10 口径）；`operator_skill` 授权必须绑定已发布 `routing_op.mandatory_skill_code`；`equipment.calib_due_date` 必须晚于批产交付节点 2027-02-28；工装建账时 `used_qty=0` 且 `life_limit` 取自校准证书。
单据：授权单 `SKG-26B-20260713-001`（前缀 `SKG` 为扩展）；校准证书无系统单据，落 `equipment` 字段。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `operator_skill` | `(OP-1102, CRIMP-3, valid_from=2026-07-13)` `[+]` | `op_scope=工序50`、`valid_to=2027-06-30`、`granted_by=车间主任+检验部长` |
| `operator_skill` | `(OP-1102, TORQUE-2)` / `(OP-1140, ESD-2 / PCBA-ASSY-2)` | `op_scope: 试制 → 工序60 批产` / `→ 工序20、30 批产`；`valid_to` 不变 |
| `operator_skill` | `(QC-2001, FOD-DUAL)` `[+]` | `op_scope=工序100 双岗`、`valid_to=2027-04-30` |
| `equipment` | `EQ-330` / `T-02` / `AR-01` | `EQ-330: curve_rate_hz 100 → 200`、`status: IDLE → QUALIFIED`、`calib_due_date=2027-01-15` 不变；`T-02/AR-01: next_calib 2026-11-30 → 2027-03-15` / `2026-12-10 → 2027-03-20` |
| `tooling_life` | `(EQ-341, life_limit=5000)` `[+]` | `used_qty=0`、`calib_cycle_days=30`、`next_calib_date=2026-08-12`、`warn_at=4500` |

```sql
SELECT wr.op_seq, wr.operator_id, wr.report_time
FROM   work_report wr JOIN routing_op ro
       ON ro.rout_id = wr.rout_id AND ro.seq = wr.op_seq
WHERE  ro.mandatory_flag='Y'
  AND  NOT EXISTS (SELECT 1 FROM operator_skill os
        WHERE os.operator_id = wr.operator_id
          AND os.skill_code  = ro.mandatory_skill_code
          AND os.status='ACTIVE'
          AND wr.report_time BETWEEN os.valid_from AND os.valid_to);
-- A10 守卫：投料前必须 0 行
```

下游：`SKILL_GRANTED`（内部）→ MES 派工守卫刷新；`EQUIPMENT_QUALIFIED` → APS 允许排产，`tooling_life.used_qty` 由力矩报工每次 +1。
守恒：A10 —— 2026-07-15 投料后工序 50/60/100 共 3 道关键工序，需 `CRIMP-3`、`TORQUE-2`、`FOD-DUAL` 三项授权，全部覆盖 19 套 × 3 工序 = 57 次报工；`Σ tooling_life.used_qty` 应等于力矩记录数 266（步骤 11 复核）。

### 步骤 7 ｜ PRR 生产准备状态检查：12 项逐条关闭

触发：2026-07-14 09:00 PRR 审查组（工艺、检验、供应、设备、计划、成本、质量 7 方）在 ERP/PLM 的"生产准备状态检查"工作台逐条举证关闭。
校验：每项必须挂 ≥1 个证据对象（单号或表行），`evidence_ref` 为空则 `result` 不能置 `PASS`；项 9（长周期齐套）与项 10（产能节拍）允许 `CONDITIONAL` 但必须写 `condition_desc` 与 `due_date`；任一 `OPEN` 项存在时，`prr_checklist.status` 不允许 `APPROVED`。
单据：`PRR-26B-20260708-001` `status: OPEN → REVIEW → APPROVED`（2026-07-14 17:30）。

| # | 检查项 | 结果 | 证据 / 遗留条件 |
|---|--------|------|----------------|
| 1 | 转阶段评审闭环 | `PASS` | `TR-26B-20260708-001=APPROVED` |
| 2 | 技术状态基线冻结 | `PASS` | `BL-26B-SADA-Z-RevA`，12 件 / 16 行 / 11 工序 |
| 3 | MBOM 与工艺路线同步一致 | `PASS` | `sync_recon` 差异 0 行（A9） |
| 4 | 关重件与关键特性识别 | `PASS` | KC 23 项，关重件 3 件号（SADA / 谐波 / 轴承，主数据标 ★） |
| 5 | 件状态检验方案绑定 | `PASS` | `qc_plan` 20 行 / `qc_plan_item` 169 行 |
| 6 | 人员资质授权覆盖 | `PASS` | `SKG-26B-20260713-001`，3 人 5 项 |
| 7 | 特殊过程鉴定有效性 | `PASS`（07-22 重开） | `SPQ-CLEAN-2026-004`、`SPQ-TORQUE-2026-005` |
| 8 | 设备校准与工装寿命基线 | `PASS` | EQ-330 200 Hz、`tooling_life(EQ-341)` 建账 |
| 9 | 长周期物料齐套 | `CONDITIONAL` | 轴承缺 2 件、电机在途 8；条件：首批发料限 18 套，07-20 前补齐 |
| 10 | 试验台位与产能节拍 | `CONDITIONAL` | 按 2.6 套/月评估；条件：2026-07-18 前提交 T-02 排产确认 |
| 11 | 首件鉴定 FAI 计划 | `PASS` | `FAI-W50-26B-001` 计划已建、34 项已授权 |
| 12 | 成本归集口径与 WBS 切换 | `PASS` | `WBS-P-26B-SADA-BATCH` 生效 2026-07-15 |

下游：`PRR_APPROVED`（扩展，幂等键 `PRR-26B-20260708-001:26B:19`）→ MES 放行门、ERP WBS 生效、APS 解锁排产。
守恒：12 = `PASS` 10 + `CONDITIONAL` 2 + `OPEN` 0；带条件放行不等于通过，`prr_item.result=CONDITIONAL` 的行计入 07-18 / 07-20 的到期核销待办，核销完成前不得解除项 9 的 18 套发料上限。

### 步骤 8 ｜ 批量投料放行与工单下达（跨系统时序）

触发：2026-07-15 08:00 PRR 批准后，计划员在 MES 批量下达页选 19 个 SN 创建 `WO-26B-20260715-0101`。
校验：时序上 QMS 是发布方、MES 只消费，**不存在跨系统同步写**——PRR 批准事务内同写 `outbox_message`，异步投递；MES 消费后把 `wo_release_gate.source='EVENT'` 才允许 `work_order.status: CREATED → RELEASED`。兜底三层：① 投递失败按 15 min 退避重试，`retry_count` 上限 5；② 超 2 h 未达，MES 车间扫码下达返回 `E_PRR_NOT_APPROVED`，工单停 `CREATED`，人工补推接口 `POST /mes/api/wo/gate-sync` 由集成岗触发；③ T+1 d 仍缺，对账任务把差异写进 `sync_recon`（A9 口径）并挂生产主管待办，禁止手工造 `gate` 行（`source` 非 `EVENT` 的记录视为无效放行，纳入审计）。
单据：`WO-26B-20260715-0101` `status: CREATED → RELEASED`（08:42）、`MI-20260715-0311` 首批发料。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `work_order` | `WO-26B-20260715-0101` `[+]` | `item=5JD00000007`、`qty=19`、`rout_id=ROUT-SADA-26B-RevA`（快照锁定）、`bom_id=BOM-SADA-26B-RevA`、`sn_class_scope='F,B,Y'`、`prr_no=PRR-26B-20260708-001` |
| `sn_master` | `SADA-26B-Z-F-001..F-012 / B-001..B-005 / Y-001..Y-002` `[+]` 19 行 | `status: (无) → RELEASED`、`wo_no=WO-26B-20260715-0101` |
| `sn_class_history` | 19 行 `[+]` | `class_from=null → class_to=F/B/Y`、`change_type=BATCH_RELEASE`、`gate_id=PRR-26B-20260708-001`、`valid_from=2026-07-15 08:42` |
| `sn_history` | 19 行 `[+]` | `status=RELEASED`、`valid_to=null`（每 SN 恰 1 行） |
| `work_order_material` | `(轴承 2JD01000043)` `[+]` ＋ 电机/谐波/钛紧/电容/脂/胶 6 行 `[+]` | 轴承 `req 40 / issued 38 / kit_qty 0 → 18 套`、`lot=LOT-BRG-26B-01`、`mi_no=MI-20260715-0311`；电机 `19/11`、谐波 `19/19`、钛紧 `266`（14×19 净）、电容 `3534`（186×19 净）、脂 `0.24`、胶 `3.20/2.80`（缺 0.40） |

下游：`WO_RELEASED`（幂等键 `woNo`）→ WMS 建拣配、ERP 建生产订单挂 `WBS-P`；`MATERIAL_ISSUED`（`txnNo`）→ MES 写 `sn_genealogy`、ERP 归集材料成本。
守恒：A1 有效件状态行 20 = `sn_master` 行 20；齐套率 = 18/19 = 94.7%（07-15）→ 100%（07-19 轴承到料 2 件、胶补采 0.40 kg）；A3 发料 38 = 装机 36 + 线边余 2 + 退料 0 + 报废 0（轴承口径，损耗在 MGU 子料单体现）。

### 步骤 9 ｜ ERP 侧 WBS 与成本归集口径切换

触发：2026-07-15 09:10 财务与项目控制员按 PRR 项 12 结论执行口径切换：研制件走研制费、批产件走生产成本，按 SN 实际成本法归集（方法见 [cost-accounting](../concepts/cost-accounting-20260906.md)）。
校验：一个 `sn` 只能挂 1 个 `cost_object`，且 `cost_object.wbs_id` 的 `cost_type` 必须与 `sn_class` 匹配（`D→R`、`F/B/Y→P`）；`project_wbs.status=CLOSED` 后任何新增 `cost_element` 被拒绝，只能走红冲重挂；WBS 切换生效日之前发生的领料按 `txn_time` 归属，不得整批改挂。
单据：`WBS-P-26B-SADA-BATCH`（目标成本 268.50 × 19 = 5,101.50 万元，预算 5,380.00 万元、裕量 278.50 万元）。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `project_wbs` | `WBS-R-26B-SADA-IDENT` | `status: FROZEN_PENDING → CLOSED`、`used_amt: 386.40 → 398.80 → 386.40 万元`（08:42 误挂批产领料 12.40，红冲后复位）、`close_date: 2026-07-15 → 2026-07-16` |
| `project_wbs` | `WBS-P-26B-SADA-BATCH` `[+]` | `cost_type=生产成本`、`budget=5,380.00`、`effect_from=2026-07-15`、`qty=19`、`method=实际成本法按 SN` |
| `cost_object` | `CO-R-26B-SADA-D001` | `wbs: WBS-R-26B-SADA-IDENT → 同`、`status: OPEN → SETTLED`、`total=386.40 万元`（材料 178.90 + 外协 12.60 + 人工 76.30 + 制费 41.20 + 试验 77.40） |
| `cost_object` | `CO-P-26B-SADA-{F001..F012}` 12 行 `[+]` | `sn_class=F`、`wbs_id=WBS-P-26B-SADA-BATCH`、`target=268.50 万元/套`、`dest=DELIVER` |
| `cost_object` | `CO-P-26B-SADA-{B001..B005}` 5 行 `[+]`、`{Y001..Y002}` 2 行 `[+]` | 备份件 `dest=STOCK_RESERVED`（完工转储备存货，不冲减生产成本而转库存价值）；验收件 `dest=REMAIN` 留厂，试验费计鉴定成本中心 |
| `cost_object` | `CO-P-26B-SADA-F001`（红冲 1 行） | `material_cost: 0 → 12.40 万元` 于 07-15 08:42 误挂 `WBS-R`，07-16 09:00 红冲并迁移至 `WBS-P`（`WBS-P 0 → 12.40`） |

下游：`OP_COMPLETED`（`woNo:opSeq:reportNo`）驱动人工/制费逐工序归集；`MATERIAL_ISSUED` 驱动材料成本；`COST_OBJECT_CREATED`（扩展）回写 MES `sn_doc` 的成本凭证号。
守恒：A8 口径 20 个 `cost_object` = 20 个 SN；飞行/备份/验收单套 268.50 万元（材料 172.30 + 外协 12.60 + 人工 38.40 + 制费 34.60 + 试验检验 10.60），19 套 = 5,101.50 万元 ≤ 预算 5,380.00 万元；鉴定件为飞行件的 386.40/268.50 = 1.44 倍。

### 步骤 10 ｜ PRR 重评：交付节点提前触发（硬耦合 2）

触发：2026-07-22 发射窗口调整、交付节点由 2027-02-28 提前 45 天至 2027-01-14（`MT-26B-2026-0044R1`，详见 [P0-1](./demo-flow-mission-change-20261116.md)），ERP 发布 `MISSION_CHANGED`，PRR 检查单被强制重评。
校验：重评规则——交付节点变动 >30 天则项 7（特殊过程鉴定有效性）、项 9（齐套）、项 10（产能节拍）自动置 `REOPEN`；`SPQ.capability_range` 与月产需求比对，超上限必须补做工艺能力验证，否则该项不能回 `PASS`，且工序 40/60 的新派工被拦截。
单据：`PRR-26B-20260708-001-R1`（同 `prr_no` 追加 `rev=R1`，历史 `rev=R0` 保留不可改）。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `prr_checklist` | `PRR-26B-20260708-001` | `rev: R0 → R1`、`status: APPROVED → RE_REVIEW`、`trigger=MT-26B-2026-0044R1`、`reopen_cnt: 0 → 1` |
| `prr_item` | `#7 特殊过程鉴定有效性` | `result: PASS → OPEN → PASS`（07-22 11:00 重开 / 07-24 15:00 关闭）、`evidence: SPQ-CLEAN-2026-004 → + SPQ-26B-CLEAN-CAP-001`、`capability_range: ≤3.0 → ≤3.4 套/月`（月产 19/5.97=3.2，原按 19/7.43=2.6 评估） |
| `special_process_qual` | `SPQ-CLEAN-2026-004` | `capability_range: ≤3.0 → ≤3.4 套/月`、`requal_reason=交付提前致月产 +23%`、`valid_to=2027-07-12`（不变） |
| `prr_item` | `#9 长周期物料齐套` | `result: CONDITIONAL → PASS`（07-19 轴承 2 件到料复验合格）、重评时改挂 FPGA 断供预警至 P0-5 |
| `prr_item` | `#10 试验台位与产能节拍` | `result: CONDITIONAL → PASS → OPEN → PASS`（07-18 关闭 / 07-22 重开 / 07-23 关闭，T-02 排产由 8 台位轮次压缩为 6 轮） |
| `routing_op` | `op_seq=40 / 60` | `hold_flag: N → Y → N`（重开期间拦截新派工，累计拦截 14 套次） |

下游：`PRR_APPROVED`（幂等键 `PRR-26B-20260708-001:26B:19:R1`）；MES 侧 `wo_release_gate` 追加 `rev=R1` 行并把 `valid_from` 对齐 2026-07-24 15:00；APS 收到 `capability_range` 变更后重排工序 40/60。
守恒：A5 —— 重开窗口 07-22 11:00 ~ 07-24 15:00 内工序 40 报工 0 行（拦截生效），`Σ(合格+不合格)=投产` 未被破坏；PRR 状态分布 12 = `PASS` 12（R1），历史 `CONDITIONAL` 2 行保留在 `rev=R0`。

### 步骤 11 ｜ 批量第一套首件鉴定 FAI，解锁后续 18 套

触发：2026-07-28 08:00 批量首套 `SADA-26B-Z-F-001` 报工序 50 完工，QMS 依据 `routing_op.fai_gate_flag=Y` 自动生成首件鉴定任务；首件按 AS9102 三要素（产品实物、工艺过程、检验记录）检查。
校验：FAI 未 `PASSED` 前，同工单同工序第 2 套起的报工被拦截（下 SQL）；首件 34 项 = 工序 50 的 F 方案 9 项检验 + 工艺与记录 25 项（AS9102 三要素，非 `qc_plan_item` 子集），逐项留存实测值 + 量具编号 + 校准有效期；"需修"项允许带 `corrective_action` 限期关闭，"不合格"项直接触发 NCR 并使 FAI 停 `FAILED`。
单据：`FAI-W50-26B-001` `status: DRAFT → IN_PROGRESS → PASSED`（2026-07-30 02:00），检查项 34、合格 32、需修 2、不合格 0。

| 数据变化：表 | 行（业务键） | 字段 before → after |
|----|----|------|
| `fai_report` | `FAI-W50-26B-001` `[+]` | `wo_no=WO-26B-20260715-0101`、`op_seq=50`、`first_sn=SADA-26B-Z-F-001`、`item_cnt=34`、`ng_cnt=0`、`need_fix_cnt=2`、`rout_id=ROUT-SADA-26B-RevA` |
| `qc_task` | `IT-20260728-20510` `[+]` | `plan_id=QP-SADA-Z-F-01-50`（由 `OP50-CRIMP` + `sn_class=F` 解析）、`sn=SADA-26B-Z-F-001`、`result=PASSED` |
| `work_order_operation` | `op_seq=50` 后续 18 套（并 `sn_history` 18 行 `hold_flag`） | `status: OPEN → HOLD`（07-28 08:10）`→ OPEN`（07-30 02:05）、`hold_by=FAI-W50-26B-001`；`sn` 状态仍 `IN_ASSY` |
| `tooling_life` | `EQ-341` | `used_qty: 0 → 266`（14 点 × 19 套，工序 60 每点 +1）、`warn_at=4500` 未触 |
| `work_report` | `op_seq=50` 19 行 `[+]` | 合格 18、不合格 1（`F-012` 过盈量 0.048 mm 超上限 0.045，`NCR-26B-20260805-001`（第四章格式扩号）转工序内返工） |

```sql
SELECT o.wo_no, o.op_seq, COUNT(*) AS held_sn
FROM   work_order_operation o
JOIN   routing_op ro ON ro.rout_id=o.rout_id AND ro.seq=o.op_seq
LEFT   JOIN fai_report f
       ON f.rout_id=o.rout_id AND f.op_seq=o.op_seq AND f.status='PASSED'
WHERE  ro.fai_gate_flag='Y' AND o.status='HOLD'
GROUP  BY 1,2;
-- 2026-07-29 期望 1 行（op 50，held_sn=18）；2026-07-31 期望 0 行
```

下游：`FAI_PASSED`（扩展，幂等键 `faiId:routId:opSeq`）→ MES 解除同工序门控、QMS 把首件参数记录写入 SPC 初始控制限（见 [tech-quality-spc](../tech/tech-quality-spc-20260816.md)）；`OP_COMPLETED`（`woNo:50:reportNo`）→ ERP 归集人工 6 h/套。
守恒：A5 —— 工序 50 投产 19 = 合格 18 + 不合格 1；工序 60 投产 19 = 合格 19 + 不合格 0，力矩记录 19×14 = 266 条 = `tooling_life.used_qty`；A1 —— 19 套 SN 的 `sn_history` 有效节点合计 19，加 D-001 共 20，与 `sn_master` 行数相等。

## 3. 异常分支

### 3.1 跳过首件鉴定直接批量压装，首件尺寸不合格

错误做法：07-28 首件送检后，车间为赶 T-02 台位（07-29 窗口），由工艺员把 `routing_op(op 50).fai_gate_flag` 临时改 `N`，并把后续 8 套一口气报工过工序 50，计划 07-30 补 FAI 结论。

拦不拦得住：部分拦得住。派工侧拦不住——`fai_gate_flag` 是 `routing_op` 上的普通字段，改主数据表的动作只受 PLM 变更权限约束，MES 读缓存 15 min 刷新，窗口期内 8 套可以报工；但两处事后拦截会响：① 首件 FAI 34 项未闭环而批量 8 套的 `work_report` 已 `PASS`，门控依据晚于被门控动作，审计报警；② `tooling_life` 与力矩记录数、`sn_genealogy` 与轴承批次号的守恒核对在日终对账失败，写 `sync_recon`。也就是说：**流程拦不住，账拦得住，晚一天发现。**

脏数据长这样（假设首件 FAI 判 `FAILED`：过盈量实测 0.062 mm，超定型窗口 0.030~0.045 mm）：

| 表 | 脏行 | 症状 |
|----|------|------|
| `work_report` | `op_seq=50` 8 行 `result=PASS` | 带不合格尺寸流转，A5 假性成立（已报工 9 = 合格 9 + 不合格 0，含首件） |
| `sn_genealogy` | 8 行 `child_lot=LOT-BRG-26B-01`、`link_type=SN2LOT` | 轴承已绑定，压装后不可直接复用 |
| `sn_history` | 8 行 `status: IN_ASSY → IN_TEST` | 越级：多余物检查（工序 100）尚未做 |
| `fai_report` | `FAI-W50-26B-001.status=FAILED` 却晚于 8 行报工 | 时间线倒挂：门控依据发生在被门控动作之后 |
| `cost_object` | 8 套 `material_cost` 已含轴承 7.60 万元/套 | 拆解后轴承报废，成本转 `scrap_cost` |
| `qc_record` | 8×9 项 F 方案首检记录缺失 | 事后补录风险，违反参数 100% 留存 |

返工范围圈定（同轴承批次 + 同压装参数漂移窗口 + 未经合格首件授权）：

```sql
SELECT DISTINCT g.parent_sn AS sn, wr.report_no, wr.report_time
FROM   sn_genealogy g
JOIN   work_report wr ON wr.wo_no = g.wo_id AND wr.op_seq = 50
WHERE  g.child_lot = 'LOT-BRG-26B-01'
  AND  wr.wo_no = 'WO-26B-20260715-0101'
  AND  wr.report_time >= (SELECT MIN(created_at) FROM qc_task
                          WHERE sn='SADA-26B-Z-F-001' AND op_seq=50)
  AND  NOT EXISTS (SELECT 1 FROM fai_report f
        WHERE f.op_seq=50 AND f.status='PASSED'
          AND f.passed_at >= wr.report_time);
-- 期望 8 行：F-001~F-006、B-001、B-002
```

回滚与红冲：① `routing_op(op 50)` 版本快照回退为 `RELEASED` 原行，禁改主表（新 `rev` 走工艺变更）；② 8 行 `work_report` 负数报工红冲（原行 `[−]` + 同 `report_no` 的 `qty=-1` 冲正行），A5 复位为 已报工 9 = 合格 0 + 不合格 9（8 套转待判定，随返工复压后重报）；③ `sn_history` 关闭 `IN_TEST` 节点、新增 `REFURBISH` 有效节点，A1 恢复；④ 开返工工单 `WO-26B-20260730-0115`（拆解 8 套 MGU、轴承退 `REJECTED` 库位、补 16 件新轴承），`inv_txn` 冲正；⑤ 损失 152 工时（8 × 19 h）+ 报废轴承 16 件 × 3.80 万元 = 60.80 万元，挂 `cost_object.rework_cost`；⑥ 质量问题开 `ZL-26B-20260730-001` 双五条归零（见 [P0-4](./demo-flow-quality-closure-20261116.md) 的归零口径）。

### 3.2 件状态由 Y 改 F 未走转阶段评审，飞行件缺热真空却已发货

错误做法：10 月下旬发现 1 套飞行件总装电测异常风险，计划员与检验员协商把验收件 `SADA-26B-Z-Y-002` 顶为飞行件，直接在 `sn_master` 执行 `update sn_class='F'` 并换贴 `SADA-26B-Z-F-013` 标签，未走转阶段评审、未申请 `sn_class_history` 变更。

拦不拦得住：拦不住，且三道闸各漏一层。① `sn_master` 是普通主档，`sn_class` 无状态机守护，直接 UPDATE 不触发校验；② 检验方案是**开工时点解析**的，Y-002 已按 `QP-SADA-Z-Y-01`（38 项、4 个试验项目）做完并 `PASSED`，改成 F 后历史 `qc_task` 不回算，不会自动补 D 件独有的 3 项鉴定试验（冲击/热真空/10 年等效寿命）；③ 工序 110 出厂评审只校验"当前方案内项目齐全"与"归零闭环"，不校验"方案族与件状态历史匹配"，于是照常放行、签发 CofC，`SN_SHIPPED` 事件正常发出。缺陷在第 4 节 Q4 的日终对账才暴露——发货后 3 天。

脏数据：

| 表 | 脏行 | 症状 |
|----|------|------|
| `sn_master` | `SADA-26B-Z-F-013` | `sn_class=F` 且 SN 第 4 段=F 看似自洽，但 F 段流水越界（应 001..012）、无任何 `gate_id` 支撑、`wo_no` 仍指 19 套工单 |
| `sn_class_history` | `…Y-002` 行成孤儿、无 `F-013` 行 | 与 `sn_master` 按 `sn` 全外连接双向差 1，A1 断链；本批分布变 13 F + 1 Y + 5 B，与主数据 12+2+5=19 冲突 |
| `test_report` | 0 行新增 | 该 SN `TVAC/SHK/LIFE` 项目数 0，飞行件试验矩阵缺 3 项 |
| `qc_record` | 38 项按 Y 方案 | 与 F 方案 41 项差 3 项出厂项，出厂放行签字人仍为"用户代表"而非"设计代表" |
| `sn_doc` | CofC `COC-26B-20261031-0013` | 声明试验矩阵与实际不符，证明文件失效；履历本 `LOG` 未建 |
| `cost_object` | `CO-P-26B-SADA-Y002` | `dest_code=REMAIN` 但已发货，`SN_SHIPPED` 与 `dest` 矛盾 |

补齐路径（按 AS9100 不合格品控制 + 厂内规范）：① 立即 `STOP_SHIP` 并向总装与用户代表发追损通知，`SN_SHIPPED` 事件补偿为 `SN_RETURN_REQUESTED`；② 实物回厂后补做热真空（`T-02` 台位 36 h，与 P0-2 RevB 低温试验争抢台位，见主数据硬耦合 3）；③ 转阶段评审补开 `TR-26B-20261102-002`，通过后由 QMS **补写** `sn_class_history[Y→F, change_type=RETRO_STAGE_PASS, gate_id=TR-26B-20261102-002]`，禁止手工插行；④ 按 `QP-SADA-Z-F-01` 补 3 项出厂检验并重签放行（检验部长 + 设计代表）；⑤ 作废 `COC-26B-20261031-0013`（`status: ISSUED → VOID`），新建履历本 `LOG-SADA-26B-Z-F-013` 并把 Y-002 的历史段整体并入；⑥ 开 `ZL` 归零，把"`sn_class` 直接 UPDATE 无守护"列为管理归零条款，改为必须经 `sn_class_change_request` 单据驱动。

## 4. 对账校验点

| # | 查询 | 期望结果 |
|---|------|---------|
| Q1 | A1 件状态唯一有效（见下 SQL） | `sn_cnt=20`、`class_valid_rows=20`、`bad_sn_cnt=0` |
| Q2 | A5 关键工序报工守恒（op 40/50/60/100） | 4 行，每行 `total=19` 且 `qualified+ng=19`；op 50 为 `18+1`，其余 `19+0` |
| Q3 | A10 关键工序操作人当时具备有效资质 | 0 行；非 0 即视为本篇写错 |
| Q4 | SN 编码第 4 段与 `sn_class` 不一致（拦 3.2） | 0 行（`SADA-26B-Z-F-013` 若出现即为脏数据） |
| Q5 | PRR 重评完整性：`reopen_cnt` 与交付节点变更次数一致 | `reopen_cnt=1`、`prr_item` 总行 12 + 重开历史 2 = 14 |

```sql
SELECT COUNT(DISTINCT m.sn) AS sn_cnt,
       SUM(CASE WHEN h.valid_to IS NULL THEN 1 ELSE 0 END) AS class_valid_rows,
       COUNT(DISTINCT CASE WHEN h.valid_to IS NULL THEN m.sn END)
         - COUNT(DISTINCT m.sn) AS bad_sn_cnt
FROM   sn_master m
LEFT   JOIN sn_class_history h ON h.sn = m.sn
WHERE  m.item_code = '5JD00000007' AND m.model_batch = '26B';
-- Q1 期望 20 | 20 | 0
```

```sql
SELECT wr.op_seq, SUM(wr.qualified_qty) AS q, SUM(wr.ng_qty) AS ng,
       COUNT(DISTINCT wr.sn) AS sn_cnt
FROM   work_report wr
WHERE  wr.wo_no='WO-26B-20260715-0101' AND wr.op_seq IN (40,50,60,100)
GROUP  BY 1 ORDER BY 1;
-- Q2 期望 4 行，op50 为 18/1/19，其余 19/0/19
```

```sql
SELECT DISTINCT SPLIT_PART(m.sn,'-',4) AS sn_seg, m.sn_class
FROM   sn_master m JOIN sn_class_history h
       ON h.sn=m.sn AND h.valid_to IS NULL
WHERE  m.item_code='5JD00000007' AND m.model_batch='26B'
  AND  SPLIT_PART(m.sn,'-',4) <> h.class_to;
-- Q4 期望 0 行
```

Q3 用步骤 6 的守卫查询按 `wo_no='WO-26B-20260715-0101'` 复跑，期望 0 行；命中 1 行即意味着该 SN 的关键工序记录不可采信，必须重新派工复压。

## 5. 指标与代价

| 指标 | 数值 | 口径 |
|------|------|------|
| 转阶段周期 | 15 天（2026-06-30 试验完成 → 2026-07-15 投料放行） | 评审 07-08 → 定型发布 07-11 = 3 天；发布 → PRR 批准 07-14 = 3 天；批准 → 投料 = 1 天 |
| PRR 一次通过率 | 83.3%（12 项中 10 项 `PASS`、2 项 `CONDITIONAL`） | 带条件放行不计入通过 |
| PRR 重评代价 | +2 天（07-22 11:00 重开 → 07-24 15:00 关闭）、拦截工序 40/60 新派工 14 套次 | 交付提前 45 天触发，项 7 重开 |
| 特殊过程能力上限 | 月产 2.6 → 3.2 套/月（+23%），`capability_range` 由 ≤3.0 提到 ≤3.4 套/月 | 超原鉴定范围，须补做工艺能力验证 |
| FAI 检查项 | 34 项：合格 32、需修 2、不合格 0；周期 2 天（07-28 08:00 → 07-30 02:00） | AS9102 三要素；门控使 18 套滞留 42 h、36 套·日 |
| 工序一次交检合格率 | 工序 50 = 94.7%（18/19）、工序 60 = 100%（19/19）、四关键工序综合 = 98.7%（75/76） | A5 口径 |
| 齐套率 | 投料日 94.7%（18/19）→ 2026-07-19 达 100% | 轴承缺口 2 件 |
| 单套成本对比 | 鉴定件 386.40 万元 vs 飞行件 268.50 万元 = 1.44 倍（+117.90 万元/套） | 材料 +6.60、人工 +37.90、制费 +6.60、试验 +66.80 |
| 批产成本总额 | 19 套 × 268.50 = 5,101.50 万元，预算 5,380.00 万元（裕量 278.50、5.5%） | 5 套备份 1,342.50 万元完工转储备、2 套验收 537.00 万元留厂 |
| WBS 切换偏差 | 12.40 万元误挂研制费 → 07-16 红冲归位，偏差率 0.24% | `cost_element` 迁移 1 笔 |
| 异常分支 3.1 代价 | 152 返工工时 + 60.80 万元轴承报废 + 8 套交付顺延 5 天 | 假设情景，非本篇主线 |
| 追溯耗时 | 单 SN 全链反查 3.2 s（递归深度 4、节点 21 个）；D 件 4 份鉴定报告 + 23 项 KC + 12 件号 MTR 齐套核验 25 min | 定型后 KC 与 MTR 清单机器可读是前提 |

一句话代价：**工艺定型的钱花在批产之前，省在 19 套之后**——鉴定件单套比飞行件贵 117.90 万元，其中 77.40 万元是试验与跟产人工，买到的正是 3 道关键工序的参数窗口与 20 条件状态检验方案；跳过它们，异常分支 3.1 一套就要赔 19 工时和 7.60 万元。

## 相关文档

- [贯穿样例主数据：SADA 26B](./demo-case-sada-20261116.md)（件状态、BOM、路线、断言库、时间线、硬耦合）
- [demos 目录说明与模板](./README.md)
- [Demo P0-1 型号任务变更传导链](./demo-flow-mission-change-20261116.md)（PRR 重评的触发源）
- [Demo P0-2 Ⅰ类技术状态更改](./demo-flow-config-change-20261116.md)（本条 RevA 定型路线与 RevB 的接续）
- [新品导入流程 NPI](../processes/process-npi-20260906.md) ｜ [PLM 业务流程](../plm/plm-workflow-20260816.md) ｜ [MES 业务流程](../mes/mes-workflow-20260816.md)
- [工艺路线与 BOP 管理](../concepts/routing-management-20260906.md) ｜ [生产成本核算](../concepts/cost-accounting-20260906.md)
- [QMS 质量管理体系概述](../systems/qms-overview-20260906.md) ｜ [质量管理与 SPC](../tech/tech-quality-spc-20260816.md) ｜ [主数据同步可靠性](../tech/tech-mdm-sync-20260816.md)
