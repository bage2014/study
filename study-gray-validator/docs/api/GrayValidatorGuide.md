# Gray Validator 使用指南

## 一、模块概述

`study-gray-validator` 是一个基于参数白名单的灰度路由验证器，支持三种运行模式：

| 模式 | 说明 | 适用场景 |
|-----|------|---------|
| **GATEWAY** | 反向代理模式，所有请求通过过滤器转发 | 独立网关层部署 |
| **EMBEDDED** | 嵌入式过滤器模式，命中规则的请求转发 | 应用内集成 |
| **AOP** | 注解驱动模式，通过 `@GrayRoute` 标记方法 | 精准控制路由 |

## 二、核心概念

### 2.1 灰度路由规则

规则定义了如何从请求中提取字段并判断是否命中灰度：

```java
// 通过 Query 参数匹配
GrayRule.byQuery("uid").matchValues("10001", "10002");

// 通过 Header 匹配
GrayRule.byHeader("X-Gray-User").matchValues("88888");

// 通过请求体 JSON 路径匹配
GrayRule.byBody("user.id").matchPrefix("2024");

// 自动探测（Query → Header → PathVar → Body）
GrayRule.byField("userId").matchValues("10001");
```

### 2.2 匹配类型

| 类型 | 方法 | 说明 |
|-----|------|------|
| VALUES | `matchValues(String...)` | 精确匹配白名单值列表 |
| PREFIX | `matchPrefix(String)` | 前缀匹配 |
| SUFFIX | `matchSuffix(String)` | 后缀匹配 |

### 2.3 FieldResolver（字段解析器）

SPI 接口，用于将原始字段值转换为用于匹配的值。例如：

```java
// orderId → userId（查缓存/数据库）
public class OrderToUserFieldResolver implements FieldResolver {
    public Optional<String> resolve(String fieldName, String rawValue, HttpServletRequest request) {
        if (!"orderId".equals(fieldName)) return Optional.empty();
        return Optional.ofNullable(orderUserMap.get(rawValue));
    }
}
```

## 三、配置说明

### 3.1 配置文件 (`application.yml`)

```yaml
server:
  port: 8090

spring:
  application:
    name: study-gray-validator

# 灰度目标配置（GrayTargetConfig 支持运行时修改）
# grayTargetUrl: "http://localhost:8090/internal/gray-echo"
# originalTargetUrl: "http://localhost:8090"
# mode: AOP
# forwardTimeoutMs: 3000
```

### 3.2 路由规则配置

规则定义在 `GrayRouteConfig` 中，按顺序执行，第一条命中即生效：

```java
@Bean
public List<GrayRule> grayRules() {
    return List.of(
        // 策略A：uid 精确白名单
        GrayRule.byQuery("uid").matchValues("10001", "10002", "88888", "99001"),
        
        // 策略B：orderId 前缀匹配（2024年6月订单）
        GrayRule.byQuery("orderId").matchPrefix("202406"),
        
        // 策略C：orderId 后缀匹配（尾号为0000）
        GrayRule.byQuery("orderId").matchSuffix("0000"),
        
        // 策略D：orderId 间接关联（通过 Resolver 转换为 uid）
        GrayRule.byQuery("orderId").matchValues("10001", "10002", "88888", "99001"),
        
        // 策略E：Header 标识匹配
        GrayRule.byHeader("X-Gray-User").matchValues("88888")
    );
}
```

## 四、使用方式

### 4.1 AOP 模式（推荐）

在 Controller 方法上添加 `@GrayRoute` 注解：

```java
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @GrayRoute
    @GetMapping("/users")
    public DemoResponse getUser(@RequestParam String uid) {
        return DemoResponse.original("user:" + uid);
    }

    @GrayRoute
    @GetMapping("/orders")
    public DemoResponse getOrder(@RequestParam String orderId) {
        return DemoResponse.original("order:" + orderId);
    }
}
```

### 4.2 嵌入式过滤器模式

修改配置文件：

```yaml
gray:
  mode: EMBEDDED
```

所有请求会经过 `GrayEmbeddedFilter`，命中规则的请求被转发到灰度目标。

### 4.3 网关代理模式

修改配置文件：

```yaml
gray:
  mode: GATEWAY
  originalTargetUrl: "http://backend-service:8080"
```

所有请求都会被代理，命中规则的转发到灰度目标，未命中的转发到原始目标。

## 五、测试示例

### 5.1 测试策略A（uid 白名单）

```bash
# 命中灰度
curl "http://localhost:8090/api/demo/users?uid=10001"
# 响应: {"source":"GRAY","resource":"gray-response:/api/demo/users","extra":{"uri":"/internal/gray-echo/api/demo/users","query":"uid=10001","params":{"uid":"10001"}}}

# 未命中灰度
curl "http://localhost:8090/api/demo/users?uid=99999"
# 响应: {"source":"ORIGINAL","resource":"user:99999","extra":null}
```

### 5.2 测试策略B（orderId 前缀）

```bash
# 命中灰度（2024年6月订单）
curl "http://localhost:8090/api/demo/orders?orderId=20240601001"
# 响应: {"source":"GRAY",...}

# 未命中灰度
curl "http://localhost:8090/api/demo/orders?orderId=20231201001"
# 响应: {"source":"ORIGINAL",...}
```

### 5.3 测试策略D（orderId 间接关联）

```bash
# orderId=20240601001 → uid=10001（在白名单）→ 命中灰度
curl "http://localhost:8090/api/demo/orders?orderId=20240601001"
# 响应: {"source":"GRAY",...}

# orderId=20240301555 → uid=55555（不在白名单）→ 未命中灰度
curl "http://localhost:8090/api/demo/orders?orderId=20240301555"
# 响应: {"source":"ORIGINAL",...}
```

### 5.4 测试策略E（Header 标识）

```bash
# 命中灰度
curl -H "X-Gray-User: 88888" "http://localhost:8090/api/demo/profile"
# 响应: {"source":"GRAY",...}

# 未命中灰度
curl "http://localhost:8090/api/demo/profile"
# 响应: {"source":"ORIGINAL","resource":"profile:anonymous","extra":null}
```

## 六、扩展开发

### 6.1 自定义 FieldResolver

实现 `FieldResolver` 接口并注册为 Bean：

```java
@Component
public class CustomFieldResolver implements FieldResolver {
    @Override
    public Optional<String> resolve(String fieldName, String rawValue, HttpServletRequest request) {
        if ("customField".equals(fieldName)) {
            // 自定义转换逻辑
            return Optional.of(convertValue(rawValue));
        }
        return Optional.empty();
    }
}
```

### 6.2 自定义路由规则

在配置类中添加自定义规则：

```java
@Configuration
public class CustomGrayRouteConfig {
    @Bean
    public List<GrayRule> customGrayRules() {
        return List.of(
            GrayRule.byQuery("customParam").matchPrefix("test-")
        );
    }
}
```

## 七、架构设计

```
┌─────────────────────────────────────────────────────────────────┐
│                       请求入口                                  │
│  ┌─────────────┐   ┌─────────────┐   ┌─────────────────────┐   │
│  │  Gateway    │   │  Embedded   │   │   @GrayRoute AOP    │   │
│  │   Filter    │   │   Filter    │   │      Aspect         │   │
│  └──────┬──────┘   └──────┬──────┘   └──────────┬──────────┘   │
└─────────┼──────────────────┼─────────────────────┼──────────────┘
          │                  │                     │
          ▼                  ▼                     ▼
┌─────────────────────────────────────────────────────────────────┐
│                   WhitelistMatcher                              │
│  ┌──────────────────┐  ┌──────────────────┐                    │
│  │ GrayFieldExtractor│→│   FieldResolver  │                    │
│  │ (提取字段值)       │  │ (字段值转换)      │                    │
│  └──────────────────┘  └────────┬─────────┘                    │
│                                 │                              │
│                                 ▼                              │
│                      ┌──────────────────┐                       │
│                      │   规则匹配引擎    │                       │
│                      │ (VALUES/PREFIX/SUFFIX)                   │
│                      └────────┬─────────┘                       │
└───────────────────────────────┼─────────────────────────────────┘
                               │
          ┌────────────────────┴────────────────────┐
          │                                         │
          ▼                                         ▼
┌──────────────────┐                    ┌──────────────────┐
│  命中灰度         │                    │  未命中灰度       │
│ForwardingEngine   │                    │   继续执行        │
│  转发请求         │                    │   原始逻辑        │
└──────────────────┘                    └──────────────────┘
          │
          ▼
┌──────────────────┐
│  GrayEchoController│ (本地灰度目标)
└──────────────────┘
```

## 八、日志说明

### 8.1 灰度路由日志

```
[GrayRouting] GET /api/demo/users?uid=10001 → http://localhost:8090/internal/gray-echo/api/demo/users?uid=10001 | params={uid=10001} | headers={Host=localhost:8090, Accept=*/*}
```

### 8.2 灰度目标接收日志

```
[GrayEcho] received GET /internal/gray-echo/api/demo/users?uid=10001
  params   : uid=10001 
  headers  : Host=localhost:8090, Accept=*/*
```

## 九、注意事项

1. **规则顺序**：规则按配置顺序执行，第一条命中即返回，注意优先级
2. **Body 读取**：使用 `CachedBodyRequestWrapper` 缓存请求体，支持多次读取
3. **AOP 返回值**：命中灰度时返回 `null`，由 `ForwardingEngine` 直接写入响应
4. **超时配置**：通过 `forwardTimeoutMs` 配置转发超时时间（默认 3000ms）
5. **异常处理**：转发异常时记录日志并返回 500 状态码