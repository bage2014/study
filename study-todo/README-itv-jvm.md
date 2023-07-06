# study-JVM  #
## 简介



主要说： HotSpot JVM



## Key关键点

- 行业调研、优势、代替方案等
- 架构
- 类加载过程
- 触发类加载
- 类加载顺序
- JVM 虚拟机各种类型
- 参数含义【核心参数、调优】
- 命令行工具
- 可视化工具
- JVM 调优
- JVM 常用参数 
- Full GC 优化
- 内存泄露监控
- 对象头信息
- 频繁GC的场景
- OOM 场景和处理过程 



## **概要** 



## 架构 

#### 内存结构 

##### 堆空间 

新生代 + 老年代 

新生代 = Eden + From + To 

Eden 新生区；From 和 To 相互拷贝回收，理论上某一时刻只用到一个



## 参数解析

### 堆相关

-Xms【memory size】 初试堆大小 

-Xmx 【memory max】 最大堆大小

-XX:MinHeapFreeRatio 空余内存比例【增大使用】

-XX:MaxHeapFreeRatio 空余内存比如 【减少使用】 

- 堆得内存由-Xms指定，默认是物理内存的1/64；最大的内存由-Xmx指定，默认是物理内存的1/4。
-  默认空余的堆内存小于40%时，就会增大，直到-Xmx设置的内存。具体的比例可以由-XX:MinHeapFreeRatio指定
-  空余的内存大于70%时，就会减少内存，直到-Xms设置的大小。具体由-XX:MaxHeapFreeRatio指定。

–XX:NewRatio 新生代老年代 的比例

新生代 ( Young ) 与老年代 ( Old ) 的比例的值为 1:2 ( 该值可以通过参数 –XX:NewRatio 来指定 )

默认的，Eden : from : to = 8 : 1 : 1 ( 可以通过参数 –XX:SurvivorRatio 来设定 )，即： Eden = 8/10 的新生代空间大小，from = to = 1/10 的新生代空间大小。

*Perm代表的是永久代，不算再堆里面，jdk8 以后移除*



### 方法区

- -Xss设置每个线程的堆栈大小
- -XX:+UseParallelGC:选择垃圾收集器为并行收集器。此配置仅对年轻代有效。即上述配置下,年轻代使用并发收集,而年老代仍旧使用串行收集。
- -XX:ParallelGCThreads=20:配置并行收集器的线程数,即:同时多少个线程一起进行垃圾回收。此值最好配置与处理器数目相等。



参数解析 https://blog.csdn.net/Pluto372/article/details/122084515

参数说明 https://zhuanlan.zhihu.com/p/92588056

## **架构** 



## Bilibili 



## 参考链接 

https://blog.csdn.net/zouliping123456/article/details/124522802

https://blog.csdn.net/weixin_59262008/article/details/125676787

https://zhuanlan.zhihu.com/p/375202547

https://www.cnblogs.com/hanease/p/15869727.html