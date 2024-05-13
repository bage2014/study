
## 参考链接 ##

Docker Desktop 安装K8s
https://docs.docker.com/desktop/kubernetes/

Spring boot 集成
Spring 基本 https://spring.io/guides/gs/rest-service
部署到 Docker https://spring.io/guides/topicals/spring-boot-docker

集成K8S https://spring.io/guides/gs/spring-boot-kubernetes



打包镜像

```

mvn install

docker build -t myorg/myapp .

// -- docker build --build-arg JAR_FILE=target/\*.jar -t myorg/myapp .

docker run -p 8080:8080 myorg/myapp

docker tag myorg/myapp myorg/myapp

docker push myorg/myapp

```



build异常 

https://github.com/spring-guides/gs-spring-boot-docker/issues/90




## 备注
环境安装 https://kubernetes.io/docs/tasks/tools/

https://kubernetes.io/docs/home/

https://kubernetes.p2hp.com/docs/setup/
http://docs.kubernetes.org.cn/


https://birthday.play-with-docker.com/kubernetes-docker-desktop/


