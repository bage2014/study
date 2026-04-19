#!/bin/bash

# 运行Playwright UI模式并自动执行所有测试

# 启动Playwright UI
cd "$(dirname "$0")"
npx playwright test --ui &

# 等待Playwright UI启动
sleep 5

# 使用AppleScript模拟点击"Run all tests"按钮
osascript << 'EOF'
tell application "System Events"
    # 查找Playwright UI窗口
    repeat with w in windows of application process "Electron"
        if name of w contains "Playwright Test Runner" then
            # 查找并点击"Run all tests"按钮
            tell w
                try
                    # 尝试点击"Run all tests"按钮
                    click button "Run all tests" of group 1 of group 1
                on error
                    # 如果找不到按钮，尝试其他可能的位置
                    try
                        click button 1 of group 1 of group 1
                    end try
                end try
            end tell
            exit repeat
        end if
    end repeat
end tell
EOF

echo "Playwright UI started and tests are running automatically..."
echo "You can view the test results in the Playwright UI window."
