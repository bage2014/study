# study-shiro-spring-boot #
Spring Boot 集成 Shiro

基于shiro的权限的功能
基本使用
权限持久化到数据库
权限的增删改查
权限的动态加载刷新

## 参考链接 ##
- github [https://github.com/apache/shiro](https://github.com/apache/shiro)
- 官网 [http://shiro.apache.org/documentation.html](http://shiro.apache.org/documentation.html)
- 配置 [http://shiro.apache.org/web.html#web-ini-configuration](http://shiro.apache.org/web.html#web-ini-configuration)
- Spring + Shiro [https://github.com/baichengzhou/SpringMVC-Mybatis-Shiro-redis-0.2](https://github.com/baichengzhou/SpringMVC-Mybatis-Shiro-redis-0.2)

## 使用说明 ##
- 启动环境 com.bage.AuthApplication
- 登录 user/user [http://localhost:8080/](http://localhost:8080/)
- 访问无权限控制API(api6/hello) [http://localhost:8080/api6/hello](http://localhost:8080/api6/hello)
- 插入权限控制 [http://localhost:8080/pathdef/insert](http://localhost:8080/pathdef/insert)
- 刷新权限 [http://localhost:8080/pathdef/refresh](http://localhost:8080/pathdef/refresh)
- 重新访问访问无权限控制API(api6/hello，此时会提示用户无权限) [http://localhost:8080/api6/hello](http://localhost:8080/api6/hello)
- 重新登录有权限用户 bage/bage [http://localhost:8080/](http://localhost:8080/)
- 重新访问访问无权限控制API(api6/hello，此时能正常访问) [http://localhost:8080/api6/hello](http://localhost:8080/api6/hello)



