package com.bage.util;

import java.util.List;

public class PrintUtils {

    public static void print(List list){
        if(list != null){
           for(Object item : list){
               System.out.println(item);
           }
        }
    }
    public static void print(Object[] objects){
        if(objects != null){
           for(Object item : objects){
               System.out.println(item);
           }
        }
    }

}
