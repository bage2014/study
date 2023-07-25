# study- Concurrent  #
## 简介

Java 并发相关知识点 

## Key关键点

- Object 锁标识位
- 锁的四种状态、和升级条件
- CAS 
- AtomicStampedReference 使用 
- AQS 结构【字段、数据架构】
- 加锁解锁基本过程、优缺点、简单应用
- 可重入锁&不可重入锁
- 公平锁&非公平锁、实现原理
- 互斥锁&共享锁
- 线程的等待、唤醒
- 线程池状态、扭转、常用方法、基本过程 
- 条件队列
- 响应中断
- Lock Support
- Lock Support 和 wait等区别 
- sleep() 和 wait() 
- Notify 和 notifyAll
- Run 和 start
- 线程循环打印 1 2 3 4



## 锁状态

**概念**

对象头中存在标识位

```text
JDK 1.6后引入
```

**状态描述**

![](https://s2.loli.net/2022/04/22/wp6c4LxGA5qt29E.png)





**四种状态**

- 无锁

- 偏向锁 

偏向锁 标志 + 记录线程ID 

避免CAS 竞争，而只是简单做上面两个字段的判断校验

- 轻量级锁

CAS 加锁过程

- 重量级锁

直接退化成最原始用法





**升级的条件**





参考链接 https://www.jianshu.com/p/c67b0dac5d2b

https://zhuanlan.zhihu.com/p/509096754?utm_id=0



## LockSupport

### **描述**

LockSupport是一个线程阻塞工具类.所有的方法都是静态方法.可以让线程在任意位置阻塞.阻塞之后也有对应的唤醒方法.其本质上调用的是Unsafe类的native方法.
其实现原理是这样的:每个使用它的线程都有一个许可(permit)关联.permit相当于1,0的开关.默认是0.

【2023-06-08】https://www.jianshu.com/p/f1f2cd289205

【2023-06-08】https://blog.csdn.net/BaiHang111/article/details/125685979

【2023-06-08】https://blog.csdn.net/zyzzxycj/article/details/90268381

【2023-06-08】https://baijiahao.baidu.com/s?id=1666548481761194849&wfr=spider&for=pc

### 对比差异 

- notify只能随机选择一个线程唤醒，无法唤醒指定的线程，unpark却可以唤醒一个指定的线程
- wait和notify都是Object中的方法,在调用这两个方法前必须先获得锁对象，但是park不需要获取某个对象的锁就可以锁住线程
- 相对于线程的stop和resume，park和unpark的先后顺序并不是那么严格。stop和resume如果顺序反了，会出现死锁现象。而park和unpark却不会
- blocker的作用是在dump线程的时候看到阻塞对象的信息



## CAS 

Compare And Set

比较和赋值



**CAS有哪些缺点？**
1. 循环时间长，开销大
2. 如果cas失败，就一直do while尝试。如果长时间不成功，可能给CPU带来很大开销。
3. 只能保证一个共享变量的原子操作
4. 如果时多个共享变量，cas无法保证原子性，只能加锁，锁住代码段。
5. 存在ABA问题。





参考链接 

https://www.jianshu.com/p/a47285790467

https://zhuanlan.zhihu.com/p/571637324



## AQS

**说明**

可见行的 状态字段 + 先进先出的双向队列

**结构**

![](https://s2.loli.net/2022/04/26/LpC4FBEqa2QJsUh.png)



AQS 解析【2023-06-05】 https://blog.csdn.net/qq_40322236/article/details/127254744

AQS 解析【2023-06-05】 https://blog.csdn.net/weixin_41835916/article/details/128411322

https://blog.csdn.net/qq_37419449/article/details/120040856

AQS 解析【2023-06-07】https://www.jianshu.com/p/6a86e10293ab

AQS 解析【2023-06-07】https://zhuanlan.zhihu.com/p/86072774



要点整理 

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247487939&idx=1&sn=560f9ec0fdbc081949383bbee2407b0e&chksm=cf21ceeaf85647fc24537661ca063f9537b5cb5090da1c4ecf1f4d8326a5359391143bd16e1a&token=1496082535&lang=zh_CN&scene=21#wechat_redirect



## 公平锁 VS 非公平锁

过程解析【2023-06-07】https://blog.csdn.net/weixin_43243916/article/details/100060923



## 读写锁

读写锁 【2023-06-07】https://blog.csdn.net/weixin_45607513/article/details/119213660



## B站视频

AQS 执行过程

2023-06-07 https://www.bilibili.com/video/BV1mP4y1i7Vm/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-06-06 https://www.bilibili.com/video/BV1mP4y1i7Vm/?spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-06-06 https://www.bilibili.com/video/BV1E14y1E7Q4/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-06-05 https://www.bilibili.com/video/BV1z44y1X7BJ/?spm_id_from=333.999.0.0&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-06-05 https://www.bilibili.com/video/BV1E14y1E7Q4/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1

