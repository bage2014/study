package com.bage.study.gc.biz.leak.memory;

/********************************************
 * Copyright (c) 2019 Kirk Pepperdine
 * All right reserved
 ********************************************/

import java.util.HashMap;

public class LeakingClass {

    private HashMap<String,String> alongForTheRide = new HashMap();
    private String alsoAlongForTheRide;
    private LeakingField leakingField;

    public LeakingClass(String parameter, LeakingField field) {
        alsoAlongForTheRide = parameter;
        leakingField = field;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(alongForTheRide.toString());
        buffer.append(alsoAlongForTheRide);
        buffer.append(leakingField);
        return buffer.toString();
    }
}