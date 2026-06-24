# Gray Flow 测试文档

## 一、测试概述

本文档描述 `study-gray-flow` 模块的测试策略和测试用例。

## 二、测试环境

| 环境 | 配置 |
|-----|------|
| Java 版本 | 21 |
| 数据库 | H2 内存数据库 |
| 测试框架 | Spring Boot Test |
| Mock 工具 | Mockito |

## 三、测试分类

### 3.1 单元测试

| 测试类 | 测试内容 | 优先级 |
|-------|---------|-----|
| LogServiceTest | 日志服务单元测试 | 高 |
| ReplayServiceTest | 回放服务单元测试 | 高 |
| GrayFieldExtractorTest | 字段提取器测试 | 中 |
| WhitelistMatcherTest | 白名单匹配器测试 | 中 |

### 3.2 集成测试

| 测试类 | 测试内容 | 优先级 |
|-------|---------|-----|
| LogControllerTest | 日志 API 集成测试 | 高 |
| ReplayControllerTest | 回放 API 集成测试 | 高 |
| GrayFlowApplicationTests | 应用启动测试 | 高 |

### 3.3 E2E 测试

由 `study-gray-flow-ui` 模块的 Playwright 测试覆盖

## 四、测试用例

### 4.1 日志查询测试

**测试场景 1**: 查询日志列表

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | POST /api/logs 创建测试数据 | 返回 201 |
| 2 | GET /api/logs | 返回日志列表 |
| 3 | GET /api/logs?page=0&size=10 | 返回分页结果 |

**测试场景 2**: 按类型筛选日志

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | GET /api/logs?logType=CONTROLLER | 只返回 CONTROLLER 类型日志 |
| 2 | GET /api/logs?logType=PROXY | 只返回 PROXY 类型日志 |

### 4.2 调用链查询测试

**测试场景**: 通过 TraceId 查询调用链

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | 获取一条日志的 traceId | traceId 不为空 |
| 2 | GET /api/logs/trace/{traceId} | 返回完整调用链列表 |
| 3 | GET /api/logs/trace/invalid-id | 返回 404 |

### 4.3 回放会话测试

**测试场景 1**: 创建回放会话

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | POST /api/replay/sessions {"count": 5} | 返回 201，status=RUNNING |
| 2 | POST /api/replay/sessions {"count": 0} | 返回 400 |
| 3 | POST /api/replay/sessions {"count": 101} | 返回 400 |

**测试场景 2**: 查询回放会话

| 步骤 | 操作 | 预期结果 |
|-----|------|---------|
| 1 | GET /api/replay/sessions | 返回会话列表 |
| 2 | GET /api/replay/sessions/{sessionId} | 返回会话详情 |
| 3 | GET /api/replay/sessions/invalid | 返回 404 |

## 五、测试执行

### 5.1 运行所有测试

```bash
cd study-gray-flow
mvn test
```

### 5.2 运行指定测试类

```bash
mvn test -Dtest=LogServiceTest
```

### 5.3 运行集成测试

```bash
mvn test -Dtest=*ControllerTest
```

## 六、测试覆盖率

目标覆盖率：
- 单元测试：80% 以上
- 集成测试：核心 API 100%

## 七、测试数据准备

### 7.1 测试日志数据

```json
{
  "logType": "CONTROLLER",
  "traceId": "test-trace-id-001",
  "className": "UserController",
  "methodName": "getUser",
  "durationMs": 10,
  "args": "[\"10001\"]",
  "resultSummary": "{\"id\":1,\"name\":\"张三\"}"
}
```

### 7.2 测试回放数据

```json
{
  "httpMethod": "GET",
  "requestUri": "/api/users",
  "matchResult": "MATCH",
  "chainMatchResult": "CHAIN_MATCH"
}
```

## 八、CI/CD 集成

在 CI 流程中执行：

```bash
cd study-gray-flow
mvn clean test
```

测试通过后才能合并代码。