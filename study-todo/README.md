# study-TODO #



## 规划TODO

【细化到可以2H内完成】/ 项

### 学习规划



| 代办                | 排期       | 备注 |
| ------------------- | ---------- | ---- |
| ES-数据基本增删改查 | 2024-07-31 |      |
| ES-数据分词索引     |            |      |
| ES-海量数据查询     |            |      |
|                     |            |      |
| Log-知识体系        |            |      |
|                     |            |      |
|                     |            |      |

### 长期规划



| 说明 | 排期 | 备注 |
| ---- | ---- | ---- |
| 算法 |      |      |
|      |      |      |
| 项目 |      |      |



## 好用链接

https://github.com/EvanLi/Github-Ranking GitHub 趋势

https://digma.ai/25-best-intellij-idea-plugins-for-developers-in-2023/ Idea 插件 

https://cloud.tencent.com/developer/article/1165567 drools 工具

https://mp.weixin.qq.com/s/RWGal4LJ3PVPaP1cFPHvFQ redis 大KEY

DB 表设计 https://www.drawdb.app/editor






## **项目实践**

阿里云性能测试PTS的文档
https://help.aliyun.com/document_detail/29338.html#section-9q1-mug-j3t

分析说明 
https://help.aliyun.com/zh/pts/test-analysis-and-tuning-29342?spm=a2c4g.11186623.0.0.3a705159bgXWVk

阿里问题排查 
https://developer.aliyun.com/article/778128

CPU、Memory、DB、NetWork、Disk ？

Bilibili https://www.bilibili.com/video/BV1G24y1m7bD/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://zhuanlan.zhihu.com/p/491927737

https://baijiahao.baidu.com/s?id=1740860079075651792&wfr=spider&for=pc



spring boot启动速度优化

mysql 不使用 连接池 vs 使用连接池 性能对比

mysql 主从赋值

瓶颈体现？

CPU、DB【connection】、IO、NETWORK



## 学习规划


https://github.com/alibaba/DataX

常见算法 动画演示 https://www.cs.usfca.edu/~galles/visualization/Algorithms.html

Spring3+jdk17

分库分表 https://tech.meituan.com/2016/11/18/dianping-order-db-sharding.html

## 安全框架

https://juejin.cn/post/7308782851360555018

## Feign

请求封装

https://juejin.cn/post/7308643782376144950

Spring Cloud 系列

https://juejin.cn/user/1714893872170573/posts



## 瓶颈分析

瓶颈分析？？

火焰图生成 


## 项目实践

8-9

线上 JVM 配置

8-12

同镜像代码，不同GC 回收器，，观测不同的GC 过程

- 待验证？？

8-13

GC 日志、GC 分析 、

不同GC，不同配置的数据参数信息 



8-14

笔画图复习！！！



8-26 

内存泄漏的程序？

高CPU程序？

慢IO程序？

高IO网卡程序？？

GC 程序？？ Full GC + Young GC



9-1 

redis 

高负载读取情况？？模拟？？热点key或者 多个key高并发去读

大KEY 读写？？



## 分布式事务

业界组件

问题挑战



## 其他

分库分表 

https://pdai.tech/md/framework/ds-sharding/sharding-x-shard.html



### 时间轮算法

https://www.bilibili.com/video/BV1ed4y167s8/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://zhuanlan.zhihu.com/p/665545437



多层级

指针一直在转，， 秒->分->时

前一秒，计算下一分钟的任务，重新挂在到秒层

前一分，计算下一个时的任务，重新挂在到分层





https://juejin.cn/post/7083795682313633822

https://zhuanlan.zhihu.com/p/609284043?utm_id=0





自适应算法

跳表 https://zhuanlan.zhihu.com/p/637407262

https://mp.weixin.qq.com/s?__biz=MzU0ODMyNDk0Mw==&mid=2247495510&idx=1&sn=7a9f174b2a5facd92ee0efccf712eecc&chksm=fb427c76cc35f560d0ce02d6b7ff2f3e28c0349434734a428b20dfa2c3366d6266b15eacb588&scene=27

原文 https://epaperpress.com/sortsearch/download/skiplist.pdf

1. **跳表的本质是一个多层的有序链表，同时结合了二分和链表的思想；**
2. 由很多层索引组成，每一层索引是通过**随机函数**随机产生的，每一层都是一个有序的链表，默认是升序 ；
3. 最底层的链表包含所有元素；
4. 如果一个元素出现在第i层的链表中，则它在第i层之下的链表也都会出现；
5. 跳表的每个节点包含两个指针，一个指向同一链表中的下一个元素，一个指向下面一层的元素。



## 分布式算法

一致性算法 Paxos等 https://zhuanlan.zhihu.com/p/130332285

https://pdai.tech/md/algorithm/alg-domain-distribute-x-raft.html





### 数据库连接池

使用连接池 vs 不使用连接池

数据库连接池 https://juejin.cn/post/7310033153904656399

连接池 Druid 实现原理 https://juejin.cn/post/7310033153904656399



LRU算法实现  https://baijiahao.baidu.com/s?id=1729434050706042976

LFU算法实现  https://baijiahao.baidu.com/s?id=1729525047952230592

lru: Least Recently Used 最近最少使用

lfu: Least Frequently Used 最不频繁使用 



磁盘 https://zhuanlan.zhihu.com/p/556224472?utm_id=0



发展路线 https://github.com/kamranahmedse/developer-roadmap

## Linux

各个参数的含义？？

top 

ps

ds

free 

常用内存分析命令



常用命令 https://baijiahao.baidu.com/s?id=1757600324807486668&wfr=spider&for=pc



## Arthas

基本使用

压测实践



## JVM

JOL  对象内存分布

基本类型占用大小

各个版本、各个垃圾回收器能效验证

不同垃圾回收器观察垃圾回收情况 

火焰图

大对象GC

大对象内存分配 



## 限流熔断

自适应

### 限流算法

https://baijiahao.baidu.com/s?id=1774745070951278024&wfr=spider&for=pc

https://cloud.tencent.com/developer/article/1838833

- 计数器
- 滑动窗口
- 令牌桶
- 漏桶



熔断机制

分布式限流 



代码实践



## 注册中心





## Hisytrix



## Sentinel

基本原理

注意事项



## Dubbo

Dubbo 协议

优缺点以及实践

调用请求过程

熔断机制



## Redis

- 环境搭建
- 基本使用
- 分布式锁
- 持久化机制 RDB AOF
- 分布式锁
- 内存使用率【组合压缩？】
- 内存淘汰策略
- 集群【哨兵、Cluster】
- SDS内存结构验证

Redis list 很大 get（i） 操作 

基本结构 

扩容缩容机制

rdb 保存 + 高并发写入读写 ？



## MQ 

Rocket 安装基本使用

Kafka 安装和基本使用



## Lock 

锁四种状态

升级条件

CAS、AQS等



## Java 基础

Java 8函数式编程

数组内存连续性验证？

数组缓存局部性质

List\Map\Set 经典原理？

IO vs NIO

不使用缓冲区 vs 使用缓冲区

https://github.com/Snailclimb/JavaGuide/blob/main/docs/java/io/io-basis.md



范型 https://pdai.tech/md/java/basic/java-basic-x-generic.html



## Java 并发

线程池 

线程池 存活时间 存活地址确认

状态

状态扭转

常用方法

构造参数



线程

常用方法

状态扭转

常用方法









## MyBatis

行业调研、优势、代替方案

核心组件

基本架构

一级缓存 + 验证

二级缓存 + 验证

延时加载  



## Spring

Spring Boot 启动流程【如何初始化yml文件？】

spring 容器信息获取

spring 动态注册bean【或者修改bean】

spring 条件加载【加载bean】

spring 启动事件监听【事件机制完成服务启动后的信息整理】

Spring 事件机制获取HTTP请求调用详情【http请求监控】

spring  钩子Aware

spring order + 配置优先级

spring profile 环境

自动装配

Spring Condition 以及回调时机



## MySQL 

分别查询、命中主键、唯一索引、常规索引的耗时情况

常用DB 维护【Docker版本】

查询回表验证？

索引结构【逻辑结构】、主键索引+普通索引

Batch Insert 

各种索引、查询备用

全文索引、空间索引、Hash索引【应用例子？】

数据库页大小？

磁盘耗时？寻址？寻道耗时？

数据库分页 【实际情况中每个节点可能不能填充满，因此在数据库中，**B+Tree的高度一般都在2-4层**。MySQL的InnoDB存储引擎在设计时是**将根节点常驻内存的**，也就是说**查找某一键值的行记录时最多只需要1~3次磁盘I/O操作**。】

数据库大小、表大小、查询性能等

两个索引 vs 联合索引 性能对比？

不同长度的字符串索引，占用空间和效率对比？

change buffer 的优化机制

MySQL 日志

MySQL WAL 机制

MySQL 执行过程开关配置？？

MySQL 数据备份恢复实践？

搭建数据库主从备份 

MySQL 主从备份切换实践

数据库操作权限控制日志等验证

数据库日志记录验证

MYSQL 日志格式，写入时机、作用【错误日志、慢SQL日志 】

添加索引、添加字段过程？原理？注意事项 



https://github.com/jsonzou/jmockdata



技术深度？

常用技术流？技术趋势？业务发展？行业概念

ES 常用场景和高级技巧

1、计算机相关专业本科及以上学历；
2、在消息、存储、分布式或高并发系统等方向有过超过3年具体的项目经验；
3、精通Java或C++，对代码有一定的洁癖，喜欢不断优化和提高性能；
4、具有良好的沟通，团队协作、计划和创新的能力，有良好的责任心和上进心；
5、本科211以上背景或者一二线互联网公司工作背景(这条不满足很难通过简历筛选)
PS：薪资范围最高只能选40K 实际上不止，我们部门可能是拼多多最轻松的技术部门了

## 在线网址

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247488041&idx=1&sn=26d55c23ecd439860c4d9865bec61976&chksm=cf21cd00f8564416fe991974d24a51798d925b2e79d62935accf02aa6895c7b02adf48e9e207&token=1990771297&lang=zh_CN#rd



## 中间间趋势

https://db-engines.com/en/ranking



## 常用链接 

Spring Valid https://mp.weixin.qq.com/s/qvn9bM7rVhFEqZwt9mp6Hw

clickhouse  https://juejin.cn/post/7221154297706430525

常用工具 https://juejin.cn/post/7222822313006235685



# 技术代办

表文档生成
https://github.com/pingfangushi/screw


Java Lock 相关、分布式锁相关

zookeeper 

订单架构相关？？何如合理架构？

常用组件，Mybatis-plus

DDD 相关技术 

技术周边？Spring、MyBits、Spring Cloud等

6-page

docker 自动缩扩容

Jenkins 运行、部署、自动发布、上传远程

Spring 新版本特性？

Java 新版本特性？

MQ 解析？深入理解

Arthas 使用和 原理和 实践？

限流熔断？？？

熔断？原理啥的？

redis 使用和 深入理解？

Kafaka

PacificA : kafka采用的一致性协议

Paxos 一致性协议

压测+ jmeter！

线上问题定位

Dump 文件分析

Spring Cloud Alibaba 相关环境搭建

Docker 版本

搭建Spring Cloud 环境

注册中心、啥的 【docker版本】

Spring Test 



核心技能

高级JVM 分析！

线上问题排查

常用工具高级用法



注册中心：

https://nacos.io/zh-cn/index.html

http://c.biancheng.net/springcloud/nacos.html



## Idea 插件 

### SequenceDiagram

https://juejin.cn/post/7359083412388315146



### Arthas Idea



### Digma

Digma is an IntelliJ plugin for automatically identifying and fixing



### Java Object Layout (JOL) 

Java Object Layout (JOL) is a tool to analyze in memory object



### Rainbow Brackets 

Rainbow Brackets for IntelliJ based IDEs/Android Studio/HUAWEI DevEco Studio



### Grep Console



### TabNine



https://juejin.cn/post/7309921545639493641?searchId=20240612092117B98606A878644F2C1142

https://juejin.cn/post/7225989135999434789?searchId=20240612092117B98606A878644F2C1142
