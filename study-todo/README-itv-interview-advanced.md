# 高级工程师面试知识点补充

## 文档说明

本文档针对有10年+经验的高级工程师和架构师级别面试，补充核心知识点和深度分析。

---

## 目录

1. [Java 高级特性](#1-java-高级特性)
2. [分布式系统设计](#2-分布式系统设计)
3. [安全进阶](#3-安全进阶)
4. [架构设计与实践](#4-架构设计与实践)
5. [性能优化与调优](#5-性能优化与调优)
6. [故障排查与稳定性保障](#6-故障排查与稳定性保障)

---

## 1. Java 高级特性

### 1.1 Java 17+ 新特性

#### 1.1.1 虚拟线程 (Virtual Threads)

```java
// 虚拟线程创建方式
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;

// 方式1：使用 Executors.newVirtualThreadPerTaskExecutor()
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> {
        System.out.println("Running in virtual thread");
    });
}

// 方式2：使用 Thread.ofVirtual()
Thread virtualThread = Thread.ofVirtual().name("my-vt").start(() -> {
    System.out.println("Virtual thread started");
});

// 方式3：结构化并发 (Java 21+)
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var future1 = scope.fork(() -> fetchData("service1"));
    var future2 = scope.fork(() -> fetchData("service2"));
    scope.join();
    // 处理结果
}
```

**面试要点**：
- 虚拟线程与平台线程的区别
- 适用场景：IO密集型任务、高并发场景
- 调度模型：M:N调度
- 注意事项：不要在虚拟线程中使用ThreadLocal

#### 1.1.2 模式匹配 (Pattern Matching)

```java
// instanceof 模式匹配
if (obj instanceof String s && s.length() > 0) {
    System.out.println(s.toUpperCase());
}

// switch 模式匹配
public String format(Object obj) {
    return switch (obj) {
        case Integer i -> String.format("Integer: %d", i);
        case String s -> String.format("String: %s", s);
        case null -> "null";
        default -> obj.toString();
    };
}

// 记录模式 (Record Patterns)
record Point(int x, int y) {}

public void printPoint(Object obj) {
    if (obj instanceof Point(var x, var y)) {
        System.out.println("Point: (" + x + ", " + y + ")");
    }
}
```

#### 1.1.3 密封类 (Sealed Classes)

```java
// 密封类定义
public sealed interface Shape 
    permits Circle, Rectangle, Triangle {}

public final class Circle implements Shape {
    private final double radius;
    // ...
}

public final class Rectangle implements Shape {
    private final double width, height;
    // ...
}

public final class Triangle implements Shape {
    private final double a, b, c;
    // ...
}
```

**面试要点**：
- 密封类的设计目的：限制继承层次
- 与final类的区别
- 使用场景：表达式类型安全、API设计

#### 1.1.4 记录类 (Records)

```java
// 记录类定义
public record User(Long id, String name, String email) {}

// 使用示例
User user = new User(1L, "John", "john@example.com");
System.out.println(user.name()); // 自动生成访问方法

// 自定义行为
public record Point(int x, int y) {
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be non-negative");
        }
    }
    
    public double distanceFromOrigin() {
        return Math.sqrt(x * x + y * y);
    }
}
```

### 1.2 并发编程进阶

#### 1.2.1 CompletableFuture 高级用法

```java
// 组合多个异步任务
CompletableFuture<String> task1 = fetchFromService1();
CompletableFuture<String> task2 = fetchFromService2();
CompletableFuture<String> task3 = fetchFromService3();

// 等待所有任务完成
CompletableFuture.allOf(task1, task2, task3)
    .thenApply(v -> {
        String result1 = task1.join();
        String result2 = task2.join();
        String result3 = task3.join();
        return combineResults(result1, result2, result3);
    })
    .exceptionally(e -> {
        log.error("Task failed", e);
        return "default";
    });

// 选择最快完成的任务
CompletableFuture.anyOf(task1, task2)
    .thenAccept(result -> {
        System.out.println("First result: " + result);
    });
```

#### 1.2.2 并发容器深度解析

```java
// ConcurrentHashMap 的分段锁机制
// Java 8+ 使用 CAS + synchronized 替代分段锁
ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

// computeIfAbsent 的原子性保证
map.computeIfAbsent("key", k -> {
    // 只有在key不存在时才执行
    return computeValue(k);
});

// 批量操作
map.forEach(4, (key, value) -> {
    // 4个线程并行处理
    process(key, value);
});
```

**面试要点**：
- ConcurrentHashMap 的演进（Java 7 vs Java 8）
- CAS 操作的 ABA 问题及解决
- 弱一致性迭代器

---

## 2. 分布式系统设计

### 2.1 分布式一致性算法

#### 2.1.1 Raft 算法

**核心概念**：
- **任期 (Term)**：时间分割为任期，每个任期有一个领导者
- **状态**：Follower、Candidate、Leader
- **日志复制**：Leader将日志复制到所有Follower

**选举过程**：
1. Follower超时未收到心跳，转为Candidate
2. Candidate向其他节点发送投票请求
3. 获得多数票成为Leader
4. 向所有节点发送心跳维持领导地位

**面试要点**：
- Raft vs Paxos 的区别
- 脑裂问题如何解决
- 日志复制的一致性保证

#### 2.1.2 Paxos 算法

**三个角色**：
- **Proposer**：提出提案
- **Acceptor**：接受或拒绝提案
- **Learner**：学习达成一致的值

**两阶段协议**：
1. **Prepare阶段**：Proposer向Acceptor请求准备
2. **Accept阶段**：Proposer收到多数准备响应后发送接受请求

### 2.2 分布式锁

#### 2.2.1 Redis 分布式锁

```java
// 正确的分布式锁实现
public class RedisDistributedLock {
    
    private static final String LOCK_PREFIX = "lock:";
    private static final String RELEASE_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    
    private final Jedis jedis;
    
    public boolean tryLock(String key, String requestId, long expireTime) {
        String result = jedis.set(LOCK_PREFIX + key, requestId, 
            SetParams.setParams().nx().px(expireTime));
        return "OK".equals(result);
    }
    
    public boolean releaseLock(String key, String requestId) {
        Long result = (Long) jedis.eval(RELEASE_SCRIPT, 
            Collections.singletonList(LOCK_PREFIX + key), 
            Collections.singletonList(requestId));
        return result == 1;
    }
}
```

#### 2.2.2 ZooKeeper 分布式锁

```java
// 使用 Curator 实现分布式锁
InterProcessMutex lock = new InterProcessMutex(client, "/locks/my-lock");

try {
    // 获取锁，最多等待10秒
    if (lock.acquire(10, TimeUnit.SECONDS)) {
        try {
            // 执行业务逻辑
        } finally {
            lock.release();
        }
    }
} catch (Exception e) {
    // 处理异常
}
```

**对比分析**：

| 特性 | Redis | ZooKeeper |
|------|-------|-----------|
| 一致性 | 最终一致 | 强一致 |
| 锁类型 | 非公平锁 | 公平锁 |
| 实现复杂度 | 较高 | 较低 |
| 性能 | 高 | 中 |
| 可靠性 | 依赖超时 | 基于会话 |

### 2.3 分布式 ID 生成

#### 2.3.1 Snowflake 算法

```java
public class SnowflakeIdGenerator {
    
    private static final long START_TIMESTAMP = 1609459200000L; // 2021-01-01
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;
    
    private final long workerId;
    private final long dataCenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards");
        }
        
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);
            if (sequence == 0) {
                timestamp = waitNextMillis(timestamp);
            }
        } else {
            sequence = 0L;
        }
        
        lastTimestamp = timestamp;
        
        return ((timestamp - START_TIMESTAMP) << (WORKER_ID_BITS + DATA_CENTER_ID_BITS + SEQUENCE_BITS))
                | (dataCenterId << (WORKER_ID_BITS + SEQUENCE_BITS))
                | (workerId << SEQUENCE_BITS)
                | sequence;
    }
}
```

**面试要点**：
- 时钟回拨问题如何解决
- 分布式部署时workerId的分配策略
- 与数据库自增ID的对比

### 2.4 分布式缓存策略

#### 2.4.1 缓存穿透

```java
// 布隆过滤器解决缓存穿透
public class BloomFilterCache {
    
    private final BloomFilter<String> bloomFilter;
    private final Cache<String, Object> cache;
    
    public Object get(String key) {
        // 先检查布隆过滤器
        if (!bloomFilter.mightContain(key)) {
            return null; // 肯定不存在，直接返回
        }
        
        // 再查缓存
        Object value = cache.get(key);
        if (value != null) {
            return value;
        }
        
        // 查数据库
        value = db.get(key);
        if (value != null) {
            cache.put(key, value);
        }
        
        return value;
    }
}
```

#### 2.4.2 缓存击穿

```java
// 分布式锁解决缓存击穿
public Object getWithLock(String key) {
    Object value = cache.get(key);
    if (value != null) {
        return value;
    }
    
    // 获取分布式锁
    String lockKey = "lock:" + key;
    if (redisLock.tryLock(lockKey, requestId, 30000)) {
        try {
            // 双重检查
            value = cache.get(key);
            if (value != null) {
                return value;
            }
            
            // 查询数据库并更新缓存
            value = db.get(key);
            if (value != null) {
                cache.put(key, value, 3600);
            }
        } finally {
            redisLock.releaseLock(lockKey, requestId);
        }
    } else {
        // 等待其他线程完成后重试
        Thread.sleep(100);
        return getWithLock(key);
    }
    
    return value;
}
```

#### 2.4.3 缓存雪崩

**解决方案**：
- **随机过期时间**：避免大量缓存同时过期
- **多级缓存**：本地缓存 + 分布式缓存
- **熔断降级**：缓存失效时降级处理
- **预热缓存**：提前加载热点数据

---

## 3. 安全进阶

### 3.1 零信任架构

**核心原则**：
1. **永不信任，始终验证**：不假设内部网络安全
2. **最小权限原则**：只授予必要的访问权限
3. **加密所有通信**：传输和存储都需要加密
4. **设备信任**：验证设备安全性

**实现要点**：
- **身份验证**：多因素认证(MFA)
- **微分段**：网络分段隔离
- **持续验证**：实时评估访问权限

### 3.2 密钥管理

#### 3.2.1 密钥轮换策略

```java
// 密钥轮换示例
public class KeyRotationService {
    
    private volatile String currentKey;
    private volatile String previousKey;
    private final ScheduledExecutorService scheduler;
    
    public void startRotation() {
        scheduler.scheduleAtFixedRate(() -> {
            // 生成新密钥
            String newKey = generateNewKey();
            
            // 更新密钥：先添加新密钥，再删除旧密钥
            previousKey = currentKey;
            currentKey = newKey;
            
            // 等待一段时间后删除旧密钥（确保旧请求完成）
            scheduler.schedule(() -> {
                previousKey = null;
            }, 5, TimeUnit.MINUTES);
        }, 0, 30, TimeUnit.DAYS);
    }
    
    public String decrypt(String data) {
        // 先尝试当前密钥
        try {
            return decryptWithKey(data, currentKey);
        } catch (Exception e) {
            // 失败则尝试旧密钥
            if (previousKey != null) {
                return decryptWithKey(data, previousKey);
            }
            throw e;
        }
    }
}
```

#### 3.2.2 HSM (硬件安全模块)

**应用场景**：
- 存储和管理加密密钥
- 执行加密/解密操作
- 数字签名和验证
- 密钥派生和协商

**优势**：
- 物理安全：密钥永不离开HSM
- 防篡改：硬件级保护
- 合规性：满足金融、政府安全标准

### 3.3 安全审计

**审计日志设计**：

```java
@Data
public class AuditLog {
    private String id;
    private String userId;
    private String action;
    private String resource;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime timestamp;
    private boolean success;
    private String errorMessage;
}

// 审计切面
@Aspect
@Component
public class AuditAspect {
    
    @Autowired
    private AuditLogRepository repository;
    
    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void auditSuccess(JoinPoint joinPoint, Auditable auditable, Object result) {
        AuditLog log = createLog(joinPoint, auditable);
        log.setSuccess(true);
        repository.save(log);
    }
    
    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "ex")
    public void auditFailure(JoinPoint joinPoint, Auditable auditable, Exception ex) {
        AuditLog log = createLog(joinPoint, auditable);
        log.setSuccess(false);
        log.setErrorMessage(ex.getMessage());
        repository.save(log);
    }
}
```

### 3.4 DevSecOps

**集成安全到CI/CD流程**：

| 阶段 | 安全实践 | 工具 |
|------|---------|------|
| 代码提交 | 静态代码分析 | SonarQube, Checkmarx |
| 构建 | 依赖安全扫描 | Snyk, OWASP Dependency-Check |
| 测试 | 动态安全测试 | OWASP ZAP |
| 部署 | 基础设施即代码安全 | Checkov, Terrascan |
| 运行 | 运行时应用自我保护 | RASP |

---

## 4. 架构设计与实践

### 4.1 架构设计原则

#### 4.1.1 SOLID 原则深度理解

**单一职责原则 (SRP)**：
- 一个类应该只有一个引起变化的原因
- 避免"上帝类"

**开闭原则 (OCP)**：
- 对扩展开放，对修改关闭
- 使用抽象层解耦

**里氏替换原则 (LSP)**：
- 子类应该可以替换父类
- 保持契约一致性

**接口隔离原则 (ISP)**：
- 客户端不应该依赖它不需要的接口
- 细粒度接口设计

**依赖倒置原则 (DIP)**：
- 依赖抽象而非具体实现
- 高层模块不依赖低层模块

#### 4.1.2 设计模式进阶

**策略模式 vs 模板方法模式**：

```java
// 策略模式：运行时切换算法
public interface PaymentStrategy {
    void pay(double amount);
}

public class CreditCardPayment implements PaymentStrategy { ... }
public class AlipayPayment implements PaymentStrategy { ... }

// 模板方法模式：定义算法骨架
public abstract class DataProcessor {
    public final void process() {
        validate();
        parse();
        transform();
        store();
    }
    
    protected abstract void validate();
    protected abstract void parse();
    protected abstract void transform();
    protected abstract void store();
}
```

**观察者模式 vs 发布-订阅模式**：

| 特性 | 观察者模式 | 发布-订阅模式 |
|------|-----------|--------------|
| 耦合度 | 高（直接依赖） | 低（通过消息队列） |
| 同步性 | 同步 | 异步 |
| 扩展性 | 较差 | 较好 |
| 使用场景 | 进程内通信 | 跨服务通信 |

### 4.2 技术选型决策框架

**决策因素**：

```
业务需求 → 功能特性 → 性能要求 → 可扩展性 → 团队能力 → 生态成熟度 → 成本
```

**决策矩阵**：

| 维度 | 权重 | 评估指标 |
|------|------|---------|
| 功能匹配 | 30% | 是否满足业务需求 |
| 性能 | 20% | 吞吐量、延迟、并发能力 |
| 可扩展性 | 20% | 水平扩展、垂直扩展能力 |
| 团队熟悉度 | 15% | 学习成本、维护成本 |
| 生态成熟度 | 10% | 社区活跃度、文档完善度 |
| 成本 | 5% | 许可证费用、运维成本 |

### 4.3 架构演进路径

**单体架构 → 垂直拆分 → 微服务 → 服务网格**

**演进阶段**：

1. **单体阶段**：适合初创期，快速迭代
2. **垂直拆分**：按业务域拆分，降低耦合
3. **微服务**：细粒度拆分，独立部署
4. **服务网格**：统一管理服务间通信

**拆分策略**：
- **按业务能力拆分**：用户、订单、支付等
- **按数据边界拆分**：避免跨服务事务
- **按团队边界拆分**：Conway定律

---

## 5. 性能优化与调优

### 5.1 JVM 调优实战

#### 5.1.1 GC 调优策略

```java
// 常用JVM参数
java -XX:+UseG1GC \
     -Xms8g -Xmx8g \
     -XX:MaxGCPauseMillis=200 \
     -XX:InitiatingHeapOccupancyPercent=45 \
     -XX:ConcGCThreads=4 \
     -XX:ParallelGCThreads=8 \
     -XX:+PrintGCDetails \
     -XX:+PrintGCTimeStamps \
     -Xloggc:gc.log
```

**GC 日志分析**：

```
// G1 GC 日志示例
2024-01-15T10:00:00.000+0800: 10.234: [GC pause (G1 Evacuation Pause) (young), 0.0234567 secs]
   [Parallel Time: 20.123 ms, GC Workers: 8]
   [GC Worker Start (ms): Min: 10234.567, Avg: 10234.578, Max: 10234.589, Diff: 0.022]
   [Ext Root Scanning (ms): Min: 0.123, Avg: 0.156, Max: 0.234, Diff: 0.111]
   [Update RS (ms): Min: 0.001, Avg: 0.002, Max: 0.003, Diff: 0.002]
   [Process Buffers (ms): Min: 0.001, Avg: 0.002, Max: 0.003, Diff: 0.002]
   [Scan RS (ms): Min: 0.001, Avg: 0.002, Max: 0.003, Diff: 0.002]
   [Object Copy (ms): Min: 15.123, Avg: 16.234, Max: 17.345, Diff: 2.222]
   [Termination (ms): Min: 0.001, Avg: 0.002, Max: 0.003, Diff: 0.002]
   [GC Worker Other (ms): Min: 0.001, Avg: 0.002, Max: 0.003, Diff: 0.002]
   [GC Worker Total (ms): Min: 19.876, Avg: 20.012, Max: 20.123, Diff: 0.247]
   [GC Worker End (ms): Min: 10254.445, Avg: 10254.590, Max: 10254.712, Diff: 0.267]
   [Code Root Fixup: 0.001 ms]
   [Code Root Purge: 0.001 ms]
   [Clear CT: 0.001 ms]
   [Other: 3.333 ms]
   [Choose CSet: 0.001 ms]
   [Ref Proc: 2.123 ms]
   [Ref Enq: 0.001 ms]
   [Redirty Cards: 0.001 ms]
   [Humongous Register: 0.001 ms]
   [Humongous Reclaim: 0.001 ms]
   [Free CSet: 0.001 ms]
   [Eden: 2048.0M(2048.0M)->0.0B(2048.0M) Survivors: 256.0M->256.0M Heap: 4096.0M(8192.0M)->2048.0M(8192.0M)]
```

**调优步骤**：
1. **监控**：收集GC日志、内存使用情况
2. **分析**：识别问题（内存泄漏、频繁GC、长停顿）
3. **优化**：调整堆大小、选择合适GC、优化代码
4. **验证**：对比优化前后指标

#### 5.1.2 内存泄漏排查

```java
// 使用 WeakReference 检测内存泄漏
public class MemoryLeakDetector {
    
    private static final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private static final Set<WeakReference<Object>> references = Collections.synchronizedSet(new HashSet<>());
    
    public static void track(Object obj) {
        references.add(new WeakReference<>(obj, queue));
    }
    
    public static void checkLeaks() {
        Reference<?> ref;
        while ((ref = queue.poll()) != null) {
            System.out.println("Memory leak detected for: " + ref);
            references.remove(ref);
        }
    }
}
```

**排查工具**：
- **VisualVM**：JDK自带工具，内存分析
- **MAT**：Eclipse Memory Analyzer，深度内存分析
- **JProfiler**：商业工具，性能分析

### 5.2 数据库优化

#### 5.2.1 索引优化策略

```sql
-- 复合索引设计原则
-- 1. 最左前缀原则
CREATE INDEX idx_user_name_age ON users (name, age);
-- 可用于: WHERE name = ? AND age = ?
-- 可用于: WHERE name = ?
-- 不可用于: WHERE age = ?

-- 2. 覆盖索引：包含查询所需的所有列
CREATE INDEX idx_order_status_total ON orders (status) INCLUDE (total, create_time);

-- 3. 索引选择性：选择性高的列放在前面
-- 选择性 = 唯一值数量 / 总行数

-- 4. 避免索引滥用
-- 不适合索引的场景：
-- - 表数据量小
-- - 频繁更新的列
-- - 低选择性列（如性别）
```

#### 5.2.2 查询优化

```sql
-- 优化前：使用 SELECT *
SELECT * FROM orders WHERE status = 'completed';

-- 优化后：只查询需要的列
SELECT id, total, create_time FROM orders WHERE status = 'completed';

-- 优化前：使用 OR
SELECT * FROM users WHERE name = 'John' OR email = 'john@example.com';

-- 优化后：使用 UNION
SELECT * FROM users WHERE name = 'John'
UNION
SELECT * FROM users WHERE email = 'john@example.com';

-- 优化前：子查询
SELECT * FROM orders WHERE user_id IN (SELECT id FROM users WHERE country = 'China');

-- 优化后：JOIN
SELECT o.* FROM orders o
JOIN users u ON o.user_id = u.id
WHERE u.country = 'China';
```

#### 5.2.3 分库分表

**分库策略**：
- **按业务域分库**：用户库、订单库、支付库
- **按数据量分库**：根据数据规模拆分

**分表策略**：
- **水平分表**：按时间、按范围、按哈希
- **垂直分表**：按列拆分（冷热分离）

```java
// 按用户ID哈希分表
public class UserShardingStrategy {
    
    private static final int TABLE_COUNT = 16;
    
    public static int getTableIndex(Long userId) {
        return userId.hashCode() % TABLE_COUNT;
    }
    
    public static String getTableName(Long userId) {
        return "user_" + getTableIndex(userId);
    }
}
```

### 5.3 网络优化

#### 5.3.1 HTTP/2 与 gRPC

**HTTP/2 优势**：
- **多路复用**：单个连接多个请求
- **头部压缩**：HPACK算法
- **服务器推送**：主动推送资源

**gRPC 优势**：
- **Protocol Buffers**：高效序列化
- **HTTP/2 传输**：多路复用
- **流式通信**：双向流支持

```proto
// gRPC 定义示例
syntax = "proto3";

package example;

service UserService {
    rpc GetUser(GetUserRequest) returns (GetUserResponse);
    rpc ListUsers(ListUsersRequest) returns (stream User);
    rpc CreateUsers(stream User) returns (CreateUsersResponse);
    rpc Chat(stream Message) returns (stream Message);
}

message GetUserRequest {
    string user_id = 1;
}

message GetUserResponse {
    User user = 1;
}

message User {
    string id = 1;
    string name = 2;
    string email = 3;
}
```

---

## 6. 故障排查与稳定性保障

### 6.1 日志体系设计

**日志分级**：

| 级别 | 使用场景 | 示例 |
|------|---------|------|
| DEBUG | 详细调试信息 | 方法进入/退出、参数值 |
| INFO | 正常业务流程 | 用户登录、订单创建 |
| WARN | 警告信息 | 资源不足、重试操作 |
| ERROR | 错误信息 | 异常抛出、业务失败 |
| FATAL | 致命错误 | 系统崩溃、数据损坏 |

**日志规范**：

```java
// 正确的日志格式
log.info("用户[{}]登录成功，IP: {}", userId, ipAddress);
log.error("订单[{}]创建失败，原因: {}", orderId, e.getMessage(), e);

// 避免的错误做法
log.info("用户" + userId + "登录成功"); // 字符串拼接
log.error("订单创建失败", e); // 缺少上下文信息
```

**结构化日志**：

```json
{
    "timestamp": "2024-01-15T10:00:00.000Z",
    "level": "INFO",
    "traceId": "abc123",
    "spanId": "def456",
    "service": "user-service",
    "logger": "com.example.UserService",
    "message": "用户登录成功",
    "data": {
        "userId": "12345",
        "ipAddress": "192.168.1.1",
        "duration": 123
    }
}
```

### 6.2 链路追踪

**OpenTelemetry 实现**：

```java
// 配置 Tracer
Tracer tracer = OpenTelemetry.getTracer("my-service");

// 创建 Span
try (var scope = tracer.spanBuilder("processOrder").startScopedSpan()) {
    Span span = tracer.getCurrentSpan();
    span.setAttribute("orderId", orderId);
    
    // 执行业务逻辑
    validateOrder(order);
    chargePayment(order);
    updateInventory(order);
}

// 跨服务追踪
Span parentSpan = tracer.getCurrentSpan();
try (var scope = tracer.spanBuilder("callPaymentService")
        .setParent(parentSpan)
        .setSpanKind(SpanKind.CLIENT)
        .startScopedSpan()) {
    // 调用支付服务
    paymentService.charge(order);
}
```

**链路追踪分析**：
- **延迟分析**：识别慢调用
- **依赖分析**：可视化服务依赖
- **错误分析**：定位失败请求

### 6.3 熔断降级

**Resilience4j 实现**：

```java
// 配置熔断
CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
    .failureRateThreshold(50)
    .waitDurationInOpenState(Duration.ofSeconds(60))
    .slidingWindowSize(100)
    .build();

CircuitBreaker circuitBreaker = CircuitBreaker.of("backendService", circuitBreakerConfig);

// 配置降级
FallbackDecorators fallbackDecorators = FallbackDecorators.of(
    Fallback.decorate(circuitBreaker, 
        throwable -> getDefaultResponse())
);

// 使用熔断
Supplier<String> decoratedSupplier = fallbackDecorators.decorateSupplier(
    () -> backendService.call()
);

String result = decoratedSupplier.get();
```

**熔断状态机**：

```
Closed → (失败率>阈值) → Open → (等待时间到期) → Half-Open → (成功) → Closed
                                              ↓ (失败)
                                            Open
```

### 6.4 容量规划

**容量估算公式**：

```
QPS = 日请求量 / 86400 / 峰值系数

峰值系数 = 峰值QPS / 平均QPS
通常取 3-5
```

**资源估算**：

```
所需CPU核数 = (QPS × 平均响应时间) / (CPU利用率 × 核心效率)

所需内存 = 单请求内存 × 并发数 + 系统预留内存
```

**压测流程**：

1. **准备阶段**：确定压测目标、场景设计
2. **执行阶段**：逐步加压，记录指标
3. **分析阶段**：识别瓶颈、优化建议
4. **验证阶段**：验证优化效果

---

## 附录：10年经验工程师面试关注点

### 技术深度
- 底层原理理解（JVM、网络、数据库）
- 源码阅读能力
- 技术选型的权衡思考

### 架构能力
- 系统设计经验
- 分布式系统设计
- 高可用架构设计

### 工程能力
- 代码质量把控
- 自动化测试
- CI/CD流程

### 问题解决能力
- 故障排查经验
- 性能调优经验
- 根因分析能力

### 团队协作
- 技术方案评审
- 代码审查
- 知识分享

---

**文档版本**：v1.0  
**更新日期**：2026年5月