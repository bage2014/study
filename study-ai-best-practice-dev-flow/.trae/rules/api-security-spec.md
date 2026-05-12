# API安全规范

## 一、认证机制

### 1.1 JWT 认证

#### 1.1.1 Token结构

```
Header.Payload.Signature
```

#### 1.1.2 Payload 字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `sub` | String | 用户ID |
| `username` | String | 用户名 |
| `exp` | Long | 过期时间戳 |
| `iat` | Long | 签发时间戳 |
| `iss` | String | 签发者 |

#### 1.1.3 Token存储

- 前端存储：`localStorage` 或 `sessionStorage`
- 后端验证：使用 `JJWT` 库解析和验证

```java
@Component
public class JwtTokenProvider {

    private final String secret = System.getenv("JWT_SECRET");
    private final long validityInMilliseconds = 3600000; // 1小时

    public String createToken(String username, Long userId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", userId);
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException("Invalid JWT token");
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret)
            .parseClaimsJws(token).getBody().getSubject();
    }
}
```

### 1.2 刷新Token机制

| 类型 | 有效期 | 用途 |
|------|--------|------|
| Access Token | 1小时 | 日常API访问 |
| Refresh Token | 7天 | 获取新的Access Token |

## 二、授权机制

### 2.1 基于角色的访问控制（RBAC）

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/users")
public ResponseEntity<List<UserDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
}
```

### 2.2 权限注解

| 注解 | 说明 |
|------|------|
| `@PreAuthorize` | 方法执行前检查权限 |
| `@PostAuthorize` | 方法执行后检查权限 |
| `@Secured` | 基于角色的安全检查 |
| `@RolesAllowed` | JSR-250角色检查 |

## 三、请求安全

### 3.1 请求头安全

| 请求头 | 说明 | 验证要求 |
|--------|------|----------|
| `Authorization` | Bearer Token | 必填（登录后） |
| `X-Request-Id` | 请求唯一标识 | 必填 |
| `X-Csrf-Token` | 防CSRF攻击 | 必填（POST请求） |

### 3.2 CSRF防护

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

### 3.3 请求频率限制

使用 `@RateLimiter` 注解限制API调用频率：

```java
@RateLimiter(name = "user", fallbackMethod = "rateLimitFallback")
@GetMapping("/users")
public ResponseEntity<List<UserDTO>> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
}

public ResponseEntity<RestResult<Void>> rateLimitFallback(Exception e) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body(RestResult.error(429, "请求过于频繁，请稍后重试"));
}
```

## 四、数据安全

### 4.1 输入验证

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 64, message = "用户名长度必须在3-64个字符之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 128, message = "密码长度必须在6-128个字符之间")
    private String password;
}
```

### 4.2 敏感数据脱敏

```java
public class UserDTO {
    
    private Long id;
    private String username;
    
    @JsonSerialize(using = SensitiveInfoSerializer.class)
    private String phone;
    
    @JsonSerialize(using = SensitiveInfoSerializer.class)
    private String email;
}
```

### 4.3 密码安全

```java
@Service
public class PasswordService {
    
    private final PasswordEncoder passwordEncoder;
    
    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

## 五、传输安全

### 5.1 HTTPS强制

```java
@Configuration
public class HttpsConfig {
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/health");
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .requiresChannel(channel -> channel
                .anyRequest().requiresSecure()
            );
        return http.build();
    }
}
```

### 5.2 CORS配置

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("https://example.com")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
}
```

## 六、日志安全

### 6.1 敏感信息过滤

```java
@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    
    @Pointcut("execution(* com.bage.study.ai.best.practice.dev.flow.controller..*.*(..))")
    public void controllerMethods() {}
    
    @Around("controllerMethods()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        log.info("Request: {} - Args: {}", methodName, maskSensitiveData(args));
        
        Object result = joinPoint.proceed();
        
        long duration = System.currentTimeMillis() - startTime;
        log.info("Response: {} - Duration: {}ms", methodName, duration);
        
        return result;
    }
    
    private Object maskSensitiveData(Object[] args) {
        // 脱敏处理：密码、手机号、身份证等
        return Arrays.stream(args)
            .map(arg -> maskIfSensitive(arg))
            .toArray();
    }
}
```

## 七、异常安全

### 7.1 异常处理策略

```java
@RestControllerAdvice
public class SecurityExceptionHandler {
    
    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<RestResult<Void>> handleInvalidJwtToken(InvalidJwtTokenException e) {
        log.warn("Invalid JWT token: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(RestResult.error(401, "登录失效，请重新登录"));
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestResult<Void>> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(RestResult.error(403, "无权限访问"));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResult<Void>> handleException(Exception e) {
        log.error("System error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(RestResult.error(500, "系统繁忙，请稍后重试"));
    }
}
```

## 八、安全审计

### 8.1 审计日志记录

```java
@Entity
@Table(name = "sys_audit_log")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "operation")
    private String operation;
    
    @Column(name = "target")
    private String target;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

### 8.2 审计切面

```java
@Aspect
@Component
public class AuditAspect {
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @AfterReturning(pointcut = "@annotation(com.bage.study.ai.annotation.Audit)", returning = "result")
    public void audit(JoinPoint joinPoint, Object result) {
        AuditLog log = new AuditLog();
        log.setUserId(getCurrentUserId());
        log.setUsername(getCurrentUsername());
        log.setOperation(joinPoint.getSignature().getName());
        log.setTarget(getTargetInfo(joinPoint));
        log.setIpAddress(getClientIpAddress());
        log.setUserAgent(getUserAgent());
        log.setCreatedAt(LocalDateTime.now());
        
        auditLogRepository.save(log);
    }
}
```

## 九、安全检查清单

| 检查项 | 检查内容 | 状态 |
|--------|----------|------|
| 认证机制 | JWT Token验证是否正确 | ✅ |
| 授权控制 | 角色和权限检查是否完整 | ✅ |
| 输入验证 | 参数校验是否完善 | ✅ |
| 密码安全 | 是否使用BCrypt加密 | ✅ |
| HTTPS | 是否强制HTTPS访问 | ✅ |
| CORS | 是否正确配置跨域 | ✅ |
| CSRF | 是否启用CSRF防护 | ✅ |
| 日志安全 | 是否过滤敏感信息 | ✅ |
| 异常处理 | 是否隐藏内部错误信息 | ✅ |
| 审计日志 | 是否记录关键操作 | ✅ |