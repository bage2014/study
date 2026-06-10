---
name: "model-provider-creator"
description: "为多模型支持创建新的 AI 模型提供商。当用户想要添加对新的 AI 模型（OpenAI、Claude 等）的支持时使用此技能。"
---

# 模型提供商创建器

## 功能描述

帮助创建新的 AI 模型提供商以扩展多模型支持，实现模型特定的聊天逻辑和配置管理。

## 何时使用

在以下情况调用此技能：
- 为项目添加新的 AI 模型支持（OpenAI、Claude 等）
- 创建自定义模型提供商
- 扩展多模型架构
- 实现模型特定的聊天逻辑

## 核心功能

- **提供商创建**：创建新的模型提供商实现
- **配置管理**：管理模型提供商的配置参数
- **服务注册**：自动注册到模型管理器
- **多模型切换**：支持运行时切换不同模型

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| providerName | String | 是 | 提供商名称（唯一标识） |
| modelType | String | 是 | 模型类型 |
| config | Map | 否 | 配置参数 |

## 输出格式

```json
{
  "providerName": "newmodel",
  "status": "SUCCESS",
  "description": "模型提供商创建成功"
}
```

## 提供商接口规范

### ModelProvider 接口

```java
public interface ModelProvider {
    String getProviderName();        // 唯一名称（如 "openai"、"claude"）
    boolean supports(String modelType);  // 检查是否支持给定模型
    ChatResponse chat(ChatRequest request);  // 处理聊天请求
    default boolean isAvailable();   // 检查提供商是否就绪
}
```

## 实现模式

### 创建提供商实现类

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

### 配置属性（可选）

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

## 支持的模型提供商

| 提供商名称 | 支持模型 | 配置方式 |
|------------|----------|----------|
| deepseek | deepseek-chat | 环境变量 |
| openai | gpt-3.5-turbo, gpt-4 | 环境变量 |

## 核心组件

| 组件 | 职责 | 文件路径 |
|------|------|----------|
| ModelProvider | 提供商接口 | `provider/ModelProvider.java` |
| ModelProviderManager | 提供商管理器 | `provider/ModelProviderManager.java` |
| ChatService | 聊天服务 | `service/ChatService.java` |

## 创建新模型提供商步骤

### 第一步：创建配置属性类

```java
@ConfigurationProperties(prefix = "spring.ai.newmodel")
public class NewModelProperties {
    private String apiKey;
    private String baseUrl;
    private String model = "default-model";
    // getters/setters
}
```

### 第二步：创建提供商实现

```java
@Component
public class NewModelProvider implements ModelProvider {
    
    private final NewModelProperties properties;
    private final ChatClient chatClient;
    
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
        // 实现聊天逻辑
    }
}
```

### 第三步：配置 Spring AI

```java
@Configuration
@EnableConfigurationProperties(NewModelProperties.class)
public class NewModelConfig {
    
    @Bean
    public ChatClient newModelChatClient(NewModelProperties properties) {
        return ChatClient.builder()
            .baseUrl(properties.getBaseUrl())
            .apiKey(properties.getApiKey())
            .defaultOptions(options -> options.model(properties.getModel()))
            .build();
    }
}
```

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `MODEL_PROVIDER_API_KEY` | 模型 API Key | - |
| `MODEL_PROVIDER_BASE_URL` | 模型 API 地址 | - |

### 配置文件

```yaml
spring:
  ai:
    newmodel:
      api-key: ${MODEL_PROVIDER_API_KEY}
      base-url: ${MODEL_PROVIDER_BASE_URL}
      chat-model: newmodel-chat
```

## 目录结构

```
src/main/java/com/bage/study/ai/best/practice/hello/
├── provider/
│   ├── ModelProvider.java          # 提供商接口
│   ├── ModelProviderManager.java   # 提供商管理器
│   └── NewModelProvider.java       # 具体提供商实现
├── config/
│   ├── NewModelProperties.java     # 配置属性类
│   └── NewModelConfig.java         # 配置类
├── service/
│   └── ChatService.java            # 聊天服务
└── model/
    ├── ChatRequest.java            # 请求 DTO
    └── ChatResponse.java           # 响应 DTO
```

## 使用方式

提供商通过 `@Component` 注解自动注册，由 `ModelProviderManager` 管理。

```java
@Autowired
private ModelProviderManager providerManager;

// 获取提供商
ModelProvider provider = providerManager.getProvider("newmodel");

// 执行聊天
ChatResponse response = provider.chat(request);
```

## 扩展指南

### 添加新的模型提供商

1. 创建配置属性类（使用 `@ConfigurationProperties`）
2. 创建配置类，注册 `ChatClient` Bean
3. 实现 `ModelProvider` 接口
4. 添加 `@Component` 注解自动注册

### 支持模型切换

在 `ModelProviderManager` 中实现模型切换逻辑，支持运行时动态切换。