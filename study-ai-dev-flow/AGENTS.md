# AI 编码助手规范

> 本文件是所有 AI 编码助手在本项目中必须遵循的规范。打开项目后自动生效。
> 技能路由表: ~/bage/github/study/study-ai-skills/SKILLS-ROUTER.md

## 1. 项目上下文

### 1.1 项目概述
- 项目名称: study-ai-dev-flow
- 类型: 研发流程标准项目（前后端分离），作为团队开发规范基线
- 后端: Java 21, Spring Boot 3.4, Spring Data JPA, H2
- 前端: Vue 3.5, TypeScript 5.7, Vite 6, TailwindCSS 3.4, Pinia

### 1.2 架构说明
- 后端分层: Controller → Service(接口) → ServiceImpl(实现) → Repository → Entity
- 前端分层: Pages → Components → Services → Stores → Types
- 前后端通过 Vite proxy 打通: `/api` → `http://localhost:8080`

### 1.3 项目结构
```
study-ai-dev-flow/
├── study-ai-dev-flow-backend/     # 后端
│   └── src/main/java/com/bage/study/ai/dev/flow/
│       ├── controller/            # 仅 GET/POST
│       ├── service/               # 接口
│       │   └── impl/             # 实现
│       ├── repository/
│       ├── entity/
│       ├── dto/request/           # 请求 DTO
│       ├── dto/response/          # 响应 DTO
│       ├── config/
│       ├── exception/             # BusinessException + GlobalExceptionHandler
│       └── util/
├── study-ai-dev-flow-frontend/    # 前端
│   └── src/
│       ├── components/common/     # 公共基础组件
│       ├── components/layout/     # 布局组件
│       ├── components/pages/      # 页面组件
│       ├── services/              # API 调用（禁止在组件中直接 axios）
│       ├── stores/                # Pinia 状态管理
│       ├── types/                 # TypeScript 类型定义
│       ├── hooks/                 # 组合式函数
│       ├── router/
│       ├── assets/
│       └── utils/
└── docs/
```

## 2. 后端编码规则（Java / Spring Boot）

### 2.1 包名规则
项目名按连字符拆分为多级包名：
- `study-ai-dev-flow` → `com.bage.study.ai.dev.flow`
- `user-service` → `com.bage.user.service`
- 禁止拼接: `com.bage.studyaidevflow` ❌

### 2.2 依赖注入
- 使用 `@Autowired` 属性注入 ✅
- 禁止 `@RequiredArgsConstructor` 构造器注入 ❌

```java
// ✅ 正确
@Autowired
private UserService userService;

// ❌ 错误
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
```

### 2.3 Service 层
- 必须有接口 + 实现类
- 实现类使用 `@Slf4j` 记录日志
- 写操作加 `@Transactional`

```java
// 接口
public interface UserService { ... }

// 实现
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    ...
}
```

### 2.4 Controller 层
- 仅使用 GET 和 POST，禁止 PUT/PATCH/DELETE
- 删除用 `POST /{id}/delete`，更新用 `POST /{id}/update`
- 使用 `@Valid` 校验参数
- 返回 DTO，不返回 Entity
- 使用 `@Slf4j` 记录日志

```java
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> list() { ... }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) { ... }

    @PostMapping("/{id}/update")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) { ... }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) { ... }
}
```

### 2.5 DTO 分层
- `dto/request/` — 请求 DTO（带 `@NotBlank` 等校验注解）
- `dto/response/` — 响应 DTO（带 `fromEntity()` 静态转换方法）
- 禁止直接暴露 Entity

### 2.6 异常处理
- 自定义异常: `BusinessException(errorCode, message)`
- 全局处理: `GlobalExceptionHandler`（`@RestControllerAdvice`）
- 统一返回: `ErrorResponse(code, message)`

### 2.7 日志
- 统一使用 `@Slf4j`（Lombok 注解）
- 入口方法用 `log.info()`
- 调试信息用 `log.debug()`
- 异常用 `log.error()` / `log.warn()`

## 3. 前端编码规则（Vue / TypeScript）

### 3.1 组件分层
- `components/common/` — 通用基础组件（Button、Input 等）
- `components/layout/` — 布局组件（Header、Sidebar 等）
- `components/pages/` — 页面组件（HomePage、SamplesPage 等）

### 3.2 API 调用
- 统一放在 `services/` 目录
- 禁止在组件中直接写 `axios.get/post`
- 使用 TypeScript 类型约束请求和响应

```typescript
// services/api.ts ✅
export const sampleApi = {
  findAll() { return http.get<Sample[]>('/samples') },
  create(data: SampleCreateRequest) { return http.post<Sample>('/samples', data) },
}

// 组件中使用 ✅
const { data } = await sampleApi.findAll()
```

### 3.3 类型定义
- 统一放在 `types/` 目录
- 接口命名用 PascalCase
- 导出所有公共类型

### 3.4 组件规范
- 使用 Composition API（`<script setup lang="ts">`）
- 单文件组件，模板在上、脚本在下
- Props 必须声明类型

## 4. 创建新项目时必须加载的 Skill

创建新项目时，**必须同时加载以下所有 Skill**，不能只加载初始化 Skill：

| 阶段 | 必须加载的 Skill | 作用 |
|------|-----------------|------|
| 初始化 | `common-spring-boot-init` | 后端项目骨架 |
| 初始化 | `common-vue-init` | 前端项目骨架 |
| **编码规范** | **`personal-backend-coding-standard`** | **包名、DI、Service、Controller、DTO、异常、日志** |
| **编码规范** | **`personal-frontend-coding-standard`** | **组件分层、services、types** |
| **项目规范** | **`common-project-standard`** | **README、AGENTS.md、.gitignore** |
| **启动管理** | **`personal-project-runner`** | **启动/重启/关闭** |

> ⚠️ 只加载 init skill 而不加载规范 skill 是最常见的错误。init skill 只管骨架，规范 skill 才管代码怎么写。

## 5. 禁止操作清单

- ❌ 包名拼接（`com.bage.studyaidevflow`）
- ❌ `@RequiredArgsConstructor` 构造器注入
- ❌ PUT / PATCH / DELETE HTTP 方法
- ❌ Controller 写业务逻辑
- ❌ 直接返回 Entity
- ❌ 组件内直接 axios 调用
- ❌ 缺少 DTO 层
- ❌ 缺少异常处理
- ❌ 缺少 @Slf4j 日志
- ❌ 缺少 AGENTS.md / README.md

## 6. 安全规范
- 禁止提交 .env 文件
- API Key 通过环境变量注入
- 数据库密码不硬编码
