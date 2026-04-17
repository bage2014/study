---
name: "frontend-validation"
description: "提供前端页面验证的标准规范，使用Playwright进行端到端测试，确保页面功能和样式的完整性。Invoke when validating frontend pages or implementing testing strategies."
---

# 前端页面验证规范

## 1. 验证工具

### 1.1 Playwright
- **用途**：端到端测试工具，用于验证前端页面的功能和样式
- **安装**：`npm install --save-dev @playwright/test`
- **配置**：通过`playwright.config.js`配置测试环境和参数

### 1.2 测试环境
- **开发服务器**：使用`npm run dev`启动本地开发服务器
- **测试浏览器**：支持Chromium、Firefox、WebKit
- **Base URL**：默认设置为`http://localhost:5173`

## 2. 测试目录结构

```
frontend/web/
├── tests/              # 测试文件目录
│   ├── auth.spec.js    # 认证相关测试
│   ├── family.spec.js  # 家族管理相关测试
│   ├── member.spec.js  # 成员管理相关测试
│   └── ...             # 其他测试文件
├── playwright.config.js # Playwright配置文件
└── package.json        # 项目配置文件
```

## 3. 页面验证要点

### 3.1 认证页面

#### 3.1.1 登录页面
- [ ] 页面标题正确显示
- [ ] 登录表单元素完整（邮箱输入框、密码输入框、登录按钮）
- [ ] 表单验证功能正常
- [ ] 登录成功后跳转到首页
- [ ] 登录失败时显示错误信息

#### 3.1.2 注册页面
- [ ] 页面标题正确显示
- [ ] 注册表单元素完整（邮箱输入框、密码输入框、确认密码输入框、昵称输入框、注册按钮）
- [ ] 密码一致性验证功能正常
- [ ] 注册成功后跳转到登录页
- [ ] 注册失败时显示错误信息

### 3.2 核心功能页面

#### 3.2.1 首页
- [ ] 页面标题正确显示
- [ ] 欢迎信息显示
- [ ] 功能卡片完整（家族管理、成员管理、家族树、成员大事件、家族地理位置、AI关系分析）
- [ ] 导航功能正常

#### 3.2.2 家族管理页面
- [ ] 页面标题正确显示
- [ ] 家族列表显示
- [ ] 添加/编辑家族功能正常
- [ ] 家族删除功能正常
- [ ] 管理员维护功能正常

#### 3.2.3 成员管理页面
- [ ] 页面标题正确显示
- [ ] 成员列表显示
- [ ] 添加/编辑成员功能正常
- [ ] 成员删除功能正常
- [ ] 成员搜索功能正常

#### 3.2.4 家族树页面
- [ ] 页面标题正确显示
- [ ] 家族选择器功能正常
- [ ] 家族树渲染正常
- [ ] 成员节点交互功能正常

#### 3.2.5 成员大事件页面
- [ ] 页面标题正确显示
- [ ] 成员选择器功能正常
- [ ] 大事件列表显示
- [ ] 添加/编辑大事件功能正常
- [ ] 大事件删除功能正常

#### 3.2.6 家族地理位置页面
- [ ] 页面标题正确显示
- [ ] 家族选择器功能正常
- [ ] 成员列表显示
- [ ] 位置地图显示
- [ ] 添加/编辑位置功能正常

#### 3.2.7 AI关系分析页面
- [ ] 页面标题正确显示
- [ ] 家族选择器功能正常
- [ ] 分析按钮功能正常
- [ ] 分析结果显示

#### 3.2.8 操作日志页面
- [ ] 页面标题正确显示
- [ ] 操作类型筛选功能正常
- [ ] 时间范围筛选功能正常
- [ ] 日志列表显示

#### 3.2.9 家族故事页面
- [ ] 页面标题正确显示
- [ ] 故事类型选择功能正常
- [ ] 家族/成员选择功能正常
- [ ] 生成故事功能正常
- [ ] 故事内容显示

### 3.3 样式验证要点

#### 3.3.1 导航条
- [ ] 背景颜色正确
- [ ] 标题显示正常
- [ ] 返回按钮功能正常
- [ ] 用户信息显示正常
- [ ] 退出登录按钮功能正常

#### 3.3.2 按钮
- [ ] 主要按钮样式正确
- [ ] 成功按钮样式正确
- [ ] 危险按钮样式正确
- [ ] 次要按钮样式正确
- [ ] 按钮悬停效果正常

#### 3.3.3 表格
- [ ] 表格布局正确
- [ ] 表头样式正确
- [ ] 表体样式正确
- [ ] 行悬停效果正常

#### 3.3.4 输入框和选择框
- [ ] 输入框样式正确
- [ ] 选择框样式正确
- [ ] 标签样式正确
- [ ] 聚焦效果正常
- [ ] 错误状态样式正确

#### 3.3.5 卡片
- [ ] 卡片样式正确
- [ ] 卡片悬停效果正常
- [ ] 卡片内容布局合理

#### 3.3.6 模态框
- [ ] 模态框布局正确
- [ ] 遮罩效果正常
- [ ] 标题样式正确
- [ ] 按钮布局合理

## 4. 测试执行

### 4.1 运行测试

#### 4.1.1 命令行测试
```bash
# 运行所有测试
npm test

# 运行指定测试文件
npm test tests/auth.spec.js

# 运行特定测试
npm test -- -g "login page"
```

#### 4.1.2 UI测试
```bash
# 启动Playwright UI
npm run test:ui
```

### 4.2 测试报告
- 测试完成后生成HTML报告
- 报告路径：`frontend/web/playwright-report/index.html`
- 报告包含测试结果、截图和视频

## 5. 测试最佳实践

### 5.1 测试用例设计
- **独立测试**：每个测试用例应独立执行，不依赖其他测试的状态
- **明确断言**：每个测试用例应有明确的断言，验证页面功能和样式
- **覆盖场景**：测试应覆盖正常场景和异常场景
- **合理等待**：使用`waitForURL`、`waitForSelector`等方法确保页面加载完成

### 5.2 测试性能
- **并行执行**：使用`fullyParallel: true`配置并行执行测试
- **合理重试**：在CI环境中设置适当的重试次数
- **资源管理**：测试完成后清理测试数据和状态

### 5.3 测试维护
- **定期更新**：随着页面功能变更，及时更新测试用例
- **代码审查**：测试代码应与业务代码一样进行代码审查
- **文档同步**：测试用例应与页面功能文档保持同步

## 6. 示例测试用例

### 6.1 登录页面测试
```javascript
test('login page has form', async ({ page }) => {
  await page.goto('/');
  
  // Expect login form elements to be present
  await expect(page.locator('input[type="email"]')).toBeVisible();
  await expect(page.locator('input[type="password"]')).toBeVisible();
  await expect(page.locator('button[type="submit"]')).toBeVisible();
});

test('login functionality', async ({ page }) => {
  await page.goto('/');
  
  // Fill in login form
  await page.fill('input[type="email"]', 'bage@qq.com');
  await page.fill('input[type="password"]', 'bage1234');
  
  // Submit form
  await page.click('button[type="submit"]');
  
  // Wait for navigation to home page
  await page.waitForURL('/home');
  
  // Expect home page elements to be present
  await expect(page.locator('h2')).toContainText('欢迎回来');
});
```

### 6.2 注册页面测试
```javascript
test('register page has form', async ({ page }) => {
  await page.goto('/register');
  
  // Expect register form elements to be present
  await expect(page.locator('input[type="email"]')).toBeVisible();
  await expect(page.locator('input[type="password"]')).toBeVisible();
  await expect(page.locator('input[type="password"]').nth(1)).toBeVisible(); // Confirm password
  await expect(page.locator('input[type="text"]')).toBeVisible(); // Nickname
  await expect(page.locator('button[type="submit"]')).toBeVisible();
});

test('password confirmation validation', async ({ page }) => {
  await page.goto('/register');
  
  // Fill in form with mismatched passwords
  await page.fill('input[type="email"]', 'test@example.com');
  await page.fill('input[type="password"]', 'password123');
  await page.fill('input[type="password"]', 'password456');
  await page.fill('input[type="text"]', 'Test User');
  
  // Submit form
  await page.click('button[type="submit"]');
  
  // Expect error message
  await expect(page.locator('p:text("两次输入的密码不一致")')).toBeVisible();
});
```

### 6.3 家族管理页面测试
```javascript
test('family management page has family list', async ({ page }) => {
  // Login first
  await page.goto('/');
  await page.fill('input[type="email"]', 'bage@qq.com');
  await page.fill('input[type="password"]', 'bage1234');
  await page.click('button[type="submit"]');
  await page.waitForURL('/home');
  
  // Navigate to family management page
  await page.click('a[href="/family-management"]');
  await page.waitForURL('/family-management');
  
  // Expect family list to be present
  await expect(page.locator('table')).toBeVisible();
  await expect(page.locator('th')).toHaveCount(4); // Name, Description, Members, Actions
});

test('family administrator functionality', async ({ page }) => {
  // Login first
  await page.goto('/');
  await page.fill('input[type="email"]', 'bage@qq.com');
  await page.fill('input[type="password"]', 'bage1234');
  await page.click('button[type="submit"]');
  await page.waitForURL('/home');
  
  // Navigate to family management page
  await page.click('a[href="/family-management"]');
  await page.waitForURL('/family-management');
  
  // Select a family
  await page.click('tr:nth-child(2) td:nth-child(4) button:first-child');
  
  // Expect administrator section to be present
  await expect(page.locator('p:text("管理员:")')).toBeVisible();
  await expect(page.locator('button:text("更改管理员")')).toBeVisible();
});
```

## 7. 总结

通过使用Playwright进行端到端测试，可以确保前端页面的功能和样式完整性，提高代码质量和用户体验。测试应覆盖所有关键页面和功能，确保页面在不同浏览器和设备上的一致性。

定期运行测试可以及时发现和解决问题，确保前端页面的稳定性和可靠性。同时，测试代码应与业务代码一样进行维护和更新，确保测试的有效性和准确性。
