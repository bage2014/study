# study-Jenkins

## 参考链接
官网文档

https://www.jenkins.io/zh/
https://www.jenkins.io/zh/doc/book/installing/#downloading-and-running-jenkins-in-docker
https://mp.weixin.qq.com/s/NHZLONQJTfyurUGkKjlGzg

https://hub.docker.com/r/jenkinsci/blueocean/

部署启动

https://www.jenkins.io/zh/doc/book/installing/

maven 应用： https://www.jenkins.io/zh/doc/tutorials/build-a-java-app-with-maven/

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





https://www.jenkins.io/zh/doc/book/installing/