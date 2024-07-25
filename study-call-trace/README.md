# Study-call-trace

Java 调用链路

生成调用关系 



## 参考链接

官网文档 

https://github.com/Adrninistrator/java-all-call-graph

参考文档 
https://github.com/Adrninistrator/java-callgraph2

https://github.com/gousiosg/java-callgraph

ASM 增强
https://github.com/avitkus/ASM-method-tracer/

newrelic
https://one.newrelic.com/catalog-pack-details?account=4557803&state=de6032a3-52a4-0e44-7cdc-bfb0a8f12b74

datadog
https://docs.datadoghq.com/tracing/trace_collection/automatic_instrumentation/dd_libraries/java/?tab=wget

## 快速开始
https://github.com/Adrninistrator/java-all-call-graph/blob/main/quick_start.md


下载运行

https://central.sonatype.com/artifact/com.github.adrninistrator/java-all-call-graph?smo=true

```

```

## 应用实践

下载运行

```
Publish Remote

```

## 原理解析

基本原理



## 参考链接

在获取Java方法调用关系时，使用了 https://github.com/gousiosg/java-callgraph项目，并对其进行了增强，java-callgraph使用Apache Commons BCEL（Byte Code Engineering Library）解析Java方法调用关系，Matthieu Vergne（https://www.matthieu-vergne.fr/）为该项目增加了解析动态调用的能力（lambda表达式等）。

