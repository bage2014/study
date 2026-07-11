# AI Pipeline Platform - 用户使用指南

## ⚠️ 重要提示

当前版本使用 **Mock AI 模式**，AI 响应为预设的模拟数据，用于验证流水线架构和工作流编排。如需使用真实 AI 模型，请参考 [附录：配置说明](#附录配置说明) 配置 OpenAI/Anthropic/Ollama API Key。

## 🚀 快速开始

```bash
# 1. 启动 Temporal 服务器
docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0

# 2. 构建项目
cd /path/to/project
mvn clean package -DskipTests -q

# 3. 启动 AI Pipeline API
cd ai-pipeline-api
java -jar target/ai-pipeline-api-1.0.0-SNAPSHOT.jar

# 4. 启动流水线（添加健康检查端点）
curl -X POST http://localhost:8080/api/pipeline/start \
  -H "Content-Type: application/json" \
  -d '{"requirementMd": "添加健康检查端点", "projectLocalPath": "/path/to/project/demo-backend"}'

# 5. 等待1-2分钟后验证结果
curl http://localhost:8081/api/health
```

**预期响应**:
```json
{"status":"UP","service":"demo-backend","timestamp":"2026-07-11T12:37:22.493432"}
```

## 目录

- [重要提示](#⚠️-重要提示)
- [快速开始](#🚀-快速开始)
- [1. 系统架构概览](#1-系统架构概览)
- [2. 环境准备与启动](#2-环境准备与启动)
- [3. 访问端点一览](#3-访问端点一览)
- [4. 完整操作流程](#4-完整操作流程)
- [5. REST API 接口文档](#5-rest-api-接口文档)
- [6. 流水线阶段说明](#6-流水线阶段说明)
- [7. 审批流程操作](#7-审批流程操作)
- [8. 常见问题与排查](#8-常见问题与排查)
- [9. 示例：添加健康检查端点](#9-示例添加健康检查端点)
- [附录：配置说明](#附录配置说明)

---

## 1. 系统架构概览

AI Pipeline Platform 是一个基于 Temporal 的 AI 自动化软件开发流水线系统，实现从自然语言需求到自动部署的全流程自动化。

### 模块架构

| 模块 | 职责 | 端口 |
|------|------|------|
| `ai-pipeline-core` | 核心工作流定义、DTO、枚举 | - |
| `ai-pipeline-agent` | AI Agent 服务、工具集成 | - |
| `ai-pipeline-api` | REST API 接口、Activity 实现 | 8080 |
| `demo-backend` | 目标演示项目（被AI修改的项目） | 8081 |

### 技术栈

- **工作流引擎**: Temporal 1.24.1
- **AI框架**: LangChain4j 0.35.0
- **应用框架**: Spring Boot 3.3.4
- **数据库**: H2 (运行时内存数据库)
- **Java版本**: 21

### 流水线流程

```
需求输入 → 需求分析 → 功能点拆分 → 任务拆分 → 代码生成 → 测试生成 
→ 代码审查 → 代码写入 → 测试执行 → 构建 → 部署
```

---

## 2. 环境准备与启动

### 2.1 前置条件

- JDK 21+
- Maven 3.8+
- Docker (用于启动 Temporal 服务器)

### 2.2 启动步骤

#### 步骤一：启动 Temporal 服务器

```bash
# 使用 Docker 启动 Temporal CLI 开发服务器
docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0

# 验证启动成功
docker ps | grep temporal
```

#### 步骤二：构建项目

```bash
cd /path/to/project
mvn clean package -DskipTests
```

#### 步骤三：启动 AI Pipeline API

```bash
cd ai-pipeline-api
java -jar target/ai-pipeline-api-1.0.0-SNAPSHOT.jar
```

#### 步骤四：验证服务启动

```bash
# 检查 API 是否启动
curl http://localhost:8080/api/pipeline

# 检查 Temporal UI 是否可访问
# 浏览器访问: http://localhost:8233
```

### 2.3 一键启动脚本

创建 `start.sh` 文件：

```bash
#!/bin/bash

# 停止旧服务
lsof -ti:8080 | xargs kill -9 2>/dev/null
lsof -ti:8081 | xargs kill -9 2>/dev/null

# 启动 Temporal
docker rm -f temporal 2>/dev/null
docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0

# 等待 Temporal 启动
sleep 10

# 构建并启动应用
mvn clean package -DskipTests -q
cd ai-pipeline-api
java -jar target/ai-pipeline-api-1.0.0-SNAPSHOT.jar
```

---

## 3. 访问端点一览

### 3.1 Web UI 界面

| 端点 | 说明 | 地址 |
|------|------|------|
| Temporal Web UI | 工作流监控界面 | http://localhost:8233 |
| H2 数据库控制台 | 查看运行时数据 | http://localhost:8080/h2-console |

### 3.2 API 接口

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/pipeline` | GET | 获取所有流水线列表 |
| `/api/pipeline/{pipelineId}` | GET | 获取指定流水线详情 |
| `/api/pipeline/start` | POST | 启动新流水线 |
| `/api/pipeline/{pipelineId}/approval/{stage}` | POST | 审批/拒绝指定阶段 |
| `/api/pipeline/{pipelineId}/approval/{stage}` | GET | 获取审批状态 |
| `/api/pipeline/{pipelineId}/cancel` | POST | 取消流水线 |
| `/api/pipeline/status/{status}` | GET | 按状态筛选流水线 |

### 3.3 部署的应用

| 端点 | 说明 | 地址 |
|------|------|------|
| demo-backend | AI 生成的目标应用 | http://localhost:8081 |

---

## 4. 完整操作流程

### 4.1 启动流水线

```bash
curl -X POST http://localhost:8080/api/pipeline/start \
  -H "Content-Type: application/json" \
  -d '{
    "requirementMd": "添加健康检查端点",
    "projectLocalPath": "/path/to/demo-backend"
  }'
```

**响应示例**:
```json
{"pipelineId":"43b61daf-361c-4b45-9f33-a0aba0f958eb"}
```

### 4.2 查看流水线状态

```bash
curl http://localhost:8080/api/pipeline/43b61daf-361c-4b45-9f33-a0aba0f958eb
```

### 4.3 查看所有流水线

```bash
curl http://localhost:8080/api/pipeline
```

### 4.4 审批阶段（当需要人工审批时）

```bash
# 审批通过
curl -X POST http://localhost:8080/api/pipeline/{pipelineId}/approval/{stage} \
  -H "Content-Type: application/json" \
  -d '{
    "approved": true,
    "reviewer": "张三",
    "comment": "代码符合规范，通过审批"
  }'

# 拒绝审批
curl -X POST http://localhost:8080/api/pipeline/{pipelineId}/approval/{stage} \
  -H "Content-Type: application/json" \
  -d '{
    "approved": false,
    "reviewer": "张三",
    "comment": "需要修改错误处理逻辑"
  }'
```

### 4.5 取消流水线

```bash
curl -X POST http://localhost:8080/api/pipeline/{pipelineId}/cancel
```

---

## 5. REST API 接口文档

### 5.1 POST /api/pipeline/start

**功能**: 启动新的 AI 自动化流水线

**请求体**:

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| requirementMd | String | ✅ | 自然语言需求描述 |
| projectLocalPath | String | ✅ | 目标项目本地路径 |
| projectType | String | ❌ | 项目类型（JAVA_SPRING/REACT等） |
| buildTool | String | ❌ | 构建工具（MAVEN/GRADLE/NPM） |
| frontendLocalPath | String | ❌ | 前端项目路径（全栈项目） |
| sandboxEnabled | boolean | ❌ | 是否启用沙箱模式（默认false） |
| autoFixOnTestFail | boolean | ❌ | 测试失败是否自动修复（默认false） |
| maxTestFixRetries | int | ❌ | 最大修复重试次数（默认3） |

**请求示例**:
```json
{
  "requirementMd": "添加用户注册功能，包括用户名、邮箱、密码字段，密码需要加密存储",
  "projectLocalPath": "/Users/user/projects/demo-backend",
  "projectType": "JAVA_SPRING",
  "buildTool": "MAVEN",
  "autoFixOnTestFail": true,
  "maxTestFixRetries": 3
}
```

**响应示例**:
```json
{"pipelineId":"43b61daf-361c-4b45-9f33-a0aba0f958eb"}
```

### 5.2 GET /api/pipeline

**功能**: 获取所有流水线列表

**响应示例**:
```json
[
  {
    "pipelineId": "43b61daf-361c-4b45-9f33-a0aba0f958eb",
    "status": "COMPLETED",
    "currentStage": "DEPLOY",
    "createdAt": "2026-07-11T12:34:38",
    "updatedAt": "2026-07-11T12:34:59"
  }
]
```

### 5.3 GET /api/pipeline/{pipelineId}

**功能**: 获取指定流水线详情

**响应示例**:
```json
{
  "pipelineId": "43b61daf-361c-4b45-9f33-a0aba0f958eb",
  "status": "COMPLETED",
  "currentStage": "DEPLOY",
  "inputJson": "{...}",
  "resultJson": "{...}",
  "createdAt": "2026-07-11T12:34:38",
  "updatedAt": "2026-07-11T12:34:59"
}
```

### 5.4 GET /api/pipeline/status/{status}

**功能**: 按状态筛选流水线

**支持的状态**:
- `PENDING` - 待启动
- `RUNNING` - 运行中
- `WAITING_APPROVAL` - 等待审批
- `COMPLETED` - 已完成
- `FAILED` - 失败
- `CANCELLED` - 已取消

**请求示例**:
```bash
curl http://localhost:8080/api/pipeline/status/COMPLETED
```

### 5.5 POST /api/pipeline/{pipelineId}/approval/{stage}

**功能**: 审批或拒绝指定阶段

**请求体**:

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| approved | boolean | ✅ | 是否批准 |
| reviewer | String | ✅ | 审批人 |
| comment | String | ❌ | 审批意见 |

**支持的阶段**:
- `REQUIREMENT_ANALYSIS` - 需求分析
- `FEATURE_POINT_SPLIT` - 功能点拆分
- `TASK_SPLIT` - 任务拆分
- `CODE_GEN` - 代码生成
- `TEST_GEN` - 测试生成
- `CODE_REVIEW` - 代码审查
- `PR_CREATION` - PR创建
- `TEST_EXEC` - 测试执行
- `BUILD` - 构建
- `DEPLOY` - 部署

**请求示例**:
```json
{
  "approved": true,
  "reviewer": "开发经理",
  "comment": "需求分析合理，继续执行"
}
```

### 5.6 GET /api/pipeline/{pipelineId}/approval/{stage}

**功能**: 获取指定阶段的审批状态

**响应示例**:
```json
{
  "pipelineId": "43b61daf-361c-4b45-9f33-a0aba0f958eb",
  "stage": "CODE_REVIEW",
  "approved": true,
  "reviewer": "开发经理",
  "comment": "代码审查通过",
  "approvedAt": "2026-07-11T12:34:38"
}
```

### 5.7 POST /api/pipeline/{pipelineId}/cancel

**功能**: 取消运行中的流水线

**响应**: 200 OK (无内容)

---

## 6. 流水线阶段说明

### 6.1 阶段流程图

```
┌─────────────────────────────────────────────────────────────────┐
│                     AI Pipeline 流水线                          │
├─────────────────────────────────────────────────────────────────┤
│  1. 需求分析      →  分析自然语言需求，提取结构化信息            │
│        ↓                                                       │
│  2. 功能点拆分    →  将需求拆分为多个功能点                      │
│        ↓                                                       │
│  3. 任务拆分      →  将功能点拆分为原子任务                      │
│        ↓                                                       │
│  4. 代码生成      →  AI生成代码文件                             │
│        ↓                                                       │
│  5. 测试生成      →  AI生成测试用例                             │
│        ↓                                                       │
│  6. 代码审查      →  AI审查代码质量                             │
│        ↓                                                       │
│  7. 代码写入      →  将代码写入项目并提交                        │
│        ↓                                                       │
│  8. 测试执行      →  运行测试用例                                │
│        ↓                                                       │
│  9. 构建          →  构建项目                                   │
│        ↓                                                       │
│ 10. 部署          →  部署应用                                   │
└─────────────────────────────────────────────────────────────────┘
```

### 6.2 各阶段详细说明

| 阶段 | 说明 | 耗时 | 自动/人工 |
|------|------|------|-----------|
| **需求分析** | AI分析用户输入的自然语言需求，提取项目类型、技术栈、验收标准等 | 1-5秒 | 自动 |
| **功能点拆分** | 将需求拆分为多个独立的功能点 | 1-5秒 | 自动 |
| **任务拆分** | 将每个功能点拆分为可执行的原子任务（如Controller、Service、Repository） | 1-5秒 | 自动 |
| **代码生成** | AI根据任务描述生成代码文件 | 5-30秒 | 自动 |
| **测试生成** | AI生成对应的单元测试代码 | 5-15秒 | 自动 |
| **代码审查** | AI审查生成的代码，检查代码质量和潜在问题 | 1-5秒 | 自动 |
| **代码写入** | 将生成的代码写入项目文件系统 | 1-5秒 | 自动 |
| **测试执行** | 运行项目测试用例，验证代码正确性 | 30-120秒 | 自动 |
| **构建** | 使用Maven/Gradle/NPM构建项目 | 30-120秒 | 自动 |
| **部署** | 将应用部署到运行环境 | 5-30秒 | 自动 |

---

## 7. 审批流程操作

### 7.1 审批机制

系统支持在每个阶段设置人工审批环节。当某个阶段需要审批时：

1. 流水线自动暂停在该阶段
2. 状态变为 `WAITING_APPROVAL`
3. 用户可以查询审批状态并做出决策
4. 审批通过后，流水线继续执行下一阶段
5. 审批拒绝后，流水线终止

### 7.2 配置自动审批

默认情况下，所有阶段都是自动审批的。可以通过请求参数控制：

```json
{
  "requirementMd": "添加健康检查端点",
  "projectLocalPath": "/path/to/demo-backend",
  "autoApproveMap": {
    "REQUIREMENT_ANALYSIS": true,
    "CODE_REVIEW": false,
    "DEPLOY": false
  }
}
```

### 7.3 审批流程示例

```bash
# 启动流水线（代码审查阶段需要人工审批）
curl -X POST http://localhost:8080/api/pipeline/start \
  -H "Content-Type: application/json" \
  -d '{
    "requirementMd": "添加用户管理功能",
    "projectLocalPath": "/path/to/demo-backend",
    "autoApproveMap": {
      "REQUIREMENT_ANALYSIS": true,
      "FEATURE_POINT_SPLIT": true,
      "TASK_SPLIT": true,
      "CODE_GEN": true,
      "TEST_GEN": true,
      "CODE_REVIEW": false,
      "PR_CREATION": true,
      "TEST_EXEC": true,
      "BUILD": true,
      "DEPLOY": true
    }
  }'

# 查询流水线状态（应该显示WAITING_APPROVAL）
curl http://localhost:8080/api/pipeline/{pipelineId}

# 查询审批状态
curl http://localhost:8080/api/pipeline/{pipelineId}/approval/CODE_REVIEW

# 审批通过
curl -X POST http://localhost:8080/api/pipeline/{pipelineId}/approval/CODE_REVIEW \
  -H "Content-Type: application/json" \
  -d '{
    "approved": true,
    "reviewer": "技术负责人",
    "comment": "代码质量良好，同意继续"
  }'
```

---

## 8. 常见问题与排查

### 8.1 启动问题

**Q: Temporal 服务器连接失败**
```
Caused by: java.net.ConnectException: Connection refused
```
**A**: 确保 Temporal 容器已启动：
```bash
docker ps | grep temporal
# 如果未启动，执行：
docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0
```

**Q: 端口 8080 已被占用**
```
Port 8080 was already in use
```
**A**: 释放端口：
```bash
lsof -ti:8080 | xargs kill -9
```

**Q: 端口 8081 已被占用**
```
Port 8081 was already in use
```
**A**: 释放端口：
```bash
lsof -ti:8081 | xargs kill -9
```

### 8.2 流水线执行问题

**Q: 流水线在部署阶段失败**
```
Direct deployment failed: Process exited immediately
```
**A**: 检查目标项目是否可执行：
```bash
cd /path/to/demo-backend
java -jar target/*.jar --server.port=8081
```
确保 pom.xml 中配置了 spring-boot-maven-plugin 的 repackage 目标。

**Q: 测试执行失败**
```
Local test execution completed, success: false
```
**A**: 检查测试日志，可能是代码编译错误或测试用例问题。

**Q: Docker 构建失败**
```
ERROR: failed to solve: eclipse-temurin:17-jre-alpine: no match for platform
```
**A**: 这是 Docker 镜像平台兼容性问题，系统会自动切换到 Java 直接部署模式。

### 8.3 数据查看

**Q: 如何查看数据库中的流水线数据**
**A**: 访问 H2 控制台：
- 地址: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:pipeline_db
- 用户名: sa
- 密码: (空)

### 8.4 工作流监控

**Q: 如何监控工作流执行**
**A**: 访问 Temporal Web UI：
- 地址: http://localhost:8233
- 在 Workflows 页面查看运行中的工作流
- 点击工作流 ID 查看详细执行历史

---

## 9. 示例：添加健康检查端点

### 9.1 完整操作步骤

#### 步骤一：启动服务

```bash
# 启动 Temporal
docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0

# 启动 AI Pipeline API
cd /path/to/project
mvn clean package -DskipTests -q
cd ai-pipeline-api
java -jar target/ai-pipeline-api-1.0.0-SNAPSHOT.jar
```

#### 步骤二：启动流水线

```bash
curl -X POST http://localhost:8080/api/pipeline/start \
  -H "Content-Type: application/json" \
  -d '{
    "requirementMd": "添加健康检查端点，返回服务状态信息",
    "projectLocalPath": "/path/to/project/demo-backend"
  }'
```

**响应**:
```json
{"pipelineId":"43b61daf-361c-4b45-9f33-a0aba0f958eb"}
```

#### 步骤三：等待执行完成

流水线将自动执行所有阶段，大约需要 1-2 分钟。可以通过日志查看进度：

```bash
# 查看应用日志（后台运行的日志文件）
cat /tmp/agent-output.log | tail -50
```

#### 步骤四：验证部署结果

```bash
# 访问健康检查端点
curl http://localhost:8081/api/health
```

**预期响应**:
```json
{"status":"UP","service":"demo-backend","timestamp":"2026-07-11T12:37:22.493432"}
```

### 9.2 查看生成的文件

生成的文件位于 `demo-backend` 项目中：

```bash
# 查看生成的 Controller
cat demo-backend/src/main/java/com/bage/demo/controller/HealthController.java
```

**生成的代码**:
```java
package com.bage.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().toString(),
            "service", "demo-backend"
        ));
    }
}
```

### 9.3 监控工作流

通过 Temporal Web UI 查看工作流执行详情：

1. 访问 http://localhost:8233
2. 在 Workflows 列表中找到对应的工作流
3. 点击查看执行历史和详细信息

---

## 附录：配置说明

### AI 模型配置

在 `ai-pipeline-api/src/main/resources/application.yml` 中配置 AI 模型：

```yaml
ai:
  requirement-model-provider: mock  # mock/openai/anthropic/ollama
  codegen-model-provider: mock       # mock/openai/anthropic/ollama
```

**支持的模型提供者**:
- `mock` - 模拟 AI 响应（无需 API Key）
- `openai` - OpenAI GPT 模型
- `anthropic` - Anthropic Claude 模型
- `ollama` - 本地 Ollama 模型

### 启用真实 AI

如需使用真实 AI 模型，需要配置 API Key：

```yaml
ai:
  requirement-model-provider: openai
  codegen-model-provider: openai
  openai:
    api-key: your-api-key
    model-name: gpt-4o
```

---

## 版本信息

- **AI Pipeline Platform**: v1.0.0-SNAPSHOT
- **Temporal SDK**: 1.24.1
- **Spring Boot**: 3.3.4
- **LangChain4j**: 0.35.0

---

*文档生成日期: 2026-07-11*
