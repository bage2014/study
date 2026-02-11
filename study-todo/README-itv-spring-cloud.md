# Spring Cloud 技术总结

## 1. 基础概念

### 1.1 什么是 Spring Cloud
Spring Cloud 是一个基于 Spring Boot 的微服务架构开发工具包，它为微服务架构中的常见问题提供了一站式解决方案，包括服务发现、配置管理、负载均衡、熔断器、API 网关等。

### 1.2 Spring Cloud 的核心价值
- **简化开发**：基于 Spring Boot，提供了丰富的starter，简化了微服务开发
- **一站式解决方案**：集成了各种微服务架构中需要的组件
- **开箱即用**：提供了默认配置，同时支持自定义配置
- **社区活跃**：拥有庞大的社区支持，持续更新和改进
- **云原生友好**：与各种云平台无缝集成
- **可扩展性**：支持各种插件和扩展

### 1.3 Spring Cloud 与其他微服务框架的对比

| 框架 | 优势 | 劣势 |
|------|------|------|
| Spring Cloud | 基于 Spring 生态，易于集成，组件丰富，社区活跃 | 相对较重，学习曲线较陡 |
| Dubbo | 高性能 RPC，轻量级，易于使用 | 生态相对简单，组件较少 |
| Kubernetes | 容器编排，自动扩展，高可用 | 复杂度高，部署成本高 |
| Istio | 服务网格，流量管理，安全策略 | 复杂度高，性能开销较大 |

## 2. 核心组件

### 2.1 服务发现

**基本概念**
服务发现是微服务架构中的关键组件，用于自动检测和注册服务实例，使服务消费者能够找到服务提供者。

**Spring Cloud 中的服务发现组件**

#### 2.1.1 Eureka
- **基本原理**：基于 AP 架构，支持服务的注册与发现，具有自我保护机制
- **核心概念**：服务注册中心、服务提供者、服务消费者
- **使用示例**：
  ```java
  // 服务注册
  @SpringBootApplication
  @EnableEurekaServer
  public class EurekaServerApplication {
      public static void main(String[] args) {
          SpringApplication.run(EurekaServerApplication.class, args);
      }
  }
  ```

#### 2.1.2 Consul
- **基本原理**：基于 CP 架构，支持服务发现、健康检查、KV 存储
- **核心概念**：服务注册、健康检查、服务发现
- **使用示例**：
  ```yaml
  spring:
    cloud:
      consul:
        host: localhost
        port: 8500
        discovery:
          service-name: my-service
  ```

#### 2.1.3 Nacos
- **基本原理**：支持 AP 和 CP 架构，提供服务发现、配置管理、服务治理
- **核心概念**：服务注册、服务发现、配置管理
- **使用示例**：
  ```yaml
  spring:
    cloud:
      nacos:
        discovery:
          server-addr: localhost:8848
          service: my-service
  ```

### 2.2 配置中心

**基本概念**
配置中心用于集中管理应用配置，支持配置的动态更新和环境隔离。

**Spring Cloud 中的配置中心组件**

#### 2.2.1 Config
- **基本原理**：基于 Git 存储配置，支持配置的版本控制和环境隔离
- **核心概念**：配置仓库、配置客户端、配置服务端
- **使用示例**：
  ```java
  // 配置服务端
  @SpringBootApplication
  @EnableConfigServer
  public class ConfigServerApplication {
      public static void main(String[] args) {
          SpringApplication.run(ConfigServerApplication.class, args);
      }
  }
  ```

#### 2.2.2 Nacos Config
- **基本原理**：基于 Nacos 存储配置，支持配置的动态更新和环境隔离
- **核心概念**：命名空间、配置分组、配置项
- **使用示例**：
  ```yaml
  spring:
    cloud:
      nacos:
        config:
          server-addr: localhost:8848
          namespace: dev
          group: DEFAULT_GROUP
          data-id: application.yml
  ```

### 2.3 负载均衡

**基本概念**
负载均衡用于在多个服务实例之间分配请求，提高系统的可用性和扩展性。

**Spring Cloud 中的负载均衡组件**

#### 2.3.1 Ribbon
- **基本原理**：客户端负载均衡，基于轮询、随机、权重等策略
- **核心概念**：负载均衡器、服务列表、选择策略
- **使用示例**：
  ```java
  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
      return new RestTemplate();
  }
  ```

#### 2.3.2 LoadBalancer
- **基本原理**：Spring Cloud 官方推荐的负载均衡组件，替代 Ribbon
- **核心概念**：负载均衡器、服务列表、选择策略
- **使用示例**：
  ```java
  @Bean
  @LoadBalanced
  public WebClient.Builder webClientBuilder() {
      return WebClient.builder();
  }
  ```

### 2.4 熔断器

**基本概念**
熔断器用于防止服务雪崩，当服务调用失败率达到阈值时，自动熔断服务调用。

**Spring Cloud 中的熔断器组件**

#### 2.4.1 Hystrix
- **基本原理**：基于熔断器模式，支持服务降级、熔断、限流
- **核心概念**：熔断器、降级策略、熔断阈值
- **使用示例**：
  ```java
  @Service
  public class UserService {
      @HystrixCommand(fallbackMethod = "fallback")
      public String getUserInfo(String userId) {
          // 调用远程服务
          return restTemplate.getForObject("http://user-service/user/{id}", String.class, userId);
      }
      
      public String fallback(String userId) {
          return " fallback value ";
      }
  }
  ```

#### 2.4.2 Resilience4j
- **基本原理**：轻量级熔断器，基于函数式编程，支持熔断、限流、重试
- **核心概念**：熔断器、限流器、重试器
- **使用示例**：
  ```java
  @Service
  public class UserService {
      private final CircuitBreaker circuitBreaker;
      
      public UserService() {
          circuitBreaker = CircuitBreaker.ofDefaults("userService");
      }
      
      public String getUserInfo(String userId) {
          return circuitBreaker.executeSupplier(() -> {
              // 调用远程服务
              return restTemplate.getForObject("http://user-service/user/{id}", String.class, userId);
          });
      }
  }
  ```

### 2.5 网关

**基本概念**
网关用于统一管理 API 请求，支持路由、过滤、限流等功能。

**Spring Cloud 中的网关组件**

#### 2.5.1 Gateway
- **基本原理**：基于 WebFlux，支持响应式编程，提供丰富的路由和过滤功能
- **核心概念**：路由、断言、过滤器
- **使用示例**：
  ```yaml
  spring:
    cloud:
      gateway:
        routes:
        - id: user-service
          uri: lb://user-service
          predicates:
          - Path=/user/**
          filters:
          - StripPrefix=1
  ```

#### 2.5.2 Zuul
- **基本原理**：基于 Servlet，提供路由和过滤功能
- **核心概念**：路由、过滤器、断路器
- **使用示例**：
  ```java
  @EnableZuulProxy
  @SpringBootApplication
  public class ZuulGatewayApplication {
      public static void main(String[] args) {
          SpringApplication.run(ZuulGatewayApplication.class, args);
      }
  }
  ```

### 2.6 链路追踪

**基本概念**
链路追踪用于监控和分析微服务调用链路，帮助定位性能问题。

**Spring Cloud 中的链路追踪组件**

#### 2.6.1 Sleuth + Zipkin
- **基本原理**：Sleuth 生成跟踪信息，Zipkin 存储和展示跟踪信息
- **核心概念**：跟踪 ID、跨度、注解
- **使用示例**：
  ```yaml
  spring:
    zipkin:
      base-url: http://localhost:9411
    sleuth:
      sampler:
        probability: 1.0
  ```

## 3. 架构设计

### 3.1 整体架构

Spring Cloud 的整体架构包括以下几个层次：

- **基础设施层**：包括网络、存储、计算等基础设施
- **平台层**：包括容器编排、服务网格等平台服务
- **框架层**：Spring Cloud 核心组件，如服务发现、配置中心、网关等
- **应用层**：基于 Spring Cloud 构建的微服务应用

### 3.2 核心架构模式

#### 3.2.1 服务注册与发现模式
- **工作原理**：服务提供者向注册中心注册服务，服务消费者从注册中心获取服务列表
- **优点**：实现服务的自动发现，无需硬编码服务地址
- **缺点**：注册中心可能成为单点故障

#### 3.2.2 配置中心模式
- **工作原理**：应用从配置中心获取配置，配置中心支持配置的动态更新
- **优点**：集中管理配置，支持配置的版本控制和环境隔离
- **缺点**：配置中心可能成为单点故障

#### 3.2.3 API 网关模式
- **工作原理**：所有请求通过网关进入系统，网关负责路由、过滤、限流等
- **优点**：统一管理 API，提供安全防护和流量控制
- **缺点**：网关可能成为性能瓶颈

#### 3.2.4 熔断器模式
- **工作原理**：当服务调用失败率达到阈值时，自动熔断服务调用
- **优点**：防止服务雪崩，提高系统的可用性
- **缺点**：可能导致部分请求失败

### 3.3 组件间的关系

Spring Cloud 各组件之间的关系如下：

1. **服务提供者**：向注册中心注册服务，从配置中心获取配置
2. **服务消费者**：从注册中心获取服务列表，通过负载均衡选择服务实例
3. **注册中心**：管理服务实例的注册和发现
4. **配置中心**：管理应用配置的集中存储和动态更新
5. **网关**：统一管理 API 请求，路由到相应的服务
6. **熔断器**：监控服务调用，防止服务雪崩
7. **链路追踪**：监控和分析服务调用链路

## 4. 使用指南

### 4.1 快速开始

**创建服务注册中心**
```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

**创建配置中心**
```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

**创建服务提供者**
```java
@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public String getUserInfo(@PathVariable String id) {
        return "User: " + id;
    }
}
```

**创建服务消费者**
```java
@SpringBootApplication
@EnableEurekaClient
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}

@RestController
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @GetMapping("/order/{id}")
    public String getOrderInfo(@PathVariable String id) {
        String userInfo = restTemplate.getForObject("http://user-service/user/1", String.class);
        return "Order: " + id + ", " + userInfo;
    }
}
```

**创建网关**
```java
@SpringBootApplication
@EnableGateway
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
```

### 4.2 核心配置

**Eureka 配置**
```yaml
spring:
  application:
    name: eureka-server

server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: true
```

**Config 配置**
```yaml
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/username/config-repo
          username: username
          password: password

server:
  port: 8888
```

**Gateway 配置**
```yaml
spring:
  application:
    name: gateway
  cloud:
    gateway:
      routes:
      - id: user-service
        uri: lb://user-service
        predicates:
        - Path=/user/**
      - id: order-service
        uri: lb://order-service
        predicates:
        - Path=/order/**

server:
  port: 8080

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### 4.3 常见问题与解决方案

**问题 1：服务注册失败**
- **原因**：网络问题、Eureka 配置错误、服务名称冲突
- **解决方案**：检查网络连接、验证 Eureka 配置、确保服务名称唯一

**问题 2：配置更新不生效**
- **原因**：配置中心缓存、客户端缓存、配置路径错误
- **解决方案**：重启配置中心、重启客户端、验证配置路径

**问题 3：网关路由失败**
- **原因**：路由配置错误、服务未注册、过滤器错误
- **解决方案**：检查路由配置、验证服务注册状态、检查过滤器逻辑

**问题 4：熔断器触发过于频繁**
- **原因**：熔断阈值设置过低、服务响应时间过长、网络不稳定
- **解决方案**：调整熔断阈值、优化服务性能、检查网络连接

## 5. 高级特性

### 5.1 分布式事务

**基本概念**
分布式事务是指涉及多个服务的事务，需要保证事务的原子性、一致性、隔离性和持久性。

**Spring Cloud 中的分布式事务解决方案**

#### 5.1.1 Seata
- **基本原理**：基于 AT、TCC、SAGA 等模式，提供分布式事务解决方案
- **核心概念**：事务协调器、事务管理器、资源管理器
- **使用示例**：
  ```java
  @GlobalTransactional
  public void createOrder(Order order) {
      // 创建订单
      orderService.create(order);
      // 扣减库存
      inventoryService.deduct(order.getProductId(), order.getCount());
      // 扣减余额
      accountService.deduct(order.getUserId(), order.getAmount());
  }
  ```

### 5.2 服务网格

**基本概念**
服务网格是一种专门处理服务间通信的基础设施层，负责服务发现、负载均衡、熔断、限流等功能。

**Spring Cloud 与服务网格的集成**

#### 5.2.1 Istio 集成
- **基本原理**：Spring Cloud 应用可以与 Istio 无缝集成，利用 Istio 提供的服务网格功能
- **核心概念**：Sidecar、Virtual Service、Destination Rule
- **使用示例**：
  ```yaml
  apiVersion: networking.istio.io/v1alpha3
  kind: VirtualService
  metadata:
    name: user-service
  spec:
    hosts:
    - user-service
    http:
    - route:
      - destination:
          host: user-service
          subset: v1
  ```

### 5.3 云原生集成

**基本概念**
云原生是一种构建和运行应用程序的方法，利用云计算的优势，如弹性伸缩、自动修复、容器化等。

**Spring Cloud 与云原生的集成**

#### 5.3.1 Kubernetes 集成
- **基本原理**：Spring Cloud 应用可以部署到 Kubernetes 集群，利用 Kubernetes 提供的容器编排、服务发现、配置管理等功能
- **核心概念**：Pod、Deployment、Service、ConfigMap
- **使用示例**：
  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: user-service
  spec:
    replicas: 3
    selector:
      matchLabels:
        app: user-service
    template:
      metadata:
        labels:
          app: user-service
      spec:
        containers:
        - name: user-service
          image: user-service:latest
          ports:
          - containerPort: 8080
  ```

#### 5.3.2 Spring Cloud Function
- **基本原理**：Spring Cloud Function 允许将应用程序编写为函数，支持在各种云平台上部署
- **核心概念**：函数、函数注册表、函数适配器
- **使用示例**：
  ```java
  @Bean
  public Function<String, String> uppercase() {
      return s -> s.toUpperCase();
  }
  ```

## 6. 最佳实践

### 6.1 架构设计

- **服务拆分**：根据业务领域拆分服务，保持服务的单一职责
- **服务粒度**：服务粒度适中，避免过细或过粗
- **异步通信**：对于非实时场景，使用消息队列进行异步通信
- **数据一致性**：根据业务场景选择合适的分布式事务解决方案
- **服务版本管理**：采用语义化版本控制，支持服务的平滑升级

### 6.2 性能优化

- **缓存策略**：合理使用缓存，减少数据库访问
- **连接池**：优化数据库连接池和 HTTP 连接池配置
- **负载均衡**：选择合适的负载均衡策略，根据服务特性进行调整
- **异步处理**：对于耗时操作，使用异步处理提高系统吞吐量
- **资源隔离**：使用线程池隔离不同业务的请求，避免相互影响

### 6.3 监控与告警

- **健康检查**：为每个服务配置健康检查端点
- **指标收集**：使用 Prometheus 等工具收集服务指标
- **日志管理**：使用 ELK 等工具集中管理日志
- **链路追踪**：使用 Sleuth + Zipkin 等工具追踪服务调用链路
- **告警配置**：配置合理的告警规则，及时发现和处理问题

### 6.4 安全防护

- **API 网关**：使用网关进行统一的安全防护
- **认证授权**：集成 OAuth2、JWT 等认证授权方案
- **加密传输**：使用 HTTPS 加密传输数据
- **输入验证**：对所有输入进行严格验证，防止注入攻击
- **限流熔断**：使用 Sentinel、Resilience4j 等工具进行限流和熔断

### 6.5 部署与运维

- **容器化**：使用 Docker 容器化应用，提高部署效率
- **编排工具**：使用 Kubernetes 等工具进行容器编排
- **CI/CD**：搭建持续集成和持续部署 pipeline，实现自动化部署
- **环境管理**：使用配置中心管理不同环境的配置
- **灾备方案**：制定完善的灾备方案，提高系统的可靠性

## 7. 面试题解析

### 7.1 常见 Spring Cloud 面试问题

#### 7.1.1 什么是 Spring Cloud？它的核心组件有哪些？
**答案**：
Spring Cloud 是一个基于 Spring Boot 的微服务架构开发工具包，它为微服务架构中的常见问题提供了一站式解决方案。

核心组件包括：
- **服务发现**：Eureka、Consul、Nacos
- **配置中心**：Config、Nacos Config
- **负载均衡**：Ribbon、LoadBalancer
- **熔断器**：Hystrix、Resilience4j
- **网关**：Gateway、Zuul
- **链路追踪**：Sleuth + Zipkin
- **分布式事务**：Seata

#### 7.1.2 Spring Cloud 与 Spring Boot 的关系是什么？
**答案**：
Spring Boot 是一个快速开发框架，用于简化 Spring 应用的初始化和配置；Spring Cloud 是一个微服务架构工具包，基于 Spring Boot 构建，提供了微服务架构中需要的各种组件。

关系：
- Spring Cloud 依赖于 Spring Boot，使用 Spring Boot 作为基础框架
- Spring Boot 专注于单个应用的快速开发，Spring Cloud 专注于微服务架构的整体解决方案
- Spring Boot 可以单独使用，Spring Cloud 必须与 Spring Boot 一起使用

#### 7.1.3 什么是服务发现？Spring Cloud 中如何实现服务发现？
**答案**：
服务发现是微服务架构中的关键组件，用于自动检测和注册服务实例，使服务消费者能够找到服务提供者。

Spring Cloud 中实现服务发现的方式：
- **Eureka**：基于 AP 架构，支持服务的注册与发现，具有自我保护机制
- **Consul**：基于 CP 架构，支持服务发现、健康检查、KV 存储
- **Nacos**：支持 AP 和 CP 架构，提供服务发现、配置管理、服务治理

#### 7.1.4 什么是配置中心？Spring Cloud 中如何实现配置中心？
**答案**：
配置中心用于集中管理应用配置，支持配置的动态更新和环境隔离。

Spring Cloud 中实现配置中心的方式：
- **Config**：基于 Git 存储配置，支持配置的版本控制和环境隔离
- **Nacos Config**：基于 Nacos 存储配置，支持配置的动态更新和环境隔离

#### 7.1.5 什么是熔断器？Spring Cloud 中如何实现熔断器？
**答案**：
熔断器用于防止服务雪崩，当服务调用失败率达到阈值时，自动熔断服务调用。

Spring Cloud 中实现熔断器的方式：
- **Hystrix**：基于熔断器模式，支持服务降级、熔断、限流
- **Resilience4j**：轻量级熔断器，基于函数式编程，支持熔断、限流、重试

#### 7.1.6 什么是 API 网关？Spring Cloud 中如何实现 API 网关？
**答案**：
API 网关用于统一管理 API 请求，支持路由、过滤、限流等功能。

Spring Cloud 中实现 API 网关的方式：
- **Gateway**：基于 WebFlux，支持响应式编程，提供丰富的路由和过滤功能
- **Zuul**：基于 Servlet，提供路由和过滤功能

#### 7.1.7 Spring Cloud 中的负载均衡是如何实现的？
**答案**：
Spring Cloud 中的负载均衡实现方式：

1. **Ribbon**：客户端负载均衡，基于轮询、随机、权重等策略
   - 工作原理：从服务列表中选择一个服务实例，发送请求
   - 使用方式：通过 `@LoadBalanced` 注解标记 RestTemplate

2. **LoadBalancer**：Spring Cloud 官方推荐的负载均衡组件，替代 Ribbon
   - 工作原理：与 Ribbon 类似，提供更简洁的 API
   - 使用方式：通过 `@LoadBalanced` 注解标记 RestTemplate 或 WebClient

3. **服务端负载均衡**：如 Nginx、HAProxy 等
   - 工作原理：在服务端进行负载均衡，客户端无需关心
   - 使用方式：配置负载均衡器，将请求分发到不同的服务实例

#### 7.1.8 Spring Cloud 中的链路追踪是如何实现的？
**答案**：
Spring Cloud 中的链路追踪实现方式：

- **Sleuth + Zipkin**：
  - Sleuth：生成跟踪信息，为每个请求添加跟踪 ID 和跨度 ID
  - Zipkin：存储和展示跟踪信息，提供可视化的调用链路
  - 工作原理：
    1. 当请求进入系统时，Sleuth 生成跟踪 ID
    2. 每个服务调用都会生成一个跨度 ID
    3. 跟踪信息通过 HTTP 或消息队列发送到 Zipkin
    4. Zipkin 存储和展示跟踪信息

#### 7.1.9 Spring Cloud 与 Kubernetes 的关系是什么？
**答案**：
Spring Cloud 与 Kubernetes 的关系：

- **互补关系**：Spring Cloud 提供了微服务架构的开发框架，Kubernetes 提供了容器编排和部署平台
- **集成方式**：
  1. Spring Cloud 应用可以部署到 Kubernetes 集群
  2. Kubernetes 提供的服务发现、配置管理等功能可以替代 Spring Cloud 的部分组件
  3. Spring Cloud Kubernetes 提供了 Spring Cloud 与 Kubernetes 的集成

- **选择建议**：
  - 小型项目：可以使用纯 Spring Cloud
  - 大型项目：建议结合 Kubernetes，利用其容器编排和自动伸缩能力

#### 7.1.10 Spring Cloud 的未来发展趋势是什么？
**答案**：
Spring Cloud 的未来发展趋势：

- **云原生**：更加注重云原生特性，与 Kubernetes 等云平台深度集成
- **服务网格**：与 Istio 等服务网格技术集成，提供更强大的流量管理和安全功能
- **函数式编程**：支持函数式编程，适应 Serverless 架构
- **响应式编程**：采用响应式编程模型，提高系统的并发处理能力
- **简化配置**：进一步简化配置，提供更智能的默认配置
- **生态整合**：与 Spring 生态的其他项目（如 Spring Security、Spring Data）深度整合

## 8. 参考链接

### 8.1 官方文档
- [Spring Cloud 官方网站](https://spring.io/projects/spring-cloud)
- [Spring Cloud 文档](https://docs.spring.io/spring-cloud/docs/current/reference/html/)
- [Spring Cloud Alibaba 文档](https://spring-cloud-alibaba-group.github.io/github-pages/greenwich/spring-cloud-alibaba.html)
- [Seata 官方文档](https://seata.io/zh-cn/docs/)

### 8.2 教程资源
- [Spring Cloud 快速开始](https://spring.io/quickstart)
- [Spring Cloud 示例代码](https://github.com/spring-cloud-samples)
- [Spring Cloud 中文指南](https://springcloud.cc/)
- [Spring Cloud Alibaba 示例](https://github.com/alibaba/spring-cloud-alibaba)

### 8.3 博客文章
- [Spring Cloud 微服务架构实战](https://www.cnblogs.com/ityouknow/category/943921.html)
- [Spring Cloud Gateway 详解](https://baijiahao.baidu.com/s?id=1735384008263810697)
- [Spring Cloud 服务发现原理](https://mp.weixin.qq.com/s?__biz=MzIzNjM3MDEyMg==&mid=2247545115&idx=1&sn=f1df76f0f39df977e36f763371bb8017)
- [Spring Cloud 分布式事务解决方案](https://zhuanlan.zhihu.com/p/471003434)
- [Spring Cloud 与 Kubernetes 集成](https://blog.csdn.net/u011919808/article/details/126851482)

### 8.4 视频教程
- [Spring Cloud 微服务架构实战](https://www.bilibili.com/video/BV1Ca411R7GG/)
- [Spring Cloud Alibaba 从入门到精通](https://www.bilibili.com/video/BV1H14y147aT/)
- [Spring Cloud Gateway 详解](https://www.bilibili.com/video/BV1HV4y1k7GW/)
- [Spring Cloud 服务网格实战](https://www.bilibili.com/video/BV1H14y147aT/)

### 8.5 工具与资源
- [Spring Initializr](https://start.spring.io/)：快速创建 Spring Boot 项目
- [Spring Cloud CLI](https://docs.spring.io/spring-cloud-cli/docs/current/reference/html/)：Spring Cloud 命令行工具
- [Spring Cloud Contract](https://spring.io/projects/spring-cloud-contract)：契约测试工具
- [Spring Cloud Sleuth](https://spring.io/projects/spring-cloud-sleuth)：分布式链路追踪
- [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream)：消息驱动的微服务框架