# study- 其他 #
## 简介





TODO

## Key关键点

- Canal 
- Poxas 算法 
- 海量数据处理
- mongo和ES的技术选型？在你负责设计一个系统时，什么场景你会选用mongo？什么场景你选用ES？
- select vs poll vs Epoll
- 对称与非对称加密算法
- 零拷贝
- 定时器Job 执行原理
- 淘汰算法
- 日志体系





## 日志体系

https://github.com/bage2014/study/tree/master/study-log

日志的知识体系

门面模式

高阶用法





## 淘汰算法

https://juejin.cn/post/7303366710693281829



## 零拷贝

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247498171&idx=1&sn=f5a7ec25a569822be0f73fbcd413e8ba&chksm=cf222692f855af84fba419166fcd4235c0e78af3a2e1ec4c723a4efb1bd1ad6f8a5b9404c599&token=2001057130&lang=zh_CN#rd

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247496788&idx=1&sn=f65ddd10d16d8376efa0037762153932&chksm=cf222b7df855a26bad76249e7b77e28da3097b226f9165d79f5031516d9c345827fca901559c&token=1496082535&lang=zh_CN&scene=21#wechat_redirect

零拷贝是指计算机执行IO操作时，CPU不需要将数据从一个存储区域复制到另一个存储区域，从而可以减少上下文切换以及CPU的拷贝时间。它是一种I/O操作优化技术。



传统的IO流程，包括read和write的过程。

- `read`：把数据从磁盘读取到内核缓冲区，再拷贝到用户缓冲区
- `write`：先把数据写入到socket缓冲区，最后写入网卡设备。

数据 -》 内核 -〉 用户缓冲区

用户缓冲区 》 socket缓冲区 〉 网卡 





## 架构

https://pdai.tech/md/arch/arch-x-overview.html



## B站视频

进度：

2023-05-23 canal

https://b23.tv/hZDiJb3



