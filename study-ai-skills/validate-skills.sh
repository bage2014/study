#!/bin/bash

# AI Skills 规范验证脚本
# 验证技能是否符合标准格式

set -e

SKILLS_DIR="skills"
VALIDATION_LOG="/tmp/skill-validation.log"

# 定义标准结构
REQUIRED_SECTIONS=(
    "## 功能描述"
    "## 触发条件"
    "## 何时使用"
    "## 核心功能"
    "## 输入参数"
    "## 输出格式"
)

RECOMMENDED_SECTIONS=(
    "## 何时不使用"
    "## 使用流程"
    "## 调用示例"
    "## 失败案例"
    "## 最佳实践"
    "## 配置要求"
    "## 扩展指南"
)

REQUIRED_FRONTMATTER=(
    "name:"
    "description:"
    "trigger:"
    "disable-when:"
    "category:"
)

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# 验证单个技能
validate_skill() {
    local skill_dir=$1
    local skill_name=$(basename "$skill_dir")
    local skill_file="$skill_dir/SKILL.md"
    local errors=()
    local warnings=()
    
    echo "============================================="
    echo "验证技能: $skill_name"
    echo "============================================="
    
    # 检查 SKILL.md 是否存在
    if [ ! -f "$skill_file" ]; then
        errors+=("SKILL.md 文件不存在")
        echo -e "${RED}✗ SKILL.md 文件不存在${NC}"
        return 1
    fi
    
    # 检查 Frontmatter
    echo "检查 Frontmatter..."
    local frontmatter=$(head -10 "$skill_file")
    
    for field in "${REQUIRED_FRONTMATTER[@]}"; do
        if ! echo "$frontmatter" | grep -q "$field"; then
            errors+=("缺少 Frontmatter 字段: $field")
            echo -e "${RED}✗ 缺少 Frontmatter 字段: $field${NC}"
        else
            echo -e "${GREEN}✓ 包含 Frontmatter 字段: $field${NC}"
        fi
    done
    
    # 检查 name 字段是否匹配目录名
    local name_value=$(grep -m 1 'name:' "$skill_file" | sed 's/name: "\(.*\)"/\1/')
    if [ "$name_value" != "$skill_name" ]; then
        warnings+=("name 字段值 '$name_value' 与目录名 '$skill_name' 不匹配")
        echo -e "${YELLOW}⚠ name 字段值 '$name_value' 与目录名 '$skill_name' 不匹配${NC}"
    fi
    
    # 检查必需章节
    echo ""
    echo "检查文档章节..."
    for section in "${REQUIRED_SECTIONS[@]}"; do
        if ! grep -q "^$section" "$skill_file"; then
            warnings+=("缺少章节: $section")
            echo -e "${YELLOW}⚠ 缺少章节: $section${NC}"
        else
            echo -e "${GREEN}✓ 包含章节: $section${NC}"
        fi
    done
    
    # 检查推荐章节
    echo ""
    echo "检查推荐章节..."
    for section in "${RECOMMENDED_SECTIONS[@]}"; do
        if ! grep -q "^$section" "$skill_file"; then
            echo -e "${BLUE}○ 建议添加章节: $section${NC}"
        else
            echo -e "${GREEN}✓ 包含章节: $section${NC}"
        fi
    done
    
    # 检查表格格式（输入参数）
    echo ""
    echo "检查输入参数表格..."
    if grep -q "## 输入参数" "$skill_file"; then
        # 检查是否有表格
        local params_section=$(sed -n '/## 输入参数/,/^## /p' "$skill_file")
        if ! echo "$params_section" | grep -q "| 参数 |"; then
            warnings+=("输入参数章节缺少表格")
            echo -e "${YELLOW}⚠ 输入参数章节缺少表格${NC}"
        else
            echo -e "${GREEN}✓ 输入参数包含表格${NC}"
        fi
    fi
    
    # 检查输出格式
    echo ""
    echo "检查输出格式..."
    if grep -q "## 输出格式" "$skill_file"; then
        local output_section=$(sed -n '/## 输出格式/,/^## /p' "$skill_file")
        if ! echo "$output_section" | grep -qE "(json|yaml|示例)"; then
            warnings+=("输出格式章节缺少示例")
            echo -e "${YELLOW}⚠ 输出格式章节缺少示例${NC}"
        else
            echo -e "${GREEN}✓ 输出格式包含示例${NC}"
        fi
    fi
    
    # 输出结果
    echo ""
    if [ ${#errors[@]} -eq 0 ] && [ ${#warnings[@]} -eq 0 ]; then
        echo -e "${GREEN}✓ 技能 '$skill_name' 验证通过${NC}"
        return 0
    elif [ ${#errors[@]} -eq 0 ]; then
        echo -e "${YELLOW}⚠ 技能 '$skill_name' 验证通过，但有警告${NC}"
        return 0
    else
        echo -e "${RED}✗ 技能 '$skill_name' 验证失败${NC}"
        return 1
    fi
}

# 修复技能格式
fix_skill() {
    local skill_dir=$1
    local skill_name=$(basename "$skill_dir")
    local skill_file="$skill_dir/SKILL.md"
    
    echo ""
    echo "============================================="
    echo "修复技能: $skill_name"
    echo "============================================="
    
    # 如果 SKILL.md 不存在，创建默认模板
    if [ ! -f "$skill_file" ]; then
        echo -e "${YELLOW}创建默认 SKILL.md 模板${NC}"
        cat > "$skill_file" << EOF
---
name: "$skill_name"
description: "$skill_name 技能描述，简要说明技能的核心价值和用途"
trigger: "用户需要使用 $skill_name 相关功能时"
disable-when: "不适合使用 $skill_name 的场景描述"
category: "common"
tags: ["$skill_name"]
---

# $skill_name

## 功能描述

该技能提供 $skill_name 相关功能。

## 触发条件

- **前置条件**：项目已初始化
- **环境要求**：根据技能需求填写
- **数据准备**：根据技能需求填写
- **触发方式**：API 调用 / 命令行执行 / 事件触发

## 何时使用

在以下情况调用此技能：
- 需要使用 $skill_name 功能时

## 何时不使用

- 当场景不适合使用此技能时

## 核心功能

- 核心功能描述

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|

## 输出格式

\`\`\`json
{
  "status": "SUCCESS",
  "data": {}
}
\`\`\`

## 使用流程

## 调用示例

## 失败案例

| 错误场景 | 错误信息 | 解决方案 |
|----------|----------|----------|

## 最佳实践

## 配置要求

## 扩展指南
EOF
        echo -e "${GREEN}✓ 已创建默认 SKILL.md${NC}"
        return
    fi
    
    # 检查并修复 Frontmatter
    echo "检查 Frontmatter..."
    if ! head -1 "$skill_file" | grep -q "^---"; then
        echo -e "${YELLOW}添加 Frontmatter${NC}"
        local content=$(cat "$skill_file")
        cat > "$skill_file" << EOF
---
name: "$skill_name"
description: "$skill_name 技能描述，简要说明技能的核心价值和用途"
trigger: "用户需要使用 $skill_name 相关功能时"
disable-when: "不适合使用 $skill_name 的场景描述"
category: "common"
tags: ["$skill_name"]
---

$content
EOF
    else
        # 检查 name 字段
        if ! grep -q 'name:' "$skill_file"; then
            sed -i '' '/^---$/a\
name: "'"$skill_name"'"' "$skill_file"
        fi
        # 检查 description 字段
        if ! grep -q 'description:' "$skill_file"; then
            sed -i '' '/^---$/a\
description: "'"$skill_name 技能描述"'"' "$skill_file"
        fi
    fi
    
    # 检查并添加缺失的章节
    for section in "${REQUIRED_SECTIONS[@]}"; do
        if ! grep -q "^$section" "$skill_file"; then
            echo -e "${YELLOW}添加缺失章节: $section${NC}"
            echo -e "\n$section\n" >> "$skill_file"
        fi
    done
    
    echo -e "${GREEN}✓ 技能 '$skill_name' 修复完成${NC}"
}

# 主函数
main() {
    local validate_only=false
    local fix_mode=false
    local target_skill=""
    
    # 解析参数
    while [[ $# -gt 0 ]]; do
        case $1 in
            -h|--help)
                echo "Usage: $0 [OPTIONS]"
                echo ""
                echo "验证技能是否符合规范"
                echo ""
                echo "Options:"
                echo "  -h, --help          显示帮助信息"
                echo "  -l, --list          列出所有技能"
                echo "  -v, --validate      仅验证（默认）"
                echo "  -f, --fix           修复模式"
                echo "  -s, --skill NAME    验证指定技能"
                exit 0
                ;;
            -l|--list)
                echo "可用技能:"
                ls -la "$SKILLS_DIR" | grep -E '^d' | awk '{print $9}'
                exit 0
                ;;
            -v|--validate)
                validate_only=true
                ;;
            -f|--fix)
                fix_mode=true
                ;;
            -s|--skill)
                target_skill="$2"
                shift
                ;;
            *)
                echo "未知参数: $1"
                exit 1
                ;;
        esac
        shift
    done
    
    echo "============================================="
    echo "        AI Skills 规范验证工具"
    echo "============================================="
    echo ""
    
    if [ -n "$target_skill" ]; then
        # 验证指定技能
        local skill_path="$SKILLS_DIR/$target_skill"
        if [ -d "$skill_path" ]; then
            if [ "$fix_mode" = true ]; then
                fix_skill "$skill_path"
            else
                validate_skill "$skill_path"
            fi
        else
            echo -e "${RED}错误: 技能 '$target_skill' 不存在${NC}"
            exit 1
        fi
    else
        # 验证所有技能
        local total_skills=$(ls -la "$SKILLS_DIR" | grep -E '^d' | wc -l)
        local passed=0
        local failed=0
        
        echo "开始验证 $total_skills 个技能..."
        echo ""
        
        for skill_dir in "$SKILLS_DIR"/*/; do
            skill_name=$(basename "$skill_dir")
            if [ "$skill_name" = "skills" ]; then
                continue
            fi
            
            if [ "$fix_mode" = true ]; then
                fix_skill "$skill_dir"
                passed=$((passed + 1))
            else
                if validate_skill "$skill_dir"; then
                    passed=$((passed + 1))
                else
                    failed=$((failed + 1))
                fi
            fi
            echo ""
        done
        
        echo "============================================="
        echo "验证结果:"
        echo "  总数: $total_skills"
        echo "  通过: ${GREEN}$passed${NC}"
        if [ "$failed" -gt 0 ]; then
            echo "  失败: ${RED}$failed${NC}"
            exit 1
        else
            echo "  失败: ${GREEN}0${NC}"
            echo -e "${GREEN}✓ 所有技能验证通过！${NC}"
        fi
    fi
}

main "$@"