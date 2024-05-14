
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

docker build -t bage/myapp .

// -- docker build --build-arg JAR_FILE=target/\*.jar -t myorg/myapp .

docker run -p 8080:8080 bage/myapp

docker tag bage/myapp bage/myapp

docker push bage/myapp

```



push 到远程仓库

https://hub.docker.com/repository/docker/bage2014/study/general

```

docker tag myapp bage2014/study

docker push bage2014/study:tagname


docker push bage2014/study


kubectl create deployment study --image=bage2014/study --dry-run -o=yaml > deployment.yaml

echo --- >> deployment.yaml

kubectl create service clusterip study --tcp=8080:8080 --dry-run -o=yaml >> deployment.yaml

kubectl apply -f deployment.yaml

kubectl port-forward svc/study 8080:8080
```



push 异常

```
denied: requested access to the resource is denied

```

build异常 

https://github.com/spring-guides/gs-spring-boot-docker/issues/90




## 备注
环境安装 https://kubernetes.io/docs/tasks/tools/

https://kubernetes.io/docs/home/

https://kubernetes.p2hp.com/docs/setup/
http://docs.kubernetes.org.cn/


https://birthday.play-with-docker.com/kubernetes-docker-desktop/


