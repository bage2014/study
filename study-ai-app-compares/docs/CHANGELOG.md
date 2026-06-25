# 更新变更日志

## v1.1.0 (2026-06-25)

### 新增功能

- **Redis缓存支持**：添加Spring Data Redis依赖，实现比价结果缓存，提升响应速度
- **Redis配置类**：创建RedisConfig配置类，配置JSON序列化器，支持Java 8时间类型
- **缓存集成**：修改PriceCompareServiceImpl，搜索结果自动缓存30分钟，缓存key基于商品名称和地址ID生成
- **生产环境配置**：新增application-prod.yml，支持MySQL数据库、关闭H2控制台、调整日志级别
- **前端环境配置**：新增.env和.env.production文件，支持环境变量配置
- **vite配置优化**：更新vite.config.js，支持环境变量加载、路径别名、WebSocket代理
- **API配置优化**：更新api.js，支持VITE_API_BASE_URL环境变量，添加响应拦截器

### 修复问题

- **配置文件修复**：修复application.yml中spring节点重复的问题
- **MySQL驱动依赖**：添加mysql-connector-j依赖，支持生产环境MySQL

### 文档更新

- **docs目录创建**：创建docs目录，整理项目文档
- **项目文档**：新增README.md，包含项目简介、技术栈、快速开始、API接口等
- **技术规格文档**：新增spec.md，包含需求分析、系统架构、数据模型、API设计等
- **变更日志**：新增CHANGELOG.md，记录版本更新内容
- **TODO待办文档**：新增TODO.md，记录待办事项

### 依赖更新

- 添加 `spring-boot-starter-data-redis`
- 添加 `mysql-connector-j`

## v1.0.0 (2026-06-24)

### 新增功能

- **价格对比功能**：支持输入商品名称，一键对比美团、京东外卖、淘宝闪购三个平台的价格
- **地址管理功能**：支持创建、查询、更新、删除收货地址，支持设置默认地址
- **搜索历史功能**：记录用户搜索历史，支持快速重复搜索
- **智能推荐功能**：自动推荐价格最低的购买方案
- **API服务封装**：封装淘宝、京东、美团三个平台的API服务，统一数据返回格式
- **模拟数据支持**：支持模拟数据返回，便于开发测试
- **健康检查接口**：提供健康检查API，便于运维监控

### 技术实现

- **后端技术栈**：Java 21 + Spring Boot 3.3.0 + Spring Data JPA + H2数据库
- **前端技术栈**：Vue 3 + Vite 5 + Pinia + Vue Router + Axios
- **数据持久化**：使用JPA实现地址、搜索历史、价格缓存的持久化
- **异步调用**：使用CompletableFuture并行调用三个平台API，提升响应速度
- **异常处理**：全局异常处理，统一错误响应格式
- **跨域配置**：配置CORS跨域支持
- **单元测试**：添加地址管理和价格比较的单元测试

### 项目结构

```
study-ai-app-compares/
├── study-ai-app-compares-backend/
│   ├── src/main/java/com/bage/study/ai/
│   │   ├── config/          # 配置类
│   │   ├── controller/      # REST控制器
│   │   ├── entity/          # 实体类
│   │   ├── exception/       # 异常处理
│   │   ├── repository/      # 数据访问层
│   │   └── service/         # 业务逻辑层
│   └── src/main/resources/  # 配置文件
└── study-ai-app-compares-frontend/
    └── src/
        ├── components/      # 公共组件
        ├── router/          # 路由配置
        ├── services/        # API服务
        ├── stores/          # 状态管理
        └── views/           # 页面视图
```

### API接口

- `POST /api/compare/search` - 搜索比价
- `GET /api/compare/search` - 查询比价结果
- `GET /api/addresses` - 获取地址列表
- `POST /api/addresses` - 创建地址
- `GET /api/addresses/{id}` - 获取单个地址
- `POST /api/addresses/{id}/update` - 更新地址
- `POST /api/addresses/{id}/delete` - 删除地址
- `GET /api/health` - 健康检查
