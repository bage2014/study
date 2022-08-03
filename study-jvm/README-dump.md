# study-jvm-dump #

Jvm 实践笔记

https://article.itxueyuan.com/amLr8x
生成 dump 文件 

## 主动生成

 **先找到PID**

ps -ef | grep java

 **jmap 转存快照**
jmap -dump:format=b,file=/opt/dump/test.dump {PID}



## 被动生成

  当程序出现OutofMemory时，将会在相应的目录下生成一份dump文件，如果不指定选项HeapDumpPath则在当前目录下生成dump文件
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/dumps



## 修改内存

多的内存资源。两种方式分配内存资源给 MAT：
1）修改启动参数 MemoryAnalyzer.exe -vmargs -Xmx4g
2）编辑文件 MemoryAnalyzer.ini 添加 -vmargs – Xmx4g



