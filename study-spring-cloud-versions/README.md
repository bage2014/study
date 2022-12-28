# study- spring- cloud- versions

文档编写目的

当前进度

后期规划

项目代办

## 参考链接

官网文档 https://spring.io/projects/spring-cloud#learn

快速开始 https://docs.spring.io/spring-cloud/docs/current/reference/html/

https://spring.io/guides/gs/cloud-circuit-breaker/

https://github.com/spring-guides/gs-cloud-circuit-breaker

依赖配置 https://start.spring.io/

代码参考 https://github.com/spring-cloud-samples/spring-cloud-circuitbreaker-demo

代码依赖 https://github.com/bage2014/study/tree/master/study-spring-cloud-circuit-breaker

## 快速开始

### 设置JDK 17 环境

下载JDK 17 【Mac M1 是ARM 架构】

https://www.oracle.com/java/technologies/downloads/#jdk17-mac

设置环境变量

```
vi .bash_profile 
```

.bash_profile 内容 

```
export JAVA_HOME=/Users/bage/bage/software/jdk-17.0.5.jdk/Contents/Home

export PATH=$JAVA_HOME/bin:$PATH
```

更新.bash_profile 内容 

```
source .bash_profile 
```



### 搭建Spring cloud 环境

以熔断为例

https://spring.io/guides/gs/cloud-circuit-breaker/

pom.xml 主要文件内容

```
   
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>17</java.version>

    <spring.boot.base.version>3.0.1</spring.boot.base.version>
    <spring.cloud-version>2022.0.0</spring.cloud-version>

  </properties>

  <dependencies>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>

  </dependencies>
  <dependencyManagement>
    <dependencies>
      <dependency>
        <!-- Import dependency management from Spring Boot -->
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring.boot.base.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>

      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring.cloud-version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  
```

启动类

```
package hello;

import com.bage.study.spring.cloud.versions.Book2Service;
import com.bage.study.spring.cloud.versions.BookService;
import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@SpringBootApplication
public class ReadingApplication {

  @Autowired
  private BookService bookService;
  @Autowired
  private Book2Service book2Service;

  @RequestMapping("/to-read")
  public Mono<String> toRead() {
    return bookService.readingList();
  }
  @RequestMapping("/to-read2")
  public Mono<String> toRead2() {
    return book2Service.readingList();
  }

  public static void main(String[] args) {
    SpringApplication.run(ReadingApplication.class, args);
  }
}
```

请求

```
有正确返回

http://localhost:8080/to-read

返回兜底响应
http://localhost:8080/to-read2
```





## 应用实践

下载运行

```

```

## 原理解析

基本原理