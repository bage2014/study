# 后端编码规范

## 一、目录结构

```
backend/src/main/java/com/bage/study/ai/best/practice/dev/flow/
├── controller/     # REST API 控制层
├── service/        # 业务逻辑层
├── repository/     # 数据访问层
├── entity/         # 数据库实体
├── dto/            # 数据传输对象
├── config/         # 配置类
├── exception/      # 异常处理
└── util/           # 工具类
```

## 二、命名规范

| 类型 | 规则 | 示例 |
|------|------|------|
| 类名 | 大驼峰 | `UserController` |
| 方法名 | 小驼峰 | `getUserById` |
| 变量名 | 小驼峰 | `userName` |
| 常量 | 全大写+下划线 | `MAX_PAGE_SIZE` |
| 文件 | 大驼峰 | `UserService.java` |

## 三、编码规范

### 1. Controller 层
- 使用 `@RestController` 注解
- 请求方法使用 `@GetMapping`、`@PostMapping` 等
- 参数校验使用 `@Valid`

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
```

### 2. Service 层
- 接口定义业务方法
- 实现类处理具体逻辑

```java
public interface UserService {
    UserDTO getUserById(Long id);
}
```

### 3. Repository 层
- 使用 Spring Data JPA
- 方法命名遵循约定

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

### 4. DTO 规范
- 请求 DTO：`{功能}Request`
- 响应 DTO：`{功能}Response` 或直接 `{实体}DTO`

## 四、错误处理

- 使用全局异常处理器 `@RestControllerAdvice`
- 统一错误响应格式

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("USER_NOT_FOUND", e.getMessage()));
    }
}
```

## 五、测试文档规范

每个功能模块必须创建测试文档：

```
docs/features/{功能}/
└── {功能}-测试文档-{日期}.md
```

**内容要求：**
- 测试概述和范围
- 单元测试用例清单
- 接口测试用例清单
- 测试数据说明
- 测试执行记录
- 缺陷记录
- 测试覆盖率统计