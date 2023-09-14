# study-itv-dubbo #

Dubbo 体系相关ITV

## 关键点

- 核心组件
- 架构分层
- 使用协议以及场景选择
- 负载均衡
- 拓展点
- SPI 机制
- 调用过程
- 容错方案
- 序列化方式

## 架构角色

交互过程 https://cn.dubbo.apache.org/zh-cn/docsv2.7/user/preface/architecture/

![](https://cn.dubbo.apache.org/imgs/user/dubbo-architecture.jpg)

节点说明

| 节点        | 角色说明                               |
| ----------- | -------------------------------------- |
| `Provider`  | 暴露服务的服务提供方                   |
| `Consumer`  | 调用远程服务的服务消费方               |
| `Registry`  | 服务注册与发现的注册中心               |
| `Monitor`   | 统计服务的调用次数和调用时间的监控中心 |
| `Container` | 服务运行容器                           |



## 架构分层

官网 https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/design/

![](https://cn.dubbo.apache.org/imgs/dev/dubbo-framework.jpg)

Dubbo的整体设计分 10 层：

- **service 层**，接口层，给服务提供者和消费者来实现的（留给开发人员来实现）；

- **config 配置层**：对外配置接口，以 `ServiceConfig`, `ReferenceConfig` 为中心，可以直接初始化配置类，也可以通过 spring 解析配置生成配置类
- **proxy 服务代理层**：服务接口透明代理，生成服务的客户端 Stub 和服务器端 Skeleton, 以 `ServiceProxy` 为中心，扩展接口为 `ProxyFactory`
- **registry 注册中心层**：封装服务地址的注册与发现，以服务 URL 为中心，扩展接口为 `RegistryFactory`, `Registry`, `RegistryService`
- **cluster 路由层**：封装多个提供者的路由及负载均衡，并桥接注册中心，以 `Invoker` 为中心，扩展接口为 `Cluster`, `Directory`, `Router`, `LoadBalance`
- **monitor 监控层**：RPC 调用次数和调用时间监控，以 `Statistics` 为中心，扩展接口为 `MonitorFactory`, `Monitor`, `MonitorService`
- **protocol 远程调用层**：封装 RPC 调用，以 `Invocation`, `Result` 为中心，扩展接口为 `Protocol`, `Invoker`, `Exporter`
- **exchange 信息交换层**：封装请求响应模式，同步转异步，以 `Request`, `Response` 为中心，扩展接口为 `Exchanger`, `ExchangeChannel`, `ExchangeClient`, `ExchangeServer`
- **transport 网络传输层**：抽象 mina 和 netty 为统一接口，以 `Message` 为中心，扩展接口为 `Channel`, `Transporter`, `Client`, `Server`, `Codec`
- **serialize 数据序列化层**：可复用的一些工具，扩展接口为 `Serialization`, `ObjectInput`, `ObjectOutput`, `ThreadPool`



## 基本配置 

### 配置项

配置可选项 https://cn.dubbo.apache.org/zh-cn/docsv2.7/user/configuration/xml/

| 标签                                                         | 主要用途     | 解释                                                         |
| ------------------------------------------------------------ | ------------ | ------------------------------------------------------------ |
| [dubbo:service/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 服务配置     | 用于暴露一个服务，定义服务的元信息，一个服务可以用多个协议暴露，一个服务也可以注册到多个注册中心 |
| [dubbo:reference/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 引用配置     | 用于创建一个远程服务代理，一个引用可以指向多个注册中心       |
| [dubbo:protocol/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 协议配置     | 用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受 |
| [dubbo:application/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 应用配置     | 用于配置当前应用信息，不管该应用是提供者还是消费者           |
| [dubbo:module/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 模块配置     | 用于配置当前模块信息，可选                                   |
| [dubbo:registry/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 注册中心配置 | 用于配置连接注册中心相关信息                                 |
| [dubbo:monitor/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 监控中心配置 | 用于配置连接监控中心相关信息，可选                           |
| [dubbo:provider/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 提供方配置   | 当 ProtocolConfig 和 ServiceConfig 某属性没有配置时，采用此缺省值，可选 |
| [dubbo:consumer/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 消费方配置   | 当 ReferenceConfig 某属性没有配置时，采用此缺省值，可选      |
| [dubbo:method/](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 方法配置     | 用于 ServiceConfig 和 ReferenceConfig 指定方法级的配置信息   |
| [dubbo:argument](https://gitee.com/souyunku/DevBooks/blob/master/docs/Dubbo/Dubbo最新2021年面试题大汇总，附答案.md) | 参数配置     | 用于指定方法参数配置                                         |

### 优先级

以 timeout 为例，下图显示了配置的查找顺序，其它 retries, loadbalance, actives 等类似：

- 方法级优先，接口级次之，全局配置再次之。
- 如果级别一样，则消费方优先，提供方次之。



## 协议及场景

【2023-06-15】官网 https://cn.dubbo.apache.org/zh-cn/blog/2018/10/05/dubbo-%e5%8d%8f%e8%ae%ae%e8%af%a6%e8%a7%a3/

【2023-06-15】https://blog.51cto.com/u_15973676/6287825

### dubbo

dubbo：单一长连接和 NIO 异步通讯，适合大并发小数据量的服务调用，以及消费者远大于提供者。传输协议 TCP，异步， Hessian 序列化；

### rmi

 rmi：采用 JDK 标准的 rmi 协议实现，传输参数和返回参数对象需要实现Serializable 接口，使用 java 标准序列化机制，使用阻塞式短连接，传输数据包大小混合，消费者和提供者个数差不多，可传文件，传输协议 TCP。多个短连接， TCP 协议传输，同步传输，适用常规的远程服务调用和 rmi 互操作。在依赖低版本的 Common-Collections 包， java 序列化存在安全漏洞；

### http

http：基于 Http 表单提交的远程调用协议，使用 Spring 的 HttpInvoke 实现。多个短连接，传输协议 HTTP，传入参数大小混合，提供者个数多于消费者，需要给应用程序和浏览器 JS 调用；

### webservice

webservice：基于 WebService 的远程调用协议，集成 CXF 实现，提供和原生 WebService 的互操作。多个短连接，基于 HTTP 传输，同步传输，适用系统集成和跨语言调用；

### hessian

hessian：集成 Hessian 服务，基于 HTTP 通讯，采用 Servlet 暴露服务，Dubbo 内嵌 Jetty 作为服务器时默认实现，提供与 Hession 服务互操作。多个短连接，同步 HTTP 传输， Hessian 序列化，传入参数较大，提供者大于消费者，提供者压力较大，可传文件；

### Redis

Redis：基于 Redis 实现的 RPC 协议



## 负载均衡

### 支持类型

支持类型 https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/loadbalance/

| 算法                          | 特性                    | 备注                                                 |
| :---------------------------- | :---------------------- | :--------------------------------------------------- |
| Weighted Random LoadBalance   | 加权随机                | 默认算法，默认权重相同                               |
| RoundRobin LoadBalance        | 加权轮询                | 借鉴于 Nginx 的平滑加权轮询算法，默认权重相同，      |
| LeastActive LoadBalance       | 最少活跃优先 + 加权随机 | 背后是能者多劳的思想                                 |
| Shortest-Response LoadBalance | 最短响应优先 + 加权随机 | 更加关注响应速度                                     |
| ConsistentHash LoadBalance    | 一致性哈希              | 确定的入参，确定的提供者，适用于有状态请求           |
| P2C LoadBalance               | Power of Two Choice     | 随机选择两个节点后，继续选择“连接数”较小的那个节点。 |
| Adaptive LoadBalance          | 自适应负载均衡          | 在 P2C 算法基础上，选择二者中 load 最小的那个节点    |

### 配置使用

配置方式 https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/advanced-features-and-usage/performance/loadbalance/#%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F

loadbalance = "XXX"

**服务端服务级别**

```xml
<dubbo:service interface="..." loadbalance="roundrobin" />
```

**客户端服务级别**

```xml
<dubbo:reference interface="..." loadbalance="roundrobin" />
```

**服务端方法级别**

```xml
<dubbo:service interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:service>
```

**客户端方法级别**

```xml
<dubbo:reference interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:reference>
```

### 自定义

自定义实现 https://cn.dubbo.apache.org/zh-cn/overview/tasks/extensibility/router/

## 拓展点

官网 https://cn.dubbo.apache.org/zh-cn/overview/tasks/extensibility/

目前支持 ：

- **Filter** 
- **Protocol**
- **Register**
- **Router**



## 调用过程

【2023-06-15】https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/service-invoking-process/

【2023-06-15】https://mikechen.cc/29721.html

![](https://cn.dubbo.apache.org/imgs/dev/send-request-process.jpg)



![](https://static.mikechen.cc/wp-content/uploads/2022/10/dubbo-principle-03.png)



1.首先服务提供者会启动服务，然后将服务注册到服务注册中心。

2.服务消费者，会定时拉取服务提供者列表。

3.生成一个动态代理对象，然后通过这个代理对象，去调用远程接口。

4.生成代理对象之后，会走到Cluster层，这里会获取服务提供者列表的数据。

5.然后，Cluster会根据指定的算法做负载均衡，选出要调用的服务提供者。

6.选择好服务提供者之后，再选择指定的协议格式。

7.Exchange会根据指定的协议格式进行请求数据封装，封装成request请求。

8.请求封装好之后，就会通过网络通信框架，将请求发送出去。

9.服务提供者那边，同样会有网络通信框架，会监听指定的端口号，当接收到请求之后会将请求进行反序列化。

10.反序列化之后，再根据Exchange根据指定协议格式将请求解析出来。

11.最后再通过动态代理对象，调用服务提供者的对应接口。





## 容错机制

官网 https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/advanced-features-and-usage/service/fault-tolerent-strategy/

【2023-06-15】https://mp.weixin.qq.com/s?__biz=MzAxMjY5NDU2Ng==&mid=2651861794&idx=1&sn=e4fc20a336d55a9939ff65d7120f73cc&chksm=8049766bb73eff7ddef39af55c80283a9db86680ce46dfc6d1930355977efb49284fe8a6104d&scene=27

### 基本策略 

**Failfast Cluster**
**快速失败**，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。

**Failsafe Cluster**
**失败安全**，出现异常时，直接忽略。通常用于写入审计日志等操作。

**Failback Cluster**
**失败自动恢复**，后台记录失败请求，定时重发。通常用于消息通知操作。

**Forking Cluster**
**并行调用**多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要**浪费更多服务资源**。可通过 forks="2" 来设置最大并行数。

**Broadcast Cluster**
**广播调用**所有提供者，逐个调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息。

### 基本使用

配置如下：

```xml
<dubbo:service cluster = "failover" retries="2" />
```

或

```xml
<dubbo:reference cluster = "broadcast" retries="2" />
```

或

```xml
<dubbo:reference>
    <dubbo:method cluster = "broadcast" name="findFoo" retries="2" />
</dubbo:reference>
```

### 自定义拓展

https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/spi/description/cluster/



## SPI 机制 

### 基本概念

官方文档 https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/spi/overview/

Dubbo 中的扩展能力是从 JDK 标准的 SPI 扩展点发现机制加强而来，它改进了 JDK 标准的 SPI 以下问题：

- JDK 标准的 SPI 会一次性实例化扩展点所有实现，如果有扩展实现初始化很耗时，但如果没用上也加载，会很浪费资源。
- 如果扩展点加载失败，连扩展点的名称都拿不到了。比如：JDK 标准的 ScriptEngine，通过 getName() 获取脚本类型的名称，但如果 RubyScriptEngine 因为所依赖的 jruby.jar 不存在，导致 RubyScriptEngine 类加载失败，这个失败原因被吃掉了，和 ruby 对应不起来，当用户执行 ruby 脚本时，会报不支持 ruby，而不是真正失败的原因。

### 使用demo 

https://cn.dubbo.apache.org/zh-cn/overview/tasks/extensibility/filter/ 

https://cn.dubbo.apache.org/zh-cn/overview/tasks/extensibility/filter/



SPI vs JDK 基本用法 



## 序列化

支持种类

差异化？对比



## 备忘





## 参考链接

官网 https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/design/

【2023-06-14】https://mikechen.cc/29721.html

【2023-06-14】https://www.imooc.com/article/312381/

【2023-06-14】https://www.jianshu.com/p/8eb0d74e393b

【2023-06-16】https://www.pianshen.com/article/9549279875/

itv https://gitee.com/souyunku/DevBooks#dubbo%E6%9C%80%E6%96%B02023%E5%B9%B4%E9%9D%A2%E8%AF%95%E9%A2%98%E5%A4%A7%E6%B1%87%E6%80%BB%E9%99%84%E7%AD%94%E6%A1%88

【itv】https://github.com/whx123/JavaHome/blob/master/Java%E9%9D%A2%E8%AF%95%E9%A2%98%E9%9B%86%E7%BB%93%E5%8F%B7/dubbo/dubbo%E9%9D%A2%E8%AF%95%E9%A2%98/dubbo%E9%9D%A2%E8%AF%95%E9%A2%98.md

https://blog.csdn.net/mfmfmfo/article/details/126818675

https://blog.csdn.net/m0_60721514/article/details/126752849



## Bilibili

