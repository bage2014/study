# Gray Flow UI 设计文档

## 一、架构设计

### 1.1 架构风格

采用 Vue 3 标准的组件化架构：

- **视图层**：Vue 组件，负责 UI 渲染
- **数据层**：API 模块，负责与后端通信
- **路由层**：Vue Router，负责页面导航

### 1.2 模块划分

```
┌─────────────────────────────────────────────────────────────┐
│                      视图层 (Views)                        │
│  Home.vue      LogsView.vue      ReplayView.vue           │
│       │              │                  │                  │
└───────┼──────────────┼──────────────────┼─────────────────┘
        ▼              ▼                  ▼
┌─────────────────────────────────────────────────────────────┐
│                    组件层 (Components)                     │
│  Header.vue      Footer.vue                               │
│       │              │                                    │
└───────┼──────────────┼────────────────────────────────────┘
        ▼              ▼
┌─────────────────────────────────────────────────────────────┐
│                      数据层 (API)                          │
│              grayflow.js                                  │
│                      │                                    │
└──────────────────────┼────────────────────────────────────┘
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                     后端 API                               │
│         study-gray-flow (http://localhost:8083)            │
└─────────────────────────────────────────────────────────────┘
```

### 1.3 路由设计

| 路径 | 组件 | 说明 |
|-----|------|------|
| `/` | Home.vue | 首页概览 |
| `/logs` | LogsView.vue | 日志管理 |
| `/replay` | ReplayView.vue | 回放管理 |

## 二、页面设计

### 2.1 首页概览

**功能**：展示统计信息和快捷入口

**布局**：
- 顶部：Logo 和导航
- 主体：两张统计卡片（日志条数、回放会话数）
- 底部：操作按钮（查看日志、查看回放）

**交互**：
- 点击按钮跳转到对应页面
- 统计数据自动刷新

### 2.2 日志管理页

**功能**：日志列表展示和调用链查看

**布局**：
- 工具栏：类型筛选下拉框、调用链搜索框
- 表格：日志列表（ID、类型、TraceId、方法名、耗时、时间）
- 分页：底部分页控件

**交互**：
- 选择类型筛选日志
- 点击 TraceId 弹出调用链 Modal
- 分页浏览

### 2.3 回放管理页

**功能**：回放会话管理和结果查看

**布局**：
- 顶部：标题和新建按钮
- 列表：会话列表（ID、状态、总数、已完成、时间）
- 展开区域：回放记录详情

**交互**：
- 点击新建按钮创建回放会话
- 点击会话行展开详情
- 查看 Diff 对比

## 三、组件设计

### 3.1 Header 组件

**职责**：顶部导航栏

**属性**：无

**事件**：无

**模板结构**：
- Logo 区域
- 导航链接（首页、日志、回放）

### 3.2 Footer 组件

**职责**：底部信息栏

**属性**：无

**事件**：无

**模板结构**：
- 版本信息
- 服务端口

### 3.3 模态框组件

**职责**：通用模态框（调用链详情）

**属性**：
- `show`: 是否显示
- `title`: 标题

**事件**：
- `close`: 关闭事件

## 四、API 设计

### 4.1 grayflow.js

**方法列表**：

| 方法 | 参数 | 返回值 | 说明 |
|-----|------|------|------|
| `fetchLogs` | `{ logType, page, size }` | Promise\<Page\> | 查询日志列表 |
| `fetchTrace` | `traceId` | Promise\<Array\> | 查询调用链 |
| `fetchSessions` | 无 | Promise\<Array\> | 查询回放会话 |
| `fetchSession` | `sessionId` | Promise\<Object\> | 查询会话详情 |
| `createSession` | `count` | Promise\<void\> | 创建回放会话 |

### 4.2 数据模型

**LogRecord**：
```javascript
{
  id: Number,
  logType: String,      // 'CONTROLLER' | 'PROXY'
  traceId: String,
  className: String,
  methodName: String,
  durationMs: Number,
  callIndex: Number,
  args: String,
  resultSummary: String,
  errorMsg: String,
  createdAt: String
}
```

**ReplaySession**：
```javascript
{
  id: Number,
  sessionId: String,
  status: String,       // 'RUNNING' | 'COMPLETED' | 'FAILED'
  totalCount: Number,
  completedCount: Number,
  createdAt: String,
  records: Array
}
```

**ReplayRecord**：
```javascript
{
  id: Number,
  httpMethod: String,
  requestUri: String,
  matchResult: String,      // 'MATCH' | 'MISMATCH'
  chainMatchResult: String, // 'CHAIN_MATCH' | 'CHAIN_MISMATCH'
  diffPatch: String,
  errorMsg: String
}
```

## 五、样式设计

### 5.1 颜色方案

| 颜色 | 值 | 用途 |
|-----|-----|------|
| 主背景 | #f5f5f5 | 页面背景 |
| 卡片背景 | #ffffff | 卡片、表格背景 |
| 主色调 | #1a1a2e | Logo、按钮、导航 |
| 强调色 | #4a9eff | 链接、高亮 |
| 成功色 | #38a169 | 成功状态 |
| 警告色 | #d69e2e | 进行中状态 |
| 错误色 | #e53e3e | 错误状态 |

### 5.2 布局规范

- 页面边距：24px
- 卡片间距：16px
- 按钮高度：36px
- 字体大小：14px（正文）、13px（表格）、12px（辅助文字）

## 六、状态管理

### 6.1 组件状态

每个组件维护自己的本地状态：

- **Home.vue**：`logCount`, `sessionCount`
- **LogsView.vue**：`logs`, `total`, `page`, `filter`, `loading`, `showTraceModal`, `traceData`
- **ReplayView.vue**：`sessions`, `loading`, `showModal`, `expandedSessionId`, `expandedRecords`

### 6.2 数据流向

```
API Response → Component State → Template Render
                   │
                   ▼
            User Interaction → API Request
```

## 七、方案比对

### 7.1 状态管理方案

| 方案 | 优点 | 缺点 | 选择 |
|-----|------|------|-----|
| Vue 3 Composition API | 轻量级，组件自治 | 跨组件通信麻烦 | 当前方案 |
| Pinia | 集中管理，易于调试 | 增加复杂度 | 大规模应用 |
| Vuex | 成熟稳定 | 较重量级 | 旧项目迁移 |

**当前选择**：Composition API，适合中小型应用

### 7.2 UI 框架方案

| 方案 | 优点 | 缺点 | 选择 |
|-----|------|------|-----|
| 纯 CSS | 轻量级，零依赖 | 开发效率低 | 当前方案 |
| Element Plus | 组件丰富 | 体积较大 | 需要更多组件时 |
| Tailwind CSS | 快速开发 | 样式文件大 | 样式复杂时 |

**当前选择**：纯 CSS，简洁高效

## 八、性能优化

### 8.1 懒加载

- 路由懒加载（可选）
- 图片懒加载（如需要）

### 8.2 请求优化

- 分页查询，避免一次性加载大量数据
- 请求缓存（使用浏览器缓存）

### 8.3 渲染优化

- 使用 `v-show` 替代 `v-if` 用于频繁切换的元素
- 列表使用 `key` 属性优化 diff

## 九、浏览器兼容性

目标浏览器：
- Chrome（推荐）
- Firefox
- Safari
- Edge

## 十、代码规范

### 10.1 命名规范

- 文件命名：kebab-case（如 `gray-home-page.js`）
- 组件命名：PascalCase（如 `Header.vue`）
- 变量命名：camelCase（如 `logCount`）

### 10.2 代码风格

- 使用 Composition API（`setup` 语法糖）
- 使用箭头函数
- 注释清晰，便于维护