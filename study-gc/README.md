# study-gc

同一份代码，不同的发布环境，

- 不同GC 回收器
- GC日志 
- GC 过程

- Jdk8、Jdk11、Jdk17 环境下

  

情况对比



## 参考链接

https://github.com/tusharjoshi024/jvisualvm-demo



## 启动参数

```
jdk8 :
java -jar -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation  -XX:+PrintHeapAtGC -XX:NumberOfGCLogFiles=100 -XX:GCLogFileSize=10M -Xloggc:my-gc-%t.gc.log -Xms64m -Xmx256m target/study-best-practice-0.0.1-SNAPSHOT.jar

jdk17 :
java -jar -Xlog:gc:my-gc.log:time,level -Xms64m -Xmx256m target/study-best-practice-0.0.1-SNAPSHOT.jar
```



## 内存增加

### 基本功能

整个过程可以打开jconsole 观察情况 



### **内存相关**

```
new 然后释放
http://localhost:8017/jvm/newAndFinish?step=10

runtime info
http://localhost:8017/jvm/info

new 放到map中
http://localhost:8017/jvm/add?step=10


```



### **CPU相关**

#### 环境准备

```
heavy 线程
http://localhost:8017/cpu/high/heavy/start

http://localhost:8017/cpu/high/heavy/stop


light 线程
http://localhost:8017/cpu/high/light/start

http://localhost:8017/cpu/high/light/stop


```



#### 生成火焰图

```
启动arthas 

java -jar arthas-boot.jar
```



生成

```
$ profiler start

Profiling started

[arthas@7778]$ profiler stop

OK

profiler output file: /Users/bage/bage/github/study/arthas-output/20230920-093041.html


访问：
http://localhost:3658/arthas-output/
或者 
/Users/bage/bage/github/study/arthas-output/20230920-093041.html

```

