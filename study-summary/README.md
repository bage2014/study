# 总结

PPT 讲解 STAR 法则，背景、遇到问题、采取的方案、取得的效果


## 常用连接

https://github.com/0voice/interview_internal_reference

https://github.com/xingshaocheng/architect-awesome

https://github.com/DeppWang/Java-Books

https://github.com/CyC2018/CS-Notes

https://github.com/PansonPanson/Java-Notes

https://github.com/singgel/JAVA_LINE

https://github.com/Snailclimb/JavaGuide

https://github.com/doocs/advanced-java

https://github.com/xbox1994/Java-Interview

https://github.com/jobbole/awesome-java-cn

https://github.com/akullpp/awesome-java

https://github.com/AobingJava/JavaFamily



## [Java 基础](https://github.com/bage2014/interview/blob/master/README-Java.md) ##

面向对象理论，基础知识；

Java基础数据类型以及所占用空间大小

final、finally、finalize

char、int、boolean、double、byte、long等基础数据类型；

Integer、String等包装类型；

array、list、map、set、table、等java数据结构；

String、StringBuilder、StringBuffer等字符串操作类；

Java反射；

Java异常体系；

JDK各个版本特性；

序列化和反序列化；

静态代理和动态代理；

## 数据库 ##
理论知识，前世今生；

主流数据库特性对比；

三大范式；

数据库索引；

数据库读取，按页读取

各种索引对比

索引结构原理；

innerDB等；

索引优化；

集群；

事物；

binlog、redolog、undolog等

## 数据结构 ##
常用数据结构

array、list、set、stack、queue、tree、graph;

跳跃表、

Bitmap

## 常用算法 ##
查找；

排序；

暴力法；

贪心；

递归与分治；

回溯；

动态规划；

## [多线程](https://github.com/bage2014/interview/blob/master/README-MulThread.md) ##
线程的理论知识；

线程创建、执行等常用方法；

线程状态，线程池状态以及转化；

AtomicInteger、AtomicXXX；

CountdownLatch、CyclicBarrier；

Semaphore、ThreadLocal；

CAS 比较交换思路；

锁机制，独占锁、非独占锁；

AQS抽象队列同步器；

### 线程池

#### 构造参数

#### 提交过程

#### 状态扭转

#### 拒绝策略

#### 阻塞队列



赋值线程名字，方便排查问题

CompletableFuture；

## [JVM](https://github.com/bage2014/interview/blob/master/README-Jvm.md) ##
常用理论知识，前世今生，开源组件；

### 内存划分 ###

了解目标：基本概念、主要特点、常见异常

- 程序计数器
- Java虚拟机栈
- 本地方法栈
- 堆
- 方法区

### java对象 ###

hotspot  虚拟机为例

- 对象创建过程
- 内存组成结构
- 对象访问方式

#### 对象回收判断 ####

了解目标：基本概念、主要特点

- 引用计数器
- 可达性分析

#### 对象引用 ####

了解目标：基本概念、适用场景

- 强引用
- 软引用
- 软引用
- 虚引用

#### 垃圾回收算法 ####

了解目标：基本概念、主要特点、基本原理、优点与不足

- 标记清除算法

- 复制算法
- 标记整理算法
- 分代整理算法

#### 垃圾收集器 ####

了解目标：基本概念、起始版本(时间)、主要特点、基本原理、优点与不足

- Serial

- Parnew

- Parallel Scavenge

- Serial Old 

- Parallel Old

- CMS

##### G1

参考连接 

美团技术团队 https://tech.meituan.com/2016/09/23/g1.html

G1 收集器原理理解与分析 https://zhuanlan.zhihu.com/p/52841787

- 差异点

- 特点
- 基本过程

#### 类加载过程 ####

了解目标：基本概念、基本作用过程

- 加载
- 验证
- 准备
- 解析
- 初始化




常用jvm参数

对象分配策略

分析工具汇总与实践


hotspot  虚拟机为例

todo jvm 常见异常实战



JMM；



常用GC算法；

GC触发原因；

垃圾收集器；

类加载过程；

常用命令，参数调优；

### Java Agent ###

[Java Agent & Instrument](https://github.com/bage2014/study/tree/master/study-java-agent-instrument)

## 性能优化 ##

CPU、Memory、Disk、Network等指标；

QPS、TPS等性能指标；

Jps、Jmap、Jstack等常用命令；

GC日志、Dump等分析；

开源分析工具；

## Mybatis ##
MyBatis核心类以及原理；

一级缓存、二级缓存；

常用配置；

常用标签；

动态SQL；

插件；

一对多、多对多等

## [Spring](https://github.com/bage2014/interview/blob/master/README-Spring.md) ##
基本使用；

Spring 启动过程；

Bean声明周期；

IoC原理；

Aop原理；

事务实现原理；

事务传播机制；

事务隔离级别；

MVC请求过程；

Spring Boot 、Spring、Spring Cloud体系

个性化改造

## 设计模式 ##
常用设计模式；

实现；

开源中的实现；

动态代理(重点说明)；

## web知识 ##
session、cookie；

servlet声明周期；

listener、filter、servlet的启动顺序；

转发、重定向；

request、response；

## 消息队列

qmq https://github.com/qunarcorp/qmq

## redis ##
集群；

分布式锁；

缓存组件对比；

底层数据类型结构；

快的原理

备份策略；

淘汰策略；

手写简约版；

## Tomcat ##
框架

启动过程

优化配置

## web安全 ##
- XSS攻击
- SQL注入攻击
- 权限
- SCRF攻击
- HTTPS

## 分布式 ##
- 集群
- CAP理论、BASE理论
- Spring Cloud VS dubbo
- 分布式主键
- 分布式锁
- 分布式事务
- 一致性Hash
- 幂等性
- 日志
- 监控
- SSO

## 调用链 ##

- Zipkin
- CAT

## Spring Cloud ##
### 注册中心 ###

#### 整体架构 ####

#### 服务高可用 ####

#### 服务注册过程 ####

#### 服务续约过程 ####

#### 服务下线过程 ####

#### 服务剔除过程 ####

#### 服务保护模式 ####

#### 服务同步过程 ####

#### 客户端服务拉群过程 ####

#### 客户端优雅关闭服务 ####

常用组件：Eureka、Consul、ZooKeeper

- 网关
- 配置中心
- 负载均衡
- 熔断
- 限流
- 分布式事务

基本组件，对比，原理，入门，实践

## Dubbo ##

### Dubbo vs Spring Cloud

#### 通信方式

#### 组成部分

#### 节点角色

https://dubbo.apache.org/zh/docs/v2.7/user/preface/architecture/

- provider 服务提供者
- consumer 服务消费者
- registry 注册中心
- monitor 监控中心

#### 配置覆盖关系

- 方法级优先，接口级次之，全局配置再次之。
- 如果级别一样，则消费方优先，提供方次之。

#### 集群容错

https://dubbo.apache.org/zh/docs/v2.7/user/examples/

Failover

Failfast

failsafe

failback

forking

## Linux ##
- 常用命令
- docker



## IDEA 插件 ##

- Grep Console:控制台输出处理
- Rainbow Brackets:彩虹🌈括号
- Save Actions:优化保存操作
- SequenceDiagram:一键生成时序图
- Maven Helper:分析Maven项目的相关依赖
- EasyCode:一键帮你生成所需代码
- CheckStyle:代码格式检查
- SonarLint:帮你优化代码
- Lombok:帮你简化代码
- CodeGlance:代码微型地图
- Java Stream Debugger:Java8 Stream调试器
- Git Commit Template:使用模板创建commit信息
- 其他常用插件推荐

## Chrome 插件 ##
- JSON Viewer
- Octotree
- Enhanced Github

