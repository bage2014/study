---
name: "spring-ai-chat"
description: "集成 Spring AI 聊天功能。当用户想要与 AI 模型对话、创建聊天端点或处理 AI 对话流程时使用此技能。"
---

# Spring AI 聊天集成

此技能帮助将 Spring AI 聊天功能集成到项目中。

## 何时使用

在以下情况调用此技能：
- 创建聊天端点或 API
- 集成新的 AI 聊天模型
- 添加聊天相关功能
- 处理 AI 对话流程

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

## 添加新的聊天功能

1. 在 `ChatService.java` 中添加方法
2. 在 `com.bage.study.ai.best.practice.hello.model` 包中创建请求/响应 DTO
3. 在 `ChatController.java` 中添加控制器端点

## 配置

编辑 `application.yml`：
```yaml
spring:
  ai:
    openai:
      base-url: ${DEEPSEEK_BASE_URL}
      api-key: ${DEEPSEEK_API_KEY}
      chat:
        options:
          model: deepseek-chat
          temperature: 0.7
```
