package com.bage.domain;

import java.util.List;

public class ClassAttribute {

    private String name;
    private String classOf;
    private List<FieldAttribute> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassOf() {
        return classOf;
    }

    public void setClassOf(String classOf) {
        this.classOf = classOf;
    }

    public List<FieldAttribute> getFields() {
        return fields;
    }

    public void setFields(List<FieldAttribute> fields) {
        this.fields = fields;
    }
}
