#!/bin/bash

set -e

PROJECT_DIR=$(cd "$(dirname "$0")" && pwd)

echo "=== AI Pipeline Platform 启动脚本 ==="
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

echo "5. 启动 ai-pipeline-api (端口 8080)..."
cd "$PROJECT_DIR/ai-pipeline-api"
nohup java -jar target/ai-pipeline-api-1.0.0-SNAPSHOT.jar > /tmp/pipeline-api.log 2>&1 &
echo "   PID: $!"
sleep 8

echo "6. 启动 ai-pipeline-gateway (端口 8082)..."
cd "$PROJECT_DIR/ai-pipeline-gateway"
nohup java -jar target/ai-pipeline-gateway-1.0.0-SNAPSHOT.jar > /tmp/pipeline-gateway.log 2>&1 &
echo "   PID: $!"
sleep 5

echo "7. 启动 ai-pipeline-ui (端口 8081)..."
cd "$PROJECT_DIR/ai-pipeline-ui"
nohup bash start.sh > /tmp/pipeline-ui.log 2>&1 &
echo "   PID: $!"
sleep 5

echo ""
echo "=== 服务启动完成 ==="
echo ""
echo "服务地址:"
echo "  - Vue UI:         http://localhost:8081"
echo "  - Gateway API:    http://localhost:8082"
echo "  - Core API:       http://localhost:8080"
echo "  - Temporal UI:    http://localhost:8233"
echo ""
echo "日志文件:"
echo "  - API:            /tmp/pipeline-api.log"
echo "  - Gateway:        /tmp/pipeline-gateway.log"
echo "  - UI:             /tmp/pipeline-ui.log"
echo ""
echo "验证服务:"
echo "  curl http://localhost:8082/api/pipelines"
