#!/usr/bin/env bash
# test_framework.sh — 轻量 shell 单元测试框架（macOS / bash 3.2+ 兼容）
# 提供：彩色输出、断言函数、技能目录发现、汇总退出码。
# 用法：在技能测试脚本中 `source` 本文件，调用 assert_*，最后调用 summary。

# ---------- 颜色 ----------
if [ -t 1 ]; then
  C_RED=$'\033[31m'; C_GREEN=$'\033[32m'; C_YELLOW=$'\033[33m'
  C_BLUE=$'\033[34m'; C_RESET=$'\033[0m'
else
  C_RED=""; C_GREEN=""; C_YELLOW=""; C_BLUE=""; C_RESET=""
fi

# ---------- 计数器 ----------
TF_PASS=0
TF_FAIL=0
TF_SKIP=0
TF_CURRENT_SKILL=""

# ---------- 技能根目录（可通过环境变量覆盖） ----------
# 默认扫描本机常见的技能安装位置
if [ -z "${SKILL_ROOTS:-}" ]; then
  SKILL_ROOTS=(
    "$HOME/.trae-cn/skills"
    "$HOME/.trae-cn/builtin_skills"
    "$HOME/.trae-cn/builtin/global/skills"
    "$HOME/.trae-cn/builtin/trae/doutops/skills"
    "$HOME/.trae-cn/plugins/trae-remote-official/lark/1.0.5/skills"
  )
fi

# ---------- 基础输出 ----------
tf_pass() { TF_PASS=$((TF_PASS+1)); echo "${C_GREEN}  PASS${C_RESET}: $1"; }
tf_fail() { TF_FAIL=$((TF_FAIL+1)); echo "${C_RED}  FAIL${C_RESET}: $1"; }
tf_skip() { TF_SKIP=$((TF_SKIP+1)); echo "${C_YELLOW}  SKIP${C_RESET}: $1"; }

tf_header() {
  TF_CURRENT_SKILL="$1"
  echo ""
  echo "${C_BLUE}=== Testing skill: $1 ===${C_RESET}"
}

# ---------- 技能目录发现 ----------
# find_skill_dir <skill_name> -> 输出目录路径，找不到返回非 0
find_skill_dir() {
  local name="$1" root
  for root in "${SKILL_ROOTS[@]}"; do
    if [ -d "$root/$name" ]; then
      echo "$root/$name"
      return 0
    fi
  done
  return 1
}

# ---------- 断言 ----------
# assert_file_exists <path>
assert_file_exists() {
  if [ -f "$1" ]; then
    tf_pass "file exists: $(basename "$1")"
  else
    tf_fail "file missing: $1"
  fi
}

# assert_dir_exists <path>
assert_dir_exists() {
  if [ -d "$1" ]; then
    tf_pass "dir exists: $(basename "$1")"
  else
    tf_fail "dir missing: $1"
  fi
}

# 提取 SKILL.md 的 frontmatter 块（两个 --- 之间），输出到 stdout
_extract_frontmatter() {
  local file="$1"
  awk '
    /^---$/ { if (in_fm) { exit } else { in_fm=1; next } }
    in_fm { print }
  ' "$file"
}

# assert_frontmatter_has <file> <key>
assert_frontmatter_has() {
  local file="$1" key="$2"
  if [ ! -f "$file" ]; then tf_fail "frontmatter[$key]: file missing"; return; fi
  if _extract_frontmatter "$file" | grep -qE "^${key}:"; then
    tf_pass "frontmatter has key: $key"
  else
    tf_fail "frontmatter missing key: $key"
  fi
}

# assert_frontmatter_eq <file> <key> <expected_value>
assert_frontmatter_eq() {
  local file="$1" key="$2" expected="$3" actual
  if [ ! -f "$file" ]; then tf_fail "frontmatter[$key]: file missing"; return; fi
  actual="$(_extract_frontmatter "$file" | grep -E "^${key}:" | head -1 | sed -E "s/^${key}:[[:space:]]*//;s/^\"//;s/\"$//")"
  if [ "$actual" = "$expected" ]; then
    tf_pass "frontmatter $key = \"$expected\""
  else
    tf_fail "frontmatter $key expected \"$expected\" but got \"$actual\""
  fi
}

# assert_name_matches_dir <skill_md_path>
# frontmatter 的 name 必须等于所在目录名
assert_name_matches_dir() {
  local file="$1" dir expected actual
  dir="$(basename "$(dirname "$file")")"
  expected="$dir"
  if [ ! -f "$file" ]; then tf_fail "name-match: file missing"; return; fi
  actual="$(_extract_frontmatter "$file" | grep -E '^name:' | head -1 | sed -E 's/^name:[[:space:]]*//;s/^"//;s/"$//')"
  if [ "$actual" = "$expected" ]; then
    tf_pass "name \"$actual\" matches directory"
  else
    tf_fail "name \"$actual\" does not match directory \"$expected\""
  fi
}

# assert_section <file> <section_heading>
# 检查是否存在 ## <heading>（允许 ### 等更多层级）
assert_section() {
  local file="$1" section="$2"
  if [ ! -f "$file" ]; then tf_fail "section[$section]: file missing"; return; fi
  if grep -qE "^##+[[:space:]]+${section}" "$file"; then
    tf_pass "has section: $section"
  else
    tf_fail "missing section: $section"
  fi
}

# ---------- 汇总 ----------
summary() {
  local total=$((TF_PASS + TF_FAIL + TF_SKIP))
  echo ""
  if [ "$TF_FAIL" -eq 0 ]; then
    echo "${C_GREEN}RESULT: PASS${C_RESET}  (pass=$TF_PASS, fail=$TF_FAIL, skip=$TF_SKIP, total=$total)  [$TF_CURRENT_SKILL]"
    return 0
  else
    echo "${C_RED}RESULT: FAIL${C_RESET}  (pass=$TF_PASS, fail=$TF_FAIL, skip=$TF_SKIP, total=$total)  [$TF_CURRENT_SKILL]"
    return 1
  fi
}
