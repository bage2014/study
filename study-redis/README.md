# study-redis #
学习Redis的笔记

## 简介 ##
### 参考链接 ###
- Redis官网 [http://www.redis.cn/](http://www.redis.cn/ "Redis 官网")
- Windows 版本 [https://github.com/MicrosoftArchive/redis](https://github.com/MicrosoftArchive/redis "Windows 版")
- Redis 总结精讲 看一篇成高手系统-4 [https://blog.csdn.net/hjm4702192/article/details/80518856](https://blog.csdn.net/hjm4702192/article/details/80518856)
- 【原创】分布式之redis复习精讲 [https://www.cnblogs.com/rjzheng/p/9096228.html](https://www.cnblogs.com/rjzheng/p/9096228.html)
- Redis进阶实践之五Redis的高级特性 [https://www.cnblogs.com/PatrickLiu/p/8341951.html](https://www.cnblogs.com/PatrickLiu/p/8341951.html)
- Redis集群方案怎么做？大牛给你介绍五种方案！ [https://www.jianshu.com/p/53a9f98976a7](https://www.jianshu.com/p/53a9f98976a7)
- redis常用集群方案 [https://www.jianshu.com/p/1ecbd1a88924](https://www.jianshu.com/p/1ecbd1a88924)
- Redis 的主从同步，及两种高可用方式 [https://blog.csdn.net/weixin_42711549/article/details/83061052](https://blog.csdn.net/weixin_42711549/article/details/83061052)

## 优势 ##
- Redis 是完全开源免费的，遵守BSD协议，是一个高性能的key-value数据库。
- Redis支持数据的持久化，可以将内存中的数据保存在磁盘中，重启的时候可以再次加载进行使用。
- Redis不仅仅支持简单的key-value类型的数据，同时还提供list，set，zset，hash等数据结构的存储。
- Redis支持数据的备份，即master-slave模式的数据备份。
- 性能极高 – Redis能读的速度是110000次/s,写的速度是81000次/s 。
- 丰富的数据类型 – Redis支持二进制案例的 Strings, Lists, Hashes, Sets 及 Ordered Sets 数据类型操作。
- 原子 – Redis的所有操作都是原子性的，意思就是要么成功执行要么失败完全不执行。单个操作是原子性的。多个操作也支持事务，即原子性，通过MULTI和EXEC指令包起来。
- 丰富的特性 – Redis还支持 publish/subscribe, 通知, key 过期等等特性。

## 不足 ##
- 缓存和数据库双写一致性问题
- 缓存雪崩问题
- 缓存击穿问题
- 缓存的并发竞争问题

## 高效原因 ##
- **纯内存**操作
- **单线程**操作，避免了频繁的上下文切换
- 采用了**非阻塞**I/O多路复用机制
- 基于事件

## redis的过期策略以及内存淘汰机制 ##
redis采用的是定期删除+惰性删除策略

在redis.conf中有一行配置

    # maxmemory-policy volatile-lru

- noeviction：当内存不足以容纳新写入数据时，新写入操作会报错。应该没人用吧。
- **allkeys-lru**：当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的key。推荐使用，目前项目在用这种。
- **allkeys-random**：当内存不足以容纳新写入数据时，在键空间中，随机移除某个key。应该也没人用吧，你不删最少使用Key,去随机删。
- **volatile-lru**：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，移除最近最少使用的key。这种情况一般是把redis既当缓存，又做持久化存储的时候才用。不推荐
- **volatile-random**：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，随机移除某个key。依然不推荐
- **volatile-ttl**：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，有更早过期时间的key优先移除。不推荐

## redis和数据库双写一致性问题 ##
首先，采取正确更新策略，先更新数据库，再删缓存。
其次，因为可能存在删除缓存失败的问题，提供一个补偿措施即可，例如利用消息队列。

## 如何应对缓存穿透和缓存雪崩问题 ##

- **缓存穿透**，即黑客故意去请求缓存中不存在的数据，导致所有的请求都怼到数据库上，从而数据库连接异常。
- **解决方案**:
  - 利用互斥锁，缓存失效的时候，先去获得锁，得到锁了，再去请求数据库。没得到锁，则休眠一段时间重试
  - 采用异步更新策略，无论key是否取到值，都直接返回。value值中维护一个缓存失效时间，缓存如果过期，异步起一个线程去读数据库，更新缓存。需要做缓存预热(项目启动前，先加载缓存)操作。
  - 提供一个能迅速判断请求是否有效的拦截机制，比如，利用布隆过滤器，内部维护一系列合法有效的key。迅速判断出，请求所携带的Key是否合法有效。如果不合法，则直接返回。
- **缓存雪崩**，即缓存同一时间大面积的失效，这个时候又来了一波请求，结果请求都怼到数据库上，从而导致数据库连接异常。
  - 给缓存的失效时间，加上一个随机值，避免集体失效。
  - 使用互斥锁，但是吞吐量会明显下降。
  - 双缓存。我们有两个缓存，缓存A和缓存B。缓存A的失效时间为20分钟，缓存B不设失效时间。自己做缓存预热操作。

## 高级特性 ##
- 语法：keys pattern，pattern可以是类似正则的匹配规则，可以是*，也可以是?，返回匹配规则的键值key的列表。
		192.168.127.128:6379>keys *
           1）"name"
           2）"set2"
           3）"set1"

           192.168.127.128:6379>keys n*
           1）"name"
- 语法：exists key [key ...]，判断一个或者多个key是否存在，并返回存在key的个数，不存在返回0。
		192.168.127.128:6379>keys *
           1）"name"
           2）"age"

           192.168.127.128:6379>exists name age address(该值存在)
           （integer）2

           192.168.127.128:6379>exists name1 age1 address(该值存在)
           （integer）0
- 语法：flushdb，清空当前数据所有的键值对。
		192.168.127.128:6379>flushdb
           OK

           192.168.127.128:6379>keys *
           (empty list or set)
- 语法：flushall，清空所有数据库的所有键值对。
           192.168.127.128:6379>flushall
           OK


           192.168.127.128:6379[1]>keys *
           (empty list or set)


           192.168.127.128:6379[5]>keys *
           (empty list or set)


## 环境搭建 ##
详见下方参考链接
### 参考链接 ###
- Redis下载和安装(Linux版) [https://redis.io/download](https://redis.io/download "下载和安装")
- Docker环境搭建 [https://hub.docker.com/_/redis/](https://hub.docker.com/_/redis/ "Docker环境搭建")

## 常用数据类型 ##
- **String**: Binary-safe strings.
- **Lists**: collections of string elements sorted according to the order of insertion. They are basically linked lists.
- **Sets**: collections of unique, unsorted string elements.
- **Sorted sets**, similar to Sets but where every string element is associated to a floating number value, called score. The elements are always taken sorted by their score, so unlike Sets it is possible to retrieve a range of elements (for example you may ask: give me the top 10, or the bottom 10).
- **Hashes**, which are maps composed of fields associated with values. Both the field and the value are strings. This is very similar to Ruby or Python hashes.
- **Bit arrays** (or **simply bitmaps**): it is possible, using special commands, to handle String values like an array of bits: you can set and clear individual bits, count all the bits set to 1, find the first set or unset bit, and so forth.
- **HyperLogLogs**: this is a probabilistic data structure which is used in order to estimate the cardinality of a set. Don't be scared, it is simpler than it seems... See later in the HyperLogLog section of this tutorial.
- **Streams**: append-only collections of map-like entries that provide an abstract log data type. They are covered in depth in the Introduction to Redis Streams.

## 常用命令 ##
- 在线命令测试 [http://try.redis.io/](http://try.redis.io/ "在线命令测试")
- 常用数据类型 [https://redis.io/topics/data-types-intro](https://redis.io/topics/data-types-intro "常用数据类型")

## Java 连接redis ##
### 参考链接 ### 
- 通过Jedis连接 [https://github.com/xetorthio/jedis](https://github.com/xetorthio/jedis "Java链接redis")

## Redis 配置项说明 ##

1. Redis默认不是以守护进程的方式运行，可以通过该配置项修改，使用yes启用守护进程
    daemonize no
2. 当Redis以守护进程方式运行时，Redis默认会把pid写入/var/run/redis.pid文件，可以通过pidfile指定
    pidfile /var/run/redis.pid
3. 指定Redis监听端口，默认端口为6379，作者在自己的一篇博文中解释了为什么选用6379作为默认端口，因为6379在手机按键上MERZ对应的号码，而MERZ取自意大利歌女Alessia Merz的名字
    port 6379
4. 绑定的主机地址
    bind 127.0.0.1
5.当 客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能
    timeout 300
6. 指定日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose
    loglevel verbose
7. 日志记录方式，默认为标准输出，如果配置Redis为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给/dev/null
    logfile stdout
8. 设置数据库的数量，默认数据库为0，可以使用SELECT <dbid>命令在连接上指定数据库id
    databases 16
9. 指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合
    save <seconds> <changes>
    Redis默认配置文件中提供了三个条件：
    save 900 1
    save 300 10
    save 60 10000
    分别表示900秒（15分钟）内有1个更改，300秒（5分钟）内有10个更改以及60秒内有10000个更改。
 
10. 指定存储至本地数据库时是否压缩数据，默认为yes，Redis采用LZF压缩，如果为了节省CPU时间，可以关闭该选项，但会导致数据库文件变的巨大
    rdbcompression yes
11. 指定本地数据库文件名，默认值为dump.rdb
    dbfilename dump.rdb
12. 指定本地数据库存放目录
    dir ./
13. 设置当本机为slav服务时，设置master服务的IP地址及端口，在Redis启动时，它会自动从master进行数据同步
    slaveof <masterip> <masterport>
14. 当master服务设置了密码保护时，slav服务连接master的密码
    masterauth <master-password>
15. 设置Redis连接密码，如果配置了连接密码，客户端在连接Redis时需要通过AUTH <password>命令提供密码，默认关闭
    requirepass foobared
16. 设置同一时间最大客户端连接数，默认无限制，Redis可以同时打开的客户端连接数为Redis进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis会关闭新的连接并向客户端返回max number of clients reached错误信息
    maxclients 128
17. 指定Redis最大内存限制，Redis在启动时会把数据加载到内存中，达到最大内存后，Redis会先尝试清除已到期或即将到期的Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis新的vm机制，会把Key存放内存，Value会存放在swap区
    maxmemory <bytes>
18. 指定是否在每次更新操作后进行日志记录，Redis在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis本身同步数据文件是按上面save条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为no
    appendonly no
19. 指定更新日志文件名，默认为appendonly.aof
     appendfilename appendonly.aof
20. 指定更新日志条件，共有3个可选值： 
    no：表示等操作系统进行数据缓存同步到磁盘（快） 
    always：表示每次更新操作后手动调用fsync()将数据写到磁盘（慢，安全） 
    everysec：表示每秒同步一次（折衷，默认值）
    appendfsync everysec
 
21. 指定是否启用虚拟内存机制，默认值为no，简单的介绍一下，VM机制将数据分页存放，由Redis将访问量较少的页即冷数据swap到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析Redis的VM机制）
     vm-enabled no
22. 虚拟内存文件路径，默认值为/tmp/redis.swap，不可多个Redis实例共享
     vm-swap-file /tmp/redis.swap
23. 将所有大于vm-max-memory的数据存入虚拟内存,无论vm-max-memory设置多小,所有索引数据都是内存存储的(Redis的索引数据 就是keys),也就是说,当vm-max-memory设置为0的时候,其实是所有value都存在于磁盘。默认值为0
     vm-max-memory 0
24. Redis swap文件分成了很多的page，一个对象可以保存在多个page上面，但一个page上不能被多个对象共享，vm-page-size是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page大小最好设置为32或者64bytes；如果存储很大大对象，则可以使用更大的page，如果不 确定，就使用默认值
     vm-page-size 32
25. 设置swap文件中的page数量，由于页表（一种表示页面空闲或使用的bitmap）是在放在内存中的，，在磁盘上每8个pages将消耗1byte的内存。
     vm-pages 134217728
26. 设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4
     vm-max-threads 4
27. 设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启
    glueoutputbuf yes
28. 指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法
    hash-max-zipmap-entries 64
    hash-max-zipmap-value 512
29. 指定是否激活重置哈希，默认为开启（后面在介绍Redis的哈希算法时具体介绍）
    activerehashing yes
30. 指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件
    include /path/to/local.conf

## 集群 ##

### 方案选择 ###

- redis主从方案
一个Master可以有多个slave主机，支持链式复制；

- 官方cluster方案
从redis 3.0版本开始支持redis-cluster集群，redis-cluster采用无中心结构，每个节点保存数据和整个集群状态，每个节点都和其他节点连接。redis-cluster是一种服务端分片技术



twemproxy代理方案

哨兵模式

codis

客户端分片

