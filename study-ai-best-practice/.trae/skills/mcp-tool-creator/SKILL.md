---
name: "mcp-tool-creator"
description: "MCP 工具创建器。当用户需要为系统添加新的 MCP 工具时使用此技能。"
---

# MCP 工具创建器

## 功能描述

帮助为系统创建新的 MCP（Model Context Provider）工具，扩展系统的信息收集和数据提供能力。

## 何时使用

在以下情况调用此技能：
- 为系统添加新的 MCP 工具
- 集成外部服务到业务流程
- 扩展系统的数据收集能力
- 模拟上下文数据用于测试

## 核心功能

- **MCP服务创建**：创建新的上下文数据提供服务
- **数据模拟**：生成模拟数据用于测试和验证
- **服务集成**：将外部服务集成到业务流程

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| serviceName | String | 是 | MCP服务名称 |
| dataType | String | 是 | 数据类型 |
| config | Map | 否 | 配置参数 |

## 输出格式

```json
{
  "serviceName": "MyMcpService",
  "status": "SUCCESS",
  "dataType": "CUSTOM_DATA",
  "description": "自定义MCP服务创建成功"
}
```

## MCP 服务结构

### 服务接口规范

```java
@Service
public class MyMcpService {
    
    public SomeData getData(String appId, LocalDateTime time, String scene) {
        // 实现数据获取逻辑
        // 返回模拟或真实数据
    }
}
```

## 创建新 MCP 服务步骤

### 第一步：创建数据模型

```java
public class CustomData {
    private String field1;
    private int field2;
    // getters and setters
}
```

### 第二步：创建 MCP 服务类

```java
@Service
public class CustomMcpService {
    
    private final Random random = new Random();
    
    public CustomData getData(String appId, LocalDateTime time, String scene) {
        CustomData data = new CustomData();
        // 根据场景生成模拟数据
        if ("custom_scenario".equals(scene)) {
            // 特定场景数据
        } else {
            // 默认数据
            data.setField1("value-" + random.nextInt(100));
            data.setField2(random.nextInt(1000));
        }
        return data;
    }
}
```

### 第三步：集成到业务服务

```java
@Service
public class BusinessServiceImpl {
    
    private final CustomMcpService customMcpService;
    
    // 在业务方法中调用
    CustomData customData = customMcpService.getData(appId, time, scene);
}
```

## 配置要求

无需额外配置，Spring Boot 自动扫描并注册 `@Service` 注解的 MCP 服务。

## 目录结构

```
src/main/java/com/bage/study/ai/best/practice/hello/
├── mcp/                    # MCP服务目录
│   └── CustomMcpService.java
└── service/
    └── impl/
        └── BusinessServiceImpl.java
```

## 使用示例

```java
// 注入 MCP 服务
@Autowired
private CustomMcpService customMcpService;

// 获取数据
CustomData data = customMcpService.getData(appId, time, scene);
```

## 扩展指南

### 添加新的数据类型

1. 创建新的数据模型类
2. 创建对应的 MCP 服务类
3. 在业务服务中注入并调用

### 支持场景参数

在 MCP 服务的方法中添加 `scene` 参数，根据不同场景返回不同的模拟数据。