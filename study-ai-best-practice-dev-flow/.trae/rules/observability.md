# 可观测性规则
# 强制添加日志、耗时记录、Prometheus 指标

## 规则说明

本规则要求所有服务和 API 必须具备可观测性能力，包括日志记录、性能指标和链路追踪。

## 日志规范

### 1. 日志级别使用

| 级别 | 使用场景 |
|------|----------|
| ERROR | 错误异常，影响功能 |
| WARN | 警告信息，潜在问题 |
| INFO | 重要业务事件 |
| DEBUG | 开发调试信息 |

### 2. 日志内容要求

每个 API 必须记录以下日志：

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        log.info("获取用户信息: userId={}", id);

        try {
            User user = userService.getUserById(id);
            long duration = System.currentTimeMillis() - startTime;
            log.info("获取用户信息成功: userId={}, duration={}ms", id, duration);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("获取用户信息失败: userId={}, duration={}ms, error={}", id, duration, e.getMessage());
            throw e;
        }
    }
}
```

### 3. 日志格式

必须包含以下字段：
- 时间戳
- 日志级别
- 请求 ID（用于链路追踪）
- 用户 ID（如果已登录）
- 操作描述
- 耗时
- 错误信息（如果异常）

## 性能指标

### 1. 关键指标

| 指标 | 描述 | 告警阈值 |
|------|------|----------|
| API 响应时间 | P95 响应时间 | ≤ 500ms |
| API 错误率 | 5xx 错误占比 | ≤ 1% |
| 数据库查询时间 | 单次查询时间 | ≤ 100ms |
| JVM 内存使用 | 堆内存使用率 | ≤ 80% |

### 2. 使用 Micrometer 记录指标

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final MeterRegistry meterRegistry;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            User user = userService.getUserById(id);
            sample.stop(meterRegistry.timer("user.get.by.id", "status", "success"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            sample.stop(meterRegistry.timer("user.get.by.id", "status", "error"));
            throw e;
        }
    }
}
```

## 链路追踪

### 1. 使用 Trace ID

每个请求必须携带 Trace ID：

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {

        String currentTraceId = traceId != null ? traceId : UUID.randomUUID().toString();
        MDC.put("traceId", currentTraceId);

        log.info("获取用户信息: traceId={}, userId={}", currentTraceId, id);
        // ... 业务逻辑
    }
}
```

### 2. 日志配置

配置 Logback 支持 MDC：

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%X{traceId}] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
</configuration>
```

## API 耗时记录

### 1. 拦截器方式

使用拦截器自动记录 API 耗时：

```java
@Component
public class PerformanceInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PerformanceInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        log.info("API: {} {} - {}ms", request.getMethod(), request.getRequestURI(), duration);

        if (duration > 500) {
            log.warn("API 响应时间超过 500ms: {} {} - {}ms", request.getMethod(), request.getRequestURI(), duration);
        }
    }
}
```

## 报告生成

### 1. 指标端点

必须实现 `/actuator/prometheus` 端点用于 Prometheus 抓取指标。

### 2. 健康检查

必须实现 `/actuator/health` 端点用于健康检查。

## 模块特定日志要求

### 轨迹模块日志

轨迹 API 必须记录以下信息：
- 用户 ID（轨迹所属用户）
- 轨迹点数量
- 坐标范围（用于性能监控）
- 地图加载时间

```java
@RestController
@RequestMapping("/api/trackpoints")
public class TrackPointController {

    private static final Logger log = LoggerFactory.getLogger(TrackPointController.class);

    @GetMapping
    public ResponseEntity<List<TrackPointDTO>> getTrackPoints(@RequestParam Long userId) {
        long startTime = System.currentTimeMillis();
        log.info("获取轨迹点列表: userId={}", userId);

        List<TrackPointDTO> points = trackPointService.getTrackPointsByUserId(userId);
        long duration = System.currentTimeMillis() - startTime;
        log.info("获取轨迹点成功: userId={}, count={}, duration={}ms", userId, points.size(), duration);

        return ResponseEntity.ok(points);
    }
}
```

### 用户管理模块日志

用户管理 API 必须记录：
- 操作类型（创建、更新、删除）
- 目标用户 ID
- 操作结果

## 违规处理

- 未记录关键日志：要求补充日志后提交
- API 响应时间超过阈值：优化代码或添加缓存
- 轨迹模块未记录坐标信息：要求补充日志