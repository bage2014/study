---
name: "personal-backend-coding-standard"
description: "后端编码规范技能。提供后端开发的编码标准和最佳实践指导。"
---

# 后端编码规范技能

## 功能描述

提供后端开发的编码标准和最佳实践指导，帮助开发者编写高质量、可维护的后端代码。

## 何时使用

在以下情况调用此技能：
- 需要编写后端代码时
- 需要审查后端代码质量时
- 需要学习后端编码规范时
- 需要培训团队成员时

## 核心功能

- **命名规范**：提供变量、方法、类的命名标准
- **代码结构**：指导代码组织和文件结构
- **错误处理**：规范异常处理和错误返回
- **数据库操作**：提供数据库访问的最佳实践
- **API设计**：指导 RESTful API 的设计规范
- **安全规范**：提供安全编码的最佳实践

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| language | String | 否 | 目标语言（Java/Python/Go等），默认Java |
| framework | String | 否 | 使用的框架（Spring Boot/Django/Gin等） |
| focusArea | String | 否 | 关注领域（naming/structure/error/db/api/security） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "language": "Java",
  "framework": "Spring Boot",
  "focusArea": "naming",
  "rules": [
    {
      "category": "命名规范",
      "rules": [
        {
          "rule": "类名使用 PascalCase",
          "example": "class UserService",
          "description": "类名应采用大驼峰命名法"
        }
      ]
    }
  ],
  "bestPractices": ["最佳实践1", "最佳实践2"]
}
```

## 编码规范分类

### 1. 命名规范

| 元素类型 | 规范 | 示例 |
|----------|------|------|
| 类名 | PascalCase | `UserService`, `OrderRepository` |
| 方法名 | camelCase | `createUser()`, `getOrderById()` |
| 变量名 | camelCase | `userName`, `orderId` |
| 常量名 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT` |
| 包名 | lowercase | `com.example.service`, `com.example.repository` |
| 接口名 | PascalCase + 以 I 开头或以 Interface 结尾 | `UserRepository`, `IUserService` |

### 2. 代码结构

```
src/main/java/com/example/
├── controller/          # REST API 控制层
│   └── UserController.java
├── service/             # 业务逻辑层
│   ├── UserService.java
│   └── impl/
│       └── UserServiceImpl.java
├── repository/          # 数据访问层
│   └── UserRepository.java
├── entity/              # 数据库实体
│   └── User.java
├── dto/                 # 数据传输对象
│   ├── request/
│   │   └── UserCreateRequest.java
│   └── response/
│       └── UserResponse.java
├── config/              # 配置类
│   └── WebConfig.java
├── exception/           # 自定义异常
│   ├── BusinessException.java
│   └── GlobalExceptionHandler.java
└── Application.java     # 启动类
```

### 3. 方法设计

| 原则 | 说明 |
|------|------|
| 单一职责 | 每个方法只做一件事 |
| 方法长度 | 尽量控制在 50 行以内 |
| 参数数量 | 尽量控制在 3-5 个以内 |
| 返回值 | 避免返回 null，使用 Optional |
| 异常处理 | 使用 try-with-resources |

### 4. 错误处理

```java
// 自定义异常
public class BusinessException extends RuntimeException {
    private final String errorCode;
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

// 全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("INTERNAL_ERROR", "系统内部错误"));
    }
}
```

### 5. 数据库操作

| 原则 | 说明 |
|------|------|
| 使用 ORM | 使用 Spring Data JPA 或 MyBatis |
| 事务管理 | 使用 `@Transactional` 注解 |
| 查询优化 | 使用索引，避免 N+1 查询 |
| 批量操作 | 使用批量插入/更新 |
| SQL 注入 | 使用参数化查询 |

### 6. API 设计

| 原则 | 说明 |
|------|------|
| RESTful | 遵循 REST 原则 |
| 路径命名 | 使用小写连字符 |
| HTTP 方法 | GET/POST/PUT/PATCH/DELETE |
| 状态码 | 使用标准 HTTP 状态码 |
| 响应格式 | 统一响应结构 |

### 7. 安全规范

| 原则 | 说明 |
|------|------|
| 输入验证 | 使用 `@Valid` 和 `@Validated` |
| 认证授权 | 使用 Spring Security |
| 密码加密 | 使用 BCrypt 或 Argon2 |
| SQL 注入 | 使用参数化查询 |
| XSS 防护 | 对输出进行转义 |
| CSRF 防护 | 启用 CSRF 保护 |

## 最佳实践指南

### Java 后端开发最佳实践

1. **使用 Lombok**：简化样板代码
2. **使用 DTO**：分离实体和传输对象
3. **使用 Optional**：避免空指针异常
4. **使用 Stream API**：简化集合操作
5. **使用断言**：验证方法参数
6. **编写单元测试**：保证代码质量
7. **使用日志框架**：使用 SLF4J + Logback
8. **配置外部化**：使用配置文件或环境变量

### Spring Boot 最佳实践

```java
// Service 层示例
@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // 参数验证
        validateRequest(request);
        
        // 创建实体
        User user = User.builder()
            .email(request.getEmail())
            .name(request.getName())
            .build();
        
        // 保存
        User savedUser = userRepository.save(user);
        
        // 返回 DTO
        return UserResponse.fromEntity(savedUser);
    }
    
    private void validateRequest(UserCreateRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new BusinessException("INVALID_PARAM", "邮箱不能为空");
        }
    }
}
```

## 代码审查检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 命名规范 | 变量、方法、类命名是否符合规范 | ✅/❌ |
| 代码格式 | 缩进、空格、换行是否符合规范 | ✅/❌ |
| 注释完整 | 是否有必要的注释说明 | ✅/❌ |
| 异常处理 | 是否正确处理异常情况 | ✅/❌ |
| 代码复用 | 是否避免重复代码 | ✅/❌ |
| 性能优化 | 是否考虑性能问题 | ✅/❌ |
| 安全性 | 是否存在安全隐患 | ✅/❌ |
| 可测试性 | 代码是否易于测试 | ✅/❌ |

## 核心组件

| 组件 | 职责 | 描述 |
|------|------|------|
| CodeStyleChecker | 代码风格检查器 | 检查代码是否符合命名规范 |
| CodeStructureAnalyzer | 代码结构分析器 | 分析代码组织和文件结构 |
| SecurityScanner | 安全扫描器 | 检测安全隐患 |
| BestPracticeRecommender | 最佳实践推荐器 | 提供最佳实践建议 |

## 配置要求

无需额外配置，基于规则引擎进行代码分析。

## 扩展指南

### 添加新的编码规则

1. 在规则引擎中添加新规则
2. 定义规则的检查逻辑
3. 更新代码审查检查清单

### 添加新语言支持

1. 创建对应语言的规则定义
2. 添加语言特定的最佳实践
3. 更新技能文档