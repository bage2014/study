# Study-itv- algorithm- structure #

常用算法 和 数据结构 



## 关键点

- 排序
- 递归
- 分治
- 动态规划 
- 暴力法
- 跳跃表
- Map
- 一致性算法





## 跳跃表



跳表 https://zhuanlan.zhihu.com/p/637407262

https://mp.weixin.qq.com/s?__biz=MzU0ODMyNDk0Mw==&mid=2247495510&idx=1&sn=7a9f174b2a5facd92ee0efccf712eecc&chksm=fb427c76cc35f560d0ce02d6b7ff2f3e28c0349434734a428b20dfa2c3366d6266b15eacb588&scene=27

原文 https://epaperpress.com/sortsearch/download/skiplist.pdf

### 基本概念结构

1. **跳表的本质是一个多层的有序链表，同时结合了二分和链表的思想；**
2. 由很多层索引组成，每一层索引是通过**随机函数**随机产生的，每一层都是一个有序的链表，默认是升序 ；
3. 最底层的链表包含所有元素；
4. 如果一个元素出现在第i层的链表中，则它在第i层之下的链表也都会出现；
5. 跳表的每个节点包含两个指针，一个指向同一链表中的下一个元素，一个指向下面一层的元素。

### vs 红黑树

内存更小？

范围查询？



## 红黑树 vs 跳表

常用结构对比

https://www.cnblogs.com/charlesblc/p/5987812.html



### 一致性算法

一致性算法 Paxos等 https://zhuanlan.zhihu.com/p/130332285

**为什么需要一致性**

1. 数据不能存在单个节点（主机）上，否则可能出现单点故障。
2. 多个节点（主机）需要保证具有相同的数据。
3. 一致性算法就是为了解决上面两个问题。



### **一致性分类**

Google的Chubby分布式锁服务，采用了Paxos算法

etcd分布式键值数据库，采用了Raft算法

ZooKeeper分布式应用协调服务，Chubby的开源实现，采用ZAB算法

- 强一致性

- - 说明：保证系统改变提交以后立即改变集群的状态。

  - 模型：

  - - Paxos
    - Raft（muti-paxos）
    - ZAB（muti-paxos）

- 弱一致性

- - 说明：也叫最终一致性，系统不保证改变提交以后立即改变集群的状态，但是随着时间的推移最终状态是一致的。

  - 模型：

  - - DNS系统
    - Gossip协议



## 参考链接







## Bilibili

