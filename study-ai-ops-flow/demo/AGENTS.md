# AI 编码助手规范（AGENTS.md）

## 1. 项目上下文

### 1.1 项目概述

- 项目名称：demo（study-ai-ops-flow 演练工程）
- 技术栈：Java 21、Spring Boot 3.3、Spring Data JPA、H2；前端 Vue 3 + Vite
- 主要功能：图书（Book）、留言（Message）、反馈（Feedback）管理

### 1.2 架构说明

- 分层架构：Controller → Service → Repository
- Service 接口在 `service/` 包，实现统一放在 `service/impl/`
- 业务异常统一定义在 `exception/` 包，由 `exception/GlobalExceptionHandler` 统一转换为 HTTP 响应
- DTO 命名：请求 `{Entity}Request`/`{Entity}CreateRequest`，响应 `{Entity}Response`

### 1.3 代码风格

- 命名：驼峰命名法
- 依赖注入：Lombok `@RequiredArgsConstructor` 构造器注入
- 日志：Lombok `@Slf4j`

## 2. 编码规则

### 2.1 必须遵循

- Controller 使用 `@RestController` + `@RequestMapping("/api/xxx")`，返回 `ResponseEntity<T>`
- Service 实现类放在 `service/impl/`，类名 `{Entity}ServiceImpl`
- Entity 主键使用 `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Repository 继承 `JpaRepository<T, ID>`
- DTO 使用 Lombok `@Data` + `@Builder`，校验注解写在 DTO 层
- 资源不存在抛 `ResourceNotFoundException`，业务冲突（如 ISBN 重复）抛 `DuplicateIsbnException`
- 构建系统统一为 Maven（父 POM 聚合模块）

### 2.2 禁止操作

- 禁止出现重复的同名类（如异常类跨包重复定义）
- 禁止在 Controller 中写业务逻辑
- 禁止提交 `.DS_Store`、`*.log`、`node_modules/`、构建产物
- 禁止同一功能并存两套构建/配置文件

### 2.3 最佳实践

- 修改功能时以测试为契约：先确认既有测试的期望行为
- 实体时间字段用 `@PrePersist/@PreUpdate` 自动填充
- 分页查询返回 Spring Data `Page<T>`

## 3. 技能使用指引

### 3.1 推荐技能

- common-project-standard - 项目结构与规范验证
- common-coding - 编码规范指导
- common-backend-unit-test - 后端单元测试

### 3.2 技能组合

- 新建功能 → common-requirement-clarification → common-coding → common-backend-unit-test

## 4. 工具配置

### 4.1 Linter/Formatter

- 暂无独立配置，遵循 Spring 官方 Java 风格

### 4.2 测试框架

- JUnit 5 + Mockito + AssertJ + Spring Boot Test
- 测试位置与被测类同包；`@WebMvcTest` 测控制层，`@DataJpaTest` 测数据层，Mockito 单测 Service

## 5. 安全规范

### 5.1 敏感信息

- 数据源账号使用 H2 内存库默认配置，生产环境凭证走环境变量
- 禁止提交 `.env`、密钥类文件

### 5.2 代码审查

- PR 必须通过 CI（`.github/workflows/ci.yml`：mvn test）
- 禁止引入重复实现/遗留文件
