# study-spring-boot-docker #
study-spring-boot-docker 学习笔记
## 参考链接 ##
- Spring Boot with Docker 官网 [https://spring.io/guides/gs/spring-boot-docker/](https://spring.io/guides/gs/spring-boot-docker/ "Spring Boot with Docker")
- Maven编译插件 [https://github.com/spotify/dockerfile-maven](https://github.com/spotify/dockerfile-maven "Maven编译插件")
- 构建Dockerfile报错问题处理 [https://blog.csdn.net/superdangbo/article/details/78540829](https://blog.csdn.net/superdangbo/article/details/78540829 "No plugin found for prefix 'docker'...")

## 相关配置 ##
### pom.xml ###
增加maven插件配置

	<plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.1</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>shade</goal>
                </goals>
                <configuration>
                    <transformers>
                        <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                            <mainClass>com.bage.Application</mainClass>
                        </transformer>
                    </transformers>
                </configuration>
            </execution>
        </executions>
    </plugin>

    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <executions>
            <execution>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

导出Jar文件

    mvn install

编辑Dockerfile文件

    # 使用的jdk，可以进行自定义
	FROM primetoninc/jdk:1.8
    
    # 需要修改 myapp.jar 为自己的jar名称
    ADD myapp.jar app.jar
    
    RUN bash -c 'touch /app.jar'
    
    ENTRYPOINT ["java","-jar","/app.jar"]

上传Dockerfile、jar文件到docker所在主机上面，放到同一目录下

    $ ls 
	myapp.jar  Dockerfile


构建镜像(Dockfile和jar目录下)

    docker build -t my-app ./
