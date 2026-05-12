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

## 四、统一响应格式

### 1. RestResult 响应对象

所有 API 响应必须使用统一的 `RestResult<T>` 格式：

```java
public class RestResult<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;

    public RestResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> RestResult<T> success(T data) {
        RestResult<T> result = new RestResult<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> RestResult<T> success(String message, T data) {
        RestResult<T> result = new RestResult<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> RestResult<T> error(int code, String message) {
        RestResult<T> result = new RestResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> RestResult<T> error(String message) {
        return error(500, message);
    }

    // Getters and Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
```

### 2. 状态码定义

| 状态码 | 含义 | 说明 |
|--------|------|------|
| 200 | 成功 | 操作成功 |
| 400 | 请求参数错误 | 参数校验失败 |
| 401 | 未授权 | 用户未登录或token失效 |
| 403 | 禁止访问 | 用户无权限 |
| 404 | 资源不存在 | 请求的资源未找到 |
| 500 | 服务器内部错误 | 系统异常 |

### 3. 响应示例

成功响应：
```json
{
  "code": 200,
  "message": "success",
  "data": { "id": 1, "username": "admin" },
  "timestamp": 1715487600000
}
```

错误响应：
```json
{
  "code": 404,
  "message": "用户不存在",
  "data": null,
  "timestamp": 1715487600000
}
```

## 五、错误处理

- 使用全局异常处理器 `@RestControllerAdvice`
- 统一错误响应格式

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResult<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(RestResult.error(400, message));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestResult<Void>> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(RestResult.error(404, e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RestResult<Void>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(RestResult.error(401, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResult<Void>> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(RestResult.error(500, "系统繁忙，请稍后重试"));
    }
}
```

## 六、测试文档规范

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