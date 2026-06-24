---
name: "common-coding"
description: "编码技能，提供代码编写指导，遵循编码规范和最佳实践"
trigger: "用户需要编写代码时"
disable-when: "已有完整代码实现或仅需简单代码片段"
category: "common"
tags: ["coding", "best-practices", "standards"]
---

# 编码技能

## 功能描述

提供代码编写指导能力，帮助开发者遵循编码规范和最佳实践，提高代码质量和可维护性。

## 何时使用

在以下情况调用此技能：
- 需要编写新代码时
- 需要审查代码质量时
- 需要优化现有代码时
- 需要生成代码文档时

## 核心功能

- **代码生成**：根据需求生成符合规范的代码
- **代码审查**：检查代码是否符合编码规范
- **代码优化**：提供代码优化建议
- **文档生成**：生成代码文档和注释
- **设计模式推荐**：根据场景推荐合适的设计模式

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| language | String | 是 | 目标编程语言（Java/Python/JavaScript等） |
| framework | String | 否 | 使用的框架（Spring Boot/React/Vue等） |
| requirement | String | 是 | 功能需求描述 |
| styleGuide | String | 否 | 编码风格指南名称 |

## 输出格式

```json
{
  "status": "SUCCESS",
  "language": "Java",
  "framework": "Spring Boot",
  "code": "生成的代码内容",
  "explanation": "代码解释和说明",
  "bestPractices": ["最佳实践1", "最佳实践2"],
  "improvements": [
    {
      "issue": "问题描述",
      "suggestion": "改进建议",
      "severity": "高/中/低"
    }
  ]
}
```

## 编码流程

```
需求分析 → 技术选型 → 代码设计 → 代码实现 → 代码审查 → 优化改进 → 文档生成
```

### 详细步骤

1. **需求分析**：理解业务需求，确定实现方案
2. **技术选型**：选择合适的技术栈和框架
3. **代码设计**：设计类结构、方法签名和数据模型
4. **代码实现**：编写符合规范的代码
5. **代码审查**：检查代码质量和规范合规性
6. **优化改进**：根据审查结果进行优化
7. **文档生成**：编写代码注释和文档

## 编码规范检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 命名规范 | 变量、方法、类命名是否符合规范 | ✅/❌ |
| 代码格式 | 缩进、空格、换行是否符合规范 | ✅/❌ |
| 注释完整 | 是否有必要的注释说明 | ✅/❌ |
| 异常处理 | 是否正确处理异常情况 | ✅/❌ |
| 代码复用 | 是否避免重复代码 | ✅/❌ |
| 性能优化 | 是否考虑性能问题 | ✅/❌ |
| 安全性 | 是否存在安全隐患 | ✅/❌ |
| 可测试性 | 代码是否易于测试 | ✅/❌ |

## 最佳实践指南

### Java 编码规范

1. **命名规范**：类名使用 PascalCase，方法和变量使用 camelCase
2. **代码格式**：使用 4 空格缩进，每行不超过 120 字符
3. **注释规范**：使用 Javadoc 格式，方法必须有注释
4. **异常处理**：使用 try-with-resources，避免空 catch 块
5. **设计原则**：遵循 SOLID 原则，单一职责

### 外部调用规范

#### 目录结构

所有外部调用相关代码必须放在 `infra` 目录下：

```
src/main/java/com/example/
├── application/          # 应用层（业务逻辑）
├── domain/               # 领域层（业务实体）
└── infra/               # 基础设施层（外部调用）
    ├── client/          # Remote Client 透传层
    ├── proxy/           # Remote Proxy 适配层
    └── adapter/         # Remote Adapter 转换层
```

#### Remote Client 模式

**职责**：透传外部接口，不做业务逻辑处理

**命名约定**：`{ServiceName}RemoteClient`

**示例**：

```java
// infra/client/UserRemoteClient.java
@FeignClient(name = "user-service")
public interface UserRemoteClient {
    
    @GetMapping("/api/users/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable Long id);
    
    @PostMapping("/api/users")
    ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request);
}
```

#### Remote Proxy 模式

**职责**：对外部接口进行适配和调用，添加业务无关的通用处理

**命名约定**：`{ServiceName}RemoteProxy`

**示例**：

```java
// infra/proxy/UserRemoteProxy.java
@Component
public class UserRemoteProxy {
    
    private final UserRemoteClient userRemoteClient;
    private final RestTemplate restTemplate;
    
    public User getUser(Long id) {
        try {
            ResponseEntity<UserDTO> response = userRemoteClient.getUserById(id);
            if (response.getStatusCode().is2xxSuccessful()) {
                return convertToDomain(response.getBody());
            }
            throw new RemoteServiceException("Failed to get user");
        } catch (FeignException e) {
            log.warn("Remote call failed, fallback to cache");
            return getFromCache(id);
        }
    }
    
    // 通用重试、熔断、降级处理
    @Retryable(maxAttempts = 3)
    public User createUser(UserCreateCommand command) {
        // ...
    }
}
```

#### Remote Adapter 模式

**职责**：数据转换和协议适配，将外部数据转换为领域模型

**命名约定**：`{ServiceName}RemoteAdapter`

**示例**：

```java
// infra/adapter/UserRemoteAdapter.java
@Component
public class UserRemoteAdapter {
    
    private final UserRemoteProxy userRemoteProxy;
    
    public UserDomain loadUser(Long userId) {
        User user = userRemoteProxy.getUser(userId);
        
        // 转换为领域模型
        return UserDomain.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
    
    public UserDomain createUser(UserCreateDomain domain) {
        CreateUserRequest request = CreateUserRequest.builder()
            .username(domain.getUsername())
            .email(domain.getEmail())
            .build();
        
        User user = userRemoteProxy.createUser(request);
        return convertToDomain(user);
    }
}
```

#### 使用规范

| 层次 | 职责 | 适用场景 |
|------|------|----------|
| Remote Client | 透传调用，无业务逻辑 | 简单的 REST/gRPC 调用 |
| Remote Proxy | 通用处理（重试、熔断、降级） | 需要容错处理的调用 |
| Remote Adapter | 数据转换、协议适配 | 需要领域模型转换 |

#### 调用流程

```
Application Service → Remote Adapter → Remote Proxy → Remote Client → External Service
```

**说明**：
1. **Application Service**：业务层，调用 Adapter
2. **Remote Adapter**：数据转换，调用 Proxy
3. **Remote Proxy**：通用处理（重试、熔断），调用 Client
4. **Remote Client**：透传调用外部服务

### Python 编码规范

1. **命名规范**：模块和函数使用 snake_case，类使用 PascalCase
2. **代码格式**：使用 4 空格缩进，每行不超过 79 字符
3. **注释规范**：使用 docstring，遵循 Google 风格
4. **类型提示**：使用类型注解提高代码可读性
5. **代码组织**：合理使用模块和包结构

### JavaScript/TypeScript 编码规范

1. **命名规范**：变量和函数使用 camelCase，类使用 PascalCase
2. **代码格式**：使用 2 空格缩进，每行不超过 120 字符
3. **注释规范**：使用 JSDoc 格式
4. **异步处理**：使用 async/await，避免回调地狱
5. **类型安全**：TypeScript 项目严格启用类型检查

## 核心组件

| 组件 | 职责 | 描述 |
|------|------|------|
| CodeGenerator | 代码生成器 | 根据需求生成代码 |
| CodeAnalyzer | 代码分析器 | 分析代码质量和规范 |
| PatternRecommender | 模式推荐器 | 推荐合适的设计模式 |
| DocGenerator | 文档生成器 | 生成代码文档 |

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| CODING_STYLE_GUIDE | 默认编码风格指南 | google |

### 配置文件

```yaml
coding:
  style-guide: google
  max-line-length: 120
  enforce-type-hints: true
```

## 扩展指南

### 添加新语言支持

1. 创建对应语言的代码生成器
2. 定义该语言的编码规范规则
3. 添加代码分析和检查逻辑

### 添加自定义规则

1. 在规则引擎中添加新规则
2. 定义规则的检查逻辑和修复建议
3. 更新编码规范检查清单
## 触发条件

