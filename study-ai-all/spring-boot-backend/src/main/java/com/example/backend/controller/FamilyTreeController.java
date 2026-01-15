package com.example.backend.controller;

import com.example.backend.model.Person;
import com.example.backend.model.Relationship;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/family-tree")
public class FamilyTreeController {
    private final Map<Long, Person> persons = new HashMap<>();
    private final Map<Long, Relationship> relationships = new HashMap<>();
    private final AtomicLong personIdCounter = new AtomicLong(1);
    private final AtomicLong relationshipIdCounter = new AtomicLong(1);

    // 初始化一些测试数据
    public FamilyTreeController() {
        // 添加一些人员
        Person person1 = new Person(1L, "张三", "男", LocalDate.of(1960, 1, 1), null, "家庭 patriarch");
        Person person2 = new Person(2L, "李四", "女", LocalDate.of(1962, 2, 2), null, "张三的妻子");
        Person person3 = new Person(3L, "张五", "男", LocalDate.of(1985, 3, 3), null, "张三和李四的儿子");
        Person person4 = new Person(4L, "张六", "女", LocalDate.of(1988, 4, 4), null, "张三和李四的女儿");
        Person person5 = new Person(5L, "王五", "男", LocalDate.of(1984, 5, 5), null, "张六的丈夫");

        persons.put(1L, person1);
        persons.put(2L, person2);
        persons.put(3L, person3);
        persons.put(4L, person4);
        persons.put(5L, person5);

        // 添加一些关系
        Relationship relationship1 = new Relationship(1L, 1L, 2L, "妻子", "张三的妻子");
        Relationship relationship2 = new Relationship(2L, 1L, 3L, "儿子", "张三的儿子");
        Relationship relationship3 = new Relationship(3L, 1L, 4L, "女儿", "张三的女儿");
        Relationship relationship4 = new Relationship(4L, 2L, 3L, "儿子", "李四的儿子");
        Relationship relationship5 = new Relationship(5L, 2L, 4L, "女儿", "李四的女儿");
        Relationship relationship6 = new Relationship(6L, 4L, 5L, "丈夫", "张六的丈夫");

        relationships.put(1L, relationship1);
        relationships.put(2L, relationship2);
        relationships.put(3L, relationship3);
        relationships.put(4L, relationship4);
        relationships.put(5L, relationship5);
        relationships.put(6L, relationship6);

        // 更新ID计数器
        personIdCounter.set(6);
        relationshipIdCounter.set(7);
    }

    // 人员管理API
    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return new ArrayList<>(persons.values());
    }

    @GetMapping("/persons/{id}")
    public Person getPersonById(@PathVariable Long id) {
        return persons.get(id);
    }

    @PostMapping("/persons")
    public Person addPerson(@RequestBody Person person) {
        long id = personIdCounter.incrementAndGet();
        person.setId(id);
        persons.put(id, person);
        return person;
    }

    @PutMapping("/persons/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person person) {
        if (!persons.containsKey(id)) {
            return null;
        }
        person.setId(id);
        persons.put(id, person);
        return person;
    }

    @DeleteMapping("/persons/{id}")
    public void deletePerson(@PathVariable Long id) {
        persons.remove(id);
        // 删除与该人员相关的所有关系
        relationships.values().removeIf(rel -> rel.getPerson1Id() == id || rel.getPerson2Id() == id);
    }

    // 关系管理API
    @GetMapping("/relationships")
    public List<Relationship> getAllRelationships() {
        return new ArrayList<>(relationships.values());
    }

    @GetMapping("/relationships/{id}")
    public Relationship getRelationshipById(@PathVariable Long id) {
        return relationships.get(id);
    }

    @PostMapping("/relationships")
    public Relationship addRelationship(@RequestBody Relationship relationship) {
        long id = relationshipIdCounter.incrementAndGet();
        relationship.setId(id);
        relationships.put(id, relationship);
        return relationship;
    }

    @PutMapping("/relationships/{id}")
    public Relationship updateRelationship(@PathVariable Long id, @RequestBody Relationship relationship) {
        if (!relationships.containsKey(id)) {
            return null;
        }
        relationship.setId(id);
        relationships.put(id, relationship);
        return relationship;
    }

    @DeleteMapping("/relationships/{id}")
    public void deleteRelationship(@PathVariable Long id) {
        relationships.remove(id);
    }

    // 获取指定人员的所有关系
    @GetMapping("/persons/{id}/relationships")
    public List<Relationship> getPersonRelationships(@PathVariable Long id) {
        List<Relationship> result = new ArrayList<>();
        for (Relationship rel : relationships.values()) {
            if (rel.getPerson1Id() == id || rel.getPerson2Id() == id) {
                result.add(rel);
            }
        }
        return result;
    }

    // 获取完整的关系图数据
    @GetMapping("/graph")
    public Map<String, Object> getRelationshipGraph() {
        Map<String, Object> graph = new HashMap<>();
        graph.put("nodes", new ArrayList<>(persons.values()));
        graph.put("edges", new ArrayList<>(relationships.values()));
        return graph;
    }
}
