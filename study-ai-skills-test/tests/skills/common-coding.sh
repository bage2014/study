#!/usr/bin/env bash
# 单元测试：common-coding
# 验证该技能的 SKILL.md 结构完整性（frontmatter + 关键章节）。
set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/test_framework.sh"

SKILL="common-coding"

tf_header "$SKILL"

SKILL_DIR="$(find_skill_dir "$SKILL")"
if [ -z "$SKILL_DIR" ]; then
  tf_skip "skill directory not found in SKILL_ROOTS (set SKILL_ROOTS env to override)"
  summary
  exit 0
fi

SKILL_MD="$SKILL_DIR/SKILL.md"

# 1. 文件与目录
assert_file_exists "$SKILL_MD"

# 2. frontmatter 必填字段
assert_frontmatter_has "$SKILL_MD" name
assert_frontmatter_has "$SKILL_MD" description
assert_frontmatter_has "$SKILL_MD" trigger

# 3. name 必须与目录名一致
assert_name_matches_dir "$SKILL_MD"

# 4. 关键章节
assert_section "$SKILL_MD" "功能描述"
assert_section "$SKILL_MD" "何时使用"
assert_section "$SKILL_MD" "核心功能"

summary
