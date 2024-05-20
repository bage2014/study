package com.bage.study.es;

import com.bage.study.es.terms.EsTermService;

import java.io.IOException;

public class EsTermsTest {

    private static EsTermService esService = new EsTermService();

    public static void main(String[] args) throws IOException {
        esService.analyze("persons","hello world, bage ,hhhhh");

    }

}
