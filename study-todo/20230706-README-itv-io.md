# Study-itv-io #

IO



## 关键点

- 核心组件
- Java标准
- 字节流 字符流 
- IO 模型
- BIO NIO
- Netty
- 常用类
- Filter流



## 基本概念

#### 字节流 字符流 

**1字符 = 2字节**【每个8bit】

字符流和字节流的使用非常相似，但是实际上**字节流的操作不会经过缓冲区**（内存）而是直接操作文本本身的，而字符流的操作会先经过缓冲区（内存）然后通过缓冲区再操作文件

​       以字节为单位输入输出数据，字节流按照8位传输

​       以字符为单位输入输出数据，字符流按照16位传输

https://www.zhihu.com/question/39262026/answer/127103286

https://zhuanlan.zhihu.com/p/292151192

https://blog.csdn.net/wkh18891843165/article/details/115310276



#### 常用对象

**File** 

https://blog.csdn.net/wkh18891843165/article/details/115310276

**InputStream、OutputStream等子类**

FileInputStream，FileOutputStream,BufferedOutputStream

字符流BufferedReader和Writer

## IO模型

 I/O 模型，包括

Java 核心知识点 https://www.kancloud.cn/imnotdown1019/java_core_full/1011417

IO 多路复用 https://zhuanlan.zhihu.com/p/615586279?utm_id=0

多路复用 https://baijiahao.baidu.com/s?id=1702203870868277915&wfr=spider&for=pc

### 阻塞 I/O

阻塞 I/O 是最简单的 I/O 模型，一般表现为进程或线程等待某个条件，如果条件不满足，则**一直等下去**。条件满足，则进行下一步操作。

### 非阻塞 I/O

与阻塞 I/O 模型相反，在非阻塞 I/O 模型下。应用进程与内核交互，目的未达到时，不再一味的等着，而是**直接返回**。然后通过**轮询**的方式，不停的去问内核数据准备好没



非阻塞 I/O 的缺点是：每次发起系统调用，只能检查**一个**文件描述符是否就绪。当文件描述符很多时，系统调用的成本很高。



### I/O 【多路】复用

https://zhuanlan.zhihu.com/p/615586279?utm_id=0

Unix/Linux 环境下的 I/O 复用模型包含三组系统调用，分别是 **select**、**poll** 和 **epoll**（FreeBSD 中则为 kqueue）

I/O 多路复用相当于将「遍历所有文件描述符、通过非阻塞 I/O 查看其是否就绪」的过程从用户线程移到了内核中，由内核来负责轮询。

进程可以通过 select、poll、epoll 发起 I/O 多路复用的系统调用，这些系统调用都是同步阻塞的：**如果传入的多个文件描述符中，有描述符就绪，则返回就绪的描述符；否则如果所有文件描述符都未就绪，就阻塞调用进程，直到某个描述符就绪，或者阻塞时长超过设置的 timeout 后，再返回**。I/O 多路复用内部使用*非阻塞 I/O* 检查每个描述符的就绪状态。

I/O 多路复用，可以**通过一次系统调用，检查多个文件描述符的状态**。这是 I/O 多路复用的主要优点，相比于非阻塞 I/O，在文件描述符较多的场景下，避免了频繁的用户态和内核态的切换，减少了系统调用的开销。

#### Select 

select 会遍历每个集合的前 nfds个描述符，分别找到可以读取、可以写入、发生错误的描述符，统称为“就绪”的描述符。然后用找到的子集替换参数中的对应集合，返回所有就绪描述符的总数。

1. 性能开销大

2. 1. 调用 `select` 时会陷入内核，这时需要将参数中的 `fd_set` 从用户空间拷贝到内核空间
   2. 内核需要遍历传递进来的所有 `fd_set` 的每一位，不管它们是否就绪

3. 同时能够监听的文件描述符数量太少。受限于 `sizeof(fd_set)` 的大小，在编译内核时就确定了且无法更改。一般是 1024，不同的操作系统不相同。

#### Poll

poll 和 select 几乎没有区别。poll 在用户态通过**数组**方式**传递**文件描述符，在内核会转为**链表**方式**存储**，没有最大数量的限制 。

#### Epoll

简而言之，epoll 有以下几个特点：

- 使用**红黑树**存储文件描述符集合
- 使用**队列**存储就绪的文件描述符
- 每个文件描述符只需在添加时传入一次；通过事件更改文件描述符状态

select、poll 模型都只使用一个函数，而 epoll 模型使用三个函数：`epoll_create`、`epoll_ctl` 和 `epoll_wait`。



### 信号驱动式 I/O 

信号驱动式 I/O 模型是指，应用进程告诉内核，如果某个 socket 的某个事件发生时，请向我发一个信号。在收到信号后，信号对应的处理函数会进行后续处理

### 异步 I/O 等

异步 I/O 是指应用进程把文件描述符传给内核后，啥都不管了，完全由内核去操作这个文件描述符。内核完成相关操作后，会发信号告诉应用进程，说 I/O 操作完成，现在可以进行后续操作了



## NIO、BIO

BIO是阻塞的，NIO是非阻塞的.

BIO是面向流的，只能单向读写，NIO是面向缓冲的, 可以双向读写

使用BIO做Socket连接时，由于单向读写，当没有数据时，会挂起当前线程，阻塞等待，为防止影响其它连接,，需要为每个连接新建线程处理.，然而系统资源是有限的,，不能过多的新建线程，线程过多带来线程上下文的切换，从来带来更大的性能损耗，因此需要使用NIO进行BIO多路复用，使用一个线程来监听所有Socket连接，使用本线程或者其他线程处理连接

AIO是非阻塞 以异步方式发起 I/O 操作。当 I/O 操作进行时可以去做其他操作，由操作系统内核空间提醒IO操作已完成（不懂的可以往下看）



------

​       BIO：同步并阻塞，服务器的实现模式是一个连接一个线程，这样的模式很明显的一个缺陷是：由于客户端连接数与服务器线程数成正比关系，可能造成不必要的线程开销，严重的还将导致服务器内存溢出。当然，这种情况可以通过线程池机制改善，但并不能从本质上消除这个弊端。

​       NIO：在JDK1.4以前，Java的IO模型一直是BIO，但从JDK1.4开始，JDK引入的新的IO模型NIO，它是同步非阻塞的。而服务器的实现模式是多个请求一个线程，即请求会注册到多路复用器Selector上，多路复用器轮询到连接有IO请求时才启动一个线程处理。

​       AIO：JDK1.7发布了NIO2.0，这就是真正意义上的异步非阻塞，服务器的实现模式为多个有效请求一个线程，客户端的IO请求都是由OS先完成再通知服务器应用去启动线程处理（回调）。

​       应用场景：并发连接数不多时采用BIO，因为它编程和调试都非常简单，但如果涉及到高并发的情况，应选择NIO或AIO，更好的建议是采用成熟的网络通信框架Netty。



几个常用类

 **PrintStream、BufferedWriter、PrintWriter的比较？**

​       1、PrintStream类的输出功能非常强大，通常如果需要输出文本内容，都应该将输出流包装成PrintStream后进行输出。它还提供其他两项功能。与其他输出流不同，PrintStream 永远不会抛出 IOException；而是，异常情况仅设置可通过 checkError 方法测试的内部标志。另外，为了自动刷新，可以创建一个 PrintStream

​       2、BufferedWriter:将文本写入字符输出流，缓冲各个字符从而提供单个字符，数组和字符串的高效写入。通过write()方法可以将获取到的字符输出，然后通过newLine()进行换行操作。BufferedWriter中的字符流必须通过调用flush方法才能将其刷出去。并且BufferedWriter只能对字符流进行操作。如果要对字节流操作，则使用BufferedInputStream

​       3、PrintWriter的println方法自动添加换行，不会抛异常，若关心异常，需要调用checkError方法看是否有异常发生，PrintWriter构造方法可指定参数，实现自动刷新缓存（autoflush）



## Filter流



## IO 进化史

### 阻塞IO



### 非阻塞IO



### 多路复用IO



### NIO





## NIO

Java NIO 包含了很多东西，但核心的东西不外乎 Buffer、Channel 和 Selector。

### Buffer 

继承体系

![](https://blog-pictures.oss-cn-shanghai.aliyuncs.com/15200845921379.jpg)



Buffer 本质就是一个数组，只不过在数组的基础上进行适当的封装，方便使用。 Buffer 中有几个重要的属性，通过这几个属性来显示数据存储的信息。这个属性分别是：

| 属性          | 说明                                                         |
| :------------ | :----------------------------------------------------------- |
| capacity 容量 | Buffer 所能容纳数据元素的最大数量，也就是底层数组的容量值。在创建时被指定，不可更改。 |
| position 位置 | 下一个被读或被写的位置                                       |
| limit 上界    | 可供读写的最大位置，用于限制 position，position < limit      |
| mark 标记     | 位置标记，用于记录某一次的读写位置，可以通过 reset 重新回到这个位置 |

Buffer 创建完成后，底层数组的结构信息如下

![](https://blog-pictures.oss-cn-shanghai.aliyuncs.com/15200935482408.jpg)

### Channel

通道是 Java NIO 的核心内容之一，在使用上，通道需和缓存类（ByteBuffer）配合完成读写等操作。与传统的流式 IO 中数据单向流动不同，通道中的数据可以双向流动。通道既可以读

通道类型分为两种，一种是面向文件的，另一种是面向网络的。具体的类声明如下：

- FileChannel
- DatagramChannel
- SocketChannel
- ServerSocketChannel

正如上列表，NIO 通道涵盖了文件 IO，TCP 和 UDP 网络 IO 等通道类型。

NIO FileChannel 在实现上，实际上是对底层操作系统的一些 API 进行了再次封装，也就是一层皮。有了这层封装后，对上就屏蔽了底层 API 的细节，以降低使用难度

### Selector

https://cloud.tencent.com/developer/article/1648648

选择器是 Java 多路复用模型的一个实现，可以同时监控多个非阻塞套接字通道。

![](https://blog-pictures.oss-cn-shanghai.aliyuncs.com/15221629315617.jpg)



## 参考链接

https://www.kancloud.cn/imnotdown1019/java_core_full/1011417

【2023-06-17】https://blog.csdn.net/SearchB/article/details/124338815

【2023-06-17】https://www.cnblogs.com/xiaobear/p/16660511.html

【2023-07-06】http://wh.mobiletrain.org/msjq/72266.html





## Bilibili

