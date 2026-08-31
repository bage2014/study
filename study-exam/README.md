# 软考高级 - 系统架构设计师 备考仓库

> 目标：2026 年 11 月考试通过
> 起点：2026-08-17，剩约 9 周
> 基础：Java 老手，已有软件设计师中级

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

### L1 背诵速记表（cheatsheet，每天默写）

| 文件 | 内容 |
|------|------|
| [架构风格 14 种速记表](notes/cheatsheet-architecture-styles.md) | 5 大类 14 风格对照表 + 7 种补充 + 4 组易混淆对比 + 5 步法答题套路 |
| [质量属性+战术速记表](notes/cheatsheet-quality-tactics.md) | 6 要素 + 6 大属性×战术矩阵 + SLA/MTBF 计算 + 案例答题 5 步法 |
| [设计模式 23 种分类速记表](notes/cheatsheet-design-patterns.md) | 创建 5 / 结构 7 / 行为 11 + 口诀 + 易混淆对比 + 案例高频考法 |

### 论文模板（essays/templates/，阶段 2 起每周一篇）

| 模板 | 主题 | 适用论文题 |
|------|------|------------|
| [01-web-architecture.md](essays/templates/01-web-architecture.md) | Web 高并发（LB + 多级缓存 + DB 优化） | 高并发架构 / 负载均衡缓存 |
| [02-architecture-style.md](essays/templates/02-architecture-style.md) | 架构风格选择与演进（单体→微服务） | 架构风格选择 / 系统重构 |
| [03-quality-attributes.md](essays/templates/03-quality-attributes.md) | 质量属性设计 + ATAM 评估 | 质量属性 / ATAM / 高可用 |
| [04-database-optimization.md](essays/templates/04-database-optimization.md) | 数据库优化（读写分离+分库+NoSQL） | 数据库架构 / 海量数据 |
| [05-microservices.md](essays/templates/05-microservices.md) | 微服务全栈（DDD + 治理 + DevOps） | 微服务 / DDD / 中台 / DevOps |

### 历年真题与模拟题（past-papers/）

- 完整索引 + 勾选追踪见 [past-papers/README.md](past-papers/README.md)
- 覆盖：2021-2025 三年真题（上午 / 案例 / 论文）× 3 套综合模拟卷

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
│   └── cheatsheet-*.md            3 张 L1 背诵速记表
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

## 今日进度（2026-08-31）

- **阶段**：阶段 1 扫盲 → W3 建模+中间件+嵌入式
- **剩余**：约 9 周
- **立即可做 3 件事**：
  1. 填 [00-project-casebank.md](notes/00-project-casebank.md) 的量化数据（1 小时搞定，论文 80% 素材）
  2. 每天默写 3 张 cheatsheet（15 分钟/天）
  3. 在 [progress-20260817.md](docs/changelog/progress-20260817.md) 补 W1、W2 打卡
