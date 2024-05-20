package com.bage.study.es.crud;

import com.bage.study.es.model.Person;

import java.util.List;

public interface EsCrudService {
    int insert(Person person);

    int update(Person person);

    int delete(Long id);

    List<Person> query(Long id);
}
