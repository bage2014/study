# study- Mybatis #
## 简介

https://blog.csdn.net/m0_59281987/article/details/127768618

## Key关键点

- 行业调研、优势、代替方案等
- 核心组件【常用类】
- 基本架构
- mybatis中的一级缓存
- mybatis中的二级缓存
- 延时加载 
- OGNL表达式
- 设计模式
- selectKey
- Executor执行器
- TypeHandler

## 架构流程

https://blog.csdn.net/m0_59281987/article/details/127768618



## 执行过程 

![](https://image.cha138.com/20210525/18ec01e6bc704de09cc28fbf8663dda5.jpg)



- 先创建**核心配置文件**(sqlMapConfig.xml)
- 再创建**映射文件【表SQL语句存放】**(可以有多个 ~ 通常有多少张表就有多少
- 通过对象**SqlSessionFactory**对象来创
- 通过SqlSessionFactory来返回**SqlSession**接口(他并不是执行,只是负责调用)
- 通过调用返回一个**Excutor**(执行器)(真正使用)
- 通过excutor(执行器)来包装**MappedStatement**
- 通过MappedStatement可以访问数据库
- 在访问数据库之前进行输入映射(Map)(String Integer等基本数据类型)(pojo)
- 在查找出来的结果集进行输出映射(Map)(String Integer等基本数据类型)(pojo)(List)





![](https://img-blog.csdnimg.cn/img_convert/7c0470f4861c4dfb8ce32687c0e40834.png)

https://blog.csdn.net/weixin_44431073/article/details/111186797

执行过程解析 https://juejin.cn/post/7302698423840833576

## 设计模式

Mybatis 至少遇到了以下的设计模式的使用：
• Builder 模式 ，例如 SqlSessionFactoryBuilder 、 XMLConfigBuilder 、
XMLMapperBuilder 、 XMLStatementBuilder 、 CacheBuilder ；
• 工厂模式 ，例如 SqlSessionFactory 、 ObjectFactory 、 MapperProxyFactory ；
• 单例模式 ，例如 ErrorContext 和 LogFactory ；
• 代理模式 ， Mybatis 实现的核心，比如 MapperProxy 、 ConnectionLogger ，用的
jdk 的动态代理；还有 executor.loader 包使用了 cglib 或者 javassist 达到延迟加
载的效果；
• 组合模式 ，例如 SqlNode 和各个子类 ChooseSqlNode 等；
• 模板方法模式 ，例如 BaseExecutor 和 SimpleExecutor ，还有 BaseTypeHandler 和
所有的子类例如 IntegerTypeHandler ；
• 适配器模式 ，例如 Log 的 Mybatis 接口和它对 jdbc 、 log4j 等各种日志框架的适
配实现；
• 装饰者模式 ，例如 Cache 包中的 cache.decorators 子包中等各个装饰者的实现；
• 迭代器模式 ，例如迭代器模式 PropertyTokenizer ；


TODO



## 核心组件 

https://codeleading.com/article/53613164398/

http://www.voidme.com/mybatis/mybatis-core-components

https://cloud.tencent.com/developer/article/2104525

mybatis的**核心组件**

### SqlSessionFactoryBuilder

- 它会根据代码或者配置来生成SqlSessionFactory，采用的是分布构建的builder模式。

- 它的唯一作用就是为了生成mybatis的核心接口对象SqlSession，所以我们一般用单例模式处理它。构建它的方法有两种：XML方式和Java代码方式。

### SqlSessionFactory

它可以生成SqlSession，采用的是工厂模式。

### SqlSession

它可以发送SQL语句返回结果，也可以获取Mapper接口。

### SQL Mapper

Java接口和一个XML文件（或注解）构成，需要给出对应的SQL和映射规则，它可以发送SQL并返回结果。



## Executor执行器

参考链接

https://it.cha138.com/python/show-6207358.html

https://www.cnblogs.com/BlogNetSpace/p/17198090.html

https://www.kancloud.cn/imnotdown1019/java_core_full/2177328



#### 组成分类 

**简单执行器SimpleExecutor**

​		这是默认的执行器类型。它每次执行都会创建一个Statement对象，并立即执行SQL语句。这种执行器不支持事务，每次都会关闭Statement对象，适用于简单的查询场景。

**可重用执行器ReuseExecutor**

​		这种执行器重用预处理的Statement对象。它会缓存Statement对象，当需要执行相同的SQL语句时，会直接使用缓存的Statement对象，而不是每次都创建新的对象。这种执行器也不支持事务。

**批量执行器BatchExecutor**

​		这种执行器用于批量操作，可以一次执行多个SQL语句。它会将相同类型的SQL语句分组，并使用JDBC的批处理功能执行。这种执行器可以提高性能，尤其适用于需要执行大量相同类型SQL语句的场景，如批量插入或更新操作。



#### 执行过程 

- 解析SQL语句：Executor首先解析SQL语句，将其转换为JDBC可执行的语句。
- 设置参数：Executor将SQL语句中的占位符替换为真实的参数值，并将参数设置到PreparedStatement中。
- 执行SQL语句：Executor执行SQL语句，并将结果映射为Java对象。
- 处理缓存：如果开启了缓存功能，则Executor会将结果缓存到一级缓存或二级缓存中，以提高数据库操作的性能。
- 提交事务或回滚事务：如果需要提交事务或回滚事务，则Executor会调用Transaction对象的commit或rollback方法。
- 返回结果：Executor将查询结果或操作结果返回给应用程序。



## OGNL表达式

https://www.jianshu.com/p/75a2ee4ef84a



## 配置文件

配置文件节点？？？

顺序相对固定？？

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

<!--
    相对顺序，不能颠倒
	properties（属性）
	settings（设置）
	typeAliases（类型别名）
	typeHandlers（类型处理器）
	objectFactory（对象工厂）
	plugins（插件）
	environments（环境配置）
	environment（环境变量）
	transactionManager（事务管理器）
	dataSource（数据源）
	databaseIdProvider（数据库厂商标识）
	mappers（映射器）
	-->
	
	<!--设置-->
  <settings>
  <!-- SESSION：开启一级缓存功能；STATEMENT：缓存只对当前执行的这一个 SQL 语句有效，也就是没有用到一级缓存功能。 -->
  <setting name="localCacheScope" value="SESSION"/>
  </settings>
  
	<!--别名-->
	<typeAliases>
		<package name="com.bage.study.mybatis.common.domain"/>
	</typeAliases>

	<!--类型处理-->
	<typeHandlers>
		<typeHandler handler="com.bage.study.mybatis.common.typehandler.SexTypeHandler"></typeHandler>
	</typeHandlers>

	<!-- mybatis-config.xml -->
	<plugins>
		<plugin interceptor="com.bage.study.mybatis.common.interceptor.SqlTimeCostPlugin">
			<property name="someProperty" value="100"/>
		</plugin>
	</plugins>

	<mappers>
		<mapper resource="com/bage/study/mybatis/springboot/org/UserMapper.xml"/>
		<mapper resource="com/bage/study/mybatis/springboot/org/UserExtMapper.xml"/>
		<mapper resource="com/bage/study/mybatis/springboot/org/DepartmentMapper.xml"/>
		<mapper resource="com/bage/study/mybatis/springboot/org/DepartmentAddressMapper.xml"/>
	</mappers>

</configuration>
```



## 缓存

参考链接 

https://baijiahao.baidu.com/s?id=1748282345119959927&wfr=spider&for=pc

![](https://pics4.baidu.com/feed/30adcbef76094b365a9c2761e6b24dd28c109dbd.jpeg@f_auto?token=5ef6fceb69a26e1c06bb2f0630a9f1ce)

#### 查询过程

执行过程

- 先查询二级缓存，因为二级缓存中可能会有其他程序已经查出来的数据，可以拿来直接使用
- 如果二级缓存没有命中，再查询一级缓存
- 如果一级缓存也没有命中，则查询数据库
- SqlSession关闭之后，一级缓存中的数据会写入二级缓存。

![](https://pics5.baidu.com/feed/aa18972bd40735fa854c865bcf2f3eb80e240819.jpeg@f_auto?token=9b32fe7a0c4a44e8dab47de5dca50d6c)



### 一级缓存

#### 基本概念

- 默认开启 
- 会话级别 SqlSession + Excutor

- 在一次 SqlSession 中（**数据库会话**），程序执行多次查询，且查询条件完全相同，多次查询之间程序没有其他增删改操作，则第二次及后面的查询可以从缓存中获取数据，避免走数据库。
- LocalCache
-  Key ：Statement Id + Offset + Limmit + Sql + Params


![](https://pics5.baidu.com/feed/0dd7912397dda144f3b0cd43fbc9e1a90df4860b.jpeg@f_auto?token=583aff2599511c4229d65f41eee4d85a)



#### 失效时机

- 不同的SqlSession对应不同的一级缓存
- 同一个SqlSession但是查询条件不同
- 同一个SqlSession两次查询期间执行了任何一次增删改操作
- 同一个SqlSession两次查询期间手动清空了缓存



### 二级缓存

#### 基本概念

- 开启二级缓存后，会使**用 CachingExecutor 装饰 Executor**，进入一级缓存的查询流程前，先在CachingExecutor 进行二级缓存的查询。

- 二级缓存开启后，同一个 namespace下的所有操作语句，都影响着同一个Cache。

![](https://pics1.baidu.com/feed/d6ca7bcb0a46f21f8931f2c6b55a5a6b0d33aea3.jpeg@f_auto?token=40824dc38be0f128c21a6db5c499ea88)



### 自定义二级缓存



## Bilibili

Mybatis 核心组件 

【2023-07-08】 https://www.bilibili.com/video/BV1eT41197VG/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1



## 参考链接

https://zhuanlan.zhihu.com/p/347935099

https://blog.csdn.net/feng8403000/article/details/122282095