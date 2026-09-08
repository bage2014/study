# 开源项目与标准参考汇总

> 创建时间：2026-09-06
> 模块：resources

汇总 MES/PLM/BOM/WMS/APS/ERP 等领域的开源项目、工业标准与参考资料，附带原始链接。

## 一、MES 开源项目

| 项目 | 语言/栈 | 许可证 | Stars | 说明 | 链接 |
|------|--------|--------|-------|------|------|
| openMES | Java + MySQL | Apache-2.0 | - | 基于 ISA-88/ISA-95 标准设计，生产可视化、设备管理、智能排程 | [GitHub](https://gitcode.com/gh_mirrors/op/openMES) |
| OpenMes (Mes-Open) | PHP/Laravel + MQTT | - | 116 | 生产管理、QMS、设备监控，支持 MQTT 数据采集 | [GitHub](https://github.com/Mes-Open/OpenMes) |
| OpenMES (exentials) | .NET 10 + Blazor | MIT | - | 生产跟踪、停机分析、质检、库存，含 WebAdmin/WebClient 双端 | [GitHub](https://github.com/exentials/OpenMES) |
| iPlusMES | - | - | 24 | 完整 MES 解决方案 | [GitHub](https://github.com/iplus-framework/iPlusMES) |
| kuaigeyun-mes | FastAPI + React | - | 26 | 国内开源 MES，多租户，含生产计划/工单/质量/设备/ERP | [GitHub](https://github.com/kuaigeyun/kuaigeyun-mes) |
| WebErpMesv2 (SMEWebify) | PHP | - | 212 | 资源管理 + MES，面向钣金/机加工/模具行业 | [GitHub](https://github.com/SMEWebify/WebErpMesv2) |
| aerospace-mes | Django + PostgreSQL | - | - | 航空航天 MES，严格装配追溯、库存预测 | [GitHub](https://github.com/muisik/aerospace-manufacturing-execution-system) |

### 参考文章
- [Top Free & Open-Source MES Software for 2026](https://mdcplus.fi/blog/top-free-mes-systems-manufacturing-execution/) — 2025 年免费/开源 MES 综述，覆盖 qcadoo、Libre、Apache OFBiz、Odoo 等
- [openMES 开源制造执行系统实战指南](https://blog.csdn.net/gitblog_00624/article/details/155920992) — openMES 安装、配置、案例
- [ISA-95 standard 详解](https://instrumentationtools.com/isa-95-standard/) — ISA-95 五层模型、四大操作域、ERP↔MES 数据流

## 二、PLM 开源项目

| 项目 | 语言/栈 | 许可证 | Stars | 说明 | 链接 |
|------|--------|--------|-------|------|------|
| DocDokuPLM | Java | AGPL-3.0 | - | 高端开源 PLM，文档管理/产品结构/BOM/变更/3D 可视化 | [GitHub](https://github.com/docdoku/docdoku-plm) |
| OdooPLM | Python (Odoo) | - | 136 | Odoo 原生 PLM/PDM，CAD 集成(SolidWorks/AutoCAD/FreeCAD)、BOM 版本、3D 预览 | [GitHub](https://github.com/OmniaGit/odooplm) |
| nanoPLM | HTML/JS | - | 62 | 面向小型机械制造商的 PLM，原生支持 FreeCAD | [GitHub](https://github.com/alekssadowski95/nanoPLM) |
| beCPG Community | Java | - | 18 | 面向消费品/化妆品/食品行业的 PLM（配方管理） | [GitHub](https://github.com/becpg/becpg-community) |
| OpenEoX | - | - | 30 | 产品 EOL/EOS 信息标准化管理 | [GitHub](https://github.com/OpenEoX/openeox) |

### 参考文章
- [PLM Product Lifecycle Management System List](https://pieteams.github.io/pieplm/getting-started/plm-systems) — 国际/国内商业 + 开源 PLM 完整清单，含选型对比
- [OdooPLM Wiki](https://github.com/OmniaGit/odooplm/wiki) — OdooPLM 完整文档、Docker 部署、CAD 客户端

## 三、WMS 开源项目

| 项目 | 语言/栈 | 许可证 | Stars | 说明 | 链接 |
|------|--------|--------|-------|------|------|
| GreaterWMS | Python | - | 4.3k | 福特亚太售后物流系统演化，支持库存共享 | [GitHub](https://github.com/GreaterWMS/GreaterWMS) |
| ModernWMS | Vue3 + TS + .NET 7 | MIT | 1.5k | 轻量完整 WMS，收发存核心功能，开箱即用 | [GitHub](https://github.com/fjykTec/ModernWMS) |
| Open WES | Spring Cloud 微服务 | - | 275 | 仓库执行系统，AI 优化、微服务架构 | [GitHub](https://github.com/jingsewu/open-wes) |
| Sentry WMS | - | Apache-2.0 | - | 面向条码扫描器的开源 WMS，含移动端 | [GitHub](https://github.com/hightower-systems/sentry-wms) |
| SmoWMS | .NET (Smobiler) | - | - | 移动端仓库管理 APP，支持 Android/iOS | [GitHub](https://github.com/xiaodongni/SmoWMS) |
| GoodsMart WMS | Flask | AGPL-3.0 | - | 多租户 WMS 后端，RESTful API | [GitHub](https://github.com/loadstarCN/GoodsMart-WMS-Backend) |
| OpenShip | Next.js | - | 1.2k | 电商多渠道履约 | [GitHub](https://github.com/openshiporg/openship) |

### 参考文章
- [ModernWMS 官网](https://modernwms.ikeyly.com/index_en.html) — 开源免费 WMS，演示地址可直接体验
- [GitHub WMS 话题页](https://github.com/topics/warehouse-management-system) — 190 个 WMS 相关仓库

## 四、APS 开源项目

| 项目 | 语言/栈 | 许可证 | 说明 | 链接 |
|------|--------|--------|------|------|
| frePPLe | C++ + Django | AGPL-3.0 (社区版) | 开源高级计划排程，支持需求预测、产能建模、瓶颈排产、甘特图、Odoo 集成 | [GitHub](https://github.com/frePPLe/frepple) |
| frePPLe 官方 | - | - | 官网、文档、在线 demo、视频教程 | [官网](https://frepple.com/) |

### frePPLe 核心特性
- **需求预测**：指数平滑、Holt-Winters、Croston 间歇需求等，自动选最优算法
- **产能建模**：瓶颈约束、资源负载可视化
- **生产排程**：交互式甘特图，支持 MTO/MTS/ATO
- **工艺路线与 BOM**：完整工艺建模
- **Odoo 集成**：原生对接 Odoo ERP

### 参考文章
- [开源制造排程软件 FrePPLe APS 系统](https://www.cnblogs.com/05-hust/p/19633003) — frePPLe 功能、Demo、社区入口

## 五、BOM 管理与展开工具

| 项目 | 语言 | 说明 | 链接 |
|------|------|------|------|
| pyBOM | Python | 基于 Excel 的 BOM 扁平化，合并同料、计算总用量、生成 DOT 树形图 | [GitHub](https://github.com/robsiegwart/python-BOM) |
| KiCost | Python | KiCad 项目 BOM 成本计算 | [GitHub](https://github.com/hildogjr/KiCost) |
| CycloneDX | XSLT | OWASP 软件物料清单(SBOM)标准，含 SBOM/SaaSBOM/MBOM 等 | [GitHub](https://github.com/CycloneDX/specification) |
| cdxgen | JS | 生成 CycloneDX 格式 BOM，支持多语言 | [GitHub](https://github.com/cdxgen/cdxgen) |
| k8s bom | Go | Kubernetes SBOM 多工具 | [GitHub](https://github.com/kubernetes-sigs/bom) |

> 注：CycloneDX/cdxgen 为软件 BOM(SBOM)，与制造 BOM(MBOM) 概念不同，但展开逻辑可借鉴。

## 六、开源 ERP（含 MES 模块）

| 项目 | 语言/栈 | 许可证 | 说明 | 链接 |
|------|--------|--------|------|------|
| Odoo Community | Python | LGPLv3 | 模块化 ERP，含制造、库存模块 | [官网](https://www.odoo.com/) |
| Apache OFBiz | Java | Apache-2.0 | 完整 ERP/MOM 框架，含生产执行 | [官网](https://ofbiz.apache.org/) |
| qcadoo MES | Java | AGPL | 浏览器端 MES 社区版 | [官网](https://qcadoo.com/) |
| WebErpMesv2 | PHP | - | ERP + MES 一体化 | [GitHub](https://github.com/SMEWebify/WebErpMesv2) |

## 七、工业标准与规范

### ISA-95（IEC 62264）— 企业-控制系统集成

全球 MES 架构基础标准，定义制造业 IT 五层金字塔：

| 层级 | 名称 | 时间尺度 | 典型系统 |
|------|------|---------|---------|
| Level 4 | 业务计划与物流 | 月→天 | ERP、SCM、PLM |
| Level 3 | 制造运营管理(MES/MOM) | 天→分钟 | MES、LIMS |
| Level 2 | 监控与过程控制 | 秒→分钟 | SCADA、DCS、HMI |
| Level 1 | 传感与执行 | 毫秒→秒 | PLC、RTU、传感器 |
| Level 0 | 物理过程 | 实时 | 设备、工装 |

**Level 3 四大操作域**：
1. 生产运营管理（Production）
2. 质量运营管理（Quality）
3. 维护运营管理（Maintenance）
4. 库存运营管理（Inventory）

**核心数据对象**：人员、设备、物料、工艺段。

**实现技术**：
- B2MML（Business To Manufacturing Markup Language）— ERP↔MES 的 XML 数据模型
- OPC UA — OT/IT 实时通信

参考链接：
- [ISA-95 Standard 详解](https://instrumentationtools.com/isa-95-standard/)
- [ISA-95: The Standard for MES Architectures](https://www.symestic.com/en-us/blog/mes/isa95)
- [ISA-95 完整指南](https://v5ultimate.com/glossary/isa-95) — 五层模型、八部分内容、B2MML、与 ISA-88 对比
- [ISA-95 MES 模型 FAQ](https://connect981.com/faqs/what-is-the-isa-95-mes-model) — 适用与不适用场景

### ISA-88 — 批处理控制

与 ISA-95 同族，侧重批处理过程控制（设备/阶段/操作/动作模型）。

### ISO 22400 — MES KPI 标准

定义制造运营管理关键绩效指标，用于跨系统对标。

## 八、行业标准与认证

| 标准 | 行业 | 说明 |
|------|------|------|
| ISO 9001 | 通用 | 质量管理体系 |
| IATF 16949 | 汽车 | 汽车行业质量管理 |
| ISO 13485 | 医疗 | 医疗器械质量管理 |
| AS 9100 | 航空 | 航空航天质量管理 |
| ISO 22000 | 食品 | 食品安全管理 |
| HACCP | 食品 | 危害分析与关键控制点 |
| ISA-95 | 全行业 | 企业-控制集成标准 |
| ISA-88 | 流程 | 批处理控制 |
| SECS/GEM | 半导体 | 半导体设备通信 |
| OPC UA | 全行业 | 工业互操作协议 |

## 九、资料检索入口

| 资源 | 链接 |
|------|------|
| GitHub MES 话题 | https://github.com/topics/mes |
| GitHub MES (manufacturing-execution-system) | https://github.com/topics/manufacturing-execution-system |
| GitHub PLM 话题 | https://github.com/topics/plm |
| GitHub WMS 话题 | https://github.com/topics/warehouse-management-system |
| GitHub BOM 话题 | https://github.com/topics/bill-of-materials |
| PLM 系统选型清单 | https://pieteams.github.io/pieplm/getting-started/plm-systems |
| 开源 MES 综述(2026) | https://mdcplus.fi/blog/top-free-mes-systems-manufacturing-execution/ |

## 十、选型建议

| 需求 | 推荐 |
|------|------|
| 快速验证 MES 概念 | openMES（ISA-95 标准）或 OpenMes（Laravel 轻量） |
| 完整 MES + ERP | WebErpMesv2 或 Odoo + MES 模块 |
| PLM + CAD 集成 | OdooPLM（Odoo 生态）或 DocDokuPLM（高端） |
| WMS 快速上线 | ModernWMS（开箱即用） |
| APS 排产 | frePPLe（成熟开源 APS） |
| 标准化集成架构 | 参照 ISA-95 五层模型设计 |
| 自研参考 | 参考本仓库 `docs/tech/` 技术实现文档 |

> **注意**：开源项目质量参差，选型前务必评估：社区活跃度、许可证合规性、文档完整度、是否符合行业标准。生产环境建议先做 POC 验证。

## 十一、相关文档

- [主流产品方案对比](../products/product-comparison-20260906.md)
- [集成架构](../integration/integration-architecture-20260816.md)
- [MES 概述](../mes/mes-overview-20260816.md)
- [PLM 概述](../plm/plm-overview-20260816.md)
