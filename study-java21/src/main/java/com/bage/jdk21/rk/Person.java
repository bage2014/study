package com.bage.jdk21.rk;

public record Person(String name, int age) {

    public static void main(String[] args) {
        Person person = new Person("张三", 18);
        System.out.println(person.age());
        System.out.println(person.name());
    }
}