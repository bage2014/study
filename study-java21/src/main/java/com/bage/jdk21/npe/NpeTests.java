package com.bage.jdk21.npe;

public class NpeTests {

    public static void main(String[] args) {
        User user = new User();
        int length = user.getName().length();
        System.out.println(length);
    }


    private static class User {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
