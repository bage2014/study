#!/bin/bash
# ============================================================
# skills.sh — 技能仓库统一入口
# 用法:
#   scripts/skills.sh install [skill...]   # 安装/更新到 ~/.trae-cn/skills/
#   scripts/skills.sh update               # 全量更新（= 重新安装所有）
#   scripts/skills.sh validate             # 校验技能格式规范
#   scripts/skills.sh list                 # 列出仓库内技能
#   scripts/skills.sh inject               # 重新注入监控埋点到已部署技能
# ============================================================
set -eo pipefail

REPO_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

case "${1:-}" in
    install)
        shift
        # ~/.trae-cn/skills 为 Trae 全局技能目录；指定技能需映射为 -s <skill>
        args=()
        if [ $# -gt 0 ]; then
            for s in "$@"; do args+=(-s "$s"); done
        fi
        export SKILLS_TARGET_DIR="skills"
        exec "$REPO_DIR/scripts/install-skills.sh" "$HOME/.trae-cn" "${args[@]}"
        ;;
    update)
        # 全量强制覆盖 + 重新注入监控埋点（仓库源码不含埋点块）
        export SKILLS_TARGET_DIR="skills"
        "$REPO_DIR/scripts/install-skills.sh" -f "$HOME/.trae-cn"
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
        echo "  install [skill...]  安装指定技能（缺省全部）到 ~/.trae-cn/skills/"
        echo "  update              全量更新已部署技能"
        echo "  validate            校验技能 Frontmatter/结构规范"
        echo "  list                列出仓库内技能"
        echo "  inject              重新注入监控埋点到已部署技能"
        exit 1 ;;
esac
