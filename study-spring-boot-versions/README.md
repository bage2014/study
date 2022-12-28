# study- spring- boot- versions

文档编写目的

当前进度

后期规划

项目代办

## 参考链接

官网文档 https://github.com/spring-projects/spring-boot/wiki#release-notes

快速开始 https://docs.spring.io/spring-boot/docs/current/reference/html/

快速开始： https://spring.io/projects/spring-boot#overview

依赖配置 https://start.spring.io/

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



### 搭建Spring boot 3.1 环境

pom.xml 主要文件内容

```

   <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>17</java.version>
    
    <spring.boot.base.version>3.0.1</spring.boot.base.version>
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

    </dependencies>
  </dependencyManagement>
  
```

启动类

```

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class DemoApplication {


  public static void main(String[] args) {
  SpringApplication.run(DemoApplication.class, args);
  }

  @GetMapping("/hello")
  public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
  return String.format("Hello %s!", name);
  }

}
```

## 应用实践

下载运行

```
Publish Remote

```

## 原理解析

基本原理