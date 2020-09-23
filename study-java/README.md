# study-java #
学习java的笔记

## Java 基础

## volatile





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

## 场景设计 ##
- 纯后端
- 整体架构图
- 并发、CPU、卡顿、用户体验、预热、异步等
- 编程题目

## 问题解析（疑难杂症） ##
针对目前项目，要结合典型场景进行阐述，要能说的别人听得懂

- 简历中存在的项目、技术点
- 主流技术的demo版本（比如redis、MQ、常用组件）
- 最难问题，如何处理，要能说一个小时！
- 常见问题解决思路，比如：并发提高、卡顿、线上、宕机、GC日志等处理思路