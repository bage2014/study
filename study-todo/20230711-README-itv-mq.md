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

activeMQ使用不多，没有大规模吞吐量场景，社区不活跃，不建议
RabbitMQ：小型公司可以考虑，支持稳定，活跃度高
RocketMQ：大公司，基础架构比较强
kafka：实时计算，日志采集等场景



https://blog.csdn.net/qq_38837032/article/details/121205821

![](https://img-blog.csdnimg.cn/7c0499fb9cd74f8e92e4500baf7fd346.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAZm9sbG93X21lIQ==,size_20,color_FFFFFF,t_70,g_se,x_16)



## **架构** 

https://blog.csdn.net/qqq3117004957/article/details/104841592

![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xMjU1NTk1NC02NDViYzIzNmNlZTQwMzViLnBuZw?x-oss-process=image/format,png)



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



## Bilibili 

