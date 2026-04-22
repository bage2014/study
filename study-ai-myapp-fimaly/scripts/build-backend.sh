#!/bin/bash

# 后端构建脚本
echo "开始构建后端应用..."

# 进入后端目录
cd "$(dirname "$0")/../backend" || exit 1

# 清理之前的构建
mvn clean

# 执行构建
mvn package -DskipTests

if [ $? -eq 0 ]; then
    echo "后端构建成功！"
    echo "构建产物：$(ls target/*.jar)"
else
    echo "后端构建失败！"
    exit 1
fi