#!/bin/bash
# ============================================================
# skills.sh — 技能仓库统一入口
# 用法:
#   scripts/skills.sh install [skill...]   # 安装/更新到 ~/.trae-cn/skills/ 和 ~/.qoder-cn/skills/
#   scripts/skills.sh update               # 全量更新（= 重新安装所有）
#   scripts/skills.sh validate             # 校验技能格式规范
#   scripts/skills.sh list                 # 列出仓库内技能
#   scripts/skills.sh inject               # 重新注入监控埋点到已部署技能
# ============================================================
set -eo pipefail

REPO_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

# 安装技能到指定平台
install_to_platform() {
    local platform=$1
    local target_dir=$2
    shift 2
    
    echo "=== 安装到 $platform ==="
    args=()
    if [ $# -gt 0 ]; then
        for s in "$@"; do args+=(-s "$s"); done
    fi
    export SKILLS_TARGET_DIR="skills"
    "$REPO_DIR/scripts/install-skills.sh" --ide "$platform" "$target_dir" "${args[@]}"
    echo ""
}

case "${1:-}" in
    install)
        shift
        # 同时安装到 Trae 和 Qoder
        install_to_platform "trae" "$HOME/.trae-cn" "$@"
        install_to_platform "qoder" "$HOME/.qoder-cn" "$@"
        echo "✓ 安装完成：Trae (~/.trae-cn/skills/) + Qoder (~/.qoder-cn/skills/)"
        ;;
    update)
        # 全量强制覆盖 + 重新注入监控埋点（仓库源码不含埋点块）
        echo "=== 更新 Trae 技能 ==="
        export SKILLS_TARGET_DIR="skills"
        "$REPO_DIR/scripts/install-skills.sh" --ide trae -f "$HOME/.trae-cn"
        
        echo ""
        echo "=== 更新 Qoder 技能 ==="
        "$REPO_DIR/scripts/install-skills.sh" --ide qoder -f "$HOME/.qoder-cn"
        
        echo ""
        echo "--- 重新注入监控埋点 ---"
        "$REPO_DIR/monitor/scripts/inject-monitor-hook.sh"
        ;;
    validate)
        "$REPO_DIR/scripts/validate-skills.sh"
        ;;
    list)
        echo "仓库内技能（$REPO_DIR/skills）:"
        ls -1 "$REPO_DIR/skills" | sed 's/^/  - /'
        ;;
    inject)
        if [ -f "$REPO_DIR/monitor/scripts/inject-monitor-hook.sh" ]; then
            exec "$REPO_DIR/monitor/scripts/inject-monitor-hook.sh"
        else
            echo "inject script not found: monitor/scripts/inject-monitor-hook.sh"
            exit 1
        fi
        ;;
    *)
        echo "用法: $0 {install|update|validate|list|inject}"
        echo ""
        echo "  install [skill...]  安装指定技能（缺省全部）到 ~/.trae-cn/skills/ 和 ~/.qoder-cn/skills/"
        echo "  update              全量更新已部署技能（Trae + Qoder）"
        echo "  validate            校验技能 Frontmatter/结构规范"
        echo "  list                列出仓库内技能"
        echo "  inject              重新注入监控埋点到已部署技能"
        exit 1 ;;
esac
