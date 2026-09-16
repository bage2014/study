# 软考高级 - 系统架构设计师 备考仓库

> 目标：2026 年 11 月考试通过
> 起点：2026-08-17，剩约 9 周
> 基础：Java 老手，已有软件设计师中级

## ★ 复习顺序（如何阅读本仓库）

> 新会话 / 不知道该干嘛时，按这张表走。与 [3 个月学习计划](docs/spec/study-plan-20260817.md) 的 4 阶段一一对应。

| 步骤 | 做什么 | 用哪些文件（按序） | 对应阶段 |
|------|--------|--------------------|----------|
| ① 读规划 | 搞清考什么、怎么考、时间怎么分 | [考试说明](docs/spec/exam-overview-20260817.md) → [复习思路](docs/spec/review-strategy-20260817.md) → [学习计划](docs/spec/study-plan-20260817.md) → [资料清单](docs/spec/resources-20260817.md) | 开局一次 |
| ② 建框架 | 通读章节笔记，先混脸熟不抠细节 | notes/01 → 02 → 03 → 04 → 05 → 06 → 08 → 09 → 10（[00 案例库](notes/00-project-casebank.md) 随时填，别攒到最后） | 阶段 1 |
| ③ 背 L1 | 每天默写 4 张速记表，各 15 分钟 | [架构风格](notes/cheatsheet-architecture-styles.md) / [质量属性+战术](notes/cheatsheet-quality-tactics.md) / [设计模式](notes/cheatsheet-design-patterns.md) / [英语词汇](notes/cheatsheet-english-glossary.md) | 阶段 1 起，每天 |
| ④ 刷真题 | 按年倒序刷题，错题归档回 notes/ | [past-papers/README.md](past-papers/README.md)（内含 ①→⑧ 刷题顺序表：2025 → 2024 → 2023 → 2022 → 2021 → mock×3） | 阶段 2-4 主体 |
| ⑤ 写论文 | 填案例库 → 套模板 → 限时成文 | [00 案例库](notes/00-project-casebank.md) → [07-essay-topics](notes/07-essay-topics.md) → [essays/templates/ 01-05](essays/README.md)（每周 1 篇，按 [评分表](essays/README.md) 自评） | 阶段 2 末起 |

**单日节奏**：早 15min 默写 cheatsheet → 晚 1-2h 专题笔记 / 真题 → 周末整套模拟 + 周日晚在 [进度记录](docs/changelog/progress-20260817.md) 打卡。

**优先级口诀**：案例库 > cheatsheet > 架构核心(03) > 真题 > 其余笔记。时间不够先砍 09/10 这类纯记忆章节（考前 1 周突击即可）。

## 快速导航

### 规划层（必看）

| 入口 | 说明 |
|------|------|
| [考试说明](docs/spec/exam-overview-20260817.md) | 三科结构 / 考点分布 / 合格线 |
| [3 个月学习计划](docs/spec/study-plan-20260817.md) | 12 周排期（4 阶段） |
| [复习思路](docs/spec/review-strategy-20260817.md) | 方法论 + 三科策略 + Java 优势转化 |
| [资料清单](docs/spec/resources-20260817.md) | 教材 / 视频 / 真题来源 / 预算 |
| [进度记录](docs/changelog/progress-20260817.md) | 每周打卡 + 错题统计 |

### 学习笔记（notes/）

| 文件 | 核心内容 | 优先级 |
|------|----------|--------|
| [00-project-casebank.md](notes/00-project-casebank.md) | 项目案例库（论文通用素材） | ★ 最高，先填 |
| [03-architecture-core.md](notes/03-architecture-core.md) | 架构核心：14 风格 / 质量属性 / ATAM / 中间件 | ★★★ 必背 |
| [02-software-engineering.md](notes/02-software-engineering.md) | 软工：开发模型对比 / UML 关系 / 测试 / 维护 | ★★★ |
| [05-emerging-tech.md](notes/05-emerging-tech.md) | 新兴：云/大数据/微服务/AI/区块链/分布式理论 | ★★★ |
| [06-security-reliability.md](notes/06-security-reliability.md) | 安全+可靠：加密 / PKI / 等保 / MTBF / RAID | ★★ |
| [01-computer-basics.md](notes/01-computer-basics.md) | 计基：CPU / OS / 网络 / 数据库基础 | ★★ |
| [04-system-modeling.md](notes/04-system-modeling.md) | 建模：UML 9 图 / MDA / DFD | ★★ |
| [07-essay-topics.md](notes/07-essay-topics.md) | 论文方法：结构 / 5 篇主题 / 评分要点 | ★★★ |
| [08-embedded-systems.md](notes/08-embedded-systems.md) | 嵌入式：RTOS / RM-EDF 调度 / 优先级反转 / AUTOSAR（案例试题三必考） | ★★★ |
| [09-math-and-mgmt.md](notes/09-math-and-mgmt.md) | 数学与管理：EVM / 关键路径 / 盈亏平衡 / EMV / 图论 / 合同招投标 | ★★ |
| [10-ip-standards-enterprise.md](notes/10-ip-standards-enterprise.md) | 知识产权期限表 + 标准代号 + ERP/EAI/电子政务（纯记忆送分） | ★★ |

### L1 背诵速记表（cheatsheet，每天默写）

| 文件 | 内容 |
|------|------|
| [架构风格 14 种速记表](notes/cheatsheet-architecture-styles.md) | 5 大类 14 风格对照表 + 7 种补充 + 4 组易混淆对比 + 5 步法答题套路 |
| [质量属性+战术速记表](notes/cheatsheet-quality-tactics.md) | 6 要素 + 6 大属性×战术矩阵 + SLA/MTBF 计算 + 案例答题 5 步法 |
| [设计模式 23 种分类速记表](notes/cheatsheet-design-patterns.md) | 创建 5 / 结构 7 / 行为 11 + 口诀 + 易混淆对比 + 案例高频考法 |
| [专业英语词汇速记表](notes/cheatsheet-english-glossary.md) | 11 类高频术语 + 完形逻辑词 + 答题技巧（上午 #71-75 送分） |

### 论文模板（essays/templates/，阶段 2 起每周一篇）

| 模板 | 主题 | 适用论文题 |
|------|------|------------|
| [01-web-architecture.md](essays/templates/01-web-architecture.md) | Web 高并发（LB + 多级缓存 + DB 优化） | 高并发架构 / 负载均衡缓存 |
| [02-architecture-style.md](essays/templates/02-architecture-style.md) | 架构风格选择与演进（单体→微服务） | 架构风格选择 / 系统重构 |
| [03-quality-attributes.md](essays/templates/03-quality-attributes.md) | 质量属性设计 + ATAM 评估 | 质量属性 / ATAM / 高可用 |
| [04-database-optimization.md](essays/templates/04-database-optimization.md) | 数据库优化（读写分离+分库+NoSQL） | 数据库架构 / 海量数据 |
| [05-microservices.md](essays/templates/05-microservices.md) | 微服务全栈（DDD + 治理 + DevOps） | 微服务 / DDD / 中台 / DevOps |

### 历年真题与模拟题（past-papers/）

★ 入口：[past-papers/README.md](past-papers/README.md)（含刷题指南 + 核心大纲 + 勾选追踪）
- 18 份文件已填真实题目（约 12 万字），可直接刷题
- 覆盖：2021-2025 五年真题（上午 / 案例 / 论文）× 3 套综合模拟卷
- 含：如何开始（4 步法）+ 核心题目大纲（4 张高频考点速查表）+ 推荐刷题顺序

## 目录结构

```
study-exam/
├── README.md                      本文件（入口）
├── docs/
│   ├── spec/                      规划文档（考试说明/计划/思路/资料）
│   ├── changelog/                 进度记录（progress-日期.md）
│   └── report/                    阶段复盘报告
├── notes/                         学习笔记 + 背诵表
│   ├── 00-project-casebank.md     ★ 项目案例库（优先填）
│   ├── 01~07-*.md                 7 大章节笔记
│   ├── 08-embedded-systems.md     嵌入式（案例试题三必考）
│   ├── 09-math-and-mgmt.md        数学/运筹/项目管理计算
│   ├── 10-ip-standards-*.md       知识产权/标准化/企业信息化
│   └── cheatsheet-*.md            4 张 L1 背诵速记表
├── past-papers/                   真题 + 模拟题（见内部 README 索引）
│   ├── README.md                  ★ 索引总表 + 勾选追踪
│   ├── morning/                   上午综合（2021-2025 × 5 份）
│   ├── afternoon-case/            下午案例（2021-2025 × 5 份）
│   ├── afternoon-essay/           下午论文（2021-2025 × 5 份）
│   └── mock/                      综合模拟卷 × 3 套
└── essays/                        论文练习
    ├── README.md                  流程 + 评分表
    └── templates/                 ★ 5 篇论文模板
```

## 三科合格线（三科一次性全部 ≥ 45/75 才通过）

| 科目 | 题量 | 时长 | 合格线 |
|------|------|------|--------|
| 上午综合知识 | 75 道单选 | 150 分钟 | ≥ 45 |
| 下午案例分析 | 5 道大题（必答） | 90 分钟 | ≥ 45 |
| 下午论文 | 4 选 1，2500 字 | 120 分钟 | ≥ 45 |

## 今日进度（2026-09-16）

- **阶段**：阶段 2 专题突破进行中 → W5（9/14-9/20）专题：质量属性 + 架构评估
- **剩余**：约 7.5 周（距 11/7 考试）
- **本周主线**：按 [past-papers/README.md](past-papers/README.md) 顺序开刷 2025 三科真题（不限时，错题归档到 notes/）
- **立即可做 3 件事**：
  1. 刷 [2025 上午 + 案例 + 论文](past-papers/README.md)（本周必做真题）
  2. 填 [00-project-casebank.md](notes/00-project-casebank.md) 的量化数据（1 小时搞定，论文 80% 素材）
  3. 每天默写 4 张 cheatsheet（15 分钟/天）
