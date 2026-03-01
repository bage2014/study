# MySQL CPU 使用率高问题分析与解决方案

## 1. 常见原因

### 1.1 索引问题
- **缺少索引**：导致全表扫描
- **索引失效**：例如使用了 `OR`、`!=`、`LIKE '%xxx'` 等操作符
- **索引选择不当**：MySQL 选择了不合适的索引

### 1.2 SQL 语句问题
- **复杂查询**：多表关联、子查询、复杂聚合函数
- **大量数据排序**：`ORDER BY` 操作
- **大量数据分组**：`GROUP BY` 操作
- **全表更新**：没有 `WHERE` 条件的更新操作

### 1.3 服务器配置问题
- **缓冲区设置过小**：如 `innodb_buffer_pool_size`
- **连接数过多**：`max_connections` 设置过大
- **并发事务**：长时间未提交的事务

### 1.4 硬件资源问题
- **CPU 性能不足**：服务器 CPU 核心数少或性能差
- **内存不足**：导致频繁的磁盘 I/O

## 2. 如何模拟 MySQL CPU 使用率高的场景

### 2.1 全表扫描场景

```sql
-- 创建测试表
CREATE TABLE test_cpu (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  age INT,
  address VARCHAR(255)
);

-- 插入大量数据
DELIMITER //
CREATE PROCEDURE insert_data()
BEGIN
  DECLARE i INT DEFAULT 0;
  WHILE i < 1000000 DO
    INSERT INTO test_cpu (name, age, address) VALUES (CONCAT('name', i), i % 100, CONCAT('address', i));
    SET i = i + 1;
  END WHILE;
END //
DELIMITER ;

CALL insert_data();

-- 执行全表扫描查询
SELECT * FROM test_cpu WHERE age = 50;
```

### 2.2 复杂查询场景

```sql
-- 创建关联表
CREATE TABLE test_cpu_related (
  id INT PRIMARY KEY AUTO_INCREMENT,
  test_id INT,
  value VARCHAR(100),
  FOREIGN KEY (test_id) REFERENCES test_cpu(id)
);

-- 插入关联数据
DELIMITER //
CREATE PROCEDURE insert_related_data()
BEGIN
  DECLARE i INT DEFAULT 0;
  WHILE i < 500000 DO
    INSERT INTO test_cpu_related (test_id, value) VALUES (i % 1000000 + 1, CONCAT('value', i));
    SET i = i + 1;
  END WHILE;
END //
DELIMITER ;

CALL insert_related_data();

-- 执行复杂关联查询
SELECT t1.*, t2.* 
FROM test_cpu t1 
JOIN test_cpu_related t2 ON t1.id = t2.test_id 
WHERE t1.age > 50 
ORDER BY t1.id DESC 
LIMIT 1000;
```

### 2.3 大量排序场景

```sql
-- 执行大量排序操作
SELECT * FROM test_cpu ORDER BY address DESC;
```

### 2.4 并发连接场景

使用压测工具如 `sysbench` 或 `mysqlslap` 模拟大量并发连接：

```bash
# 使用 mysqlslap 模拟 100 个并发连接，每个连接执行 1000 次查询
mysqlslap --user=root --password=password --host=localhost --concurrency=100 --iterations=1 --query="SELECT * FROM test_cpu WHERE age = RAND() * 100" --create-schema=test
```

## 3. 如何查看和分析 MySQL CPU 使用率高的问题

### 3.1 查看系统 CPU 使用率

```bash
# 查看整体 CPU 使用率
top

# 查看 MySQL 进程的 CPU 使用率
top -p $(pidof mysqld)

# 查看详细的 CPU 使用情况
mpstat -P ALL 1
```

### 3.2 查看 MySQL 内部状态

```sql
-- 查看当前正在执行的 SQL 语句
SHOW PROCESSLIST;

-- 查看慢查询日志
-- 首先开启慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL slow_query_log_file = '/var/log/mysql/mysql-slow.log';
SET GLOBAL long_query_time = 1; -- 1秒以上的查询记录为慢查询

-- 查看慢查询日志内容
SHOW GLOBAL VARIABLES LIKE '%slow%';

-- 查看 Innodb 状态
SHOW ENGINE INNODB STATUS;

-- 查看 MySQL 全局状态
SHOW GLOBAL STATUS;

-- 查看连接数
SHOW GLOBAL STATUS LIKE 'Threads%';

-- 查看缓冲区使用情况
SHOW GLOBAL STATUS LIKE 'Innodb_buffer_pool%';
```

### 3.3 使用性能分析工具

#### 3.3.1 EXPLAIN 分析 SQL 执行计划

```sql
EXPLAIN SELECT * FROM test_cpu WHERE age = 50;
```

#### 3.3.2 使用 Performance Schema

```sql
-- 查看 Performance Schema 是否开启
SHOW VARIABLES LIKE 'performance_schema';

-- 查看 SQL 语句的执行统计信息
SELECT * FROM performance_schema.events_statements_summary_by_digest ORDER BY sum_timer_wait DESC LIMIT 10;

-- 查看等待事件
SELECT * FROM performance_schema.events_waits_summary_global_by_event_name ORDER BY sum_timer_wait DESC LIMIT 10;
```

#### 3.3.3 使用 MySQL Profiler

```sql
-- 开启 Profiler
SET profiling = 1;

-- 执行 SQL 语句
SELECT * FROM test_cpu WHERE age = 50;

-- 查看执行计划
SHOW PROFILES;

-- 查看详细的执行步骤
SHOW PROFILE FOR QUERY 1;
```

## 4. 解决思路

### 4.1 优化 SQL 语句

- **添加合适的索引**：根据查询条件创建索引
- **优化查询语句**：避免全表扫描，减少复杂关联
- **使用分页查询**：避免一次性返回大量数据
- **优化排序操作**：使用索引排序，避免文件排序

### 4.2 优化服务器配置

- **调整缓冲区大小**：
  ```ini
  innodb_buffer_pool_size = 8G  # 根据服务器内存调整，一般为内存的 50-80%
  ```

- **调整连接数**：
  ```ini
  max_connections = 1000  # 根据服务器性能调整
  ```

- **调整查询缓存**：
  ```ini
  query_cache_type = 1
  query_cache_size = 64M
  ```

- **调整 innodb 相关参数**：
  ```ini
  innodb_flush_log_at_trx_commit = 2
  innodb_log_buffer_size = 16M
  innodb_thread_concurrency = 0  # 0 表示不限制
  ```

### 4.3 硬件优化

- **增加 CPU 核心数**：提升并发处理能力
- **增加内存**：减少磁盘 I/O
- **使用 SSD**：提升磁盘 I/O 性能
- **使用负载均衡**：分散数据库压力

### 4.4 应用层优化

- **使用连接池**：减少连接建立和关闭的开销
- **缓存热点数据**：使用 Redis 等缓存工具
- **批量操作**：减少数据库交互次数
- **异步处理**：将耗时操作异步化

## 5. 案例分析

### 5.1 案例一：缺少索引导致的 CPU 使用率高

**现象**：执行 `SELECT * FROM users WHERE email = 'user@example.com'` 时 CPU 使用率飙升。

**分析**：`email` 字段没有创建索引，导致全表扫描。

**解决方案**：
```sql
CREATE INDEX idx_email ON users(email);
```

### 5.2 案例二：复杂查询导致的 CPU 使用率高

**现象**：执行包含多个表关联和聚合函数的查询时 CPU 使用率高。

**分析**：查询过于复杂，需要大量的计算和排序操作。

**解决方案**：
- 优化查询语句，减少关联表数量
- 为关联字段创建索引
- 考虑使用视图或中间表

### 5.3 案例三：并发连接过多导致的 CPU 使用率高

**现象**：系统高峰期 CPU 使用率高，连接数接近最大值。

**分析**：并发连接数过多，导致 CPU 资源竞争。

**解决方案**：
- 调整 `max_connections` 参数
- 使用连接池管理连接
- 优化应用程序，减少长时间占用连接的操作

## 6. 总结

MySQL CPU 使用率高是一个常见的性能问题，需要从多个方面进行分析和优化：

1. **SQL 语句优化**：确保使用合适的索引，避免复杂查询
2. **服务器配置优化**：根据实际情况调整缓冲区大小、连接数等参数
3. **硬件资源优化**：提升服务器硬件性能
4. **应用层优化**：使用缓存、连接池等技术减少数据库压力

通过综合使用这些方法，可以有效降低 MySQL 的 CPU 使用率，提升系统性能。