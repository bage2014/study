package com.bage;

import com.bage.domain.ComplexDomain;
import com.bage.domain.ComplexDomain2;
import com.bage.domain.FieldAttribute;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MappingCodeGeneratorTest {

    @Test
    public void getInstance() {
        CodeGenerator codeGenerator = new CodeGenerator();
        System.out.println(codeGenerator.getCode(ComplexDomain.class, ComplexDomain2.class));
    }

    private void prints(Map<String, List<FieldAttribute>> map) {
        map.forEach((key, value) -> {
            System.out.print(key);
            System.out.print("-");
            System.out.print(Arrays.toString(value.stream().map(item -> item.getName()).collect(Collectors.toList()).toArray()));
            System.out.println();
        });

    }

    private void prints(List<List<FieldAttribute>> classArray) {
        for (List<FieldAttribute> classAttributes : classArray) {
            for (FieldAttribute classAttribute : classAttributes) {
                System.out.print(classAttribute.getClassOf() + ":" + classAttribute.getName() + "\t");
            }
            System.out.println();
        }
    }

}
