package com.bage.study.gc.biz.leak.memory;

/********************************************
 * Copyright (c) 2019 Kirk Pepperdine
 * All right reserved
 ********************************************/

public class LeakingField {

    private String string = "more string stuff to leak";

    public LeakingField() {}

    public String toString() {
        return string;
    }
}