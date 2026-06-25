# 价格比较助手 - 项目文档

## 项目简介

价格比较助手是一款基于 **Spring Boot + Vue 3** 技术栈的全栈应用，旨在帮助用户快速比较美团、京东外卖、淘宝闪购三个平台的商品价格，提供智能推荐和配送地址管理功能。

## 核心功能

### 1. 价格对比
- 输入商品名称，一键获取三个平台的价格信息
- 展示商品价格、配送费、总价等详细信息
- 自动推荐价格最低的购买方案

### 2. 地址管理
- 维护收货地址列表
- 支持设置默认地址
- 配送地址关联价格计算

### 3. 搜索历史
- 记录用户搜索历史
- 快速重复搜索
- 查看历史比价结果

### 4. 缓存优化
- Redis缓存比价结果
- 降低API调用频率
- 提升响应速度

## 技术栈

### 后端技术栈
| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21 | 编程语言 |
| Spring Boot | 3.3.0 | 后端框架 |
| Spring Data JPA | 3.3.x | 数据持久化 |
| Spring Data Redis | 3.3.x | 缓存管理 |
| H2 Database | 2.2.x | 开发环境数据库 |
| MySQL | 8.0+ | 生产环境数据库 |
| Maven | 3.9+ | 构建工具 |

### 前端技术栈
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4+ | 前端框架 |
| Vite | 5.1+ | 构建工具 |
| Pinia | 3.0+ | 状态管理 |
| Vue Router | 4.6+ | 路由管理 |
| Axios | 1.6+ | HTTP客户端 |
| Lucide Vue | 0.577+ | 图标库 |

## 项目结构

```
study-ai-app-compares/
├── docs/                    # 项目文档
├── study-ai-app-compares-backend/  # 后端服务
│   ├── src/main/java/com/bage/study/ai/
│   │   ├── config/          # 配置类
│   │   ├── controller/      # REST控制器
│   │   ├── entity/          # 实体类
│   │   ├── exception/       # 异常处理
│   │   ├── repository/      # 数据访问层
│   │   └── service/         # 业务逻辑层
│   └── src/main/resources/  # 配置文件
└── study-ai-app-compares-frontend/  # 前端应用
    └── src/
        ├── components/      # 公共组件
        ├── router/          # 路由配置
        ├── services/        # API服务
        ├── stores/          # 状态管理
        └── views/           # 页面视图
```

## 快速开始

### 后端启动

```bash
cd study-ai-app-compares-backend
mvn spring-boot:run
```

服务启动后访问：http://localhost:8080

### 前端启动

```bash
cd study-ai-app-compares-frontend
npm install
npm run dev
```

前端启动后访问：http://localhost:5173

### 生产环境

后端使用生产配置启动：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

前端构建：
```bash
npm run build
```

## 配置说明

### 环境变量

后端支持以下环境变量：

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| SERVER_PORT | 8080 | 服务端口 |
| DB_HOST | localhost | 数据库主机 |
| DB_PORT | 3306 | 数据库端口 |
| DB_NAME | pricecompare | 数据库名 |
| REDIS_HOST | localhost | Redis主机 |
| REDIS_PORT | 6379 | Redis端口 |
| TAOBAO_APP_KEY | - | 淘宝API Key |
| TAOBAO_APP_SECRET | - | 淘宝API Secret |
| JD_APP_KEY | - | 京东API Key |
| JD_APP_SECRET | - | 京东API Secret |
| MEITUAN_APP_KEY | - | 美团API Key |
| MEITUAN_APP_SECRET | - | 美团API Secret |

前端支持以下环境变量：

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| VITE_API_BASE_URL | /api | API基础地址 |
| VITE_PORT | 5173 | 前端端口 |

## API接口

### 价格对比
- `POST /api/compare/search` - 搜索比价
- `GET /api/compare/search` - 查询比价结果

### 地址管理
- `GET /api/addresses` - 获取地址列表
- `POST /api/addresses` - 创建地址
- `GET /api/addresses/{id}` - 获取单个地址
- `POST /api/addresses/{id}/update` - 更新地址
- `POST /api/addresses/{id}/delete` - 删除地址

### 健康检查
- `GET /api/health` - 健康检查

## 开发规范

### 代码规范
- 后端遵循 Java 编码规范，使用 Lombok 简化代码
- 前端遵循 Vue 3 组合式API规范
- 使用 Prettier 格式化代码

### 提交规范
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式
- refactor: 重构
- test: 测试
- chore: 构建/工具

## 许可证

MIT License
