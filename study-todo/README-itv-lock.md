# study- Lock #
## 简介

## Key关键点

- 锁标识位
- 锁的四种状态、和升级条件
- CAS 
- AQS 结构【字段、数据架构】

- 加锁解锁基本过程、优缺点、简单应用
- 可重入锁&不可重入锁
- 公平锁&非公平锁、实现原理
- 互斥锁&共享锁
- 线程的等待、唤醒
- 条件队列
- 响应中断
- Lock Support



## 锁状态

**概念**

**状态描述**

![](https://s2.loli.net/2022/04/22/wp6c4LxGA5qt29E.png)



**升级的条件**

## LockSupport

**描述**

LockSupport是一个线程阻塞工具类.所有的方法都是静态方法.可以让线程在任意位置阻塞.阻塞之后也有对应的唤醒方法.其本质上调用的是Unsafe类的native方法.
其实现原理是这样的:每个使用它的线程都有一个许可(permit)关联.permit相当于1,0的开关.默认是0.

## CAS 







## AQS

**说明**

可见行的 状态字段 + 先进先出的双向队列

**结构**

![](https://s2.loli.net/2022/04/26/LpC4FBEqa2QJsUh.png)



AQS 解析【2023-06-05】 https://blog.csdn.net/qq_40322236/article/details/127254744

AQS 解析【2023-06-05】 https://blog.csdn.net/weixin_41835916/article/details/128411322

https://blog.csdn.net/qq_37419449/article/details/120040856

AQS 解析【2023-06-07】https://www.jianshu.com/p/6a86e10293ab

https://zhuanlan.zhihu.com/p/86072774







## B站视频

AQS 执行过程

2023-06-07 https://www.bilibili.com/video/BV1mP4y1i7Vm/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-06-06 https://www.bilibili.com/video/BV1mP4y1i7Vm/?spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-06-06 https://www.bilibili.com/video/BV1E14y1E7Q4/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-06-05 https://www.bilibili.com/video/BV1z44y1X7BJ/?spm_id_from=333.999.0.0&vd_source=72424c3da68577f00ea40a9e4f9001a1

2023-06-05 https://www.bilibili.com/video/BV1E14y1E7Q4/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1

