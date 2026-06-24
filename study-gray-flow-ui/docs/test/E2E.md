# Gray Flow UI E2E 测试文档

## 一、测试概述

本文档描述 `study-gray-flow-ui` 模块的 Playwright E2E 测试策略和测试用例。

## 二、测试环境

| 环境 | 配置 |
|-----|------|
| Node.js | 18+ |
| 浏览器 | Chromium（默认） |
| 测试工具 | Playwright 1.49.x |
| 测试框架 | Playwright Test |

## 三、测试结构

```
e2e/
├── pages/                    # 页面对象模型
│   ├── GrayHomePage.js       # 首页页面对象
│   ├── LogsViewPage.js       # 日志页页面对象
│   └── ReplayViewPage.js     # 回放页页面对象
├── home.spec.js              # 首页测试
├── logs.spec.js              # 日志页测试
└── replay.spec.js            # 回放页测试
```

## 四、页面对象设计

### 4.1 GrayHomePage

| 元素/方法 | 类型 | 说明 |
|----------|------|------|
| `logCountCard` | Locator | 日志统计卡片 |
| `sessionCountCard` | Locator | 会话统计卡片 |
| `goToLogsButton` | Locator | 查看日志按钮 |
| `goToReplayButton` | Locator | 查看回放按钮 |
| `goto()` | Method | 导航到首页 |
| `waitForLoad()` | Method | 等待页面加载 |

### 4.2 LogsViewPage

| 元素/方法 | 类型 | 说明 |
|----------|------|------|
| `typeSelect` | Locator | 类型筛选下拉框 |
| `searchButton` | Locator | 查询按钮 |
| `traceInput` | Locator | TraceId 输入框 |
| `tableRows` | Locator | 日志表格行 |
| `goto()` | Method | 导航到日志页 |
| `waitForLoad()` | Method | 等待页面加载 |
| `filterByType(type)` | Method | 按类型筛选 |
| `openTraceInput(traceId)` | Method | 打开调用链输入 |

### 4.3 ReplayViewPage

| 元素/方法 | 类型 | 说明 |
|----------|------|------|
| `sessionRows` | Locator | 会话列表行 |
| `createButton` | Locator | 新建回放按钮 |
| `createModal` | Locator | 新建模态框 |
| `countInput` | Locator | 数量输入框 |
| `goto()` | Method | 导航到回放页 |
| `waitForLoad()` | Method | 等待页面加载 |
| `expandSession(index)` | Method | 展开会话详情 |
| `createSession(count)` | Method | 创建回放会话 |

## 五、测试用例

### 5.1 首页测试

**测试场景 1**: 统计数据展示

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 访问首页 `/` | 页面加载成功 |
| 2 | 等待 API 响应 | 日志条数显示正确 |
| 3 | 等待 API 响应 | 会话数显示正确 |

**测试场景 2**: 页面导航

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 点击「查看日志」按钮 | 跳转到 `/logs` |
| 2 | 点击「查看回放」按钮 | 跳转到 `/replay` |

**测试场景 3**: 接口异常处理

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | Mock API 返回 500 | 页面显示占位符 `—` |
| 2 | 页面不崩溃 | 正常显示 |

### 5.2 日志页测试

**测试场景 1**: 日志列表加载

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 访问 `/logs` | 页面加载成功 |
| 2 | 等待 API 响应 | 表格显示日志数据 |

**测试场景 2**: 类型筛选

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 选择 CONTROLLER | 只显示 CONTROLLER 类型 |
| 2 | 选择 PROXY | 只显示 PROXY 类型 |

**测试场景 3**: 调用链查看

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 点击 TraceId 单元格 | 弹出调用链 Modal |
| 2 | 查看调用链详情 | 显示调用顺序、参数、结果 |
| 3 | 点击关闭按钮 | Modal 关闭 |

**测试场景 4**: 分页功能

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 查看分页信息 | 显示正确的页码和总数 |
| 2 | 点击下一页 | 切换到下一页 |

### 5.3 回放页测试

**测试场景 1**: 会话列表加载

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 访问 `/replay` | 页面加载成功 |
| 2 | 等待 API 响应 | 列表显示会话数据 |

**测试场景 2**: 创建回放会话

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 点击「新建回放」按钮 | 弹出创建 Modal |
| 2 | 输入数量并确认 | Modal 关闭 |
| 3 | 列表刷新 | 显示新创建的会话 |

**测试场景 3**: 会话详情展开

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 点击会话行 | 展开详情区域 |
| 2 | 查看回放记录 | 显示记录列表 |
| 3 | 再次点击 | 收起详情区域 |

**测试场景 4**: Diff 查看

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 点击「查看 Diff」 | 显示 Diff 内容 |
| 2 | 点击「收起 Diff」 | 隐藏 Diff 内容 |

## 六、测试执行

### 6.1 运行所有测试

```bash
cd study-gray-flow-ui
npm run test:e2e
```

### 6.2 运行指定测试文件

```bash
npm run test:e2e -- home.spec.js
```

### 6.3 运行测试并生成报告

```bash
npm run test:e2e
npm run test:e2e:report
```

### 6.4 调试模式

```bash
npm run test:e2e -- --debug
```

## 七、Mock 数据

### 7.1 日志数据

```javascript
{
  content: [
    {
      id: 1,
      logType: 'CONTROLLER',
      traceId: 'abc12345-dead-beef-0000-000000000001',
      className: 'UserController',
      methodName: 'list',
      durationMs: 12,
      createdAt: '2026-06-23T10:00:00Z'
    }
  ],
  totalElements: 42
}
```

### 7.2 调用链数据

```javascript
[
  {
    id: 1,
    logType: 'CONTROLLER',
    traceId: 'abc12345-dead-beef-0000-000000000001',
    className: 'UserController',
    methodName: 'list',
    durationMs: 12,
    callIndex: 0,
    args: '[]',
    resultSummary: '[{\"id\":1}]'
  }
]
```

### 7.3 回放会话数据

```javascript
{
  id: 1,
  sessionId: 'sess-uuid-0000-0001',
  status: 'COMPLETED',
  totalCount: 2,
  completedCount: 2,
  records: [
    { id: 1, httpMethod: 'GET', requestUri: '/api/users', matchResult: 'MATCH' },
    { id: 2, httpMethod: 'POST', requestUri: '/api/users', matchResult: 'MISMATCH' }
  ]
}
```

## 八、测试配置

### 8.1 playwright.config.js

```javascript
import { defineConfig } from '@playwright/test'

export default defineConfig({
  testDir: './e2e',
  fullyParallel: true,
  reporter: 'html',
  use: {
    baseURL: 'http://localhost:5174',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
  },
  webServer: {
    command: 'npm run dev',
    url: 'http://localhost:5174',
    reuseExistingServer: !process.env.CI,
  },
})
```

### 8.2 CI 集成

在 CI 流程中执行：

```bash
cd study-gray-flow-ui
npm install
npm run test:e2e
```

测试通过后才能合并代码。