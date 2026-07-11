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
cd ai-pipeline-gateway
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
│       │       ├── PipelineGatewayApplication.java   # 启动类
│       │       ├── config/
│       │       │   └── WebClientConfig.java          # WebClient 配置
│       │       ├── controller/
│       │       │   └── PipelineGatewayController.java # REST API 控制器
│       │       └── service/
│       │           └── PipelineGatewayService.java   # 服务层
│       └── resources/
│           └── application.yml                       # 配置文件
└── pom.xml
```

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/pipelines` | 查询所有流水线 |
| GET | `/api/pipelines/{pipelineId}` | 查询流水线详情 |
| POST | `/api/pipelines/start` | 启动流水线 |
| GET | `/api/pipelines/{pipelineId}/stages` | 查询阶段记录 |
| GET | `/api/pipelines/{pipelineId}/files` | 查询生成文件列表 |
| GET | `/api/pipelines/{pipelineId}/files/{fileName}` | 查询文件内容 |
| POST | `/api/pipelines/{pipelineId}/cancel` | 取消流水线 |

## 架构说明

```
Vue UI (8082) 
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

app:
  api-base-url: http://localhost:8080/api/pipeline
```

## 端口说明

- Gateway 服务：8083
- API 服务：8080（后端核心）
- UI 服务：8082（前端）
