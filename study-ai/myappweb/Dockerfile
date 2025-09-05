# 多阶段构建：构建阶段
FROM node:18 AS builder

# 设置工作目录
WORKDIR /app

# 复制package文件
COPY package*.json ./

# 使用可靠的镜像源并安装依赖
RUN npm install --legacy-peer-deps

# 复制源代码
COPY . .

# 构建应用
RUN ./node_modules/.bin/vite build

# 多阶段构建：生产阶段
FROM nginx:alpine

# 复制构建产物
COPY --from=builder /app/dist /usr/share/nginx/html

# 创建nginx配置文件
RUN echo 'server { \
    listen 80; \
    server_name localhost; \
    \
    # 静态文件服务 \
    location / { \
        root /usr/share/nginx/html; \
        index index.html index.htm; \
        try_files \$uri \$uri/ /index.html; \
    } \
    \
    # API代理配置 \
    location /api { \
        proxy_pass http://host.docker.internal:8080; \
        proxy_set_header Host \$host; \
        proxy_set_header X-Real-IP \$remote_addr; \
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for; \
        proxy_set_header X-Forwarded-Proto \$scheme; \
    } \
}' > /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动nginx
CMD ["nginx", "-g", "daemon off;"]