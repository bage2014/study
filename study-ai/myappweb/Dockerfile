# 多阶段构建
# 第一阶段：构建应用
FROM node:18 AS builder  

# 改为完整Node.js镜像，非Alpine

# 设置工作目录
WORKDIR /app

# 复制package.json和package-lock.json
COPY package*.json ./

# 清理缓存并安装依赖
RUN npm cache clean --force && \
    npm install --include=dev --legacy-peer-deps

# 验证vite是否可用
RUN npx vite --version

# 复制源代码
COPY . .

# 使用npx运行vite构建
RUN npx vite build

# 第二阶段：生产环境
FROM nginx:alpine

# 复制构建产物到nginx默认目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 创建nginx配置文件
RUN echo 'server { \
    listen 80; \
    server_name localhost; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html index.htm; \
        try_files $uri $uri/ /index.html; \
    } \
    location /api { \
        proxy_pass http://host.docker.internal:8080; \
        proxy_set_header Host $host; \
        proxy_set_header X-Real-IP $remote_addr; \
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; \
        proxy_set_header X-Forwarded-Proto $scheme; \
    } \
}' > /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动nginx
CMD ["nginx", "-g", "daemon off;"]