#!/bin/bash

# 启动前端服务脚本
echo "========================================"
echo "启动Web前端服务"
echo "========================================"

# 检查是否在项目根目录
if [ ! -d "frontend/web" ]; then
    echo "错误: 请在项目根目录执行此脚本"
    exit 1
fi

# 启动Web前端服务
echo "启动Vue前端服务..."
cd frontend/web && npm run dev
