# 技能清单

## 个人定制技能

| 技能名称 | 描述 |
|----------|------|
| personal-backend-coding-standard | 后端编码规范技能 |
| personal-frontend-coding-standard | 前端编码规范技能 |
| personal-java-common | Java 通用工具技能，提供日期工具、日志工具等公共代码 |
| personal-project-runner | 项目启动管理技能，支持Java和Vue项目的启动、重启、关闭 |
| personal-doc-manager | 个人文档管理技能，管理变更记录、方案设计Spec文档、测试报告等 |
| personal-solution-research | 方案调研技能，进行多方案比对分析，至少3个方案，建议5个方案比对 |
| personal-brief-style | 简洁风格技能，禁止废话，采用caveman式简短直接的表达方式 |

## 通用技能

| 技能名称 | 描述 |
|----------|------|
| common-project-standard | 项目规范技能 |
| common-contract-generation | 契约生成技能 |
| common-frontend-playwright-test | 前端Playwright测试技能 |
| common-backend-unit-test | 后端单元测试技能 |
| common-coding | 编码技能 |
| common-requirement-clarification | 需求澄清技能（含头脑风暴模块） |
| common-spec-driven | Spec驱动的技能规范 |
| common-grill-me | Grill Me 深度质询技能 |
| common-code-review | 代码审查技能 |
| common-spring-boot-init | Spring Boot 项目初始化技能 |
| common-db-design | 数据库设计技能 |
| common-api-doc | API文档生成技能 |
| common-vue-init | Vue 项目初始化技能 |
| common-ui-design-spec | UI设计规范技能 |
| common-ui-mockup | UI原型图生成技能 |
| common-data-migration | 数据迁移技能 |
| common-dockerize | Docker容器化技能 |
| common-test-driven-development | TDD 测试驱动开发，红绿重构循环（obra/superpowers@test-driven-development） |
| common-systematic-debugging | 系统化调试，根因优先四阶段流程（obra/superpowers@systematic-debugging） |
| common-design-brainstorming | 设计头脑风暴，路径分类 + 渐进式设计决策（obra/superpowers@brainstorming） |
| common-technical-writer | 技术文档专家，README/API 文档/中英文互译（参考 shubhamsabooapps@technical-writer） |
| common-visual-design | 视觉设计哲学，避免 AI 默认风格的有辨识度 UI（anthropics/skills@frontend-design） |

## 开源技能包装

| 技能名称 | 描述 | 底层实现 |
|----------|------|----------|
| os-ui-library | UI组件库技能 | 可配置（自研/Ant Design/Material UI） |
| os-ui-library-impl-native | UI组件库自研实现 | 自研轻量级实现 |
| os-chat-model | 聊天模型技能 | 可配置（OpenAI/DeepSeek/Claude） |

## 安装与更新

### 统一入口（推荐）

```bash
# 安装所有技能到 Trae 全局目录（~/.trae-cn/skills/）和 Qoder（~/.qoder-cn/skills/）
./scripts/skills.sh install

# 安装指定技能
./scripts/skills.sh install common-coding common-project-standard

# 全量更新（强制覆盖 + 重新注入监控埋点 + 同步文档）
./scripts/skills.sh update

# 同步汇总文档（SKILLS.md / AGENTS.md / SKILLS-ROUTER.md）
./scripts/skills.sh sync-docs

# 校验技能规范 / 列出技能 / 重新注入埋点
./scripts/skills.sh validate
./scripts/skills.sh list
./scripts/skills.sh inject
```

### 技能管理流程

```
创建/修改技能 → 验证格式 → 安装到双平台 → 同步汇总文档
     ↓              ↓              ↓              ↓
  skills/     validate-      skills.sh      更新 SKILLS.md
  SKILL.md    skills.sh      install        agents.md
                                              SKILLS-ROUTER.md
```

#### 创建新技能

1. 在 `skills/` 目录下创建技能目录（命名：`personal-` 或 `common-` 前缀）
2. 创建 `SKILL.md` 文件（包含完整的 Frontmatter 和章节）
3. 运行验证：`./scripts/validate-skills.sh -s <skill-name>`
4. 安装到双平台：`./scripts/skills.sh install <skill-name>`
5. **同步汇总文档**：`./scripts/skills.sh sync-docs`
6. 手动更新以下文档（如需要）：
   - `SKILLS.md` — 添加技能到对应分类表格
   - `agents.md` — 更新技能路由表
   - `SKILLS-ROUTER.md` — 添加场景索引

#### 更新现有技能

1. 修改 `skills/<name>/SKILL.md`
2. 运行验证：`./scripts/validate-skills.sh -s <skill-name>`
3. 全量更新：`./scripts/skills.sh update`（自动同步文档）
4. 检查文档是否需要更新（描述变更、新增场景等）

#### 删除技能

1. 删除 `skills/<name>/` 目录
2. 从 `SKILLS.md` 中移除对应行
3. 从 `agents.md` 中移除路由条目
4. 从 `SKILLS-ROUTER.md` 中移除场景索引
5. 运行 `./scripts/skills.sh update` 同步到双平台

**注意事项**：
- 新增技能后必须运行 `sync-docs` 更新汇总文档
- 删除技能时需手动从所有文档中移除对应条目
- `update` 命令会自动调用 `sync-docs`

---

### 任务流程强制约束

**核心原则**：必要环节不能少，可以多，但绝对不能少！

技能系统根据任务类型强制执行不同的完整流程：

#### 调研/方案类任务（7 步强制流程）

| 步骤 | 环节 | 必须加载的技能 | 输出物 |
|------|------|---------------|--------|
| 1 | 明确调研目标 | — | 问题边界 + 评估维度 |
| 2 | 收集候选方案 | `personal-solution-research` | **至少 3 个互联网大厂/业界标杆** |
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

#### 编码/开发类任务（8 步强制流程）

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
- ❌ 禁止跳过测试直接提交

---

#### Bug 修复类任务（4 步强制流程）

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

### 底层脚本

```bash
# 安装所有技能到指定项目
./scripts/install-skills.sh /path/to/project

# 安装指定技能
./scripts/install-skills.sh -s common-coding -s common-project-standard .

# 列出可用技能
./scripts/install-skills.sh -l

# 全局命令安装（skills-install / skills-validate / skills-list / skills-update）
./scripts/install-global.sh
```

## 规范验证

```bash
# 验证所有技能
./scripts/validate-skills.sh

# 验证指定技能
./scripts/validate-skills.sh -s os-ui-library

# 修复技能格式
./scripts/validate-skills.sh -f
```

## 配置示例

```yaml
# .trae/config.yml
skills:
  auto-load: true
  paths:
    - .trae/skills
  open-source:
    os-ui-library:
      active: native
    os-chat-model:
      active: deepseek
```

## 完整技能矩阵

| 类别 | 技能 | 适用场景 |
|------|------|----------|
| 项目初始化 | common-spring-boot-init, common-vue-init | 新建项目 |
| 数据库 | common-db-design, common-data-migration | 数据库设计与迁移 |
| API开发 | common-api-doc, common-contract-generation | API设计与文档 |
| 代码质量 | common-code-review, common-coding | 代码审查与规范 |
| 测试 | common-backend-unit-test, common-frontend-playwright-test, common-test-driven-development | 单元测试/UI测试/TDD |
| 部署 | common-dockerize | Docker容器化 |
| 工具类 | personal-java-common | Java通用工具类 |
| 规范管理 | common-project-standard, common-requirement-clarification | 项目规范与需求澄清 |
| 设计决策 | common-design-brainstorming, common-grill-me | 方案探索/深度质询 |
| 调试修复 | common-systematic-debugging | Bug 根因排查 |
| UI设计 | common-visual-design, common-ui-design-spec, common-ui-mockup | 视觉设计哲学/规范/原型 |
| 文档写作 | common-technical-writer, personal-doc-manager | 技术文档/项目文档管理 |