package com.bage.jdk21.scopevalue;

/**
 * https://openjdk.org/jeps/464
 */
public class ScopedValueTest {
    private static final ScopedValue<String> X = ScopedValue.newInstance();

    void foo() {
        ScopedValue.where(X, "hello").run(() -> bar());
    }

    void bar() {
        System.out.println(STR."bar = \{X.get()}"); // prints hello
        ScopedValue.where(X, "goodbye").run(() -> baz());
        System.out.println(STR."bar = \{X.get()}"); // prints hello
    }

    void baz() {
        System.out.println(STR."baz = \{X.get()}"); // prints goodbye
    }

    public static void main(String[] args) {
        new ScopedValueTest().foo();
//        new ScopedValueTest().bar();
//        new ScopedValueTest().baz();
    }
}
