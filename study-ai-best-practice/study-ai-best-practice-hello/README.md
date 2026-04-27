# Study AI Best Practice

Spring Boot AI 最佳实践项目 - 基于 Spring AI 1.0.0-M6，支持多模型切换和 MCP 服务集成。

## 项目特性

- **多模型支持**：通过 `ModelProvider` 接口架构，支持轻松切换不同 AI 模型
- **DeepSeek 集成**：开箱即用的 DeepSeek 模型支持
- **MCP 服务框架**：灵活的 MCP (Model Context Protocol) 工具管理
- **动态配置**：运行时配置更新能力

## 技术栈

- Java 17
- Spring Boot 3.3.0
- Spring AI 1.0.0-M6
- Maven

## 项目结构

```
study-ai-best-practice/
├── pom.xml                                    # 父 POM
├── study-ai-best-practice-hello/              # Hello World 子模块
│   ├── pom.xml
│   └── src/main/java/com/bage/study/ai/best/practice/hello/
│       ├── StudyAiApplication.java            # 应用入口
│       ├── config/                           # 配置模块
│       │   └── DynamicConfigManager.java    # 动态配置管理器
│       ├── model/                            # 数据模型
│       │   ├── ModelType.java               # 模型类型枚举
│       │   ├── ChatRequest.java             # 聊天请求 DTO
│       │   └── ChatResponse.java            # 聊天响应 DTO
│       ├── provider/                         # 模型提供者
│       │   ├── ModelProvider.java           # 提供者接口
│       │   ├── ModelProviderManager.java    # 提供者管理器
│       │   └── DeepSeekProvider.java        # DeepSeek 实现
│       ├── service/                          # 服务层
│       │   └── ChatService.java             # 聊天服务
│       ├── controller/                       # REST API
│       │   └── ChatController.java          # 聊天控制器
│       └── mcp/                             # MCP 工具
│           ├── McpTool.java                 # 工具接口
│           ├── McpToolResult.java           # 工具结果
│           ├── McpToolManager.java          # 工具管理器
│           └── CurrentTimeTool.java         # 示例工具
```

## 快速开始

### 1. 配置 API Key

设置环境变量：
```bash
export DEEPSEEK_API_KEY=your-api-key
export DEEPSEEK_BASE_URL=https://api.deepseek.com
```

或修改 `study-ai-best-practice-hello/src/main/resources/application.yml`：
```yaml
spring:
  ai:
    openai:
      api-key: your-api-key
```

### 2. 编译运行

```bash
# 编译整个项目
mvn compile

# 运行（从父目录）
mvn spring-boot:run -pl study-ai-best-practice-hello

# 或进入子模块目录运行
cd study-ai-best-practice-hello
mvn spring-boot:run
```

### 3. API 调用

```bash
# 聊天接口
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello AI!"}'

# 指定模型
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"modelType": "deepseek", "message": "Hello!"}'

# 查看可用模型
curl http://localhost:8080/api/ai/models

# 健康检查
curl http://localhost:8080/api/ai/health
```

## 扩展指南

### 添加新的 AI 模型

1. 在 `provider` 包下创建新的 Provider 类实现 `ModelProvider` 接口
2. 使用 `@Component` 注解自动注册

```java
@Component
public class NewModelProvider implements ModelProvider {
    @Override
    public String getProviderName() { return "newmodel"; }

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

### 添加 MCP 工具

在 `mcp` 包下创建新的 Tool 类实现 `McpTool` 接口：

```java
@Component
public class MyTool implements McpTool {
    @Override
    public String getName() { return "my_tool"; }

    @Override
    public String getDescription() {
        return "Description";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of("param", "description");
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        return McpToolResult.success("result");
    }
}
```

## API 接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/ai/chat` | 发送聊天请求 |
| GET | `/api/ai/models` | 获取可用模型列表 |
| GET | `/api/ai/health` | 健康检查 |

## License

MIT
