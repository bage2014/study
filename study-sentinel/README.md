
## study-sentinel
## 链接

官网    https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D

使用
https://sentinelguard.io/zh-cn/docs/basic-api-resource-rule.html

如何使用

https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8

## 依赖

<dependency>
<groupId>com.alibaba.csp</groupId>
<artifactId>sentinel-core</artifactId>
<version>1.8.4</version>
</dependency>



流量控制有以下几个角度:

- 资源的调用关系，例如资源的调用链路，资源和资源之间的关系；
- 运行指标，例如 QPS、线程池、系统负载等；
- 控制的效果，例如直接限流、冷启动、排队等。

Sentinel 的设计理念是让您自由选择控制的角度，并进行灵活组合，从而达到想要的效果。



demo https://github.com/alibaba/Sentinel/wiki/%E6%96%B0%E6%89%8B%E6%8C%87%E5%8D%97#%E5%85%AC%E7%BD%91-demo



服务端控制台

https://github.com/alibaba/Sentinel/wiki/%E6%8E%A7%E5%88%B6%E5%8F%B0#2-%E5%90%AF%E5%8A%A8%E6%8E%A7%E5%88%B6%E5%8F%B0

docer 版本
https://hub.docker.com/r/bladex/sentinel-dashboard



## 基本架构图

![](https://sentinelguard.io/docs/zh-cn/img/sentinel-slot-chain-architecture.png)



## 基本组件解析

https://github.com/alibaba/Sentinel/wiki/Sentinel-%E6%A0%B8%E5%BF%83%E7%B1%BB%E8%A7%A3%E6%9E%90



## 常用组件

- `NodeSelectorSlot` 负责收集资源的路径，并将这些资源的调用路径，以树状结构存储起来，用于根据调用路径来限流降级；
- `ClusterBuilderSlot` 则用于存储资源的统计信息以及调用者信息，例如该资源的 RT, QPS, thread count 等等，这些信息将用作为多维度限流，降级的依据；
- `StatisticSlot` 则用于记录、统计不同纬度的 runtime 指标监控信息；
- `FlowSlot` 则用于根据预设的限流规则以及前面 slot 统计的状态，来进行流量控制；
- `AuthoritySlot` 则根据配置的黑白名单和调用来源信息，来做黑白名单控制；
- `DegradeSlot` 则通过统计信息以及预设的规则，来做熔断降级；
- `SystemSlot` 则通过系统的状态，例如 load1 等，来控制总的入口流量；



## 流量控制



### 基本配置

|      Field      | 说明                                                         | 默认值                        |
| :-------------: | :----------------------------------------------------------- | :---------------------------- |
|    resource     | 资源名，资源名是限流规则的作用对象                           |                               |
|      count      | 限流阈值                                                     |                               |
|      grade      | 限流阈值类型，QPS 或线程数模式                               | QPS 模式                      |
|    limitApp     | 流控针对的调用来源                                           | `default`，代表不区分调用来源 |
|    strategy     | 调用关系限流策略：直接、链路、关联                           | 根据资源本身（直接）          |
| controlBehavior | 流控效果（直接拒绝 / 排队等待 / 慢启动模式），不支持按调用关系限流 | 直接拒绝                      |



### 类型配置

流量控制的手段

- 直接拒绝
- 冷启动
- 匀速器



根据调用方限流

- 调用方限流
- 链路限流
- 关联流量控制



Sentinel 提供以下几种熔断策略

- 慢调用比例 (SLOW_REQUEST_RATIO)
- 异常比例 (ERROR_RATIO)
- 异常数 (ERROR_COUNT)



## Sentinel

熔断基本过程

参考链接：

https://zhuanlan.zhihu.com/p/399531631

https://juejin.cn/post/6875577249078935566

https://zhuanlan.zhihu.com/p/64786381



## 自定义 

![](https://user-images.githubusercontent.com/9434884/46783631-93324d00-cd5d-11e8-8ad1-a802bcc8f9c9.png)

slot 
https://github.com/alibaba/Sentinel/tree/master/sentinel-demo/sentinel-demo-slot-spi/src/main/java/com/alibaba/csp/sentinel/demo/slot
https://blog.51cto.com/u_14888386/2515150

限流基本过程？





VS 

Histrix


### interview 
https://blog.csdn.net/pastxu/article/details/124531980
https://blog.csdn.net/pastxu/article/details/124531980

