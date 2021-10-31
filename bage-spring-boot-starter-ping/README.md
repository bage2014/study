# bage-spring-boot-starter-ping #
自定义 spring-boot-starter学习笔记

## 步骤 ##
1. 引入Spring 自动配置相关的依赖

```
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    <spring.boot.base.version>2.0.1.RELEASE</spring.boot.base.version>
</properties>
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
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

2. 编写AutoConfigure配置类

```
public class PingService {

    public String ping(String msg) {
        return String.format("PingService ping , msg = %s", msg);
    }
}
```

```
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(PingService.class)
public class PingAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(PingService.class)
    @ConditionalOnProperty(prefix = "bage.ping", value = "enabled", havingValue = "true")
    PingService pingService() {
        return new PingService();
    }

}
```

3. 编写 spring.factories 注册配置文件

新建文件 

```
\src\main\resources\META-INF\spring.factories
```

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.bage.starter.ping.PingAutoConfigure
```

4. Spring Boot 项目直接引用即可