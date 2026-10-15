# AI Pipeline Gateway

基于 Spring Boot 3.3 的 API 网关服务，提供统一的前端访问入口和 CORS 支持。

## 技术栈

- Spring Boot 3.3+
- Spring WebFlux
- WebClient

## 环境要求

- JDK 21+
- Maven 3.9+

## 快速开始

### 构建项目

```bash
cd study-ai-dev-flow/pipeline/ai-pipeline-gateway
mvn clean package -DskipTests
```

### 启动服务

```bash
java -jar target/ai-pipeline-gateway-1.0.0-SNAPSHOT.jar
```

服务启动后监听端口 8083。

## 项目结构

```
ai-pipeline-gateway/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/bage/ai/pipeline/gateway/
│       │       ├── PipelineGatewayApplication.java       # 启动类
│       │       ├── config/
│       │       │   └── WebClientConfig.java              # WebClient 配置
│       │       ├── controller/
│       │       │   ├── PipelineGatewayController.java    # 流水线 API
│       │       │   ├── ProjectGatewayController.java     # 项目 API
│       │       │   └── RequirementGatewayController.java # 需求 API
│       │       └── service/
│       │           └── PipelineGatewayService.java       # 服务层（请求转发）
│       └── resources/
│           └── application.yml                           # 配置文件
└── pom.xml
```

## API 接口

与 Core API（8080）路径一致，仅端口不同。

| 分组 | 路径前缀 | 说明 |
|------|---------|------|
| 流水线 | `/api/pipeline` | 启动 / 查询 / 取消 / 审批 / 阶段记录 / 生成文件 |
| 项目 | `/api/project` | CRUD / 统计 / 项目应用 start-stop-restart |
| 需求 | `/api/requirement` | CRUD / 按项目或流水线查询 |

流水线主要接口：

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/pipeline/start` | 启动流水线 |
| GET | `/api/pipeline` | 查询所有流水线 |
| GET | `/api/pipeline/{pipelineId}` | 查询流水线详情 |
| GET | `/api/pipeline/{pipelineId}/stages` | 查询阶段记录 |
| GET | `/api/pipeline/{pipelineId}/files` | 查询生成文件列表 |
| GET | `/api/pipeline/{pipelineId}/files/{fileName}` | 查询文件内容 |
| POST | `/api/pipeline/{pipelineId}/cancel` | 取消流水线 |
| POST | `/api/pipeline/{pipelineId}/approval/{stage}` | 阶段人工审批 |

## 架构说明

```
Vue UI (8081) 
    ↓ /api/* 代理
Gateway (8083)  ← 当前模块
    ↓ WebClient 调用
API (8080) 
```

### 职责

1. **统一入口**：为前端提供单一 API 入口
2. **CORS 处理**：解决跨域问题
3. **请求转发**：将请求转发到后端 API
4. **协议转换**：支持 REST 和 SSE 等协议

## 配置说明

```yaml
server:
  port: 8083

pipeline:
  api-url: http://localhost:8080/api/pipeline
```

## 端口说明

- Gateway 服务：8083
- API 服务：8080（后端核心）
- UI 服务：8081（前端）
