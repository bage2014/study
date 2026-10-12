---
name: "common-test-driven-development"
description: "TDD 测试驱动开发技能，遵循红绿重构循环，先写测试再写实现（包装自 obra/superpowers@test-driven-development）"
trigger: "用户提到 TDD、测试驱动、红绿重构、先写测试时"
disable-when: "原型代码或配置文件等不适用 TDD 的场景"
category: "common"
tags: ["tdd", "test-driven", "red-green-refactor", "testing", "best-practices"]
requires: ["common-backend-unit-test", "common-coding", "common-systematic-debugging"]
---

# TDD 测试驱动开发技能

## 功能描述

引导开发者遵循 TDD（Test-Driven Development）模式，按「红-绿-重构」循环开发，确保每个功能都经过测试验证。

## 触发条件

- 用户提到 TDD、测试驱动、红绿重构时
- 用户要求"先写测试再写代码"时
- 开发前明确要遵循 TDD 流程时

## 何时使用

- 新功能开发时
- Bug 修复时
- 代码重构时
- 行为变更时

## 何时不使用

- 一次性原型代码（需人工确认）
- 生成的代码
- 纯配置文件

## 核心原则

```
NO PRODUCTION CODE WITHOUT A FAILING TEST FIRST
```

没有失败测试就不写生产代码。先写代码再写测试 = 删除重来。

## 核心功能

- **红绿重构循环**：严格遵循 RED → GREEN → REFACTOR
- **失败测试先行**：强制在写实现前确认测试失败
- **验证清单**：每步都有验证点防止跳过
- **Bug 修复流程**：先写复现测试再修
- **红线警告**：识别自我欺骗的借口

## 红绿重构循环详解

### RED - 写失败测试

写一个最小测试，展示期望行为。

**好的测试：**
- 一件事一个测试
- 清晰的测试名称（描述行为，不是方法名）
- 用真实代码测试（非必要不用 mock）

### Verify RED - 确认失败

**必须执行，不能跳过。**

```bash
# 运行单个测试，确认它失败
npm test path/to/test.test.ts
# 或
mvn test -Dtest=ClassName#methodName
```

检查：测试确实失败（不是语法错误），失败原因正确（功能缺失）。

### GREEN - 写最小实现

写最简单的代码让测试通过。

- 只写够让当前测试通过的代码
- 不要加额外功能（YAGNI）
- 不要重构其他代码

### Verify GREEN - 确认通过

**必须执行。**

```bash
# 运行完整测试套件，确认全部通过
npm test
# 或
mvn test
```

检查：目标测试通过 + 其他测试不被破坏。

### REFACTOR - 清理重构

只在绿色状态下重构：
- 消除重复
- 改善命名
- 提取辅助方法

重构后必须保持全部测试通过。

## 好的测试特征

| 质量 | 好 | 坏 |
|------|---|---|
| 最小化 | 一件事，名字里有 "and" 就该拆分 | 一个测试验证多个行为 |
| 清晰 | 名字描述期望行为 | `test1`, `test_retry` |
| 展示意图 | 用测试说明 API 应该怎么用 | 用 mock 掩盖设计问题 |

## Bug 修复流程

1. 写一个失败测试复现 bug（RED）
2. 验证测试确实因为 bug 而失败（Verify RED）
3. 写最小修复代码（GREEN）
4. 测试通过（Verify GREEN）
5. 清理重构（REFACTOR）
6. 提交修复 + 测试（防止回归）

**铁律：不写测试就不修 bug。**

## 红线警告 - 停下重来

如果发现自己在找这些借口，**删掉代码，回到测试优先：**

| 借口 | 真相 |
|------|------|
| "太简单不用测" | 简单代码也会坏，测试只要 30 秒 |
| "先写代码后补测" | 后写的测试永远通过，证明不了能抓 bug |
| "手动测过了" | 手动测试无记录、不可重复、易漏边界 |
| "删掉 X 小时太浪费" | 沉没成本谬误，删掉才是对的 |
| "作为参考留着" | 你会忍不住适配它，这就是后写测试 |

## TDD 常见困境

| 问题 | 解法 |
|------|------|
| 不知道怎么测试 | 先写你期望的 API，先写断言，逐步补全 |
| 测试太复杂 | 说明设计太复杂，简化接口 |
| 必须 mock 所有东西 | 耦合太重，用依赖注入解耦 |
| 测试 setup 巨大 | 提取测试工具类；还是复杂就简化设计 |

## 验证清单

完成前必须全部打勾：

- [ ] 每个新方法/函数都有测试
- [ ] 每个测试实现前都看到过它失败
- [ ] 失败原因正确（功能缺失，不是拼写错）
- [ ] 实现代码是让测试通过的最小代码
- [ ] 完整测试套件全部通过
- [ ] 输出干净（无错误、无警告）
- [ ] 用真实代码测试（mock 仅在不可避免时用）
- [ ] 边界条件和异常路径都覆盖

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feature | String | 是 | 要实现的功能描述 |
| language | String | 否 | 编程语言 |
| framework | String | 否 | 测试框架（Jest/Pytest/JUnit 等） |
| scope | String | 否 | 作用域：unit/integration/e2e，默认 unit |

## 输出格式

```json
{
  "cycle": "RED",
  "testFile": "path/to/test.test.ts",
  "productionFile": "path/to/production.ts",
  "testCode": "测试代码",
  "productionCode": "生产代码",
  "verification": {
    "redVerified": true,
    "greenVerified": true,
    "refactorDone": false
  }
}
```

## 与现有 skill 的关系

- **common-backend-unit-test**：提供后端单元测试框架（JUnit/Mockito）
- **common-coding**：提供编码规范指导
- **common-systematic-debugging**：bug 修复时两者配合使用

## 参考来源

原始 skill：`obra/superpowers@test-driven-development`
来源仓库：https://github.com/obra/superpowers
安装命令：`npx skills add obra/superpowers@test-driven-development -y -g`
