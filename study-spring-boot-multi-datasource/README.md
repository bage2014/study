# Accessing JPA Data with REST #
study-spring-boot-multi-datasource 学习笔记

## 参考链接 ##
- mybatis-spring-boot-multi-ds-demo [https://github.com/kazuki43zoo/mybatis-spring-boot-multi-ds-demo](https://github.com/kazuki43zoo/mybatis-spring-boot-multi-ds-demo)
- Configure Two DataSources [https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-two-datasources](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-two-datasources)
- Spring Boot Multiple Datasource [https://stackoverflow.com/questions/27614301/spring-boot-multiple-datasource](https://stackoverflow.com/questions/27614301/spring-boot-multiple-datasource)
- Multi Datasource #78 [https://github.com/mybatis/spring-boot-starter/issues/78](https://github.com/mybatis/spring-boot-starter/issues/78)
- Spring Boot Configure and Use Two DataSources [https://stackoverflow.com/questions/30337582/spring-boot-configure-and-use-two-datasources](https://stackoverflow.com/questions/30337582/spring-boot-configure-and-use-two-datasources)
- Spring Boot Configure and Use Two DataSources [https://www.java4s.com/spring-boot-tutorials/spring-boot-jdbc-mysql-how-to-configure-multiple-datasource/](https://www.java4s.com/spring-boot-tutorials/spring-boot-jdbc-mysql-how-to-configure-multiple-datasource/)



## Druid

initialSize=15。该配置项控制数据源初始化时就创建出来的连接数，默认是0；
maxActive=30。该配置项决定数据源的最大连接数，即可用连接和已用连接之和不能大于这个值，默认是8；
minIdle=15。该配置项决定数据源的核心连接数，所谓核心连接，就是只要空闲时间满足小于等于maxEvictableIdleTimeMillis的连接，这个连接就不会被销毁，默认是0。关于这个参数，需要结合minEvictableIdleTimeMillis和maxEvictableIdleTimeMillis一起理解；
minEvictableIdleTimeMillis=60000。连接会被销毁的最小空闲时间。非核心连接只要空闲时间大于等于该配置，那么这个非核心连接就会被销毁。默认是30m，如果不想常态让数据源维持太多连接，可以考虑配置一个较小的值；
maxEvictableIdleTimeMillis=25200000。连接会被销毁的最大空闲时间。无论是核心连接还是非核心连接，只要空闲时间大于该配置，那么这个连接就会被销毁，默认是7h，建议配置为略小于数据库服务端连接空闲超时时间，比如MySQL服务端连接的空闲超时时间是8h，那么这里可以配置为7h；
phyTimeoutMillis=25200000。连接的物理存活时间，默认是 -1表示一直存活，可以配置为和maxEvictableIdleTimeMillis一样的值，在配置了maxEvictableIdleTimeMillis时，phyTimeoutMillis也可以不配置；

作者：半夏之沫
链接：https://juejin.cn/post/7372084329079865354
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。