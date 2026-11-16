# 航天贯穿样例：太阳翼驱动机构 SADA

> 创建时间：2026-11-16
> 模块：demos

`docs/demos/` 下 7 篇跨系统流程 demo 共用的型号、产品结构、主数据、编码与数量基线。**所有 demo 的单据号、物料编码、数量必须取自本文，不得各造一套**，否则读者无法把 7 篇串成一条链。

## 一、场景设定

| 项 | 设定 |
|----|------|
| 单位 | 某卫星整星及配套部件研制厂（航天科研院所下属厂） |
| 型号 | 低轨通信卫星 26B 批次（简称 26B） |
| 贯穿产品 | 太阳翼驱动机构 SADA（Solar Array Drive Assembly） |
| 产品定位 | 关重件★，单件序列号管理，全生命周期履历 |
| 每星用量 | 2 套（+Y / −Y 侧各一） |
| 本批规模 | 飞行件 12 套 + 备份件 5 套 + 验收件 2 套 = 19 套（鉴定件 D-001 已于 2026-06 完工，不占本批投料） |
| 主贯穿个体 | `SADA-26B-Z-F-007`（第 7 台飞行件，装机去向 004 星 −Y 侧） |
| 生产模式 | 项目制单件小批，实际成本法按 SN 归集，不用标准成本 |
| 周期 | 2026-07-15 投料，交付总装节点原定 2027-02-28，因窗口提前改为 2027-01-14 |

### 组织架构（数据权限维度）

```
工厂 F26  航天器机电事业部
└─ 成本中心 CC-DJ  电源与机构车间
   ├─ 工位 W-DZ-01  电子装联（DCU 装配、PCBA 检验）
   ├─ 工位 W-JD-01  机构装配（MGU、力矩、压装）
   ├─ 工位 W-ZH-01  部装联调
   ├─ 试验站 T-02   环试（振动/热循环）
   ├─ 老化房 AR-01  三综合老炼 72h
   └─ 库房 WH-01   精密元器件库（温湿度受控）/ WH-02 金属材料库 / WH-03 线边库
```

### 系统职责边界

| 系统 | 拥有的数据 | 不得由谁写入 |
|------|-----------|-------------|
| PLM | 物料、图样文档、BOM、工艺路线、技术状态基线、ECR/ECN | ERP/MES 只读同步 |
| ERP | 型号任务书、采购订单、项目 WBS、库存总账、成本汇总 | MES 不直接改 |
| MES | 工单、派工、报工、SN 履历、装机谱系、设备与人员资质 | ERP 不回改 |
| WMS | 批次库存、库位、拣配、有效期与锁定状态 | MES 只发起事务 |
| QMS | 检验任务与结果、NCR/MRB、超差让步、归零、DPA、特殊过程鉴定 | MES 只读判定 |
| 集成层 | 本地消息表、幂等记录、对账差异 | — |

跨系统一致性机制见 [tech-mdm-sync](../tech/tech-mdm-sync-20260816.md)，接口清单见 [integration-architecture](../integration/integration-architecture-20260816.md)。

## 二、产品结构（装机谱系）

```
6ZH00000001 低轨通信卫星 26B            SN: SAT-26B-Z-F-004
└─ 5BZ00000002 太阳翼组件（−Y 侧）       SN: WING-26B-Z-F-004B
   └─ 5JD00000007 太阳翼驱动机构 SADA ★   SN: SADA-26B-Z-F-007   ← 主线
      ├─ 4DZ00000012 驱动控制单元 DCU     SN: DCU-26B-Z-F-007
      │  ├─ 4DZ10000021 DCU 控制板 PCBA   SN: PCBA-26B-F-0231
      │  │  ├─ 2DZ10000034 FPGA 现场可编程门阵列      LOT-FPGA-2512-07
      │  │  ├─ 2DZ10000035 抗辐照电源管理芯片         LOT-PMIC-2601-03
      │  │  ├─ 2DZ10000036 贴片电容 0402（批次件）    LOT-MLCC-2602-11
      │  │  └─ 7DZ00000005 锡膏 SAC305（有效期辅料）  LOT-PASTE-2604-02
      │  ├─ 3JD02000044 铝基结构板（机加件）          LOT-AL7075-26B-05
      │  └─ 7RL00000008 灌封胶 EPOXY-21（有效期辅料） LOT-EPOX-2603-06
      ├─ 4JD00000013 机械驱动组件 MGU      SN: MGU-26B-Z-F-007
      │  ├─ 2DL00000051 步进电机（长周期外购）        LOT-MOTOR-26B-01
      │  ├─ 2JD01000042 谐波减速器（SN 件）           SN: HD-26B-0042
      │  ├─ 2JD01000043 角接触球轴承 71904 ★         LOT-BRG-26B-01   ← P0-2 更改对象
      │  ├─ 2JD02000045 钛合金紧固件（力矩记录）      LOT-TI-26B-11
      │  └─ 7BZ00000009 润滑脂 LP-400（有效期辅料）   LOT-GREASE-2601-2
      └─ 4DL00000014 滑环组件（特殊过程委外）         LOT-SLIP-26B-03
```

上表为 **2026-07-15 投料时点的初始装机谱系**。P0-6 的让步拆换、P0-2 的轴承更改、P0-7 的加改装会各自改写其中若干行，每篇 demo 必须写明"改写了哪几行、改成了什么"。

覆盖能力：单件 SN 追溯、批次追溯、装机位置、有效期物料、辅料损耗、特殊过程、人员资质、超差让步、委外外协、DPA 元器件、软件配置项（PCBA 固件）。

## 三、编码规则

### 物料编码（11 位定长）

`{大类1}{专业2}{小类3}{流水5}`

| 段 | 取值 |
|----|------|
| 大类 | 1 原材料 · 2 元器件/外购件 · 3 零件 · 4 组件 · 5 部件 · 6 整机 · 7 工艺辅料 · 8 包装低值 |
| 专业 | JD 机构 · DZ 电子 · DL 动力 · RL 热控 · BZ 复材 · ZH 总装 |
| 小类 | 010 传动件 · 020 结构紧固件 · 100 半导体 · 200 混合集成 · 900 辅料 |

物料版本用 `Rev`（A、B、C…）区分技术状态，**同一编码多 Rev 并存**，生效由技术状态基线决定。

### 序列号 SN

`{产品代码}-{型号批次}-{研制阶段}-{件状态}-{顺序3}`

| 维度 | 取值 |
|------|------|
| 研制阶段 | M 模样 · C 初样 · Z 正样 |
| 件状态 | D 鉴定件 · Y 验收件 · F 飞行件 · B 备份件 |

例：`SADA-26B-Z-F-007` = 正样阶段飞行件第 7 台。件状态决定检验方案、成本科目与放行权限，见 P0-3。

### 批次号 LOT

`LOT-{物料缩写}-{年周}-{日内序号}`，例 `LOT-BRG-26B-01`。批次与供应商批号（`sup_lot`）、材料证明书号（`mtr_no`）三元绑定。

## 四、单据号规则

前缀固定，中段型号，尾段取号采用号段模式（实现见 [tech-concurrency](../tech/tech-concurrency-20260816.md)）。

| 单据 | 前缀 | 示例 | 归属系统 |
|------|------|------|---------|
| 型号任务书 | MT | MT-26B-2026-0044 | ERP |
| 技术状态更改请求 | ECR | ECR-26B-20260820-0007 | PLM |
| 更改委员会决议 | CCB | CCB-26B-G5-003 | PLM |
| 技术状态更改通知单 | ECN | ECN-26B-20260908-0003 | PLM |
| 更改执行单 | ECO | ECO-26B-20260909-0012 | PLM |
| 工艺路线 | ROUT | ROUT-SADA-26B-RevA | PLM |
| 生产工单 | WO | WO-26B-20260715-0101 | MES |
| 排故/加改装工单 | WO | WO-26B-20261105-0233 | MES |
| 派工单 | DP | DP-20260715-00871 | MES |
| 领料单 / 退料单 | MI / MR | MI-20260715-0311 | MES↔WMS |
| 委外发出 / 接收 | OS-OUT / OS-RCV | OS-OUT-20260722-004 | ERP↔MES |
| 库存事务 | TXN | TXN-20260715-100872 | WMS |
| 检验任务 | IT | IT-20260716-20441 | QMS |
| 不合格品审理单 | NCR | NCR-26B-20260811-004 | QMS |
| 超差让步 / 豁免 | DEV | DEV-26B-20260813-002 | QMS |
| 复验申请 | RV | RV-26B-20260820-015 | QMS |
| DPA 报告 | DPA | DPA-26B-20260811-006 | QMS |
| 首件鉴定 | FAI | FAI-W50-26B-001 | QMS |
| 特殊过程鉴定 | SPQ | SPQ-PLATING-2026-003 | QMS |
| 质量问题归零单 | ZL | ZL-26B-20260827-001 | QMS |
| 履历本 | LOG | LOG-SADA-26B-Z-F-007 | MES |
| 基线（技术状态） | BL | BL-26B-SADA-Z-RevA | PLM |

## 五、主数据基线

### 物料属性（12 个主线件）

| 编码 | 名称 | 追溯 | 关键属性 | 来源 | 提前期 |
|------|------|------|---------|------|-------|
| 5JD00000007 | SADA 驱动机构 | SN | 关重件★、寿命 10 年 | 自制装配 | 60 d |
| 4DZ00000012 | 驱动控制单元 DCU | SN | 含软件配置项 CSCI | 自制 | 25 d |
| 4DZ10000021 | DCU 控制板 PCBA | SN | 静电敏感等级 2 | 自制 SMT | 12 d |
| 2DZ10000034 | FPGA | LOT+SN | 抗辐照、DPA 抽样 2% | 进口外购 | 180 d |
| 2DZ10000035 | 抗辐照电源芯片 | LOT | QML 名录内 | 外购 | 90 d |
| 2DZ10000036 | 贴片电容 0402 | LOT | 湿度敏感等级 3 | 外购 | 30 d |
| 2JD01000042 | 谐波减速器 | SN | 关重件★、回差 ≤0.6′ | 外购 | 120 d |
| 2JD01000043 | 角接触球轴承 71904 | LOT | 关重件★、复验必检、需 MTR | 外购 | 90 d |
| 2JD02000045 | 钛合金紧固件 | LOT | 力矩 100% 记录 | 外购 | 45 d |
| 2DL00000051 | 步进电机 | LOT | 长周期、批次一致性 | 外购 | 150 d |
| 4DL00000014 | 滑环组件 | LOT | 特殊过程：镀覆委外 | 委外 | 70 d |
| 7BZ00000009 | 润滑脂 LP-400 | LOT | 有效期 12 个月、低温存放 | 外购 | 60 d |

有效期件与锁定：`inv_lot.expire_date` 过期由 WMS 自动置 `lock_flag=EXPIRED`，MES 发料校验拦截。

### BOM-SADA-26B-RevA（1 套用量）

| 行 | 父件 | 子件 | 用量 | 损耗 | 装机位置 | 备注 |
|----|------|------|------|------|---------|------|
| 10 | SADA | DCU 4DZ00000012 | 1 | 0 | +Y 腔体 | SN 绑定 |
| 20 | SADA | MGU 4JD00000013 | 1 | 0 | 主轴侧 | SN 绑定 |
| 30 | SADA | 滑环 4DL00000014 | 1 | 0 | 底座 | 委外件 |
| 40 | DCU | PCBA 4DZ10000021 | 1 | 0 | 槽位 1 | |
| 50 | DCU | 铝基板 3JD02000044 | 1 | 3% | — | 机加 |
| 60 | DCU | 灌封胶 7RL00000008 | 0.15 kg | 15% | — | 辅料 |
| 70 | MGU | 步进电机 2DL00000051 | 1 | 0 | 电机座 | |
| 80 | MGU | 谐波减速器 2JD01000042 | 1 | 0 | 输入级 | |
| 90 | MGU | 轴承 2JD01000043 | 2 | 5% | 上/下支点 | RevA 为 71904 |
| 100 | MGU | 钛紧固件 2JD02000045 | 14 | 7% | 法兰 8 / 支座 6 | |
| 110 | MGU | 润滑脂 7BZ00000009 | 0.010 kg | 25% | 滚道 | |
| 120 | PCBA | FPGA 2DZ10000034 | 1 | 0 | U7 | |
| 130 | PCBA | 电源芯片 2DZ10000035 | 2 | 5% | U3/U4 | |
| 140 | PCBA | 贴片电容 2DZ10000036 | 186 | 8% | 多位置 | 虚拟件"片式元件组"展开 |
| 150 | PCBA | 锡膏 7DZ00000005 | 0.020 kg | 40% | — | 印刷工序 |
| 160 | SADA | 包装箱 8ZH00000006 | 1 | 0 | — | 交付随件 |

`bom_line` 表核心字段：`bom_id, line_no, parent_item, child_item, qty, loss_rate, pos, phantom_flag, substitute_group, routing_op_id, effective_from, effective_to, change_order_id`。`pos` 是装机位置，追溯与拆换判定依赖它。

### ROUT-SADA-26B-RevA 工艺路线

| 工序 | 名称 | 工作中心 | 类型 | 强制校验 | 标准工时 |
|------|------|---------|------|---------|---------|
| 10 | 齐套确认与领料 | WH-01 | 仓库 | 齐套率 100%、有效期、锁定态 | 4 h |
| 20 | DCU 结构装配 | W-DZ-01 | 装配 | 资质、洁净度 | 8 h |
| 30 | PCBA 检验与软件装载 | W-DZ-01 | 装联 | CSCI 版本入档、静电防护 | 6 h |
| 40 | MGU 清洗涂脂 | W-JD-01 | 特殊过程 | SPQ 有效、脂批次在效期 | 5 h |
| 50 | 轴承压装 ★ | W-JD-01 | 关键工序 | 过盈量 100% 检、压装曲线采集 | 6 h |
| 60 | 力矩紧固 | W-JD-01 | 关键工序 | 14 点力矩全记录、扭矩扳手校准 | 3 h |
| 70 | 组件联调 | W-ZH-01 | 测试 | 测试程序版本、数据自动采集 | 12 h |
| 80 | 环境试验（振动/热循环） | T-02 | 试验 | 试验剖面、传感器校准 | 36 h |
| 90 | 三综合老炼 72 h | AR-01 | 试验 | 连续时长按班次切分 | 72 h |
| 100 | 多余物检查与复检 ★ | W-ZH-01 | 关键工序 | 双岗、内窥影像留存 | 4 h |
| 110 | 出厂评审与履历本归档 | — | 放行 | 归零闭环、FCA/PCA 通过 | 8 h |

★ = 关键工序，禁止跳序、禁止代签、参数 100% 留存。工序字段：`routing_op(op_id, rout_id, seq, name, work_center, op_type, qc_plan_id, std_hours, mandatory_flag, reject_next_seq)`。

### 资源与资质

| 资源 | 类型 | 关键约束 |
|------|------|---------|
| 装配工 OP-1102 | 人员 | 技能矩阵：机构装配 3 级、力矩作业授权、证书 2027-06-30 到期 |
| 装调工 OP-1140 | 人员 | 静电防护授权、PCBA 装联 2 级 |
| 检验员 QC-2001 | 人员 | 计量证书、多余物检查双岗资格 |
| 压装机 EQ-330 | 设备 | 校准有效期 2027-01-15，曲线采集 200 Hz |
| 扭矩扳手 EQ-341 | 工装 | 校准周期 30 d，累计使用次数寿命 5000 |
| 环试台 T-02 | 设备 | 排产需按试验剖面独占，一次 36 h |
| 老化房 AR-01 | 设备 | 72 h 连续，跨班次交接记录 |

人员资质过期、设备校准超期、工装寿命耗尽三类校验在派工与开工时点拦截。

### 库存与在途基线（2026-07-14 17:00 投料前快照）

| 物料 | 需求（19 套） | 可用库存 | 已分配 | 在途 | 缺口 |
|------|-------------|---------|-------|------|------|
| 轴承 2JD01000043 | 40 | 38 | 0 | 0 | **2** |
| FPGA 2DZ10000034 | 21 | 12 | 0 | 9（ETA 2026-09-20） | 0 |
| 谐波减速器 | 19 | 19 | 0 | 0 | 0 |
| 步进电机 | 19 | 11 | 0 | 8（ETA 2026-08-25） | **0 但晚于需求** |
| 钛紧固件 | 280 | 1200 | 0 | 0 | 0 |
| 润滑脂 LP-400 | 0.24 kg | 1.20 kg | 0 | 0 | 0（效期 2027-03-31） |
| 灌封胶 | 3.20 kg | 2.80 kg | 0 | 0 | **0.40 kg** |
| 贴片电容 | 3800 | 52000 | 0 | 0 | 0 |

"已分配"指被其他工单占用量，本批开工前须先解占。缺口计算与齐套算法见 [tech-material-kitting](../tech/tech-material-kitting-20260816.md)。

## 六、核心表清单与外键链

```
item ─┬─ item_rev(rev, lifecycle, change_order_id)
      ├─ item_class(class_id, attr_set)
      └─ bom ── bom_line(child_item_rev, pos, routing_op_id)
                     ↑
      doc ── doc_rev ┘（图纸/规范/试验大纲 关联 item_rev）
      baseline ── baseline_item（技术状态基线：某型号某阶段某件状态的冻结清单）
      ecr ── ccb_decision ── ecn ── eco ──┬─ item_rev(新)
                                          ├─ bom_line(生效区间切换)
                                          └─ change_impact(影响对象+处置结论)

routing ── routing_op ── qc_plan ── qc_item

mission(ERP 任务书) ── production_order(ERP) ── work_order(MES)
   work_order ─┬─ work_order_operation ── dispatch_task ── work_report
               ├─ work_order_material ── 齐套结果 kit_result
               └─ sn_master ─┬─ sn_genealogy(父 SN / 子 SN / 子 LOT + pos)
                             ├─ sn_history(状态流转)
                             ├─ sn_doc(履历本、质量证明文件 CofC)
                             └─ csci_load(固件版本)

inv_lot ── inv_txn ── work_order_material（发料/倒冲/退料三类事务）

qc_task ── qc_record ── ncmr ─┬─ concession(DEV)
                              ├─ zeroing(ZL 双五条)
                              └─ rework_order / scrap_txn
```

`sn_genealogy` 是追溯的心脏，字段：`id, parent_sn, child_sn(nullable), child_lot(nullable), pos, wo_id, op_id, link_type(SN2SN/SN2LOT/LOT2LOT), qty, created_at`。SN 与 LOT 允许在同一父件下混挂，因此必须有 `link_type`。

## 七、状态机总表

| 对象 | 状态流 |
|------|-------|
| 物料 Rev | `WORKING → REVIEW → RELEASED → CHANGED(被新 Rev 取代) → OBSOLETE` |
| BOM | `DRAFT → IN_REVIEW → RELEASED → SUPERSEDED → ARCHIVED` |
| ECR | `NEW → ASSESSED → CCB_PENDING → APPROVED / REJECTED / DEFERRED → CONVERTED` |
| ECN | `DRAFT → SIGNED → USER_APPROVED → RELEASING → EFFECTIVE → CLOSED` |
| SN 履历 | `PLANNED → RELEASED(投料) → IN_ASSY → IN_TEST → PASSED → SHIPPED → INSTALLED → ON_ORBIT → FAILED → REFURBISH → RETURNED` |
| 件状态 | `D 鉴定件 → Y 验收件 → F 飞行件 / B 备份件`（转阶段需重新评审，SN 不变、`sn_class` 变） |
| 工单 | `CREATED → RELEASED → STARTED → SUSPENDED → STARTED → COMPLETED → CLOSED` |
| 批次库存 | `OK → ALLOCATED → ISSUED`，异常分支 `LOCKED(超差/待判) → OK(让步放行) / REJECTED(退货)` |
| NCR | `RAISED → ISOLATED → MRB_PENDING → DECIDED(CONCESSION/REWORK/USE_AS_IS/SCRAP/RETURN) → CLOSED → ZEROED` |
| 归零单 ZL | `STARTED → 技术归零五条 → 管理归零五条 → HORIZONTAL_SCAN → VERIFIED → CLOSED` |

状态机实现约束（禁止 `setStatus`、转移表守护）见 [tech-workorder-state](../tech/tech-workorder-state-20260816.md)。

## 八、跨系统消息

| 事件 | 发布方 | 订阅方 | 幂等键 |
|------|-------|-------|-------|
| `ITEM_RELEASED` | PLM | ERP, MES, WMS | `itemCode:rev` |
| `BOM_EFFECTIVE` | PLM | ERP, MES | `bomId:rev:effectiveFrom` |
| `ECN_EFFECTIVE` | PLM | MES, QMS, ERP | `ecnNo` |
| `WO_RELEASED` | MES | WMS, ERP | `woNo` |
| `MATERIAL_ISSUED` | WMS | MES, ERP | `txnNo` |
| `OP_COMPLETED` | MES | ERP(成本), QMS | `woNo:opSeq:reportNo` |
| `NCR_RAISED` | QMS | MES, PLM | `ncrNo` |
| `ZEROING_CLOSED` | QMS | PLM, MES | `zlNo` |
| `SN_SHIPPED` | MES | ERP | `sn` |

投递可靠性（本地消息表 + 重试 + 对账）见 [tech-mdm-sync](../tech/tech-mdm-sync-20260816.md)。

## 九、对账断言库（所有 demo 必须引用编号）

| 编号 | 断言 | 校验 SQL 思路 |
|------|------|-------------|
| A1 | 单件守恒：一个 SN 同一时刻只有一个 `sn_history` 有效节点 | `count(sn_history where sn=? and valid_to is null) = 1` |
| A2 | 数量守恒：Σ 装机子件数 = BOM 应装数，且 `pos` 无重复无缺失 | `bom_line.pos` 与 `sn_genealogy.pos` 全外连接，双向为 0 |
| A3 | 物料守恒：Σ 发料 = Σ 装机 + Σ 线边余 + Σ 退料 + Σ 报废 | `inv_txn(type=ISSUE)` 对 `work_order_material` |
| A4 | 批次守恒：Σ `inv_lot.qty` = `inv_balance.qty` | 分组求和相等 |
| A5 | 报工守恒：合格数 + 不合格数 = 本工序投产数 | `work_report` 聚合 |
| A6 | 追溯守恒：追溯图无孤儿节点、无环 | 递归 CTE，`depth < 20` |
| A7 | 技术状态守恒：ECN 生效后，同一 `pos` 仅一个有效 `child_item_rev` | 有效性区间不重叠 |
| A8 | 成本守恒：SN 归集成本 = Σ 材料 + Σ 外协 + Σ 工时 + Σ 制费分摊 | `cost_txn` 按 `sn` 汇总 |
| A9 | 跨系统守恒：PLM 与 MES 的 BOM 行数、Rev 一致 | 对账表 `sync_recon` 差异行为 0 |
| A10 | 权限守恒：关键工序记录的操作人当时具备有效资质 | 校验 `operator_skill` 与 `work_report.created_at` 交集 |

任一条不成立即视为流程 demo 写错，需回改。

## 十、全局时间线

7 篇 demo 是同一条时间轴上的 7 段切片，**不得互相矛盾**。所有 demo 引用下表日期与单号。

| 日期 | 事件 | 单号 | 所属 demo |
|------|------|------|----------|
| 2026-06-20 | 26B 批任务书下达，SADA 交付节点 2027-02-28 | MT-26B-2026-0044 | 前置 |
| 2026-07-08 | 鉴定件鉴定试验通过，转阶段评审、工艺定型、PRR | BL-26B-SADA-Z-D | P0-3 |
| 2026-07-15 | 19 套（F/B/Y）投料开工 | WO-26B-20260715-0101 | 前置 |
| 2026-07-22 | 发射窗口调整，交付节点提前 45 天至 2027-01-14 | MT-26B-2026-0044R1 | P0-1 |
| 2026-08-03 | FPGA 供应商正式断供，在途 9 件取消 | PO-26B-0091 变更 | P0-5 |
| 2026-08-11 | 贴片电容批次 DPA 判定内部电极缩孔 | DPA-26B-20260811-006 | P0-6 |
| 2026-08-13 | 该批电容超差让步接收（用户代表批准） | DEV-26B-20260813-002 | P0-6 |
| 2026-08-20 | 轴承 71904→71905 Ⅰ类技术状态更改请求受理 | ECR-26B-20260820-0007 | P0-2 |
| 2026-08-27 | F-005 三综合老炼输出力矩衰减失效 | ZL-26B-20260827-001 | P0-4 |
| 2026-09-10 | 轴承更改生效，RevB 切换、在制件拆换 | ECN-26B-20260908-0003 | P0-2 |
| 2026-10-31 | 19 套 SADA 全部交付总装 | SN 批量转 `SHIPPED` | 前置 |
| 2026-11-05 | 004 星总装电测异常，F-007 返厂排故加改装 | WO-26B-20261105-0233 | P0-7 |
| 2026-11-14 | F-007 复测通过，履历本与基线回写完成 | LOG-SADA-26B-Z-F-007 | P0-7 |

五条硬耦合，写 demo 时必须体现，否则 7 篇只是互不相干的孤岛：

1. P0-1 交付节点提前 45 天，使 P0-5 的 FPGA 断供从"可消化"升级为关键路径风险。
2. P0-3 的 PRR 检查单在 P0-1 变更后必须重评，其中"特殊过程鉴定有效性"一项重新打开。
3. P0-2 更改后的 RevB 件须补做低温试验，占用本已排满的环试台 T-02，与 P0-5 争抢台位。
4. P0-4 归零的举一反三排查范围，包含 P0-6 让步放行后仍在流转的 4 块 PCBA 所在整机。
5. P0-7 排故发现的线束接地问题，根因追溯到 P0-2 拆换轴承时的重新布线作业。

## 十一、7 篇 demo 索引

| # | 文档 | 剧情 | 核心断言 |
|---|------|------|---------|
| P0-1 | [demo-flow-mission-change](./demo-flow-mission-change-20261116.md) | 发射窗口提前 45 天，任务变更逐级传导 | A3 A4 A9 |
| P0-2 | [demo-flow-config-change](./demo-flow-config-change-20261116.md) | 轴承 71904→71905 Ⅰ类更改，在制 19 套如何切换与拆换 | A1 A2 A7 A9 |
| P0-3 | [demo-flow-qualification-gate](./demo-flow-qualification-gate-20261116.md) | 鉴定件通过→转验收件→转飞行件，工艺定型与 PRR | A1 A5 A10 |
| P0-4 | [demo-flow-quality-closure](./demo-flow-quality-closure-20261116.md) | F-005 老炼失效，双五条归零与同类件水平排查 | A1 A6 A8 |
| P0-5 | [demo-flow-part-shortage](./demo-flow-part-shortage-20261116.md) | FPGA 断供，国产化替代验证、停工损失与成本超支 | A3 A5 A8 |
| P0-6 | [demo-flow-concession](./demo-flow-concession-20261116.md) | 电容 DPA 缩孔超差，让步接收、装机锁定、反查已交付星 | A4 A7 A10 |
| P0-7 | [demo-flow-refix-rework](./demo-flow-refix-rework-20261116.md) | 飞行件返厂排故与加改装技术状态回写 | A1 A2 A6 |

阅读顺序建议：P0-3 → P0-1 → P0-2 → P0-6 → P0-4 → P0-5 → P0-7（先建立主干与转阶段，再叠加变更、质量与返工）。

## 十二、相关文档

- [demos 目录说明与模板](./README.md)
- [PLM 业务流程](../plm/plm-workflow-20260816.md)
- [MES 业务流程](../mes/mes-workflow-20260816.md)
- [BOM 业务流程](../bom/bom-workflow-20260816.md)
- [核心数据模型](../tech/tech-data-model-20260816.md)
- [集成架构](../integration/integration-architecture-20260816.md)
