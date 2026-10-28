# 技能路由表（Skill Router）

> 本文件是全局技能索引。AI 工具在任何项目中打开时，根据当前任务场景，查找并加载对应技能。
> 位置: study-ai-skills/SKILLS-ROUTER.md
> 项目引用方式: 在项目 AGENTS.md 中写 `技能路由: ~/bage/github/study/study-ai-skills/SKILLS-ROUTER.md`

---

## 使用方式

AI 收到任务时，按以下流程操作：
1. **识别任务类型** → 调研类 / 编码类 / Bug 修复类
2. **执行强制流程** → 按对应类型的完整流程执行（必要环节不可跳过）
3. **加载技能** → 按流程步骤调用对应 Skill
4. **按技能指令执行** → 每个 SKILL.md 内含完整流程

---

## 强制流程约束

**核心原则**：必要环节不能少，可以多，但绝对不能少！

### 调研/方案类任务（7 步强制流程）

| 步骤 | 环节 | 必须加载的技能 | 输出物 |
|------|------|---------------|--------|
| 1 | 明确调研目标 | — | 问题边界 + 评估维度 |
| 2 | 收集候选方案 | `personal-solution-research` | 至少 3 个互联网大厂/业界标杆 |
| 3 | 深度比对分析 | `personal-solution-research` | 功能/性能/成本/生态/维护性对比 |
| 4 | 优劣特色总结 | `personal-solution-research` | 每个方案的核心优劣势 |
| 5 | 受益情况分析 | `personal-solution-research` | 各方案实际收益评估 |
| 6 | 推荐理由 | `personal-solution-research` | 基于项目上下文的推荐及理由 |
| 7 | 输出调研报告 | `personal-doc-manager` | `docs/report/` 或 `docs/spec/` |

**强制要求**：
- ✅ 至少比对 3 个互联网大厂或业界标杆
- ✅ 必须给出优劣特色和理由
- ✅ 必须分析受益情况
- ❌ 禁止只给 1-2 个方案就下结论

---

### 编码/开发类任务（8 步强制流程）

| 步骤 | 环节 | 必须加载的技能 | 输出物 |
|------|------|---------------|--------|
| 1 | 需求澄清 | `common-requirement-clarification` + `common-grill-me` | 澄清记录 |
| 2 | 需求文档化 | `personal-doc-manager` | `docs/spec/` 需求文档 |
| 3 | 方案设计 | `common-design-brainstorming` + `personal-solution-research` | 方案对比 |
| 4 | 技术方案 | `common-spec-driven` | 架构/接口/数据模型 |
| 5 | 编码实现 | `personal-backend-coding-standard` 或 `personal-frontend-coding-standard` | 代码 |
| 6 | 单元测试 | `common-backend-unit-test` 或 `common-test-driven-development` | 测试代码 |
| 7 | UI 测试验证 | `common-frontend-playwright-test` | **涉及 UI 时 mandatory** |
| 8 | 测试报告汇总 | `personal-doc-manager` | 覆盖率/通过率/UI 截图 |

**强制要求**：
- ✅ 需求澄清不可跳过（必须经过 grill-me 深度质询）
- ✅ 单元测试不可跳过
- ✅ **涉及 UI 页面时，UI 测试验证 mandatory**
-  禁止跳过测试直接提交

---

### Bug 修复类任务（4 步强制流程）

| 步骤 | 环节 | 必须加载的技能 | 输出物 |
|------|------|---------------|--------|
| 1 | 问题定位 | `common-systematic-debugging` | 根因分析 |
| 2 | 修复实现 | 编码规范技能 | 最小化改动代码 |
| 3 | 测试验证 | 单元测试 + 回归测试 | 测试通过 |
| 4 | UI 验证 | `common-frontend-playwright-test` | **涉及 UI 时 mandatory** |

**简化说明**：
- ✅ 不需要完整的需求澄清和方案设计
- ✅ 测试环节不可跳过
- ✅ **涉及 UI 时，UI 验证 mandatory**

---

## 场景路由表

### 一、项目生命周期

| 场景 | 必须加载的技能 | 可选技能 | 说明 |
|------|---------------|---------|------|
| **创建新项目** | `common-spring-boot-init` + `common-vue-init` + `personal-backend-coding-standard` + `personal-frontend-coding-standard` + `common-project-standard` | `personal-project-runner` | init 只管骨架，规范 skill 才管代码怎么写 |
| **启动/重启/关闭项目** | `personal-project-runner` | — | 自动检测端口，保持原端口不变 |
| **项目规范检查** | `common-project-standard` | `common-code-review` | 检查 AGENTS.md / README / .gitignore 等 |

### 二、需求 → 设计

| 场景 | 必须加载的技能 | 可选技能 | 说明 |
|------|---------------|---------|------|
| **需求不清晰/模糊** | `common-requirement-clarification` | `common-grill-me` | 先澄清再动手 |
| **需求验证/深度质询** | `common-grill-me` | — | 验证需求的合理性、必要性和收益 |
| **方案设计/多方案比对** | `personal-solution-research` | `common-design-brainstorming` | 至少 3 方案，建议 5 方案 |
| **设计探索/渐进式决策** | `common-design-brainstorming` | — | 路径分类 + 渐进式设计 |
| **Spec 驱动开发** | `common-spec-driven` | — | 变更说明 + 方案设计 + 受益分析 |

### 三、编码实现

> ⚠️ **编码完成后，必须自动生成测试**（见第四节质量保障）

| 场景 | 必须加载的技能 | 可选技能 | 说明 |
|------|---------------|---------|------|
| **写 Java 后端代码** | `personal-backend-coding-standard` | `personal-java-common` | 包名/DI/Service/Controller/DTO/异常/日志 |
| **写 Vue 前端代码** | `personal-frontend-coding-standard` | `common-visual-design` | 组件分层/services/types |
| **通用编码** | `common-coding` | — | 编码规范和最佳实践 |
| **数据库设计** | `common-db-design` | — | 表结构 + 实体类生成 |
| **API 文档** | `common-api-doc` | `common-contract-generation` | OpenAPI 规范 |
| **API 契约** | `common-contract-generation` | — | 契约文档管理 |
| **UI 设计** | `common-ui-design-spec` | `common-visual-design` | 设计系统/配色/排版 |
| **UI 原型** | `common-ui-mockup` | `common-visual-design` | 页面设计草稿/线框图 |
| **Java 通用工具** | `personal-java-common` | — | 日期/日志等公共代码 |

### 四、质量保障

| 场景 | 必须加载的技能 | 可选技能 | 说明 |
|------|---------------|---------|------|
| **代码审查** | `common-code-review` | — | 代码质量检查 + 改进建议 |
| **写后端单元测试** | `common-backend-unit-test` | `common-test-driven-development` | JUnit 5 + Mockito |
| **写前端 E2E 测试** | `common-frontend-playwright-test` | — | Playwright UI 自动化 |
| **TDD 开发** | `common-test-driven-development` | — | 红→绿→重构循环 |

### 五、运维部署

| 场景 | 必须加载的技能 | 可选技能 | 说明 |
|------|---------------|---------|------|
| **Docker 容器化** | `common-dockerize` | — | 生成 Dockerfile + compose |
| **数据库迁移** | `common-data-migration` | — | Flyway / Liquibase |

### 六、文档与调试

| 场景 | 必须加载的技能 | 可选技能 | 说明 |
|------|---------------|---------|------|
| **写技术文档** | `common-technical-writer` | `personal-doc-manager` | README/API 文档/教程 |
| **管理项目文档** | `personal-doc-manager` | — | 变更记录/Spec/测试报告 |
| **调试/排错** | `common-systematic-debugging` | — | 根因优先，四阶段排查 |

### 七、技能管理

| 场景 | 必须加载的技能 | 可选技能 | 说明 |
|------|---------------|---------|------|
| **创建新技能** | `common-project-standard` | — | 目录结构 + SKILL.md 规范 + 注册流程 |
| **更新技能内容** | `common-project-standard` | — | 修改后运行 `skills.sh update` 同步双平台 |
| **删除技能** | `common-project-standard` | — | 删除目录 + 清理文档条目 + `skills.sh update` |
| **同步汇总文档** | — | — | 运行 `./scripts/skills.sh sync-docs` |

---

## 技能组合速查

常见工作流的技能调用链：

```
新建项目:
  common-spring-boot-init + common-vue-init        → 骨架
  + personal-backend-coding-standard                → 后端规范
  + personal-frontend-coding-standard               → 前端规范
  + common-project-standard                         → 项目规范文件

需求 → 交付:
  common-requirement-clarification                  → 澄清需求
  → common-grill-me                                 → 验证合理性
  → common-design-brainstorming                     → 探索方案
  → personal-solution-research                      → 方案比对
  → common-spec-driven                              → 写 Spec
  → common-coding + personal-*-coding-standard      → 编码
  → common-test-driven-development                  → 测试
  → common-code-review                              → 审查
  → personal-doc-manager                            → 文档

排查问题:
  common-systematic-debugging                       → 根因排查
  → common-backend-unit-test                        → 补测试
  → common-code-review                              → 审查修复

技能管理:
  创建新技能:
    1. skills/<name>/SKILL.md                       → 编写技能
    2. ./scripts/validate-skills.sh -s <name>       → 验证格式
    3. ./scripts/skills.sh install <name>           → 安装双平台
    4. ./scripts/skills.sh sync-docs                → 同步文档
  
  更新技能:
    1. 修改 skills/<name>/SKILL.md                  → 编辑内容
    2. ./scripts/skills.sh update                   → 全量更新 + 同步文档
  
  删除技能:
    1. 删除 skills/<name>/                          → 移除目录
    2. 更新 SKILLS.md / agents.md / SKILLS-ROUTER.md → 清理条目
    3. ./scripts/skills.sh update                   → 同步双平台
```

---

## 技能仓库

所有技能源码位于: `~/bage/github/study/study-ai-skills/skills/`

| 前缀 | 说明 | 优先级 |
|------|------|--------|
| `personal-*` | 个人定制技能，项目强约束 | 高（冲突时优先） |
| `common-*` | 通用技能，跨项目复用 | 中 |
| `os-*` | 开源技能包装，可切换实现 | 低 |
