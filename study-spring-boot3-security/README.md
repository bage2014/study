# study-spring-boot3-security


## 参考链接

官网文档
https://docs.spring.io/spring-boot/how-to/security.html
https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
官方样例【重点看看】
https://github.com/spring-projects/spring-security-samples/

jwt login https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/jwt/login
oauth2 https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/oauth2/authorization-server
https://www.baeldung.com/spring-dynamic-client-registration

博客
https://dzone.com/articles/secure-spring-rest-with-spring-security-and-oauth2

自定义client https://stackoverflow.com/questions/77969646/how-to-register-oauth2-clients-from-a-persistent-database-in-spring-authorizatio

## 登陆服务

### 基本登陆

user/user 登陆

admin/admin 登陆

```
http://localhost:8080/api/admin/hello

http://localhost:8080/api/user/hello

http://localhost:8080/login

http://localhost:8080/logout

```



### 自定义用户

自定义用户信息

```
@Component
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if("zhangsan".equals(username)){
            return User.withDefaultPasswordEncoder()
                    .username("zhangsan")
                    .password("admin")
                    .roles("ADMIN")
                    .build();
        }
        if("lisi".equals(username)){
            return User.withDefaultPasswordEncoder()
                    .username("lisi")
                    .password("admin")
                    .roles("USER")
                    .build();
        }
        // todo load from database 
        return null;
    }
}
```



## 认证服务

参考样例 https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/jwt/login





## 原理解析

基本原理