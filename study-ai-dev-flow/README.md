# Study AI Dev Flow

研发流程标准项目，包含前后端完整项目骨架，作为团队开发规范和流程的参考基线。

## 技术栈

| 模块 | 技术 | 版本 |
|------|------|------|
| 后端 | Spring Boot | 3.4 |
| 后端 | Java | 21 |
| 后端 | Spring Data JPA | - |
| 后端 | H2 (开发) | - |
| 前端 | Vue | 3.5 |
| 前端 | TypeScript | 5.7 |
| 前端 | Vite | 6.x |
| 前端 | TailwindCSS | 3.4 |
| 前端 | Pinia | 2.x |
| 前端 | Vue Router | 4.x |

## 项目结构

```
study-ai-dev-flow/
├── study-ai-dev-flow-backend/     # 后端 Spring Boot 项目
│   └── src/main/java/com/bage/study/ai/dev/flow/
│       ├── controller/            # REST API 控制层
│       ├── service/               # 业务接口层
│       │   └── impl/             # 业务实现层
│       ├── repository/            # 数据访问层
│       ├── entity/                # 数据库实体
│       ├── dto/                   # 数据传输对象
│       │   ├── request/          # 请求 DTO
│       │   └── response/         # 响应 DTO
│       ├── config/                # 配置类
│       ├── exception/             # 异常处理
│       └── util/                  # 工具类
├── study-ai-dev-flow-frontend/    # 前端 Vue 项目
│   └── src/
│       ├── components/
│       │   ├── common/           # 公共基础组件
│       │   ├── layout/           # 布局组件
│       │   └── pages/            # 页面组件
│       ├── services/             # API 服务
│       ├── stores/               # Pinia 状态管理
│       ├── types/                # TypeScript 类型定义
│       ├── hooks/                # 组合式函数
│       ├── router/               # 路由配置
│       ├── assets/               # 静态资源
│       └── utils/                # 工具函数
└── docs/                         # 项目文档
```

## 快速开始

### 后端启动

```bash
cd study-ai-dev-flow-backend
mvn spring-boot:run
# 端口: 8080
# H2 控制台: http://localhost:8080/h2-console
```

### 前端启动

```bash
cd study-ai-dev-flow-frontend
npm install
npm run dev
# 端口: 5173
# API 代理: /api → http://localhost:8080
```

## 编码规范

- 后端: `@Autowired` 属性注入、Service 接口+实现、仅 GET/POST、`@Slf4j` 日志
- 前端: TypeScript 严格模式、Composition API、组件按职责分层
- 包名: 项目名按连字符拆分为多级包名（`study-ai-dev-flow` → `com.bage.study.ai.dev.flow`）

## API 示例

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/samples | 查询所有示例 |
| GET | /api/samples/{id} | 根据 ID 查询 |
| POST | /api/samples | 创建示例 |
| POST | /api/samples/{id}/update | 更新示例 |
| POST | /api/samples/{id}/delete | 删除示例 |
