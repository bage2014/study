# study-spring-boot-security #
Spring Security 学习笔记

实现自定义登录认证

## 参考链接 ##
- Securing a Web Application [https://spring.io/guides/gs/securing-web/](https://spring.io/guides/gs/securing-web/)
- Spring Security Reference [https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/reference/htmlsingle/](https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/reference/htmlsingle/)
- Hierarchical Roles [https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#authz-hierarchical-roles](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#authz-hierarchical-roles)

## 角色权限控制 ##
- ADMIN 角色(admin/admin) [http://localhost:8080/admin/hello](http://localhost:8080/admin/hello)
- DBA+ADMIN 角色(dba/dba) [http://localhost:8080/db/hello](http://localhost:8080/db/hello)

## 角色继承 ##
目前 ADMIN > USER





