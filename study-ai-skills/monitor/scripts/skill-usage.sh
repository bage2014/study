#!/bin/bash
# ============================================================
# Trae Skill Monitor v2.0
#   log <skill> [--trigger auto|manual]   Append JSONL record
#   upload                                 Incremental upload with offset
#   report [--days N]                     Local analysis
#   start-cron / stop-cron                macOS launchd (every 60s)
# ============================================================

set -eo pipefail

JSONL_FILE="${HOME}/.trae-cn/skill-usage.jsonl"
UPLOAD_OFFSET="${HOME}/.trae-cn/skill-upload.offset"
MONITOR_URL="${SKILL_MONITOR_URL:-http://localhost:8090}"
UPLOAD_ENDPOINT="/api/skill-usage/batch"

mkdir -p "$(dirname "$JSONL_FILE")"

# ── log ─────────────────────────────────────────────────────
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

    local ts
    ts=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
    cat >> "$JSONL_FILE" << EOF
{"ts":"$ts","skill":"$skill_name","event":"$event","client":"trae-macos","session":"no-session","duration_ms":0,"status":"ok","trigger":"$trigger","user":"${USER:-unknown}"}
EOF
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

# ── main ────────────────────────────────────────────────────
case "${1:-}" in
    log|record)   shift; log_usage "$@" ;;
    upload|push)  shift; do_upload "$@" ;;
    report|show)  shift; do_report "$@" ;;
    start-cron)   install_cron ;;
    stop-cron)    uninstall_cron ;;
    *)
        echo "Trae Skill Monitor v2.0"
        echo "  log <skill> [--trigger auto|manual]"
        echo "  upload           # incremental -> Spring Boot"
        echo "  report [days]"
        echo "  start-cron       # macOS launchd every 60s"
        echo "  stop-cron"
        echo ""
        echo "  env: SKILL_MONITOR_URL=http://localhost:8090"
        exit 0 ;;
esac
