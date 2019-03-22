# study-java #
学习java的笔记

## JVM ##
### 参考链接  ###
- JVM参数配置 [https://www.cnblogs.com/smyhvae/p/4736162.html](https://www.cnblogs.com/smyhvae/p/4736162.html "JVM参数配置") 
- 参数说明 [https://www.cnblogs.com/marcotan/p/4256885.html](https://www.cnblogs.com/marcotan/p/4256885.html "参数说明")
### 参数配置(VM Options) ###
- `-XX:+PrintGCDetails` 打印日志参数
- `-Xmx –Xms`：指定最大堆和最小堆

new ArrayList().subList(0,1) 返回的不是ArrayList对象，而是SubList 

subList 不是返回ArrayList，而是SubList

## TODO ##
- 情景设计题目(比如秒杀)
- 微服务技术组件预研(有技术对比)
- hashmap 常用方式过程(大致的过程)
- 多线程常用方法过程(常用方法、过程)
- redis（分布式锁）
- 分布式事务
- HashMap死循环
- threadlocal 内存溢出
- 存储引擎的 InnoDB与MyISAM区别，优缺点，使用场景
- 索引类别（B+树索引、全文索引、哈希索引）、索引的原理
- 什么是自适应哈希索引（AHI）
- 为什么要用 B+tree作为MySQL索引的数据结构
- 聚集索引与非聚集索引的区别
- Redis 集群方案与实现
- 消息的重发补偿解决思路
- 消息的幂等性解决思路
- 消息的堆积解决思路
- 自己如何实现消息队列
- 如何保证消息的有序性
- redis集群
