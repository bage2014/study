# AGENTS.md — AI 编码助手规范

本文件定义 AI 在本项目中应遵循的约定。

## 项目概述

- 项目名称：study-ai-skills-test
- 目标：对当前会话可用的全部技能（Skill）进行单元测试
- 技术栈：Shell（bash 3.2+，macOS 原生兼容）

## 目录结构

```
study-ai-skills-test/
├── AGENTS.md                 # 本文件
└── tests/
    ├── README.md             # 测试说明
    ├── run_tests.sh          # 测试入口
    ├── lib/test_framework.sh # 断言框架
    └── skills/               # 每个技能一个 .sh 测试脚本
```

## 编码规则

### 必须遵循
- 测试脚本使用纯 shell（bash），不引入 Python/Node 等外部运行时
- 兼容 macOS 自带 bash 3.2，不使用关联数组、`declare -A`、`mapfile` 等 bash 4+ 特性
- 脚本头部使用 `#!/usr/bin/env bash` 与 `set -uo pipefail`（不使用 `set -e`，断言失败需继续执行）
- 技能测试脚本必须 `source tests/lib/test_framework.sh`，并以 `summary` 结尾
- 通过 `find_skill_dir` 发现技能目录，不硬编码绝对路径

### 禁止操作
- 不要在测试脚本中硬编码用户家目录以外的绝对路径
- 不要在断言失败时 `exit`，由 `summary` 统一返回退出码
- 不要修改 `tests/lib/test_framework.sh` 中的断言签名（已有测试脚本依赖）

## 技能测试使用指引

- 运行全部测试：`./tests/run_tests.sh`
- 运行单个技能：`./tests/run_tests.sh <skill-name>`
- 新增技能测试：复制 `tests/skills/common-coding.sh` 模板，改 `SKILL` 变量即可
- 详细说明见 [tests/README.md](tests/README.md)

## 文档约定

- 测试说明文档：`tests/README.md`
- 不主动创建其他文档，除非用户明确要求
