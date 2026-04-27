---
name: "model-provider-creator"
description: "为多模型支持创建新的 AI 模型提供商。当用户想要添加对新的 AI 模型（OpenAI、Claude 等）的支持时使用此技能。"
---

# 模型提供商创建器

此技能帮助创建新的 AI 模型提供商以扩展多模型支持。

## 何时使用

在以下情况调用此技能：
- 为项目添加新的 AI 模型支持（OpenAI、Claude 等）
- 创建自定义模型提供商
- 扩展多模型架构
- 实现模型特定的聊天逻辑

## 提供商接口

```java
public interface ModelProvider {
    String getProviderName();        // 唯一名称（如 "openai"、"claude"）
    boolean supports(String modelType);  // 检查是否支持给定模型
    ChatResponse chat(ChatRequest request);  // 处理聊天请求
    default boolean isAvailable();   // 检查提供商是否就绪
}
```

## 实现模式

```java
@Component
public class NewModelProvider implements ModelProvider {

    @Override
    public String getProviderName() {
        return "newmodel";
    }

    @Override
    public boolean supports(String modelType) {
        return "newmodel".equalsIgnoreCase(modelType);
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        try {
            // 使用 Spring AI ChatClient 或特定客户端
            String content = chatClient.prompt()
                .user(request.message())
                .call()
                .content();
            return ChatResponse.success(getProviderName(), content, "model-name");
        } catch (Exception e) {
            return ChatResponse.error(getProviderName(), e.getMessage());
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
```

## 配置属性（可选）

如果需要自定义配置，创建 `*Properties.java` 类：

```java
@ConfigurationProperties(prefix = "spring.ai.newmodel")
public class NewModelProperties {
    private String baseUrl;
    private String apiKey;
    private String chatModel;
    private boolean enabled = true;
    // getters/setters
}
```

## 文件位置

- 提供商：`study-ai-best-practice-hello/src/main/java/com/bage/study/ai/best/practice/hello/provider/`
- 配置类：`study-ai-best-practice-hello/src/main/java/com/bage/study/ai/best/practice/hello/config/`

## 使用方式

提供商通过 `@Component` 注解自动注册，由 `ModelProviderManager` 管理。
