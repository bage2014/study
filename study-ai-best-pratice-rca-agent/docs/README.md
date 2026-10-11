# RCA Agent 文档中心

本目录统一管理项目全部文档。文档按类型分目录存放，文件名遵循统一规范，本文档为全部文档的索引入口。

---

## 1. 目录结构

```
docs/
├── README.md        # 本文件：文档索引与规范说明
├── api/             # API 接口文档
├── requirement/     # 需求文档（项目扩展目录）
├── spec/            # 方案设计 / PRD / 实施计划
├── changelog/       # 变更记录
└── report/          # 测试报告
```

> `requirement/` 为项目在标准规范（changelog/spec/report/design/api/database）基础上扩展的目录，用于承接功能需求文档。

## 2. 文件命名规范

```
{模块}-{内容描述}-{日期}.md
```

- **模块**：小写英文，连字符分隔（如 `rca-agent`、`rca-demo`、`rca-record-replay`）
- **内容描述**：简洁中文描述
- **日期**：`YYYY-MM-DD`（新建文档取创建日；存量文档归档时取归档日，并在文首注明）

## 3. 文档索引

### API 文档（docs/api/）

| 文档 | 说明 |
|------|------|
| [rca-agent-接口文档-2026-10-11](api/rca-agent-接口文档-2026-10-11.md) | RCA Agent 后端全部 HTTP 接口：核心分析端点、录制/历史/回放接口、请求响应示例、配置说明 |

### 需求文档（docs/requirement/）

| 文档 | 说明 |
|------|------|
| [rca-record-replay-需求文档-2026-10-11](requirement/rca-record-replay-需求文档-2026-10-11.md) | 「录制-回放」功能需求：功能范围、数据模型、接口清单、原理与使用示例 |

### 方案设计（docs/spec/）

| 文档 | 说明 |
|------|------|
| [rca-record-replay-录制回放PRD-2026-10-11](spec/rca-record-replay-录制回放PRD-2026-10-11.md) | 录制回放功能 PRD：目标、FR/NFR、验收标准（AC-1~AC-10） |
| [rca-record-replay-实施计划-2026-10-11](spec/rca-record-replay-实施计划-2026-10-11.md) | 录制回放实施计划：Task 1~12 任务分解、依赖关系、测试要求 |
| [rca-demo-服务说明与故障注入手册-2026-10-08](spec/rca-demo-服务说明与故障注入手册-2026-10-08.md) | 两个 RCA 演练服务（demo-order-service / demo-supplier-service）完整手册：架构、启动、接口清单、6 大故障场景演练、RCA 排查路径 |

### 变更记录（docs/changelog/）

| 文档 | 说明 |
|------|------|
| [rca-demo-新增两个RCA演练服务-2026-10-08](changelog/rca-demo-新增两个RCA演练服务-2026-10-08.md) | 新增 demo-order-service / demo-supplier-service 并统一 demo- 命名的变更记录 |

### 测试报告（docs/report/）

| 文档 | 说明 |
|------|------|
| [rca-demo-冒烟测试报告-2026-10-11](report/rca-demo-冒烟测试报告-2026-10-11.md) | 两个 demo 服务的构建测试与全场景冒烟测试结果归档 |

## 4. 模块与文档对照

| 模块 | 端口 | 相关文档 |
|------|------|---------|
| rca-agent-backend | 8082 | 接口文档、录制回放需求/PRD/实施计划 |
| rca-agent-web | 5173（dev） | 录制回放需求文档（前端部分） |
| demo-order-service | 8083 | demo 手册、冒烟测试报告、[模块 README](../demo-order-service/README.md) |
| demo-supplier-service | 8084 | demo 手册、冒烟测试报告、[模块 README](../demo-supplier-service/README.md) |

## 5. 维护约定

1. **新增文档**：按类型放入对应目录，命名遵循第 2 节规范，并同步更新本文档第 3 节索引。
2. **归档存量文档**：补充日期后缀时取归档日，并在文首「命名说明」注明原文件名。
3. **变更记录**：代码/配置变更完成后在 `changelog/` 新增记录，引用相关 spec/report 文档形成追溯链。
4. **交叉引用**：相关文档间使用相对路径链接，保持目录迁移后链接有效。
