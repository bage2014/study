# Demo 项目

全栈 Demo 工程，用于 study-ai-ops-flow 研发流程演练。

## 项目简介

图书 / 留言 / 反馈管理的演示系统，前后端分离：

```
demo/
├── pom.xml                 # Maven 父 POM（聚合 demo-backend）
├── demo-backend/           # Spring Boot 后端
├── demo-frontend/          # Vue 3 前端
└── docs/                   # 项目文档（spec / changelog / report / requirement）
```

## 技术栈

| 端 | 技术 |
|---|---|
| 后端 | Java 21、Spring Boot 3.3.0、Spring Data JPA、Validation、H2（内存库）、Lombok、Maven |
| 前端 | Vue 3、Vite 5、Element Plus、vue-router、axios、Vitest、Playwright |

## 快速开始

```bash
# 后端（端口 8082）
cd demo-backend
mvn spring-boot:run

# 前端（dev server 5173，/api 代理到后端）
cd demo-frontend
npm install
npm run dev
```

## API 接口

| 模块 | 路径 | 说明 |
|---|---|---|
| Health | `GET /api/health` | 健康检查 |
| Book | `POST /api/books`、`GET /api/books`、`GET/PUT/DELETE /api/books/{id}` | 图书 CRUD，ISBN 唯一 |
| Message | `POST /api/messages`、`GET /api/messages`、`GET/PUT/DELETE /api/messages/{id}` | 留言 CRUD，列表分页 |
| Feedback | `POST /api/feedback`、`GET /api/feedback` | 反馈提交与列表 |

错误响应统一由 `GlobalExceptionHandler` 处理（400/404/409/500）。

## 项目结构（后端）

```
demo-backend/src/main/java/com/bage/demo/
├── controller/     # REST 控制层
├── service/        # 业务接口
│   └── impl/       # 业务实现
├── repository/     # 数据访问层（JpaRepository）
├── entity/         # JPA 实体
├── dto/            # 请求/响应 DTO
├── exception/      # 业务异常 + 全局异常处理
└── config/         # 配置类
```

## 开发指南

- 编码约定见 [AGENTS.md](AGENTS.md)
- 测试：`mvn test`（后端）、`npm run test` / `npm run test:e2e`（前端）
- 数据库：H2 内存库，控制台 `/h2-console`

## 部署说明

```bash
cd demo-backend
docker build -t demo-backend .
```

## 贡献指南

- 遵循 AGENTS.md 中的编码规则
- 提交前确保 `mvn test` 通过
- 文档变更放入 `docs/` 对应子目录
