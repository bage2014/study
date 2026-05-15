# 前端测试规则

## 一、测试原则

1. **测试先行**：所有前端功能开发必须先编写 Playwright 测试脚本，再实现功能代码
2. **覆盖核心流程**：登录、用户管理、轨迹追踪等核心功能必须 100% 覆盖
3. **自动化验证**：代码提交前必须通过所有 Playwright UI 模式测试 且在IDE 内置浏览器中进行验证
4. **报告生成**：每次测试执行后自动生成 HTML 测试报告
5. **UI 模式验证**：支持在 IDE 内置浏览器中进行可视化验证，确保测试结果与实际用户体验一致

## 二、测试环境配置

### 1. 安装依赖

```bash
# 安装 Playwright 测试框架
npm install -D @playwright/test

# 安装浏览器（首次运行）
npx playwright install
```

### 2. 配置文件

**playwright.config.js**：

```javascript
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: 'html',
  use: {
    baseURL: 'http://localhost:5173',
    trace: 'on-first-retry',
    headless: false,
    viewport: { width: 1280, height: 720 },
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
});
```

### 3. Playwright UI 模式配置

Playwright 支持 UI 模式进行交互式测试验证：

```bash
# 启动 UI 模式（交互式测试）
npx playwright test --ui

# 指定测试文件启动 UI 模式
npx playwright test tests/login.spec.js --ui

# 指定浏览器启动 UI 模式
npx playwright test --ui --project=chromium
```

**UI 模式功能**：
- 实时预览测试执行过程
- 支持断点调试
- 可视化选择器定位
- 交互式查看测试结果
- 支持单步执行测试用例

### 3. package.json 脚本

```json
{
  "scripts": {
    "test": "npx playwright test --reporter=html",
    "test:chrome": "npx playwright test --project=chromium",
    "test:report": "npx playwright show-report"
  }
}
```

## 三、测试文件结构

```
frontend/
├── tests/                      # Playwright 测试目录
│   ├── login.spec.js           # 登录功能测试
│   ├── user-management.spec.js # 用户管理功能测试
│   ├── track.spec.js           # 轨迹追踪功能测试
│   └── [功能].spec.js          # 其他功能测试
├── playwright.config.js        # Playwright 配置文件
└── playwright-report/          # 测试报告输出目录（自动生成）
```

## 四、测试脚本编写规范

### 1. 文件命名规范

| 测试类型 | 命名规则 | 示例 |
|----------|----------|------|
| 登录模块 | `login.spec.js` | 测试登录、退出、登录态持久化 |
| 用户管理 | `user-management.spec.js` | 测试用户增删改查 |
| 轨迹模块 | `track.spec.js` | 测试轨迹展示、添加 |

### 2. 测试用例结构

```javascript
import { test, expect } from '@playwright/test';

test.describe('功能模块名称', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');
  });

  test('测试场景描述', async ({ page }) => {
    // 步骤1：操作
    // 步骤2：断言验证
    // 步骤3：清理（如需要）
  });
});
```

### 3. 选择器规范

| 选择器类型 | 推荐方式 | 示例 |
|------------|----------|------|
| 按钮 | `.btn-class` 或 `button:has-text('文本')` | `page.locator('.login-btn')` |
| 输入框 | `input[placeholder='提示文字']` | `page.locator('input[placeholder="用户名"]')` |
| 模态框 | `.modal-class` | `page.locator('.auth-modal')` |
| 表格 | `table` 或 `.table-class` | `page.locator('table')` |

### 4. 常用断言

```javascript
// 元素可见性
await expect(page.locator('.element')).toBeVisible();

// 元素文本
await expect(page.locator('.title')).toHaveText('用户管理');

// 页面跳转
await expect(page).toHaveURL('http://localhost:5173/users');

// 元素计数
await expect(page.locator('table tr')).toHaveCount(3);
```

## 五、测试覆盖要求

### 1. 登录模块

| 测试场景 | 描述 |
|----------|------|
| 登录成功 | 输入正确用户名密码登录 |
| 登录失败 | 输入错误密码 |
| 登录态持久化 | 刷新页面保持登录状态 |
| 退出登录 | 退出后返回未登录状态 |

### 2. 用户管理模块

| 测试场景 | 描述 |
|----------|------|
| 用户列表展示 | 登录后查看用户列表 |
| 添加用户 | 新增用户成功 |
| 编辑用户 | 修改用户信息 |
| 删除用户 | 删除用户确认 |

### 3. 轨迹模块

| 测试场景 | 描述 |
|----------|------|
| 轨迹展示 | 查看用户轨迹列表 |
| 添加轨迹 | 新增轨迹点 |
| 删除轨迹 | 删除轨迹 |

## 六、测试执行流程

### 1. 启动前端服务

```bash
npm run dev
```

### 2. 运行测试

```bash
# 运行所有测试并生成报告
npm test

# 运行指定测试文件
npx playwright test tests/login.spec.js

# 仅运行 Chromium 测试
npx playwright test --project=chromium
```

### 3. 查看测试报告

```bash
npx playwright show-report
```

## 七、测试报告规范

### 1. 报告结构

```
docs/reports/
├── TEST_{日期}.md      # 测试报告
└── DAILY_{日期}.md     # 每日进度报告
```

### 2. 测试报告模板

#### 2.1 报告信息
- **报告日期**：YYYY-MM-DD
- **测试版本**：v1.0.0
- **测试环境**：开发环境/测试环境
- **测试范围**：登录模块、用户管理模块

#### 2.2 测试执行统计

| 测试类型 | 总数 | 通过 | 失败 | 通过率 |
|----------|------|------|------|--------|
| 单元测试 | 20 | 18 | 2 | 90% |
| 接口测试 | 15 | 15 | 0 | 100% |
| UI 测试 | 8 | 7 | 1 | 87.5% |
| **总计** | **43** | **40** | **3** | **93%** |

#### 2.3 测试用例详情

| 测试用例 | 模块 | 结果 | 备注 |
|----------|------|------|------|
| `登录成功` | Login | ✅ 通过 | - |
| `登录态持久化` | Login | ✅ 通过 | - |
| `添加用户` | UserManagement | ❌ 失败 | 模态框选择器定位失败 |

#### 2.4 缺陷记录

| 序号 | 缺陷描述 | 优先级 | 状态 | 关联测试 |
|------|----------|--------|------|----------|
| 1 | 添加用户模态框定位失败 | 中 | 待修复 | `添加用户` |

#### 2.5 测试结论
- 本次测试共执行 X 个测试用例，通过 Y 个，失败 Z 个
- 整体通过率 X%
- 建议修复高优先级缺陷后再进行回归测试

### 3. 每日进度报告模板

#### 3.1 日期：YYYY-MM-DD

#### 3.2 今日完成

| 任务 | 状态 | 备注 |
|------|------|------|
| 登录功能开发 | ✅ 完成 | - |
| 用户管理 UI 开发 | ✅ 完成 | - |

#### 3.3 待完成任务

| 任务 | 优先级 | 预估时间 |
|------|--------|----------|
| 轨迹模块开发 | 高 | 2天 |
| 单元测试补全 | 中 | 1天 |

#### 3.4 阻塞问题
- 暂无

## 八、命名规范

| 类型 | 格式 | 示例 |
|------|------|------|
| 测试文件 | `{功能}.spec.js` | `login.spec.js` |
| 测试报告 | `TEST_{日期}.md` | `TEST_2026-05-12.md` |
| 进度报告 | `DAILY_{日期}.md` | `DAILY_2026-05-12.md` |

## 九、违规处理

- 未编写测试脚本：拒绝代码提交
- 测试失败：拒绝代码提交
- 核心流程未覆盖：拒绝代码合并
- 未生成测试报告：拒绝代码合并