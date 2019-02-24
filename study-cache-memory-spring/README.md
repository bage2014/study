# study-cache-memory-spring #
Spring 使用cache

## 参考链接 ##
- Spring+EhCache缓存实例 [http://www.cnblogs.com/mxmbk/articles/5162813.html](http://www.cnblogs.com/mxmbk/articles/5162813.html "Spring+EhCache缓存实例")
- spring集成redis [https://www.cnblogs.com/hello-daocaoren/p/7891907.html](https://www.cnblogs.com/hello-daocaoren/p/7891907.html "spring集成redis")
- spring-data-keyvalue-examples [https://github.com/spring-projects/spring-data-keyvalue-examples](https://github.com/spring-projects/spring-data-keyvalue-examples "spring-data-keyvalue-examples")
- 各种缓存介绍 [https://blog.csdn.net/Jesse_cool/article/details/76174778](https://blog.csdn.net/Jesse_cool/article/details/76174778)
- 缓存的基本概念和常用的缓存技术 [https://blog.csdn.net/qq_27607965/article/details/79814833](https://blog.csdn.net/qq_27607965/article/details/79814833)

## 注意事项 ##
- 内嵌Tomcat环境下自定义Redis注解失效

## 注意事项 ##

### 参考链接 ###
- 缓存的三种方式 [https://www.cnblogs.com/llzhang123/p/9037346.html](https://www.cnblogs.com/llzhang123/p/9037346.html)

### 事项点 ###
- **先更新数据库，后缓存失效**；不是先更新数据库后更新缓存；也不是先缓存更新后再到数据库；
- **设置缓存失效时间**
- **写操作要考虑并发加锁**



