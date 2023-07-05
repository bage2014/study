# study-itv-dubbo #

Dubbo 体系相关ITV

TODO

## 关键点

- 核心组件
- 架构分层
- 使用协议以及场景选择
- 拓展点
- SPI 机制
- 调用过程
- 容错方案
- 序列化方式



## 架构核心模块分层

【2023-06-15】https://www.jianshu.com/p/eab2b272ba75

Dubbo的整体设计分 10 层：

第一层：service 层，接口层，给服务提供者和消费者来实现的（留给开发人员来实现）；

第二层：config 层，配置层，主要是对 Dubbo 进行各种配置的，Dubbo 相关配置；

第三层：proxy 层，服务代理层，透明生成客户端的 stub 和服务单的 skeleton，调用的是接

口，实现类没有，所以得生成代理，代理之间再进行网络通讯、负责均衡等；

第四层：registry 层，服务注册层，负责服务的注册与发现；

第五层：cluster 层，集群层，封装多个服务提供者的路由以及负载均衡，将多个实例组合成一 个服务；

第六层：monitor 层，监控层，对 rpc 接口的调用次数和调用时间进行监控；

第七层：protocol 层，远程调用层，封装 rpc 调用；

第八层：exchange 层，信息交换层，封装请求响应模式，同步转异步；

第九层：transport 层，网络传输层，抽象 mina 和 netty 为统一接口；

第十层：serialize 层，数据序列化层。



## 核心角色组件

【2023-06-15】https://mikechen.cc/19925.html

**注册中心**

**服务提供者**

**服务消费者**

**监控器**

**容器**

![](https://static.mikechen.cc/wp-content/uploads/2022/10/dubbo-principle-01.png)



## 协议以及场景

【2023-06-15】https://blog.51cto.com/u_15973676/6287825

【2023-06-15】https://cn.dubbo.apache.org/zh-cn/blog/2018/10/05/dubbo-%e5%8d%8f%e8%ae%ae%e8%af%a6%e8%a7%a3/

**dubbo协议**

使用基于mina1.1.7+hessian3.2.1的tbremoting交互

**rmi协议**

**http协议**

**rthift协议**



## 负载均衡

支持类型

自定义实现

https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/loadbalance/



## 调用过程

【2023-06-15】https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/service-invoking-process/

【2023-06-15】https://mikechen.cc/29721.html

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

【2023-06-15】https://mp.weixin.qq.com/s?__biz=MzAxMjY5NDU2Ng==&mid=2651861794&idx=1&sn=e4fc20a336d55a9939ff65d7120f73cc&chksm=8049766bb73eff7ddef39af55c80283a9db86680ce46dfc6d1930355977efb49284fe8a6104d&scene=27



## SPI 机制 

https://cn.dubbo.apache.org/zh-cn/overview/tasks/extensibility/filter/





## 序列化

支持种类

差异化？对比



## 备忘





## 参考链接

【2023-06-14】https://mikechen.cc/29721.html

【2023-06-14】https://www.imooc.com/article/312381/

【2023-06-14】https://www.jianshu.com/p/8eb0d74e393b

【2023-06-16】https://www.pianshen.com/article/9549279875/

https://blog.csdn.net/mfmfmfo/article/details/126818675

https://blog.csdn.net/m0_60721514/article/details/126752849



## Bilibili

