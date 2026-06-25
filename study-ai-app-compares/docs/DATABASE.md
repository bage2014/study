# 数据库设计文档

## 1. 数据库概述

本项目使用 **H2** 作为开发环境数据库，**MySQL 8.0+** 作为生产环境数据库。数据库设计遵循规范化原则，确保数据完整性和查询效率。

## 2. 数据库配置

### 2.1 开发环境（H2）

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:pricecompare;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: 
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true
      path: /h2-console
```

### 2.2 生产环境（MySQL）

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 3. ER关系图

```
┌──────────────────────────────────────────────────────────────────────┐
│                          ER Diagram                                  │
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌──────────────┐        ┌─────────────────┐                       │
│   │   ADDRESS    │        │ SEARCH_HISTORY   │                       │
│   ├──────────────┤        ├─────────────────┤                       │
│   │ id (PK)      │◄───────│ address_id (FK) │                       │
│   │ receiver_name│        │ keyword         │                       │
│   │ phone_number │        │ platform_count  │                       │
│   │ province...  │        │ best_price      │                       │
│   │ detail_addr  │        │ best_platform   │                       │
│   │ is_default   │        │ created_at      │                       │
│   │ created_at   │        └─────────────────┘                       │
│   │ updated_at   │                                                  │
│   └──────────────┘                                                  │
│                                                                      │
│   ┌─────────────────────────────────┐                               │
│   │    PLATFORM_PRICE_CACHE         │                               │
│   ├─────────────────────────────────┤                               │
│   │ id (PK)                         │                               │
│   │ keyword                         │                               │
│   │ platform                        │                               │
│   │ product_name                    │                               │
│   │ store_name                      │                               │
│   │ price                           │                               │
│   │ delivery_fee                    │                               │
│   │ total_price                     │                               │
│   │ store_rating                    │                               │
│   │ delivery_time                   │                               │
│   │ promotion                       │                               │
│   │ expire_time                     │                               │
│   │ created_at                      │                               │
│   └─────────────────────────────────┘                               │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```

## 4. 表结构详细设计

### 4.1 ADDRESS（收货地址表）

#### 表定义

```sql
CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    phone_number VARCHAR(20) NOT NULL COMMENT '联系电话',
    province_city_district VARCHAR(100) NOT NULL COMMENT '省市区',
    detail_address VARCHAR(200) NOT NULL COMMENT '详细地址',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认地址',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_address_default (is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址表';
```

#### 实体类映射

| 字段名 | 实体属性 | Java类型 | JPA注解 |
|--------|----------|----------|---------|
| id | id | Long | @Id @GeneratedValue |
| receiver_name | receiverName | String | @Column(nullable=false) |
| phone_number | phoneNumber | String | @Column(nullable=false) |
| province_city_district | provinceCityDistrict | String | @Column(nullable=false) |
| detail_address | detailAddress | String | @Column(nullable=false) |
| is_default | isDefault | Boolean | @Column(columnDefinition="BOOLEAN DEFAULT FALSE") |
| created_at | createdAt | LocalDateTime | @Column(nullable=false) |
| updated_at | updatedAt | LocalDateTime | @Column(nullable=false) |

### 4.2 SEARCH_HISTORY（搜索历史表）

#### 表定义

```sql
CREATE TABLE search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(100) NOT NULL COMMENT '搜索关键词',
    address_id BIGINT COMMENT '关联地址ID',
    platform_count INT NOT NULL COMMENT '返回平台数量',
    best_price DECIMAL(10,2) COMMENT '最优价格',
    best_platform VARCHAR(50) COMMENT '最优平台',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_search_keyword (keyword),
    INDEX idx_search_created (created_at),
    CONSTRAINT fk_search_address FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='搜索历史表';
```

#### 实体类映射

| 字段名 | 实体属性 | Java类型 | JPA注解 |
|--------|----------|----------|---------|
| id | id | Long | @Id @GeneratedValue |
| keyword | keyword | String | @Column(nullable=false) |
| address_id | addressId | Long | @Column |
| platform_count | platformCount | Integer | @Column(nullable=false) |
| best_price | bestPrice | Double | @Column |
| best_platform | bestPlatform | String | @Column |
| created_at | createdAt | LocalDateTime | @Column(nullable=false) |

### 4.3 PLATFORM_PRICE_CACHE（价格缓存表）

#### 表定义

```sql
CREATE TABLE platform_price_cache (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(100) NOT NULL COMMENT '搜索关键词',
    platform VARCHAR(50) NOT NULL COMMENT '平台名称',
    product_name VARCHAR(200) COMMENT '商品名称',
    store_name VARCHAR(100) COMMENT '店铺名称',
    price DECIMAL(10,2) COMMENT '商品价格',
    delivery_fee DECIMAL(10,2) COMMENT '配送费',
    total_price DECIMAL(10,2) COMMENT '总价',
    store_rating VARCHAR(10) COMMENT '店铺评分',
    delivery_time INT COMMENT '配送时间(分钟)',
    promotion VARCHAR(100) COMMENT '优惠信息',
    expire_time TIMESTAMP NOT NULL COMMENT '过期时间',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_cache_keyword (keyword),
    INDEX idx_cache_platform (platform),
    INDEX idx_cache_expire (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='价格缓存表';
```

#### 实体类映射

| 字段名 | 实体属性 | Java类型 | JPA注解 |
|--------|----------|----------|---------|
| id | id | Long | @Id @GeneratedValue |
| keyword | keyword | String | @Column(nullable=false) |
| platform | platform | String | @Column(nullable=false) |
| product_name | productName | String | @Column |
| store_name | storeName | String | @Column |
| price | price | Double | @Column |
| delivery_fee | deliveryFee | Double | @Column |
| total_price | totalPrice | Double | @Column |
| store_rating | storeRating | String | @Column |
| delivery_time | deliveryTime | Integer | @Column |
| promotion | promotion | String | @Column |
| expire_time | expireTime | LocalDateTime | @Column(nullable=false) |
| created_at | createdAt | LocalDateTime | @Column(nullable=false) |

## 5. 索引优化策略

### 5.1 索引设计原则

1. **主键索引**：所有表的id字段都应建立主键索引
2. **查询优化**：根据业务查询场景建立合适的普通索引
3. **外键索引**：外键字段建议建立索引，加速关联查询
4. **复合索引**：对于联合查询条件，可以考虑建立复合索引

### 5.2 索引清单

| 表名 | 索引名 | 字段 | 类型 | 用途 |
|------|--------|------|------|------|
| address | PRIMARY | id | 主键索引 | 主键查询 |
| address | idx_address_default | is_default | 普通索引 | 查询默认地址 |
| search_history | PRIMARY | id | 主键索引 | 主键查询 |
| search_history | idx_search_keyword | keyword | 普通索引 | 关键词搜索 |
| search_history | idx_search_created | created_at | 普通索引 | 时间范围查询 |
| platform_price_cache | PRIMARY | id | 主键索引 | 主键查询 |
| platform_price_cache | idx_cache_keyword | keyword | 普通索引 | 关键词查询 |
| platform_price_cache | idx_cache_platform | platform | 普通索引 | 平台过滤 |
| platform_price_cache | idx_cache_expire | expire_time | 普通索引 | 过期数据清理 |

## 6. 数据初始化

### 6.1 默认地址数据

系统启动时会自动初始化两条默认地址数据，便于开发测试：

```sql
INSERT INTO address (receiver_name, phone_number, province_city_district, detail_address, is_default, created_at, updated_at)
VALUES 
('张三', '13800138000', '北京市朝阳区', '某某街道123号', true, NOW(), NOW()),
('李四', '13900139000', '上海市浦东新区', '某某路456号', false, NOW(), NOW());
```

### 6.2 缓存清理策略

平台价格缓存表的数据需要定期清理过期数据，建议通过定时任务执行：

```sql
DELETE FROM platform_price_cache WHERE expire_time < NOW();
```

## 7. 数据库迁移

### 7.1 Flyway/Liquibase（建议）

生产环境建议使用数据库迁移工具管理Schema变更：

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
```

### 7.2 迁移脚本示例

```sql
-- V1__create_tables.sql
CREATE TABLE address (...);
CREATE TABLE search_history (...);
CREATE TABLE platform_price_cache (...);

-- V2__add_indexes.sql
CREATE INDEX idx_address_default ON address(is_default);
CREATE INDEX idx_search_keyword ON search_history(keyword);
CREATE INDEX idx_cache_keyword ON platform_price_cache(keyword);
```

## 8. 性能优化建议

### 8.1 查询优化

1. **避免全表扫描**：确保所有查询都使用索引
2. **分页查询**：大数据量查询使用分页
3. **查询缓存**：利用Redis缓存减少数据库查询

### 8.2 连接池配置

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
```

### 8.3 读写分离（可选）

对于高并发场景，可以考虑配置MySQL读写分离：

```yaml
spring:
  datasource:
    master:
      url: jdbc:mysql://master:3306/pricecompare
    slave:
      url: jdbc:mysql://slave:3306/pricecompare
```

## 9. 数据库安全

### 9.1 权限管理

- 生产环境使用独立数据库用户
- 遵循最小权限原则，只授予必要权限
- 定期轮换数据库密码

### 9.2 数据加密

- 敏感字段（如手机号）建议加密存储
- 使用AES等对称加密算法
- 密钥通过环境变量配置，不硬编码

### 9.3 备份策略

- 定期全量备份
- 增量备份（binlog）
- 备份文件存储在安全位置
