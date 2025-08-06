#!/bin/bash

# 检查是否安装了Flutter
if ! command -v flutter &> /dev/null
then
    echo "错误: 未找到Flutter命令。请确保Flutter已安装并添加到PATH中。"
    exit 1
fi

# 列出所有可用的模拟器
echo "可用的模拟器列表:"
flutter emulators

# 提示用户选择模拟器
read -p "请输入要启动的模拟器ID (例如: Pixel_6_Pro_API_33): " emulator_id

# 验证模拟器ID是否为空
if [ -z "$emulator_id" ]
then
    echo "错误: 模拟器ID不能为空。"
    exit 1
fi

# 启动模拟器
echo "正在启动模拟器: $emulator_id..."
flutter emulators --launch $emulator_id

# 等待模拟器启动完成
echo "等待模拟器启动完成..."
sleep 10  # 等待10秒，根据实际情况调整

# 脚本结束