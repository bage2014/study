# DevOps 规范

## 1. 技能概述

本技能提供了家族树应用的DevOps规范，包括CI/CD流程、容器化部署、监控系统、日志管理等方面。通过本规范，开发者可以实现自动化构建、部署和运维，提高开发效率和系统可靠性。

## 2. DevOps 原则

### 2.1 核心原则
- **自动化**：自动化所有可自动化的流程
- **持续集成**：频繁集成代码，尽早发现问题
- **持续部署**：自动化部署到生产环境
- **监控与反馈**：实时监控系统状态，快速响应问题
- **协作**：开发和运维团队紧密协作

### 2.2 工具链
- **版本控制**：Git
- **CI/CD**：GitHub Actions、GitLab CI、Jenkins
- **容器化**：Docker、Docker Compose
- **编排**：Kubernetes
- **监控**：Prometheus、Grafana
- **日志**：ELK Stack（Elasticsearch, Logstash, Kibana）
- **配置管理**：Ansible、Terraform

## 3. CI/CD 流程

### 3.1 持续集成

#### 3.1.1 流程设计
1. **代码提交**：开发者提交代码到Git仓库
2. **自动化构建**：CI系统自动构建项目
3. **自动化测试**：运行单元测试、集成测试
4. **代码质量检查**：检查代码质量、代码风格
5. **依赖安全扫描**：扫描依赖的安全漏洞
6. **构建产物**：生成构建产物

#### 3.1.2 GitHub Actions 配置
```yaml
# .github/workflows/ci.yml
name: CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Build backend
      run: |
        cd backend
        mvn clean package -DskipTests
    
    - name: Set up Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
    
    - name: Build frontend
      run: |
        cd frontend/web
        npm install
        npm run build
    
    - name: Run tests
      run: |
        cd backend
        mvn test
    
    - name: Upload artifacts
      uses: actions/upload-artifact@v3
      with:
        name: build-artifacts
        path: |
          backend/target/*.jar
          frontend/web/dist
```

### 3.2 持续部署

#### 3.2.1 流程设计
1. **构建产物**：获取CI流程生成的构建产物
2. **环境准备**：准备部署环境
3. **部署应用**：部署应用到目标环境
4. **健康检查**：检查应用是否正常运行
5. **回滚机制**：部署失败时自动回滚

#### 3.2.2 GitHub Actions 部署配置
```yaml
# .github/workflows/cd.yml
name: CD

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    needs: build
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Download artifacts
      uses: actions/download-artifact@v3
      with:
        name: build-artifacts
    
    - name: Deploy to production
      run: |
        # 部署脚本
        chmod +x scripts/deploy.sh
        ./scripts/deploy.sh production
    
    - name: Health check
      run: |
        # 健康检查脚本
        chmod +x scripts/health-check.sh
        ./scripts/health-check.sh
    
    - name: Rollback on failure
      if: failure()
      run: |
        # 回滚脚本
        chmod +x scripts/rollback.sh
        ./scripts/rollback.sh
```

## 4. 容器化部署

### 4.1 Docker 配置

#### 4.1.1 后端 Dockerfile
```dockerfile
# backend/Dockerfile
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/backend-1.0.0.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/family_tree
ENV SPRING_DATASOURCE_USERNAME=familytree
ENV SPRING_DATASOURCE_PASSWORD=your_secure_password
ENV JWT_SECRET=your_jwt_secret

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 4.1.2 前端 Dockerfile
```dockerfile
# frontend/web/Dockerfile
FROM node:18-alpine as build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

#### 4.1.3 Docker Compose 配置
```yaml
# docker-compose.yml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: family_tree
      POSTGRES_USER: familytree
      POSTGRES_PASSWORD: your_secure_password
    volumes:
      - postgres-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    restart: unless-stopped

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    depends_on:
      - postgres
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/family_tree
      SPRING_DATASOURCE_USERNAME: familytree
      SPRING_DATASOURCE_PASSWORD: your_secure_password
      JWT_SECRET: your_jwt_secret
    restart: unless-stopped

  frontend:
    build:
      context: ./frontend/web
      dockerfile: Dockerfile
    depends_on:
      - backend
    ports:
      - "80:80"
    restart: unless-stopped

volumes:
  postgres-data:
```

### 4.2 容器管理

#### 4.2.1 容器编排
- **Docker Compose**：本地开发和测试环境
- **Kubernetes**：生产环境，支持自动扩缩容

#### 4.2.2 容器最佳实践
- **最小化镜像**：使用Alpine基础镜像
- **多阶段构建**：减少镜像大小
- **环境变量**：使用环境变量管理配置
- **健康检查**：配置容器健康检查
- **资源限制**：设置容器资源限制

## 5. 监控系统

### 5.1 监控架构

#### 5.1.1 监控组件
- **Prometheus**：指标收集和存储
- **Grafana**：指标可视化
- **Alertmanager**：告警管理
- **Node Exporter**：主机指标收集
- **Spring Boot Actuator**：应用指标收集

#### 5.1.2 监控配置
```yaml
# docker-compose.monitoring.yml
version: '3.8'

services:
  prometheus:
    image: prom/prometheus:latest
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"
    restart: unless-stopped

  grafana:
    image: grafana/grafana:latest
    volumes:
      - grafana-data:/var/lib/grafana
    ports:
      - "3000:3000"
    restart: unless-stopped

  node-exporter:
    image: prom/node-exporter:latest
    ports:
      - "9100:9100"
    restart: unless-stopped

volumes:
  grafana-data:
```

### 5.2 监控指标

#### 5.2.1 系统指标
- **CPU使用率**：系统CPU使用情况
- **内存使用率**：系统内存使用情况
- **磁盘使用率**：磁盘空间使用情况
- **网络流量**：网络进出流量

#### 5.2.2 应用指标
- **响应时间**：API响应时间
- **错误率**：API错误率
- **QPS**：每秒查询数
- **JVM指标**：Java虚拟机指标
- **数据库指标**：数据库连接数、查询性能

### 5.3 告警配置

#### 5.3.1 告警规则
- **系统告警**：CPU、内存、磁盘使用率超过阈值
- **应用告警**：API错误率超过阈值，响应时间过长
- **服务告警**：服务不可用

#### 5.3.2 告警通知
- **邮件通知**：发送告警邮件
- **短信通知**：发送告警短信
- **即时通讯**：通过Slack、钉钉等发送告警

## 6. 日志管理

### 6.1 日志架构

#### 6.1.1 日志组件
- **Elasticsearch**：日志存储和搜索
- **Logstash**：日志收集和处理
- **Kibana**：日志可视化
- **Filebeat**：日志采集

#### 6.1.2 日志配置
```yaml
# docker-compose.logging.yml
version: '3.8'

services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.17.0
    environment:
      - discovery.type=single-node
      - ES_JAVA_OPTS=-Xms512m -Xmx512m
    volumes:
      - elasticsearch-data:/usr/share/elasticsearch/data
    ports:
      - "9200:9200"
    restart: unless-stopped

  logstash:
    image: docker.elastic.co/logstash/logstash:7.17.0
    volumes:
      - ./logstash.conf:/etc/logstash/conf.d/logstash.conf
    ports:
      - "5044:5044"
    depends_on:
      - elasticsearch
    restart: unless-stopped

  kibana:
    image: docker.elastic.co/kibana/kibana:7.17.0
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch
    restart: unless-stopped

  filebeat:
    image: docker.elastic.co/beats/filebeat:7.17.0
    volumes:
      - ./filebeat.yml:/usr/share/filebeat/filebeat.yml
      - /var/lib/docker/containers:/var/lib/docker/containers:ro
      - /var/run/docker.sock:/var/run/docker.sock:ro
    depends_on:
      - logstash
    restart: unless-stopped

volumes:
  elasticsearch-data:
```

### 6.2 日志规范

#### 6.2.1 日志格式
- **结构化日志**：使用JSON格式
- **日志级别**：DEBUG、INFO、WARN、ERROR、FATAL
- **日志内容**：包含时间戳、级别、模块、消息、上下文信息

#### 6.2.2 日志最佳实践
- **适当的日志级别**：根据消息重要性选择合适的级别
- **避免敏感信息**：不记录密码、令牌等敏感信息
- **日志轮转**：配置日志轮转，避免日志文件过大
- **日志压缩**：压缩归档日志
- **日志保留**：设置合理的日志保留期限

## 7. 配置管理

### 7.1 配置管理策略

#### 7.1.1 配置分类
- **环境配置**：开发、测试、生产环境配置
- **应用配置**：应用特定配置
- **敏感配置**：密码、令牌等敏感信息

#### 7.1.2 配置存储
- **配置文件**：非敏感配置存储在配置文件中
- **环境变量**：敏感配置通过环境变量注入
- **配置中心**：使用Consul、Spring Cloud Config等配置中心

### 7.2 配置最佳实践

#### 7.2.1 配置版本控制
- 配置文件纳入版本控制
- 敏感配置不纳入版本控制
- 配置变更记录

#### 7.2.2 配置管理工具
- **Ansible**：配置管理和自动化
- **Terraform**：基础设施即代码
- **Vault**： secrets管理

## 8. 环境管理

### 8.1 环境类型

#### 8.1.1 开发环境
- **特点**：本地开发，快速迭代
- **配置**：使用H2内存数据库，简化配置
- **工具**：Docker Compose

#### 8.1.2 测试环境
- **特点**：模拟生产环境，进行集成测试
- **配置**：使用PostgreSQL，接近生产配置
- **工具**：Docker Compose或Kubernetes

#### 8.1.3 生产环境
- **特点**：正式环境，高可用性
- **配置**：使用PostgreSQL主从复制，完整配置
- **工具**：Kubernetes，负载均衡

### 8.2 环境部署策略

#### 8.2.1 蓝绿部署
- **原理**：同时运行两个版本的应用，通过切换流量实现部署
- **优点**：零 downtime，快速回滚
- **适用场景**：生产环境部署

#### 8.2.2 滚动部署
- **原理**：逐步替换旧版本的实例
- **优点**：资源占用少
- **适用场景**：常规更新

#### 8.2.3 金丝雀部署
- **原理**：先部署少量实例，逐步扩大范围
- **优点**：风险小，可快速回滚
- **适用场景**：重大更新

## 9. 自动化运维

### 9.1 自动化脚本

#### 9.1.1 构建脚本
```bash
#!/bin/bash
# scripts/build.sh

set -e

echo "Building backend..."
cd backend
mvn clean package -DskipTests

 echo "Building frontend..."
cd ../frontend/web
npm install
npm run build

echo "Build completed successfully!"
```

#### 9.1.2 部署脚本
```bash
#!/bin/bash
# scripts/deploy.sh

set -e

ENVIRONMENT=$1

echo "Deploying to $ENVIRONMENT environment..."

if [ "$ENVIRONMENT" = "production" ]; then
  # 生产环境部署
  docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
elif [ "$ENVIRONMENT" = "staging" ]; then
  # 测试环境部署
  docker-compose -f docker-compose.yml -f docker-compose.staging.yml up -d
else
  # 开发环境部署
  docker-compose up -d
fi

echo "Deployment completed successfully!"
```

#### 9.1.3 备份脚本
```bash
#!/bin/bash
# scripts/backup.sh

set -e

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="./backups"

mkdir -p $BACKUP_DIR

echo "Backing up database..."
docker exec familytree-postgres pg_dump -U familytree family_tree > $BACKUP_DIR/familytree_$DATE.sql

echo "Backing up files..."
tar -czf $BACKUP_DIR/files_$DATE.tar.gz ./frontend/web/dist

echo "Backup completed successfully!"

# 清理7天前的备份
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
find $BACKUP_DIR -name "*.tar.gz" -mtime +7 -delete
```

### 9.2 运维监控

#### 9.2.1 监控仪表盘
- **系统仪表盘**：显示系统资源使用情况
- **应用仪表盘**：显示应用性能指标
- **业务仪表盘**：显示业务关键指标

#### 9.2.2 自动化运维工具
- **Ansible**：自动化配置管理
- **Terraform**：基础设施即代码
- **Jenkins**：自动化构建和部署
- **Nagios**：系统监控

## 10. 灾难恢复

### 10.1 灾难恢复计划

#### 10.1.1 风险评估
- **识别风险**：识别可能的灾难场景
- **影响分析**：分析灾难对业务的影响
- **恢复时间目标**：设定合理的恢复时间目标

#### 10.1.2 恢复策略
- **数据备份**：定期备份数据
- **多区域部署**：跨区域部署，提高可用性
- **灾备演练**：定期进行灾备演练

### 10.2 恢复流程

#### 10.2.1 应急响应
- **启动应急响应**：发现灾难后立即启动
- **评估情况**：评估灾难影响范围
- **实施恢复**：执行恢复计划
- **验证恢复**：验证系统是否恢复正常

#### 10.2.2 恢复测试
- **定期测试**：定期测试恢复流程
- **更新计划**：根据测试结果更新恢复计划
- **培训演练**：培训团队成员熟悉恢复流程

## 11. 最佳实践

1. **自动化优先**：自动化所有可自动化的流程
2. **持续集成**：频繁集成代码，尽早发现问题
3. **持续部署**：自动化部署到生产环境
4. **监控先行**：在开发阶段就加入监控
5. **安全集成**：将安全检查集成到CI/CD流程
6. **配置管理**：使用配置管理工具管理配置
7. **容器化**：使用容器化技术提高部署效率
8. **基础设施即代码**：使用代码管理基础设施
9. **灾难恢复**：制定完善的灾难恢复计划
10. **持续改进**：不断优化DevOps流程

## 12. 常见问题及解决方案

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| 构建失败 | 依赖问题 | 检查依赖版本，使用缓存 |
| 部署失败 | 配置错误 | 检查配置文件，使用环境变量 |
| 服务不可用 | 资源不足 | 增加资源，优化配置 |
| 监控告警 | 阈值设置不合理 | 调整告警阈值 |
| 日志过多 | 日志级别设置不当 | 调整日志级别，使用结构化日志 |

## 13. 总结

本DevOps规范提供了全面的DevOps实践指南，包括CI/CD流程、容器化部署、监控系统、日志管理、配置管理、环境管理、自动化运维和灾难恢复等方面。通过遵循本规范，开发者可以实现自动化构建、部署和运维，提高开发效率和系统可靠性。

DevOps是一个持续改进的过程，需要不断地评估和优化。开发者应该保持学习，关注最新的DevOps工具和实践，不断提高DevOps水平。