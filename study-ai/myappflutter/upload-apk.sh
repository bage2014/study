#!/bin/bash

# APK文件上传脚本
# 将本地APK文件上传到远程服务器

# 配置参数
LOCAL_APK_PATH="build/app/outputs/flutter-apk/app-debug.apk"
REMOTE_HOST="8.153.38.92"
REMOTE_USER="bage"
REMOTE_DIR="/home/bage/apks"

# 检查本地APK文件是否存在
if [ ! -f "$LOCAL_APK_PATH" ]; then
    echo "错误: 本地APK文件不存在: $LOCAL_APK_PATH"
    echo "请先构建APK文件: flutter build apk"
    exit 1
fi

# 获取APK文件名（带版本信息）
APK_FILENAME=$(basename "$LOCAL_APK_PATH")

# 添加时间戳到文件名，避免覆盖
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
NEW_APK_FILENAME="app-debug_${TIMESTAMP}.apk"

# 检查远程目录是否存在，如果不存在则创建
echo "检查远程目录是否存在..."
ssh ${REMOTE_USER}@${REMOTE_HOST} "mkdir -p ${REMOTE_DIR}"

# 上传APK文件
echo "正在上传APK文件到远程服务器..."
scp "$LOCAL_APK_PATH" ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/${NEW_APK_FILENAME}

# 检查上传是否成功
if [ $? -eq 0 ]; then
    echo "✅ APK文件上传成功!"
    echo "本地文件: $LOCAL_APK_PATH"
    echo "远程路径: ${REMOTE_DIR}/${NEW_APK_FILENAME}"
    echo "远程访问地址: http://${REMOTE_HOST}:8080/${NEW_APK_FILENAME}"
    
    # 可选：在远程服务器上设置文件权限
    echo "设置远程文件权限..."
    ssh ${REMOTE_USER}@${REMOTE_HOST} "chmod 644 ${REMOTE_DIR}/${NEW_APK_FILENAME}"
    
    # 显示远程目录文件列表
    echo "远程目录文件列表:"
    ssh ${REMOTE_USER}@${REMOTE_HOST} "ls -la ${REMOTE_DIR}/ | grep .apk"
else
    echo "❌ APK文件上传失败!"
    exit 1
fi