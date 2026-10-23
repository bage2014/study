# AI 编码助手规范

## 1. 项目上下文

### 1.1 项目概述
- 项目名称: study-ai-dev-flow
- 类型: 研发流程标准项目（前后端分离）
- 后端: Java 21, Spring Boot 3.4, Spring Data JPA, H2
- 前端: Vue 3.5, TypeScript 5.7, Vite 6, TailwindCSS 3.4, Pinia

### 1.2 架构说明
- 后端分层: Controller → Service(接口) → ServiceImpl(实现) → Repository → Entity
- 前端分层: Pages → Components → Services → Stores → Types
- 前后端通过 Vite proxy 打通: `/api` → `http://localhost:8080`

### 1.3 代码风格
- 后端: Google Java Style, Lombok 简化
- 前端: TypeScript 严格模式, Composition API, 单引号, 无分号

## 2. 编码规则

### 2.1 必须遵循
- 包名按连字符拆分: `study-ai-dev-flow` → `com.bage.study.ai.dev.flow`
- 使用 `@Autowired` 属性注入，不使用 `@RequiredArgsConstructor`
- Service 层必须有接口 + 实现类
- Controller 仅使用 GET 和 POST 方法
- 使用 `@Slf4j` 注解记录日志
- 使用 DTO 进行数据传输，不直接暴露 Entity
- 前端 API 调用统一放在 `services/` 目录
- 前端类型定义统一放在 `types/` 目录

### 2.2 禁止操作
- 禁止在 Controller 中写业务逻辑
- 禁止使用 PUT、PATCH、DELETE HTTP 方法
- 禁止使用构造器注入（`@RequiredArgsConstructor`）
- 禁止直接返回 Entity，必须转换为 Response DTO
- 禁止在前端组件中直接写 axios 调用

### 2.3 最佳实践
- 异常统一通过 GlobalExceptionHandler 处理
- 使用 `@Transactional` 管理事务
- 使用 `@Valid` 进行参数校验
- 前端组件按职责分层: common(通用) / layout(布局) / pages(页面)

## 3. 技能使用指引

### 3.1 推荐技能
- `personal-backend-coding-standard` - 后端编码规范
- `personal-frontend-coding-standard` - 前端编码规范
- `common-project-standard` - 项目规范
- `personal-project-runner` - 项目启动管理
- `common-spring-boot-init` - Spring Boot 项目初始化
- `common-vue-init` - Vue 项目初始化

## 4. 工具配置

### 4.1 后端
- Lombok: 简化样板代码
- Spring Boot Starter Validation: 参数校验
- Spring Boot Actuator: 健康检查

### 4.2 前端
- ESLint + Prettier: 代码质量 + 格式化
- TailwindCSS: 原子化 CSS
- TypeScript 严格模式

## 5. 安全规范

### 5.1 敏感信息
- 禁止提交 .env 文件
- API Key 通过环境变量注入
- 数据库密码不硬编码
