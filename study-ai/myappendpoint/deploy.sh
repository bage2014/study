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

# 配置项目信息
PROJECT_NAME="myappendpoint"
JAR_FILE="target/${PROJECT_NAME}-0.0.1-SNAPSHOT.jar"

# 配置重试参数
MAX_RETRIES=3
RETRY_DELAY=5

# 步骤1: 打包项目为Jar文件
echo "[1/4] 开始打包项目..."
./mvnw clean package -DskipTests

# 检查打包是否成功
if [ ! -f "${JAR_FILE}" ]; then
  echo "错误: Jar包打包失败，文件不存在: ${JAR_FILE}"
  exit 1
fi

echo "成功: Jar包已打包完成"

# 步骤2: 构建Docker镜像
echo "[2/4] 开始构建Docker镜像...(基于JDK 21)"
# 确保Dockerfile使用JDK 21
if grep -q "openjdk:21" Dockerfile; then
  echo "Dockerfile已使用JDK 21"
else
  echo "警告: Dockerfile未使用JDK 21，尝试修改..."
  sed -i '' 's|openjdk:[0-9][0-9]*|openjdk:21|g' Dockerfile
  echo "成功: 已修改Dockerfile使用JDK 21"
fi

docker build  -t ${DOCKER_IMAGE} .

# 检查Docker镜像构建是否成功
if [ $? -ne 0 ]; then
  echo "错误: Docker镜像构建失败"
  exit 1
fi

echo "成功: Docker镜像已构建完成: ${DOCKER_IMAGE}"

# 步骤3: 导出Docker镜像
echo "[3/4] 开始导出Docker镜像..."
docker save -o ${DOCKER_TAR_FILE} ${DOCKER_IMAGE}

# 检查导出是否成功
if [ ! -f "${DOCKER_TAR_FILE}" ]; then
  echo "错误: Docker镜像导出失败，文件不存在: ${DOCKER_TAR_FILE}"
  exit 1
fi

echo "成功: Docker镜像已导出到: ${DOCKER_TAR_FILE}"

# 步骤4: 上传镜像压缩文件到远程主机
echo "[4/4] 开始上传镜像压缩文件到远程主机..."

# 上传文件
retry_count=0
while [ $retry_count -lt $MAX_RETRIES ]; do
  echo "上传文件 (${retry_count+1}/${MAX_RETRIES})..."
  scp ${DOCKER_TAR_FILE} ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/${DOCKER_TAR_FILE}
  if [ $? -eq 0 ]; then
    echo "成功: 镜像压缩文件已上传至 ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}"
    # 清理临时文件
    rm -f ${DOCKER_TAR_FILE}
    echo "成功: 已清理临时文件"
    exit 0
  else
    echo "警告: 上传失败，${RETRY_DELAY}秒后重试..."
    sleep $RETRY_DELAY
    retry_count=$((retry_count+1))
  fi
done

# 达到最大重试次数
echo "错误: 上传失败，已达到最大重试次数"
echo "提示: 请检查以下内容:"
echo "1. 网络连接是否正常"
echo "2. 远程服务器的${REMOTE_PATH}路径是否存在"
echo "3. 您是否有权限访问远程服务器"
exit 1