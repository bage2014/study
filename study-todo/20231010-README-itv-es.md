# study-itv-es #

ES



## 关键点

- 核心组件
- 角色节点
- 集群结构
- lucence内部结构、全文搜索原理
- 倒排索引
- 常用组件概念
- 写入过程
- 查询过程
- FST 数据结构 
- ES 集群节点扩容
- master选举过程
- 并发情况下保证读写一致
- Elasticsearch 索引数据多了怎么办呢，如何调优，部署
- Elasticsearch 对于大数据量（上亿量级）的聚合如何实现？
- 海量数据查询
- 日志存储实践



## 安装

参考 https://github.com/bage2014/study/tree/master/study-docker



## 使用场景 

### 网站搜索 

设计了丰富的api来提供搜索服务，github、stackoverflow等网站的搜索都是基于elasticsearch。 

### 日志 

将分散的日志，集中化存储到elasticsearch上。 

日志管理一般分为：日志收集，格式化，检索，风险告警。 

### 数据库同步 

通过某种同步机制将数据库某个表的数据同步到elasticsearch上，然后提供搜索服务。 

### 指标分析

提供了分组查询、top查询、排序、相关度打分，可以进行数据分析



## 常用API 

### 基本API

**健康检测**

节点状态

```undefined
http://localhost:9092/?pretty
```

集群状态

```undefined
http://localhost:9092/_cat/health?v&pretty

http://localhost:9092/_cat/nodes?pretty
```

查看索引

```undefined
http://localhost:9092/_cat/indices?v&pretty
```



分词器

```undefined
http://localhost:9092/??
```





### CRUD

增删改查操作

https://www.elastic.co/guide/en/elasticsearch/reference/8.10/docs.html



// 	query 

```
http://localhost:9092/persons/_doc/1716165342474?pretty

http://localhost:9092/{index}/_doc/{id}

```







## 基本原理

整体架构

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4016a5cd5b8c4b07aa3eb6b478a4588f~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)

如上图所示，elasticsearch整体的功能划分：

1. restful api，表示提供rest风格的api来实现编程何管理
2. Transport, 网络传输模块，支持http，thrift等主流协议，默认tcp
3. Scripting，脚本语言，支持使用groovy、painless等脚本语言
4. 3rd plugin，支持第三方插件
5. Index module 索引文档、search module，搜索文档
6. River，支持接入其它数据源
7. Jmx，监控
8. Discovery，服务发现模块
9. Lucene directory，底层存储基于lucene实现
10. Gateway，可以持久化到本地或者其它文件系统



## 名词解释

elasticsearch核心概念 vs. 数据库核心概念

| Elasticsearch | 数据库 |
| :------------ | :----- |
| Document      | 行     |
| Type          | 表     |
| Index         | 库     |

文档 

可搜索的最小单位，我们向elasticsearch存储的一条数据，就是一个文档。每个文档都有一个id，可以自己指定，也可以让elasticsearch生成。 

索引 

索引是文档的容器，一类文档的集合。可以对文档元数据进行定义，比如名称、类型等。在物理上，索引的数据分布在分片上。

Type 

在7.0以前，一个索引可以定义多个type，7.0版本后，type废除了，只有一个type为“_doc”。

## master选举过程



## FST 数据结构 

https://article.juejin.cn/post/7244335987576602680

http://examples.mikemccandless.com/fst.py?terms=mop%2F0%0D%0Acat%2F1%0D%0Adog%2F2%0D%0Amemory%2F3%0D%0Amax%2F4&cmd=Build+it%21



## 角色节点划分

节点概念和划分 https://blog.csdn.net/wlei0618/article/details/127371710

更细维度划分 https://blog.csdn.net/weixin_36340771/article/details/121766890

### master

master node： 索引的创建与删除

应避免重任务

### data

data node：存储索引数据、对文档数据增删查改 

### cordinate

cordinate node：协调节点，接收用户请求、转发请求、汇总结果

### ingest

ingest node：拦截请求，对文档进行转换和预处理

### 等等



## 倒排索引 

官网 

https://www.elastic.co/guide/cn/elasticsearch/guide/current/inverted-index.html

https://zhuanlan.zhihu.com/p/28320841?utm_id=0

https://juejin.cn/post/6971054573976813582

#### 基本概念

Elasticsearch的倒排索引就是文档的单词和文档的id关联

倒排索引（英文：Inverted Index），是一种索引方法，常被用于全文检索系统中的一种单词文档映射结构

关键词——文档 === 形式的一种映射结构

逆向思维运算，是现代信息检索领域里面最有效的一种索引

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5a99164a07074dac9cc38f04a6c8d642~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)





#### 数据结构 

FST

https://zhuanlan.zhihu.com/p/612483065

## 组件概念

```
1、索引 index ： 关系型数据库中的 table

2、文档 document ： row

3、字段 field text\keyword\byte ： 列

4、映射Mapping ： Schema。

5、查询方式 DSL ： SQL ES的新版本也支持SQL

6、分片 sharding 和 副本 replicas： index都是由sharding组成的。每个sharding都有一个或多个备份。 ES集群健康状态：
```

## 写入过程

（1）客户端发送写请求给cordinate node

（2）cordinate node 计算路由计算，转发到对应的主分片所在的节点上

（3）主分片节点处理请求，并将数据同步至副本分片节点

（4）当所有节点写完数据后，cordinate node 返回相应给客户端

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/288a2007b0804cdb848d5a51e30d83be~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)



如图所示，当一个索引请求到了node3节点上，产生的流程为：

1. 客户端向 Node 3 发送新建、删除请求。
2. node3发现该文档属于p1分片，转发请求到node1 
3. Node1完成p1分片的索引，转发node2、node3请求副本分片 
4. 所有的副本分片完成后node3返回结果。



## 查询过程 

https://juejin.cn/post/7096796055940890632 解析 

一般地，当接受到用户查询请求时，进入到倒排索引进行检索时，在返回结果的过程中，主要有以下几个步骤：

Step1：在分词系统对用户请求等原始Query进行分析，产生对应的terms；

Step2：terms在倒排索引中的词项列表中查找对应的terms的结果列表；

Step3：对结果列表数据进行微运算，如：计算文档静态分，文档相关性等；

Step4：基于上述运算得分对文档进行综合排序，最后返回结果给用户；

![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b3578ff2b7e24f51beec62cf70fea620~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)

如图所示，当一个查询请求，请求到node1上，它的查询流程如下：

1. node1为协调节点，它随机从这6个主副分片选择3个分片发送查询请求。
2. 每个分片查询返回from size排序好了的id和排序值给协调节点。
3. 协调节点进入query阶段，将每个分片获取的id和排序值汇总重新排序，选取from size个id。
4. 并发携带id向对应的分片获取详细信息，然后返回数据给客户端。



## 海量数据实践



## 分词器

es支持不同的分析器，在中文分词器中使用较多的是ik分词

https://github.com/medcl/elasticsearch-analysis-ik

官网

https://www.elastic.co/guide/en/elasticsearch/reference/8.10/analysis-analyzers.html

**Standard Analyzer**

The `standard` analyzer divides text into terms on word boundaries, as defined by the Unicode Text Segmentation algorithm. It removes most punctuation, lowercases terms, and supports removing stop words.

**Simple Analyzer**

The `simple` analyzer divides text into terms whenever it encounters a character which is not a letter. It lowercases all terms.

**Whitespace Analyzer**

The `whitespace` analyzer divides text into terms whenever it encounters any whitespace character. It does not lowercase terms.

**Stop Analyzer**

The `stop` analyzer is like the `simple` analyzer, but also supports removal of stop words.

**Keyword Analyzer**

The `keyword` analyzer is a “noop” analyzer that accepts whatever text it is given and outputs the exact same text as a single term.

**Pattern Analyzer**

The `pattern` analyzer uses a regular expression to split the text into terms. It supports lower-casing and stop words.

**Language Analyzers**

Elasticsearch provides many language-specific analyzers like `english` or `french`.

**Fingerprint Analyzer**

The `fingerprint` analyzer is a specialist analyzer which creates a fingerprint which can be used for duplicate detection.



基本使用 

https://www.elastic.co/guide/en/elasticsearch/reference/8.10/test-analyzer.html



## 参考链接
知识点汇总 
https://juejin.cn/column/7026888535719886885

官方网址 https://www.elastic.co/guide/en/elasticsearch/reference/8.10/docs.html

ES 解析 https://juejin.cn/post/7026902638176239653

ES 知识点 汇总 https://www.kancloud.cn/imnotdown1019/java_core_full/2157765

入门 https://cloud.tencent.com/developer/article/1547867



【2023-10-10】https://ac.nowcoder.com/discuss/1029975?type=0&order=0&page=1&channel=-1

【2023-06-19】https://blog.csdn.net/yanpenglei/article/details/121859896

【2023-06-19】https://cloud.tencent.com/developer/article/2141868



## Bilibili

https://www.bilibili.com/video/BV1TR4y1Q7kd?p=9&spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1

