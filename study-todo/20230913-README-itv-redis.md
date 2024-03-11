# study- Redis  #
## 简介

官方文档 https://redis.io/docs/

中文官网？http://redis.cn/documentation.html

redis 知识汇总解析 https://pdai.tech/md/db/nosql-redis/db-redis-overview.html

## Key关键点

- 行业调研、优势、代替方案等
- 架构
- 快的原因？
- 引入多线程？
- 管道
- 大量插入数据？优化？
- 内存优化？使用内存和学习一些使用技巧？
- 底层数据结构
- 持久化机制 RDB AOF
- 分布式锁
- 内存使用率【组合压缩？】
- 淘汰策略
- 集群【哨兵、Cluster】
- Redis 监控 



## **概要** 



## **架构** 

![](https://static001.geekbang.org/resource/image/79/e7/79da7093ed998a99d9abe91e610b74e7.jpg)





## 写的过程

**首先我们来看一下数据库在进行写操作时到底做了哪些事，主要有下面五个过程**： 

- 客户端向服务端发送写操作（数据在客户端的内存中）。
- 数据库服务端接收到写请求的数据（数据在服务端的内存中）。
- 服务端调用write这个系统调用，将数据往磁盘上写（数据在系统内存的缓冲区中）。
- 操作系统将缓冲区中的数据转移到磁盘控制器上（数据在磁盘缓存中）。
- 磁盘控制器将数据写到磁盘的物理介质中（数据真正落到磁盘上）。



## 快的原因

https://baijiahao.baidu.com/s?id=1708807538121555902&wfr=spider&for=pc

![](https://pics3.baidu.com/feed/9d82d158ccbf6c81a5b7d1c201bbb13c32fa40a2.png@f_auto?token=dc734ccb2c67c55528d712a9d8dbabf8)



1.redis是基于内存的，内存的读写速度非常快；

2.redis是单线程的，省去了很多上下文切换线程的时间；

3.redis使用多路复用技术，可以处理并发的连接。非阻塞IO 内部实现采用epoll，采用了epoll+自己实现的简单的事件框架。epoll中的读、写、关闭、连接都转化成了事件，然后利用epoll的多路复用特性，绝不在io上浪费一点时间。

**IO多路复用**

https://zhuanlan.zhihu.com/p/615586279?utm_id=0

https://github.com/bage2014/study/blob/master/study-todo/20230706-README-itv-io.md

redis 采用网络IO多路复用技术来保证在多连接的时候， 系统的高吞吐量。

多路-指的是多个socket连接，复用-指的是复用一个线程。多路复用主要有三种技术：select，poll，epoll。epoll是最新的也是目前最好的多路复用技术。

这里“多路”指的是多个网络连接，“复用”指的是复用同一个线程。采用多路 I/O 复用技术可以让单个线程高效的处理多个连接请求（尽量减少网络IO的时间消耗），且Redis在内存中操作数据的速度非常快（内存内的操作不会成为这里的性能瓶颈），主要以上两点造就了Redis具有很高的吞吐量。



## 引入多线程

https://www.zhihu.com/question/596651123/answer/3074526739?utm_id=0

**Redis 从 6.0 版本开始引入了多线程**

**Redis 的瓶颈并不在 CPU，而在内存和网络**。

**Redis 的多线程部分只是用来处理网络数据的读写和协议解析，执行命令仍然是单线程顺序执行。**



## 分布式锁 

https://github.com/bage2014/study/blob/master/study-redis/README-distribute-lock.md



## 数据类型

### String

https://baijiahao.baidu.com/s?id=1708807538121555902&wfr=spider&for=pc

注意点

- 最大存储为512M

与String对比

- SDS 数据结构 
- 直接O(1)复杂度获取长度
- 杜绝缓冲区溢出
- 预分配容量
- 惰性释放
- 二进制安全



基本操作

set、get、获取成功、更新、截取、追加

### Hash



### Map



### List



### Set



### 字典

https://baijiahao.baidu.com/s?id=1741543694850029622&wfr=spider&for=pc

https://www.jianshu.com/p/4f38121df613



![](https://pics1.baidu.com/feed/cc11728b4710b912bbd8c82dba2c2d0993452227.png@f_auto?token=7d0d4d640e61512e06f365a9ab67330d)

dictht 是一个散列表结构，使用拉链法保存哈希冲突。

Redis 的字典 dict 中包含两个哈希表 dictht，这是为了方便进行 rehash 操作。

在扩容时，将其中一个 dictht 上的键值对 rehash 到另一个 dictht 上面，完成之后释放空间并交换两个 dictht 的角色。

rehash 操作不是一次性完成，而是采用渐进方式，这是为了避免一次性执行过多的 rehash 操作给服务器带来过大的负担。

渐进式 rehash 通过记录 dict 的 rehashidx 完成，它从 0 开始，然后每执行一次 rehash 都会递增。例如在一次 rehash 中，要把 dict[0] rehash 到 dict[1]，这一次会把 dict[0] 上 table[rehashidx] 的键值对 rehash 到 dict[1] 上，dict[0] 的 table[rehashidx] 指向 null，并令 rehashidx++。

在 rehash 期间，每次对字典执行添加、删除、查找或者更新操作时，都会执行一次渐进式 rehash。

采用渐进式 rehash 会导致字典中的数据分散在两个 dictht 上，因此对字典的查找操作也需要到对应的 dictht 去执行。



### 跳跃表

作为 Redis 中特有的数据结构-跳跃表，其在链表的基础上增加了多级索引来提升查找效率。

​	![](https://pics3.baidu.com/feed/dbb44aed2e738bd48d889ca8190e87df267ff908.png@f_auto?token=e9e8176e77a03ebca861cd9d45a50f96)





Bitmap

HyperLogLog

Geospatial

Pipeline

pub/sub



## 淘汰策略

【2023-06-25】https://baijiahao.baidu.com/s?id=1729630215002937706&wfr=spider&for=pc

淘汰算法 https://juejin.cn/post/7303366710693281829

过期删除 

- 定时删除
- 惰性删除



**LRU的全称为Least Recently Used;   LFU的全称为Least Frequently Used**

Redis 具体有 6 种淘汰策略：

|      策略       | 描述                                                 |
| :-------------: | :--------------------------------------------------- |
|  volatile-lru   | 从已设置过期时间的数据集中挑选最近最少使用的数据淘汰 |
|  volatile-ttl   | 从已设置过期时间的数据集中挑选将要过期的数据淘汰     |
| volatile-random | 从已设置过期时间的数据集中任意选择数据淘汰           |
|   allkeys-lru   | 从所有数据集中挑选最近最少使用的数据淘汰             |
| allkeys-random  | 从所有数据集中任意选择数据进行淘汰                   |
|   noeviction    | 禁止驱逐数据                                         |

**Redis 4.0 引入了 volatile-lfu 和 allkeys-lfu 淘汰策略，LFU 策略通过统计访问频率，将访问频率最少的键值对淘汰。**



### 淘汰策略

- volatile-lru：当内存不足以容纳新写入数据时，从设置了过期时间的key中使用LRU（最近最少使用）算法进行淘汰；
- allkeys-lru：当内存不足以容纳新写入数据时，从所有key中使用LRU（最近最少使用）算法进行淘汰。
- volatile-lfu：4.0版本新增，当内存不足以容纳新写入数据时，在过期的key中，使用LFU算法进行删除key。
- allkeys-lfu：4.0版本新增，当内存不足以容纳新写入数据时，从所有key中使用LFU算法进行淘汰；
- volatile-random：当内存不足以容纳新写入数据时，从设置了过期时间的key中，随机淘汰数据；。
- allkeys-random：当内存不足以容纳新写入数据时，从所有key中随机淘汰数据。
- volatile-ttl：当内存不足以容纳新写入数据时，在设置了过期时间的key中，根据过期时间进行淘汰，越早过期的优先被淘汰；
- noeviction：默认策略，当内存不足以容纳新写入数据时，新写入操作会报错。

**[volatile 或 allkeys] * [lru 或 lfu 或 random] + noeviction + volatile-ttl**

lru: Least Recently Used 最近最少使用

lfu: Least Frequently Used 最不频繁使用 



### LRU算法

 https://baijiahao.baidu.com/s?id=1729434050706042976

https://www.bilibili.com/video/BV1Va411677h/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1



### LFU 算法

 https://baijiahao.baidu.com/s?id=1729525047952230592





## 持久化策略

解析 https://juejin.cn/post/6844903655527677960

Java keeper https://javakeeper.starfish.ink/data-management/Redis/Redis-Persistence.html#rdb-redis-database

### RDB

#### 概念

Redis Database Backup file(Redis数据备份文件)，也被叫作Redis数据快照

RDB持久化是指在指定的时间间隔内将内存中的数据集快照写入磁盘。

也是默认的持久化方式，这种方式是就是将内存中数据以快照的方式写入到二进制文件中,默认的文件名为dump.rdb。

全量快照，内存数据以快照的形式保存到磁盘

#### 配置

可以通过配置设置自动做快照持久化的方式。我们可以配置redis在n秒内如果超过m个key被修改就自动做快照，下面是默认的快照保存配置

```
 save 900 1     #900秒内如果超过1个key被修改，则发起快照保存
```

#### 优劣说明

**优点**

- 适合大规模的数据恢复场景，如备份，全量复制等

- 一旦采用该方式，那么你的整个Redis数据库将只包含一个文件，这样非常方便进行备份。比如你可能打算每1天归档一些数据。
- 方便备份，我们可以很容易的将一个一个RDB文件移动到其他的存储介质上
- RDB 在恢复大数据集时的速度比 AOF 的恢复速度要快。
- RDB 可以最大化 Redis 的性能：父进程在保存 RDB 文件时唯一要做的就是 fork 出一个子进程，然后这个子进程就会处理接下来的所有保存工作，父进程无须执行任何磁盘 I/O 操作。

**不足**

- 如果你需要尽量避免在服务器故障时丢失数据，那么 RDB 不适合你。 虽然 Redis 允许你设置不同的保存点（save point）来控制保存 RDB 文件的频率， 但是， 因为RDB 文件需要保存整个数据集的状态， 所以它并不是一个轻松的操作。 因此你可能会至少 5 分钟才保存一次 RDB 文件。 在这种情况下， 一旦发生故障停机， 你就可能会丢失好几分钟的数据。
- 每次保存 RDB 的时候，Redis 都要 fork() 出一个子进程，并由子进程来进行实际的持久化工作。 在数据集比较庞大时， fork() 可能会非常耗时，造成服务器在某某毫秒内停止处理客户端； 如果数据集非常巨大，并且 CPU 时间非常紧张的话，那么这种停止时间甚至可能会长达整整一秒。 虽然 AOF 重写也需要进行 fork() ，但无论 AOF 重写的执行间隔有多长，数据的耐久性都不会有任何损失。

### AOF  

官方网址 https://redis.io/docs/management/persistence/

将写命令添加到 AOF 文件（Append Only File）的末尾。

使用 AOF 持久化需要设置同步选项，从而确保写命令什么时候会同步到磁盘文件上。这是因为对文件进行写入并不会马上将内容同步到磁盘上，而是先存储到缓冲区，然后由操作系统决定什么时候同步到磁盘。有以下同步选项：

|   选项   |         同步频率         |
| :------: | :----------------------: |
|  always  |     每个写命令都同步     |
| everysec |       每秒同步一次       |
|    no    | 让操作系统来决定何时同步 |

**概念**

append only file

采用日志的形式来记录每个写操作，追加到文件中，重启时再重新执行AOF文件中的命令来恢复数据

redis会将每一个收到的写命令都通过write函数追加到文件中(默认是 appendonly.aof)。

当redis重启时会通过重新执行文件中保存的写命令来在内存中重建整个数据库的内容。当然由于os会在内核中缓存 write做的修改，所以可能不是立即写到磁盘上。这样aof方式的持久化也还是有可能会丢失部分修改。不过我们可以通过配置文件告诉redis我们想要 通过fsync函数强制os写入到磁盘的时机。有三种方式如下（默认是：每秒fsync一次）

**配置**

```
appendonly yes              //启用aof持久化方式
```

#### 优劣说明

**优点**

- 数据安全，aof持久化可以配置appendfsync属性，有always，每进行一次命令操作就记录到aof文件中一次；everySec，就是每秒内进行一次文件的写操作；no就是不进行aof文件的写操作。
- 通过append模式写文件，即使中途服务器宕机，可以通过redis-check-aof工具解决数据一致性问题。
- AOF机制的rewrite模式，用来将过大的aof文件缩小，实现原理是将所有的set 通过一句set 命令总结，所有的SADD命令用总结为一句，这样每种命令都概括为一句来执行，就可以减少aof文件的大小了。（注意，在重写的过程中，是创建子进程来完成重写操作，主进程每个命令都会在AOF缓冲区和AOF重写缓冲区进行保存，这样旧版aof文件可以实现数据最新，当更新完后将重写缓冲区中的数据写入新的aof文件中然后就可以将新的文件替换掉旧版的文件。

- 主要解决数据持久化的实时性问题

**不足**

- 文件会比RDB形式的文件大。
- 数据集大的时候，比rdb启动效率低



## 集群

集群概念？ https://juejin.cn/post/7303398827225514003

集群解析 https://juejin.cn/post/7302029216142901283

https://www.bilibili.com/video/BV1MW4y187AH/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1



https://baijiahao.baidu.com/s?id=1739276119927050514&wfr=spider&for=pc

https://mp.weixin.qq.com/s?__biz=MzI3MjY1ODI2Ng==&mid=2247486094&idx=1&sn=f727b9fe6f53f4ebc5280dd09d6161ed&chksm=eb2e70bbdc59f9ad8157859f0c6eb5fa881c9e147d7e03ca9693633f7d2bff7215d0f813c847&scene=27

https://blog.csdn.net/Drw_Dcm/article/details/127111524

https://cloud.tencent.com/developer/article/1530481?ivk_sa=1024320u



https://baijiahao.baidu.com/s?id=1739276119927050514&wfr=spider&for=pc

http://www.xbhp.cn/news/75679.html



### 主从复制 

说明 https://zhuanlan.zhihu.com/p/614779697?utm_id=0

解析 说明 https://zhuanlan.zhihu.com/p/648035368

原文链接：https://pdai.tech/md/db/nosql-redis/db-redis-x-copy.html





#### 注意事项

> 在进行主从复制设置时，强烈建议在主服务器上开启持久化，当不能这么做时，比如考虑到延迟的问题，应该将实例配置为避免自动重启。



#### 基本作用

**主从复制的作用**主要包括：

- **数据冗余**：主从复制实现了数据的热备份，是持久化之外的一种数据冗余方式。
- **故障恢复**：当主节点出现问题时，可以由从节点提供服务，实现快速的故障恢复；实际上是一种服务的冗余。
- **负载均衡**：在主从复制的基础上，配合读写分离，可以由主节点提供写服务，由从节点提供读服务（即写Redis数据时应用连接主节点，读Redis数据时应用连接从节点），分担服务器负载；尤其是在写少读多的场景下，通过多个从节点分担读负载，可以大大提高Redis服务器的并发量。
- **高可用基石**：除了上述作用以外，主从复制还是哨兵和集群能够实施的基础，因此说主从复制是Redis高可用的基础。



#### 拓扑结构

redis主从**结构**特点：一个master可以有多个salve节点；salve节点可以有slave节点，从节点是级联结构。

![](https://mmbiz.qpic.cn/mmbiz_png/JfTPiahTHJhp6HqJk2evzNMibJUBlHLrb61jpaHzW2qVz0SOa5YkfkBlcWiaTib2qd0wW0Avsrb8fRx0zyXYhcFOBA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

#### 复制过程

- 当新建立一个从服务器时，从服务器将向主服务器发送SYNC命令，接收到SYNC命令后的主服务器会进行一次BGSAVE命令
- 在执行期间，会将所有命令写入缓冲区中，当BGSAVE命令执行完毕之后会将生成的RDB文件发送给从服务器，从服务器使用这个文件加载数据到内存中，之后主服务器会以Redis命令协议的格式将缓冲区的命令发送给从服务器
- 此后每次主服务执行命令都会同步给从服务器。即使有多个从服务器向主服务器发送SYNC命令，主服务器也只会执行一个BGSAVE命令，就可以处理接下来的同步请求
- 一个主服务器可以拥有多个从服务器，而从服务器也可拥有从服务器，从而形成一个图状结构，复制功能不会阻塞主服务器，即使有一个或多个同步请求，主服务器依然能处理命令请求。



#### 无磁盘复制模式

**无磁盘复制模式**：master创建一个新进程直接dump RDB到slave的socket，不经过主进程，不经过硬盘。适用于disk较慢，并且网络较快的时候。



### 哨兵模式 

https://pdai.tech/md/db/nosql-redis/db-redis-x-sentinel.html

哨兵实现了什么功能呢？下面是Redis官方文档的描述：

- **监控（Monitoring）**：哨兵会不断地检查主节点和从节点是否运作正常。
- **自动故障转移（Automatic failover）**：当主节点不能正常工作时，哨兵会开始自动故障转移操作，它会将失效主节点的其中一个从节点升级为新的主节点，并让其他从节点改为复制新的主节点。
- **配置提供者（Configuration provider）**：客户端在初始化时，通过连接哨兵来获得当前Redis服务的主节点地址。
- **通知（Notification）**：哨兵可以将故障转移的结果发送给客户端。

 

**故障迁移**

- 当一个Sentinel发现主服务器下线时，称为主观下线。

- 只有多个Sentinel都发现主服务下线，并相互之间通过命令进行交流判断主服务器下线时，称为客观下线。
- 只有对主服务器进行客观下线时，会选举出领头Sentinel，选举出之后，会进行新的主服务器投票选举，选举出一个从服务器升级为主服务器。
- 并向被选中的从服务器发送Slaveof no one命令，让其变为主服务器。
- 通过发布订阅的功能，将新的配置广播给其他Sentinel进行更新。
- 并向下线的主服务器发送Slaveof命令，让其复制新的主服务器。
- 当所有从服务器都已经开始复制新的主服务器时，领头Sentinel终止本次故障迁移。

当一个 Redis 实例被重新配置是，无论是被设置成主服务器、从服务器、又或者被设置成其他主服务器的从服务器 ，Sentinel 都会向被重新配置的实例发送一个 `CONFIG REWRITE` 命令， 从而确保这些配置会持久化在硬盘里。

https://baijiahao.baidu.com/s?id=1739276119927050514&wfr=spider&for=pc







### Cluster模式

![](https://pics1.baidu.com/feed/f703738da9773912e77e977f24983712377ae2e4.jpeg@f_auto?token=cb6174db31a2223c2713e9fe1e831709)



- 之前的主从复制，哨兵模式都难以再现扩容。

- 而Redis cluster集群实现了对Redis的水平扩容，即启动N个Redis节点，每个节点又可以有自己的从服务器，将数据均匀分布的存储在这N个结点上，每个节点存储数据的1/N。
- Redis cluster集群就是一个可以在多个Redis节点之间进行数据共享的设施；
- Redis cluster集群采用的是无中心化配置，即节点A无法处理，会将请求转发只节点B进行处理。



Redis集群中的键空间被分割为16384个槽位。

每个主节点负责16384中槽位的一部分，Redis使用CRC16 算法进行槽位分配。

为了保证高可用，cluster模式也引入了主从复制模式，一个主节点对应一个或多个从节点，当主节点发生宕机时，可进行故障转移，将子节点升级为主节点。

https://baijiahao.baidu.com/s?id=1739276119927050514&wfr=spider&for=pc



## 注意事项 

【2023-06-25】

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247488325&idx=1&sn=6d9bbe5bf2f2f2904755de5c786fb21b&chksm=cf21cc6cf856457a9d23b3e25ec48107a582e709f05964dfdb5ba77e9a239d8307334c485fdf&token=162724582&lang=zh_CN&scene=21#wechat_redirect





## 参考链接

java keeper https://javakeeper.starfish.ink/data-management/Redis/ReadRedis.html#redis-%E4%BC%98%E5%8A%BF

经典知识点【2023-06-10】https://blog.csdn.net/guorui_java/article/details/117194603

经典知识点【2023-06-10】https://zhuanlan.zhihu.com/p/536216476

总结 【2023-06-25】https://github.com/whx123/JavaHome/blob/master/%E7%BC%93%E5%AD%98Redis%E6%80%BB%E7%BB%93/README.MD

【2023-06-25】经典知识点 https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247494124&idx=1&sn=c185f7d999d5f006608d05707a8a7eea&chksm=cf2236c5f855bfd329c6e2ee27f23f8131ebcd312960190a10f1a819d67f07a21a08ad17f263&token=162724582&lang=zh_CN&scene=21#wechat_redirect



【2023-07-10】https://zhuanlan.zhihu.com/p/427496556



## Bilibili 

2023-05-25

https://www.bilibili.com/video/BV1R14y1p71k/?buvid=XY16A5614167C2F6C633EF8328DA0DBDD2FF2&is_story_h5=false&mid=Sj3flcq0QTXM78iJrhwidA%3D%3D&p=6&plat_id=114&share_from=ugc&share_medium=android&share_plat=android&share_session_id=ed74437e-f5f1-4445-a873-d0f1cf9abf98&share_source=COPY&share_tag=s_i&timestamp=1684977786&unique_k=NwNDREW&up_id=436556327&vd_source=72424c3da68577f00ea40a9e4f9001a1



https://www.bilibili.com/video/BV1ed4y1g7XU?spm_id_from=333.1007.tianma.2-1-4.click&vd_source=72424c3da68577f00ea40a9e4f9001a1