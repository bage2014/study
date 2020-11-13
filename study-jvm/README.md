# study-jvm #

Jvm 实践笔记

ref [https://www.cnblogs.com/redcreen/](https://www.cnblogs.com/redcreen/)

[https://www.cnblogs.com/snowwhite/p/9569443.html](https://www.cnblogs.com/snowwhite/p/9569443.html)

【你假笨@JVM】 http://lovestblog.cn/blog/2016/07/20/jstat/

http://www.jintiankansha.me/column/VEAxWixHBx

redcreen的专栏

https://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html


## 分类 ##
内存模型
程序计数器
堆
方法取
jvm站
本地房发展
垃圾回收
回收算法
垃圾收集器
对象存货判断

常用jdk 工具

类加载过程
及时编译、jit


## jvm 参数 ##

java.io.tmpdir windows 默认：C:\Users\\{user}\AppData\Local\Temp\; linux  默认： /tmp



## Jvm 命令 ##

### JPS  ###

reference link : https://docs.oracle.com/javase/7/docs/technotes/tools/share/jps.html

Java Virtual Machine Process Status Tool

只能列出当前用户的Java进程

默认会在java.io.tmpdir指定的临时文件夹目录下生成一个 hsperfdata_{user}的文件夹



- jps --help 帮助

```
>jps --help
illegal argument: --help
usage: jps [-help]
       jps [-q] [-mlvV] [<hostid>]
```



- jps -m 主函数参数

```
>jps -m
12320 Jps -m
5284
5316 RemoteMavenServer36
7268 JpsMain
```

- jps -l 全类名

```
>jps -l
11488 sun.tools.jps.Jps
5284
5316 org.jetbrains.idea.maven.server.RemoteMavenServer36

```

- jps -v 程序的jvm参数

```
jps -v
11348 Jps -Dapplication.home=D:\professional\jdk1.8.0_131 -Xms8m
5284  exit -Xms128m -Xmx1009m -XX:ReservedCodeCacheSize=240m -XX:+UseConcMarkSweepGC -XX:SoftRefLRUPolicyMSPerMB=50 -ea -XX:CICompilerCount=2 -Dsun.io.useCanonPrefixCache=false -Djdk.http.auth.tunneling.disa
bledSchemes="" -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -Djdk.attach.allowAttachSelf=true -Dkotlinx.coroutines.debug=off -Djdk.module.illegalAccess.silent=true -Djb.vmOptionsFile=C:\Use
rs\89354\AppData\Roaming\JetBrains\IdeaIC2020.1\idea64.exe.vmoptions -Djava.library.path=D:\professional\IntelliJ IDEA\IntelliJ IDEA Community Edition 2020.1\jbr\\bin;D:\professional\IntelliJ IDEA\IntelliJ I
DEA Community Edition 2020.1\jbr\\bin\server -Didea.platform.prefix=Idea -Didea.jre.check=true -Dide.native.launcher=true -Didea.paths.selector=IdeaIC2020.1 -XX:ErrorFile=C:\Users\89354\java_error_in_idea_%p
.log -XX:HeapDumpPath=C:\Users\89354\java_error_in_idea.hprof
5316 RemoteMavenServer36 -Djava.awt.headless=true -Dmaven.defaultProjectBuilder.disableGlobalModelCache=true -Xmx768m -Didea.maven.embedder.version=3.6.1 -Dmaven.ext.class.path=D:\professional\IntelliJ IDEA\
IntelliJ IDEA Community Edition 2020.1\plugins\maven\lib\maven-event-listener.jar -Dfile.encoding=GBK

```



### Jstat  ###

reference link : https://docs.oracle.com/javase/7/docs/technotes/tools/share/jstat.html

解析说明 https://www.cnblogs.com/perfma/p/12456056.html

Java Virtual Machine statistics monitoring tool

Jvm 虚拟机实时信息，内存，垃圾回收等

数据来源于目标程序，一个共享文件中 PerfData；指 指的是/tmp/hsperfdata_{user}/{pid}这个文件

参数指定： -XX:+PerfDisableSharedMem或者-XX:-UsePerfData，此时，这个文件是不会存在；一个是是否共享，一个是是否创建存储这些指标

- PerfDisableSharedMem：决定了存储PerfData的内存是不是可以被共享，如果设置了这个参数，说明不能被共享，此时其他进程将访问不了该内存，这样一来，譬如我们jps，jstat等都无法工作。默认这个参数是关闭的，也就是默认支持共享的方式

- UsePerfData：如果关闭了UsePerfData这个参数，那么jvm启动过程中perf memory都不会被创建，jvm运行过程中自然不会再将这些性能数据保存起来，默认情况是是打开的

文件更新策略，这个文件是通过mmap的方式映射到了内存里，而jstat是直接通过DirectByteBuffer的方式从PerfData里读取的，所以只要内存里的值变了，那我们从jstat看到的值就会发生变化，内存里的值什么时候变，取决于-XX:PerfDataSamplingInterval这个参数，默认是50ms，也就是说50ms更新一次值，基本上可以认为是实时的了。





- jstat--help 帮助

```
>jstat --help
invalid argument count
Usage: jstat -help|-options
       jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]

Definitions:
  <option>      An option reported by the -options option
  <vmid>        Virtual Machine Identifier. A vmid takes the following form:
                     <lvmid>[@<hostname>[:<port>]]
                Where <lvmid> is the local vm identifier for the target
                Java virtual machine, typically a process id; <hostname> is
                the name of the host running the target Java virtual machine;
                and <port> is the port number for the rmiregistry on the
                target host. See the jvmstat documentation for a more complete
                description of the Virtual Machine Identifier.
  <lines>       Number of samples between header lines.
  <interval>    Sampling interval. The following forms are allowed:
                    <n>["ms"|"s"]
                Where <n> is an integer and the suffix specifies the units as
                milliseconds("ms") or seconds("s"). The default units are "ms".
  <count>       Number of samples to take before terminating.
  -J<flag>      Pass <flag> directly to the runtime system.

```



参数解析

- option jstat 选项，比如 gc、class、compiler 等；可以通过  jstat -options 查看
- -t 显示时间戳
- -h 可以在周期性数据数据的时候，可以在指定输出多少行以后输出一次表头，-h3 或者 -h 3 都可以，每三行显示一次表头
- vmid 必填，Jps 的Java进程号，通过 Jps 命令可以得到
- interval 周期间隔时间，默认毫秒，也可以直接指定，3000ms 或3s
- count 统计次数，默认打印一次？



- jstat -options 查看选项

```
>jstat -options
-class // 类的装载、卸载、空间、耗时情况
-compiler // JIT 编译过的方法、耗时情况
-gc // 堆情况，包括 Survivor区、Eden区、老年区等
-gccapacity
-gccause // 额外输出上次 GC 原因
-gcmetacapacity
-gcnew
-gcnewcapacity
-gcold
-gcoldcapacity
-gcutil
-printcompilation

```



- jstat -class {vmid} 类加载情况

每 3000 毫秒查询一次，一共查询 4 次 

```
>jstat -class 5532 3000ms 4
Loaded  Bytes  Unloaded  Bytes     Time
   667  1330.4        0     0.0       0.25
   667  1330.4        0     0.0       0.25
   667  1330.4        0     0.0       0.25
   667  1330.4        0     0.0       0.25

```

参数解析

| Column   | Description                                             |
| -------- | ------------------------------------------------------- |
| Loaded   | Number of classes loaded.                               |
| Bytes    | Number of Kbytes loaded.                                |
| Unloaded | Number of classes unloaded.                             |
| Bytes    | Number of Kbytes unloaded.                              |
| Time     | Time spent performing class load and unload operations. |

Time 加载执行时间



- jstat -compiler {vmid}  编译情况

```
>jstat -compiler 7476
Compiled Failed Invalid   Time   FailedType FailedMethod
    7076      1       0    62.44          1 org/springframework/core/annotation/AnnotatedElementUtils searchWithGetSemanticsInAnnotations

```

参数解析

| Column       | Description                                            |
| ------------ | ------------------------------------------------------ |
| Compiled     | Number of compilation tasks performed.                 |
| Failed       | Number of compilation tasks that failed.               |
| Invalid      | Number of compilation tasks that were invalidated.     |
| Time         | Time spent performing compilation tasks.               |
| FailedType   | Compile type of the last failed compilation.           |
| FailedMethod | Class name and method for the last failed compilation. |

FailedType 失败类型

FailedMethod 失败的方法



- jstat -gc {vmid} 垃圾回收情况

```
>jstat -gc 7476
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
18944.0 26624.0  0.0    0.0   635904.0 298351.9  150016.0   47957.1   59008.0 57022.0 7808.0 7453.1     18    0.215   3      0.414    0.629

```

参数解析



| Column | Description                               |
| ------ | ----------------------------------------- |
| S0C    | Current survivor space 0 capacity (KB).   |
| S1C    | Current survivor space 1 capacity (KB).   |
| S0U    | Survivor space 0 utilization (KB).        |
| S1U    | Survivor space 1 utilization (KB).        |
| EC     | Current eden space capacity (KB).         |
| EU     | Eden space utilization (KB).              |
| OC     | Current old space capacity (KB).          |
| OU     | Old space utilization (KB).               |
| PC     | Current permanent space capacity (KB).    |
| PU     | Permanent space utilization (KB).         |
| YGC    | Number of young generation GC Events.     |
| YGCT   | Young generation garbage collection time. |
| FGC    | Number of full GC events.                 |
| FGCT   | Full garbage collection time.             |
| GCT    | Total garbage collection time.            |

- S0C 第一个幸存区的大小
- S1C 第二个幸存区的大小
- S0U 第一个幸存区的使用大小
- S1U 第二个幸存区的使用大小
- EC 伊甸园区的大小
- EU 伊甸园区的使用大小
- OC 老年代大小
- OU 老年代使用大小
- MC 方法区大小
- MU 方法区使用大小
- CCSC  压缩类空间大小
- CCSU  压缩类空间使用大小
- YGC  年轻代垃圾回收次数
- YGCT 年轻代垃圾回收消耗时间
- FGC  老年代垃圾回收次数
- FGCT  老年代垃圾回收消耗时间
- GCT  垃圾回收消耗总时间



- jstat -gcutil {vmid} 垃圾回收统计情况

```
>jstat -gcutil 1936
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT
  0.00   0.00  73.10  30.66  96.67  95.45     24    0.280     3    0.427    0.707

```

参数解析



- jstat -printcompilation {vmid} 方法编译？

```
>jstat -printcompilation 1936
Compiled  Size  Type Method
    6616      5    1 org/springframework/aop/target/AbstractBeanFactoryBasedTargetSource getBeanFactory
```

参数解析



### Jinfo  ###

reference link : https://docs.oracle.com/javase/7/docs/technotes/tools/share/jinfo.html

Configuration Info

实时查看JVM虚拟机的参数并进行调整

Jvm 虚拟机实时信息，内存，垃圾回收等

- jinfo --help 帮助

```
>jinfo -help
Usage:
    jinfo [option] <pid>
        (to connect to running process)
    jinfo [option] <executable <core>
        (to connect to a core file)
    jinfo [option] [server_id@]<remote server IP or hostname>
        (to connect to remote debug server)

where <option> is one of:
    -flag <name>         to print the value of the named VM flag
    -flag [+|-]<name>    to enable or disable the named VM flag
    -flag <name>=<value> to set the named VM flag to the given value
    -flags               to print VM flags
    -sysprops            to print Java system properties
    <no option>          to print both of the above
    -h | -help           to print this help message

```

- jinfo {vmid} 打印所有配置信息

```
>jinfo 5532
Attaching to process ID 5532, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.131-b11
Java System Properties:

java.runtime.name = Java(TM) SE Runtime Environment
java.vm.version = 25.131-b11
sun.boot.library.path = D:\professional\jdk1.8.0_131\jre\bin
java.vendor.url = http://java.oracle.com/
java.vm.vendor = Oracle Corporation
path.separator = ;
file.encoding.pkg = sun.io
java.vm.name = Java HotSpot(TM) 64-Bit Server VM
sun.os.patch.level =
sun.java.launcher = SUN_STANDARD
user.script =
user.country = CN
user.dir = E:\GitHubDesktop\study
java.vm.specification.name = Java Virtual Machine Specification
java.runtime.version = 1.8.0_131-b11
java.awt.graphicsenv = sun.awt.Win32GraphicsEnvironment
os.arch = amd64
java.endorsed.dirs = D:\professional\jdk1.8.0_131\jre\lib\endorsed
line.separator =

java.io.tmpdir = C:\Users\89354\AppData\Local\Temp\
java.vm.specification.vendor = Oracle Corporation
user.variant =
os.name = Windows 10
sun.jnu.encoding = GBK
java.library.path = D:\professional\jdk1.8.0_131\bin;C:\Windows\Sun\Java\bin;C:\Windows\system32;C:\Windows;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem
;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Windows\System32\OpenSSH\;D:\professional\Git\cmd;D:\professional\jdk1.8.0_131\bin;D:\professional\apache-maven-3.6
.3\bin;D:\professional\PuTTY\;D:\professional\nodejs\;C:\Program Files\Docker\Docker\resources\bin;C:\ProgramData\DockerDesktop\version-bin;C:\Users\89354\AppData\
Local\Microsoft\WindowsApps;D:\professional\IntelliJ IDEA\IntelliJ IDEA 2020.1\bin;C:\Users\89354\AppData\Local\GitHubDesktop\bin;C:\Users\89354\AppData\Roaming\np
m;.
java.specification.name = Java Platform API Specification
java.class.version = 52.0
sun.management.compiler = HotSpot 64-Bit Tiered Compilers
os.version = 10.0
user.home = C:\Users\89354
user.timezone = Asia/Shanghai
java.awt.printerjob = sun.awt.windows.WPrinterJob
file.encoding = UTF-8
java.specification.version = 1.8
user.name = 89354
java.class.path = D:\professional\jdk1.8.0_131\jre\lib\charsets.jar;D:\professional\jdk1.8.0_131\jre\lib\deploy.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\access
-bridge-64.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\cldrdata.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\dnsns.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\ja
ccess.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\jfxrt.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\localedata.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\nasho
rn.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\sunec.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\sunjce_provider.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\sun
mscapi.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\sunpkcs11.jar;D:\professional\jdk1.8.0_131\jre\lib\ext\zipfs.jar;D:\professional\jdk1.8.0_131\jre\lib\javaws.ja
r;D:\professional\jdk1.8.0_131\jre\lib\jce.jar;D:\professional\jdk1.8.0_131\jre\lib\jfr.jar;D:\professional\jdk1.8.0_131\jre\lib\jfxswt.jar;D:\professional\jdk1.8.
0_131\jre\lib\jsse.jar;D:\professional\jdk1.8.0_131\jre\lib\management-agent.jar;D:\professional\jdk1.8.0_131\jre\lib\plugin.jar;D:\professional\jdk1.8.0_131\jre\l
ib\resources.jar;D:\professional\jdk1.8.0_131\jre\lib\rt.jar;E:\GitHubDesktop\study\study-jvm\target\classes;D:\professional\IntelliJ IDEA\IntelliJ IDEA Community
Edition 2020.1\lib\idea_rt.jar
java.vm.specification.version = 1.8
sun.arch.data.model = 64
sun.java.command = com.bage.JpsMain
java.home = D:\professional\jdk1.8.0_131\jre
user.language = zh
java.specification.vendor = Oracle Corporation
awt.toolkit = sun.awt.windows.WToolkit
java.vm.info = mixed mode
java.version = 1.8.0_131
java.ext.dirs = D:\professional\jdk1.8.0_131\jre\lib\ext;C:\Windows\Sun\Java\lib\ext
sun.boot.class.path = D:\professional\jdk1.8.0_131\jre\lib\resources.jar;D:\professional\jdk1.8.0_131\jre\lib\rt.jar;D:\professional\jdk1.8.0_131\jre\lib\sunrsasig
n.jar;D:\professional\jdk1.8.0_131\jre\lib\jsse.jar;D:\professional\jdk1.8.0_131\jre\lib\jce.jar;D:\professional\jdk1.8.0_131\jre\lib\charsets.jar;D:\professional\
jdk1.8.0_131\jre\lib\jfr.jar;D:\professional\jdk1.8.0_131\jre\classes
java.vendor = Oracle Corporation
file.separator = \
java.vendor.url.bug = http://bugreport.sun.com/bugreport/
sun.io.unicode.encoding = UnicodeLittle
sun.cpu.endian = little
sun.desktop = windows
sun.cpu.isalist = amd64

VM Flags:
Non-default VM flags: -XX:CICompilerCount=3 -XX:InitialHeapSize=134217728 -XX:MaxHeapSize=2118123520 -XX:MaxNewSize=705691648 -XX:MinHeapDeltaBytes=524288 -XX:NewS
ize=44564480 -XX:OldSize=89653248 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps -XX:-UseLargePagesIndividualAllocation -XX
:+UseParallelGC
Command line:  -javaagent:D:\professional\IntelliJ IDEA\IntelliJ IDEA Community Edition 2020.1\lib\idea_rt.jar=63157:D:\professional\IntelliJ IDEA\IntelliJ IDEA Co
mmunity Edition 2020.1\bin -Dfile.encoding=UTF-8


```

- jinfo -flag {XXX} {vmid} 打印当前 flag 配置信息

```
>jinfo -flag PrintGC 5532
-XX:-PrintGC

```

- jinfo -flag {XXX}=XXX {vmid} 修改配置 或 jinfo -flag +{XXX} {vmid}

```
>jinfo -flag -PrintGC 5532

>jinfo -flag PrintGC 5532
-XX:-PrintGC

>jinfo -flag +PrintGC 5532

>jinfo -flag PrintGC 5532
-XX:+PrintGC


```



### Jmap  ###

reference link : https://docs.oracle.com/javase/7/docs/technotes/tools/share/jmap.html

https://www.jianshu.com/p/cf118e8f929a

Memory Map for Java 

生成堆存储快照，heapdump；查询finalize执行队列；空间使用率等等

**jmap** prints shared object memory maps or heap memory details of a given process or core file or a remote debug server. If the given process is running on a 64-bit VM, you may need to specify the *-J-d64* option, e.g.:

实现原理

通过jmap和jvm之间进行通信，有两种实现方式：attach 和 SA。

attach方式，

客户端和服务端通信模式，客户端发送请求，逻辑在服务端执行，jmap相当于客户端，JVM相当于服务端。

在JVM中，有一个叫"Attach Listener"的线程，专门负责监听attach的请求，并执行对应的操作。



比如现在执行"jmap -histo:live 5409"，一步一步的实现如下：
 1、在Jmap.java类的main函数中，对参数进行解析。
 2、解析出来参数中有“-histo:live”，则执行histo方法：

attach方法建立了jmap进程和JVM之间的socket连接，然后进行通信。

socket连接向JVM发送命令 + 参数。

虚拟机的"Attach Listener"线程处理，并将结果以字符串返回。

jmap 进行显示



假如执行"jmap -heap 5409"，就不会使用attach方式实现了。

在参数解析中，如果参数是"-heap|-heap:format=b|-permstat|-finalizerinfo"中的一种，或者添加了"-F"，比如"jmap -histo -F 5409"，则使用SA的方式。

SA方式，和attach方式不同的是，相关的主要逻辑都在SA中实现，从JVM中获取数据即可。

可以大概看下"jmap -heap"的实现，对应的实现类是"HeapSummary"，内部通过BugSpotAgent工具类attach到目标VM，更具体的底层细节，可以参考[HotSpot Serviceability Agent 实现浅析](https://link.jianshu.com?t=https%3A%2F%2Fyq.aliyun.com%2Farticles%2F20231)

执行jmap -heap有些时候可能会导致进程变T，一般是有一个线程在等信号量，这时会block住其它所有线程，可以执行kill -CONT <pid>进行恢复，不过还是强烈建议别执行这个命令。





- jmap -help 帮助

```
>jmap --help
Usage:
    jmap [option] <pid>
        (to connect to running process)
    jmap [option] <executable <core>
        (to connect to a core file)
    jmap [option] [server_id@]<remote server IP or hostname>
        (to connect to remote debug server)

where <option> is one of:
    <none>               to print same info as Solaris pmap
    -heap                to print java heap summary
    -histo[:live]        to print histogram of java object heap; if the "live"
                         suboption is specified, only count live objects
    -clstats             to print class loader statistics
    -finalizerinfo       to print information on objects awaiting finalization
    -dump:<dump-options> to dump java heap in hprof binary format
                         dump-options:
                           live         dump only live objects; if not specified,
                                        all objects in the heap are dumped.
                           format=b     binary format
                           file=<file>  dump heap to <file>
                         Example: jmap -dump:live,format=b,file=heap.bin <pid>
    -F                   force. Use with -dump:<dump-options> <pid> or -histo
                         to force a heap dump or histogram when <pid> does not
                         respond. The "live" suboption is not supported
                         in this mode.
    -h | -help           to print this help message
    -J<flag>             to pass <flag> directly to the runtime system

```

参数解析

**option：** 选项参数。

**pid：** 需要打印配置信息的进程ID。

**executable：** 产生核心dump的Java可执行文件。

**core：** 需要打印配置信息的核心文件。

**server-id** 可选的唯一id，如果相同的远程主机上运行了多台调试服务器，用此选项参数标识服务器。

**remote server IP or hostname** 远程调试服务器的IP地址或主机名。

**heap：** 显示Java堆详细信息，打印一个堆的摘要信息，包括使用的GC算法、堆配置信息和各内存区域内存使用信息

**histo[:live]：** 显示堆中对象的统计信息，其中包括每个Java类、对象数量、内存大小(单位：字节)、完全限定的类名。打印的虚拟机内部的类名称将会带有一个’*’前缀。如果指定了live子选项，则只计算活动的对象。

**clstats：**打印类加载器信息，-clstats是-permstat的替代方案，在JDK8之前，-permstat用来打印类加载器的数据
 打印Java堆内存的永久保存区域的类加载器的智能统计信息。对于每个类加载器而言，它的名称、活跃度、地址、父类加载器、它所加载的类的数量和大小都会被打印。此外，包含的字符串数量和大小也会被打印。

**finalizerinfo：** 显示在F-Queue队列等待Finalizer线程执行finalizer方法的对象，打印等待终结的对象信息

**dump:：**生成堆转储快照

**F：** 当-dump没有响应时，使用-dump或者-histo参数. 在这个模式下,live子参数无效.

**help：**打印帮助信息

**J：**指定传递给运行jmap的JVM的参数



- jmap -heap {vmid} 打印当前进程的堆栈信息

```
>jmap -heap 13716
Attaching to process ID 13716, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.131-b11

using thread-local object allocation.
Parallel GC with 4 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 2118123520 (2020.0MB)
   NewSize                  = 44564480 (42.5MB)
   MaxNewSize               = 705691648 (673.0MB)
   OldSize                  = 89653248 (85.5MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 34078720 (32.5MB)
   used     = 4776896 (4.55560302734375MB)
   free     = 29301824 (27.94439697265625MB)
   14.017240084134615% used
From Space:
   capacity = 5242880 (5.0MB)
   used     = 0 (0.0MB)
   free     = 5242880 (5.0MB)
   0.0% used
To Space:
   capacity = 5242880 (5.0MB)
   used     = 0 (0.0MB)
   free     = 5242880 (5.0MB)
   0.0% used
PS Old Generation
   capacity = 89653248 (85.5MB)
   used     = 0 (0.0MB)
   free     = 89653248 (85.5MB)
   0.0% used

3178 interned Strings occupying 260408 bytes.

```

参数解析



- jmap -histo:live {vmid} 打印存活的类信息

```
>>jmap -histo:live 13716

 num     #instances         #bytes  class name
----------------------------------------------
   1:          4702         447336  [C
   2:           417         133488  [B
   3:          4552         109248  java.lang.String
   4:           753          85792  java.lang.Class
   5:           644          42320  [Ljava.lang.Object;
   6:           836          33440  java.util.TreeMap$Entry
   7:           629          25160  java.util.LinkedHashMap$Entry
   8:           427          18992  [Ljava.lang.String;
   9:           415          13280  java.util.HashMap$Node
  10:           150           9136  [I
  11:            26           8992  [Ljava.util.HashMap$Node;
  12:           113           8136  java.lang.reflect.Field
  13:            87           5568  java.net.URL
  14:           153           4896  java.util.Hashtable$Entry
  15:           111           4440  java.lang.ref.SoftReference
  16:           256           4096  java.lang.Integer
  17:           111           3552  java.util.concurrent.ConcurrentHashMap$Node
  18:             7           2632  java.lang.Thread
  19:            42           2352  sun.misc.URLClassPath$JarLoader
  20:            26           2080  java.lang.reflect.Constructor
  21:            16           2048  [Ljava.util.concurrent.ConcurrentHashMap$Node;
  22:            39           1872  sun.util.locale.LocaleObjectCache$CacheEntry
  23:            45           1800  java.lang.ref.Finalizer
  24:            35           1680  java.util.HashMap
  25:             1           1560  [[B
  26:            95           1520  java.lang.Object
  27:            27           1512  java.lang.Class$ReflectionData
  28:            37           1480  java.io.ObjectStreamField
  29:            10           1392  [Ljava.util.Hashtable$Entry;
  30:            21           1344  java.util.concurrent.ConcurrentHashMap
  31:            15           1200  [Ljava.util.WeakHashMap$Entry;
  32:             2           1064  [Ljava.lang.invoke.MethodHandle;
  33:             1           1040  [Ljava.lang.Integer;
  34:             1           1040  [[C
  35:            41            984  java.io.ExpiringCache$Entry
  36:            15            840  sun.nio.cs.UTF_8$Encoder
  37:            12            768  java.util.jar.JarFile
  38:            19            760  sun.util.locale.BaseLocale$Key
  39:            13            728  java.util.zip.ZipFile$ZipFileInputStream
  40:            15            720  java.util.WeakHashMap
  41:            22            704  java.lang.ref.ReferenceQueue
  42:             7            672  java.util.jar.JarFile$JarFileEntry
  43:            11            648  [Ljava.lang.reflect.Field;
  44:             8            640  [S
  45:            19            608  java.io.File
  46:            19            608  java.util.Locale
  47:            19            608  sun.util.locale.BaseLocale
  48:             6            528  java.lang.reflect.Method
  49:            21            504  java.util.jar.Attributes$Name
  50:            12            480  java.security.AccessControlContext
  51:            19            456  java.util.Locale$LocaleKey
  52:            11            440  java.io.FileDescriptor
  53:            18            432  sun.misc.MetaIndex
  54:            13            392  [Ljava.io.ObjectStreamField;
  55:            20            392  [Ljava.lang.Class;
  56:             1            384  com.intellij.rt.execution.application.AppMainV2$1
  57:             1            384  java.lang.ref.Finalizer$FinalizerThread
  58:            24            384  java.lang.ref.ReferenceQueue$Lock
  59:             6            384  java.nio.DirectByteBuffer
  60:            12            384  java.util.zip.ZipCoder
  61:             1            376  java.lang.ref.Reference$ReferenceHandler
  62:             6            336  java.nio.DirectLongBufferU
  63:            10            320  java.lang.OutOfMemoryError
  64:             3            312  [D
  65:            13            312  [Ljava.lang.reflect.Constructor;
  66:            13            312  sun.reflect.NativeConstructorAccessorImpl
  67:            12            288  java.util.ArrayDeque
  68:             5            280  sun.util.calendar.ZoneInfo
  69:            11            264  java.util.ArrayList
  70:             8            256  java.util.Vector
  71:             3            240  [Ljava.lang.ThreadLocal$ThreadLocalMap$Entry;
  72:            10            240  java.security.CryptoPrimitive
  73:             5            240  java.util.Hashtable
  74:             5            240  java.util.Properties
  75:             5            240  java.util.TreeMap
  76:             6            240  java.util.WeakHashMap$Entry
  77:             7            224  java.io.FileInputStream
  78:             4            224  java.util.LinkedHashMap
  79:             2            216  [J
  80:             9            216  java.util.LinkedList$Node
  81:             3            216  java.util.regex.Pattern
  82:            13            208  sun.reflect.DelegatingConstructorAccessorImpl
  83:             5            200  java.security.ProtectionDomain
  84:             6            192  java.lang.ThreadLocal$ThreadLocalMap$Entry
  85:             2            160  [[Ljava.lang.String;
  86:             4            160  java.lang.ClassLoader$NativeLibrary
  87:             5            160  java.security.CodeSource
  88:             5            160  java.util.LinkedList
  89:             5            160  sun.util.locale.provider.LocaleProviderAdapter$Type
  90:             3            144  java.nio.HeapByteBuffer
  91:             6            144  sun.misc.PerfCounter
  92:             3            144  sun.misc.URLClassPath
  93:             6            144  sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator
  94:             2            128  java.io.ExpiringCache$1
  95:             4            128  java.util.Stack
  96:             2            128  sun.nio.cs.ext.DoubleByte$Encoder
  97:             1            120  java.net.SocksSocketImpl
  98:             5            120  java.util.Collections$UnmodifiableRandomAccessList
  99:             5            120  java.util.regex.Pattern$Slice
 100:             5            120  sun.misc.FloatingDecimal$PreparedASCIIToBinaryBuffer
 101:             2            112  [Ljava.security.CryptoPrimitive;
 102:             2            112  java.lang.Package
 103:             7            112  java.util.HashSet
 104:             2            112  java.util.ResourceBundle$CacheKey
 105:             2            112  java.util.zip.ZipFile$ZipFileInflaterInputStream
 106:             2            112  sun.nio.cs.ext.DoubleByte$Decoder
 107:             3             96  java.io.FileOutputStream
 108:             4             96  java.lang.RuntimePermission
 109:             3             96  java.lang.StringCoding$StringEncoder
 110:             2             96  java.lang.ThreadGroup
 111:             2             96  java.util.ResourceBundle$BundleReference
 112:             3             96  java.util.regex.Pattern$Curly
 113:             2             96  java.util.zip.Inflater
 114:             1             96  sun.misc.Launcher$AppClassLoader
 115:             3             96  sun.net.spi.DefaultProxySelector$NonProxyInfo
 116:             2             96  sun.nio.cs.StreamEncoder
 117:             1             88  java.net.DualStackPlainSocketImpl
 118:             1             88  sun.misc.Launcher$ExtClassLoader
 119:             5             80  [Ljava.security.Principal;
 120:             2             80  java.io.BufferedWriter
 121:             2             80  java.io.ExpiringCache
 122:             5             80  java.lang.ThreadLocal
 123:             5             80  java.security.ProtectionDomain$Key
 124:             2             80  sun.misc.FloatingDecimal$BinaryToASCIIBuffer
 125:             3             72  [Ljava.lang.reflect.Method;
 126:             2             72  [Ljava.util.regex.Pattern$Node;
 127:             3             72  java.lang.ThreadLocal$ThreadLocalMap
 128:             3             72  java.net.Proxy$Type
 129:             3             72  java.util.Arrays$ArrayList
 130:             3             72  java.util.Collections$SynchronizedSet
 131:             1             72  java.util.ResourceBundle$RBClassLoader
 132:             3             72  java.util.concurrent.atomic.AtomicLong
 133:             3             72  java.util.regex.Pattern$Ctype
 134:             3             72  java.util.regex.Pattern$Single
 135:             3             72  sun.misc.FloatingDecimal$ExceptionalBinaryToASCIIBuffer
 136:             1             72  sun.util.locale.provider.JRELocaleProviderAdapter
 137:             1             64  [F
 138:             2             64  [Ljava.lang.Thread;
 139:             2             64  java.io.FilePermission
 140:             2             64  java.io.PrintStream
 141:             2             64  java.lang.ClassValue$Entry
 142:             2             64  java.lang.StringCoding$StringDecoder
 143:             2             64  java.lang.VirtualMachineError
 144:             2             64  java.lang.ref.ReferenceQueue$Null
 145:             2             64  java.lang.ref.WeakReference
 146:             2             64  java.security.BasicPermissionCollection
 147:             2             64  java.security.Permissions
 148:             2             64  java.util.ResourceBundle$LoaderReference
 149:             2             64  java.util.regex.Pattern$Branch
 150:             2             48  java.io.BufferedOutputStream
 151:             1             48  java.io.BufferedReader
 152:             2             48  java.io.File$PathStatus
 153:             2             48  java.io.FilePermissionCollection
 154:             2             48  java.io.OutputStreamWriter
 155:             1             48  java.lang.ProcessEnvironment
 156:             2             48  java.net.InetAddress$Cache
 157:             2             48  java.net.InetAddress$Cache$Type
 158:             1             48  java.net.SocketInputStream
 159:             1             48  java.nio.HeapCharBuffer
 160:             2             48  java.nio.charset.CoderResult
 161:             3             48  java.nio.charset.CodingErrorAction
 162:             2             48  java.util.regex.Pattern$GroupHead
 163:             2             48  java.util.regex.Pattern$GroupTail
 164:             2             48  java.util.regex.Pattern$SliceI
 165:             2             48  java.util.regex.Pattern$Start
 166:             2             48  java.util.zip.ZStreamRef
 167:             2             48  sun.misc.NativeSignalHandler
 168:             2             48  sun.misc.Signal
 169:             3             48  sun.net.www.protocol.jar.Handler
 170:             1             48  sun.nio.cs.StreamDecoder
 171:             1             48  sun.nio.cs.US_ASCII$Decoder
 172:             2             48  sun.reflect.NativeMethodAccessorImpl
 173:             1             48  sun.util.locale.provider.LocaleResources$ResourceReference
 174:             1             48  sun.util.resources.TimeZoneNames
 175:             1             48  sun.util.resources.en.TimeZoneNames_en
 176:             1             40  [Lsun.security.util.DisabledAlgorithmConstraints$Constraint$Operator;
 177:             1             40  [Lsun.util.locale.provider.LocaleProviderAdapter$Type;
 178:             1             40  java.io.BufferedInputStream
 179:             1             40  java.util.ResourceBundle$1
 180:             1             40  sun.nio.cs.StandardCharsets$Aliases
 181:             1             40  sun.nio.cs.StandardCharsets$Cache
 182:             1             40  sun.nio.cs.StandardCharsets$Classes
 183:             1             40  sun.nio.cs.UTF_8$Decoder
 184:             1             40  sun.nio.cs.ext.ExtendedCharsets
 185:             1             32  [Ljava.lang.OutOfMemoryError;
 186:             2             32  [Ljava.lang.StackTraceElement;
 187:             1             32  [Ljava.lang.ThreadGroup;
 188:             1             32  [Ljava.net.Proxy$Type;
 189:             1             32  java.io.WinNTFileSystem
 190:             1             32  java.lang.ArithmeticException
 191:             2             32  java.lang.Boolean
 192:             1             32  java.lang.NullPointerException
 193:             1             32  java.net.InetAddress$InetAddressHolder
 194:             1             32  java.net.Socket
 195:             2             32  java.nio.ByteOrder
 196:             1             32  java.util.Collections$UnmodifiableMap
 197:             2             32  java.util.LinkedHashMap$LinkedKeySet
 198:             1             32  java.util.RegularEnumSet
 199:             2             32  java.util.concurrent.atomic.AtomicInteger
 200:             1             32  java.util.concurrent.atomic.AtomicReferenceFieldUpdater$AtomicReferenceFieldUpdaterImpl
 201:             1             32  java.util.regex.Pattern$BnM
 202:             2             32  java.util.regex.Pattern$BranchConn
 203:             1             32  sun.instrument.InstrumentationImpl
 204:             1             32  sun.nio.cs.StandardCharsets
 205:             2             32  sun.reflect.DelegatingMethodAccessorImpl
 206:             1             32  sun.security.util.DisabledAlgorithmConstraints$KeySizeConstraint
 207:             1             32  sun.util.locale.provider.LocaleResources
 208:             1             32  sun.util.locale.provider.LocaleServiceProviderPool
 209:             1             24  [Ljava.io.File$PathStatus;
 210:             1             24  [Ljava.lang.ClassValue$Entry;
 211:             1             24  [Ljava.net.InetAddress$Cache$Type;
 212:             1             24  [Ljava.security.ProtectionDomain;
 213:             1             24  [Lsun.launcher.LauncherHelper;
 214:             1             24  java.io.InputStreamReader
 215:             1             24  java.lang.ClassValue$Version
 216:             1             24  java.lang.StringBuilder
 217:             1             24  java.lang.invoke.MethodHandleImpl$4
 218:             1             24  java.lang.reflect.ReflectPermission
 219:             1             24  java.net.Inet4Address
 220:             1             24  java.net.Inet6AddressImpl
 221:             1             24  java.net.Proxy
 222:             1             24  java.util.BitSet
 223:             1             24  java.util.Collections$EmptyMap
 224:             1             24  java.util.Collections$SetFromMap
 225:             1             24  java.util.Locale$Cache
 226:             1             24  java.util.ResourceBundle$Control$CandidateListCache
 227:             1             24  java.util.jar.Manifest
 228:             1             24  sun.instrument.TransformerManager
 229:             1             24  sun.launcher.LauncherHelper
 230:             1             24  sun.misc.JarIndex
 231:             1             24  sun.misc.URLClassPath$FileLoader
 232:             1             24  sun.nio.cs.ISO_8859_1
 233:             1             24  sun.nio.cs.ThreadLocalCoders$1
 234:             1             24  sun.nio.cs.ThreadLocalCoders$2
 235:             1             24  sun.nio.cs.US_ASCII
 236:             1             24  sun.nio.cs.UTF_16
 237:             1             24  sun.nio.cs.UTF_16BE
 238:             1             24  sun.nio.cs.UTF_16LE
 239:             1             24  sun.nio.cs.UTF_8
 240:             1             24  sun.nio.cs.ext.GBK
 241:             1             24  sun.security.util.DisabledAlgorithmConstraints
 242:             1             24  sun.util.locale.BaseLocale$Cache
 243:             1             24  sun.util.locale.provider.TimeZoneNameProviderImpl
 244:             1             16  [Ljava.lang.Enum;
 245:             1             16  [Ljava.lang.Throwable;
 246:             1             16  [Ljava.security.cert.Certificate;
 247:             1             16  [Lsun.instrument.TransformerManager$TransformerInfo;
 248:             1             16  java.io.FileDescriptor$1
 249:             1             16  java.lang.CharacterDataLatin1
 250:             1             16  java.lang.ClassValue$Identity
 251:             1             16  java.lang.ProcessEnvironment$EntryComparator
 252:             1             16  java.lang.ProcessEnvironment$NameComparator
 253:             1             16  java.lang.Runtime
 254:             1             16  java.lang.String$CaseInsensitiveComparator
 255:             1             16  java.lang.System$2
 256:             1             16  java.lang.Terminator$1
 257:             1             16  java.lang.invoke.MemberName$Factory
 258:             1             16  java.lang.invoke.MethodHandleImpl$2
 259:             1             16  java.lang.invoke.MethodHandleImpl$3
 260:             1             16  java.lang.ref.Reference$1
 261:             1             16  java.lang.ref.Reference$Lock
 262:             1             16  java.lang.reflect.ReflectAccess
 263:             1             16  java.net.InetAddress$2
 264:             1             16  java.net.URLClassLoader$7
 265:             1             16  java.nio.Bits$1
 266:             1             16  java.nio.charset.CoderResult$1
 267:             1             16  java.nio.charset.CoderResult$2
 268:             1             16  java.security.ProtectionDomain$2
 269:             1             16  java.security.ProtectionDomain$JavaSecurityAccessImpl
 270:             1             16  java.util.Collections$EmptyIterator
 271:             1             16  java.util.Collections$EmptyList
 272:             1             16  java.util.Collections$EmptySet
 273:             1             16  java.util.Collections$UnmodifiableSet
 274:             1             16  java.util.HashMap$EntrySet
 275:             1             16  java.util.Hashtable$EntrySet
 276:             1             16  java.util.ResourceBundle$Control
 277:             1             16  java.util.WeakHashMap$KeySet
 278:             1             16  java.util.concurrent.atomic.AtomicBoolean
 279:             1             16  java.util.jar.Attributes
 280:             1             16  java.util.jar.JavaUtilJarAccessImpl
 281:             1             16  java.util.regex.Pattern$4
 282:             1             16  java.util.regex.Pattern$LastNode
 283:             1             16  java.util.regex.Pattern$Node
 284:             1             16  java.util.zip.ZipFile$1
 285:             1             16  sun.misc.ASCIICaseInsensitiveComparator
 286:             1             16  sun.misc.FloatingDecimal$1
 287:             1             16  sun.misc.Launcher
 288:             1             16  sun.misc.Launcher$Factory
 289:             1             16  sun.misc.Perf
 290:             1             16  sun.misc.Unsafe
 291:             1             16  sun.net.spi.DefaultProxySelector
 292:             1             16  sun.net.www.protocol.file.Handler
 293:             1             16  sun.reflect.ReflectionFactory
 294:             1             16  sun.security.util.AlgorithmDecomposer
 295:             1             16  sun.security.util.DisabledAlgorithmConstraints$Constraints
 296:             1             16  sun.util.calendar.Gregorian
 297:             1             16  sun.util.locale.provider.AuxLocaleProviderAdapter$NullProvider
 298:             1             16  sun.util.locale.provider.SPILocaleProviderAdapter
 299:             1             16  sun.util.locale.provider.TimeZoneNameUtility$TimeZoneNameGetter
 300:             1             16  sun.util.resources.LocaleData
 301:             1             16  sun.util.resources.LocaleData$LocaleDataResourceBundleControl
Total         15713        1016880

```

参数说明



- jmap -clstats {vmid} 查看类加载器情况

```
>jmap -clstats 13716

Attaching to process ID 13716, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.131-b11
finding class loader instances ..done.
computing per loader stat ..done.
please wait.. computing liveness...............................................done.
class_loader    classes bytes   parent_loader   alive?  type

<bootstrap>     663     1228703   null          live    <internal>
0x0000000081ceeaa8      0       0       0x0000000081c09288      live    java/util/ResourceBundle$RBClassLoader@0x000000010006dd08
0x0000000081c09288      26      78403   0x0000000081c092f8      live    sun/misc/Launcher$AppClassLoader@0x000000010000f6a0
0x0000000081c092f8      0       0         null          live    sun/misc/Launcher$ExtClassLoader@0x000000010000fa48

total = 4       689     1307106     N/A         alive=4, dead=0     N/A


```

参数解析



- jmap -finalizerinfo{vmid} 查看回收队列情况

```
>jmap -finalizerinfo 13716

Attaching to process ID 13716, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.131-b11
Number of objects pending for finalization: 0

```

参数解析



- jmap -dump {vmid} 生成dump 文件

```
>jmap -dump:format=b,file=dump.hprof 13716

Dumping heap to E:\GitHubDesktop\study\dump.hprof ...
Heap dump file created

```

参数解析

Jvm 对象分配过程

https://github.com/bage2014/interview/blob/master/README-Jvm.md



## Arthas ##

## Debug ##


参考链接：http://blog.kail.xyz/post/2017-02-17/java/java-use-jpda-remote-debug.html

Debug参数说明：
 -Xrunjdwp                   # 启用JDWP实现，它包含若干子选项：
    transport=dt_socket     # JPDA front-end和back-end之间的传输方法。dt_socket表示使用套接字传输。
    address=8000            # JVM在8000端口上监听请求。
    server=y                # y表示启动的JVM是被调试者。如果为n，则表示启动的JVM是调试器。
    suspend=n               # y表示启动的JVM会暂停等待，直到调试器连接上，n表示不暂停等待 
	
	
 Java IDE，比如 Netbeans 和 IntelliJ 等等也都提供了类似的功能，您甚至能不用 IDE 提供的图形界面，使用 JDK 自带的 jdb 工具，以文本命令的形式来调试您的 Java 程序

 Java 平台调试体系结构（JPDA）工具集


图JPDA 模块层次：
https://developer.ibm.com/developer/default/articles/j-lo-jpda1/images/jdwp.jpg


Java 的 JPDA 就是一套为调试和优化服务的虚拟机的操作工具，其中，JVMTI 是整合在虚拟机中的接口，JDWP 是一个通讯层，而 JDI 是前端为开发人员准备好的工具和运行库


Java 虚拟机工具接口（JVMTI）


JVMTI（Java Virtual Machine Tool Interface）


它是一套由虚拟机直接提供的 native 接口，它处于整个 JPDA 体系的最底层，所有调试功能本质上都需要通过 JVMTI 来提供。通过这些接口，开发人员不仅调试在该虚拟机上运行的 Java 程序，还能查看它们运行的状态，设置回调函数，控制某些环境变量，从而优化程序性能。我们知道，JVMTI 的前身是 JVMDI 和 JVMPI，它们原来分别被用于提供调试 Java 程序以及 Java 程序调节性能的功能。



Java 调试线协议（JDWP）
JDWP（Java Debug Wire Protocol）是一个为 Java 调试而设计的一个通讯交互协议，它定义了调试器和被调试程序之间传递的信息的格式
详细完整地定义了请求命令、回应数据和错误代码，保证了前端和后端的 JVMTI 和 JDI 的通信通畅


Java 调试接口（JDI）
JDI（Java Debug Interface）由 Java 语言实现的。 JDI 由针对前端定义的接口组成，通过它，调试工具开发人员就能通过前端虚拟机上的调试器来远程操控后端虚拟机上被调试程序的运行，JDI 不仅能帮助开发人员格式化 JDWP 数据，而且还能为 JDWP 数据传输提供队列、缓存等优化服务。从理论上说，开发人员只需使用 JDWP 和 JVMTI 即可支持跨平台的远程调试，但是直接编写 JDWP 程序费时费力，而且效率不高。因此基于 Java 的 JDI 层的引入，简化了操作，提高了开发人员开发调试程序的效率。


模块	层次	编程语言	作用
JVMTI	底层	C	获取及控制当前虚拟机状态
JDWP	中介层	C	定义 JVMTI 和 JDI 交互的数据格式
JDI	高层	Java	提供 Java API 来远程控制被调试虚拟机




JVMTI 基本功能
JVMTI 的功能非常丰富，包含了虚拟机中线程、内存 / 堆 / 栈，类 / 方法 / 变量，事件 / 定时器处理等等 20 多类功能，下面我们介绍一下，并举一些简单列子



设置回调函数的时候，开发者需要注意以下几点：

如同 Java 异常机制一样，如果在回调函数中自己抛出一个异常（Exception），或者在调用 JNI 函数的时候制造了一些麻烦，让 JNI 丢出了一个异常，那么任何在回调之前发生的异常就会丢失，这就要求开发人员要在处理错误的时候需要当心。
虚拟机不保证回调函数会被同步，换句话说，程序有可能同时运行同一个回调函数（比如，好几个线程同时开始运行了，这个 HandleThreadStart 就会被同时调用几次），那么开发人员在开发回调函数时需要处理同步的问题。


