---
name: "common-contract-test"
description: "契约测试技能，提供API契约验证、消费者驱动契约测试、接口兼容性检查，确保服务间接口一致性和向后兼容"
trigger: "微服务间接口对接、API版本升级、服务提供方和消费方联调时"
disable-when: "单体应用内部调用、无服务间通信时"
category: "common"
tags: ["contract-test", "api-contract", "consumer-driven", "pact", "spring-cloud-contract"]
requires: ["common-api-doc", "common-integration-test"]
---

# common-contract-test

## 功能描述

契约测试技能，提供消费者驱动契约（Consumer-Driven Contract）测试方法论和工具支持。确保服务提供方和消费方之间的接口契约一致，防止接口变更导致的集成问题。支持 API 版本管理、向后兼容性验证、接口演进管理。

## 触发条件

- 微服务间进行接口对接
- API 提供方准备升级接口版本
- 消费者需要验证接口兼容性
- 多个团队并行开发不同服务
- 需要保证接口向后兼容

## 何时使用

- 服务提供方开发 API 时
- 服务消费方集成 API 时
- API 版本升级时
- 多团队并行开发时
- 持续集成流水线中

## 何时不使用

- 单体应用内部调用
- 无服务间通信的场景
- 简单的 CRUD 接口（使用集成测试即可）

## 核心概念

### 1. 什么是契约测试

**契约**：服务提供方和消费方之间的接口约定，包括：
- 请求格式（URL、方法、参数、请求体）
- 响应格式（状态码、响应头、响应体结构）
- 交互规则（调用顺序、频率限制）

**契约测试**：验证服务是否遵守约定的测试。

### 2. 消费者驱动契约（CDC）

```
消费者定义契约 → 提供者实现 → 双方验证 → 持续监控
     ↓                ↓            ↓           ↓
  契约文件        API 实现     契约测试    兼容性检查
```

**核心思想**：由消费者定义期望的接口行为，提供者保证实现符合期望。

### 3. 契约测试 vs 集成测试

| 维度 | 契约测试 | 集成测试 |
|------|---------|---------|
| **范围** | 单个接口契约 | 完整业务流程 |
| **速度** | 快（毫秒级） | 慢（秒级） |
| **依赖** | 无外部依赖 | 需要真实服务 |
| **目的** | 验证接口兼容性 | 验证业务正确性 |
| **维护成本** | 低 | 高 |

## Spring Cloud Contract

### 1. 基础配置

#### 1.1 提供方配置

```xml
<!-- pom.xml (Provider) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-contract-verifier</artifactId>
    <scope>test</scope>
</dependency>

<plugin>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-contract-maven-plugin</artifactId>
    <version>${spring-cloud-contract.version}</version>
    <extensions>true</extensions>
    <configuration>
        <baseClassForTests>com.example.BaseContractTest</baseClassForTests>
    </configuration>
</plugin>
```

#### 1.2 消费方配置

```xml
<!-- pom.xml (Consumer) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-contract-stub-runner</artifactId>
    <scope>test</scope>
</dependency>
```

### 2. 定义契约

#### 2.1 Groovy DSL

```groovy
// src/test/resources/contracts/userService/shouldReturnUser.groovy
package contracts.userService

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "根据ID获取用户"
    
    request {
        method GET()
        url '/api/users/1'
        headers {
            contentType applicationJson()
            header 'Authorization', 'Bearer test-token'
        }
    }
    
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body '''
        {
            "userId": 1,
            "email": "test@example.com",
            "nickname": "测试用户",
            "avatar": "https://example.com/avatar.jpg",
            "createdAt": "2026-01-01T00:00:00"
        }
        '''
    }
}
```

#### 2.2 YAML 格式

```yaml
# src/test/resources/contracts/userService/shouldReturnUser.yml
description: "根据ID获取用户"

request:
  method: GET
  url: /api/users/1
  headers:
    Content-Type: application/json
    Authorization: "Bearer test-token"

response:
  status: 200
  headers:
    Content-Type: application/json
  body:
    userId: 1
    email: "test@example.com"
    nickname: "测试用户"
    avatar: "https://example.com/avatar.jpg"
    createdAt: "2026-01-01T00:00:00"
  matchers:
    body:
      - path: $.userId
        type: by_regex
        value: "[0-9]+"
      - path: $.email
        type: by_regex
        value: "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
```

### 3. 提供方实现

#### 3.1 基础测试类

```java
package com.example;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class BaseContractTest {
    
    @Autowired
    private WebApplicationContext context;
    
    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }
}
```

#### 3.2 自动生成测试

```bash
# 生成契约测试
mvn spring-cloud-contract:generateTests

# 生成的测试代码位于
# target/generated-test-sources/contracts/
```

生成的测试示例：

```java
// 自动生成
@Test
public void validate_shouldReturnUser() throws Exception {
    // given:
    MockMvcRequestSpecification request = given()
        .header("Authorization", "Bearer test-token")
        .contentType("application/json");
    
    // when:
    ResponseOptions response = given().spec(request)
        .get("/api/users/1");
    
    // then:
    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.header("Content-Type")).matches("application/json.*");
    
    // and:
    DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
    assertThatJson(parsedJson).field("userId").isEqualTo(1);
    assertThatJson(parsedJson).field("email").isEqualTo("test@example.com");
    assertThatJson(parsedJson).field("nickname").isEqualTo("测试用户");
}
```

### 4. 消费方使用

#### 4.1 Stub Runner 配置

```java
@SpringBootTest
@AutoConfigureStubRunner(
    ids = "com.example:user-service:+:stubs:8080",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
class UserServiceClientContractTest {
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    @Test
    void shouldGetUserFromService() {
        // When
        User user = userServiceClient.getUserById(1L);
        
        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }
}
```

#### 4.2 应用配置

```yaml
# application-test.yml
stubrunner:
  ids:
    - com.example:user-service:+:stubs:8080
  stubs-mode: local
```

## Pact 契约测试

### 1. 消费者定义契约

```java
package com.example.consumer;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(PactConsumerTestExt)
@PactTestFor(providerName = "UserService", port = "8080")
class UserConsumerTest {
    
    @Pact(provider = "UserService", consumer = "OrderService")
    RequestResponsePact getUserPact(PactDslWithProvider builder) {
        return builder
            .given("user exists")
            .uponReceiving("a request to get user by id")
            .path("/api/users/1")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(Map.of("Content-Type", "application/json"))
            .body(new PactDslJsonBody()
                .integerType("userId", 1)
                .stringType("email", "test@example.com")
                .stringType("nickname", "测试用户")
                .stringType("avatar", "https://example.com/avatar.jpg")
                .timestamp("createdAt", "yyyy-MM-dd'T'HH:mm:ss"))
            .toPact();
    }
    
    @Test
    @PactTestFor(pactMethod = "getUserPact")
    void testGetUser() {
        given()
            .get("http://localhost:8080/api/users/1")
        .then()
            .statusCode(200)
            .body("userId", equalTo(1))
            .body("email", equalTo("test@example.com"));
    }
}
```

### 2. 提供者验证契约

```java
package com.example.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("UserService")
@PactBroker(url = "http://pact-broker:9292")
class UserProviderPactTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    void setup(PactVerificationContext context) {
        System.setProperty("pact.verifier.publishResults", "true");
        context.setTarget(new HttpTestTarget("localhost", port));
    }
    
    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }
    
    @State("user exists")
    void userExists() {
        userRepository.save(new User(1L, "test@example.com", "测试用户"));
    }
}
```

### 3. Pact 配置文件

```yaml
# pact.yml
pact:
  broker:
    url: http://pact-broker:9292
    authentication:
      username: ${PACT_BROKER_USERNAME}
      password: ${PACT_BROKER_PASSWORD}
  provider:
    name: UserService
    version: 1.0.0
  consumer:
    name: OrderService
    version: 1.0.0
```

## 契约版本管理

### 1. API 版本策略

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 {
    
    @GetMapping("/{id}")
    public UserResponseV1 getUser(@PathVariable Long id) {
        // V1 实现
    }
}

@RestController
@RequestMapping("/api/v2/users")
public class UserControllerV2 {
    
    @GetMapping("/{id}")
    public UserResponseV2 getUser(@PathVariable Long id) {
        // V2 实现，可能包含更多字段
    }
}
```

### 2. 契约版本化

```
contracts/
├── v1/
│   ├── shouldReturnUser.groovy
│   └── shouldCreateUser.groovy
├── v2/
│   ├── shouldReturnUser.groovy
│   └── shouldCreateUser.groovy
└── deprecated/
    └── v0/
        └── shouldReturnUser.groovy
```

### 3. 向后兼容性检查

```java
@SpringBootTest
class ApiBackwardCompatibilityTest {
    
    @Test
    void v2ShouldBeBackwardCompatibleWithV1() {
        // V1 契约
        ContractV1 contractV1 = loadContract("v1/user-contract.json");
        
        // V2 响应
        UserResponseV2 responseV2 = callApiV2(1L);
        
        // 验证 V2 包含 V1 的所有字段
        assertThat(responseV2.getUserId()).isEqualTo(contractV1.getUserId());
        assertThat(responseV2.getEmail()).isEqualTo(contractV1.getEmail());
        assertThat(responseV2.getNickname()).isEqualTo(contractV1.getNickname());
        
        // V2 可以有新字段，但不能删除或修改 V1 字段
    }
}
```

## 契约测试流水线

### 1. CI/CD 集成

```yaml
# .github/workflows/contract-test.yml
name: Contract Tests

on:
  pull_request:
    branches: [main]
  push:
    branches: [main]

jobs:
  consumer-contract-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      
      - name: Run Consumer Tests
        run: mvn test -Dtest=*ConsumerTest
      
      - name: Publish Consumer Pact
        run: mvn pact:publish
        env:
          PACT_BROKER_URL: ${{ secrets.PACT_BROKER_URL }}
          PACT_BROKER_TOKEN: ${{ secrets.PACT_BROKER_TOKEN }}
  
  provider-contract-test:
    runs-on: ubuntu-latest
    needs: consumer-contract-test
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      
      - name: Start Provider Service
        run: mvn spring-boot:run &
      
      - name: Wait for Service
        run: npx wait-on http://localhost:8080/actuator/health
      
      - name: Verify Provider Pacts
        run: mvn pact:verify
        env:
          PACT_BROKER_URL: ${{ secrets.PACT_BROKER_URL }}
          PACT_BROKER_TOKEN: ${{ secrets.PACT_BROKER_TOKEN }}
      
      - name: Publish Provider Results
        run: mvn pact:publishResults
```

### 2. Pact Broker 部署

```yaml
# docker-compose.yml
version: '3'
services:
  pact-broker:
    image: pactfoundation/pact-broker
    ports:
      - "9292:9292"
    environment:
      PACT_BROKER_DATABASE_URL: "postgres://pact:pact@postgres/pact"
      PACT_BROKER_LOG_LEVEL: "INFO"
    depends_on:
      - postgres
  
  postgres:
    image: postgres:14
    environment:
      POSTGRES_USER: pact
      POSTGRES_PASSWORD: pact
      POSTGRES_DB: pact
    volumes:
      - pact-db:/var/lib/postgresql/data

volumes:
  pact-db:
```

## 契约测试最佳实践

### 1. 契约定义原则

```markdown
## DO ✅
- 使用具体的示例数据
- 验证关键业务字段
- 保持契约简洁
- 为每个接口创建独立契约
- 使用正则表达式验证格式

## DON'T ❌
- 验证所有字段（只验证关键）
- 使用随机数据
- 在契约中实现业务逻辑
- 共享契约定义
- 过度使用精确匹配
```

### 2. 数据匹配策略

```groovy
// 精确匹配
body([
    userId: 1,
    email: "test@example.com"
])

// 类型匹配
body([
    userId: $(consumer(1), producer(regex('[0-9]+'))),
    email: $(consumer("test@example.com"), producer(regex('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}')))
])

// 可选字段
body([
    userId: 1,
    nickname: $(optional("测试用户"))
])
```

### 3. 状态管理

```java
// 使用 @State 注解管理测试数据
@State("user with id 1 exists")
void userExists() {
    userRepository.save(new User(1L, "test@example.com"));
}

@State("no users exist")
void noUsers() {
    userRepository.deleteAll();
}
```

## 常见问题处理

### 1. 契约冲突

**场景**：消费者期望的接口与提供者实现不一致

**解决方案**：
```markdown
1. 消费者修改期望（如果提供者是正确的）
2. 提供者修改实现（如果消费者是正确的）
3. 协商新的契约（双方都需要调整）
4. 版本化接口（保持向后兼容）
```

### 2. 契约演进

**场景**：接口需要变更

**解决方案**：
```markdown
1. 创建新版本的契约
2. 提供者同时支持新旧版本
3. 消费者逐步迁移到新版本
4. 废弃旧版本
5. 删除旧版本契约
```

### 3. 测试失败排查

**排查步骤**：
```bash
# 1. 查看详细的失败信息
mvn test -X

# 2. 对比实际响应和期望
# 检查差异字段

# 3. 验证测试数据
# 确保 @State 正确设置

# 4. 检查服务状态
# 确保提供者服务正常运行
```

## 工具对比

| 工具 | 语言支持 | 特点 | 适用场景 |
|------|---------|------|---------|
| **Spring Cloud Contract** | Java/Kotlin | Spring 生态集成好 | Spring Boot 项目 |
| **Pact** | 多语言 | 语言无关，社区活跃 | 多语言微服务 |
| **Prism** | 任意 | Mock Server + 验证 | API 优先开发 |
| **Dredd** | 任意 | OpenAPI 验证 | REST API |

## 输出格式

契约测试完成后输出：

```markdown
## 契约测试报告

### 测试概览
- 测试时间：2026-11-03 15:30
- 提供者：UserService v1.2.3
- 消费者：OrderService v2.0.1

### 契约验证结果
| 契约 | 状态 | 备注 |
|------|------|------|
| getUserById | ✅ 通过 | - |
| createUser | ✅ 通过 | - |
| updateUser | ⚠️ 警告 | 新增字段未验证 |
| deleteUser | ❌ 失败 | 响应格式不匹配 |

### 兼容性检查
- V1 → V2：✅ 向后兼容
- V2 → V3：⚠️ 有破坏性变更

### 建议
1. 修复 deleteUser 契约
2. 为 updateUser 添加新字段验证
3. V3 需要版本化接口
```

## 埋点指令

**第 1 步**：调用 `~/.trae-cn/scripts/skill-usage.sh log common-contract-test --trigger auto`
若报 `Permission denied` 或沙箱拦截，必须以非沙箱方式重试。

## 参考资源

- Spring Cloud Contract: https://cloud.spring.io/spring-cloud-contract/
- Pact: https://pact.io/
- Consumer-Driven Contracts: https://martinfowler.com/articles/consumerDrivenContracts.html
