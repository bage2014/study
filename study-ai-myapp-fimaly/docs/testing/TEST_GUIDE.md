# 测试运行指南

本文档说明如何运行项目的各种测试。

## 前端测试

### 环境准备

```bash
cd frontend/web
npm install
```

### Playwright E2E测试

#### 安装浏览器

```bash
npx playwright install
```

#### 运行所有测试

```bash
npm test
```

#### 运行测试并显示UI

```bash
npm run test:ui
```

#### 运行特定测试文件

```bash
npx playwright test tests/auth.spec.js
npx playwright test tests/family.spec.js
npx playwright test tests/member.spec.js
npx playwright test tests/features.spec.js
```

#### 运行特定测试用例

```bash
npx playwright test --grep "登录"
npx playwright test --grep "注册"
```

### Vitest单元测试

```bash
npm run test:unit
```

### 代码质量检查

```bash
# ESLint检查
npm run lint

# 代码格式化
npm run format
```

## 后端测试

### 运行所有测试

```bash
cd backend
mvn test
```

### 运行特定测试类

```bash
mvn test -Dtest=AuthControllerTest
mvn test -Dtest=AuthServiceTest
```

### 生成测试覆盖率报告

```bash
mvn test jacoco:report
```

报告位置：`backend/target/site/jacoco/index.html`

### API文档

启动服务后访问：
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 持续集成

### GitHub Actions示例

```yaml
name: CI

on: [push, pull_request]

jobs:
  frontend-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      - name: Install dependencies
        run: cd frontend/web && npm install
      - name: Run Playwright tests
        run: cd frontend/web && npm test

  backend-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          java-version: '21'
      - name: Run Maven tests
        run: cd backend && mvn test
```

## 测试最佳实践

1. **编写有意义的测试用例名称**
   - ✅ `login_Success`
   - ❌ `test1`

2. **保持测试独立**
   - 每个测试应该独立运行，不依赖其他测试

3. **使用Arrange-Act-Assert模式**
   ```javascript
   test('should login successfully', async ({ page }) => {
     // Arrange
     await page.goto('/login');

     // Act
     await page.fill('input[id="email"]', 'test@example.com');
     await page.click('button[type="submit"]');

     // Assert
     await expect(page).toHaveURL('/home');
   });
   ```

4. **及时更新测试**
   - 功能变更后及时更新相关测试用例

5. **覆盖率目标**
   - 核心业务逻辑覆盖率 > 80%
   - 关键路径E2E测试覆盖率 100%
