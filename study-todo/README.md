# study-TODO #

## 在线网址

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247488041&idx=1&sn=26d55c23ecd439860c4d9865bec61976&chksm=cf21cd00f8564416fe991974d24a51798d925b2e79d62935accf02aa6895c7b02adf48e9e207&token=1990771297&lang=zh_CN#rd



## 分布式事务

业界组件

问题挑战

## Linux

常用内存分析命令

Top 



## Arthas

基本使用

压测实践



## JVM

JOL  对象内存分布

基本类型占用大小

各个版本、各个垃圾回收器能效验证

不同垃圾回收器观察垃圾回收情况 



## 限流熔断

自适应

限流算法

熔断机制

分布式限流 

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



## MQ 

Rocket 安装基本使用

Kafka 安装和基本使用

## Lock 

锁四种状态

升级条件

CAS、AQS等



## Java 基础

Java 8函数式编程

## Java 并发

线程池

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

一级缓存

二级缓存

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



常用链接 

Spring Valid https://mp.weixin.qq.com/s/qvn9bM7rVhFEqZwt9mp6Hw

clickhouse  https://juejin.cn/post/7221154297706430525

常用工具 https://juejin.cn/post/7222822313006235685

!!!! 计划进行时！！！！！

## MySQL 

常用DB 维护【Docker版本】

DB 监控 负载情况查看

DB、Table

Index

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



# 技术代办

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




