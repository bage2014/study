# 前端开发规范

## 1. 技术栈

- **框架**：Vue 3.5.30+
- **构建工具**：Vite 8.0.1+
- **状态管理**：Pinia 3.0.4+
- **路由**：Vue Router 4.6.4+
- **HTTP客户端**：Axios 1.14.0+
- **CSS框架**：Tailwind CSS 4.2.2+
- **PostCSS**：8.5.8+
- **Autoprefixer**：10.4.27+
- **测试工具**：Selenium WebDriver、Jest、Cypress、Playwright

## 2. 项目结构

```
frontend/web/
├── src/                       # 源代码目录
│   ├── assets/                # 静态资源
│   ├── components/            # 通用组件
│   ├── router/                # 路由配置
│   ├── stores/                # Pinia状态管理
│   ├── utils/                 # 工具函数
│   ├── views/                 # 页面组件
│   ├── App.vue                # 根组件
│   ├── main.js                # 入口文件
│   └── style.css              # 全局样式
├── public/                    # 公共资源
├── tests/                     # 测试文件目录
│   ├── selenium-config.js     # Selenium配置和运行器
│   ├── run-all-tests.js       # 运行所有测试
│   ├── auth/                  # 认证相关测试
│   ├── family/                # 家族管理相关测试
│   ├── member/                # 成员管理相关测试
│   └── pages/                 # 其他页面测试
├── index.html                 # HTML模板
├── vite.config.js             # Vite配置
└── package.json               # 项目配置
```

## 3. 编码规范

### 3.1 命名规范
- **文件命名**：小写字母，使用连字符分隔，如 `login-view.vue`
- **组件命名**：首字母大写，驼峰命名，如 `HelloWorld.vue`
- **变量命名**：首字母小写，驼峰命名，如 `userStore`
- **常量命名**：全大写，下划线分隔，如 `API_BASE_URL`
- **方法命名**：首字母小写，驼峰命名，如 `loginUser`

### 3.2 代码风格
- 使用 2 空格缩进
- 组件使用 `<script setup>` 语法
- 模板使用 PascalCase 命名组件
- 样式使用 Tailwind CSS 类名
- 注释清晰，说明组件功能和参数含义
- 方法体之间空一行
- 类成员之间空一行

## 4. 核心规范

### 4.1 组件规范
- 组件应遵循单一职责原则
- 组件命名应清晰表达其功能
- 组件 props 应定义类型和默认值
- 组件事件应使用 kebab-case 命名
- 复杂组件应拆分为更小的子组件

### 4.2 状态管理规范
- 使用 Pinia 进行状态管理
- 按功能模块划分 store
- 状态定义应清晰，包含必要的类型注释
- 异步操作应使用 async/await
- 状态更新应通过 actions 进行

### 4.3 路由规范
- 使用 Vue Router 4
- 路由配置应按模块组织
- 路由路径使用 kebab-case
- 路由守卫应统一管理
- 懒加载路由以优化性能

### 4.4 HTTP请求规范
- 使用 Axios 进行 HTTP 请求
- 配置统一的 baseURL 和拦截器
- 请求和响应数据应进行类型检查
- 错误处理应统一管理
- API 调用应封装在 store 或单独的服务中

### 4.5 样式规范
- 使用 Tailwind CSS 进行样式开发
- 避免使用内联样式
- 自定义样式应使用 `@layer` 指令
- 响应式设计应使用 Tailwind 的响应式类
- 主题配置应在 `tailwind.config.js` 中定义

### 4.6 表单处理规范
- 使用 v-model 进行双向绑定
- 表单验证应在提交前进行
- 错误信息应清晰显示
- 表单提交应使用异步操作
- 提交状态应提供视觉反馈

### 4.7 错误处理规范
- 全局错误处理应在 Axios 拦截器中实现
- 页面级错误应在组件中捕获并显示
- 错误信息应友好且具有指导性
- 关键操作应提供错误重试机制

## 5. 核心功能实现

### 5.1 用户管理
- **登录/注册**：`Login.vue` 和 `Register.vue` 实现了用户认证功能
- **状态管理**：`user.js` 存储用户信息和认证状态
- **路由守卫**：`router/index.js` 实现了基于JWT的权限控制

### 5.2 家族管理
- **家族树**：`FamilyTree.vue` 实现了家族选择、成员管理和关系管理
- **家族管理**：`FamilyManagement.vue` 实现了家族的创建、编辑和删除
- **管理员功能**：`FamilyManagement.vue` 实现了管理员维护功能，包括查看管理员、更改管理员
- **状态管理**：`family.js` 管理家族数据

### 5.3 成员管理
- **成员列表**：`Members.vue` 展示家族成员列表
- **成员详情**：`MemberDetail.vue` 展示和编辑成员详细信息
- **成员搜索**：`MemberSearch.vue` 实现成员搜索功能
- **状态管理**：`member.js` 管理成员数据

### 5.4 关系管理
- **关系管理**：`Relationships.vue` 实现成员关系的绑定和管理
- **状态管理**：`relationship.js` 管理关系数据

### 5.5 历史记录
- **历史记录**：`Events.vue` 展示家族历史事件
- **状态管理**：`event.js` 管理事件数据

### 5.6 多媒体管理
- **多媒体库**：`Media.vue` 实现照片、视频、文档的上传和管理
- **状态管理**：`media.js` 管理媒体数据

### 5.7 设置
- **设置页面**：`Settings.vue` 实现基本设置和数据备份

## 6. API设计规范

- **参数传递**：
  - GET请求：使用查询参数（query parameters）传递参数
  - POST/PUT请求：使用请求体（request body）传递参数
  - 禁止将参数直接放在URL路径中

- **正确示例**：
  - GET请求：`/members/family?familyId=1`
  - POST请求：`/members` 配合请求体 `{ "familyId": 1, "name": "张三" }`

## 7. 性能优化

- **组件懒加载**：使用路由懒加载减少初始加载时间
- **数据缓存**：合理使用 Pinia 状态缓存数据
- **API 调用优化**：避免重复请求，使用适当的缓存策略
- **图片优化**：使用适当的图片格式和大小
- **代码分割**：利用 Vite 的代码分割功能
- **缓存策略**：合理设置 HTTP 缓存

## 8. 安全性

- **认证**：使用 JWT 进行用户认证
- **权限控制**：基于路由守卫的权限控制
- **输入验证**：对用户输入进行验证
- **XSS 防护**：使用 Vue 的内置防护机制
- **HTTPS**：生产环境使用 HTTPS
- **CSP**：配置内容安全策略

## 9. 测试规范

### 9.1 测试类型
- **单元测试**：测试单个组件或函数
- **集成测试**：测试组件间的交互
- **端到端测试**：测试完整的用户流程
- **性能测试**：测试页面加载速度和响应时间
- **可访问性测试**：测试网站对所有用户的可访问性

### 9.2 Selenium测试

所有前端功能验证使用Selenium WebDriver进行端到端测试：

```bash
# 运行所有测试
node tests/run-all-tests.js

# 运行特定测试
node tests/family/selenium-family-test.js

# 开发服务器需要先启动
cd frontend/web && npm run dev
```

### 9.3 Chrome DevTools 验证
使用 Chrome DevTools 验证前端页面功能的正确性：

#### 9.3.1 元素检查器 (Elements Panel)
- 验证 HTML 结构是否正确
- 检查 CSS 样式是否应用正确
- 测试响应式布局在不同屏幕尺寸下的表现
- 检查元素的盒模型和定位

#### 9.3.2 控制台 (Console Panel)
- 检查 JavaScript 错误和警告
- 查看控制台日志输出
- 测试 JavaScript 代码片段
- 监控网络请求和响应

#### 9.3.3 网络面板 (Network Panel)
- 分析网络请求的性能
- 检查 API 响应状态和数据
- 测试资源加载顺序和时间
- 验证缓存策略的有效性

#### 9.3.4 应用面板 (Application Panel)
- 检查 localStorage 和 sessionStorage 数据
- 验证 Cookie 设置和管理
- 测试 Service Worker 功能
- 查看 manifest.json 配置

#### 9.3.5 性能面板 (Performance Panel)
- 分析页面加载性能
- 测试交互响应时间
- 识别性能瓶颈
- 优化渲染性能

#### 9.3.6 安全面板 (Security Panel)
- 验证 HTTPS 配置
- 检查内容安全策略 (CSP)
- 测试混合内容警告
- 验证证书有效性

### 9.4 测试工具
- **Jest**：单元测试框架
- **Cypress**：端到端测试框架
- **Playwright**：跨浏览器测试框架
- **Lighthouse**：性能和可访问性评估
- **Vue Test Utils**：Vue 组件测试工具

### 9.5 测试标准
- 所有功能模块必须通过单元测试
- 关键用户流程必须通过端到端测试
- 页面性能指标必须达到 Lighthouse 建议值
- 代码覆盖率必须达到项目要求

## 10. 开发流程

1. 需求分析与设计
2. 页面结构设计
3. 组件开发
4. 状态管理实现
5. 路由配置
6. API 集成
7. 样式开发
8. 测试与调试
9. 代码审查
10. 构建与部署

## 11. 最佳实践

- **组件复用**：提取通用组件提高代码复用率
- **状态管理**：合理使用 Pinia 管理应用状态
- **API 调用**：封装 API 调用，统一处理错误和认证
- **代码组织**：按功能模块组织代码，提高可维护性
- **性能优化**：关注应用性能，优化渲染和 API 调用
- **功能验证**：使用Selenium进行端到端测试验证功能正确性
- **TypeScript**：使用 TypeScript 增强类型安全性
- **响应式设计**：确保在不同设备上的良好体验
- **可访问性**：遵循 Web 可访问性标准
- **国际化**：支持多语言
- **代码格式化**：使用 ESLint 和 Prettier 保持代码风格一致

## 12. 常见问题与解决方案

- **API 404 错误**：检查 API 路径是否正确，后端是否实现了相应接口
- **认证失败**：检查 JWT token 是否有效，是否在请求头中正确传递
- **状态更新问题**：确保 Pinia store 的状态更新逻辑正确
- **响应式问题**：使用 Vue 的响应式 API 确保数据变化能正确触发视图更新
- **性能问题**：使用 Chrome DevTools 分析性能瓶颈
- **样式问题**：检查 Tailwind CSS 类名是否正确，是否有样式冲突

## 13. 总结

本前端实现基于 Vue 3 + Pinia + Tailwind CSS 技术栈，实现了家庭族谱APP的所有核心功能。代码结构清晰，遵循最佳实践，具有良好的可维护性和扩展性。所有功能通过Selenium进行端到端测试验证，确保功能的正确性和可靠性。