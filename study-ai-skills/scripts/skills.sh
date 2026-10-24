#!/bin/bash
# ============================================================
# skills.sh — 技能仓库统一入口
# 用法:
#   scripts/skills.sh install [skill...]   # 安装/更新到 ~/.trae-cn/skills/ 和 ~/.qoder-cn/skills/
#   scripts/skills.sh update               # 全量更新（= 重新安装所有）
#   scripts/skills.sh validate             # 校验技能格式规范
#   scripts/skills.sh list                 # 列出仓库内技能
#   scripts/skills.sh inject               # 重新注入监控埋点到已部署技能
#   scripts/skills.sh sync-docs            # 同步更新汇总文档（SKILLS.md / AGENTS.md）
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

# 同步更新汇总文档
sync_docs() {
    echo "=== 同步汇总文档 ==="
    
    # 1. 更新 SKILLS.md 中的技能列表
    echo "更新 SKILLS.md..."
    python3 "$REPO_DIR/scripts/sync-skills-md.py" 2>/dev/null || echo "  (自动同步脚本不存在，请手动更新 SKILLS.md)"
    
    # 2. 检查 AGENTS.md 是否需要更新
    echo "检查 AGENTS.md..."
    if [ -f "$REPO_DIR/agents.md" ]; then
        echo "  agents.md 存在，请确认技能路由表是否需要更新"
    fi
    
    echo ""
    echo "提示: 如有新增/删除技能，请手动更新以下文档:"
    echo "  - SKILLS.md（技能清单）"
    echo "  - agents.md（技能路由表）"
    echo "  - SKILLS-ROUTER.md（场景路由索引）"
    echo ""
}

case "${1:-}" in
    install)
        shift
        # 同时安装到 Trae 和 Qoder
        install_to_platform "trae" "$HOME/.trae-cn" "$@"
        install_to_platform "qoder" "$HOME/.qoder-cn" "$@"
        echo "✓ 安装完成：Trae (~/.trae-cn/skills/) + Qoder (~/.qoder-cn/skills/)"
        
        # 提示更新文档
        echo ""
        echo "提示: 如有新增/修改技能，请运行:"
        echo "  $0 sync-docs    # 同步汇总文档"
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
        
        # 同步文档
        echo ""
        sync_docs
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
    sync-docs)
        sync_docs
        ;;
    *)
        echo "用法: $0 {install|update|validate|list|inject|sync-docs}"
        echo ""
        echo "  install [skill...]  安装指定技能（缺省全部）到 ~/.trae-cn/skills/ 和 ~/.qoder-cn/skills/"
        echo "  update              全量更新已部署技能（Trae + Qoder）+ 同步文档"
        echo "  validate            校验技能 Frontmatter/结构规范"
        echo "  list                列出仓库内技能"
        echo "  inject              重新注入监控埋点到已部署技能"
        echo "  sync-docs           同步更新汇总文档（SKILLS.md / AGENTS.md）"
        exit 1 ;;
esac
