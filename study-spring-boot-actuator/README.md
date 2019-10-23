# study-spring-boot-actuator #
Spring Boot Actuator 学习笔记
## 参考链接 ##
- Spring Boot Actuator [https://spring.io/guides/gs/actuator-service/](https://spring.io/guides/gs/actuator-service/)
- Spring Boot - Actuator [https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-actuator](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-actuator)
- Spring Boot Actuator [https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready0](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready)

## 常用endpoint ##

http://localhost:8080/actuator/health 


http://localhost:8080/actuator/auditevents 

    ID	Description	Enabled by default
    auditevents
    
    Exposes audit events information for the current application.
    
    Yes. Requires an AuditEventRepository bean
    
    beans
    
    Displays a complete list of all the Spring beans in your application.
    
    Yes
    
    caches
    
    Exposes available caches.
    
    Yes
    
    conditions
    
    Shows the conditions that were evaluated on configuration and auto-configuration classes and the reasons why they did or did not match.
    
    Yes
    
    configprops
    
    Displays a collated list of all @ConfigurationProperties.
    
    Yes
    
    env
    
    Exposes properties from Spring’s ConfigurableEnvironment.
    
    Yes
    
    flyway
    
    Shows any Flyway database migrations that have been applied.
    
    Yes
    
    health
    
    Shows application health information.
    
    Yes
    
    httptrace
    
    Displays HTTP trace information (by default, the last 100 HTTP request-response exchanges).
    
    Yes. Requires an HttpTraceRepository bean
    
    info
    
    Displays arbitrary application info.
    
    Yes
    
    integrationgraph
    
    Shows the Spring Integration graph.
    
    Yes
    
    loggers
    
    Shows and modifies the configuration of loggers in the application.
    
    Yes
    
    liquibase
    
    Shows any Liquibase database migrations that have been applied.
    
    Yes
    
    metrics
    
    Shows ‘metrics’ information for the current application.
    
    Yes
    
    mappings
    
    Displays a collated list of all @RequestMapping paths.
    
    Yes
    
    scheduledtasks
    
    Displays the scheduled tasks in your application.
    
    Yes
    
    sessions
    
    Allows retrieval and deletion of user sessions from a Spring Session-backed session store. Not available when using Spring Session’s support for reactive web applications.
    
    Yes
    
    shutdown
    
    Lets the application be gracefully shutdown.
    
    No
    
    threaddump
    
    Performs a thread dump.
    
    Yes



