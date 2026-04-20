---
name: "frontend-validation"
description: "提供前端页面验证的标准规范，使用Selenium进行端到端测试，确保页面功能和样式的完整性。Invoke when validating frontend pages or implementing testing strategies."
---

# 前端页面验证规范

## 1. 验证工具

### 1.1 Selenium WebDriver
- **用途**：端到端测试工具，用于验证前端页面的功能和样式
- **安装**：`npm install --save-dev selenium-webdriver`
- **配置**：通过`selenium-config.js`配置测试环境和参数
- **浏览器驱动**：需要下载Chrome/Firefox/Edge浏览器驱动

### 1.2 测试环境
- **开发服务器**：使用`npm run dev`启动本地开发服务器
- **测试浏览器**：支持Chrome(Chromium)、Firefox、Edge
- **Base URL**：默认设置为`http://localhost:5173`

## 2. 测试目录结构

```
frontend/web/
├── tests/                      # 测试文件目录
│   ├── selenium-config.js      # Selenium配置和测试运行器
│   ├── run-all-tests.js        # 运行所有测试
│   ├── auth/                   # 认证相关测试
│   │   └── selenium-auth-test.js
│   ├── family/                 # 家族管理相关测试
│   │   └── selenium-family-test.js
│   ├── member/                 # 成员管理相关测试
│   │   └── selenium-member-test.js
│   └── pages/                  # 其他页面测试
│       └── selenium-pages-test.js
└── package.json                # 项目配置文件
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
  - [ ] 管理员信息显示
  - [ ] 更改管理员按钮可见（仅管理员可见）
  - [ ] 更改管理员弹窗功能正常
  - [ ] 管理员权限验证正常

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
node tests/run-all-tests.js

# 运行特定测试文件
node tests/family/selenium-family-test.js

# 运行单个测试函数
node -e "import('./tests/family/selenium-family-test.js').then(m => m.testUpdateAdministratorButton())"
```

#### 4.1.2 开发服务器
```bash
# 启动前端开发服务器（测试前需要先启动）
cd frontend/web
npm run dev
```

### 4.2 测试报告
- 测试完成后在控制台输出测试结果
- 显示通过/失败统计
- 截图记录失败场景（如配置了截图功能）

## 5. Selenium测试运行器

### 5.1 SeleniumTestRunner 类
```javascript
class SeleniumTestRunner {
  constructor(testName, headless = true)
  async setup()
  async navigateTo(path)
  async waitAndFindElement(By)
  async fillInput(By, value)
  async clickButton(By)
  async getCurrentUrl()
  async sleep(ms)
  pass(message)
  fail(message)
  info(message)
  async teardown()
  printSummary()
}
```

### 5.2 By定位器
```javascript
By.id('element-id')
By.css('.class-name')
By.xpath("//button[contains(text(),'text')]")
By.name('element-name')
By.tagName('tag-name')
```

## 6. 测试最佳实践

### 6.1 测试用例设计
- **独立测试**：每个测试用例应独立执行，不依赖其他测试的状态
- **明确断言**：每个测试用例应有明确的断言，验证页面功能和样式
- **覆盖场景**：测试应覆盖正常场景和异常场景
- **合理等待**：使用`sleep`、`waitAndFindElement`等方法确保页面加载完成

### 6.2 测试性能
- **并行限制**：避免同时运行过多测试实例
- **合理等待**：使用适当的等待时间而非固定延迟
- **资源管理**：测试完成后确保关闭浏览器实例

### 6.3 测试维护
- **定期更新**：随着页面功能变更，及时更新测试用例
- **代码审查**：测试代码应与业务代码一样进行代码审查
- **文档同步**：测试用例应与页面功能文档保持同步

## 7. 示例测试用例

### 7.1 登录页面测试
```javascript
async function testLoginPage() {
  const runner = new SeleniumTestRunner('登录页面测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/login');
    
    // 检查登录表单元素
    await runner.waitAndFindElement(By.id('email'));
    await runner.waitAndFindElement(By.id('password'));
    await runner.clickButton(By.css('button[type="submit"]'));
    
    runner.pass('登录页面加载正常');
  } catch (error) {
    runner.fail(`登录页面加载失败: ${error.message}`);
  } finally {
    await runner.teardown();
    runner.printSummary();
  }
}
```

### 7.2 家族管理员功能测试
```javascript
async function testUpdateAdministratorButton() {
  const runner = new SeleniumTestRunner('更改管理员按钮测试');
  await runner.setup(false);

  try {
    // 登录
    await runner.navigateTo('/login');
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到家族管理页面
    await runner.navigateTo('/family-management');
    await runner.sleep(3000);

    // 检查更改管理员按钮
    const updateAdminButton = await runner.driver.findElement(
      By.xpath("//button[contains(text(),'更改管理员')]")
    );
    
    if (await updateAdminButton.isDisplayed()) {
      runner.pass('更改管理员按钮存在并显示');
    }
  } catch (error) {
    runner.fail(`测试失败: ${error.message}`);
  } finally {
    await runner.teardown();
    runner.printSummary();
  }
}
```

### 7.3 管理员权限验证测试
```javascript
async function testAdministratorPermission() {
  const runner = new SeleniumTestRunner('管理员权限验证测试');
  await runner.setup(false);

  try {
    // 登录
    await runner.navigateTo('/login');
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到家族管理页面
    await runner.navigateTo('/family-management');
    await runner.sleep(3000);

    // 管理员应该能看到更改管理员按钮
    const changeAdminButton = await runner.driver.findElement(
      By.xpath("//button[contains(text(),'更改管理员')]")
    );
    
    if (await changeAdminButton.isDisplayed()) {
      runner.pass('管理员可以看到更改管理员按钮');
    }
  } catch (error) {
    runner.fail(`权限验证失败: ${error.message}`);
  } finally {
    await runner.teardown();
    runner.printSummary();
  }
}
```

## 8. 家族管理员功能测试要点

### 8.1 管理员信息显示
- [ ] 管理员名称正确显示
- [ ] 管理员标识清晰可见

### 8.2 更改管理员功能
- [ ] 更改管理员按钮仅对当前管理员可见
- [ ] 点击按钮打开模态框
- [ ] 模态框包含成员选择器
- [ ] 选择新管理员后保存成功
- [ ] 保存后管理员信息更新

### 8.3 权限控制
- [ ] 非管理员用户看不到更改管理员按钮
- [ ] 非管理员用户无法通过API直接更改管理员

## 9. 总结

通过使用Selenium进行端到端测试，可以确保前端页面的功能和样式完整性，提高代码质量和用户体验。测试应覆盖所有关键页面和功能，确保页面在不同浏览器和设备上的一致性。

定期运行测试可以及时发现和解决问题，确保前端页面的稳定性和可靠性。同时，测试代码应与业务代码一样进行维护和更新，确保测试的有效性和准确性。