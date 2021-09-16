# study-java #
学习java的笔记

## Java 基础


- Java序列化和反序列化



本文将逐一的介绍几个情境，顺序如下面的列表。

- 序列化 ID 的问题
- 静态变量序列化
- 父类的序列化与 Transient 关键字
- 对敏感字段加密
- 序列化存储规则


java 线程池

- 参数
- 提交过程
- 结束过程
- 注意事项

参考： 池子技术：https://mp.weixin.qq.com/s?__biz=MzUxMzQ0Njc1NQ==&mid=2247494137&idx=1&sn=85962cae0f772c2b41298dd322c6ce5a&chksm=f957aff5ce2026e3201a7b26c08cce9788b340ae371e432b13ef8d035f491cbc55ff3a6b9b79&scene=126&sessionid=1631634578&key=4f90c08c96d622750d1f94cfc9c828d2b4f9b8a4d242217acb0c892b1f5b508c3361dc32b972957612ff0fb223133a8ce49e1f65df8f4ce0aea14a1ee9521ba15fb08ac413c4a7ec31cddc52f10866e62f5854ceb9c8f6d4fc71e97d72416567c92af6c1b4572fb0e40c90d6e037f8e77fc6424650165ad3aa2d40bbe19947fa&ascene=1&uin=MjU5MTQ1MDcxMQ%3D%3D&devicetype=Windows+10+x64&version=6302019c&lang=zh_CN&exportkey=A%2FarF0N%2Fbt0u8YbyerDiyRY%3D&pass_ticket=Uc9EXPQYEBD8FJh0ig5oIlm3BsTVmcxdxPOZc%2BFgOdCZbEfmuz9f7krxzG470APE&wx_header=0&fontgear=2

怎么实现Java的序列化 

为什么实现了java.io.Serializable接口才能被序列化 

transient的作用是什么 

怎么自定义序列化策略 

自定义的序列化策略是如何被调用的 

ArrayList对序列化的实现有什么好处



https://developer.ibm.com/zh/articles/j-lo-serial/

https://cloud.tencent.com/developer/article/1341385




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
