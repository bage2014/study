package com.bage.util;

public class Logger {

    public static void debug(String format,Object ...args){
        format = format.replace("{}","%s");
        System.out.println(String.format(format,args));
    }

}
