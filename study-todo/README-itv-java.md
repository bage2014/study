# Java 核心技术

## 目录

- [Java 核心技术](#java-核心技术)
  - [目录](#目录)
  - [1. 基础概念](#1-基础概念)
  - [2. 数据类型](#2-数据类型)
  - [3. 集合框架](#3-集合框架)
    - [3.1 List](#31-list)
    - [3.2 Map](#32-map)
    - [3.3 Set](#33-set)
  - [4. 多线程](#4-多线程)
    - [4.1 线程状态](#41-线程状态)
    - [4.2 线程池](#42-线程池)
    - [4.3 ThreadLocal](#43-threadlocal)
  - [5. 反射与注解](#5-反射与注解)
    - [5.1 反射](#51-反射)
    - [5.2 注解](#52-注解)
  - [6. JVM 核心](#6-jvm-核心)
    - [6.1 内存结构](#61-内存结构)
    - [6.2 垃圾回收](#62-垃圾回收)
    - [6.3 类加载](#63-类加载)
  - [7. Java 8+ 新特性](#7-java-8-新特性)
  - [8. 设计模式](#8-设计模式)
  - [9. 面试题解析](#9-面试题解析)
  - [10. 参考链接](#10-参考链接)

## 1. 基础概念

Java 是一种面向对象的编程语言，具有以下特点：

- **简单性**：语法清晰，易于学习和使用
- **面向对象**：支持封装、继承、多态
- **平台无关性**：一次编写，到处运行
- **分布式**：支持网络编程
- **健壮性**：强类型检查，自动内存管理
- **安全性**：内置安全机制
- **多线程**：支持并发编程
- **动态性**：支持反射和运行时类型识别

## 2. 数据类型

Java 中的数据类型分为两大类：

- **基本数据类型**：byte、short、int、long、float、double、char、boolean
- **引用数据类型**：类、接口、数组

### 基本数据类型占用空间

| 数据类型 | 大小/位 | 可表示的数据范围 | 默认值 |
|---------|--------|----------------|--------|
| byte | 8 | -128 ～ 127 | 0 |
| short | 16 | -32768 ～ 32767 | 0 |
| int | 32 | -2147483648 ～ 2147483647 | 0 |
| long | 64 | -9223372036854775808 ～ 9223372036854775807 | 0L |
| float | 32 | -3.4E38 ～ 3.4E38 | 0.0f |
| double | 64 | -1.7E308 ～ 1.7E308 | 0.0d |
| char | 16 | 0 ～ 65535 | '\u0000' |
| boolean | 1/8 (实际) | true/false | false |

### 引用类型

- **String**：不可变字符串
- **StringBuilder**：可变字符串，非线程安全
- **StringBuffer**：可变字符串，线程安全

## 3. 集合框架

### 3.1 List

- **ArrayList**：基于动态数组实现，随机访问快，插入删除慢
- **LinkedList**：基于双向链表实现，插入删除快，随机访问慢
- **Vector**：线程安全的ArrayList，性能较差

### 3.2 Map

- **HashMap**：基于哈希表实现，非线程安全
  - 结构：数组 + 链表（JDK 7）/ 数组 + 链表 + 红黑树（JDK 8+）
  - 初始容量：16
  - 负载因子：0.75
  - 扩容：当元素数量超过容量 * 负载因子时，扩容为原来的 2 倍
- **LinkedHashMap**：继承自HashMap，维护插入顺序
- **TreeMap**：基于红黑树实现，有序
- **ConcurrentHashMap**：线程安全的HashMap，性能优于Hashtable
- **WeakHashMap**：弱引用键，适用于缓存

### 3.3 Set

- **HashSet**：基于HashMap实现，无序
- **LinkedHashSet**：继承自HashSet，维护插入顺序
- **TreeSet**：基于TreeMap实现，有序

## 4. 多线程

### 4.1 线程状态

- **NEW**：新建状态，线程尚未启动
- **RUNNABLE**：运行状态，线程正在执行
- **BLOCKED**：阻塞状态，线程等待锁
- **WAITING**：等待状态，线程等待其他线程通知
- **TIMED_WAITING**：超时等待状态，线程在指定时间内等待
- **TERMINATED**：终止状态，线程执行完毕

### 4.2 线程池

#### 核心参数

- **corePoolSize**：核心线程数
- **maximumPoolSize**：最大线程数
- **keepAliveTime**：非核心线程存活时间
- **unit**：时间单位
- **workQueue**：工作队列
- **threadFactory**：线程工厂
- **handler**：拒绝策略

#### 拒绝策略

- **AbortPolicy**：抛出异常
- **CallerRunsPolicy**：由调用者线程执行
- **DiscardOldestPolicy**：丢弃最老的任务
- **DiscardPolicy**：丢弃当前任务

#### 常用线程池

- **FixedThreadPool**：固定大小线程池
- **CachedThreadPool**：可缓存线程池
- **SingleThreadExecutor**：单线程线程池
- **ScheduledThreadPool**：定时任务线程池

### 4.3 ThreadLocal

- **概念**：线程本地变量，为每个线程提供独立的变量副本
- **实现原理**：基于ThreadLocalMap，每个Thread对象有一个ThreadLocalMap
- **内存泄漏**：ThreadLocalMap中的键是弱引用，值是强引用，可能导致内存泄漏
- **使用建议**：使用完毕后调用remove()方法清理

## 5. 反射与注解

### 5.1 反射

- **概念**：在运行时获取类的信息并操作类的成员
- **核心类**：Class、Method、Field、Constructor
- **用途**：动态创建对象、调用方法、访问属性
- **优缺点**：
  - 优点：灵活性高，支持动态编程
  - 缺点：性能较差，破坏封装性

### 5.2 注解

- **概念**：一种标记，用于为代码添加元数据
- **内置注解**：@Override、@Deprecated、@SuppressWarnings等
- **元注解**：@Target、@Retention、@Documented、@Inherited
- **自定义注解**：使用@interface关键字定义
- **注解处理器**：通过反射或APT处理注解

## 6. JVM 核心

### 6.1 内存结构

- **程序计数器**：记录当前线程执行的字节码地址
- **Java虚拟机栈**：存储栈帧，每个方法对应一个栈帧
- **本地方法栈**：存储本地方法的栈帧
- **堆**：存储对象实例，垃圾回收的主要区域
  - 新生代：Eden区 + 两个Survivor区（8:1:1）
  - 老年代：存储存活时间长的对象
- **方法区**：存储类信息、常量、静态变量等
  - JDK 8+ 中称为元空间，使用本地内存

### 6.2 垃圾回收

#### 垃圾判定算法

- **引用计数法**：简单但无法解决循环引用
- **可达性分析**：以GC Roots为起点，遍历引用链

#### 垃圾回收算法

- **标记-清除**：效率低，产生碎片
- **复制**：无碎片，内存利用率低
- **标记-整理**：无碎片，效率较低
- **分代收集**：新生代用复制算法，老年代用标记-整理算法

#### 垃圾收集器

- **Serial**：单线程收集器
- **ParNew**：Serial的多线程版本
- **Parallel Scavenge**：关注吞吐量
- **CMS**：关注响应时间
- **G1**：分区收集，可预测停顿时间

### 6.3 类加载

#### 加载过程

1. **加载**：通过类名获取二进制字节流，转换为运行时数据结构
2. **链接**：
   - 验证：确保字节流符合规范
   - 准备：为静态变量分配内存并设置默认值
   - 解析：将符号引用转换为直接引用
3. **初始化**：执行静态代码块和静态变量赋值

#### 类加载器

- **Bootstrap ClassLoader**：加载核心类库
- **Extension ClassLoader**：加载扩展类库
- **Application ClassLoader**：加载应用类
- **自定义类加载器**：实现特定加载逻辑

#### 双亲委派模型

- 类加载器首先委托父类加载器加载类
- 只有当父类加载器无法加载时，才尝试自己加载
- 优点：避免类重复加载，保证安全

## 7. Java 新特性

### Java 8

- **Lambda表达式**：函数式编程
  ```java
  // 示例：使用Lambda表达式创建Runnable
  Runnable runnable = () -> System.out.println("Hello Lambda");
  ```
- **Stream API**：流式处理
  ```java
  // 示例：使用Stream API过滤和映射
  List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
  List<String> result = names.stream()
      .filter(name -> name.length() > 3)
      .map(String::toUpperCase)
      .collect(Collectors.toList());
  ```
- **Optional**：处理空指针
  ```java
  // 示例：使用Optional避免空指针异常
  Optional<String> optional = Optional.ofNullable(null);
  String result = optional.orElse("Default Value");
  ```
- **默认方法**：接口可以有默认实现
  ```java
  // 示例：接口默认方法
  interface MyInterface {
      default void defaultMethod() {
          System.out.println("Default method");
      }
  }
  ```
- **方法引用**：简化Lambda表达式
  ```java
  // 示例：使用方法引用
  List<String> names = Arrays.asList("Alice", "Bob");
  names.forEach(System.out::println);
  ```
- **Date/Time API**：新的日期时间处理
  ```java
  // 示例：使用新的日期时间API
  LocalDateTime now = LocalDateTime.now();
  LocalDate tomorrow = LocalDate.now().plusDays(1);
  ```
- **CompletableFuture**：异步编程
  ```java
  // 示例：使用CompletableFuture
  CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
      .thenApply(s -> s + " World")
      .thenApply(String::toUpperCase);
  ```

### Java 9

- **模块系统**：JPMS（Java Platform Module System）
  ```java
  // module-info.java
  module com.example {
      requires java.base;
      exports com.example.package;
  }
  ```
- **集合工厂方法**：创建不可变集合
  ```java
  // 示例：使用集合工厂方法
  List<String> list = List.of("a", "b", "c");
  Set<String> set = Set.of("x", "y", "z");
  Map<String, Integer> map = Map.of("key1", 1, "key2", 2);
  ```
- **接口私有方法**：接口可以有私有方法
  ```java
  // 示例：接口私有方法
  interface MyInterface {
      default void publicMethod() {
          privateMethod();
      }
      private void privateMethod() {
          System.out.println("Private method");
      }
  }
  ```

### Java 11

- **var关键字**：局部变量类型推断
  ```java
  // 示例：使用var关键字
  var list = new ArrayList<String>();
  var map = new HashMap<String, Integer>();
  ```
- **HttpClient API**：新的HTTP客户端
  ```java
  // 示例：使用新的HttpClient
  HttpClient client = HttpClient.newHttpClient();
  HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://example.com"))
      .build();
  HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
  System.out.println(response.body());
  ```
- **String新方法**：isBlank()、lines()、strip()等
  ```java
  // 示例：String新方法
  String str = "  Hello\nWorld  ";
  System.out.println(str.isBlank()); // false
  System.out.println(str.strip()); // "Hello\nWorld"
  str.lines().forEach(System.out::println);
  ```
- **Files新方法**：readString()、writeString()
  ```java
  // 示例：Files新方法
  Path path = Paths.get("example.txt");
  Files.writeString(path, "Hello World");
  String content = Files.readString(path);
  ```

### Java 17

- **记录类**：简化数据类
  ```java
  // 示例：记录类
  record Person(String name, int age) {
      // 可以添加自定义方法
      public Person {
          if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
      }
  }
  
  // 使用
  Person person = new Person("Alice", 30);
  System.out.println(person.name()); // Alice
  System.out.println(person.age()); // 30
  ```
- **密封类**：限制继承
  ```java
  // 示例：密封类
  sealed class Shape permits Circle, Rectangle {
  }
  
  final class Circle extends Shape {
      private final double radius;
      public Circle(double radius) { this.radius = radius; }
  }
  
  final class Rectangle extends Shape {
      private final double width, height;
      public Rectangle(double width, double height) {
          this.width = width;
          this.height = height;
      }
  }
  ```
- **文本块**：多行字符串
  ```java
  // 示例：文本块
  String html = """
      <html>
          <body>
              <p>Hello World</p>
          </body>
      </html>
      """;
  ```
- **switch表达式**：增强的switch语句
  ```java
  // 示例：switch表达式
  int day = 1;
  String dayName = switch (day) {
      case 1 -> "Monday";
      case 2 -> "Tuesday";
      case 3 -> "Wednesday";
      default -> "Other day";
  };
  ```
- **模式匹配**：instanceof增强
  ```java
  // 示例：模式匹配
  Object obj = "Hello";
  if (obj instanceof String s) {
      // 直接使用s，无需强制类型转换
      System.out.println(s.length());
  }
  ```

### Java 21

- **虚拟线程**：轻量级线程
  ```java
  // 示例：虚拟线程
  try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      executor.submit(() -> System.out.println("Hello Virtual Thread"));
  }
  
  // 或者使用Thread.startVirtualThread()
  Thread.startVirtualThread(() -> System.out.println("Hello Virtual Thread"));
  ```
- **字符串模板**：预览功能
  ```java
  // 示例：字符串模板
  String name = "Alice";
  int age = 30;
  String message = STR."Hello name}, you are age} years old";
  System.out.println(message); // Hello Alice, you are 30 years old
  ```
- **模式匹配for switch**：增强的switch模式匹配
  ```java
  // 示例：模式匹配for switch
  Object obj = new ArrayList<String>();
  String result = switch (obj) {
      case Integer i -> "Integer: " + i;
      case String s -> "String: " + s;
      case List<?> list -> "List with " + list.size() + " elements";
      default -> "Other type";
  };
  ```
- **顺序集合**：SequencedCollection接口
  ```java
  // 示例：顺序集合
  SequencedCollection<String> collection = new ArrayList<>();
  collection.add("a");
  collection.add("b");
  collection.add("c");
  
  String first = collection.getFirst(); // a
  String last = collection.getLast(); // c
  
  SequencedCollection<String> reversed = collection.reversed(); // [c, b, a]
  ```

### Java 后续版本（预览）

- **值对象**：不可变对象的新类型
- **作用域值**：替代ThreadLocal的新机制
- **向量API**：高性能数值计算
- **结构化并发**：简化并发编程
- **模式匹配增强**：更强大的模式匹配功能

### 版本选择建议

- **生产环境**：优先选择LTS版本（如JDK 11、JDK 17、JDK 21）
- **开发环境**：可以使用最新版本体验新特性
- **升级策略**：
  1. 评估应用兼容性
  2. 逐步升级，先在测试环境验证
  3. 关注废弃API和行为变更
  4. 利用新特性优化代码

## 8. 设计模式

### 创建型模式

- **单例模式**：确保只有一个实例
- **工厂方法模式**：通过工厂创建对象
- **抽象工厂模式**：创建一系列相关对象
- **建造者模式**：分步构建复杂对象
- **原型模式**：通过复制创建对象

### 结构型模式

- **适配器模式**：转换接口
- **装饰器模式**：动态添加功能
- **代理模式**：控制对象访问
- **组合模式**：树形结构
- **外观模式**：提供统一接口
- **桥接模式**：分离抽象和实现
- **享元模式**：共享对象

### 行为型模式

- **策略模式**：定义算法族
- **模板方法模式**：定义算法骨架
- **观察者模式**：发布-订阅
- **迭代器模式**：遍历集合
- **责任链模式**：请求处理链
- **命令模式**：封装命令
- **备忘录模式**：保存状态
- **状态模式**：状态驱动行为
- **访问者模式**：分离算法和数据结构
- **中介者模式**：减少对象间耦合
- **解释器模式**：语言解释器

## 9. 面试题解析

### 1. 什么是Java的平台无关性？

**答案**：
Java的平台无关性是指Java程序可以在不同的操作系统和硬件平台上运行，而不需要修改代码。实现原理：
- **Java虚拟机**：Java程序运行在JVM上，JVM负责与底层系统交互
- **字节码**：Java源代码编译为字节码，字节码是平台无关的
- **标准库**：Java提供了跨平台的标准库

### 2. ArrayList和LinkedList的区别？

**答案**：
- **底层实现**：ArrayList基于动态数组，LinkedList基于双向链表
- **随机访问**：ArrayList快，LinkedList慢
- **插入删除**：ArrayList慢（需要移动元素），LinkedList快（只需修改指针）
- **内存占用**：ArrayList小（只需存储元素），LinkedList大（需要存储元素和指针）

### 3. HashMap的实现原理？

**答案**：
- **底层结构**：JDK 7使用数组+链表，JDK 8+使用数组+链表+红黑树
- **哈希计算**：对key的hashCode进行扰动处理，减少哈希冲突
- **冲突解决**：使用链表存储哈希冲突的元素
- **树化**：当链表长度超过8且数组容量大于64时，链表转换为红黑树
- **扩容**：当元素数量超过容量*负载因子时，扩容为原来的2倍

### 4. 线程池的工作原理？

**答案**：
- 线程池维护一定数量的核心线程
- 当提交任务时，首先尝试使用核心线程执行
- 如果核心线程都在忙，将任务加入工作队列
- 如果队列已满，创建非核心线程执行任务
- 如果线程数达到最大值，执行拒绝策略
- 当非核心线程空闲时间超过keepAliveTime时，销毁

### 5. ThreadLocal的实现原理和内存泄漏问题？

**答案**：
- **实现原理**：每个Thread对象有一个ThreadLocalMap，ThreadLocalMap的键是ThreadLocal的弱引用，值是线程本地变量
- **内存泄漏**：当ThreadLocal被回收后，ThreadLocalMap中的键变为null，但值仍然存在，导致内存泄漏
- **解决方案**：使用完毕后调用remove()方法清理

### 6. 反射的用途和实现原理？

**答案**：
- **用途**：动态创建对象、调用方法、访问属性，实现框架功能
- **实现原理**：通过Class类获取类的信息，使用Method、Field等类操作类的成员
- **优缺点**：
  - 优点：灵活性高
  - 缺点：性能较差，破坏封装性

### 7. JVM内存结构？

**答案**：
- **程序计数器**：记录当前线程执行的字节码地址
- **Java虚拟机栈**：存储栈帧，每个方法对应一个栈帧
- **本地方法栈**：存储本地方法的栈帧
- **堆**：存储对象实例，分为新生代和老年代
- **方法区**：存储类信息、常量、静态变量等，JDK 8+中称为元空间

### 8. 垃圾回收的过程？

**答案**：
1. **标记**：通过可达性分析标记存活对象
2. **清除**：清除未标记的对象
3. **整理**：将存活对象移至内存一端，减少碎片
4. **复制**：将新生代中的存活对象复制到Survivor区

### 9. 类加载的过程？

**答案**：
1. **加载**：通过类名获取二进制字节流，转换为运行时数据结构
2. **链接**：
   - 验证：确保字节流符合规范
   - 准备：为静态变量分配内存并设置默认值
   - 解析：将符号引用转换为直接引用
3. **初始化**：执行静态代码块和静态变量赋值

### 10. Java 8的新特性？

**答案**：
- **Lambda表达式**：简化匿名内部类
- **Stream API**：流式处理集合
- **Optional**：处理空指针
- **默认方法**：接口可以有默认实现
- **方法引用**：简化Lambda表达式
- **Date/Time API**：新的日期时间处理
- **CompletableFuture**：异步编程

## 10. 参考链接

### 基础概念
- [Java 数据类型占用空间](https://haicoder.net/java/java-basic-datatype.html)
- [Java 平台无关性](https://segmentfault.com/a/1190000023264152)

### 集合框架
- [HashMap 解析](https://blog.csdn.net/v123411739/article/details/78996181)
- [Java 集合框架](https://pdai.tech/md/interview/x-interview.html#_2-java-%E9%9B%86%E5%90%88)

### 多线程
- [线程状态](https://www.cnblogs.com/happy-coder/p/6587092.html)
- [线程池](https://segmentfault.com/a/1190000015368896)
- [ThreadLocal 内存泄漏](https://www.bilibili.com/video/BV1ZY4y1L7WJ/)

### JVM
- [JVM 内存结构](https://www.cnblogs.com/dolphin0520/p/3613043.html)
- [垃圾回收](https://www.cnblogs.com/wanhua-wu/p/6582994.html)
- [类加载](https://blog.csdn.net/u011109589/article/details/80320562)

### 反射与注解
- [Java 反射](https://github.com/whx123/JavaHome/blob/master/Java%E5%9F%BA%E7%A1%80%E5%AD%A6%E4%B9%A0/%E8%B0%88%E8%B0%88Java%E5%8F%8D%E5%B0%84%EF%BC%9A%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E5%AE%9E%E8%B7%B5%EF%BC%8C%E5%86%8D%E5%88%B0%E5%8E%9F%E7%90%86.md)

### Java 新特性
- [JDK 特性](https://juejin.cn/post/7250734439709048869)

### 设计模式
- [设计模式原则](https://www.cnblogs.com/lcngu/p/5339555.html)

### 进阶知识
- [Java 并发编程](https://www.jianshu.com/p/ae67972d1156)
- [Java 性能优化](http://www.cnblogs.com/dolphin0520/p/3932906.html)