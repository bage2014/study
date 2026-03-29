#!/bin/bash

# 停止脚本 - 用于停止家庭族谱应用的所有服务

echo "========================================"
echo "家庭族谱应用停止脚本"
echo "========================================"

# 停止后端服务
echo "\n1. 停止后端服务..."
BACKEND_PID=$(ps aux | grep "spring-boot:run" | grep -v grep | awk '{print $2}')
if [ ! -z "$BACKEND_PID" ]; then
    kill $BACKEND_PID
    echo "后端服务已停止，PID: $BACKEND_PID"
else
    echo "后端服务未运行"
fi

# 停止Web前端服务
echo "\n2. 停止Web前端服务..."
FRONTEND_PID=$(ps aux | grep "npm run dev" | grep -v grep | awk '{print $2}')
if [ ! -z "$FRONTEND_PID" ]; then
    kill $FRONTEND_PID
    echo "Web前端服务已停止，PID: $FRONTEND_PID"
else
    echo "Web前端服务未运行"
fi

# 停止Flutter开发服务器
echo "\n3. 停止Flutter开发服务器..."
FLUTTER_PID=$(ps aux | grep "flutter run" | grep -v grep | awk '{print $2}')
if [ ! -z "$FLUTTER_PID" ]; then
    kill $FLUTTER_PID
    echo "Flutter开发服务器已停止，PID: $FLUTTER_PID"
else
    echo "Flutter开发服务器未运行"
fi

echo "\n========================================"
echo "所有服务已停止"
echo "========================================"
