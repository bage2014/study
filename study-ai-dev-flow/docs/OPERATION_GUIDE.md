# AI Pipeline Platform - 操作手册

## 📖 目录

1. [系统简介](#1-系统简介)
2. [启动系统](#2-启动系统)
3. [访问页面](#3-访问页面)
4. [选择项目与录入需求](#4-选择项目与录入需求)
5. [查看项目详情](#5-查看项目详情)
6. [查看流水线详情](#6-查看流水线详情)
7. [自动编码实现](#7-自动编码实现)
8. [自动编译构建](#8-自动编译构建)
9. [自动发布部署](#9-自动发布部署)
10. [访问验证](#10-访问验证)
11. [查看执行日志](#11-查看执行日志)
12. [常见问题](#12-常见问题)

---

## 1. 系统简介

AI Pipeline Platform 是一个 AI 自动化软件开发平台，采用**项目中心化**工作流，您只需选择项目并输入自然语言需求，AI 将自动分析项目代码结构并完成从方案设计到代码实现的全过程。

### 核心工作流

```
选择项目 → AI 自动分析代码 → 输入需求 → 一键启动 → 自动完成全流程
```

### 自动化流程

| 步骤 | 系统自动执行 |
|------|-------------|
| 代码分析 | AI 自动分析项目代码结构，识别技术栈和数据库配置 |
| 需求分析 | 基于代码分析结果，分析您的需求，提取关键信息 |
| 方案设计 | AI 生成技术方案、架构设计和 API 设计 |
| 功能拆分 | 将需求拆分为功能点和原子任务 |
| 代码生成 | AI 自动编写代码（三层分解聚焦生成）|
| 测试生成 | AI 自动生成测试用例 |
| 代码审查 | AI 审查代码质量，检测 CRITICAL 问题 |
| 代码提交 | 将代码写入项目并 Git 提交 |
| 测试执行 | 运行测试用例，失败自动修复 |
| 项目构建 | 编译打包项目 |
| 应用部署 | 启动应用服务（Docker 容器化）|

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

## 4. 选择项目与录入需求

### 4.1 通过 UI 录入需求

1. **访问首页**：打开 http://localhost:8082/

2. **选择项目**：从项目列表中点击选择目标项目（如 demo-backend）
   - 选择后项目卡片会高亮显示
   - AI 会自动分析项目代码结构，识别技术栈和数据库配置

3. **输入需求**：在文本框中输入自然语言需求描述
   ```
   添加用户注册功能，支持手机号验证和密码加密
   ```

4. **提交启动**：点击"提交需求，启动AI流水线"按钮
   - 按钮在未选择项目或未输入需求时禁用
   - 提交后跳转至流水线详情页面

### 4.2 通过 API 录入需求

使用 curl 命令录入需求：

```bash
curl -X POST http://localhost:8083/api/pipelines/start \
  -H "Content-Type: application/json" \
  -d '{
    "requirementMd": "添加健康检查端点，返回服务状态信息",
    "projectId": 1,
    "projectLocalPath": "/Users/bage/bage/github/study/study-ai-dev-flow/demo-backend"
  }'
```

### 4.3 需求格式说明

`requirementMd` 字段支持自然语言描述，例如：

- ✅ "添加用户注册功能，包括用户名、邮箱、密码字段"
- ✅ "创建一个产品管理CRUD接口"
- ✅ "添加订单查询API，支持分页和状态筛选"

### 4.4 系统响应

录入需求后，系统会返回一个流水线 ID：

```json
{"pipelineId":"26c83132-7b81-404e-884b-e98b824d9843"}
```

保存这个 ID，用于后续查看状态。

---

## 5. 查看项目详情

### 5.1 进入项目详情

1. **访问项目管理页面**：点击左侧菜单"项目管理"
2. **点击项目卡片**：点击任意项目卡片或"查看详情"按钮

### 5.2 项目详情内容

项目详情页面包含以下信息：

#### 需求汇总统计

| 统计项 | 说明 |
|--------|------|
| 总需求数 | 项目下所有需求数量 |
| 已完成 | 状态为"已完成"的需求数量 |
| 执行中 | 状态为"执行中"的需求数量 |
| 待处理 | 状态为"待处理"的需求数量 |

#### 需求列表

- 展示项目下所有需求
- 支持按状态筛选（全部、已完成、执行中、待处理）
- 点击需求可跳转至对应的流水线详情

#### 流水线历史

- 展示项目下所有流水线执行记录
- 显示流水线状态和创建时间

---

## 6. 查看流水线详情

### 6.1 进入流水线详情

1. **访问流水线列表**：点击左侧菜单"流水线列表"
2. **点击流水线**：点击任意流水线进入详情页面

### 6.2 流水线详情内容

#### 流程进度条

- 可视化展示流水线执行进度
- 显示当前阶段和各阶段状态（已完成/执行中/待执行/失败）

#### 阶段详情

每个阶段可展开查看详细信息：

| 阶段 | 详情内容 |
|------|---------|
| 需求分析 | 功能点拆分结果、需求结构化文档 |
| 方案设计 | 技术方案、架构设计、API 设计 |
| 编码实现 | 生成的代码文件列表和内容预览 |
| 变更预览 | 代码差异对比 |
| 代码审查 | 审查结果、问题列表 |
| 测试执行 | 测试用例、测试结果 |
| 编译构建 | 构建状态、构建日志 |
| 发布部署 | 部署状态、访问地址 |

---

## 7. 自动编码实现

### 7.1 系统自动执行

提交需求后，系统自动执行以下编码步骤：

1. **代码分析**：AI 自动分析项目代码结构，识别技术栈和数据库配置
2. **需求分析**：基于代码分析结果，分析您的需求，提取关键信息
3. **方案设计**：AI 生成技术方案、架构设计和 API 设计
4. **功能点拆分**：将需求拆分为 3~8 个功能点
5. **任务拆分**：将功能点拆分为 1~3 个原子任务（Controller、Service、Repository等）
6. **代码生成**：AI 根据任务描述生成代码文件（三层分解聚焦生成）
7. **测试生成**：AI 生成对应的单元测试代码
8. **代码审查**：AI 审查生成的代码质量，检测 CRITICAL 问题

### 7.2 查看编码进度

通过日志查看编码进度：

```bash
# 查看应用日志
curl http://localhost:8083/api/pipelines/{pipelineId}
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

### 7.3 查看生成的代码

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

## 8. 自动编译构建

### 8.1 系统自动执行

编码完成后，系统自动执行：

1. **代码写入**：将生成的代码写入项目文件系统
2. **Git 提交**：将代码提交到版本控制系统
3. **测试执行**：运行项目测试用例，验证代码正确性
4. **自动修复**：测试失败时自动触发修复循环（最多重试3次）
5. **项目构建**：使用 Maven 编译打包项目

### 8.2 查看构建进度

通过日志查看构建状态：

```bash
curl http://localhost:8083/api/pipelines/{pipelineId}
```

当 `currentStage` 显示为 `BUILD` 时，表示正在构建。

### 8.3 确认构建成功

构建完成后，状态会更新：

```json
{
    "pipelineId": "26c83132-7b81-404e-884b-e98b824d9843",
    "status": "RUNNING",
    "currentStage": "DEPLOY"
}
```

---

## 9. 自动发布部署

### 9.1 系统自动执行

构建成功后，系统自动部署应用：

1. 创建 Docker 镜像
2. 启动 Docker 容器
3. 监听端口 8081

### 9.2 查看部署进度

通过日志查看部署状态：

```bash
curl http://localhost:8083/api/pipelines/{pipelineId}
```

当 `currentStage` 显示为 `DEPLOY` 时，表示正在部署。

---

## 10. 访问验证

### 10.1 确认流水线完成

部署完成后，流水线状态变为 `COMPLETED`：

```bash
curl http://localhost:8083/api/pipelines/{pipelineId}
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

### 10.2 访问部署的应用

访问 AI 生成的应用：

```bash
curl http://localhost:8081/api/health
```

预期响应：

```json
{"status":"UP","service":"demo-backend","timestamp":"2026-07-11T13:27:37"}
```

### 10.3 查看所有流水线

查看所有历史流水线：

```bash
curl http://localhost:8083/api/pipelines
```

---

## 11. 查看执行日志

### 11.1 查看应用日志

如果应用在后台运行，可以查看日志文件：

```bash
# 查看最新的日志
tail -f /tmp/agent-output.log

# 或者查看后台任务日志
cat /var/folders/.../jobs/.../output.log
```

### 11.2 查看 Temporal 日志

通过 Temporal Web UI 查看详细的工作流执行日志：

1. 访问 http://localhost:8233
2. 点击工作流 ID
3. 查看 **History** 标签页

### 11.3 日志关键字

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

## 12. 常见问题

### Q1: 如何确认系统是否启动成功？

```bash
curl http://localhost:8083/api/pipelines
# 返回 [] 表示成功
```

### Q2: 流水线执行需要多长时间？

通常需要 1-3 分钟，取决于：
- 需求复杂度
- 项目大小
- 测试执行时间

### Q3: 如何查看某个流水线的详细信息？

```bash
curl http://localhost:8083/api/pipelines/{pipelineId}
```

### Q4: 如何取消一个正在运行的流水线？

```bash
curl -X POST http://localhost:8083/api/pipelines/{pipelineId}/cancel
```

### Q5: 如何按状态筛选流水线？

```bash
# 查看已完成的流水线
curl http://localhost:8083/api/pipelines?status=COMPLETED

# 查看运行中的流水线
curl http://localhost:8083/api/pipelines?status=RUNNING

# 查看失败的流水线
curl http://localhost:8083/api/pipelines?status=FAILED
```

### Q6: 如何查看项目的需求列表？

```bash
curl http://localhost:8083/api/project/{projectId}/requirements
```

### Q7: 端口被占用怎么办？

```bash
# 释放端口 8080
lsof -ti:8080 | xargs kill -9

# 释放端口 8081
lsof -ti:8081 | xargs kill -9

# 释放端口 8082
lsof -ti:8082 | xargs kill -9

# 释放端口 8083
lsof -ti:8083 | xargs kill -9
```

### Q8: Temporal 服务器连接失败？

```bash
# 检查容器是否运行
docker ps | grep temporal

# 重新启动
docker restart temporal
```

### Q9: 部署失败怎么办？

检查目标项目是否可执行：

```bash
cd /path/to/demo-backend
java -jar target/*.jar --server.port=8081
```

### Q10: 如何查看项目详情？

```bash
curl http://localhost:8083/api/project/{projectId}
```

---

## 📋 完整操作流程总结

```
1. 启动系统
   → docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0
   → mvn clean package -DskipTests
   → java -jar ai-pipeline-api/target/ai-pipeline-api-1.0.0-SNAPSHOT.jar
   → java -jar ai-pipeline-gateway/target/ai-pipeline-gateway-1.0.0-SNAPSHOT.jar
   → cd ai-pipeline-ui && npm run dev

2. 选择项目并录入需求
   → 访问 http://localhost:8082/
   → 选择项目（如 demo-backend）
   → 输入需求描述
   → 点击"提交需求，启动AI流水线"

3. 等待执行（1-3分钟）
   → 系统自动完成：代码分析 → 需求分析 → 方案设计 → 功能拆分 → 任务拆分 → 代码生成 → 测试生成 → 代码审查 → 测试执行 → 构建 → 部署

4. 查看进度
   → 访问 http://localhost:8082/pipelines/{pipelineId}

5. 验证结果
   → curl http://localhost:8081/api/health
   → 预期响应: {"status":"UP","service":"demo-backend","timestamp":"..."}
```

---

**文档版本**: v2.0.0  
**生成日期**: 2026-07-12
