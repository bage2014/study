#!/bin/bash

# 配置远程服务器信息 - 请修改以下参数
REMOTE_USER="bage"
REMOTE_HOST=""

# 配置Docker信息 - 请修改以下参数
DOCKER_REPO="bage2014"
DOCKER_IMAGE_NAME="myappendpoint"
DOCKER_IMAGE_TAG="latest"

# 配置项目信息
PROJECT_NAME="myappendpoint"
JAR_FILE="target/${PROJECT_NAME}-0.0.1-SNAPSHOT.jar"

# 步骤1: 打包项目为Jar文件
echo "[1/3] 开始打包项目..."
./mvnw clean package -DskipTests

# 检查打包是否成功
if [ ! -f "${JAR_FILE}" ]; then
  echo "错误: Jar包打包失败，文件不存在: ${JAR_FILE}"
  exit 1
fi

echo "成功: Jar包已打包完成"

# 步骤2: 打包成Docker镜像
echo "[2/3] 开始构建Docker镜像..."
docker build -t ${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .

# 检查Docker镜像构建是否成功
if [ $? -ne 0 ]; then
  echo "错误: Docker镜像构建失败"
  exit 1
fi

echo "成功: Docker镜像已构建完成: ${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"

# 步骤3: 上传到远程Docker服务
echo "[3/3] 开始上传Docker镜像到远程服务..."

docker push ${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}

# 检查上传是否成功
if [ $? -eq 0 ]; then
  echo "成功: Docker镜像已上传至远程服务"
  # 清理环境变量
  exit 0
else
  echo "错误: Docker镜像上传失败"
  # 清理环境变量
  exit 1
fi