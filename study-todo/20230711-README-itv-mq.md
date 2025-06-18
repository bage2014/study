# study- MQ  #
## 简介





## Key关键点

- 行业调研、优势、代替方案等
- 架构
- 选型方案、对比
- AMQP协议
- 手撕MQ ？【自己实现协议？】
- 组件
- 如何保证可靠性
- 死信队列



## **概要** 



## 方案选型对比

https://zhuanlan.zhihu.com/p/32987256150





ActiveMQ: 使用不多，没有大规模吞吐量场景，社区不活跃，不建议
RabbitMQ：小型公司可以考虑，支持稳定，活跃度高
RocketMQ：大公司，基础架构比较强
kafka：实时计算，日志采集等场景

https://blog.csdn.net/qq_38837032/article/details/121205821

![](https://img-blog.csdnimg.cn/7c0499fb9cd74f8e92e4500baf7fd346.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAZm9sbG93X21lIQ==,size_20,color_FFFFFF,t_70,g_se,x_16)





| 比较点       | RabbitMQ                                           | Kafka                             |
| ------------ | -------------------------------------------------- | --------------------------------- |
| 起源         | 实施AMQP(高级消息队列协议)诞生于金融行业的消息队列 | 从领英开始                        |
| 性能         | 4K-10K消息/秒                                      | 1百万条消息/秒                    |
| 有效负载大小 | 无限制                                             | 默认限制1MB                       |
| 消息保留     | 基于确认                                           | 基于策略(例如7天)                 |
| 拓扑结构     | 交换类型：直接，扇出，主题，基于标题               | 基于发布-订阅                     |
| 资料类型     | 交易数据                                           | 营运数据                          |
| 再平衡功能   | 不支持                                             | Kafka支持                         |
| 设计         | 它使用智能经纪人/愚蠢的消费者模型                  | 它使用了傻瓜经纪人/智能消费者模型 |

## 架构

https://blog.csdn.net/qqq3117004957/article/details/104841592

![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xMjU1NTk1NC02NDViYzIzNmNlZTQwMzViLnBuZw?x-oss-process=image/format,png)





https://github.com/rbmonster/learning-note/blob/master/src/main/java/com/toc/MESSAGEQUEUE.md

对于一个消息队列而言，从数据流向的维度，可以拆解为三大部分：**生产者、消息队列集群、消费者**，数据是从生产者流向消息队列集群，最终再从消息队列集群流向消费者



**生产者**： 生产数据的服务，通常也称为数据的输入提供方，这里的数据通常指我们的业务数据。其次生产者通常会有多个，消息队列集群内部也会有多个分区队列，所以在生产者发送数据时，通常会存在负载均衡的一些策略，常见的有按 **key hash、轮询、随机等方式。其本质是一条数据，被消息队列封装后也被称为一条消息，该条消息只能发送到其消息队列集群内部的一个分区队列中。因此只需按照一定的策略从多个队列中选择一个队列即可**。



**消息队列集群**： 消息队列集群是消息队列这种组件实现中的核心中的核心，它的主要功能是存储消息、过滤消息、分发消息。



**消费者**： 最终消息队列存储的消息会被消费者消费使用，消费者也可以看做消息队列中数据的输出方。消费者通常有两种方式从消息队列中获取数据：**推送(push)数据、拉取(pull)数据**



## 消息积压

### 概念

消费速率  < 生产速率

### 解决思路





## 消息可靠性

### 处理思路

- 持久化
- 确认机制
- 消息幂等
- 消息重试
- 事务？



## 集群高可用



- 队列、消息持久化
- 主从模式等



## AMQP协议

可以重点看看

【2023-06-13】

http://rabbitmq.mr-ping.com/AMQP/AMQP_0-9-1_Model_Explained.html

https://blog.csdn.net/zhoupenghui168/article/details/130322210

### Publisher

生产者

### Broker

服务器，一般独立部署，英译为代理人

### Exchange

消息接收的入口，英译为交换机

### Queue

队列，保存消息

### Banding

交换机到队列的路由关系

### Virtual Host

逻辑概念，隔离Exchange+Queue

### Connection

客户端和服务端的网络

### Channel

客户端和服务端的一次通信



## 对比

https://zhuanlan.zhihu.com/p/717975237

| 维度           | ActiveMQ     | RabbitMQ                       | RocketMQ                              | Kafka                                                   | Pulsar                                                       |
| -------------- | ------------ | ------------------------------ | ------------------------------------- | ------------------------------------------------------- | ------------------------------------------------------------ |
| 单机吞吐量     | 较低(万级)   | 一般（万级）                   | 高（十万级）                          | 高（十万级）                                            | 高（十万级）                                                 |
| 开发语言       | Java         | Erlang                         | Java                                  | Java/Scala                                              | Java                                                         |
| 维护者         | Apache       | Spring                         | Apache（Alibaba）                     | Apache（Confluent）                                     | Apache（StreamNative）                                       |
| Star 数量      | 2.3K         | 12K                            | 21K                                   | 28.2K                                                   | 14.1K                                                        |
| Contributor    | 139          | 264                            | 527                                   | 1189                                                    | 661                                                          |
| 社区活跃度     | 低           | 高                             | 较高                                  | 高                                                      | 高                                                           |
| 消费模式       | P2P、Pub-Sub | direct、topic、Headers、fanout | 基于 Topic 和 MessageTag 的的 Pub-Sub | 基于 Topic 的 Pub-Sub                                   | 基于 Topic 的 Pub-Sub，支持独占（exclusive）、共享（shared）、灾备（failover）、key 共享（key_shared）4 种模式 |
| 持久化         | 支持（小）   | 支持（小）                     | 支持（大）                            | 支持（大）                                              | 支持（大）                                                   |
| 顺序消息       | 不支持       | 不支持                         | 支持                                  | 支持                                                    | 支持                                                         |
| 性能稳定性     | 好           | 好                             | 一般                                  | 较差                                                    | 一般                                                         |
| 集群支持       | 主备模式     | 复制模式                       | 主备模式                              | Leader-Slave 每台既是 master 也是 slave，集群可扩展性强 | 集群模式，broker 无状态，易迁移，支持跨数据中心              |
| 管理界面       | 一般         | 较好                           | 一般                                  | 无                                                      | 无                                                           |
| 计算和存储分离 | 不支持       | 不支持                         | 不支持                                | 不支持                                                  | 支持                                                         |
| AMQP 支持      | 支持         | 支持                           | 支持                                  | 不完全支持                                              | 不完全支持                                                   |



对比图

![](https://img2018.cnblogs.com/blog/1157088/201906/1157088-20190611223924278-1894525121.png)

![](https://img2018.cnblogs.com/blog/1157088/201906/1157088-20190611223946456-627408638.png)



## 确认机制ACK

消息投递到消费者后，不立即删除，而是等待ACK确认

## 死信队列

产生条件

- 超过TTL未被消费
- 消费后被拒绝
- 超出队列长度

【2023-06-13】https://blog.51cto.com/u_15067237/4168952



## kafaka 概念

1. 生产者`Producer`：生产者，负责创建消息，然后投递到 Kafka 集群中，投递时需要指定消息所属的 Topic，同时确定好发往哪个 Partition。
2. 消费者 `Consumer`：消费者，会根据它所订阅的 Topic 以及所属的消费组，决定从哪些 Partition 中拉取消息。
3. 话题`Topic`(具体的队列)： Kafka将消息种子(Feed)分门别类， 每一类的消息称之为话题(Topic).
4. 代理`Broker`：已发布的消息保存在一组服务器中，称之为Kafka集群。集群中的每一个服务器都是一个代理(Broker).
5. 分区 `Partition`：为了提高一个队列(topic)的吞吐量，Kafka会把topic进行分区(Partition)。Topic由一个或多个Partition(分区)组成，生产者的消息可以指定或者由系统根据算法分配到指定分区。
6. `Zookeeper`：负责集群的元数据管理等功能，比如集群中有哪些 broker 节点以及 Topic，每个 Topic 又有哪些 Partition 等。
7. 消费组`Consumer Group`：一群消费者的集合，向Topic订阅消费消息的单位是Consumers。



## Kafaka 高吞吐

https://zhuanlan.zhihu.com/p/526545922

### 高效文件存储设计【分区】

Kafka把topic中一个Parition大文件分成多个小文件segment，通过多个小文件segment，就容易定期清除或删除已经消费完的文件，减少磁盘占用。

为了进一步的查询优化，Kafka默认为分段后的数据文件建立了索引文件，就是文件系统上的.index文件。

索引文件通过稀疏存储，降低index文件元数据占用的空间大小。

![](https://pic2.zhimg.com/80/v2-75b7c2bd9d1ba31af4f9b37f00f1a685_1440w.webp)

### 顺序写入

因为硬盘每次读写都会寻址和写入，其中寻址是一个耗时的操作

所以为了提高读写硬盘的速度，Kafka使用顺序I/O，来减少了寻址时间：

收到消息后Kafka会把数据插入到文件末尾，每个消费者（Consumer）对每个Topic都有一个offset用来表示读取的进度。

因为顺序写入的特性，所以Kafka是无法删除数据的，它会将所有数据都保留下来。

### 零拷贝

kafka基于sendfile实现零拷贝，数据不需要在应用程序做业务处理

仅仅是从一个DMA设备传输到另一个DMA设备

此时数据只需要复制到内核态，用户态不需要复制数据，然后发送网卡。

![](https://pic2.zhimg.com/80/v2-9f94c4f8f383265fb3becef8e78c1321_1440w.webp)





## 参考链接  

https://zhuanlan.zhihu.com/p/717975237

https://cloud.tencent.com/developer/article/2335397

消息队列设计精要  https://tech.meituan.com/2016/07/01/mq-design.html

笔记 https://zhuanlan.zhihu.com/p/436103370

基本原理 https://www.cnblogs.com/zsbinup/p/15997845.html

参考链接 https://github.com/rbmonster/learning-note/blob/master/src/main/java/com/toc/MESSAGEQUEUE.md