package com.bage.study.data.mock;

import cn.binarywang.tools.generator.ChineseAddressGenerator;

/**
 * https://github.com/binarywang/java-testdata-generator/blob/master/src/test/java/cn/binarywang/tools/generator/InsertSQLGeneratorTest.java
 *
 * 
 */
public class TestDataGeneratorMock {

    public static void main(String[] args) {
        String generatedAddress = ChineseAddressGenerator.getInstance()
                .generate();
        System.err.println(generatedAddress);
    }

}
