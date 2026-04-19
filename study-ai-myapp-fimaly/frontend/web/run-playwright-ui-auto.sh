#!/bin/bash

# 运行Playwright UI模式并自动执行所有测试

# 切换到脚本所在目录
cd "$(dirname "$0")"

# 启动Playwright UI模式，使用--ui-host参数但不指定端口（使用随机端口）
echo "Starting Playwright UI mode..."

# 启动Playwright UI在后台
npx playwright test --ui --ui-host localhost &

# 保存进程ID
PLAYWRIGHT_PID=$!

echo "Playwright UI started with PID: $PLAYWRIGHT_PID"
echo "UI will open in your default browser automatically"
echo ""
echo "=== Instructions ==="
echo "1. Playwright UI will open in your browser"
echo "2. Click the 'Run all tests' button to start testing"
echo "3. You can observe the test execution in real-time"
echo "4. Press Ctrl+C in this terminal to stop the Playwright UI"
echo ""
echo "Waiting for Playwright UI to start..."

# 等待用户终止
wait $PLAYWRIGHT_PID
