# MyBatis 技术总结

## 1. 基础概念

### 1.1 什么是 MyBatis
MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集的操作。MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接口和 Java 的 POJO（Plain Old Java Objects，普通的 Java 对象）映射成数据库中的记录。

### 1.2 MyBatis 的优势
- **简单易学**：相比 Hibernate，MyBatis 学习成本更低，SQL 语句编写更加灵活
- **灵活性高**：可以完全控制 SQL 语句，适合复杂查询场景
- **性能优秀**：避免了 ORM 框架的性能开销，支持优化 SQL
- **易于集成**：可以与 Spring 等框架无缝集成
- **支持动态 SQL**：通过 XML 或注解实现动态 SQL，适应不同的查询条件
- **支持高级映射**：支持一对一、一对多、多对多等复杂关系映射

### 1.3 MyBatis 与其他 ORM 框架的对比

| 框架 | 优势 | 劣势 |
|------|------|------|
| MyBatis | 灵活，SQL 可控，性能好，学习成本低 | 需要手写 SQL，数据库移植性差 |
| Hibernate | 完全 ORM，数据库移植性好，无需手写 SQL | 学习成本高，复杂查询性能差，SQL 不可控 |
| JPA | 标准规范，跨实现，易于使用 | 灵活性不足，复杂查询支持有限 |

## 2. 核心组件

### 2.1 SqlSessionFactoryBuilder
- **作用**：根据配置信息构建 SqlSessionFactory
- **特点**：采用 Builder 模式，构建过程分步进行
- **使用场景**：应用启动时创建 SqlSessionFactory，之后可丢弃
- **构建方式**：
  - XML 配置文件方式
  - Java 代码配置方式

### 2.2 SqlSessionFactory
- **作用**：创建 SqlSession 实例的工厂
- **特点**：采用工厂模式，线程安全，应用级别单例
- **生命周期**：应用启动时创建，应用结束时销毁
- **核心方法**：
  ```java
  SqlSession openSession(); // 默认非自动提交事务
  SqlSession openSession(boolean autoCommit); // 指定是否自动提交事务
  SqlSession openSession(Connection connection); // 使用指定连接
  ```

### 2.3 SqlSession
- **作用**：执行 SQL 语句，管理事务
- **特点**：非线程安全，会话级别对象
- **生命周期**：请求开始时创建，请求结束时关闭
- **核心方法**：
  ```java
  <T> T selectOne(String statement, Object parameter);
  <E> List<E> selectList(String statement, Object parameter);
  int insert(String statement, Object parameter);
  int update(String statement, Object parameter);
  int delete(String statement, Object parameter);
  <T> T getMapper(Class<T> type); // 获取 Mapper 接口
  void commit(); // 提交事务
  void rollback(); // 回滚事务
  void close(); // 关闭会话
  ```

### 2.4 Mapper 接口
- **作用**：定义数据访问方法，与映射文件或注解对应
- **特点**：无需实现类，由 MyBatis 动态代理生成实现
- **命名规范**：通常与映射文件同名，放在同一包下
- **示例**：
  ```java
  public interface UserMapper {
      User selectById(Long id);
      List<User> selectAll();
      int insert(User user);
      int update(User user);
      int delete(Long id);
  }
  ```

### 2.5 Executor 执行器
- **作用**：执行 SQL 语句，管理缓存
- **类型**：
  - **SimpleExecutor**：默认执行器，每次执行都创建新的 Statement
  - **ReuseExecutor**：重用 Statement，缓存 SQL 与 Statement 的映射
  - **BatchExecutor**：批量执行 SQL，提高批量操作性能
- **执行流程**：
  1. 解析 SQL 语句
  2. 设置参数
  3. 执行 SQL
  4. 处理缓存
  5. 映射结果

### 2.6 MappedStatement
- **作用**：封装 SQL 语句、参数映射、结果映射等信息
- **来源**：从映射文件或注解中解析生成
- **核心属性**：
  - id：SQL 语句的唯一标识
  - sqlSource：SQL 语句的源代码
  - parameterMap：参数映射
  - resultMap：结果映射
  - statementType：Statement 类型（STATEMENT、PREPARED、CALLABLE）

### 2.7 TypeHandler
- **作用**：处理 Java 类型与数据库类型之间的转换
- **内置类型处理器**：支持常见的 Java 类型与数据库类型转换
- **自定义类型处理器**：通过实现 TypeHandler 接口或继承 BaseTypeHandler 类
- **使用场景**：处理枚举类型、日期类型等特殊类型

## 3. 架构与执行流程

### 3.1 整体架构
MyBatis 的架构分为三层：

1. **API 接口层**：提供给应用程序使用的接口，如 SqlSession 和 Mapper 接口
2. **核心处理层**：处理 SQL 解析、参数映射、结果映射、缓存管理等核心功能
3. **基础支撑层**：提供数据源管理、事务管理、日志管理等基础服务

### 3.2 执行流程详解

1. **初始化阶段**：
   - 加载配置文件（mybatis-config.xml）
   - 解析映射文件或注解
   - 构建 SqlSessionFactory

2. **执行阶段**：
   - 创建 SqlSession
   - 获取 Mapper 接口代理对象
   - 调用 Mapper 方法
   - Executor 执行 SQL
   - 处理缓存
   - 映射结果

3. **结束阶段**：
   - 提交或回滚事务
   - 关闭 SqlSession

### 3.3 核心配置文件
核心配置文件（mybatis-config.xml）的基本结构：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <!-- 属性配置 -->
  <properties resource="db.properties"/>
  
  <!-- 全局设置 -->
  <settings>
    <setting name="cacheEnabled" value="true"/>
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="aggressiveLazyLoading" value="false"/>
  </settings>
  
  <!-- 类型别名 -->
  <typeAliases>
    <package name="com.example.domain"/>
  </typeAliases>
  
  <!-- 类型处理器 -->
  <typeHandlers>
    <typeHandler handler="com.example.handler.SexTypeHandler"/>
  </typeHandlers>
  
  <!-- 插件 -->
  <plugins>
    <plugin interceptor="com.example.plugin.SqlLogPlugin">
      <property name="logLevel" value="DEBUG"/>
    </plugin>
  </plugins>
  
  <!-- 环境配置 -->
  <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
        <property name="driver" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
      </dataSource>
    </environment>
  </environments>
  
  <!-- 映射器 -->
  <mappers>
    <package name="com.example.mapper"/>
  </mappers>
</configuration>
```

## 4. 映射文件

### 4.1 基本结构
映射文件（如 UserMapper.xml）的基本结构：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mapper.UserMapper">
  <!-- 结果映射 -->
  <resultMap id="UserResultMap" type="User">
    <id column="id" property="id"/>
    <result column="name" property="name"/>
    <result column="age" property="age"/>
    <result column="email" property="email"/>
  </resultMap>
  
  <!-- 查询语句 -->
  <select id="selectById" resultMap="UserResultMap">
    SELECT id, name, age, email FROM user WHERE id = #{id}
  </select>
  
  <!-- 插入语句 -->
  <insert id="insert" parameterType="User" useGeneratedKeys="true" keyProperty="id">
    INSERT INTO user (name, age, email) VALUES (#{name}, #{age}, #{email})
  </insert>
  
  <!-- 更新语句 -->
  <update id="update" parameterType="User">
    UPDATE user SET name = #{name}, age = #{age}, email = #{email} WHERE id = #{id}
  </update>
  
  <!-- 删除语句 -->
  <delete id="delete" parameterType="Long">
    DELETE FROM user WHERE id = #{id}
  </delete>
</mapper>
```

### 4.2 SQL 语句配置
- **select**：查询语句，支持结果映射
- **insert**：插入语句，支持自动生成主键
- **update**：更新语句
- **delete**：删除语句
- **sql**：可重用的 SQL 片段
- **include**：引用 SQL 片段

### 4.3 参数映射
- **#{parameter}**：预编译参数，安全，防止 SQL 注入
- **${parameter}**：字符串替换，不安全，用于动态表名、排序字段等
- **参数类型**：基本类型、POJO、Map、List 等
- **参数注解**：@Param 注解指定参数名称

### 4.4 结果映射
- **resultMap**：自定义结果映射，处理复杂映射关系
- **resultType**：简单结果类型，自动映射
- **关联映射**：
  - **association**：一对一关联
  - **collection**：一对多关联
- **鉴别器**：**discriminator**，根据条件选择不同的结果映射

### 4.5 动态 SQL
MyBatis 提供了强大的动态 SQL 功能，支持条件判断、循环等：

- **if**：条件判断
- **choose/when/otherwise**：多条件选择
- **trim/where/set**：处理 SQL 语句的前缀和后缀
- **foreach**：循环处理集合参数
- **bind**：绑定变量

**示例**：
```xml
<select id="selectByCondition" resultMap="UserResultMap">
  SELECT id, name, age, email FROM user
  <where>
    <if test="name != null and name != ''">
      AND name LIKE #{name}
    </if>
    <if test="age != null">
      AND age = #{age}
    </if>
    <if test="email != null and email != ''">
      AND email = #{email}
    </if>
  </where>
</select>
```

## 5. 缓存机制

### 5.1 一级缓存
- **作用域**：SqlSession 级别，默认开启
- **实现**：LocalCache，基于 PerpetualCache（HashMap 实现）
- **缓存键**：StatementId + Offset + Limit + Sql + Params
- **失效时机**：
  - 不同的 SqlSession
  - 同一 SqlSession 但查询条件不同
  - 同一 SqlSession 执行了增删改操作
  - 手动调用 clearCache() 方法

### 5.2 二级缓存
- **作用域**：Mapper 级别（namespace），默认关闭
- **实现**：基于 PerpetualCache，可自定义缓存实现
- **开启方式**：
  1. 在核心配置文件中设置 `cacheEnabled=true`
  2. 在映射文件中添加 `<cache/>` 标签
- **缓存键**：StatementId + Sql + Params
- **刷新机制**：当同一 namespace 下执行增删改操作时，缓存会被刷新

### 5.3 缓存配置与优化
- **缓存配置**：
  ```xml
  <cache 
    eviction="LRU" 
    flushInterval="60000" 
    size="1024" 
    readOnly="false"/>
  ```
  - eviction：缓存回收策略（LRU、FIFO、SOFT、WEAK）
  - flushInterval：缓存刷新间隔
  - size：缓存大小
  - readOnly：是否只读

- **缓存优化建议**：
  - 对于查询频繁、修改较少的数据使用二级缓存
  - 避免缓存大对象
  - 合理设置缓存大小和刷新间隔
  - 考虑使用第三方缓存实现（如 Redis）

## 6. 高级特性

### 6.1 插件机制
- **作用**：拦截 MyBatis 的核心操作，如执行 SQL、参数处理、结果映射等
- **实现**：实现 Interceptor 接口
- **拦截点**：
  - Executor（执行器）
  - ParameterHandler（参数处理器）
  - ResultSetHandler（结果集处理器）
  - StatementHandler（语句处理器）
- **使用场景**：SQL 日志、性能监控、数据脱敏等

**示例**：
```java
@Intercepts({
    @Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
    )
})
public class SqlLogPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截逻辑
        return invocation.proceed();
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    @Override
    public void setProperties(Properties properties) {
        // 设置属性
    }
}
```

### 6.2 类型处理器
- **作用**：处理 Java 类型与数据库类型之间的转换
- **自定义类型处理器**：
  ```java
  public class SexTypeHandler extends BaseTypeHandler<SexEnum> {
      @Override
      public void setNonNullParameter(PreparedStatement ps, int i, SexEnum parameter, JdbcType jdbcType) throws SQLException {
          ps.setInt(i, parameter.getCode());
      }
      
      @Override
      public SexEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
          int code = rs.getInt(columnName);
          return rs.wasNull() ? null : SexEnum.valueOf(code);
      }
      
      // 其他方法实现...
  }
  ```

### 6.3 延迟加载
- **作用**：按需加载关联对象，提高查询性能
- **开启方式**：
  ```xml
  <settings>
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="aggressiveLazyLoading" value="false"/>
  </settings>
  ```
- **实现原理**：使用 CGLIB 或 Javassist 生成代理对象，当访问关联属性时触发加载

### 6.4 批量操作
- **使用 BatchExecutor**：通过设置 executorType="BATCH"
- **示例**：
  ```java
  SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
  try {
      UserMapper mapper = sqlSession.getMapper(UserMapper.class);
      for (User user : users) {
          mapper.insert(user);
      }
      sqlSession.commit();
  } finally {
      sqlSession.close();
  }
  ```

## 7. 最佳实践

### 7.1 代码规范
- **Mapper 接口**：
  - 方法名与 SQL 语句 id 一致
  - 使用 @Param 注解指定参数名称
  - 返回类型与结果映射一致

- **映射文件**：
  - 使用 namespace 与 Mapper 接口对应
  - 合理使用 resultMap 处理复杂映射
  - 使用动态 SQL 处理条件查询

### 7.2 性能优化
- **SQL 优化**：
  - 避免 SELECT *，只查询需要的字段
  - 使用索引字段作为查询条件
  - 合理使用 JOIN，避免多表复杂关联

- **MyBatis 优化**：
  - 使用二级缓存缓存热点数据
  - 合理设置 batchSize 处理批量操作
  - 使用延迟加载减少不必要的关联查询
  - 避免在循环中执行 SQL 语句

### 7.3 常见问题与解决方案

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| SQL 注入 | 使用 ${} 进行字符串替换 | 使用 #{} 预编译参数 |
| 缓存不一致 | 增删改操作后缓存未刷新 | 确保增删改操作与查询在同一 namespace 下 |
| 延迟加载失效 | 未正确配置或立即访问了关联属性 | 检查延迟加载配置，避免立即访问关联属性 |
| 批量操作性能差 | 未使用 BatchExecutor | 使用 ExecutorType.BATCH 执行批量操作 |

## 8. 面试题解析

### 8.1 常见 MyBatis 面试问题

#### 8.1.1 MyBatis 中 #{} 和 ${} 的区别是什么？
**答案**：
- **#{}`**：预编译参数，会将参数转换为 JDBC 的预编译语句参数，自动处理类型转换，防止 SQL 注入，是推荐使用的方式。
- **${}`**：字符串替换，直接将参数值替换到 SQL 语句中，不做类型转换，存在 SQL 注入风险，仅用于动态表名、排序字段等场景。

#### 8.1.2 MyBatis 的一级缓存和二级缓存有什么区别？
**答案**：
| 特性 | 一级缓存 | 二级缓存 |
|------|----------|----------|
| 作用域 | SqlSession 级别 | Mapper（namespace）级别 |
| 默认状态 | 默认开启 | 默认关闭 |
| 实现方式 | LocalCache（PerpetualCache） | PerpetualCache（可自定义） |
| 失效时机 | SqlSession 关闭或执行增删改操作 | 同一 namespace 下执行增删改操作 |
| 缓存键 | StatementId + Offset + Limit + Sql + Params | StatementId + Sql + Params |

#### 8.1.3 MyBatis 的执行器有哪些类型？它们的区别是什么？
**答案**：
MyBatis 有三种执行器类型：
- **SimpleExecutor**：默认执行器，每次执行都创建新的 Statement，执行完后关闭。
- **ReuseExecutor**：重用 Statement，缓存 SQL 与 Statement 的映射，相同 SQL 语句使用同一个 Statement。
- **BatchExecutor**：批量执行 SQL，将相同类型的 SQL 语句分组，使用 JDBC 的批处理功能执行，提高批量操作性能。

#### 8.1.4 MyBatis 如何实现延迟加载？
**答案**：
MyBatis 的延迟加载通过以下方式实现：
1. 在核心配置文件中开启延迟加载：`lazyLoadingEnabled=true`
2. 使用 CGLIB 或 Javassist 为目标对象生成代理对象
3. 当访问代理对象的关联属性时，触发拦截器方法
4. 拦截器方法执行关联查询，加载关联数据
5. 将加载的数据设置到目标对象中

#### 8.1.5 MyBatis 的插件机制是如何工作的？
**答案**：
MyBatis 的插件机制基于动态代理实现：
1. 插件实现 Interceptor 接口，定义拦截逻辑
2. 使用 @Intercepts 注解指定要拦截的目标方法
3. MyBatis 启动时，插件会被注册到 Configuration 中
4. 当创建 Executor、ParameterHandler、ResultSetHandler、StatementHandler 等对象时，会通过 Plugin.wrap() 方法为这些对象生成代理
5. 当执行被拦截的方法时，会触发插件的 intercept() 方法，执行拦截逻辑

#### 8.1.6 MyBatis 如何处理多表关联查询？
**答案**：
MyBatis 处理多表关联查询的方式有：
1. **嵌套结果映射**：使用 resultMap 的 association（一对一）和 collection（一对多）标签处理关联关系
2. **嵌套查询**：通过 select 属性指定子查询，实现延迟加载
3. **多表连接查询**：直接编写多表 JOIN SQL，通过 resultMap 映射结果

#### 8.1.7 MyBatis 与 Spring 如何集成？
**答案**：
MyBatis 与 Spring 集成的方式：
1. 使用 MyBatis-Spring 集成包
2. 配置 SqlSessionFactoryBean，设置数据源和映射文件
3. 配置 MapperScannerConfigurer，自动扫描 Mapper 接口
4. 使用 @Mapper 注解或在 XML 中配置 Mapper
5. 事务管理由 Spring 负责

#### 8.1.8 MyBatis 的动态 SQL 有哪些标签？
**答案**：
MyBatis 的动态 SQL 标签包括：
- **if**：条件判断
- **choose/when/otherwise**：多条件选择
- **trim/where/set**：处理 SQL 语句的前缀和后缀
- **foreach**：循环处理集合参数
- **bind**：绑定变量

## 9. 参考链接

### 9.1 官方文档
- [MyBatis 官方文档](https://mybatis.org/mybatis-3/zh/index.html)
- [MyBatis-Spring 官方文档](https://mybatis.org/spring/zh/index.html)

### 9.2 教程资源
- [MyBatis 入门教程](https://www.baeldung.com/mybatis)
- [MyBatis 实战教程](https://www.w3cschool.cn/mybatis/)
- [MyBatis 技术详解](https://blog.csdn.net/m0_59281987/article/details/127768618)

### 9.3 博客文章
- [MyBatis 核心原理分析](https://zhuanlan.zhihu.com/p/347935099)
- [MyBatis 执行流程详解](https://juejin.cn/post/7302698423840833576)
- [MyBatis 缓存机制分析](https://blog.csdn.net/feng8403000/article/details/122282095)

### 9.4 视频教程
- [MyBatis 核心组件详解](https://www.bilibili.com/video/BV1eT41197VG/)

### 9.5 最佳实践
- [MyBatis 官方最佳实践](https://mybatis.org/mybatis-3/zh/getting-started.html)
- [阿里巴巴 Java 开发手册 - MyBatis 部分](https://github.com/alibaba/p3c/blob/master/p3c-gitbook/chapter2.md)