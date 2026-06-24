#!/bin/bash

# AI Skills 安装脚本
# 支持跨平台：Linux、macOS、Windows（通过WSL或Git Bash）
# 支持多IDE：Trae、Cursor、VS Code、JetBrains系列

set -e

# 检测操作系统
detect_os() {
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        echo "linux"
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        echo "macos"
    elif [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "msys" ]]; then
        echo "windows"
    else
        echo "unknown"
    fi
}

# 检测当前IDE
detect_ide() {
    local ide="unknown"
    
    # 检测 Trae
    if [[ -n "$TRAE_HOME" ]] || [[ -n "$TRAE_PROJECT_DIR" ]]; then
        ide="trae"
    # 检测 Cursor
    elif [[ "$TERM_PROGRAM" == "Cursor" ]] || [[ -n "$CURSOR_HOME" ]]; then
        ide="cursor"
    # 检测 VS Code
    elif [[ "$TERM_PROGRAM" == "vscode" ]] || [[ -n "$VSCODE_CWD" ]]; then
        ide="vscode"
    # 检测 JetBrains IDE (IntelliJ, CLion, GoLand等)
    elif [[ "$TERMINAL_EMULATOR" == "JetBrains-JediTerm" ]]; then
        ide="jetbrains"
    # 检测 Codeium (CC)
    elif [[ -n "$CODEIUM_DIR" ]] || [[ "$TERM_PROGRAM" == "Codeium" ]]; then
        ide="codeium"
    fi
    
    echo "$ide"
}

# 获取IDE名称显示
get_ide_name() {
    case "$1" in
        trae) echo "Trae" ;;
        cursor) echo "Cursor" ;;
        vscode) echo "VS Code" ;;
        jetbrains) echo "JetBrains IDE (IntelliJ/CLion等)" ;;
        codeium) echo "Codeium (CC)" ;;
        *) echo "未知IDE" ;;
    esac
}

# 获取系统名称显示
get_os_name() {
    case "$1" in
        linux) echo "Linux" ;;
        macos) echo "macOS" ;;
        windows) echo "Windows" ;;
        *) echo "未知系统" ;;
    esac
}

# 显示欢迎信息
show_welcome() {
    local os=$(detect_os)
    local ide=$(detect_ide)
    
    echo "============================================="
    echo "          AI Skills 安装脚本"
    echo "============================================="
    echo "  系统: $(get_os_name "$os")"
    echo "  IDE:  $(get_ide_name "$ide")"
    echo "============================================="
    echo ""
}

# 显示帮助信息
show_help() {
    echo "Usage: $0 [OPTIONS] [TARGET_DIR]"
    echo ""
    echo "将 AI Skills 安装到目标项目中"
    echo ""
    echo "Options:"
    echo "  -h, --help          显示帮助信息"
    echo "  -l, --list          列出可用的技能"
    echo "  -a, --all           安装所有技能（默认）"
    echo "  -s, --skill NAME    安装指定的技能（可多次使用）"
    echo "  -f, --force         强制覆盖已存在的技能"
    echo "  -i, --interactive   交互式安装模式"
    echo "  --ide IDE           指定目标IDE (trae/cursor/vscode/jetbrains/codeium)"
    echo "  --os OS             指定操作系统 (linux/macos/windows)"
    echo ""
    echo "Examples:"
    echo "  $0 /path/to/project          # 安装所有技能到指定项目"
    echo "  $0 -s personal-backend-coding-standard /path/to/project  # 安装指定技能"
    echo "  $0 -i                        # 交互式安装"
    echo "  $0 --ide trae .              # 指定IDE安装"
}

# 列出所有可用技能
list_skills() {
    echo "可用技能列表:"
    echo ""
    echo "=== 个人定制技能 (personal-*) ==="
    find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d -name "personal-*" | sort | while read -r dir; do
        skill_name=$(basename "$dir")
        desc=$(grep -m 1 "description:" "$dir/SKILL.md" 2>/dev/null | sed 's/description: \"\(.*\)\"/\1/')
        echo "  $skill_name"
        if [ -n "$desc" ]; then
            echo "      $desc"
        fi
    done
    
    echo ""
    echo "=== 通用技能 (common-*) ==="
    find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d -name "common-*" | sort | while read -r dir; do
        skill_name=$(basename "$dir")
        desc=$(grep -m 1 "description:" "$dir/SKILL.md" 2>/dev/null | sed 's/description: \"\(.*\)\"/\1/')
        echo "  $skill_name"
        if [ -n "$desc" ]; then
            echo "      $desc"
        fi
    done
    
    echo ""
    echo "=== 其他技能 ==="
    find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d ! -name "personal-*" ! -name "common-*" ! -name "." ! -name "skills" | sort | while read -r dir; do
        skill_name=$(basename "$dir")
        desc=$(grep -m 1 "description:" "$dir/SKILL.md" 2>/dev/null | sed 's/description: \"\(.*\)\"/\1/')
        echo "  $skill_name"
        if [ -n "$desc" ]; then
            echo "      $desc"
        fi
    done
}

# 获取技能列表数组
get_all_skills() {
    local skills=()
    
    # 个人定制技能
    while read -r dir; do
        skills+=("$(basename "$dir")")
    done < <(find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d -name "personal-*" | sort)
    
    # 通用技能
    while read -r dir; do
        skills+=("$(basename "$dir")")
    done < <(find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d -name "common-*" | sort)
    
    # 其他技能
    while read -r dir; do
        skills+=("$(basename "$dir")")
    done < <(find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d ! -name "personal-*" ! -name "common-*" ! -name "." ! -name "skills" | sort)
    
    echo "${skills[@]}"
}

# 交互式选择技能
interactive_select() {
    local all_skills=($(get_all_skills))
    local selected=()
    local i=1
    
    echo "请选择要安装的技能（输入数字，空格分隔，按回车确认，输入 'all' 选择全部）:"
    echo ""
    
    for skill in "${all_skills[@]}"; do
        local desc=$(grep -m 1 "description:" "$SKILLS_SOURCE_DIR/$skill/SKILL.md" 2>/dev/null | sed 's/description: \"\(.*\)\"/\1/')
        echo "  $i. $skill"
        if [ -n "$desc" ]; then
            echo "     $desc"
        fi
        ((i++))
    done
    
    echo ""
    read -p "请输入选择: " input
    
    if [[ "$input" == "all" ]]; then
        selected=("${all_skills[@]}")
    else
        for num in $input; do
            if [[ "$num" -ge 1 && "$num" -le ${#all_skills[@]} ]]; then
                selected+=("${all_skills[$((num-1))]}")
            fi
        done
    fi
    
    echo "${selected[@]}"
}

# 交互式选择IDE
interactive_select_ide() {
    local ides=("trae" "cursor" "vscode" "jetbrains" "codeium" "none")
    local names=("Trae" "Cursor" "VS Code" "JetBrains IDE" "Codeium (CC)" "不配置IDE")
    local i=1
    
    echo "请选择目标IDE:"
    echo ""
    
    for idx in "${!ides[@]}"; do
        echo "  $i. ${names[$idx]}"
        ((i++))
    done
    
    echo ""
    read -p "请输入选择 (1-${#ides[@]}): " input
    
    if [[ "$input" -ge 1 && "$input" -le ${#ides[@]} ]]; then
        echo "${ides[$((input-1))]}"
    else
        echo "none"
    fi
}

# 安装单个技能
install_skill() {
    local skill_name=$1
    local target_dir=$2
    local force=$3
    
    local source_path="$SKILLS_SOURCE_DIR/$skill_name"
    local target_path="$target_dir/$SKILLS_TARGET_DIR/$skill_name"
    
    if [ ! -d "$source_path" ]; then
        echo "错误: 技能 '$skill_name' 不存在"
        exit 1
    fi
    
    if [ -d "$target_path" ] && [ "$force" != "true" ]; then
        echo "跳过 '$skill_name' (已存在，使用 -f 强制覆盖)"
        return
    fi
    
    echo "安装技能: $skill_name"
    mkdir -p "$target_path"
    cp -r "$source_path"/. "$target_path"/
    echo "  ✓ 已安装到 $target_path"
}

# 配置IDE支持
configure_ide() {
    local ide=$1
    local target_dir=$2
    
    if [ "$ide" = "none" ]; then
        return
    fi
    
    echo ""
    echo "=== 配置 $ide 支持 ==="
    
    # 创建IDE特定配置
    local ide_config_dir="$target_dir/.trae/ide"
    mkdir -p "$ide_config_dir"
    
    # 创建IDE配置文件
    cat > "$ide_config_dir/$ide.yml" << EOF
# $ide IDE 配置
ide: $ide
enabled: true
settings:
  auto-load-skills: true
  skill-paths:
    - .trae/skills
EOF
    
    echo "  ✓ 已创建 $ide 配置"
    
    # 根据IDE类型添加特定配置
    case "$ide" in
        trae)
            # Trae 特定配置
            cat > "$target_dir/.trae/agents.yml" << EOF
# Trae 代理配置
agents:
  - name: skills-agent
    skills:
      - personal-backend-coding-standard
      - personal-frontend-coding-standard
      - common-project-standard
      - common-coding
EOF
            echo "  ✓ 已创建 Trae 代理配置"
            ;;
        cursor|vscode)
            # VS Code/Cursor 配置
            mkdir -p "$target_dir/.vscode"
            cat > "$target_dir/.vscode/settings.json" << EOF
{
  "trae.skills.path": ".trae/skills",
  "trae.skills.autoLoad": true
}
EOF
            echo "  ✓ 已创建 VS Code/Cursor 设置"
            ;;
        jetbrains)
            # JetBrains IDE 配置
            mkdir -p "$target_dir/.idea"
            cat > "$target_dir/.idea/trae-skills.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="TraeSkillsConfiguration">
    <option name="skillPath" string="true">.trae/skills</option>
    <option name="autoLoad" string="false">true</option>
  </component>
</project>
EOF
            echo "  ✓ 已创建 JetBrains IDE 配置"
            ;;
        codeium)
            # Codeium (CC) 配置
            cat > "$target_dir/.codeium/skills.json" << EOF
{
  "skillsPath": ".trae/skills",
  "autoLoad": true
}
EOF
            echo "  ✓ 已创建 Codeium 配置"
            ;;
    esac
}

# 主函数
main() {
    local target_dir="."
    local install_all=true
    local force=false
    local interactive=false
    local target_ide=""
    local skills=()
    
    # 检测环境
    local os=$(detect_os)
    local detected_ide=$(detect_ide)
    
    # 解析参数
    while [[ $# -gt 0 ]]; do
        case $1 in
            -h|--help)
                show_help
                exit 0
                ;;
            -l|--list)
                list_skills
                exit 0
                ;;
            -a|--all)
                install_all=true
                ;;
            -s|--skill)
                install_all=false
                skills+=("$2")
                shift
                ;;
            -f|--force)
                force=true
                ;;
            -i|--interactive)
                interactive=true
                ;;
            --ide)
                target_ide="$2"
                shift
                ;;
            --os)
                os="$2"
                shift
                ;;
            *)
                target_dir=$1
                ;;
        esac
        shift
    done
    
    # 显示欢迎信息
    show_welcome
    
    # 交互式模式
    if [ "$interactive" = true ]; then
        # 选择目标目录
        read -p "请输入目标项目目录 (默认: 当前目录): " input_dir
        if [ -n "$input_dir" ]; then
            target_dir="$input_dir"
        fi
        
        # 选择技能
        selected_skills=$(interactive_select)
        skills=($selected_skills)
        install_all=false
        
        # 选择IDE
        if [ -z "$target_ide" ]; then
            echo ""
            echo "检测到当前IDE: $(get_ide_name "$detected_ide")"
            read -p "是否使用检测到的IDE配置? (y/n): " use_detected
            if [ "$use_detected" = "y" ] || [ "$use_detected" = "Y" ]; then
                target_ide="$detected_ide"
            else
                target_ide=$(interactive_select_ide)
            fi
        fi
        
        # 确认安装
        echo ""
        echo "即将安装 ${#skills[@]} 个技能到 $target_dir"
        echo "目标IDE: $(get_ide_name "$target_ide")"
        read -p "确认安装? (y/n): " confirm
        if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
            echo "安装已取消"
            exit 0
        fi
    fi
    
    # 检查目标目录
    if [ ! -d "$target_dir" ]; then
        echo "错误: 目标目录 '$target_dir' 不存在"
        exit 1
    fi
    
    # 获取绝对路径
    target_dir=$(cd "$target_dir" && pwd)
    
    # 创建目标技能目录
    mkdir -p "$target_dir/$SKILLS_TARGET_DIR"
    
    # 安装技能
    if [ "$install_all" = true ]; then
        echo "安装所有技能到 $target_dir"
        echo "=================================="
        
        # 安装个人定制技能
        echo ""
        echo "=== 安装个人定制技能 ==="
        for skill in $(find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d -name "personal-*" | sort | xargs -n1 basename); do
            install_skill "$skill" "$target_dir" "$force"
        done
        
        # 安装通用技能
        echo ""
        echo "=== 安装通用技能 ==="
        for skill in $(find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d -name "common-*" | sort | xargs -n1 basename); do
            install_skill "$skill" "$target_dir" "$force"
        done
        
        # 安装其他技能
        echo ""
        echo "=== 安装其他技能 ==="
        for skill in $(find "$SKILLS_SOURCE_DIR" -maxdepth 1 -type d ! -name "personal-*" ! -name "common-*" ! -name "." ! -name "skills" | sort | xargs -n1 basename); do
            install_skill "$skill" "$target_dir" "$force"
        done
    else
        echo "安装指定技能到 $target_dir"
        echo "=================================="
        for skill in "${skills[@]}"; do
            install_skill "$skill" "$target_dir" "$force"
        done
    fi
    
    # 配置IDE
    if [ -n "$target_ide" ]; then
        configure_ide "$target_ide" "$target_dir"
    fi
    
    echo ""
    echo "安装完成！"
    echo ""
    echo "技能已安装到: $target_dir/$SKILLS_TARGET_DIR"
    echo ""
    echo "使用说明:"
    echo "  1. 在项目根目录创建 AGENTS.md 文件，注册技能"
    echo "  2. 创建 SKILLS.md 文件，列出可用技能"
    echo "  3. 参考 common-project-standard 技能了解详细配置规范"
}

# 获取脚本绝对路径
SKILLS_SOURCE_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/skills
SKILLS_TARGET_DIR=".trae/skills"

main "$@"