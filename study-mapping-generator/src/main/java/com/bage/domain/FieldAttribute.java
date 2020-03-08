package com.bage.domain;

import java.lang.reflect.Field;

public class FieldAttribute {

    private String name;
    private String classOf;
    private Class cls;
    private Field field;
    private ClassAttribute classAttribute;

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

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public ClassAttribute getClassAttribute() {
        return classAttribute;
    }

    public void setClassAttribute(ClassAttribute classAttribute) {
        this.classAttribute = classAttribute;
    }

}
