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

**详细解释说明**

当线程竞争激烈时，轻量级锁会升级为重量级锁，主要发生在以下情况：

1. **自旋失败**：当线程尝试通过自旋获取锁时，如果自旋超过一定次数（JVM 会根据 CPU 核心数等因素动态调整）仍然无法获取锁，会升级为重量级锁
2. **线程数超过 CPU 核心数**：当竞争线程数超过 CPU 核心数时，自旋会导致 CPU 资源浪费，此时会升级为重量级锁
3. **长时间持有锁**：当持有锁的线程执行时间较长时，其他线程的自旋会浪费 CPU 资源，此时会升级为重量级锁

**重量级锁的特点**：
- 依赖操作系统的互斥量（mutex）实现
- 线程阻塞时会进入内核态，上下文切换开销较大
- 但在竞争激烈时，重量级锁比自旋更节省 CPU 资源

**代码样例**

```java
public class LockUpgradeDemo {
    private static final Object lock = new Object();
    private static int counter = 0;
    
    public static void main(String[] args) {
        // 创建多个线程竞争锁
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    synchronized (lock) {
                        counter++;
                    }
                }
            }).start();
        }
        
        // 等待所有线程执行完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Counter: " + counter);
    }
}
```

**执行说明**：
- 当只有一个线程执行时，使用偏向锁
- 当有少量线程竞争时，使用轻量级锁（自旋）
- 当有大量线程竞争时（如示例中的10个线程），会升级为重量级锁
- 升级后，线程会通过操作系统的互斥量进行同步，避免 CPU 资源浪费

### 2.2 CAS 机制

#### 2.2.1 CAS 的基本概念

CAS（Compare And Swap）是一种乐观锁机制，它通过比较内存值和期望值，如果相等则更新内存值，否则重试。

#### 2.2.2 CAS 的实现原理

**CAS 操作原理**

CAS 操作包含三个参数：内存位置（V）、期望值（A）和新值（B）。如果内存位置的值等于期望值，则将内存位置的值更新为新值，否则不做任何操作。

**CAS 数据结构**

**1. 原子类内部结构**

```java
// AtomicInteger 内部结构简化版
public class AtomicInteger extends Number implements java.io.Serializable {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset; // 值的内存偏移量
    
    static {
        try {
            // 通过反射获取 value 字段的内存偏移量
            valueOffset = unsafe.objectFieldOffset(
                AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
    
    private volatile int value; // 存储实际值，使用 volatile 保证可见性
    
    // CAS 操作核心方法
    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }
}
```

**2. Unsafe 类核心方法**

| 方法 | 描述 | 参数 |
|------|------|------|
| compareAndSwapInt | 原子更新 int 类型 | obj: 对象, offset: 内存偏移量, expect: 期望值, update: 新值 |
| compareAndSwapLong | 原子更新 long 类型 | obj: 对象, offset: 内存偏移量, expect: 期望值, update: 新值 |
| compareAndSwapObject | 原子更新引用类型 | obj: 对象, offset: 内存偏移量, expect: 期望值, update: 新值 |

**CAS 数据结构图**

```
+================================+
|           内存位置 (V)          |
|  存储当前值，如：0x1000         |
+================================+
                ↑
                | 读取
                |
+================================+
|         CPU 寄存器             |
|  期望值 (A): 0x1000            |
|  新值 (B): 0x2000               |
+================================+
                |
                | 比较并交换
                ↓
+================================+
|         比较逻辑               |
| if (V == A) then V = B         |
| else do nothing                |
+================================+
                |
                ↓
+================================+
|         结果                    |
| 成功: 返回 true, V = 0x2000     |
| 失败: 返回 false, V 不变        |
+================================+
```

**CAS 详细流程说明**

**1. 完整执行流程**

```
开始 → 读取内存值到寄存器 → 计算新值 → 比较内存值与期望值 → 相等？
    ├─ 是 → 更新内存值为新值 → 返回成功
    └─ 否 → 检查是否需要重试 → 需要？
        ├─ 是 → 重新读取内存值 → 计算新值 → 比较内存值与期望值
        └─ 否 → 返回失败
```

**2. 自旋重试机制流程**

```
开始 → 初始化期望值 = 当前值 → 计算新值
    → 尝试 CAS 操作 → 成功？
        ├─ 是 → 返回新值
        └─ 否 → 重新读取当前值 → 当前值 == 期望值？
            ├─ 是 → 可能有其他线程修改后又改回，继续尝试
            └─ 否 → 更新期望值为当前值 → 重新计算新值 → 继续尝试
```

**底层实现**

CAS 操作在底层是通过 CPU 指令实现的，不同 CPU 架构有不同的实现：

- **x86 架构**：使用 `cmpxchg` 指令
- **ARM 架构**：使用 `ldrex` 和 `strex` 指令

这些指令是原子的，保证了 CAS 操作的原子性。

**CAS 与 volatile 的关系**

- **volatile 保证可见性**：确保其他线程对变量的修改能被当前线程看到
- **CAS 保证原子性**：确保更新操作的原子性
- **两者结合**：volatile 确保 CAS 操作能读取到最新值，CAS 确保更新操作的原子性

**CAS 性能特点**

| 场景 | 性能表现 | 原因 |
|------|----------|------|
| 低竞争 | 非常快 | 无锁操作，无需上下文切换 |
| 中竞争 | 良好 | 自旋重试次数较少 |
| 高竞争 | 较差 | 自旋重试次数多，CPU 开销大 |
| 超高竞争 | 很差 | 几乎所有线程都在自旋，CPU 资源浪费严重 |

**代码示例**

```java
public class CASDemo {
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    public static void main(String[] args) {
        // 尝试将 counter 从 0 改为 1
        boolean success = counter.compareAndSet(0, 1);
        System.out.println("CAS 操作结果: " + success);
        System.out.println("counter 值: " + counter.get());
        
        // 尝试将 counter 从 0 改为 2（会失败）
        success = counter.compareAndSet(0, 2);
        System.out.println("CAS 操作结果: " + success);
        System.out.println("counter 值: " + counter.get());
    }
}
```

**执行说明**：
1. 第一次 CAS 操作：内存值为 0，期望值为 0，新值为 1，操作成功
2. 第二次 CAS 操作：内存值为 1，期望值为 0，不相等，操作失败

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

#### 2.3.3 AQS 的基本原理和常见过程

**AQS 基本原理**

AQS（AbstractQueuedSynchronizer）【抽象队列同步器】是 Java 并发包中的基础框架，它通过以下机制实现同步：

1. **状态管理**：通过 volatile 修饰的 state 字段管理同步状态
2. **队列管理**：通过 CLH 双向队列管理等待线程
3. **模板方法**：提供模板方法让子类实现具体的同步逻辑
4. **CAS 操作**：使用 CAS 操作保证状态更新的原子性

**AQS 数据结构详细说明**

```
+================================+
|        AQS 同步器              |
+================================+
| - state: volatile int          |
| - head: Node (队首)            |
| - tail: Node (队尾)             |
+================================+
                |
                v
+================================+
|          CLH 队列              |
+================================+
| Node1 → Node2 → Node3 → Node4  |
| (head)        (tail)           |
+================================+

每个 Node 节点包含：
- thread: Thread (当前线程)
- prev: Node (前驱节点)
- next: Node (后继节点)
- waitStatus: int (等待状态)
- nextWaiter: Node (条件队列中的后继节点)
```

**AQS 常见过程**

**1. 获取锁过程**

```
开始 → 尝试获取锁 (tryAcquire) → 成功？
    ├─ 是 → 返回成功
    └─ 否 → 创建节点并加入队列 → 阻塞线程 → 被唤醒后再次尝试获取锁
```

**2. 释放锁过程**

```
开始 → 尝试释放锁 (tryRelease) → 成功？
    ├─ 是 → 唤醒队列中的后继节点 → 返回成功
    └─ 否 → 返回失败
```

**3. 条件等待过程**

```
开始 → 获取锁 → 检查条件 → 条件不满足？
    ├─ 是 → 进入条件队列 → 释放锁 → 被唤醒后重新获取锁 → 再次检查条件
    └─ 否 → 执行条件满足后的逻辑
```

**4. 条件唤醒过程**

```
开始 → 获取锁 → 唤醒条件队列中的线程 → 线程从条件队列转移到同步队列 → 释放锁
```

#### 2.3.3.1 AQS 核心过程解析

**1. 获取锁的核心过程**

```
+----------------------------------------+
|          开始获取锁                     |
+----------------------------------------+
                  |
                  v
+----------------------------------------+
|     调用 tryAcquire(int arg)            |
|     尝试获取锁                          |
+----------------------------------------+
                  |
                  v
+----------------------------------------+
|           成功？                        |
+----------------------------------------+
         /        |        \
        /         |         \
       v          v          v
+------------+ +------------+ +------------+
|   成功     | |    失败     | |    失败     |
|   返回     | | 创建节点    | | 加入队列    |
+------------+ +------------+ +------------+
                  |            |
                  v            v
+----------------------------------------+
|       加入 CLH 队列尾部                 |
+----------------------------------------+
                  |
                  v
+----------------------------------------+
|     调用 acquireQueued(Node, int)      |
|     在队列中等待获取锁                  |
+----------------------------------------+
                  |
                  v
+----------------------------------------+
|       前驱节点是否为头节点？             |
+----------------------------------------+
         /        |        \
        /         |         \
       v          v          v
+------------+ +------------+ +------------+
|    是      | |    否      | |    否      |
|尝试获取锁  | | 阻塞线程   | | 阻塞线程   |
+------------+ +------------+ +------------+
                  |            |
                  v            v
+----------------------------------------+
|          被唤醒后再次尝试获取锁          |
+----------------------------------------+
                  |
                  v
+----------------------------------------+
|           获取锁成功？                   |
+----------------------------------------+
         /        |        \
        /         |         \
       v          v          v
+------------+ +------------+ +------------+
|    是      | |    否      | |    否      |
| 设置头节点 | | 继续阻塞   | | 继续阻塞   |
|   返回     | |            | |            |
+------------+ +------------+ +------------+
```

**2. 释放锁的核心过程**

```
+----------------------------------------+
|          开始释放锁                     |
+----------------------------------------+
                  |
                  v
+----------------------------------------+
|     调用 tryRelease(int arg)            |
|     尝试释放锁                          |
+----------------------------------------+
                  |
                  v
+----------------------------------------+
|           成功？                        |
+----------------------------------------+
         /        |        \
        /         |         \
       v          v          v
+------------+ +------------+ +------------+
|   成功     | |    失败     | |    失败     |
| 唤醒后继节点| |   返回     | |   返回     |
+------------+ +------------+ +------------+
                  |            |
                  v            v
+----------------------------------------+
|        调用 unparkSuccessor(Node)       |
|        唤醒队列中的后继节点              |
+----------------------------------------+
                  |
                  v
+----------------------------------------+
|          释放锁完成                     |
+----------------------------------------+
```

#### 2.3.3.2 核心过程解释说明

**1. 获取锁的核心过程**

**a. 尝试获取锁（tryAcquire）**
- **工作原理**：子类实现的模板方法，尝试获取锁
- **执行线程**：当前线程
- **目标**：获取锁并返回成功，否则返回失败

**b. 创建节点并加入队列**
- **工作原理**：如果 tryAcquire 失败，创建一个 Node 节点表示当前线程
- **执行线程**：当前线程
- **目标**：将节点加入 CLH 队列尾部

**c. 在队列中等待（acquireQueued）**
- **工作原理**：循环检查前驱节点是否为头节点，如果是则尝试获取锁
- **执行线程**：当前线程
- **目标**：获取锁或阻塞线程

**d. 阻塞线程**
- **工作原理**：如果前驱节点不是头节点，调用 LockSupport.park() 阻塞线程
- **执行线程**：当前线程
- **目标**：等待被唤醒

**e. 被唤醒后再次尝试获取锁**
- **工作原理**：线程被唤醒后，再次检查前驱节点是否为头节点，如果是则尝试获取锁
- **执行线程**：当前线程
- **目标**：获取锁

**f. 获取锁成功**
- **工作原理**：获取锁成功后，将当前节点设置为头节点
- **执行线程**：当前线程
- **目标**：完成锁的获取

**2. 释放锁的核心过程**

**a. 尝试释放锁（tryRelease）**
- **工作原理**：子类实现的模板方法，尝试释放锁
- **执行线程**：当前线程
- **目标**：释放锁并返回成功，否则返回失败

**b. 唤醒后继节点（unparkSuccessor）**
- **工作原理**：如果 tryRelease 成功，唤醒队列中的后继节点
- **执行线程**：当前线程
- **目标**：唤醒等待的线程

**c. 释放锁完成**
- **工作原理**：返回释放锁的结果
- **执行线程**：当前线程
- **目标**：完成锁的释放

#### 2.3.3.3 AQS 核心技术点

**1. CLH 队列**
- **工作原理**：CLH 是一种基于链表的无锁队列，用于存储等待获取锁的线程
- **优势**：结构简单，实现高效，支持 FIFO 顺序
- **应用**：在 AQS 中用于管理等待线程

**2. 状态管理**
- **工作原理**：通过 volatile 修饰的 state 字段管理同步状态
- **优势**：保证状态的可见性，支持原子操作
- **应用**：在 AQS 中用于表示锁的状态

**3. 模板方法模式**
- **工作原理**：AQS 提供模板方法，让子类实现具体的同步逻辑
- **优势**：代码复用，结构清晰
- **应用**：在 AQS 中用于构建不同类型的同步器

**4. CAS 操作**
- **工作原理**：使用 CAS 操作保证状态更新的原子性
- **优势**：无锁操作，性能高
- **应用**：在 AQS 中用于更新 state 字段和修改队列

**代码示例**

```java
// 自定义同步器示例
class MySync extends AbstractQueuedSynchronizer {
    @Override
    protected boolean tryAcquire(int arg) {
        if (compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }
    
    @Override
```
    protected boolean tryRelease(int arg) {
        if (getState() == 0) {
            throw new IllegalMonitorStateException();
        }
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }
    
    @Override
    protected boolean isHeldExclusively() {
        return getState() == 1;
    }
    
    public void lock() {
        acquire(1);
    }
    
    public void unlock() {
        release(1);
    }
}
```

**使用场景**

AQS 被广泛应用于 Java 并发包中的各种同步器：
- **ReentrantLock**：基于 AQS 实现的可重入锁
- **CountDownLatch**：基于 AQS 实现的倒计时器
- **Semaphore**：基于 AQS 实现的信号量
- **CyclicBarrier**：基于 AQS 实现的循环屏障
- **FutureTask**：基于 AQS 实现的异步任务

#### 2.3.4 AQS 的模板方法

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

**线程池工作流程图**

```
开始 → 提交任务 → 核心线程数已满？
    ├─ 否 → 创建核心线程执行任务 → 结束
    └─ 是 → 工作队列已满？
        ├─ 否 → 将任务放入工作队列 → 结束
        └─ 是 → 最大线程数已满？
            ├─ 否 → 创建非核心线程执行任务 → 结束
            └─ 是 → 执行拒绝策略 → 结束
```

**详细工作流程**

1. **当有任务提交时**，首先检查核心线程数是否已满
2. **如果核心线程数未满**，创建核心线程执行任务
3. **如果核心线程数已满**，检查工作队列是否已满
4. **如果工作队列未满**，将任务放入工作队列
5. **如果工作队列已满**，检查最大线程数是否已满
6. **如果最大线程数未满**，创建非核心线程执行任务
7. **如果最大线程数已满**，执行拒绝策略

**线程池状态转换**

```
+================================+
|        线程池状态              |
+================================+
| RUNNING: 接受新任务并处理队列  |
| SHUTDOWN: 不接受新任务，处理队列|
| STOP: 不接受新任务，中断处理中线程|
| TIDYING: 所有任务已终止         |
| TERMINATED: 线程池已终止        |
+================================+


```

#### 2.4.4 拒绝策略类型

**常见的拒绝策略（RejectedExecutionHandler）类型**

| 拒绝策略                       | 含义           | 特点                                                       |
| ------------------------------ | -------------- | ---------------------------------------------------------- |
| AbortPolicy                    | 中止策略       | 直接抛出 RejectedExecutionException 异常，阻止系统正常运行 |
| CallerRunsPolicy               | 调用者运行策略 | 由提交任务的线程执行该任务，降低新任务的提交速度           |
| DiscardPolicy                  | 丢弃策略       | 直接丢弃无法处理的任务，不做任何处理                       |
| DiscardOldestPolicy            | 丢弃最旧策略   | 丢弃工作队列中最旧的任务，然后尝试提交新任务               |
| CustomRejectedExecutionHandler | 自定义拒绝策略 | 根据业务需求自定义处理逻辑                                 |

**拒绝策略选择建议**

- **AbortPolicy**：适用于需要明确知道任务被拒绝的场景，确保系统稳定性
- **CallerRunsPolicy**：适用于任务量不大且要求不丢失任务的场景
- **DiscardPolicy**：适用于对任务丢失不敏感的场景
- **DiscardOldestPolicy**：适用于需要处理最新任务的场景
- **CustomRejectedExecutionHandler**：适用于有特殊业务需求的场景

#### 2.4.5 阻塞队列类型和含义

**常见的阻塞队列（BlockingQueue）类型**

| 队列类型              | 含义                     | 特点                                                         |
| --------------------- | ------------------------ | ------------------------------------------------------------ |
| ArrayBlockingQueue    | 基于数组的有界阻塞队列   | 有固定容量，FIFO 顺序，使用 ReentrantLock 保证线程安全       |
| LinkedBlockingQueue   | 基于链表的阻塞队列       | 可选有界/无界，默认无界，FIFO 顺序，吞吐量通常高于 ArrayBlockingQueue |
| SynchronousQueue      | 同步队列                 | 无容量，每个插入操作必须等待一个对应的移除操作，适用于直接提交策略 |
| PriorityBlockingQueue | 基于优先级的无界阻塞队列 | 无界，按优先级排序，元素必须实现 Comparable 接口或提供 Comparator |
| DelayQueue            | 基于优先级的延迟队列     | 无界，元素必须实现 Delayed 接口，只有到期的元素才能被取出    |
| LinkedTransferQueue   | 基于链表的无界传输队列   | 无界，支持 transfer() 方法，生产者可以等待消费者接收元素     |
| LinkedBlockingDeque   | 基于链表的双向阻塞队列   | 可选有界/无界，支持 FIFO 和 LIFO 操作，适用于工作窃取算法    |

**队列选择建议**

- **ArrayBlockingQueue**：适用于需要严格控制队列大小的场景
- **LinkedBlockingQueue**：适用于任务量较大且不需要严格控制队列大小的场景
- **SynchronousQueue**：适用于直接提交策略，要求处理速度快的场景
- **PriorityBlockingQueue**：适用于需要按优先级处理任务的场景
- **DelayQueue**：适用于需要延迟执行任务的场景

#### 2.4.6 线程工厂使用样例

**自定义线程工厂**

```java
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public CustomThreadFactory(String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}

```

**线程工厂使用示例**

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadFactoryDemo {
    public static void main(String[] args) {
        // 使用自定义线程工厂
        CustomThreadFactory customFactory = new CustomThreadFactory("MyApp");
        
        // 创建线程池
        ExecutorService executorService = new ThreadPoolExecutor(
            5, // 核心线程数
            10, // 最大线程数
            60, // 线程存活时间
            TimeUnit.SECONDS, // 时间单位
            new LinkedBlockingQueue<>(100), // 工作队列
            customFactory, // 自定义线程工厂
            new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
        
        // 提交任务
        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            executorService.submit(() -> {
                System.out.println("Task " + taskId + " executed by thread: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        
        // 关闭线程池
        executorService.shutdown();
    }
}
```

**内置线程工厂对比**

| 线程工厂 | 特点 | 适用场景 |
|---------|------|----------|
| Executors.defaultThreadFactory() | 默认线程工厂，创建的线程属于同一线程组，非守护线程，优先级为 NORM_PRIORITY | 一般场景 |
| Executors.privilegedThreadFactory() | 创建的线程拥有当前访问控制上下文和类加载器 | 需要特定权限的场景 |
| CustomThreadFactory | 可自定义线程名称、优先级、是否为守护线程等 | 需要定制线程属性的场景 |

**线程工厂最佳实践**

1. **命名规范**：为线程池和线程设置有意义的名称，便于日志分析和问题定位
2. **线程属性**：根据实际需求设置线程的优先级、是否为守护线程等属性
3. **异常处理**：在 run() 方法中捕获未处理的异常，避免线程意外终止
4. **资源管理**：确保线程池能够正确关闭，释放资源
5. **监控**：可以在线程工厂中添加监控逻辑，统计线程创建和销毁情况

#### 2.4.7 动态线程池

**1. 动态线程池的基本背景和概念**

**基本背景**：
在传统的线程池使用中，线程池的参数（如核心线程数、最大线程数、队列大小等）通常是在初始化时固定设置的，无法根据运行时的系统负载和业务需求进行动态调整。这在面对流量波动较大的场景时，可能会导致以下问题：
- **流量低谷时**：线程池线程数过多，导致资源浪费
- **流量高峰时**：线程池线程数不足，导致任务排队积压，响应时间变长
- **系统资源变化时**：无法根据系统资源的变化（如 CPU 使用率、内存使用情况）调整线程池参数

**概念**：
动态线程池是指能够根据运行时的系统负载、业务需求和资源使用情况，自动或手动调整线程池参数的线程池实现。它可以：

- **动态调整核心线程数**：根据系统负载调整核心线程数量
- **动态调整最大线程数**：根据峰值流量调整最大线程数量
- **动态调整队列大小**：根据任务积压情况调整队列容量
- **动态调整拒绝策略**：根据业务需求调整任务拒绝策略
- **动态调整线程存活时间**：根据线程空闲情况调整线程存活时间

**2. 动态线程池的实现原理**

**核心原理**：
动态线程池的实现核心在于：
1. **参数管理**：将线程池参数从硬编码改为可配置、可动态修改的方式
2. **参数监听**：监控配置参数的变化
3. **参数更新**：当参数变化时，实时更新线程池的实际参数
4. **状态监控**：监控线程池的运行状态，为参数调整提供依据

**实现方式**：

**方式一：基于 ThreadPoolExecutor 扩展**

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DynamicThreadPool extends ThreadPoolExecutor {
    private AtomicInteger corePoolSize;
    private AtomicInteger maximumPoolSize;
    private long keepAliveTime;
    private TimeUnit unit;
    private BlockingQueue<Runnable> workQueue;
    
    public DynamicThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.corePoolSize = new AtomicInteger(corePoolSize);
        this.maximumPoolSize = new AtomicInteger(maximumPoolSize);
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;
    }
    
    // 动态调整核心线程数
    public void setCorePoolSize(int newCorePoolSize) {
        this.corePoolSize.set(newCorePoolSize);
        super.setCorePoolSize(newCorePoolSize);
    }
    
    // 动态调整最大线程数
    public void setMaximumPoolSize(int newMaximumPoolSize) {
        this.maximumPoolSize.set(newMaximumPoolSize);
        super.setMaximumPoolSize(newMaximumPoolSize);
    }
    
    // 动态调整线程存活时间
    public void setKeepAliveTime(long time, TimeUnit unit) {
        this.keepAliveTime = time;
        this.unit = unit;
        super.setKeepAliveTime(time, unit);
    }
    
    // 获取当前参数
    public int getCurrentCorePoolSize() {
        return this.corePoolSize.get();
    }
    
    public int getCurrentMaximumPoolSize() {
        return this.maximumPoolSize.get();
    }
    
    public long getCurrentKeepAliveTime() {
        return this.keepAliveTime;
    }
    
    public TimeUnit getCurrentTimeUnit() {
        return this.unit;
    }
}
```

**方式二：基于配置中心实现**

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigurableThreadPool {
    private DynamicThreadPool executor;
    private String poolName;
    private ConfigCenter configCenter; // 配置中心客户端
    
    public ConfigurableThreadPool(String poolName, ConfigCenter configCenter) {
        this.poolName = poolName;
        this.configCenter = configCenter;
        
        // 从配置中心加载初始参数
        ThreadPoolConfig initialConfig = configCenter.getThreadPoolConfig(poolName);
        this.executor = new DynamicThreadPool(
            initialConfig.getCorePoolSize(),
            initialConfig.getMaximumPoolSize(),
            initialConfig.getKeepAliveTime(),
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(initialConfig.getQueueCapacity())
        );
        
        // 监听配置变化
        configCenter.addConfigListener(poolName, this::onConfigChange);
    }
    
    // 配置变化回调
    private void onConfigChange(ThreadPoolConfig newConfig) {
        executor.setCorePoolSize(newConfig.getCorePoolSize());
        executor.setMaximumPoolSize(newConfig.getMaximumPoolSize());
        executor.setKeepAliveTime(newConfig.getKeepAliveTime(), TimeUnit.SECONDS);
        // 注意：队列大小通常无法动态调整，需要特殊处理
        System.out.println("ThreadPool " + poolName + " config updated: " + newConfig);
    }
    
    public ExecutorService getExecutor() {
        return executor;
    }
    
    // 线程池配置类
    public static class ThreadPoolConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private int queueCapacity;
        private long keepAliveTime;
        
        // getters and setters
    }
    
    // 配置中心接口
    public interface ConfigCenter {
        ThreadPoolConfig getThreadPoolConfig(String poolName);
        void addConfigListener(String poolName, ConfigChangeListener listener);
        
        interface ConfigChangeListener {
            void onConfigChange(ThreadPoolConfig newConfig);
        }
    }
}
```

**3. 动态线程池的注意事项**

**注意事项**：

1. **线程安全**：
   - 线程池参数的修改必须是线程安全的
   - 使用原子类或同步机制确保参数更新的原子性

2. **队列大小调整**：
   - 标准的 BlockingQueue 通常不支持动态调整容量
   - 可以通过自定义队列实现或替换队列的方式实现
   - 替换队列时需要注意线程安全和任务迁移

3. **参数合理性**：
   - 核心线程数：通常设置为 CPU 核心数的 1-2 倍（CPU 密集型）或更多（IO 密集型）
   - 最大线程数：根据系统资源和峰值流量设置，避免过多线程导致上下文切换开销
   - 队列大小：根据任务处理速度和内存大小设置，避免队列过大导致内存溢出
   - 线程存活时间：根据线程空闲情况设置，避免频繁创建和销毁线程

4. **监控和告警**：
   - 监控线程池的运行状态：活跃线程数、队列大小、任务完成数、拒绝次数等
   - 设置合理的告警阈值：如队列使用率超过 80%、活跃线程数接近最大线程数等
   - 建立完善的监控体系，及时发现和处理线程池异常

5. **平滑调整**：
   - 参数调整应平滑进行，避免突然的参数变化导致系统波动
   - 对于核心线程数的减少，应等待线程自然终止，而不是强制中断
   - 对于最大线程数的增加，应考虑系统资源的承载能力

6. **回滚机制**：
   - 当参数调整导致系统性能下降时，应能够快速回滚到之前的参数配置
   - 建立参数调整的审计和回滚机制

7. **负载评估**：
   - 建立科学的负载评估模型，根据系统负载和业务需求调整参数
   - 考虑 CPU 使用率、内存使用情况、网络 IO、磁盘 IO 等因素
   - 可以采用自适应算法，根据历史数据自动调整参数

**4. 动态线程池的使用场景**

| 场景 | 特点 | 动态调整策略 |
|------|------|------------|
| **Web 应用** | 流量波动大，有明显的高峰期和低谷期 | 根据 QPS 动态调整核心线程数和最大线程数 |
| **批处理系统** | 任务量不稳定，有时任务集中到达 | 根据任务积压情况动态调整队列大小和最大线程数 |
| **实时数据处理** | 对延迟敏感，需要快速响应 | 保持足够的核心线程数，根据系统负载调整最大线程数 |
| **微服务架构** | 服务间依赖复杂，负载不均衡 | 为每个服务设置独立的动态线程池，根据服务负载调整参数 |

**5. 参考链接**

- **官方文档**：
  - [Java ThreadPoolExecutor 文档](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html)
  - [Java 并发编程官方教程](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html)

- **开源实现**：
  - [Dynamic Thread Pool](https://github.com/dromara/dynamic-tp) - 轻量级动态线程池框架
  - [Hystrix ThreadPool](https://github.com/Netflix/Hystrix) - Netflix 的熔断框架中的线程池实现
  - [Resilience4j ThreadPoolBulkhead](https://github.com/resilience4j/resilience4j) - 弹性4j中的线程池隔离实现

- **技术博客**：
  - [美团技术团队：动态线程池在美团的实践](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)
  - [阿里巴巴技术团队：Java线程池的设计与实现](https://www.aliyun.com/article/734234)
  - [字节跳动技术团队：线程池最佳实践](https://bytedance.larkoffice.com/docx/DQzVdVjWbo60TxxUaL6cQVczn9c)

- **书籍**：
  - 《Java 并发编程实战》
  - 《Java 并发编程艺术》
  - 《实战 Java 高并发程序设计》

通过使用动态线程池，可以根据系统的实际运行情况和业务需求，灵活调整线程池参数，提高系统的资源利用率和响应速度，同时避免资源浪费和系统过载。



## 第三部分：Java 并发工具类

### 3.1 Lock 接口

#### 3.1.1 Lock 接口的基本概念

Lock 接口是 Java 5 引入的，它提供了比 synchronized 更灵活的锁机制。Lock 接口允许更细粒度的线程控制，支持尝试获取锁、可中断的锁获取、超时获取锁等特性。

#### 3.1.2 Lock 接口的核心方法

- **void lock()** - 获取锁，阻塞直到获取到锁
- **void lockInterruptibly()** - 获取锁，但允许被中断
- **boolean tryLock()** - 尝试获取锁，立即返回，不阻塞
- **boolean tryLock(long time, TimeUnit unit)** - 尝试获取锁，最多等待指定时间
- **void unlock()** - 释放锁
- **Condition newCondition()** - 创建条件对象

#### 3.1.3 Lock 接口的实现类

- **ReentrantLock** - 可重入锁
- **ReadWriteLock** - 读写锁
- **StampedLock** - 乐观读锁

#### 3.1.4 Lock 接口的使用场景

| 场景 | 推荐使用的 Lock 实现 | 原因 |
|------|-------------------|------|
| 一般同步场景 | ReentrantLock | 提供与 synchronized 类似的功能，但更灵活 |
| 读多写少场景 | ReadWriteLock | 允许多个线程同时读，提高并发性能 |
| 高并发读场景 | StampedLock | 提供乐观读模式，进一步提高读性能 |
| 需要可中断锁的场景 | ReentrantLock | 支持 lockInterruptibly() 方法 |
| 需要超时获取锁的场景 | ReentrantLock | 支持 tryLock(long, TimeUnit) 方法 |
| 需要公平锁的场景 | ReentrantLock | 构造函数可指定公平性 |
| 需要多个条件变量的场景 | ReentrantLock | 支持多个 Condition 对象 |

#### 3.1.5 ReentrantLock 详细说明

##### 3.1.5.1 基本原理

ReentrantLock 是一种可重入的独占锁，它基于 AQS 实现，通过状态变量 state 来表示锁的持有情况：
- 当 state = 0 时，表示锁未被持有
- 当 state > 0 时，表示锁被持有，state 的值表示重入次数

##### 3.1.5.2 核心过程

**获取锁过程**：
1. 调用 lock() 方法
2. 检查 state 是否为 0
3. 如果 state = 0，尝试通过 CAS 将 state 设置为 1
4. 如果 CAS 成功，设置当前线程为锁的持有者
5. 如果 state > 0，检查当前线程是否为锁的持有者
6. 如果是，将 state 加 1（重入）
7. 如果不是，将线程加入 AQS 等待队列并阻塞

**释放锁过程**：
1. 调用 unlock() 方法
2. 检查当前线程是否为锁的持有者
3. 如果是，将 state 减 1
4. 如果 state 减到 0，设置锁的持有者为 null，并唤醒 AQS 等待队列中的线程
5. 如果 state 不为 0，直接返回（仍持有锁）

##### 3.1.5.3 优缺点

**优点**：
- 支持可中断获取锁
- 支持超时获取锁
- 支持公平锁
- 支持多个条件变量
- 提供了更多的锁操作信息

**缺点**：
- 需要手动释放锁，容易遗漏 unlock() 调用
- 比 synchronized 稍复杂，需要更多的代码

##### 3.1.5.4 注意事项

1. **必须在 finally 块中释放锁**：确保即使发生异常，锁也能被正确释放
2. **避免死锁**：不要在持有锁的同时获取其他锁，或者确保所有线程按相同的顺序获取锁
3. **公平性选择**：公平锁会降低性能，只有在确实需要公平性时才使用
4. **重入次数控制**：避免过多的重入，可能导致栈溢出
5. **线程中断处理**：使用 lockInterruptibly() 时，需要处理 InterruptedException

##### 3.1.5.5 样例代码

**基本用法**：

```java
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    private final ReentrantLock lock = new ReentrantLock();
    private int counter = 0;
    
    public void increment() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }
    
    public int getCounter() {
        lock.lock();
        try {
            return counter;
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        
        // 创建多个线程测试
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    demo.increment();
                }
            }).start();
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Counter: " + demo.getCounter());
    }
}
```

**使用条件变量**：

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final Object[] buffer = new Object[10];
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;
    
    public void put(Object item) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length) {
                notFull.await(); // 缓冲区满，等待
            }
            buffer[putIndex] = item;
            putIndex = (putIndex + 1) % buffer.length;
            count++;
            notEmpty.signal(); // 通知消费者缓冲区非空
        } finally {
            lock.unlock();
        }
    }
    
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await(); // 缓冲区空，等待
            }
            Object item = buffer[takeIndex];
            takeIndex = (takeIndex + 1) % buffer.length;
            count--;
            notFull.signal(); // 通知生产者缓冲区非满
            return item;
        } finally {
            lock.unlock();
        }
    }
}
```

#### 3.1.6 ReadWriteLock 详细说明

##### 3.1.6.1 基本概念

ReadWriteLock 是一种读写分离的锁，它允许多个线程同时读，但只允许一个线程写。这种锁适用于读多写少的场景，可以提高并发性能。

##### 3.1.6.2 核心原理

ReadWriteLock 维护了一对锁：
- **读锁**：允许多个线程同时获取
- **写锁**：只允许一个线程获取

**获取规则**：
- 读锁：当没有线程持有写锁时，可以获取
- 写锁：当没有线程持有读锁或写锁时，可以获取

##### 3.1.6.3 优缺点

**优点**：
- 提高读多写少场景的并发性能
- 读写分离，逻辑清晰

**缺点**：
- 实现复杂，开销较大
- 在写操作频繁的场景，可能比独占锁性能更差

##### 3.1.6.4 注意事项

1. **读写锁的升级和降级**：
   - 不支持从读锁升级到写锁（可能导致死锁）
   - 支持从写锁降级到读锁

2. **锁的释放顺序**：
   - 必须先释放写锁，再释放读锁

3. **性能考虑**：
   - 只有在读操作远多于写操作时才使用
   - 写操作频繁时，读写锁可能比独占锁更慢

##### 3.1.6.5 样例代码

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    private int data = 0;
    
    public void read() {
        readLock.lock();
        try {
            System.out.println("Read data: " + data);
            try {
                Thread.sleep(100); // 模拟读操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            readLock.unlock();
        }
    }
    
    public void write(int newValue) {
        writeLock.lock();
        try {
            data = newValue;
            System.out.println("Write data: " + data);
            try {
                Thread.sleep(100); // 模拟写操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        
        // 创建多个读线程
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    demo.read();
                }
            }).start();
        }
        
        // 创建写线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                demo.write(i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
```

#### 3.1.7 StampedLock 详细说明

##### 3.1.7.1 基本概念

StampedLock 是 Java 8 引入的一种新型锁，它提供了乐观读模式，可以进一步提高读多写少场景的并发性能。StampedLock 的设计目的是在高并发读场景下，比 ReadWriteLock 有更好的性能。

##### 3.1.7.2 核心原理

StampedLock 使用一个长整型的 stamp 来表示锁的状态：
- **读锁**：通过 readLock() 获取，返回一个 stamp
- **写锁**：通过 writeLock() 获取，返回一个 stamp
- **乐观读**：通过 tryOptimisticRead() 获取，返回一个 stamp

**乐观读流程**：
1. 获取乐观读 stamp
2. 读取数据
3. 通过 validate(stamp) 验证 stamp 是否有效
4. 如果有效，直接返回数据
5. 如果无效，升级为悲观读锁或写锁

##### 3.1.7.3 优缺点

**优点**：
- 提供乐观读模式，提高读性能
- 支持锁的升级和降级
- 比 ReadWriteLock 有更好的并发性能

**缺点**：
- 不支持可重入
- 用法复杂，容易出错
- 不支持条件变量

##### 3.1.7.4 注意事项

1. **不支持可重入**：同一个线程不能多次获取同一把锁
2. **必须使用 stamp**：所有锁操作都需要使用 stamp
3. **乐观读验证**：必须在使用乐观读获取的数据前验证 stamp
4. **异常处理**：需要在 finally 块中释放锁
5. **性能考虑**：只在高并发读场景下使用

##### 3.1.7.5 样例代码

```java
import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo {
    private final StampedLock lock = new StampedLock();
    private double x = 0.0;
    private double y = 0.0;
    
    // 写操作
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    // 读操作（使用乐观读）
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();
        double currentX = x;
        double currentY = y;
        
        // 验证乐观读是否有效
        if (!lock.validate(stamp)) {
            // 乐观读无效，升级为悲观读锁
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
    
    // 读操作（使用悲观读）
    public double[] getPosition() {
        long stamp = lock.readLock();
        try {
            return new double[] { x, y };
        } finally {
            lock.unlockRead(stamp);
        }
    }
    
    public static void main(String[] args) {
        StampedLockDemo demo = new StampedLockDemo();
        
        // 创建多个读线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    System.out.println("Distance: " + demo.distanceFromOrigin());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        
        // 创建写线程
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                demo.move(1.0, 1.0);
                System.out.println("Moved to: " + demo.getPosition()[0] + ", " + demo.getPosition()[1]);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
```

### 3.2 同步工具类

#### 3.2.1 CountDownLatch

##### 3.2.1.1 基本概念

CountDownLatch 是一种同步工具，它允许一个或多个线程等待其他线程完成操作。CountDownLatch 通过一个计数器实现，计数器初始化为一个正整数，当线程完成操作时，计数器减 1，当计数器减到 0 时，所有等待的线程被唤醒。

##### 3.2.1.2 核心原理

CountDownLatch 基于 AQS 实现，使用 state 字段作为计数器：
- 当线程调用 await() 方法时，如果 state > 0，线程会被加入 AQS 等待队列并阻塞
- 当线程调用 countDown() 方法时，state 减 1，如果 state == 0，唤醒所有等待的线程

##### 3.2.1.3 核心方法

- **CountDownLatch(int count)** - 创建一个计数器初始化为 count 的 CountDownLatch
- **void await()** - 等待计数器减到 0，阻塞直到条件满足
- **boolean await(long timeout, TimeUnit unit)** - 等待计数器减到 0，最多等待指定时间
- **void countDown()** - 计数器减 1
- **long getCount()** - 获取当前计数器的值

##### 3.2.1.4 使用场景

| 场景 | 示例 | 原因 |
|------|------|------|
| 等待多个线程完成初始化 | 主线程等待所有工作线程初始化完成 | 确保所有线程都准备就绪后再开始工作 |
| 等待多个任务完成 | 主线程等待所有子任务完成 | 确保所有任务都完成后再汇总结果 |
| 限制线程执行顺序 | 线程 A 等待线程 B 完成某个操作 | 确保操作的顺序执行 |
| 模拟并发测试 | 多个线程同时开始执行 | 确保测试的并发性能 |

##### 3.2.1.5 优缺点

**优点**：
- 简单易用，功能明确
- 支持超时等待
- 可以等待多个线程完成

**缺点**：
- 计数器只能使用一次，不能重置
- 不支持线程间的交互，只是简单的等待

##### 3.2.1.6 注意事项

1. **计数器初始化**：计数器的初始值应等于需要等待的事件数量
2. **countDown() 调用**：确保每个事件完成后都调用 countDown()，否则可能导致线程永久阻塞
3. **超时处理**：使用 await(long, TimeUnit) 时，需要处理超时情况
4. **异常处理**：在 countDown() 调用处需要处理异常，确保即使发生异常也能正确计数
5. **内存可见性**：CountDownLatch 可以保证线程间的内存可见性

##### 3.2.1.7 样例代码

**基本用法**：

```java
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        // 计数器初始化为 3
        CountDownLatch latch = new CountDownLatch(3);
        
        // 创建 3 个线程
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " is working");
                    Thread.sleep(1000);
                    System.out.println("Thread " + threadId + " has finished");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 计数器减 1
                    latch.countDown();
                    System.out.println("Countdown: " + latch.getCount());
                }
            }).start();
        }
        
        System.out.println("Main thread is waiting for all threads to finish");
        // 等待计数器减到 0
        latch.await();
        System.out.println("All threads have finished, main thread continues");
    }
}
```

**使用超时等待**：

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchWithTimeoutDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        
        // 创建 2 个线程，故意少一个
        for (int i = 0; i < 2; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " is working");
                    Thread.sleep(500);
                    System.out.println("Thread " + threadId + " has finished");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        
        System.out.println("Main thread is waiting for all threads to finish (with timeout)");
        // 等待最多 1 秒
        boolean completed = latch.await(1, TimeUnit.SECONDS);
        if (completed) {
            System.out.println("All threads have finished, main thread continues");
        } else {
            System.out.println("Timeout: Not all threads have finished, main thread continues");
        }
    }
}
```

#### 3.2.2 CyclicBarrier

##### 3.2.2.1 基本概念

CyclicBarrier 是一种同步工具，它允许一组线程互相等待，直到达到某个公共屏障点。与 CountDownLatch 不同，CyclicBarrier 的计数器可以重置，因此可以重复使用。

##### 3.2.2.2 核心原理

CyclicBarrier 基于 ReentrantLock 和 Condition 实现：
- 内部维护一个计数器和一个 Condition 对象
- 当线程调用 await() 方法时，计数器减 1
- 如果计数器减到 0，唤醒所有等待的线程，并重置计数器（如果需要）
- 如果计数器不为 0，线程会被加入 Condition 等待队列并阻塞

##### 3.2.2.3 核心方法

- **CyclicBarrier(int parties)** - 创建一个 parties 个线程的 CyclicBarrier
- **CyclicBarrier(int parties, Runnable barrierAction)** - 创建一个 parties 个线程的 CyclicBarrier，并指定屏障动作
- **int await()** - 等待所有线程到达屏障点，阻塞直到条件满足
- **int await(long timeout, TimeUnit unit)** - 等待所有线程到达屏障点，最多等待指定时间
- **int getParties()** - 获取参与的线程数
- **int getNumberWaiting()** - 获取当前等待的线程数
- **boolean isBroken()** - 检查屏障是否被打破
- **void reset()** - 重置屏障

##### 3.2.2.4 使用场景

| 场景 | 示例 | 原因 |
|------|------|------|
| 并行计算 | 多个线程分别计算不同部分，然后汇总结果 | 确保所有计算都完成后再汇总 |
| 数据处理 pipeline | 多个线程按阶段处理数据，每个阶段都需要等待所有线程完成 | 确保数据处理的顺序执行 |
| 游戏开发 | 多个玩家准备就绪后开始游戏 | 确保所有玩家都准备好后再开始 |
| 测试场景 | 多个线程同时执行测试用例 | 确保测试的一致性 |

##### 3.2.2.5 优缺点

**优点**：
- 计数器可以重置，支持重复使用
- 支持屏障动作，当所有线程到达屏障点时执行
- 功能更丰富，支持更多的操作

**缺点**：
- 实现复杂，开销较大
- 只适用于固定数量的线程

##### 3.2.2.6 注意事项

1. **线程数量**：参与的线程数必须等于 parties，否则可能导致线程永久阻塞
2. **异常处理**：如果某个线程在等待过程中被中断或超时，屏障会被打破，所有等待的线程会被唤醒并抛出 BrokenBarrierException
3. **重置操作**：reset() 方法会唤醒所有等待的线程并抛出 BrokenBarrierException，需要谨慎使用
4. **内存可见性**：CyclicBarrier 可以保证线程间的内存可见性

##### 3.2.2.7 样例代码

**基本用法**：

```java
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        // 创建一个 3 个线程的 CyclicBarrier
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("All threads have reached the barrier, barrier action executed");
        });
        
        // 创建 3 个线程
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " is working");
                    Thread.sleep(1000);
                    System.out.println("Thread " + threadId + " is waiting at the barrier");
                    // 等待所有线程到达屏障点
                    barrier.await();
                    System.out.println("Thread " + threadId + " continues after the barrier");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

**重复使用**：

```java
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierReuseDemo {
    public static void main(String[] args) {
        // 创建一个 2 个线程的 CyclicBarrier
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            System.out.println("Barrier action executed");
        });
        
        // 创建 2 个线程，重复使用 CyclicBarrier
        for (int i = 0; i < 2; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    for (int round = 0; round < 3; round++) {
                        System.out.println("Thread " + threadId + " working on round " + round);
                        Thread.sleep(500);
                        System.out.println("Thread " + threadId + " waiting at barrier for round " + round);
                        // 等待所有线程到达屏障点
                        barrier.await();
                        System.out.println("Thread " + threadId + " continues after barrier for round " + round);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

#### 3.2.3 Semaphore

##### 3.2.3.1 基本概念

Semaphore 是一种同步工具，它用于控制对共享资源的访问数量。Semaphore 通过一个计数器实现，计数器表示可用资源的数量，当线程获取资源时，计数器减 1，当线程释放资源时，计数器加 1。

##### 3.2.3.2 核心原理

Semaphore 基于 AQS 实现，使用 state 字段作为计数器：
- 当线程调用 acquire() 方法时，如果 state > 0，state 减 1 并获取资源；否则，线程会被加入 AQS 等待队列并阻塞
- 当线程调用 release() 方法时，state 加 1，如果有等待的线程，唤醒其中一个

##### 3.2.3.3 核心方法

- **Semaphore(int permits)** - 创建一个许可证数量为 permits 的 Semaphore
- **Semaphore(int permits, boolean fair)** - 创建一个许可证数量为 permits 的 Semaphore，并指定公平性
- **void acquire()** - 获取一个许可证，阻塞直到获取到
- **void acquire(int permits)** - 获取 permits 个许可证，阻塞直到获取到
- **boolean tryAcquire()** - 尝试获取一个许可证，立即返回，不阻塞
- **boolean tryAcquire(int permits)** - 尝试获取 permits 个许可证，立即返回，不阻塞
- **boolean tryAcquire(long timeout, TimeUnit unit)** - 尝试获取一个许可证，最多等待指定时间
- **boolean tryAcquire(int permits, long timeout, TimeUnit unit)** - 尝试获取 permits 个许可证，最多等待指定时间
- **void release()** - 释放一个许可证
- **void release(int permits)** - 释放 permits 个许可证
- **int availablePermits()** - 获取当前可用的许可证数量
- **int drainPermits()** - 获取并返回所有可用的许可证
- **boolean isFair()** - 检查是否为公平模式
- **boolean hasQueuedThreads()** - 检查是否有线程在等待
- **int getQueueLength()** - 获取等待线程的数量

##### 3.2.3.4 使用场景

| 场景 | 示例 | 原因 |
|------|------|------|
| 限流 | 限制同时访问某个服务的请求数 | 防止服务过载 |
| 资源池管理 | 限制同时使用数据库连接的数量 | 合理利用资源 |
| 并发控制 | 限制同时执行某个操作的线程数 | 避免竞态条件 |
| 生产者-消费者模式 | 限制生产者和消费者的数量 | 平衡生产和消费速度 |

##### 3.2.3.5 优缺点

**优点**：
- 灵活控制资源访问数量
- 支持公平和非公平模式
- 支持获取和释放多个许可证
- 功能丰富，支持多种获取方式

**缺点**：
- 需要手动释放许可证，容易遗漏
- 公平模式会降低性能

##### 3.2.3.6 注意事项

1. **许可证释放**：必须确保获取的许可证都能被释放，否则可能导致资源耗尽
2. **异常处理**：在 finally 块中释放许可证，确保即使发生异常也能正确释放
3. **公平性选择**：公平模式会降低性能，只有在确实需要公平性时才使用
4. **许可证数量**：根据实际资源数量和并发需求设置合适的许可证数量
5. **线程中断**：使用 acquire() 时，需要处理 InterruptedException

##### 3.2.3.7 样例代码

**基本用法**：

```java
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public static void main(String[] args) {
        // 创建一个许可证数量为 2 的 Semaphore
        Semaphore semaphore = new Semaphore(2);
        
        // 创建 5 个线程
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " is waiting for a permit");
                    // 获取许可证
                    semaphore.acquire();
                    System.out.println("Thread " + threadId + " has acquired a permit");
                    // 模拟使用资源
                    Thread.sleep(1000);
                    System.out.println("Thread " + threadId + " is releasing a permit");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放许可证
                    semaphore.release();
                }
            }).start();
        }
    }
}
```

**资源池管理**：

```java
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ResourcePoolDemo {
    private final Semaphore semaphore;
    private final AtomicInteger availableResources;
    private final int maxResources;
    
    public ResourcePoolDemo(int maxResources) {
        this.maxResources = maxResources;
        this.semaphore = new Semaphore(maxResources);
        this.availableResources = new AtomicInteger(maxResources);
    }
    
    public void acquireResource() throws InterruptedException {
        semaphore.acquire();
        availableResources.decrementAndGet();
        System.out.println("Resource acquired. Available: " + availableResources.get());
    }
    
    public void releaseResource() {
        semaphore.release();
        availableResources.incrementAndGet();
        System.out.println("Resource released. Available: " + availableResources.get());
    }
    
    public static void main(String[] args) {
        ResourcePoolDemo pool = new ResourcePoolDemo(3);
        
        // 创建 10 个线程
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " is trying to acquire a resource");
                    pool.acquireResource();
                    // 模拟使用资源
                    Thread.sleep(500);
                    System.out.println("Thread " + threadId + " is releasing a resource");
                    pool.releaseResource();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

#### 3.2.4 Exchanger

##### 3.2.4.1 基本概念

Exchanger 是一种同步工具，它允许两个线程在指定点交换数据。Exchanger 提供了一个同步点，两个线程可以在此交换彼此的数据。

##### 3.2.4.2 核心原理

Exchanger 基于 AQS 实现，使用一个特殊的节点结构来存储线程和数据：
- 当线程调用 exchange() 方法时，会创建一个节点存储自己和数据
- 如果没有其他线程在等待交换，线程会被阻塞
- 如果有其他线程在等待交换，会唤醒该线程并交换数据

##### 3.2.4.3 核心方法

- **Exchanger()** - 创建一个 Exchanger
- **V exchange(V x)** - 交换数据，阻塞直到另一个线程到达交换点
- **V exchange(V x, long timeout, TimeUnit unit)** - 交换数据，最多等待指定时间

##### 3.2.4.4 使用场景

| 场景 | 示例 | 原因 |
|------|------|------|
| 生产者-消费者模式 | 生产者和消费者交换数据 | 简化数据交换逻辑 |
| 数据校对 | 两个线程分别处理数据，然后交换结果进行校对 | 方便结果比较 |
| 管道通信 | 两个线程通过 Exchanger 传递数据 | 简化线程间通信 |
| 游戏开发 | 两个玩家交换游戏状态 | 同步游戏状态 |

##### 3.2.4.5 优缺点

**优点**：
- 简化两个线程间的数据交换
- 支持超时设置
- 实现简单，使用方便

**缺点**：
- 只支持两个线程间的交换
- 交换过程中如果一个线程异常，可能导致另一个线程永久阻塞

##### 3.2.4.6 注意事项

1. **线程数量**：Exchanger 只适用于两个线程间的交换
2. **超时处理**：使用带超时的 exchange() 方法，避免永久阻塞
3. **异常处理**：在 exchange() 调用处需要处理异常，确保线程不会意外终止
4. **内存可见性**：Exchanger 可以保证线程间的内存可见性

##### 3.2.4.7 样例代码

**基本用法**：

```java
import java.util.concurrent.Exchanger;

public class ExchangerDemo {
    public static void main(String[] args) {
        // 创建一个 Exchanger
        Exchanger<String> exchanger = new Exchanger<>();
        
        // 创建线程 A
        new Thread(() -> {
            try {
                String data = "Data from Thread A";
                System.out.println("Thread A is sending: " + data);
                // 交换数据
                String receivedData = exchanger.exchange(data);
                System.out.println("Thread A received: " + receivedData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        // 创建线程 B
        new Thread(() -> {
            try {
                String data = "Data from Thread B";
                System.out.println("Thread B is sending: " + data);
                // 交换数据
                String receivedData = exchanger.exchange(data);
                System.out.println("Thread B received: " + receivedData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
```

**使用超时**：

```java
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ExchangerWithTimeoutDemo {
    public static void main(String[] args) {
        // 创建一个 Exchanger
        Exchanger<String> exchanger = new Exchanger<>();
        
        // 创建线程 A
        new Thread(() -> {
            try {
                String data = "Data from Thread A";
                System.out.println("Thread A is sending: " + data);
                // 交换数据，最多等待 1 秒
                String receivedData = exchanger.exchange(data, 1, TimeUnit.SECONDS);
                System.out.println("Thread A received: " + receivedData);
            } catch (Exception e) {
                System.out.println("Thread A exchange failed: " + e.getMessage());
            }
        }).start();
        
        // 线程 B 不执行 exchange
        System.out.println("Thread B is not sending data");
    }
}
```

### 3.3 并发集合

#### 3.3.1 ConcurrentHashMap

**ConcurrentHashMap 数据结构图**

```
+================================+
|        ConcurrentHashMap       |
+================================+
| - sizeCtl: int (控制表初始化和扩容)|
| - table: Node<K,V>[] (哈希表数组)|
| - nextTable: Node<K,V>[] (扩容时的新表)|
+================================+
                |
                v
+================================+
|        哈希表数组 (table)       |
+================================+
| 0: Node → Node → Node          |
| 1: Node                        |
| 2: ForwardingNode (扩容时使用)  |
| 3: Node → Node                 |
| 4: TreeNode → TreeNode (红黑树) |
| ...                            |
+================================+
```

**ConcurrentHashMap put 操作流程图**

```
开始 → 计算哈希值 → 检查哈希表是否初始化 → 未初始化？
    ├─ 是 → 初始化哈希表 → 继续
    └─ 否 → 继续
→ 定位桶位置 → 桶为空？
    ├─ 是 → CAS 插入新节点 → 结束
    └─ 否 → 检查桶头节点类型 → 是 ForwardingNode？
        ├─ 是 → 帮助扩容 → 重试
        └─ 否 → 是 TreeNode？
            ├─ 是 → 红黑树插入
            └─ 否 → 链表插入 → 检查是否需要转换为红黑树
→ 检查是否需要扩容 → 需要？
    ├─ 是 → 触发扩容
    └─ 否 → 结束
```

**ConcurrentHashMap get 操作流程图**

```
开始 → 计算哈希值 → 检查哈希表是否初始化 → 未初始化？
    ├─ 是 → 返回 null
    └─ 否 → 继续
→ 定位桶位置 → 桶为空？
    ├─ 是 → 返回 null
    └─ 否 → 检查桶头节点 → 哈希值匹配？
        ├─ 是 → 返回节点值
        └─ 否 → 是 TreeNode？
            ├─ 是 → 红黑树查找
            └─ 否 → 链表查找 → 找到？
                ├─ 是 → 返回节点值
                └─ 否 → 返回 null
```

**ConcurrentHashMap 特点**

ConcurrentHashMap 是一种线程安全的哈希表，它支持高并发的读写操作，主要特点包括：

- **分段锁**：在 Java 8 之前使用分段锁，Java 8 之后使用 CAS + synchronized
- **无锁读**：读操作不需要加锁，通过 volatile 保证可见性
- **红黑树**：当链表长度超过阈值时，会转换为红黑树
- **扩容优化**：支持多线程同时扩容，提高扩容效率
- **高并发**：在保证线程安全的同时，提供了较高的并发性能

**代码示例**

```java
// 创建 ConcurrentHashMap
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// put 操作
map.put("key1", 1);
map.put("key2", 2);

// get 操作
Integer value = map.get("key1");

// 遍历
map.forEach((k, v) -> System.out.println(k + ": " + v));

// 原子操作
map.computeIfAbsent("key3", k -> 3);
map.computeIfPresent("key1", (k, v) -> v + 1);
```

参考链接 

[Java 8系列之重新认识HashMap](https://tech.meituan.com/2016/06/24/java-hashmap.html)



#### 3.3.2 CopyOnWriteArrayList

CopyOnWriteArrayList 是一种线程安全的列表，它通过复制底层数组来实现线程安全。

#### 3.3.3 BlockingQueue

BlockingQueue 是一种阻塞队列，它支持在队列为空时阻塞获取操作，在队列满时阻塞插入操作。

### 3.4 LockSupport

#### 3.4.1 LockSupport 的基本概念

LockSupport 是一种线程阻塞工具，它可以在任意位置阻塞线程，并且可以唤醒指定的线程。

#### 3.4.2 LockSupport 的数据结构

**LockSupport 内部数据结构图**

```
+================================+
|        LockSupport             |
+================================+
| - Unsafe: sun.misc.Unsafe (底层实现)|
+================================+
                |
                v
+================================+
|        线程 park 状态管理        |
+================================+
| 线程1: _counter = 1 (可运行)     |
| 线程2: _counter = 0 (已阻塞)     |
| 线程3: _counter = 1 (可运行)     |
| ...                            |
+================================+
```

**说明**：
- LockSupport 内部使用 Unsafe 类实现线程的阻塞和唤醒
- 每个线程都有一个 _counter 变量，用于表示线程的 park 状态
- 当 _counter = 0 时，线程可以被 park
- 当 _counter = 1 时，线程不可被 park（或者说 park 会立即返回）

#### 3.4.3 LockSupport 的方法

- **park()** - 阻塞当前线程
- **parkNanos(long nanos)** - 阻塞当前线程指定时间
- **parkUntil(long deadline)** - 阻塞当前线程直到指定时间
- **unpark(Thread thread)** - 唤醒指定线程

#### 3.4.4 LockSupport 操作流程图

**park 操作流程**

```
开始 → 调用 park() → 检查线程的 _counter 值 → _counter == 1？
    ├─ 是 → 设置 _counter = 0 → 返回（不阻塞）
    └─ 否 → 阻塞线程 → 被唤醒或中断 → 设置 _counter = 0 → 返回
```

**unpark 操作流程**

```
开始 → 调用 unpark(thread) → 设置线程的 _counter = 1 → 检查线程是否阻塞 → 是？
    ├─ 是 → 唤醒线程
    └─ 否 → 不做操作
→ 结束
```

#### 3.4.5 LockSupport 与 wait/notify 的区别

- **LockSupport 不需要获取对象锁** - wait/notify 需要在同步块中调用
- **LockSupport 可以唤醒指定线程** - notify 只能随机唤醒一个线程
- **LockSupport 不会产生死锁** - 如果先调用 unpark 再调用 park，线程不会阻塞
- **LockSupport 支持中断响应** - park 方法会响应线程中断，但不会抛出 InterruptedException
- **LockSupport 可以设置超时** - 提供 parkNanos 和 parkUntil 方法支持超时阻塞

**代码示例**

```java
public class LockSupportDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("线程开始执行");
            System.out.println("线程准备阻塞");
            LockSupport.park(); // 阻塞线程
            System.out.println("线程被唤醒");
        });
        
        thread.start();
        Thread.sleep(1000); // 等待 1 秒
        System.out.println("主线程准备唤醒子线程");
        LockSupport.unpark(thread); // 唤醒线程
    }
}
```

### 3.5 Thread 类相关内容

#### 3.5.1 线程对象的常用属性

##### 3.5.1.1 基本属性

| 属性 | 类型 | 描述 | 示例 |
|------|------|------|------|
| **id** | long | 线程的唯一标识符 | `Thread.currentThread().getId()` |
| **name** | String | 线程的名称 | `Thread.currentThread().getName()` |
| **priority** | int | 线程的优先级（1-10） | `Thread.currentThread().getPriority()` |
| **state** | Thread.State | 线程的状态 | `Thread.currentThread().getState()` |
| **isDaemon** | boolean | 是否为守护线程 | `Thread.currentThread().isDaemon()` |
| **isAlive** | boolean | 线程是否存活 | `Thread.currentThread().isAlive()` |
| **isInterrupted** | boolean | 线程是否被中断 | `Thread.currentThread().isInterrupted()` |

##### 3.5.1.2 线程状态

Java 线程有 6 种状态：

| 状态 | 描述 |
|------|------|
| **NEW** | 线程已创建但尚未启动 |
| **RUNNABLE** | 线程正在运行或准备运行 |
| **BLOCKED** | 线程被阻塞，等待锁 |
| **WAITING** | 线程无限期等待 |
| **TIMED_WAITING** | 线程有限期等待 |
| **TERMINATED** | 线程已终止 |

##### 3.5.1.3 核心方法

- **Thread(String name)** - 创建一个指定名称的线程
- **Thread(Runnable target)** - 创建一个执行指定任务的线程
- **Thread(Runnable target, String name)** - 创建一个执行指定任务并指定名称的线程
- **void start()** - 启动线程
- **void run()** - 线程的执行方法
- **void sleep(long millis)** - 线程睡眠指定时间
- **void sleep(long millis, int nanos)** - 线程睡眠指定时间（精确到纳秒）
- **void join()** - 等待线程终止
- **void join(long millis)** - 等待线程终止，最多等待指定时间
- **void join(long millis, int nanos)** - 等待线程终止，最多等待指定时间（精确到纳秒）
- **void interrupt()** - 中断线程
- **boolean isInterrupted()** - 检查线程是否被中断
- **static boolean interrupted()** - 检查当前线程是否被中断，并清除中断状态
- **static Thread currentThread()** - 获取当前线程
- **static void yield()** - 线程礼让
- **void setPriority(int newPriority)** - 设置线程优先级
- **int getPriority()** - 获取线程优先级
- **void setDaemon(boolean on)** - 设置线程为守护线程
- **boolean isDaemon()** - 检查线程是否为守护线程
- **long getId()** - 获取线程 ID
- **String getName()** - 获取线程名称
- **void setName(String name)** - 设置线程名称
- **Thread.State getState()** - 获取线程状态
- **boolean isAlive()** - 检查线程是否存活

#### 3.5.2 线程对象的使用样例

##### 3.5.2.1 基本使用

```java
public class ThreadBasicDemo {
    public static void main(String[] args) {
        // 创建线程
        Thread thread = new Thread(() -> {
            System.out.println("Thread name: " + Thread.currentThread().getName());
            System.out.println("Thread ID: " + Thread.currentThread().getId());
            System.out.println("Thread priority: " + Thread.currentThread().getPriority());
            System.out.println("Thread state: " + Thread.currentThread().getState());
            System.out.println("Is daemon: " + Thread.currentThread().isDaemon());
            System.out.println("Is alive: " + Thread.currentThread().isAlive());
        }, "DemoThread");
        
        // 启动线程
        thread.start();
        
        // 等待线程终止
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Thread state after termination: " + thread.getState());
        System.out.println("Is alive after termination: " + thread.isAlive());
    }
}
```

##### 3.5.2.2 线程优先级

```java
public class ThreadPriorityDemo {
    public static void main(String[] args) {
        // 创建低优先级线程
        Thread lowPriorityThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Low priority thread running: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "LowPriorityThread");
        
        // 创建高优先级线程
        Thread highPriorityThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("High priority thread running: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "HighPriorityThread");
        
        // 设置优先级
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY); // 1
        highPriorityThread.setPriority(Thread.MAX_PRIORITY); // 10
        
        // 启动线程
        lowPriorityThread.start();
        highPriorityThread.start();
    }
}
```

##### 3.5.2.3 守护线程

```java
public class DaemonThreadDemo {
    public static void main(String[] args) {
        // 创建守护线程
        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("Daemon thread is running");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "DaemonThread");
        
        // 设置为守护线程
        daemonThread.setDaemon(true);
        
        // 启动守护线程
        daemonThread.start();
        
        // 主线程睡眠 2 秒后退出
        System.out.println("Main thread is running");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread exiting");
        // 主线程退出后，守护线程也会退出
    }
}
```

##### 3.5.2.4 线程中断

```java
public class ThreadInterruptDemo {
    public static void main(String[] args) {
        // 创建一个可中断的线程
        Thread interruptibleThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Thread is running");
                    Thread.sleep(1000);
                }
                System.out.println("Thread was interrupted");
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted while sleeping");
                // 重新设置中断状态
                Thread.currentThread().interrupt();
            }
        }, "InterruptibleThread");
        
        // 启动线程
        interruptibleThread.start();
        
        // 主线程睡眠 3 秒后中断线程
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Interrupting thread");
        interruptibleThread.interrupt();
        
        // 等待线程终止
        try {
            interruptibleThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread has terminated");
    }
}
```

#### 3.5.3 使用的注意事项

1. **线程创建和启动**：
   - 不要直接调用 run() 方法，应该调用 start() 方法启动线程
   - 一个线程只能启动一次，多次启动会抛出 IllegalThreadStateException

2. **线程优先级**：
   - 线程优先级只是一个提示，操作系统可能会忽略
   - 不要依赖线程优先级来控制程序的执行顺序
   - 合理设置优先级，避免优先级反转

3. **守护线程**：
   - 守护线程在主线程退出后会自动退出，不适合执行需要完成的任务
   - 守护线程中不要执行 IO 操作，可能会导致资源泄漏
   - 守护线程的优先级较低，可能会被系统忽略

4. **线程中断**：
   - 线程中断只是一个状态标记，不会立即终止线程
   - 在阻塞方法中，线程中断会抛出 InterruptedException
   - 捕获 InterruptedException 后，应该重新设置中断状态

5. **线程状态**：
   - 不要依赖线程状态来控制程序的执行逻辑
   - 线程状态会随着线程的执行而变化，可能不可预测

6. **线程安全**：
   - 多个线程访问共享资源时，需要保证线程安全
   - 合理使用同步机制，避免死锁和竞态条件

7. **资源管理**：
   - 线程执行完成后，会自动释放资源
   - 避免创建过多线程，可能导致系统资源耗尽
   - 使用线程池管理线程，提高资源利用率

8. **异常处理**：
   - 线程中的异常不会传播到主线程，需要在线程内部处理
   - 使用 UncaughtExceptionHandler 处理未捕获的异常

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

**线程池相关**
- [Java 线程池原理分析](https://juejin.cn/post/7303461209142099978)
- [美团技术团队：线程池实现原理及其在美团业务中的实践](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)
- [阿里巴巴技术团队：Java线程池的设计与实现](https://www.aliyun.com/article/734234)
- [字节跳动技术团队：线程池最佳实践](https://bytedance.larkoffice.com/docx/OBqfdI4d1o4rYjx3Jf3cVfczn5d)

**CAS 相关**
- [深入理解CAS原理](https://zhuanlan.zhihu.com/p/383674857)
- [阿里巴巴技术团队：CAS机制详解](https://www.aliyun.com/article/728321)
- [腾讯技术团队：CAS原理与ABA问题](https://cloud.tencent.com/developer/article/1707338)

**AQS 相关**
- [Java AQS原理深度解析](https://juejin.cn/post/6844908087458201614)
- [美团技术团队：AQS原理与应用](https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html)
- [百度技术团队：深入理解AQS框架](https://cloud.baidu.com/article/3156735)

**综合并发编程**
- [Java 并发编程指南](https://pdai.tech/md/java/thread/java-thread-x-overview.html)
- [Java 并发编程核心技术](https://zhuanlan.zhihu.com/p/571637324)
- [字节跳动技术团队：并发编程最佳实践](https://bytedance.larkoffice.com/docx/DQzVdVjWbo60TxxUaL6cQVczn9c)

### 6.4 视频教程

- [Java 并发编程实战 - B 站](https://www.bilibili.com/video/BV1mP4y1i7Vm/)
- [Java 并发编程核心技术 - B 站](https://www.bilibili.com/video/BV1E14y1E7Q4/)
- [Java 线程池原理与实战 - B 站](https://www.bilibili.com/video/BV1z44y1X7BJ/)

### 6.5 工具和框架

- [Disruptor](https://lmax-exchange.github.io/disruptor/)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [Project Loom](https://openjdk.org/projects/loom/)