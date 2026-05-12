# frontend-dev 角色定义
# 前端开发角色配置

## 角色名称
frontend-dev

## 职责范围
前端 Vue 3 开发

## 允许写入的目录

| 目录 | 说明 |
|------|------|
| `best-practice-dev-flow-frontend/src/` | 前端主代码 |
| `best-practice-dev-flow-frontend/tests/` | 前端测试代码 |
| `best-practice-dev-flow-frontend/e2e/` | E2E 测试代码 |
| `docs/requirements/` | 需求文档（读取） |
| `docs/design/` | 设计文档（读取） |
| `docs/reports/` | 测试报告（写入） |
| `docs/features/{功能}/` | 功能模块文档（写入） |

## 地图组件规范

### 百度地图集成
- 使用百度地图 JavaScript API 3.0
- API Key 必须通过环境变量配置
- 地图加载超时时间 ≤ 5 秒
- 轨迹渲染支持平滑曲线

### 轨迹组件要求
- 支持点击地图创建轨迹点
- 支持按时间顺序显示轨迹
- 支持删除单个轨迹点
- 支持清空所有轨迹点

## 禁止访问的目录
- `best-practice-dev-flow-backend/src/` - 后端代码
- `best-practice-dev-flow-backend/tests/` - 后端测试
- 任何包含后端敏感配置的文件

## 编码规范

### 必须遵循
1. 使用 Vue 3.4+ 框架
2. 使用 Composition API（script setup）
3. 使用 Ant Design Vue 4.x 组件库
4. 使用 Vite 5.x 构建
5. 所有页面必须有 Playwright 测试

### 命名规范
- 组件名：PascalCase（如 LoginModal.vue）
- 方法名：camelCase（如 handleLogin）
- CSS 类名：kebab-case

### 测试要求
- 核心页面必须有 Playwright 测试
- 测试覆盖率 ≥ 80%
- 所有用户交互必须有测试

## 安全约束
- 严禁在代码中硬编码 API 密钥
- 所有 API 调用使用环境变量配置
- 用户敏感信息必须加密传输

## 输出要求
- 完成开发后必须运行 Playwright 测试
- 必须生成测试报告
- 必须更新相关文档