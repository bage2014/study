package com.bage.jdk21.swt;

public class SwitchTest {
    public static void main(String[] args) {
        System.out.println(new SwitchTest().formatterPatternSwitch("22"));
        System.out.println(new SwitchTest().formatterPatternSwitch(22));
        System.out.println(new SwitchTest().formatterPatternSwitch(Double.parseDouble("13.23")));
    }

    /**
     * 1、 支持箭头
     * 2、 支持类型
     * @param obj
     * @return
     */
    public static String formatterPatternSwitch(Object obj) {
        return switch (obj) {
            case Integer i -> String.format("int %d", i);
            case Long l    -> String.format("long %d", l);
            case Double d  -> String.format("double %f", d);
            case String s  -> String.format("String %s", s);
            default        -> obj.toString();
        };
    }
}
