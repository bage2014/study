

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



https://my.oschina.net/jiagoushi/blog/8590385

1. 请求被发送到本机端口 8080，被在那里侦听的 Coyote HTTP/1.1 Connector 获得
2. Connector 把该请求交给它所在的 Service 的 Engine 来处理，并等待 Engine 的回应
3. Engine 获得请求 localhost:8080/test/index.jsp，匹配它所有虚拟主机 Host
4. Engine 匹配到名为 localhost 的 Host（即使匹配不到也把请求交给该 Host 处理，因为该 Host 被定义为该 Engine 的默认主机）
5. localhost Host 获得请求 /test/index.jsp，匹配它所拥有的所有 Context
6. Host 匹配到路径为 /test 的 Context（如果匹配不到就把该请求交给路径名为”” 的 Context 去处理）
7. path=”/test” 的 Context 获得请求 /index.jsp，在它的 mapping table 中寻找对应的 servlet
8. Context 匹配到 URL PATTERN 为 *.jsp 的 servlet，对应于 JspServlet 类
9. 构造 HttpServletRequest 对象和 HttpServletResponse 对象，作为参数调用 JspServlet 的 doGet 或 doPost 方法
10. Context 把执行完了之后的 HttpServletResponse 对象返回给 Host
11. Host 把 HttpServletResponse 对象返回给 Engine
12. Engine 把 HttpServletResponse 对象返回给 Connector
13. Connector 把 HttpServletResponse 对象返回给客户 browser

## 核心配置 

https://www.bilibili.com/video/BV1dJ411N7Um?p=20&vd_source=72424c3da68577f00ea40a9e4f9001a1

### 线程池

- `maxSpareThreads:` 如果空闲状态的线程数多于设置的数目，则将这些线程中止，减少这个池中的线程总数。
- `minSpareThreads: `最小备用线程数，tomcat启动时的初始化的线程数。
- `enableLookups: `这个功效和Apache中的HostnameLookups一样，设为关闭。
- `connectionTimeout :` connectionTimeout为网络连接超时时间毫秒数。
- `maxThreads :` maxThreads Tomcat使用线程来处理接收的每个请求。这个值表示Tomcat可创建的最大的线程数，即最大并发数。
- `acceptCount :` acceptCount是当线程数达到maxThreads后，后续请求会被放入一个等待队列，这个acceptCount是这个队列的大小，如果这个队列也满了，就直接refuse connection。
- `maxProcessors与minProcessors: `在 Java中线程是程序运行时的路径，是在一个程序中与其它控制线程无关的、能够独立运行的代码段。它们共享相同的地址空间。多线程帮助程序员写出CPU最 大利用率的高效程序，使空闲时间保持最低，从而接受更多的请求。



### server.xml

https://my.oschina.net/jiagoushi/blog/8590385

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- Server代表一个 Tomcat 实例。可以包含一个或多个 Services，其中每个Service都有自己的Engines和Connectors。
       port="8005"指定一个端口，这个端口负责监听关闭tomcat的请求
  -->
<Server port="8005" shutdown="SHUTDOWN">
    <!-- 监听器 -->
    <Listener className="org.apache.catalina.startup.VersionLoggerListener" />
    <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
    <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />
    <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />
    <Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener" />
    <!-- 全局命名资源，定义了UserDatabase的一个JNDI(java命名和目录接口)，通过pathname的文件得到一个用户授权的内存数据库 -->
    <GlobalNamingResources>
        <Resource name="UserDatabase" auth="Container"
               type="org.apache.catalina.UserDatabase"
               description="User database that can be updated and saved"
               factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
               pathname="conf/tomcat-users.xml" />
    </GlobalNamingResources>
    <!-- Service它包含一个<Engine>元素,以及一个或多个<Connector>,这些Connector元素共享用同一个Engine元素 -->
    <Service name="Catalina">
        <!-- 
         每个Service可以有一个或多个连接器<Connector>元素，
         第一个Connector元素定义了一个HTTP Connector,它通过8080端口接收HTTP请求;第二个Connector元素定
         义了一个JD Connector,它通过8009端口接收由其它服务器转发过来的请求.
     -->
        <Connector port="8080" protocol="HTTP/1.1"
                connectionTimeout="20000"
                redirectPort="8443" />
        <Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
        <!-- 每个Service只能有一个<Engine>元素 -->
        <Engine name="Catalina" defaultHost="localhost">
            <Realm className="org.apache.catalina.realm.LockOutRealm">
                <Realm className="org.apache.catalina.realm.UserDatabaseRealm"
                resourceName="UserDatabase"/></Realm>
            <!-- 默认host配置，有几个域名就配置几个Host，但是这种只能是同一个端口号 -->
            <Host name="localhost"  appBase="webapps"
             unpackWARs="true" autoDeploy="true">
                <!-- Tomcat的访问日志，默认可以关闭掉它，它会在logs文件里生成localhost_access_log的访问日志 -->
                <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
                prefix="localhost_access_log" suffix=".txt"
                pattern="%h %l %u %t "%r" %s %b" />
            </Host>
            <Host name="www.hzg.com"  appBase="webapps"
             unpackWARs="true" autoDeploy="true">
                <Context path="" docBase="/myweb1" />
                <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
                prefix="hzg_access_log" suffix=".txt"
                pattern="%h %l %u %t "%r" %s %b" />
            </Host>
        </Engine>
    </Service>
</Server>
```



### 相互关系

https://my.oschina.net/jiagoushi/blog/8590385

![](https://oscimg.oschina.net/oscnet/up-b65977a7e070b20f311ae018cbfce826f18.png)





## 性能配置 

https://www.bilibili.com/video/BV1dJ411N7Um?p=42&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV1dJ411N7Um?p=44&vd_source=72424c3da68577f00ea40a9e4f9001a1





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