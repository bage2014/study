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

- option jstat 选项，比如 gc、class、compiler 等；可以通过  查看
- -t 显示时间戳
- -h 可以在周期性数据数据的时候，可以在指定输出多少行以后输出一次表头
- vmid 必填，Jps 的Java进程号
- interval 周期间隔时间
- count 统计次数，默认一直打印









## Arthas ##



