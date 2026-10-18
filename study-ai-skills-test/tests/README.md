# 技能单元测试说明

本目录是技能（Skill）的单元测试套件，纯 shell 实现，可在 macOS 直接运行。

## 目录结构

```
tests/
├── run_tests.sh              # 测试入口（单跑 / 全跑）
├── README.md                 # 本说明文档
├── lib/
│   └── test_framework.sh     # 测试框架：断言、技能目录发现、汇总
└── skills/
    └── <skill-name>.sh       # 每个技能一个测试脚本
```

## 运行方式

```bash
# 运行全部技能测试
./tests/run_tests.sh

# 运行单个技能测试
./tests/run_tests.sh common-coding

# 运行多个指定技能测试
./tests/run_tests.sh common-coding common-project-standard
```

退出码：全部通过返回 `0`，任一失败返回 `1`。

## 测试框架（lib/test_framework.sh）

每个技能测试脚本 `source` 框架后，调用断言函数，最后调用 `summary` 输出结果。

### 可用断言

| 函数 | 说明 |
|------|------|
| `assert_file_exists <path>` | 文件存在 |
| `assert_dir_exists <path>` | 目录存在 |
| `assert_frontmatter_has <file> <key>` | SKILL.md frontmatter 含指定字段 |
| `assert_frontmatter_eq <file> <key> <value>` | frontmatter 字段值等于期望值 |
| `assert_name_matches_dir <skill_md>` | frontmatter 的 `name` 等于所在目录名 |
| `assert_section <file> <heading>` | 存在 `## <heading>` 章节 |

### 技能目录发现

`find_skill_dir <skill-name>` 自动扫描以下本机技能根目录：

- `~/.trae-cn/skills`
- `~/.trae-cn/builtin_skills`
- `~/.trae-cn/builtin/global/skills`
- `~/.trae-cn/builtin/trae/doutops/skills`
- `~/.trae-cn/plugins/trae-remote-official/lark/1.0.5/skills`

如需覆盖，设置环境变量 `SKILL_ROOTS`（空格分隔的多个目录）。

## 新增一个技能测试

1. 在 `tests/skills/` 下新建 `<skill-name>.sh`，参考 `common-coding.sh`。
2. 模板：

```bash
#!/usr/bin/env bash
set -uo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/test_framework.sh"

SKILL="<skill-name>"
tf_header "$SKILL"

SKILL_DIR="$(find_skill_dir "$SKILL")"
if [ -z "$SKILL_DIR" ]; then
  tf_skip "skill directory not found"
  summary
  exit 0
fi
SKILL_MD="$SKILL_DIR/SKILL.md"

assert_file_exists "$SKILL_MD"
assert_frontmatter_has "$SKILL_MD" name
assert_frontmatter_has "$SKILL_MD" description
assert_frontmatter_has "$SKILL_MD" trigger
assert_name_matches_dir "$SKILL_MD"
assert_section "$SKILL_MD" "功能描述"
assert_section "$SKILL_MD" "何时使用"
assert_section "$SKILL_MD" "核心功能"

summary
```

3. `chmod +x tests/skills/<skill-name>.sh`
4. `./tests/run_tests.sh <skill-name>` 验证

## 设计约定

- 每个测试脚本**自包含**：可独立 `bash tests/skills/xxx.sh` 运行，不依赖外部参数。
- 断言**不中断**：某条断言失败后继续执行后续断言，由 `summary` 统一汇总。
- 找不到技能目录时用 `tf_skip` 跳过，不算失败。
- 框架兼容 macOS 自带 bash 3.2，不使用 bash 4+ 特性。
