# study- Redis  #
## 简介



官方文档 https://redis.io/docs/



## Key关键点

- 行业调研、优势、代替方案等
- 架构
- 底层数据结构
- 持久化机制 RDB AOF
- 分布式锁
- 内存使用率【组合压缩？】
- 淘汰策略
- 集群【哨兵、Cluster】
- Redis 监控 



## **概要** 



## **架构** 



## 分布式锁 

https://github.com/bage2014/study/blob/master/study-redis/README-distribute-lock.md



## 数据类型

### String

注意点

- 最大存储为512M

与String对比

- SDS 数据结构 
- 杜绝缓冲区溢出
- 预分配容量
- 惰性释放
- 二进制安全



### Hash



### Map



### List



### Set



Bitmap

HyperLogLog

Geospatial

Pipeline

pub/sub



## 淘汰策略

【2023-06-25】https://baijiahao.baidu.com/s?id=1729630215002937706&wfr=spider&for=pc

过期删除 

- 定时删除
- 惰性删除

**内存淘汰策略** 

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



LRU算法 https://baijiahao.baidu.com/s?id=1729434050706042976

https://www.bilibili.com/video/BV1Va411677h/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1





LFU 算法 https://baijiahao.baidu.com/s?id=1729525047952230592

## 持久化策略

### RDB

全量快照，内存数据以快照的形式保存到磁盘

- 适合大规模的数据恢复场景，如备份，全量复制等



### AOF  

append only file

采用日志的形式来记录每个写操作，追加到文件中，重启时再重新执行AOF文件中的命令来恢复数据

- 主要解决数据持久化的实时性问题



官方网址 https://redis.io/docs/management/persistence/



## 集群

https://www.bilibili.com/video/BV1MW4y187AH/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1



https://baijiahao.baidu.com/s?id=1739276119927050514&wfr=spider&for=pc

https://mp.weixin.qq.com/s?__biz=MzI3MjY1ODI2Ng==&mid=2247486094&idx=1&sn=f727b9fe6f53f4ebc5280dd09d6161ed&chksm=eb2e70bbdc59f9ad8157859f0c6eb5fa881c9e147d7e03ca9693633f7d2bff7215d0f813c847&scene=27

https://blog.csdn.net/Drw_Dcm/article/details/127111524

https://cloud.tencent.com/developer/article/1530481?ivk_sa=1024320u



## 主从复制 

- 当新建立一个从服务器时，从服务器将向主服务器发送SYNC命令，接收到SYNC命令后的主服务器会进行一次BGSAVE命令
- 在执行期间，会将所有命令写入缓冲区中，当BGSAVE命令执行完毕之后会将生成的RDB文件发送给从服务器，从服务器使用这个文件加载数据到内存中，之后主服务器会以Redis命令协议的格式将缓冲区的命令发送给从服务器
- 此后每次主服务执行命令都会同步给从服务器。即使有多个从服务器向主服务器发送SYNC命令，主服务器也只会执行一个BGSAVE命令，就可以处理接下来的同步请求
- 一个主服务器可以拥有多个从服务器，而从服务器也可拥有从服务器，从而形成一个图状结构，复制功能不会阻塞主服务器，即使有一个或多个同步请求，主服务器依然能处理命令请求。



http://www.xbhp.cn/news/75679.html



## 注意事项 

【2023-06-25】

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247488325&idx=1&sn=6d9bbe5bf2f2f2904755de5c786fb21b&chksm=cf21cc6cf856457a9d23b3e25ec48107a582e709f05964dfdb5ba77e9a239d8307334c485fdf&token=162724582&lang=zh_CN&scene=21#wechat_redirect





## 参考链接

经典知识点【2023-06-10】https://blog.csdn.net/guorui_java/article/details/117194603

经典知识点【2023-06-10】https://zhuanlan.zhihu.com/p/536216476

总结 【2023-06-25】https://github.com/whx123/JavaHome/blob/master/%E7%BC%93%E5%AD%98Redis%E6%80%BB%E7%BB%93/README.MD

【2023-06-25】经典知识点 https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247494124&idx=1&sn=c185f7d999d5f006608d05707a8a7eea&chksm=cf2236c5f855bfd329c6e2ee27f23f8131ebcd312960190a10f1a819d67f07a21a08ad17f263&token=162724582&lang=zh_CN&scene=21#wechat_redirect



【2023-07-10】https://zhuanlan.zhihu.com/p/427496556



## Bilibili 

2023-05-25

https://www.bilibili.com/video/BV1R14y1p71k/?buvid=XY16A5614167C2F6C633EF8328DA0DBDD2FF2&is_story_h5=false&mid=Sj3flcq0QTXM78iJrhwidA%3D%3D&p=6&plat_id=114&share_from=ugc&share_medium=android&share_plat=android&share_session_id=ed74437e-f5f1-4445-a873-d0f1cf9abf98&share_source=COPY&share_tag=s_i&timestamp=1684977786&unique_k=NwNDREW&up_id=436556327&vd_source=72424c3da68577f00ea40a9e4f9001a1



https://www.bilibili.com/video/BV1ed4y1g7XU?spm_id_from=333.1007.tianma.2-1-4.click&vd_source=72424c3da68577f00ea40a9e4f9001a1