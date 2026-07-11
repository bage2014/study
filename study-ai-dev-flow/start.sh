#!/bin/bash

# 停止旧服务
lsof -ti:8080 | xargs kill -9 2>/dev/null
lsof -ti:8081 | xargs kill -9 2>/dev/null

# 启动 Temporal
docker rm -f temporal 2>/dev/null
docker run -d --name temporal -p 7233:7233 -p 8233:8233 temporalio/temporal:latest server start-dev --ip 0.0.0.0

# 等待 Temporal 启动
sleep 10

# 构建并启动应用
cd "$(dirname "$0")"
mvn clean package -DskipTests -q
cd ai-pipeline-api
java -jar target/ai-pipeline-api-1.0.0-SNAPSHOT.jar
