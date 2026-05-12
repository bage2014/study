# 安全规则
# 强制安全编码规范，防止常见安全漏洞

## 规则说明

本规则强制执行安全编码标准，防止 SQL 注入、XSS、敏感信息泄露等安全漏洞。

## 禁止的行为

### 1. SQL 注入

**严禁使用**：字符串拼接构建 SQL 语句

**正确做法**：使用参数化查询（PreparedStatement）或 ORM 框架

```java
// ❌ 禁止：字符串拼接
String sql = "SELECT * FROM users WHERE username = '" + username + "'";

// ✅ 正确：使用 JPA Repository
Optional<User> user = userRepository.findByUsername(username);

// ✅ 正确：使用参数化查询
@Query("SELECT u FROM User u WHERE u.username = :username")
Optional<User> findByUsername(@Param("username") String username);
```

### 2. 硬编码密钥

**严禁**：在代码中硬编码密码、密钥、API Token 等敏感信息

**正确做法**：使用环境变量或配置文件

```java
// ❌ 禁止
private static final String API_KEY = "sk-1234567890abcdef";

// ✅ 正确：从环境变量或配置获取
private static final String API_KEY = System.getenv("API_KEY");
```

### 3. XSS 跨站脚本

**严禁**：直接输出未转义的用户输入

**正确做法**：对输出进行转义

```html
<!-- ❌ 禁止：直接输出用户输入 -->
<div>{{{userInput}}}</div>

<!-- ✅ 正确：使用双括号转义 -->
<div>{{userInput}}</div>
```

### 4. 敏感信息日志

**严禁**：在日志中记录密码、密钥等敏感信息

```java
// ❌ 禁止
log.info("User login: username={}, password={}", username, password);

// ✅ 正确
log.info("User login: username={}", username);
```

## 输入验证

### 1. 参数校验

所有用户输入必须进行校验：

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
    // 使用 @Valid 注解触发校验
    // 自定义校验注解进行复杂校验
}
```

### 2. 校验规则

| 字段 | 校验规则 |
|------|----------|
| 用户名 | 非空，长度 3-20 字符 |
| 邮箱 | 非空，符合邮箱格式 |
| 密码 | 至少 6 位 |
| 经度 | -180 ~ 180 |
| 纬度 | -90 ~ 90 |

### 3. 轨迹数据安全

**地理位置数据保护：**
- 用户轨迹数据属于隐私信息，必须妥善保护
- 轨迹数据只能被创建者本人访问
- API 必须验证用户身份，确保用户只能访问自己的轨迹
- 轨迹数据传输必须使用 HTTPS

```java
// ✅ 正确：验证用户权限
public List<TrackPoint> getTrackPoints(Long userId) {
    // 验证当前用户是否有权限访问
    if (!isCurrentUser(userId)) {
        throw new SecurityException("无权访问该用户的轨迹");
    }
    return trackPointRepository.findByUserIdOrderByCreatedAtAsc(userId);
}
```

## 输出控制

### 1. 敏感字段过滤

用户密码等敏感字段严禁返回给前端：

```java
// ❌ 禁止：返回密码
return user;

// ✅ 正确：使用 DTO，不包含密码
return UserDTO.from(user);
```

### 2. 错误信息控制

对外暴露的错误信息不能包含内部实现细节：

```java
// ❌ 禁止：暴露内部信息
return "Database error: " + e.getMessage();

// ✅ 正确：返回通用错误信息
return "系统繁忙，请稍后重试";
```

## 安全配置

### 1. CORS 配置

合理配置跨域访问：

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
```

### 2. 密码加密

必须使用 BCrypt 加密密码：

```java
@Autowired
private PasswordEncoder passwordEncoder;

// 密码加密
String encryptedPassword = passwordEncoder.encode(rawPassword);

// 密码验证
boolean matches = passwordEncoder.matches(rawPassword, encryptedPassword);
```

## 违规处理

- 发现 SQL 注入风险：立即修复，拒绝提交
- 发现硬编码密钥：立即移除，拒绝提交
- 发现 XSS 风险：立即修复，拒绝提交
- 日志记录敏感信息：立即移除，拒绝提交