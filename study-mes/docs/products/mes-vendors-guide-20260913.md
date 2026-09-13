# 业界主流 MES 软件使用指南（特点 / 使用手册 / 优缺点）

> 创建时间：2026-09-13
> 模块：products
> 定位：在 [product-comparison-20260906.md](product-comparison-20260906.md) 简表基础上，逐个软件展开特点、模块构成、上手路径与优缺点，供选型与对标使用。

## 一、市场格局（2025-2026）

- 全球 MES 市场 2024 年约 215 亿美元，CAGR 约 10.8%；中国市场 CAGR 约 23%，新能源/电子/医药增速超 45%。
- Gartner 已用《Market Guide for MES》取代原魔力象限（2023 为最后一版 MQ）。Siemens 连续六次 MQ Leader；Rockwell（Plex + FactoryTalk）为 2025 Market Guide 代表厂商。
- 四股势力：
  1. **MOM 套件派**：Siemens Opcenter、Dassault DELMIA——APS/MES/QMS/LIMS 一体。
  2. **ERP 延伸派**：SAP DMC、Oracle、Infor——与 ERP 数据同源。
  3. **自动化绑定派**：Rockwell（Plex/FactoryTalk）、GE Proficy、AVEVA——PLC/SCADA 直连。
  4. **国产/SaaS 派**：宝信、中控、鼎捷、用友、黑湖——本地化、信创、轻量上线。

## 二、评估维度

| 维度 | 权重 | 关注点 |
|------|------|--------|
| 功能匹配 | 25% | ISA-95 核心功能覆盖率（工单、追溯、质量、OEE、人员） |
| 行业深度 | 20% | 离散/流程/混合；汽车、电子、医药等模板成熟度 |
| 集成能力 | 15% | ERP/PLM/SCADA/设备协议（OPC-UA、SECS/GEM） |
| 成本 | 15% | License + 实施 + 运维，订阅 vs 买断 |
| 实施复杂度 | 10% | 周期、二开门槛、低代码能力 |
| 部署与生态 | 10% | 云/本地/混合、国产化适配、服务网络 |
| 扩展性 | 5% | 多工厂复制、定制不破坏升级 |

## 三、方案 1：Siemens Opcenter（国际标杆）

| 项 | 内容 |
|----|------|
| 厂商 | Siemens Digital Industries Software（德国） |
| 前身 | Camstar（电子/半导体）+ SIMATIC IT，合并为 Opcenter |
| 部署 | 本地 / 私有云 / Opcenter X SaaS |
| 优势行业 | 汽车、电子半导体、医药食品、航空 |
| 典型成本 | License+实施百万级起步，周期 6-18 个月 |
| ISA-95 | Level 3 完整 MOM |

### 特点

- **MOM 全家桶**：APS（Preactor）+ MES + QMS + LIMS + 制造智能合一，ABI Research 综合排名第一。
- **数字主线（Digital Thread）**：与 Teamcenter PLM、Tecnomatix 工艺仿真、西门子 PLC/SCADA 硬件一条链打通，工厂数字孪生可直接落地。
- **行业版 Execution**：Discrete / Electronics / Process / Pharma 专用版本，行业模板开箱即用。
- **Mendix 低代码扩展**：定制界面与微流程不硬编码，升级不被定制锁死。
- Opcenter X SaaS 版降低中型企业准入门槛。

### 使用手册（模块与上手路径）

核心模块：

| 模块 | 干什么 |
|------|--------|
| Opcenter Execution | 工单执行、派工、报工、电子作业指导书（ESI） |
| Quality | 检验、SPC、NCR 不合格品、CAPA |
| Manufacturing Intelligence | OEE、看板、KPI 分析 |
| Scheduler（Preactor） | APS 有限能力排程 |
| R&D/Lab（LIMS） | 批次配方、实验室（流程/医药版） |

典型实施路径：

1. 工厂建模：组织 → 车间 → 产线 → 工位（对应 ISA-95 层级）。
2. 主数据接入：PLM 下发 MBOM/BOP；ERP 下发工单与物料。
3. 设备建模：OPC-UA 接 PLC，定义采集点与事件。
4. 流程配置：用内置流程设计器配工单生命周期、质检路径、异常升级规则。
5. 试点线 → 多工厂模板复制。

上手资源：Siemens Learning Center（官方培训认证）、Support Center 文档、实施伙伴交付（几乎必需）。

### 优点

- 功能最全，离散+流程通吃；多工厂集团复制能力强。
- 与西门子自动化硬件/Tecnomatix/Teamcenter 无缝，数字孪生落地最顺。
- Mendix 定制保留升级能力，行业模板降低配置量。
- 电子/半导体（原 Camstar）与医药合规（支持 FDA 21 CFR Part 11）根基深。

### 缺点

- 贵：License 与服务费均为市场顶级定价，TCO 高。
- 重：实施依赖西门子伙伴，内部需强治理与专职管理员。
- 最大价值前提是"全栈西门子"；若 PLM/设备用别家，集成优势打折。
- 界面对新手不友好，学习曲线陡。

### 适用 / 不适用

- 适用：大型集团、汽车/电子/医药、已用西门子 PLM 或自动化、追求数字主线整合。
- 不适用：预算有限的中小企业；单一工厂、流程简单、只需报工追溯的场景。

## 四、方案 2：SAP Digital Manufacturing（DMC，ERP 原生派）

| 项 | 内容 |
|----|------|
| 厂商 | SAP（德国） |
| 产品 | SAP DMC（云版，替代本地 SAP ME + MII） |
| 部署 | 公有云 / 私有云（BTP 平台） |
| 优势行业 | 汽车、机械、消费品、离散+混合 |
| 典型成本 | 订阅制，实施 6-12 个月，百万级 |

### 特点

- **与 S/4HANA 同源**：工单、BOM、工艺路线、物料直接来自 ERP，无接口对账问题——这是核心卖点。
- 云原生微服务架构，嵌入 SAP BTP；边缘端用 DMC Edge（Plant Connectivity）做设备接入与断网续传。
- 自带低代码（可见性策略、Production Process Designer 流程编排）。
- 支持行业云能力：可持续（碳足迹）、预测性资产维护与 AI 视觉质检扩展。

### 使用手册（模块与上手路径）

核心模块：

| 模块 | 干什么 |
|------|--------|
| Execute Production | 工单执行、报工、电子指导书 |
| Track Production | 批次/序列号追溯、在制 WIP |
| Analyze Production | OEE、工厂绩效看板 |
| Resource Orchestration | 设备/人员/工装资源管理 |
| Integration（PP/DS、QM） | 与 S/4 生产、质量模块直连 |

实施路径：

1. S/4HANA 侧打通主数据（物料/BOM/工作中心/工艺路线）。
2. BTP 订阅 DMC，配置工厂与组织模型。
3. PCo（Plant Connectivity）连设备，OPC-UA/MQTT 采数。
4. 用 Process Designer 编排业务流程与自定义表单。
5. 按工厂 rollout，标准模板化推广。

上手资源：SAP Learning Hub（DMC 认证）、SAP Help Portal、Fiori 风格界面（比 ME 老客户端友好）。

### 优点

- ERP-MES 数据一致性最好，主数据零冗余，财务-生产实时闭环。
- 云原生、迭代快；Fiori 用户体验优于传统 MES 客户端。
- 全球化、多语言、多合规体系开箱支持。
- 已有 SAP 的企业增量部署，集成成本最低。

### 缺点

- 离开 SAP ERP 生态，性价比骤降。
- 设备层/自动化深度弱于 Siemens、Rockwell，复杂工艺建模需二开。
- 云版本地化与信创适配弱（中国市场需评估数据合规）。
- 订阅长期成本高，深度定制受云版本发布节奏约束。

### 适用 / 不适用

- 适用：已用或确定上 S/4HANA 的中大型/跨国制造。
- 不适用：非 SAP ERP 用户；设备协议复杂、追求自动化硬集成的工厂。

## 五、方案 3：Rockwell Plex + FactoryTalk（自动化派）

| 项 | 内容 |
|----|------|
| 厂商 | Rockwell Automation（美国），Plex 为收购整合 |
| 产品 | Plex Smart Manufacturing（云套件）+ FactoryTalk ProductionCentre（本地） |
| 部署 | Plex 多租户 SaaS；FactoryTalk 本地 |
| 优势行业 | 汽车供应链、工业制造、食品饮料、医药冷链 |
| Gartner Peer Insights | 4.8/5 |

### 特点

- **Plex 定位"数字系统记录"而非单点 MES**：MES+QMS+轻 ERP+供应链计划+APM+分析共用一套多租户数据模型，多工厂离散制造可一次选型替掉 3-4 个系统。
- FactoryTalk 与 Allen-Bradley PLC 生态深度绑定，自动化层直连能力顶级。
- 2025 增强：Connected Worker（数字作业指导、技能矩阵）、罐区/BOM/配方管理、物料齐套、冷链 WMS。

### 使用手册（模块与上手路径）

Plex 标准模块：Production Monitoring、MES、Quality（QMS）、Maintenance（APM）、ERP、Supply Chain Planning、Analytics。

典型路径：

1. 设备接入：FactoryTalk Edge / 罗克韦尔网关采 PLC 数据。
2. Plex 配置工厂模型、BOM/配方、工作中心。
3. 质量计划（检验项/SPC 规则）与资产维护计划配置。
4. 车间终端（Connected Worker）上线：派工、指导书、点检。
5. 多工厂按统一模板开通（多租户 SaaS 上线快，典型 1-3 月可见雏形）。

上手资源：Plex 官方 Academy、Rockwell 全球伙伴网络、G2/Gartner Peer Insights 大量用户评价。

### 优点

- 云套件一体化，多工厂离散制造 TCO 与实施周期优于拼装方案。
- 与 AB PLC/驱动/安全控制系统原生集成，自动化数据最可靠。
- 质量记录与生产记录同库，追溯与召回处理强。
- SaaS 运维负担低，升级由厂商负责。

### 缺点

- 套件化意味着"按它的模型走"，深度个性化不如可二开的本地方案。
- 工厂若以西门子/三菱/国产 PLC 为主，自动化绑定优势减弱。
- Plex 轻 ERP 难替代重型 ERP，大型集团仍需与 SAP/Oracle 并存。
- 中国本地化与信创支持弱于国产厂商。

### 适用 / 不适用

- 适用：多工厂离散制造、汽车零部件、罗克韦尔自动化存量客户、想快速上云套件的中型企业。
- 不适用：流程行业重场景（选 AVEVA/Honeywell）；强信创要求的国内企业。

## 六、方案 4：Dassault DELMIA Apriso（全球运营派）

| 项 | 内容 |
|----|------|
| 厂商 | Dassault Systèmes（法国） |
| 平台 | 3DEXPERIENCE 平台上的 DELMIA |
| 部署 | 本地 / 云 |
| 优势行业 | 汽车、航空航天、消费品、全球多工厂集团 |
| 典型成本 | 百万级，周期 6-18 个月 |

### 特点

- **强在"全球制造网络统一运营"**：一套流程模板跨国家/工厂部署，本地化差异用配置处理。
- 低代码流程建模（Process Builder）是核心能力，业务流程改动不需重开发。
- 与 CATIA/SOLIDWORKS/ENOVIA、DELMIA 工艺仿真打通，3D 作业指导与虚拟工厂是差异化亮点。
- 范围覆盖生产、仓储、质量、人工、库存维护，偏"制造运营大平台"。

### 使用手册（模块与上手路径）

1. 3DEXPERIENCE 平台建企业/工厂/产线层级。
2. Apriso Process Builder 建模：工单、质量、物流、人员流程统一编排。
3. ENOVIA 接收 EBOM，在 DELMIA 转 MBOM/工艺 BOP 后下发执行。
4. 设备集成通过标准连接器与 IIoT 服务。
5. 总部定标准模板 → 各工厂差异化配置 → 全球 rollout。

上手资源：3DEXPERIENCE Edu、达索认证伙伴实施（必需）。

### 优点

- 多工厂全球化部署与流程治理能力最强之一。
- 低代码流程灵活，工艺变更响应快。
- 与达索设计/仿真体系一体，3D 可视化作业指导成熟。
- 航空/汽车行业方案与合规经验深。

### 缺点

- 贵且重，实施高度依赖伙伴。
- 平台化带来复杂度，单工厂用不满、投资回报差。
- 车间底层自动化连接不如 Rockwell/Siemens 开箱即用。
- 国内本地化服务与信创生态弱。

### 适用 / 不适用

- 适用：航空航天、汽车整车/一级供应商、全球多工厂、已用 CATIA/ENOVIA。
- 不适用：中小企业；单工厂、以设备自动化为核心诉求的场景。

## 七、方案 5：黑湖智造（国产 SaaS 代表）

| 项 | 内容 |
|----|------|
| 厂商 | 黑湖科技（中国上海） |
| 产品 | 黑湖小工单（轻量）、黑湖智造（旗舰 SaaS MES）、黑湖 MES 私有化 |
| 部署 | 公有云 SaaS 为主，支持私有化 |
| 优势行业 | 食品饮料、日化、注塑、五金、电子组装等中小制造 |
| 典型成本 | 年费制（数万-数十万/年），1-3 个月上线 |

### 使用手册（模块与上手路径）

核心模块：生产工单、报工报数、质检、设备点检/OEE、物料/库存、来料与成品追溯、移动看板、供应商协同。

落地路径（SaaS 标准化打法）：

1. 注册租户，组织/人员/权限初始化。
2. Excel 导入物料、BOM、工艺路线、设备台账。
3. 配置工单流程与质检表单（拖拽式表单/流程引擎）。
4. 车间用手机/平板/工位终端扫码报工，无需重型硬件。
5. 老板看板与报表开箱即用；设备数采按需加网关。

上手资源：官方帮助中心、视频教程、客户成功经理、标准培训（约数天）。

### 优点

- 上线极快、年费门槛低，无需专职 IT 运维。
- 移动端体验国内领先，工人扫码即用，培训成本低。
- 表单/流程可配置，适配中小厂多变需求。
- 迭代快，供应链协同（小工单/供应商门户）生态完整。

### 缺点

- 深度有限：复杂排程、多约束工艺、行业合规（GMP/SECS-GEM）支持弱。
- 多工厂复杂集团管控、跨系统深度集成能力不及大厂。
- SaaS 模式数据在云端，涉密/强信创场景需私有化版本。
- 深度定制受标准产品边界限制。

### 适用 / 不适用

- 适用：百人级中小离散制造、先数字化再深化、预算少要求快。
- 不适用：制药/半导体等强合规、钢铁化工等流程重场景、万人集团。

## 八、方案 6：宝信 / 鼎捷（国产行业派）

| 项 | 宝信软件 | 鼎捷软件 |
|----|----------|----------|
| 背景 | 宝武钢铁旗下 | 台资背景，深耕制造 40 余年 |
| 产品 | 宝信 MES（xIn3Plat/iPlat） | 鼎捷 MES（配 ERP E10/雅典娜） |
| 强项 | 钢铁、有色、化工流程行业市占第一 | 机械、电子、汽配离散行业模板 |
| 部署 | 本地/私有云，信创全栈适配 | 本地/云，轻量化 |
| 成本 | 数十万-数百万，3-12 月 | 数十万级，3-9 月 |

### 特点

- **宝信**：从钢铁 MES 起家，四级计算机体系（L4 经营-L3 制造-L2 过程控制-L1 基础自动化）经验深厚；与 DCS/过程控制集成强；信创适配（国产芯片/OS/数据库）完整。
- **鼎捷**：ERP+MES 一体化，行业模板（机加工、注塑、冲压、电子组装）成熟，排产/AWP 智能车间方案落地快；PLM 线 2026 报价集团版 30-100 万、中小企业 INSIGHT 版约 28 万。

### 使用手册要点

1. 行业模板复制：选定行业包后做差异配置（约占 70% 开箱）。
2. 与国产 ERP（用友/金蝶/鼎捷）或 DCS 做接口，接口规范国内项目化交付。
3. 设备数采用国产网关 + OPC-UA/MQTT/Modbus。
4. 厂商驻场实施 + 厂内关键用户培养。

### 优点

- 行业 Know-how 深、中文与国标合规天然贴合。
- 信创/国产化适配强，数据自主可控。
- 成本与实施周期低于国际大厂；本地服务响应快。
- 鼎捷类产品对中小厂性价比高。

### 缺点

- 跨行业可复制性弱（宝信离开冶金优势大减）。
- 产品化程度参差，部分能力靠项目开发，升级风险高。
- 多工厂全球化、多语言多合规能力不足。
- 高端数字孪生/AI 能力与国际头部仍有差距。

### 适用 / 不适用

- 宝信适用：冶金、石化等流程行业、国资/信创要求集团。
- 鼎捷适用：机械、汽配、电子离散中型厂，尤其已用鼎捷 ERP。
- 不适用：全球化布局企业；高度定制且要求产品化升级的场景。

## 九、横向对比与评分

| 维度（权重） | Opcenter | SAP DMC | Rockwell Plex | DELMIA Apriso | 黑湖智造 | 宝信/鼎捷 |
|--------------|----------|---------|---------------|---------------|----------|-----------|
| 功能匹配（25%） | 5 | 4 | 4 | 4 | 3 | 3.5（行业内 4.5） |
| 行业深度（20%） | 5 | 4 | 4 | 5 | 3 | 4.5（本行业） |
| 集成能力（15%） | 5 | 5（SAP 内 5） | 4.5 | 4 | 3 | 3.5 |
| 成本（15%，越省越高） | 2 | 2.5 | 3.5 | 2 | 4.5 | 4 |
| 实施复杂度（10%，越易越高） | 2 | 3 | 3.5 | 2.5 | 5 | 4 |
| 部署/生态（10%） | 4 | 4 | 4 | 4 | 3.5（信创需私有版） | 4.5 |
| 扩展性（5%） | 5 | 4 | 4 | 4.5 | 3 | 3.5 |
| **加权总分（5 分制）** | **4.2** | **3.8** | **3.9** | **3.9** | **3.6** | **3.9** |

> 评分是通用视角；落到具体行业（如制药看 Werum PAS-X、半导体看 Critical Manufacturing/Aegis）排序会变。

## 十、选型决策

```
你的情况
├── 已用 S/4HANA，要 ERP-MES 同源
│   └── SAP DMC
├── 全栈西门子 / 要数字孪生与 MOM 整合，预算充足
│   └── Siemens Opcenter
├── 罗克韦尔 PLC 多厂、想云套件替换多系统
│   └── Rockwell Plex
├── 航空汽车、全球多工厂、CATIA 用户
│   └── DELMIA Apriso
├── 中小厂、预算少、1-3 月上线、移动报工
│   └── 黑湖智造（或摩尔 N2 低代码）
├── 钢铁化工流程 + 信创
│   └── 宝信 / 中控
└── 机械汽配离散中型、鼎捷 ERP 用户
    └── 鼎捷 MES
```

特殊合规：制药 GMP/FDA 21 CFR Part 11 → Werum PAS-X、Honeywell POMS；电子 PCB → Aegis、Critical Manufacturing（SECS/GEM）。

## 十一、通用实施手册（买谁都适用）

| 阶段 | 动作 | 产出 |
|------|------|------|
| 1. 蓝图 | 业务梳理、痛点量化、ISA-95 差距分析 | 蓝图文档、ROI 测算 |
| 2. 主数据治理 | 物料/BOM/工艺/设备/人员编码先行 | 主数据标准 |
| 3. 试点 | 选一条代表产线，跑通工单-报工-质检-追溯闭环 | 试点验收 |
| 4. 集成 | ERP/PLM/SCADA 接口规范、事件总线 | 接口文档、联调报告 |
| 5. 推广 | 模板化复制到其他产线/工厂 | 推广模板 |
| 6. 运营 | KPI 复盘、用户培训、持续改善 | OEE/质量基线 |

铁律：**先治理主数据，再上 MES；先试点，再推广；定制用配置/低代码，不动核心代码。**

## 十二、参考资料

- [Top 9 MES Manufacturing Software & Systems 2026 - Averroes](https://averroes.ai/blog/mes-manufacturing-software)
- [12 Best MES Software for Discrete Manufacturing 2026 - FactoryDesk](https://factorydeskai.com/mes-software-for-discrete-manufacturing/)
- [MES 厂商与产品对比（2025-2026 市场趋势）](https://jishuzhan.net/article/2087869257953460226)
- [Rockwell Named Representative Vendor in 2025 Gartner Market Guide for MES](https://www.rockwellautomation.com/content/plex/global/en/company/newsroom/rockwell-automation-named-representative-vendor-2025-gartner-market-guide.html)
- [Siemens Gartner MES Leader 公告（Opcenter Execution）](https://news.siemens.com/ko-kr/gartner-magic-quadrant-mes-23/)
- 关联文档：[MES 概述](../mes/mes-overview-20260816.md)、[MES 模块](../mes/mes-modules-20260816.md)、[主流产品方案对比](product-comparison-20260906.md)
