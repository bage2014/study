
## 参考链接 ##
Spring 文档 
https://docs.spring.io/spring-data/neo4j/reference/getting-started.html
https://spring.io/guides/gs/accessing-data-neo4j

中文 http://neo4j.com.cn/public/docs/index.html

## 数据查看
http://localhost:7474/browser/



## 查询语句



### 对比SQL

https://neo4j.com/docs/getting-started/cypher-intro/cypher-sql/



### 基本查询 

增删改查 https://neo4j.com/docs/getting-started/cypher-intro/results/

```
增
CREATE (matrix:Movie {title: 'The Matrix', released: 1997})
CREATE (cloudAtlas:Movie {title: 'Cloud Atlas', released: 2012})
CREATE (forrestGump:Movie {title: 'Forrest Gump', released: 1994})
CREATE (keanu:Person {name: 'Keanu Reeves', born: 1964})
CREATE (robert:Person {name: 'Robert Zemeckis', born: 1951})
CREATE (tom:Person {name: 'Tom Hanks', born: 1956})
CREATE (tom)-[:ACTED_IN {roles: ['Forrest']}]->(forrestGump)
CREATE (tom)-[:ACTED_IN {roles: ['Zachry']}]->(cloudAtlas)
CREATE (robert)-[:DIRECTED]->(forrestGump)

删
MATCH (m:Person {name: 'Mark'})
DELETE m

MATCH (j:Person {name: 'Jennifer'})-[r:IS_FRIENDS_WITH]->(m:Person {name: 'Mark'})
DELETE r


改
MATCH (p:Person {name: 'Jennifer'})
SET p.birthdate = date('1980-01-01')
RETURN p


查 
MATCH (m:Movie)
WHERE m.title = 'The Matrix'
RETURN m

```

