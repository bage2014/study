

#!/bin/bash

# Flutter APK 构建和上传脚本
# 功能：
# 1. 执行 flutter build apk
# 2. 调用本地 /app/upload 接口上传文件
# 3. 上传路径：build/app/outputs/flutter-apk/release.apk

set -e  # 遇到错误立即退出

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Flutter APK 构建和上传脚本 ===${NC}"

# 获取当前目录
CURRENT_DIR=$(pwd)
APK_PATH="${CURRENT_DIR}/build/app/outputs/flutter-apk/app-release.apk"

# 步骤1：构建APK
echo -e "${YELLOW}步骤1：开始构建APK...${NC}"
flutter clean
flutter pub get
flutter build apk --release

# 检查APK是否存在
if [ ! -f "$APK_PATH" ]; then
    echo -e "${RED}错误：APK文件不存在：$APK_PATH${NC}"
    exit 1
fi

echo -e "${GREEN}APK构建成功：$APK_PATH${NC}"

# 步骤2：上传到本地接口
echo -e "${YELLOW}步骤2：开始上传APK到接口...${NC}"

# 获取文件大小
FILE_SIZE=$(stat -c%s "$APK_PATH")
echo -e "${YELLOW}文件大小：$(($FILE_SIZE / 1024 / 1024))MB${NC}"

# 执行上传
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST \
  -F "file=@${APK_PATH}" \
  -F "filename=release.apk" \
  -F "size=$FILE_SIZE" \
  http://localhost:8080/app/upload)

# 解析响应
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
RESPONSE_BODY=$(echo "$RESPONSE" | sed '$d')

echo -e "${GREEN}上传完成！${NC}"
echo -e "${GREEN}HTTP状态码：$HTTP_CODE${NC}"
echo -e "${GREEN}响应内容：$RESPONSE_BODY${NC}"

# 步骤3：验证上传结果
if [ "$HTTP_CODE" -eq 200 ]; then
    echo -e "${GREEN}✅ APK上传成功！${NC}"
else
    echo -e "${RED}❌ APK上传失败！${NC}"
    exit 1
fi

echo -e "${GREEN}=== 脚本执行完成 ===${NC}"


