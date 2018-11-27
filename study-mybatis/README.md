# study-mybatis
学习mybatis的笔记<br>
TODO 有待继续了解 typeHandlers、处理枚举类型...

## 参考链接
MyBatis 官网： [http://www.mybatis.org/mybatis-3/zh/configuration.html](http://www.mybatis.org/mybatis-3/zh/configuration.html "MyBatis官网")

## 项目说明
src\main\java\com\bage\study\mybatis 目录下
- tutorial 入门程序

## XML 配置[顺序相对有序]
详见 ： \src\main\resources\mybatis-config.xml

- properties 属性，用于设置一些变量值，配置属性
  - [配置]可外部配置且可动态替换；既可以在Java 属性文件中配置，亦可通过 properties 元素的子元素来传递
  - [优先级]Java方法参数属性具有最高优先级，resource/url 属性中指定的配置文件次之， properties 属性中指定的属性最低
  - [高级特性]MyBatis 3.4.2开始，可以为占位符指定一个默认值、使用 OGNL 表达式的三元运算符等,
  
- settings 设置，修改mybatis的配置
  - 缓存、主键、执行器等，MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为
  
- typeAliases 类型别名，用来减少类完全限定名的冗余
  - 类型别名是为 Java 类型设置一个短的名字。它只和 XML 配置有关
  - 主要有 类型别名、包别名和注解别名
  - 常见 Java 类型内建的相应的类型别名
  -     别名	映射的类型
        _byte	byte
        _long	long
        _short	short
        _int	int
        _integer	int
        _double	double
        _float	float
        _boolean	boolean
        string	String
        byte	Byte
        long	Long
        short	Short
        int	Integer
        integer	Integer
        double	Double
        float	Float
        boolean	Boolean
        date	Date
        decimal	BigDecimal
        bigdecimal	BigDecimal
        object	Object
        map	Map
        hashmap	HashMap
        list	List
        arraylist	ArrayList
        collection	Collection
        iterator	Iterator
        
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



