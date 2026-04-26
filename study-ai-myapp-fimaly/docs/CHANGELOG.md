# 家族树应用 - 更新日志

## v1.1.0 (2026-04-25)

### 新增功能

#### 后端架构优化
- **业务异常体系**
  - 新增 `ErrorCode` 枚举类，统一管理所有业务错误码
  - 新增 `BusinessException` 自定义业务异常类
  - 更新 `GlobalExceptionHandler`，支持统一的异常响应格式

- **配置化AI服务架构**
  - 新增 `AiProperties` 配置属性类，支持多AI Provider配置
  - 新增 `AiProvider` 接口，定义AI服务抽象
  - 新增 `LocalAiProvider` 本地AI实现
  - 新增 `AiService` AI服务门面，统一调度AI能力

- **API文档完善**
  - 新增 `OpenApiConfig` 配置类
  - 为 AuthController、FamilyController、MemberController 添加完整的OpenAPI注解
  - 支持Swagger UI访问API文档

- **集成测试框架**
  - 新增 TestContainers 依赖，支持PostgreSQL和Redis容器化测试
  - 新增 `BaseIntegrationTest` 集成测试基类
  - 新增 `FamilyApiIntegrationTest` 家族API集成测试示例

#### 前端优化
- **网络层封装增强**
  - 完善 axios 请求/响应拦截器
  - 添加 Token 自动刷新机制
  - 添加请求重试机制（指数退避策略）
  - 完善错误处理和日志追踪
  - 支持 FormData 创建和文件下载工具函数

### 代码质量改进

#### 日志规范统一
- 新增 `LoggingInterceptor` 请求日志拦截器
- 统一使用 SLF4J + MDC 进行日志追踪
- 为 Service 层添加结构化日志记录

#### 参数校验增强
- DTO类添加 `@Valid` 注解和校验约束
- 控制器方法添加参数验证
- Service层添加业务数据校验

#### 事务管理完善
- 所有写操作方法添加 `@Transactional` 注解
- 确保数据一致性和完整性

### 安全改进
- **JWT Secret安全加固**
  - JwtUtils 强制要求配置 JWT Secret
  - 最小密钥长度要求提升至32字符
  - 更新生产环境配置文件，添加环境变量支持
  - 改进错误消息，避免敏感信息泄露

### 配置更新
- `application-dev.properties` - 开发环境JWT密钥长度增加
- `application-prod.properties` - 生产环境JWT通过环境变量配置

## v1.0.0 (2026-04-20)

### 初始版本功能
- 用户认证系统（注册、登录、JWT令牌）
- 家族管理（CRUD、成员管理）
- 成员管理（CRUD、搜索）
- 关系管理
- 位置服务
- 媒体管理
- 事件管理
- 里程碑管理
- AI关系分析
- AI家族故事生成
- 操作日志记录

---

## 更新日志规范

每次进行核心变更时，需要在此文档中添加更新记录：

### 记录格式
```markdown
## v[x.y.z] (YYYY-MM-DD)

### 新增功能
- 功能描述

### 改进
- 改进描述

### 修复
- 问题修复描述

### 破坏性变更
- 需要注意的变更
```

### 版本号规则
- **主版本号**: 重大架构变更
- **次版本号**: 新功能添加
- **修订版本号**: Bug修复和小改进

### 更新时机
- 每次发布新版本时更新
- 重大功能完成时更新
- 修复关键Bug后更新
