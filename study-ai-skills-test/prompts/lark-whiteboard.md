# lark-whiteboard 触发验证提示词

> 用法：将下方「提示词」整段复制到 TRAE 对话窗口发送，观察 lark-whiteboard 是否被真实触发调用。

## 提示词

<!-- 复制以下内容 -->

---

请使用 **lark-whiteboard** 技能完成下面的任务。

任务：创建一个测试白板

执行要求：
1. 通过 Skill 工具加载 lark-whiteboard 技能；若 SKILL.md 中要求执行使用监控埋点命令（~/.trae-cn/scripts/skill-usage.sh log lark-whiteboard --trigger auto），必须先执行该命令
2. 严格按照该技能的规范/流程执行任务
3. 执行完成后，输出「技能执行响应」，包含：
   - 技能加载状态（Skill Path）
   - 埋点命令执行结果
   - 任务核心产出

---

## 预期验证点

- [ ] 回复中出现技能加载信息（如 Skill Path 指向 lark-whiteboard）
- [ ] 埋点命令被执行（可另开终端执行 tail -n 1 ~/.trae-cn/skill-usage.jsonl 查看最新记录）
- [ ] 输出了技能执行响应 / 任务产出

## 执行响应记录

- 日期：
- 结果：触发 / 未触发
- 响应摘要：
