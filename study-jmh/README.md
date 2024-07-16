# study-jmh

## 参考链接

GitHub 
https://github.com/openjdk/jmh/tree/master/jmh-samples/src/main/java/org/openjdk/jmh/samples

官网文档 
https://openjdk.org/projects/code-tools/jmh/

## 快速开始

http://imyhq.com/software-engineering/4517.html

https://cloud.tencent.com/developer/article/2100320

https://juejin.cn/post/6990031829071822856


## 代码样例 

https://hg.openjdk.org/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/

## 应用实践

Warmup 预热

Measurement 执行

Timeout 超时

Threads 线程



下载运行

```
# JMH version: 1.36
# VM version: JDK 1.8.0_382, OpenJDK 64-Bit Server VM, 25.382-b05
# VM invoker: ~/software/zulu-8.jdk/Contents/Home/jre/bin/java
# VM options: -Ddigma.flavor=Default -javaagent:/var/folders/cq/j95gptn923n2rg0f3gr8p0r80000gn/T/digma-otel-jars/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/var/folders/cq/j95gptn923n2rg0f3gr8p0r80000gn/T/digma-otel-jars/digma-otel-agent-extension.jar -Dotel.exporter.otlp.traces.endpoint=http://localhost:5050 -Dotel.service.name=study-jmh -Dotel.traces.exporter=otlp -Dotel.exporter.otlp.protocol=grpc -Dotel.metrics.exporter=none -Dotel.logs.exporter=none -Dotel.instrumentation.common.experimental.controller.telemetry.enabled=true -Dotel.instrumentation.common.experimental.view.telemetry.enabled=true -Dotel.instrumentation.experimental.span-suppression-strategy=none -Dotel.instrumentation.digma-methods.enabled=false -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/lib/idea_rt.jar=63512:/Applications/IntelliJ IDEA CE.app/Contents/bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.bage.study.jmh.JMHSample_01_HelloWorld.wellHelloThere

# Run progress: 0.00% complete, ETA 00:01:40
# Fork: 1 of 1
Picked up JAVA_TOOL_OPTIONS:  -Ddigma.flavor=Default  -javaagent:/var/folders/cq/j95gptn923n2rg0f3gr8p0r80000gn/T/digma-otel-jars/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/var/folders/cq/j95gptn923n2rg0f3gr8p0r80000gn/T/digma-otel-jars/digma-otel-agent-extension.jar -Dotel.exporter.otlp.traces.endpoint=http://localhost:5050 -Dotel.service.name=study-jmh -Dotel.traces.exporter=otlp -Dotel.exporter.otlp.protocol=grpc -Dotel.metrics.exporter=none -Dotel.logs.exporter=none -Dotel.instrumentation.common.experimental.controller.telemetry.enabled=true -Dotel.instrumentation.common.experimental.view.telemetry.enabled=true -Dotel.instrumentation.experimental.span-suppression-strategy=none -Dotel.instrumentation.digma-methods.enabled=false 
[otel.javaagent 2024-07-16 09:08:40:402 +0800] [main] INFO io.opentelemetry.javaagent.tooling.VersionLogger - opentelemetry-javaagent - version: 2.1.0
[otel.javaagent 2024-07-16 09:08:40:472 +0800] [main] INFO com.digma.otel.javaagent.extension.DigmaVersionLogger - Digma-Agent-Extension - version: 0.8.13
# Warmup Iteration   1: 1856532681.727 ops/s
# Warmup Iteration   2: 1857609280.093 ops/s
# Warmup Iteration   3: 1861650369.108 ops/s
# Warmup Iteration   4: 1859323883.200 ops/s
# Warmup Iteration   5: 1860567137.352 ops/s
Iteration   1: 1860788209.686 ops/s
Iteration   2: 1859441716.549 ops/s
Iteration   3: 1862499568.116 ops/s
Iteration   4: 1859791837.313 ops/s
Iteration   5: 1862781324.711 ops/s


Result "com.bage.study.jmh.JMHSample_01_HelloWorld.wellHelloThere":
  1861060531.275 ±(99.9%) 5882878.402 ops/s [Average]
  (min, avg, max) = (1859441716.549, 1861060531.275, 1862781324.711), stdev = 1527764.839
  CI (99.9%): [1855177652.873, 1866943409.677] (assumes normal distribution)


# Run complete. Total time: 00:01:47

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                                Mode  Cnt           Score         Error  Units
JMHSample_01_HelloWorld.wellHelloThere  thrpt    5  1861060531.275 ± 5882878.402  ops/s

Process finished with exit code 0

```

## 原理解析

基本原理