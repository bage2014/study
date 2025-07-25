

#!/bin/bash

# 配置远程服务器信息 - 请修改以下参数
REMOTE_USER="bage"
REMOTE_HOST="101.34.78.152"
REMOTE_PATH="/home/bage/docker-images"

# 配置Docker信息
DOCKER_IMAGE_NAME="myappendpoint"
# 使用当前日期作为版本号 (格式: YYYYMMDD)
DOCKER_IMAGE_TAG=$(date +"%Y%m%d")
DOCKER_IMAGE="${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
DOCKER_TAR_FILE="${DOCKER_IMAGE_NAME}-${DOCKER_IMAGE_TAG}.tar"

# 配置容器运行参数
CONTAINER_NAME="myappendpoint"
# 不是8000端口，这里使用8081端口
HOST_PORT="8000"
CONTAINER_PORT="8080"

# 配置重试参数
MAX_RETRIES=3
RETRY_DELAY=5

# 步骤1: 远程连接到远程主机并执行操作
 echo "[1/3] 开始远程连接到 ${REMOTE_USER}@${REMOTE_HOST}..."

# 远程执行命令
ssh ${REMOTE_USER}@${REMOTE_HOST} << EOF
  # 步骤2: 导入Docker镜像
  echo "[2/3] 开始导入Docker镜像..."
  cd ${REMOTE_PATH}
  
  # 检查镜像文件是否存在
  if [ ! -f "${DOCKER_TAR_FILE}" ]; then
    echo "错误: 镜像文件不存在: ${DOCKER_TAR_FILE}"
    exit 1
  fi
  
  # 导入镜像
  docker load -i ${DOCKER_TAR_FILE}
  
  if [ $? -ne 0 ]; then
    echo "错误: 镜像导入失败"
    exit 1
  fi
  
  echo "成功: 镜像已导入: ${DOCKER_IMAGE}"
  
  # 步骤3: 启动Docker容器
  echo "[3/3] 开始启动Docker容器..."
  
  # 检查容器是否已存在，如果存在则停止并删除
  if [ $(docker ps -a -q -f name=${CONTAINER_NAME}) ]; then
    echo "警告: 容器 ${CONTAINER_NAME} 已存在，正在停止并删除..."
    docker stop ${CONTAINER_NAME}
    docker rm ${CONTAINER_NAME}
  fi
  
  # 启动容器，映射到非8000端口
  docker run -d -p ${HOST_PORT}:${CONTAINER_PORT} --name ${CONTAINER_NAME} ${DOCKER_IMAGE}
  
  if [ $? -ne 0 ]; then
    echo "错误: 容器启动失败"
    exit 1
  fi
  
  echo "成功: 容器已启动，访问端口: ${HOST_PORT}"
  echo "服务已成功部署到 http://${REMOTE_HOST}:${HOST_PORT}"
EOF

# 检查远程执行是否成功
if [ $? -ne 0 ]; then
  echo "错误: 远程执行失败"
  echo "提示: 请检查以下内容:"
  echo "1. 网络连接是否正常"
  echo "2. 您是否有权限访问远程服务器"
  echo "3. 远程服务器上的Docker是否已安装并运行"
  exit 1
fi

 echo "成功: 所有操作已完成"

