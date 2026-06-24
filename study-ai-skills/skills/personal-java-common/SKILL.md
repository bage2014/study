---
name: "personal-java-common"
description: "Java 通用工具技能，提供日期工具、日志工具等公共代码，基于 study-common 代码库"
trigger: "需要使用通用工具类或公共代码时"
disable-when: "项目已有完善的工具类库或使用其他工具框架"
category: "personal"
tags: ["java", "util", "common", "tools", "date", "logging"]
requires: ["common-coding"]
---

# personal-java-common

## 功能描述

该技能提供 Java 通用工具类和公共代码，基于 `study-common` 代码库。包含日期处理、日志工具、字符串处理、集合工具等常用工具类。

## 触发条件

- 新建 Java 项目需要基础工具类时
- 需要统一的日期格式化处理时
- 需要标准化的日志记录方式时
- 需要通用的字符串和集合处理工具时

## 何时使用

- 新项目初始化时，需要快速引入基础工具类
- 现有项目需要统一工具类风格时
- 需要复用 study-common 中的公共代码时

## 何时不使用

- 项目已使用成熟的工具库（如 Apache Commons、Guava）
- 团队已有统一的工具类规范
- 仅需要少量简单的工具方法

## 核心功能

### 1. 日期工具类

- DateUtils - 日期格式化、解析、计算
- DateTimeUtils - Java 8+ DateTime API 封装
- TimestampUtils - 时间戳处理

### 2. 日志工具类

- LogUtils - 日志记录封装
- TraceLogUtils - 链路追踪日志
- AccessLogUtils - 访问日志记录

### 3. 字符串工具类

- StringUtils - 字符串处理工具
- RegexUtils - 正则表达式工具
- EncodeUtils - 编码解码工具

### 4. 集合工具类

- CollectionUtils - 集合操作工具
- ListUtils - List 操作工具
- MapUtils - Map 操作工具

### 5. 埋点工具类（注解+切面实现）

- @Metric - 指标埋点注解，自动记录方法调用次数、耗时、错误率
- MetricAspect - 埋点切面，处理 @Metric 注解
- MetricService - 指标服务，提供手动埋点能力
- MetricAutoConfiguration - 自动配置类

### 6. 线程池工具类（自动配置实现）

- ThreadPoolAutoConfiguration - 线程池自动配置类
- ThreadPoolProperties - 线程池配置属性类
- study.thread-pool.* - 配置项：corePoolSize, maxPoolSize, queueCapacity 等

### 7. 异常处理模块（注解+切面）

- BusinessException - 业务异常类
- ErrorCode - 错误码枚举
- ErrorResponse - 错误响应封装
- ExceptionAspect - 全局异常处理切面
- ExceptionAutoConfiguration - 自动配置类

### 8. 通用工具类

- DateUtil - 日期处理工具
- JsonUtil - JSON 序列化/反序列化
- CollectionUtil - 集合操作工具
- AesUtil - AES 加密工具
- HttpUtil - HTTP 请求工具
- IdGenerator - ID 生成器
- ValidatorUtil - 参数校验工具

## 输入参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| projectPath | String | 是 | 项目路径 |
| includeDateUtils | Boolean | 否 | 是否引入日期工具类，默认 true |
| includeLogUtils | Boolean | 否 | 是否引入日志工具类，默认 true |
| includeStringUtils | Boolean | 否 | 是否引入字符串工具类，默认 true |
| includeCollectionUtils | Boolean | 否 | 是否引入集合工具类，默认 true |
| includeMetricUtils | Boolean | 否 | 是否引入埋点工具类，默认 true |
| includeThreadUtils | Boolean | 否 | 是否引入线程池工具类，默认 true |
| includeJsonUtils | Boolean | 否 | 是否引入 JSON 工具类，默认 true |

## 输出格式

生成的模块结构（基于 study-common）：
├── study-common-util/              # 工具类模块
│   └── src/main/java/com/study/common/util/
│       ├── DateUtil.java
│       ├── JsonUtil.java
│       ├── CollectionUtil.java
│       ├── AesUtil.java
│       ├── HttpUtil.java
│       ├── IdGenerator.java
│       └── ValidatorUtil.java
├── study-common-log/               # 日志模块（注解+切面）
│   └── src/main/java/com/study/common/log/
│       ├── annotation/Log.java
│       ├── aspect/LogAspect.java
│       └── config/LogAutoConfiguration.java
├── study-common-metrics/           # 埋点模块（注解+切面）
│   └── src/main/java/com/study/common/metrics/
│       ├── annotation/Metric.java
│       ├── aspect/MetricAspect.java
│       ├── config/MetricAutoConfiguration.java
│       └── service/MetricService.java
├── study-common-thread-pool/       # 线程池模块（自动配置）
│   └── src/main/java/com/study/common/threadpool/
│       ├── config/ThreadPoolAutoConfiguration.java
│       └── config/ThreadPoolProperties.java
└── study-common-exception/         # 异常处理模块（注解+切面）
    └── src/main/java/com/study/common/exception/
        ├── BusinessException.java
        ├── ErrorCode.java
        ├── response/ErrorResponse.java
        ├── aspect/ExceptionAspect.java
        └── config/ExceptionAutoConfiguration.java

## 使用流程

1. 在项目中引入 study-common 依赖
2. 配置 Maven/Gradle 依赖
3. 按需引入所需的工具类
4. 在业务代码中使用工具类

## 调用示例

java
// 日期处理
LocalDateTime now = DateUtil.now();
String formatted = DateUtil.format(now, "yyyy-MM-dd HH:mm:ss");

// 日志记录（基于 @Log 注解）
@Log
public void processOrder(String orderId) {
    // 业务逻辑
}

// 字符串处理
boolean isBlank = StringUtil.isBlank(str);
String trimmed = StringUtil.trimToEmpty(str);

// JSON处理
String json = JsonUtil.toJson(obj);
MyObject obj = JsonUtil.fromJson(json, MyObject.class);

// 埋点监控（基于 @Metric 注解 + 切面）
@Metric("order.create")
public Order createOrder(OrderCreateRequest request) {
    // 自动记录：调用次数、耗时、错误率
    return orderService.create(request);
}

// 手动埋点
@Autowired
private MetricService metricService;

public void handleEvent() {
    metricService.record("event.count", 1);
    metricService.recordDuration("event.duration", System.currentTimeMillis());
}


## 配置要求

### Maven 依赖

xml
<!-- 工具类模块 -->
<dependency>
    <groupId>com.study</groupId>
    <artifactId>study-common-util</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- 日志模块（注解+切面） -->
<dependency>
    <groupId>com.study</groupId>
    <artifactId>study-common-log</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- 埋点模块（注解+切面） -->
<dependency>
    <groupId>com.study</groupId>
    <artifactId>study-common-metrics</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- 线程池模块（自动配置） -->
<dependency>
    <groupId>com.study</groupId>
    <artifactId>study-common-thread-pool</artifactId>
    <version>1.0.0</version>
</dependency>


### 线程池配置

yaml
# application.yml
study:
  thread-pool:
    core-pool-size: 4
    max-pool-size: 8
    queue-capacity: 100
    thread-name-prefix: study-thread-
    keep-alive-seconds: 60


### 日志配置

xml
<!-- logback.xml -->
<logger name="com.study.common.log" level="INFO" />


## 扩展指南

- 新增工具类时，按照功能分类放置到对应目录
- 遵循统一的命名规范和代码风格
- 添加单元测试确保工具类的正确性
- 更新 README 文档说明新增功能

## 最佳实践

1. 工具类方法应保持幂等性
2. 避免在工具类中持有状态
3. 使用 static 方法便于调用
4. 提供完善的异常处理和日志记录
5. 遵循单一职责原则，每个工具类专注于一类功能
