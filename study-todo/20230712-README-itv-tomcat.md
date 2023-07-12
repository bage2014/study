

# study- Tomcat  #

## 简介

Tomcat 服务器是一个免费的开放源代码的Web 应用服务器，

Tomcat是Apache 软件基金会（Apache Software Foundation）的Jakarta 项目中的一个核心项目，

它早期的名称为catalina，后来由Apache、Sun 和其他一些公司及个人共同开发而成，并更名为Tomcat。

Tomcat 是一个小型的轻量级应用服务器，在中小型系统和并发访问用户不是很多的场合下被普遍使用，

是开发和调试JSP 程序的首选，因为Tomcat 技术先进、性能稳定，成为目前比较流行的Web 应用服务器。



## Key关键点

- 行业调研、优势、代替方案等
- 架构【server\service\connector\context】
- 请求过程 
- 核心配置 
- 性能配置 
- Servlet 加载过程
-  Pipeline-Valve机制 
-  配置优化
-  Tomcat 安全



## 目录结构 

- bin：启动和关闭tomcat的bat文件。

- conf：配置文件。
  - server.xml该文件用于配置server相关的信息，比如tomcat启动的端口号，配置主机(Host)。
  - web.xml文件配置与web应用(web应用相当于一个web站点)
  - tomcat-user.xml配置用户名密码和相关权限。

- lib：该目录放置运行tomcat运行需要的jar包。

- logs：存放日志，当我们需要查看日志的时候，可以查询信息。

- webapps：放置我们的web应用。

- work工作目录：该目录用于存放jsp被访问后生成对应的server文件和.class文件。



## 请求过程 

【2023-06-23】8分钟半左右

https://www.bilibili.com/video/BV1dJ411N7Um?p=15&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV1dJ411N7Um?p=16&vd_source=72424c3da68577f00ea40a9e4f9001a1



https://zhuanlan.zhihu.com/p/473877606



## 核心配置 

https://www.bilibili.com/video/BV1dJ411N7Um?p=20&vd_source=72424c3da68577f00ea40a9e4f9001a1



- `maxSpareThreads:` 如果空闲状态的线程数多于设置的数目，则将这些线程中止，减少这个池中的线程总数。
- `minSpareThreads: `最小备用线程数，tomcat启动时的初始化的线程数。
- `enableLookups: `这个功效和Apache中的HostnameLookups一样，设为关闭。
- `connectionTimeout :` connectionTimeout为网络连接超时时间毫秒数。
- `maxThreads :` maxThreads Tomcat使用线程来处理接收的每个请求。这个值表示Tomcat可创建的最大的线程数，即最大并发数。
- `acceptCount :` acceptCount是当线程数达到maxThreads后，后续请求会被放入一个等待队列，这个acceptCount是这个队列的大小，如果这个队列也满了，就直接refuse connection。
- `maxProcessors与minProcessors: `在 Java中线程是程序运行时的路径，是在一个程序中与其它控制线程无关的、能够独立运行的代码段。它们共享相同的地址空间。多线程帮助程序员写出CPU最 大利用率的高效程序，使空闲时间保持最低，从而接受更多的请求。



## 性能配置 

https://www.bilibili.com/video/BV1dJ411N7Um?p=42&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV1dJ411N7Um?p=44&vd_source=72424c3da68577f00ea40a9e4f9001a1

## 



## **概要** 

https://u19900101.github.io/2021-09-07-tomcat/



## 请求处理流程

### Web请求过程

1）用户通过浏览器进行了一个操作，比如输入网址并回车，或者是点击链接，接着浏览器获取了这个事件。

2）浏览器向服务端发出TCP连接请求。

3）服务程序接受浏览器的连接请求，并经过TCP三次握手建立连接。

4）浏览器将请求数据打包成一个HTTP协议格式的数据包。

5）浏览器将该数据包推入网络，数据包经过网络传输，最终达到端服务程序。

6）服务端程序拿到这个数据包后，同样以HTTP协议格式解包，获取到客户端的意图。

7）得知客户端意图后进行处理，比如提供静态文件或者调用服务端程序获得动态结果。

8）服务器将响应结果（可能是HTML或者图片等）按照HTTP协议格式打包。

9）服务器将响应数据包推入网络，数据包经过网络传输最终达到到浏览器。

10）浏览器拿到数据包后，以HTTP协议的格式解包，然后解析数据，假设这里的数据是HTML。

11）浏览器将HTML文件展示在页面上。



### Tomcat 请求过程

当一个请求到来时，Mapper组件通过解析请求URL里的域名和路径，再到自己保存的 Map里去查找，就能定位到一个Servlet。一个请求URL最后只会定位到一个 Wrapper容器，也就是一个Servlet。 



![](https://u19900101.github.io/blogimg/tomcat/image-20210908163256963.png)





![](https://u19900101.github.io/blogimg/tomcat/image-20210908163317030.png)

步骤如下:

1) Connector组件Endpoint中的Acceptor监听客户端套接字连接并接收Socket。

2) 将连接交给线程池Executor处理，开始执行请求响应任务。

3) Processor组件读取消息报文，解析请求行、请求体、请求头，封装成Request对象。

4) Mapper组件根据请求行的URL值和请求头的Host值匹配由哪个Host容器、Context容

器、Wrapper容器处理请求。

5) CoyoteAdaptor组件负责将Connector组件和Engine容器关联起来，把生成的 Request对象和响应对象Response传递到Engine容器中，调用 Pipeline。

6) Engine容器的管道开始处理，管道中包含若干个Valve、每个Valve负责部分处理逻 辑。执行完Valve后会执行基础的 Valve–StandardEngineValve，负责调用Host容器的 Pipeline。

7) Host容器的管道开始处理，流程类似，最后执行 Context容器的Pipeline。

8) Context容器的管道开始处理，流程类似，最后执行 Wrapper容器的Pipeline。

9) Wrapper容器的管道开始处理，流程类似，最后执行 Wrapper容器对应的Servlet对象 的 处理方法。







## **架构** 

https://u19900101.github.io/2021-09-07-tomcat/

### 请求过程

当客户请求某个资源时，HTTP服务器会用一个ServletRequest对象把客户的请求信息封 装起来，

然后调用Servlet容器的service方法，Servlet容器拿到请求后，

根据请求的URL 和Servlet的映射关系，找到相应的Servlet，

如果Servlet还没有被加载，就用反射机制创 建这个Servlet，并调用Servlet的init方法来完成初始化，

接着调用Servlet的service方法 来处理请求，

把ServletResponse对象返回给HTTP服务器，HTTP服务器会把响应发送给 客户端。

![](https://u19900101.github.io/blogimg/tomcat/image-20210907224310159.png)



### 整体模块架构

模块分层

![](https://u19900101.github.io/blogimg/tomcat/image-20210907230006015.png)



## 启动过程

启动步骤 :

1） 启动tomcat ， 需要调用 bin/startup.bat (在linux 目录下 , 需要调用 bin/startup.sh) ， 在startup.bat 脚本中, 调用了catalina.bat。

2） 在catalina.bat 脚本文件中，调用了BootStrap 中的main方法。

3）在BootStrap 的main 方法中调用了 init 方法 ， 来创建Catalina 及 初始化类加载器。 

4）在BootStrap 的main 方法中调用了 load 方法 ， 在其中又调用了Catalina的load方 法。

5）在Catalina 的load 方法中 , 需要进行一些初始化的工作, 并需要构造Digester 对象, 用 于解析 XML。

6） 然后在调用后续组件的初始化操作 。。。 加载Tomcat的配置文件，初始化容器组件 ，监听对应的端口号， 准备接受客户端请求

![](https://u19900101.github.io/blogimg/tomcat/image-20210908162910205.png)



## Tomcat 安全

### 配置安全

1） 删除webapps目录下的所有文件，禁用tomcat管理界面；

2） 注释或删除tomcat-users.xml文件内的所有用户权限

3） 更改关闭tomcat指令或禁用； tomcat的server.xml中定义了可以直接关闭 Tomcat 实例的管理端口（默认8005）。 可以通过 telnet 连接上该端口之后，输入 SHUTDOWN （此为默认关闭指令）即可关闭 Tomcat 实例（注意，此时虽然实例关闭了，但是进程还是存在的）。由于默认关闭 Tomcat 的端口和指令都很简单。默认端口为8005，指令为SHUTDOWN 。

### 其他配置

应用： 实现 认证 和授权

传输： https



## Bilibili 

【2023-06-23】https://www.bilibili.com/video/BV1dJ411N7Um?p=15&vd_source=72424c3da68577f00ea40a9e4f9001a1



【2023-07-12】【黑马程序员Java进阶教程Tomcat核心原理解析-哔哩哔哩】 https://b23.tv/tRxGiYM



## 参考链接 

【2023-07-12】https://www.jb51.net/article/219367.htm

【2023-07-12】https://www.cnblogs.com/davidwang456/p/11271326.html

【2023-07-12】https://i.vycc.cn/article/1213542.html