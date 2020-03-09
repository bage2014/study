package com.bage;

import com.bage.domain.ComplexDomain;
import com.bage.domain.ComplexDomain2;
import org.junit.Test;

public class SearchMappingTest {

    @Test
    public void getInstance() {
        CodeGenerator codeGenerator = new CodeGenerator();
        System.out.println(codeGenerator.getCode(ComplexDomain.class, ComplexDomain2.class));
    }

}
