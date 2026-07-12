#!/bin/bash

echo "=== AI Pipeline UI 启动脚本 ==="
echo ""

echo "[1/2] 关闭端口 8081 占用..."
lsof -ti:8081 | xargs kill -9 2>/dev/null || true
echo "      ✓ 端口清理完成"

echo "[2/2] 启动前端开发服务器..."
cd "$(dirname "$0")"
npm run dev