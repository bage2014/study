# 第一阶段：使用更稳定的Node.js版本在本地构建
FROM node:18-alpine AS build-stage

# 增加Docker容器的内存限制
ENV NODE_OPTIONS="--max-old-space-size=4096"

# 设置工作目录
WORKDIR /app

# 配置npm镜像源和缓存
RUN npm config set registry https://registry.npmmirror.com && \
    npm config set cache /app/.npm-cache

# 复制package.json和package-lock.json
COPY package*.json ./

# 使用yarn代替npm进行安装（更稳定）
RUN npm install -g yarn && \
    yarn install --frozen-lockfile

# 复制项目源代码
COPY . .

# 构建生产版本
RUN yarn build

# 第二阶段：使用Nginx作为Web服务器
# 直接使用Nginx作为基础镜像
FROM nginx:stable-alpine

# 添加自定义Nginx配置以处理Vue路由
COPY <<'EOF' /etc/nginx/conf.d/default.conf
server {
    listen       80;
    listen  [::]:80;
    server_name  localhost;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
        try_files $uri $uri/ /index.html;
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }
}
EOF

# 复制本地构建的产物到Nginx的静态文件目录
COPY dist /usr/share/nginx/html

# 暴露80端口
EXPOSE 80

# 启动Nginx
CMD ["nginx", "-g", "daemon off;"]