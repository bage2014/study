# Study-itv-io #

IO



## 关键点

- 核心组件
- Java标准
- 字节流 字符流 
- BIO NIO
- Netty
- 常用类
- Filter流



## 基本概念

#### 字节流 字符流 

1字符 = 2字节【每个8bit】

字符流和字节流的使用非常相似，但是实际上字节流的操作不会经过缓冲区（内存）而是直接操作文本本身的，而字符流的操作会先经过缓冲区（内存）然后通过缓冲区再操作文件

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







## 参考链接

https://www.kancloud.cn/imnotdown1019/java_core_full/1011417

【2023-06-17】https://blog.csdn.net/SearchB/article/details/124338815

【2023-06-17】https://www.cnblogs.com/xiaobear/p/16660511.html

【2023-07-06】http://wh.mobiletrain.org/msjq/72266.html





## Bilibili

