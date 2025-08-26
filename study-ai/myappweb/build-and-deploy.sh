#!/bin/bash

# 配置变量
IMAGE_NAME="myappweb"
IMAGE_TAG="latest"
REMOTE_USER="bage"
REMOTE_HOST="101.34.78.152"
REMOTE_PATH="/home/bage/docker-images"
CONTAINER_NAME="myappweb-container"

# 确保脚本在出错时退出并打印详细信息
set -euo pipefail

# 1. 在本地清理并构建项目（使用本地npm环境）
echo "正在本地清理项目..."
rm -rf dist node_modules || true

# 2. 安装依赖（使用本地npm环境，更稳定）
echo "正在本地安装依赖..."
npm install --legacy-peer-deps

# 3. 构建生产版本
echo "正在本地构建项目..."
npm run build

# 4. 构建Docker镜像（仅复制构建产物）
echo "正在构建Docker镜像..."
docker build -t $IMAGE_NAME:$IMAGE_TAG .

# 5. 保存镜像为tar文件
echo "正在保存Docker镜像..."
docker save -o $IMAGE_NAME-$IMAGE_TAG.tar $IMAGE_NAME:$IMAGE_TAG

# 6. 创建远程目录（如果不存在）
echo "正在准备远程目录..."
ssh -o StrictHostKeyChecking=no $REMOTE_USER@$REMOTE_HOST "mkdir -p $REMOTE_PATH"

# 7. 上传镜像到远程服务器
echo "正在上传Docker镜像到远程服务器..."
scp -o StrictHostKeyChecking=no $IMAGE_NAME-$IMAGE_TAG.tar $REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH/

# 8. 在远程服务器上加载和运行镜像
echo "正在远程服务器上部署镜像..."
ssh -o StrictHostKeyChecking=no $REMOTE_USER@$REMOTE_HOST << 'EOF'
  set -euo pipefail
  
  # 停止并删除旧容器
  if [ \$(docker ps -q -f name=myappweb-container) ]; then
    echo "停止旧容器..."
    docker stop myappweb-container
    echo "删除旧容器..."
    docker rm myappweb-container
  fi
  
  # 加载新镜像
  echo "加载新镜像..."
  docker load -i /home/bage/docker-images/myappweb-latest.tar
  
  # 运行新容器
  echo "运行新容器..."
  docker run -d --name myappweb-container -p 80:80 myappweb:latest
  
  # 清理临时文件
  echo "清理临时文件..."
  rm -f /home/bage/docker-images/myappweb-latest.tar
  
  echo "远程部署完成！"
EOF

# 9. 清理本地临时文件
echo "正在清理本地临时文件..."
rm -f $IMAGE_NAME-$IMAGE_TAG.tar

# 显示完成信息
echo "本地清理完成！"
echo "项目已成功打包并部署到远程服务器！"