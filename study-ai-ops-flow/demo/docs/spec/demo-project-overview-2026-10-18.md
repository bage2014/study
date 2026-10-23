# Demo 项目说明

> 全栈 Demo 工程，用于 study-ai-ops-flow 研发流程演练。

## 1. 项目结构

```
demo/
├── pom.xml                 # 父 POM（继承 com.bage.ai:ai-ops，聚合 demo-backend）
├── demo-backend/           # Spring Boot 后端
└── demo-frontend/          # Vue 3 前端
```

## 2. 技术栈

| 端 | 技术 |
|---|---|
| 后端 | Java 21、Spring Boot 3.3.0、Spring Data JPA、Validation、H2（内存库）、Lombok、Maven |
| 前端 | Vue 3、Vite 5、Element Plus、vue-router、axios、Vitest、Playwright |

## 3. 后端 demo-backend

### 3.1 分层架构

Controller → Service → Repository → Entity，遵循 [SKILL.md](file:///Users/bage/bage/github/study/study-ai-ops-flow/demo/demo-backend/SKILL.md) 中的约定（`@RestController`、`ResponseEntity`、Lombok、JpaRepository 等）。

### 3.2 API 一览

| 模块 | 路径 | 说明 |
|---|---|---|
| Book | `POST /api/books`、`GET /api/books` | 图书创建与列表，ISBN 唯一 |
| Message | `POST/GET/PUT/DELETE /api/messages` | 留言 CRUD，列表分页（默认 size=20，按 createdAt 排序） |
| Feedback | `POST /api/feedback`、`GET /api/feedback` | 反馈提交与列表 |
| Health | `GET /api/health` | 健康检查 |

### 3.3 数据模型

- **Book**：id、title、author、isbn（唯一）、price
- **Message**：id、content、sender、receiver、read、createdAt、updatedAt（`@PrePersist/@PreUpdate` 自动填充）
- **Feedback**：id、name、email、content
- 迁移脚本：`src/main/resources/db/migration/V1__*.sql`（products / users / messages / http_access_logs）

### 3.4 配置

- 端口：`8082`（[application.yml](file:///Users/bage/bage/github/study/study-ai-ops-flow/demo/demo-backend/src/main/resources/application.yml)）
- H2 Console 开启；`ddl-auto: update`；SQL 日志格式化输出
- 全局异常处理：`GlobalExceptionHandler`

### 3.5 运行与测试

```bash
cd demo-backend
mvn spring-boot:run        # 启动，端口 8082
mvn test                   # 单测 + 集成测试
docker build -t demo-backend .   # Dockerfile（容器内暴露 8081）
```

## 4. 前端 demo-frontend

- 页面：商品列表（[ProductList.vue](file:///Users/bage/bage/github/study/study-ai-ops-flow/demo/demo-frontend/src/views/ProductList.vue)），支持新增/编辑/删除弹窗
- 路由：`/` → ProductList
- API 封装：[api/index.js](file:///Users/bage/bage/github/study/study-ai-ops-flow/demo/demo-frontend/src/api/index.js)，baseURL `/api`，调用 `/products`
- 代理：dev server `5173`，`/api` → `http://localhost:8081`（[vite.config.js](file:///Users/bage/bage/github/study/study-ai-ops-flow/demo/demo-frontend/vite.config.js)）

```bash
cd demo-frontend
npm run dev         # 开发
npm run test        # Vitest 单测
npm run test:e2e    # Playwright e2e
```

## 5. 已知不一致点（待收敛）

1. **端口不一致**：后端配置 `8082`，但 Dockerfile 与前端代理均指向 `8081`
2. **接口缺失**：前端调用 `/api/products`，后端无对应 ProductController（仅有建表 SQL 与后端内嵌 React 试验页 `src/main/frontend`）
3. **文件错位**：`.github/workflows/ci.yml` 内容为 HealthController Java 代码，非真实 CI 配置
4. **双构建文件**：`pom.xml` 与 `build.gradle` 并存，需统一
