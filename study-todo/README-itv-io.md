# IO 流与网络IO

## 目录

- [IO 流与网络IO](#io-流与网络io)
  - [目录](#目录)
  - [1. 基础概念](#1-基础概念)
  - [2. IO 流分类](#2-io-流分类)
  - [3. 字节流与字符流](#3-字节流与字符流)
    - [3.1 区别与联系](#31-区别与联系)
    - [3.2 常用类](#32-常用类)
  - [4. IO 模型](#4-io-模型)
    - [4.1 阻塞 IO](#41-阻塞-io)
    - [4.2 非阻塞 IO](#42-非阻塞-io)
    - [4.3 IO 多路复用](#43-io-多路复用)
    - [4.4 信号驱动式 IO](#44-信号驱动式-io)
    - [4.5 异步 IO](#45-异步-io)
  - [5. Java NIO](#5-java-nio)
    - [5.1 核心组件](#51-核心组件)
    - [5.2 Buffer](#52-buffer)
    - [5.3 Channel](#53-channel)
    - [5.4 Selector](#54-selector)
  - [6. Java AIO](#6-java-aio)
  - [7. Netty 简介](#7-netty-简介)
  - [8. 面试题解析](#8-面试题解析)
  - [9. 参考链接](#9-参考链接)

## 1. 基础概念

IO（Input/Output）是指计算机与外部设备之间的数据传输。在 Java 中，IO 操作主要分为两大类：

- **文件 IO**：处理文件的读写操作
- **网络 IO**：处理网络数据的传输

IO 操作是程序中非常重要的一部分，也是性能瓶颈的常见来源。了解不同的 IO 模型和技术，可以帮助我们编写更高效的程序。

## 2. IO 流分类

Java 中的 IO 流可以按照不同的标准进行分类：

| 分类标准 | 类型 | 说明 |
|---------|------|------|
| 数据方向 | 输入流 | 从外部设备读取数据到程序 |
|         | 输出流 | 从程序写入数据到外部设备 |
| 数据单位 | 字节流 | 以字节为单位传输数据（8位） |
|         | 字符流 | 以字符为单位传输数据（16位） |
| 功能 | 节点流 | 直接与数据源连接的流 |
|     | 处理流 | 对节点流进行包装，提供额外功能 |

## 3. 字节流与字符流

### 3.1 区别与联系

| 特性 | 字节流 | 字符流 |
|------|--------|--------|
| 数据单位 | 字节（8位） | 字符（16位） |
| 处理对象 | 二进制数据 | 文本数据 |
| 缓冲区 | 无内置缓冲区 | 有内置缓冲区 |
| 编码转换 | 需手动处理 | 自动处理编码转换 |
| 基类 | InputStream/OutputStream | Reader/Writer |

### 3.2 常用类

#### 字节流
- **FileInputStream/FileOutputStream**：文件字节流
- **BufferedInputStream/BufferedOutputStream**：缓冲字节流
- **DataInputStream/DataOutputStream**：数据字节流
- **ObjectInputStream/ObjectOutputStream**：对象字节流
- **ByteArrayInputStream/ByteArrayOutputStream**：字节数组流

#### 字符流
- **FileReader/FileWriter**：文件字符流
- **BufferedReader/BufferedWriter**：缓冲字符流
- **InputStreamReader/OutputStreamWriter**：字节流与字符流的转换
- **PrintWriter**：打印字符流
- **CharArrayReader/CharArrayWriter**：字符数组流

## 4. IO 模型

### 4.1 阻塞 IO

- **概念**：当应用程序发起 IO 请求后，如果数据未准备就绪，应用进程会一直阻塞等待，直到数据准备就绪并完成 IO 操作
- **优点**：实现简单，逻辑清晰
- **缺点**：每个连接需要一个线程，并发性能差
- **适用场景**：并发连接数较少的场景

### 4.2 非阻塞 IO

- **概念**：当应用程序发起 IO 请求后，如果数据未准备就绪，系统会立即返回一个错误，应用进程可以继续执行其他任务，然后通过轮询的方式检查数据是否准备就绪
- **优点**：一个线程可以处理多个连接
- **缺点**：轮询会消耗 CPU 资源
- **适用场景**：并发连接数较多，但数据传输量较小的场景

### 4.3 IO 多路复用

- **概念**：通过一个系统调用同时监控多个文件描述符，当其中任何一个文件描述符就绪时，系统会通知应用进程
- **优点**：一个线程可以处理多个连接，避免了轮询的 CPU 消耗
- **缺点**：实现相对复杂
- **适用场景**：高并发场景

#### 三种实现方式

| 实现方式 | 特点 | 缺点 |
|---------|------|------|
| select | 可监控最多 1024 个文件描述符，需要将文件描述符集合从用户空间拷贝到内核空间 | 性能开销大，文件描述符数量有限 |
| poll | 使用链表存储文件描述符，无数量限制 | 仍需要遍历所有文件描述符 |
| epoll | 使用红黑树存储文件描述符，使用事件通知机制 | 仅在 Linux 系统支持 |

### 4.4 信号驱动式 IO

- **概念**：应用进程通过系统调用注册信号处理函数，当数据准备就绪时，内核会发送信号通知应用进程
- **优点**：避免了轮询的 CPU 消耗
- **缺点**：信号处理可能会打断应用进程的正常执行
- **适用场景**：实时性要求较高的场景

### 4.5 异步 IO

- **概念**：应用进程发起 IO 请求后，立即返回，内核会在后台完成数据的准备和传输，完成后通过信号或回调通知应用进程
- **优点**：完全异步，应用进程无需等待
- **缺点**：实现复杂
- **适用场景**：高并发、大吞吐量的场景

## 5. Java NIO

Java NIO（New IO）是从 JDK 1.4 开始引入的新 IO 模型，提供了非阻塞 IO 操作的能力。

### 5.1 核心组件

- **Buffer**：缓冲区，用于存储数据
- **Channel**：通道，用于数据传输
- **Selector**：选择器，用于监控多个通道的事件

### 5.2 Buffer

Buffer 是一个抽象类，用于存储数据。常用的实现类包括：

- **ByteBuffer**：字节缓冲区
- **CharBuffer**：字符缓冲区
- **ShortBuffer**：短整型缓冲区
- **IntBuffer**：整型缓冲区
- **LongBuffer**：长整型缓冲区
- **FloatBuffer**：浮点型缓冲区
- **DoubleBuffer**：双精度浮点型缓冲区

#### 重要属性

| 属性 | 说明 |
|------|------|
| capacity | 缓冲区的容量，创建后不可改变 |
| position | 下一个要读写的位置 |
| limit | 缓冲区的边界，position 不能超过 limit |
| mark | 标记位置，用于后续的 reset 操作 |

#### 重要方法

- **put**：向缓冲区写入数据
- **get**：从缓冲区读取数据
- **flip**：切换到读模式，设置 limit = position，position = 0
- **rewind**：重置 position = 0，用于重复读取
- **clear**：清空缓冲区，设置 position = 0，limit = capacity
- **compact**：压缩缓冲区，将未读取的数据移到缓冲区开头
- **mark**：标记当前 position
- **reset**：重置 position 到 mark 位置

### 5.3 Channel

Channel 是一个通道，可以双向传输数据。常用的实现类包括：

- **FileChannel**：文件通道
- **SocketChannel**：Socket 通道
- **ServerSocketChannel**：服务器 Socket 通道
- **DatagramChannel**：数据报通道

#### 重要方法

- **read**：从通道读取数据到缓冲区
- **write**：从缓冲区写入数据到通道
- **open**：打开通道
- **close**：关闭通道
- **transferFrom**：从其他通道传输数据
- **transferTo**：传输数据到其他通道

### 5.4 Selector

Selector 是一个选择器，可以监控多个通道的事件。常用的事件包括：

- **OP_READ**：可读事件
- **OP_WRITE**：可写事件
- **OP_CONNECT**：连接事件
- **OP_ACCEPT**：接受连接事件

#### 使用步骤

1. 创建 Selector
2. 注册通道到 Selector
3. 轮询 Selector 获取就绪事件
4. 处理就绪事件

## 6. Java AIO

Java AIO（Asynchronous IO）是从 JDK 1.7 开始引入的异步 IO 模型，也称为 NIO.2。

### 核心组件

- **AsynchronousChannel**：异步通道
- **AsynchronousSocketChannel**：异步 Socket 通道
- **AsynchronousServerSocketChannel**：异步服务器 Socket 通道
- **AsynchronousFileChannel**：异步文件通道
- **CompletionHandler**：完成处理器，用于处理异步操作的结果

### 特点

- **完全异步**：应用进程无需等待 IO 操作完成
- **回调机制**：通过 CompletionHandler 处理 IO 操作结果
- **更高的性能**：适用于高并发场景

## 7. Netty 简介

Netty 是一个高性能的异步事件驱动的网络应用框架，基于 Java NIO 实现。

### 核心特点

- **高性能**：基于 NIO，支持高并发
- **可靠性**：经过大规模生产环境验证
- **易用性**：提供简洁的 API
- **扩展性**：模块化设计，易于扩展
- **安全性**：支持 SSL/TLS

### 适用场景

- **RPC 框架**：如 Dubbo
- **消息中间件**：如 RocketMQ
- **游戏服务器**：实时通讯
- **流媒体服务器**：音视频传输
- **HTTP 服务器**：如 Spring WebFlux

## 8. 面试题解析

### 1. 字节流与字符流的区别是什么？

**答案**：
- **数据单位**：字节流以字节（8位）为单位传输数据，字符流以字符（16位）为单位传输数据
- **处理对象**：字节流处理二进制数据，字符流处理文本数据
- **缓冲区**：字节流无内置缓冲区，字符流有内置缓冲区
- **编码转换**：字节流需手动处理编码转换，字符流自动处理编码转换
- **基类**：字节流的基类是 InputStream/OutputStream，字符流的基类是 Reader/Writer

### 2. 什么是 IO 多路复用？有哪些实现方式？

**答案**：
- **IO 多路复用**：通过一个系统调用同时监控多个文件描述符，当其中任何一个文件描述符就绪时，系统会通知应用进程
- **实现方式**：
  - **select**：可监控最多 1024 个文件描述符，需要将文件描述符集合从用户空间拷贝到内核空间
  - **poll**：使用链表存储文件描述符，无数量限制，但仍需要遍历所有文件描述符
  - **epoll**：使用红黑树存储文件描述符，使用事件通知机制，性能最好，但仅在 Linux 系统支持

### 3. Java NIO 的核心组件有哪些？

**答案**：
- **Buffer**：缓冲区，用于存储数据
- **Channel**：通道，用于数据传输
- **Selector**：选择器，用于监控多个通道的事件

### 4. BIO、NIO、AIO 的区别是什么？

**答案**：
- **BIO**：同步阻塞 IO，一个连接一个线程，实现简单，但并发性能差
- **NIO**：同步非阻塞 IO，一个线程可以处理多个连接，通过 Selector 监控通道事件
- **AIO**：异步非阻塞 IO，完全异步，通过回调处理 IO 操作结果

### 5. 什么是缓冲区？Java NIO 中的 Buffer 有哪些重要属性？

**答案**：
- **缓冲区**：是一个用于存储数据的容器，在 Java NIO 中，所有的数据传输都必须通过缓冲区进行
- **重要属性**：
  - **capacity**：缓冲区的容量，创建后不可改变
  - **position**：下一个要读写的位置
  - **limit**：缓冲区的边界，position 不能超过 limit
  - **mark**：标记位置，用于后续的 reset 操作

### 6. 如何使用 Selector 实现多路复用？

**答案**：
1. **创建 Selector**：通过 Selector.open() 创建
2. **注册通道**：将通道注册到 Selector，并指定要监控的事件
3. **轮询事件**：通过 selector.select() 轮询获取就绪事件
4. **处理事件**：遍历就绪的 SelectionKey，处理对应的事件
5. **重复轮询**：回到步骤 3，继续轮询事件

### 7. Netty 为什么性能高？

**答案**：
- **基于 NIO**：使用非阻塞 IO，一个线程可以处理多个连接
- **事件驱动**：基于事件模型，减少线程上下文切换
- **零拷贝**：使用 FileChannel.transferTo() 实现零拷贝
- **内存池**：减少内存分配和回收的开销
- **高效的线程模型**：使用 Reactor 模式，优化线程使用
- **编解码优化**：提供高效的编解码框架

### 8. 什么是零拷贝？Java 中如何实现零拷贝？

**答案**：
- **零拷贝**：避免数据在用户空间和内核空间之间的拷贝，减少 CPU 开销和内存带宽
- **实现方式**：
  - **mmap**：内存映射，将文件映射到内存
  - **sendfile**：通过内核直接将数据从文件传输到网络
  - **FileChannel.transferTo()**：Java 中实现零拷贝的方法，底层使用 sendfile 系统调用

## 9. 参考链接

### 基础概念
- [Java IO 流详解](https://www.zhihu.com/question/39262026/answer/127103286)
- [字节流与字符流的区别](https://zhuanlan.zhihu.com/p/292151192)

### IO 模型
- [IO 多路复用详解](https://zhuanlan.zhihu.com/p/615586279)
- [五种 IO 模型对比](https://baijiahao.baidu.com/s?id=1702203870868277915&wfr=spider&for=pc)
- [Java 核心知识点 - IO 模型](https://www.kancloud.cn/imnotdown1019/java_core_full/1011417)

### Java NIO
- [Java NIO 详解](https://cloud.tencent.com/developer/article/1648648)
- [NIO 核心组件](https://blog.csdn.net/SearchB/article/details/124338815)

### Netty
- [Netty 官方文档](https://netty.io/wiki/)
- [Netty 入门教程](https://www.cnblogs.com/xiaobear/p/16660511.html)

### 编码知识
- [常用字符编码所占字节数](http://wh.mobiletrain.org/msjq/72266.html)

### 进阶知识
- [Java IO 性能优化](https://blog.csdn.net/wkh18891843165/article/details/115310276)
- [零拷贝技术详解](https://blog.csdn.net/qq_35958391/article/details/124509242)