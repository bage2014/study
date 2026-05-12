# 研发流程范式

## 1. 流程概述

本文档定义了 Best Practice Dev Flow 项目的标准化研发流程，后续所有新需求和功能迭代都必须严格按照此流程执行。

### 1.1 流程总览

```
需求分析 → 需求文档完善 → 交互设计 → 契约文档更新 → 后端实现 → 后端测试 → 前端实现 → 前端测试 → 验证交付
```

### 1.2 流程阶段说明

| 阶段 | 名称 | 主要产出 | 责任人 |
|------|------|----------|--------|
| 1 | 需求分析 | 需求细化文档 | 产品/开发 |
| 2 | 需求文档 | 更新 requirements.md | 开发 |
| 3 | 交互设计 | 更新 interaction.md | 开发/设计 |
| 4 | 契约文档 | 更新 api-contract.md | 后端开发 |
| 5 | 后端实现 | 后端代码 | 后端开发 |
| 6 | 后端测试 | 单元测试通过 | 后端开发 |
| 7 | 前端实现 | 前端代码 | 前端开发 |
| 8 | 前端测试 | UI自动化测试通过 | 前端开发 |
| 9 | 验证交付 | 功能验证报告 | 测试/开发 |

---

## 2. 阶段一：需求分析

### 2.1 需求收集

- 收集用户需求描述
- 分析需求背景和业务价值
- 确定需求优先级

### 2.2 需求细化

将需求分解为可执行的功能点：
- 明确功能范围
- 定义输入输出
- 确定边界条件
- 识别依赖关系

### 2.3 需求确认

与相关方确认需求理解一致：
- 需求是否清晰
- 是否有遗漏
- 是否存在冲突

---

## 3. 阶段二：需求文档完善

### 3.1 更新需求文档位置

需求文档位置：`docs/requirements/requirements.md`

### 3.2 文档更新内容

| 章节 | 更新内容 |
|------|----------|
| 功能需求 | 添加新需求编号、功能模块、描述、优先级 |
| 非功能需求 | 添加性能、兼容性、安全性要求 |
| 验收标准 | 定义功能验收条件 |
| 项目进度 | 更新里程碑和任务清单 |
| 待办事项 | 添加相关待办任务 |

### 3.3 需求编号规则

- **前端需求**：FR-XXX (Frontend Requirement)
- **后端需求**：BR-XXX (Backend Requirement)
- **测试需求**：TR-XXX (Test Requirement)

---

## 4. 阶段三：交互设计

### 4.1 更新交互文档位置

交互文档位置：`docs/interaction/interaction.md`

### 4.2 交互流程设计

针对每个功能模块设计完整的交互流程：

#### 4.2.1 流程设计要素

| 要素 | 说明 |
|------|------|
| 触发条件 | 用户什么操作触发该功能 |
| 执行步骤 | 功能执行的详细步骤 |
| 预期结果 | 每个步骤的预期输出 |
| 异常处理 | 异常情况的处理方式 |

#### 4.2.2 测试用例设计

为每个交互流程设计测试用例：

| 字段 | 说明 |
|------|------|
| 测试编号 | TC-XXX 或 TC-API-XXX |
| 测试场景 | 描述测试的功能场景 |
| 测试步骤 | 具体执行步骤 |
| 预期结果 | 期望的输出或状态 |
| 优先级 | 高/中/低 |

---

## 5. 阶段四：契约文档更新

### 5.1 契约文档位置

契约文档位置：`docs/api-contract.md`

### 5.2 更新时机

当需求涉及后端接口变更时，必须同步更新契约文档：
- 新增API接口
- 修改现有API接口（路径、参数、响应结构）
- 删除API接口
- 数据模型变更

### 5.3 更新内容

#### 5.3.1 API接口文档

| 字段 | 说明 |
|------|------|
| 路径 | API接口路径 |
| 方法 | HTTP方法（GET/POST/PUT/DELETE） |
| 描述 | 接口功能描述 |
| 请求体 | 请求参数结构（JSON格式） |
| 响应体 | 响应数据结构（JSON格式） |
| 状态码 | HTTP状态码及含义 |

#### 5.3.2 数据模型文档

| 字段 | 说明 |
|------|------|
| 字段名 | 实体属性名称 |
| 类型 | 数据类型 |
| 约束 | 非空、唯一、长度限制等 |
| 描述 | 字段含义说明 |

#### 5.3.3 更新检查清单

- [ ] 新增接口已添加到对应模块章节
- [ ] 请求参数已完整定义
- [ ] 响应结构已完整定义
- [ ] 错误响应已定义
- [ ] 数据模型已更新（如涉及）
- [ ] 状态码已正确标注

### 5.4 契约文档结构

```
docs/api-contract.md
├── 概述（基础信息、错误响应格式）
├── 用户管理API
├── 项目管理API
├── 任务管理API
├── 数据模型
└── 状态码定义
```

---

## 6. 阶段五：后端代码实现

### 6.1 技术栈

- **框架**：Spring Boot 3.1.10
- **语言**：Java 21
- **数据库**：H2 (开发环境) / MySQL (生产环境)
- **ORM**：Spring Data JPA

### 6.2 代码结构

```
src/main/java/com/bage/study/ai/best/practice/dev/flow/
├── controller/     # REST API控制层
├── service/        # 业务逻辑层接口
├── service/impl/   # 业务逻辑层实现
├── repository/     # 数据访问层
├── entity/         # 数据库实体
├── dto/            # 数据传输对象
├── config/         # 配置类
└── Application.java # 启动类
```

### 6.3 编码规范

#### 6.3.1 命名规范

| 类型 | 规范示例 |
|------|----------|
| 类名 | PascalCase (UserController, UserServiceImpl) |
| 方法名 | camelCase (getUserById, createUser) |
| 变量名 | camelCase (userRepository, passwordEncoder) |
| 常量名 | UPPER_SNAKE_CASE (MAX_PAGE_SIZE) |
| 文件命名 | PascalCase (User.java, UserDTO.java) |

#### 6.3.2 代码结构规范

- Controller层：处理HTTP请求，参数校验，调用Service
- Service层：业务逻辑处理，事务管理
- Repository层：数据访问，继承JpaRepository
- DTO：数据传输，避免直接暴露实体

#### 6.3.3 异常处理

- 使用全局异常处理器 `GlobalExceptionHandler`
- 返回统一的错误响应格式：
  ```json
  {
    "timestamp": "2024-01-01T12:00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Resource not found"
  }
  ```

---

## 7. 阶段六：后端测试验证

### 7.1 测试类型

| 测试类型 | 描述 | 工具 |
|----------|------|------|
| 单元测试 | 测试单个方法/类 | JUnit 5 |
| 集成测试 | 测试模块间交互 | Spring Test |
| API测试 | 测试REST接口 | curl/Postman |

### 7.2 测试用例编写

#### 7.2.1 单元测试规范

测试类位置：`src/test/java/com/bage/study/ai/best/practice/dev/flow/`

测试类命名：`{ServiceName}Test.java`

#### 7.2.2 测试用例结构

```java
@Test
void testMethodName() {
    // 准备测试数据
    // 执行测试方法
    // 验证结果
    // 清理资源
}
```

#### 7.2.3 测试覆盖率要求

- 单元测试覆盖率 ≥ 80%
- 核心业务逻辑必须有测试覆盖

### 7.3 测试执行

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成测试报告
mvn surefire-report:report
```

---

## 8. 阶段七：前端代码实现

### 8.1 技术栈

- **框架**：Vue 3.4+
- **UI组件**：Ant Design Vue 4.x
- **构建工具**：Vite 5.x
- **语言**：JavaScript/TypeScript

### 8.2 代码结构

```
src/
├── components/     # 可复用组件
├── api/           # API服务封装
├── views/         # 页面视图
├── App.vue        # 根组件
├── main.js        # 入口文件
└── style.css      # 全局样式
```

### 8.3 编码规范

#### 8.3.1 命名规范

| 类型 | 规范示例 |
|------|----------|
| 组件名 | PascalCase (LoginModal.vue, UserTable.vue) |
| 变量名 | camelCase (currentUser, loading) |
| 方法名 | camelCase (handleLogin, loadUsers) |
| 文件命名 | kebab-case 或 PascalCase |

#### 8.3.2 组件规范

- 使用 `<script setup>` 语法
- 组件职责单一
- Props定义清晰，带有类型和默认值
- Emits事件明确声明

#### 8.3.3 API调用规范

- API调用统一封装在 `api/` 目录
- 使用 async/await 语法
- 统一错误处理
- 请求和响应数据类型定义

---

## 9. 阶段八：前端测试验证

### 9.1 测试类型

| 测试类型 | 描述 | 工具 |
|----------|------|------|
| UI自动化测试 | 模拟用户操作验证 | Playwright |
| 组件测试 | 测试单个组件 | Vue Test Utils |
| E2E测试 | 端到端流程测试 | Playwright |

### 9.2 Playwright测试配置

#### 9.2.1 安装依赖

```bash
npm install @playwright/test
npx playwright install
```

#### 9.2.2 测试脚本位置

测试脚本位置：`tests/` 目录

#### 9.2.3 测试脚本结构

```javascript
import { test, expect } from '@playwright/test'

test('测试场景描述', async ({ page }) => {
  // 导航到页面
  await page.goto('http://localhost:5173')
  
  // 执行操作
  await page.click('button')
  
  // 验证结果
  await expect(page.locator('text=成功')).toBeVisible()
})
```

### 9.3 测试执行

```bash
# 运行所有测试
npx playwright test

# 运行特定测试文件
npx playwright test login.spec.js

# 生成测试报告
npx playwright test --reporter=html
```

---

## 10. 阶段九：验证交付

### 10.1 验证清单

| 验证项 | 检查内容 |
|--------|----------|
| 代码质量 | 无语法错误，代码格式化 |
| 测试通过 | 所有单元测试和UI测试通过 |
| 文档完整 | 需求、交互、API文档更新 |
| 功能验证 | 手动验证核心功能 |
| 性能测试 | API响应时间、页面加载速度 |

### 10.2 交付物

| 交付物 | 描述 |
|--------|------|
| 代码变更 | 新增/修改的源代码 |
| 测试报告 | 单元测试和UI测试报告 |
| 文档更新 | 需求、交互、API文档 |
| 变更说明 | 功能变更描述 |

---

## 11. 流程检查清单

### 11.1 需求阶段

- [ ] 需求已细化和确认
- [ ] 需求文档已更新
- [ ] 需求优先级已确定

### 11.2 设计阶段

- [ ] 交互流程已设计
- [ ] 测试用例已编写
- [ ] API契约已定义

### 11.3 开发阶段

- [ ] 代码符合编码规范
- [ ] 代码已提交版本控制
- [ ] 代码注释完整

### 11.4 测试阶段

- [ ] 单元测试已编写
- [ ] 所有单元测试通过
- [ ] UI自动化测试已编写
- [ ] 所有UI测试通过

### 11.5 交付阶段

- [ ] 文档已更新
- [ ] 功能已验证
- [ ] 测试报告已生成

---

## 12. 版本控制规范

### 12.1 分支管理

| 分支类型 | 用途 |
|----------|------|
| main | 主分支，稳定版本 |
| develop | 开发分支，集成功能 |
| feature/XXX | 功能特性分支 |
| bugfix/XXX | Bug修复分支 |

### 12.2 提交规范

```
<类型>(<模块>): <描述>

<详细说明>

[相关需求编号]
```

**类型说明：**
- feat: 新增功能
- fix: 修复Bug
- docs: 更新文档
- refactor: 代码重构
- test: 添加测试
- chore: 构建/工具更新

**示例：**
```
feat(user): 实现用户登录功能

- 添加登录API接口
- 创建登录页面组件
- 添加登录验证逻辑

[FR-006, BR-006]
```

---

## 12. 工具链推荐

| 工具 | 用途 | 版本 |
|------|------|------|
| Maven | Java构建工具 | 3.9+ |
| Vite | Vue构建工具 | 5.x |
| JUnit 5 | Java单元测试 | 5.9+ |
| Playwright | UI自动化测试 | 1.40+ |
| ESLint | JavaScript检查 | 8.x |
| Prettier | 代码格式化 | 3.x |