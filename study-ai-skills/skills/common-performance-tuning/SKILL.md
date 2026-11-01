---
name: "common-performance-tuning"
description: "性能优化技能，提供性能分析、瓶颈定位、优化方案和压力测试指导"
trigger: "需要进行性能优化、瓶颈分析或压力测试时"
disable-when: "已有专门的性能团队负责或性能要求不高时"
category: "common"
tags: ["performance", "optimization", "profiling", "benchmark", "tuning"]
requires: ["common-backend-unit-test"]
---

# common-performance-tuning

## 功能描述

性能优化技能，用于系统性地分析和优化应用程序的性能。覆盖 CPU、内存、I/O、网络、数据库等多个维度，提供 profiling 工具、基准测试方法和优化策略。

## 何时使用

在以下情况调用此技能：
- **性能问题排查**：响应慢、吞吐低、资源占用高
- **上线前验证**：新功能/模块的性能基准测试
- **容量规划**：评估系统能支撑的最大并发/负载
- **优化迭代**：持续性能改进和回归测试
- **故障复盘**：性能相关线上问题的根因分析

## 核心功能

### 1. 性能指标体系

#### 后端指标（Spring Boot）

| 指标 | 说明 | 健康阈值 | 工具 |
|------|------|----------|------|
| **响应时间 P95** | 95% 请求的响应时间 | < 200ms | Micrometer + Prometheus |
| **吞吐量 QPS** | 每秒处理请求数 | 根据业务定 | JMeter/Gatling |
| **错误率** | 失败请求占比 | < 0.1% | Actuator /metrics |
| **CPU 使用率** | CPU 占用百分比 | < 70% | top/htop |
| **内存使用率** | Heap/Non-Heap 占用 | < 80% | jstat/jmap |
| **GC 频率** | Full GC 间隔 | > 1 小时 | GC logs |
| **线程池队列** | 等待任务数 | < 100 | ThreadPoolExecutor |
| **数据库连接池** | 活跃连接数 | < maxPoolSize*0.8 | HikariCP metrics |

#### 前端指标（Vue）

| 指标 | 说明 | 健康阈值 | 工具 |
|------|------|----------|------|
| **FCP** | First Contentful Paint | < 1.5s | Lighthouse |
| **LCP** | Largest Contentful Paint | < 2.5s | Web Vitals |
| **FID** | First Input Delay | < 100ms | Web Vitals |
| **CLS** | Cumulative Layout Shift | < 0.1 | Web Vitals |
| **Bundle Size** | JS/CSS 包大小 | < 200KB gzipped | webpack-bundle-analyzer |

### 2. Profiling 工具推荐

#### Java 应用 Profiling

```bash
# 1. Async-Profiler（轻量级，生产可用）
git clone https://github.com/async-profiler/async-profiler.git
cd async-profiler
./profiler.sh -d 30 -f profile.html <pid>

# 2. Arthas（阿里开源，动态诊断）
curl -O https://arthas.aliyun.com/arthas-boot.jar
java -jar arthas-boot.jar
# 进入后执行：
trace com.example.service.UserService getUserById   # 方法追踪
monitor -c 5 com.example.controller.UserController  # 实时监控
thread -n 3                                          # Top 3 繁忙线程

# 3. JFR（JDK Flight Recorder，JDK 自带）
jcmd <pid> JFR.start duration=60s filename=recording.jfr
jfr print --events GC,Allocation recording.jfr

# 4. VisualVM（图形化，开发环境）
jvisualvm  # 启动后连接目标 JVM
```

#### 数据库 Profiling

```sql
-- MySQL 慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1;  -- 超过 1 秒记录

-- 查看慢查询
SELECT * FROM mysql.slow_log ORDER BY query_time DESC LIMIT 10;

-- EXPLAIN 分析
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'test@example.com';

-- PostgreSQL pg_stat_statements
SELECT query, calls, total_exec_time, mean_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;
```

#### 前端 Profiling

```javascript
// Chrome DevTools Performance Tab
// 1. 录制页面加载过程
// 2. 分析 Flame Chart
// 3. 查找 Long Task (>50ms)

// Web Vitals 监控
import { getCLS, getFID, getFCP, getLCP, getTTFB } from 'web-vitals';

getCLS(console.log);
getFID(console.log);
getFCP(console.log);
getLCP(console.log);
getTTFB(console.log);
```

### 3. 常见性能瓶颈及优化方案

#### CPU 瓶颈

**症状**：CPU 使用率持续 > 80%，响应时间随并发线性增长

**排查步骤**：
1. `top -H -p <pid>` 找到最耗 CPU 的线程
2. `printf "%x\n" <tid>` 转换线程 ID 为十六进制
3. `jstack <pid> | grep <hex_tid> -A 20` 查看堆栈

**优化方案**：
```java
// ❌ 错误：同步锁竞争
public synchronized void process() { ... }

// ✅ 正确：细粒度锁或无锁
private final ReentrantLock lock = new ReentrantLock();
public void process() {
    lock.lock();
    try { ... } finally { lock.unlock(); }
}

// ✅ 更好：ConcurrentHashMap
Map<String, Object> cache = new ConcurrentHashMap<>();

// ✅ 最佳：局部变量避免共享
public Result compute(Input input) {
    // 所有变量都是局部变量，无线程安全问题
}
```

#### 内存瓶颈

**症状**：频繁 Full GC，OOM，响应时间抖动

**排查步骤**：
1. `jstat -gcutil <pid> 1000` 观察 GC 频率
2. `jmap -histo:live <pid> | head -20` 查看对象分布
3. MAT（Memory Analyzer Tool）分析 Heap Dump

**优化方案**：
```java
// ❌ 错误：内存泄漏
private static List<Object> cache = new ArrayList<>();
public void add(Object obj) { cache.add(obj); }  // 永不清理

// ✅ 正确：有界缓存
private static final Cache<String, Object> cache = 
    Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(Duration.ofMinutes(10))
        .build();

// ❌ 错误：大对象频繁创建
public byte[] processData() {
    return new byte[10 * 1024 * 1024];  // 每次 10MB
}

// ✅ 正确：对象复用或流式处理
public void processData(InputStream in, OutputStream out) {
    byte[] buffer = new byte[8192];
    int len;
    while ((len = in.read(buffer)) != -1) {
        out.write(buffer, 0, len);
    }
}
```

#### I/O 瓶颈

**症状**：磁盘/网络 I/O wait 高，线程阻塞

**优化方案**：
```java
// ❌ 错误：同步 I/O
FileReader reader = new FileReader("large-file.txt");
String line;
while ((line = reader.readLine()) != null) { ... }

// ✅ 正确：异步 NIO
AsynchronousFileChannel channel = AsynchronousFileChannel.open(path);
ByteBuffer buffer = ByteBuffer.allocate(8192);
channel.read(buffer, 0, buffer, new CompletionHandler<...>() { ... });

// ✅ 数据库批量操作
// ❌ 错误：逐条插入
for (User user : users) {
    userRepository.save(user);  // N 次 DB 往返
}

// ✅ 正确：批量插入
userRepository.saveAll(users);  // 1 次 DB 往返
```

#### 数据库瓶颈

**症状**：慢查询多，连接池耗尽，锁等待

**优化方案**：
```sql
-- ❌ 错误：N+1 查询
SELECT * FROM orders;  -- 1 次查询
-- 然后对每个 order 查询 user
SELECT * FROM users WHERE id = ?;  -- N 次查询

-- ✅ 正确：JOIN 一次性查询
SELECT o.*, u.name FROM orders o JOIN users u ON o.user_id = u.id;

-- ❌ 错误：全表扫描
SELECT * FROM users WHERE name LIKE '%john%';

-- ✅ 正确：索引优化
CREATE INDEX idx_users_name ON users(name);
SELECT * FROM users WHERE name LIKE 'john%';  -- 使用前缀匹配

-- ❌ 错误：未分页的大结果集
SELECT * FROM logs WHERE created_at > '2026-01-01';  -- 可能百万行

-- ✅ 正确：游标分页
SELECT * FROM logs 
WHERE created_at > '2026-01-01' 
ORDER BY id 
LIMIT 100 OFFSET 0;
```

### 4. 压力测试流程

#### 工具选择

| 工具 | 适用场景 | 优点 | 缺点 |
|------|----------|------|------|
| **JMeter** | HTTP/API 压测 | GUI + CLI，插件丰富 | 资源占用高 |
| **Gatling** | 高并发场景 | Scala DSL，报告精美 | 学习曲线陡 |
| **wrk** | 简单 HTTP 压测 | 极轻量，高性能 | 功能单一 |
| **k6** | 开发者友好 | JS 脚本，CI/CD 集成好 | 生态较小 |

#### JMeter 压测示例

```xml
<!-- test-plan.jmx -->
<TestPlan>
    <ThreadGroup>
        <num_threads>100</num_threads>      <!-- 并发用户数 -->
        <ramp_time>10</ramp_time>            <!-- 10 秒内启动 -->
        <loop_count>-1</loop_count>          <!-- 无限循环 -->
        <duration>300</duration>             <!-- 持续 5 分钟 -->
    </ThreadGroup>
    
    <HTTPSamplerProxy>
        <domain>localhost</domain>
        <port>8090</port>
        <path>/api/users/{id}</path>
        <method>GET</method>
    </HTTPSamplerProxy>
    
    <ResultCollector>
        <filename>results.jtl</filename>
    </ResultCollector>
</TestPlan>
```

```bash
# 执行压测
jmeter -n -t test-plan.jmx -l results.jtl

# 生成报告
jmeter -g results.jtl -o report/
```

#### 压测指标分析

```
========== 压测报告 ==========
样本数：     50,000
错误率：     0.02%
平均响应：   45 ms
P50：        38 ms
P90：        72 ms
P95：        95 ms
P99：        150 ms
吞吐量：     1,650 req/s
```

**判断标准**：
- ✅ P95 < 200ms，错误率 < 0.1% → 通过
- ⚠️ P95 200-500ms → 需要优化
- ❌ P95 > 500ms 或错误率 > 1% → 严重问题

### 5. 优化检查清单

#### 代码层

- [ ] 避免 N+1 查询
- [ ] 使用连接池/线程池
- [ ] 缓存热点数据（Redis/Caffeine）
- [ ] 异步处理非关键路径
- [ ] 减少对象创建（对象池/复用）
- [ ] 避免同步锁竞争（Concurrent 集合）

#### 数据库层

- [ ] 添加合适索引
- [ ] 避免 SELECT *
- [ ] 使用批量操作
- [ ] 读写分离
- [ ] 分库分表（大数据量）

#### 架构层

- [ ] CDN 加速静态资源
- [ ] API 网关限流降级
- [ ] 微服务拆分热点模块
- [ ] 消息队列削峰填谷
- [ ] 多级缓存（本地 + Redis）

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| targetService | String | 是 | 目标服务名称 |
| testType | String | 否 | 测试类型（baseline/load/stress/spike），默认 load |
| concurrentUsers | Integer | 否 | 并发用户数，默认 100 |
| durationSeconds | Integer | 否 | 测试持续时间（秒），默认 300 |
| outputFormat | String | 否 | 输出格式（json/html/markdown），默认 markdown |

## 输出格式

```markdown
# 性能优化报告

## 执行摘要
- 测试时间：2026-10-31
- 测试类型：Load Test
- 并发用户：100
- 持续时间：300 秒

## 性能基线

| 指标 | 当前值 | 目标值 | 状态 |
|------|--------|--------|------|
| P95 响应时间 | 95 ms | < 200 ms | ✅ |
| 吞吐量 QPS | 1,650 | > 1,000 | ✅ |
| 错误率 | 0.02% | < 0.1% | ✅ |
| CPU 使用率 | 65% | < 70% | ✅ |
| 内存使用率 | 72% | < 80% | ✅ |

## 瓶颈分析

### 1. [瓶颈名称] - P0/P1/P2

**现象**：[描述]

**根因**：[分析]

**影响**：[量化影响]

**优化方案**：
- [具体方案 1]
- [具体方案 2]

**预期收益**：
- P95 响应时间：95ms → 50ms（-47%）
- 吞吐量：1,650 → 2,500 QPS（+51%）

## 优化建议优先级

1. **[P0] 数据库 N+1 查询优化**
   - 预计收益：P95 -30%
   - 实施难度：低
   - 预计工时：2h

2. **[P1] Redis 缓存热点数据**
   - 预计收益：P95 -20%
   - 实施难度：中
   - 预计工时：4h

3. **[P2] 异步化处理日志写入**
   - 预计收益：P95 -10%
   - 实施难度：低
   - 预计工时：1h

## 复测计划

- 优化后重新压测
- 对比优化前后指标
- 确认无性能回退
```

## 使用流程

### 基线测试（Baseline）

1. 单用户串行请求，建立性能基线
2. 记录 P50/P95/P99 响应时间
3. 作为后续优化的对比基准

### 负载测试（Load Test）

1. 逐步增加并发用户（10 → 50 → 100 → 200）
2. 观察响应时间和错误率变化
3. 找到系统最大吞吐点

### 压力测试（Stress Test）

1. 超出正常负载（如 500 并发）
2. 观察系统崩溃点
3. 验证优雅降级机制

### 尖峰测试（Spike Test）

1. 突然增加大量并发（如 0 → 1000）
2. 观察系统恢复能力
3. 验证自动扩容机制

## 失败案例

| 错误场景 | 错误信息 | 解决方案 |
|----------|----------|----------|
| 压测机资源不足 | JMeter OOM | 分布式压测或增加机器配置 |
| 目标服务过载 | Connection refused | 降低并发数或增加服务实例 |
| 网络带宽瓶颈 | 吞吐量上不去 | 检查网络带宽或使用内网压测 |
| 数据不一致 | 压测后数据脏乱 | 使用独立测试数据库，压测后清理 |

## 最佳实践

1. **持续性能测试**：每次发布前运行基线测试
2. **性能预算**：设定 P95/吞吐量上限，CI 流水线强制检查
3. **渐进式优化**：先解决 P0 瓶颈，再优化 P1/P2
4. **真实流量回放**：使用生产流量影子测试
5. **容量规划**：定期评估系统容量，提前扩容

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| PERF_TEST_CONCURRENT | 默认并发数 | 100 |
| PERF_TEST_DURATION | 默认测试时长（秒） | 300 |
| JMETER_HOME | JMeter 安装路径 | /opt/jmeter |

### Maven 依赖

```xml
<!-- Micrometer + Prometheus -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-spring-legacy</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>

<!-- Caffeine 缓存 -->
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

## 扩展指南

### 自定义性能指标

```java
@Component
public class CustomMetrics {
    private final Timer requestTimer;
    
    public CustomMetrics(MeterRegistry registry) {
        this.requestTimer = Timer.builder("custom.request.duration")
            .tag("service", "user-service")
            .register(registry);
    }
    
    public void recordRequest(long durationMs) {
        requestTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }
}
```

### 集成 CI/CD 性能门禁

```yaml
# .gitlab-ci.yml
performance-test:
  stage: test
  script:
    - jmeter -n -t perf-test.jmx -l results.jtl
    - python check-performance.py results.jtl  # 检查是否达标
  rules:
    - if: $CI_PIPELINE_SOURCE == "merge_request_event"
```

## 触发条件

- **前置条件**：服务可正常运行，有测试环境
- **环境要求**：Java 21+, JMeter 5.6+ 或 Gatling 3.9+
- **数据准备**：测试数据集（脱敏的生产数据副本）
- **触发方式**：API 调用 / CLI 执行 / CI/CD 触发
