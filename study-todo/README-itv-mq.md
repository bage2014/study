# study- MQ  #
## 简介





## Key关键点

- 行业调研、优势、代替方案等
- 架构
- AMQP协议
- 手撕MQ ？【自己实现协议？】
- 组件
- 如何保证可靠性
- 死信队列



## **概要** 



## **架构** 

https://blog.csdn.net/qqq3117004957/article/details/104841592

![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xMjU1NTk1NC02NDViYzIzNmNlZTQwMzViLnBuZw?x-oss-process=image/format,png)



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



## Bilibili 

