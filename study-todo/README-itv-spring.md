# Spring 技术文档

## 第一部分：Spring 基础概念

### 1.1 Spring 简介

Spring 是一个开源的 Java 企业级应用开发框架，它提供了全面的基础设施支持，用于构建企业级应用程序。Spring 的核心是控制反转（IoC）和面向切面编程（AOP），它使得开发者可以专注于业务逻辑的实现，而不用过多关注底层的技术细节。

### 1.2 Spring 核心功能

- **轻量级** - Spring 在代码量和透明度方面都很轻便。
- **IOC** - 控制反转，将对象的创建和依赖关系的管理交给 Spring 容器。
- **AOP** - 面向切面编程，可以将应用业务逻辑和系统服务分离，以实现高内聚。
- **容器** - Spring 负责创建和管理对象（Bean）的生命周期和配置。
- **MVC** - 对 web 应用提供了高度可配置性，其他框架的集成也十分方便。
- **事务管理** - 提供了用于事务管理的通用抽象层。Spring 的事务支持也可用于容器较少的环境。
- **JDBC 异常** - Spring 的 JDBC 抽象层提供了一个异常层次结构，简化了错误处理策略。

### 1.3 Spring 架构

![](https://img-blog.csdnimg.cn/img_convert/d6ab48a77bd32bfec4e73d457c7a2682.png)

## 第二部分：Spring 核心技术

### 2.1 IOC 容器

IOC（Inversion of Control）是 Spring 的核心概念，它将对象的创建和依赖关系的管理交给 Spring 容器，而不是由应用程序代码直接控制。

### 2.2 DI 依赖注入

DI（Dependency Injection）是 IOC 的具体实现方式，它通过构造函数、setter 方法或字段注入的方式，将依赖的对象注入到目标对象中。

### 2.3 AOP 面向切面编程

AOP（Aspect-Oriented Programming）是 Spring 的另一个核心概念，它允许开发者将横切关注点（如日志、事务、安全等）从业务逻辑中分离出来，以提高代码的可维护性和可重用性。

#### 2.3.1 AOP 核心概念

- **切面（Aspect）** - 横切关注点的模块化。
- **连接点（Join Point）** - 程序执行过程中的一个点。
- **通知（Advice）** - 切面在特定连接点执行的操作。
- **切点（Pointcut）** - 匹配连接点的表达式。
- **引入（Introduction）** - 向现有类添加新方法或字段。
- **织入（Weaving）** - 将切面与目标对象结合，创建代理对象的过程。

#### 2.3.2 AOP 通知类型

- **前置通知（Before）** - 在目标方法执行前执行。
- **后置通知（After）** - 在目标方法执行后执行，无论方法是否抛出异常。
- **返回通知（After Returning）** - 在目标方法成功执行后执行。
- **异常通知（After Throwing）** - 在目标方法抛出异常后执行。
- **环绕通知（Around）** - 包围目标方法，可在方法执行前后执行自定义逻辑。

#### 2.3.3 AOP 执行顺序

**多个切面的执行顺序**
多个 AOP 切面执行的顺序由 `@Order` 注解或实现 `Ordered` 接口来控制，值越小优先级越高。

**同方法多切点执行顺序**

**执行顺序流程图**
```
+------------------------+
| 方法执行开始            |
+------------------------+
            |
            v
+------------------------+
| 1. 环绕通知开始（Around）|
+------------------------+
            |
            v
+------------------------+
| 2. 前置通知（Before）    |
+------------------------+
            |
            v
+------------------------+
| 3. 目标方法执行          |
+------------------------+
            |
            v
+------------------------+
| 4. 环绕通知结束（Around）|
+------------------------+
            |
            v
+------------------------+
| 5. 返回通知（After Returning）|
+------------------------+
            |
            v
+------------------------+
| 6. 后置通知（After）      |
+------------------------+
            |
            v
+------------------------+
| 方法执行结束            |
+------------------------+
```

**异常情况下的执行顺序**
```
+------------------------+
| 方法执行开始            |
+------------------------+
            |
            v
+------------------------+
| 1. 环绕通知开始（Around）|
+------------------------+
            |
            v
+------------------------+
| 2. 前置通知（Before）    |
+------------------------+
            |
            v
+------------------------+
| 3. 目标方法执行（抛出异常）|
+------------------------+
            |
            v
+------------------------+
| 4. 异常通知（After Throwing）|
+------------------------+
            |
            v
+------------------------+
| 5. 后置通知（After）      |
+------------------------+
            |
            v
+------------------------+
| 方法执行结束            |
+------------------------+
```

**执行顺序详细说明**
1. **环绕通知（Around）开始**：环绕通知最先执行，包围整个目标方法
2. **前置通知（Before）**：在目标方法执行前执行
3. **目标方法执行**：执行实际的业务逻辑方法
4. **环绕通知（Around）结束**：在目标方法执行后执行
5. **返回通知（After Returning）**：在目标方法成功执行后执行，仅当方法没有抛出异常时
6. **异常通知（After Throwing）**：在目标方法抛出异常后执行，仅当方法抛出异常时
7. **后置通知（After）**：无论方法是否抛出异常，都会执行

**注意事项**
- 环绕通知可以控制目标方法的执行，甚至可以阻止目标方法的执行
- 环绕通知需要调用 `proceed()` 方法来执行目标方法
- 同一个切面内的多个通知，执行顺序由 Spring 内部实现决定，建议在同一个切面内只使用一种类型的通知

### 2.4 事务管理

Spring 提供了强大的事务管理能力，支持声明式事务和编程式事务。事务管理是企业应用开发中的核心功能，用于确保数据操作的一致性和可靠性。

#### 2.4.1 事务隔离级别

事务隔离级别定义了多个并发事务之间的隔离程度，主要解决脏读、不可重复读和幻读等并发问题。

- **DEFAULT** - 使用底层数据库的默认隔离级别。例如，MySQL 默认使用 REPEATABLE_READ，Oracle 默认使用 READ_COMMITTED。
  **适用场景**：大多数应用场景，无需特殊配置，依赖数据库默认设置。

- **READ_UNCOMMITTED** - 读未提交。允许读取其他事务未提交的数据，可能导致脏读、不可重复读和幻读。
  **适用场景**：对数据一致性要求极低，追求极致性能的场景，如实时监控系统。

- **READ_COMMITTED** - 读已提交。只允许读取其他事务已提交的数据，可以避免脏读，但可能导致不可重复读和幻读。
  **适用场景**：大多数互联网应用，注重性能和数据一致性的平衡，如电商系统的订单查询。

- **REPEATABLE_READ** - 可重复读。确保同一事务中多次读取同一数据的结果一致，可以避免脏读和不可重复读，但可能导致幻读。
  **适用场景**：需要确保数据一致性的业务场景，如金融系统的账户余额查询。

- **SERIALIZABLE** - 串行化。确保事务串行执行，完全避免并发问题，但性能开销最大。
  **适用场景**：对数据一致性要求极高的场景，如金融系统的资金转账。

#### 2.4.2 事务传播行为

事务传播行为定义了当一个事务方法被另一个事务方法调用时，事务如何传播。

- **REQUIRED** - 如果当前存在事务，则加入该事务；否则，创建一个新事务。这是最常用的传播行为。
  **适用场景**：大多数业务操作，如用户注册、订单创建等核心业务逻辑。

- **SUPPORTS** - 如果当前存在事务，则加入该事务；否则，以非事务方式执行。适用于那些不依赖事务但如果有事务也可以参与的操作。
  **适用场景**：可选事务的操作，如数据查询、统计分析等。

- **MANDATORY** - 如果当前存在事务，则加入该事务；否则，抛出异常。确保方法必须在事务中执行。
  **适用场景**：必须在事务中执行的操作，如资金扣减、库存更新等关键业务逻辑。

- **REQUIRES_NEW** - 创建一个新事务，如果当前存在事务，则挂起该事务。适用于需要独立事务的操作，如日志记录。
  **适用场景**：需要独立事务的操作，如操作日志记录、审计记录等，确保即使主事务失败也能保存记录。

- **NOT_SUPPORTED** - 以非事务方式执行，如果当前存在事务，则挂起该事务。适用于那些不需要事务的操作，如只读查询。
  **适用场景**：不需要事务的操作，如缓存更新、只读查询等，提高性能。

- **NEVER** - 以非事务方式执行，如果当前存在事务，则抛出异常。确保方法绝对不在事务中执行。
  **适用场景**：绝对不能在事务中执行的操作，如某些特定的外部系统调用。

- **NESTED** - 如果当前存在事务，则创建一个嵌套事务；否则，创建一个新事务。嵌套事务是外部事务的一部分，只有外部事务提交，嵌套事务才会提交。
  **适用场景**：需要部分回滚的场景，如批量操作中，部分操作失败不影响其他操作。

## 第三部分：Spring Bean

### 3.1 Bean 定义

Bean 是 Spring 容器管理的对象，它可以通过 XML 配置、注解或 Java 配置类来定义。

### 3.2 Bean 作用域

默认的作用域是 singleton。

- **singleton**：Spring 将 bean 定义的范围限定为每个 Spring IOC 容器只有一个单实例。
- **prototype**：单个 bean 定义有任意数量的对象实例。
- **request**：作用域为一次 HTTP 请求，该作用域仅在基于 web 的 Spring ApplicationContext 情形下有效。
- **session**：作用域为 HTTP Session，该作用域仅在基于 web 的 Spring ApplicationContext 情形下有效。
- **application**：作用域为 ServletContext，该作用域仅在基于 web 的 Spring ApplicationContext 情形下有效。
- **websocket**：作用域为 WebSocket，该作用域仅在基于 web 的 Spring ApplicationContext 情形下有效。

### 3.3 Bean 生命周期

Bean 的生命周期包括以下阶段：

1. **实例化** - 创建 Bean 实例
2. **属性注入** - 注入依赖的 Bean
3. **初始化前** - 执行 BeanPostProcessor 的 postProcessBeforeInitialization 方法
4. **初始化** - 执行 InitializingBean 的 afterPropertiesSet 方法，或执行自定义的初始化方法
5. **初始化后** - 执行 BeanPostProcessor 的 postProcessAfterInitialization 方法
6. **使用** - Bean 可以被应用程序使用
7. **销毁** - 执行 DisposableBean 的 destroy 方法，或执行自定义的销毁方法

![](https://picx.zhimg.com/80/v2-b1579aad1db6b6b7ea7376d19454d7b1_1440w.webp?source=1940ef5c)

### 3.4 Bean 装配方式

#### 3.4.1 基于 XML 的装配

```xml
<bean id="userService" class="com.example.UserService">
    <property name="userDao" ref="userDao"/>
</bean>

<bean id="userDao" class="com.example.UserDaoImpl"/>
```

#### 3.4.2 基于注解的装配

```java
@Component
public class UserService {
    @Autowired
    private UserDao userDao;
}

@Component
public class UserDaoImpl implements UserDao {
}
```

#### 3.4.3 基于 Java 配置类的装配

```java
@Configuration
public class AppConfig {
    @Bean
    public UserService userService() {
        UserService userService = new UserService();
        userService.setUserDao(userDao());
        return userService;
    }
    
    @Bean
    public UserDao userDao() {
        return new UserDaoImpl();
    }
}
```

### 3.5 自动装配

Spring 支持自动装配 Bean，可以通过 `@Autowired`、`@Resource` 或 `@Inject` 注解来实现。

#### 3.5.1 自动装配的局限性

- **覆盖的可能性** - 您始终可以使用 `<constructor-arg>` 和 `<property>` 设置指定依赖项，这将覆盖自动装配。
- **基本元数据类型** - 简单属性（如原数据类型，字符串和类）无法自动装配。
- **令人困惑的性质** - 总是喜欢使用明确的装配，因为自动装配不太精确。

## 第四部分：Spring 高级特性

### 4.1 事件机制

Spring 提供了强大的事件机制，它允许应用程序组件之间通过事件进行通信，实现松耦合的组件交互。

#### 4.1.1 内置事件

- **ContextRefreshedEvent** - Spring 容器刷新完成事件
  **适用场景**：应用启动完成后执行初始化操作，如加载缓存数据、初始化定时任务等。

- **ContextStartedEvent** - Spring 容器启动事件
  **适用场景**：容器启动时需要执行的操作，如启动后台服务、初始化连接池等。

- **ContextStoppedEvent** - Spring 容器停止事件
  **适用场景**：容器停止时需要执行的操作，如优雅关闭服务、释放资源等。

- **ContextClosedEvent** - Spring 容器关闭事件
  **适用场景**：容器关闭时需要执行的操作，如保存状态数据、清理临时文件等。

- **ServletRequestHandledEvent** - HTTP 请求处理完成事件
  **适用场景**：请求处理完成后执行的操作，如记录访问日志、统计请求耗时等。

#### 4.1.2 自定义事件

**基本使用示例**

```java
// 定义事件
public class CustomEvent extends ApplicationEvent {
    private String message;
    
    public CustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}

// 发布事件
@Component
public class EventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    public void publishEvent(String message) {
        applicationEventPublisher.publishEvent(new CustomEvent(this, message));
    }
}

// 监听事件
@Component
public class EventListener {
    @EventListener
    public void handleCustomEvent(CustomEvent event) {
        System.out.println("Received custom event: " + event.getMessage());
    }
}
```

**场景一：用户注册事件**

```java
// 定义用户注册事件
public class UserRegisteredEvent extends ApplicationEvent {
    private Long userId;
    private String username;
    private String email;
    
    public UserRegisteredEvent(Object source, Long userId, String username, String email) {
        super(source);
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
}

// 用户服务
@Service
public class UserService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void registerUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user = userRepository.save(user);
        
        eventPublisher.publishEvent(new UserRegisteredEvent(this, user.getId(), username, email));
    }
}

// 发送欢迎邮件监听器
@Component
public class WelcomeEmailListener {
    @Autowired
    private EmailService emailService;
    
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
    }
}

// 发送注册积分监听器
@Component
public class PointsListener {
    @Autowired
    private PointsService pointsService;
    
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        pointsService.addRegistrationPoints(event.getUserId());
    }
}
```

**场景二：订单创建事件**

```java
// 定义订单创建事件
public class OrderCreatedEvent extends ApplicationEvent {
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    
    public OrderCreatedEvent(Object source, Long orderId, Long userId, BigDecimal amount) {
        super(source);
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
}

// 订单服务
@Service
public class OrderService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void createOrder(Long userId, BigDecimal amount) {
        Order order = new Order();
        order.setUserId(userId);
        order.setAmount(amount);
        order = orderRepository.save(order);
        
        eventPublisher.publishEvent(new OrderCreatedEvent(this, order.getId(), userId, amount));
    }
}

// 库存扣减监听器
@Component
public class InventoryListener {
    @Autowired
    private InventoryService inventoryService;
    
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        inventoryService.deductInventory(event.getOrderId());
    }
}

// 发送订单确认短信监听器
@Component
public class SmsNotificationListener {
    @Autowired
    private SmsService smsService;
    
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        smsService.sendOrderConfirmation(event.getUserId(), event.getOrderId());
    }
}
```

**场景三：异步事件处理**

```java
// 异步配置
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-event-");
        executor.initialize();
        return executor;
    }
}

// 异步监听器
@Component
public class AsyncEventListener {
    @Async
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        try {
            Thread.sleep(2000);
            System.out.println("Async processing: " + event.getUsername());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

**场景四：条件监听器**

```java
// 条件监听器 - 只监听金额大于1000的订单
@Component
public class HighValueOrderListener {
    @EventListener(condition = "#event.amount > 1000")
    public void handleHighValueOrder(OrderCreatedEvent event) {
        System.out.println("High value order: " + event.getAmount());
    }
}

// 条件监听器 - 只监听特定类型的用户
@Component
public class VipUserListener {
    @EventListener(condition = "#event.username.startsWith('vip_')")
    public void handleVipUser(UserRegisteredEvent event) {
        System.out.println("VIP user registered: " + event.getUsername());
    }
}
```

**场景五：事务事件监听器**

```java
// 事务成功提交后执行
@Component
public class TransactionEventListener {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(OrderCreatedEvent event) {
        System.out.println("Order committed: " + event.getOrderId());
    }
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleAfterRollback(OrderCreatedEvent event) {
        System.out.println("Order rolled back: " + event.getOrderId());
    }
}
```

### 4.2 条件加载

Spring 4.0 引入了条件加载功能，它允许根据特定条件来决定是否加载某个 Bean。

#### 4.2.1 常用条件注解

- **@Conditional** - 基于自定义条件加载 Bean
- **@ConditionalOnBean** - 当容器中存在指定 Bean 时加载
- **@ConditionalOnMissingBean** - 当容器中不存在指定 Bean 时加载
- **@ConditionalOnClass** - 当类路径中存在指定类时加载
- **@ConditionalOnMissingClass** - 当类路径中不存在指定类时加载
- **@ConditionalOnProperty** - 当指定属性满足条件时加载
- **@ConditionalOnWebApplication** - 当应用是 Web 应用时加载
- **@ConditionalOnNotWebApplication** - 当应用不是 Web 应用时加载

### 4.3 环境与 Profile

Spring 提供了环境和 Profile 功能，它允许应用程序在不同的环境中使用不同的配置。

#### 4.3.1 环境配置

```java
@Configuration
public class AppConfig {
    @Autowired
    private Environment environment;
    
    @Bean
    public DataSource dataSource() {
        String url = environment.getProperty("jdbc.url");
        String username = environment.getProperty("jdbc.username");
        String password = environment.getProperty("jdbc.password");
        // 创建并返回 DataSource
    }
}
```

#### 4.3.2 Profile 配置

```java
@Configuration
@Profile("dev")
public class DevConfig {
    @Bean
    public DataSource dataSource() {
        // 返回开发环境的 DataSource
    }
}

@Configuration
@Profile("prod")
public class ProdConfig {
    @Bean
    public DataSource dataSource() {
        // 返回生产环境的 DataSource
    }
}
```

### 4.4 SpEL 表达式

Spring 表达式语言（SpEL）是一种强大的表达式语言，它支持在运行时查询和操作对象图。

#### 4.4.1 SpEL 基本语法

```java
// 在 @Value 注解中使用
@Value("#{systemProperties['user.name']}")
private String userName;

// 在 XML 配置中使用
<bean id="user" class="com.example.User">
    <property name="name" value="#{systemProperties['user.name']}"/>
</bean>
```

### 4.5 异步处理

Spring 提供了异步处理功能，它允许方法异步执行，而不会阻塞调用线程。

#### 4.5.1 异步方法配置

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}

// 使用 @Async 注解标记异步方法
@Service
public class AsyncService {
    @Async
    public CompletableFuture<String> asyncMethod() {
        // 执行异步操作
        return CompletableFuture.completedFuture("Async method result");
    }
}
```

### 4.6 BeanFactoryPostProcessor

BeanFactoryPostProcessor 是 Spring 提供的一个扩展点，它允许在 Bean 定义加载完成后，Bean 实例化之前修改 Bean 定义。

```java
@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 修改 Bean 定义
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
        beanDefinition.getPropertyValues().add("timeout", 3000);
    }
}
```

### 4.7 ApplicationListener

ApplicationListener 是 Spring 提供的一个接口，它允许监听 Spring 容器的事件。

```java
@Component
public class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 处理容器刷新事件
        System.out.println("Spring context refreshed");
    }
}
```

### 4.8 Aware 接口

Spring 提供了一系列 Aware 接口，它们允许 Bean 获取 Spring 容器的相关资源。

- **BeanNameAware** - 获取 Bean 的名称
- **BeanFactoryAware** - 获取 BeanFactory
- **ApplicationContextAware** - 获取 ApplicationContext
- **ApplicationEventPublisherAware** - 获取 ApplicationEventPublisher
- **EnvironmentAware** - 获取 Environment
- **ResourceLoaderAware** - 获取 ResourceLoader

## 第五部分：Spring Boot

### 5.1 Spring Boot 简介

Spring Boot 是 Spring 的一个子项目，它旨在简化 Spring 应用程序的创建和开发过程。Spring Boot 提供了自动配置、 starter 依赖和嵌入式服务器等功能，使得开发者可以快速创建和部署 Spring 应用程序。

### 5.2 Spring Boot 启动过程

Spring Boot 应用程序的启动过程包括以下阶段：

1. **初始化 SpringApplication** - 创建 SpringApplication 实例，加载配置
2. **准备环境** - 加载环境变量、配置文件等
3. **创建 ApplicationContext** - 根据应用类型创建相应的 ApplicationContext
4. **刷新 ApplicationContext** - 加载 Bean 定义，实例化 Bean
5. **执行 CommandLineRunner 和 ApplicationRunner** - 执行应用程序的初始化逻辑

```java
public ConfigurableApplicationContext run(String... args) {
   StopWatch stopWatch = new StopWatch();
   stopWatch.start();
   ConfigurableApplicationContext context = null;
   Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
   configureHeadlessProperty();
   SpringApplicationRunListeners listeners = getRunListeners(args);
   listeners.starting();
   try {
      ApplicationArguments applicationArguments = new DefaultApplicationArguments(
            args);
      ConfigurableEnvironment environment = prepareEnvironment(listeners,
            applicationArguments);
      configureIgnoreBeanInfo(environment);
      Banner printedBanner = printBanner(environment);
      context = createApplicationContext();
      exceptionReporters = getSpringFactoriesInstances(
            SpringBootExceptionReporter.class,
            new Class[] { ConfigurableApplicationContext.class }, context);
      prepareContext(context, environment, listeners, applicationArguments,
            printedBanner);
      refreshContext(context);
      afterRefresh(context, applicationArguments);
      stopWatch.stop();
      if (this.logStartupInfo) {
         new StartupInfoLogger(this.mainApplicationClass)
               .logStarted(getApplicationLog(), stopWatch);
      }
      listeners.started(context);
      callRunners(context, applicationArguments);
   }
   catch (Throwable ex) {
      handleRunFailure(context, ex, exceptionReporters, listeners);
      throw new IllegalStateException(ex);
   }

   try {
      listeners.running(context);
   }
   catch (Throwable ex) {
      handleRunFailure(context, ex, exceptionReporters, null);
      throw new IllegalStateException(ex);
   }
   return context;
}
```

### 5.3 Spring Boot Starter

Spring Boot Starter 是 Spring Boot 提供的一系列依赖包，它们封装了常用的第三方库和框架，使得开发者可以通过简单的依赖声明来使用这些库和框架。

#### 5.3.1 常用 Starter

- **spring-boot-starter-web** - Web 应用开发
- **spring-boot-starter-data-jpa** - JPA 数据访问
- **spring-boot-starter-security** - 安全框架
- **spring-boot-starter-test** - 测试框架
- **spring-boot-starter-actuator** - 应用监控

### 5.4 自动配置原理

**自动配置原理过程图**
```
+----------------------------------+
| 1. 应用启动                      |
+----------------------------------+
            |
            v
+----------------------------------+
| 2. @SpringBootApplication 注解   |
+----------------------------------+
            |
            v
+----------------------------------+
| 3. @EnableAutoConfiguration 注解 |
+----------------------------------+
            |
            v
+----------------------------------+
| 4. AutoConfigurationImportSelector|
+----------------------------------+
            |
            v
+----------------------------------+
| 5. 扫描 META-INF/spring.factories |
+----------------------------------+
            |
            v
+----------------------------------+
| 6. 加载自动配置类列表            |
+----------------------------------+
            |
            v
+----------------------------------+
| 7. 条件注解判断                  |
+----------------------------------+
            |
            v
+----------------------------------+
| 8. 符合条件的配置类生效          |
+----------------------------------+
            |
            v
+----------------------------------+
| 9. Bean 实例化和注入            |
+----------------------------------+
            |
            v
+----------------------------------+
| 10. 应用启动完成                  |
+----------------------------------+
```

**自动配置详细原理**

1. **启用自动配置**：
   - `@SpringBootApplication` 注解包含了 `@EnableAutoConfiguration` 注解
   - `@EnableAutoConfiguration` 注解通过 `AutoConfigurationImportSelector` 类实现自动配置

2. **扫描自动配置类**：
   - `AutoConfigurationImportSelector` 扫描所有 JAR 包中的 `META-INF/spring.factories` 文件
   - 从文件中加载 `EnableAutoConfiguration` 对应的自动配置类列表

3. **条件判断**：
   - 每个自动配置类都使用了 `@Conditional` 系列注解
   - 根据条件判断是否加载该配置类，例如：
     - `@ConditionalOnClass`：当类路径中存在指定类时
     - `@ConditionalOnMissingBean`：当容器中不存在指定 Bean 时
     - `@ConditionalOnProperty`：当指定属性满足条件时

4. **配置生效**：
   - 符合条件的自动配置类会被加载到 Spring 容器中
   - 这些配置类会根据需要创建和配置 Bean

5. **优先级控制**：
   - 自动配置类的优先级低于用户自定义配置
   - 用户可以通过 `@EnableAutoConfiguration(exclude = {...})` 排除不需要的自动配置

**自动配置的优点**
- **简化配置**：减少了大量的 XML 或 Java 配置代码
- **智能适配**：根据环境和依赖自动调整配置
- **可扩展性**：用户可以轻松覆盖或扩展自动配置
- **一致性**：提供了一套标准化的配置方式

**示例**
以 `DataSource` 自动配置为例：
1. Spring Boot 扫描 `spring-boot-autoconfigure.jar` 中的 `META-INF/spring.factories` 文件
2. 加载 `DataSourceAutoConfiguration` 类
3. 检查类路径中是否存在数据库驱动类
4. 检查容器中是否已存在 `DataSource` Bean
5. 如果条件满足，自动创建并配置 `DataSource` Bean
6. 用户可以通过 `application.properties` 中的 `spring.datasource.*` 属性自定义配置

### 5.5 自定义 Spring Boot Starter

**基本概念**
Spring Boot Starter 是一种依赖管理机制，它将相关的依赖项打包在一起，简化了项目的依赖配置。自定义 Starter 可以将业务逻辑或通用功能封装成可重用的模块。

**实现步骤**

**自定义 Starter 过程图**
```
+----------------------------------+
| 1. 创建 Maven 项目               |
+----------------------------------+
            |
            v
+----------------------------------+
| 2. 添加依赖                      |
+----------------------------------+
            |
            v
+----------------------------------+
| 3. 实现业务逻辑                  |
+----------------------------------+
            |
            v
+----------------------------------+
| 4. 创建配置类                    |
+----------------------------------+
            |
            v
+----------------------------------+
| 5. 创建自动配置类                |
+----------------------------------+
            |
            v
+----------------------------------+
| 6. 配置 spring.factories         |
+----------------------------------+
            |
            v
+----------------------------------+
| 7. 打包安装                      |
+----------------------------------+
            |
            v
+----------------------------------+
| 8. 在其他项目中使用              |
+----------------------------------+
```

**样例代码**

**1. 创建 Maven 项目**

**2. 添加依赖**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <version>2.7.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
        <version>2.7.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <version>2.7.0</version>
        <optional>true</optional>
    </dependency>
</dependencies>
```

**3. 实现业务逻辑**
```java
// 核心服务接口
public interface HelloService {
    String sayHello(String name);
}

// 核心服务实现
public class HelloServiceImpl implements HelloService {
    private String prefix;
    private String suffix;
    
    public HelloServiceImpl(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    @Override
    public String sayHello(String name) {
        return prefix + name + suffix;
    }
}
```

**4. 创建配置类**
```java
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom.hello")
public class HelloProperties {
    private String prefix = "Hello, ";
    private String suffix = "!";
    
    // getters and setters
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
```

**5. 创建自动配置类**
```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HelloProperties.class)
public class HelloAutoConfiguration {
    private final HelloProperties properties;
    
    public HelloAutoConfiguration(HelloProperties properties) {
        this.properties = properties;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public HelloService helloService() {
        return new HelloServiceImpl(properties.getPrefix(), properties.getSuffix());
    }
}
```

**6. 配置 spring.factories**
在 `src/main/resources/META-INF/` 目录下创建 `spring.factories` 文件：
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.hello.autoconfigure.HelloAutoConfiguration
```

**7. 打包安装**
```bash
mvn clean install
```

**8. 在其他项目中使用**

**添加依赖**
```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>hello-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

**配置属性**
在 `application.properties` 文件中添加：
```properties
custom.hello.prefix=Hi, 
custom.hello.suffix=!!
```

**使用服务**
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private HelloService helloService;
    
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return helloService.sayHello(name);
    }
}
```

**测试**
启动应用后，访问 `http://localhost:8080/hello/World`，应该返回 `Hi, World!!`。

**自定义 Starter 的最佳实践**
- **命名规范**：推荐使用 `xxx-spring-boot-starter` 命名格式
- **依赖管理**：避免引入过多依赖，保持 Starter 轻量
- **条件配置**：使用 `@Conditional` 系列注解实现智能配置
- **属性配置**：使用 `@ConfigurationProperties` 提供灵活的配置选项
- **文档完善**：提供清晰的使用文档和示例

**常见问题**
- **自动配置不生效**：检查 `spring.factories` 文件路径和内容是否正确
- **依赖冲突**：注意版本兼容性，避免依赖冲突
- **配置覆盖**：确保用户可以通过配置文件覆盖默认配置

**总结**
自定义 Spring Boot Starter 是一种将业务逻辑或通用功能封装成可重用模块的有效方式，它遵循 "约定优于配置" 的原则，简化了项目的依赖管理和配置。通过合理的设计和实现，可以大大提高代码的复用性和可维护性。

## 第六部分：Spring 常见问题

### 6.1 循环依赖

循环依赖是指两个或多个 Bean 之间相互依赖，形成一个闭环。Spring 通过三级缓存机制来解决循环依赖问题。

#### 6.1.1 三级缓存

- **一级缓存**（singletonObjects）- 存储完全初始化的 Bean
- **二级缓存**（earlySingletonObjects）- 存储早期暴露的 Bean 实例
- **三级缓存**（singletonFactories）- 存储 Bean 工厂，用于创建早期暴露的 Bean 实例

#### 6.1.2 循环依赖解决过程

**详细的获取 Bean 过程**：

1. **获取 Bean A**：
   - 首先尝试从一级缓存（singletonObjects）中获取 A
   - 如果一级缓存没有，尝试从二级缓存（earlySingletonObjects）中获取 A
   - 如果二级缓存也没有，尝试从三级缓存（singletonFactories）中获取 A
   - 如果三级缓存也没有，开始创建 Bean A

2. **创建 Bean A**：
   - 实例化 A（调用构造函数）
   - 将 A 的工厂对象放入三级缓存（singletonFactories）
   - 开始为 A 注入依赖

3. **注入依赖 B**：
   - A 需要注入依赖 B，开始获取 Bean B

4. **获取 Bean B**：
   - 首先尝试从一级缓存中获取 B
   - 如果一级缓存没有，尝试从二级缓存中获取 B
   - 如果二级缓存也没有，尝试从三级缓存中获取 B
   - 如果三级缓存也没有，开始创建 Bean B

5. **创建 Bean B**：
   - 实例化 B（调用构造函数）
   - 将 B 的工厂对象放入三级缓存
   - 开始为 B 注入依赖

6. **注入依赖 A**：
   - B 需要注入依赖 A，开始获取 Bean A
   - 从三级缓存中获取 A 的工厂对象
   - 使用工厂对象创建 A 的早期实例
   - 将 A 的早期实例放入二级缓存（earlySingletonObjects）
   - 从三级缓存中移除 A 的工厂对象
   - 将 A 的早期实例注入到 B 中

7. **完成 Bean B 的初始化**：
   - B 初始化完成（调用初始化方法）
   - 将 B 从三级缓存中移除
   - 将 B 放入一级缓存（singletonObjects）

8. **完成 Bean A 的初始化**：
   - A 注入 B 完成
   - A 初始化完成（调用初始化方法）
   - 将 A 从二级缓存中移除
   - 将 A 放入一级缓存（singletonObjects）

**最终状态**：
- 一级缓存（singletonObjects）中包含完全初始化的 Bean A 和 Bean B
- 二级缓存（earlySingletonObjects）为空
- 三级缓存（singletonFactories）为空

### 6.2 事务失效

事务失效是指 Spring 事务注解 `@Transactional` 不生效的情况。

#### 6.2.1 事务失效的原因

- **方法的访问权限必须是 public**，其他 private 等权限，事务失效
- **方法被定义成了 final 的**，这样会导致事务失效
- **在同一个类中的方法直接内部调用**，会导致事务失效
- **一个方法如果没交给 spring 管理**，就不会生成 spring 事务
- **如果自己 try...catch 误吞了异常**，事务失效
- **错误的传播行为**，如使用了 `NOT_SUPPORTED` 或 `NEVER`
- **多线程调用**，两个方法不在同一个线程中，获取到的数据库连接不一样的
- **表的存储引擎不支持事务**，如 MyISAM

### 6.3 DispatcherServlet 的工作流程

Spring MVC 的核心是 DispatcherServlet，它负责处理 HTTP 请求并返回响应。

![](https://mmbiz.qpic.cn/mmbiz_png/ibLfOIwWn242drgCjBCwUTfnzr76ftCdn29LjYtpwBYmaicaKGJMg89nS0YBrlmBTxZ9KicFEqYDAK3l4s95PicjxQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

**详细的工作流程图**
```
+----------------------------------+
| 1. 客户端发送 HTTP 请求           |
+----------------------------------+
            |
            v
+----------------------------------+
| 2. 请求被 DispatcherServlet 捕获   |
+----------------------------------+
            |
            v
+----------------------------------+
| 3. 调用 HandlerMapping            |
+----------------------------------+
            |
            v
+----------------------------------+
| 4. 解析请求 URL，找到匹配的 Handler |
+----------------------------------+
            |
            v
+----------------------------------+
| 5. 返回 HandlerExecutionChain     |
+----------------------------------+
            |
            v
+----------------------------------+
| 6. 调用 HandlerAdapter            |
+----------------------------------+
            |
            v
+----------------------------------+
| 7. 执行前置处理器（PreHandler）    |
+----------------------------------+
            |
            v
+----------------------------------+
| 8. 执行 Controller 方法           |
+----------------------------------+
            |
            v
+----------------------------------+
| 9. 执行后置处理器（PostHandler）   |
+----------------------------------+
            |
            v
+----------------------------------+
| 10. 返回 ModelAndView 对象        |
+----------------------------------+
            |
            v
+----------------------------------+
| 11. 调用 ViewResolver             |
+----------------------------------+
            |
            v
+----------------------------------+
| 12. 解析视图名称，找到对应视图     |
+----------------------------------+
            |
            v
+----------------------------------+
| 13. 渲染视图（填充模型数据）       |
+----------------------------------+
            |
            v
+----------------------------------+
| 14. 返回 HTTP 响应给客户端         |
+----------------------------------+
```

**步骤说明**：

1. **请求捕获** - 向服务器发送 HTTP 请求，请求被前端控制器 DispatcherServlet 捕获
2. **处理器映射** - DispatcherServlet 根据请求的 URL 调用 HandlerMapping 获得该 Handler 配置的所有相关的对象，最后以 HandlerExecutionChain 对象的形式返回
3. **处理器适配** - DispatcherServlet 根据获得的 Handler ，选择一个合适的 HandlerAdapter
4. **处理器执行** - 提取 Request 中的模型数据，填充 Handler 入参，开始执行 Handler（Controller），返回一个 ModelAndView 对象
5. **视图解析** - 根据返回的 ModelAndView ，选择一个适合的 ViewResolver 返回给 DispatcherServlet
6. **视图渲染** - ViewResolver 结合 Model 和 View ，来渲染视图
7. **响应返回** - 视图负责将渲染结果返回给客户端

## 第七部分：Spring 面试题解析

### 7.1 基础概念

#### 7.1.1 什么是 Spring？

**答案**：Spring 是一个开源的 Java 企业级应用开发框架，它提供了全面的基础设施支持，用于构建企业级应用程序。Spring 的核心是控制反转（IoC）和面向切面编程（AOP），它使得开发者可以专注于业务逻辑的实现，而不用过多关注底层的技术细节。

#### 7.1.2 Spring 的核心功能有哪些？

**答案**：Spring 的核心功能包括：
- 轻量级容器
- 控制反转（IoC）
- 依赖注入（DI）
- 面向切面编程（AOP）
- 事务管理
- MVC Web 框架
- 数据访问支持
- 集成其他框架

### 7.2 核心技术

#### 7.2.1 什么是 IoC？什么是 DI？它们之间的关系是什么？

**答案**：
- **IoC（Inversion of Control）** - 控制反转，将对象的创建和依赖关系的管理交给 Spring 容器，而不是由应用程序代码直接控制。
- **DI（Dependency Injection）** - 依赖注入，是 IoC 的具体实现方式，它通过构造函数、setter 方法或字段注入的方式，将依赖的对象注入到目标对象中。
- **关系** - DI 是 IoC 的具体实现，IoC 是一种设计思想，而 DI 是这种思想的具体实现技术。

#### 7.2.2 什么是 AOP？AOP 的核心概念有哪些？

**答案**：
- **AOP（Aspect-Oriented Programming）** - 面向切面编程，是一种编程范式，它允许开发者将横切关注点（如日志、事务、安全等）从业务逻辑中分离出来，以提高代码的可维护性和可重用性。
- **核心概念** - 切面（Aspect）、连接点（Join Point）、通知（Advice）、切点（Pointcut）、引入（Introduction）、织入（Weaving）。

#### 7.2.3 Spring AOP 和 AspectJ 的区别是什么？

**答案**：
- **Spring AOP** - 基于动态代理实现，只能拦截方法调用，性能较好，适合于简单的横切关注点。
- **AspectJ** - 基于字节码增强实现，可以拦截字段访问、构造函数调用等，功能更强大，适合于复杂的横切关注点。

### 7.3 Bean 相关

#### 7.3.1 Spring Bean 的作用域有哪些？

**答案**：Spring Bean 的作用域包括：
- **singleton** - 单例，每个 Spring 容器只有一个实例
- **prototype** - 原型，每次获取都创建一个新实例
- **request** - 请求，每个 HTTP 请求创建一个实例
- **session** - 会话，每个 HTTP 会话创建一个实例
- **application** - 应用，每个 ServletContext 创建一个实例
- **websocket** - WebSocket，每个 WebSocket 连接创建一个实例

#### 7.3.2 Spring Bean 的生命周期是什么？

**答案**：Spring Bean 的生命周期包括以下阶段：
1. 实例化 - 创建 Bean 实例
2. 属性注入 - 注入依赖的 Bean
3. 初始化前 - 执行 BeanPostProcessor 的 postProcessBeforeInitialization 方法
4. 初始化 - 执行 InitializingBean 的 afterPropertiesSet 方法，或执行自定义的初始化方法
5. 初始化后 - 执行 BeanPostProcessor 的 postProcessAfterInitialization 方法
6. 使用 - Bean 可以被应用程序使用
7. 销毁 - 执行 DisposableBean 的 destroy 方法，或执行自定义的销毁方法

### 7.4 Spring Boot

#### 7.4.1 Spring Boot 的核心特性有哪些？

**答案**：Spring Boot 的核心特性包括：
- **自动配置** - 根据依赖自动配置应用程序
- **Starter 依赖** - 提供常用依赖的集合
- **嵌入式服务器** - 内置 Tomcat、Jetty 或 Undertow
- **命令行界面** - 用于快速开发 Spring 应用程序
- **Actuator** - 提供应用程序监控功能
- **无代码生成和 XML 配置** - 使用注解和 Java 配置

#### 7.4.2 Spring Boot 的启动过程是什么？

**Spring Boot 启动过程流程图**
```
+----------------------------------+
| 1. 执行 main 方法                 |
+----------------------------------+
            |
            v
+----------------------------------+
| 2. 初始化 SpringApplication       |
|    - 创建 SpringApplication 实例  |
|    - 加载配置信息                 |
|    - 注册监听器                   |
+----------------------------------+
            |
            v
+----------------------------------+
| 3. 执行 run 方法                  |
+----------------------------------+
            |
            v
+----------------------------------+
| 4. 准备环境                       |
|    - 加载环境变量                 |
|    - 加载配置文件                 |
|    - 创建 Environment 对象        |
+----------------------------------+
            |
            v
+----------------------------------+
| 5. 打印 Banner                    |
+----------------------------------+
            |
            v
+----------------------------------+
| 6. 创建 ApplicationContext        |
|    - 根据应用类型创建上下文       |
|    - Web 应用：AnnotationConfigServletWebServerApplicationContext |
|    - 非 Web 应用：AnnotationConfigApplicationContext |
+----------------------------------+
            |
            v
+----------------------------------+
| 7. 准备上下文                     |
|    - 应用环境信息                 |
|    - 注册 Bean 定义               |
|    - 应用监听器                   |
+----------------------------------+
            |
            v
+----------------------------------+
| 8. 刷新 ApplicationContext        |
|    - 加载 Bean 定义               |
|    - 实例化单例 Bean              |
|    - 初始化 Bean                  |
|    - 处理 Bean 依赖关系           |
+----------------------------------+
            |
            v
+----------------------------------+
| 9. 刷新后处理                     |
|    - 执行自定义初始化逻辑         |
|    - 启动嵌入式服务器             |
+----------------------------------+
            |
            v
+----------------------------------+
| 10. 执行 CommandLineRunner        |
|     和 ApplicationRunner          |
+----------------------------------+
            |
            v
+----------------------------------+
| 11. 应用启动完成                  |
+----------------------------------+
```

**Spring 框架启动过程流程图**
```
+----------------------------------+
| 1. 加载 Spring 配置文件/类         |
+----------------------------------+
            |
            v
+----------------------------------+
| 2. 创建 BeanFactory              |
|    - XmlBeanFactory / DefaultListableBeanFactory |
+----------------------------------+
            |
            v
+----------------------------------+
| 3. 加载 Bean 定义                 |
|    - 解析 XML 配置文件            |
|    - 扫描注解配置类               |
|    - 注册 BeanDefinition          |
+----------------------------------+
            |
            v
+----------------------------------+
| 4. 注册 BeanPostProcessor        |
|    - 前置处理器                   |
|    - 后置处理器                   |
+----------------------------------+
            |
            v
+----------------------------------+
| 5. 实例化单例 Bean                |
|    - 调用构造函数                 |
|    - 处理循环依赖                 |
+----------------------------------+
            |
            v
+----------------------------------+
| 6. 注入依赖                       |
|    - setter 方法注入              |
|    - 构造函数注入                 |
|    - 字段注入                     |
+----------------------------------+
            |
            v
+----------------------------------+
| 7. 初始化 Bean                    |
|    - 执行 BeanNameAware           |
|    - 执行 BeanFactoryAware        |
|    - 执行 ApplicationContextAware |
|    - 执行 InitializingBean        |
|    - 执行自定义初始化方法         |
+----------------------------------+
            |
            v
+----------------------------------+
| 8. 执行后置处理器                 |
|    - 执行 postProcessAfterInitialization |
|    - AOP 代理创建                 |
+----------------------------------+
            |
            v
+----------------------------------+
| 9. 注册销毁回调                   |
|    - 执行 DisposableBean          |
|    - 执行自定义销毁方法           |
+----------------------------------+
            |
            v
+----------------------------------+
| 10. Spring 容器启动完成           |
+----------------------------------+
```

**答案**：

**Spring Boot 应用程序的启动过程**包括以下阶段：
1. 初始化 SpringApplication - 创建 SpringApplication 实例，加载配置
2. 准备环境 - 加载环境变量、配置文件等
3. 创建 ApplicationContext - 根据应用类型创建相应的 ApplicationContext
4. 刷新 ApplicationContext - 加载 Bean 定义，实例化 Bean
5. 执行 CommandLineRunner 和 ApplicationRunner - 执行应用程序的初始化逻辑

**Spring 框架的启动过程**包括以下阶段：
1. 加载 Spring 配置文件/类 - XML 配置文件或注解配置类
2. 创建 BeanFactory - 如 XmlBeanFactory 或 DefaultListableBeanFactory
3. 加载 Bean 定义 - 解析配置文件，扫描注解，注册 BeanDefinition
4. 注册 BeanPostProcessor - 前置处理器和后置处理器
5. 实例化单例 Bean - 调用构造函数，处理循环依赖
6. 注入依赖 - setter 方法注入、构造函数注入或字段注入
7. 初始化 Bean - 执行各种 Aware 接口，InitializingBean，自定义初始化方法
8. 执行后置处理器 - 如 AOP 代理创建
9. 注册销毁回调 - 执行 DisposableBean 和自定义销毁方法
10. Spring 容器启动完成

### 7.5 常见问题

#### 7.5.1 Spring 是如何解决循环依赖的？

**答案**：Spring 通过三级缓存机制来解决循环依赖问题：
- **一级缓存**（singletonObjects）- 存储完全初始化的 Bean
- **二级缓存**（earlySingletonObjects）- 存储早期暴露的 Bean 实例
- **三级缓存**（singletonFactories）- 存储 Bean 工厂，用于创建早期暴露的 Bean 实例

**详细的获取 Bean 过程**：

1. **获取 Bean A**：
   - 首先尝试从一级缓存（singletonObjects）中获取 A
   - 如果一级缓存没有，尝试从二级缓存（earlySingletonObjects）中获取 A
   - 如果二级缓存也没有，尝试从三级缓存（singletonFactories）中获取 A
   - 如果三级缓存也没有，开始创建 Bean A

2. **创建 Bean A**：
   - 实例化 A（调用构造函数）
   - 将 A 的工厂对象放入三级缓存（singletonFactories）
   - 开始为 A 注入依赖

3. **注入依赖 B**：
   - A 需要注入依赖 B，开始获取 Bean B

4. **获取 Bean B**：
   - 首先尝试从一级缓存中获取 B
   - 如果一级缓存没有，尝试从二级缓存中获取 B
   - 如果二级缓存也没有，尝试从三级缓存中获取 B
   - 如果三级缓存也没有，开始创建 Bean B

5. **创建 Bean B**：
   - 实例化 B（调用构造函数）
   - 将 B 的工厂对象放入三级缓存
   - 开始为 B 注入依赖

6. **注入依赖 A**：
   - B 需要注入依赖 A，开始获取 Bean A
   - 从三级缓存中获取 A 的工厂对象
   - 使用工厂对象创建 A 的早期实例
   - 将 A 的早期实例放入二级缓存（earlySingletonObjects）
   - 从三级缓存中移除 A 的工厂对象
   - 将 A 的早期实例注入到 B 中

7. **完成 Bean B 的初始化**：
   - B 初始化完成（调用初始化方法）
   - 将 B 从三级缓存中移除
   - 将 B 放入一级缓存（singletonObjects）

8. **完成 Bean A 的初始化**：
   - A 注入 B 完成
   - A 初始化完成（调用初始化方法）
   - 将 A 从二级缓存中移除
   - 将 A 放入一级缓存（singletonObjects）

#### 7.5.2 什么情况下事务会失效？

**答案**：事务失效的原因包括：
- **方法的访问权限必须是 public**，其他 private 等权限，事务失效
- **方法被定义成了 final 的**，这样会导致事务失效
- **在同一个类中的方法直接内部调用**，会导致事务失效
- **一个方法如果没交给 spring 管理**，就不会生成 spring 事务
- **如果自己 try...catch 误吞了异常**，事务失效
- **错误的传播行为**，如使用了 `NOT_SUPPORTED` 或 `NEVER`
- **多线程调用**，两个方法不在同一个线程中，获取到的数据库连接不一样的
- **表的存储引擎不支持事务**，如 MyISAM

## 第八部分：参考链接

### 8.1 官方文档

- [Spring Framework 官方文档](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)

### 8.2 学习资源

- [Spring 官方博客](https://spring.io/blog)
- [Spring 官方 GitHub](https://github.com/spring-projects)
- [Spring 教程 - Baeldung](https://www.baeldung.com/spring-tutorial)

### 8.3 中文资源

- [Spring 中文文档](https://springdoc.cn/)
- [Spring Boot 中文文档](https://springdoc.cn/spring-boot/)
- [Spring 实战（第 5 版）](https://www.amazon.cn/dp/B07P8XSYGX)

### 8.4 视频教程

- [Spring 核心技术 - B 站](https://www.bilibili.com/video/BV1jR4y187Yn/)
- [Spring Boot 实战 - B 站](https://www.bilibili.com/video/BV1PE411i7CV/)

### 8.5 其他资源

- [Spring 源码分析](https://github.com/seaswalker/spring-analysis)
- [Spring Boot 源码分析](https://github.com/seaswalker/spring-boot-analysis)
- [Spring 常见问题汇总](https://github.com/seaswalker/spring-questions)

