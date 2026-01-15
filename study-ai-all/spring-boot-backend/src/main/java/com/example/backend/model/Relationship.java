package com.example.backend.model;

public class Relationship {
    private Long id;
    private Long person1Id;
    private Long person2Id;
    private String type; // 如：父亲、母亲、儿子、女儿、妻子、丈夫、兄弟、姐妹等
    private String description;

    public Relationship() {
    }

    public Relationship(Long id, Long person1Id, Long person2Id, String type, String description) {
        this.id = id;
        this.person1Id = person1Id;
        this.person2Id = person2Id;
        this.type = type;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPerson1Id() {
        return person1Id;
    }

    public void setPerson1Id(Long person1Id) {
        this.person1Id = person1Id;
    }

    public Long getPerson2Id() {
        return person2Id;
    }

    public void setPerson2Id(Long person2Id) {
        this.person2Id = person2Id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}