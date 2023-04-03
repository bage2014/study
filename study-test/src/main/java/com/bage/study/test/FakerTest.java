package com.bage.study.test;

import com.github.javafaker.Faker;

public class FakerTest {

    public static void main(String[] args) {

        Faker faker = new Faker();

        String name = faker.name().fullName(); // Miss Samanta Schmidt
        String firstName = faker.name().firstName(); // Emory
        String lastName = faker.name().lastName(); // Barton

        String streetAddress = faker.address().streetAddress(); // 60018 Sawayn Brooks Suite 449

        System.out.println(faker.name());
        System.out.println("name:" +  name);
        System.out.println("firstName:" +  firstName);
        System.out.println("lastName:" +  lastName);
        System.out.println("streetAddress:" +  streetAddress);

    }

}
