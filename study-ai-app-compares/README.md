# Price Compare Application

价格比较应用 - 一键对比美团、京东外卖、淘宝闪购三大平台价格。

## 项目特性

- **商品价格搜索**：输入商品名称即可查询三个平台价格
- **智能价格对比**：自动对比并推荐最低价平台
- **收货地址管理**：支持添加、管理多个收货地址
- **完整信息展示**：商品价格、配送费、店铺评分、配送时间、促销信息
- **响应式设计**：支持移动端和桌面端

## 技术栈

| 模块 | 技术 | 版本 |
|------|------|------|
| 后端 | Java | 21 |
| 后端 | Spring Boot | 3.3.0 |
| 前端 | Vue | 3.x |
| 前端 | Vite | 5.x |
| 前端 | Axios | 1.6.x |

## 项目结构

```
study-ai-app-compares/
├── study-ai-app-compares-backend/     # 后端服务
│   ├── src/main/java/com/bage/study/ai/
│   ├── src/main/resources/
│   └── pom.xml
├── study-ai-app-compares-frontend/    # 前端应用
│   ├── src/
│   └── package.json
└── README.md
```

## 快速开始

### 1. 启动后端服务

```bash
cd study-ai-app-compares-backend
mvn spring-boot:run
```

后端服务运行在：http://localhost:8080

### 2. 启动前端服务

```bash
cd study-ai-app-compares-frontend
npm install
npm run dev
```

前端服务运行在：http://localhost:5173

### 3. 访问应用

打开浏览器访问：http://localhost:5173

## API 接口

### 价格比较

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/compare/search` | 搜索并比较价格 |
| GET | `/api/compare/search?productName=xxx` | 搜索并比较价格(GET) |

### 地址管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/addresses` | 获取地址列表 |
| GET | `/api/addresses/{id}` | 获取单个地址 |
| POST | `/api/addresses` | 创建地址 |
| POST | `/api/addresses/{id}/update` | 更新地址 |
| POST | `/api/addresses/{id}/delete` | 删除地址 |

### 健康检查

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/health` | 健康检查 |

## 使用示例

1. 在搜索框输入"生椰拿铁"
2. 选择收货地址
3. 点击搜索按钮
4. 查看三个平台的价格对比结果
5. 系统自动推荐最低价平台

## 默认数据

系统预置了两个测试地址：

1. **张三** - 北京市朝阳区望京SOHO T1 1201室（默认地址）
2. **李四** - 上海市浦东新区陆家嘴金融中心88层

## License

MIT