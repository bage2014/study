#!/usr/bin/env bash
# run_tests.sh — 技能单元测试入口
# 用法：
#   ./run_tests.sh                 # 运行 tests/skills/ 下全部测试
#   ./run_tests.sh common-coding   # 运行指定技能测试
#   ./run_tests.sh a b c           # 运行多个指定技能测试
# 退出码：全部通过为 0，任一失败为 1。
set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SKILLS_DIR="$SCRIPT_DIR/skills"

if [ ! -d "$SKILLS_DIR" ]; then
  echo "ERROR: skills test dir not found: $SKILLS_DIR" >&2
  exit 2
fi

run_one() {
  local name="$1" file="$SKILLS_DIR/$name.sh"
  if [ ! -f "$file" ]; then
    echo "${C_RED:-}ERROR: test script not found: $file${C_RESET:-}" >&2
    return 2
  fi
  bash "$file"
}

if [ $# -eq 0 ]; then
  echo "Running all skill tests..."
  rc=0
  for f in "$SKILLS_DIR"/*.sh; do
    [ -e "$f" ] || continue
    bash "$f" || rc=1
  done
  exit $rc
else
  rc=0
  for name in "$@"; do
    run_one "$name" || rc=1
  done
  exit $rc
fi
