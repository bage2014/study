#!/bin/bash
# ============================================================
# monitor.sh — Skill 监控服务统一入口
#   包装 monitor/scripts/skill-usage.sh（埋点/上传/生命周期）
# 用法:
#   monitor/scripts/monitor.sh start | stop | restart | status
#   monitor/scripts/monitor.sh cron-start | cron-stop
#   monitor/scripts/monitor.sh deploy          # 部署最新脚本到 ~/.trae-cn/scripts/
#   monitor/scripts/monitor.sh log <skill>     # 记录一条埋点
# ============================================================
set -eo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SOURCE_SCRIPT="$SCRIPT_DIR/skill-usage.sh"
DEPLOYED_SCRIPT="$HOME/.trae-cn/scripts/skill-usage.sh"

deploy() {
    if [ ! -f "$SOURCE_SCRIPT" ]; then
        echo "source script not found: $SOURCE_SCRIPT"
        exit 1
    fi
    mkdir -p "$(dirname "$DEPLOYED_SCRIPT")"
    cp "$SOURCE_SCRIPT" "$DEPLOYED_SCRIPT"
    chmod +x "$DEPLOYED_SCRIPT"
    echo "deployed -> $DEPLOYED_SCRIPT"
}

# 优先用已部署副本（与 cron/埋点一致），不存在则部署后再用
if [ ! -x "$DEPLOYED_SCRIPT" ]; then
    deploy
fi

case "${1:-}" in
    deploy)     deploy ;;
    cron-start) exec "$DEPLOYED_SCRIPT" start-cron ;;
    cron-stop)  exec "$DEPLOYED_SCRIPT" stop-cron ;;
    start)      exec "$DEPLOYED_SCRIPT" start-server ;;
    stop)       exec "$DEPLOYED_SCRIPT" stop-server ;;
    restart)    exec "$DEPLOYED_SCRIPT" restart-server ;;
    status)     exec "$DEPLOYED_SCRIPT" status ;;
    log)        shift; exec "$DEPLOYED_SCRIPT" log "$@" ;;
    *)
        echo "用法: $0 {start|stop|restart|status|cron-start|cron-stop|deploy|log <skill>}"
        echo ""
        echo "  start        启动监控服务（8090，health 探活后返回）"
        echo "  stop         关闭监控服务"
        echo "  restart      重启监控服务"
        echo "  status       服务/Cron/数据管道状态总览"
        echo "  cron-start   安装 launchd 定时上传（60s）"
        echo "  cron-stop    卸载定时上传"
        echo "  deploy       部署仓库内最新脚本到 ~/.trae-cn/scripts/"
        echo "  log <skill>  记录一条埋点（--trigger auto|manual）"
        exit 1 ;;
esac
