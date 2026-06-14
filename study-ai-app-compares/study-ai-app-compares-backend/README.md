# Price Compare Backend

价格比较应用后端服务 - 基于 Spring Boot 3.3.0，提供商品价格对比和地址管理功能。

## 项目特性

- **价格对比**：支持对比美团、京东外卖、淘宝闪购三大平台价格
- **地址管理**：收货地址的增删改查
- **智能推荐**：自动推荐最低价平台
- **RESTful API**：标准化的 API 接口设计

## 技术栈

- Java 21
- Spring Boot 3.3.0
- Spring Boot Validation
- Lombok

## 项目结构

```
study-ai-app-compares-backend/
├── src/main/java/com/bage/study/ai/
│   ├── Application.java              # 启动类
│   ├── config/                       # 配置模块
│   │   └── WebConfig.java            # CORS配置
│   ├── controller/                   # REST API
│   │   ├── PriceCompareController.java  # 价格比较接口
│   │   ├── AddressController.java       # 地址管理接口
│   │   └── HealthController.java        # 健康检查接口
│   ├── service/                      # 服务层
│   │   ├── PriceCompareService.java     # 价格比较服务接口
│   │   ├── AddressService.java          # 地址服务接口
│   │   └── impl/
│   │       ├── PriceCompareServiceImpl.java
│   │       └── AddressServiceImpl.java
│   ├── dto/                          # 数据传输对象
│   │   ├── request/
│   │   │   ├── ProductSearchRequest.java
│   │   │   └── AddressRequest.java
│   │   └── response/
│   │       ├── ProductPriceResponse.java
│   │       ├── CompareResultResponse.java
│   │       └── AddressResponse.java
│   └── exception/                    # 异常处理
│       ├── BusinessException.java
│       ├── ErrorResponse.java
│       └── GlobalExceptionHandler.java
├── src/main/resources/
│   └── application.yml               # 应用配置
└── pom.xml
```

## 快速开始

### 1. 环境要求

- JDK 21+
- Maven 3.8+

### 2. 编译运行

```bash
# 编译
mvn compile

# 运行
mvn spring-boot:run
```

### 3. 健康检查

```bash
curl http://localhost:8080/api/health
```

## API 接口

### 价格比较

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/compare/search` | 搜索并比较价格 |
| GET | `/api/compare/search?productName=xxx&addressId=xxx` | 搜索并比较价格(GET) |

**请求示例：**
```bash
curl -X POST http://localhost:8080/api/compare/search \
  -H "Content-Type: application/json" \
  -d '{"productName": "生椰拿铁", "addressId": "1"}'
```

**响应示例：**
```json
{
  "productName": "生椰拿铁",
  "address": "北京市朝阳区 望京SOHO T1 1201室",
  "prices": [...],
  "bestRecommendation": {...}
}
```

### 地址管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/addresses` | 获取地址列表 |
| GET | `/api/addresses/{id}` | 获取单个地址 |
| POST | `/api/addresses` | 创建地址 |
| POST | `/api/addresses/{id}/update` | 更新地址 |
| POST | `/api/addresses/{id}/delete` | 删除地址 |

**创建地址请求示例：**
```bash
curl -X POST http://localhost:8080/api/addresses \
  -H "Content-Type: application/json" \
  -d '{
    "receiverName": "张三",
    "phoneNumber": "13800138000",
    "provinceCityDistrict": "北京市朝阳区",
    "detailAddress": "望京SOHO T1 1201室",
    "isDefault": true
  }'
```

## 默认数据

系统预置了两个测试地址：

1. **张三** - 北京市朝阳区望京SOHO T1 1201室（默认地址）
2. **李四** - 上海市浦东新区陆家嘴金融中心88层

## License

MIT