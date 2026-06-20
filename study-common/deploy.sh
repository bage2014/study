#!/bin/bash

set -e

PROJECT_NAME="study-common"
PROJECT_VERSION="1.0.0-SNAPSHOT"
BASE_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

echo "======================================"
echo "  ${PROJECT_NAME} 部署脚本"
echo "  Version: ${PROJECT_VERSION}"
echo "  Base Dir: ${BASE_DIR}"
echo "======================================"
echo ""

echo "[1/4] 清理项目..."
cd "${BASE_DIR}"
mvn clean -q
echo "      ✓ 清理完成"

echo ""
echo "[2/4] 编译项目..."
mvn compile -q
echo "      ✓ 编译完成"

echo ""
echo "[3/4] 运行测试..."
mvn test -q
echo "      ✓ 测试通过"

echo ""
echo "[4/4] 安装到本地Maven仓库..."
mvn install -DskipTests -q
echo "      ✓ 安装完成"

echo ""
echo "======================================"
echo "  部署成功！"
echo ""
echo "  项目已安装到本地Maven仓库："
echo "  GroupId: com.study"
echo "  ArtifactId: study-common"
echo "  Version: ${PROJECT_VERSION}"
echo ""
echo "  在其他项目中引入依赖："
echo "  <dependency>"
echo "      <groupId>com.study</groupId>"
echo "      <artifactId>study-common-util</artifactId>"
echo "      <version>${PROJECT_VERSION}</version>"
echo "  </dependency>"
echo "======================================"
