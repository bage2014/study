package com.bage.study.java.attr;

public class SubClass extends SuperClass {
    public String attr = "SubClass.attr";
    public static String staticAttr = "SubClass.staticAttr";


    public String method(){
        return "SubClass.method";
    }

    public static String staticMethod(){
        return "SubClass.staticMethod";
    }
}
