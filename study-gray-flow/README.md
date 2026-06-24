# Gray Flow 模块

## 一、项目介绍

`study-gray-flow` 是一个基于 Spring Boot 的灰度流量管理后端服务，提供以下核心功能：

- **日志记录**：记录请求链路日志，支持按 TraceId 查询完整调用链
- **请求回放**：支持将历史请求重新发送到目标服务进行灰度验证
- **结果比对**：对比原始请求和回放请求的响应差异

## 二、技术栈

| 分类 | 技术 | 版本 |
|-----|------|-----|
| 语言 | Java | 21 |
| 框架 | Spring Boot | 3.2.x |
| 数据库 | H2 | 2.x |
| ORM | Spring Data JPA | 3.2.x |
| AOP | Spring AOP | - |

## 三、项目结构

```
study-gray-flow/
├── src/main/java/com/bage/study/grayflow/
│   ├── GrayFlowApplication.java      # 启动类
│   ├── controller/                   # REST API 控制层
│   ├── service/                      # 业务逻辑层
│   ├── repository/                   # 数据访问层
│   ├── entity/                       # 数据库实体
│   ├── dto/                          # 数据传输对象
│   ├── aop/                          # AOP 切面
│   └── config/                       # 配置类
├── src/main/resources/
│   └── application.yml               # 应用配置
└── pom.xml                           # Maven 配置
```

## 四、功能说明

### 4.1 日志记录功能

- 自动记录带有 `@GrayLog` 注解的方法调用
- 记录请求参数、返回结果、执行时间
- 支持通过 TraceId 串联完整调用链

### 4.2 请求回放功能

- 支持选择最近 N 条请求进行回放
- 回放请求发送到灰度目标服务
- 记录回放结果和差异对比

### 4.3 结果比对功能

- 对比原始响应和灰度响应的差异
- 支持 JSON 结构的深度对比
- 生成结构化的差异报告

## 五、快速开始

### 5.1 环境要求

- JDK 21+
- Maven 3.8+

### 5.2 启动方式

```bash
cd study-gray-flow
mvn spring-boot:run
```

### 5.3 访问地址

- 应用服务：http://localhost:8083
- H2 控制台：http://localhost:8083/h2-console

## 六、API 接口

| 接口 | 方法 | 说明 |
|-----|------|------|
| `/api/logs` | GET | 查询日志列表 |
| `/api/logs/trace/{traceId}` | GET | 查询调用链 |
| `/api/replay/sessions` | GET | 查询回放会话列表 |
| `/api/replay/sessions` | POST | 创建回放会话 |
| `/api/replay/sessions/{sessionId}` | GET | 查询回放详情 |

详细 API 文档：[docs/api/API.md](docs/api/API.md)

## 七、设计文档

详细设计说明：[docs/design/DESIGN.md](docs/design/DESIGN.md)

## 八、测试文档

测试用例说明：[docs/test/TEST.md](docs/test/TEST.md)