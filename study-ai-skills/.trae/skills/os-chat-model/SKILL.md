---
name: "os-chat-model"
description: "聊天模型技能 - 支持多种AI模型切换"
---

# 聊天模型技能

## 功能描述

提供统一的聊天模型接口，支持多种 AI 模型切换，包括 OpenAI、DeepSeek、Claude 等。

## 何时使用

需要调用 AI 聊天功能时，通过配置选择不同模型。

## 核心功能

- 统一的聊天接口
- 多模型支持
- API Key 管理
- 运行时模型切换

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| message | String | 是 | 用户消息 |
| model | String | 否 | 模型名称 |
| temperature | Number | 否 | 温度参数（0-1） |
| maxTokens | Number | 否 | 最大 token 数 |
| implementation | String | 否 | 指定实现（openai/deepseek/claude） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "implementation": "deepseek",
  "model": "deepseek-chat",
  "content": "AI 回复内容",
  "usage": {
    "promptTokens": 100,
    "completionTokens": 50
  }
}
```

## 实现配置

在 `.trae/config.yml` 中配置：

```yaml
skills:
  open-source:
    os-chat-model:
      active: deepseek
      implementations:
        - name: openai
          description: OpenAI API
          enabled: true
          env:
            - OPENAI_API_KEY
            - OPENAI_BASE_URL
        - name: deepseek
          description: DeepSeek API
          enabled: true
          env:
            - DEEPSEEK_API_KEY
            - DEEPSEEK_BASE_URL
        - name: claude
          description: Claude API
          enabled: true
          env:
            - CLAUDE_API_KEY
```

## 可用实现

| 实现名称 | 描述 | 状态 | 环境变量 |
|----------|------|------|----------|
| openai | OpenAI API | ✅ | OPENAI_API_KEY, OPENAI_BASE_URL |
| deepseek | DeepSeek API | ✅ | DEEPSEEK_API_KEY, DEEPSEEK_BASE_URL |
| claude | Claude API | ✅ | CLAUDE_API_KEY |

## 使用流程

1. 设置对应 API Key 环境变量
2. 在配置文件中选择 active 实现
3. 调用技能进行聊天

## 最佳实践

- 开发环境使用 deepseek（免费/低成本）
- 生产环境根据需求选择模型
- 使用环境变量管理 API Key，不要硬编码

## 配置要求

### 环境变量

| 变量名 | 说明 |
|--------|------|
| OPENAI_API_KEY | OpenAI API Key |
| OPENAI_BASE_URL | OpenAI API 地址 |
| DEEPSEEK_API_KEY | DeepSeek API Key |
| DEEPSEEK_BASE_URL | DeepSeek API 地址 |
| CLAUDE_API_KEY | Claude API Key |

### 配置文件

```yaml
skills:
  open-source:
    os-chat-model:
      active: deepseek
```

## 扩展指南

### 添加新模型

1. 创建实现技能 `os-chat-model-impl-{name}`
2. 在配置文件中添加实现定义
3. 更新可用实现列表

### 切换模型

```bash
# 切换到 OpenAI
sed -i '' 's/active: deepseek/active: openai/' .trae/config.yml

# 设置环境变量
export OPENAI_API_KEY="your-api-key"
```