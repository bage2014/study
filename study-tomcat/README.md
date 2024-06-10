# study-Tomcat #
## ***性能优化***

参考链接：

https://www.cnblogs.com/zhuawang/p/5213192.html

***\*一:Tomcat内存优化,启动时告诉JVM我要一块大内存(调优内存是最直接的方式)\****

Windows 下的catalina.bat

Linux 下的catalina.sh 如:

JAVA_OPTS='-Xms256m -Xmx512m'

-Xms<size> JVM初始化堆的大小

-Xmx<size> JVM堆的最大值 实际参数大小根据服务器配置或者项目具体设置.

***\*二:Tomcat 线程优化\**** 在server.xml中 如:

<Connector port="80" protocol="HTTP/1.1" maxThreads="600" minSpareThreads="100" maxSpareThreads="500" acceptCount="700"

connectionTimeout="20000"  />

maxThreads="X" 表示最多同时处理X个连接

minSpareThreads="X" 初始化X个连接

maxSpareThreads="X" 表示如果最多可以有X个线程，一旦超过X个,则会关闭不在需要的线程

acceptCount="X" 当同时连接的人数达到maxThreads时,还可以排队,队列大小为X.超过X就不处理

***\*三:Tomcat IO优化\****

1:同步阻塞IO（JAVA BIO） 同步并阻塞，服务器实现模式为一个连接一个线程(one connection one thread 想想都觉得恐怖,线程可是非常宝贵的资源)，当然可以通过线程池机制改善.

2:JAVA NIO:又分为同步非阻塞IO,异步阻塞IO 与BIO最大的区别one request one thread.可以复用同一个线程处理多个connection(多路复用).

3:,异步非阻塞IO(Java NIO2又叫AIO) 主要与NIO的区别主要是操作系统的底层区别.可以做个比喻:比作快递，NIO就是网购后要自己到官网查下快递是否已经到了(可能是多次)，然后自己去取快递；AIO就是快递员送货上门了(不用关注快递进度)。

BIO方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4以前的唯一选择，但程序直观简单易理解.

NIO方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，并发局限于应用中，编程比较复杂，JDK1.4开始支持.

AIO方式使用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持.

在server.xml中

<Connector port="80" protocol="org.apache.coyote.http11.Http11NioProtocol" 

  connectionTimeout="20000" 

  URIEncoding="UTF-8" 

  useBodyEncodingForURI="true" 

  enableLookups="false" 

  redirectPort="8443" />

实现对Tomcat的IO切换.

## **2.** ***\*配置\****

参考链接：

https://www.cnblogs.com/xuwc/p/8523681.html

 配置文件
 server.xml：主要的配置文件。
 web.xml：缺省的web app配置，WEB-INF/web.xml会覆盖该配置。
 context.xml：不清楚跟server.xml里面的context是否有关系。

 server.xml配置
 server标签
 port：指定一个端口，这个端口负责监听关闭tomcat的请求。
 shutdown：指定向端口发送的命令字符串。

 service标签
 name：指定service的名字。

 Connector(表示客户端和service之间的连接)标签

port：指定服务器端要创建的端口号，并在这个端口监听来自客户端的请求。
 minProcessors：服务器启动时创建的处理请求的线程数。
 maxProcessors：最大可以创建的处理请求的线程数。
 enableLookups：如果为true，则可以通过调用request.getRemoteHost()进行DNS查询来得到远程客户端的实际主机名，若为false则不进行DNS查询，而是返回其ip地址。
 redirectPort：指定服务器正在处理http请求时收到了一个SSL传输请求后重定向的端口号。
 acceptCount：指定当所有可以使用的处理请求的线程数都被使用时，可以放到处理队列中的请求数，超过这个数的请求将不予处理。
 connectionTimeout：指定超时的时间数(以毫秒为单位)。

 Engine(表示指定service中的请求处理机，接收和处理来自Connector的请求)标签
 defaultHost：指定缺省的处理请求的主机名，它至少与其中的一个host元素的name属性值是一样的。

 Context(表示一个web应用程序，通常为WAR文件，关于WAR的具体信息见servlet规范)标签
 docBase：该web应用的文档基准目录（Document Base，也称为Context Root），或者是WAR文件的路径。可以使用绝对路径，也可以使用相对于context所属的Host的appBase路径。
 path：表示此web应用程序的url的前缀，这样请求的url为http://localhost:8080/path/****。
 ***\*reloadable：这个属性非常重要，如果为true，则tomcat会自动检测应用程序的/WEB-INF/lib和/WEB-INF/classes目录的变化，自动装载新的应用程序，我们可以在不重起tomcat的情况下改变应用程序。\****
 useNaming：如果希望Catalina为该web应用使能一个JNDI InitialContext对象，设为true。该InitialialContext符合J2EE平台的约定，缺省值为true。
 workDir：Context提供的临时目录的路径，用于servlet的临时读/写。利用javax.servlet.context.tempdir属性，servlet可以访问该目录。如果没有指定，使用$CATALINA_HOME/work下一个合适的目录。
 swallowOutput：如果该值为true，System.out和System.err的输出被重定向到web应用的logger。如果没有指定，缺省值为false
 debug：与这个Engine关联的Logger记录的调试信息的详细程度。数字越大，输出越详细。如果没有指定，缺省为0。

 host(表示一个虚拟主机)标签
 name：指定主机名。
 appBase：应用程序基本目录，即存放应用程序的目录。
 unpackWARs：如果为true，则tomcat会自动将WAR文件解压，否则不解压，直接从WAR文件中运行应用程序。

 Logger(表示日志，调试和错误信息)标签
 className：指定logger使用的类名，此类必须实现org.apache.catalina.Logger接口。
 prefix：指定log文件的前缀。
 suffix：指定log文件的后缀。
 timestamp：如果为true，则log文件名中要加入时间，如下例:localhost_log.2001-10-04.txt。

 Realm(表示存放用户名，密码及role的数据库)标签
 className：指定Realm使用的类名，此类必须实现org.apache.catalina.Realm接口。

 Valve(功能与Logger差不多，其prefix和suffix属性解释和Logger  中的一样)标签
 className：指定Valve使用的类名，如用org.apache.catalina.valves.AccessLogValve类可以记录应用程序的访问信息。
 directory：指定log文件存放的位置。
 pattern：有两个值，common方式记录远程主机名或ip地址，用户名，日期，第一行请求的字符串，HTTP响应代码，发送的字节数。combined方式比common方式记录的值更多。

# ***\*tomcat的安全配置：\****

https://blog.51cto.com/8248183/2062343

 

首次安装完成后立即删除webapps下面的所有代码
rm -rf /srv/apache-tomcat/webapps/*
注释或删除 tomcat-users.xml 所有用户权限，看上去如下：

# ***\*cat conf/tomcat-users.xml\****

<?xml version='1.0' encoding='utf-8'?>

<tomcat-users>
</tomcat-users>

2、隐藏tomcat版本
01.首先找到这个jar包，$TOMCAT_HOME/lib/catalina.jar
02.解压catalina.jar之后按照路径\org\apache\catalina\util\ServerInfo.properties找到文件
03.打开ServerInfo.properties文件修改如下：把server.number、server.built置空
server.info=Apache Tomcat
server.number=
server.built=
04.重新打成jar包，重启tomcat。
3、隐藏tomcat 的服务类型
conf/server.xml文件中，为connector元素添加server="
"，注意不是空字符串，是空格组成的长度为1的字符串，或者输入其他的服务类型，这时候，在response header中就没有server的信息啦！
4、应用程序安全
关闭war自动部署 unpackWARs="false" autoDeploy="false"。防止被植入木马等恶意程序
5、修改服务监听端口
一般公司的 Tomcat 都是放在内网的，因此我们针对 Tomcat 服务的监听地址都是内网地址。
修改实例：

<Connector port="8080" address="172.16.100.1" />

 

## 手写Tomcat

参考连接： https://blog.csdn.net/weixin_39033443/article/details/83901722、https://www.cnblogs.com/jyroy/p/10778760.html





### 主要步骤

HTTP 协议基本解析

处理 GET 或者 POST 请求逻辑

启动容器（socket）



## Spring Boot 启动

Spring Boot 应用程序被打包成的jar包之所以可以直接通过 `java -jar` 命令运行，是因为Spring Boot在构建过程中做了一些特殊的设计和配置。具体原因：

1. **Fat/Uber JAR**: Spring Boot使用maven插件`spring-boot-maven-plugin`（或Gradle对应的插件）将项目及其所有依赖项打包成一个单一的、自包含的jar文件，通常称为“Fat JAR”或“Uber JAR”。这意味着不仅包含了自己的类文件，还包含了运行应用所需的所有第三方库。
2. **Manifest.MF**: 在打包过程中，此插件会修改MANIFEST.MF文件，这是jar包中的一个元数据文件。在MANIFEST.MF中，特别指定了`Main-Class`属性，该属性指向Spring Boot的一个内置的启动类（如`org.springframework.boot.loader.JarLauncher`），这个启动器类知道如何正确启动Spring Boot应用程序。
3. **嵌入式Servlet容器**：Spring Boot默认集成了诸如Tomcat、Jetty或Undertow等嵌入式Web容器，使得无需外部服务器环境也能运行Web应用。
4. **启动器类加载器**：当通过`java -jar`运行Spring Boot应用时，JVM会根据MANIFEST.MF中的`Main-Class`找到并运行指定的启动器类。这个启动器类加载器能够解压并加载内部的依赖库，并定位到实际的应用主类（在`spring-boot-starter-parent`或`@SpringBootApplication`注解标记的类），进而执行其`main`方法。
5. **类路径扫描和自动配置**：Spring Boot应用通过特定的类路径扫描机制和自动配置功能，能够在启动时识别出应用所依赖的服务和组件，并自动配置它们，大大简化了传统Java应用的配置和部署过程。
6. springboot内嵌的tomcat最终也是需要将tomcat中的jar文件解压到操作系统的临时目录，作为web根目录运行的。

Spring Boot通过精心设计的打包流程和启动器类，使得生成的jar包可以直接作为一个独立的应用程序运行，极大地简化了部署和运维复杂度。

作者：码农Academy
链接：https://juejin.cn/post/7353582927680208933
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。



## 参考链接 

https://juejin.cn/post/7353582927680208933











