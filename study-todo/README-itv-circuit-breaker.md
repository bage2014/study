# Study-itv- circuit- breaker #





## 关键点

- 核心组件
- Sentinel
- Sentinel 原理解析 
- Sentinel 自适应原理？
- 熔断原理？
- 限流原理，支持算法
- Resilience4j
- Hystrix



## Sentinel

官方网址 https://sentinelguard.io/zh-cn/



【2023-06-20】https://wenku.csdn.net/answer/93da450f1ca84a2bad308c17202cfad7



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



### 自适应解析

【2023-06-20】 https://blog.csdn.net/m0_64867047/article/details/121859915

【2023-06-20】 https://juejin.cn/post/6876011500515885064

【2023-06-20】 https://blog.csdn.net/gaoliang1719/article/details/110298251



### 熔断机制 

【2023-06-20】https://zhuanlan.zhihu.com/p/399531631

【2023-06-21】https://www.jianshu.com/p/500d461d2391

#### 熔断策略

DEGRADE_GRADE_RT(响应时间)
DEGRADE_GRADE_EXCEPTION_RATIO(异常比例)
DEGRADE_GRADE_EXCEPTION_COUNT(异常数)

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

令牌桶等



#### 分布式限流

https://sentinelguard.io/zh-cn/docs/cluster-flow-control.html





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



## Bilibili

https://www.bilibili.com/video/BV1eZ4y1b7qB/?spm_id_from=333.337.search-card.all.click

https://www.bilibili.com/video/BV12A411E7aX/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV12o4y127GC?p=3&vd_source=72424c3da68577f00ea40a9e4f9001a1