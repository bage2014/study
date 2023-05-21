package com.bage.study.data.mock;

import com.github.jsonzou.jmockdata.JMockData;

public class DataMock {
    public static void main(String[] args) {
        //调用模拟数据的方法模拟Java对象
        BasicBean basicBean = JMockData.mock(BasicBean.class);
        System.out.println(basicBean);
    }
}
