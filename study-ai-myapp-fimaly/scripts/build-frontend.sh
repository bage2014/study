#!/bin/bash

# 前端构建脚本
echo "开始构建前端应用..."

# 进入前端目录
cd "$(dirname "$0")/../frontend/web" || exit 1

# 安装依赖
npm install

if [ $? -eq 0 ]; then
    echo "依赖安装成功"
else
    echo "依赖安装失败！"
    exit 1
fi

# 执行构建
npm run build

if [ $? -eq 0 ]; then
    echo "前端构建成功！"
    echo "构建产物：$(ls -la dist/)"
else
    echo "前端构建失败！"
    exit 1
fi