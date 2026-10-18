#!/bin/bash
# ============================================================
# Skill 监控埋点批量注入脚本
# 在每个 SKILL.md 的 frontmatter 之后、正文之前插入监控引导
# ============================================================

SKILLS_DIR="${HOME}/.trae-cn/skills"
MONITOR_BLOCK='
<!-- ⚠️ 使用监控埋点（必须执行） -->
<!-- 任何 Agent 加载本 Skill 时，在执行核心功能前必须先运行以下命令 -->
> **📊 使用监控：** 在执行本 Skill 的**任何核心功能之前**，**必须先运行**以下命令记录调用：
> ```bash
> ~/.trae-cn/scripts/skill-usage.sh log {skill-name} --trigger auto
> ```
> 如果脚本不存在（非本机环境），请跳过此步骤。此记录用于 Skill 使用分析。

'

echo "=== 扫描所有 SKILL.md ==="
total=0
already=0
patched=0

for skill_dir in "$SKILLS_DIR"/*/; do
  skill_md="${skill_dir}SKILL.md"
  [ ! -f "$skill_md" ] && continue
  
  total=$((total + 1))
  skill_name=$(basename "$skill_dir")
  
  # 已注入过？
  if grep -q "使用监控埋点" "$skill_md" 2>/dev/null; then
    already=$((already + 1))
    continue
  fi
  
  # 找 frontmatter 结束位置（--- 分隔）
  # frontmatter 从第 1 行 --- 开始，到下一行 --- 结束
  first_sep=$(grep -n '^---$' "$skill_md" | head -1 | cut -d: -f1)
  second_sep=$(grep -n '^---$' "$skill_md" | head -2 | tail -1 | cut -d: -f1)
  
  if [ -z "$first_sep" ] || [ -z "$second_sep" ] || [ "$first_sep" = "$second_sep" ]; then
    echo "  ⚠️  $skill_name: 找不到 frontmatter 分隔符，跳过"
    continue
  fi
  
  # 替换 {skill-name} 占位符
  local_block="${MONITOR_BLOCK//\{skill-name\}/$skill_name}"
  
  # 读取 frontmatter 部分 + 后续正文
  head -n "$second_sep" "$skill_md" > /tmp/skill_fm_$$.md
  tail -n +$((second_sep + 1)) "$skill_md" > /tmp/skill_body_$$.md
  
  # 组合：frontmatter + 空行 + 监控块 + 正文
  {
    cat /tmp/skill_fm_$$.md
    echo ""
    echo "$local_block"
    cat /tmp/skill_body_$$.md
  } > "${skill_md}.tmp"
  
  mv "${skill_md}.tmp" "$skill_md"
  
  rm -f /tmp/skill_fm_$$.md /tmp/skill_body_$$.md
  
  patched=$((patched + 1))
  echo "  ✅ $skill_name"
done

echo ""
echo "=== 完成 ==="
echo "总计: $total 个 Skill"
echo "已注入: $patched"
echo "已存在: $already"
echo "跳过: $((total - patched - already))"
