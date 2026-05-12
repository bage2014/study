#!/bin/bash

# 前端服务启动脚本
# 检查5173端口是否被占用，如果占用则关闭

FRONTEND_DIR="best-practice-dev-flow-ui"
PORT=5173

echo "=================================="
echo "  启动前端服务"
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

# 进入前端目录并启动服务
echo ""
echo "启动前端服务..."
cd "$FRONTEND_DIR"

# 检查是否安装了依赖
if [ ! -d "node_modules" ]; then
    echo "首次启动，正在安装依赖..."
    npm install -q
fi

# 启动Vite开发服务器
npm run dev -- --host 0.0.0.0 --port $PORT