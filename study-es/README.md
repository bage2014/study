
# study-es #
ES 学习笔记

## 本地访问 
http://localhost:9092/







## 基本使用

参考链接 https://pdai.tech/md/db/nosql-es/elasticsearch-x-usage.html

ES 特性 https://www.elastic.co/cn/elasticsearch/features

基本API https://www.elastic.co/guide/en/elasticsearch/reference/current/docs.html





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
localhost:9092/persons/_doc/1711947442539?pretty

命令行
curl -X GET "localhost:9092/persons/_doc/1711947442539?pretty"

合并查询
curl -X GET "localhost:9092/_mget?pretty" -H 'Content-Type: application/json' -d'
{
  "docs": [
    {
      "_index": "persons",
      "_id": "1711947442539"
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


