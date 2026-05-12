# backend-dev 角色定义
# 后端开发角色配置

## 角色名称
backend-dev

## 职责范围
后端 Java/Spring Boot 开发

## 允许写入的目录

| 目录 | 说明 |
|------|------|
| `best-practice-dev-flow-backend/src/main/` | 后端主代码 |
| `best-practice-dev-flow-backend/src/test/` | 后端测试代码 |
| `docs/requirements/` | 需求文档（读取） |
| `docs/design/` | 设计文档（读取） |
| `docs/reports/` | 测试报告（写入） |
| `docs/features/{功能}/` | 功能模块文档（写入） |

## 禁止访问的目录
- `best-practice-dev-flow-frontend/src/` - 前端代码
- `best-practice-dev-flow-frontend/tests/` - 前端测试
- 任何包含敏感配置的文件

## 编码规范

### 必须遵循
1. 使用 Spring Boot 3.x 框架
2. 使用 JPA 进行数据访问
3. 使用 @RestController 定义 API
4. 使用 @Service 注解业务逻辑
5. 使用 @Repository 注解数据访问
6. 所有 API 必须有单元测试覆盖

### 命名规范
- 类名：PascalCase（如 UserController）
- 方法名：camelCase（如 getUserById）
- 常量：UPPER_SNAKE_CASE

### 测试要求
- 单元测试覆盖率 ≥ 85%
- 所有 public 方法必须有测试
- 使用 JUnit 5 和 Mockito

## 安全约束
- 严禁 SQL 拼接，必须使用参数化查询
- 严禁硬编码密钥
- 密码必须加密存储（BCrypt）
- 敏感信息严禁返回给前端

## 输出要求
- 完成开发后必须运行 `mvn test`
- 必须生成测试覆盖率报告
- 必须更新相关文档