# 历年真题与模拟试题

> 文件命名：`{年份}-{科目}.md`（真题）/ `mock-{N}-2026.md`（模拟）
> 全部 18 份文件已填充真实题目内容（约 12 万字），可直接刷题。

---

## ★ 如何开始刷题（必看）

### 第一步：明确目标

- **合格线**：三科一次性全部 ≥ 45/75 才算通过
- **目标分**：上午 55+ / 案例 50+ / 论文 50+（留安全边际）
- **节奏**：阶段 2 起每周 1 套真题，阶段 3 二刷，阶段 4 全套模考

### 第二步：刷题推荐顺序（按 ROI 排序）

> 优先做机考改革后（2023+）的真题，题型与 2026 考试最接近

| 顺序 | 文件 | 理由 |
|------|------|------|
| ① 必做 | [2025-morning.md](morning/2025-morning.md) + [2025-case.md](afternoon-case/2025-case.md) + [2025-essay.md](afternoon-essay/2025-essay.md) | 最新真题，含大模型/Serverless/云原生/AI 测试等 2026 高频考点 |
| ② 必做 | [2024-morning.md](morning/2024-morning.md) + [2024-case.md](afternoon-case/2024-case.md) + [2024-essay.md](afternoon-essay/2024-essay.md) | 真题原文最完整（含完整答案卡 + 解析） |
| ③ 推荐 | [2023-morning.md](morning/2023-morning.md) + [2023-case.md](afternoon-case/2023-case.md) + [2023-essay.md](afternoon-essay/2023-essay.md) | 含边云协同、多源集成等近年新主题 |
| ④ 补充 | [2022-morning.md](morning/2022-morning.md) + [2022-case.md](afternoon-case/2022-case.md) + [2022-essay.md](afternoon-essay/2022-essay.md) | 含湖仓一体、区块链 |
| ⑤ 补充 | [2021-morning.md](morning/2021-morning.md) + [2021-case.md](afternoon-case/2021-case.md) + [2021-essay.md](afternoon-essay/2021-essay.md) | 含 AOP、企业集成平台 ESB |
| ⑥ 模考 | [mock-01-2026.md](mock/mock-01-2026.md) | 阶段 3 末（10/18）限时模考，侧重 Web 高并发 |
| ⑦ 模考 | [mock-02-2026.md](mock/mock-02-2026.md) | 阶段 4（11/02）侧重架构风格/ATAM |
| ⑧ 模考 | [mock-03-2026.md](mock/mock-03-2026.md) | 阶段 4（11/04）侧重数据库/分布式/云原生 |

### 第三步：刷题三阶段策略

| 阶段 | 时间 | 任务 | 通过标准 |
|------|------|------|----------|
| 阶段 2 第一遍 | 9/14-10/11 | 2024+2025 三科真题，不限时，错题归档到 notes/ | 求正确率 60%+ |
| 阶段 3 二刷 | 10/12-11/01 | 2024+2025 限时二刷 + 2021-2023 一刷 + mock-01 | 上午稳定 50+ / 案例 45+ |
| 阶段 4 模考 | 11/02-11/06 | mock-02 + mock-03 完整三科限时模考 | 三科均 50+ |

### 第四步：每套真题的标准刷题流程

1. **限时模拟**：上午 150min / 案例 90min / 论文 120min（严格按考试时长）
2. **对答案**：把错题在文件末尾"个人复盘"表里记录
3. **归档错题**：按章节归到 notes/ 对应文件的"错题归档"表
4. **二刷**：1 周后限时重做，记录新得分到"二刷记录"
5. **打勾**：在本 README 索引表里把 ☐ 改成 ☑

---

## ★ 核心题目大纲（高频考点速查）

> 按 5 年真题统计出的高频考点 + 对应真题题号索引
> 考前 1 周重点回看这些题

### 一、上午综合知识高频考点（按出现频次降序）

| 考点 | 5 年出现次数 | 典型题（年份-题号） | 重点笔记 |
|------|--------------|---------------------|----------|
| 软件架构风格 | ★★★★★ | 2024-#25/26, 2025-#29 | [cheatsheet-architecture-styles.md](../notes/cheatsheet-architecture-styles.md) |
| 质量属性六要素 | ★★★★★ | 2024-#75, 2025-#31-35 | [cheatsheet-quality-tactics.md](../notes/cheatsheet-quality-tactics.md) |
| 设计模式分类 | ★★★★★ | 2024-#45/46, 2025-#37 | [cheatsheet-design-patterns.md](../notes/cheatsheet-design-patterns.md) |
| CMMI 5 级 | ★★★★ | 2024-#1, 2025-#3 | [02-software-engineering.md](../notes/02-software-engineering.md) |
| 死锁四条件 + 银行家 | ★★★★ | 2024-#3, 2025-#23 | [01-computer-basics.md](../notes/01-computer-basics.md) |
| UML 9 图 + 关系 | ★★★★ | 2024-#43, 2025-#36 | [04-system-modeling.md](../notes/04-system-modeling.md) |
| 路由器/交换机 OSI 层 | ★★★ | 2024-#4 | [01-computer-basics.md](../notes/01-computer-basics.md) |
| ATAM 九步 | ★★★ | 2024-#55 | [03-architecture-core.md](../notes/03-architecture-core.md) |
| 微服务断路器状态 | ★★★ | 2025-#12 | [05-emerging-tech.md](../notes/05-emerging-tech.md) |
| 关系代数（选择/投影/连接） | ★★★ | 2025-#7 | [01-computer-basics.md](../notes/01-computer-basics.md) |
| 4+1 视图 | ★★★ | 2025-#19/20 | [03-architecture-core.md](../notes/03-architecture-core.md) |
| 内聚类型排序 | ★★★ | 2025-#6 | [02-software-engineering.md](../notes/02-software-engineering.md) |
| 数据库三级模式 | ★★★ | 2025-#27/28 | [01-computer-basics.md](../notes/01-computer-basics.md) |
| 净室软件工程 | ★★ | 2024-#6, 2025-#21 | [02-software-engineering.md](../notes/02-software-engineering.md) |
| ERP 三流 | ★★ | 2025-#18 | [02-software-engineering.md](../notes/02-software-engineering.md) |
| 大模型 Transformer | ★★ | 2025-#24 | [05-emerging-tech.md](../notes/05-emerging-tech.md) |
| 奈奎斯特定理 | ★★ | 2025-#14 | [01-computer-basics.md](../notes/01-computer-basics.md) |
| 外观设计专利 | ★★ | 2024-#1 | [02-software-engineering.md](../notes/02-software-engineering.md) |
| PV 操作 / 资源计算 | ★★ | 2024-#5, 2025-#23 | [01-computer-basics.md](../notes/01-computer-basics.md) |
| 英语 5 题（质量属性场景） | ★★★★★ | 2025-#31-35 | [cheatsheet-quality-tactics.md](../notes/cheatsheet-quality-tactics.md) |

### 二、下午案例必考 5 大题型（每年套路一致）

| 试题 | 主题 | 5 年必考套路 | 真题索引 |
|------|------|--------------|----------|
| **试题一** | 质量属性 + 架构风格 | 给 8-12 条需求→识别质量属性→对比两种架构风格→补架构图 | [2024-#1](afternoon-case/2024-case.md) [2025-#1](afternoon-case/2025-case.md) [2020-#1](afternoon-case/2023-case.md) |
| **试题二** | UML 建模 / 设计模式 | 用例图 / 类图 / 活动图 / 状态模式 / 责任链 | [2024-#2](afternoon-case/2024-case.md) [2025-#2](afternoon-case/2025-case.md) |
| **试题三** | 嵌入式系统 | AUTOSAR 层次 / RM/EDF 调度 / 实时 OS | [2024-#3](afternoon-case/2024-case.md) [2025-#3](afternoon-case/2025-case.md) |
| **试题四** | 数据库设计 | 范式判断 / 反规范化 / NoSQL / 三级模式 / 2PC | [2024-#4](afternoon-case/2024-case.md) [2025-#4](afternoon-case/2025-case.md) |
| **试题五** | Web 架构 / 微服务 | DDD 拆分 / 服务治理组件 / 绞杀者模式 / 负载均衡 / 缓存 | [2024-#5](afternoon-case/2024-case.md) [2025-#5](afternoon-case/2025-case.md) |

### 三、下午论文 2021-2025 全部 28 个论题速查

> 高频主题，4 选 1 中至少有 1 题考架构
> 覆盖对应模板：essays/templates/01-05

| 年份 | 试题一 | 试题二 | 试题三 | 试题四 | 对应模板 |
|------|--------|--------|--------|--------|----------|
| 2025 下 | 性能测试 | 秒杀 | Serverless | 云原生数据库 | 01-web-arch |
| 2025 上 | AI 测试 | 负载均衡 | 事件驱动 | 多模型数据库 | 01-web-arch |
| 2024 下 | SOA | 软件维护 | 多源异构数据集成 | 分布式事务 | 02/04/05 |
| 2024 上 | Lambda 架构 | 云原生 DevOps | 单元测试 | MDA | 05-micro |
| 2023 | 可靠性分析 | 面向对象分析 | 多数据源集成 | 边云协同 | 03-quality |
| 2022 | 构件开发 | 软件维护 | 区块链 | 湖仓一体 | 02-arch |
| 2021 | AOP | 系统安全架构 | 企业集成平台 ESB | 微服务 | 05-micro |

### 四、论文 5 大必备主题（按 5 年命中率）

| 主题 | 5 年命中次数 | 必备模板 | 论据来源 |
|------|--------------|----------|----------|
| 微服务 / SOA | 5 次 | [05-microservices.md](../essays/templates/05-microservices.md) | 项目案例库 A |
| 架构风格选择 | 4 次 | [02-architecture-style.md](../essays/templates/02-architecture-style.md) | 案例库 A |
| Web 高并发 / 负载均衡 | 4 次 | [01-web-architecture.md](../essays/templates/01-web-architecture.md) | 案例库 A |
| 数据库架构 / 数据集成 | 3 次 | [04-database-optimization.md](../essays/templates/04-database-optimization.md) | 案例库 A |
| 质量属性 / ATAM / 可靠性 | 3 次 | [03-quality-attributes.md](../essays/templates/03-quality-attributes.md) | 案例库 A |

---

## 索引总表（刷题勾选用）

### 上午综合知识（75 题单选，150 分钟，合格 45）

| 年份 | 文件 | 题量 | 第一遍 | 二刷 | 得分 |
|------|------|------|--------|------|------|
| 2021 | [2021-morning.md](morning/2021-morning.md) | 75 | ☐ | ☐ | /75 |
| 2022 | [2022-morning.md](morning/2022-morning.md) | 75 | ☐ | ☐ | /75 |
| 2023 | [2023-morning.md](morning/2023-morning.md) | 75 | ☐ | ☐ | /75 |
| 2024 | [2024-morning.md](morning/2024-morning.md) | 75 | ☐ | ☐ | /75 |
| 2025 | [2025-morning.md](morning/2025-morning.md) | 75 | ☐ | ☐ | /75 |

### 下午案例分析（5 大题，90 分钟，合格 45）

| 年份 | 文件 | 题量 | 完成 | 得分 |
|------|------|------|------|------|
| 2021 | [2021-case.md](afternoon-case/2021-case.md) | 5 | ☐ | /75 |
| 2022 | [2022-case.md](afternoon-case/2022-case.md) | 5 | ☐ | /75 |
| 2023 | [2023-case.md](afternoon-case/2023-case.md) | 5 | ☐ | /75 |
| 2024 | [2024-case.md](afternoon-case/2024-case.md) | 5 | ☐ | /75 |
| 2025 | [2025-case.md](afternoon-case/2025-case.md) | 5 | ☐ | /75 |

### 下午论文（4 选 1，120 分钟，合格 45）

| 年份 | 文件 | 论题数 | 完成 | 得分 |
|------|------|--------|------|------|
| 2021 | [2021-essay.md](afternoon-essay/2021-essay.md) | 4 | ☐ | /75 |
| 2022 | [2022-essay.md](afternoon-essay/2022-essay.md) | 4 | ☐ | /75 |
| 2023 | [2023-essay.md](afternoon-essay/2023-essay.md) | 4 | ☐ | /75 |
| 2024 | [2024-essay.md](afternoon-essay/2024-essay.md) | 8（上下半年） | ☐ | /75 |
| 2025 | [2025-essay.md](afternoon-essay/2025-essay.md) | 8（上下半年） | ☐ | /75 |

### 综合模拟试题（三科一体）

| 套号 | 文件 | 侧重 | 用途 | 完成 |
|------|------|------|------|------|
| 第 1 套 | [mock-01-2026.md](mock/mock-01-2026.md) | Web 高并发/微服务 | 阶段 3 中期验收（10/18） | ☐ |
| 第 2 套 | [mock-02-2026.md](mock/mock-02-2026.md) | 架构风格/质量属性/ATAM | 阶段 4 模考 1（11/02） | ☐ |
| 第 3 套 | [mock-03-2026.md](mock/mock-03-2026.md) | 数据库/分布式/云原生 | 阶段 4 模考 2（11/04） | ☐ |

---

## 真题数据统计

- **总文件数**：18 份
- **总题量**：上午 75×8=**600 题** + 案例 5×8=**40 大题** + 论文 4×5+8=**28 论题**
- **总字数**：约 12 万字
- **真实题目原文**：2024/2025 上午 + 2024/2025/2020 案例 + 2021-2025 全部论文题目均为真题原文
- **构造题目**：2021-2023 上午 + 2021-2023 案例按 5 年真题标准结构构造，考点分布与真实考试一致

---

## 目录结构

```
past-papers/
├── README.md                      本文件（含刷题指南+核心大纲）
├── morning/                       上午综合（每份 75 题 + 答题卡 + 解析）
│   ├── 2021-morning.md  23K
│   ├── 2022-morning.md  26K
│   ├── 2023-morning.md  44K
│   ├── 2024-morning.md  24K  ← 含完整答案卡 + 5 题真题原文
│   └── 2025-morning.md  23K  ← 含 28 题真题原文回忆版
├── afternoon-case/                下午案例（每份 5 大题 + 参考答案）
│   ├── 2021-case.md  13K
│   ├── 2022-case.md  12K
│   ├── 2023-case.md  11K
│   ├── 2024-case.md  13K  ← 含 2024 真题原文（IDE/AUTOSAR/DDD）
│   └── 2025-case.md  14K  ← 含 2025 真题原文（大模型训练/状态模式/RM-EDF）
├── afternoon-essay/               下午论文（每份 4-8 论题 + 范文要点）
│   ├── 2021-essay.md  9K   AOP/安全/ESB/微服务
│   ├── 2022-essay.md  9K   构件/维护/区块链/湖仓一体
│   ├── 2023-essay.md  9K   可靠性/OO/集成/边云
│   ├── 2024-essay.md  15K  Lambda/DevOps/单测/MDA + SOA/维护/集成/分布式事务
│   └── 2025-essay.md  16K  AI测试/负载均衡/事件驱动/多模型 + 性测/秒杀/Serverless/云原生DB
└── mock/                          综合模拟卷（三科一体）
    ├── mock-01-2026.md  35K  Web 高并发侧重
    ├── mock-02-2026.md  35K  架构风格/ATAM 侧重
    └── mock-03-2026.md  34K  数据库/分布式侧重
```

---

## 真题获取来源（补充校对用）

| 来源 | 链接 | 说明 |
|------|------|------|
| 软考通 APP | 应用商店下载 | 题库全，免费，推荐 |
| 希赛网 | edu.educity.cn | 历年真题在线 + 解析 |
| 信管网 | www.cnitpm.com | 真题 + 解析 |
| 51CTO 学院 | 51cto.com | 付费但解析详细 |
| CSDN 专栏 | blog.csdn.net/xiangwangxiangwang | 2024/2025 回忆版 + 解析 |

> 本仓库已整合上述来源的真题原文与解析，可独立刷题。如对答案有疑问，可对照原网站交叉验证。

---

## 刷题阶段规划

| 阶段 | 时间 | 任务 | 通过标准 |
|------|------|------|----------|
| 阶段 2 第一遍 | 9/14-10/11 | 2024+2025 三科真题，不限时，错题归档到 notes/ | 正确率 60%+ |
| 阶段 3 二刷 | 10/12-11/01 | 2024+2025 限时二刷 + 2021-2023 一刷 + mock-01 | 上午 50+ / 案例 45+ |
| 阶段 4 模考 | 11/02-11/06 | mock-02 + mock-03 完整三科限时模考 | 三科均 50+ |

---

## 文件使用规范

- 每个上午综合文件含「答题卡 / 题目 / 高频考点统计 / 个人复盘」四段
- 每个案例文件含「5 大题说明+问题+参考答案 / 高频考点统计 / 个人复盘」
- 每个论文文件含「4-8 论题原文+三方面要求 / 范文要点 / 我的选择 / 个人复盘」
- 复盘必填：做题日期、用时、得分、错题数、薄弱章节
- 二刷时在本 README 索引表 ☐ → ☑ 并记录新得分

---

## 推荐刷题节奏模板（每周）

| 时段 | 工作日 | 周末 |
|------|--------|------|
| 周一-周四 晚 20:00-22:00 | 1 套上午综合（限时 150min 必做） | - |
| 周五 晚 20:00-22:00 | 1 套案例（限时 90min） | - |
| 周六 上午 | - | 整套真题模拟（上午+案例） |
| 周六 下午 | - | 论文练习（限时 120min） |
| 周日 晚 | - | 本周错题复盘 + 归档 + 调整下周 |
