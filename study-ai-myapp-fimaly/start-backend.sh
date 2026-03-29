#!/bin/bash

# 启动后端服务脚本
echo "========================================"
echo "启动后端服务"
echo "========================================"

# 检查是否在项目根目录
if [ ! -f "backend/pom.xml" ]; then
    echo "错误: 请在项目根目录执行此脚本"
    exit 1
fi

# 启动后端服务
echo "启动Spring Boot后端服务..."
cd backend && mvn spring-boot:run
