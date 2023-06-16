# study- SQL #
- 



**SQL****学习笔记**

1. **Spring-jdbc****环境**

https://spring.io/guides/gs/accessing-data-mysql/

1. **索引**

避免全表扫描数据，查询出某一条查询数据

1. 1. **优点**

加快查询效率

1. 1. **缺点**

新增、修改、删除都要维护索引

索引页需要空间

1. 1. **使用原则**

经常插入更新的表尽量少用，经常查询的多用索引

数据量较少没有必要使用

列值较少不需要使用，比如性别

1. 1. **联合索引**

https://blog.csdn.net/Abysscarry/article/details/80792876



复合索引的结构与电话簿类似，人名由姓和名构成，电话簿首先按姓氏对进行排序，然后按名字对有相同姓氏的人进行排序。如果您知道姓，电话簿将非常有用；如果您知道姓和名，电话簿则更为有用，但如果您只知道名不姓，电话簿将没有用处。



所以说创建复合索引时，应该仔细考虑列的顺序。对索引中的所有列执行搜索或仅对前几列执行搜索时，复合索引非常有用；仅对后面的任意列执行搜索时，复合索引则没有用处。



**联合索引本质：**

当创建**(a,b,c)****联合索引**时，相当于创建了**(a)****单列索引**，**(a,b)****联合索引**以及**(a,b,c)****联合索引** 
 想要索引生效的话,只能使用 a和a,b和a,b,c三种组合；当然，我们上面测试过，**a,c****组合也可以，但实际上只用到了****a****的索引，****c****并没有用到！** 





查看表大小 

```
SELECT   TABLE_NAME AS `Table`,   ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024) AS `Size (MB)` FROM   information_schema.TABLES WHERE   TABLE_SCHEMA

= "mydbpro" ORDER BY   (DATA_LENGTH + INDEX_LENGTH) DESC;
```

## SQL 解析
https://github.com/JSQLParser/JSqlParser
https://www.yii666.com/blog/307676.html