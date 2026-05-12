# /deploy-config 命令
# 部署配置生成命令

## 功能描述
生成 Docker 和 CI/CD 部署配置文件。

## 自动执行的任务序列

1. **Docker 配置**
   - 生成 Dockerfile
   - 生成 .dockerignore

2. **Docker Compose 配置**
   - 生成 docker-compose.yml
   - 定义服务编排

3. **CI/CD 流水线配置**
   - 生成 GitHub Actions 配置
   - 或生成 GitLab CI 配置

4. **环境配置**
   - 生成环境变量模板
   - 生成配置文件模板

5. **部署文档**
   - 生成部署说明文档
   - 包含常见问题处理

## 使用方式

```
/deploy-config [环境]
```

## 示例

```
/deploy-config 开发环境
/deploy-config 生产环境
```

## 输出文件

```
deploy/
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .env.example
└── github/
    └── workflows/
        └── ci.yml
```

## 部署检查清单

- [ ] Dockerfile 构建成功
- [ ] docker-compose 启动成功
- [ ] CI/CD 流水线运行正常
- [ ] 环境变量配置正确
- [ ] 数据库迁移脚本就绪