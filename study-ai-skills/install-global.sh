#!/bin/bash

# ================================================
# AI Skills 全局安装脚本
# ================================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置常量
SKILLS_REPO_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TRAE_GLOBAL_DIR="$HOME/.trae"
SKILLS_CONFIG_FILE="$TRAE_GLOBAL_DIR/skills-config.yml"
SKILLS_PATH_FILE="$TRAE_GLOBAL_DIR/skills-path"

# 欢迎信息
echo -e "${BLUE}===============================================${NC}"
echo -e "${BLUE}      AI Skills 全局安装脚本                     ${NC}"
echo -e "${BLUE}===============================================${NC}"
echo ""

# 检查是否以 root 身份运行
if [ "$(id -u)" -eq 0 ]; then
    echo -e "${RED}❌ 警告：不建议以 root 身份运行此脚本${NC}"
    echo -e "${RED}   请使用普通用户身份运行${NC}"
    exit 1
fi

# 创建全局目录
echo -e "${YELLOW}创建全局配置目录...${NC}"
mkdir -p "$TRAE_GLOBAL_DIR"
mkdir -p "$TRAE_GLOBAL_DIR/bin"

# 创建技能配置文件
echo -e "${YELLOW}创建全局技能配置...${NC}"
cat > "$SKILLS_CONFIG_FILE" << 'EOF'
# 全局技能配置
skills:
  # 技能仓库位置
  repository: __SKILLS_REPO_DIR__/skills
  
  # 默认启用的技能
  default-enabled:
    - personal-backend-coding-standard
    - personal-frontend-coding-standard
    - common-project-standard
    - common-coding
    - common-contract-generation
    - common-backend-unit-test
    - common-frontend-playwright-test
    - common-requirement-clarification
    - common-code-review
    - common-spring-boot-init
    - common-db-design
    - common-api-doc
    - common-vue-init
    - common-data-migration
    - common-dockerize
    - spring-ai-chat
    - model-provider-creator
    - mcp-tool-creator
    - plan-executor
    - exception-analysis
  
  # 自动同步更新
  auto-sync: true
  
  # 开源技能配置
  open-source:
    os-ui-library:
      active: native
    os-chat-model:
      active: deepseek
EOF

# 替换技能仓库路径
sed -i.bak "s|__SKILLS_REPO_DIR__|$SKILLS_REPO_DIR|g" "$SKILLS_CONFIG_FILE"
rm -f "$SKILLS_CONFIG_FILE.bak"

# 创建技能路径文件
echo -e "${YELLOW}创建技能路径文件...${NC}"
echo "$SKILLS_REPO_DIR/skills" > "$SKILLS_PATH_FILE"

# 创建全局命令链接
echo -e "${YELLOW}创建全局命令...${NC}"

# 创建 skills-install 命令
cat > "$TRAE_GLOBAL_DIR/bin/skills-install" << 'EOF'
#!/bin/bash
# AI Skills 安装命令

SKILLS_DIR=$(cat "$HOME/.trae/skills-path")
SKILLS_REPO_DIR=$(dirname "$SKILLS_DIR")

"$SKILLS_REPO_DIR/install-skills.sh" "$@"
EOF
chmod +x "$TRAE_GLOBAL_DIR/bin/skills-install"

# 创建 skills-validate 命令
cat > "$TRAE_GLOBAL_DIR/bin/skills-validate" << 'EOF'
#!/bin/bash
# AI Skills 验证命令

SKILLS_DIR=$(cat "$HOME/.trae/skills-path")
SKILLS_REPO_DIR=$(dirname "$SKILLS_DIR")

"$SKILLS_REPO_DIR/validate-skills.sh" "$@"
EOF
chmod +x "$TRAE_GLOBAL_DIR/bin/skills-validate"

# 创建 skills-list 命令
cat > "$TRAE_GLOBAL_DIR/bin/skills-list" << 'EOF'
#!/bin/bash
# AI Skills 列出技能命令

SKILLS_DIR=$(cat "$HOME/.trae/skills-path")
SKILLS_REPO_DIR=$(dirname "$SKILLS_DIR")

"$SKILLS_REPO_DIR/install-skills.sh" -l
EOF
chmod +x "$TRAE_GLOBAL_DIR/bin/skills-list"

# 创建 skills-update 命令
cat > "$TRAE_GLOBAL_DIR/bin/skills-update" << 'EOF'
#!/bin/bash
# AI Skills 更新命令

echo "正在更新技能仓库..."
SKILLS_DIR=$(cat "$HOME/.trae/skills-path")
SKILLS_REPO_DIR=$(dirname "$SKILLS_DIR")

cd "$SKILLS_REPO_DIR"
git pull origin main

echo "技能仓库已更新!"
EOF
chmod +x "$TRAE_GLOBAL_DIR/bin/skills-update"

# 创建 skills-config 命令
cat > "$TRAE_GLOBAL_DIR/bin/skills-config" << 'EOF'
#!/bin/bash
# AI Skills 配置管理命令

SKILLS_CONFIG_FILE="$HOME/.trae/skills-config.yml"

if [ "$1" = "edit" ]; then
    ${EDITOR:-nano} "$SKILLS_CONFIG_FILE"
elif [ "$1" = "show" ]; then
    cat "$SKILLS_CONFIG_FILE"
elif [ "$1" = "path" ]; then
    cat "$HOME/.trae/skills-path"
else
    echo "Usage: skills-config [edit|show|path]"
    echo "  edit   - 编辑配置文件"
    echo "  show   - 显示配置内容"
    echo "  path   - 显示技能仓库路径"
fi
EOF
chmod +x "$TRAE_GLOBAL_DIR/bin/skills-config"

# 添加到 PATH
echo -e "${YELLOW}配置环境变量...${NC}"

add_to_path() {
    local profile_file="$1"
    if [ -f "$profile_file" ]; then
        if ! grep -q "export PATH=\"\$HOME/.trae/bin:\$PATH\"" "$profile_file"; then
            echo 'export PATH="$HOME/.trae/bin:$PATH"' >> "$profile_file"
            echo -e "${GREEN}✓ 已添加到 $profile_file${NC}"
        else
            echo -e "${YELLOW}⚠️  $profile_file 已配置${NC}"
        fi
    fi
}

# 添加到各种 shell 配置文件
add_to_path "$HOME/.bashrc"
add_to_path "$HOME/.bash_profile"
add_to_path "$HOME/.zshrc"
add_to_path "$HOME/.profile"

# 设置环境变量
cat > "$TRAE_GLOBAL_DIR/env.sh" << 'EOF'
#!/bin/bash
# AI Skills 环境变量配置

export TRAE_SKILLS_PATH="$HOME/skills"
export TRAE_CONFIG_PATH="$HOME/.trae"
export PATH="$HOME/.trae/bin:$PATH"
EOF
chmod +x "$TRAE_GLOBAL_DIR/env.sh"

# 创建技能软链接
echo -e "${YELLOW}创建技能软链接...${NC}"
rm -rf "$TRAE_GLOBAL_DIR/skills"
ln -sf "$SKILLS_REPO_DIR/skills" "$TRAE_GLOBAL_DIR/skills"

# 安装完成信息
echo ""
echo -e "${GREEN}===============================================${NC}"
echo -e "${GREEN}         安装完成！                              ${NC}"
echo -e "${GREEN}===============================================${NC}"
echo ""
echo -e "${BLUE}已创建的全局命令：${NC}"
echo -e "  ${YELLOW}skills-install${NC}   - 安装技能到项目"
echo -e "  ${YELLOW}skills-validate${NC}  - 验证技能规范"
echo -e "  ${YELLOW}skills-list${NC}      - 列出所有技能"
echo -e "  ${YELLOW}skills-update${NC}    - 更新技能仓库"
echo -e "  ${YELLOW}skills-config${NC}    - 管理技能配置"
echo ""
echo -e "${BLUE}使用方式：${NC}"
echo -e "  ${YELLOW}技能安装${NC}: skills-install /path/to/project"
echo -e "  ${YELLOW}验证技能${NC}: skills-validate"
echo -e "  ${YELLOW}列出技能${NC}: skills-list"
echo ""
echo -e "${BLUE}使配置立即生效：${NC}"
echo -e "  ${YELLOW}bash/zsh${NC}: source ~/.bashrc 或 source ~/.zshrc"
echo -e "  ${YELLOW}重启终端${NC}: 关闭并重新打开终端"
echo ""
echo -e "${GREEN}✓ 全局安装完成！${NC}"