---
name: "common-dockerize"
description: "Docker容器化技能 - 生成Docker配置"
---

# Docker容器化技能

## 功能描述

为项目生成 Docker 配置文件，支持 Spring Boot 和 Vue 项目的容器化部署。

## 何时使用

- 项目需要容器化部署时
- 创建开发/测试/生产环境的 Docker 镜像
- 配置多容器编排

## 核心功能

- 生成 Dockerfile
- 生成 docker-compose.yml
- 配置多环境 Docker 镜像
- 优化镜像大小
- 支持 Spring Boot 和 Vue 项目

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| projectType | String | 是 | 项目类型（spring-boot/vue） |
| appName | String | 是 | 应用名称 |
| appPort | String | 否 | 应用端口（默认 8080） |
| withCompose | Boolean | 否 | 是否生成 docker-compose（默认 true） |
| withDatabase | Boolean | 否 | 是否包含数据库服务（默认 false） |
| databaseType | String | 否 | 数据库类型（mysql/postgresql，默认 mysql） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "projectType": "spring-boot",
  "filesCreated": [
    "Dockerfile",
    "docker-compose.yml",
    ".dockerignore"
  ],
  "imageName": "my-spring-app:latest",
  "commands": [
    "docker build -t my-spring-app .",
    "docker-compose up -d"
  ]
}
```

## 使用流程

1. 指定项目类型
2. 提供应用信息
3. 选择是否需要数据库
4. 生成 Docker 配置文件

## 最佳实践

- 使用多阶段构建减小镜像大小
- 开发环境使用 docker-compose 编排
- 生产环境使用 Kubernetes 或 Swarm

## 配置要求

### Dockerfile 示例（Spring Boot）

```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml 示例

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - db
  db:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=password
      - MYSQL_DATABASE=example_db
    volumes:
      - mysql_data:/var/lib/mysql
volumes:
  mysql_data:
```

## 扩展指南

支持自定义镜像模板和编排配置。