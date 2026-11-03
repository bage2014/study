---
name: "common-visual-regression-test"
description: "视觉回归测试技能，提供UI截图对比、样式检查、视觉差异检测，确保UI变更符合预期且不破坏现有样式"
trigger: "UI样式修改、前端组件更新、主题变更、响应式适配验证时"
disable-when: "纯后端变更、无UI修改时"
category: "common"
tags: ["visual-testing", "regression", "screenshot", "ui", "css", "frontend"]
requires: ["common-frontend-playwright-test", "personal-frontend-coding-standard"]
---

# common-visual-regression-test

## 功能描述

视觉回归测试技能，通过自动化截图对比和视觉差异检测，确保 UI 变更符合预期且不破坏现有样式。在每次前端代码变更后，自动对比新旧截图，发现意外的视觉变化，防止样式回归问题。

## 触发条件

- CSS/样式文件修改
- 前端组件更新或重构
- 主题/配色方案变更
- 响应式布局调整
- UI 库升级（如 Ant Design、Element Plus）
- 浏览器兼容性修复

## 何时使用

- 前端代码提交后的自动化测试
- UI 重构后的视觉验证
- 多浏览器/多设备适配检查
- 主题切换功能验证
- 设计稿与实现对比
- 上线前的最终视觉检查

## 何时不使用

- 纯后端代码变更
- API 接口修改（无 UI 影响）
- 数据库结构变更
- 业务逻辑修改（不涉及样式）

## 核心工具

### 1. Playwright 截图对比

#### 1.1 安装配置

```bash
# 安装 Playwright 和截图对比工具
npm install -D @playwright/test pixelmatch pngjs

# 或使用 BackstopJS（专业视觉回归工具）
npm install -D backstopjs
```

#### 1.2 Playwright 配置

```javascript
// playwright.config.js
module.exports = {
  use: {
    screenshot: 'on',
    // 截图对比阈值
    expect: {
      toHaveScreenshot: {
        maxDiffPixels: 100,  // 最多允许 100 像素差异
        threshold: 0.2,      // 颜色容差
      },
    },
  },
};
```

#### 1.3 基础截图测试

```javascript
// tests/visual-regression.spec.js
const { test, expect } = require('@playwright/test');

test.describe('视觉回归测试', () => {
  
  test('首页视觉检查', async ({ page }) => {
    await page.goto('http://localhost:3000');
    
    // 全页面截图对比
    await expect(page).toHaveScreenshot('homepage.png', {
      maxDiffPixels: 100,
    });
  });

  test('登录页视觉检查', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    
    // 全页面截图对比
    await expect(page).toHaveScreenshot('login-page.png');
  });

  test('按钮组件视觉检查', async ({ page }) => {
    await page.goto('http://localhost:3000/components');
    
    // 指定元素截图
    const button = page.locator('.btn-primary');
    await expect(button).toHaveScreenshot('primary-button.png');
  });
});
```

### 2. BackstopJS 配置

#### 2.1 初始化配置

```javascript
// backstop.json
{
  "id": "visual_regression",
  "viewports": [
    {
      "label": "phone",
      "width": 375,
      "height": 667
    },
    {
      "label": "tablet",
      "width": 768,
      "height": 1024
    },
    {
      "label": "desktop",
      "width": 1920,
      "height": 1080
    }
  ],
  "onBeforeScript": "puppeteer/onBefore.js",
  "onReadyScript": "puppeteer/onReady.js",
  "scenarios": [
    {
      "label": "首页",
      "cookiePath": "",
      "url": "http://localhost:3000",
      "referenceUrl": "",
      "readyEvent": "",
      "readySelector": "",
      "delay": 500,
      "hideSelectors": [],
      "removeSelectors": [],
      "hoverSelector": "",
      "clickSelector": "",
      "postInteractionWait": 0,
      "selectors": [],
      "selectorExpansion": true,
      "expect": 0,
      "misMatchThreshold": 0.1
    },
    {
      "label": "登录页",
      "url": "http://localhost:3000/login",
      "delay": 500,
      "misMatchThreshold": 0.1
    }
  ],
  "paths": {
    "bitmaps_reference": "backstop_data/bitmaps_reference",
    "bitmaps_test": "backstop_data/bitmaps_test",
    "engine_scripts": "backstop_data/engine_scripts",
    "html_report": "backstop_data/html_report",
    "ci_report": "backstop_data/ci_report"
  },
  "report": ["browser", "CI"],
  "engine": "puppeteer",
  "engineOptions": {
    "args": ["--no-sandbox"]
  },
  "asyncCaptureLimit": 5,
  "asyncCompareLimit": 50,
  "debug": false,
  "debugWindow": false
}
```

#### 2.2 运行测试

```bash
# 生成参考截图（基线）
npx backstop reference

# 运行视觉回归测试
npx backstop test

# 查看报告
npx backstop openReport

# 批准变更（更新基线）
npx backstop approve

# CI 模式运行
npx backstop test --config=backstop.json
```

### 3. Percy 视觉测试

#### 3.1 安装配置

```bash
# 安装 Percy CLI
npm install -D @percy/cli @percy/playwright

# 配置 Percy
# .percy.yml
version: 2
snapshot:
  widths:
    - 375
    - 768
    - 1920
  minHeight: 1024
  percyCSS: ""
discovery:
  networkIdleTimeout: 100
upload:
  files: "**/*.{html,htm}"
  ignore: "**/node_modules/**"
```

#### 3.2 Playwright 集成

```javascript
const { test, expect } = require('@playwright/test');
const percySnapshot = require('@percy/playwright');

test.describe('视觉回归测试 with Percy', () => {
  
  test('首页快照', async ({ page }) => {
    await page.goto('http://localhost:3000');
    
    // 等待页面完全加载
    await page.waitForLoadState('networkidle');
    
    // 发送快照到 Percy
    await percySnapshot(page, '首页');
  });

  test('登录页快照', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.waitForLoadState('networkidle');
    
    await percySnapshot(page, '登录页');
  });

  test('响应式检查', async ({ page }) => {
    // 移动端
    await page.setViewportSize({ width: 375, height: 667 });
    await page.goto('http://localhost:3000');
    await percySnapshot(page, '首页-移动端');
    
    // 平板
    await page.setViewportSize({ width: 768, height: 1024 });
    await percySnapshot(page, '首页-平板');
    
    // 桌面端
    await page.setViewportSize({ width: 1920, height: 1080 });
    await percySnapshot(page, '首页-桌面端');
  });
});
```

#### 3.3 运行 Percy

```bash
# 设置 Percy Token
export PERCY_TOKEN=your_token_here

# 运行测试并上传快照
npx percy exec -- npm run test

# 或使用 Playwright
npx percy exec -- npx playwright test
```

## 视觉测试流程

### 1. 建立基线

```bash
# 1. 确保当前版本 UI 正确
git checkout main

# 2. 生成基线截图
npx backstop reference
# 或
npx playwright test --update-snapshots

# 3. 提交基线到版本控制
git add backstop_data/bitmaps_reference/
git add tests/snapshots/
git commit -m "chore: update visual baseline"
```

### 2. 开发阶段测试

```bash
# 1. 创建功能分支
git checkout -b feature/ui-update

# 2. 修改 UI 代码
# ...

# 3. 运行视觉回归测试
npx backstop test
# 或
npx playwright test tests/visual-regression.spec.js

# 4. 查看差异报告
npx backstop openReport
```

### 3. 差异分析

```markdown
## 视觉差异分析报告

### 测试时间：2026-11-03 15:30
### 测试环境：Chrome 120, 1920x1080

### 差异汇总
- 总测试页面：10
- 通过：8
- 失败：2
- 差异率：20%

### 失败页面分析

#### 1. 首页（差异率：2.3%）
- **差异位置**：顶部导航栏
- **差异描述**：Logo 间距从 20px 变为 25px
- **原因分析**：CSS 修改导致
- **是否预期**：✅ 是（本次需求要求调整间距）
- **处理方案**：批准变更，更新基线

#### 2. 登录页（差异率：15.7%）
- **差异位置**：整个表单区域
- **差异描述**：输入框高度从 40px 变为 30px
- **原因分析**：意外修改了全局样式
- **是否预期**：❌ 否（这是 Bug）
- **处理方案**：回滚修改，修复 Bug
```

### 4. 批准或修复

```bash
# 如果差异是预期的（设计变更）
npx backstop approve
# 或
npx playwright test --update-snapshots

# 如果差异是非预期的（Bug）
# 1. 修复代码
# 2. 重新运行测试
npx backstop test

# 3. 提交代码和更新的基线
git add .
git commit -m "fix: resolve visual regression issues"
```

### 5. CI/CD 集成

```yaml
# .github/workflows/visual-regression.yml
name: Visual Regression Tests

on:
  pull_request:
    branches: [main]
    paths:
      - 'src/**/*.{css,scss,less}'
      - 'src/**/*.vue'
      - 'src/**/*.tsx'
      - 'src/**/*.jsx'

jobs:
  visual-test:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      
      - name: Install dependencies
        run: npm ci
      
      - name: Build project
        run: npm run build
      
      - name: Start server
        run: npm run dev &
      
      - name: Wait for server
        run: npx wait-on http://localhost:3000
      
      - name: Run visual tests
        run: npx playwright test tests/visual-regression.spec.js
      
      - name: Upload test results
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: visual-test-results
          path: |
            test-results/
            playwright-report/
      
      - name: Check for visual changes
        run: |
          if [ -d "test-results/failed" ]; then
            echo "Visual differences detected!"
            exit 1
          fi
```

## 多浏览器/设备测试

### 1. 跨浏览器测试

```javascript
// tests/cross-browser-visual.spec.js
const { test, expect, devices } = require('@playwright/test');

const browsers = [
  { name: 'chromium', browser: 'chromium' },
  { name: 'firefox', browser: 'firefox' },
  { name: 'webkit', browser: 'webkit' },
];

const viewports = [
  { name: 'mobile', ...devices['iPhone 13'] },
  { name: 'tablet', ...devices['iPad Pro 11'] },
  { name: 'desktop', viewport: { width: 1920, height: 1080 } },
];

for (const { name, browser } of browsers) {
  test.describe(`${name} 浏览器`, () => {
    for (const viewport of viewports) {
      test(`${viewport.name} 视觉检查`, async ({ playwright }) => {
        const browserInstance = await playwright[browser].launch();
        const context = await browserInstance.newContext({
          viewport: viewport.viewport,
          userAgent: viewport.userAgent,
        });
        const page = await context.newPage();
        
        await page.goto('http://localhost:3000');
        
        await expect(page).toHaveScreenshot(
          `homepage-${name}-${viewport.name}.png`
        );
        
        await browserInstance.close();
      });
    }
  });
}
```

### 2. 响应式断点测试

```javascript
// tests/responsive-visual.spec.js
const breakpoints = [
  { name: 'xs', width: 320, height: 568 },   // iPhone SE
  { name: 'sm', width: 375, height: 667 },   // iPhone 13
  { name: 'md', width: 768, height: 1024 },  // iPad
  { name: 'lg', width: 1024, height: 768 },  // Laptop
  { name: 'xl', width: 1920, height: 1080 }, // Desktop
];

test.describe('响应式视觉测试', () => {
  for (const bp of breakpoints) {
    test(`${bp.name} (${bp.width}x${bp.height})`, async ({ page }) => {
      await page.setViewportSize({ width: bp.width, height: bp.height });
      await page.goto('http://localhost:3000');
      
      await expect(page).toHaveScreenshot(`responsive-${bp.name}.png`);
    });
  }
});
```

## 主题切换测试

```javascript
// tests/theme-visual.spec.js
const themes = ['light', 'dark', 'high-contrast'];

test.describe('主题视觉测试', () => {
  for (const theme of themes) {
    test(`${theme} 主题`, async ({ page }) => {
      await page.goto('http://localhost:3000');
      
      // 切换主题
      await page.evaluate((t) => {
        document.documentElement.setAttribute('data-theme', t);
      }, theme);
      
      // 等待主题应用
      await page.waitForTimeout(500);
      
      await expect(page).toHaveScreenshot(`theme-${theme}.png`);
    });
  }
});
```

## 组件级视觉测试

```javascript
// tests/component-visual.spec.js
const components = [
  { name: 'button', selector: '.btn', url: '/components/button' },
  { name: 'input', selector: '.input', url: '/components/input' },
  { name: 'card', selector: '.card', url: '/components/card' },
  { name: 'modal', selector: '.modal', url: '/components/modal' },
];

test.describe('组件视觉测试', () => {
  for (const component of components) {
    test(`${component.name} 组件`, async ({ page }) => {
      await page.goto(`http://localhost:3000${component.url}`);
      
      // 等待组件渲染完成
      await page.waitForSelector(component.selector);
      
      // 组件截图
      const element = page.locator(component.selector).first();
      await expect(element).toHaveScreenshot(`component-${component.name}.png`);
    });
  }
});
```

## 视觉测试最佳实践

### 1. 稳定测试环境

```javascript
// 等待页面完全加载
await page.waitForLoadState('networkidle');

// 等待动画完成
await page.waitForTimeout(1000);

// 禁用动画（避免截图时机问题）
await page.addStyleTag({
  content: `
    *, *::before, *::after {
      animation-duration: 0s !important;
      transition-duration: 0s !important;
    }
  `,
});
```

### 2. 处理动态内容

```javascript
// 隐藏动态内容（时间戳、随机数据等）
await page.evaluate(() => {
  document.querySelectorAll('.timestamp, .random-data').forEach(el => {
    el.style.visibility = 'hidden';
  });
});

// 或使用 mask 遮挡
await expect(page).toHaveScreenshot('page.png', {
  mask: [
    page.locator('.timestamp'),
    page.locator('.user-avatar'),
  ],
});
```

### 3. 合理的阈值设置

```javascript
// 根据组件重要性设置不同阈值
const thresholds = {
  critical: { maxDiffPixels: 0, threshold: 0 },      // 关键组件，零容忍
  important: { maxDiffPixels: 50, threshold: 0.1 },  // 重要组件，少量容差
  normal: { maxDiffPixels: 100, threshold: 0.2 },    // 普通组件，适度容差
};

await expect(page.locator('.logo')).toHaveScreenshot('logo.png', {
  ...thresholds.critical,
});
```

### 4. 定期更新基线

```bash
# 每周更新一次基线（避免基线老化）
npm run update-visual-baseline

# 或在大型 UI 重构后更新
npm run update-visual-baseline -- --force
```

## 常见问题处理

### 1. 字体渲染差异

```javascript
// 使用系统字体，避免跨平台差异
await page.addStyleTag({
  content: `
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 
                   'Helvetica Neue', Arial, sans-serif !important;
    }
  `,
});
```

### 2. 图片加载问题

```javascript
// 等待所有图片加载完成
await page.waitForFunction(() => {
  const images = Array.from(document.querySelectorAll('img'));
  return images.every(img => img.complete);
});
```

### 3. 滚动位置问题

```javascript
// 重置滚动位置
await page.evaluate(() => {
  window.scrollTo(0, 0);
});
```

## 输出格式

视觉测试完成后输出：

```markdown
## 视觉回归测试报告

### 测试概览
- 测试时间：2026-11-03 15:30
- 测试环境：Chrome 120, Ubuntu 22.04
- 基线版本：v1.2.0
- 测试版本：v1.2.1

### 测试结果
| 指标 | 数值 |
|------|------|
| 总测试页面 | 20 |
| 通过 | 18 |
| 失败 | 2 |
| 跳过 | 0 |
| 通过率 | 90% |

### 失败详情

#### 1. 首页导航栏
- **差异率**：2.3%
- **差异位置**：Logo 区域
- **差异描述**：间距从 20px 变为 25px
- **原因**：设计变更（已批准）
- **处理**：更新基线

#### 2. 登录页表单
- **差异率**：15.7%
- **差异位置**：输入框区域
- **差异描述**：输入框高度异常变化
- **原因**：CSS 冲突（Bug）
- **处理**：修复代码

### 视觉覆盖率
- 关键页面：100%（5/5）
- 重要组件：95%（19/20）
- 总覆盖率：96.2%

### 建议
1. ✅ 修复登录页 Bug 后重新测试
2. ✅ 更新首页基线（设计变更已批准）
3. ⚠️ 补充移动端视觉测试
```

## 埋点指令

**第 1 步**：调用 `~/.trae-cn/scripts/skill-usage.sh log common-visual-regression-test --trigger auto`
若报 `Permission denied` 或沙箱拦截，必须以非沙箱方式重试。

## 参考资源

- Playwright Visual Testing: https://playwright.dev/docs/test-snapshots
- BackstopJS: https://github.com/garris/BackstopJS
- Percy: https://percy.io/
- Visual Testing Best Practices: https://storybook.js.org/docs/react/writing-tests/visual-testing
