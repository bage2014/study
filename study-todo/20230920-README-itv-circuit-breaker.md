# Study-itv- circuit- breaker #





## 关键点

- 核心组件
- Sentinel
- Sentinel 原理解析 
- Sentinel 自适应原理？
- Sentinel 优缺点？
- Sentinel 注意事项？
- Sentinel 自定义异常返回
- Sentinel 限流模式
- Sentinel fallback 处理【降级回调方法】
- Sentinel 熔断原理？
- 限流原理，支持算法
- 限流算法
- Resilience4j
- Hystrix



## Sentinel

官方网址 https://sentinelguard.io/zh-cn/

【2023-06-20】https://wenku.csdn.net/answer/93da450f1ca84a2bad308c17202cfad7



### 背景介绍

2012诞生

2018开源



### 整体架构 

#### 核心组件

- `NodeSelectorSlot` 负责收集资源的路径，并将这些资源的调用路径，以树状结构存储起来，用于根据调用路径来限流降级；
- `ClusterBuilderSlot` 则用于存储资源的统计信息以及调用者信息，例如该资源的 RT, QPS, thread count 等等，这些信息将用作为多维度限流，降级的依据；
- `StatisticSlot` 则用于记录、统计不同纬度的 runtime 指标监控信息；
- `FlowSlot` 则用于根据预设的限流规则以及前面 slot 统计的状态，来进行流量控制；
- `AuthoritySlot` 则根据配置的黑白名单和调用来源信息，来做黑白名单控制；
- `DegradeSlot` 则通过统计信息以及预设的规则，来做熔断降级；
- `SystemSlot` 则通过系统的状态，例如 load1 等，来控制总的入口流量；

Sentinel 将 `ProcessorSlot` 作为 SPI 接口进行扩展（1.7.2 版本【2020年】以前 `SlotChainBuilder` 作为 SPI），使得 Slot Chain 具备了扩展的能力。您可以自行加入自定义的 slot 并编排 slot 间的顺序，从而可以给 Sentinel 添加自定义的功能。



**NodeSelectorSlot** 构建的结构可以通过  curl http://localhost:8719/tree?type=root 查看

**核心类解析** https://github.com/alibaba/Sentinel/wiki/Sentinel-%E6%A0%B8%E5%BF%83%E7%B1%BB%E8%A7%A3%E6%9E%90



#### 常用类

- Entry：表示一个请求的入口，它包含了请求的上下文信息和流量控制策略，用于限制请求的流量。

- Resource：表示一个请求的资源，它可以是一个URL、一个方法、一个服务等等，用于标识请求的类型和范围。

- Rule：表示一个流量控制规则，包含了资源、流量控制模式、阈值等信息，用于限制请求的流量。

- SlotChain：表示一个拦截器链，用于对请求进行拦截和处理，包含了各种拦截器和处理器。

- ProcessorSlot：表示一个拦截器或处理器，用于对请求进行处理和转发，可以实现流量控制、熔断降级等功能。

- SphU：表示一个入口工具类，用于创建Entry对象和进行流量控制。

- Tracer：表示一个请求追踪器，用于记录请求的上下文信息和状态。



#### 结构图

![](https://sentinelguard.io/docs/zh-cn/img/sentinel-slot-chain-architecture.png)



过程解析 https://cloud.tencent.com/developer/article/2244608





### 业务场景支持多

#### 流量控制 

流量控制有以下几个角度:

- 资源的调用关系，例如资源的调用链路，资源和资源之间的关系；
- 运行指标，例如 QPS、线程池、系统负载等；
- 控制的效果，例如直接限流、冷启动、排队等。

Sentinel 的设计理念是让您自由选择控制的角度，并进行灵活组合，从而达到想要的效果。

![](https://sentinelguard.io/docs/zh-cn/img/sentinel-flow-overview.jpg)

- 熔断降级

![](https://user-images.githubusercontent.com/9434884/62410811-cd871680-b61d-11e9-9df7-3ee41c618644.png)

- 

试试监控

开源、生态

SPI 机制拓展 





### 插槽

https://sentinelguard.io/zh-cn/docs/basic-implementation.html

- `NodeSelectorSlot` 负责收集资源的路径，并将这些资源的调用路径，以树状结构存储起来，用于根据调用路径来限流降级；
- `ClusterBuilderSlot` 则用于存储资源的统计信息以及调用者信息，例如该资源的 RT, QPS, thread count 等等，这些信息将用作为多维度限流，降级的依据；
- `StatisticSlot` 则用于记录、统计不同纬度的 runtime 指标监控信息；
- `FlowSlot` 则用于根据预设的限流规则以及前面 slot 统计的状态，来进行流量控制；
- `AuthoritySlot` 则根据配置的黑白名单和调用来源信息，来做黑白名单控制；
- `DegradeSlot` 则通过统计信息以及预设的规则，来做熔断降级；
- `SystemSlot` 则通过系统的状态，例如 load1 等，来控制总的入口流量；



### 自定义插槽

![](https://user-images.githubusercontent.com/9434884/46783631-93324d00-cd5d-11e8-8ad1-a802bcc8f9c9.png)





### 限流规则-流控模式之关联模式

在添加限流规则时，点击高级选项，可以选择三种流控模式：

- 直接：统计当前资源的请求，触发阈值时对当前资源直接限流，也是默认的模式
- 关联：统计与当前资源相关的另一个资源，触发阈值时，对当前资源限流
- 链路：统计从指定链路访问到本资源的请求，触发阈值时，对指定链路限流

https://www.cnblogs.com/linjiqin/p/15368805.html



### 限流效果 

流控效果是指请求达到流控阈值时应该采取的措施，包括三种：

- 快速失败：达到阈值后，新的请求会被立即拒绝并抛出FlowException异常。是默认的处理方式
- warm up：预热模式，对超出阈值的请求同样是拒绝并抛出异常。但这种模式阈值会动态变化，从一个较小值逐渐增加到最大阈值。
- 排队等待：让所有的请求按照先后次序排队执行，两个请求的间隔不能小于指定时长

https://blog.csdn.net/weixin_45481821/article/details/128932230



### 自适应解析

【2023-06-20】 https://blog.csdn.net/m0_64867047/article/details/121859915

【2023-06-20】 https://juejin.cn/post/6876011500515885064

【2023-06-20】 https://blog.csdn.net/gaoliang1719/article/details/110298251



### 熔断机制 

【2023-06-20】https://zhuanlan.zhihu.com/p/399531631

【2023-06-21】https://www.jianshu.com/p/500d461d2391

#### 熔断策略

**DEGRADE_GRADE_RT(响应时间)**
**DEGRADE_GRADE_EXCEPTION_RATIO(异常比例)**
**DEGRADE_GRADE_EXCEPTION_COUNT(异常数)**

Sentinel 提供以下几种熔断策略：

- 慢调用比例 (`SLOW_REQUEST_RATIO`)：选择以慢调用比例作为阈值，需要设置允许的慢调用 RT（即最大的响应时间），请求的响应时间大于该值则统计为慢调用。当单位统计时长（`statIntervalMs`）内请求数目大于设置的最小请求数目，并且慢调用的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断。
- 异常比例 (`ERROR_RATIO`)：当单位统计时长（`statIntervalMs`）内请求数目大于设置的最小请求数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。异常比率的阈值范围是 `[0.0, 1.0]`，代表 0% - 100%。
- 异常数 (`ERROR_COUNT`)：当单位统计时长内的异常数目超过阈值之后会自动进行熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。



#### 熔断状态

OPEN：表示熔断开启，拒绝所有请求

HALF_OPEN：探测恢复状态，如果接下来一个请求通过则结束熔断，否则继续熔断

CLOSED：表示熔断关闭，请求顺利通过

#### 熔断过程



### 限流

【2023-06-21】https://blog.csdn.net/qq_35958391/article/details/124509242

限流类型

线程数限流 

```
RuleConstant.FLOW_GRADE_THREAD
```

QPS 限流

```
RuleConstant.FLOW_GRADE_QPS
```



#### 拒绝策略

0. default(reject directly)
1. warm up
2. rate limiter
3. warm up + rate limiter



#### 限流算法

计数器

固定窗口

滑动窗口

令牌桶等

https://zhuanlan.zhihu.com/p/479956069



#### 分布式限流

https://sentinelguard.io/zh-cn/docs/cluster-flow-control.html

https://sentinelguard.io/zh-cn/docs/cluster-flow-control.html



https://www.codetd.com/article/14268671



集群环境下限流 



### 参考链接 

17 itv https://blog.csdn.net/pastxu/article/details/124531980

sentinal itv https://blog.csdn.net/KGyyds/article/details/121559251

https://blog.csdn.net/Skyhaohao/article/details/120634032



## Hysitrx

原理解析 https://blog.csdn.net/csdn_tiger1993/article/details/122974050

https://blog.csdn.net/wen5026/article/details/127821600

https://blog.csdn.net/csdn_tiger1993/article/details/122974050

http://www.lygchunhua.com/html/show-517048.html



## Resilience4j

概要 https://www.saoniuhuo.com/article/detail-482538.html

【2023-06-20】https://www.jianshu.com/p/5531b66b777a



## 参考链接

对比比较 https://blog.csdn.net/lizz861109/article/details/103581742



## 名词解释 

SphU



## Bilibili

https://www.bilibili.com/video/BV1eZ4y1b7qB/?spm_id_from=333.337.search-card.all.click

https://www.bilibili.com/video/BV12A411E7aX/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV12o4y127GC?p=3&vd_source=72424c3da68577f00ea40a9e4f9001a1