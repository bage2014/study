# study-mes 知识库

本仓库沉淀 MES（制造执行系统）、BOM（物料清单）、PLM（产品生命周期管理）及通用管理系统相关的知识点、模块说明、业务流程与集成方案。

## 目录结构

```
docs/
├── README.md                          # 知识库导航（本文件）
├── mes/                               # MES 制造执行系统
│   ├── mes-overview-20260816.md       # 概述：定义、定位、价值
│   ├── mes-modules-20260816.md        # 核心模块：生产、质量、设备、库存等
│   └── mes-workflow-20260816.md       # 业务流程：工单、报工、质检、追溯
├── bom/                               # BOM 物料清单
│   ├── bom-overview-20260816.md       # 概述：定义、作用
│   ├── bom-types-20260816.md          # 类型：EBOM/MBOM/SBOM 等
│   └── bom-workflow-20260816.md       # 流程：构建、变更、展开
├── plm/                               # PLM 产品生命周期管理
│   ├── plm-overview-20260816.md       # 概述：定义、定位
│   ├── plm-modules-20260816.md        # 模块：物料、文档、变更、项目管理
│   └── plm-workflow-20260816.md       # 流程：研发、变更、发布
├── management/                        # 通用管理系统
│   └── management-overview-20260816.md # 概述：权限、组织、审批等通用能力
├── integration/                       # 系统集成
│   └── integration-architecture-20260816.md # MES/BOM/PLM/ERP 集成架构
└── tech/                              # 技术实现文档
    ├── tech-framework-20260816.md             # 框架选型与分层架构
    ├── tech-data-model-20260816.md            # 核心数据模型设计
    ├── tech-workorder-state-20260816.md       # 工单状态机实现
    ├── tech-bom-explode-20260816.md           # BOM 展开算法实现
    ├── tech-traceability-20260816.md          # 追溯查询实现
    ├── tech-permission-20260816.md            # 权限与数据权限实现
    ├── tech-workflow-20260816.md              # 审批工作流实现
    ├── tech-mdm-sync-20260816.md              # 主数据事件同步实现
    └── tech-concurrency-20260816.md           # 报工高并发与编码生成
```

## 文件命名规范

`{module}-{content}-{date}.md`，例如 `mes-overview-20260816.md`。

## 阅读建议

1. 新人入门：先读各模块的 `overview`，再读 `modules`，最后读 `workflow`。
2. 架构选型：重点看 `integration/integration-architecture` + `tech/tech-framework`。
3. 业务对接：参考各模块的 `workflow` 文档。
4. 技术实现：参考 `tech/` 目录下各实现思路文档。

## 维护说明

- 知识更新时新增文件，不覆盖历史文件，便于追溯演进。
- 重大概念变更在文件头部加 `> 更新说明` 区块。
