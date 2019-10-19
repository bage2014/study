# study-spring-boot-sso-oauth2-server-jwt #
Spring Boot Oauth2 JWT 学习笔记
## 参考链接 ##
[https://spring.io/guides/tutorials/spring-boot-oauth2/](https://spring.io/guides/tutorials/spring-boot-oauth2/)

[https://www.baeldung.com/spring-security-oauth-jwt](https://www.baeldung.com/spring-security-oauth-jwt)

[https://blog.marcosbarbero.com/centralized-authorization-jwt-spring-boot2/](https://blog.marcosbarbero.com/centralized-authorization-jwt-spring-boot2/)

[https://www.tutorialspoint.com/spring_boot/spring_boot_oauth2_with_jwt.htm](https://www.tutorialspoint.com/spring_boot/spring_boot_oauth2_with_jwt.htm)

[https://github.com/habuma/spring-security-oauth2-jwt-example](https://github.com/habuma/spring-security-oauth2-jwt-example)

[https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html/boot-features-security-oauth2-authorization-server.html#oauth2-boot-testing-authorization-code-flow](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html/boot-features-security-oauth2-authorization-server.html#oauth2-boot-testing-authorization-code-flow)

[https://github.com/murraco/spring-boot-jwt](https://github.com/murraco/spring-boot-jwt)


curl client:secret@localhost:8080/oauth/token -d grant_type=client_credentials

curl client:secret@localhost:8080/oauth/token -d grant_type=password -d username=bage -d password=bage

curl localhost:8080/user/me -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzEzNDgzNzgsInVzZXJfbmFtZSI6ImJhZ2UiLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjVjMjA3M2E1LTM4ZTAtNDg0Yy1hNTI5LWI3M2Y3MGYzMDRjOCIsImNsaWVudF9pZCI6ImNsaWVudCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.AxHDPwLrM_NoEgojbOnfp1HDyPzbPQAaHBniezl41tNLO6j5s1Ko0UAw6ns2n9xFPqxJxMNe13UypRNqQVl5RD0YKgO2BSAtOJZKPoyHdPxs2ryYEmKRQEzY5LMafgdCvk0a4yixg7d4G-3m65Lwy4e9Rv9an84ADpHNv3Iw8YVV41htEHsyjzVw_Dul36do3YQrZy_87ZVFEKidS7oxPDoJ1yIu5Bmn8Ou2Ghn6myLt8vKhJU7ExsESK9iFCOgP2_yUrzosqwV1hC1HuYl2bOsnR4cc7iXauEl3Mb6DswBtTQaQdA5ZEfqrJQRb_NQAYhwDPltr8dlIouVuI_9FKA"




