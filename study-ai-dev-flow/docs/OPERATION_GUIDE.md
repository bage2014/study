# AI Pipeline Platform - 操作手册

## 📖 目录

1. [系统简介](#1-系统简介)
2. [启动系统](#2-启动系统)
3. [访问页面](#3-访问页面)
4. [录入需求](#4-录入需求)
5. [自动编码实现](#5-自动编码实现)
6. [自动编译构建](#6-自动编译构建)
7. [自动发布部署](#7-自动发布部署)
8. [访问验证](#8-访问验证)
9. [查看执行日志](#9-查看执行日志)
10. [常见问题](#10-常见问题)

---

## 1. 系统简介

AI Pipeline Platform 是一个 AI 自动化软件开发平台，您只需输入自然语言需求，系统将自动完成以下工作：

| 步骤 | 系统自动执行 |
|------|-------------|
| 需求分析 | 分析您的需求，提取关键信息 |
| 功能拆分 | 将需求拆分为功能点和任务 |
| 代码生成 | AI 自动编写代码 |
| 测试生成 | AI 自动生成测试用例 |
| 代码审查 | AI 审查代码质量 |
| 代码提交 | 将代码写入项目 |
| 测试执行 | 运行测试用例 |
| 项目构建 | 编译打包项目 |
| 应用部署 | 启动应用服务 |

---

## 2. 启动系统

### 方法一：一键启动（推荐）

```bash
# 进入项目目录
cd /path/to/project

# 执行启动脚本
bash start.sh
```

### 方法二：手动启动

```bash
# 1. 启动 Temporal 服务器（工作流引擎）
docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0

# 2. 构建项目
mvn clean package -DskipTests -q

# 3. 启动 AI Pipeline API
cd ai-pipeline-api
java -jar target/ai-pipeline-api-1.0.0-SNAPSHOT.jar
```

### 确认系统启动成功

等待约30秒后，执行：

```bash
curl http://localhost:8080/api/pipeline
```

如果返回 `[]`（空数组），表示系统启动成功。

---

## 3. 访问页面

系统启动后，您可以访问以下页面：

| 页面 | 地址 | 用途 |
|------|------|------|
| **Temporal 工作流监控** | http://localhost:8233 | 查看流水线执行状态和历史 |
| **H2 数据库控制台** | http://localhost:8080/h2-console | 查看流水线运行数据 |
| **AI Pipeline API** | http://localhost:8080 | API 接口地址 |
| **部署的应用** | http://localhost:8081 | AI 生成的目标应用 |

### 3.1 查看工作流监控

打开浏览器访问 http://localhost:8233：

1. 在左侧菜单点击 **Workflows**
2. 可以看到所有运行中的流水线
3. 点击流水线 ID 可以查看详细执行历史

### 3.2 查看数据库数据

打开浏览器访问 http://localhost:8080/h2-console：

- JDBC URL: `jdbc:h2:mem:pipeline_db`
- 用户名: `sa`
- 密码: （空，直接回车）

---

## 4. 录入需求

### 4.1 通过 API 录入需求

使用 curl 命令录入需求：

```bash
curl -X POST http://localhost:8080/api/pipeline/start \
  -H "Content-Type: application/json" \
  -d '{
    "requirementMd": "添加健康检查端点，返回服务状态信息",
    "projectLocalPath": "/Users/bage/bage/github/study/study-ai-dev-flow/demo-backend"
  }'
```

### 4.2 需求格式说明

`requirementMd` 字段支持自然语言描述，例如：

- ✅ "添加用户注册功能，包括用户名、邮箱、密码字段"
- ✅ "创建一个产品管理CRUD接口"
- ✅ "添加订单查询API，支持分页和状态筛选"

### 4.3 系统响应

录入需求后，系统会返回一个流水线 ID：

```json
{"pipelineId":"26c83132-7b81-404e-884b-e98b824d9843"}
```

保存这个 ID，用于后续查看状态。

---

## 5. 自动编码实现

### 5.1 系统自动执行

录入需求后，系统自动执行以下编码步骤：

1. **需求分析**：分析您的需求，识别项目类型和技术栈
2. **功能点拆分**：将需求拆分为多个功能点
3. **任务拆分**：将功能点拆分为可执行的原子任务（Controller、Service、Repository等）
4. **代码生成**：AI 根据任务描述生成代码文件
5. **测试生成**：AI 生成对应的单元测试代码
6. **代码审查**：AI 审查生成的代码质量

### 5.2 查看编码进度

通过日志查看编码进度：

```bash
# 查看应用日志
curl http://localhost:8080/api/pipeline/{pipelineId}
```

响应示例：

```json
{
    "pipelineId": "26c83132-7b81-404e-884b-e98b824d9843",
    "status": "RUNNING",
    "currentStage": "CODE_GEN",
    "createdAt": "2026-07-11T13:27:04"
}
```

### 5.3 查看生成的代码

编码完成后，代码会自动写入目标项目目录：

```bash
# 查看生成的文件列表
ls -la /Users/bage/bage/github/study/study-ai-dev-flow/demo-backend/src/main/java/com/bage/demo/controller/

# 查看生成的代码内容
cat /Users/bage/bage/github/study/study-ai-dev-flow/demo-backend/src/main/java/com/bage/demo/controller/HealthController.java
```

生成的代码示例：

```java
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

---

## 6. 自动编译构建

### 6.1 系统自动执行

编码完成后，系统自动执行：

1. **代码写入**：将生成的代码写入项目文件系统
2. **测试执行**：运行项目测试用例，验证代码正确性
3. **项目构建**：使用 Maven 编译打包项目

### 6.2 查看构建进度

通过日志查看构建状态：

```bash
curl http://localhost:8080/api/pipeline/{pipelineId}
```

当 `currentStage` 显示为 `BUILD` 时，表示正在构建。

### 6.3 确认构建成功

构建完成后，状态会更新：

```json
{
    "pipelineId": "26c83132-7b81-404e-884b-e98b824d9843",
    "status": "RUNNING",
    "currentStage": "DEPLOY"
}
```

---

## 7. 自动发布部署

### 7.1 系统自动执行

构建成功后，系统自动部署应用：

1. 查找构建好的 JAR 文件
2. 启动 Java 应用进程
3. 监听端口 8081

### 7.2 查看部署进度

通过日志查看部署状态：

```bash
curl http://localhost:8080/api/pipeline/{pipelineId}
```

当 `currentStage` 显示为 `DEPLOY` 时，表示正在部署。

---

## 8. 访问验证

### 8.1 确认流水线完成

部署完成后，流水线状态变为 `COMPLETED`：

```bash
curl http://localhost:8080/api/pipeline/{pipelineId}
```

响应示例：

```json
{
    "pipelineId": "26c83132-7b81-404e-884b-e98b824d9843",
    "status": "COMPLETED",
    "currentStage": "DEPLOY",
    "resultJson": "{\"status\":\"COMPLETED\",\"success\":true,\"message\":\"Pipeline completed. Access: http://localhost:8081\"}"
}
```

### 8.2 访问部署的应用

访问 AI 生成的应用：

```bash
curl http://localhost:8081/api/health
```

预期响应：

```json
{"status":"UP","service":"demo-backend","timestamp":"2026-07-11T13:27:37"}
```

### 8.3 查看所有流水线

查看所有历史流水线：

```bash
curl http://localhost:8080/api/pipeline
```

---

## 9. 查看执行日志

### 9.1 查看应用日志

如果应用在后台运行，可以查看日志文件：

```bash
# 查看最新的日志
tail -f /tmp/agent-output.log

# 或者查看后台任务日志
cat /var/folders/.../jobs/.../output.log
```

### 9.2 查看 Temporal 日志

通过 Temporal Web UI 查看详细的工作流执行日志：

1. 访问 http://localhost:8233
2. 点击工作流 ID
3. 查看 **History** 标签页

### 9.3 日志关键字

| 关键字 | 说明 |
|--------|------|
| `Requirement analysis completed` | 需求分析完成 |
| `Feature point split completed` | 功能点拆分完成 |
| `Code generation completed` | 代码生成完成 |
| `Test generation completed` | 测试生成完成 |
| `Code review completed` | 代码审查完成 |
| `Test execution completed` | 测试执行完成 |
| `Local build completed` | 构建完成 |
| `Local deployment completed` | 部署完成 |

---

## 10. 常见问题

### Q1: 如何确认系统是否启动成功？

```bash
curl http://localhost:8080/api/pipeline
# 返回 [] 表示成功
```

### Q2: 流水线执行需要多长时间？

通常需要 1-3 分钟，取决于：
- 需求复杂度
- 项目大小
- 测试执行时间

### Q3: 如何查看某个流水线的详细信息？

```bash
curl http://localhost:8080/api/pipeline/{pipelineId}
```

### Q4: 如何取消一个正在运行的流水线？

```bash
curl -X POST http://localhost:8080/api/pipeline/{pipelineId}/cancel
```

### Q5: 如何按状态筛选流水线？

```bash
# 查看已完成的流水线
curl http://localhost:8080/api/pipeline/status/COMPLETED

# 查看运行中的流水线
curl http://localhost:8080/api/pipeline/status/RUNNING

# 查看失败的流水线
curl http://localhost:8080/api/pipeline/status/FAILED
```

### Q6: 端口被占用怎么办？

```bash
# 释放端口 8080
lsof -ti:8080 | xargs kill -9

# 释放端口 8081
lsof -ti:8081 | xargs kill -9
```

### Q7: Temporal 服务器连接失败？

```bash
# 检查容器是否运行
docker ps | grep temporal

# 重新启动
docker restart temporal
```

### Q8: 部署失败怎么办？

检查目标项目是否可执行：

```bash
cd /path/to/demo-backend
java -jar target/*.jar --server.port=8081
```

---

## 📋 完整操作流程总结

```
1. 启动系统
   → docker run -d --name temporal ...
   → mvn clean package -DskipTests
   → java -jar ai-pipeline-api/target/ai-pipeline-api-1.0.0-SNAPSHOT.jar

2. 录入需求
   → curl -X POST http://localhost:8080/api/pipeline/start -d '{"requirementMd": "..."}'

3. 等待执行（1-3分钟）
   → 系统自动完成：需求分析 → 功能拆分 → 代码生成 → 测试生成 → 代码审查 → 测试执行 → 构建 → 部署

4. 查看状态
   → curl http://localhost:8080/api/pipeline/{pipelineId}

5. 验证结果
   → curl http://localhost:8081/api/health
   → 预期响应: {"status":"UP","service":"demo-backend","timestamp":"..."}
```

---

**文档版本**: v1.0.0  
**生成日期**: 2026-07-11
