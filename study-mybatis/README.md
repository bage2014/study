# study-mybatis  #
学习mybatis的笔记<br>
TODO 有待继续了解 typeHandlers、处理枚举类型...

## 参考链接 ##
- MyBatis 官网： [http://www.mybatis.org/mybatis-3/zh/configuration.html](http://www.mybatis.org/mybatis-3/zh/configuration.html "MyBatis官网")
- H2 官网： [http://www.h2database.com/html/tutorial.html#connecting_using_jdbc](http://www.h2database.com/html/tutorial.html#connecting_using_jdbc "H2数据库入门")
- 分页插件：[https://github.com/pagehelper/Mybatis-PageHelper](https://github.com/pagehelper/Mybatis-PageHelper)
- Spring Boot 集成分页插件：[https://github.com/pagehelper/pagehelper-spring-boot](https://github.com/pagehelper/pagehelper-spring-boot)

## 项目说明 ##
- `src\main\java\com\bage\study\mybatis` 目录下
 - `tutorial` 入门程序
 - `springboot` Spring Boot的使用示例

## 请求用例 ##
- Mapper注解：[http://localhost:8080/](http://localhost:8080/user/all "Mapper注解查询数据")
- XML配置：[http://localhost:8080/user/1](http://localhost:8080/user/1 "XML配置查询数据")
- H2控制台（jdbc:h2:~/myDb@sa@root）：[http://localhost:8080/h2-console/](http://localhost:8080/h2-console/ "H2控制台")
## XML 配置[顺序相对有序] ##
详见 ： \src\main\resources\mybatis-config.xml

- properties 属性，用于设置一些变量值，配置属性
  - [配置]可外部配置且可动态替换；既可以在Java 属性文件中配置，亦可通过 properties 元素的子元素来传递
  - [优先级]Java方法参数属性具有最高优先级，resource/url 属性中指定的配置文件次之， properties 属性中指定的属性最低
  - [高级特性]MyBatis 3.4.2开始，可以为占位符指定一个默认值、使用 OGNL 表达式的三元运算符等,
  
- settings 设置，修改mybatis的配置
  - 缓存、主键、执行器等，MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为
  - 详见：[http://www.mybatis.org/mybatis-3/zh/configuration.html#settings](http://www.mybatis.org/mybatis-3/zh/configuration.html#settings "settings设置说明")
- typeAliases 类型别名，用来减少类完全限定名的冗余
  - 可以通过 包名配置、类配置、或者注解实现别名设置
  - 类型别名是为 Java 类型设置一个短的名字。它只和 XML 配置有关
  - 主要有 类型别名、包别名和注解别名
  - [常见 Java 类型内建的相应的类型别名](http://www.mybatis.org/mybatis-3/zh/configuration.html "详见官网typeAliases")       
  
- typeHandlers 类型处理器，SQL和Java类型的转化
  - 无论是 MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，还是从结果集中取出一个值时， 都会用类型处理器将获取的值以合适的方式转换成 Java 类型
  - MyBatis 默认支持BooleanTypeHandler、ByteTypeHandler、ShortTypeHandler等
  
- objectFactory 对象工厂
- plugins 插件
- environments 环境
- environment 环境变量
- transactionManager 事务管理器
- dataSource 数据源
- databaseIdProvider 数据库厂商标识
- mappers 映射器


## 缓存 ##
### 参考链接 ###
MyBatis 一、二级缓存和自定义缓存 [https://www.cnblogs.com/moongeek/p/7689683.html](https://www.cnblogs.com/moongeek/p/7689683.html)
http://localhost:8080/user/all

### 一级缓存 ###
MyBatis **默认开启**了一级缓存，一级缓存是在SqlSession 层面进行缓存的。即，同一个SqlSession ，多次调用同一个Mapper和同一个方法的同一个参数，只会进行一次数据库查询，然后把数据缓存到缓冲中，以后直接先从缓存中取出数据，不会直接去查数据库。

 但是不同的SqlSession对象，因为不用的SqlSession都是相互隔离的，所以相同的Mapper、参数和方法，他还是会再次发送到SQL到数据库去执行，返回结果。

### 二级缓存 ###
 为了克服一级缓存问题，需要开启二级缓存，是的缓存zaiSqlSessionFactory层面给各个SqlSession 对象共享。默认二级缓存是不开启的，需要手动进行配置

    <cache/>

映射文件所有的select 语句会被缓存
映射文件的所有的insert、update和delete语句会刷新缓存
缓存会使用默认的Least Recently Used（LRU，最近最少使用原则）的算法来回收缓存空间
根据时间表，比如No Flush Interval，（CNFI，没有刷新间隔），缓存不会以任何时间顺序来刷新
缓存会存储列表集合或对象（无论查询方法返回什么）的1024个引用
缓存会被视为是read/write（可读/可写）的缓存，意味着对象检索不是共享的，而且可以很安全的被调用者修改，不干扰其他调用者或县城所作的潜在修改


## XML ##
### 参考链接 ###
XML 映射文件 [http://www.mybatis.org/mybatis-3/zh/sqlmap-xml.html](http://www.mybatis.org/mybatis-3/zh/sqlmap-xml.html)

### insert ###
简单插入
请求用例 [http://localhost:8080/user/insert](http://localhost:8080/user/insert)	

	<insert id="insertAuthor">
	  insert into Author (id,username,password,email,bio)
	  values (#{id},#{username},#{password},#{email},#{bio})
	</insert>


批量插入

	<insert id="insertAuthor" useGeneratedKeys="true"
	    keyProperty="id">
	  insert into Author (username, password, email, bio) values
	  <foreach item="item" collection="list" separator=",">
	    (#{item.username}, #{item.password}, #{item.email}, #{item.bio})
	  </foreach>
	</insert>

返回ID主键

```java
useGeneratedKeys="true"
```

具体实践：
    com\bage\study\mybatis\UserMapper.xml

      简单插入 insert
    	批量插入 batchInsert

### delete ###

	<delete id="deleteAuthor">
	  delete from Author where id = #{id}
	</delete>

### update ###

	<update id="updateAuthor">
	  update Author set
	    username = #{username},
	    password = #{password},
	    email = #{email},
	    bio = #{bio}
	  where id = #{id}
	</update>

### select ###

普通查询
请求用例 [http://localhost:8080/user/all](http://localhost:8080/user/all)	

	<select id="selectPerson" parameterType="int" resultType="hashmap">
	  SELECT * FROM PERSON WHERE ID = #{id}
	</select>

一对一 association + 一对多 collection
请求用例 [http://localhost:8080/department/all](http://localhost:8080/department/all)	

    <resultMap id="DepartmentResultMap" type="Department">
        <result property="id" column="id"/>
        <result property="name" column="name"/>
    
        <association property="departmentAddress" column="id" select="com.bage.study.mybatis.common.dao.DepartmentAddressMapper.queryByDepartmentId"/>
    
        <collection property="users" column="id" select="com.bage.study.mybatis.common.dao.UserMapper.queryByDepartmentId"/>
    
    </resultMap>

延时加载

```
collection fetchType="lazy" 
```

性能 vs 潜在问题

自动映射【必须类属性名完全一致】

```
resultType="a.b.c.ABCclass"
```



### SQL ###

    <sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>   
    <select id="selectUsers" resultType="map">
      select
        <include refid="userColumns"><property name="alias" value="t1"/></include>
      from some_table t1
        cross join some_table t2
    </select>

### 参数 ###

    #{age,javaType=int,jdbcType=NUMERIC,typeHandler=MyTypeHandler}

### TypeHandler ###
目前实现了一个示例，SexTypeHandler

