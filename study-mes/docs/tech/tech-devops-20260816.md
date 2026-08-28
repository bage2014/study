# 部署与运维

> 创建时间：2026-08-16
> 模块：tech

MES/PLM 系统的部署架构、环境管理、CI/CD、监控运维思路。

## 一、环境分层

| 环境 | 用途 | 数据 | 部署 |
|------|------|------|------|
| local | 本地开发 | H2 / 本地 | 手工 |
| dev | 联调 | 独立 DB | 自动 |
| test | 测试 | 测试数据 | 自动 |
| staging | 预生产（UAT） | 生产脱敏 | 审批 |
| prod | 生产 | 真实 | 审批 |

### 配置隔离
- 配置走 Nacos / 环境变量，不入代码。
- 敏感信息（密码、密钥）用 Vault / 加密注入。

## 二、部署架构

### 单体部署

```
Nginx
  ├─ 静态资源（前端 dist）
  └─ 反向代理 → Spring Boot（多实例）
                  ├─ MySQL（主从）
                  ├─ Redis
                  ├─ Kafka
                  └─ MinIO
```

### 容器化（Docker Compose，中小规模）

```yaml
# docker-compose.yml
services:
  app:
    image: registry/mes:${TAG}
    ports: ["8080:8080"]
    environment:
      SPRING_PROFILES_ACTIVE: ${PROFILE}
      MYSQL_HOST: mysql
      REDIS_HOST: redis
    depends_on: [mysql, redis, kafka, minio]
    deploy:
      replicas: 2
      resources:
        limits: { cpus: '2', memory: 2G }

  mysql:
    image: mysql:8
    volumes: ["mysql_data:/var/lib/mysql"]

  redis:
    image: redis:7
    volumes: ["redis_data:/data"]

  kafka:
    image: bitnami/kafka:3.7
    volumes: ["kafka_data:/bitnami/kafka"]

  minio:
    image: minio/minio
    command: server /data
    volumes: ["minio_data:/data"]

  nginx:
    image: nginx:1.25
    ports: ["80:80", "443:443"]
    volumes: ["./nginx.conf:/etc/nginx/nginx.conf", "./dist:/usr/share/nginx/html"]
    depends_on: [app]
```

### K8s（大规模）

- 应用 Deployment + HPA（按 CPU/内存自动伸缩）
- 数据库用 StatefulSet 或托管服务
- 配置 ConfigMap / Secret
- 入口 Ingress

## 三、Dockerfile

```dockerfile
# 多阶段构建
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline                # 依赖缓存层
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
EXPOSE 8080
HEALTHCHECK --interval=30s CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
```

## 四、CI/CD

### 流水线

```
Push → 构建 → 单元测试 → 集成测试 → 镜像构建 → 推送 → 部署 dev
                                                  ├─ 部署 test（tag）
                                                  └─ 审批 → 部署 staging/prod
```

### 示例（GitLab CI）

```yaml
stages: [build, test, image, deploy]

build:
  stage: build
  script: ./mvnw -B package -DskipTests
  artifacts: { paths: [target/*.jar] }

unit-test:
  stage: test
  script: ./mvnw test
  coverage: '/Total.*?([0-9]{1,3})%/'

integration-test:
  stage: test
  services: [mysql:8, redis:7]
  script: ./mvnw verify -Pintegration

docker-build:
  stage: image
  script: docker build -t $CI_REGISTRY/mes:$CI_COMMIT_SHORT_SHA .
  only: [main, tags]

deploy-dev:
  stage: deploy
  script: ./deploy.sh dev
  only: [main]

deploy-prod:
  stage: deploy
  script: ./deploy.sh prod
  when: manual                       # 手动审批
  only: [tags]
```

### 部署策略

| 策略 | 说明 | 风险 |
|------|------|------|
| 滚动更新 | 逐实例替换 | 低 |
| 蓝绿 | 两套环境切换 | 中，瞬时切换 |
| 金丝雀 | 小流量验证 | 低，推荐 |

## 五、数据库迁移

### Flyway

```
resources/db/migration/
├── V1__init_schema.sql
├── V2__add_workorder_table.sql
├── V3__add_traceability.sql
└── V4__add_inspection_plan.sql
```

### 原则
- 版本号严格递增，不修改已发布脚本。
- 应用启动自动迁移（`spring.flyway.enabled=true`）。
- 生产大表变更走在线 DDL / pt-osc，分批执行。
- 迁移前备份。

## 六、监控

### 指标分类

| 类别 | 指标 |
|------|------|
| 应用 | QPS、RT、错误率、线程池 |
| JVM | 堆内存、GC、CPU |
| DB | 连接数、慢 SQL、主从延迟 |
| 中间件 | Redis 命中率、MQ 堆积、MinIO 容量 |
| 业务 | 工单积压、报工延迟、MQ 死信 |

### 工具链
```
Spring Boot Actuator → Micrometer → Prometheus → Grafana
```

### 自定义业务指标
```java
@Component
public class BusinessMetrics {
    private final Counter workReportCounter;
    private final Timer reportLatency;

    public BusinessMetrics(MeterRegistry registry) {
        workReportCounter = Counter.builder("mes.workreport.count").register(registry);
        reportLatency = Timer.builder("mes.workreport.latency").register(registry);
    }

    public void recordReport(long durationMs) {
        workReportCounter.increment();
        reportLatency.record(durationMs, MILLISECONDS);
    }
}
```

## 七、日志

### 规范
- JSON 格式输出，便于采集解析。
- 统一 traceId（SkyWalking / Sleuth）贯穿。
- 关键操作 INFO，异常 ERROR + 堆栈。
- 不打敏感信息（密码、密钥）。

### 采集
```
应用 → Filebeat → Logstash / Loki → 检索
```

## 八、告警

| 指标 | 阈值 | 通知 |
|------|------|------|
| 应用错误率 | > 1% | 钉钉/电话 |
| RT P99 | > 2s | 钉钉 |
| DB 连接数 | > 80% | 钉钉 |
| 磁盘使用 | > 85% | 邮件 |
| MQ 堆积 | > 10000 | 钉钉 |
| 服务宕机 | 探针失败 | 电话 |

## 九、健康检查与自愈

### 健康端点
```yaml
management:
  endpoint:
    health:
      probes:
        enabled: true
      show-details: always
```

### K8s 探针
```yaml
livenessProbe:
  httpGet: { path: /actuator/health/liveness, port: 8080 }
  initialDelaySeconds: 60
readinessProbe:
  httpGet: { path: /actuator/health/readiness, port: 8080 }
```

- liveness 失败 → 重启
- readiness 失败 → 移出流量

## 十、备份与恢复

| 对象 | 频率 | 保留 |
|------|------|------|
| MySQL | 每日全量 + binlog 增量 | 30 天 |
| MinIO | 跨区域复制 | 按需 |
| Redis | RDB + AOF | 7 天 |
| 配置 | Nacos / Git | 永久 |

### 演练
- 定期恢复演练（每季度）。
- 验证备份可用性。

## 十一、安全

| 项 | 措施 |
|----|------|
| 传输 | HTTPS / TLS |
| 认证 | JWT + 强密码策略 |
| 授权 | RBAC + 数据权限 |
| 注入 | 参数化查询、输入校验 |
| 审计 | 操作日志 |
| 密钥 | Vault 管理、轮换 |
| 漏洞 | 定期扫描、依赖更新 |
| 限流 | 网关 / Bucket4j |

## 十二、运维流程

| 流程 | 要点 |
|------|------|
| 变更 | 提交变更单 → 评审 → 窗口期执行 |
| 发布 | 灰度 → 观察 → 全量 |
| 回滚 | 镜像版本 + DB 迁移回滚预案 |
| 故障 | 告警 → 应急 → 止血 → 根因 → 复盘 |

## 十三、相关文档

- [框架与分层](./tech-framework-20260816.md)
- [测试策略](./tech-testing-20260816.md)
- [主数据事件同步](./tech-mdm-sync-20260816.md)
- [集成架构](../integration/integration-architecture-20260816.md)
