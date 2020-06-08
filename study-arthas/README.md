# study-arthas #
阿里巴巴 Arthas 使用

当你遇到以下类似问题而束手无策时，`Arthas`可以帮助你解决：

1. 这个类从哪个 jar 包加载的？为什么会报各种类相关的 Exception？
2. 我改的代码为什么没有执行到？难道是我没 commit？分支搞错了？
3. 遇到问题无法在线上 debug，难道只能通过加日志再重新发布吗？
4. 线上遇到某个用户的数据处理有问题，但线上同样无法 debug，线下无法重现！
5. 是否有一个全局视角来查看系统的运行状况？
6. 有什么办法可以监控到JVM的实时运行状态？
7. 怎么快速定位应用的热点，生成火焰图？

## 参考链接 ##

- github [https://github.com/alibaba/arthas](https://github.com/alibaba/arthas)
- Arthas [https://alibaba.github.io/arthas/](https://alibaba.github.io/arthas/)
- 在线操作验证 [https://alibaba.github.io/arthas/arthas-tutorials?language=cn](https://alibaba.github.io/arthas/arthas-tutorials?language=cn)

## 快速安装

    curl -O https://alibaba.github.io/arthas/arthas-boot.jar
    java -jar arthas-boot.jar

## 启动Demo

    curl -O https://alibaba.github.io/arthas/arthas-demo.jar
    java -jar arthas-demo.jar


​    
## Attach 
在命令行下面执行（使用和目标进程一致的用户启动，否则可能attach失败）：

    curl -O https://alibaba.github.io/arthas/arthas-boot.jar
    java -jar arthas-boot.jar


## dashboard  
输入dashboard，按回车/enter，会展示当前进程的信息，按ctrl+c可以中断执行。
    
          $ dashboard
          ID     NAME                   GROUP          PRIORI STATE  %CPU    TIME   INTERRU DAEMON
          17     pool-2-thread-1        system         5      WAITIN 67      0:0    false   false
          27     Timer-for-arthas-dashb system         10     RUNNAB 32      0:0    false   true
          11     AsyncAppender-Worker-a system         9      WAITIN 0       0:0    false   true
          9      Attach Listener        system         9      RUNNAB 0       0:0    false   true
          3      Finalizer              system         8      WAITIN 0       0:0    false   true
          2      Reference Handler      system         10     WAITIN 0       0:0    false   true
          4      Signal Dispatcher      system         9      RUNNAB 0       0:0    false   true
          26     as-command-execute-dae system         10     TIMED_ 0       0:0    false   true
          13     job-timeout            system         9      TIMED_ 0       0:0    false   true
          1      main                   main           5      TIMED_ 0       0:0    false   false
          14     nioEventLoopGroup-2-1  system         10     RUNNAB 0       0:0    false   false
          18     nioEventLoopGroup-2-2  system         10     RUNNAB 0       0:0    false   false
          23     nioEventLoopGroup-2-3  system         10     RUNNAB 0       0:0    false   false
          15     nioEventLoopGroup-3-1  system         10     RUNNAB 0       0:0    false   false
          Memory             used   total max    usage GC
          heap               32M    155M  1820M  1.77% gc.ps_scavenge.count  4
          ps_eden_space      14M    65M   672M   2.21% gc.ps_scavenge.time(m 166
          ps_survivor_space  4M     5M    5M           s)
          ps_old_gen         12M    85M   1365M  0.91% gc.ps_marksweep.count 0
          nonheap            20M    23M   -1           gc.ps_marksweep.time( 0
          code_cache         3M     5M    240M   1.32% ms)
          Runtime
          os.name                Mac OS X
          os.version             10.13.4
          java.version           1.8.0_162
          java.home              /Library/Java/JavaVir
                                 tualMachines/jdk1.8.0
                                 _162.jdk/Contents/Hom
                                 e/jre

## thread
thread {threadId} 会打印线程的栈
         
         $ thread 10086 | grep 'main('
             at demo.MathGame.main(MathGame.java:17)

## watch
通过watch命令来查看demo.MathGame#primeFactors函数的返回值：

    $ watch demo.MathGame primeFactors returnObj
    Press Ctrl+C to abort.
    Affect(class-cnt:1 , method-cnt:1) cost in 107 ms.
    ts=2018-11-28 19:22:30; [cost=1.715367ms] result=null
    ts=2018-11-28 19:22:31; [cost=0.185203ms] result=null
    ts=2018-11-28 19:22:32; [cost=19.012416ms] result=@ArrayList[
        @Integer[5],
        @Integer[47],
        @Integer[2675531],
    ]


​    
## 退出
如果只是退出当前的连接，可以用quit或者exit命令。Attach到目标进程上的arthas还会继续运行，端口会保持开放，下次连接时可以直接连接上。
如果想完全退出arthas，可以执行stop命令。  
                       
