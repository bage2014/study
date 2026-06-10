---
name: "spring-ai-chat"
description: "集成 Spring AI 聊天功能。当用户想要与 AI 模型对话、创建聊天端点或处理 AI 对话流程时使用此技能。"
---

# Spring AI 聊天集成

## 功能描述

提供 AI 聊天交互能力，支持与多种大语言模型进行对话，基于 Spring AI 框架实现。

## 何时使用

在以下情况调用此技能：
- 创建聊天端点或 API
- 集成新的 AI 聊天模型
- 添加聊天相关功能
- 处理 AI 对话流程

## 核心功能

- **多模型支持**：支持 OpenAI、DeepSeek 等多种 AI 模型
- **聊天对话**：支持简单聊天和完整对话流程
- **参数配置**：支持温度、token 数量等参数配置
- **流式响应**：支持流式输出模式

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| model | String | 否 | 模型名称（默认使用配置的模型） |
| message | String | 是 | 用户消息 |
| temperature | Double | 否 | 温度参数（0-1，默认0.7） |
| maxTokens | Integer | 否 | 最大 token 数（默认2048） |
| options | Map | 否 | 其他配置选项 |

## 输出格式

```json
{
  "provider": "deepseek",
  "content": "AI 回复内容",
  "model": "deepseek-chat",
  "usage": {
    "promptTokens": 100,
    "completionTokens": 50
  },
  "success": true,
  "errorMessage": null
}
```

## 支持模型

| 模型类型 | 模型名称 | 配置方式 |
|----------|----------|----------|
| DeepSeek | deepseek-chat | 环境变量配置 |
| OpenAI | gpt-3.5-turbo | 环境变量配置 |
| OpenAI | gpt-4 | 环境变量配置 |

## API 调用示例

```bash
# 简单聊天
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "你好，AI！"
  }'

# 指定模型
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "deepseek",
    "message": "解释一下量子计算",
    "temperature": 0.7,
    "maxTokens": 2048
  }'

# 流式响应
curl -X POST http://localhost:8080/api/chat/stream \
  -H "Content-Type: application/json" \
  -d '{
    "message": "写一首诗"
  }'
```

## 使用模式

```java
// 注入 ChatService
private final ChatService chatService;

// 简单聊天
ChatResponse response = chatService.chat("你好 AI！");

// 指定模型
ChatResponse response = chatService.chat("deepseek", "你好！");

// 完整请求
ChatRequest request = new ChatRequest("deepseek", "你好", 0.7, 2048, Map.of());
ChatResponse response = chatService.chat(request);
```

## 核心组件

| 组件 | 职责 | 文件路径 |
|------|------|----------|
| ChatService | 聊天服务接口 | `service/ChatService.java` |
| ChatServiceImpl | 聊天服务实现 | `service/impl/ChatServiceImpl.java` |
| ChatController | REST API 控制层 | `controller/ChatController.java` |
| ChatRequest | 请求 DTO | `model/ChatRequest.java` |
| ChatResponse | 响应 DTO | `model/ChatResponse.java` |

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `DEEPSEEK_API_KEY` | DeepSeek API Key | - |
| `DEEPSEEK_BASE_URL` | DeepSeek API 地址 | https://api.deepseek.com/v1 |
| `OPENAI_API_KEY` | OpenAI API Key | - |
| `OPENAI_BASE_URL` | OpenAI API 地址 | https://api.openai.com/v1 |
| `SERVER_PORT` | 服务端口 | 8080 |

### 配置文件

```yaml
# application.yml
server:
  port: ${SERVER_PORT:8080}

spring:
  ai:
    openai:
      base-url: ${DEEPSEEK_BASE_URL:https://api.deepseek.com/v1}
      api-key: ${DEEPSEEK_API_KEY}
      chat:
        options:
          model: deepseek-chat
          temperature: 0.7
          max-tokens: 2048
```

## 添加新的聊天功能

1. 在 `ChatService.java` 中添加方法
2. 在 `model` 包中创建请求/响应 DTO
3. 在 `ChatController.java` 中添加控制器端点

## 扩展指南

### 添加新的模型提供者

1. 实现 `ModelProvider` 接口
2. 创建对应的配置类
3. 在 `ModelProviderManager` 中注册

### 自定义聊天逻辑

继承 `ChatServiceImpl` 并重写相关方法，实现自定义业务逻辑。

## 目录结构

```
src/main/java/com/bage/study/ai/best/practice/hello/
├── controller/
│   └── ChatController.java
├── service/
│   ├── ChatService.java
│   └── impl/
│       └── ChatServiceImpl.java
├── model/
│   ├── ChatRequest.java
│   └── ChatResponse.java
├── provider/
│   └── ModelProvider.java
└── config/
    └── ChatConfig.java
```