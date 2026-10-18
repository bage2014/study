#!/usr/bin/env python3
"""
Skill test runner / re-verification tool.

Skills are invoked by an AI assistant through natural-language prompts; this tool
manages the test suite: list cases, show the exact prompt + assertions, and record
pass/fail results back into the per-skill case files.

Usage:
  python3 verify_skills.py --list
  python3 verify_skills.py --list --category lark
  python3 verify_skills.py --show common-coding
  python3 verify_skills.py --run common-coding            # prints the prompt to send the assistant
  python3 verify_skills.py --run-all                      # walk all untested cases
  python3 verify_skills.py --record common-coding pass "produced null-safe code"
  python3 verify_skills.py --report
"""
import argparse
import json
import os
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
MANIFEST = os.path.join(HERE, "manifest.json")
CASES_DIR = os.path.join(HERE, "cases")


def load_manifest():
    with open(MANIFEST, encoding="utf-8") as f:
        return json.load(f)


def case_path(sid):
    return os.path.join(CASES_DIR, f"{sid}.test.md")


def read_case(sid):
    p = case_path(sid)
    if not os.path.exists(p):
        return None
    with open(p, encoding="utf-8") as f:
        return f.read()


def get_status(case_text):
    for line in case_text.splitlines():
        if line.startswith("- **Status**:"):
            return line.split(":", 1)[1].strip()
    return "unknown"


def set_status_and_note(sid, status, note):
    p = case_path(sid)
    with open(p, encoding="utf-8") as f:
        c = f.read()
    import re
    c = re.sub(r"- \*\*Status\*\*: .*", f"- **Status**: {status}", c, count=1)
    # replace the notes section content
    marker = "## Notes / Actual result"
    idx = c.find(marker)
    if idx != -1:
        c = c[: idx + len(marker)] + "\n\n" + note + "\n"
    with open(p, "w", encoding="utf-8") as f:
        f.write(c)


def cmd_list(m, category, status_filter):
    rows = []
    for s in m["skills"]:
        if category and s["category"] != category:
            continue
        txt = read_case(s["id"]) or ""
        st = get_status(txt)
        if status_filter and st != status_filter:
            continue
        rows.append((s["id"], s["category"], st, s["requires_external"]))
    print(f"{'ID':<35} {'CATEGORY':<10} {'STATUS':<10} {'EXT':<4}")
    print("-" * 62)
    for r in rows:
        print(f"{r[0]:<35} {r[1]:<10} {r[2]:<10} {'yes' if r[3] else 'no':<4}")
    print(f"\nTotal: {len(rows)}")


def cmd_show(m, sid):
    s = next((x for x in m["skills"] if x["id"] == sid), None)
    if not s:
        print(f"Skill not found: {sid}", file=sys.stderr)
        sys.exit(1)
    print(f"=== {s['id']} ({s['category']}) ===")
    print(f"Description: {s['description']}")
    print(f"Trigger: {s['trigger']}")
    print(f"Requires external setup: {'yes' if s['requires_external'] else 'no'}")
    print("\nPrerequisites:")
    for p in s["prerequisites"]:
        print(f"  - {p}")
    print("\n--- TEST PROMPT (send to assistant) ---")
    print(s["test_prompt"])
    print("\n--- EXPECTED BEHAVIOR ---")
    print(s["expected_behavior"])
    print("\n--- ASSERTIONS ---")
    for a in s["assertions"]:
        print(f"  [ ] {a}")
    txt = read_case(sid) or ""
    print(f"\nCurrent status: {get_status(txt)}")


def cmd_run(m, sid):
    s = next((x for x in m["skills"] if x["id"] == sid), None)
    if not s:
        print(f"Skill not found: {sid}", file=sys.stderr)
        sys.exit(1)
    print(f"# Test: {s['id']}")
    print("# Send the following prompt to the assistant, then check assertions.\n")
    print(s["test_prompt"])
    print("\n# Assertions:")
    for a in s["assertions"]:
        print(f"#   - {a}")


def cmd_run_all(m, only_untested):
    for s in m["skills"]:
        txt = read_case(s["id"]) or ""
        st = get_status(txt)
        if only_untested and st != "untested":
            continue
        print("=" * 70)
        cmd_run(m, s["id"])
        print()


def cmd_record(m, sid, status, note):
    s = next((x for x in m["skills"] if x["id"] == sid), None)
    if not s:
        print(f"Skill not found: {sid}", file=sys.stderr)
        sys.exit(1)
    set_status_and_note(sid, status, note)
    print(f"Recorded {status} for {s['id']}")


def cmd_report(m):
    by_cat = {}
    by_status = {}
    ext = 0
    for s in m["skills"]:
        txt = read_case(s["id"]) or ""
        st = get_status(txt)
        by_cat.setdefault(s["category"], {"total": 0, "pass": 0, "fail": 0, "untested": 0})
        by_cat[s["category"]]["total"] += 1
        by_cat[s["category"]][st if st in ("pass", "fail", "untested") else "untested"] += 1
        by_status[st] = by_status.get(st, 0) + 1
        if s["requires_external"]:
            ext += 1
    total = len(m["skills"])
    passed = by_status.get("pass", 0)
    failed = by_status.get("fail", 0)
    untested = by_status.get("untested", 0)
    print(f"=== Skill Test Report ===")
    print(f"Total skills : {total}")
    print(f"Passed       : {passed}")
    print(f"Failed       : {failed}")
    print(f"Untested     : {untested}")
    print(f"Require ext  : {ext}")
    rate = (passed / total * 100) if total else 0
    print(f"Pass rate    : {rate:.1f}% ({passed}/{total})")
    print("\nBy category:")
    for cat, d in sorted(by_cat.items()):
        print(f"  {cat:<10} total={d['total']:<3} pass={d['pass']:<3} fail={d['fail']:<3} untested={d['untested']}")


def main():
    ap = argparse.ArgumentParser(description="Skill test suite runner")
    ap.add_argument("--list", action="store_true")
    ap.add_argument("--category")
    ap.add_argument("--status")
    ap.add_argument("--show")
    ap.add_argument("--run")
    ap.add_argument("--run-all", action="store_true")
    ap.add_argument("--only-untested", action="store_true")
    ap.add_argument("--record", nargs=3, metavar=("SKILL", "STATUS", "NOTE"))
    ap.add_argument("--report", action="store_true")
    args = ap.parse_args()

    m = load_manifest()

    if args.list:
        cmd_list(m, args.category, args.status)
    elif args.show:
        cmd_show(m, args.show)
    elif args.run:
        cmd_run(m, args.run)
    elif args.run_all:
        cmd_run_all(m, args.only_untested)
    elif args.record:
        cmd_record(m, args.record[0], args.record[1], args.record[2])
    elif args.report:
        cmd_report(m)
    else:
        ap.print_help()


if __name__ == "__main__":
    main()
