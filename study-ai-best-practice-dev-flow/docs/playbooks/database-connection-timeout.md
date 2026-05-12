# 数据库连接超时故障排查手册

## 故障描述

应用程序无法连接到数据库，抛出连接超时异常。

## 常见症状

```
org.springframework.jdbc.CannotGetJdbcConnectionException:
  Could not get JDBC Connection; nested exception is java.sql.SQLException:
  Timeout waiting for connection from pool
```

或

```
java.net.ConnectException: Connection refused
```

## 排查步骤

### 1. 检查数据库服务状态

```bash
# 检查 MySQL 服务状态
sudo systemctl status mysql

# 检查 H2 数据库文件是否存在
ls -la *.db
```

### 2. 检查数据库连接配置

确认 `application.properties` 或 `application.yml` 配置正确：

```properties
# MySQL 配置
spring.datasource.url=jdbc:mysql://localhost:3306/dbname
spring.datasource.username=root
spring.datasource.password=password

# H2 配置
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
```

### 3. 检查连接池配置

```properties
# HikariCP 连接池配置
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
```

### 4. 检查防火墙和端口

```bash
# 检查端口是否开放
netstat -an | grep 3306

# 检查防火墙规则
sudo iptables -L -n | grep 3306
```

## 自动修复步骤

### 方案一：重启数据库服务

```bash
# MySQL
sudo systemctl restart mysql

# H2 (删除锁文件)
rm -f *.db.lock
```

### 方案二：增加连接超时时间

在 `application.properties` 中添加：

```properties
spring.datasource.hikari.connection-timeout=60000
```

### 方案三：切换到内存数据库

如遇持久化数据库问题，可临时切换到 H2 内存数据库：

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

## 预防措施

1. 监控数据库连接池使用情况
2. 设置合理的连接超时时间
3. 使用连接池监控工具
4. 定期检查数据库服务状态

## 相关配置

- 连接池依赖：`spring-boot-starter-jdbc`
- HikariCP 默认配置：`spring.datasource.hikari.*`