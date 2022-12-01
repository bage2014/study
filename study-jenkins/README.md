# study-Jenkins

## 参考链接
官网文档 https://www.jenkins.io/zh/
https://www.jenkins.io/zh/doc/book/installing/#downloading-and-running-jenkins-in-docker
https://mp.weixin.qq.com/s/NHZLONQJTfyurUGkKjlGzg

Docker https://hub.docker.com/r/jenkinsci/blueocean/

安装部署 https://www.jenkins.io/zh/doc/book/installing/

部署启动

https://www.jenkins.io/zh/doc/book/installing/

maven 应用： https://www.jenkins.io/zh/doc/tutorials/build-a-java-app-with-maven/

流水线 https://www.jenkins.io/zh/doc/book/pipeline/multibranch/

https://www.jenkins.io/doc/book/pipeline/multibranch/



下载&启动

```
docker pull jenkinsci/blueocean

docker run --name=bage-jenkins -p 8080:8080   -v /Users/bage/bage/docker-data/jenkins:/var/jenkins_home jenkinsci/blueocean

```



访问

```
http://localhost:8080

docker exec -it bage-jenkins bash

cat /var/jenkins_home/secrets/initialAdminPassword

拷贝密码
dc631cefb1154ab39e54d538e99a09bd

bage/bage123456

```



设置时区

个人中心 - 设置 - 用户定义的时区

```
选择时区

```





发布

Publish Remote

```
Publish Remote


```





编译打包



```
Publish Remote


```





https://www.jenkins.io/zh/doc/book/installing/