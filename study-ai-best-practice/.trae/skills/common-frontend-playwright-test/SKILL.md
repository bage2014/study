---
name: "common-frontend-playwright-test"
description: "前端Playwright测试技能。提供前端UI自动化测试的指导和生成能力。"
---

# 前端 Playwright 测试技能

## 功能描述

提供前端 UI 自动化测试能力，基于 Playwright 框架，帮助开发者编写高质量的端到端测试用例。

## 何时使用

在以下情况调用此技能：
- 需要编写前端 UI 自动化测试时
- 需要测试用户交互流程时
- 需要验证页面功能正确性时
- 需要生成测试报告时

## 核心功能

- **测试生成**：根据页面结构生成测试用例
- **定位器生成**：生成元素定位器
- **测试审查**：检查测试代码质量
- **测试优化**：提供测试优化建议
- **报告生成**：生成测试报告和截图

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageUrl | String | 是 | 目标页面 URL |
| testType | String | 否 | 测试类型（功能测试/视觉测试/A11y测试） |
| browser | String | 否 | 目标浏览器（chromium/firefox/webkit） |
| viewport | String | 否 | 视口大小（desktop/mobile/tablet） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "pageUrl": "https://example.com/login",
  "browser": "chromium",
  "testCode": "生成的测试代码",
  "testCases": [
    {
      "name": "登录页面测试",
      "steps": ["步骤1", "步骤2", "步骤3"],
      "expectedResult": "期望结果"
    }
  ],
  "locators": [
    {
      "name": "用户名输入框",
      "locator": "input[name='username']",
      "type": "text"
    }
  ],
  "suggestions": ["优化建议1", "优化建议2"]
}
```

## 测试流程

```
页面分析 → 定位器识别 → 测试设计 → 测试编写 → 测试执行 → 结果验证 → 报告生成
```

### 详细步骤

1. **页面分析**：分析页面结构和元素
2. **定位器识别**：识别页面元素的定位方式
3. **测试设计**：设计测试用例和步骤
4. **测试编写**：编写 Playwright 测试代码
5. **测试执行**：在指定浏览器中运行测试
6. **结果验证**：验证测试结果是否符合预期
7. **报告生成**：生成测试报告和截图

## 测试质量检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 定位器稳定 | 元素定位器是否稳定可靠 | ✅/❌ |
| 等待策略 | 是否正确使用等待机制 | ✅/❌ |
| 断言明确 | 断言是否清晰明确 | ✅/❌ |
| 测试独立 | 测试用例是否相互独立 | ✅/❌ |
| 异常处理 | 是否处理异常情况 | ✅/❌ |
| 截图验证 | 是否有必要的截图验证 | ✅/❌ |
| 性能考虑 | 是否考虑测试性能 | ✅/❌ |
| 文档完整 | 是否有必要的注释说明 | ✅/❌ |

## 最佳实践指南

### Playwright 测试规范

```typescript
import { test, expect } from '@playwright/test';

test.describe('登录页面', () => {
  test('成功登录', async ({ page }) => {
    // 导航到登录页面
    await page.goto('/login');
    
    // 填写表单
    await page.locator('input[name="username"]').fill('test@example.com');
    await page.locator('input[name="password"]').fill('password123');
    
    // 点击登录按钮
    await page.locator('button[type="submit"]').click();
    
    // 验证跳转
    await expect(page).toHaveURL('/dashboard');
    await expect(page.locator('h1')).toHaveText('欢迎回来');
  });

  test('密码错误时显示错误消息', async ({ page }) => {
    await page.goto('/login');
    
    await page.locator('input[name="username"]').fill('test@example.com');
    await page.locator('input[name="password"]').fill('wrongpassword');
    await page.locator('button[type="submit"]').click();
    
    // 验证错误消息
    await expect(page.locator('.error-message')).toHaveText('用户名或密码错误');
  });
});
```

### 定位器最佳实践

| 优先级 | 定位方式 | 示例 | 说明 |
|--------|----------|------|------|
| 1 | role + name | `page.getByRole('button', { name: '登录' })` | 最稳定的定位方式 |
| 2 | data-testid | `page.locator('[data-testid="login-button"]')` | 推荐的测试属性 |
| 3 | label | `page.getByLabel('用户名')` | 基于表单标签定位 |
| 4 | placeholder | `page.getByPlaceholder('请输入邮箱')` | 基于占位符定位 |
| 5 | text | `page.getByText('提交')` | 基于文本定位 |
| 6 | css | `page.locator('button[type="submit"]')` | CSS选择器 |

### 等待策略

```typescript
// 等待元素可见
await page.locator('button').waitFor({ state: 'visible' });

// 等待网络空闲
await page.waitForLoadState('networkidle');

// 等待特定请求完成
const [response] = await Promise.all([
  page.waitForResponse('**/api/login'),
  page.locator('button').click()
]);
```

## 测试模式

### 视觉测试

```typescript
test('页面视觉测试', async ({ page }) => {
  await page.goto('/dashboard');
  await expect(page).toHaveScreenshot('dashboard.png', {
    fullPage: true,
    maxDiffPixels: 100
  });
});
```

### 可访问性测试

```typescript
import { AxeBuilder } from '@axe-core/playwright';

test('页面可访问性测试', async ({ page }) => {
  await page.goto('/login');
  const accessibilityScanResults = await new AxeBuilder({ page }).analyze();
  expect(accessibilityScanResults.violations).toEqual([]);
});
```

## 核心组件

| 组件 | 职责 | 描述 |
|------|------|------|
| TestGenerator | 测试生成器 | 根据页面生成测试代码 |
| LocatorGenerator | 定位器生成器 | 生成元素定位器 |
| TestRunner | 测试运行器 | 执行测试用例 |
| ReportGenerator | 报告生成器 | 生成测试报告 |

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| PLAYWRIGHT_BROWSER | 默认浏览器 | chromium |
| PLAYWRIGHT_TIMEOUT | 测试超时时间 | 30000 |

### 配置文件

```yaml
playwright:
  browser: chromium
  timeout: 30000
  viewport:
    width: 1920
    height: 1080
  trace:
    enabled: true
    output-dir: test-results/traces
```

## 扩展指南

### 添加新测试类型支持

1. 创建对应测试类型的生成器
2. 定义测试模板和规范
3. 添加测试执行逻辑

### 添加自定义定位策略

1. 在定位器生成器中添加新策略
2. 定义策略的优先级和匹配规则
3. 更新定位器最佳实践文档