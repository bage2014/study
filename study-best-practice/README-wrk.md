# study-best-practice

wrk 




## GC频繁

基本使用

```
wrk -t12 -c400 -d30s http://127.0.0.1:8080/index.html


```

参数说明 

```
-c,    --connections（连接数）:      total number of HTTP connections to keep open with each thread handling N = connections/threads

-d,    --duration（测试持续时间）:     duration of the test, e.g. 2s, 2m, 2h

-t,    --threads（线程）:            total number of threads to use

-s,    --script（脚本）:             LuaJIT script, see SCRIPTING

-H,    --header（头信息）:           HTTP header to add to request, e.g. "User-Agent: wrk"

       --latency（响应信息）:         print detailed latency statistics

       --timeout（超时时间）:         record a timeout if a response is not received within this amount of time.
```



响应信息

```
Running 30s test @ http://127.0.0.1:8080/index.html
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   635.91us    0.89ms  12.92ms   93.69%
    Req/Sec    56.20k     8.07k   62.00k    86.54%
  22464657 requests in 30.00s, 17.76GB read
Requests/sec: 748868.53
Transfer/sec:    606.33MB

```

解释说明

```

Latency: 响应信息, 包括平均值, 标准偏差, 最大值, 正负一个标准差占比。

Req/Sec: 每个线程每秒钟的完成的请求数，同样有平均值，标准偏差，最大值，正负一个标准差占比。

30秒钟总共完成请求数为22464657，读取数据量为17.76GB。

线程总共平均每秒钟处理748868.53个请求（QPS）,每秒钟读取606.33MB数据量。

```







## 参考链接

https://www.jianshu.com/p/686233ca909e

https://zhuanlan.zhihu.com/p/428731068

JVM 相关 https://www.kancloud.cn/luoyoub/jvm-note/1890187



