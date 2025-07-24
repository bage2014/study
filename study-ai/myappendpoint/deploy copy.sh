#!/bin/bash

# 配置远程服务器信息 - 请修改以下参数
REMOTE_USER="bage"
REMOTE_HOST="101.34.78.152"
REMOTE_PATH="/home/bage/jars"

# 配置项目信息
PROJECT_NAME="myappendpoint"
JAR_FILE="target/${PROJECT_NAME}-0.0.1-SNAPSHOT.jar"

# 步骤1: 打包项目为Jar文件
echo "[1/2] 开始打包项目..."
./mvnw clean package -DskipTests

# 检查打包是否成功
if [ ! -f "${JAR_FILE}" ]; then
  echo "错误: Jar包打包失败，文件不存在: ${JAR_FILE}"
  exit 1
fi

# 步骤2: 上传到远程服务器
echo "[2/2] 开始上传到远程服务器..."
scp ${JAR_FILE} ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}

# 检查上传是否成功
if [ $? -eq 0 ]; then
  echo "成功: Jar包已上传至 ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}"
  exit 0
else
  echo "错误: Jar包上传失败"
  exit 1
fi