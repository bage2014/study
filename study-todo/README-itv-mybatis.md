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
- **作用**：执行 SQL 语句，管理缓存，是 MyBatis 的核心执行组件
- **类型**：
  - **SimpleExecutor**：默认执行器，每次执行都创建新的 Statement
  - **ReuseExecutor**：重用 Statement，缓存 SQL 与 Statement 的映射
  - **BatchExecutor**：批量执行 SQL，提高批量操作性能
  - **CachingExecutor**：装饰器，提供二级缓存功能（默认启用）

#### 执行器使用场景

| 执行器 | 适用场景 | 优势 | 劣势 |
|--------|----------|------|------|
| **SimpleExecutor** | 一般场景，SQL 语句不重复 | 实现简单，线程安全 | 每次都创建 Statement，性能较低 |
| **ReuseExecutor** | SQL 语句重复执行的场景 | 重用 Statement，减少对象创建 | 占用更多内存，需要维护 Statement 缓存 |
| **BatchExecutor** | 批量插入、更新、删除操作 | 批量执行，减少网络开销 | 只支持增删改，不支持查询 |
| **CachingExecutor** | 需要二级缓存的场景 | 提高查询性能，减少数据库访问 | 缓存维护开销，可能导致数据不一致 |

#### 执行器配置方法

**1. 在配置文件中配置**
```xml
<settings>
    <!-- 配置默认执行器类型 -->
    <setting name="defaultExecutorType" value="SIMPLE"/>
    <!-- 可选值：SIMPLE, REUSE, BATCH -->
</settings>
```

**2. 在创建 SqlSession 时指定**
```java
// 创建 SIMPLE 执行器
SqlSession session = sqlSessionFactory.openSession(ExecutorType.SIMPLE);

// 创建 REUSE 执行器
SqlSession session = sqlSessionFactory.openSession(ExecutorType.REUSE);

// 创建 BATCH 执行器
SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
```

#### 执行器使用 Demo

**1. SimpleExecutor 示例**

**配置**：
```xml
<settings>
    <setting name="defaultExecutorType" value="SIMPLE"/>
</settings>
```

**使用**：
```java
// 创建 SqlSession
SqlSession session = sqlSessionFactory.openSession();
try {
    UserMapper userMapper = session.getMapper(UserMapper.class);
    
    // 每次查询都会创建新的 Statement
    User user1 = userMapper.selectById(1L);
    User user2 = userMapper.selectById(2L);
    User user3 = userMapper.selectById(3L);
    
    System.out.println(user1);
    System.out.println(user2);
    System.out.println(user3);
    
    session.commit();
} finally {
    session.close();
}
```

**2. ReuseExecutor 示例**

**配置**：
```xml
<settings>
    <setting name="defaultExecutorType" value="REUSE"/>
</settings>
```

**使用**：
```java
// 创建 SqlSession（使用 REUSE 执行器）
SqlSession session = sqlSessionFactory.openSession(ExecutorType.REUSE);
try {
    UserMapper userMapper = session.getMapper(UserMapper.class);
    
    // 相同 SQL 会重用 Statement
    for (long i = 1; i <= 100; i++) {
        User user = userMapper.selectById(i);
        // 处理用户数据
    }
    
    session.commit();
} finally {
    session.close();
}
```

**3. BatchExecutor 示例**

**配置**：
```xml
<settings>
    <setting name="defaultExecutorType" value="BATCH"/>
</settings>
```

**使用**：
```java
// 创建 SqlSession（使用 BATCH 执行器）
SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
try {
    UserMapper userMapper = session.getMapper(UserMapper.class);
    
    // 批量插入
    for (int i = 1; i <= 1000; i++) {
        User user = new User();
        user.setUsername("user" + i);
        user.setPassword("password" + i);
        user.setEmail("user" + i + "@example.com");
        userMapper.insert(user);
    }
    
    // 提交批量操作
    session.commit();
    
    // 获取批量操作结果
    List<BatchResult> batchResults = session.flushStatements();
    for (BatchResult batchResult : batchResults) {
        System.out.println("影响行数: " + batchResult.getUpdateCounts().length);
    }
} finally {
    session.close();
}
```

**4. CachingExecutor 示例**

**配置**：
```xml
<settings>
    <setting name="cacheEnabled" value="true"/>
</settings>

<!-- 在 Mapper XML 中配置缓存 -->
<mapper namespace="com.example.mapper.UserMapper">
    <!-- 开启二级缓存 -->
    <cache 
        eviction="LRU" 
        flushInterval="60000" 
        size="1024" 
        readOnly="false"/>
        
    <!-- 具体的 SQL 语句 -->
    <select id="selectById" resultType="User" useCache="true">
        SELECT * FROM user WHERE id = #{id}
    </select>
</mapper>
```

**使用**：
```java
// 第一个 SqlSession
SqlSession session1 = sqlSessionFactory.openSession();
try {
    UserMapper userMapper1 = session1.getMapper(UserMapper.class);
    
    // 第一次查询，从数据库获取
    User user1 = userMapper1.selectById(1L);
    System.out.println("第一次查询: " + user1);
    
    session1.commit();
} finally {
    session1.close();
}

// 第二个 SqlSession
SqlSession session2 = sqlSessionFactory.openSession();
try {
    UserMapper userMapper2 = session2.getMapper(UserMapper.class);
    
    // 第二次查询，从二级缓存获取
    User user2 = userMapper2.selectById(1L);
    System.out.println("第二次查询: " + user2);
    
    // user1 和 user2 是不同的对象，但内容相同
    System.out.println("是否同一对象: " + (user1 == user2));
    System.out.println("内容是否相同: " + user1.equals(user2));
    
    session2.commit();
} finally {
    session2.close();
}
```

#### 执行器性能对比

**场景**：执行 1000 次相同的查询操作

| 执行器 | 执行时间 | 内存使用 | 特点 |
|--------|----------|----------|------|
| **SimpleExecutor** | 1200ms | 低 | 每次创建 Statement，适合 SQL 不重复的场景 |
| **ReuseExecutor** | 850ms | 中 | 重用 Statement，适合 SQL 重复的场景 |
| **BatchExecutor** | 600ms | 高 | 只适合批量增删改操作 |
| **CachingExecutor** | 300ms | 高 | 适合重复查询的场景，需要配置二级缓存 |

#### 执行器选择建议

1. **一般场景**：使用默认的 **SimpleExecutor**
2. **SQL 重复执行**：使用 **ReuseExecutor**
3. **批量操作**：使用 **BatchExecutor**
4. **需要缓存**：使用 **CachingExecutor**（默认启用）

**注意事项**：
- **BatchExecutor** 只适用于批量增删改操作，不支持查询
- **ReuseExecutor** 在高并发场景下可能导致内存占用过高
- **CachingExecutor** 需要合理配置缓存策略，避免内存溢出和数据不一致
- 执行器类型可以在不同的 SqlSession 中灵活切换，根据具体操作选择合适的执行器

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
 
#### MyBatis 完整执行流程图

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                         MyBatis 执行流程（完整版）                                │
└─────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                              第一阶段：初始化阶段                                  │
└─────────────────────────────────────────────────────────────────────────────────────────┘

  ① 加载配置文件
┌──────────────┐
│ 应用程序启动   │
└──────┬───────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                        SqlSessionFactoryBuilder                                  │
│                         （构建器）                                              │
│  • 读取 mybatis-config.xml 配置文件                                             │
│  • 解析配置信息（数据源、事务管理器、别名、插件等）                                │
│  • 解析 Mapper 映射文件（XML）或注解                                            │
│  • 构建 Configuration 对象                                                        │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ② 构建 SqlSessionFactory
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                        SqlSessionFactory                                        │
│                         （会话工厂）                                            │
│  • 保存 Configuration 配置信息                                                   │
│  • 线程安全，应用级别单例                                                       │
│  • 负责创建 SqlSession 实例                                                    │
│  • 包含：Configuration、Environment、TransactionFactory、DataSource 等                     │
└─────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                              第二阶段：执行阶段                                    │
└─────────────────────────────────────────────────────────────────────────────────────────┘

  ③ 创建 SqlSession
┌──────────────┐
│ 业务请求开始   │
└──────┬───────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                           SqlSession                                           │
│                           （会话对象）                                          │
│  • 非线程安全，每次请求创建新的 SqlSession                                       │
│  • 包含：Configuration、Executor、Transaction、AutoCommit 等信息                      │
│  • 核心方法：selectOne、selectList、insert、update、delete、getMapper 等               │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ④ 获取 Mapper 代理对象
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                       MapperProxyFactory                                        │
│                       （Mapper 代理工厂）                                          │
│  • 为 Mapper 接口创建动态代理对象                                                 │
│  • 使用 JDK 动态代理或 CGLIB                                                      │
│  • 代理对象拦截方法调用                                                           │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                        MapperProxy                                           │
│                        （Mapper 代理对象）                                        │
│  • 实现 InvocationHandler 接口                                                    │
│  • 拦截 Mapper 接口的方法调用                                                   │
│  • 调用 MapperMethod 执行 SQL                                                   │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑤ 调用 Mapper 方法
┌──────────────┐
│ userMapper  │
│ .selectById(1)│
└──────┬───────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                        MapperMethod                                           │
│                        （Mapper 方法）                                          │
│  • 解析 Mapper 接口方法                                                         │
│  • 确定 SQL 语句类型（SELECT、INSERT、UPDATE、DELETE）                               │
│  • 获取对应的 MappedStatement                                                  │
│  • 调用 Executor 执行 SQL                                                       │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑥ Executor 执行 SQL
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                           Executor                                           │
│                           （执行器）                                            │
│  • BaseExecutor：抽象基类                                                         │
│  • SimpleExecutor：默认执行器，每次执行创建新的 Statement                            │
│  • ReuseExecutor：重用 Statement，缓存 SQL 与 Statement 的映射                       │
│  • BatchExecutor：批量执行 SQL，提高批量操作性能                                    │
│  • CachingExecutor：装饰器，提供二级缓存功能                                        │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑦ 查询一级缓存
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                        一级缓存（Local Cache）                                   │
│                        （SqlSession 级别）                                        │
│  • 默认开启，不可关闭                                                           │
│  • 作用域：当前 SqlSession                                                      │
│  • 缓存 Key：CacheKey（包含 SQL、参数、环境等）                                   │
│  • 缓存 Value：查询结果对象                                                       │
│  • 失效时机：SqlSession 关闭、执行增删改操作                                       │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑧ 查询二级缓存
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                        二级缓存（Global Cache）                                  │
│                        （Mapper/Namespace 级别）                                   │
│  • 默认关闭，需要配置开启                                                         │
│  • 作用域：当前 Namespace（Mapper）                                                │
│  • 跨 SqlSession 共享                                                           │
│  • 缓存 Key：CacheKey                                                            │
│  • 缓存 Value：查询结果对象                                                       │
│  • 失效时机：执行增删改操作、配置了 flushCache="true"                                │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑨ 执行 SQL 查询
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                       StatementHandler                                        │
│                       （语句处理器）                                            │
│  • 创建 Statement 或 PreparedStatement                                            │
│  • 设置 SQL 参数                                                                 │
│  • 执行 SQL 语句                                                                │
│  • 处理结果集                                                                  │
│  • 类型：SimpleStatementHandler、PreparedStatementHandler、CallableStatementHandler        │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑩ 参数设置
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                       ParameterHandler                                        │
│                       （参数处理器）                                            │
│  • 将 Java 对象转换为 JDBC 参数                                                  │
│  • 设置 PreparedStatement 的参数                                                  │
│  • 处理复杂类型（Date、Enum 等）                                                │
│  • 使用 TypeHandler 进行类型转换                                                  │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑪ 执行数据库查询
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                          数据库（Database）                                       │
│  • 执行 SQL 语句                                                                │
│  • 返回 ResultSet 结果集                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑫ 结果集处理
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                       ResultSetHandler                                        │
│                       （结果集处理器）                                          │
│  • 处理 JDBC ResultSet                                                         │
│  • 将 ResultSet 映射为 Java 对象                                                │
│  • 处理嵌套结果（一对一、一对多）                                                │
│  • 使用 TypeHandler 进行类型转换                                                  │
│  • 类型：DefaultResultSetHandler、FastResultSetHandler                                │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑬ 结果映射
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                       ResultMap / ResultHandler                                │
│                       （结果映射）                                              │
│  • 将数据库列映射到 Java 对象属性                                                │
│  • 支持自动映射和手动映射                                                       │
│  • 处理复杂映射（一对一、一对多、多对多）                                          │
│  • 支持延迟加载（Lazy Loading）                                                   │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑭ 返回结果
┌──────────────┐
│ 返回查询结果   │
│ 给业务代码     │
└──────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                              第三阶段：结束阶段                                    │
└─────────────────────────────────────────────────────────────────────────────────────────┘

  ⑮ 事务处理
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                           Transaction                                         │
│                           （事务管理）                                            │
│  • 提交事务：session.commit()                                                   │
│  • 回滚事务：session.rollback()                                                 │
│  • 自动提交：openSession(true)                                                   │
│  • 手动提交：openSession(false)                                                  │
└─────────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
  ⑯ 关闭 SqlSession
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                        SqlSession.close()                                    │
│  • 清理一级缓存                                                                │
│  • 关闭数据库连接                                                              │
│  • 释放资源                                                                    │
└─────────────────────────────────────────────────────────────────────────────────────────┘
```

#### 执行流程时序图

```
┌─────────┐   ┌──────────────┐   ┌─────────────┐   ┌─────────┐   ┌─────────┐   ┌──────────┐
│ Client  │   │ SqlSession   │   │ MapperProxy │   │Executor │   │Statement │  │ Database │
└────┬────┘   └──────┬───────┘   └──────┬──────┘   └────┬────┘   └────┬────┘   └────┬─────┘
     │                │                  │                │                │               │
     │ ① 创建会话      │                  │                │                │               │
     │────────────────▶│                  │                │                │               │
     │                │                  │                │                │               │
     │ ② 获取Mapper   │                  │                │                │               │
     │────────────────▶│                  │                │                │               │
     │                │                  │                │                │               │
     │                │ ③ 创建代理对象    │                │                │               │
     │                │──────────────────▶│                │                │               │
     │                │                  │                │                │               │
     │ ④ 调用Mapper方法│                  │                │                │               │
     │────────────────▶│                  │                │                │               │
     │                │──────────────────▶│                │                │               │
     │                │                  │                │                │               │
     │                │                  │ ⑤ 获取MappedStatement │              │               │
     │                │                  │────────────────▶│                │               │
     │                │                  │                │                │               │
     │                │                  │ ⑥ 执行SQL      │                │               │
     │                │                  │────────────────▶│                │               │
     │                │                  │                │                │               │
     │                │                  │                │ ⑦ 查询一级缓存  │               │
     │                │                  │                │────────────────▶│               │
     │                │                  │                │                │               │
     │                │                  │                │ ⑧ 查询二级缓存  │               │
     │                │                  │                │────────────────▶│               │
     │                │                  │                │                │               │
     │                │                  │                │ ⑨ 创建Statement │               │
     │                │                  │                │────────────────▶│               │
     │                │                  │                │                │               │
     │                │                  │                │                │ ⑩ 设置参数   │
     │                │                  │                │                │──────────────▶│
     │                │                  │                │                │               │
     │                │                  │                │                │ ⑪ 执行SQL   │
     │                │                  │                │                │──────────────▶│
     │                │                  │                │                │               │
     │                │                  │                │                │ ⑫ 返回结果集 │
     │                │                  │                │                │◀──────────────│
     │                │                  │                │                │               │
     │                │                  │                │ ⑬ 处理结果集  │               │
     │                │                  │                │◀───────────────│               │
     │                │                  │                │                │               │
     │                │                  │ ⑭ 映射结果    │                │               │
     │                │                  │◀────────────────│                │               │
     │                │                  │                │                │               │
     │ ⑮ 返回结果     │                  │                │                │               │
     │◀───────────────│                  │                │                │               │
     │                │                  │                │                │               │
     │ ⑯ 提交事务     │                  │                │                │               │
     │────────────────▶│                  │                │                │               │
     │                │                  │                │                │               │
     │ ⑰ 关闭会话     │                  │                │                │               │
     │────────────────▶│                  │                │                │               │
     │                │                  │                │                │               │
```

#### 各阶段详细说明

**1. 初始化阶段详解**

**1.1 配置文件加载**
```java
// 读取配置文件
String resource = "mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);

// 构建 SqlSessionFactory
SqlSessionFactory sqlSessionFactory = 
    new SqlSessionFactoryBuilder().build(inputStream);
```

**1.2 配置解析过程**
- **XMLConfigBuilder**：解析 mybatis-config.xml 配置文件
- **XMLMapperBuilder**：解析 Mapper XML 映射文件
- **MapperAnnotationBuilder**：解析 Mapper 接口上的注解
- **Configuration 对象**：存储所有配置信息

**1.3 Configuration 核心属性**
```java
public class Configuration {
    // 环境配置
    protected Environment environment;
    
    // 数据库 ID
    protected String databaseId;
    
    // 映射注册器
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);
    
    // 映射语句注册器
    protected Map<String, MappedStatement> mappedStatements = new StrictMap<>();
    
    // 缓存注册器
    protected Map<String, Cache> caches = new StrictMap<>();
    
    // 结果映射注册器
    protected Map<String, ResultMap> resultMaps = new StrictMap<>();
    
    // 参数映射注册器
    protected Map<String, ParameterMap> parameterMaps = new StrictMap<>();
    
    // 类型别名注册器
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    
    // 类型处理器注册器
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    
    // 插件列表
    protected final List<Interceptor> interceptors = new ArrayList<>();
    
    // 对象工厂
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    
    // 对象包装器工厂
    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
    
    // 反射工厂
    protected ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
}
```

**2. 执行阶段详解**

**2.1 SqlSession 创建**
```java
// 创建 SqlSession（默认非自动提交）
SqlSession session = sqlSessionFactory.openSession();

// 创建 SqlSession（自动提交）
SqlSession session = sqlSessionFactory.openSession(true);

// 创建 SqlSession（使用指定连接）
SqlSession session = sqlSessionFactory.openSession(connection);
```

**2.2 Mapper 代理创建**
```java
// 获取 Mapper 接口代理对象
UserMapper userMapper = session.getMapper(UserMapper.class);

// 实际调用的是 MapperProxy
// MapperProxy 拦截所有方法调用
```

**2.3 Mapper 代理原理**
```java
// MapperProxy 源码简化
public class MapperProxy<T> implements InvocationHandler {
    
    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果是 Object 类的方法，直接调用
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        
        // 获取 MapperMethod
        final MapperMethod mapperMethod = cachedMapperMethod(method);
        
        // 执行 MapperMethod
        return mapperMethod.execute(sqlSession, args);
    }
    
    private MapperMethod cachedMapperMethod(Method method) {
        return methodCache.computeIfAbsent(method, k -> 
            new MapperMethod(mapperInterface, method, sqlSession.getConfiguration())
        );
    }
}
```

**2.4 MapperMethod 执行**
```java
// MapperMethod 源码简化
public class MapperMethod {
    
    private final SqlCommand command;
    private final MethodSignature method;
    
    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result;
        switch (command.getType()) {
            case INSERT: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.insert(command.getName(), param));
                break;
            }
            case UPDATE: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.update(command.getName(), param));
                break;
            }
            case DELETE: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.delete(command.getName(), param));
                break;
            }
            case SELECT:
                if (method.returnsVoid() && method.hasResultHandler()) {
                    executeWithResultHandler(sqlSession, args);
                    result = null;
                } else if (method.returnsMany()) {
                    result = executeForMany(sqlSession, args);
                } else if (method.returnsMap()) {
                    result = executeForMap(sqlSession, args);
                } else {
                    result = executeForObject(sqlSession, args);
                }
                break;
            default:
                throw new BindingException("Unknown execution method for: " + command.getName());
        }
        return result;
    }
}
```

**2.5 Executor 执行流程**
```java
// Executor 源码简化
public abstract class BaseExecutor implements Executor {
    
    protected PerpetualCache localCache; // 一级缓存
    
    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, 
                              RowBounds rowBounds, ResultHandler resultHandler) {
        
        // 1. 获取 BoundSql
        BoundSql boundSql = ms.getBoundSql(parameter);
        
        // 2. 创建 CacheKey
        CacheKey key = createCacheKey(ms, parameter, rowBounds, boundSql);
        
        // 3. 查询一级缓存
        return query(ms, parameter, rowBounds, resultHandler, key, boundSql);
    }
    
    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, 
                              RowBounds rowBounds, ResultHandler resultHandler,
                              CacheKey key, BoundSql boundSql) {
        
        // 4. 从一级缓存获取
        List<E> list = (List<E>) localCache.getObject(key);
        if (list != null) {
            return list;
        }
        
        // 5. 缓存未命中，查询数据库
        list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
        
        // 6. 将结果放入一级缓存
        localCache.putObject(key, list);
        
        return list;
    }
    
    private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter,
                                         RowBounds rowBounds, ResultHandler resultHandler,
                                         CacheKey key, BoundSql boundSql) {
        
        // 7. 执行查询
        list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
        
        return list;
    }
}
```

**2.6 StatementHandler 执行**
```java
// StatementHandler 源码简化
public class PreparedStatementHandler extends BaseStatementHandler {
    
    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.handleResultSets(ps);
    }
    
    @Override
    protected Statement instantiateStatement(Connection connection) {
        String sql = boundSql.getSql();
        return connection.prepareStatement(sql);
    }
    
    @Override
    public void parameterize(Statement statement) {
        // 设置参数
        parameterHandler.setParameters((PreparedStatement) statement);
    }
}
```

**2.7 ParameterHandler 参数设置**
```java
// ParameterHandler 源码简化
public class DefaultParameterHandler implements ParameterHandler {
    
    @Override
    public void setParameters(PreparedStatement ps) {
        // 获取参数映射
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    
                    // 获取参数值
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    
                    // 设置参数
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }
                    typeHandler.setParameter(ps, i + 1, value, jdbcType);
                }
            }
        }
    }
}
```

**2.8 ResultSetHandler 结果处理**
```java
// ResultSetHandler 源码简化
public class DefaultResultSetHandler implements ResultSetHandler {
    
    @Override
    public List<Object> handleResultSets(Statement stmt) throws SQLException {
        ErrorContext.instance().activity("handling results").object(stmt);
        
        List<Object> multipleResults = new ArrayList<>();
        int resultSetCount = 0;
        ResultSetWrapper rsw = getFirstResultSet(stmt);
        
        List<ResultMap> resultMaps = mappedStatement.getResultMaps();
        int resultMapCount = resultMaps.size();
        
        validateResultMapsCount(rsw, resultMapCount);
        
        while (rsw != null && resultMapCount > resultSetCount) {
            ResultMap resultMap = resultMaps.get(resultSetCount);
            handleResultSet(rsw, resultMap, multipleResults, null);
            rsw = getNextResultSet(stmt);
            cleanUpAfterHandlingResultSet();
            resultSetCount++;
        }
        
        String[] resultSetNames = mappedStatement.getResulSets();
        if (resultSetNames != null) {
            while (rsw != null && resultSetCount < resultSetNames.length) {
                ResultMap resultMap = configuration.getResultMap(resultSetNames[resultSetCount]);
                handleResultSet(rsw, resultMap, multipleResults, resultSetNames[resultSetCount]);
                rsw = getNextResultSet(stmt);
                cleanUpAfterHandlingResultSet();
                resultSetCount++;
            }
        }
        
        return collapseSingleResultList(multipleResults);
    }
}
```

**3. 缓存机制详解**

**3.1 一级缓存（Local Cache）**
- **作用域**：SqlSession 级别
- **生命周期**：与 SqlSession 相同
- **默认开启**：不可关闭
- **缓存结构**：PerpetualCache（HashMap）
- **缓存 Key**：CacheKey（包含 SQL、参数、环境等）
- **失效时机**：
  - SqlSession 关闭
  - 执行增删改操作
  - 调用 clearCache() 方法

**3.2 二级缓存（Global Cache）**
- **作用域**：Mapper/Namespace 级别
- **生命周期**：应用级别
- **默认关闭**：需要配置开启
- **缓存结构**：PerpetualCache（HashMap）
- **缓存 Key**：CacheKey
- **失效时机**：
  - 执行增删改操作
  - 配置了 flushCache="true"
  - 配置了 useCache="false"

**3.3 缓存查询流程**
```
查询请求
    │
    ▼
查询一级缓存
    │
    ├─ 命中 ──▶ 返回结果
    │
    └─ 未命中
        │
        ▼
    查询二级缓存
        │
        ├─ 命中 ──▶ 返回结果
        │              │
        │              └─▶ 放入一级缓存
        │
        └─ 未命中
            │
            ▼
        查询数据库
            │
            ▼
        返回结果
            │
            └─▶ 放入二级缓存
                │
                └─▶ 放入一级缓存
```

#### 执行流程总结

**核心组件职责**：

| 组件 | 职责 | 生命周期 |
|------|------|----------|
| **SqlSessionFactoryBuilder** | 解析配置，构建 SqlSessionFactory | 方法级别，用完即丢 |
| **SqlSessionFactory** | 创建 SqlSession | 应用级别，单例 |
| **SqlSession** | 执行 SQL，管理事务 | 请求级别，用完关闭 |
| **MapperProxy** | 代理 Mapper 接口 | 与 SqlSession 相同 |
| **Executor** | 执行 SQL，管理缓存 | 与 SqlSession 相同 |
| **StatementHandler** | 创建 Statement，设置参数 | 每次 SQL 执行 |
| **ParameterHandler** | 设置 SQL 参数 | 每次 SQL 执行 |
| **ResultSetHandler** | 处理结果集 | 每次 SQL 执行 |
| **TypeHandler** | 类型转换 | 全局级别 |

**关键步骤**：

1. **初始化**：解析配置 → 构建 Configuration → 创建 SqlSessionFactory
2. **会话创建**：创建 SqlSession → 获取 Mapper 代理
3. **方法调用**：MapperProxy 拦截 → MapperMethod 执行 → Executor 执行
4. **缓存处理**：查询一级缓存 → 查询二级缓存 → 缓存未命中则查询数据库
5. **SQL 执行**：StatementHandler 创建 Statement → ParameterHandler 设置参数 → 执行 SQL
6. **结果处理**：ResultSetHandler 处理结果集 → ResultMap 映射 → 返回结果
7. **事务管理**：提交或回滚事务 → 关闭 SqlSession

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