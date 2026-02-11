# Study Best Practice

## 项目简介

本项目是一个Java最佳实践学习项目，包含性能测试、数据库性能分析、JVM优化、Redis优化等多个模块，旨在提供可操作的性能优化指南和实践案例。

## 目录结构

- **src/main/java/com/bage/study/best/practice/**
  - **cache/**: 缓存相关实现
  - **config/**: 配置类
  - **controller/**: 控制器
  - **metrics/**: 指标监控
  - **model/**: 数据模型
  - **mq/**: 消息队列
  - **repo/**: 数据访问
  - **service/**: 业务逻辑
  - **trial/**: 性能测试相关控制器
  - **utils/**: 工具类

## 核心功能模块



### 1. 性能测试

#### 1.1 WRK压测

**安装**
```bash
brew install wrk
```

**使用示例**
```bash
# 10线程，50连接，压测90秒
wrk -t10 -c50 -d90s http://localhost:8000/user/insert

# 带延迟统计
wrk -t10 -c100 -d60s -T3s --latency http://localhost:8000/log/insert
```

**压测结果分析**

- **Latency**: 响应时间分布
- **Req/Sec**: 每秒请求数
- **Requests/sec**: 总体QPS
- **Transfer/sec**: 每秒传输数据量

#### 1.2 JMeter压测

**启动命令**
```bash
cd /Users/bage/bage/software/apache-jmeter-5.5/bin

java -jar jmeter.jar
```

### 2. 数据库性能分析

#### 2.1 MySQL慢SQL分析

**慢SQL检测**
```sql
-- 查看慢查询配置
show variables like '%slow_query%';

-- 开启慢查询日志
set global slow_query_log = 'ON';
set global slow_query_log_file = '/var/log/mysql/slow-query.log';
set global long_query_time = 1;
```

**慢SQL示例**
```bash
http://localhost:8000/mysql/sql/slow?key=64415%20Hudson%20Drives

```

**慢SQL场景测试API**
```bash
# 全表扫描
http://localhost:8000/mysql/full/table/scan

# 复杂JOIN查询
http://localhost:8000/mysql/complex/join

# 无索引排序
http://localhost:8000/mysql/order/by/no/index

# LIKE前缀模糊匹配
http://localhost:8000/mysql/like/prefix?prefix=xxx

# LIKE中缀模糊匹配（索引失效）
http://localhost:8000/mysql/like/infix?keyword=xxx

# 函数操作（索引失效）
http://localhost:8000/mysql/function/operation
```

**慢SQL分析工具**
- MySQL慢查询日志
- EXPLAIN分析
- Performance Schema
- MySQL Enterprise Monitor
- Percona Toolkit

#### 2.2 数据库连接池优化

**JDBC vs 连接池性能对比**
- JDBC直连: `http://localhost:8000/data/source/jdbc/get?phone=18119069047`
- 连接池: `http://localhost:8000/data/source/pool/get?phone=18119069047`

**Druid监控**
```bash
http://localhost:8000/druid/index.html
http://localhost:8000/druid/sql.html
```

### 2.3 数据库连接池监控详解

#### 2.3.1 监控方法

**1. Druid内置监控**
- **Web控制台**: `http://localhost:8000/druid/index.html`
- **登录信息**: 用户名: sa, 密码: bage
- **功能**: 连接池状态、SQL执行统计、慢SQL分析、并发线程监控

**2. 自定义监控API**

- **连接池基本信息**: `http://localhost:8000/data/source/info`
- **连接池状态**: `http://localhost:8000/data/source/status`
- **连接获取测试**: `http://localhost:8000/data/source/get/connection`
- **并发测试**: `http://localhost:8000/data/source/test/concurrency?threadCount=10&iterations=100`
- **连接池极限测试**: `http://localhost:8000/data/source/test/limit?connectionCount=100`
- **连接池性能监控**: `http://localhost:8000/data/source/monitor?testCount=50`
- **批量测试**: `http://localhost:8000/data/source/batch/test?batchSize=100`

**3. 第三方监控工具**
- **Prometheus + Grafana**: 通过Spring Boot Actuator暴露指标
- **JMX**: 监控连接池运行时状态
- **APM工具**: 如SkyWalking、Pinpoint等

#### 2.3.2 核心监控指标

**连接池状态指标**
| 指标 | 说明 | 常规值范围 | 异常值 | 优化建议 |
|------|------|------------|--------|----------|
| **activeCount** | 当前活跃连接数 | < maxActive | >= maxActive | 增加maxActive或检查连接泄漏 |
| **poolingCount** | 当前空闲连接数 | 10-30% maxActive | < minIdle 或 > 80% maxActive | 调整minIdle或maxActive |
| **waitThreadCount** | 等待连接的线程数 | 0-5 | > 10 | 增加maxActive或检查连接释放 |
| **usageRate** | 连接池使用率 | 20-70% | > 80% 或 < 10% | 调整maxActive或检查连接泄漏 |
| **acquisitionTime** | 连接获取时间 | < 10ms | > 100ms | 检查连接池配置或数据库响应 |

**性能指标**
| 指标 | 说明 | 常规值范围 | 异常值 | 优化建议 |
|------|------|------------|--------|----------|
| **connectionSuccessRate** | 连接获取成功率 | > 99% | < 95% | 检查数据库连接或网络 |
| **avgAcquisitionTime** | 平均连接获取时间 | < 5ms | > 50ms | 优化连接池配置 |
| **errorCount** | 错误计数 | 0 | > 0 | 检查错误原因并修复 |
| **removeAbandonedCount** | 移除的废弃连接数 | 0 | > 0 | 检查连接释放代码 |

**SQL执行指标**
| 指标 | 说明 | 常规值范围 | 异常值 | 优化建议 |
|------|------|------------|--------|----------|
| **slowSqlCount** | 慢SQL数量 | 0 | > 0 | 优化SQL语句和索引 |
| **avgSqlExecuteTime** | 平均SQL执行时间 | < 100ms | > 500ms | 优化SQL语句和索引 |
| **sqlExecuteCount** | SQL执行次数 | 正常业务量 | 异常增长 | 检查是否存在SQL注入或死循环 |

#### 2.3.3 常见错误与解决方案

**连接池常见错误**
| 错误 | 症状 | 原因 | 解决方案 |
|------|------|------|----------|
| **连接泄漏** | activeCount持续增长，最终达到maxActive | 连接未正确关闭 | 使用try-with-resources或确保finally中关闭连接 |
| **连接超时** | 获取连接时抛出timeout异常 | 连接池无可用连接 | 增加maxActive，检查连接释放，设置合理的maxWait |
| **连接失效** | 执行SQL时抛出connection reset异常 | 连接长时间空闲被数据库关闭 | 配置validationQuery和testWhileIdle |
| **内存泄漏** | 应用内存持续增长 | 连接池配置过大，连接对象未释放 | 调整连接池大小，检查连接释放 |
| **线程阻塞** | 应用线程阻塞在获取连接 | 连接池耗尽，连接未及时释放 | 增加maxActive，检查连接释放，使用异步操作 |

**数据库常见错误**
| 错误 | 症状 | 原因 | 解决方案 |
|------|------|------|----------|
| **Too many connections** | 数据库拒绝连接 | 连接池maxActive超过数据库max_connections | 调整maxActive，增加数据库max_connections |
| **Connection reset by peer** | 连接突然关闭 | 网络问题或数据库重启 | 检查网络连接，配置合理的连接超时 |
| **Communications link failure** | 通信链路失败 | 网络问题或数据库服务异常 | 检查网络连接，配置连接重试机制 |

#### 2.3.4 高阶用法与优化

**1. 连接池配置优化**
```java
// 推荐配置示例
@Bean
dataSource {
    DruidDataSource dataSource = new DruidDataSource()
    dataSource.url = "jdbc:mysql://localhost:3306/mydb"
    dataSource.username = "root"
    dataSource.password = "password"
    
    // 基本配置
    dataSource.initialSize = 10         // 初始连接数
    dataSource.maxActive = 100          // 最大连接数
    dataSource.minIdle = 5              // 最小空闲连接数
    dataSource.maxWait = 60000          // 获取连接的最大等待时间
    
    // 连接验证
    dataSource.validationQuery = "SELECT 1"
    dataSource.testWhileIdle = true     // 空闲时验证连接
    dataSource.testOnBorrow = false     // 获取连接时不验证
    dataSource.testOnReturn = false     // 返回连接时不验证
    
    // 连接回收
    dataSource.timeBetweenEvictionRunsMillis = 60000  // 连接回收间隔
    dataSource.minEvictableIdleTimeMillis = 300000    // 连接最小空闲时间
    
    // 监控配置
    dataSource.filters = "stat,wall,log4j"  // 启用监控和防火墙
    
    dataSource
}
```

**2. 分库分表场景优化**
- **多数据源管理**: 使用动态数据源路由
- **连接池隔离**: 为不同数据库配置独立的连接池
- **读写分离**: 主库用于写操作，从库用于读操作

**3. 高并发场景优化**
- **连接池大小**: 根据并发数和数据库性能调整
- **异步操作**: 使用CompletableFuture处理并发请求
- **批量操作**: 使用JDBC批处理减少连接次数
- **事务管理**: 合理控制事务范围，避免长事务

**4. 监控与告警**
- **实时监控**: 集成Prometheus + Grafana
- **告警配置**: 当连接池使用率超过80%时告警
- **慢SQL监控**: 配置slowSqlMillis，监控执行时间超过阈值的SQL

#### 2.3.5 案例分析

**案例1: 连接泄漏导致系统崩溃**

**场景描述**:
- 应用在高峰期出现响应缓慢，最终系统崩溃
- 监控发现activeCount持续增长，达到maxActive后不再下降

**根因分析**:
- 代码中使用了try-catch但未在finally中关闭连接
- 异常情况下连接未被释放，导致连接池耗尽

**解决方案**:
- 使用try-with-resources自动关闭连接
- 添加removeAbandoned配置，自动回收长时间未使用的连接
- 增加连接池监控和告警

**优化效果**:
- 系统稳定性显著提高
- 连接池使用率保持在合理范围
- 高峰期不再出现连接耗尽的情况

**案例2: 慢SQL导致连接池阻塞**

**场景描述**:
- 应用响应时间突然变长
- 监控发现大量线程等待获取连接

**根因分析**:
- 某个复杂查询执行时间过长，占用连接时间太久
- 导致其他请求无法获取连接，形成阻塞链

**解决方案**:
- 优化SQL语句，添加合适的索引
- 调整连接池配置，设置合理的maxWait
- 实现SQL超时机制，避免长时间占用连接

**优化效果**:
- SQL执行时间从5秒降至100ms以内
- 连接池阻塞问题解决
- 系统响应时间恢复正常

#### 2.3.6 实践验证

**验证场景1: 连接池基本功能**
```bash
# 获取连接池信息
http://localhost:8000/data/source/info

# 测试连接获取
http://localhost:8000/data/source/get/connection
```

**验证场景2: 并发性能测试**
```bash
# 10个线程，每个线程执行100次连接获取
http://localhost:8000/data/source/test/concurrency?threadCount=10&iterations=100

# 预期结果: 成功率>99%，平均获取时间<10ms
```

**验证场景3: 连接池极限测试**
```bash
# 尝试获取100个连接
http://localhost:8000/data/source/test/limit?connectionCount=100

# 预期结果: 当connectionCount>maxActive时，会出现获取失败
```

**验证场景4: 性能监控**
```bash
# 执行50次连接获取测试并监控性能
http://localhost:8000/data/source/monitor?testCount=50

# 预期结果: 生成详细的性能报告，包括成功率、平均获取时间等
```

**验证场景5: 批量测试**
```bash
# 批量执行100次连接获取测试
http://localhost:8000/data/source/batch/test?batchSize=100

# 预期结果: 生成批量测试报告，分析连接池性能趋势
```

#### 2.3.7 参考链接

**官方文档**:
- [Druid官方文档](https://github.com/alibaba/druid/wiki)
- [Druid连接池配置说明](https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8)

**技术博客**:
- [数据库连接池原理与实现](https://www.iteye.com/blog/jinnianshilongnian-1493190)
- [Druid连接池最佳实践](https://www.cnblogs.com/fangjian0423/p/druid-best-practice.html)

**监控工具**:
- [Prometheus官方文档](https://prometheus.io/docs/introduction/overview/)
- [Grafana官方文档](https://grafana.com/docs/)

**性能调优**:
- [MySQL性能调优指南](https://dev.mysql.com/doc/refman/8.0/en/optimization.html)
- [JDBC性能优化技巧](https://www.oracle.com/technical-resources/articles/java/jdbc-performance.html)

### 3. JVM优化

#### 3.1 JVM参数调优

**基础参数配置**
```bash
# JDK 8
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation \
-XX:+PrintHeapAtGC -XX:NumberOfGCLogFiles=100 -XX:GCLogFileSize=10M \
-Xloggc:my-gc-%t.gc.log -jar -Xms64m -Xmx256m target/study-best-practice-0.0.1-SNAPSHOT.jar

# JDK 17+
java -jar -Xlog:gc:my-gc.log:time,level -Xms64m -Xmx256m target/study-best-practice-0.0.1-SNAPSHOT.jar
```

**推荐生产环境参数**
```bash
java -Xms4g -Xmx4g -Xmn2g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m \
-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=8m \
-XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=45 \
-XX:ParallelGCThreads=8 -XX:ConcGCThreads=2 \
-XX:+DisableExplicitGC -XX:+AlwaysPreTouch \
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/path/to/dump \
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/path/to/gc.log \
-jar application.jar
```

#### 3.2 垃圾回收器优化

**垃圾回收器选择**
| 回收器 | 参数 | 适用场景 |
|--------|------|----------|
| 串行回收器 | `-XX:+UseSerialGC` | 单线程、小型应用 |
| 并行回收器 | `-XX:+UseParallelGC` | 吞吐量优先 |
| CMS回收器 | `-XX:+UseConcMarkSweepGC` | 低延迟优先 |
| G1回收器 | `-XX:+UseG1GC` | 平衡吞吐量和延迟 |
| ZGC | `-XX:+UseZGC` | 超大堆、低延迟 |

#### 3.3 GC日志分析

**GC日志配置**
```bash
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation \
-XX:+PrintHeapAtGC -XX:NumberOfGCLogFiles=100 -XX:GCLogFileSize=10M \
-Xloggc:/opt/app/gc-%t.log
```

**GC日志分析工具**
- [GCViewer](https://github.com/chewiebug/GCViewer)
- [GCEasy](https://gceasy.io/)
- [HPROF Viewer](https://www.eclipse.org/mat/)

#### 3.4 内存泄漏分析

**生成堆转储文件**
```bash
jps
jmap -dump:file=heap.hprof,format=b {pid}
```

**分析堆转储文件**
- 使用MAT (Memory Analyzer Tool)
- 使用VisualVM

### 4. Redis优化

#### 4.1 大Key检测与优化

**大Key检测**
```bash
redis-cli -a password --bigkeys
```

**大Key示例**
- 大集合: `http://localhost:8000/redis/big/key/set?count=10000`
- 大值: `http://localhost:8000/redis/big/value/set?count=10000`

**大Key影响**
- 内存占用高
- 操作阻塞
- 网络传输慢

#### 4.2 Redis性能测试

**基本操作**
```bash
# 初始化数据
http://localhost:8000/redis/random/init?max=10000

# 随机写入
http://localhost:8000/redis/random/set

# 随机读取
http://localhost:8000/redis/random/get
```

#### 4.3 Redis性能监控与压力测试

**性能测试API**
```bash
# 单线程写入测试
http://localhost:8000/redis/performance/single/write?count=1000

# 多线程写入测试
http://localhost:8000/redis/performance/multi/write?count=10000&threads=10

# 单线程读取测试
http://localhost:8000/redis/performance/single/read?count=1000

# 多线程读取测试
http://localhost:8000/redis/performance/multi/read?count=10000&threads=10

# 混合读写测试
http://localhost:8000/redis/performance/mixed/read/write?count=10000&threads=10&readRatio=0.7
```

**Redis场景测试API**
```bash
# 生成大key（大字符串）
http://localhost:8000/redis/performance/big/key/generate?size=1024

# 生成大List
http://localhost:8000/redis/performance/big/list/generate?size=10000

# 模拟热点key访问
http://localhost:8000/redis/performance/hot/key?count=10000&threads=10

# 测试大key性能
http://localhost:8000/redis/performance/big/key/test?size=512

# 清理测试数据
http://localhost:8000/redis/performance/cleanup
```

**监控工具**
- Redis Insight: 可视化管理和监控Redis
- Prometheus + Grafana: 监控Redis指标
- Redis Exporter: 提供Redis监控指标

**性能分析指南**
详细的Redis性能分析过程请参考：[Redis-MQ性能分析指南.md](Redis-MQ性能分析指南.md)

## 5. RabbitMQ优化

### 5.1 RabbitMQ消息验证

```bash
// 随机发送MQ 
http://localhost:8000/mq/send/random

// MQ 异步写入 
http://localhost:8000/user/insert/async/mq

// 接受MQ消息 
MQMessageReceiver
```

### 5.2 RabbitMQ性能监控与压力测试

**性能测试API**
```bash
# 单线程发送测试
http://localhost:8000/mq/performance/single/send?count=1000

# 多线程发送测试
http://localhost:8000/mq/performance/multi/send?count=10000&threads=10

# 发送并接收测试
http://localhost:8000/mq/performance/send/receive?count=100
```

**监控工具**
- RabbitMQ Management UI: `http://localhost:15672`
- Prometheus + Grafana: 监控RabbitMQ指标
- RabbitMQ Prometheus插件: 提供RabbitMQ监控指标

**性能分析指南**
详细的RabbitMQ性能分析过程请参考：[Redis-MQ性能分析指南.md](Redis-MQ性能分析指南.md)

## 6. MyBatis缓存验证

### 6.1 缓存类型说明

**MyBatis缓存分为两级：**

| 缓存级别 | 作用域 | 默认状态 | 特点 |
|---------|-------|---------|------|
| **一级缓存** | SqlSession级别 | 默认开启 | 同一个SqlSession内有效，执行更新操作会清空 |
| **二级缓存** | Mapper级别 | 默认关闭 | 多个SqlSession共享，需要手动开启 |

### 6.2 一级缓存验证

**测试API**
```bash
# 测试同一请求内的相同查询（使用缓存）
http://localhost:8000/mybatis/cache/level1/same?phone=13800138000

# 测试执行更新操作后（缓存清空）
http://localhost:8000/mybatis/cache/level1/after/update?phone=13800138000
```

**预期结果**
- 第一次查询会发送SQL到数据库
- 第二次相同查询会使用一级缓存，不会发送SQL
- 执行更新操作后，缓存会被清空，后续查询会重新发送SQL

### 6.3 二级缓存验证

**配置步骤**
1. **开启全局缓存**（默认已开启）
   ```xml
   <settings>
       <setting name="cacheEnabled" value="true"/>
   </settings>
   ```

2. **在Mapper接口上开启二级缓存**
   ```java
   @CacheNamespace(size = 512, flushInterval = 60000)
   public interface UserMapper extends BaseMapper<UserEntity> {
   }
   ```

3. **实体类实现Serializable接口**
   ```java
   public class UserEntity implements Serializable {
       private static final long serialVersionUID = 1L;
       // 字段...
   }
   ```

**测试API**
```bash
# 测试二级缓存
http://localhost:8000/mybatis/cache/level2?phone=13800138000
```

**预期结果**
- 第一次请求会发送SQL到数据库
- 第二次请求（不同SqlSession）会使用二级缓存，不会发送SQL
- 执行更新操作后，对应Mapper的二级缓存会被清空

### 6.4 注意事项

**一级缓存注意事项**
1. **作用域限制**：只在同一个SqlSession内有效，不同SqlSession之间不共享
2. **自动清空**：执行`insert`、`update`、`delete`操作时会自动清空一级缓存
3. **查询条件**：缓存基于SQL语句和参数，即使是同一个方法，不同参数也会有不同缓存
4. **性能影响**：对于频繁重复查询的场景，一级缓存可以显著提升性能

**二级缓存注意事项**
1. **配置复杂**：需要手动开启，且实体类需要实现Serializable接口
2. **内存占用**：缓存会占用内存，需要合理设置缓存大小
3. **一致性问题**：多表关联查询时，可能出现缓存不一致的问题
4. **序列化开销**：对象需要序列化和反序列化，会有一定性能开销
5. **适用场景**：适合查询频率高、数据变更频率低的场景

**常见问题与解决方案**

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| **缓存不一致** | 多表关联查询时，更新了一张表但另一张表的缓存未更新 | 谨慎使用二级缓存，或在相关Mapper上都开启缓存 |
| **内存溢出** | 缓存大小设置过大，或缓存了大对象 | 合理设置缓存大小，避免缓存大对象 |
| **性能下降** | 序列化开销过大，或缓存命中率低 | 只在合适的场景使用二级缓存，监控缓存命中率 |
| **数据过期** | 缓存未及时更新，导致读取到旧数据 | 设置合理的缓存刷新间隔，或使用缓存淘汰策略 |

### 6.5 最佳实践

1. **优先使用一级缓存**：一级缓存默认开启，无需额外配置，适用于大多数场景

2. **二级缓存使用场景**：
   - 数据变更频率低的查询
   - 对响应时间要求高的场景
   - 系统内存充足

3. **缓存配置建议**：
   ```java
   // 合理的缓存配置
   @CacheNamespace(
       size = 512,                // 缓存大小
       flushInterval = 60000,     // 60秒自动刷新
       readWrite = true,          // 读写缓存
       blocking = false           // 非阻塞
   )
   ```

4. **监控与调优**：
   - 监控缓存命中率
   - 根据业务场景调整缓存大小
   - 定期清理过期缓存

## 7. 线程池监控与分析

### 6.1 Tomcat线程池配置

**application.properties配置**
```properties
server.tomcat.threads.max=200
server.tomcat.threads.min-spare=20
server.tomcat.max-connections=10000
server.tomcat.accept-count=100
```

### 6.2 线程分析工具

**查看线程状态**
```bash
jstack {pid} > threads.txt
```

**分析线程阻塞**
- 使用Arthas
- 使用jstack分析线程堆栈

## 性能问题排查指南

### 1. CPU使用率高

**排查步骤**
1. 使用`top`命令查看CPU使用情况
2. 使用`jstack`查看线程堆栈
3. 使用Arthas生成火焰图
4. 定位耗时方法并优化

**常见原因**
- 死循环
- 复杂计算
- 线程阻塞
- GC频繁

### 2. 内存泄漏

**排查步骤**
1. 使用`jmap -heap`查看内存使用情况
2. 生成堆转储文件
3. 使用MAT分析堆转储
4. 定位大对象和内存泄漏点

**常见原因**
- 静态集合持有对象
- 监听器未移除
- 线程局部变量未清理
- 连接未关闭

### 3. GC频繁

**排查步骤**
1. 查看GC日志
2. 分析GC原因和频率
3. 调整JVM参数
4. 优化代码减少对象创建

**常见原因**
- 内存分配不合理
- 对象创建过于频繁
- 大对象直接进入老年代
- 内存泄漏

### 4. 数据库性能问题

**排查步骤**
1. 开启慢查询日志
2. 分析慢SQL
3. 检查索引使用情况
4. 优化SQL语句

**常见原因**
- 缺少索引
- SQL语句复杂
- 全表扫描
- 连接池配置不合理

## 实践案例

### 案例1: 高并发下的Redis大Key问题

**场景**
- 系统在高峰期出现Redis响应延迟
- 监控显示Redis CPU使用率高

**排查过程**
1. 使用`redis-cli --bigkeys`检测大Key
2. 发现多个包含10万+元素的集合
3. 分析代码发现使用了`HGETALL`操作大Hash

**解决方案**
1. 拆分大Key为多个小Key
2. 使用`HSCAN`分批获取数据
3. 增加Redis集群节点

**优化效果**
- Redis响应时间从50ms降至5ms
- CPU使用率从80%降至20%

### 案例2: JVM GC频繁导致的服务卡顿

**场景**
- 服务每几分钟出现一次短暂卡顿
- GC日志显示Young GC频繁

**排查过程**
1. 分析GC日志
2. 发现新生代空间过小
3. 检查代码发现大量临时对象创建

**解决方案**
1. 调整JVM参数，增大新生代空间
2. 优化代码，减少临时对象创建
3. 使用对象池复用对象

**优化效果**
- Young GC频率从每分钟10次降至每分钟2次
- 服务卡顿现象消失

### 案例3: 数据库慢查询优化

**场景**
- 接口响应时间超过2秒
- 数据库CPU使用率高

**排查过程**
1. 查看慢查询日志
2. 发现一条SQL执行时间超过1秒
3. 分析执行计划，发现全表扫描

**解决方案**
1. 添加合适的索引
2. 优化SQL语句，减少关联查询
3. 增加数据库缓存

**优化效果**
- 接口响应时间从2秒降至200ms
- 数据库CPU使用率从70%降至30%

## 监控系统

### 1. Prometheus + Grafana

**部署步骤**
1. 启动Prometheus
```bash
docker run --network bage-net -d --name bage-prometheus -p 9090:9090 \
-v /path/to/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```

2. 启动Grafana
```bash
docker run -d --name=bage-grafana -p 3000:3000 grafana/grafana
```

3. 配置数据源
- 访问: `http://localhost:3000`
- 用户名/密码: admin/admin
- 添加Prometheus数据源: `http://bage-prometheus:9090`

**推荐仪表盘**
- JVM监控: [4701-JVM Micrometer](https://grafana.com/grafana/dashboards/4701-jvm-micrometer/)
- MySQL监控: [7362-MySQL](https://grafana.com/dashboards/7362)
- Redis监控: [Redis Dashboard](https://grafana.com/grafana/dashboards/763)

### 2. Spring Boot Actuator

**访问端点**
- 健康检查: `http://localhost:8000/actuator/health`
- 指标: `http://localhost:8000/actuator/prometheus`
- 环境: `http://localhost:8000/actuator/env`

## 7. 类初始化顺序验证

### 7.1 类初始化顺序测试

**测试API**
```bash
# 完整验证类初始化顺序（父类+子类）
http://localhost:8000/class/init/test

# 单独验证父类初始化顺序
http://localhost:8000/class/init/parent
```

**预期执行顺序**
1. 父类静态变量初始化
2. 父类静态代码块执行
3. 子类静态变量初始化
4. 子类静态代码块执行
5. 父类实例变量初始化
6. 父类实例代码块执行
7. 父类构造函数执行
8. 子类实例变量初始化
9. 子类实例代码块执行
10. 子类构造函数执行

**验证说明**
- 类的初始化过程只会执行一次，首次访问类时触发
- 静态代码块和静态变量的执行顺序与它们在代码中的定义顺序一致
- 实例代码块和实例变量的执行顺序与它们在代码中的定义顺序一致
- 构造函数会在实例代码块执行完成后执行

## 8. JVM垃圾回收器验证

### 8.0 JVM参数分类

**JVM参数根据来源和特性分为三类：**

| 分类 | 前缀 | 示例 | 说明 |
|------|------|------|------|
| **标准参数** | 无 | `-version`、`-cp` | 所有JVM实现都必须支持的参数，向后兼容 |
| **非标准参数** | `-X` | `-Xms`、`-Xmx` | 特定JVM实现支持的参数，可能在不同版本间变化 |
| **高级参数** | `-XX:` | `-XX:MaxGCPauseMillis`、`-XX:+UseG1GC` | 用于JVM调优和调试的参数，变化频繁 |

**常见内存配置参数：**

| 参数 | 类型 | 含义 | 示例 |
|------|------|------|------|
| `-Xms` | 非标准 | 初始堆内存大小 | `-Xms512m`（初始堆内存512MB） |
| `-Xmx` | 非标准 | 最大堆内存大小 | `-Xmx1g`（最大堆内存1GB） |
| `-Xmn` | 非标准 | 年轻代大小 | `-Xmn256m`（年轻代256MB） |
| `-Xss` | 非标准 | 线程栈大小 | `-Xss1m`（每个线程栈1MB） |

**常见高级参数：**

| 参数 | 类型 | 含义 | 示例 |
|------|------|------|------|
| `-XX:SurvivorRatio` | 数值型 | 伊甸区与幸存者区比例 | `-XX:SurvivorRatio=8`（伊甸区:幸存者区=8:1） |
| `-XX:MaxMetaspaceSize` | 数值型 | 元空间最大大小 | `-XX:MaxMetaspaceSize=256m` |
| `-XX:MaxGCPauseMillis` | 数值型 | 最大GC停顿时间目标 | `-XX:MaxGCPauseMillis=100`（目标100ms） |
| `-XX:ParallelGCThreads` | 数值型 | 并行GC线程数 | `-XX:ParallelGCThreads=4` |
| `-XX:+UseG1GC` | 布尔型 | 启用G1垃圾收集器 | `-XX:+UseG1GC` |
| `-XX:+UseZGC` | 布尔型 | 启用Z垃圾收集器 | `-XX:+UseZGC` |
| `-XX:CMSInitiatingOccupancyFraction` | 数值型 | CMS触发阈值 | `-XX:CMSInitiatingOccupancyFraction=75` |
| `-XX:InitiatingHeapOccupancyPercent` | 数值型 | G1触发阈值 | `-XX:InitiatingHeapOccupancyPercent=45` |

### 8.1 垃圾回收器配置

**Serial收集器配置**
```bash
-XX:+UseSerialGC -Xms512m -Xmx512m -Xmn256m -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:serial-gc.log
```

**Parallel收集器配置**
```bash
-XX:+UseParallelGC -XX:+UseParallelOldGC -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=8 -XX:ParallelGCThreads=4 -XX:MaxGCPauseMillis=100 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:parallel-gc.log
```

**CMS收集器配置**
```bash
-XX:+UseConcMarkSweepGC -XX:+UseParNewGC -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=8 -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+CMSParallelRemarkEnabled -XX:+CMSScavengeBeforeRemark -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:cms-gc.log
```

**G1收集器配置**
```bash
-XX:+UseG1GC -Xms1g -Xmx1g -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=4 -XX:ConcGCThreads=2 -XX:InitiatingHeapOccupancyPercent=45 -XX:G1HeapRegionSize=16m -XX:G1MaxNewSizePercent=60 -XX:G1ReservePercent=15 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:g1-gc.log
```

**ZGC收集器配置 (JDK 11+)**
```bash
-XX:+UseZGC -Xms1g -Xmx1g -XX:ZGCHeapSizeMax=1g -XX:ZGCHeapSizeMin=512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:zgc-gc.log
```

### 8.2 GC场景验证API

**测试API**
```bash
# 触发年轻代GC
http://localhost:8000/gc/young/gc

# 触发老年代GC
http://localhost:8000/gc/old/gc

# 测试内存分配策略
http://localhost:8000/gc/memory/allocation?size=100

# 测试G1 GC行为
http://localhost:8000/gc/g1/test?count=1000
```

### 8.3 常见参数验证

**内存配置验证**
| 参数 | 说明 | 验证方法 |
|------|------|----------|
| Xms/Xmx | 初始/最大堆内存 | 启动时指定不同值，观察内存分配 |
| Xmn | 年轻代大小 | 调整大小，观察GC频率变化 |
| SurvivorRatio | 伊甸区与幸存者区比例 | 调整比例，观察对象晋升行为 |
| MaxMetaspaceSize | 元空间最大大小 | 调整大小，观察类加载行为 |

**GC行为验证**
| 参数 | 说明 | 验证方法 |
|------|------|----------|
| MaxGCPauseMillis | 最大GC停顿时间 | 调整值，观察GC停顿时间变化 |
| ParallelGCThreads | 并行GC线程数 | 调整线程数，观察GC速度变化 |
| CMSInitiatingOccupancyFraction | CMS触发阈值 | 调整阈值，观察CMS启动时机 |
| InitiatingHeapOccupancyPercent | G1触发阈值 | 调整阈值，观察G1启动时机 |
| G1MaxNewSizePercent | G1年轻代最大比例 | 调整比例，观察年轻代大小变化 |
| G1HeapRegionSize | G1区域大小 | 调整大小，观察内存分配行为 |

### 8.4 验证说明

1. **GC日志分析**
   - 启动时添加`-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log`参数
   - 使用GCViewer等工具分析GC日志
   - 关注GC类型、GC时间、内存使用情况等指标

2. **内存使用监控**
   - 使用JConsole、VisualVM等工具监控内存使用
   - 观察年轻代、老年代内存变化
   - 关注对象晋升情况和内存碎片

3. **性能对比**
   - 在相同硬件环境下测试不同垃圾回收器
   - 对比GC频率、GC停顿时间、吞吐量等指标
   - 根据应用特点选择合适的垃圾回收器

4. **调优建议**
   - 对于低延迟应用，优先考虑G1或ZGC
   - 对于高吞吐量应用，优先考虑Parallel收集器
   - 根据堆大小和硬件配置调整GC线程数
   - 定期分析GC日志，持续优化GC配置

### 8.5 垃圾回收性能指标实践

#### 8.5.1 性能指标监控

**核心性能指标**
| 指标 | 说明 | 理想值 |
|------|------|--------|
| **吞吐量** | 非GC时间占总时间的比例 | >95% |
| **GC停顿时间** | 单次GC的暂停时间 | <100ms（低延迟应用） |
| **GC频率** | 单位时间内GC的次数 | 越少越好 |
| **内存使用率** | 堆内存的使用比例 | <70% |
| **GC CPU占用** | GC过程中CPU的使用率 | <20% |

**监控工具**
1. **JDK自带工具**
   - **jstat**：实时监控GC统计信息
   - **jconsole**：图形化监控工具
   - **jvisualvm**：可视化性能分析工具

2. **第三方工具**
   - **GCViewer**：GC日志分析工具
   - **Prometheus + Grafana**：监控告警系统
   - **Arthas**：Java诊断工具

**监控命令示例**
```bash
# 使用jstat监控GC情况（每1秒输出一次，共10次）
jstat -gcutil <pid> 1000 10

# 使用jstat监控内存使用情况
jstat -gccapacity <pid>

# 查看GC详细信息
jstat -gc <pid>
```

#### 8.5.2 性能指标验证代码

**测试API**
```bash
# 测试垃圾回收性能指标
http://localhost:8000/gc/performance/test?count=1000&interval=10

# 比较不同垃圾回收器的性能
http://localhost:8000/gc/performance/compare?count=500
```

**性能指标验证示例**
```java
// 初始化GC监控器
GCMonitor gcMonitor = new GCMonitor();
gcMonitor.start();

// 创建对象，触发GC
List<byte[]> objects = new ArrayList<>();
for (int i = 0; i < 1000; i++) {
    byte[] obj = new byte[(i % 10 + 1) * 1024 * 1024];
    objects.add(obj);
    
    if (i % 50 == 0) {
        objects.clear();
        System.gc();
    }
    
    Thread.sleep(10);
}

// 停止监控并获取结果
GCMonitor.GCMonitorResult result = gcMonitor.stop();
System.out.println("GC性能测试结果: " + result);
```

#### 8.5.3 验证场景

**场景1：大量小对象**
- **测试方法**：创建大量1MB大小的对象，每5个清空一次
- **预期结果**：年轻代GC频繁，老年代GC较少
- **适合回收器**：Parallel GC（吞吐量优先）

**场景2：少量大对象**
- **测试方法**：创建少量10MB大小的对象，每2个清空一次
- **预期结果**：可能直接进入老年代，触发老年代GC
- **适合回收器**：G1 GC（大对象处理较好）

**场景3：混合大小对象**
- **测试方法**：创建混合大小的对象，模拟真实应用场景
- **预期结果**：年轻代和老年代GC都有发生
- **适合回收器**：根据应用特点选择

#### 8.5.4 注意事项与易错点

**注意事项**
1. **GC日志配置**
   - 生产环境中建议开启GC日志，但要注意日志文件大小
   - 可使用`-XX:+UseGCLogFileRotation`参数进行日志轮转

2. **内存分配策略**
   - 避免频繁创建大对象，可能导致直接进入老年代
   - 合理设置年轻代大小，减少对象晋升

3. **并发处理**
   - 注意GC线程与应用线程的资源竞争
   - 合理设置GC线程数，避免CPU过度使用

**易错点**
1. **过度调优**
   - 不要盲目追求GC停顿时间，可能影响吞吐量
   - 根据应用特点选择合适的垃圾回收器

2. **参数冲突**
   - 注意不同GC参数之间的兼容性
   - 例如，G1 GC的一些参数不适用于其他回收器

3. **监控盲区**
   - 不要只关注GC指标，还要关注应用的整体性能
   - 结合业务指标进行调优

4. **版本差异**
   - 不同JDK版本的GC行为可能有所不同
   - 升级JDK时要重新评估GC配置

**调优建议**
1. **循序渐进**
   - 先使用默认配置，观察性能表现
   - 再根据监控结果进行针对性调优

2. **对比测试**
   - 在相同条件下测试不同配置
   - 选择最适合应用的GC配置

3. **持续监控**
   - 建立长期的GC监控机制
   - 定期分析GC日志，及时发现问题

4. **容量规划**
   - 根据应用的内存使用情况，合理规划堆大小
   - 预留足够的内存空间，避免频繁GC

通过以上实践，可以全面了解垃圾回收的性能指标，选择合适的垃圾回收器和配置参数，从而提高应用的性能和稳定性。

## 9. 类加载过程验证

### 9.1 类加载过程测试

**测试API**
```bash
# 完整验证类加载过程
http://localhost:8000/class/load/test

# 获取类加载信息
http://localhost:8000/class/load/info?className=com.bage.study.best.practice.trial.classload.TestClass

# 测试不同类加载器
http://localhost:8000/class/load/loader/test
```

### 9.2 类加载过程说明

**类加载的三个阶段**
1. **加载**：将类的字节码加载到内存，生成Class对象
2. **链接**：验证、准备、解析
3. **初始化**：执行静态代码块和静态变量初始化

**触发类初始化的场景**
1. 访问类的静态变量（非final常量）
2. 调用类的静态方法
3. 创建类的实例
4. 初始化子类时，父类会先初始化
5. 使用反射API访问类

**类加载器层次**
| 类加载器 | 加载范围 | 父加载器 |
|---------|---------|----------|
| Bootstrap ClassLoader | JDK核心类库 | 无 |
| Extension ClassLoader | JDK扩展类库 | Bootstrap |
| App ClassLoader | 应用类路径 | Extension |
| Custom ClassLoader | 自定义路径 | App |

### 9.3 验证场景

**场景1：访问静态常量**
- 访问`TestClass.STATIC_CONSTANT`
- 结果：不会触发类初始化，只会触发加载

**场景2：访问静态变量**
- 访问`TestClass.staticVar`
- 结果：触发类初始化，执行静态代码块和静态变量初始化

**场景3：调用静态方法**
- 调用`TestClass.staticMethod()`
- 结果：如果类未初始化，会先初始化

**场景4：创建实例**
- 创建`new TestClass()`
- 结果：如果类未初始化，会先初始化，然后执行实例代码块和构造函数

**场景5：创建多个实例**
- 创建多个`TestClass`实例
- 结果：类初始化只执行一次，实例化会执行多次

### 9.4 验证方法

1. **查看日志输出**
   - 启动应用后，访问测试API
   - 观察控制台日志，查看类加载、初始化、实例化的顺序
   - 关注不同阶段的执行时间点

2. **分析类加载信息**
   - 访问`/class/load/info`接口
   - 查看类的加载状态、初始化状态、加载器信息
   - 查看实例化数量

3. **使用工具监控**
   - 使用JConsole、VisualVM等工具监控类加载
   - 观察类加载数量和内存使用变化
   - 分析类加载的性能影响

### 9.5 代码样例

**TestClass.java**
```java
public class TestClass {
    // 静态常量
    public static final String STATIC_CONSTANT = "static constant";
    
    // 静态变量
    public static String staticVar = initStaticVar();
    
    // 静态代码块
    static {
        log.info("TestClass static block executed");
        ClassLoadMonitor.recordClassInit(TestClass.class.getName());
    }
    
    // 实例变量
    private String instanceVar = initInstanceVar();
    
    // 实例代码块
    {
        log.info("TestClass instance block executed");
    }
    
    // 构造函数
    public TestClass() {
        log.info("TestClass constructor executed");
        ClassLoadMonitor.recordInstanceCreation(TestClass.class.getName());
    }
    
    // 静态方法
    public static void staticMethod() {
        log.info("TestClass staticMethod executed");
    }
    
    // 实例方法
    public void instanceMethod() {
        log.info("TestClass instanceMethod executed");
    }
    
    private static String initStaticVar() {
        log.info("TestClass initStaticVar executed");
        return "initialized static var";
    }
    
    private String initInstanceVar() {
        log.info("TestClass initInstanceVar executed");
        return "initialized instance var";
    }
}
```

## 工具集

### 1. 诊断工具

- **Arthas**: 阿里巴巴开源的Java诊断工具
- **JConsole**: JDK自带的监控工具
- **VisualVM**: 可视化JVM监控工具
- **MAT**: 内存分析工具
- **GCViewer**: GC日志分析工具

### 2. 压测工具

- **WRK**: 轻量级HTTP压测工具
- **JMeter**: 功能强大的压测工具
- **Gatling**: 高性能压测工具

## 部署指南

### 1. 本地部署

**打包**
```bash
mvn clean package
```

**启动**
```bash
java -jar target/study-best-practice-0.0.1-SNAPSHOT.jar
```

### 2. 远程部署

**拷贝文件**
```bash
scp -r ./target/study-best-practice-0.0.1-SNAPSHOT.jar user@server:/path/to/deploy
```

**启动**

```bash
java -jar study-best-practice-0.0.1-SNAPSHOT.jar \
--spring.config.location=file:///path/to/application.properties
```

## 参考资料

- [阿里巴巴Java开发手册](https://github.com/alibaba/p3c)
- [字节跳动技术博客](https://bytedance.larkoffice.com/wiki/Na3Owt5dniqrlkkPHj2cWGYunfg)
- [美团技术沙龙](https://tech.meituan.com/salon.html)
- [Oracle JVM官方文档](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/)
- [《深入理解Java虚拟机》](https://item.jd.com/12494631.html)
- [Spring Boot官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Redis官方文档](https://redis.io/documentation)
- [RabbitMQ官方文档](https://www.rabbitmq.com/documentation.html)
- [MySQL官方文档](https://dev.mysql.com/doc/)
