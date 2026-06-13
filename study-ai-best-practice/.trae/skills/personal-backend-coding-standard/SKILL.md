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

## 项目技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 21 | 项目默认使用 JDK 21 |
| Spring Boot | 3.x | 项目默认使用 Spring Boot 3.x |
| 日志框架 | SLF4J + Logback | 使用 Lombok @Slf4j 注解 |

## 项目模块结构

```
xx-parent/
├── xx-gateway/     # 网关模块 - 处理请求路由和认证
├── xx-biz/         # 业务模块 - 核心业务逻辑
└── xx-infra/       # 基础设施模块 - 公共组件、工具类
```

## 编码规范分类

### 1. 命名规范

| 元素类型 | 规范 | 示例 |
|----------|------|------|
| 类名 | PascalCase | `UserService`, `OrderRepository` |
| 方法名 | camelCase | `createUser()`, `getOrderById()` |
| 变量名 | camelCase | `userName`, `orderId` |
| 常量名 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT` |
| 包名 | lowercase | `com.bage.xx.service`, `com.bage.xx.repository` |
| 接口名 | PascalCase + 以 Interface 结尾或直接命名 | `UserRepository`, `UserService` |
| 模块名 | kebab-case | `xx-gateway`, `xx-biz`, `xx-infra` |

### 2. 代码结构

```
src/main/java/com/bage/xx/
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
├── util/                # 工具类
│   └── StringUtils.java
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

### 4. 日志规范

**统一使用 Lombok @Slf4j 注解**

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    @Override
    public UserResponse createUser(UserCreateRequest request) {
        log.info("开始创建用户，邮箱: {}", request.getEmail());
        try {
            // 业务逻辑
            log.debug("用户创建成功，ID: {}", userId);
            return response;
        } catch (Exception e) {
            log.error("创建用户失败，邮箱: {}", request.getEmail(), e);
            throw e;
        }
    }
}
```

### 5. 错误处理

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

### 6. 数据库操作

| 原则 | 说明 |
|------|------|
| 使用 ORM | 使用 Spring Data JPA 或 MyBatis |
| 事务管理 | 使用 `@Transactional` 注解 |
| 查询优化 | 使用索引，避免 N+1 查询 |
| 批量操作 | 使用批量插入/更新 |
| SQL 注入 | 使用参数化查询 |

### 7. API 设计

**HTTP 方法限制**：Controller 只暴露 GET 和 POST 请求，不使用 PUT、PATCH、DELETE 等其他方法

| 原则 | 说明 |
|------|------|
| RESTful | 遵循 REST 原则 |
| 路径命名 | 使用小写连字符 |
| HTTP 方法 | **仅使用 GET 和 POST** |
| 状态码 | 使用标准 HTTP 状态码 |
| 响应格式 | 统一响应结构 |

**Controller 示例**：

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 查询 - 使用 GET
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    // 列表查询 - 使用 GET
    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }
    
    // 创建 - 使用 POST
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createUser(request));
    }
    
    // 更新 - 使用 POST（不使用 PUT/PATCH）
    @PostMapping("/{id}/update")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id, 
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }
    
    // 删除 - 使用 POST（不使用 DELETE）
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 8. 安全规范

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

1. **使用 Lombok**：简化样板代码，使用 @Slf4j、@Data、@Builder 等注解
2. **使用 DTO**：分离实体和传输对象
3. **使用 Optional**：避免空指针异常
4. **使用 Stream API**：简化集合操作
5. **使用断言**：验证方法参数
6. **编写单元测试**：保证代码质量
7. **使用日志框架**：使用 @Slf4j 注解（SLF4J + Logback）
8. **配置外部化**：使用配置文件或环境变量

### Spring Boot 最佳实践

```java
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("创建用户请求: {}", request.getEmail());
        
        validateRequest(request);
        
        User user = User.builder()
            .email(request.getEmail())
            .name(request.getName())
            .build();
        
        User savedUser = userRepository.save(user);
        log.debug("用户创建成功，ID: {}", savedUser.getId());
        
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
| 日志使用 | 是否正确使用 @Slf4j 注解 | ✅/❌ |
| HTTP 方法 | 是否仅使用 GET 和 POST | ✅/❌ |

## 核心组件

| 组件 | 职责 | 描述 |
|------|------|------|
| CodeStyleChecker | 代码风格检查器 | 检查代码是否符合命名规范 |
| CodeStructureAnalyzer | 代码结构分析器 | 分析代码组织和文件结构 |
| SecurityScanner | 安全扫描器 | 检测安全隐患 |
| BestPracticeRecommender | 最佳实践推荐器 | 提供最佳实践建议 |

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| JAVA_HOME | JDK 21 路径 | - |

### Maven 依赖

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>

<properties>
    <java.version>21</java.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## 扩展指南

### 添加新的编码规则

1. 在规则引擎中添加新规则
2. 定义规则的检查逻辑
3. 更新代码审查检查清单

### 添加新语言支持

1. 创建对应语言的规则定义
2. 添加语言特定的最佳实践
3. 更新技能文档