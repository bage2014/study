# 项目规范文档

## 1. 项目概述

本项目是一个完整的家庭族谱APP应用，包括Web端、Flutter移动端和Spring Boot后端服务。通过该应用，用户可以创建、管理和查看家族成员信息，构建完整的家族树，记录家族历史和重要事件。

## 2. 技术栈

### 2.1 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.1.10 | 后端框架 |
| Java | 21 | 开发语言 |
| Spring Security | - | 安全认证 |
| Spring Data JPA | - | 数据访问 |
| H2 Database | - | 内存数据库 |
| JWT | 0.11.5 | 身份认证 |
| SpringDoc OpenAPI | 2.3.0 | API文档 |
| Lombok | - | 代码简化 |

### 2.2 前端Web技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5.30 | 前端框架 |
| Vue Router | 4.6.4 | 路由管理 |
| Pinia | 3.0.4 | 状态管理 |
| TailwindCSS | 4.2.2 | CSS框架 |
| Axios | 1.14.0 | HTTP客户端 |
| D3.js | 7.9.0 | 数据可视化 |
| Selenium WebDriver | 4.43.0 | 端到端测试 |
| Vite | 8.0.1 | 构建工具 |

### 2.3 移动端技术栈

| 技术 | 用途 |
|------|------|
| Flutter | 跨平台移动开发 |
| Dart | 开发语言 |

## 3. 项目结构

```
├── backend/           # 后端Spring Boot项目
│   ├── src/           # 源代码
│   └── pom.xml        # Maven配置
├── frontend/          # 前端项目
│   ├── web/           # Web端实现
│   └── android/       # Flutter移动端
├── docs/              # 项目文档
└── README.md          # 项目说明
```

## 4. 开发规范

### 4.1 后端开发规范

- **代码风格**：遵循Java编码规范
- **包结构**：按功能模块组织
- **测试**：使用JUnit 5 + Mockito进行单元测试
- **API文档**：使用SpringDoc自动生成OpenAPI文档
- **异常处理**：统一全局异常处理
- **日志**：使用Spring Boot默认日志系统

### 4.2 前端Web开发规范

- **代码风格**：ESLint + Prettier
- **组件命名**：使用PascalCase
- **文件命名**：使用kebab-case
- **状态管理**：使用Pinia
- **路由管理**：使用Vue Router
- **样式**：使用TailwindCSS
- **测试**：使用Selenium进行端到端测试

### 4.3 移动端开发规范

- **代码风格**：遵循Dart编码规范
- **Widget命名**：使用PascalCase
- **文件命名**：使用snake_case
- **状态管理**：使用Provider
- **路由管理**：使用GoRouter

## 5. 测试规范

### 5.1 后端测试

- **单元测试**：使用JUnit 5 + Mockito
- **集成测试**：使用Spring Boot Test
- **代码覆盖率**：使用JaCoCo

### 5.2 前端测试

- **端到端测试**：使用Selenium WebDriver
- **测试目录**：`frontend/web/tests/`
- **测试命令**：
  - `npm run test:all` - 运行所有测试
  - `npm run test:auth` - 运行认证测试
  - `npm run test:family` - 运行家族管理测试
  - `npm run test:member` - 运行成员管理测试

## 6. 部署规范

### 6.1 后端部署

- **构建**：`mvn clean package`
- **运行**：`java -jar backend-1.0.0.jar`
- **默认端口**：8080

### 6.2 前端Web部署

- **构建**：`npm run build`
- **部署目录**：`dist/`
- **开发服务器**：`npm run dev` (端口：5174)

### 6.3 移动端部署

- **Android**：`flutter build apk`
- **iOS**：`flutter build ios`

## 7. 技术文档

- **产品需求文档**：`docs/prd/`
- **UI设计文档**：`docs/ui-design/`
- **API文档**：通过SpringDoc生成，访问 `/swagger-ui.html`
- **测试文档**：`docs/testing/`

## 8. 开发流程

1. **产品调研**：市场分析、用户需求调研
2. **PRD生成**：详细的产品需求文档
3. **UI设计**：界面设计和用户体验优化
4. **编码实现**：前后端开发和集成
5. **测试验证**：功能测试和性能优化
6. **部署上线**：应用部署和维护

## 9. 注意事项

- **数据库**：默认使用H2内存数据库，生产环境需配置其他数据库
- **认证**：使用JWT进行身份认证
- **测试**：所有前端测试基于Selenium WebDriver
- **端口**：后端默认8080，前端开发服务器5174

## 10. 版本控制

- 使用Git进行版本控制
- 遵循语义化版本规范

## 11. 许可证

本项目采用MIT许可证。