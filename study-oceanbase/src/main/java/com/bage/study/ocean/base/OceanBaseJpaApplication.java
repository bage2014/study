package com.bage.study.ocean.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class OceanBaseJpaApplication {

  public static void main(String[] args) {
    SpringApplication.run(OceanBaseJpaApplication.class, args);
  }


  @Bean
  public CommandLineRunner demo(CustomerRepository repository) {
    return (args) -> {
      // save a few customers
//      repository.save(new Customer(System.currentTimeMillis()%100000,"Jack", "Bauer"));
//      repository.save(new Customer(System.currentTimeMillis()%100000,"Chloe", "O'Brian"));
//      repository.save(new Customer(System.currentTimeMillis()%100000,"Kim", "Bauer"));
//      repository.save(new Customer(System.currentTimeMillis()%100000,"David", "Palmer"));
//      repository.save(new Customer(System.currentTimeMillis()%100000,"Michelle", "Dessler"));

      // fetch all customers
      log.info("Customers found with findAll():");
      log.info("-------------------------------");
      repository.findAll().forEach(customer -> {
        log.info(customer.toString());
      });
      log.info("");

      // fetch an individual customer by ID
      Customer customer = repository.findById(1L);
      log.info("Customer found with findById(1L):");
      log.info("--------------------------------");
      log.info(customer.toString());
      log.info("");

      // fetch customers by last name
      log.info("Customer found with findByLastName('Bauer'):");
      log.info("--------------------------------------------");
      repository.findByLastName("Bauer").forEach(bauer -> {
        log.info(bauer.toString());
      });
      log.info("");
    };
  }

}