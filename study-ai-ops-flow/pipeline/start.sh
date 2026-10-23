#!/bin/bash

set -e

PROJECT_DIR=$(cd "$(dirname "$0")" && pwd)

echo "=== AI Ops Platform 启动脚本 ==="
echo ""

echo "1. 停止旧服务..."
lsof -ti:8080 | xargs kill -9 2>/dev/null || true
lsof -ti:8081 | xargs kill -9 2>/dev/null || true
lsof -ti:8082 | xargs kill -9 2>/dev/null || true
lsof -ti:8083 | xargs kill -9 2>/dev/null || true

echo "2. 启动 Temporal 服务..."
docker compose up -d

echo "3. 等待 Temporal 启动..."
sleep 15

echo "4. 构建项目..."
cd "$PROJECT_DIR"
mvn clean package -DskipTests -q

echo "5. 加载环境变量..."
if [ -f "$PROJECT_DIR/.env" ]; then
    echo "   加载 .env 文件..."
    export $(cat "$PROJECT_DIR/.env" | grep -v '^#' | xargs)
fi

echo "6. 启动 ai-ops-api (端口 8080)..."
cd "$PROJECT_DIR/pipeline/ai-ops-api"
nohup java -jar target/ai-ops-api-1.0.0-SNAPSHOT-exec.jar > /tmp/ops-api.log 2>&1 &
echo "   PID: $!"
sleep 8

echo "7. 启动 ai-ops-gateway (端口 8082)..."
cd "$PROJECT_DIR/pipeline/ai-ops-gateway"
nohup java -jar target/ai-ops-gateway-1.0.0-SNAPSHOT.jar > /tmp/ops-gateway.log 2>&1 &
echo "   PID: $!"
sleep 5

echo "8. 启动 ai-ops-ui (端口 8081)..."
cd "$PROJECT_DIR/pipeline/ai-ops-ui"
nohup bash start.sh > /tmp/ops-ui.log 2>&1 &
echo "   PID: $!"
sleep 5

echo ""
echo "=== 服务启动完成 ==="
echo ""
echo "服务地址:"
echo "  - Vue UI:         http://localhost:8081"
echo "  - Gateway API:    http://localhost:8083"
echo "  - Core API:       http://localhost:8080"
echo "  - Temporal UI:    http://localhost:8233"
echo ""
echo "日志文件:"
echo "  - API:            /tmp/ops-api.log"
echo "  - Gateway:        /tmp/ops-gateway.log"
echo "  - UI:             /tmp/ops-ui.log"
echo ""
echo "验证服务:"
echo "  curl http://localhost:8083/api/pipeline"
