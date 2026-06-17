---
name: "common-coding"
description: "编码技能。提供代码编写指导，遵循编码规范和最佳实践。"
---

# 编码技能

## 功能描述

提供代码编写指导能力，帮助开发者遵循编码规范和最佳实践，提高代码质量和可维护性。

## 何时使用

在以下情况调用此技能：
- 需要编写新代码时
- 需要审查代码质量时
- 需要优化现有代码时
- 需要生成代码文档时

## 核心功能

- **代码生成**：根据需求生成符合规范的代码
- **代码审查**：检查代码是否符合编码规范
- **代码优化**：提供代码优化建议
- **文档生成**：生成代码文档和注释
- **设计模式推荐**：根据场景推荐合适的设计模式

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| language | String | 是 | 目标编程语言（Java/Python/JavaScript等） |
| framework | String | 否 | 使用的框架（Spring Boot/React/Vue等） |
| requirement | String | 是 | 功能需求描述 |
| styleGuide | String | 否 | 编码风格指南名称 |

## 输出格式

```json
{
  "status": "SUCCESS",
  "language": "Java",
  "framework": "Spring Boot",
  "code": "生成的代码内容",
  "explanation": "代码解释和说明",
  "bestPractices": ["最佳实践1", "最佳实践2"],
  "improvements": [
    {
      "issue": "问题描述",
      "suggestion": "改进建议",
      "severity": "高/中/低"
    }
  ]
}
```

## 编码流程

```
需求分析 → 技术选型 → 代码设计 → 代码实现 → 代码审查 → 优化改进 → 文档生成
```

### 详细步骤

1. **需求分析**：理解业务需求，确定实现方案
2. **技术选型**：选择合适的技术栈和框架
3. **代码设计**：设计类结构、方法签名和数据模型
4. **代码实现**：编写符合规范的代码
5. **代码审查**：检查代码质量和规范合规性
6. **优化改进**：根据审查结果进行优化
7. **文档生成**：编写代码注释和文档

---

## 1. 自定义异常规范

### 1.1 异常体系结构

```
BusinessException (基础业务异常)
├── IllegalArgumentException (参数异常)
├── RemoteException (远程调用异常)
├── DatabaseException (数据库异常)
├── ServiceException (服务异常)
├── NotFoundException (资源未找到)
└── ForbiddenException (权限不足)
```

### 1.2 基础异常定义

```java
/**
 * 业务异常基类
 */
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    private final String detail;
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.detail = null;
    }
    
    public BusinessException(String errorCode, String message, String detail) {
        super(message);
        this.errorCode = errorCode;
        this.detail = detail;
    }
    
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.detail = null;
    }
    
    // getters
}
```

### 1.3 常用异常子类

```java
/**
 * 参数异常 - 用于参数校验失败
 */
public class IllegalArgumentException extends BusinessException {
    public IllegalArgumentException(String message) {
        super("INVALID_PARAM", message);
    }
    
    public IllegalArgumentException(String field, String message) {
        super("INVALID_PARAM", message, field);
    }
}

/**
 * 远程调用异常 - 用于外部服务调用失败
 */
public class RemoteException extends BusinessException {
    private final String remoteService;
    
    public RemoteException(String remoteService, String message) {
        super("REMOTE_ERROR", message);
        this.remoteService = remoteService;
    }
    
    public RemoteException(String remoteService, String message, Throwable cause) {
        super("REMOTE_ERROR", message, cause);
        this.remoteService = remoteService;
    }
}

/**
 * 数据库异常 - 用于数据库操作失败
 */
public class DatabaseException extends BusinessException {
    public DatabaseException(String message) {
        super("DB_ERROR", message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super("DB_ERROR", message, cause);
    }
}

/**
 * 资源未找到异常
 */
public class NotFoundException extends BusinessException {
    public NotFoundException(String resourceType, String identifier) {
        super("NOT_FOUND", resourceType + "不存在: " + identifier);
    }
}

/**
 * 权限不足异常
 */
public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message) {
        super("FORBIDDEN", message);
    }
}
```

### 1.4 异常使用规范

| 场景 | 使用异常 | 错误码 |
|------|----------|--------|
| 参数校验失败 | IllegalArgumentException | INVALID_PARAM |
| 外部服务调用失败 | RemoteException | REMOTE_ERROR |
| 数据库操作失败 | DatabaseException | DB_ERROR |
| 资源不存在 | NotFoundException | NOT_FOUND |
| 权限不足 | ForbiddenException | FORBIDDEN |
| 业务规则校验失败 | BusinessException | 自定义 |

---

## 2. 日志与埋点规范

### 2.1 日志切面 (LogAspect)

```java
/**
 * 日志切面 - 统一记录方法调用日志
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    
    @Around("execution(* com.bage.xx..*Controller.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.info("[Controller] {}#{} 开始执行", className, methodName);
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("[Controller] {}#{} 执行完成, 耗时: {}ms", className, methodName, duration);
            return result;
        } catch (Throwable e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[Controller] {}#{} 执行失败, 耗时: {}ms, 异常: {}", 
                className, methodName, duration, e.getMessage());
            throw e;
        }
    }
    
    @Around("execution(* com.bage.xx..*Service.*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        // Service 层日志记录...
    }
}
```

### 2.2 埋点切面 (MetricsAspect)

```java
/**
 * 埋点切面 - 统一记录业务指标
 */
@Aspect
@Component
public class MetricsAspect {
    
    private final MeterRegistry meterRegistry;
    
    public MetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    @Around("@annotation(com.bage.xx.annotation.Metric)")
    public Object recordMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Metric metricAnnotation = signature.getMethod().getAnnotation(Metric.class);
        
        String metricName = metricAnnotation.value();
        Timer.Sample sample = Timer.start(meterRegistry);
        
        try {
            Object result = joinPoint.proceed();
            sample.stop(Timer.builder(metricName)
                .tag("result", "success")
                .register(meterRegistry));
            return result;
        } catch (Throwable e) {
            sample.stop(Timer.builder(metricName)
                .tag("result", "failure")
                .tag("error", e.getClass().getSimpleName())
                .register(meterRegistry));
            throw e;
        }
    }
}
```

### 2.3 埋点注解

```java
/**
 * 埋点注解 - 标记需要记录指标的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metric {
    String value();  // 指标名称
    String description() default "";  // 指标描述
}
```

### 2.4 日志级别使用规范

| 级别 | 用途 | 使用场景 |
|------|------|----------|
| DEBUG | 详细的调试信息 | 开发调试、详细参数输出 |
| INFO | 一般信息 | 方法入口、关键业务步骤 |
| WARN | 警告信息 | 异常情况但不影响流程 |
| ERROR | 错误信息 | 异常、失败操作 |

---

## 3. 分层架构规范

### 3.1 分层结构

```
┌─────────────────────────────────────────────────────────────────┐
│                      Controller 层                            │
│  职责：接收请求、参数校验、调用 Service、返回响应               │
│  依赖：Service                                                │
├─────────────────────────────────────────────────────────────────┤
│                      Service 层                               │
│  职责：业务编排、事务管理、调用 Domain Service/Repository       │
│  依赖：Domain Service、Repository、Adapter                     │
├─────────────────────────────────────────────────────────────────┤
│                  Domain Service 层                            │
│  职责：核心业务逻辑、业务规则、可复用业务组件                   │
│  依赖：Repository、领域模型                                    │
├─────────────────────────────────────────────────────────────────┤
│                      Repository 层                            │
│  职责：数据访问、数据库操作                                   │
│  依赖：Entity、DataSource                                     │
├─────────────────────────────────────────────────────────────────┤
│                      Adapter 层                               │
│  职责：外部系统适配、第三方服务调用                             │
│  依赖：外部服务                                               │
└─────────────────────────────────────────────────────────────────┘
```

### 3.2 分层职责说明

| 层级 | 职责 | 说明 |
|------|------|------|
| Controller | REST API 入口 | 处理 HTTP 请求，参数校验，调用 Service |
| Service | 业务编排 | 事务管理，调用多个 Domain Service |
| Domain Service | 核心业务逻辑 | 可复用的业务组件，纯业务规则 |
| Repository | 数据访问 | 数据库 CRUD 操作 |
| Adapter | 外部适配 | 调用外部服务、MQ、缓存等 |

### 3.3 依赖关系

```
Controller → Service → Domain Service → Repository
                  ↘           ↓
                   ↘      Adapter
                    ↘          ↓
                     → 外部系统
```

### 3.4 代码示例

**Controller**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
```

**Service**
```java
@Service
public class UserService {
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // 业务编排
        User user = userDomainService.createUserEntity(request);
        return UserResponse.fromEntity(userRepository.save(user));
    }
}
```

**Domain Service**
```java
@Component
public class UserDomainService {
    public User createUserEntity(UserCreateRequest request) {
        // 核心业务逻辑
        validateRequest(request);
        return User.builder()
            .email(request.getEmail())
            .name(request.getName())
            .build();
    }
    
    private void validateRequest(UserCreateRequest request) {
        // 业务规则校验
    }
}
```

---

## 4. 分布式锁规范

### 4.1 锁类型选择

| 场景 | 推荐锁类型 | 说明 |
|------|----------|------|
| 单服务实例 | synchronized / ReentrantLock | 进程内锁 |
| 多服务实例 | Redis 分布式锁 | 跨进程锁 |
| 数据库操作 | 数据库乐观锁 / Redis 锁 | 防止并发修改 |
| MQ 消费 | Redis 锁 / 消息幂等 | 保证消息只处理一次 |

### 4.2 Redis 分布式锁实现

```java
/**
 * 分布式锁工具类
 */
@Component
public class DistributedLock {
    
    private final StringRedisTemplate redisTemplate;
    private static final String LOCK_PREFIX = "lock:";
    private static final long DEFAULT_EXPIRE = 30000L;  // 30秒
    
    public boolean tryLock(String key) {
        return tryLock(key, DEFAULT_EXPIRE);
    }
    
    public boolean tryLock(String key, long expireMillis) {
        String lockKey = LOCK_PREFIX + key;
        String value = UUID.randomUUID().toString();
        
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, value, expireMillis, TimeUnit.MILLISECONDS);
        
        return Boolean.TRUE.equals(success);
    }
    
    public void unlock(String key) {
        String lockKey = LOCK_PREFIX + key;
        redisTemplate.delete(lockKey);
    }
    
    /**
     * 带重试的锁获取
     */
    public boolean tryLockWithRetry(String key, int maxRetries, long waitMillis) {
        for (int i = 0; i < maxRetries; i++) {
            if (tryLock(key)) {
                return true;
            }
            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}
```

### 4.3 分布式锁使用规范

```java
@Service
public class OrderService {
    private final DistributedLock distributedLock;
    
    public void createOrder(String userId) {
        String lockKey = "order:create:" + userId;
        
        if (!distributedLock.tryLockWithRetry(lockKey, 3, 500)) {
            throw new BusinessException("LOCK_ERROR", "操作过于频繁，请稍后重试");
        }
        
        try {
            // 业务逻辑
        } finally {
            distributedLock.unlock(lockKey);
        }
    }
}
```

---

## 5. 线程池规范

### 5.1 线程池配置

```java
/**
 * 线程池配置
 */
@Configuration
public class ThreadPoolConfig {
    
    @Bean
    public ExecutorService businessExecutor() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = corePoolSize * 2;
        
        return new ThreadPoolExecutor(
            corePoolSize,                           // 核心线程数
            maxPoolSize,                            // 最大线程数
            60L,                                   // 空闲线程存活时间
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),        // 任务队列
            new ThreadFactory() {                   // 线程工厂
                private final AtomicInteger counter = new AtomicInteger(0);
                
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("business-pool-" + counter.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略
        );
    }
    
    @Bean
    public ExecutorService ioExecutor() {
        // IO密集型任务线程池
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 4;
        // ...
    }
}
```

### 5.2 线程池使用规范

| 任务类型 | 线程池类型 | 核心线程数 | 说明 |
|----------|----------|----------|------|
| CPU密集型 | businessExecutor | CPU核心数 | 计算任务 |
| IO密集型 | ioExecutor | CPU核心数 * 4 | 数据库、网络调用 |
| 定时任务 | schedulerExecutor | 按需配置 | 定时任务 |

### 5.3 线程池使用示例

```java
@Service
public class AsyncService {
    private final ExecutorService businessExecutor;
    
    public CompletableFuture<Result> executeAsyncTask(String param) {
        return CompletableFuture.supplyAsync(() -> {
            // 异步任务逻辑
            return processTask(param);
        }, businessExecutor);
    }
}
```

---

## 编码规范检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 命名规范 | 变量、方法、类命名是否符合规范 | ✅/❌ |
| 代码格式 | 缩进、空格、换行是否符合规范 | ✅/❌ |
| 注释完整 | 是否有必要的注释说明 | ✅/❌ |
| 异常处理 | 是否正确使用自定义异常 | ✅/❌ |
| 代码复用 | 是否避免重复代码 | ✅/❌ |
| 性能优化 | 是否考虑性能问题 | ✅/❌ |
| 安全性 | 是否存在安全隐患 | ✅/❌ |
| 可测试性 | 代码是否易于测试 | ✅/❌ |
| 分层架构 | 是否遵循 Controller-Service-Domain-Repo 分层 | ✅/❌ |
| 日志埋点 | 是否使用统一切面记录 | ✅/❌ |
| 分布式锁 | 是否正确使用分布式锁 | ✅/❌ |
| 线程池 | 是否使用统一线程池 | ✅/❌ |

---

## 最佳实践指南

### Java 编码规范

1. **命名规范**：类名使用 PascalCase，方法和变量使用 camelCase
2. **代码格式**：使用 4 空格缩进，每行不超过 120 字符
3. **注释规范**：使用 Javadoc 格式，方法必须有注释
4. **异常处理**：优先使用自定义业务异常，避免抛出底层异常
5. **设计原则**：遵循 SOLID 原则，单一职责

### 架构设计原则

1. **分层清晰**：严格遵循 Controller → Service → Domain Service → Repository 依赖链
2. **依赖倒置**：高层模块不依赖低层模块，都依赖抽象
3. **接口隔离**：使用接口定义服务契约
4. **开闭原则**：对扩展开放，对修改关闭

---

## 核心组件

| 组件 | 职责 | 描述 |
|------|------|------|
| CodeGenerator | 代码生成器 | 根据需求生成代码 |
| CodeAnalyzer | 代码分析器 | 分析代码质量和规范 |
| PatternRecommender | 模式推荐器 | 推荐合适的设计模式 |
| DocGenerator | 文档生成器 | 生成代码文档 |

---

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| CODING_STYLE_GUIDE | 默认编码风格指南 | google |

### 配置文件

```yaml
coding:
  style-guide: google
  max-line-length: 120
  enforce-type-hints: true
```

---

## 扩展指南

### 添加新语言支持

1. 创建对应语言的代码生成器
2. 定义该语言的编码规范规则
3. 添加代码分析和检查逻辑

### 添加自定义规则

1. 在规则引擎中添加新规则
2. 定义规则的检查逻辑和修复建议
3. 更新编码规范检查清单