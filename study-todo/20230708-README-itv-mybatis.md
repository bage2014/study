# study- Mybatis #
## 简介

https://blog.csdn.net/m0_59281987/article/details/127768618



## 架构流程

https://blog.csdn.net/m0_59281987/article/details/127768618



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



## Bilibili

Mybatis 核心组件 

【2023-07-08】 https://www.bilibili.com/video/BV1eT41197VG/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1

