package com.bage.study.spring.boot.neo4j;

import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

  Person findByName(String name);
  List<Person> findByTeammatesName(String name);
}