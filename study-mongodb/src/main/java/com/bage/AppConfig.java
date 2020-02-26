package com.bage;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration
public class AppConfig {

  /*
   * Use the standard Mongo driver API to create a com.mongodb.MongoClient instance.
   */
//   public @Bean MongoClient mongoClient() {
////       return new MongoClient("localhost");
//       return new MongoClient(singletonList(new ServerAddress("192.168.146.133", 27017)),
//               singletonList(MongoCredential.createCredential("test", "bagedb", "bagetest".toCharArray())));
//   }
}