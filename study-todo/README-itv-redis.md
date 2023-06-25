# study- Redis  #
## 简介





## Key关键点

- 行业调研、优势、代替方案等
- 架构
- 底层数据结构
- 持久化机制 RDB AOF
- 分布式锁
- 内存使用率【组合压缩？】
- 淘汰策略
- 集群【哨兵、Cluster】



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





LRU算法 https://baijiahao.baidu.com/s?id=1729434050706042976

https://www.bilibili.com/video/BV1Va411677h/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1





LFU 算法 https://baijiahao.baidu.com/s?id=1729525047952230592

## 持久化策略





## 注意事项 

【2023-06-25】

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247488325&idx=1&sn=6d9bbe5bf2f2f2904755de5c786fb21b&chksm=cf21cc6cf856457a9d23b3e25ec48107a582e709f05964dfdb5ba77e9a239d8307334c485fdf&token=162724582&lang=zh_CN&scene=21#wechat_redirect





## 参考链接

经典知识点【2023-06-10】https://blog.csdn.net/guorui_java/article/details/117194603

经典知识点【2023-06-10】https://zhuanlan.zhihu.com/p/536216476

总结 【2023-06-25】https://github.com/whx123/JavaHome/blob/master/%E7%BC%93%E5%AD%98Redis%E6%80%BB%E7%BB%93/README.MD

【2023-06-25】经典知识点 https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247494124&idx=1&sn=c185f7d999d5f006608d05707a8a7eea&chksm=cf2236c5f855bfd329c6e2ee27f23f8131ebcd312960190a10f1a819d67f07a21a08ad17f263&token=162724582&lang=zh_CN&scene=21#wechat_redirect

## Bilibili 

2023-05-25

https://www.bilibili.com/video/BV1R14y1p71k/?buvid=XY16A5614167C2F6C633EF8328DA0DBDD2FF2&is_story_h5=false&mid=Sj3flcq0QTXM78iJrhwidA%3D%3D&p=6&plat_id=114&share_from=ugc&share_medium=android&share_plat=android&share_session_id=ed74437e-f5f1-4445-a873-d0f1cf9abf98&share_source=COPY&share_tag=s_i&timestamp=1684977786&unique_k=NwNDREW&up_id=436556327&vd_source=72424c3da68577f00ea40a9e4f9001a1



https://www.bilibili.com/video/BV1ed4y1g7XU?spm_id_from=333.1007.tianma.2-1-4.click&vd_source=72424c3da68577f00ea40a9e4f9001a1