# study- Mysql #
## 简介

MySQL是一个关系型数据库管理系统，由瑞典MySQL AB 公司开发，属于 Oracle 旗下产品。

MySQL 是最流行的关系型数据库管理系统之一，在 WEB 应用方面，MySQL是最好的 RDBMS (Relational Database Management System，关系数据库管理系统) 应用软件之一。

在Java企业级开发中非常常用，因为 MySQL 是开源免费的，并且方便扩展。



TODO

## Key关键点

- 行业调研、优势、代替方案等
- 索引结构 
- 数据库页
- 分页机制
- 聚簇索引
- change buffer 优化机制
- 各种Log【3大Log】
- Bin Log
- RedoLog
- UndoLog
- WAL 机制【write ahead log】
- MySQL 恢复机制，比如CRASH后如何数据恢复
- MySQL大表查询为什么不会爆内存
- 数据库主从备份
- 主从切换过程 
- 并发链接和并发查询
- 叶子节点分裂
- 相关锁
- MySQL自带表
- 聚集索引
- 隔离级别
- 三大范式
- 自适应哈希索引
- 错误日志、慢语句日志
- 监控慢SQL
- DB 监控 



## 关系 VS 非关系

参考链接 https://blog.csdn.net/weixin_51468875/article/details/114087402



## MyISAM vs InnoDB

InnoDB

插入缓冲（insert buffer)
二次写(double write)
自适应哈希索引(ahi)
预读(read ahead)



## MySQL 架构

参考链接 

https://blog.csdn.net/qq_39234967/article/details/126091672

https://blog.csdn.net/weixin_70730532/article/details/125164081

- 网络连接层：提供与Mysql服务器建立连接的支持
- 核心服务层：主要包含系统管理和控制工具、连接池、SQL接口、解析器、查询优化器和缓存六个部分
- 存储引擎：负责Mysql中数据的存储与提取，与底层系统文件进行交互
- 文件系统：负责将数据库的数据和日志存储在文件系统之上，并完成与存储引擎的交互，是文件的物理存储层
  

架构图

![转载而来](https://img-blog.csdnimg.cn/20210303232240506.png)



## SQL执行过程

参考链接 https://www.cnblogs.com/roadwide/p/17149506.html

包含数据恢复 https://www.qycn.com/xzx/article/17076.html



**执行过程参考**

https://baijiahao.baidu.com/s?id=1761898275313869224&wfr=spider&for=pc

1、词法分析：MySQL首先对SQL语句进行词法分析，将SQL语句分解成一个个token（关键字、标识符、运算符等），同时对token进行分类和解析，生成相应的数据结构。

2、语法分析：MySQL对SQL语句进行语法分析，根据SQL语法规则检查语句的正确性，并生成语法树。

3、语义分析：MySQL对SQL语句进行语义分析，对语法树进行遍历，确定语句中表和列的信息，包括表名、列名、列类型等，同时检查语句的语义正确性。

4、优化器处理：MySQL通过优化器对SQL语句进行处理和优化，包括执行计划的生成、索引的选择、连接方式的选择等。

5、执行计划生成：MySQL通过执行计划生成器生成SQL语句的执行计划，即具体的执行方式，包括数据的访问方式、索引的使用方式、连接方式、排序方式等。

6、数据库引擎处理：MySQL将执行计划发送给相应的数据库引擎进行处理，执行计划可能被翻译成一组底层操作指令，如数据扫描、索引查找、排序、分组等。

7、数据返回：MySQL将执行结果返回给客户端，可以是查询结果集或者操作结果。



## 索引结构

https://blog.csdn.net/m0_50275872/article/details/124138516

https://www.cnblogs.com/xingchong/p/16478639.html

https://zhuanlan.zhihu.com/p/536330578



![](https://pic3.zhimg.com/v2-66e70af9257c5e174baa1b103aff9f1a_r.jpg)



![](https://img-blog.csdnimg.cn/img_convert/bd036f8e24ed955b33450520697a3e12.png)



![](https://sns-img-bd.xhscdn.com/14c6982a-c275-eb55-2e9c-03d1539a7289?imageView2/2/w/1920/format/webp|imageMogr2/strip)



### 索引分类

B+树索引
Hash索引(底层是哈希表，哈希索引做区间查询的速度是很慢,要扫描链表，适合等值查询)
Full-Text全文索引： https://blog.csdn.net/qq_41993206/article/details/109548867
R-Tree索引

不同角度，不同分类



空间索引 

https://www.cnblogs.com/LBSer/p/3392491.html

https://www.cnblogs.com/LBSer/p/3403933.html

### 索引底层结构





## 事务提交过程

参考链接 http://c.biancheng.net/view/7290.html





## 数据库页

目前绝大部分数据库系统的存储介质都是磁盘，磁盘的读写都是以扇区为单位，每一个扇区的大小是512字节，硬件的特点决定了操作系统、数据库系统等程序的最小文件IO单位是512字节。为了文件读写的效率，数据库系统不直接使用512B的最小粒度，而是采用更大的页来作为文件IO的基本单位，PostgreSQL的页大小默认是8K



## 主从备份

参考链接 https://www.yzktw.com.cn/post/523759.html



## 主从切换 

http://cnblogs.com/liang24/p/14149412.html

## DB 监控

https://pythonjishu.com/nmizsyeicjldigb/

### 监控指标

监控MySQL需要关注一些重要的指标，从而控制好MySQL的性能和健康状况：

- 连接数量

连接数量是一个重要的指标，它反映的是当前MySQL的负载情况。可以使用mysqladmin命令查看当前连接情况或者使用如下SQL语句：show processlist;

- InnoDB缓存

InnoDB是MySQL的默认存储引擎，它可以使用缓存来提高查询效率。可以通过如下SQL语句查看InnoDB的缓存情况：SHOW ENGINE INNODB STATUS;

- 慢查询

慢查询是一种常见的性能瓶颈，可以使用mysqldumpslow命令，或者查询慢查询日志的方式来分析慢查询，进行调优。

- 磁盘空间

MySQL的数据文件和日志文件需要占用磁盘空间，当磁盘空间不足时，会影响MySQL的正常运行。可以使用如下语句查看磁盘空间使用情况：df -h



### 命令行工具

查看负载 

```
show global status
```

查看查询 

```
show processlist;
```

配置查看 

```
 连接数
 SHOW GLOBAL VARIABLES WHERE Variable_name='max_connections';
 SHOW STATUS WHERE Variable_name like 'Threads_connect%';
 
 缓冲区 
 SHOW STATUS WHERE Variable_name like '%buffer%';

```

调整【重启后失效】

```
SET GLOBAL max_connections=10;

```



### 自带工具

- mysqladmin

基本使用：

https://www.jianshu.com/p/5fa5e977ee6f

https://www.yingsoo.com/news/database/54573.html



用于管理mysql服务，查询状态信息，如可用于查看当前数据库的连接数和线程数等。

```bash
# 查看链接
mysqladmin -uroot -p -i 1 processlist

# 查看参数 
mysqladmin -uroot -p variables

# 等等
```

- mysqldumpslow

用于分析mysql慢查询日志，获取慢查询的信息，如查询的SQL、查询时间、锁定时间、返回记录数等。

- mysqlslap

模拟多用户请求并发访问mysql服务器，以测试和评估mysql服务器性能的工具。

### 三方工具

除了MySQL自带工具，还有一些第三方的监控工具：

- MySQL Enterprise Monitor

是MySQL官方提供的一款商业版监控工具，它能够实时监控MySQL服务器的状态，并通过告警系统及时通知管理员发现问题，辅助管理员解决问题。

- Zabbix

是一款开源的监控软件，可用于监控MySQL的各种性能指标，支持数据收集和图表展示。

- Nagios

是另一款开源的监控工具，可用于监控MySQL的运行状态、数据库空间、连接数等信息。





## DB LOG

记录的是SQL 还是 记录的是数据？？

分别的作用？？

写入的时机？？

格式？大概结构？



### Bin Log

binlog有有几种录入格式：statement，row和mixed

系统变量？当前时间等问题？？

sync_bin_log = 1 强一致性？

### undo Log

### redolog



## 聚集索引 

- MyISAM（ 非聚集）

- InnoDB（ 聚集索引）

- InnoDB要求表必须有主键（MyISAM可以没有）



## 索引

https://zhuanlan.zhihu.com/p/112857507 有很多关于索引的点



## 隔离级别

https://baijiahao.baidu.com/s?id=1742126549941015304&wfr=spider&for=pc

- 读未提交（read uncommitted）

一个事务还没提交时，它修改的数据都可以被别的事物看到。

事务可以读取未提交的数据，这也被称为脏读（Dirty Read）。

- 读已提交(read committed)

一个事务提交之后，它修改的数据才会被别的事物看到。

一个事务开始时，只能”看见”已经提交的事务所做的修改

这个级别有时候叫做不可重复读（nonrepeatble read），因为两次执行同样的查询，可能会得到不一样的结果。

- 可重复读(repeatable read)

一个事务执行过程中看到的数据，总是和这个事务开启时看到的数据是一致的。在可重复读的隔离级别下，未提交的事务对其他事务也是不可见的。

当某个事务在读取某个范围内的记录时，另一个事务又在该范围内插入了新的记录，当之前的事务再次读取该范围的记录时，会产生幻行（Phantom Row）

- 串行化(serializable)

数据的读和写都会加锁，读会加读锁，写会加写锁。当遇到读写锁冲突时，后访问的事务必须等前一个事务执行完成后，再继续执行。

以上四种隔离级别，由上往下隔离强度越来越大，但是执行效率会随之降低。在设置隔离级别时候，需要在隔离级别和执行效率两者做平衡取舍。



**概念解释**

- 脏读

  所谓的脏读，其实就是读到了别的事务回滚前的脏数据。比如事务B执行过程中修改了数据X，在未提交前，事务A读取了X，而事务B却回滚了，这样事务A就形成了脏读。也就是说，当前事务读到的数据是别的事务想要修改成为的但是没有修改成功的数据。

- 不可重复读

  事务A首先读取了一条数据，然后执行逻辑的时候，事务B将这条数据改变了，然后事务A再次读取的时候，发现数据不匹配了，就是所谓的不可重复读了。也就是说，当前事务先进行了一次数据读取，然后再次读取到的数据是别的事务修改成功的数据，导致两次读取到的数据不匹配，也就照应了不可重复读的语义。

- 幻读

  事务A首先根据条件索引得到N条数据，然后事务B改变了这N条数据之外的M条或者增添了M条符合事务A搜索条件的数据，导致事务A再次搜索发现有N+M条数据了，就产生了幻读。也就是说，当前事务读第一次取到的数据比后来读取到数据条目少。不可重复读和幻读比较：两者有些相似，但是前者针对的是update或delete，后者针对的insert。

## 数据库三范式

**第一范式：属性不可分**

属性它是“不可分的”。而第一范式要求属性也不可分



**第二范式：属性完全依赖于主键 [ 消除部分子函数依赖 ]**

每一个非主属性完全函数依赖于R的某个候选键， 则称为第二范式模式。



**第三范式：属性不依赖于其它非主属性 [ 消除传递依赖 ]**

每个非主属性都不传递依赖于R的候选键，则称R为第三范式模式。



## MySQL 项目结构

MySQL 常用操作记录？

权限



## 其他

查看表大小

```
show table status;

```



## 备忘

https://www.bilibili.com/video/BV1Ca411R7GG?p=46&vd_source=72424c3da68577f00ea40a9e4f9001a1




索引相关解释说明 https://blog.csdn.net/taoqilin/article/details/121230649

聚簇索引非聚簇索引 

https://blog.csdn.net/weixin_42131383/article/details/125338068

https://blog.csdn.net/weixin_33709364/article/details/94728136

索引覆盖、索引下推

https://mp.weixin.qq.com/s?__biz=MzU3Mjk2NDc3Ng==&mid=2247483828&idx=1&sn=c21c6720a83240c2b899ed81ef57b912&chksm=fcc9ab73cbbe2265ac05ec67cca251bba661e995a00ad2acf2fabb69471bdc56987cdcf5b5e9&scene=27

https://zhuanlan.zhihu.com/p/470255206

https://baijiahao.baidu.com/s?id=1716515482593299829&wfr=spider&for=pc

SQL执行过程 

https://blog.csdn.net/qq_41116027/article/details/124135359



## B站视频

进度：

2023-05-17

https://www.bilibili.com/video/BV1G54y1P7e7?p=70&spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV1G54y1P7e7?p=64&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-05-15

https://www.bilibili.com/video/BV1G54y1P7e7?p=12&spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV1G54y1P7e7?p=8&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-05-10

https://www.bilibili.com/video/BV1G54y1P7e7/?p=6&spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-05-09 

https://www.bilibili.com/video/BV1G54y1P7e7/?p=5&spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV1G54y1P7e7/?p=4&spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1





## 进度 

**ALL/ALL 2023-05-30**https://mp.weixin.qq.com/s?__biz=MzU5OTc3NDM2Mw==&mid=2247483928&idx=1&sn=a7dd0674a661402975f7d13a2c3f2095&chksm=feae87ecc9d90efa3a8b642314c9b40ce136201fdfe55534d70e0fc984f3eff861774fa9cb17&scene=27

**0/ALL 2023-05-29** http://imyhq.com/blockchain/2692.html

**ALL/ALL 2023-05-29** https://blog.csdn.net/m0_68850571/article/details/125854787

ALL/ALL 2023-05-29**https://www.cnblogs.com/qingmuchuanqi48/p/11006760.html

**ALL/ALL 2023-05-08**  https://zhuanlan.zhihu.com/p/424217408

**ALL/ALL 2023-05-08** https://blog.csdn.net/m0_59281987/article/details/127768618

**动力2021.1/ALL 2023-05-06** https://blog.csdn.net/m0_59281987/article/details/127768618

**1.四/ALL 2023-05-04** https://blog.csdn.net/m0_59281987/article/details/127768618

**ALL/ALL 2023-05-04** https://blog.csdn.net/qq_33036061/article/details/105209254

**6/ALL 2023-05-01** https://blog.csdn.net/qq_33036061/article/details/105209254

**索引底层结构/ALL** https://github.com/bage2014/study/blob/master/study-todo/README-itv-mysql.md

**done/ALL 2023-04-25** https://www.ewbang.com/community/article/details/961524446.html

**事务/ALL 2023-04-23** https://zhuanlan.zhihu.com/p/112857507

**1/1 2023-04-23** https://blog.csdn.net/m0_63592370/article/details/126039076

**60/60  2023-04-17** https://blog.csdn.net/hahazz233/article/details/125372412

**36/60  2023-04-13** https://blog.csdn.net/hahazz233/article/details/125372412

**16/60  2023-04-10** https://blog.csdn.net/hahazz233/article/details/125372412