package com.bage.jdk21.scopevalue.transaction;

public class OrderConfirmDomain1Service {

    public int transaction(String name){
        System.out.println((name == null ? this.getClass().getSimpleName(): name) + "-db transaction");
        return 1;
    }

    public void process() {
        System.out.println(this.getClass().getSimpleName() + "-process-");
    }

}
