# study-jvm #

Jvm 实践笔记

ref [https://www.cnblogs.com/redcreen/](https://www.cnblogs.com/redcreen/)

[https://www.cnblogs.com/snowwhite/p/9569443.html](https://www.cnblogs.com/snowwhite/p/9569443.html)



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

Java Virtual Machine statistics monitoring tool

Jvm 虚拟机实时信息，内存，垃圾回收等

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

Memory Map for Java 

生成堆存储快照，heapdump；查询finalize执行队列；空间使用率等等

**jmap** prints shared object memory maps or heap memory details of a given process or core file or a remote debug server. If the given process is running on a 64-bit VM, you may need to specify the *-J-d64* option, e.g.:



- jmap--help 帮助

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







## Arthas ##



