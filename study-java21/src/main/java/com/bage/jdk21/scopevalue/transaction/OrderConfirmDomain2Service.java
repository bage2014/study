package com.bage.jdk21.scopevalue.transaction;

public class OrderConfirmDomain2Service {

    public int transaction(){
        System.out.println(this.getClass().getSimpleName() + "-db transaction");
        return 1;
    }

    public void process() {
        System.out.println(this.getClass().getSimpleName() + "-process-");
    }
}
