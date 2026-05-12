# 数据库设计规范

## 一、数据库选择

| 数据库类型 | 使用场景 | 示例 |
|------------|----------|------|
| MySQL | 生产环境主数据库 | 用户数据、业务数据 |
| H2 | 开发环境、测试环境 | 本地开发、单元测试 |
| Redis | 缓存、会话存储 | Token缓存、热点数据 |

## 二、命名规范

### 2.1 数据库命名

- 使用小写字母
- 使用下划线分隔
- 格式：`{项目缩写}_{环境}`

| 环境 | 数据库名示例 |
|------|--------------|
| 开发 | `study_dev` |
| 测试 | `study_test` |
| 生产 | `study_prod` |

### 2.2 表命名

- 使用小写字母
- 使用下划线分隔
- 格式：`{模块}_{功能}`

| 表名 | 说明 |
|------|------|
| `sys_user` | 用户表 |
| `sys_role` | 角色表 |
| `sys_permission` | 权限表 |
| `track_point` | 轨迹点表 |

### 2.3 字段命名

- 使用小写字母
- 使用下划线分隔
- 避免使用SQL关键字

| 字段名 | 说明 |
|--------|------|
| `id` | 主键 |
| `user_id` | 用户ID |
| `created_at` | 创建时间 |
| `updated_at` | 更新时间 |

### 2.4 索引命名

- 主键索引：`PRIMARY`（默认）
- 唯一索引：`uk_{表名}_{字段名}`
- 普通索引：`idx_{表名}_{字段名}`

## 三、表结构规范

### 3.1 必备字段

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| `id` | BIGINT | 主键ID | 自增、非空 |
| `created_at` | DATETIME | 创建时间 | 非空、默认CURRENT_TIMESTAMP |
| `updated_at` | DATETIME | 更新时间 | 非空、默认CURRENT_TIMESTAMP ON UPDATE |
| `created_by` | VARCHAR(64) | 创建人 | 可空 |
| `updated_by` | VARCHAR(64) | 更新人 | 可空 |
| `deleted` | TINYINT | 删除标记 | 默认0，0=未删除，1=已删除 |

### 3.2 表结构示例

```sql
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(64) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
  `email` VARCHAR(128) COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '手机号',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0禁用, 1启用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` VARCHAR(64) COMMENT '创建人',
  `updated_by` VARCHAR(64) COMMENT '更新人',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_username` (`username`),
  UNIQUE KEY `uk_sys_user_email` (`email`),
  INDEX `idx_sys_user_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

## 四、数据类型规范

### 4.1 常用数据类型选择

| 数据类型 | 使用场景 | 注意事项 |
|----------|----------|----------|
| INT | 小范围整数 | 范围：-2^31 ~ 2^31-1 |
| BIGINT | 大范围整数 | 主键ID推荐使用 |
| VARCHAR | 可变长度字符串 | 最大65535字符 |
| TEXT | 长文本 | 超过VARCHAR长度时使用 |
| DATETIME | 日期时间 | 精度到秒 |
| DECIMAL | 精确小数 | 金额、计算结果 |
| BOOLEAN | 布尔值 | 0/1 |

### 4.2 字段长度规范

| 字段类型 | 推荐长度 | 说明 |
|----------|----------|------|
| 用户名 | VARCHAR(64) | 最大64字符 |
| 密码 | VARCHAR(255) | 加密后长度 |
| 邮箱 | VARCHAR(128) | 符合邮箱格式 |
| 手机号 | VARCHAR(20) | 支持国际区号 |
| 姓名 | VARCHAR(64) | 中文姓名 |
| 地址 | VARCHAR(512) | 详细地址 |

## 五、索引规范

### 5.1 索引类型

| 索引类型 | 使用场景 |
|----------|----------|
| PRIMARY | 主键 |
| UNIQUE | 唯一约束字段（用户名、邮箱） |
| INDEX | 经常查询的字段 |

### 5.2 索引创建原则

1. **高频查询字段必须建索引**
2. **唯一约束必须建唯一索引**
3. **外键字段建议建索引**
4. **避免在大表上创建过多索引**
5. **复合索引遵循最左前缀原则**

### 5.3 禁止创建索引的场景

1. **频繁更新的字段**
2. **区分度低的字段（如status）**
3. **TEXT类型字段**

## 六、外键约束

### 6.1 外键使用原则

- 使用外键约束保证数据完整性
- 级联操作需谨慎使用
- 外键字段建议建索引

### 6.2 外键示例

```sql
CREATE TABLE `track_point` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `latitude` DECIMAL(10,7) NOT NULL COMMENT '纬度',
  `longitude` DECIMAL(10,7) NOT NULL COMMENT '经度',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_track_point_user_id` (`user_id`),
  CONSTRAINT `fk_track_point_user` FOREIGN KEY (`user_id`) 
    REFERENCES `sys_user` (`id`) 
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轨迹点表';
```

## 七、SQL编写规范

### 7.1 SELECT语句规范

```sql
-- ✅ 正确：指定字段，避免SELECT *
SELECT id, username, email FROM sys_user WHERE status = 1;

-- ❌ 错误：使用SELECT *
SELECT * FROM sys_user;
```

### 7.2 WHERE条件规范

```sql
-- ✅ 正确：避免在字段上使用函数
SELECT * FROM sys_user WHERE created_at >= '2024-01-01';

-- ❌ 错误：在字段上使用函数，无法使用索引
SELECT * FROM sys_user WHERE DATE(created_at) = '2024-01-01';
```

### 7.3 JOIN语句规范

```sql
-- ✅ 正确：使用INNER JOIN
SELECT u.username, t.latitude, t.longitude 
FROM sys_user u
INNER JOIN track_point t ON u.id = t.user_id
WHERE u.status = 1;
```

### 7.4 分页查询规范

```sql
-- ✅ 正确：使用LIMIT分页
SELECT * FROM sys_user WHERE status = 1 ORDER BY created_at DESC LIMIT 0, 20;
```

## 八、数据迁移规范

### 8.1 迁移文件命名

格式：`{日期}_{序号}_{描述}.sql`

示例：
- `20240101_001_create_sys_user.sql`
- `20240101_002_add_index_sys_user.sql`

### 8.2 迁移脚本要求

1. 必须包含回滚脚本
2. 必须有详细注释
3. 必须在测试环境验证通过

## 九、事务规范

### 9.1 事务使用原则

1. 保持事务简短
2. 避免在事务中进行耗时操作
3. 使用合适的事务隔离级别

### 9.2 事务隔离级别

| 隔离级别 | 说明 |
|----------|------|
| READ UNCOMMITTED | 读未提交 |
| READ COMMITTED | 读已提交（MySQL默认） |
| REPEATABLE READ | 可重复读 |
| SERIALIZABLE | 串行化 |

## 十、安全规范

### 10.1 禁止SQL拼接

```java
// ✅ 正确：使用参数化查询
@Query("SELECT u FROM User u WHERE u.username = :username")
Optional<User> findByUsername(@Param("username") String username);

// ❌ 错误：字符串拼接
String sql = "SELECT * FROM sys_user WHERE username = '" + username + "'";
```

### 10.2 敏感数据加密

- 密码使用BCrypt加密
- 敏感字段（手机号、身份证）脱敏存储

## 十一、性能优化

### 11.1 查询优化

1. 添加合适的索引
2. 避免全表扫描
3. 使用覆盖索引

### 11.2 批量操作

```java
// ✅ 正确：批量插入
userRepository.saveAll(userList);

// ✅ 正确：批量更新
@Modifying
@Query("UPDATE User u SET u.status = :status WHERE u.id IN :ids")
void updateStatusByIds(@Param("status") Integer status, @Param("ids") List<Long> ids);
```

### 11.3 缓存策略

- 使用Redis缓存热点数据
- 设置合理的缓存过期时间
- 数据更新时同步更新缓存