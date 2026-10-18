# RCA Agent 文档中心

项目全部文档的统一索引与维护规范。

**快速跳转**：[目录结构](#1-目录结构) · [命名规范](#2-文件命名规范) · [文档索引](#3-文档索引) · [模块对照](#4-模块与文档对照) · [维护约定](#5-维护约定)

---

## 1. 目录结构

```
docs/
├── README.md        # 本文件：文档索引与规范说明
├── api/             # API 接口文档（1）
├── requirement/     # 需求文档（1）
├── spec/            # 方案设计 / PRD / 实施计划（4）
├── changelog/       # 变更记录（4）
└── report/          # 测试报告 / 调研报告（3）
```

> `requirement/` 为项目在标准规范（changelog/spec/report/design/api/database）基础上扩展的目录。

## 2. 文件命名规范

```
{模块}-{内容描述}-{日期}.md
```

- **模块**：小写英文，连字符分隔。示例：`rca-agent`、`rca-demo`、`rca-record-replay`
- **内容描述**：简洁中文描述
- **日期**：`YYYY-MM-DD`。新建文档取创建日；存量文档归档时取归档日，并在文首注明

**示例**：`rca-agent-录制数据H2持久化-2026-10-13.md`

## 3. 文档索引

> 最新动态见 [changelog/](changelog/)；最近一次更新：**2026-10-18**（项目规范化：补齐 ignore 体系 + SKILLS.md + skills/ 目录）。

### API 文档（docs/api/）

| 文档 | 日期 | 说明 |
|------|------|------|
| [rca-agent-接口文档](api/rca-agent-接口文档-2026-10-11.md) | 2026-10-11 | RCA Agent 后端全部 HTTP 接口：核心分析端点、录制/历史/回放接口、请求响应示例、配置说明 |

### 需求文档（docs/requirement/）

| 文档 | 日期 | 说明 |
|------|------|------|
| [rca-record-replay-需求文档](requirement/rca-record-replay-需求文档-2026-10-11.md) | 2026-10-11 | 「录制-回放」功能需求：功能范围、数据模型、接口清单、原理与使用示例 |

### 方案设计（docs/spec/）

| 文档 | 日期 | 说明 |
|------|------|------|
| [rca-record-replay-录制回放PRD](spec/rca-record-replay-录制回放PRD-2026-10-11.md) | 2026-10-11 | 录制回放功能 PRD：目标、FR/NFR、验收标准（AC-1~AC-10） |
| [rca-record-replay-实施计划](spec/rca-record-replay-实施计划-2026-10-11.md) | 2026-10-11 | 录制回放实施计划：Task 1~12 任务分解、依赖关系、测试要求 |
| [rca-demo-服务说明与故障注入手册](spec/rca-demo-服务说明与故障注入手册-2026-10-08.md) | 2026-10-08 | demo-order-service / demo-supplier-service 完整手册：架构、启动、接口清单、6 大故障场景演练、RCA 排查路径 |
| [rca-agent-web-UI设计规范](spec/rca-agent-web-UI设计规范-2026-10-13.md) | 2026-10-13 | 前端控制台设计规范：设计令牌、布局、5 页面规范、组件状态、无障碍与验收标准 |

### 变更记录（docs/changelog/）

| 文档 | 日期 | 说明 |
|------|------|------|
| [rca-demo-新增两个RCA演练服务](changelog/rca-demo-新增两个RCA演练服务-2026-10-08.md) | 2026-10-08 | 新增 demo-order-service / demo-supplier-service 并统一 demo- 命名 |
| [rca-agent-录制数据H2持久化](changelog/rca-agent-录制数据H2持久化-2026-10-13.md) | 2026-10-13 | 录制数据从内存迁移至 H2 持久化（JPA + JSON 序列化），解决重启丢失/容量上限/多实例不共享 |
| [rca-demo-H2持久化与WebUI规范测试](changelog/rca-demo-H2持久化与WebUI规范测试-2026-10-13.md) | 2026-10-13 | demo 两服务业务数据接入 H2；新增 Web UI 设计规范与 15 条 Playwright UI 测试 |
| [rca-agent-项目规范化](changelog/rca-agent-项目规范化-2026-10-18.md) | 2026-10-18 | 按 common-project-standard 补齐 .gitignore / .traeignore / 各 AI ignore / SKILLS.md / skills/ 目录 / .trae/config.yml，清理空 specs/ |

### 测试报告（docs/report/）

| 文档 | 日期 | 说明 |
|------|------|------|
| [rca-demo-冒烟测试报告](report/rca-demo-冒烟测试报告-2026-10-11.md) | 2026-10-11 | 两个 demo 服务的构建测试与全场景冒烟测试结果 |
| [rca-agent-优化空间调研报告](report/rca-agent-优化空间调研报告-2026-10-13.md) | 2026-10-13 | RCA Agent 源码走读：置信度模型、拓扑、反事实验证、并发模型等 10 项优化空间与优先级矩阵 |
| [rca-agent-web-UI测试报告](report/rca-agent-web-UI测试报告-2026-10-13.md) | 2026-10-13 | 15 条 Playwright UI 用例全过 + 真实后端浏览器实测记录 |

## 4. 模块与文档对照

| 模块 | 端口 | 相关文档 |
|------|------|---------|
| rca-agent-backend | 8082 | [接口文档](api/rca-agent-接口文档-2026-10-11.md) · [录制回放需求](requirement/rca-record-replay-需求文档-2026-10-11.md) · [PRD](spec/rca-record-replay-录制回放PRD-2026-10-11.md) · [实施计划](spec/rca-record-replay-实施计划-2026-10-11.md) |
| rca-agent-web | 5173（dev） | [UI 设计规范](spec/rca-agent-web-UI设计规范-2026-10-13.md) · [UI 测试报告](report/rca-agent-web-UI测试报告-2026-10-13.md) |
| demo-order-service | 8083 | [demo 手册](spec/rca-demo-服务说明与故障注入手册-2026-10-08.md) · [冒烟测试报告](report/rca-demo-冒烟测试报告-2026-10-11.md) · [模块 README](../demo-order-service/README.md) |
| demo-supplier-service | 8084 | [demo 手册](spec/rca-demo-服务说明与故障注入手册-2026-10-08.md) · [冒烟测试报告](report/rca-demo-冒烟测试报告-2026-10-11.md) · [模块 README](../demo-supplier-service/README.md) |

## 5. 维护约定

1. **新增文档**：按类型入目录 → 命名遵循第 2 节 → 同步第 3 节索引 → 更新「最近一次更新」日期。
2. **归档存量文档**：日期取归档日，文首「命名说明」注明原文件名。
3. **变更记录**：代码/配置变更完成后在 `changelog/` 新增记录，引用相关 spec/report 形成追溯链。
4. **交叉引用**：使用相对路径链接，保证目录迁移后链接有效。
