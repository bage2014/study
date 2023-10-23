# study-Maven #
## 简介





## Key关键点

- Vs gradle
- 依赖顺序
- 生命周期
- 依赖范围





## 依赖顺序

https://my.oschina.net/u/4090830/blog/10089392

1. 最短路径原则：面对多级（两级及以上）的不同依赖，会优先选择路径最短的依赖；

2. 声明优先原则：面对多级（两级及以上）的同级依赖，先声明的依赖会覆盖后声明的依赖；

3. 同级依赖中，后声明的依赖会覆盖先声明的依赖；



## 生命周期

常用

```
mvn compile

mvn clean

mvn test

mvn package

mvn install

mvn deploy

```



## 依赖范围

依赖

```
compile 

provided

runtime 
```



## 优缺点

https://blog.csdn.net/yang_guang3/article/details/129006607





## B站视频



## 参考链接

集锦 

https://blog.csdn.net/yang_guang3/article/details/129006607

