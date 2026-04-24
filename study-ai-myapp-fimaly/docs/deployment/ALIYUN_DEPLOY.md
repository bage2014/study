# 阿里云轻量应用服务器部署配置

## 一、服务器购买

### 1.1 购买入口
1. 访问 [阿里云轻量应用服务器](https://www.aliyun.com/product/swas)
2. 选择"轻量应用服务器"
3. 选择地域（推荐选择离用户最近的地域）

### 1.2 推荐配置
| 配置项 | 推荐选择 |
|--------|----------|
| 地域 | 华北2（北京）或华东1（杭州） |
| 应用镜像 | Ubuntu 22.04 LTS |
| 套餐配置 | 2核2G/200M带宽/不限流量 |
| 带宽计费 | 包年包月 |

### 1.3 费用
- **首购价格**：38元/年（限时秒杀）或 68元/年（标准价）
- **续费价格**：约60-80元/年

---

## 二、远程连接

### 2.1 获取初始密码
1. 登录阿里云控制台
2. 进入轻量应用服务器页面
3. 点击目标服务器"管理"
4. 在"远程连接"中获取密码

### 2.2 SSH连接
```bash
ssh root@你的服务器IP地址
```

### 2.3 安全组配置
确保以下端口已开放：
| 端口 | 用途 |
|------|------|
| 22 | SSH远程连接 |
| 80 | HTTP访问 |
| 443 | HTTPS访问 |
| 8080 | 后端API服务 |
| 5432 | PostgreSQL（建议限制IP访问） |

---

## 三、环境安装

### 3.1 安装Docker
```bash
# 更新系统
apt update && apt upgrade -y

# 安装Docker
curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun

# 启动Docker
systemctl start docker
systemctl enable docker

# 安装Docker Compose
curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# 验证安装
docker --version
docker-compose --version
```

### 3.2 安装Node.js（前端构建用）
```bash
# 安装nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# 加载nvm
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

# 安装Node.js 18
nvm install 18
nvm use 18
nvm alias default 18

# 验证安装
node --version
npm --version
```

### 3.3 安装Java 21
```bash
# 添加 Azul Zulu 源
curl -fsSL https://cdn.azul.com/zulu/bin/zulu-repo_8.0.412-8.0.85-1_amd64.deb | gdebi --single-clueball -

# 安装JDK 21
apt install zulu21-jdk-headless -y

# 验证安装
java --version
```

---

## 四、项目部署

### 4.1 创建项目目录
```bash
mkdir -p /opt/familytree
cd /opt/familytree
```

### 4.2 克隆项目
```bash
# 如果使用Git
git clone https://github.com/yourusername/study-ai-myapp-fimaly.git .

# 或者手动上传项目文件到 /opt/familytree
```

### 4.3 构建后端
```bash
cd /opt/familytree/backend

# 安装Maven
apt install maven -y

# 构建（跳过测试）
mvn clean package -DskipTests

# 检查构建产物
ls -la target/*.jar
```

### 4.4 构建前端
```bash
cd /opt/familytree/frontend/web

# 安装依赖
npm install

# 构建生产版本
npm run build

# 检查构建产物
ls -la dist/
```

### 4.5 配置环境变量
```bash
# 创建环境变量文件
cat > /opt/familytree/.env << EOF
# 数据库配置
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/family_tree
SPRING_DATASOURCE_USERNAME=familytree
SPRING_DATASOURCE_PASSWORD=your_secure_password

# JWT配置
JWT_SECRET=your_very_secure_jwt_secret_key_at_least_32_characters

# PostgreSQL配置
POSTGRES_DB=family_tree
POSTGRES_USER=familytree
POSTGRES_PASSWORD=your_secure_password
EOF
```

---

## 五、数据库部署（Docker）

### 5.1 创建Docker网络
```bash
docker network create familytree-network
```

### 5.2 启动PostgreSQL
```bash
docker run -d \
  --name familytree-postgres \
  --network familytree-network \
  -e POSTGRES_DB=family_tree \
  -e POSTGRES_USER=familytree \
  -e POSTGRES_PASSWORD=your_secure_password \
  -e POSTGRES_INITDB_ARGS="-E UTF8" \
  -v /opt/familytree/postgres-data:/var/lib/postgresql/data \
  -p 5432:5432 \
  --restart unless-stopped \
  postgres:15-alpine
```

### 5.3 验证数据库
```bash
# 等待数据库启动
sleep 10

# 测试连接
docker exec -it familytree-postgres psql -U familytree -d family_tree -c "SELECT version();"
```

---

## 六、后端部署（Docker）

### 6.1 创建后端Dockerfile
```bash
cat > /opt/familytree/backend/Dockerfile << EOF
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/backend-1.0.0.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/family_tree
ENV SPRING_DATASOURCE_USERNAME=familytree
ENV SPRING_DATASOURCE_PASSWORD=your_secure_password
ENV JWT_SECRET=your_very_secure_jwt_secret_key_at_least_32_characters

ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
```

### 6.2 构建后端镜像
```bash
cd /opt/familytree/backend
docker build -t familytree-backend:latest .
```

### 6.3 启动后端
```bash
docker run -d \
  --name familytree-backend \
  --network familytree-network \
  -p 8080:8080 \
  -v /opt/familytree/logs:/app/logs \
  --restart unless-stopped \
  familytree-backend:latest
```

### 6.4 验证后端
```bash
# 查看日志
docker logs -f familytree-backend

# 测试API
curl http://localhost:8080/actuator/health
```

---

## 七、前端部署（Nginx）

### 7.1 安装Nginx
```bash
apt install nginx -y

# 启动Nginx
systemctl start nginx
systemctl enable nginx
```

### 7.2 配置Nginx
```bash
cat > /etc/nginx/sites-available/familytree << EOF
server {
    listen 80;
    server_name your-domain.com;  # 替换为你的域名

    # 前端静态文件
    root /opt/familytree/frontend/dist;
    index index.html;

    # Gzip压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;
    gzip_min_length 1000;

    # 前端路由支持
    location / {
        try_files \$uri \$uri/ /index.html;
    }

    # API代理
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    # Swagger文档
    location /swagger-ui/ {
        proxy_pass http://localhost:8080/swagger-ui/;
        proxy_set_header Host \$host;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 30d;
        add_header Cache-Control "public, immutable";
    }

    # 安全头
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
}
EOF

# 启用配置
ln -sf /etc/nginx/sites-available/familytree /etc/nginx/sites-enabled/

# 测试配置
nginx -t

# 重载Nginx
systemctl reload nginx
```

### 7.3 上传前端文件
```bash
# 复制前端构建产物
cp -r /opt/familytree/frontend/web/dist/* /opt/familytree/frontend/

# 设置权限
chown -R www-data:www-data /opt/familytree/frontend
```

---

## 八、SSL证书配置（可选但强烈推荐）

### 8.1 安装Certbot
```bash
apt install certbot python3-certbot-nginx -y
```

### 8.2 获取SSL证书
```bash
certbot --nginx -d your-domain.com
```

### 8.3 自动续期
```bash
# 测试自动续期
certbot renew --dry-run

# 设置定时任务
crontab -e
# 添加以下行：
# 0 0 * * * certbot renew --quiet
```

---

## 九、域名解析配置

### 9.1 阿里云DNS配置
1. 登录阿里云DNS控制台
2. 添加域名解析：
   | 记录类型 | 主机记录 | 记录值 |
   |----------|----------|--------|
   | A | @ | 你的服务器IP |
   | A | www | 你的服务器IP |

### 9.2 验证解析
```bash
# Windows
nslookup your-domain.com

# Linux/Mac
dig your-domain.com
```

---

## 十、防火墙配置

### 10.1 UFW防火墙
```bash
# 安装UFW
apt install ufw -y

# 配置规则
ufw allow 22/tcp
ufw allow 80/tcp
ufw allow 443/tcp
ufw allow 8080/tcp

# 启用防火墙
ufw enable

# 查看状态
ufw status
```

---

## 十一、监控和日志

### 11.1 Docker日志
```bash
# 查看后端日志
docker logs -f familytree-backend --tail 100

# 查看数据库日志
docker logs -f familytree-postgres --tail 100
```

### 11.2 Nginx日志
```bash
# 查看访问日志
tail -f /var/log/nginx/access.log

# 查看错误日志
tail -f /var/log/nginx/error.log
```

---

## 十二、服务管理

### 12.1 创建systemd服务（后端）
```bash
cat > /etc/systemd/system/familytree-backend.service << EOF
[Unit]
Description=Family Tree Backend Service
After=network.target docker.service
Requires=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/opt/familytree/backend
ExecStart=/usr/bin/docker start familytree-backend
ExecStop=/usr/bin/docker stop familytree-backend
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable familytree-backend.service
```

### 12.2 服务命令
```bash
# 后端服务
systemctl start familytree-backend
systemctl stop familytree-backend
systemctl restart familytree-backend
systemctl status familytree-backend

# 查看所有容器状态
docker ps -a
```

---

## 十三、备份策略

### 13.1 数据库备份脚本
```bash
cat > /opt/familytree/backup-db.sh << EOF
#!/bin/bash
DATE=\$(date +%Y%m%d_%H%M%S)
BACKUP_DIR=/opt/familytree/backups
mkdir -p \$BACKUP_DIR

# 备份数据库
docker exec familytree-postgres pg_dump -U familytree family_tree > \$BACKUP_DIR/familytree_\$DATE.sql

# 保留最近7天的备份
find \$BACKUP_DIR -name "familytree_*.sql" -mtime +7 -delete

echo "Database backup completed: familytree_\$DATE.sql"
EOF

chmod +x /opt/familytree/backup-db.sh

# 添加定时任务
crontab -e
# 添加以下行：
# 0 2 * * * /opt/familytree/backup-db.sh
```

---

## 十四、更新部署

### 14.1 更新后端
```bash
cd /opt/familytree/backend
git pull
mvn clean package -DskipTests
docker build -t familytree-backend:latest .
docker stop familytree-backend
docker rm familytree-backend
docker run -d \
  --name familytree-backend \
  --network familytree-network \
  -p 8080:8080 \
  --restart unless-stopped \
  familytree-backend:latest
```

### 14.2 更新前端
```bash
cd /opt/familytree/frontend/web
git pull
npm install
npm run build
cp -r dist/* /opt/familytree/frontend/
systemctl reload nginx
```

---

## 十五、故障排查

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 后端启动失败 | 数据库连接错误 | 检查数据库配置和环境变量 |
| 前端无法访问 | Nginx配置错误 | 检查Nginx配置和日志 |
| API调用失败 | JWT密钥不一致 | 确保环境变量中的JWT_SECRET正确 |
| 数据库连接超时 | 网络或数据库问题 | 检查数据库服务状态和防火墙 |
| SSL证书失败 | 域名未解析 | 确保域名已正确解析到服务器IP |