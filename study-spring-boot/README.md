配置 HTTPS



生成key

```common-lisp
keytool -genkey -alias tutorials -dname "CN=bage,OU=bage,O=bage,L=bage,ST=Shanghai,C=CN" -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 365
```

#SSL 配置

```properties
server:
  port: 8080
  servlet:
    context-path: /
  ssl:
    key-alias: tutorials
    key-password: bage
    key-store-type: PKCS12
    key-store: classpath:keystore.p12
    enabled: true
```



注意：请将在上一步生成的证书放到src/main/resources目录下。



配置Bean 

     
    import org.apache.catalina.Context;
    import org.apache.catalina.connector.Connector;
    import org.apache.tomcat.util.descriptor.web.SecurityCollection;
    import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
    @Configuration
    public class HttpsConfig {
    
        //如果没有使用默认值80
        @Value("${http.port:8080}")
        Integer httpPort;
    
        //正常启用的https端口 如443
        @Value("${server.port}")
        Integer httpsPort;
    
        @Bean
        public TomcatServletWebServerFactory servletContainer() {
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
                @Override
                protected void postProcessContext(Context context) {
                    SecurityConstraint constraint = new SecurityConstraint();
                    constraint.setUserConstraint("CONFIDENTIAL");
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern("/*");
                    constraint.addCollection(collection);
                    context.addConstraint(constraint);
                }
            };
            tomcat.addAdditionalTomcatConnectors(httpConnector());
            return tomcat;
        }
    
        @Bean
        public Connector httpConnector() {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            //Connector监听的http的端口号
            connector.setPort(httpPort);
            connector.setSecure(false);
            //监听到http的端口号后转向到的https的端口号
            connector.setRedirectPort(httpsPort);
            return connector;
        }
    }
    
  
## Filter 
    
    https://www.baeldung.com/spring-boot-add-filter

