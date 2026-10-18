# 项目技能目录

本目录存放项目级 Trae 技能。

## 技能结构

```
skills/
└── {skill-name}/       # 小写连字符命名
    └── SKILL.md        # 技能定义文件（必填）
```

## 命名规范

| 类型 | 前缀 | 示例 |
|------|------|------|
| 个人技能 | `personal-` | `personal-backend-coding-standard` |
| 研发流程 | `common-` | `common-coding` |
| 项目规范 | `common-project-` | `common-project-standard` |
| 开源包装 | `os-` | `os-ui-library` |

## 注册流程

1. 在本目录创建技能子目录
2. 编写 `SKILL.md`（含 frontmatter：name / description / trigger / disable-when / category）
3. 在 [SKILLS.md](../SKILLS.md) 注册
4. 在 [AGENTS.md](../AGENTS.md) 第 3 节添加使用场景
5. 在 [.trae/config.yml](../.trae/config.yml) 加入 `enabled` 列表

详细规范见 `common-project-standard` 技能第 1 节。
