# 家庭族谱APP上线指南

## 推荐部署方案

根据预算和使用场景，推荐以下部署方案：

| 方案 | 域名 | 服务器 | 月成本 | 适用场景 |
|------|------|--------|--------|----------|
| **最低成本** | Dynu免费域名 | 阿里云轻量 | ~3元 | 个人学习、测试项目 |
| **性价比** | FreeDomain | 恒创科技香港 | ~20元 | 个人博客、小型网站 |
| **正规专业** | 阿里云.com | 阿里云轻量 | ~5元 | 国内正式业务 |

详细方案请参考：[域名与服务器调研报告](./HOSTING_RESEARCH.md)

**阿里云轻量一键部署**：使用 [一键部署脚本](../scripts/deploy-aliyun.sh)，全自动安装Docker、Java、Node.js、PostgreSQL并完成部署。

详细部署配置请参考：[阿里云部署配置](./ALIYUN_DEPLOY.md)

## 1. 系统要求

### 1.1 硬件要求
- **服务器**：至少2核4GB内存
- **存储空间**：至少20GB
- **网络**：稳定的网络连接

### 1.2 软件要求
- **Java**：JDK 21或更高版本
- **PostgreSQL**：15.0或更高版本
- **Nginx**：1.20或更高版本（用于前端部署）
- **Docker**：20.10或更高版本（可选，用于容器化部署）

## 2. 数据库配置

### 2.1 PostgreSQL安装
```bash
# Ubuntu/Debian
apt update && apt install postgresql postgresql-contrib

# CentOS/RHEL
dnf install postgresql-server postgresql-contrib
postgresql-setup --initdb
systemctl start postgresql
```

### 2.2 数据库创建
```bash
# 登录PostgreSQL
psql -U postgres

# 创建数据库
CREATE DATABASE family_tree;

# 创建用户
CREATE USER familytree WITH PASSWORD 'familytree123';

# 授予权限
GRANT ALL PRIVILEGES ON DATABASE family_tree TO familytree;

# 退出
\q
```

## 3. 后端部署

### 3.1 构建后端
```bash
cd backend
mvn clean package -DskipTests
```

### 3.2 配置环境变量
```bash
# 生产环境配置
export SPRING_PROFILES_ACTIVE=prod
export JWT_SECRET=your_secure_jwt_secret_key
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/family_tree
export SPRING_DATASOURCE_USERNAME=familytree
export SPRING_DATASOURCE_PASSWORD=familytree123
```

### 3.3 启动后端服务
```bash
# 直接启动
java -jar target/backend-1.0.0.jar

# 或使用systemd服务
# 创建文件: /etc/systemd/system/familytree-backend.service
[Unit]
Description=Family Tree Backend
After=network.target

[Service]
User=your_user
WorkingDirectory=/path/to/backend
Environment=SPRING_PROFILES_ACTIVE=prod
Environment=JWT_SECRET=your_secure_jwt_secret_key
Environment=SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/family_tree
Environment=SPRING_DATASOURCE_USERNAME=familytree
Environment=SPRING_DATASOURCE_PASSWORD=familytree123
ExecStart=/usr/bin/java -jar target/backend-1.0.0.jar
Restart=always

[Install]
WantedBy=multi-user.target

# 启动服务
systemctl daemon-reload
systemctl enable familytree-backend
systemctl start familytree-backend
```

## 4. 前端部署

### 4.1 构建前端
```bash
cd frontend/web
npm install
npm run build
```

### 4.2 Nginx配置
```nginx
# 创建文件: /etc/nginx/sites-available/familytree
server {
    listen 80;
    server_name your_domain.com;

    location / {
        root /path/to/frontend/web/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # HTTPS配置（可选）
    # listen 443 ssl;
    # ssl_certificate /path/to/certificate.crt;
    # ssl_certificate_key /path/to/private.key;
}

# 启用站点
ln -s /etc/nginx/sites-available/familytree /etc/nginx/sites-enabled/
systemctl restart nginx
```

## 5. Docker部署（可选）

### 5.1 构建和运行
```bash
# 在项目根目录执行
docker-compose up -d
```

### 5.2 配置说明
- **PostgreSQL**：自动创建数据库和用户
- **后端**：自动构建和启动
- **前端**：自动构建和启动
- **访问地址**：http://localhost (前端)，http://localhost:8080 (后端API)

## 6. 系统初始化

### 6.1 初始用户
系统启动后会自动创建以下初始用户：
- **管理员**：admin@example.com / password
- **测试用户**：user1@example.com / password, user2@example.com / password

### 6.2 初始数据
系统会自动创建测试家族和成员数据，用于演示和测试。

## 7. 监控和维护

### 7.1 健康检查
- **后端健康检查**：http://your_domain:8080/actuator/health
- **API文档**：http://your_domain:8080/swagger-ui.html

### 7.2 日志管理
- **后端日志**：应用启动目录下的logs文件夹
- **Nginx日志**：/var/log/nginx/

### 7.3 常见问题排查
| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 后端启动失败 | 数据库连接错误 | 检查数据库配置和连接状态 |
| 前端无法访问 | Nginx配置错误 | 检查Nginx配置和端口占用 |
| API调用失败 | JWT密钥不一致 | 确保环境变量中的JWT_SECRET正确 |
| 数据库连接超时 | 网络或数据库问题 | 检查数据库服务状态和网络连接 |

## 8. 安全配置

### 8.1 HTTPS配置
- 申请SSL证书（推荐使用Let's Encrypt）
- 配置Nginx使用HTTPS
- 更新前端API地址为HTTPS

### 8.2 安全最佳实践
- 定期更新JWT密钥
- 使用强密码策略
- 限制API访问频率
- 定期备份数据库
- 关闭不必要的端口和服务

## 9. 升级指南

### 9.1 后端升级
```bash
# 停止服务
systemctl stop familytree-backend

# 拉取最新代码
git pull

# 重新构建
mvn clean package -DskipTests

# 启动服务
systemctl start familytree-backend
```

### 9.2 前端升级
```bash
# 拉取最新代码
git pull

# 重新构建
npm install && npm run build

# 重启Nginx
systemctl restart nginx
```

## 10. 联系方式

- **技术支持**：your-email@example.com
- **系统文档**：http://your_domain:8080/swagger-ui.html
- **用户指南**：[用户指南](../user-guide/USER_GUIDE.md)

---

**注意**：本指南仅作为参考，实际部署时请根据具体环境进行调整。