package com.bage.study.data.mock;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaFakerMock {

    public static void main(String[] args) {
        Faker faker = new Faker();

        String name = faker.name().fullName(); // Miss Samanta Schmidt
        String firstName = faker.name().firstName(); // Emory
        String lastName = faker.name().lastName(); // Barton

        String streetAddress = faker.address().streetAddress(); // 60018 Sawayn Brooks Suite 449

        System.out.println("name = " + name);
        System.out.println("firstName = " + firstName);
        System.out.println("lastName = " + lastName);
        System.out.println("streetAddress = " + streetAddress);


        Date date = faker.date().birthday();
        System.out.println(date);

        System.out.println(faker.demographic().sex());

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        String email = fakeValuesService.bothify("????##@gmail.com");
//        Matcher emailMatcher = Pattern.compile("\\w{4}\\d{2}@gmail.com").matcher(email);

        System.out.println(email);
    }

}
