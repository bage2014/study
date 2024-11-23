package com.bage.jdk21.npe;

public class NpeTests {

    private String hhh;
    private NpeTests hello = new NpeTests();
    public static void main(String[] args) {
        NpeTests hhh = new NpeTests();
        int length = hhh.getHello().hhh.length();
        System.out.println(length);
    }

    public String getHhh() {
        return hhh;
    }

    public void setHhh(String hhh) {
        this.hhh = hhh;
    }

    public NpeTests getHello() {
        return hello;
    }

    public void setHello(NpeTests hello) {
        this.hello = hello;
    }
}
