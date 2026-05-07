#!/bin/bash

#================================================================
# 家庭族谱APP - 阿里云一键部署脚本
# 适用于阿里云轻量应用服务器（Ubuntu 22.04）
# 使用MySQL数据库
#================================================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 配置变量
PROJECT_DIR="/opt/familytree"
DB_PASSWORD="Familytree@2025"
JWT_SECRET="FamilytreeSecretKey2025AtLeast32Characters"
DOMAIN_NAME=""

# 打印函数
print_header() {
    echo -e "\n${GREEN}========================================${NC}"
    echo -e "${GREEN}$1${NC}"
    echo -e "${GREEN}========================================${NC}\n"
}

print_step() {
    echo -e "${YELLOW}[$1]${NC} $2"
}

print_success() {
    echo -e "${GREEN}[✓]${NC} $1"
}

print_error() {
    echo -e "${RED}[✗]${NC} $1"
}

# 检查root权限
check_root() {
    if [ "$EUID" -ne 0 ]; then
        print_error "请使用root权限运行此脚本"
        echo "运行: sudo bash $0"
        exit 1
    fi
}

# 获取服务器IP
get_server_ip() {
    SERVER_IP=$(curl -s ifconfig.me)
    echo "服务器IP地址: $SERVER_IP"
}

# 更新系统
update_system() {
    print_step "1" "更新系统软件包..."
    apt update && apt upgrade -y
    print_success "系统更新完成"
}

# 安装Docker
install_docker() {
    print_step "2" "安装Docker和Docker Compose..."

    # 安装必要组件
    apt install -y apt-transport-https ca-certificates curl gnupg lsb-release

    # 添加Docker GPG密钥
    mkdir -p /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg

    # 添加Docker仓库
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

    # 安装Docker
    apt update
    apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

    # 启动Docker
    systemctl start docker
    systemctl enable docker

    print_success "Docker安装完成"
}

# 安装Node.js
install_nodejs() {
    print_step "3" "安装Node.js 18..."

    # 安装nvm
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

    # 加载nvm
    export NVM_DIR="$HOME/.nvm"
    [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

    # 安装Node.js
    nvm install 18
    nvm use 18
    nvm alias default 18

    print_success "Node.js安装完成: $(node --version)"
}

# 安装Java
install_java() {
    print_step "4" "安装Java 21..."

    # 安装Zulu JDK
    curl -fsSL https://cdn.azul.com/zulu/bin/zulu-repo_8.0.412-8.0.85-1_amd64.deb -o /tmp/zulu-repo.deb
    apt install -y /tmp/zulu-repo.deb
    apt install -y zulu21-jdk-headless

    print_success "Java安装完成: $(java --version)"
}

# 安装Maven
install_maven() {
    print_step "5" "安装Maven..."
    apt install -y maven
    print_success "Maven安装完成: $(mvn --version | head -n1)"
}

# 创建目录结构
create_directories() {
    print_step "6" "创建项目目录结构..."
    mkdir -p $PROJECT_DIR
    mkdir -p $PROJECT_DIR/mysql-data
    mkdir -p $PROJECT_DIR/logs
    mkdir -p $PROJECT_DIR/backups
    mkdir -p $PROJECT_DIR/frontend
    print_success "目录创建完成"
}

# 克隆项目
clone_project() {
    print_step "7" "请将项目文件复制到 $PROJECT_DIR"
    echo ""
    echo "方法1: Git克隆"
    echo "  cd $PROJECT_DIR"
    echo "  git clone https://github.com/yourusername/study-ai-myapp-fimaly.git ."
    echo ""
    echo "方法2: 手动上传"
    echo "  使用SCP/SFTP将项目文件上传到 $PROJECT_DIR"
    echo ""

    read -p "项目文件已准备好？(y/n): " -n 1 -r
    echo

    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_error "请先准备好项目文件"
        exit 1
    fi
}

# 构建后端
build_backend() {
    print_step "8" "构建后端应用..."

    if [ ! -d "$PROJECT_DIR/backend" ]; then
        print_error "后端目录不存在，请检查项目文件"
        exit 1
    fi

    cd $PROJECT_DIR/backend
    mvn clean package -DskipTests

    if [ -f "target/backend-1.0.0.jar" ]; then
        print_success "后端构建完成"
    else
        print_error "后端构建失败"
        exit 1
    fi
}

# 构建前端
build_frontend() {
    print_step "9" "构建前端应用..."

    if [ ! -d "$PROJECT_DIR/frontend/web" ]; then
        print_error "前端目录不存在，请检查项目文件"
        exit 1
    fi

    cd $PROJECT_DIR/frontend/web
    npm install
    npm run build

    if [ -d "dist" ]; then
        cp -r dist/* $PROJECT_DIR/frontend/
        print_success "前端构建完成"
    else
        print_error "前端构建失败"
        exit 1
    fi
}

# 启动MySQL数据库
start_database() {
    print_step "10" "启动MySQL数据库..."

    # 创建Docker网络
    docker network create familytree-network 2>/dev/null || true

    # 停止已有容器（如果存在）
    docker stop familytree-mysql 2>/dev/null || true
    docker rm familytree-mysql 2>/dev/null || true

    # 启动MySQL
    docker run -d \
        --name familytree-mysql \
        --network familytree-network \
        -e MYSQL_ROOT_PASSWORD=$DB_PASSWORD \
        -e MYSQL_DATABASE=family_tree \
        -e MYSQL_USER=familytree \
        -e MYSQL_PASSWORD=$DB_PASSWORD \
        -v $PROJECT_DIR/mysql-data:/var/lib/mysql \
        -p 3306:3306 \
        --restart unless-stopped \
        mysql:8.0 \
        --character-set-server=utf8mb4 \
        --collation-server=utf8mb4_unicode_ci \
        --default-time-zone=+08:00

    # 等待数据库启动
    sleep 15

    # 验证数据库
    if docker exec familytree-mysql mysqladmin ping -u familytree -p$DB_PASSWORD --silent; then
        print_success "数据库启动完成"
    else
        print_error "数据库启动失败"
        docker logs familytree-mysql
        exit 1
    fi
}

# 构建并启动后端
start_backend() {
    print_step "11" "启动后端服务..."

    cd $PROJECT_DIR/backend

    # 创建后端Dockerfile
    cat > Dockerfile << EOF
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/backend-1.0.0.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:mysql://familytree-mysql:3306/family_tree?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=utf8
ENV SPRING_DATASOURCE_USERNAME=familytree
ENV SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD
ENV JWT_SECRET=$JWT_SECRET

ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

    # 构建镜像
    docker build -t familytree-backend:latest .

    # 停止已有容器（如果存在）
    docker stop familytree-backend 2>/dev/null || true
    docker rm familytree-backend 2>/dev/null || true

    # 启动容器
    docker run -d \
        --name familytree-backend \
        --network familytree-network \
        -p 8080:8080 \
        -v $PROJECT_DIR/logs:/app/logs \
        --restart unless-stopped \
        familytree-backend:latest

    # 等待服务启动
    sleep 20

    # 验证服务
    if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
        print_success "后端服务启动完成"
    else
        print_error "后端服务可能未正常启动，请检查日志"
        docker logs familytree-backend
    fi
}

# 配置Nginx
setup_nginx() {
    print_step "12" "配置Nginx..."

    # 安装Nginx
    apt install -y nginx

    # 创建Nginx配置
    cat > /etc/nginx/sites-available/familytree << EOF
server {
    listen 80;
    server_name localhost;

    root $PROJECT_DIR/frontend;
    index index.html;

    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;
    gzip_min_length 1000;

    location / {
        try_files \$uri \$uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    location /swagger-ui/ {
        proxy_pass http://localhost:8080/swagger-ui/;
        proxy_set_header Host \$host;
    }

    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 30d;
        add_header Cache-Control "public, immutable";
    }

    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
}
EOF

    # 启用配置
    ln -sf /etc/nginx/sites-available/familytree /etc/nginx/sites-enabled/familytree
    rm -f /etc/nginx/sites-enabled/default

    # 测试并重载Nginx
    nginx -t && systemctl reload nginx

    # 设置权限
    chown -R www-data:www-data $PROJECT_DIR/frontend

    print_success "Nginx配置完成"
}

# 配置防火墙
setup_firewall() {
    print_step "13" "配置防火墙..."

    apt install -y ufw

    ufw allow 22/tcp
    ufw allow 80/tcp
    ufw allow 443/tcp
    ufw allow 8080/tcp
    ufw allow 3306/tcp

    echo "y" | ufw enable

    print_success "防火墙配置完成"
}

# 创建备份脚本
create_backup_script() {
    print_step "14" "创建备份脚本..."

    cat > $PROJECT_DIR/backup.sh << EOF
#!/bin/bash
DATE=\$(date +%Y%m%d_%H%M%S)
BACKUP_DIR=$PROJECT_DIR/backups
mkdir -p \$BACKUP_DIR

docker exec familytree-mysql mysqldump -u familytree -p$DB_PASSWORD family_tree > \$BACKUP_DIR/familytree_\$DATE.sql

find \$BACKUP_DIR -name "familytree_*.sql" -mtime +7 -delete

echo "Backup completed: familytree_\$DATE.sql"
EOF

    chmod +x $PROJECT_DIR/backup.sh

    # 添加定时任务
    (crontab -l 2>/dev/null; echo "0 2 * * * $PROJECT_DIR/backup.sh") | crontab -

    print_success "备份脚本创建完成"
}

# 打印部署信息
print_deployment_info() {
    SERVER_IP=$(curl -s ifconfig.me)

    print_header "部署完成！"

    echo -e "${GREEN}访问地址：${NC}"
    echo -e "  前端页面: http://$SERVER_IP"
    echo -e "  后端API:  http://$SERVER_IP:8080"
    echo -e "  API文档:  http://$SERVER_IP:8080/swagger-ui.html"
    echo ""
    echo -e "${GREEN}初始账号：${NC}"
    echo -e "  管理员: admin@example.com / password"
    echo -e "  测试用户: user1@example.com / password"
    echo ""
    echo -e "${GREEN}服务管理：${NC}"
    echo -e "  查看后端日志: docker logs -f familytree-backend"
    echo -e "  查看数据库日志: docker logs -f familytree-mysql"
    echo -e "  重启后端: docker restart familytree-backend"
    echo -e "  备份数据库: $PROJECT_DIR/backup.sh"
    echo ""
    echo -e "${YELLOW}后续步骤：${NC}"
    echo -e "  1. 配置域名解析，指向服务器IP"
    echo -e "  2. 申请SSL证书: certbot --nginx -d your-domain.com"
    echo -e "  3. 更新Nginx配置中的server_name为你的域名"
    echo ""
}

# 主函数
main() {
    clear
    print_header "家庭族谱APP - 阿里云一键部署"

    check_root
    get_server_ip

    echo "开始自动化部署..."
    echo ""

    update_system
    install_docker
    install_nodejs
    install_java
    install_maven
    create_directories
    clone_project
    build_backend
    build_frontend
    start_database
    start_backend
    setup_nginx
    setup_firewall
    create_backup_script

    print_deployment_info
}

# 执行主函数
main