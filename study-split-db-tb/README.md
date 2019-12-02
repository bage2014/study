# study-split-db-tb #
分库分表 学习笔记

## 参考链接 ##
开源 数据库分库分表 [https://www.aliyun.com/citiao/999897.html](https://www.aliyun.com/citiao/999897.html)、[https://my.oschina.net/u/2335525/blog/1855103](https://my.oschina.net/u/2335525/blog/1855103)

## 背景 ##
单表数据量过大，比如一张表的日增数据量达到10000条，则一年有 3650000条，基于数据库的查询，出现了性能瓶颈

## 概述 ##
顾名思义，将数据，分别存储到不同的数据库中
比如

    原来用户表tb_user的数据全部存储在mydb中，

现在改为：


    用户表tb_user的数据，经由一定规则，分别存储到mydb1、mydb2、mydb3中

## 优缺点 ##
- 优点
 1.	解决性能瓶颈问题，提高查询效率
 2.	降低数据库读写压力
 3.	
- 缺点
 1. join、queryByPage、group by等操作变得难实现或不能实现
 2. 增加程序实现的复杂性

## 基本原理 ##



## 开源实现 ##

### incubator-shardingsphere ### 
- 参考链接

github [https://github.com/apache/incubator-shardingsphere](https://github.com/apache/incubator-shardingsphere)

document [https://shardingsphere.apache.org/document/current/cn/overview/](https://shardingsphere.apache.org/document/current/cn/overview/)

quick start [https://shardingsphere.apache.org/document/current/cn/quick-start/sharding-jdbc-quick-start/](https://shardingsphere.apache.org/document/current/cn/quick-start/sharding-jdbc-quick-start/)

demo [https://github.com/sharding-sphere/sharding-sphere-example](https://github.com/sharding-sphere/sharding-sphere-example)


- Sharding-JDBC
	1. 轻量级Java框架
	2. 在Java的JDBC层提供的额外服务
	3. 以jar包形式提供服务，无需额外部署和依赖，为增强版的JDBC驱动，完全兼容JDBC和各种ORM框架
	
- Sharding-Proxy
    1. 透明化的数据库代理端，用于对异构语言的支持
    2. 先提供MySQL/PostgreSQL版本
    3. 完全透明，可直接当做MySQL/PostgreSQL使用
    4. 适用于任何兼容MySQL/PostgreSQL协议的的客户端。

- Sharding-Sidecar（计划中）

### tsharding ###
GitHub [https://github.com/baihui212/tsharding](https://github.com/baihui212/tsharding)
- 优点
	1. 很少的资源投入即可开发完成
	2. 支持交易订单表的Sharding需求，分库又分表
	3. 支持数据源路由
	4. 支持事务
	5. 支持结果集合并
	6. 支持读写分离
- 不足 
	1. 最新更新于4年前。。。
	2. 文档比较简单
	
### DAL ###
GitHub [https://github.com/ctripcorp/dal](https://github.com/ctripcorp/dal)
- 优点
	1. 分库分表
	2. 读写分离
	
- 不足 
	1. 待验证

## 重难点实现思路 ##




