# Best Practice Dev Flow - 项目规范文档

## 1. 项目概述

### 1.1 项目名称
Best Practice Dev Flow

### 1.2 项目目标
建立一套完整的AI辅助开发流程范式，包含前端和后端项目模板，实现标准化的开发、测试、验证流程。

### 1.3 技术栈
| 分类 | 技术 | 版本 |
|------|------|------|
| 前端框架 | Vue | 3.4.21 |
| UI组件库 | Ant Design Vue | 4.2.5 |
| 构建工具 | Vite | 5.1.6 |
| 后端框架 | Spring Boot | 3.1.10 |
| 数据库 | H2 | 2.2.x |
| 构建工具 | Maven | 3.9.x |

---

## 2. 目录结构规范

### 2.1 项目根目录
```
study-ai-best-practice-dev-flow/
├── best-practice-dev-flow-ui/          # 前端项目
├── best-practice-dev-flow-backend/     # 后端项目
├── docs/                               # 文档目录
│   ├── specs/                          # 技术规范
│   ├── requirements/                   # 需求文档
│   ├── interaction/                    # 交互文档
│   └── test-reports/                   # 测试报告
├── scripts/                            # 自动化脚本
└── .github/workflows/                  # CI/CD配置
```

### 2.2 前端目录结构
```
best-practice-dev-flow-ui/
├── src/
│   ├── components/                     # 公共组件
│   ├── views/                          # 页面视图
│   ├── utils/                          # 工具函数
│   ├── assets/                         # 静态资源
│   ├── App.vue                         # 根组件
│   └── main.js                         # 入口文件
├── index.html
├── vite.config.js
└── package.json
```

### 2.3 后端目录结构
```
best-practice-dev-flow-backend/
├── src/main/java/com/bage/study/ai/best/practice/dev/flow/
│   ├── controller/                     # REST控制器
│   ├── service/                        # 业务服务层
│   │   └── impl/                       # 服务实现
│   ├── repository/                     # 数据访问层
│   ├── entity/                         # 实体类
│   ├── dto/                            # 数据传输对象
│   ├── config/                         # 配置类
│   └── Application.java                # 启动类
├── src/main/resources/
│   ├── application.yml                 # 应用配置
│   └── schema.sql                      # 数据库初始化
├── src/test/                           # 单元测试
└── pom.xml                             # Maven配置
```

---

## 3. 编码规范

### 3.1 Java编码规范

#### 3.1.1 命名规则
- 类名：大驼峰命名，如 `TaskController`
- 方法名：小驼峰命名，如 `getTaskById`
- 变量名：小驼峰命名，如 `taskService`
- 常量名：全大写，下划线分隔，如 `MAX_PAGE_SIZE`

#### 3.1.2 代码风格
- 使用4空格缩进
- 类成员顺序：静态常量 → 成员变量 → 构造函数 → 公共方法 → 私有方法
- 方法不超过80行，避免过长的方法

### 3.2 JavaScript/Vue编码规范

#### 3.2.1 命名规则
- 组件名：大驼峰命名，如 `TaskList.vue`
- 变量名：小驼峰命名，如 `taskList`
- 方法名：小驼峰命名，如 `handleSubmit`

#### 3.2.2 代码风格
- 使用2空格缩进
- 优先使用Composition API
- 组件脚本使用 `<script setup>`

---

## 4. API接口规范

### 4.1 接口命名规则
- 使用RESTful风格
- 资源名称使用复数形式，如 `/api/tasks`
- 动词使用HTTP方法：GET(查询)、POST(创建)、PUT(更新)、DELETE(删除)

### 4.2 接口示例

#### 任务管理接口
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/tasks | 获取所有任务 |
| GET | /api/tasks/{id} | 获取单个任务 |
| POST | /api/tasks | 创建任务 |
| PUT | /api/tasks/{id} | 更新任务 |
| DELETE | /api/tasks/{id} | 删除任务 |

#### 项目管理接口
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/projects | 获取所有项目 |
| GET | /api/projects/{id} | 获取单个项目 |
| POST | /api/projects | 创建项目 |
| PUT | /api/projects/{id} | 更新项目 |
| DELETE | /api/projects/{id} | 删除项目 |

---

## 5. 数据库规范

### 5.1 表命名规则
- 使用小写字母，下划线分隔，如 `tasks`, `projects`

### 5.2 字段命名规则
- 使用小写字母，下划线分隔，如 `created_at`, `updated_at`

### 5.3 通用字段
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键，自增 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

---

## 6. 测试规范

### 6.1 单元测试
- 测试类命名：`{ClassName}Test`，如 `TaskServiceTest`
- 测试方法命名：`test{MethodName}`，如 `testCreateAndGetTask`
- 每个业务方法至少有一个测试用例

### 6.2 前端验证
- 使用浏览器自动化工具验证UI交互
- 验证脚本存放于 `scripts/` 目录

---

## 7. 文档规范

### 7.1 需求文档
- 位置：`docs/requirements/`
- 包含需求概述、功能描述、验收标准

### 7.2 交互文档
- 位置：`docs/interaction/`
- 包含页面流程、交互细节、测试用例

### 7.3 测试报告
- 位置：`docs/test-reports/`
- 包含测试结果、覆盖率报告

---

## 8. 开发流程

### 8.1 需求分析阶段
1. 编写需求文档
2. 确定技术方案
3. 创建任务清单

### 8.2 开发阶段
1. 编写单元测试
2. 实现业务逻辑
3. 验证功能正确性

### 8.3 测试阶段
1. 运行单元测试
2. 执行UI自动化测试
3. 生成测试报告

### 8.4 交付阶段
1. 代码审查
2. 文档更新
3. 版本发布