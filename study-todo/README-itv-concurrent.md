# Java 并发编程技术文档

## 第一部分：并发编程基础概念

### 1.1 什么是并发编程

并发编程是指多个线程同时执行的编程模式，它可以提高程序的执行效率，充分利用多核 CPU 的资源。在 Java 中，并发编程主要通过 Thread 类和相关的并发工具类来实现。

### 1.2 线程的基本概念

#### 1.2.1 线程的定义

线程是程序执行的最小单位，一个进程可以包含多个线程。线程共享进程的内存空间，但每个线程有自己的栈空间。

#### 1.2.2 线程的状态

Java 线程有以下几种状态：

- **NEW** - 线程刚创建，尚未启动
- **RUNNABLE** - 线程正在执行或准备执行
- **BLOCKED** - 线程被阻塞，等待获取锁
- **WAITING** - 线程等待，需要其他线程通知或中断
- **TIMED_WAITING** - 线程限时等待
- **TERMINATED** - 线程执行完毕

![](https://pdai.tech/images/pics/ace830df-9919-48ca-91b5-60b193f593d2.png)

### 1.3 线程的创建和启动

#### 1.3.1 继承 Thread 类

```java
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running");
    }
}

// 创建并启动线程
MyThread thread = new MyThread();
thread.start();
```

#### 1.3.2 实现 Runnable 接口

```java
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread is running");
    }
}

// 创建并启动线程
MyRunnable runnable = new MyRunnable();
Thread thread = new Thread(runnable);
thread.start();
```

#### 1.3.3 使用 Callable 和 Future

```java
public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "Thread is running";
    }
}

// 创建并启动线程
MyCallable callable = new MyCallable();
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(callable);
String result = future.get();
executor.shutdown();
```

### 1.4 线程的生命周期

线程的生命周期包括以下阶段：
1. **创建** - 实例化 Thread 对象
2. **启动** - 调用 start() 方法
3. **运行** - 执行 run() 方法
4. **阻塞** - 线程被暂停执行
5. **终止** - 线程执行完毕或被中断

## 第二部分：Java 并发核心技术

### 2.1 锁机制

#### 2.1.1 锁的基本概念

锁是并发编程中用于保护共享资源的机制，它可以确保同一时刻只有一个线程能够访问共享资源。

#### 2.1.2 锁的状态

在 JDK 1.6 之后，Java 引入了锁的升级机制，锁有四种状态：

- **无锁** - 没有线程持有锁
- **偏向锁** - 锁偏向于第一个获取它的线程
- **轻量级锁** - 线程通过 CAS 操作获取锁
- **重量级锁** - 线程通过操作系统的互斥量获取锁

![](https://s2.loli.net/2022/04/22/wp6c4LxGA5qt29E.png)

#### 2.1.3 锁的升级条件

1. **无锁 → 偏向锁** - 当第一个线程获取锁时，升级为偏向锁
2. **偏向锁 → 轻量级锁** - 当有其他线程竞争锁时，升级为轻量级锁
3. **轻量级锁 → 重量级锁** - 当线程竞争激烈时，升级为重量级锁

### 2.2 CAS 机制

#### 2.2.1 CAS 的基本概念

CAS（Compare And Swap）是一种乐观锁机制，它通过比较内存值和期望值，如果相等则更新内存值，否则重试。

#### 2.2.2 CAS 的实现原理

CAS 操作包含三个参数：内存位置（V）、期望值（A）和新值（B）。如果内存位置的值等于期望值，则将内存位置的值更新为新值，否则不做任何操作。

#### 2.2.3 CAS 的优缺点

**优点：**
- 无锁操作，不需要上下文切换
- 可以实现原子操作

**缺点：**
- 循环时间长，开销大
- 只能保证一个共享变量的原子操作
- 存在 ABA 问题

### 2.3 AQS 框架

#### 2.3.1 AQS 的基本概念

AQS（AbstractQueuedSynchronizer）是 Java 并发包中的基础框架，它提供了一种基于 FIFO 队列的同步器实现，用于构建锁和同步器。

#### 2.3.2 AQS 的数据结构

AQS 内部维护了一个 volatile 修饰的 state 字段和一个 FIFO 双向队列：

- **state 字段** - 用于表示同步状态
- **CLH 队列** - 用于存储等待获取锁的线程

![](https://mmbiz.qpic.cn/mmbiz_png/sMmr4XOCBzH49o61bopUB4EnjNCwHOecxIIErSt29lDBx4pUsV0ghoAXuia1M4GeNBCw8iaOpdIqv3wiaib81KLkjA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

#### 2.3.3 AQS 的模板方法

AQS 提供了以下模板方法，供子类实现：

- **isHeldExclusively()** - 判断当前线程是否独占资源
- **tryAcquire(int)** - 独占方式尝试获取资源
- **tryRelease(int)** - 独占方式尝试释放资源
- **tryAcquireShared(int)** - 共享方式尝试获取资源
- **tryReleaseShared(int)** - 共享方式尝试释放资源

### 2.4 线程池

#### 2.4.1 线程池的基本概念

线程池是一种管理线程的机制，它可以复用线程，减少线程创建和销毁的开销。

#### 2.4.2 线程池的参数

```java
public ThreadPoolExecutor(int corePoolSize,                      // 核心线程数
                          int maximumPoolSize,                  // 最大线程数
                          long keepAliveTime,                   // 线程存活时间
                          TimeUnit unit,                        // 时间单位
                          BlockingQueue<Runnable> workQueue,    // 工作队列
                          ThreadFactory threadFactory,          // 线程工厂
                          RejectedExecutionHandler handler)     // 拒绝策略
```

#### 2.4.3 线程池的工作流程

1. 当有任务提交时，首先检查核心线程数是否已满
2. 如果核心线程数未满，创建核心线程执行任务
3. 如果核心线程数已满，检查工作队列是否已满
4. 如果工作队列未满，将任务放入工作队列
5. 如果工作队列已满，检查最大线程数是否已满
6. 如果最大线程数未满，创建非核心线程执行任务
7. 如果最大线程数已满，执行拒绝策略

![](https://camo.githubusercontent.com/aa9e708171a22704b09a2b0a57732ae81a2e04b014063e68f15aa2fafbc7ebb3/68747470733a2f2f70362d6a75656a696e2e62797465696d672e636f6d2f746f732d636e2d692d6b3375316662706663702f65666539656438323039336534633862616237363865616337396466666564337e74706c762d6b3375316662706663702d7a6f6f6d2d312e696d616765)

## 第三部分：Java 并发工具类

### 3.1 Lock 接口

#### 3.1.1 Lock 接口的基本概念

Lock 接口是 Java 5 引入的，它提供了比 synchronized 更灵活的锁机制。

#### 3.1.2 Lock 接口的实现类

- **ReentrantLock** - 可重入锁
- **ReadWriteLock** - 读写锁
- **StampedLock** - 乐观读锁

### 3.2 同步工具类

#### 3.2.1 CountDownLatch

CountDownLatch 是一种同步工具，它允许一个或多个线程等待其他线程完成操作。

#### 3.2.2 CyclicBarrier

CyclicBarrier 是一种同步工具，它允许一组线程互相等待，直到达到某个公共屏障点。

#### 3.2.3 Semaphore

Semaphore 是一种同步工具，它用于控制对共享资源的访问数量。

#### 3.2.4 Exchanger

Exchanger 是一种同步工具，它允许两个线程在指定点交换数据。

### 3.3 并发集合

#### 3.3.1 ConcurrentHashMap

ConcurrentHashMap 是一种线程安全的哈希表，它支持高并发的读写操作。

#### 3.3.2 CopyOnWriteArrayList

CopyOnWriteArrayList 是一种线程安全的列表，它通过复制底层数组来实现线程安全。

#### 3.3.3 BlockingQueue

BlockingQueue 是一种阻塞队列，它支持在队列为空时阻塞获取操作，在队列满时阻塞插入操作。

### 3.4 LockSupport

#### 3.4.1 LockSupport 的基本概念

LockSupport 是一种线程阻塞工具，它可以在任意位置阻塞线程，并且可以唤醒指定的线程。

#### 3.4.2 LockSupport 的方法

- **park()** - 阻塞当前线程
- **parkNanos(long nanos)** - 阻塞当前线程指定时间
- **parkUntil(long deadline)** - 阻塞当前线程直到指定时间
- **unpark(Thread thread)** - 唤醒指定线程

#### 3.4.3 LockSupport 与 wait/notify 的区别

- **LockSupport 不需要获取对象锁** - wait/notify 需要在同步块中调用
- **LockSupport 可以唤醒指定线程** - notify 只能随机唤醒一个线程
- **LockSupport 不会产生死锁** - 如果先调用 unpark 再调用 park，线程不会阻塞

## 第四部分：并发编程最佳实践

### 4.1 线程安全

#### 4.1.1 线程安全的基本概念

线程安全是指多个线程同时访问共享资源时，不会导致数据不一致或其他异常情况。

#### 4.1.2 线程安全的实现方式

- **互斥锁** - 使用 synchronized 或 Lock
- **原子操作** - 使用 Atomic 类
- **无锁编程** - 使用 CAS 操作
- **线程本地存储** - 使用 ThreadLocal

### 4.2 性能优化

#### 4.2.1 减少锁的粒度

通过减小锁的范围，减少线程竞争，提高并发性能。

#### 4.2.2 使用无锁数据结构

使用 ConcurrentHashMap 等无锁数据结构，提高并发性能。

#### 4.2.3 合理使用线程池

根据任务类型和系统资源，合理配置线程池参数。

### 4.3 避免死锁

#### 4.3.1 死锁的基本概念

死锁是指两个或多个线程互相等待对方释放资源，导致所有线程都无法继续执行的情况。

#### 4.3.2 死锁的避免方法

- **避免嵌套锁** - 不要在持有一个锁的同时获取另一个锁
- **使用超时机制** - 使用 tryLock() 方法设置超时时间
- **按顺序获取锁** - 所有线程按照相同的顺序获取锁

## 第五部分：并发编程面试题解析

### 5.1 基础概念

#### 5.1.1 线程和进程的区别是什么？

**答案**：
- **进程** - 是操作系统分配资源的基本单位，每个进程有自己的内存空间
- **线程** - 是程序执行的最小单位，线程共享进程的内存空间
- **区别** - 进程是资源分配的基本单位，线程是调度的基本单位；进程切换开销大，线程切换开销小；进程间通信复杂，线程间通信简单

#### 5.1.2 sleep() 和 wait() 的区别是什么？

**答案**：
- **sleep()** - 是 Thread 类的方法，它不会释放锁，只是让线程暂停执行一段时间
- **wait()** - 是 Object 类的方法，它会释放锁，让线程进入等待状态，需要其他线程通过 notify() 或 notifyAll() 唤醒

#### 5.1.3 notify() 和 notifyAll() 的区别是什么？

**答案**：
- **notify()** - 随机唤醒一个等待在对象上的线程
- **notifyAll()** - 唤醒所有等待在对象上的线程
- **使用场景** - 当只有一个线程需要被唤醒时使用 notify()，当多个线程需要被唤醒时使用 notifyAll()

### 5.2 核心技术

#### 5.2.1 什么是 CAS？CAS 有哪些缺点？

**答案**：
- **CAS** - Compare And Swap，是一种乐观锁机制，它通过比较内存值和期望值，如果相等则更新内存值，否则重试
- **缺点** - 循环时间长，开销大；只能保证一个共享变量的原子操作；存在 ABA 问题

#### 5.2.2 AQS 的工作原理是什么？

**答案**：
- AQS 内部维护了一个 volatile 修饰的 state 字段和一个 FIFO 双向队列
- 当线程获取锁时，如果 state 为 0，则设置 state 为 1 并获取锁；否则，将线程加入队列并阻塞
- 当线程释放锁时，设置 state 为 0，并唤醒队列中的第一个线程

#### 5.2.3 线程池的工作原理是什么？

**答案**：
- 当有任务提交时，首先检查核心线程数是否已满
- 如果核心线程数未满，创建核心线程执行任务
- 如果核心线程数已满，检查工作队列是否已满
- 如果工作队列未满，将任务放入工作队列
- 如果工作队列已满，检查最大线程数是否已满
- 如果最大线程数未满，创建非核心线程执行任务
- 如果最大线程数已满，执行拒绝策略



### 5.3 实际应用

#### 5.3.1 如何实现线程安全的单例模式？

**答案**：
- **饿汉式** - 在类加载时创建实例
- **懒汉式** - 使用 synchronized 或双重检查锁定
- **静态内部类** - 利用类加载机制实现线程安全
- **枚举** - 利用枚举的特性实现线程安全

#### 5.3.2 如何实现线程间通信？

**答案**：
- **wait/notify** - 使用 Object 类的 wait() 和 notify() 方法
- **Lock/Condition** - 使用 Lock 接口和 Condition 接口
- **BlockingQueue** - 使用阻塞队列
- **CountDownLatch/CyclicBarrier** - 使用同步工具类

#### 5.3.3 如何处理并发修改异常？

**答案**：
- **使用迭代器** - 使用 Iterator 进行遍历，并使用 remove() 方法删除元素
- **使用并发集合** - 使用 ConcurrentHashMap 等并发集合
- **使用 CopyOnWriteArrayList** - 适用于读多写少的场景
- **使用 Collections.synchronizedList** - 适用于并发度不高的场景

## 第六部分：参考链接

### 6.1 官方文档

- [Java 并发编程官方文档](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Java 并发包文档](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html)

### 6.2 学习资源

- [Java 并发编程实战](https://www.amazon.cn/dp/B00U8J0NKE)
- [Java 并发编程艺术](https://www.amazon.cn/dp/B01646KMDM)
- [实战 Java 高并发程序设计](https://www.amazon.cn/dp/B01D6CHWJ0)

### 6.3 博客和文章

- [Java 并发编程指南](https://pdai.tech/md/java/thread/java-thread-x-overview.html)
- [Java 并发编程核心技术](https://zhuanlan.zhihu.com/p/571637324)
- [Java 线程池原理分析](https://juejin.cn/post/7303461209142099978)

### 6.4 视频教程

- [Java 并发编程实战 - B 站](https://www.bilibili.com/video/BV1mP4y1i7Vm/)
- [Java 并发编程核心技术 - B 站](https://www.bilibili.com/video/BV1E14y1E7Q4/)
- [Java 线程池原理与实战 - B 站](https://www.bilibili.com/video/BV1z44y1X7BJ/)

### 6.5 工具和框架

- [Disruptor](https://lmax-exchange.github.io/disruptor/)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [Project Loom](https://openjdk.org/projects/loom/)