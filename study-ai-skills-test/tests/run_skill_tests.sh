#!/usr/bin/env bash
# Skill test re-verification runner (thin wrapper around verify_skills.py).
#
# Usage:
#   ./run_skill_tests.sh list                  # list all skills + status
#   ./run_skill_tests.sh show <skill-id>       # show prompt + assertions
#   ./run_skill_tests.sh run <skill-id>        # print the test prompt to send
#   ./run_skill_tests.sh run-all               # walk all untested cases
#   ./run_skill_tests.sh record <skill> <pass|fail> "<notes>"
#   ./run_skill_tests.sh report                # summary report
#   ./run_skill_tests.sh regen                 # regenerate all test cases from inventory
set -euo pipefail
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PY="${DIR}/verify_skills.py"

cmd="${1:-}"
shift || true

case "$cmd" in
  list)     python3 "$PY" --list "$@" ;;
  show)     python3 "$PY" --show "$@" ;;
  run)      python3 "$PY" --run "$@" ;;
  run-all)  python3 "$PY" --run-all --only-untested ;;
  record)   python3 "$PY" --record "$@" ;;
  report)   python3 "$PY" --report ;;
  regen)    python3 "${DIR}/../tools/generate_skill_tests.py" ;;
  ""|help|-h|--help)
    sed -n '2,12p' "${BASH_SOURCE[0]}"
    ;;
  *)
    echo "Unknown command: $cmd" >&2
    "${BASH_SOURCE[0]}" help
    exit 1
    ;;
esac
