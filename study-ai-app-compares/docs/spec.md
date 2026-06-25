# 价格比较助手 - 技术规格文档

## 1. 需求分析

### 1.1 业务背景

用户在日常购物时，需要对比美团、京东外卖、淘宝闪购三个平台的商品价格，以获得最优购买方案。手动逐个平台搜索比价效率低下，需要一个统一的工具来简化这个过程。

### 1.2 功能需求

| 需求编号 | 需求描述 | 优先级 | 关联模块 |
|----------|----------|--------|----------|
| FR-001 | 用户输入商品名称，系统查询三个平台价格 | 高 | 后端服务、前端页面 |
| FR-002 | 系统展示各平台商品价格、配送费、总价 | 高 | 后端服务、前端页面 |
| FR-003 | 系统自动推荐价格最低的购买方案 | 高 | 后端服务、前端页面 |
| FR-004 | 用户维护收货地址列表 | 中 | 后端服务、前端页面 |
| FR-005 | 用户设置默认收货地址 | 中 | 后端服务、前端页面 |
| FR-006 | 系统记录搜索历史 | 中 | 后端服务、前端页面 |
| FR-007 | 用户查看搜索历史 | 中 | 后端服务、前端页面 |
| FR-008 | 搜索结果缓存，提升响应速度 | 中 | 后端服务 |

### 1.3 非功能需求

| 需求编号 | 需求描述 | 优先级 |
|----------|----------|--------|
| NFR-001 | API响应时间 < 3秒 | 高 |
| NFR-002 | 支持并发用户数 >= 100 | 中 |
| NFR-003 | 数据一致性保障 | 中 |
| NFR-004 | 系统可扩展性，便于添加新平台 | 中 |

## 2. 系统架构

### 2.1 架构设计

采用经典的**前后端分离**架构：

```
┌─────────────────────────────────────────────────────────────┐
│                        前端层                                │
│  Vue 3 + Pinia + Vue Router + Axios                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                  │
│  │  Home    │  │ Address  │  │ History  │                  │
│  │  Page    │  │  List    │  │  Page    │                  │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘                  │
└───────┼──────────────┼──────────────┼───────────────────────┘
        │              │              │
        ▼              ▼              ▼
┌─────────────────────────────────────────────────────────────┐
│                        后端层                                │
│  Spring Boot 3.3.0 + Java 21                                │
│  ┌──────────────────────────────────────────────────────┐   │
│  │               REST API Controller                     │   │
│  │  PriceCompareController | AddressController          │   │
│  └─────────────────┬────────────────────────────────────┘   │
│                    ▼                                        │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                     Service Layer                    │   │
│  │  PriceCompareService | AddressService                │   │
│  │  TaobaoApiService | JdApiService | MeituanApiService│   │
│  └─────────────────┬────────────────────────────────────┘   │
│                    ▼                                        │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                   Data Access Layer                   │   │
│  │  Repository (JPA) | Redis Cache                      │   │
│  └─────────────────┬────────────────────────────────────┘   │
│                    ▼                                        │
└───────────────────┬─────────────────────────────────────────┘
                    │
        ┌───────────┼───────────┐
        ▼           ▼           ▼
   ┌─────────┐ ┌─────────┐ ┌─────────┐
   │  H2/    │ │  Redis  │ │  第三方 │
   │ MySQL   │ │  Cache  │ │  API    │
   └─────────┘ └─────────┘ └─────────┘
```

### 2.2 核心流程

#### 2.2.1 价格对比流程

```
用户输入商品名称 → 前端调用API → 后端检查缓存 → 缓存命中返回结果
                                              ↓ 缓存未命中
                                    并行调用三个平台API → 聚合结果 → 存入缓存 → 返回结果
```

#### 2.2.2 地址管理流程

```
用户操作 → 前端调用对应API → 后端Service处理 → Repository持久化 → 返回结果
```

## 3. 数据模型设计

### 3.1 数据库实体

#### 3.1.1 Address（收货地址）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | Long | PK, AUTO_INCREMENT | 主键ID |
| receiverName | String | NOT NULL, VARCHAR(50) | 收货人姓名 |
| phoneNumber | String | NOT NULL, VARCHAR(20) | 联系电话 |
| provinceCityDistrict | String | NOT NULL, VARCHAR(100) | 省市区 |
| detailAddress | String | NOT NULL, VARCHAR(200) | 详细地址 |
| isDefault | Boolean | DEFAULT FALSE | 是否默认地址 |
| createdAt | DateTime | NOT NULL | 创建时间 |
| updatedAt | DateTime | NOT NULL | 更新时间 |

#### 3.1.2 SearchHistory（搜索历史）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | Long | PK, AUTO_INCREMENT | 主键ID |
| keyword | String | NOT NULL, VARCHAR(100) | 搜索关键词 |
| addressId | Long | NULLABLE, FK | 关联地址ID |
| platformCount | Integer | NOT NULL | 返回平台数量 |
| bestPrice | Double | NULLABLE | 最优价格 |
| bestPlatform | String | NULLABLE, VARCHAR(50) | 最优平台 |
| createdAt | DateTime | NOT NULL | 创建时间 |

#### 3.1.3 PlatformPriceCache（价格缓存）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | Long | PK, AUTO_INCREMENT | 主键ID |
| keyword | String | NOT NULL, VARCHAR(100) | 搜索关键词 |
| platform | String | NOT NULL, VARCHAR(50) | 平台名称 |
| productName | String | VARCHAR(200) | 商品名称 |
| storeName | String | VARCHAR(100) | 店铺名称 |
| price | Double | 商品价格 |
| deliveryFee | Double | 配送费 |
| totalPrice | Double | 总价 |
| storeRating | String | VARCHAR(10) | 店铺评分 |
| deliveryTime | Integer | 配送时间(分钟) |
| promotion | String | VARCHAR(100) | 优惠信息 |
| expireTime | DateTime | NOT NULL | 过期时间 |
| createdAt | DateTime | NOT NULL | 创建时间 |

### 3.2 索引设计

| 表名 | 索引名 | 字段 | 类型 |
|------|--------|------|------|
| address | idx_address_default | is_default | 普通索引 |
| search_history | idx_search_keyword | keyword | 普通索引 |
| search_history | idx_search_created | created_at | 普通索引 |
| platform_price_cache | idx_cache_keyword | keyword | 普通索引 |
| platform_price_cache | idx_cache_platform | platform | 普通索引 |
| platform_price_cache | idx_cache_expire | expire_time | 普通索引 |

## 4. API接口设计

### 4.1 价格对比接口

#### 4.1.1 搜索比价（POST）

- **路径**: `POST /api/compare/search`
- **请求体**:
```json
{
  "productName": "生椰拿铁",
  "addressId": "1"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| productName | String | 是 | 商品名称 |
| addressId | String | 否 | 地址ID |

- **成功响应** (200):
```json
{
  "productName": "生椰拿铁",
  "address": "北京市朝阳区 某某街道123号",
  "prices": [
    {
      "platform": "淘宝闪购",
      "platformLogo": "https://...",
      "productName": "生椰拿铁 - 淘宝闪购",
      "productImage": "https://...",
      "price": 25.9,
      "deliveryFee": 3.5,
      "totalPrice": 29.4,
      "storeName": "天猫超市",
      "storeRating": "4.8",
      "deliveryTime": 30,
      "promotion": "满30减8"
    }
  ],
  "bestRecommendation": {
    "platform": "淘宝闪购",
    "totalPrice": 29.4
  }
}
```

#### 4.1.2 查询比价结果（GET）

- **路径**: `GET /api/compare/search?productName=xxx&addressId=xxx`
- **响应**: 同POST接口

### 4.2 地址管理接口

#### 4.2.1 获取地址列表

- **路径**: `GET /api/addresses`
- **成功响应** (200):
```json
[
  {
    "id": 1,
    "receiverName": "张三",
    "phoneNumber": "13800138000",
    "provinceCityDistrict": "北京市朝阳区",
    "detailAddress": "某某街道123号",
    "isDefault": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

#### 4.2.2 创建地址

- **路径**: `POST /api/addresses`
- **请求体**:
```json
{
  "receiverName": "张三",
  "phoneNumber": "13800138000",
  "provinceCityDistrict": "北京市朝阳区",
  "detailAddress": "某某街道123号",
  "isDefault": false
}
```

#### 4.2.3 获取单个地址

- **路径**: `GET /api/addresses/{id}`

#### 4.2.4 更新地址

- **路径**: `POST /api/addresses/{id}/update`
- **请求体**: 同创建地址

#### 4.2.5 删除地址

- **路径**: `POST /api/addresses/{id}/delete`

### 4.3 健康检查接口

- **路径**: `GET /api/health`
- **成功响应** (200):
```json
{
  "status": "UP",
  "timestamp": "2024-01-01T10:00:00"
}
```

## 5. 缓存策略

### 5.1 Redis缓存设计

| 缓存Key | 类型 | 过期时间 | 说明 |
|---------|------|----------|------|
| price:compare:{keyword}:{addressId} | String(JSON) | 30分钟 | 比价结果缓存 |

### 5.2 缓存流程

1. 接收搜索请求
2. 构建缓存Key（关键词+地址ID）
3. 查询Redis缓存
4. 缓存命中 → 直接返回
5. 缓存未命中 → 调用API → 聚合结果 → 存入缓存 → 返回

## 6. 错误处理

### 6.1 错误响应格式

```json
{
  "code": 400,
  "message": "参数错误",
  "detail": "商品名称不能为空",
  "timestamp": "2024-01-01T10:00:00"
}
```

### 6.2 错误码定义

| 错误码 | 说明 |
|--------|------|
| 400 | 参数错误 |
| 404 | 资源不存在 |
| 500 | 系统内部错误 |
| 503 | 第三方服务不可用 |

## 7. 部署方案

### 7.1 开发环境

- 数据库：H2（内存模式）
- 缓存：Redis（本地）
- 前端：Vite开发服务器

### 7.2 生产环境

- 数据库：MySQL 8.0+
- 缓存：Redis
- 前端：Nginx静态资源
- 后端：Spring Boot Jar包

### 7.3 Docker部署（可选）

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/study-ai-app-compares-backend.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
