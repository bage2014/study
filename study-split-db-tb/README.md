# study-split-db-tb #
分库分表 学习笔记



注意事项

在路由过程中，不能查询数据库？？

深入了解
1、基本使用、原理
2、不足、对比
3、优化点、坑



## 参考链接 ##
开源 数据库分库分表 [https://www.aliyun.com/citiao/999897.html](https://www.aliyun.com/citiao/999897.html)、[https://my.oschina.net/u/2335525/blog/1855103](https://my.oschina.net/u/2335525/blog/1855103)

shadring-jdbc 解析 https://www.shared-code.com/type/9/2



相关文档链接：

https://wenku.baidu.com/view/d8e75e1ddc88d0d233d4b14e852458fb770b38c4.html、

https://wenku.baidu.com/view/3153a8e46529647d272852c3.html、

 https://wenku.baidu.com/view/9e4b1ead3868011ca300a6c30c2259010302f348.html、

https://max.book118.com/html/2017/0622/117283023.shtm、

https://cloud.tencent.com/developer/article/1608469、

https://my.oschina.net/tinyframework/blog/531444、

http://blog.itpub.net/69940575/viewspace-2684066/





二次查询法[https://blog.csdn.net/uiuan00/article/details/102716457#3%E3%80%81%E7%BB%88%E6%9E%81%E6%AD%A6%E5%99%A8-%E4%BA%8C%E6%AC%A1%E6%9F%A5%E8%AF%A2%E6%B3%95](https://blog.csdn.net/uiuan00/article/details/102716457#3、终极武器-二次查询法)

比如三个库
[1,4,7,10,13]
[2,5,8,11,14]
[3,6,9,12,15]

查询 第 101 页，每页 10 条

正常查询 limit 100 * 10,10
改写为 limit 100 * 10 / 3,10; 3 为分库的个数

然后三个结果，

第二次查询， min_id_of_dbs,current_max_id_each

查询的结果，在继续merge ; 取出来真正的 offset ，内存中排序获取即可

## 背景 ##
单表数据量过大，比如一张表的日增数据量达到10000条，则一年有 3650000条，基于数据库的查询，出现了性能瓶颈
单机数据量瓶颈问题处理

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
 3. 需知道 数据存在于哪个数据库
 4. 跨库事务

## 基本原理 ##

### 核心步骤 ###

SQL解析 => 执行器优化 => SQL路由 => SQL改写 => SQL执行 => 结果归并

如下图

![基本过程](C:\Users\89354\Desktop\sharding_architecture_cn.png)

### SQL解析 ###

分为词法解析和语法解析。

 先通过词法解析器将SQL拆分为一个个不可再分的单词。

再使用语法解析器对SQL进行理解，并最终提炼出解析上下文。

 解析上下文包括表、选择项、排序项、分组项、聚合函数、分页信息、查询条件以及可能需要修改的占位符的标记。



解析过程分为词法解析和语法解析。

 词法解析器用于将SQL拆解为不可再分的原子符号，称为Token。并根据不同数据库方言所提供的字典，将其归类为关键字，表达式，字面量和操作符。 再使用语法解析器将SQL转换为抽象语法树。



入口

SQLParsingEngine的parse方法





### 执行器优化 ###
合并和优化分片条件，如OR等。



### SQL路由 ###
根据解析上下文匹配用户配置的分片策略，并生成路由路径。目前支持分片路由和广播路由。

代码入口

com.dangdang.ddframe.rdb.sharding.jdbc.core.statement.ShardingPreparedStatement

分片入口

ShardingStrategy



### SQL改写 ###

将SQL改写为在真实数据库中可以正确执行的语句。SQL改写分为正确性改写和优化改写。

代码入口

com.dangdang.ddframe.rdb.sharding.routing.router.ParsingSQLRouter



### SQL执行 ###
通过多线程执行器异步执行。

代码入口

com.dangdang.ddframe.rdb.sharding.jdbc.core.statement.ShardingPreparedStatement



### 结果归并 ###
将多个执行结果集归并以便于通过统一的JDBC接口输出。结果归并包括流式归并、内存归并和使用装饰者模式的追加归并这几种方式。



实现link [https://github.com/bage2014/study/tree/master/study-split-db-tb](https://github.com/bage2014/study/tree/master/study-split-db-tb)

[https://github.com/apache/shardingsphere-example](https://github.com/apache/shardingsphere-example)


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

#### 核心功能点 #### 
- 分片键
如无分片字段，将执行全路由，性能较差
支持单字段分片、多字段分片

- 分片算法
1. 精确分片算法
对应PreciseShardingAlgorithm，用于处理使用单一键作为分片键的=与IN进行分片的场景。需要配合StandardShardingStrategy使用。
2. 范围分片算法
对应RangeShardingAlgorithm，用于处理使用单一键作为分片键的BETWEEN AND、>、<、>=、<=进行分片的场景。需要配合StandardShardingStrategy使用。
3. 复合分片算法
对应ComplexKeysShardingAlgorithm，用于处理使用多键作为分片键进行分片的场景，包含多个分片键的逻辑较复杂，需要应用开发者自行处理其中的复杂度。需要配合ComplexShardingStrategy使用。
4. Hint分片算法
对应HintShardingAlgorithm，用于处理使用Hint行分片的场景。需要配合HintShardingStrategy使用。

- 分片策略
1. 标准分片策略
对应StandardShardingStrategy。提供对SQL语句中的=, >, <, >=, <=, IN和BETWEEN AND的分片操作支持。StandardShardingStrategy只支持单分片键，提供PreciseShardingAlgorithm和RangeShardingAlgorithm两个分片算法。PreciseShardingAlgorithm是必选的，用于处理=和IN的分片。RangeShardingAlgorithm是可选的，用于处理BETWEEN AND, >, <, >=, <=分片，如果不配置RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理。
2. 复合分片策略
对应ComplexShardingStrategy。复合分片策略。提供对SQL语句中的=, >, <, >=, <=, IN和BETWEEN AND的分片操作支持。ComplexShardingStrategy支持多分片键，由于多分片键之间的关系复杂，因此并未进行过多的封装，而是直接将分片键值组合以及分片操作符透传至分片算法，完全由应用开发者实现，提供最大的灵活度。
3. 行表达式分片策略
对应InlineShardingStrategy。使用Groovy的表达式，提供对SQL语句中的=和IN的分片操作支持，只支持单分片键。对于简单的分片算法，可以通过简单的配置使用，从而避免繁琐的Java代码开发，如: t_user_$->{u_id % 8} 表示t_user表根据u_id模8，而分成8张表，表名称为t_user_0到t_user_7。
4. Hint分片策略
对应HintShardingStrategy。通过Hint而非SQL解析的方式分片的策略。
5. 不分片策略
对应NoneShardingStrategy。不分片的策略。
#### 内核解析[重要] #### 

#### 不支持项[重要] #### 


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

### 路由过程 ###
- 通常根据业务主键进行路由
- 路由方式一般是根据ID的范围、hash(ID)等方式

### 分页查询 ###
不像单表查询，比如一个简单的分页SQL，此时的实现则需要更加复杂
- 禁用调页（因为跳页比较难实现）
- 采用二次查询方式实现（参考58同城沈剑的思路）

### 数据库扩容 ###
不用的路由方式，将影响到数据库的扩容负责度
- hash方式路由，分布均匀 + 停更服务 的矛盾
- 区间路由，平滑迁移 + 分布不均的矛盾



