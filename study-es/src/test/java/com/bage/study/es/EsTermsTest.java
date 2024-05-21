package com.bage.study.es;

import com.bage.study.es.terms.EsTermService;
import org.junit.Test;

import java.io.IOException;

public class EsTermsTest {

    private static EsTermService esService = new EsTermService();

    @Test
    public void main() throws IOException {
        esService.analyze("persons","hello world, bage ,hhhhh");

    }

}
