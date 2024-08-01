
# study-es #
ES 学习笔记

## 本地访问 
http://localhost:9092/



## 基本使用

官方文档 https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/installation.html

Docker 部署 https://www.elastic.co/guide/en/elasticsearch/reference/8.13/docker.html



参考链接 https://pdai.tech/md/db/nosql-es/elasticsearch-x-usage.html

ES 特性 https://www.elastic.co/cn/elasticsearch/features

基本API https://www.elastic.co/guide/en/elasticsearch/reference/current/docs.html

Client 使用 【官网】

https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/index.html

https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/indexing.html

博客baeldung https://www.baeldung.com/elasticsearch-java

Sense 工具

https://www.elastic.co/guide/cn/elasticsearch/guide/current/running-elasticsearch.html#sense



## 基本命令

新增

```
ID 自动生成 
curl -X POST "localhost:9092/persons/_doc/?pretty" -H 'Content-Type: application/json' -d'
{
  "id" : 1711947442539999,
   "age" : 25,
   "fullName" : "Janette Doe",
   "dateOfBirth" : 1711947444181
}
'

指定ID： 1711947442539999
curl -X POST "localhost:9092/persons/_doc/1711947442539999?pretty" -H 'Content-Type: application/json' -d'
{
  "id" : 1711947442539999,
   "age" : 25,
   "fullName" : "Janette Doe",
   "dateOfBirth" : 1711947444181
}
'


```

查询

```
浏览器查询
http://localhost:9092/persons/_doc/1711947442539999?pretty

localhost:9092/{index}/_doc/{id}?pretty


命令行
curl -X GET "localhost:9092/persons/_doc/1711947442539999?pretty"

合并查询
curl -X GET "localhost:9092/_mget?pretty" -H 'Content-Type: application/json' -d'
{
  "docs": [
    {
      "_index": "persons",
      "_id": "1711947442539999"
    },
    {
      "_index": "persons",
      "_id": "1711947442539999"
    }
  ]
}
'
```

更新

```
curl -X PUT "localhost:9092/persons/_doc/1711947442539?pretty" -H 'Content-Type: application/json' -d'
{
  "id" : 1711947442539999,
   "age" : 25,
   "fullName" : "Janette Doe【new-by-curl】",
   "dateOfBirth" : 1711947444181
}
'

curl -X GET "localhost:9092/persons/_doc/1711947442539?pretty"

```

删除

```
curl -X DELETE "localhost:9092/persons/_doc/1711947442539"

curl -X GET "localhost:9092/persons/_doc/1711947442539?pretty"

```



## Java client 

官方 Tests https://github.com/elastic/elasticsearch-java/tree/8.13/java-client/src/test/java/co/elastic/clients/documentation

异步

```java
// Asynchronous non-blocking client
ElasticsearchAsyncClient asyncClient =
    new ElasticsearchAsyncClient(transport);
```



复杂查询

https://www.elastic.co/guide/en/elasticsearch/reference/8.13/query-dsl-intervals-query.html

```
ElasticsearchClient client = ...
SearchResponse<SomeApplicationData> results = client
    .search(b0 -> b0
        .query(b1 -> b1
            .intervals(b2 -> b2
                .field("my_text")
                .allOf(b3 -> b3
                    .ordered(true)
                    .intervals(b4 -> b4
                        .match(b5 -> b5
                            .query("my favorite food")
                            .maxGaps(0)
                            .ordered(true)
                        )
                    )
                    .intervals(b4 -> b4
                        .anyOf(b5 -> b5
                            .intervals(b6 -> b6
                                .match(b7 -> b7
                                    .query("hot water")
                                )
                            )
                            .intervals(b6 -> b6
                                .match(b7 -> b7
                                    .query("cold porridge")
                                )
                            )
                        )
                    )
                )
            )
        ),
    SomeApplicationData.class 
);
```



## 文本分词

https://www.elastic.co/guide/en/elasticsearch/reference/current/test-analyzer.html

使用空格分词

```
curl -X POST "localhost:9092/_analyze?pretty" -H 'Content-Type: application/json' -d'
{
  "analyzer": "whitespace",
  "text":     "The quick brown fox."
}
'
```

如何使用？？

使用的过程



## 参考文档 ##

官网 https://www.elastic.co
java client https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/index.html
java client https://www.baeldung.com/elasticsearch-java

Docker https://github.com/bage2014/study/tree/master/study-docker
Spring https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/


https://www.cnblogs.com/sunsky303/p/9438737.html
https://baijiahao.baidu.com/s?id=1709437902597420314&wfr=spider&for=pc


