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

## 开源技能包装

| 技能名称 | 描述 | 底层实现 |
|----------|------|----------|
| os-ui-library | UI组件库技能 | 可配置（自研/Ant Design/Material UI） |
| os-ui-library-impl-native | UI组件库自研实现 | 自研轻量级实现 |
| os-chat-model | 聊天模型技能 | 可配置（OpenAI/DeepSeek/Claude） |

## 安装方式

### 使用安装脚本

```bash
# 安装所有技能到项目
./install-skills.sh /path/to/project

# 安装指定技能
./install-skills.sh -s common-coding -s common-project-standard .

# 列出可用技能
./install-skills.sh -l
```

## 规范验证

```bash
# 验证所有技能
./validate-skills.sh

# 验证指定技能
./validate-skills.sh -s os-ui-library

# 修复技能格式
./validate-skills.sh -f
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
| 测试 | common-backend-unit-test, common-frontend-playwright-test | 单元测试与UI测试 |
| 部署 | common-dockerize | Docker容器化 |
| 工具类 | personal-java-common | Java通用工具类 |
| 规范管理 | common-project-standard, common-requirement-clarification | 项目规范与需求澄清 |