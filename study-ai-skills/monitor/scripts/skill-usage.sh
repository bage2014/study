#!/bin/bash
# ============================================================
# Trae Skill Monitor v2.1
#   log <skill> [--trigger auto|manual]   Append JSONL record
#   upload                                 Incremental upload with offset
#   report [--days N]                     Local analysis
#   start-cron / stop-cron                macOS launchd (every 60s)
#   status                                Server + cron + data status
#   start-server / stop-server / restart-server
#                                         Spring Boot lifecycle
# ============================================================

set -eo pipefail

JSONL_FILE="${HOME}/.trae-cn/skill-usage.jsonl"
UPLOAD_OFFSET="${HOME}/.trae-cn/skill-upload.offset"
UPLOAD_LOG="${HOME}/.trae-cn/skill-upload.log"
SERVER_LOG="${HOME}/.trae-cn/skill-monitor-server.log"
MONITOR_URL="${SKILL_MONITOR_URL:-http://localhost:8090}"
UPLOAD_ENDPOINT="/api/skill-usage/batch"
SERVER_DIR="${SKILL_MONITOR_DIR:-${HOME}/bage/github/study/study-ai-skills/monitor/skill-monitor-server}"

mkdir -p "$(dirname "$JSONL_FILE")"

# ── log ─────────────────────────────────────────────────────
# 检测当前项目名：从 PWD 向上找最近的项目标记（pom.xml/package.json/.git/AGENTS.md 等），
# 找不到则回退 git 根目录名，最后回退当前目录名。支持 monorepo 场景（仓库内多个子项目）。
detect_project() {
    local dir="$PWD" root m
    root=$(git rev-parse --show-toplevel 2>/dev/null)
    while [ "$dir" != "/" ] && [ "$dir" != "$HOME" ]; do
        for m in .git pom.xml package.json build.gradle settings.gradle go.mod Cargo.toml pyproject.toml AGENTS.md; do
            if [ -e "$dir/$m" ]; then basename "$dir"; return; fi
        done
        [ -n "$root" ] && [ "$dir" = "$root" ] && break
        dir=$(dirname "$dir")
    done
    if [ -n "$root" ]; then basename "$root"; else basename "$PWD"; fi
}

log_usage() {
    local skill_name="" trigger="manual" event="tool_load"
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --trigger) trigger="$2"; shift 2 ;;
            --event)   event="$2";   shift 2 ;;
            *)         skill_name="$1"; shift ;;
        esac
    done
    [ -z "$skill_name" ] && { echo "missing skill-name"; return 1; }

    local ts project
    ts=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
    project=$(detect_project)
    local line
    line=$(printf '{"ts":"%s","skill":"%s","event":"%s","client":"trae-macos","session":"no-session","duration_ms":0,"status":"ok","trigger":"%s","user":"%s","project":"%s"}\n' "$ts" "$skill_name" "$event" "$trigger" "${USER:-unknown}" "$project")
    if ! echo "$line" >> "$JSONL_FILE" 2>/dev/null; then
        echo "MONITOR-LOG-FAILED: 无法写入 $JSONL_FILE（可能被沙箱拦截）"
        echo "请以非沙箱方式重试: ~/.trae-cn/scripts/skill-usage.sh log $skill_name --trigger $trigger"
        return 2
    fi
    echo "monitor: logged $skill_name ($event, $trigger, $project)"
}

# ── upload (incremental + resume) ──────────────────────────
do_upload() {
    [ ! -f "$JSONL_FILE" ] && echo "no data" && return 0

    local last_offset=0
    if [ -f "$UPLOAD_OFFSET" ]; then
        last_offset=$(cat "$UPLOAD_OFFSET")
    fi

    local total_lines
    total_lines=$(wc -l < "$JSONL_FILE" | tr -d ' ')

    if [ "$last_offset" -ge "$total_lines" ]; then
        echo "skip: offset=$last_offset >= total=$total_lines"
        return 0
    fi

    local delta=$((total_lines - last_offset))
    local chunk
    chunk=$(tail -n +$((last_offset + 1)) "$JSONL_FILE")

    echo "uploading $delta lines (offset=$last_offset -> $total_lines) to $MONITOR_URL$UPLOAD_ENDPOINT"

    local http_code
    http_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -X POST -H "Content-Type: text/plain" \
        --data "$chunk" \
        "$MONITOR_URL$UPLOAD_ENDPOINT" 2>/dev/null || echo "000")

    if [ "$http_code" = "200" ]; then
        echo "$total_lines" > "$UPLOAD_OFFSET"
        echo "ok (HTTP 200), offset -> $total_lines"
    else
        echo "failed (HTTP $http_code), offset stays $last_offset"
        return 1
    fi
}

# ── report ─────────────────────────────────────────────────
do_report() {
    local days="${1:-7}"
    local total
    total=$(wc -l < "$JSONL_FILE" 2>/dev/null || echo 0)
    echo "=== Trae Skill Report (last $days days) ==="
    echo "total records: $total"
    echo ""
    if [ ! -f "$JSONL_FILE" ] || [ "$total" -eq 0 ]; then
        echo "no data yet."
        return 0
    fi
    echo "top skills:"
    grep -o '"skill":"[^"]*"' "$JSONL_FILE" | sed 's/"skill":"//;s/"//' | sort | uniq -c | sort -rn | head -10 | \
        awk '{printf "  %4d  %s\n", $1, $2}'
    echo ""
    echo "trigger distribution:"
    grep -o '"trigger":"[^"]*"' "$JSONL_FILE" | sed 's/"trigger":"//;s/"//' | sort | uniq -c | sort -rn | \
        awk '{printf "  %-10s %4d\n", $2, $1}'
}

# ── macOS launchd cron ─────────────────────────────────────
install_cron() {
    local plist="$HOME/Library/LaunchAgents/com.studyskills.skill-monitor.plist"
    mkdir -p "$HOME/Library/LaunchAgents"
    local script_path
    script_path=$(realpath "$0")

    cat > "$plist" << PLIST
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>Label</key>
    <string>com.studyskills.skill-monitor</string>
    <key>ProgramArguments</key>
    <array>
        <string>/bin/bash</string>
        <string>${script_path}</string>
        <string>upload</string>
    </array>
    <key>StartInterval</key>
    <integer>60</integer>
    <key>StandardOutPath</key>
    <string>${HOME}/.trae-cn/skill-upload.log</string>
    <key>StandardErrorPath</key>
    <string>${HOME}/.trae-cn/skill-upload.err</string>
    <key>RunAtLoad</key>
    <true/>
</dict>
</plist>
PLIST

    launchctl unload "$plist" 2>/dev/null || true
    launchctl load "$plist"
    echo "cron installed (every 60s)"
    echo "  plist: $plist"
    echo "  check: launchctl list com.studyskills.skill-monitor"
    echo "  logs:  tail -f ~/.trae-cn/skill-upload.log"
}

uninstall_cron() {
    local plist="$HOME/Library/LaunchAgents/com.studyskills.skill-monitor.plist"
    [ -f "$plist" ] && { launchctl unload "$plist"; rm "$plist"; echo "cron removed"; } || echo "no plist found"
}

# ── server lifecycle ───────────────────────────────────────
health_ok() {
    curl -s -m 2 "$MONITOR_URL/api/skill-usage/health" >/dev/null 2>&1
}

do_status() {
    local server="down" cron_state="not-installed" lines=0 offset=0
    if health_ok; then server="up"; fi
    if launchctl list com.studyskills.skill-monitor >/dev/null 2>&1; then
        cron_state="installed(loaded)"
    elif [ -f "$HOME/Library/LaunchAgents/com.studyskills.skill-monitor.plist" ]; then
        cron_state="plist-only(not loaded)"
    fi
    if [ -f "$JSONL_FILE" ]; then
        lines=$(wc -l < "$JSONL_FILE" | tr -d ' ')
    fi
    if [ -f "$UPLOAD_OFFSET" ]; then
        offset=$(cat "$UPLOAD_OFFSET")
    fi
    echo "server     : $server ($MONITOR_URL)"
    echo "cron       : $cron_state"
    echo "jsonl      : $lines lines (offset $offset, pending $((lines - offset)))"
    echo "upload log : $UPLOAD_LOG (last: $(tail -1 "$UPLOAD_LOG" 2>/dev/null || echo '-'))"
    echo "server log : $SERVER_LOG"
    echo "server dir : $SERVER_DIR"
}

start_server() {
    if health_ok; then
        echo "already running ($MONITOR_URL)"
        return 0
    fi
    if [ ! -d "$SERVER_DIR" ]; then
        echo "server dir not found: $SERVER_DIR (set SKILL_MONITOR_DIR)"
        return 1
    fi
    if ! command -v mvn >/dev/null 2>&1; then
        echo "mvn not found in PATH"
        return 1
    fi
    echo "starting Spring Boot from $SERVER_DIR ..."
    (cd "$SERVER_DIR" && nohup mvn -q spring-boot:run >> "$SERVER_LOG" 2>&1 &)
    local i=0
    until health_ok; do
        i=$((i + 1))
        if [ "$i" -gt 45 ]; then
            echo "timeout: not healthy after 90s, check $SERVER_LOG"
            return 1
        fi
        sleep 2
    done
    echo "server is up ($MONITOR_URL)"
}

stop_server() {
    if ! health_ok; then
        echo "already stopped"
        return 0
    fi
    # [i] 括号技巧：避免 pkill -f 匹配到本脚本自身 shell
    pkill -f 'SkillMonitorAppl[i]cation' || true
    local i=0
    while health_ok; do
        i=$((i + 1))
        if [ "$i" -gt 10 ]; then
            echo "warn: port still responding after 10s"
            return 1
        fi
        sleep 1
    done
    echo "server stopped"
}

restart_server() {
    stop_server || true
    start_server
}

# ── main ────────────────────────────────────────────────────
case "${1:-}" in
    log|record)   shift; log_usage "$@" ;;
    upload|push)  shift; do_upload "$@" ;;
    report|show)  shift; do_report "$@" ;;
    start-cron)   install_cron ;;
    stop-cron)    uninstall_cron ;;
    status)       do_status ;;
    start-server) start_server ;;
    stop-server)  stop_server ;;
    restart-server) restart_server ;;
    *)
        echo "Trae Skill Monitor v2.1"
        echo "  log <skill> [--trigger auto|manual]"
        echo "  upload           # incremental -> Spring Boot"
        echo "  report [days]"
        echo "  status           # server + cron + data overview"
        echo "  start-server     # mvn spring-boot:run (nohup, waits for health)"
        echo "  stop-server"
        echo "  restart-server"
        echo "  start-cron       # macOS launchd every 60s"
        echo "  stop-cron"
        echo ""
        echo "  env: SKILL_MONITOR_URL=http://localhost:8090"
        echo "       SKILL_MONITOR_DIR=~/bage/github/study/study-ai-skills/monitor/skill-monitor-server"
        exit 0 ;;
esac
