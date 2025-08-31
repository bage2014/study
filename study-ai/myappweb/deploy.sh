#!/bin/bash

# 设置变量
APP_NAME="myappweb"
IMAGE_NAME="$APP_NAME:latest"
CONTAINER_NAME="$APP_NAME-container"
HOST_PORT="80"

# 输出颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    local color=$1
    local message=$2
    echo -e "${color}[$(date '+%Y-%m-%d %H:%M:%S')] ${message}${NC}"
}

# 1. 打包项目
print_message $YELLOW "开始打包项目..."
npm run build
if [ $? -ne 0 ]; then
    print_message $RED "项目打包失败！"
    exit 1
fi
print_message $GREEN "项目打包完成！"

# 2. 构建Docker镜像
print_message $YELLOW "开始构建Docker镜像..."
docker build -t $IMAGE_NAME .
if [ $? -ne 0 ]; then
    print_message $RED "Docker镜像构建失败！"
    exit 1
fi
print_message $GREEN "Docker镜像构建完成！"

# 3. 停止并删除旧容器（如果存在）
if [ $(docker ps -q -f name=$CONTAINER_NAME) ]; then
    print_message $YELLOW "停止现有容器..."
    docker stop $CONTAINER_NAME
    print_message $YELLOW "删除现有容器..."
    docker rm $CONTAINER_NAME
fi

# 4. 运行新容器
print_message $YELLOW "启动新容器..."
docker run -d \
    --name $CONTAINER_NAME \
    -p $HOST_PORT:80 \
    --restart unless-stopped \
    $IMAGE_NAME

if [ $? -ne 0 ]; then
    print_message $RED "容器启动失败！"
    exit 1
fi

print_message $GREEN "部署成功！应用已运行在 http://localhost:$HOST_PORT"