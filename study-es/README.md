
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

### 增删改查

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



### 高阶

查询所有【分页+排序】

```
get /persons/_search
{
    "query": {
        "match_all": {}
    },
    "sort": [
        {
            "age": "asc"
        }
    ],
    "from": 2,
    "size": 3
}
```

特定条件 【"fullName": "bage new" // 匹配fullname 包含bage或者包含new】

```
{
    "query": {
        "match": {
            "fullName": "bage new"
        }
    },
    "sort": [
        {
            "age": "asc"
        }
    ],
    "from": 0,
    "size": 3
}
```

段落查询【match_phrase】

```
{
    "query": {
        "match_phrase": {
            "fullName": "bage lu"
        }
    },
    "sort": [
        {
            "age": "asc"
        }
    ],
    "from": 0,
    "size": 3
}
```

多条件查询【age=XXX+fullName notContains XXX】

```
{
    "query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "age": "25"
                    }
                }
            ],
            "must_not": [
                {
                    "match": {
                        "fullName": "Janette"
                    }
                }
            ]
        }
    },
    "sort": [
        {
            "age": "asc"
        }
    ],
    "from": 0,
    "size": 3
}
```



官方例子？

https://pdai.tech/md/db/nosql-es/elasticsearch-x-dsl-com.html

- `must`： 必须匹配。贡献算分
- `must_not`：过滤子句，必须不能匹配，但不贡献算分
- `should`： 选择性匹配，至少满足一条。贡献算分
- `filter`： 过滤子句，必须匹配，但不贡献算分

```
{
  "query": {
    "bool" : {
      "must" : {
        "term" : { "user.id" : "kimchy" }
      },
      "filter": {
        "term" : { "tags" : "production" }
      },
      "must_not" : {
        "range" : {
          "age" : { "gte" : 10, "lte" : 20 }
        }
      },
      "should" : [
        { "term" : { "tags" : "env1" } },
        { "term" : { "tags" : "deployed" } }
      ],
      "minimum_should_match" : 1,
      "boost" : 1.0
    }
  }
}
```

## 分词器

分词器使用的两个情形：  
1，Index time analysis.  创建或者更新文档时，会对文档进行分词
2，Search time analysis.  查询时，对查询语句分词

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

分词 

```
localhost:9092/_analyze?pretty

{
  "analyzer": "standard",
  "text": "In 2020, Java is the best language in the world."
}
```

指定分词

```
localhost:9092/_analyze?pretty

{
  "analyzer": "whitespace",
  "text": "In 2020, Java is the best language in the world."
}
```

不分词

```
localhost:9092/_analyze?pretty

{
  "analyzer": "keyword",
  "text": ["The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."]
}
```

指定分词器查询

```
localhost:9092/persons/_search

{
    "query": {
        "match": {
            "fullName": {
                "query": "bage new",
                "analyzer": "standard"
            }
        }
    },
    "sort": [
        {
            "age": "asc"
        }
    ],
    "from": 0,
    "size": 3
}

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





## 参考文档 ##

官网 https://www.elastic.co
java client https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/index.html
java client https://www.baeldung.com/elasticsearch-java

Docker https://github.com/bage2014/study/tree/master/study-docker
Spring https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/


https://www.cnblogs.com/sunsky303/p/9438737.html
https://baijiahao.baidu.com/s?id=1709437902597420314&wfr=spider&for=pc


