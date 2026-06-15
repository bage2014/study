---
name: "common-code-review"
description: "代码审查技能 - 提供代码质量检查和改进建议"
---

# 代码审查技能

## 功能描述

提供自动化代码审查服务，检查代码质量、安全性、规范性，并给出改进建议。

## 何时使用

- 提交代码前进行自检
- 代码审查会议前准备
- 团队代码规范执行检查

## 核心功能

- 代码风格检查
- 潜在 bug 识别
- 安全漏洞检测
- 性能优化建议
- 最佳实践建议

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| code | String | 是 | 需要审查的代码内容 |
| language | String | 否 | 编程语言（java/javascript/python等） |
| framework | String | 否 | 使用的框架（spring/vue/react等） |
| focus | Array | 否 | 审查重点（security/performance/style/bestpractice） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "totalIssues": 5,
  "issues": [
    {
      "severity": "HIGH",
      "category": "security",
      "line": 15,
      "message": "SQL 注入风险：直接拼接 SQL 语句",
      "suggestion": "使用预编译语句或 JPA 命名查询"
    },
    {
      "severity": "MEDIUM",
      "category": "performance",
      "line": 28,
      "message": "循环中频繁创建对象",
      "suggestion": "将对象创建移到循环外部"
    }
  ],
  "improvements": [
    "建议添加单元测试",
    "考虑使用 Lombok 简化代码"
  ]
}
```

## 使用流程

1. 输入代码片段或文件路径
2. 指定语言和框架（可选）
3. 选择审查重点（可选）
4. 获取审查报告和改进建议

## 最佳实践

- 定期对关键模块进行代码审查
- 将审查结果纳入持续集成流程
- 团队共享审查标准和改进策略

## 配置要求

无需额外配置

## 扩展指南

支持自定义审查规则和检查项。