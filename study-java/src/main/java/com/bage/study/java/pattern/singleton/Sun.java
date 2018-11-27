package com.bage.study.java.pattern.singleton;

public class Sun {

	
}
class SingletonS {
    private static  SingletonS singleton; // 要加上 volatile 防止指令重排

    private SingletonS(){}

    public static SingletonS getInstance(){
        if(singleton == null){                              // 1
            synchronized (SingletonS.class){                 // 2
                if(singleton == null){                      // 3
                    singleton = new SingletonS();            // 4
                }
            }
        }
        return singleton;
    }
}
