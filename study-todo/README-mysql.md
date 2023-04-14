# study- Mysql #
TODO

## 关系 VS 非关系

参考链接 https://blog.csdn.net/weixin_51468875/article/details/114087402



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

参考链接 https://blog.csdn.net/qwer123451234123/article/details/124344299

https://www.cnblogs.com/roadwide/p/17149506.html

包含数据恢复 https://www.qycn.com/xzx/article/17076.html



sql执行原理





## 事务提交过程





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

### Key关键点

- 行业调研、优势、代替方案等
- 索引结构 
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