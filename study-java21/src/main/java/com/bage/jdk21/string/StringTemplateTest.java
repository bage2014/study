package com.bage.jdk21.string;

import java.io.File;
import java.util.List;

import static java.lang.StringTemplate.RAW;

public class StringTemplateTest {
    public static void main(String[] args) {
        int x = 10;
        int y = 20;
        StringTemplate st = RAW."\{x} + \{y} = \{x + y}";
        List<String> fragments = st.fragments();
        List<Object> values = st.values();

        System.out.println(fragments);
        System.out.println(values);
        System.out.println(st.interpolate());


        String name = "Joan";
        String info = STR."My name is \{name}";
        System.out.println(info);


        String filePath = "tmp.dat";
        File file = new File(filePath);
        String msg = STR. "The file \{ filePath } \{ file.exists() ? "does" : "does not" } exist" ;
        System.out.println(msg);

    }
}
