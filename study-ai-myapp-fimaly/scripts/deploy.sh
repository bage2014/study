#!/bin/bash

# 部署脚本
echo "开始部署家庭族谱APP..."

# 进入脚本所在目录
cd "$(dirname "$0")" || exit 1

# 构建后端
./build-backend.sh
if [ $? -ne 0 ]; then
    echo "后端构建失败，部署中止！"
    exit 1
fi

# 构建前端
./build-frontend.sh
if [ $? -ne 0 ]; then
    echo "前端构建失败，部署中止！"
    exit 1
fi

echo "所有构建完成，准备部署..."

# 复制构建产物到部署目录
mkdir -p ../deploy/backend
mkdir -p ../deploy/frontend

# 复制后端jar包
cp ../backend/target/*.jar ../deploy/backend/

# 复制前端构建产物
cp -r ../frontend/web/dist/* ../deploy/frontend/

# 复制配置文件
cp -r ../backend/src/main/resources/application-*.properties ../deploy/backend/

# 复制数据库初始化脚本
cp -r ../backend/src/main/resources/db ../deploy/backend/

echo "部署文件准备完成！"
echo "部署目录：$(pwd)/../deploy/"
echo "
部署步骤：
1. 确保PostgreSQL数据库已安装并运行
2. 创建数据库：CREATE DATABASE family_tree;
3. 创建数据库用户：CREATE USER familytree WITH PASSWORD 'familytree123';
4. 授予权限：GRANT ALL PRIVILEGES ON DATABASE family_tree TO familytree;
5. 启动后端服务：java -jar deploy/backend/*.jar --spring.profiles.active=prod
6. 配置前端服务器（如Nginx）指向deploy/frontend/目录
7. 访问应用：http://localhost:8080 (后端API) 和 http://前端服务器地址 (前端页面)"

