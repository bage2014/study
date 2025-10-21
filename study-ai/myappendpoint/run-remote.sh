#!/bin/bash

# 配置区域 - 请修改以下参数
REMOTE_USER="bage"
REMOTE_HOST="8.153.38.92"

REMOTE_PATH="/home/bage/docker-images"
DOCKER_IMAGE_NAME="myappendpoint"
PROJECT_NAME="myappendpoint"
JAR_FILE="target/${PROJECT_NAME}-0.0.1-SNAPSHOT.jar"
CONTAINER_NAME="myappendpoint"
HOST_PORT="8000"
CONTAINER_PORT="8080"
MAX_RETRIES=3
RETRY_DELAY=5

# 生成版本号(YYYYMMDD)
DOCKER_IMAGE_TAG=$(date +"%Y%m%d")
DOCKER_IMAGE="${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
DOCKER_TAR_FILE="${DOCKER_IMAGE_NAME}-${DOCKER_IMAGE_TAG}.tar"

# 清理旧镜像文件
cleanup() {
  if [ -f "${DOCKER_TAR_FILE}" ]; then
    echo "清理本地临时文件: ${DOCKER_TAR_FILE}"
    rm -f "${DOCKER_TAR_FILE}"
  fi
}

trap cleanup EXIT

# 步骤1: 构建项目
echo "[1/7] 开始构建项目..."
if ! mvn clean package -DskipTests; then
  echo "错误: 项目构建失败"
exit 1
fi

# 检查JAR文件是否存在
echo "[2/7] 验证构建结果..."
if [ ! -f "${JAR_FILE}" ]; then
  echo "错误: JAR文件不存在: ${JAR_FILE}"
exit 1
fi

# 步骤2: 构建Docker镜像
echo "[3/7] 构建Docker镜像..."
if ! docker build -t "${DOCKER_IMAGE}" .; then
  echo "错误: Docker镜像构建失败"
exit 1
fi

# 步骤3: 导出Docker镜像
echo "[4/7] 导出Docker镜像..."
if ! docker save -o "${DOCKER_TAR_FILE}" "${DOCKER_IMAGE}"; then
  echo "错误: Docker镜像导出失败"
exit 1
fi

# 步骤4: 建立SSH控制连接(优化密码输入)
echo "[5/7] 建立SSH连接..."
CONTROL_PATH="~/.ssh/control-%r@%h:%p"
ssh -M -f -N -o ControlPath="${CONTROL_PATH}" -o ConnectTimeout=10 "${REMOTE_USER}@${REMOTE_HOST}"
if [ $? -ne 0 ]; then
  echo "错误: SSH控制连接建立失败"
exit 1
fi

# 步骤5: 上传镜像文件
echo "[6/7] 上传镜像文件..."
retry_count=0
success=0
while [ $retry_count -lt $MAX_RETRIES ]; do
  if scp -o ControlPath="${CONTROL_PATH}" "${DOCKER_TAR_FILE}" "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/"; then
    success=1
    break
  fi
  retry_count=$((retry_count + 1))
  echo "上传失败，重试 ${retry_count}/${MAX_RETRIES}..."
  sleep $RETRY_DELAY
done

if [ $success -ne 1 ]; then
  echo "错误: 镜像文件上传失败"
  exit 1
fi

# 步骤6: 远程部署
echo "[7/7] 远程部署容器..."
ssh -o ControlPath="${CONTROL_PATH}" "${REMOTE_USER}@${REMOTE_HOST}" << EOF
  # 导入镜像
  docker load -i "${REMOTE_PATH}/${DOCKER_TAR_FILE}"

  # 停止并删除旧容器
  if [ \$(docker ps -a --filter name=^/${CONTAINER_NAME}\$ --format '{{.Names}}') = "${CONTAINER_NAME}" ]; then
    docker stop "${CONTAINER_NAME}"
    docker rm "${CONTAINER_NAME}"
  fi

  # 启动新容器
  docker run -d -p ${HOST_PORT}:${CONTAINER_PORT} --name "${CONTAINER_NAME}" \
    -e APP_UPDATE_FILE_DIR=/data/uploads \
    -v /home/bage/uploads:/data/uploads \
    "${DOCKER_IMAGE}"

  # 清理远程文件
  rm -f "${REMOTE_PATH}/${DOCKER_TAR_FILE}"
EOF

# 关闭SSH控制连接
ssh -o ControlPath="${CONTROL_PATH}" -O exit "${REMOTE_USER}@${REMOTE_HOST}"

# 验证部署结果
echo "部署完成! 服务已启动在 http://${REMOTE_HOST}:${HOST_PORT}"