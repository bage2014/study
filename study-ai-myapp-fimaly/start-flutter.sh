#!/bin/bash

# 启动Flutter服务脚本
echo "========================================"
echo "启动Flutter开发服务器"
echo "========================================"

# 检查是否在项目根目录
if [ ! -d "frontend/android" ]; then
    echo "错误: 请在项目根目录执行此脚本"
    exit 1
fi

# 启动Flutter开发服务器
echo "启动Flutter开发服务器..."
cd frontend/android && flutter run
