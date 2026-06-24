# Gray Flow UI 模块

## 一、项目介绍

`study-gray-flow-ui` 是 `study-gray-flow` 后端服务的前端管理界面，提供以下功能：

- **概览面板**：展示日志统计和回放会话概览
- **日志管理**：查看日志列表、按类型筛选、查看调用链详情
- **回放管理**：创建回放会话、查看回放结果、对比响应差异

## 二、技术栈

| 分类 | 技术 | 版本 |
|-----|------|-----|
| 语言 | JavaScript/TypeScript | ES6+ |
| 框架 | Vue | 3.4.x |
| 路由 | Vue Router | 4.3.x |
| 构建工具 | Vite | 5.2.x |
| 测试工具 | Playwright | 1.49.x |

## 三、项目结构

```
study-gray-flow-ui/
├── src/
│   ├── api/                  # API 接口封装
│   │   └── grayflow.js       # 灰度流程 API
│   ├── components/           # 公共组件
│   │   ├── Header.vue        # 顶部导航
│   │   └── Footer.vue        # 底部信息
│   ├── router/               # 路由配置
│   │   └── index.js          # 路由定义
│   ├── views/                # 页面视图
│   │   ├── Home.vue          # 首页概览
│   │   ├── LogsView.vue      # 日志管理页
│   │   └── ReplayView.vue    # 回放管理页
│   ├── App.vue               # 根组件
│   └── main.js               # 入口文件
├── e2e/                      # Playwright 测试
│   ├── pages/                # 页面对象
│   │   ├── GrayHomePage.js   # 首页页面对象
│   │   ├── LogsViewPage.js   # 日志页页面对象
│   │   └── ReplayViewPage.js # 回放页页面对象
│   ├── home.spec.js          # 首页测试
│   ├── logs.spec.js          # 日志页测试
│   └── replay.spec.js        # 回放页测试
├── index.html                # HTML 模板
├── vite.config.js            # Vite 配置
├── playwright.config.js      # Playwright 配置
└── package.json              # 项目依赖
```

## 四、功能说明

### 4.1 首页概览

- 展示日志总条数统计
- 展示回放会话总数统计
- 快捷入口按钮跳转到日志和回放页面

### 4.2 日志管理

- 分页展示日志列表
- 支持按日志类型筛选（CONTROLLER/PROXY）
- 点击 TraceId 查看完整调用链
- 调用链详情 Modal 展示调用顺序、参数、结果

### 4.3 回放管理

- 展示回放会话列表
- 创建新的回放会话（选择最近 N 条请求）
- 展开会话查看回放记录详情
- 查看响应差异对比（Diff）

## 五、快速开始

### 5.1 环境要求

- Node.js 18+
- npm 9+

### 5.2 安装依赖

```bash
cd study-gray-flow-ui
npm install
```

### 5.3 启动开发服务器

```bash
npm run dev
```

访问地址：http://localhost:5174

### 5.4 构建生产版本

```bash
npm run build
```

构建产物输出到 `../study-gray-flow/src/main/resources/static`

### 5.5 运行 E2E 测试

```bash
npm run test:e2e
```

查看测试报告：

```bash
npm run test:e2e:report
```

## 六、页面路由

| 路径 | 页面 | 说明 |
|-----|------|------|
| `/` | Home.vue | 首页概览 |
| `/logs` | LogsView.vue | 日志管理 |
| `/replay` | ReplayView.vue | 回放管理 |

## 七、API 接口

前端调用的后端接口：

| 接口 | 方法 | 说明 |
|-----|------|------|
| `/api/logs` | GET | 查询日志列表 |
| `/api/logs/trace/{traceId}` | GET | 查询调用链 |
| `/api/replay/sessions` | GET | 查询回放会话 |
| `/api/replay/sessions` | POST | 创建回放会话 |
| `/api/replay/sessions/{sessionId}` | GET | 查询回放详情 |

## 八、设计文档

详细设计说明：[docs/design/DESIGN.md](docs/design/DESIGN.md)

## 九、测试文档

E2E 测试说明：[docs/test/E2E.md](docs/test/E2E.md)