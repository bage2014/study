#!/bin/bash

# 后端服务启动脚本
# 检查8080端口是否被占用，如果占用则关闭

BACKEND_DIR="best-practice-dev-flow-backend"
PORT=8080

echo "=================================="
echo "  启动后端服务"
echo "=================================="

# 检查端口是否被占用
echo "检查端口 $PORT 是否被占用..."
PID=$(lsof -ti:$PORT)

if [ -n "$PID" ]; then
    echo "端口 $PORT 被进程 $PID 占用，正在关闭..."
    kill -9 $PID
    sleep 2
    
    # 再次检查是否关闭成功
    PID=$(lsof -ti:$PORT)
    if [ -n "$PID" ]; then
        echo "警告：无法关闭进程 $PID，可能需要手动处理"
    else
        echo "端口 $PORT 已释放"
    fi
else
    echo "端口 $PORT 未被占用"
fi

# 进入后端目录并启动服务
echo ""
echo "启动后端服务..."
cd "$BACKEND_DIR"

# 检查是否安装了依赖
if [ ! -d "target" ]; then
    echo "首次启动，正在编译项目..."
    mvn clean compile -q
fi

# 启动Spring Boot服务
mvn spring-boot:run