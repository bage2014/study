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

Jvm 实时信息



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
- -h 可以在周期性数据数据的时候，可以在指定输出多少行以后输出一次表头
- vmid 必填，Jps 的Java进程号，通过 Jps 命令可以得到
- interval 周期间隔时间
- count 统计次数，默认一直打印



- jstat -options 查看选项

```
>jstat -options
-class
-compiler
-gc
-gccapacity
-gccause
-gcmetacapacity
-gcnew
-gcnewcapacity
-gcold
-gcoldcapacity
-gcutil
-printcompilation

```



- jstat -class {vmid} 类加载情况

```
>jstat -class 7476
Loaded  Bytes  Unloaded  Bytes     Time
 12913 22951.1        0     0.0      17.40

```

参数解析

- Loaded 数量
- Bytes 大小
- Unloaded 未加载数量
- Bytes 未加载大小
- Time 加载时间



- jstat -compiler {vmid}  编译情况
- 

```
>jstat -compiler 7476
Compiled Failed Invalid   Time   FailedType FailedMethod
    7076      1       0    62.44          1 org/springframework/core/annotation/AnnotatedElementUtils searchWithGetSemanticsInAnnotations

```

参数解析

- Compiled 数量
- Failed 数量
- Invalid 不可用数量
- Time 时间
- FailedType 失败类型
- FailedMethod 失败的方法





- jstat -gc {vmid} 垃圾回收情况

```
>jstat -gc 7476
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
18944.0 26624.0  0.0    0.0   635904.0 298351.9  150016.0   47957.1   59008.0 57022.0 7808.0 7453.1     18    0.215   3      0.414    0.629

```

参数解析

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



### Jinfo  ###

reference link : https://docs.oracle.com/javase/7/docs/technotes/tools/share/jinfo.html

Configuration Info

实时查看JVM虚拟机的参数并进行调整





## Arthas ##



