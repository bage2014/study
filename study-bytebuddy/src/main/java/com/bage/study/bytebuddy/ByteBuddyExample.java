package com.bage.study.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ByteBuddyExample {
    public static void main(String[] args) {
        try {
            // Use ByteBuddy to create a new class
            Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class) // Inherit from the Object class
                .name("com.bage.study.bytebuddy.HelloWorld") // Define the class name
                .method(named("hello")) // Define the method to intercept
                .intercept(FixedValue.value("Hello, ByteBuddy!")) // Method returns a fixed value
                .make()
                .load(ByteBuddyExample.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

            // Create an instance of the class and call the toString method
            Object instance = dynamicType.getDeclaredConstructor().newInstance();
            System.out.println(instance.toString()); // Output: Hello, ByteBuddy!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}